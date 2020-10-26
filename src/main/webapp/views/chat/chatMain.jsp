<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>채팅 메인</title>
</head>
<body>
	<c:if test="${ !empty sessionScope.loginUser }">
		<h2>${loginUser.userId} 님 환영합니다.</h2>
	</c:if>
	
	<header>
		<div id="loginDiv">
		</div>
	</header>
	
</body>
</html>