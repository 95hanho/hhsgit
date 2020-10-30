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
<script type="text/javascript" src="websocket/js/websocket.js"></script>
</head>
<body>
	<c:import url="../user/loginMain.jsp"></c:import>
	
	<section>
		<div id="talksDiv">
			<div id="talkHeader">${TalkTitle}</div>
			<div id="talks">
				<!-- 
				<div id="talkMargin"></div>
				<div class="talkline">
					<div class="mytalk">나의아이디 : 채팅내용</div>
				</div>
				<div class="talkline">
					<div class="otherstalk">타인아이디 : 채팅내용</div>
				</div>
				 -->
			</div>
		</div>
		<div id="backDiv" >
			<button onclick="location.href='userLogin'">&lt;</button>
		</div>
		<div id="talkchatDiv">
			<div id="talkchatId">${ loginUser.userId }(Shift+Enter=줄바꿈):</div>
			<textarea id="talktext" onkeydown="return enterkey();"></textarea>
			<input id="inviteshowBtn" type="button" value="초대목록보기">
			<button onclick="exitTalk();">채팅방 나가기</button>
			<div id="inviteListDiv">
				<div id="inviteHeader">초대목록</div>
				<div id="invitelineDiv">
					<div class="inviteline">
						<label>한호성</label>
						<button>+</button>
					</div>
				</div>
				
			</div>
		</div>
		
	</section>
</body>
<script>
	sinTalkYN = 'N';
	if("${newTalkYN}" != ""){
		sinTalkYN = '${newTalkYN}';
	}
	var tsnum = '${ tsnum }';
	var userid = '${ loginUser.userId }';
	var tmd = '${ TalkTitle }';
	var webmessage = '${ webmessage }';
</script>
</html>