package board.action;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.model.BoardDAO;
import board.model.BoardVO;

public class ListAction implements CommandAction {
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable{
	
	String pageNum = request.getParameter("pageNum");
	if(pageNum==null){
		pageNum = "1";
	}
	String find = null;
	String find_box = null;		
	find = request.getParameter("find");
	find_box = request.getParameter("find_box"); 
	if(find==null){
		find="no";
	}
	if(find_box ==null){
		find_box="no";
	}
	int pageSize = 3;
	int currentPage = Integer.parseInt(pageNum);
	int startRow = (currentPage - 1) * pageSize + 1;
	int endRow = currentPage * pageSize;
	int count = 0;
	int number = 0;
	int imsi = 0;
    int pageCount = 0;
    int pageBlock = 0;
    int startPage = 0;
    int endPage = 0;
	List<BoardVO> articleList = null;
	
	BoardDAO dbPro = BoardDAO.getInstance();
	count = dbPro.getArticleCount(find,find_box);	
	if(count>0){
		 imsi = count%pageSize == 0?0:1;
         pageCount = count/pageSize + imsi;
         pageBlock = 3;
         startPage = (int)((currentPage-1)/pageBlock)*pageBlock + 1; 
         endPage = startPage + pageBlock-1;    
         if(endPage>pageCount) endPage = pageCount;
         articleList = dbPro.getArticles(find, find_box, startRow, endRow);
        
	}else{
	articleList = Collections.emptyList();
	}
	number=count-(currentPage-1)*pageSize;
	 request.setAttribute("imsi", new Integer(imsi));
     request.setAttribute("pageCount", new Integer(pageCount));
     request.setAttribute("pageBlock", new Integer(pageBlock));
     request.setAttribute("startPage", new Integer(startPage));
     request.setAttribute("endPage", new Integer(endPage));	
	request.setAttribute("currentPage",new Integer(currentPage));
	request.setAttribute("startRow",new Integer(startRow));
	request.setAttribute("endRow",new Integer(endRow));
	request.setAttribute("count",new Integer(count));
	request.setAttribute("pageSize",new Integer(pageSize));
	request.setAttribute("number",new Integer(number));
	request.setAttribute("find",new String(find));
	request.setAttribute("find_box", new String(find_box));
	request.setAttribute("articleList",articleList);
	return"/board/list.jsp";
	}
}
