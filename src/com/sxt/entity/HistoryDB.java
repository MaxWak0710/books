package com.sxt.entity;

import java.time.LocalDateTime;
import java.util.Date;
/**
 * 图书借阅历史记录
 * @author 17279
 *
 */

public class HistoryDB {
	private Integer hid;
	private Integer uid;
	private String name;
	private String account;
	private Integer bid;
	private String bookName;
	private LocalDateTime beginTime;
	private LocalDateTime endTime;
	private Integer status;
	public Integer getHid() {
		return hid;
	}
	public void setHid(Integer hid) {
		this.hid = hid;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public Integer getBid() {
		return bid;
	}
	public void setBid(Integer bid) {
		this.bid = bid;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public LocalDateTime getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(LocalDateTime beginTime) {
		this.beginTime = beginTime;
	}
	public LocalDateTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "HistoryDB [hid=" + hid + ", uid=" + uid + ", name=" + name + ", account=" + account + ", bid=" + bid
				+ ", bookName=" + bookName + ", beginTime=" + beginTime + ", endTime=" + endTime + ", status=" + status
				+ "]";
	}

	
	
}
	
	
 

