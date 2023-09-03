package com.sxt.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import com.google.gson.Gson;
import com.sxt.entity.BookDB;
import com.sxt.entity.TypeDB;
import com.sxt.entity.UserDB;
import com.sxt.service.BookService;
import com.sxt.service.TypeService;
import com.sxt.utils.PageTool;
import com.sxt.utils.PaginationUtils;
import com.sxt.utils.ResBean;

@WebServlet("/book")
public class BookServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;
	
	private BookService bookService = new BookService();
	
	private TypeService typeService = new TypeService();
	
	/**
	 * 图书列表 分页
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	public void listByPage(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		UserDB userDB = (UserDB) request.getSession().getAttribute("userDB");
		//根据当前登录用户获取角色
		Integer role = userDB.getRole();
		String word = request.getParameter("word");
		String currentPage = request.getParameter("pageNum");
		String pageSize = request.getParameter("pageSize");
		PageTool<BookDB> pageTool = bookService.listByPage(currentPage, pageSize,word,null);
		String path = "book?method=listByPage";
		if (word != null && word != "") {
			path += "&word=" + word;
		}
		//生成前段分页按钮
		String pagation = PaginationUtils.getPagation(pageTool.getTotalCount(), pageTool.getCurrentPage(), pageTool.getPageSize(), path);
		List<TypeDB> typeList = typeService.list(null,null);
		request.setAttribute("pagation", pagation);
		request.setAttribute("typeList", typeList);
		request.setAttribute("word", word);
		request.setAttribute("bList", pageTool.getRows());
		//根据role判断跳转页面
		if(role == 1 ) {
			//普通用户
			request.getRequestDispatcher("user/select.jsp").forward(request,response);
		}else {
			//管理员
			request.getRequestDispatcher("admin/admin_book.jsp").forward(request,response);
		}
			
	}
	
	/**
	 * 图书名称校验
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void checkBook(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String bookName = request.getParameter("bookName");
		List<BookDB> list = bookService.list(bookName);
		ResBean resBean = new ResBean();
		if (list != null && list.size() > 0 ) {
			//request.setAttribute("msg", "账号已存在");
			resBean.setCode(400);
			resBean.setMsg("图书名称已存在");
		}else {
			resBean.setCode(200);
			resBean.setMsg("图书名称可以使用");
		}
		//将resBean转换成json字符串
		Gson gson =new Gson();
		String json = gson.toJson(resBean);
		response.getWriter().print(json);
	}
	
	/**
	 * 添加图书
	 * @param request
	 * @param response
	 */
	public void addBook(HttpServletRequest request,HttpServletResponse response) throws Exception {
		BookDB bookDB = new BookDB();
		BeanUtils.populate(bookDB, request.getParameterMap());
		bookService.addBook(bookDB);
		response.sendRedirect("book?method=listByPage");
	}
	
	/**
	 * 修改图书信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void updBook(HttpServletRequest request,HttpServletResponse response) throws Exception {
		BookDB bookDB = new BookDB();
		BeanUtils.populate(bookDB, request.getParameterMap());
		bookService.updBook(bookDB);
		response.sendRedirect("book?method=listByPage");
	}
	
	/**
	 * 删除图书
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void delBook(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String bid = request.getParameter("bid");
		bookService.delBook(bid);
		response.sendRedirect("book?method=listByPage");
	}
	/**
	 *用户借阅图书
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void borrowBook(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//获取要借阅的图书ID
		String bid = request.getParameter("bid");
		//获取当前用户信息
		UserDB userDB = (UserDB) request.getSession().getAttribute("userDB");
		bookService.borrowBook(userDB,bid);
		response.sendRedirect("history?method=list");
	}
	/**
	 * 图书归还
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void backBook(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//获取借阅记录的图书ID
		String hid = request.getParameter("hid");
		bookService.backBook(hid);
		response.sendRedirect("history?method=backList");
	}
	/**
	 * 热门图书推荐
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
		PageTool<BookDB> pageTool = bookService.listByPage(currentPage, pageSize,null,1);
		String path = "book?method=rank";
		//生成前段分页按钮
		String pagation = PaginationUtils.getPagation(pageTool.getTotalCount(), pageTool.getCurrentPage(), pageTool.getPageSize(), path);
		request.setAttribute("pagation", pagation);
		request.setAttribute("start", pageTool.getStartIndex());
		request.setAttribute("bList", pageTool.getRows());
		//根据role判断跳转页面
		if(role == 1 ) {
			//普通用户
			request.getRequestDispatcher("user/bdtimes.jsp").forward(request,response);
		}else {
			//管理员
			request.getRequestDispatcher("admin/admin_bdtimes.jsp").forward(request,response);
		}
			
	}

}
