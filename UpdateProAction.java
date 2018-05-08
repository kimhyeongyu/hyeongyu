package board.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.model.BoardDAO;
import board.model.BoardVO;

public class UpdateProAction implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		System.out.println("여기까지오나?");
		int num = Integer.parseInt(request.getParameter("num"));
		System.out.println(num); 
		request.setCharacterEncoding("utf-8");
		String pageNum = request.getParameter("pageNum");
		BoardVO article = new BoardVO();
		article.setNum(Integer.parseInt(request.getParameter("num")));
		article.setWriter(request.getParameter("writer"));
		article.setEmail(request.getParameter("email"));
		article.setSubject(request.getParameter("subject"));
		article.setContent(request.getParameter("content"));
		article.setPass(request.getParameter("pass"));
		BoardDAO dao = BoardDAO.getInstance();
		int check = dao.updateArticle(article);
		request.setAttribute("pageNum", pageNum);
		request.setAttribute("check", check);
		
		System.out.println("여기까니는오려나?");
		return "/board/updatePro.jsp";
	}

}
