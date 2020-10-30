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
			for ( var key in data) {
				var $div = $('<div class="talkSpaces">');
				var $label = $('<label>');
				var partyNum = data[key].participants.split(',');
				$label.text('('+partyNum.length+')'+data[key].participants);
				$label.attr('ondblclick', 'talkView(' + data[key].tsnum + ');');
				var $button = $('<button>');
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
// 채팅방 초대할 사람 추가
function guestAdd(userId){
	var dupltest = true;
	var tmd = $('#talkmakeDiv').text().split(',');
	var tmd2 = $('#talkmakeDiv').text().split(',');
	for(var key2 in tmd2){
		if(tmd2[key2] == userId){
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
	
	if(tmd.length!='0'){
		var tmdList = tmd.split(',');
		if(tmdList.length=='1'){
			$('#talkmakeDiv').text('');
		} else{
			var lastdot = tmd.lastIndexOf(',');
			$('#talkmakeDiv').text(tmd.substring(0, lastdot));
		}
	}
}