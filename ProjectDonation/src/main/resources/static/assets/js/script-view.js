$(document).ready(function(){
	
	
	/*dialog danh sach nha hao tam moi*/	
	$('#xem_nhahaotam').on('click', function(){
		loadDonor();
		$('body').css('overflow', 'hidden');
	});
	   
	/*
	$( window ).scroll(function() {
		const top = window.pageXOffset ? window.pageXOffset : document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop;
		const col_right = $('.col-right');
		if (top > 550) {
			col_right.addClass('col-right-fixed');
		    //  head.style.position = "fixed";
		    //  head.style.height = "50px"
		    //  head.style.top = "0px"
		} else {
			/*
			if (head.getAttribute("style"))
				head.removeAttribute("style")
		
		}
	});
	*/
});

/*open dialog tutorius*/
function openDialogTutorial(){
	/*dialog hướng dẫn quyên góp*/
	
	const username = $('#d_username').html();
	console.log(username);
	if ($('#d_username').html() !== undefined){			
		showHelpDonation();
			
	}else{		
		$('.dialog-tutorial').append(dialogTutorialLogin());
	}
		
	$('body').css('overflow', 'hidden');
	
}

/*ajax show help donation*/
function showHelpDonation(){
	const username = $('#d_username').html();
	const campaignId = $('.data-campaign').attr('data-id');
	var objectData = {};
	objectData['action'] = 'help';
	objectData['campaignId'] = campaignId;
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: setPathURL()+"/Donation/actionHelp",
		data: JSON.stringify(objectData),
		dataType: "json",
		cache: false,
		timeout: 60000,
		success: function(data){
			//console.log(data);
			
			$('.dialog-tutorial').append(dialogTutorial(data['donation'], username));
			/*
			var dialog = dialogListDonor(data['donations']);
			$('.dialog-list-donor').append(dialog);
			set_scroll_dialog_donor();
				*/		
		},
		error: function(e){
			console.log(e);
		}
	});
}

/*ajax get load dialog donor*/
function loadDonor(){
	const campaignId = $('.data-campaign').attr('data-id');
	
	var objectData = {};
	objectData['action'] = 'load';
	objectData['campaignId'] = campaignId;
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: setPathURL()+"/Donation/actionLoadDonor",
		data: JSON.stringify(objectData),
		dataType: "json",
		cache: false,
		timeout: 60000,
		success: function(data){
			//console.log(data);
			var dialog = dialogListDonor(data['donations']);
			$('.dialog-list-donor').append(dialog);
			set_scroll_dialog_donor();
						
		},
		error: function(e){
			console.log(e);
		}
	});
}

/*load more donor*/
function set_scroll_dialog_donor(){
	
	var check_scroll = false;
	$("#scrollableDiv").scroll(function(){
		if($(this).scrollTop() + $(this).innerHeight() >= $(this)[0].scrollHeight && !check_scroll) {
			//load data
			//$(this).find('.infinite-scroll-component').append('<div id="loader"></div>');
			
			check_scroll = true;
		    $(this).find('.infinite-scroll-component').append('<div class="div-loader flex items-center justify-between text-sm text-gray-700 font-semibold py-2 border-b border-gray-100 last:border-0">'+
		    				'<div id="loader"></div>'+
									'</div>');
		    const campaignId = $('.data-campaign').attr('data-id');
			const currentItem = $('.d-items').length;
			console.log(currentItem);
			var objectData = {};
			objectData['action'] = 'loadMore';
			objectData['campaignId'] = campaignId;
			objectData['currentItem'] = currentItem;
			
			$.ajax({
				type: "POST",
				contentType: "application/json",
				url: setPathURL()+"/Donation/actionLoadDonor",
				data: JSON.stringify(objectData),
				dataType: "json",
				cache: false,
				timeout: 60000,
				success: function(data){
					
					$('#scrollableDiv').find('.div-loader').remove();
					var html = "";
					if(data['donations'] != null){
						$.each(data['donations'], function(i, value){
							html += '<div class="d-items flex items-center justify-between text-sm text-gray-700 font-semibold py-2 border-b border-gray-100 last:border-0">'+
										'<div class="w-12 mr-1 flex items-center ">'+
											'<div class="w-9 h-9 text-sm font-bold text-gray-500 bg-gray-300 rounded-full flex justify-center items-center">'+value.firstLetterName+'</div>'+
										'</div>'+
										'<div class="flex-1 text-left">'+
											'<div class="pt-1 mb-1">'+value.fullName+'</div>'+
											'<div class="text-xs text-gray-400">'+value.phone+'</div>'+
										'</div>'+
										'<div class="flex item-end justify-end">'+formatInteger(value.amount,'de-DE')+'đ</div>'+
									'</div>';
						});
					}
					$("#scrollableDiv").find('.infinite-scroll-component').append(html);
					
					check_scroll = false;	
						
				},
				error: function(e){
					console.log(e);
					check_scroll = false;
				}
			});
		    
		}
		    
	});
}

/*closr dialog */
function close_dialog_tutorial(){	
	$('.dialog-content').remove();
	$('body').css('overflow', 'scroll');
}

/*show list donnor*/
function dialogListDonor(values){
	var el_Dialog = '<div class="dialog-content fixed inset-0 flex flex-row items-end justify-center pt-3 text-gray-800 bg-transparent md:items-center modal md:pp-9 max-h-full ">'+
				'<div class="relative flex flex-col w-full max-w-full max-h-full   bg-white shadow-xl modalFadeUp modal-cluster md:rounded-xl  rounded-t-xl md:h-auto  md:max-w-3xl ">'+
					'<button onclick="close_dialog_tutorial()" class="z-20 absolute flex items-center justify-center w-12 h-12 top-0 mt-1 left-0 right-auto md:left-auto md:right-3 rounded-full hover:bg-gray-50 hover:bg-opacity-20 cursor-pointer border-none ">'+
						'<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" class="jsx-ec3d9023a8eed023  w-6 h-6 block">'+
							'<path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd" class="jsx-ec3d9023a8eed023"></path>'+
						'</svg>'+
					'</button>'+
					'<header class="pt-4 pbb-4 pl-12 pr-12 font-semibold flex items-center justify-center border-b border-gray-200">'+
						'<h3 class="overflow-hidden text-ellipsis whitespace-nowrap text-l">Danh sách các nhà hảo tâm</h3>'+
					'</header>'+
					'<div class="modal-body  h-full overflow-auto p-0 overflow-hidden bg-white">'+
						'<div id="scrollableDiv" class="px-4 md:px-6 overflow-y-auto h-[80vh] md:h-[70vh]">'+
							'<div class="infinite-scroll-component__outerdiv">'+
								'<div class="infinite-scroll-component " style="height: auto; overflow: auto;">';
	
	$.each(values, function(i, value){
		el_Dialog += '<div class="flex items-center justify-between text-sm text-gray-700 font-semibold py-2 border-b border-gray-100 last:border-0">'+
							'<div class="w-12 mr-1 flex items-center ">'+
							'<div class="w-9 h-9 text-sm font-bold text-gray-500 bg-gray-300 rounded-full flex justify-center items-center">'+value.firstLetterName+'</div>'+
						'</div>'+
						'<div class="flex-1 text-left">'+
							'<div class="pt-1 mb-1">'+value.fullName+'</div>'+
							'<div class="text-xs text-gray-400">'+value.phone+'</div>'+
						'</div>'+
						'<div class="flex item-end justify-end">'+formatInteger(value.amount,'de-DE')+'đ</div>'+
					'</div>';
	});
	
	el_Dialog += '</div>'+
					'</div>'+
					'</div>'+
				'</div>'+
				'<footer class="modal-footer flex border-t border-gray-200 py-2 md:py-4 px-5 items-center justify-end flex">'+
					'<button onclick="close_dialog_tutorial()" type="button" class="inline-block px-5 py-2 text-sm font-bold text-center text-white uppercase transition-all bg-pink-700 rounded-md cursor-pointer btn text-opacity-90 hover:bg-pink-800">Đóng</button>'+
				'</footer>'+
				'</div>'+
				'<div class="fixed inset-0 overflow-y-auto bg-black bg-opacity-50 modal-overlay"></div>'+
				'</div>';
	
	return el_Dialog;
}

/*dialog help donation*/
function dialogTutorial(value, user_name){
	var el_Dialog = '<div class="dialog-content fixed inset-0 flex flex-row items-end justify-center pt-3 text-gray-800 bg-transparent md:items-center modal md:pp-9 max-h-full ">'+
			'<div class="relative flex flex-col w-full max-w-full max-h-full bg-white shadow-xl modalFadeUp modal-cluster md:rounded-xl  rounded-t-xl md:h-auto  md:max-w-3x">'+
				'<button id="btn_close_dialog_tutorial" onclick="close_dialog_tutorial()" class="z-20 absolute flex items-center justify-center w-12 h-12 top-0 mt-1 left-0 right-auto md:left-auto md:right-3 rounded-full hover:bg-gray-50 hover:bg-opacity-20 cursor-pointer border-none ">'+
					'<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" class="jsx-ec3d9023a8eed023  w-6 h-6 block">'+
						'<path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd" class="jsx-ec3d9023a8eed023"></path>'+
					'</svg>'+
				'</button>'+
				'<div class="modal-body  h-full overflow-auto p-0 bg-white rounded">'+
					'<div class="w-100 pt-6">'+
						'<h5 class="text-center text-1.5xl font-bold">Quyên góp trao yêu thương</h5>'+
					'</div>'+
					'<div class="pl-8 pr-4">'+
						'<div class="max-w-3xl mx-auto ">'+
							'<div class="flex justify-center flex-nowrap">'+
								'<div class="flex-none w-72">'+    								
								'</div>'+
								'<div class="flex-auto pt-4 pl-10 ">'+
									'<div class="htu-list">'+
										'<div class="relative pl-12 cursor-pointer list-item">'+
											'<div class=""></div>'+
											'<div class="pt-1 transition-colors  text-md text-gray-700 font-medium">Quyên góp được chuyển về tài khoản sau:</div>'+
										'</div>'+
										'<div class="relative pl-12 cursor-pointer list-item">'+
											'<div class="absolute top-0 left-0 w-8 h-8  transition-colors text-md font-semibold rounded-full  flex items-center justify-center bg-gray-200 text-gray-700">1</div>'+
											'<div class="pt-1 transition-colors  text-md text-gray-700 font-medium">Số TK: '+value.bank_account_number+'</div>'+
										'</div>'+
										'<div class="relative pl-12 cursor-pointer list-item">'+
											'<div class="absolute top-0 left-0 w-8 h-8  transition-colors text-md font-semibold rounded-full  flex items-center justify-center bg-gray-200 text-gray-700">2</div>'+
											'<div class="pt-1 transition-colors  text-md text-gray-700 font-medium">Tên tài Khoản: '+value.bank_account_name+' - Tại NH '+value.bank_name+'</div>'+
										'</div>'+
										'<div class="relative pl-12 cursor-pointer list-item">'+
											'<div class="absolute top-0 left-0 w-8 h-8  transition-colors text-md font-semibold rounded-full  flex items-center justify-center bg-gray-200 text-gray-700">3</div>'+
											'<div class="pt-1 transition-colors  text-md text-gray-700 font-medium">Nội dung chuyển khoản, Nhà Hảo Tâm cần ghi rõ : '+user_name+'_TYT_'+value.campaignId+'</div>'+
										'</div>'+
										'<div class="relative pl-12 cursor-pointer list-item">'+
											'<div class="absolute top-0 left-0 w-8 h-8  transition-colors text-md font-semibold rounded-full  flex items-center justify-center bg-gray-200 text-gray-700">4</div>'+
											'<div class="pt-1 transition-colors  text-md text-gray-700 font-medium">Mọi sự đóng góp của Nhà Hảo Tâm, sẽ được chúng tôi cập nhật lên website của chương trình được quyên góp.</div>'+
										'</div>'+
										
									'</div>'+
								'</div>'+
							'</div>'+
						'</div>'+
					'</div>'+
				'</div>'+
			'</div>'+
			'<div class="jsx-ec3d9023a8eed023 fixed inset-0 overflow-y-auto bg-black bg-opacity-50 modal-overlay"></div>'+	
		'</div>';
	
	return el_Dialog;
}

/*dialog alert login*/
function dialogTutorialLogin(){
	var el_Dialog = '<div class="dialog-content fixed inset-0 flex flex-row items-end justify-center pt-3 text-gray-800 bg-transparent md:items-center modal md:pp-9 max-h-full ">'+
			'<div class="relative flex flex-col w-full max-w-full max-h-full bg-white shadow-xl modalFadeUp modal-cluster md:rounded-xl  rounded-t-xl md:h-auto  md:max-w-3x">'+
				'<button id="btn_close_dialog_tutorial" onclick="close_dialog_tutorial()" class="z-20 absolute flex items-center justify-center w-12 h-12 top-0 mt-1 left-0 right-auto md:left-auto md:right-3 rounded-full hover:bg-gray-50 hover:bg-opacity-20 cursor-pointer border-none ">'+
					'<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" class="jsx-ec3d9023a8eed023  w-6 h-6 block">'+
						'<path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd" class="jsx-ec3d9023a8eed023"></path>'+
					'</svg>'+
				'</button>'+
				'<div class="modal-body  h-full overflow-auto p-0 bg-white rounded">'+
					'<div class="w-100 pt-6">'+
						'<h5 class="text-center text-1.5xl font-bold">Quyên góp trao yêu thương</h5>'+
					'</div>'+
					'<div class="pl-8 pr-4">'+
						'<div class="max-w-3xl mx-auto ">'+
							'<div class="flex justify-center flex-nowrap">'+
								'<div class="flex-none w-72">'+    								
								'</div>'+
								'<div class="flex-auto pt-4 pl-10 ">'+
									'<div class="htu-list">'+
										'<div class="relative pl-12 cursor-pointer list-item">'+
											'<div class=""></div>'+
											'<div class="pt-1 transition-colors  text-md text-gray-700 font-medium">- Bạn cần <strong><a href="'+setPathURL()+'/Donation/login">đăng nhập</a></strong> để chúng tôi có thể ghi nhập mọi đóng góp từ bạn!</div>'+
										'</div>'+
										'<div class="relative pl-12 cursor-pointer list-item">'+
											'<div class="></div>'+
											'<div class="pt-1 transition-colors  text-md text-gray-700 font-medium">- Nếu bạn chưa có tài khoản. Hãy đăng ký <strong><a href="'+setPathURL()+'/Donation/register">tại đây</a></strong></div>'+
										'</div>'+										
									'</div>'+
								'</div>'+
							'</div>'+
						'</div>'+
					'</div>'+
				'</div>'+
			'</div>'+
			'<div class="jsx-ec3d9023a8eed023 fixed inset-0 overflow-y-auto bg-black bg-opacity-50 modal-overlay"></div>'+	
		'</div>';
	
	return el_Dialog;
}