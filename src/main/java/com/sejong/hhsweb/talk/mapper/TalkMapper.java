package com.sejong.hhsweb.talk.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.sejong.hhsweb.model.Talk;
import com.sejong.hhsweb.model.TalkSpace;

@Mapper
public interface TalkMapper {

	ArrayList<TalkSpace> selectTalkList(String userId);

	ArrayList<Talk> selectTalksList(int tsnum);

	void insertTalkSpace(String tmd);

	void insertTalkSpace2(String tmd);

	int selectTsNum(String tmd);

	void insertTalk(Talk talk);

}
