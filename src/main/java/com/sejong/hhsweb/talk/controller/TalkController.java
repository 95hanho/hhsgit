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

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.sejong.hhsweb.model.Talk;
import com.sejong.hhsweb.model.TalkSpace;
import com.sejong.hhsweb.model.User;
import com.sejong.hhsweb.talk.service.TalkService;

@Controller
public class TalkController {

	static final Logger logger = LoggerFactory.getLogger(TalkController.class);

	@Autowired
	private TalkService talkService;

	@GetMapping("talkinfo")
	@ResponseBody
	public ArrayList<TalkSpace> talkInfo(HttpSession session) {
		logger.info("talkSpaces INFO");
		
		User user = (User) session.getAttribute("loginUser");
		String userId = user.getUserId();
		ArrayList<TalkSpace> talkList = talkService.selectTalkList(userId);

		return talkList;
	}

	@GetMapping("talkView")
	public String talkView(Model m, 
			@RequestParam("tsnum") int tsnum, 
			HttpSession session) {
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

	@GetMapping("deletets")
	@ResponseBody
	public void deletets(Model m, 
			@RequestParam("tsnum") int tsnum,
			@RequestParam("ifone") String ifone,
			HttpSession session) {
		String userId = ((User) session.getAttribute("loginUser")).getUserId();
		
		TalkSpace ts = new TalkSpace();
		ts.setTsnum(tsnum);
		ts.setIfone(ifone);
		talkService.exitTalkSpace(ts, userId);
		
	}

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
	
	@GetMapping("talkmake2")
	@ResponseBody
	public String talkMake(Model m, 
			@RequestParam("tmd") String tmd, 
			@RequestParam("content") String content,
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
		talk.setUserId(((User)session.getAttribute("loginUser")).getUserId());
		talk.setTsnum(tsnum);
		talkService.insertTalk(talk);
		
		return tsnum+"";
	}

	@GetMapping("insertTalk")
	@ResponseBody
	public void insertTalk(Model m,
			@RequestParam("tsnum") int tsnum,
			@RequestParam("content") String content, 
			HttpSession session) {
		logger.info("INSERT Talk content");
		
		Talk talk = new Talk();
		talk.setContent(content);
		talk.setUserId(((User)session.getAttribute("loginUser")).getUserId());
		talk.setTsnum(tsnum);
		talkService.insertTalk(talk);
		
	}
	
	@GetMapping("selectTalks")
	@ResponseBody
	public ArrayList<Talk> selectTalks(@RequestParam("tsnum") int tsnum) {
		logger.info("Talk content INFO");
		ArrayList<Talk> TalkList = talkService.selectTalksList(tsnum);
		
		return TalkList;
	}
	
	@GetMapping("selectParticipant")
	@ResponseBody
	public String selectParticipant(@RequestParam("tsnum") int tsnum, HttpSession session) {
		String TalkTitle = "";
		
		String userId = ((User)session.getAttribute("loginUser")).getUserId();
		ArrayList<TalkSpace> talkList = talkService.selectTalkList(userId);
		for (TalkSpace talkSpace : talkList) {
			if (talkSpace.getTsnum() == tsnum) {
				TalkTitle = talkSpace.getParticipants();
			}
		}
		return TalkTitle;
	}

}
