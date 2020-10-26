package com.sejong.hhsweb.talk.dao;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sejong.hhsweb.model.Talk;
import com.sejong.hhsweb.model.TalkSpace;
import com.sejong.hhsweb.talk.mapper.TalkMapper;

@Repository("talkDAO")
public class TalkDAO {

	@Autowired
	private TalkMapper talkmapper;
	
	public ArrayList<TalkSpace> selectTalkList(String userId) {
		return talkmapper.selectTalkList(userId);
	}

	public ArrayList<Talk> selectTalksList(int tsnum) {
		return talkmapper.selectTalksList(tsnum);
	}

}
