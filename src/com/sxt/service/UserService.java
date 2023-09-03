package com.sxt.service;

import java.util.List;

import com.sxt.dao.UserDao;
import com.sxt.entity.UserDB;
import com.sxt.utils.PageTool;

/**
 * 用户业务层
 * @author 17279
 *
 */
public class UserService {

	private UserDao userDao = new UserDao();
	/**
	 * 登录
	 * @param account
	 * @param password
	 * @return
	 */
	public UserDB login(String account,String password) {
		return userDao.login(account,password);
	}
	
	/**
	 * 用户添加
	 * @param userDB
	 * @return
	 */
	public Integer addUser(UserDB userDB) {
		return userDao.addUser(userDB);
	}
	
	public PageTool<UserDB> list(String currentPage,String pageSize,Integer order){
		return userDao.list(currentPage, pageSize,order);
	}
	
	public List<UserDB> getList(UserDB userDB){
		return userDao.getList(userDB);
	}
	/**
	 * 修改用户信息
	 * @param userDB
	 * @return
	 */
	public Integer updUser(UserDB userDB) {
		return userDao.updUser(userDB);	
	}
	/**
	 * 删除用户
	 * @param uid
	 * @return
	 */
	public Integer delUser(Integer uid) {
		return userDao.delUser(uid);
	}
	
}
