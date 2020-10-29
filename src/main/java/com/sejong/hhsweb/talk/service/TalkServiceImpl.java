package com.sejong.hhsweb.talk.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sejong.hhsweb.model.Talk;
import com.sejong.hhsweb.model.TalkSpace;
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
	public ArrayList<Talk> selectTalksList(int tsnum) {
		return talkDAO.selectTalksList(tsnum);
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


}
