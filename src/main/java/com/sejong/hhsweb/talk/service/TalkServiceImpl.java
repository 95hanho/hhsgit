package com.sejong.hhsweb.talk.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sejong.hhsweb.model.Talk;
import com.sejong.hhsweb.model.TalkSpace;
import com.sejong.hhsweb.model.UploadFile;
import com.sejong.hhsweb.talk.dao.TalkDAO;

@Service("talkService")
public class TalkServiceImpl implements TalkService{
	
	@Autowired
	private TalkDAO talkDAO;

	@Override
	public ArrayList<TalkSpace> selectTalkList(String userId) {
		return talkDAO.selectTalkList(userId);
	}

	@Override
	public ArrayList<Talk> selectTalksList(Talk t) {
		return talkDAO.selectTalksList(t);
	}

	@Override
	public int insertTalkSpace(String tmd) {
		return talkDAO.insertTalkSpace(tmd);
	}

	@Override
	public void insertTalk(Talk talk) {
		talkDAO.insertTalk(talk);
	}

	@Override
	public void insertTalkEntry(TalkSpace ts) {
		talkDAO.insertTalkEntry(ts);
	}

	@Override
	public void exitTalkSpace(TalkSpace ts, String userId) {
		talkDAO.exitTalkSpace(ts, userId);
	}

	@Override
	public void updateTalkSpace(TalkSpace ts) {
		talkDAO.updateTalkSpace(ts);
	}

	@Override
	public void insertUploadFile(UploadFile uf) {
		talkDAO.insertUploadFile(uf);
	}

	@Override
	public UploadFile insertSelectImage(int tnum) {
		return talkDAO.insertSelectImage(tnum);
	}

	@Override
	public int updateTalkRead(int tsnum, String userId) {
		return talkDAO.updateTalkRead(tsnum, userId);
	}

	@Override
	public void deleteTalkRead(int tsnum, String userId) {
		talkDAO.deleteTalkRead(tsnum, userId);
	}


}
