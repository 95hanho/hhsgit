/**
 * 웹소켓 실행
 */
var wsUri = "ws://202.30.249.27:9438/websocket";
var websocket = null;

function send_message(data) {

    websocket = new WebSocket(wsUri);

    websocket.onopen = function(evt) {

        onOpen(evt, data);

    };

    websocket.onmessage = function(evt) {
    	
    		onMessage(evt);

    };

    websocket.onerror = function(evt) {

        onError(evt);

    };
}

function onOpen(evt, data) {
	if(data != null){
		websocket.send(data); // data(즉 보낼메시지)가 있으면 서버에 data를 보냄
	}
}

function onMessage(evt) {
	if(evt.data == 'reTalkInfo'){
		// talkInfo()가 존재할 시 실행, selectTalks()가 존재할 시 실행
		if(typeof talkInfo =='function'){
			talkInfo(); // 채팅방목록 불러오기
		}
		if(typeof selectTalks =='function'){
			selectTalks(); // 톡내용 불러오기
		}
	}
	if(evt.data.split(':')[0] == 'userList'){
		var userList = evt.data.split(':')[1];
		// [유저목록]이므로 []을 없애줌
		var userArr = userList.substring(1, userList.length-1);
		if(typeof userInfo == 'function'){
			userInfo(userArr); // 접속회원목록 불러오기
		}
		if(typeof userInfo2 == 'function'){
			userInfo2(userArr); // 초대가능목록 불러오기
		}
	}
}

function onError(evt) {

}


$(document).ready(function(){
	// 서버로부터 웹소켓메시지가 왔으면 서버에 웹메시지보냄, 아니면 그냥 실행
	if(webmessage == ''){
		send_message();
	} else{
		send_message(webmessage);
	}
});
