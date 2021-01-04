package com.sejong.hhsweb.model;

import java.sql.Date;

import org.apache.ibatis.type.Alias;

@Alias("TalkSpace")
public class TalkSpace {
	private int tsnum;
	private Date enrolldate;
	private String participants;
	private String ifone;
	
	// -------
	private String lastTalk;
	private int noReadNum;
	
	public TalkSpace() {
	}

	public TalkSpace(int tsnum, Date enrolldate, String participants, String ifone) {
		super();
		this.tsnum = tsnum;
		this.enrolldate = enrolldate;
		this.participants = participants;
		this.ifone = ifone;
	}
	
	public TalkSpace(int tsnum, Date enrolldate, String participants, String ifone, String lastTalk) {
		super();
		this.tsnum = tsnum;
		this.enrolldate = enrolldate;
		this.participants = participants;
		this.ifone = ifone;
		this.lastTalk = lastTalk;
	}

	public int getTsnum() {
		return tsnum;
	}

	public void setTsnum(int tsnum) {
		this.tsnum = tsnum;
	}

	public Date getEnrolldate() {
		return enrolldate;
	}

	public void setEnrolldate(Date enrolldate) {
		this.enrolldate = enrolldate;
	}

	public String getParticipants() {
		return participants;
	}

	public void setParticipants(String participants) {
		this.participants = participants;
	}

	public String getIfone() {
		return ifone;
	}

	public void setIfone(String ifone) {
		this.ifone = ifone;
	}
	
	public String getLastTalk() {
		return lastTalk;
	}

	public void setLastTalk(String lastTalk) {
		this.lastTalk = lastTalk;
	}

	public int getNoReadNum() {
		return noReadNum;
	}

	public void setNoReadNum(int noReadNum) {
		this.noReadNum = noReadNum;
	}

	@Override
	public String toString() {
		return "TalkSpace [tsnum=" + tsnum + ", enrolldate=" + enrolldate + ", participants=" + participants
				+ ", ifone=" + ifone + ", lastTalk=" + lastTalk + ", noReadNum=" + noReadNum + "]";
	}

}
