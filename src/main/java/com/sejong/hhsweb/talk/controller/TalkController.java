package com.sejong.hhsweb.talk.controller;

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

@Controller
public class TalkController {
	
	static final Logger logger = LoggerFactory.getLogger(TalkController.class);
	
	@Autowired
	private TalkService talkService;
	
	@GetMapping("talkinfo")
	@ResponseBody
	public ArrayList<TalkSpace> talkInfo(HttpSession session) {
		User user = (User)session.getAttribute("loginUser");
		String userId = user.getUserId();
		ArrayList<TalkSpace> talkList = talkService.selectTalkList(userId);
		
		return talkList;
	}
	
	@GetMapping("talkView")
	public String talkView(Model m, @RequestParam("tsnum") int tsnum, HttpSession session) {
		ArrayList<Talk> TalkList = talkService.selectTalksList(tsnum);
		User user = (User)session.getAttribute("loginUser");
		String userId = user.getUserId();
		ArrayList<TalkSpace> talkList = talkService.selectTalkList(userId);
		for(TalkSpace talkSpace : talkList) {
			if(talkSpace.getTsnum() == tsnum) {
				m.addAttribute("TalkTitle", talkSpace.getParticipants());
			}
		}
		m.addAttribute("TalkList", TalkList);
		
		return "talk/talkSpace";
	}
}
