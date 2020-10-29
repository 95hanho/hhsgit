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
			<div id="talkHeader">${ TalkTitle }</div>
			<div id="talks">
				<c:set var="talkcount" value="${ TalkList.size() }"/>
				<div id="talkMargin"></div>
				<c:forEach var="t" items="${ TalkList }">
					<c:if test="${ t.userId == loginUser.userId }">
				<div class="talkline">
					<div class="mytalk">${t.userId} : ${ t.content }</div>
				</div>
					</c:if>
					<c:if test="${ t.userId != loginUser.userId }">
				<div class="talkline">
					<div class="otherstalk">${t.userId} : ${ t.content }</div>
				</div>
					</c:if>
				<c:set var="tsnum" value="${ t.tsnum }"/>
				</c:forEach>
			</div>
		</div>
		<div id="backDiv" >
			<button onclick="location.href='userLogin'">&lt;</button>
		</div>
		<div id="talkchatDiv">
			<div id="talkchatId">${ loginUser.userId }(Shift+Enter=줄바꿈):</div>
			<textarea id="talktext" onkeydown="return enterkey();"></textarea>
		</div>
	</section>
</body>
<script>
	if("${talkcount}" != ""){
		var tmheight = 340 - '${talkcount}' * 31.1;
		$('#talkMargin').css('height', tmheight);
	}
	sinTalkYN = 'N';
	if("${newTalkYN}" != ""){
		sinTalkYN = '${newTalkYN}';
	}
	var tsnum = '${ tsnum }';
	var userid = '${ loginUser.userId }';
	var tmd = '${ TalkTitle }';
</script>
</html>