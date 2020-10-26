<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
</head>
<body>
	<c:import url="loginMain.jsp"></c:import>
	
	<header>
		<div id="loginInfoDiv">
			<h3>회원가입</h3>
			<form action="userInsert" method="post">
				<div><label>아이디 : </label><input name="userId"><br></div>
				<div><label>비번 : </label><input name="userPwd" type="password"></div><br>
				<div><label>이메일 : </label><input name="userEmail" type="email"></div><br>
				<div><input type="submit" value="완료"><input type="reset" value="리셋"></div>
			</form>
		</div>
	</header>

</body>
</html>