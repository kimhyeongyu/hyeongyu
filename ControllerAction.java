package board.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.servlet4preview.RequestDispatcher;

import board.action.CommandAction;

public class ControllerAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Map<String, Object> commandMap = new HashMap<String, Object>();

	@SuppressWarnings("unchecked")
	public void init(ServletConfig config) throws ServletException {
		
		String props = config.getInitParameter("propertyConfig");
		
		Properties pr = new Properties();
		FileInputStream f = null;
		String path = config.getServletContext().getRealPath("/WEB-INF");
		try {
			f = new FileInputStream(new File(path, props));
			pr.load(f);
		} catch (IOException e) {
			throw new ServletException(e);
		} finally {
			if (f != null)
				try {
					f.close();
				} catch (IOException e) {
				}
		}
		Iterator<Object> keyIter = pr.keySet().iterator();

		while (keyIter.hasNext()) {
			String command = (String) keyIter.next();
			String className = pr.getProperty(command);
			try {
				Class commandClass = Class.forName(className);
				Object commandInstance = commandClass.newInstance();
				commandMap.put(command, commandInstance);
			} catch (ClassNotFoundException e) {
				throw new ServletException(e);
			} catch (InstantiationException e) {
				throw new ServletException(e);
				
			} catch (IllegalAccessException e) {
				throw new ServletException(e);
			}
		}
	}

	public ControllerAction() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		requestPro(request,response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		requestPro(request,response);
	}
	private void requestPro(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String view = null;
		CommandAction com =null;
		
		try {
			String command = request.getRequestURI();
			if(command.indexOf(request.getContextPath())==0) {
				command = command.substring(request.getContextPath().length());
			}
			com= (CommandAction)commandMap.get(command);
			view = com.requestPro(request,response);
		}catch(Throwable e) {
			throw new ServletException(e);
		}
		javax.servlet.RequestDispatcher dispatcher = request.getRequestDispatcher(view);
		dispatcher.forward(request, response);
}
}
