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
			$('#backDiv>button:nth-child(1)').show(); // 파파고 번역버튼
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
			} else if(sinTalkYN == 'N'){ // 만들어진 채팅이면 톡추가만 해줌
				$.ajax({
					url: 'insertTalk',
					data: {
						tsnum:tsnum,
						content:'파일'
					},
					async:false
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
				selectTalks(1); // 톡 재조회
				send_message('talkmake:'+tmd); // 톡방 인원에게 웹소켓메시지
				//location.reload();
			},2500);
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
			// 채팅의 갯수만큼 마진을 없애줘서 아래부터 채팅이 채워질 수 있게 해줌
			var tmheight = 340 - data.length * 31.1;
			$('#talkMargin').css('height', tmheight); 
			
			for(var key in data){
				var $div1 = $('<div class="talkline '+data[key].tnum+'">');
				$div1.attr('onmouseover', 'langover('+data[key].tnum+');'); // 톡에 마우스 올리면 무슨 언어인지
				$div1.attr('onmouseout', 'langout('+data[key].tnum+');'); // 톡에 마우스 내리면 사라짐
				if(data[key].content == '파일') { // 톡내용이 파일일 시
					$.ajax({
						url : 'selectImage',
						data : {
							tnum:data[key].tnum
						},
						async : false,
						success:function(data2){
							// 해당회원 톡내용 일 떄
							if(data[key].userId == userid){
								var $div2 = $('<div class="mytalk">');
								$div2.text(data[key].userId+' : 파일');
								var $div3Img = $('<div class="mytalk-image">');
								var str = data2.fileRename;
								// 사진
								if(str.slice(-3, str.length) == 'png' || str.slice(-3, str.length) == 'PNG' || str.slice(-3, str.length) == 'jpg' || str.slice(-3, str.length) == 'JPG' || str.slice(-4, str.length) == 'jpeg'){
									var $div3_1 = '<div>사진</div>';
									var $img3_3 = $('<img alt="하하" src="uploadfiles/'+ data2.fileRename +'">');
								// 텍스트
								} else if(str.slice(-3, str.length) == 'txt'){
									var $div3_1 = '<div>메모</div>';
									var $img3_3 = $('<img alt="하하" src="talk/images/notepad.png">');
								}
								var $div3_2 = $('<div>');
								$img3_3.attr('onclick','fn_downfile("'+ data2.fileRename +'");'); // 사진클릭 시 다운로드
								$div3_2.append($img3_3);
								$div3Img.append($div3_1).append($div3_2);
							// 타회원 톡내용 일 때
							} else{
								var $div2 = $('<div class="otherstalk">');
								$div2.text(data[key].userId+' : 파일');
								var $div3Img = $('<div class="otherstalk-image">');
								var str = data2.fileRename;
								// 사진
								if(str.slice(-3, str.length) == 'png' || str.slice(-3, str.length) == 'PNG' || str.slice(-3, str.length) == 'jpg' || str.slice(-3, str.length) == 'JPG'){
									var $div3_1 = '<div>사진</div>';
									var $img3_3 = $('<img alt="하하" src="uploadfiles/'+ data2.fileRename +'">');
								// 텍스트
								} else if(str.slice(-3, str.length) == 'txt'){
									var $div3_1 = '<div>메모</div>';
									var $img3_3 = $('<img alt="하하" src="talk/images/notepad.png">');
								}
								var $div3_2 = $('<div>');
								$img3_3.attr('onclick','fn_downfile("'+ data2.fileRename +'");'); // 사진클릭 시 다운로드
								$div3_2.append($img3_3);
								$div3Img.append($div3_1).append($div3_2);
							}
							$div1.append($div2);
							$div1.append($div3Img);
						}
					});
				} else{ // 톡내용이 파일이 아닐 시
					var langs = langsCheck(data[key].content); // 언어종류를 가져옴
					var $div3 = $('<div class="langs '+data[key].tnum+'">'); // 언어 정보를 알려줌 div
					$div1.append($div3);
					if(langs == 'ko'){ // 한국어 시
						$div3.text('한국어');
					} else if(langs == 'en'){ // 영어 시
						$div3.text('영어'); 
					} else { // 기타
						$div3.text('알수없음');
					}
					// 해당 회원 톡일 시
					if(data[key].userId == userid){
						var $div2 = $('<div class="mytalk '+ data[key].tnum +'">');
						$div2.text(data[key].userId+' : '+data[key].content);
					// 타 회원 톡일 시
					} else{
						var $div2 = $('<div class="otherstalk">');
						$div2.text(data[key].userId+' : '+data[key].content);
					}
					// trans가 '2'면 한국어와 영어를 파파고 번역한 문장으로 변경
					if(trans == 2 && (langs == 'ko' || langs == 'en')){
						if(langs == 'ko'){
							$div3.text('영어');
						} else if(langs == 'en'){
							$div3.text('한국어');
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
					$div1.append($div2);
				}
				$('#talks').append($div1);
				$('#talks').scrollTop($('#talks')[0].scrollHeight); // 스크롤이 가장 아래에서 로드됨
			}
			
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
				$('#transBt').text('파파고 번역(한->영, 영->한)');
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
