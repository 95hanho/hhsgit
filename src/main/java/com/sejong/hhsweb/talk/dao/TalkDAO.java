package com.sejong.hhsweb.talk.dao;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sejong.hhsweb.model.Talk;
import com.sejong.hhsweb.model.TalkEntry;
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

	public int insertTalkSpace(String tmd) {
		String[] tmdList = tmd.split(",");
		if(tmdList.length == 2) {
			talkmapper.insertTalkSpace(tmd);
		} else {
			talkmapper.insertTalkSpace2(tmd);
		}
		return talkmapper.selectTsNum(tmd);
		
	}

	public void insertTalk(Talk talk) {
		talkmapper.insertTalk(talk);
	}

	public void insertTalkEntry(TalkSpace ts) {
		String[] tsList = ts.getParticipants().split(",");
		for(String user : tsList) {
			ts.setParticipants(user);
			talkmapper.insertTalkEntry(ts);
		}
	}

	public void exitTalkSpace(TalkSpace ts, String userId) {
		System.out.println("exitTalkSpace DAO");
		System.out.println(ts.getIfone());
		
		if(ts.getIfone().equals("N")) {
			String updateParty = null;
			String participants = talkmapper.selectPartry(ts.getTsnum());
			String[] memList = participants.split(",");
			for(int i=0;i<memList.length;i++) {
				if(userId.equals(memList[memList.length-1])) {
					updateParty = participants.replace(","+userId, "");
				} else if(userId.equals(memList[i])) {
					updateParty = participants.replace(userId+",", "");
					break;
				} 
				if(memList.length == 1) {
					updateParty = "";
				}
			}
			ts.setParticipants(updateParty);
			talkmapper.updateTalkSpace(ts);
		}
		// talkentry 업데이트 했지만 필요없는거였음
		TalkEntry te = new TalkEntry();
		te.setTsnum(ts.getTsnum());
		te.setUserId(userId);
		talkmapper.updateTalkEntry(te);
		////////////////////////////////////
		Talk t = new Talk();
		t.setContent("님이 나갔습니다.");
		t.setTsnum(ts.getTsnum());
		t.setUserId(userId);
		talkmapper.insertTalk(t);
	}

}
