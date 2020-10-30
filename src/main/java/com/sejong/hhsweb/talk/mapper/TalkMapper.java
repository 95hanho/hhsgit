package com.sejong.hhsweb.talk.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.sejong.hhsweb.model.Talk;
import com.sejong.hhsweb.model.TalkEntry;
import com.sejong.hhsweb.model.TalkSpace;

@Mapper
public interface TalkMapper {

	ArrayList<TalkSpace> selectTalkList(String userId);

	ArrayList<Talk> selectTalksList(Talk t);

	void insertTalkSpace(String tmd);

	void insertTalkSpace2(String tmd);

	int selectTsNum(String tmd);

	void insertTalk(Talk talk);
	
	void updateTSdate(Talk talk);

	void insertTalkEntry(TalkSpace ts);

	String selectPartry(int tsnum);

	void updateTalkSpace(TalkSpace ts);

	void deleteTalkEntry(TalkEntry te);

	void updateTalkEntry(TalkEntry te);

	


}
