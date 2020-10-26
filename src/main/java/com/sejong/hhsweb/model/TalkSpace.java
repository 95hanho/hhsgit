package com.sejong.hhsweb.model;

import java.sql.Date;

import org.apache.ibatis.type.Alias;

@Alias("TalkSpace")
public class TalkSpace {
	private int tsnum;
	private Date enrolldate;
	private String participants;
	
	public TalkSpace() {
	}

	public TalkSpace(int tsnum, Date enrolldate, String participants) {
		super();
		this.tsnum = tsnum;
		this.enrolldate = enrolldate;
		this.participants = participants;
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

	@Override
	public String toString() {
		return "TalkSpace [tsnum=" + tsnum + ", enrolldate=" + enrolldate + ", participants=" + participants + "]";
	}
	
	
}
