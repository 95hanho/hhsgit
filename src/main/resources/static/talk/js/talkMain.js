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
			console.log(data);
			$('#talkDiv').html('');
			for ( var key in data) {
				var $div = $('<div class="talkSpaces">');
				var partyNum = data[key].participants.split(',');
				$div.text(data[key].participants+'('+partyNum.length+')');
				$div.attr('ondblclick', 'talkView(' + data[key].tsnum + ');');
				var $button = $('<button>');
				$button.attr('onclick', 'deletets(' + data[key].tsnum + ',"'+ data[key].ifone +'");');
				$button.text('X');
				$div.append($button);
				$('#talkDiv').append($div);
			}
		}
	});
}
function talkView(tsnum){
	location.href="talkView?tsnum=" + tsnum;
}
function deletets(tsnum, ifone){
	location.href="deletets?tsnum=" + tsnum + "&ifone=" + ifone;
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