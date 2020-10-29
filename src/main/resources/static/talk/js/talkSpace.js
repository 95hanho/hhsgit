/**
 * 톡방
 */
 $(function() {
	selectTalks();
});
function selectTalks() {
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
				if(data[key].userId == userid){
					var $div2 = $('<div class="mytalk">');
					$div2.text(data[key].userId+' : '+data[key].content);
				} else{
					var $div2 = $('<div class="otherstalk">');
					$div2.text(data[key].userId+' : '+data[key].content);
				}
				$div1.append($div2);
				$('#talks').append($div1);
				$('#talks').scrollTop($('#talks')[0].scrollHeight);
			}
			
		}
	});
	$.ajax({
		url : 'selectParticipant',
		data : {
			tsnum:tsnum
		},
		async:false,
		success: function(data){
			$('#talkHeader').text(data);
		}
	});
}
function enterkey(){
	if(window.event.keyCode == 13){
		if(!window.event.shiftKey){
			var content = $('#talktext').val();
			console.log(content);
			content = content.replace('\r\n','<br>');
			console.log(content);
			if(sinTalkYN == 'Y'){
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
			} else if(sinTalkYN == 'N'){
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
			return false;
		}
	}
}