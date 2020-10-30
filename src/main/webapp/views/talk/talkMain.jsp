<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>채팅 메인</title>
<link rel="stylesheet" href="talk/css/talkMain.css">
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script type="text/javascript" src="talk/js/talkMain.js"></script>
<script type="text/javascript" src="websocket/js/websocket.js"></script>
</head>
<body>
	<c:import url="../user/loginMain.jsp"></c:import>

	<br>
	<section>
		<div id="talkDiv">	<!-- 참여 채팅방 목록 -->
			<div class="talkSpaces" ondblclick="">&nbsp;채팅방참여자들
				<label></label>
				<button>X</button>
			</div>
		</div>
		
		<div id="talkadd">
			<div id="talkaddBtn" onclick="userOut();">-</div> <!-- 초대유저제외 -->
			<div id="userListDiv"> <!-- 초대가능유저목록 -->
				<div id="userListHeader">접속유저목록</div>
				<div id="userList">
					<c:forEach var="aul" items="${ allUserList }">
					<div class="userline" onclick="guestAdd('${ aul.userId}');">${ aul.userId }</div>
					</c:forEach>
				</div>
			</div>
		</div>
		<div id="talkmakeDiv"></div> <!-- 초대할사람 -->
		<button id="talkmakeBtn" onclick="talkmake();">톡방생성</button>
	</section>
	
</body>
<script>
	// 웹소켓메시지
	var webmessage = '${ webmessage }';
</script>
</html>