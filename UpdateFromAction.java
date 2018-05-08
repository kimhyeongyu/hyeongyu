package board.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.model.*;

public class UpdateFromAction implements CommandAction{
	
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable{
		int num = Integer.parseInt(request.getParameter("num"));
		String pageNum = request.getParameter("pageNum");
		BoardDAO dao = BoardDAO.getInstance();
		BoardVO vo = dao.updateGetArticel(num);
		request.setAttribute("pageNum", pageNum);
		request.setAttribute("article", vo);
		return "/board/updateForm.jsp";
		
	}

}
