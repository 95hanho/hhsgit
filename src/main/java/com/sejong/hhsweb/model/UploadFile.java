package com.sejong.hhsweb.model;

import java.sql.Date;

import org.apache.ibatis.type.Alias;

@Alias("UploadFile")
public class UploadFile {
	private int uploadNum;
	private Date uploadDate;
	private String originName;
	private String fileRename;
	private int tnum;
	
	public UploadFile() {
	}

	public UploadFile(int uploadNum, Date uploadDate, String originName, String fileRename, int tnum) {
		super();
		this.uploadNum = uploadNum;
		this.uploadDate = uploadDate;
		this.originName = originName;
		this.fileRename = fileRename;
		this.tnum = tnum;
	}

	public int getUploadNum() {
		return uploadNum;
	}

	public void setUploadNum(int uploadNum) {
		this.uploadNum = uploadNum;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getOriginName() {
		return originName;
	}

	public void setOriginName(String originName) {
		this.originName = originName;
	}

	public String getFileRename() {
		return fileRename;
	}

	public void setFileRename(String fileRename) {
		this.fileRename = fileRename;
	}

	public int getTnum() {
		return tnum;
	}

	public void setTnum(int tnum) {
		this.tnum = tnum;
	}

	@Override
	public String toString() {
		return "UploadFile [uploadNum=" + uploadNum + ", uploadDate=" + uploadDate + ", originName=" + originName
				+ ", fileRename=" + fileRename + ", tnum=" + tnum + "]";
	}
	
}
