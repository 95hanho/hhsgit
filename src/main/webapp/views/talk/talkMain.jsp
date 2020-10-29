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
		<div id="talkDiv">
			<div class="talkSpaces" ondblclick="">&nbsp;채팅방참여자들
				<button>X</button>
			</div>
		</div>
		<div id="talkadd">
			<div id="talkaddBtn">+</div>
			<div id="userListDiv">
				<div id="userListHeader">접속유저목록</div>
				<div id="userList">
					<c:forEach var="aul" items="${ allUserList }">
					<div class="userline" onclick="guestAdd('${ aul.userId}');">${ aul.userId }</div>
					</c:forEach>
				</div>
			</div>
		</div>
		<div id="talkmakeDiv"></div>
		<button id="talkmakeBtn" onclick="talkmake();">톡방생성</button>
	</section>
	
	
</body>
</html>