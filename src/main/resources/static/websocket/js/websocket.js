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
		if(typeof talkInfo =='function'){
			talkInfo();
		}
		if(typeof selectTalks =='function'){
			selectTalks();
		}
	}
}

function onError(evt) {

}

$(document).ready(function(){
	if(webmessage == ''){
		send_message();
	} else{
		send_message(webmessage);
	}
});
