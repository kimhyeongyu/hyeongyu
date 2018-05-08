package board.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.model.BoardDAO;
import board.model.BoardVO;

public class ContentAction implements CommandAction{

	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable{
		int num = Integer.parseInt(request.getParameter("num"));
		String pageNum = request.getParameter("pageNum");
		
		BoardDAO dao = BoardDAO.getInstance();
		BoardVO article = dao.getArticle(num);
		
		request.setAttribute("num", new Integer(num));
		request.setAttribute("pageNum", new Integer(pageNum));
		request.setAttribute("article", article);
		
		return "/board/content.jsp";
	}
}
