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
</head>
<body>
	<c:import url="../user/loginMain.jsp"></c:import>

	<br>
	<section>
		<div id="talkDiv">
			<div class="talkSpaces" ondblclick="">&nbsp;채팅방참여자들</div>
		</div>
	</section>

</body>
<script>
	$(function() {
		talkInfo();
	});
	function talkInfo() {
		$.ajax({
			url : 'talkinfo',
			async : false,
			success : function(data) {
				console.log(data);
				$('#talkDiv').html('');
				for ( var key in data) {
					var $div = $('<div class="talkSpaces">');
					var partyNum = data[key].participants.split(',');
					$div.text(data[key].participants+'('+partyNum.length+')');
					$div.attr('ondblclick', 'talkView(' + data[key].tsnum + ');');
					$('#talkDiv').append($div);
					console.log(data[key]);
				}
			}
		});
	}

	function talkView(tsnum){
		location.href="talkView?tsnum=" + tsnum;
	}
</script>
<script type="text/javascript" src="talk/js/talk.js"></script>
</html>