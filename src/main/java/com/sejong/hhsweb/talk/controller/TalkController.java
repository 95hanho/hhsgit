package com.sejong.hhsweb.talk.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sejong.hhsweb.model.Talk;
import com.sejong.hhsweb.model.TalkSpace;
import com.sejong.hhsweb.model.User;
import com.sejong.hhsweb.talk.service.TalkService;
import com.sejong.hhsweb.user.service.UserService;

@Controller
public class TalkController {

	/*
	 1.채팅방목록 가져오기
	 2.채팅방 들어가기
	 3.채팅창 목록에서 채팅방나가기
	 4.채팅방에서 채팅나가기
	 5.채팅방생성 전에 채팅화면으로 가줌
	 6.새 채팅방에서 채팅칠시 채팅방 생성
	 7.채팅 추가
	 8.채팅불러오기
	 9.채팅참여목록 가져오기
	 10.초대가능유저목록 가져오기
	 11.초대가능목록에서 초대하기
	*/
	static final Logger logger = LoggerFactory.getLogger(TalkController.class);

	@Autowired
	private TalkService talkService;

	@Autowired
	private UserService userService;

	// 채팅방목록 가져오기
	@GetMapping("talkinfo")
	@ResponseBody
	public ArrayList<TalkSpace> talkInfo(HttpSession session) {
		logger.info("talkSpaces INFO");

		User user = (User) session.getAttribute("loginUser");
		String userId = user.getUserId();
		ArrayList<TalkSpace> talkList = talkService.selectTalkList(userId);

		return talkList;
	}
	
	// 채팅방 들어가기
	@GetMapping("talkView")
	public String talkView(Model m, @RequestParam("tsnum") int tsnum, HttpSession session) {
		logger.info("old Talk ENTER");

		User user = (User) session.getAttribute("loginUser");
		String userId = user.getUserId();
		ArrayList<TalkSpace> talkList = talkService.selectTalkList(userId);
		for (TalkSpace talkSpace : talkList) {
			if (talkSpace.getTsnum() == tsnum) {
				m.addAttribute("TalkTitle", talkSpace.getParticipants());
			}
		}

		m.addAttribute("tsnum", tsnum);

		return "talk/talkSpace";
	}

	// 채팅창 목록에서 채팅방나가기
	@GetMapping("deletets")
	@ResponseBody
	public void deletets(Model m, @RequestParam("tsnum") int tsnum, @RequestParam("ifone") String ifone,
			HttpSession session) {
		String userId = ((User) session.getAttribute("loginUser")).getUserId();

		TalkSpace ts = new TalkSpace();
		ts.setTsnum(tsnum);
		ts.setIfone(ifone);
		talkService.exitTalkSpace(ts, userId);

	}
	
	// 채팅방에서 채팅나가기
	@GetMapping("exitTalk")
	public String exitTalk(Model m, @RequestParam("tsnum") int tsnum, HttpSession session) {
		String userId = ((User) session.getAttribute("loginUser")).getUserId();
		String ifone = null;
		String tmd = null;

		ArrayList<TalkSpace> tsList = talkService.selectTalkList(userId);
		for (TalkSpace talkSpace : tsList) {
			if (talkSpace.getTsnum() == tsnum) {
				ifone = talkSpace.getIfone();
				tmd = talkSpace.getParticipants();
			}
		}

		TalkSpace ts = new TalkSpace();
		ts.setTsnum(tsnum);
		ts.setIfone(ifone);
		talkService.exitTalkSpace(ts, userId);

		ArrayList<User> allUserList = userService.AllSelectUser(userId);
		m.addAttribute("allUserList", allUserList);
		m.addAttribute("webmessage", "talkmake:" + tmd);
		return "talk/talkMain";

	}
	
	// 채팅방생성 전에 채팅화면으로 가줌
	@GetMapping("talkmake")
	public String talkView(Model m, @RequestParam("tmd") String tmd, HttpSession session) {
		logger.info("new Talk view");
		User user = (User) session.getAttribute("loginUser");
		String userId = user.getUserId();

		tmd = userId + "," + tmd;
		String[] tmdList = tmd.split(",");
		if (tmdList.length == 2) {
			ArrayList<TalkSpace> talkList = talkService.selectTalkList(userId);
			for (TalkSpace ts : talkList) {
				if (ts.getParticipants().equals(tmd)) {
					return "redirect:talkView?tsnum=" + ts.getTsnum();
				}
			}
		}
		m.addAttribute("TalkTitle", tmd);
		m.addAttribute("newTalkYN", "Y");

		return "talk/talkSpace";
	}

	// 새 채팅방에서 채팅칠시 채팅방 생성
	@GetMapping("talkmake2")
	@ResponseBody
	public String talkMake(Model m, @RequestParam("tmd") String tmd, @RequestParam("content") String content,
			HttpSession session) throws UnsupportedEncodingException {
		logger.info("new Talk CREATE");

		int tsnum = talkService.insertTalkSpace(tmd);
		// 톡방이 만들어 질 때 엔트리도 작성//////////////////
		TalkSpace ts = new TalkSpace();
		ts.setParticipants(tmd);
		ts.setTsnum(tsnum);
		talkService.insertTalkEntry(ts);
		// 엔트리 작성 구간////////////////////////////
		Talk talk = new Talk();
		talk.setContent(content);
		talk.setUserId(((User) session.getAttribute("loginUser")).getUserId());
		talk.setTsnum(tsnum);
		talkService.insertTalk(talk);

		return tsnum + "";
	}
	
	// 채팅 추가
	@GetMapping("insertTalk")
	@ResponseBody
	public void insertTalk(Model m, @RequestParam("tsnum") int tsnum, @RequestParam("content") String content,
			HttpSession session) {
		logger.info("INSERT Talk content");

		Talk talk = new Talk();
		talk.setContent(content);
		talk.setUserId(((User) session.getAttribute("loginUser")).getUserId());
		talk.setTsnum(tsnum);
		talkService.insertTalk(talk);

	}

	// 채팅불러오기
	@GetMapping("selectTalks")
	@ResponseBody
	public ArrayList<Talk> selectTalks(@RequestParam("tsnum") int tsnum, HttpSession session) {
		logger.info("Talk content INFO");
		String userId = ((User) session.getAttribute("loginUser")).getUserId();

		Talk t = new Talk();
		t.setUserId(userId);
		t.setTsnum(tsnum);
		ArrayList<Talk> TalkList = talkService.selectTalksList(t);

		return TalkList;
	}

	// 채팅참여목록 가져오기
	@GetMapping("selectParticipant")
	@ResponseBody
	public TalkSpace selectParticipant(@RequestParam("tsnum") int tsnum, HttpSession session) {
		TalkSpace resultTS = new TalkSpace();

		String userId = ((User) session.getAttribute("loginUser")).getUserId();
		ArrayList<TalkSpace> talkList = talkService.selectTalkList(userId);
		for (TalkSpace talkSpace : talkList) {
			if (talkSpace.getTsnum() == tsnum) {
				resultTS = talkSpace;
			}
		}
		return resultTS;
	}

	
	// 초대가능유저목록 가져오기
	@GetMapping("selectIUlist")
	@ResponseBody
	public ArrayList<User> selectIUlist(HttpSession session, @RequestParam("tmd") String tmd) {
		String userId = ((User) session.getAttribute("loginUser")).getUserId();
		ArrayList<User> allUserList = userService.AllSelectUser(userId);
		String[] inUserList = tmd.split(",");
		ArrayList<User> inviteUserList = new ArrayList<User>();

		for (int i = 0; i < allUserList.size(); i++) {
			boolean dupl = true;
			for (String inUser : inUserList) {
				if (inUser.equals(allUserList.get(i).getUserId())) {
					dupl = false;
				}
			}
			if (dupl) {
				inviteUserList.add(allUserList.get(i));
			}
		}
		return inviteUserList;
	}

	// 초대가능목록에서 초대하기
	@GetMapping("inviteUser")
	@ResponseBody
	public void inviteUser(@RequestParam("tsnum") int tsnum, @RequestParam("userId") String inviteId,
			@RequestParam("tmd") String tmd) {

		// 채팅방 인원 추가
		tmd = tmd + "," + inviteId;
		TalkSpace ts = new TalkSpace();
		ts.setTsnum(tsnum);
		ts.setParticipants(tmd);
		talkService.updateTalkSpace(ts);

		// 엔트리 추가
		TalkSpace ts2 = new TalkSpace();
		ts2.setParticipants(inviteId);
		ts2.setTsnum(tsnum);
		talkService.insertTalkEntry(ts2);

		// 입장 표시 채팅 추가
		Talk t = new Talk();
		t.setContent("님이 들어왔습니다.");
		t.setTsnum(tsnum);
		t.setUserId(inviteId);
		talkService.insertTalk(t);

	}
}
