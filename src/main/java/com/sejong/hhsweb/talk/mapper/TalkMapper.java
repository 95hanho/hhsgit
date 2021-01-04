package com.sejong.hhsweb.talk.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.sejong.hhsweb.model.Talk;
import com.sejong.hhsweb.model.TalkEntry;
import com.sejong.hhsweb.model.TalkSpace;
import com.sejong.hhsweb.model.UploadFile;

@Mapper
public interface TalkMapper {

	ArrayList<TalkSpace> selectTalkList(String userId);

	ArrayList<Talk> selectTalksList(Talk t);
	
	ArrayList<Talk> allSelectTalksList(int tsnum);

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

	void insertUploadFile(UploadFile uf);

	UploadFile insertSelectImage(int tnum);

	int updateTalkRead(int tnum, String talkRead);


}
