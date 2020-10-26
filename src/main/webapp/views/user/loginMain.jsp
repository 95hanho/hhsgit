<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인 메인</title>
	<link rel="stylesheet" href="user/css/loginMain.css">
</head>
<body>
	<header>
		<div id="loginDiv">
			<h3>로그인</h3>
			<form action="userLogin" method="post">
				<div><label>아이디 : </label><input name="userId"><br></div>
				<div><label>비번 : </label><input name="userPwd" type="password"></div><br>
				<div><input type="submit" value="로그인"><input onclick="location.href='logininfoView'" type="button" value="회원가입"></div>
			</form>
			<c:if test="${ !empty requestScope.message }">
			<h2>${ message }</h2>
			</c:if>
		</div>
		
	</header>
	
</body>
</html>