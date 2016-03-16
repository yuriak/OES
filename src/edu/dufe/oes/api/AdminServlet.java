package edu.dufe.oes.api;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.dufe.oes.bean.Administrator;
import edu.dufe.oes.service.AdministratorService;

public class AdminServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public AdminServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		response.setContentType("text/html");
//		request.setCharacterEncoding("utf-8");
//		response.setCharacterEncoding("utf-8");
//		HttpSession session = request.getSession();
//		PrintWriter out = response.getWriter();
//		if(request.getParameter("userName")!=null&&request.getParameter("password")!=null){
//			if(request.getParameter("userName").length()>0&&request.getParameter("password").length()>0){
//				String username=request.getParameter("userName");
//				String password=request.getParameter("password");
//				AdministratorService administratorService=new AdministratorService();
//				Administrator administrator=administratorService.administratorLogin(username, password);
//				if(administrator==null){
//					response.sendRedirect("../adminLogin.jsp");
//				}else{
//					session.setAttribute("token", administrator.getToken());
//					response.sendRedirect("../adminIndex.jsp");
//				}
//			}
//		}
		
		
	}

	private void adminLogin(HttpServletRequest request,HttpServletResponse response){
		
	}
	
	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
