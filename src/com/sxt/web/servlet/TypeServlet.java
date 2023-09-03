package com.sxt.web.servlet;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.sxt.entity.TypeDB;
import com.sxt.service.TypeService;
import com.sxt.utils.PageTool;
import com.sxt.utils.PaginationUtils;
import com.sxt.utils.ResBean;
/**
 * 图书分类
 * @author 17279
 *
 */
@WebServlet("/type")
public class TypeServlet extends BaseServlet{
	
	private static final long serialVersionUID = 1L;
	
	private TypeService typeService = new TypeService();

	/**
	 * 列表 分页
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	public void listByPage(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		String currentPage = request.getParameter("pageNum");
		String pageSize = request.getParameter("pageSize");
		PageTool<TypeDB> pageTool = typeService.listByPage(currentPage, pageSize);
		//生成前段分页按钮
		String pagation = PaginationUtils.getPagation(pageTool.getTotalCount(), pageTool.getCurrentPage(), pageTool.getPageSize(), "type?method=listByPage");
		request.setAttribute("pagation", pagation);
		request.setAttribute("tList", pageTool.getRows());
		request.getRequestDispatcher("admin/admin_booktype.jsp").forward(request,response);
	}
	
	
	/**
	 * 类别名称校验
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void checkType(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String typeName = request.getParameter("typeName");
		/*String tid = request.getParameter("tid");*/
		List<TypeDB> list = typeService.list(null, typeName);
		ResBean resBean = new ResBean();
		if (list != null && list.size() > 0 ) {
			//request.setAttribute("msg", "账号已存在");
			resBean.setCode(400);
			resBean.setMsg("类别已存在");
		}else {
			resBean.setCode(200);
			resBean.setMsg("类别可以使用");
		}
		//将resBean转换成json字符串
		Gson gson =new Gson();
		String json = gson.toJson(resBean);
		response.getWriter().print(json);
	}
	/**
	 * 类型添加
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void addType(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		String typeName = request.getParameter("typeName");
		typeService.addType(typeName);
		request.getRequestDispatcher("type?method=listByPage").forward(request,response);
	}
	
	/**
	 * 类型修改
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void updType(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		String typeName = request.getParameter("typeName");
		String tid = request.getParameter("tid");
		TypeDB typeDB = new TypeDB();
		typeDB.setTid(Integer.parseInt(tid));
		typeDB.setTypeName(typeName);
		typeService.updType(typeDB);
		request.getRequestDispatcher("type?method=listByPage").forward(request,response);
	}
	
	/**
	 * 类型删除
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void delType(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		String tid = request.getParameter("tid");
		typeService.delType(Integer.parseInt(tid));
		request.getRequestDispatcher("type?method=listByPage").forward(request,response);
	}
}
