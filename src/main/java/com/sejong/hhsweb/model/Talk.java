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
	private String talkRead;
	
	// --------------------------------------
	private String sendBoolean;
	
	public Talk() {
	}

	public Talk(int tnum, Date enrollDate, String content, String userId, int tsnum, String status, String talkRead,
			String sendBoolean) {
		super();
		this.tnum = tnum;
		this.enrollDate = enrollDate;
		this.content = content;
		this.userId = userId;
		this.tsnum = tsnum;
		this.status = status;
		this.talkRead = talkRead;
		this.sendBoolean = sendBoolean;
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

	public String getTalkRead() {
		return talkRead;
	}

	public void setTalkRead(String talkRead) {
		this.talkRead = talkRead;
	}

	public String getSendBoolean() {
		return sendBoolean;
	}

	public void setSendBoolean(String sendBoolean) {
		this.sendBoolean = sendBoolean;
	}

	@Override
	public String toString() {
		return "Talk [tnum=" + tnum + ", enrollDate=" + enrollDate + ", content=" + content + ", userId=" + userId
				+ ", tsnum=" + tsnum + ", status=" + status + ", talkRead=" + talkRead + ", sendBoolean=" + sendBoolean
				+ "]";
	}
	
}
