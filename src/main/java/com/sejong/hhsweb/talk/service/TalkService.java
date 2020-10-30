package com.sejong.hhsweb.talk.service;

import java.util.ArrayList;

import com.sejong.hhsweb.model.Talk;
import com.sejong.hhsweb.model.TalkSpace;

public interface TalkService {

	// 채팅방목록가져오기
	ArrayList<TalkSpace> selectTalkList(String userId);
	// 채팅내용가져오기
	ArrayList<Talk> selectTalksList(Talk t);
	// 채팅방 생성하기
	int insertTalkSpace(String tmd);
	// 톡 내용추가하기
	void insertTalk(Talk talk);
	// 톡 입장관리
	void insertTalkEntry(TalkSpace ts);
	// 채팅방 나가기
	void exitTalkSpace(TalkSpace ts, String userId);
	// 채팅참여자 추가
	void updateTalkSpace(TalkSpace ts);

}
