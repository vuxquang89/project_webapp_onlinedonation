/*
$("#f_account_info").submit(function(){
	var email = $("#msg_email");
	if($('#email').val().trim().length < 1){
		email.html("Mời nhập Email");
		email.focus();
		return false;
	}else if(!isEmail($('#email').val().trim())){
    	email.html("Email không hợp lệ.");
    	email.focus();
        return false;
    }else{
    	
    }
	
});
*/
function updatePass(){
	var pass_old = $('#pass_old'),
		pass_new = $('#pass_new'),
		confirm_pass_new = $('#confirm_pass_new');
	var msg_pass_old = $('#msg_pass_old'),
		msg_pass_new = $('#msg_pass_new'),
		msg_con_pass = $('#msg_con_pass');
	var check_pass_new = true, 
		check_pass_old = true,
		check_con_pass = true;
	
	if(pass_old.val().trim().length < 1){
		msg_pass_old.html("Mời nhập mật khẩu cũ");
		check_pass_old = false;
	}else {
		check(pass_old.val(), msg_pass_old);
		//check_pass_old = false;
	}
	
	if(pass_new.val().trim().length < 8){
		msg_pass_new.html("Mật khẩu phải có độ dài lớn hơn 7 ký tự");
		check_pass_new = false;
	}else if(pass_new.val().trim() === pass_old.val().trim()){
		msg_pass_new.html("Mật khẩu mới trùng với mật khẩu cũ");
		check_pass_new = false;
	}else{
		msg_pass_new.html("");
	}
	
	if(confirm_pass_new.val() !== pass_new.val()){
		msg_con_pass.html("Cần xác nhận chính xác mật khẩu mới");        
		check_con_pass = false;
    }else{
    	msg_con_pass.html("");
    }
	
	if(check_pass_new && check_pass_old && check_con_pass){		
		savePass(pass_new.val());
		return true;
	}else{
		return false;
	}
}

/*update password*/
function savePass(value){
	var objectData = {};
	objectData['action'] = 'update';
	objectData['value'] = value;
	objectData['username'] = $('#h_username').html();
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: setPathURL()+"/Donation/user/check",
		data: JSON.stringify(objectData),
		dataType: "json",
		cache: false,
		timeout: 60000,
		success: function(data){
			var check = data['check'];
			if(!check){
				
				showBoxMsg("Không thể cập nhật mật khẩu", 1);
			}else{
				$('#pass_old').val(""),
				$('#pass_new').val(""),
				$('#confirm_pass_new').val("");
				showBoxMsg("Cập nhật mật khẩu mới thành công", 0);
			}
		},
		error: function(e){
			console.log(e);
			showBoxMsg("Không thể cập nhật mật khẩu", 1);
		}
	});
}

/*check password old*/
function check(value, msg){
	var objectData = {};
	objectData['action'] = 'checkpass';
	objectData['value'] = value;
	objectData['username'] = $('#h_username').html();
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: setPathURL()+"/Donation/user/check",
		data: JSON.stringify(objectData),
		dataType: "json",
		cache: false,
		timeout: 60000,
		success: function(data){
			var check = data['check'];
			if(!check){
				
				msg.html("Mật khẩu không đúng");
			}else{
				msg.html("");
				
			}
		},
		error: function(e){
			
			console.log(e);
		}
	});
}

/*nhập lại mật khẩu*/
function resetInput(){
	$('#pass_old').val("");
	$('#msg_pass_old').html('');
	$('#pass_new').val("");
	$('#msg_pass_new').html('');
	$('#confirm_pass_new').val("");
	$('#msg_con_pass').html('');
}

//read url open image
function readURL_img(input, view){
    if(input.files && input.files[0]){
        var reader = new FileReader();
        reader.onload = function(e){
            view.attr('src', e.target.result);
        }

        reader.readAsDataURL(input.files[0]);
    }
}

/*get next / prev page his donation */
function getAccountDonationPageChange(){
	
	const limitItem = $('#limitShowItemHisDonation').val();
	const current = $('.pagination-body').find('.pageNumber.pactive a').text();
	const fromDate = $('#aDonation_date').val();
	var objectData = {};
	objectData['action'] = 'get';
	objectData['fromDate'] = fromDate;
	objectData['limitItem'] = limitItem;
	objectData['current'] = current;
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: setPathURL()+"/Donation/user/actionHisDonation",
		data: JSON.stringify(objectData),
		dataType: "json",
		cache: false,
		timeout: 60000,
		success: function(data){			
			var html = "";
			var donations = data['donations'];
			const totalRecords = data['totalRecords'];
			
			/*load data*/			
			$.each(donations, function(i, value){
				html += "<tr>"
					+"<td><div class='checkbox-del'>"
					+"<input class='checkbox-del-group check-del' type='checkbox' onclick='onClickItemCheckBox_Donation(this)'></div></td>"
                    +"<td style='display:none;'>"+value.donationId+"</td>"
                    +"<td style='width:700px'>"+value.campaignName+"</td>"
                    +"<td>"+value.dateTime+"</td>"
                    +"<td>"+ formatInteger(value.amount,'de-DE')+"</td>" 
                    +"<td class='td-action'>"
                    +"<a href='#' class='delete' title='Xóa' data='"+value.donationId+"' onclick='aDelGeneralDonation(this);return false;'><i class='ti-trash'></i></a>"
                    +"</td>"
                    +"</tr>;"				
			});
			$('#mTableHisDonation').find('tbody').html(html);
			
			$('.hint-text').html('Hiển thị <b>'+(limitItem*current > totalRecords ? totalRecords : limitItem*current)+'</b> trong số <b>'+totalRecords+'</b> mục');
			
		},
		error: function(e){
			console.log(e);
		}
	});
}

/*========ajax get donation=====*/
function getHisDonationOnChange(){
	const limitItem = $('#limitShowItemHisDonation').val();
	//const current = $('.pagination-body').find('.pageNumber.pactive a').text();
	const current = 1;
	const fromDate = $('#aDonation_date').val();
	var objectData = {};
	objectData['action'] = 'get';
	objectData['fromDate'] = fromDate;
	objectData['limitItem'] = limitItem;
	objectData['current'] = current;
	
	//var style = setStyleStatus(index);
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: setPathURL()+"/Donation/user/actionHisDonation",
		data: JSON.stringify(objectData),
		dataType: "json",
		cache: false,
		timeout: 60000,
		success: function(data){
			
			var html = "", htmlpag = "";
			var donations = data['donations'];
			const totalRecords = data['totalRecords'];
			
			const avgPage = totalRecords / limitItem;
			const totalPage = Math.ceil(avgPage);
			
			/*load data*/			
			$.each(donations, function(i, value){
				html += "<tr>"
					+"<td><div class='checkbox-del'>"
					+"<input class='checkbox-del-group check-del' type='checkbox' onclick='onClickItemCheckBox_Donation(this)'></div></td>"
                    +"<td style='display:none;'>"+value.donationId+"</td>"
                    +"<td style='width:700px'>"+value.campaignName+"</td>"
                    +"<td>"+value.dateTime+"</td>"
                    +"<td>"+ formatInteger(value.amount,'de-DE')+"</td>" 
                    +"<td class='td-action'>"
                    +"<a href='#' class='delete' title='Xóa' data='"+value.donationId+"' onclick='aDelGeneralDonation(this);return false;'><i class='ti-trash'></i></a>"
                    +"</td>"
                    +"</tr>;"				
			});
			$('#mTableHisDonation').find('tbody').html(html);
			
									
			/*load pagination*/
			
			htmlpag += "<li><a href='#' onclick='return false' class='prev'><<</a></li>";
			for(var i = 1; i <= totalPage; i++){				
				if(i == 1)
					htmlpag += "<li class='pageNumber pactive'><a href='#' onclick='return false'>"+i+"</a></li>";
				else
                    htmlpag += "<li class='pageNumber'><a href='#' onclick='return false'>"+i+"</a></li>";                          
			}
			
			htmlpag += "<li><a href='#' onclick='return false' class='next'>>></a></li>";
			$('.pagination-body').html(htmlpag);
			setActionHisDonationPagination();
			const cur = $('.pagination-body').find('.pageNumber.pactive a').text();
			$('.hint-text').html('Hiển thị <b>'+(limitItem*cur > totalRecords ? totalRecords : limitItem*cur)+'</b> trong số <b>'+totalRecords+'</b> mục');
			
		},
		error: function(e){
			console.log(e);
		}
	});
}

/*set action button pagination*/
function setActionHisDonationPagination(){
	/*button page number*/
	   $('.pageNumber').click(function(){
		   $('.pagination-body').find('.pageNumber.pactive').removeClass('pactive');	   
		   $(this).addClass('pactive');
		   var keySearch = $('#iSearchHisDonation').val().trim();
		   if(keySearch.length > 0){
			   getNextPageSearchHisDonation();
		   }else
			   getHisDonationPageChange();		   
	   });
	   
	   /*button next page*/
	   $('.next').click(function(){	   
	       $('.pagination-body').find('.pageNumber.pactive').next().addClass('pactive');
	       $('.pagination-body').find('.pageNumber.pactive').prev().removeClass('pactive');
	       if($('.pagination-body').find('.pageNumber').length > 0){  
	    	   
	    	   //if($('.pagination-body').attr('ul-click') == 'user'){
	    	   var keySearch = $('#iSearchHisDonation').val().trim();
	    	   if(keySearch.length > 0){
				   getNextPageSearchHisDonation();
			   }else
				   getHisDonationPageChange();
	       }
	    	   
	   });
	   /*button prev page*/
	   $('.prev').click(function(){	   
	       $('.pagination-body').find('.pageNumber.pactive').prev().addClass('pactive');
	       $('.pagination-body').find('.pageNumber.pactive').next().removeClass('pactive');
	       if($('.pagination-body').find('.pageNumber').length > 0){    	   
	    	   var keySearch = $('#iSearchHisDonation').val().trim();
	    	   if(keySearch.length > 0){
				   getNextPageSearchHisDonation();
			   }else
				   getHisDonationPageChange();
	       }
	   });
}

/*get next / prev page donation */
function getHisDonationPageChange(){
	
	const limitItem = $('#limitShowItemHisDonation').val();
	const current = $('.pagination-body').find('.pageNumber.pactive a').text();
	const fromDate = $('#aDonation_date').val();
	var objectData = {};
	objectData['action'] = 'get';
	objectData['fromDate'] = fromDate;
	objectData['limitItem'] = limitItem;
	objectData['current'] = current;
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: setPathURL()+"/Donation/user/actionHisDonation",
		data: JSON.stringify(objectData),
		dataType: "json",
		cache: false,
		timeout: 60000,
		success: function(data){
			//console.log(data['donations']);
			var html = "";
			var donations = data['donations'];
			const totalRecords = data['totalRecords'];
			
			/*load data*/			
			$.each(donations, function(i, value){
				html += "<tr>"
					+"<td><div class='checkbox-del'>"
					+"<input class='checkbox-del-group check-del' type='checkbox' onclick='onClickItemCheckBox_Donation(this)'></div></td>"
                    +"<td style='display:none;'>"+value.donationId+"</td>"
                    +"<td style='width:700px'>"+value.campaignName+"</td>"
                    +"<td>"+value.dateTime+"</td>"
                    +"<td>"+ formatInteger(value.amount,'de-DE')+"</td>" 
                    +"<td class='td-action'>"
                    +"<a href='#' class='delete' title='Xóa' data='"+value.donationId+"' onclick='aDelGeneralDonation(this);return false;'><i class='ti-trash'></i></a>"
                    +"</td>"
                    +"</tr>;"				
			});
			$('#mTableHisDonation').find('tbody').html(html);
			
			$('.hint-text').html('Hiển thị <b>'+(limitItem*current > totalRecords ? totalRecords : limitItem*current)+'</b> trong số <b>'+totalRecords+'</b> mục');
		},
		error: function(e){
			console.log(e);
		}
	});
}

/*get next page search history donation*/
function getNextPageSearchHisDonation(){
	const limitItem = $('#limitShowItemHisDonation').val();
	const current = $('.pagination-body').find('.pageNumber.pactive a').text();
	const fromDate = $('#aDonation_date').val();
	var keySearch = $('#iSearchHisDonation').val().trim();
	var objectData = {};
	objectData['action'] = 'search';
	objectData['keySearch'] = keySearch;
	objectData['fromDate'] = fromDate;
	objectData['limitItem'] = limitItem;
	objectData['current'] = current;
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: setPathURL()+"/Donation/user/actionHisDonation",
		data: JSON.stringify(objectData),
		dataType: "json",
		cache: false,
		timeout: 60000,
		success: function(data){
			//console.log(data['donations']);
			var html = "";
			var donations = data['donations'];
			const totalRecords = data['totalRecords'];
			
			/*load data*/			
			$.each(donations, function(i, value){
				html += "<tr>"
					+"<td><div class='checkbox-del'>"
					+"<input class='checkbox-del-group check-del' type='checkbox' onclick='onClickItemCheckBox_Donation(this)'></div></td>"
                    +"<td style='display:none;'>"+value.donationId+"</td>"
                    +"<td style='width:700px'>"+value.campaignName+"</td>"
                    +"<td>"+value.dateTime+"</td>"
                    +"<td>"+ formatInteger(value.amount,'de-DE')+"</td>" 
                    +"<td class='td-action'>"
                    +"<a href='#' class='delete' title='Xóa' data='"+value.donationId+"' onclick='aDelGeneralDonation(this);return false;'><i class='ti-trash'></i></a>"
                    +"</td>"
                    +"</tr>;"				
			});
			$('#mTableHisDonation').find('tbody').html(html);
			
			$('.hint-text').html('Hiển thị <b>'+(limitItem*current > totalRecords ? totalRecords : limitItem*current)+'</b> trong số <b>'+totalRecords+'</b> mục được tìm thấy');
		},
		error: function(e){
			console.log(e);
		}
	});
}

/*========ajax seach =====================*/
function searchHistoryDonation(){
	
	const limitItem = $('#limitShowItemHisDonation').val();
	const current = 1;
	const fromDate = $('#aDonation_date').val();
	var keySearch = $('#iSearchHisDonation').val().trim();
	var objectData = {};
	objectData['action'] = 'search';
	objectData['keySearch'] = keySearch;
	objectData['fromDate'] = fromDate;
	objectData['limitItem'] = limitItem;
	objectData['current'] = current;
	
	
	//e.preventDefault();
	if(keySearch.length > 0){
		$.ajax({
			type: "POST",
			contentType: "application/json",
			url: setPathURL()+"/Donation/user/actionHisDonation",
			data: JSON.stringify(objectData),
			dataType: "json",
			cache: false,
			timeout: 60000,
			success: function(data){
				//console.log(data['campaigns']);
				var html = "", htmlpag = "";
				var donations = data['donations'];
				const totalRecords = data['totalRecords'];
				const avgPage = totalRecords / limitItem;
				const totalPage = Math.ceil(avgPage);
				
				/*load data*/			
				$.each(donations, function(i, value){
					html += "<tr>"
						+"<td><div class='checkbox-del'>"
						+"<input class='checkbox-del-group check-del' type='checkbox' onclick='onClickItemCheckBox_Donation(this)'></div></td>"
                        +"<td style='display:none;'>"+value.donationId+"</td>"
                        +"<td style='width:700px'>"+value.campaignName+"</td>"
                        +"<td>"+value.dateTime+"</td>"
                        +"<td>"+ formatInteger(value.amount,'de-DE')+"</td>" 
                        +"<td class='td-action'>"
                        +"<a href='#' class='delete' title='Xóa' data='"+value.donationId+"' onclick='aDelGeneralDonation(this);return false;'><i class='ti-trash'></i></a>"
                        +"</td>"
                        +"</tr>;"				
				});
				$('#mTableHisDonation').find('tbody').html(html);
				
				/*load pagination*/
				htmlpag += "<li><a href='#' onclick='return false' class='prev'><<</a></li>";
				for(var i = 1; i <= totalPage; i++){				
					if(i == 1)
						htmlpag += "<li class='pageNumber pactive'><a href='#' onclick='return false'>"+i+"</a></li>";
					else
	                    htmlpag += "<li class='pageNumber'><a href='#' onclick='return false'>"+i+"</a></li>";                          
				}
				
				htmlpag += "<li><a href='#' onclick='return false' class='next'>>></a></li>";
				$('.pagination-body').html(htmlpag);
				setActionHisDonationPagination()
				const cur = $('.pagination-body').find('.pageNumber.pactive a').text();
				$('.hint-text').html('Hiển thị <b>'+(limitItem*cur > totalRecords ? totalRecords : limitItem*cur)+'</b> trong số <b>'+totalRecords+'</b> mục được tìm thấy');
			},
			error: function(e){
				console.log(e);
			}
		});
	}else
		getHisDonationOnChange();
	
}

//delete a item general donation
function aDelGeneralDonation(e){
	const limitItem = $('#limitShowItemHisDonation').val();
	var objectData = {};		
	objectData['id'] = e.getAttribute('data') + " ";
	objectData['fromDate'] = $('#aDonation_date').val();	
	objectData['limitItem'] = limitItem;
	console.log(objectData);
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: setPathURL()+"/Donation/user/deleteGeneralDonation",
		data: JSON.stringify(objectData),
		dataType: "json",
		cache: false,
		timeout: 60000,
		success: function(data){
			console.log(data);
			const status = data['status'];
			var msg = data['message'];
			if(status != 1){
				var html = "", htmlpag ="";
				var donations = data['donations'];
				const totalRecords = data['totalRecords'];
				const avgPage = totalRecords / limitItem;
				const totalPage = Math.ceil(avgPage);
				
				/*load data*/			
				$.each(donations, function(i, value){
					html += "<tr>"
							+"<td><div class='checkbox-del'>"
							+"<input class='checkbox-del-group check-del' type='checkbox' onclick='onClickItemCheckBox_Donation(this)'></div></td>"
                            +"<td style='display:none;'>"+value.donationId+"</td>"
	                        +"<td style='width:700px'>"+value.campaignName+"</td>"
	                        +"<td>"+value.dateTime+"</td>"
	                        +"<td>"+ formatInteger(value.amount,'de-DE')+"</td>" 
	                        +"<td class='td-action'>"
                            +"<a href='#' class='delete' title='Xóa' data='"+value.donationId+"' onclick='aDelGeneralDonation(this);return false;'><i class='ti-trash'></i></a>"
                            +"</td>"
	                        +"</tr>;"				
				});
				$('#mTableHisDonation').find('tbody').html(html);
				$("#btnDelGroup_Donation").prop('disabled', true);
				
				/*load pagination*/
				htmlpag += "<li><a href='#' onclick='return false' class='prev'><<</a></li>";
				for(var i = 1; i <= totalPage; i++){				
					if(i == 1)
						htmlpag += "<li class='pageNumber pactive'><a href='#' onclick='return false'>"+i+"</a></li>";
					else
	                    htmlpag += "<li class='pageNumber'><a href='#' onclick='return false'>"+i+"</a></li>";                          
				}
				
				htmlpag += "<li><a href='#' onclick='return false' class='next'>>></a></li>";
				$('.pagination-body').html(htmlpag);
				setActionHisDonationPagination()
				const cur = $('.pagination-body').find('.pageNumber.pactive a').text();
				$('.hint-text').html('Hiển thị <b>'+(limitItem*cur > totalRecords ? totalRecords : limitItem*cur)+'</b> trong số <b>'+totalRecords+'</b> mục được tìm thấy');
				$("#checkDelGroup_Donation").prop('checked', false);
				showBoxMsg(msg, 0);
			}else{
				showBoxMsg(msg, 1);
			}
			
		},
		error: function(e){
			showBoxMsg("Lỗi! Không thể xóa", 1);
			
			console.log(e);
		}
	});
}

//delete general donation
function deleteGeneralDonation(){
	const limitItem = $('#limitShowItemHisDonation').val();
	var objectData = {};		
	objectData['id'] = getDonationId();
	objectData['fromDate'] = $('#aDonation_date').val();	
	objectData['limitItem'] = limitItem;
	console.log(objectData);
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: setPathURL()+"/Donation/user/deleteGeneralDonation",
		data: JSON.stringify(objectData),
		dataType: "json",
		cache: false,
		timeout: 60000,
		success: function(data){
			console.log(data);
			const status = data['status'];
			var msg = data['message'];
			if(status != 1){
				var html = "", htmlpag ="";
				var donations = data['donations'];
				const totalRecords = data['totalRecords'];
				const avgPage = totalRecords / limitItem;
				const totalPage = Math.ceil(avgPage);
				
				/*load data*/			
				$.each(donations, function(i, value){
					html += "<tr>"
							+"<td><div class='checkbox-del'>"
							+"<input class='checkbox-del-group check-del' type='checkbox' onclick='onClickItemCheckBox_Donation(this)'></div></td>"
                            +"<td style='display:none;'>"+value.donationId+"</td>"
	                        +"<td style='width:700px'>"+value.campaignName+"</td>"
	                        +"<td>"+value.dateTime+"</td>"
	                        +"<td>"+ formatInteger(value.amount,'de-DE')+"</td>" 
	                        +"<td class='td-action'>"
                            +"<a href='#' class='delete' title='Xóa' data='"+value.donationId+"' onclick='aDelGeneralDonation(this);return false;'><i class='ti-trash'></i></a>"
                            +"</td>"
	                        +"</tr>;"				
				});
				$('#mTableHisDonation').find('tbody').html(html);
				$("#btnDelGroup_Donation").prop('disabled', true);
				
				/*load pagination*/
				htmlpag += "<li><a href='#' onclick='return false' class='prev'><<</a></li>";
				for(var i = 1; i <= totalPage; i++){				
					if(i == 1)
						htmlpag += "<li class='pageNumber pactive'><a href='#' onclick='return false'>"+i+"</a></li>";
					else
	                    htmlpag += "<li class='pageNumber'><a href='#' onclick='return false'>"+i+"</a></li>";                          
				}
				
				htmlpag += "<li><a href='#' onclick='return false' class='next'>>></a></li>";
				$('.pagination-body').html(htmlpag);
				setActionHisDonationPagination()
				const cur = $('.pagination-body').find('.pageNumber.pactive a').text();
				$('.hint-text').html('Hiển thị <b>'+(limitItem*cur > totalRecords ? totalRecords : limitItem*cur)+'</b> trong số <b>'+totalRecords+'</b> mục được tìm thấy');
				$("#checkDelGroup_Donation").prop('checked', false);
				showBoxMsg(msg, 0);
			}else{
				showBoxMsg(msg, 1);
			}
			
		},
		error: function(e){
			showBoxMsg("Lỗi! Không thể xóa", 1);
			
			console.log(e);
		}
	}); 
	
}

/*get id general donation in table*/
function getDonationId(){
	var id = "";
	$('#mTableHisDonation tbody tr:has(td)').find('input[type=checkbox]:checked').each(function () {		
        var row = $(this).closest("tr").find("td:eq(1)").html();  
        id += row;        
        id += " ";
    });
	return id;
}

function onClickItemCheckBox_Donation(e){
	
	//var isChecked = $(this).prop("checked");
	var isChecked = e.checked;
    var isbtnChecked = true;
    
    var isHeaderChecked = $("#checkDelGroup_Donation").prop("checked");
    if (isChecked == false && isHeaderChecked){
    	$("#checkDelGroup_Donation").prop('checked', isChecked);
    	
    }else {
    	$('#mTableHisDonation tr:has(td)').find('input[type="checkbox"]').each(function() {
    		if ($(this).prop("checked") == false)
    			isChecked = false;
    	});
    	
    	$("#checkDelGroup_Donation").prop('checked', isChecked);
    }
    
    $('#mTableHisDonation tr:has(td)').find('input[type="checkbox"]').each(function() {
    	
		if ($(this).prop("checked") == true){
			isbtnChecked = false;    			
		}
	});
	
	$("#btnDelGroup_Donation").prop('disabled', isbtnChecked);
}