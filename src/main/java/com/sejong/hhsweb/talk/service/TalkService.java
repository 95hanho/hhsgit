package com.sejong.hhsweb.talk.service;

import java.util.ArrayList;

import com.sejong.hhsweb.model.Talk;
import com.sejong.hhsweb.model.TalkSpace;

public interface TalkService {

	ArrayList<TalkSpace> selectTalkList(String userId);

	ArrayList<Talk> selectTalksList(Talk t);

	int insertTalkSpace(String tmd);

	void insertTalk(Talk talk);

	void insertTalkEntry(TalkSpace ts);
	
	void exitTalkSpace(TalkSpace ts, String userId);

	void updateTalkSpace(TalkSpace ts);

	


}
