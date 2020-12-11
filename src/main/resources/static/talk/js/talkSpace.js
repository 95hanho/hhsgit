/**
 * 톡방
 1. 동기적으로 새로고침해줘야할 톡방 내용들(채팅목록,참여자명단,초대가능명단)
 2. 채팅입력엔터키 입력 시 채팅생성 및 채팅추가, 쉬프트와 같이 누르면 줄바꿈
 3. 현재 톡방 나가기
 4. 새로운 유저 초대하기
 */
$(function() {
	if(tsnum != ''){ // 처음 화면만 들어올떄는 채팅방생성이 안되었으므로 null체크
 		selectTalks();
 	}
	// 초대목록 보여주기, 숨기기
	$('#inviteshowBtn').click(function(){
		if($('#inviteshowBtn').val() == '초대목록보기'){
			$('#inviteListDiv').show();
			$('#inviteshowBtn').val('초대목록숨기기');
		} else{
			$('#inviteListDiv').hide();
			$('#inviteshowBtn').val('초대목록보기');
		}
	});
	// 사진 올리고 저장하기
	$('#btn-upload').on('click', function(){
		var filetf = $('#file').val();
		if(filetf != ''){
			if(sinTalkYN == 'Y'){ // 새로 만든 채팅이면 채팅입력과 동시에 채팅방 생성
				$.ajax({
					url: 'talkmake2',
					data: {
						tmd:tmd,
						content:'파일'
					},
					async:false,
					success:function(data){
						tsnum=data;
						$('#hidTs').val(tsnum);
						sinTalkYN = 'N';
					}
				});
			} else if(sinTalkYN == 'N'){ // 만들어진 채팅이면 톡추가만 해줌
				$.ajax({
					url: 'insertTalk',
					data: {
						tsnum:tsnum,
						content:'파일'
					},
					async:false,
					success:function(data){
					}
				});
			}
			var form = new FormData(document.getElementById('uploadDiv'));
			$.ajax({
				url:"filechat",
				data: form,
				processData: false,
				contentType: false,
				async:false,
				type: 'POST',
				success: function (response){
				}
			});
			$('#talktext').val('');
			
			//file태그 초기화
			$('#file').val('');
			
			setTimeout(function(){
				selectTalks();
				send_message('talkmake:'+tmd);
				//location.reload();
			},2500);
		}
		
	});
});

function selectTalks() {
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
			var tmheight = 340 - data.length * 31.1;
			$('#talkMargin').css('height', tmheight);
			
			for(var key in data){
				var $div1 = $('<div class="talkline">');
				if(data[key].content == '파일'){
					$.ajax({
						url : 'selectImage',
						data : {
							tnum:data[key].tnum
						},
						async : false,
						success:function(data2){
							if(data[key].userId == userid){
								var $div2 = $('<div class="mytalk">');
								$div2.text(data[key].userId+' : 파일');
								var $div3 = $('<div class="mytalk-image">');
								var str = data2.fileRename;
								if(str.slice(-3, str.length) == 'png' || str.slice(-3, str.length) == 'PNG' || str.slice(-3, str.length) == 'jpg' || str.slice(-3, str.length) == 'JPG'){
									var $div3_1 = '<div>사진</div>';
									var $img3_3 = $('<img alt="하하" src="uploadfiles/'+ data2.fileRename +'">');
								} else if(tr.slice(-3, str.length) == 'txt'){
									var $div3_1 = '<div>메모</div>';
									var $img3_3 = $('<img alt="하하" src="talk/images/notepad.png">');
								}
								var $div3_2 = $('<div>');
								$img3_3.attr('onclick','fn_downfile("'+ data2.fileRename +'");');
								$div3_2.append($img3_3);
								$div3.append($div3_1).append($div3_2);
							} else{
								var $div2 = $('<div class="otherstalk">');
								$div2.text(data[key].userId+' : 파일');
								var $div3 = $('<div class="otherstalk-image">');
								var str = data2.fileRename;
								if(str.slice(-3, str.length) == 'png' || str.slice(-3, str.length) == 'PNG' || str.slice(-3, str.length) == 'jpg' || str.slice(-3, str.length) == 'JPG'){
									var $div3_1 = '<div>사진</div>';
									var $img3_3 = $('<img alt="하하" src="uploadfiles/'+ data2.fileRename +'">');
								} else if(tr.slice(-3, str.length) == 'txt'){
									var $div3_1 = '<div>메모</div>';
									var $img3_3 = $('<img alt="하하" src="talk/images/notepad.png">');
								}
								var $div3_2 = $('<div>');
								$img3_3.attr('onclick','fn_downfile("'+ data2.fileRename +'");');
								$div3_2.append($img3_3);
								$div3.append($div3_1).append($div3_2);
							}
							$div1.append($div2);
							$div1.append($div3);
						}
					});
				} else{
					if(data[key].userId == userid){
						var $div2 = $('<div class="mytalk">');
						$div2.text(data[key].userId+' : '+data[key].content);
					} else{
						var $div2 = $('<div class="otherstalk">');
						$div2.text(data[key].userId+' : '+data[key].content);
					}
					$div1.append($div2);
				}
				
				
				$('#talks').append($div1);
				$('#talks').scrollTop($('#talks')[0].scrollHeight);
			}
			
		}
	});
	// 참여목록을 가져오고, 1:1채팅이면 새로 초대가 불가능, 단체 채팅은 가능
	$.ajax({
		url : 'selectParticipant',
		data : {
			tsnum:tsnum
		},
		async:false,
		success: function(data){
			$('#talkHeader').text(data.participants);
			tmd = data.participants;
			if(data.ifone == 'Y'){
				$('#inviteshowBtn').hide();
			} else if(data.ifone == 'N'){
				$('#inviteshowBtn').show();
			}
		}
	});
	// 추가 초대가능한 목록을 보여줌
	$.ajax({
		url : 'selectIUlist',
		data : {
			tmd:tmd
		},
		async:false,
		success: function(data){
			$('#invitelineDiv').html('');
			for(var key in data){
				var $div = $('<div class="inviteline">');
				var $label = $('<label>');
				var $button = $('<button>');
				$label.text(data[key].userId);
				$button.text('+');
				$button.attr('onclick','inviteUser("'+ data[key].userId +'");');
				$div.append($label);
				$div.append($button);
				$('#invitelineDiv').append($div);
			}
		}	
	});
}
// 채팅입력엔터키 입력 시, 쉬프트와 같이 누르면 줄바꿈
function enterkey(){
	if(window.event.keyCode == 13){
		if(!window.event.shiftKey){
			var content = $('#talktext').val();
			content = content.replace('\r\n','<br>');
			if(sinTalkYN == 'Y'){ // 새로 만든 채팅이면 채팅입력과 동시에 채팅방 생성
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
						selectTalks();
						send_message('talkmake:'+tmd);
						sinTalkYN = 'N';
					}
				});
			} else if(sinTalkYN == 'N'){ // 만들어진 채팅이면 톡추가만 해줌
				$.ajax({
					url: 'insertTalk',
					data: {
						tsnum:tsnum,
						content:content
					},
					async:false,
					success:function(data){
						$('#talktext').val('');
						selectTalks();
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
	$.ajax({
		url: 'inviteUser',
		data: {
			tsnum:tsnum,
			userId:userId,
			tmd:tmd
		},
		async:false,
		success:function(data){
			selectTalks();
			send_message('talkmake:'+tmd);
		}
	});
}

function fn_downfile(document_nm){
	location.href = "/document/fileDownload.do?document_nm=" + document_nm;
}
