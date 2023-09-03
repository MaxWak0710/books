package com.sxt.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.sxt.dao.BookDao;
import com.sxt.dao.HistoryDao;
import com.sxt.dao.UserDao;
import com.sxt.entity.BookDB;
import com.sxt.entity.HistoryDB;
import com.sxt.entity.UserDB;
import com.sxt.utils.C3p0Tool;
import com.sxt.utils.DateUtils;
import com.sxt.utils.MyException;
import com.sxt.utils.PageTool;

public class BookService {
	
	private  BookDao bookDao = new BookDao();
	
	private HistoryDao historyDao = new HistoryDao();
	
	private UserDao userDao = new UserDao();
	
	/**
	 * 图书分页查询
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageTool<BookDB> listByPage(String currentPage,String pageSize,String word,Integer order){
		return bookDao.list(currentPage, pageSize,word,order);
	}
	
	public List<BookDB> list(String bookName){
		return bookDao.list(bookName,null);
	}
	
	public Integer addBook(BookDB bookDB) {
		return bookDao.addBook(bookDB);
	}
	
	public Integer updBook(BookDB bookDB) {
		return bookDao.updBook(bookDB);
	}
	
	public int delBook(String bid) {
		return bookDao.delBook(bid);
	}

	
	/**
	 * 用户借阅图书
	 * @param userDB
	 * @param bid
	 * @throws SQLException 
	 */
	public void borrowBook(UserDB userDB, String bid) {
		//获取数据库连接
		Connection conn = C3p0Tool.getConnection();
		try {
			//设置事务不自动提交
			conn.setAutoCommit(false);
			//根据bid获取图书信息
			List<BookDB> list = bookDao.list(null, bid);
			BookDB bookDB = list.get(0);
			//保证用户数据与数据库数据同步
			userDB = userDao.getList(userDB).get(0);
			
			//t_history 创建图书借阅历史记录
			HistoryDB historyDB = new HistoryDB();
			historyDB.setUid(userDB.getUid());
			historyDB.setName(userDB.getName());
			historyDB.setAccount(userDB.getAccount());
			historyDB.setBid(bookDB.getBid());
			historyDB.setBookName(bookDB.getBookName());
			LocalDateTime now = LocalDateTime.now();
			historyDB.setBeginTime(now);
			historyDB.setEndTime(now.plusDays(userDB.getLendNum()));//还书时间等于借书时间+最大可借阅时间
			historyDB.setStatus(1);
			historyDao.addHistory(historyDB,conn);
			
			//t_book 改变图书的库存  book_num--  book_time++
			bookDB.setTimes(bookDB.getTimes() + 1 );
			bookDao.changeNum(bookDB,conn);
			
			//t_user 改变用户信息 user_times++ ma_xnum--
			userDB.setTimes(userDB.getTimes() + 1);
			//可借阅数量判断
			if (userDB.getMaxNum() <= 0) {
				throw new MyException("已达最大借阅量");
			}
			userDB.setMaxNum(userDB.getMaxNum() - 1);
			userDao.updNum(userDB,conn);
			//事务提交
			conn.commit();
		}catch(Exception e) {
			//事务回滚
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			//判断自定义异常
			if (e instanceof MyException) {
				throw new MyException(e.getMessage());
			}else {
				e.printStackTrace();
				throw new MyException("借阅失败");
			}
		}
		
		
	}

	/**
	 * 图书归还
	 * @param userDB
	 * @param bid
	 */
	public void backBook(String hid) {
		//获取数据库连接
		Connection conn = C3p0Tool.getConnection();
		try {
			//设置事务不自动提交
			conn.setAutoCommit(false);
			//根据hid获取historyDB，修改status为2
			HistoryDB historyDB = historyDao.list(hid).get(0);
			historyDB.setStatus(2);
			historyDao.updHistory(historyDB, conn);
			
			//根据historyDB获取图书bid
			//根据bid获取图书信息
			
			//根据historyDB获取用户uid
			String account = historyDB.getAccount();
			//根据uid获取用户信息，修改max_num+1
			UserDB userDB = new UserDB();
			userDB.setAccount(account);
			userDB = userDao.getList(userDB).get(0);
			userDB.setMaxNum(userDB.getMaxNum()+1);
			userDao.updNum(userDB, conn);
			//事务提交
			conn.commit();
		}catch(Exception e) {
			//事务回滚
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			//判断自定义异常
			if (e instanceof MyException) {
				throw new MyException(e.getMessage());
			}else {
				e.printStackTrace();
				throw new MyException("还书失败");
			}
		}
		
		
	}
		
	
}
