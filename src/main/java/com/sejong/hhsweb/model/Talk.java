package com.sejong.hhsweb.model;

import java.sql.Date;

import org.apache.ibatis.type.Alias;

@Alias("Talk")
public class Talk {
	private int tnum;
	private Date enrollDate;
	private String content;
	private String userId;
	private int tsnum;
	private String status;
	
	public Talk() {
	}

	public Talk(int tnum, Date enrollDate, String content, String userId, int tsnum, String status) {
		super();
		this.tnum = tnum;
		this.enrollDate = enrollDate;
		this.content = content;
		this.userId = userId;
		this.tsnum = tsnum;
		this.status = status;
	}

	public int getTnum() {
		return tnum;
	}

	public void setTnum(int tnum) {
		this.tnum = tnum;
	}

	public Date getEnrollDate() {
		return enrollDate;
	}

	public void setEnrollDate(Date enrollDate) {
		this.enrollDate = enrollDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getTsnum() {
		return tsnum;
	}

	public void setTsnum(int tsnum) {
		this.tsnum = tsnum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Talk [tnum=" + tnum + ", enrollDate=" + enrollDate + ", content=" + content + ", userId=" + userId
				+ ", tsnum=" + tsnum + ", status=" + status + "]";
	}
	
}
