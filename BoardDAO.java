package board.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import board.comment.CommentVo;

public class BoardDAO {
	StringBuffer query ;
	CommentVo comment;
	 ArrayList<CommentVo> comments;

	 
	private BoardDAO() {

	}

	private static BoardDAO instance = new BoardDAO();

	public static BoardDAO getInstance() {

		return instance;
	}

	private Connection getConnection() {
		Connection conn = null;
		try {
			Context init = new InitialContext();
			DataSource ds = (DataSource) init.lookup("java:comp/env/jdbc/myOracle");
			conn = ds.getConnection();
		} catch (Exception e) {
			System.err.println("Connection 생성실패");
		}
		return conn;
	}

	public int insertArticle(BoardVO article) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int x = 0;
		int number = 0;
		String sql = "";

		int num = article.getNum();
		int ref = article.getRef();
		int step = article.getStep();
		int depth = article.getDepth();
		System.out.println(num + "+" + ref + "+" + step + "+" + depth + "넘어온넘버ref스텝deptㅗ");
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement("select max(num) from board");
			rs = pstmt.executeQuery();

			if (rs.next()) {
				number = rs.getInt(1) + 1;
			} else {
				number = 1;
			}
			if (num != 0) {
				sql = "update board set step=step+1 where ref=? and step>?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, ref);
				pstmt.setInt(2, step);
				pstmt.executeUpdate();
				step = step + 1;
				depth = depth + 1;

			}

			else {

				ref = number;
				step = 0;
				depth = 0;
			}
			sql = "insert into board(num,writer,subject,content,pass,regdate,ip,ref,step,depth,email)values(BOARD_NUM.NEXTVAL,?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, article.getWriter());
			pstmt.setString(2, article.getSubject());
			pstmt.setString(3, article.getContent());
			pstmt.setString(4, article.getPass());
			pstmt.setTimestamp(5, article.getRegdate());
			pstmt.setString(6, article.getIp());
			pstmt.setInt(7, ref);
			pstmt.setInt(8, step);
			pstmt.setInt(9, depth);
			pstmt.setString(10, article.getEmail());
			int a = pstmt.executeUpdate();
			x = 1;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException sqle1) {
				}
			;
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException sqle1) {
				}
			;
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException sqle1) {
				}
			;
		}

		return x;
	}

	public int getArticleCount(String find, String find_box) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int x = 0;

		try {
			conn = getConnection();
			if(find.equals("writer")){
				pstmt = conn.prepareStatement("select count(*) from board where writer=?");
				pstmt.setString(1, find_box);
			}else if(find.equals("subject")){
				pstmt = conn.prepareStatement("select count(*) from board where subject like '%" + find_box + "%'");
			}else if(find.equals("content")){
				pstmt = conn.prepareStatement("select count(*) from board where content like '%" + find_box + "%'");
			}else{
				pstmt = conn.prepareStatement("select count(*) from board");//전체 글의 수
			}
			
			rs = pstmt.executeQuery();

			if (rs.next())
				x = rs.getInt(1);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
				}
		}
		return x;
	}

	public List<BoardVO> getArticles(String find, String find_box,int start, int end) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardVO> articleList = null;// 글목록을 저장하는 객체
		try {
			conn = getConnection();
			StringBuffer sql = new StringBuffer();
			sql.append("select * from ");
			sql.append("(select rownum rnum, num, writer, email, subject, pass, regdate, readcount, ref, step, depth, content, ip,de from ");
			if(find.equals("writer")){
				sql.append("(select * from board where writer=? order by ref desc, step asc)) where rnum>=? and rnum<=?");
				pstmt = conn.prepareStatement(sql.toString());
				pstmt.setString(1, find_box);
				pstmt.setInt(2,start);
				pstmt.setInt(3,end);
			}else if(find.equals("subject")){
				sql.append("(select * from board where subject like '%" + find_box + "%' order by ref desc, step asc)) where rnum>=? and rnum<=?");
				pstmt = conn.prepareStatement(sql.toString());
				pstmt.setInt(1,start);
				pstmt.setInt(2,end);
			}else if(find.equals("content")){
				sql.append("(select * from board where content like '%" + find_box + "%' order by ref desc, step asc)) where rnum>=? and rnum<=?");
				pstmt = conn.prepareStatement(sql.toString());
				pstmt.setInt(1,start);
				pstmt.setInt(2,end);
			}else{
				sql.append("(select * from board order by ref desc, step asc)) where rnum>=? and rnum<=?");
				pstmt = conn.prepareStatement(sql.toString());
				pstmt.setInt(1,start);
				pstmt.setInt(2,end);
			}
			rs = pstmt.executeQuery();

			if (rs.next()) {// ResultSet이 레코드를 가짐
				articleList = new ArrayList<BoardVO>();
				do {
					BoardVO article = new BoardVO();
					article.setNum(rs.getInt("num"));
					article.setWriter(rs.getString("writer"));
					article.setSubject(rs.getString("subject"));
					article.setContent(rs.getString("content"));
					article.setPass(rs.getString("pass"));
					article.setRegdate(rs.getTimestamp("regdate"));
					article.setReadcount(rs.getInt("readcount"));
					article.setRef(rs.getInt("ref"));
					article.setStep(rs.getInt("step"));
					article.setDepth(rs.getInt("depth"));
					article.setContent(rs.getString("content"));
					article.setIp(rs.getString("ip"));
					article.setDe(rs.getInt("de"));
					// List객체에 데이터저장빈인 BoardDataBean객체를 저장
					articleList.add(article);
					System.out.println(articleList.size());
				} while (rs.next());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
				}
		}
		return articleList;// List객체의 레퍼런스를 리턴
	}

	public BoardVO getArticle(int num) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardVO article = null;
		query = new StringBuffer();
	    query.append("SELECT * FROM bbs WHERE article_number = ?");


	
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement("update board set readcount=readcount+1 where num =?");
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
			pstmt = conn.prepareStatement("select * from board where num =?");
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				article = new BoardVO();
				article.setNum(rs.getInt("num"));
				article.setWriter(rs.getString("writer"));
				article.setSubject(rs.getString("subject"));
				article.setPass(rs.getString("pass"));
				article.setRegdate(rs.getTimestamp("regdate"));
				article.setEmail(rs.getString("email"));
				article.setReadcount(rs.getInt("readcount"));
				article.setRef(rs.getInt("ref"));
				article.setStep(rs.getInt("step"));
				article.setDepth(rs.getInt("depth"));
				article.setContent(rs.getString("content"));
				article.setIp(rs.getString("ip"));
				article.setDe(rs.getInt("de"));
			}
		/*	 query = new StringBuffer();
			    query.append("SELECT count(*) FROM comments WHERE article_number = ?");
			    pstmt = conn.prepareStatement(query.toString());
			    pstmt.setInt(1, num);
			    rs = pstmt.executeQuery();
			    if(rs.next()) {
			        article.setCommentCount(rs.getLong(1));
			    }*/


			

			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException sqle1) {
				}
			;
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException sqle1) {
				}
			;
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException sqle1) {
				}

		}
		System.out.println("여기까진온다!");
		return article;
	}

	public BoardVO updateGetArticel(int num) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardVO article = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement("select * from board where num =?");
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				article = new BoardVO();
				article.setNum(rs.getInt("num"));
				article.setWriter(rs.getString("writer"));
				article.setSubject(rs.getString("subject"));
				article.setEmail(rs.getString("email"));
				article.setPass(rs.getString("pass"));
				article.setRegdate(rs.getTimestamp("regdate"));
				article.setReadcount(rs.getInt("readcount"));
				article.setRef(rs.getInt("ref"));
				article.setStep(rs.getInt("step"));
				article.setDepth(rs.getInt("depth"));
				article.setContent(rs.getString("content"));
				article.setIp(rs.getString("ip"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException sqle1) {
				}
			;
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException sqle1) {
				}
			;
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException sqle1) {
				}

		}
		return article;
	}

	public int updateArticle(BoardVO article) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String dbpass="";
		String sql= "";
		int result =-1;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement("select pass from board where num =?");
			pstmt.setInt(1, article.getNum());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				dbpass = rs.getString("pass");
				if(dbpass.equals(article.getPass())) {
					sql = "update board set writer =?,email=?,subject=?,content=? where num=?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, article.getWriter());
					pstmt.setString(2, article.getEmail());
					pstmt.setString(3, article.getSubject());
					pstmt.setString(4, article.getContent());
					pstmt.setInt(5, article.getNum());
					pstmt.executeUpdate();
					result = 1;
				}else {
					result = 0;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException sqle1) {
				}
			;
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException sqle1) {
				}
			;
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException sqle1) {
				}
	}
		System.out.println(result);
		return result;
}
	public int deleteArticle(int num,String pass) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String dbpass= "";
		int result = -1;
		try {
			conn=getConnection();
			pstmt= conn.prepareStatement("select pass from board where num= ?");
			pstmt.setInt(1, num);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				dbpass=rs.getString("pass");
				if(dbpass.equals(pass)) {
					pstmt=conn.prepareStatement("update board set de = 1 where num=?");
					pstmt.setInt(1, num);
					pstmt.executeUpdate();
					result =1;
				}else {
					result= 0;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException sqle1) {
				}
			;
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException sqle1) {
				}
			;
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException sqle1) {
				}
	}
		return result;
	}
	public ArrayList<CommentVo> selectComments(int articleNumber, int commPageSize) throws ClassNotFoundException, SQLException {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
		conn = getConnection();
		
	    query = new StringBuffer();
	    query.append("SELECT * ");
	    query.append("  FROM (SELECT comment_content, comment_date, article_number ");
	    query.append("               FROM comments ");
	    query.append("             WHERE article_number = ? ");
	    query.append("             ORDER BY comment_number DESC");
	    query.append("           ) comments ");
	    query.append(" WHERE rownum BETWEEN 1 AND ?");
	    pstmt = conn.prepareStatement(query.toString());
	    pstmt.setInt(1, articleNumber);
	    pstmt.setInt(2, commPageSize);
	    System.out.println(commPageSize+"이게마지막싸이즈이이이이");
	    rs = pstmt.executeQuery();
	    
	    comments = new ArrayList<>();
	    
	    while(rs.next()) {
	        comment = new CommentVo();
	      
	        comment.setCommentContent(rs.getString("comment_content"));
	        comment.setCommentDate(rs.getString("comment_date"));
	        comment.setArticleNumber(rs.getInt("article_number"));
	        comments.add(comment);
	    }
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException sqle1) {
				}
			;
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException sqle1) {
				}
			;
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException sqle1) {
				}
		}
	    
	    
	    return comments;
	}
	 
public synchronized HashMap<String, Object> insertComment( String commentContent, int articleNumber) throws ClassNotFoundException, SQLException {
	Connection conn = null;
	PreparedStatement pstmt = null;
	HashMap<String, Object> hm = new HashMap<>();
	try {
	conn = getConnection();
	System.out.println(commentContent);
    pstmt = conn.prepareStatement("INSERT INTO comments VALUES(comment_seq.nextval,?, sysdate, ?)");
    
    pstmt.setString(1, commentContent);
    pstmt.setInt(2, articleNumber);
    int result = pstmt.executeUpdate();
    ArrayList<CommentVo> comments = selectComments(articleNumber, 10);
    
    
    hm.put("result", result);
    hm.put("comments", comments);
	} finally {
		if (pstmt != null)
			try {
				pstmt.close();
			} catch (SQLException sqle1) {
			}
		;
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException sqle1) {
			}
	}
    
    
    return hm;
}



	


	
	
	
}