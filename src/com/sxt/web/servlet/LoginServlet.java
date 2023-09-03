package com.sxt.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sxt.entity.UserDB;
import com.sxt.service.UserService;
import com.sxt.utils.MD5;

@WebServlet("/login")
public class LoginServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;
	
	private UserService  userService = new UserService();

	/**
	 * 用户登录
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void login(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		String account = request.getParameter("account");
		String password = request.getParameter("password");
		HttpSession session = request.getSession();
		UserDB userDB = userService.login(account, MD5.valueOf(password));
		if (userDB == null) {
			//账号密码错误
			request.setAttribute("msg", "账号密码错误");
			request.getRequestDispatcher("login.jsp").forward(request,response);
		}else {
			//登录成功
			session.setAttribute("userDB", userDB);
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}
	}
	
}
