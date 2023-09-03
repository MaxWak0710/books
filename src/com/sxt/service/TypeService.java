package com.sxt.service;

import java.util.List;

import com.sxt.dao.TypeDao;
import com.sxt.entity.TypeDB;
import com.sxt.utils.PageTool;

/**
 * 图书类别
 * @author 17279
 *
 */
public class TypeService {
	
	private TypeDao typeDao = new TypeDao();
	
	public PageTool<TypeDB> listByPage(String currentPage,String pageSize){
		return typeDao.listByPage(currentPage, pageSize);
	}
	
	public List<TypeDB>list(String tid ,String typeName ) {
		return typeDao.list(tid, typeName);
	}
	
	public Integer addType(String typeName) {
		return typeDao.addType(typeName);
	}
	
	public Integer updType(TypeDB typeDB) {
		return typeDao.updType(typeDB);
	}
	
	public int delType(Integer tid) {
		return typeDao.delType(tid);
	}

}
