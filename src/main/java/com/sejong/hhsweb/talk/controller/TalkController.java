package com.sejong.hhsweb.talk.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.sejong.hhsweb.model.Talk;
import com.sejong.hhsweb.model.TalkSpace;
import com.sejong.hhsweb.model.UploadFile;
import com.sejong.hhsweb.model.User;
import com.sejong.hhsweb.talk.download.DownloadView;
import com.sejong.hhsweb.talk.papago.ApiExamDetectLangs;
import com.sejong.hhsweb.talk.papago.ApiExamTranslateNmt;
import com.sejong.hhsweb.talk.service.TalkService;
import com.sejong.hhsweb.user.service.UserService;

@Controller
public class TalkController {

	/*
	 * 1.채팅방목록 가져오기 2.채팅방 들어가기 3.채팅창 목록에서 채팅방나가기 4.채팅방에서 채팅나가기 5.채팅방생성 전에 채팅화면으로 가줌
	 * 6.새 채팅방에서 채팅칠시 채팅방 생성 7.채팅 추가 8.채팅불러오기 9.채팅참여목록 가져오기 10.초대가능유저목록 가져오기
	 * 11.초대가능목록에서 초대하기
	 */
	static final Logger logger = LoggerFactory.getLogger(TalkController.class);
	private Properties prop = new Properties();

	@Autowired
	private TalkService talkService;

	@Autowired
	private UserService userService;

	public TalkController() {
		String fileName = TalkController.class.getResource("/path.properties").getPath();
		try {
			prop.load(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 채팅방목록 가져오기
	@GetMapping("talkinfo")
	@ResponseBody
	public ArrayList<TalkSpace> talkInfo(HttpSession session) {
		logger.info("talkSpaces INFO");

		String userId = ((User) session.getAttribute("loginUser")).getUserId();
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
		if (ifone != null) {
			talkService.exitTalkSpace(ts, userId);
		}

		m.addAttribute("webmessage", "talkmake:" + tmd);
		return "talk/talkMain";

	}

	// 채팅방생성 전에 채팅화면으로 가줌
	@GetMapping("talkmake")
	public String talkView(Model m, @RequestParam("tmd") String tmd, HttpSession session) {
		logger.info("new Talk view");
		User user = (User) session.getAttribute("loginUser");
		String userId = user.getUserId();
		String[] tmdList = tmd.split(",");
		
		boolean userDupl = false;
		for(String id : tmdList) {
			if(id.equals(userId)) {
				userDupl = true;
			}
		}
		if(!userDupl) {
			tmd = userId + "," + tmd;
		}
		
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

	// ------------------기본형태는 완성 ------------------------

	// 채팅에 사진업로드
	@PostMapping(value = "filechat", produces = "text/plain")
	@ResponseBody
	public void fileChatting(MultipartHttpServletRequest multi, @RequestParam("tsnum") int tsnum, HttpSession session) {
		logger.info("file save");

		String userId = ((User) session.getAttribute("loginUser")).getUserId();
//		// "파일"이라는 톡을 남김 그리고 tnum을 가져옴
		Talk t = new Talk();
//		t.setContent("파일");
		t.setTsnum(tsnum);
		t.setUserId(userId);
//		talkService.insertTalk(t);

		ArrayList<Talk> TalkList = talkService.selectTalksList(t);
		int tnum = TalkList.get(TalkList.size() - 1).getTnum();
		// 파일을 저장하고, uploadfile테이블에 파일정보 저장
		MultipartFile uploadFile = multi.getFile("file");
		String renameFileName = saveFile(uploadFile, "uploadPath");
		UploadFile uf = new UploadFile();
		uf.setOriginName(uploadFile.getOriginalFilename());
		uf.setFileRename(renameFileName);
		uf.setTnum(tnum);
		talkService.insertUploadFile(uf);

	}

	// 파일저장메소드
	private String saveFile(MultipartFile file, String savePath) {
		File folder = new File(prop.getProperty(savePath));
		if (!folder.exists()) {
			folder.mkdirs();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String originFileName = file.getOriginalFilename();
		String renameFileName = sdf.format(new Date(System.currentTimeMillis())) + "."
				+ originFileName.substring(originFileName.lastIndexOf(".") + 1);
		String renamePath = folder + "//" + renameFileName;

		try {
			file.transferTo(new File(renamePath));
			logger.info("file save complete");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return renameFileName;
	}

	// 이미지 불러오기
	@GetMapping("selectImage")
	@ResponseBody
	private UploadFile selectImage(@RequestParam("tnum") int tnum) {
		UploadFile file = talkService.insertSelectImage(tnum);
		return file;
	}

	// 파일 다운로드
	@GetMapping("/document/fileDownload.do")
	public void fileDownload(@RequestParam("document_nm") String document_nm, HttpSession session,
			HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Throwable {
		try {
			DownloadView fileDown = new DownloadView();
			fileDown.fileDown(req, res,
					"C:/STUDY/WorkspaceCollection/hhsgit/hhsweb/src/main/resources/static/uploadfiles" + "/",
					document_nm, document_nm);
			logger.info("download!!!");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
	}

	// text를 받아서 언어의 종류를 파악하여 반환
	@GetMapping("langsCheck")
	@ResponseBody
	public String langsCheck(@RequestParam("text") String text) {
		ApiExamDetectLangs adl = new ApiExamDetectLangs();
		String result = adl.detectLangs(text);

		return result;
	}
	
	// langs가 한국어면 content의 내용을 영어로, 영어면 한국어로 바꾸어 반환
	@GetMapping("papagotrans")
	@ResponseBody
	public String papagotrans(@RequestParam("content") String content, @RequestParam("langs") String langs) {
		ApiExamTranslateNmt atn = new ApiExamTranslateNmt();
		String result = atn.transLangs(content, langs);
		return result;
	}

	// 접속한 회원 목록
	@GetMapping("connectUser")
	@ResponseBody
	public ArrayList<User> connectUser(@RequestParam("userArr") String userArr, HttpSession session) {
		String userId = ((User) session.getAttribute("loginUser")).getUserId();
		ArrayList<User> allUserList = userService.AllSelectUser(userId);
		String[] userInfo = userArr.split(", ");

		// 해당 회원을 제외한 회원들 중에서
		for (User u : allUserList) {
			u.setConnect("f");
			u.setUserConnect(connectTimes(u.getUserConnect())); // 로그아웃시간
			
			// 접속한 회원과
			for (String user : userInfo) {
				// 일치하는 회원은 connect가 't' 
				if (user.equals(u.getUserId())) {
					u.setConnect("t");
				}
			}
		}
		
		return allUserList;
	}

	// 초대가능유저목록 가져오기
	@GetMapping("selectIUlist")
	@ResponseBody
	public ArrayList<User> selectIUlist(@RequestParam("userArr") String userArr, HttpSession session,
			@RequestParam("tmd") String tmd) {
		String userId = ((User) session.getAttribute("loginUser")).getUserId();
		ArrayList<User> allUserList = userService.AllSelectUser(userId);
		String[] inUserList = tmd.split(",");
		String[] userInfo = userArr.split(", ");
		ArrayList<User> inviteUserList = new ArrayList<User>();

		for (int i = 0; i < allUserList.size(); i++) {
			// 해당 회원을 제외한 회원 중에서
			boolean dupl = true;
			for (String inUser : inUserList) {
				// 톡방에 있는 회원을 제외
				if (inUser.equals(allUserList.get(i).getUserId())) {
					dupl = false;
				}
			}
			if (dupl) {
				inviteUserList.add(allUserList.get(i));
			}
		}

		// 톡방에 있는 회원을 제외한 회원 중
		for (User u : inviteUserList) {
			u.setConnect("f");
			u.setUserConnect(connectTimes(u.getUserConnect())); // 로그아웃시간
			
			
			for (String user : userInfo) {
				if (user.equals(u.getUserId())) {
					// 접속한 회원은 't'
					u.setConnect("t");
				}
			}
		}

		return inviteUserList;
	}
	
	// '2020-12-28 10:44:47식'으로 돼있는 문자를 Date객체로 바꾸어 현재시각에서 얼마나 지났는지 반환
	public String connectTimes(String strTimes) {
		String result = null;
		
		int year = Integer.parseInt(strTimes.substring(0, 4));
		int month = Integer.parseInt(strTimes.substring(5, 7)) - 1;
		int dayOfMonth = Integer.parseInt(strTimes.substring(8, 10));
		int hourOfDay = Integer.parseInt(strTimes.substring(11, 13));
		int minute = Integer.parseInt(strTimes.substring(14, 16));
		int second = Integer.parseInt(strTimes.substring(17, 19));
		GregorianCalendar g = new GregorianCalendar(year, month, dayOfMonth, hourOfDay, minute, second);
		Date now = new Date();
		Date d = new Date(g.getTimeInMillis());
		int nowSecond = (int) (now.getTime() * 0.001);
		int dSecond = (int) (d.getTime() * 0.001);
		int mili = nowSecond - dSecond;
		if (mili < 60) {
			result = mili + "초 전";
		} else if (60 <= mili && mili < 3600) {
			mili = mili / 60;
			result = mili + "분 전";
		} else if (3600 <= mili && mili < (3600 * 24)) {
			mili = mili / 3600;
			result = mili + "시간 전";
		} else if ((3600 * 24) <= mili && mili < (3600 * 24 * 365)) {
			mili = mili / (3600 * 24);
			result = mili + "일 전";
		} else if ((3600 * 24 * 365) <= mili) {
			mili = mili / (3600 * 24 * 365);
			result = mili + "년 전";
		}
		
		return result;
	}
	
}
