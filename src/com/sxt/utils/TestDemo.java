package com.sxt.utils;

import java.sql.SQLException;
import java.util.Date;

import com.sxt.dao.BookDao;
import com.sxt.dao.HistoryDao;
import com.sxt.dao.UserDao;
import com.sxt.entity.BookDB;
import com.sxt.entity.HistoryDB;
import com.sxt.entity.UserDB;

public class TestDemo {
	
	public static void main(String[] args) throws SQLException {
		HistoryDao historyDao = new HistoryDao();
		HistoryDB historyDB = new HistoryDB();
		historyDB.setHid(7);
		historyDB.setStatus(2);
		System.out.println(historyDao.updHistory(historyDB, C3p0Tool.getConnection()));
	}

	
	
}
