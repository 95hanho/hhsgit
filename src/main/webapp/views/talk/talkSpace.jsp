<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>채팅 방</title>
<link rel="stylesheet" href="talk/css/talkMain.css">
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script type="text/javascript" src="talk/js/talkSpace.js"></script>
</head>
<body>
	<c:import url="../user/loginMain.jsp"></c:import>
	
	<section>
		<div id="talksDiv"> <!-- 톡제목 및 내용들 -->
			<div id="talkHeader">${TalkTitle}</div>
			<div id="talks">
			<!-- 
				<div id="talkMargin"></div>
				<div class="talkline">
					<div class="mytalk">나의아이디 : 채팅내용</div>
					<div class="mytalk-image">
						<div>사진 : </div>
						<div><img alt="하하" src="talk/images/꽃2.png"></div>
					</div>x
				</div>
				<div class="talkline">
					<div class="otherstalk">타인아이디 : 채팅내용</div>
					<div class="otherstalk-image">
						<div>사진 : </div>
						<div><img alt="하하" src="talk/images/꽃2.png"></div>
					</div>
				</div>
			 -->
			 <!-- 
			 	<div id="talkMargin"></div>
			 	<div class="talkline">
			 		<div class="mytalk">hanho : ㅁㄴㅇㄹㅁㄴㅇ<div class="langs">한국어</div></div>
			 	</div>
			 	<div class="talkline">
			 		<div class="mytalk">hanho : ㅁㄴㅇㄹㅁㄴㅇ<div class="langs">한국어</div></div>
			 	</div>
			 	<div class="talkline">
			 		<div class="mytalk">hanho : ㅁㄴㅇㄹㅁㄴㅇ<div class="langs">한국어</div></div>
			 	</div>
			 	<div class="talkline">
			 		<div class="mytalk">hanho : ㅁㄴㅇㄹㅁㄴㅇ<div class="langs">한국어</div></div>
			 	</div>
			  -->
			</div>
		</div>
		<div id="backDiv" > <!-- 채팅방목록으로 돌아가기 -->
			<button id="transBt" onclick="transBtn();">파파고 번역(한->영, 영->한)</button>
			<button onclick="location.href='loginComplete'">&lt;</button>
		</div>
		<div id="talkchatDiv"> 
			<!-- 채팅치는칸 -->
			<div id="talkchatId">${ loginUser.userId }(Shift+Enter=줄바꿈):</div>
			<textarea id="talktext" onkeydown="return enterkey();"></textarea>
			<!-- 초대할 유저목록 보기버튼 -->
			<input id="inviteshowBtn" type="button" value="초대목록보기">
			<!-- 채팅방 exit -->
			<button onclick="exitTalk();">채팅방 나가기</button>
			<!-- 초대할 유저목록 -->
			<div id="inviteListDiv">
				<div id="inviteHeader">초대목록</div>
				<div id="invitelineDiv">
					<div class="inviteline">
						<label>한호성</label>
						<button>+</button>
					</div>
				</div>
			</div>
			<!-- 파일 올리기 -->
			<form id="uploadDiv" method="POST" enctype="multipart/form-data" action="filechat">
				<input type="file" id="file" name="file">
				<input id="hidTs" type="hidden" value="${ tsnum }" name="tsnum">
				<button type="button" id="btn-upload">전송</button>
			</form>
		</div>
		
	</section>
</body>
<script>
	sinTalkYN = 'N'; // 새로만들어진톡방인지?
	if("${newTalkYN}" != ""){
		sinTalkYN = '${newTalkYN}';
	}
	var tsnum = '${ tsnum }'; // 톡방의 기본키값
	var userid = '${ loginUser.userId }'; // 접속유저아이디
	var tmd = '${ TalkTitle }'; // 톡 참여자목록
	var webmessage = '${ webmessage }'; // 웹소켓메시지
	var ifone = 'N'; // 단체톡인지 단체톡은 N
</script>
</html>