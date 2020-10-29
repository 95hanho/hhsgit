package com.sejong.hhsweb.model;

import java.sql.Date;

import org.apache.ibatis.type.Alias;

@Alias("TalkEntry")
public class TalkEntry {
	private int tenum;
	private Date enrollDate;
	private int tsnum;
	private String userId;
	
	public TalkEntry() {
	}
	public TalkEntry(int tenum, Date enrollDate, int tsnum, String userId) {
		super();
		this.tenum = tenum;
		this.enrollDate = enrollDate;
		this.tsnum = tsnum;
		this.userId = userId;
	}
	public int getTenum() {
		return tenum;
	}
	public void setTenum(int tenum) {
		this.tenum = tenum;
	}
	public Date getEnrollDate() {
		return enrollDate;
	}
	public void setEnrollDate(Date enrollDate) {
		this.enrollDate = enrollDate;
	}
	public int getTsnum() {
		return tsnum;
	}
	public void setTsnum(int tsnum) {
		this.tsnum = tsnum;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "TalkEntry [tenum=" + tenum + ", enrollDate=" + enrollDate + ", tsnum=" + tsnum + ", userId=" + userId
				+ "]";
	}
	
}
