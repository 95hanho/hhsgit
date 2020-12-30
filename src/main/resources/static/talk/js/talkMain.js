/**
 * 톡메인화면
 1. 채팅창 목록소환함수
 2. 톡방 나가기
 3. 더블클릭 후 톡방으로 들어감(기존있던 채팅방)
 4.	새로운 채팅방을 생성하면서 톡방화면으로 들어감(채팅방 생성은x)
 5. 채팅방 초대할 사람 추가
 6. 초대할 사람제외(뒤부터 제외시킴)
 */
$(function() {
	talkInfo(); // 채팅창목록소환
});

// 채팅창 목록소환함수
function talkInfo() {
	$.ajax({
		url : 'talkinfo',
		async : false,
		success : function(data) {
			$('#talkDiv').html('');
			for (var key in data) {
				var $div = $('<div class="talkSpaces">');
				var $label = $('<label>');
				var partyNum = data[key].participants.split(',');
				// 1:1채팅
				if(data[key].ifone == 'Y'){
					$label.text('1:1채팅:' + data[key].participants);
				// 단체톡이면 인원수도 표시
				} else if(data[key].ifone == 'N') {
					$label.text('단톡:' + '(' + partyNum.length + ')' + data[key].participants);
				}
				// 더블클릭 시 채팅방 이동
				$label.attr('ondblclick', 'talkView(' + data[key].tsnum + ');');
				var $button = $('<button>');
				// 'X'버튼 클릭 시 채팅방 나가기
				$button.attr('onclick', 'deletets(' + data[key].tsnum + ',"'+ data[key].ifone +'","'+data[key].participants +'");');
				$button.text('X');
				$div.append($label);
				$div.append($button);
				$('#talkDiv').append($div);
			}
		}
	});
}
// 톡방 나가기
function deletets(tsnum, ifone, tmd){
	$.ajax({
		url: 'deletets',
		data:{
			tsnum:tsnum,
			ifone:ifone
		},
		async:false,
		success:function(data){
			talkInfo();
			send_message('talkmake:'+tmd);
		}
	});
}
// 더블클릭 후 톡방으로 들어감(기존있던 채팅방)
function talkView(tsnum){
	location.href="talkView?tsnum=" + tsnum;
}
// 새로운 채팅방을 생성하면서 톡방화면으로 들어감(채팅방 생성은x)
function talkmake(){
	var tmd = $('#talkmakeDiv').text();
	if(tmd.length=='0'){
		alert('참가자를 선택하세요');
	} else{
		location.href="talkmake?tmd="+tmd;
	}
}
// 채팅방 초대할 사람 추가 - 동일 인원이 추가되지 않게 중복 확인을 해준다.
function guestAdd(userId){
	var dupltest = true;
	var tmd = $('#talkmakeDiv').text().split(',');
	for(var key in tmd){
		if(tmd[key] == userId){
			dupltest = false;
		}
	}
	if(dupltest){
		if($('#talkmakeDiv').text() == ''){
			$('#talkmakeDiv').append(userId);
		} else{
			$('#talkmakeDiv').append(","+userId);
		}
	}
}

// 초대할 사람제외(뒤부터 제외시킴)
function userOut(){
	var tmd = $('#talkmakeDiv').text();
	
	if(tmd.length != '0'){
		var tmdList = tmd.split(',');
		if(tmdList.length=='1'){ // 회원하나 남았을 때는 비워줌
			$('#talkmakeDiv').text('');
		} else{	// 여러 명 남았을 때는 처음부터 마지막 ','위치 전까지
			var lastdot = tmd.lastIndexOf(',');
			$('#talkmakeDiv').text(tmd.substring(0, lastdot));
		}
	}
}

// 접속유저 목록을 가져옴(웹소켓에서 로그인된 회원정보 목록을 매개변수로함)
function userInfo(userArr){
	$.ajax({
		url: 'connectUser',
		data: {
			userArr:userArr
		},
		async:false,
		success:function(data){
			$('#userList').html('');
			for(var key in data){
				var $div1 = $('<div class="userInfo">');
				var $div2 = $('<div class="userline">');
				$div2.text(data[key].userId);
				// 클릭 시 초대목록에 추가
				$div2.attr('onclick', 'guestAdd("'+data[key].userId+'")');
				var $div3 = $('<div class="userConnect">');
				// 접속 정보
				if(data[key].connect == 't'){ // 회원접속 중 일시
					$div3.text('●');
					$div3.css('color', 'lime');
				} else if(data[key].connect == 'f') { // 회원비접속 중 일시
					// 로그아웃한 기준으로 경과시간
					$div3.text(data[key].userConnect);
				}
				$div1.append($div2);
				$div1.append($div3);
				$('#userList').append($div1);
			}
		}
	});
}