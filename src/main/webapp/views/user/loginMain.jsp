<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인 메인</title>
<link rel="stylesheet" href="user/css/loginMain.css">
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script type="text/javascript" src="websocket/js/websocket.js"></script>
</head>
<body>
<c:if test="${ !empty requestScope.message }">
	<h2 style="text-align: center;">${ message }</h2>
</c:if>
<c:if test="${ empty loginUser }">
	<header>
		<div id="loginDiv">
			<h3>로그인</h3>
			<form action="userLogin" method="post">
				<div><label>아이디 : </label><input name="userId"><br></div>
				<div><label>비번 : </label><input name="userPwd" type="password"></div><br>
				<div><input type="submit" value="로그인"><input onclick="location.href='logininfoView'" type="button" value="회원가입"></div>
			</form>
		</div>
	</header>
</c:if>
<c:if test="${ !empty loginUser }">
	<div id="logout-Wrap">
		<div id="logoutDiv">
			<button onclick="location.href='logoutView'">${ loginUser.userId } 로그아웃</button>
		</div>
	</div>
</c:if>
</body>
<script>
var webmessage = '${ webmessage }'; // 웹소켓메시지
var ms = '${ms}'; // 섹션 종료 메시지
if(ms != ''){ // 있다면 알람을 표시
	alert('세션이 종료되어 로그인화면으로 돌아갑니다.');
}
</script>
</html>