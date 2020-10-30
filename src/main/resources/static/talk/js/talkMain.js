/**
 * 톡메인화면
 */
$(function() {
	talkInfo();
});
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
function talkView(tsnum){
	location.href="talkView?tsnum=" + tsnum;
}
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
function talkmake(){
	var tmd = $('#talkmakeDiv').text();
	if(tmd.length=='0'){
		alert('참가자를 선택하세요');
	} else{
		location.href="talkmake?tmd="+tmd;
	}
}
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