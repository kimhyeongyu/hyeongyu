package board.action;

import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import board.comment.CommentVo;
import board.model.BoardDAO;

public class CommentReadAction implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
			response.setCharacterEncoding("utf-8");       
	        
	        int commPageNum = Integer.parseInt(request.getParameter("commPageNum"));
	        int articleNumber =Integer.parseInt(request.getParameter("num"));
	        ArrayList<CommentVo> comments = null;
	        CommentVo vo = new CommentVo();
	        
	        
	        try {
	            comments = BoardDAO.getInstance().selectComments(articleNumber, commPageNum);
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        /*request.setAttribute("c", comments);
	        for(int i=0;i<comments.size();i++) {
	        vo.setCommentContent(comments.get(i).getCommentContent());
	        }*/
	        
	        JSONArray jsonArr = new JSONArray(comments);        // 스프링에선 애노테이션(?)
	        PrintWriter pw = response.getWriter();
	        pw.println(jsonArr);
	        
	        /*for( CommentVo a : comments) {
	        	System.out.println(a);
	        }*/
	        
	        
	        return "/board/content.jsp?comments";
	    }


	

}
