package com.sxt.web.servlet;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import com.google.gson.Gson;
import com.sxt.entity.UserDB;
import com.sxt.service.UserService;
import com.sxt.utils.MD5;
import com.sxt.utils.PageTool;
import com.sxt.utils.PaginationUtils;
import com.sxt.utils.ResBean;
/**
 * 用户
 * @author 17279
 *
 */
@WebServlet("/user")
public class UserSevlet extends BaseServlet {

	private static final long serialVersionUID = 1L;
	
	private UserService userService = new UserService();
	
	/**
	 * 用户列表 分页
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	public void list(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		String currentPage = request.getParameter("pageNum");
		String pageSize = request.getParameter("pageSize");
		PageTool<UserDB> pageTool = userService.list(currentPage, pageSize,null);
		//生成前段分页按钮
		String pagation = PaginationUtils.getPagation(pageTool.getTotalCount(), pageTool.getCurrentPage(), pageTool.getPageSize(), "user?method=list");
		request.setAttribute("pagation", pagation);
		request.setAttribute("uList", pageTool.getRows());
		request.getRequestDispatcher("admin/admin_user.jsp").forward(request,response);
	}
	
	/**
	 * 添加用户
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void addUser(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException, IllegalAccessException, InvocationTargetException {
		/*
		String account = request.getParameter("account");
		String password = request.getParameter("password");
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String maxNum = request.getParameter("maxNum");
		String lendNum = request.getParameter("lendNum");
		String role = request.getParameter("role");
		UserDB userDB = new UserDB();
		userDB.setAccount(account);
		userDB.setPassword(password);
		userDB.setName(name);
		userDB.setPhone(phone);
		userDB.setLendNum(Integer.parseInt(lendNum));
		userDB.setMaxNum(Integer.parseInt(maxNum));
		userDB.setRole(Integer.parseInt(role));
		System.out.println(userDB);
		*/
		
		UserDB userDB = new UserDB();
		BeanUtils.populate(userDB, request.getParameterMap());
		userDB.setTimes(0);
		userDB.setPassword(MD5.valueOf(userDB.getPassword()));
		System.out.println(userDB);
		userService.addUser(userDB);
		response.sendRedirect("user?method=list");
	}
	
	/**
	 * 修改用户信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void updUser(HttpServletRequest request,HttpServletResponse response) throws Exception {
		UserDB userDB = new UserDB();
		BeanUtils.populate(userDB, request.getParameterMap());
		userService.updUser(userDB);
		response.sendRedirect("user?method=list");
	}
	/**
	 * 删除用户
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void delUser(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String uid = request.getParameter("uid");
		userService.delUser(Integer.parseInt(uid));
		response.sendRedirect("user?method=list");
	}
	
		
	/**
	 * 校验用户账号是否存在
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void checkUser(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String account = request.getParameter("account");
		UserDB userDB = new UserDB();
		userDB.setAccount(account);
		List<UserDB> list = userService.getList(userDB);
		ResBean resBean = new ResBean();
		if (list != null && list.size() > 0 ) {
			//request.setAttribute("msg", "账号已存在");
			resBean.setCode(400);
			resBean.setMsg("账号已存在");
		}else {
			resBean.setCode(200);
			resBean.setMsg("账号可以使用");
		}
		//将resBean转换成json字符串
		Gson gson =new Gson();
		String json = gson.toJson(resBean);
		response.getWriter().print(json);
	}	
	/**
	 * 读者排行
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void rank(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		UserDB userDB = (UserDB) request.getSession().getAttribute("userDB");
		//根据当前登录用户获取角色
		Integer role = userDB.getRole();
		String currentPage = request.getParameter("pageNum");
		String pageSize = request.getParameter("pageSize");
		PageTool<UserDB> pageTool = userService.list(currentPage, pageSize,1);
		//生成前段分页按钮
		String pagation = PaginationUtils.getPagation(pageTool.getTotalCount(), pageTool.getCurrentPage(), pageTool.getPageSize(), "user?method=rank");
		request.setAttribute("pagation", pagation);
		request.setAttribute("start", pageTool.getStartIndex());
		request.setAttribute("uList", pageTool.getRows());
		//根据role判断跳转页面
		if(role == 1 ) {
			//普通用户
			request.getRequestDispatcher("user/brtimes.jsp").forward(request,response);
		}else {
			//管理员
			request.getRequestDispatcher("admin/admin_brtimes.jsp").forward(request,response);
		}
	}
}
