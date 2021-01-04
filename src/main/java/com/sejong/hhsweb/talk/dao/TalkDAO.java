package com.sejong.hhsweb.talk.dao;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sejong.hhsweb.model.Talk;
import com.sejong.hhsweb.model.TalkEntry;
import com.sejong.hhsweb.model.TalkSpace;
import com.sejong.hhsweb.model.UploadFile;
import com.sejong.hhsweb.talk.mapper.TalkMapper;

@Repository("talkDAO")
public class TalkDAO {

	@Autowired
	private TalkMapper talkmapper;

	public ArrayList<TalkSpace> selectTalkList(String userId) {
		return talkmapper.selectTalkList(userId);
	}

	public ArrayList<Talk> selectTalksList(Talk t) {
		return talkmapper.selectTalksList(t);
	}

	public int insertTalkSpace(String tmd) {
		// 1:1인지 단체톡인지 구별하여 입력
		String[] tmdList = tmd.split(",");
		if (tmdList.length == 2) {
			talkmapper.insertTalkSpace(tmd);
		} else {
			talkmapper.insertTalkSpace2(tmd);
		}
		return talkmapper.selectTsNum(tmd);

	}

	public void insertTalk(Talk talk) {
		// 톡추가
		// 톡date를 최신으로
		talkmapper.insertTalk(talk);
		talkmapper.updateTSdate(talk);
	}

	public void insertTalkEntry(TalkSpace ts) {
		// 톡 입장명부 추가
		String[] tsList = ts.getParticipants().split(",");
		for (String user : tsList) {
			ts.setParticipants(user);
			talkmapper.insertTalkEntry(ts);
		}
	}

	public void exitTalkSpace(TalkSpace ts, String userId) {
		// 톡방 나가기
		if (ts.getIfone().equals("N")) {
			String updateParty = null;
			String participants = talkmapper.selectPartry(ts.getTsnum());
			String[] memList = participants.split(",");
			for (int i = 0; i < memList.length; i++) {
				if (userId.equals(memList[memList.length - 1])) {
					updateParty = participants.replace("," + userId, "");
				} else if (userId.equals(memList[i])) {
					updateParty = participants.replace(userId + ",", "");
					break;
				}
				if (memList.length == 1) {
					updateParty = "";
				}
			}
			ts.setParticipants(updateParty);
			talkmapper.updateTalkSpace(ts);

			Talk t = new Talk();
			t.setContent("님이 나갔습니다.");
			t.setTsnum(ts.getTsnum());
			t.setUserId(userId);
			talkmapper.insertTalk(t);

			// talkentry 삭제시키기
			TalkEntry te = new TalkEntry();
			te.setTsnum(ts.getTsnum());
			te.setUserId(userId);
			talkmapper.deleteTalkEntry(te);
		} else if (ts.getIfone().equals("Y")) {
			// talkentry enrolldate업데이트
			TalkEntry te = new TalkEntry();
			te.setTsnum(ts.getTsnum());
			te.setUserId(userId);
			talkmapper.updateTalkEntry(te);
		}

	}

	// 톡 유저 추가
	public void updateTalkSpace(TalkSpace ts) {
		talkmapper.updateTalkSpace(ts);
	}

	public void insertUploadFile(UploadFile uf) {
		talkmapper.insertUploadFile(uf);
	}

	public UploadFile insertSelectImage(int tnum) {
		return talkmapper.insertSelectImage(tnum);
	}

	public int updateTalkRead(int tsnum, String userId) {
		int result = 0;
		
		ArrayList<Talk> talkList = talkmapper.allSelectTalksList(tsnum);
		for(Talk talk : talkList) {
			boolean alReadyRead = true;
			String talkread = talk.getTalkRead();
			if(talkread == null) {
				result += talkmapper.updateTalkRead(talk.getTnum(), userId);
			} else {
				String[] talkReadUser = talk.getTalkRead().split(",");
				for(String user : talkReadUser) {
					if(user.equals(userId)) {
						alReadyRead = false;
					}
				}
				if(alReadyRead) {
					result += talkmapper.updateTalkRead(talk.getTnum(), talk.getTalkRead() + "," + userId);
				}
			}
		}
		
		return result;
	}

	public void deleteTalkRead(int tsnum, String userId) {
		ArrayList<Talk> talkList = talkmapper.allSelectTalksList(tsnum);
		for(Talk talk : talkList) {
			String talkread = talk.getTalkRead();
			String newTalkRead = "";
			if(talkread != null) {
				String[] talkReadUser = talk.getTalkRead().split(",");
				for(String user : talkReadUser) {
					if(user.equals(userId)) {
						continue;
					}
					newTalkRead = newTalkRead + user + ",";
				}
				talkmapper.updateTalkRead(talk.getTnum(), newTalkRead.substring(0, newTalkRead.length() - 1));
			}
		}
	}

}
