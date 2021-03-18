/**
 * 톡방
 1. 동기적으로 새로고침해줘야할 톡방 내용들(채팅목록,참여자명단,초대가능명단)
 2. 채팅입력엔터키 입력 시 채팅생성 및 채팅추가, 쉬프트와 같이 누르면 줄바꿈
 3. 현재 톡방 나가기
 4. 새로운 유저 초대하기
 */
$(function() {
	if(tsnum != ''){ // 처음 화면만 들어올떄는 채팅방생성이 안되었으므로 null체크
		if(sinTalkYN == 'N'){ // 만들어진 톡이면 버튼들 활성화
			$('#inviteshowBtn').show(); // 초대목록보기버튼
			$('#talkchatDiv>button').show(); // 톡방 나가기버튼
			$('#transBt').show(); // 파파고 번역버튼
		}
 		selectTalks(1); // 톡목록을 가져옴
 		// 나간 채팅방이면 톡방 인원 조회가 되지않아 나간채팅방으로 표시
 		var tHtext = $('#talkHeader').text();
 		if(tHtext == ''){
 			$('#talkHeader').text('나간채팅방입니다.')
 		}
 	}
	// 초대목록 보여주기, 숨기기
	$('#inviteshowBtn').click(function(){
		if($('#inviteshowBtn').val() == '초대목록보기'){
			$('#inviteListDiv').css('visibility','visible');
			$('#inviteshowBtn').val('초대목록숨기기');
		} else{
			$('#inviteListDiv').css('visibility','hidden');
			$('#inviteshowBtn').val('초대목록보기');
		}
	});
	// 사진 올리고 저장하기
	$('#btn-upload').on('click', function(){ // 파일전송을 했을 때
		var filetf = $('#file').val(); // 파일input
		if(filetf != ''){ // 파일이 있으면
			if(sinTalkYN == 'Y'){ // 새로 만든 채팅이면
				// 채팅방을 추가하고 첫 톡을 추가해줌
				$.ajax({
					url: 'talkmake2',
					data: {
						tmd:tmd,
						content:'파일'
					},
					async:false,
					success:function(data){
						tsnum=data; // tsnum 업데이트
						$('#hidTs').val(tsnum); // form데이터에 tsnum을 추가
						sinTalkYN = 'N'; // 만들어진 톡으로 변경
					}
				});
			}
			// 파일정보 multipart/form-data를 변수에 담아 서버에 전송
			var form = new FormData(document.getElementById('uploadDiv'));
			$.ajax({
				url:"filechat",
				data: form,
				processData: false,
				contentType: false,
				async:false,
				type: 'POST'
			});
			$('#talktext').val(''); // 채팅 대화 초기화
			$('#file').val(''); //file태그 초기화
			
			// 2.5초의 파일전송 시간을 둠
			setTimeout(function(){
				send_message('talkmake:'+tmd); // 톡방 인원에게 웹소켓메시지
			},1000);
			setTimeout(function(){
				selectTalks(1); // 톡 재조회
			},1500);
			
		}
		
	});
});

function selectTalks(trans) {
	// 채팅목록을 가져옴
	$.ajax({
		url : 'selectTalks',
		data : {
			tsnum:tsnum
		},
		async : false,
		success : function(data) {
			$('#talks').html('');
			var $div = $('<div id="talkMargin">');
			$('#talks').append($div);
			// 일단 채팅의 갯수만큼 마진을 없애줘서 아래부터 채팅이 채워질 수 있게 해줌
			var tmheight = 340 - data.length * 5;
			
			// 톡방에 회원이 들어와서 읽은 기록을 업데이트할 경우 톡방의 타회원들에게 톡방을 새로고침하게 메시지를 보냄
			if(data[0].sendBoolean != null){
				setTimeout(function(){
					send_message('talkmake:'+tmd); // 톡방 인원에게 웹소켓메시지
				},1000);
			}
			
			for(var key in data){
				var $div1 = $('<div class="talkline '+data[key].tnum+'">');
				$div1.attr('onmouseover', 'langover('+data[key].tnum+');'); // 톡에 마우스 올리면 무슨 언어인지
				$div1.attr('onmouseout', 'langout('+data[key].tnum+');'); // 톡에 마우스 내리면 사라짐
				$('#talks').append($div1);
				
				var $div2 = $('<div>');
				$div2.text(data[key].userId+' : '+data[key].content);
				$div1.append($div2);
				
				var $divImg = $('<div>');
				
				// 출입정보를 나타내는 채팅일 시
				if((data[key].content == '님이 나갔습니다.') || (data[key].content == '님이 들어왔습니다.')) {
					$div2.addClass('entertalk');
				} else { // 출입 정보가 아닌 채팅일 시
					var $label = $('<label>');
					if(data[key].talkRead != '0'){
						$label.text(data[key].talkRead);
					}
					$div1.append($label);
					
					// 톡내용이 파일일 시
					if(data[key].content == '사진' || data[key].content == '텍스트') {
						$.ajax({
							url : 'selectImage',
							data : {
								tnum:data[key].tnum
							},
							async : false,
							success:function(data2){
								var str = data2.fileRename;
								var $img = $('<img>');
								$img.attr('alt','하하')
								$img.attr('onclick','fn_downfile("'+ str +'");'); // 사진클릭 시 다운로드
								var $divDown = $('<div>');
								// 사진
								if(str.slice(-3, str.length) == 'png' || str.slice(-3, str.length) == 'PNG' || str.slice(-3, str.length) == 'jpg' || str.slice(-3, str.length) == 'JPG' || str.slice(-4, str.length) == 'jpeg'){
									$img.attr('src','uploadfiles/'+ str);
								// 텍스트
								} else if(str.slice(-3, str.length) == 'txt'){
									$img.attr('src','talk/images/notepad.png');
								}
								$divDown.append($img);
								$divImg.append($divDown);
								$div1.append($divImg);
							}
						});
					} else { // 톡내용이 파일아닐 시
						var langs = langsCheck(data[key].content); // 언어종류를 가져옴
						var $divLangs = $('<div class="langs '+data[key].tnum+'">'); // 언어 정보를 알려줌 div
						
						if(langs == 'ko'){ // 한국어 시
							$divLangs.text('한국어');
						} else if(langs == 'en'){ // 영어 시
							$divLangs.text('영어'); 
						} else { // 기타
							$divLangs.text('알수없음');
						}
						$div1.append($divLangs);
						
						// trans가 '2'면 한국어와 영어를 파파고 번역한 문장으로 변경
						if(trans == 2 && (langs == 'ko' || langs == 'en')){
							if(langs == 'ko'){
								$divLangs.text('영어');
							} else if(langs == 'en'){
								$divLangs.text('한국어');
							}
							// 언어종류와 내용을 보내서 반환 후 표시
							$.ajax({
								url: 'papagotrans',
								dataType: 'json',
								data: {
									content:data[key].content,
									langs:langs
								},
								async: false,
								success: function(re){
									$div2.text(data[key].userId+' : '+re.message.result.translatedText);
								}
							});
						}
					}
					if(data[key].userId == userid) {
						$div2.addClass('mytalk');
						$label.addClass('mytalkLabel');
						$divImg.addClass('mytalk-image');
					} else {
						$div2.addClass('otherstalk');
						$label.addClass('otherLabel');
						$divImg.addClass('otherstalk-image');
					}
				}
				tmheight = tmheight - $div1.height();
			}
			$div.css('height', tmheight); 
			$('#talks').scrollTop($('#talks')[0].scrollHeight); // 스크롤이 가장 아래에서 로드됨
		}
	});
	// 참여목록을 가져오고, 1:1채팅이면 새로운 단체톡을 만듬, 단체 채팅은 초대 가능
	$.ajax({
		url : 'selectParticipant',
		data : {
			tsnum:tsnum
		},
		async:false,
		success: function(data){
			$('#talkHeader').text(data.participants); // 채팅참여자를 가져옴
			tmd = data.participants; // tmd변수에 담음
			if(data.ifone == 'Y'){ // 1:1톡일 때
				ifone = 'Y';
			} else if(data.ifone == 'N'){ // 단체 톡일 때
				ifone = 'N';
			}
		}
	});
}

// 파파고 번역 버튼
function transBtn()	{
	if($('#transBt').text() == '파파고 번역(한->영, 영->한)'){
		selectTalks(2);
		$('#transBt').text('번역중(취소 시 클릭)');
	} else {
		selectTalks(1);
		$('#transBt').text('파파고 번역(한->영, 영->한)');
	}
	
}

// 현재 감지된 언어 종류를 보여줌
function langover(a) {
	var ls = '.langs.'+a;
	$(ls).show();
	
}

// 현재 감지된 언어 종류를 가려줌
function langout(a) {
	var ls = '.langs.'+a;
	$(ls).hide();
}	

// text매개변수의 언어종류를 알려줌 ex) ko(한국어), en(영어)
function langsCheck(text){
	var result2 = '';
	$.ajax({
		url: 'langsCheck',
		dataType: 'json',
		data:{
			text:text
		},
		async:false,
		success: function(data){
			result2 = data.langCode;
		}
	});
	if(result2 != ''){
		return result2;
	}
}
// 채팅입력엔터키 입력 시, 쉬프트와 같이 누르면 줄바꿈
function enterkey(){
	if(window.event.keyCode == 13){
		if(!window.event.shiftKey){
			var content = $('#talktext').val();
			content = content.replace('\r\n','<br>');
			if(sinTalkYN == 'Y'){ // 새로 만든 채팅이면 채팅입력과 동시에 채팅방 생성
				$('#inviteshowBtn').show(); // 초대목록버튼 활성화
				$('#talkchatDiv>button').show(); // 채팅방 나가기 버튼 활성화
				$('#backDiv>button:nth-child(1)').show(); // 채팅목록가기 버튼 활성화
				$.ajax({
					url: 'talkmake2',
					data: {
						tmd:tmd,
						content:content
					},
					async:false,
					success:function(data){
						tsnum=data;
						$('#talktext').val('');
						selectTalks(1);
						send_message('talkmake:'+tmd);
						sinTalkYN = 'N';
					}
				});
			} else if(sinTalkYN == 'N'){ // 만들어진 채팅이면 톡추가만 해줌
				$('#transBt').text('파파고 번역(한->영, 영->한)'); // 번역 중에 대화를 칠 경우 원래 값을 초기화
				$.ajax({
					url: 'insertTalk',
					data: {
						tsnum:tsnum,
						content:content
					},
					async:false,
					success:function(data){
						$('#talktext').val('');
						selectTalks(1);
						send_message('talkmake:'+tmd);
					}
				});
			}
			return false; // 기존 엔터키(줄바꿈) 막기
		}
	}
}
// 현재 톡방 나가기
function exitTalk(){
	location.href="exitTalk?tsnum="+tsnum;
}
// 새로운 유저 초대하기
function inviteUser(userId){
	if(ifone == 'N'){
		$.ajax({
			url: 'inviteUser',
			data: {
				tsnum:tsnum,
				userId:userId,
				tmd:tmd
			},
			async:false,
			success:function(data){
				selectTalks(1);
				send_message('talkmake:'+tmd);
			}
		});
	} else if(ifone == 'Y'){
		tmd = tmd + ',' + userId;
		location.href='talkmake?tmd='+tmd;
	}
	
}

// 다운로드 URL
function fn_downfile(document_nm){
	location.href = "/document/fileDownload.do?document_nm=" + document_nm;
}

// 추가 초대가능한 목록을 보여줌
function userInfo2(userArr){
	$.ajax({
		url : 'selectIUlist',
		data : {
			tmd:tmd,
			userArr:userArr
		},
		async:false,
		success: function(data){
			$('#invitelineDiv').html('');
			for(var key in data){
				var $div = $('<div class="inviteline">');
				
				var $div2 = $('<div>');
				$div2.text(data[key].userId);
				
				var $div3 = $('<div>');
				// 접속 정보
				if(data[key].connect == 't'){ // 회원접속 중 일시
					$div3.text('●');
					$div3.css('color', 'lime');
				} else if(data[key].connect == 'f') { // 회원비접속 중 일시
					// 로그아웃한 기준으로 경과시간
					$div3.text(data[key].userConnect);
				}
				
				var $button = $('<button>');
				$button.text('+');
				// 클릭 시 초대
				$button.attr('onclick','inviteUser("'+ data[key].userId +'");');
				
				$div.append($div2);
				$div.append($div3);
				$div.append($button);
				$('#invitelineDiv').append($div);
			}
		}	
	});
}
