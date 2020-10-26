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
</head>
<body>
	<c:import url="../user/loginMain.jsp"></c:import>

	<section>
		<div id="talkDiv">
			<div id="talkHeader">${ TalkTitle }</div>
			<c:forEach var="t" items="${ TalkList }">
				<c:if test="${ t.userId == loginUser.userId }">
			<div class="talkline">
				<div class="mytalk">${ t.content }</div>
			</div>
				</c:if>
				<c:if test="${ t.userId != loginUser.userId }">
			<div class="talkline">
				<div class="otherstalk">${ t.content }</div>
			</div>
				</c:if>
			</c:forEach>
		</div>
	</section>
</body>
<script>
	function selectTalks() {
		$.ajax({
			url : 'selectTalks',
			async : false,
			success : function(data) {
				
			}
		});
	}
</script>
</html>