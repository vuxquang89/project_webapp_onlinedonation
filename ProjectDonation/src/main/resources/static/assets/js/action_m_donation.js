
/*========ajax select status donation=====*/
function getDonationOnChange(){
	const index = $('#campaignStatus_Donation :selected').val();
	var nameStatus = $('#campaignStatus_Donation :selected').text();
	const limitItem = $('#limitShowItemDonation').val();
	//const current = $('.pagination-body').find('.pageNumber.pactive a').text();
	const current = 1;
	var objectData = {};
	objectData['action'] = 'get';
	objectData['campaignStatus'] = index;
	objectData['limitItem'] = limitItem;
	objectData['current'] = current;
	
	//var style = setStyleStatus(index);
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: setPathURL()+"/Donation/admin/actionDonation",
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
                        +"<td>"+value.campaignId+"</td>"
                        +"<td class='td-style'>"+value.campaignName+"</td>"
                        +"<td>"+ formatInteger(value.amount,'de-DE')+"</td>"
                        +"<td>"+ formatInteger(value.goal,'de-DE')+"</td>"
                        +"<td>"+ value.bank_account_name+"</td>"
                        +"<td class='td-action'>"
                        +"<a href='"+setPathURL()+"/Donation/admin/infoDonation/"+value.campaignId+"' class='edit' title='Chi tiết'><i class='ti-info'></i></a>"                                                                
                        +"</td></tr>";
				
			});
			$('#mTableDonation').find('tbody').html(html);
			
									
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
			setActionDonationPagination();
			const cur = $('.pagination-body').find('.pageNumber.pactive a').text();
			$('.hint-text').html('Hiển thị <b>'+(limitItem*cur > totalRecords ? totalRecords : limitItem*cur)+'</b> trong số <b>'+totalRecords+'</b> mục');
			
		},
		error: function(e){
			console.log(e);
		}
	});
}


/*set action button pagination*/
function setActionDonationPagination(){
	/*button page number*/
	   $('.pageNumber').click(function(){
		   $('.pagination-body').find('.pageNumber.pactive').removeClass('pactive');	   
		   $(this).addClass('pactive');
		   var keySearch = $('#iSearchDonation').val().trim();
		   if(keySearch.length > 0){
			   getNextPageSearchDonation();
		   }else
			   getDonationPageChange();		   
	   });
	   
	   /*button next page*/
	   $('.next').click(function(){	   
	       $('.pagination-body').find('.pageNumber.pactive').next().addClass('pactive');
	       $('.pagination-body').find('.pageNumber.pactive').prev().removeClass('pactive');
	       if($('.pagination-body').find('.pageNumber').length > 0){  
	    	   
	    	   //if($('.pagination-body').attr('ul-click') == 'user'){
	    	   var keySearch = $('#iSearchDonation').val().trim();
			   if(keySearch.length > 0){
				   getNextPageSearchDonation();
			   }else{
				   getDonationPageChange();
			   }
	       }
	    	   
	   });
	   /*button prev page*/
	   $('.prev').click(function(){	   
	       $('.pagination-body').find('.pageNumber.pactive').prev().addClass('pactive');
	       $('.pagination-body').find('.pageNumber.pactive').next().removeClass('pactive');
	       if($('.pagination-body').find('.pageNumber').length > 0){    	   
	    	   var keySearch = $('#iSearchDonation').val().trim();
			   if(keySearch.length > 0){
				   getNextPageSearchDonation();
			   }else{
				   getDonationPageChange();
			   }
	       }
	   });
}

/*get next / prev page donation */
function getDonationPageChange(){
	
	const index = $('#campaignStatus_Donation :selected').val();
	
	const limitItem = $('#limitShowItemDonation').val();
	const current = $('.pagination-body').find('.pageNumber.pactive a').text();
	var objectData = {};
	objectData['action'] = 'get';
	objectData['campaignStatus'] = index;
	objectData['limitItem'] = limitItem;
	objectData['current'] = current;
	
	//var style = setStyleStatus(index);
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: setPathURL()+"/Donation/admin/actionDonation",
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
                        +"<td>"+value.campaignId+"</td>"
                        +"<td class='td-style'>"+value.campaignName+"</td>"
                        +"<td>"+ formatInteger(value.amount,'de-DE')+"</td>"
                        +"<td>"+ formatInteger(value.goal,'de-DE')+"</td>"
                        +"<td>"+ value.bank_account_name+"</td>"
                        +"<td class='td-action'>"
                        +"<a href='"+setPathURL()+"/Donation/admin/infoDonation/"+value.campaignId+"' class='edit' title='Chi tiết'><i class='ti-info'></i></a>"                                                                
                        +"</td></tr>";
				
			});
			$('#mTableDonation').find('tbody').html(html);
			
			$('.hint-text').html('Hiển thị <b>'+(limitItem*current > totalRecords ? totalRecords : limitItem*current)+'</b> trong số <b>'+totalRecords+'</b> mục');
		},
		error: function(e){
			console.log(e);
		}
	});
}

/*get next page result search*/
function getNextPageSearchDonation(){
	const index = $('#campaignStatus_Donation :selected').val();
	var nameStatus = $('#campaignStatus_Donation :selected').text();
	const limitItem = $('#limitShowItemDonation').val();
	const current = $('.pagination-body').find('.pageNumber.pactive a').text();
	var keySearch = $('#iSearchDonation').val().trim();
	
	var objectData = {};
	objectData['action'] = 'search';
	objectData['keySearch'] = keySearch;
	objectData['limitItem'] = limitItem;
	objectData['campaignStatus'] = index;
	objectData['current'] = current;
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: setPathURL()+"/Donation/admin/actionDonation",
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
                        +"<td>"+value.campaignId+"</td>"
                        +"<td class='td-style'>"+value.campaignName+"</td>"
                        +"<td>"+ formatInteger(value.amount,'de-DE')+"</td>"
                        +"<td>"+ formatInteger(value.goal,'de-DE')+"</td>"
                        +"<td>"+ value.bank_account_name+"</td>"
                        +"<td class='td-action'>"
                        +"<a href='"+setPathURL()+"/Donation/admin/infoDonation/"+value.campaignId+"' class='edit' title='Chi tiết'><i class='ti-info'></i></a>"                                                                
                        +"</td></tr>";
				
			});
			$('#mTableDonation').find('tbody').html(html);
			
			$('.hint-text').html('Hiển thị <b>'+(limitItem*current > totalRecords ? totalRecords : limitItem*current)+'</b> trong số <b>'+totalRecords+'</b> mục được tìm thấy');
		},
		error: function(e){
			console.log(e);
		}
	});
}

/*========ajax seach =====================*/
function searchDonation(){
	const index = $('#campaignStatus_Donation :selected').val();
	
	const limitItem = $('#limitShowItemDonation').val();
	const current = 1;	
	var keySearch = $('#iSearchDonation').val().trim();
	
	var objectData = {};
	objectData['action'] = 'search';
	objectData['keySearch'] = keySearch;
	objectData['limitItem'] = limitItem;
	objectData['campaignStatus'] = index;
	objectData['current'] = current;
	
	
	//e.preventDefault();
	if(keySearch.length > 0){
		$.ajax({
			type: "POST",
			contentType: "application/json",
			url: setPathURL()+"/Donation/admin/actionDonation",
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
				
				$.each(donations, function(i, value){
					
					html += "<tr>"                                                            
	                        +"<td>"+value.campaignId+"</td>"
	                        +"<td class='td-style'>"+value.campaignName+"</td>"
	                        +"<td>"+ formatInteger(value.amount,'de-DE')+"</td>"
	                        +"<td>"+ formatInteger(value.goal,'de-DE')+"</td>"
	                        +"<td>"+ value.bank_account_name+"</td>"
	                        +"<td class='td-action'>"
	                        +"<a href='"+setPathURL()+"/Donation/admin/infoDonation/"+value.campaignId+"' class='edit' title='Chi tiết'><i class='ti-info'></i></a>"                                                                
	                        +"</td></tr>";
					
				});
				$('#mTableDonation').find('tbody').html(html);
				
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
				setActionDonationPagination()
				const cur = $('.pagination-body').find('.pageNumber.pactive a').text();
				$('.hint-text').html('Hiển thị <b>'+(limitItem*cur > totalRecords ? totalRecords : limitItem*cur)+'</b> trong số <b>'+totalRecords+'</b> mục được tìm thấy');
			},
			error: function(e){
				console.log(e);
			}
		});
	}else
		getDonationOnChange();
	
}

/*info donation page*/
/*get next / prev page donation */
function getInfoDonationPageChange(){
	const id = $('#cID').val();
	
	const limitItem = $('#limitShowItemDonation').val();
	const current = $('.pagination-body').find('.pageNumber.pactive a').text();
	var objectData = {};
	objectData['action'] = 'get';
	objectData['id'] = id;
	objectData['limitItem'] = limitItem;
	objectData['current'] = current;
	
	//var style = setStyleStatus(index);
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: setPathURL()+"/Donation/admin/actionInfoDonation",
		data: JSON.stringify(objectData),
		dataType: "json",
		cache: false,
		timeout: 60000,
		success: function(data){
			console.log(data);
			var html = "";
			var donations = data['donations'];
			const totalRecords = data['totalRecords'];
						
			/*load data*/
			$.each(donations, function(i, value){
				html += "<tr><td style='display:none;'>"+value.donationId+"</td>"                                                          
	                    +"<td>"+value.userId+"</td>"
	                    +"<td>"+value.fullName+"</td>"
	                    +"<td>"+value.username+"</td>"
	                    +"<td><input type='text' value='"+value.amount+"' disabled></td>"
	                    +"<td>"+value.dateTime+"</td>"
	                    +"<td class='td-action'><div class='div-action'>"
	                    +"<button class='bt-edit' title='Chỉnh sửa' "+((value.campaignStatus == 1) ? '' : 'disabled')+">Edit</button>"
	                    +"<button class='bt-cancel' title='Hủy'>Cancel</button></td>"
	                    +"</div></tr>";				
			});
			
			$('#mTableInfoDonation').find('tbody').html(html);
			$('#mTableInfoDonation tbody tr td .bt-cancel').css("display", "none");
			$('.hint-text').html('Hiển thị <b>'+(limitItem*current > totalRecords ? totalRecords : limitItem*current)+'</b> trong số <b>'+totalRecords+'</b> mục');
			
		},
		error: function(e){
			console.log(e);
		}
	});
}

/*========ajax info donation=====*/
function getInfoDonationOnChange(){
	const id = $('#cID').val();
	
	const limitItem = $('#limitShowItemDonation').val();
	
	//const current = $('.pagination-body').find('.pageNumber.pactive a').text();
	const current = 1;
	var objectData = {};
	objectData['action'] = 'get';
	objectData['id'] = id;
	objectData['limitItem'] = limitItem;
	objectData['current'] = current;
	
	//var style = setStyleStatus(index);
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: setPathURL()+"/Donation/admin/actionInfoDonation",
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
				html += "<tr><td style='display:none;'>"+value.donationId+"</td>"                                                          
	                    +"<td>"+value.userId+"</td>"
	                    +"<td>"+value.fullName+"</td>"
	                    +"<td>"+value.username+"</td>"
	                    +"<td><input type='text' value='"+value.amount+"' disabled></td>"
	                    +"<td>"+value.dateTime+"</td>"   
	                    +"<td class='td-action'><div class='div-action'>"
	                    +"<button class='bt-edit' title='Chỉnh sửa' "+((value.campaignStatus == 1) ? '' : 'disabled')+">Edit</button>"
	                    +"<button class='bt-cancel' title='Hủy'>Cancel</button></td>"
	                    +"</div></tr>";
				
			});
			
			$('#mTableInfoDonation').find('tbody').html(html);
			$('#mTableInfoDonation tbody tr td .bt-cancel').css("display", "none");
									
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
			setActionInfoDonationPagination();
			const cur = $('.pagination-body').find('.pageNumber.pactive a').text();
			$('.hint-text').html('Hiển thị <b>'+(limitItem*cur > totalRecords ? totalRecords : limitItem*cur)+'</b> trong số <b>'+totalRecords+'</b> mục');
			
		},
		error: function(e){
			console.log(e);
		}
	});
}

/*set action button pagination*/
function setActionInfoDonationPagination(){
	/*button page number*/
	   $('.pageNumber').click(function(){
		   $('.pagination-body').find('.pageNumber.pactive').removeClass('pactive');	   
		   $(this).addClass('pactive');
		   var keySearch = $('#iSearchInfoDonation').val().trim();
		   if(keySearch.length > 0){
			   getNextPageSearchInfoDonation();
		   }else
			   getInfoDonationPageChange();		   
	   });
	   
	   /*button next page*/
	   $('.next').click(function(){	   
	       $('.pagination-body').find('.pageNumber.pactive').next().addClass('pactive');
	       $('.pagination-body').find('.pageNumber.pactive').prev().removeClass('pactive');
	       if($('.pagination-body').find('.pageNumber').length > 0){  
	    	   
	    	   //if($('.pagination-body').attr('ul-click') == 'user'){
	    	   var keySearch = $('#iSearchInfoDonation').val().trim();
			   if(keySearch.length > 0){
				   getNextPageSearchInfoDonation();
			   }else{
				   getInfoDonationPageChange();
			   }
	       }
	    	   
	   });
	   /*button prev page*/
	   $('.prev').click(function(){	   
	       $('.pagination-body').find('.pageNumber.pactive').prev().addClass('pactive');
	       $('.pagination-body').find('.pageNumber.pactive').next().removeClass('pactive');
	       if($('.pagination-body').find('.pageNumber').length > 0){    	   
	    	   var keySearch = $('#iSearchInfoDonation').val().trim();
			   if(keySearch.length > 0){
				   getNextPageSearchInfoDonation();
			   }else{
				   getInfoDonationPageChange();
			   }
	       }
	   });
}

/*========ajax seach info donation =====================*/
function searchInfoDonation(){
	const id = $('#cID').val();
	
	const limitItem = $('#limitShowItemDonation').val();
	const current = 1;	
	var keySearch = $('#iSearchInfoDonation').val().trim();
	
	var objectData = {};
	objectData['action'] = 'search';
	objectData['keySearch'] = keySearch;
	objectData['limitItem'] = limitItem;
	objectData['id'] = id;
	objectData['current'] = current;
	
	
	//e.preventDefault();
	if(keySearch.length > 0){
		$.ajax({
			type: "POST",
			contentType: "application/json",
			url: setPathURL()+"/Donation/admin/actionInfoDonation",
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
					html += "<tr><td style='display:none;'>"+value.donationId+"</td>"                                                          
		                    +"<td>"+value.userId+"</td>"
		                    +"<td>"+value.fullName+"</td>"
		                    +"<td>"+value.username+"</td>"
		                    +"<td><input type='text' value='"+value.amount+"' disabled></td>"
		                    +"<td>"+value.dateTime+"</td>" 
		                    +"<td class='td-action'><div class='div-action'>"
		                    +"<button class='bt-edit' title='Chỉnh sửa' "+((value.campaignStatus == 1) ? '' : 'disabled')+">Edit</button>"
		                    +"<button class='bt-cancel' title='Hủy'>Cancel</button></td>"
		                    +"</div></tr>";
					
				});
				
				$('#mTableInfoDonation').find('tbody').html(html);
				$('#mTableInfoDonation tbody tr td .bt-cancel').css("display", "none");
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
				setActionInfoDonationPagination()
				const cur = $('.pagination-body').find('.pageNumber.pactive a').text();
				$('.hint-text').html('Hiển thị <b>'+(limitItem*cur > totalRecords ? totalRecords : limitItem*cur)+'</b> trong số <b>'+totalRecords+'</b> mục được tìm thấy');
			},
			error: function(e){
				console.log(e);
			}
		});
	}else
		getInfoDonationOnChange();
	
}

/*get next page result search*/
function getNextPageSearchInfoDonation(){
	const id = $('#cID').val();
	//var nameStatus = $('#campaignStatus_Donation :selected').text();
	const limitItem = $('#limitShowItemDonation').val();
	const current = $('.pagination-body').find('.pageNumber.pactive a').text();
	var keySearch = $('#iSearchInfoDonation').val().trim();
	
	var objectData = {};
	objectData['action'] = 'search';
	objectData['keySearch'] = keySearch;
	objectData['limitItem'] = limitItem;
	objectData['id'] = id;
	objectData['current'] = current;
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: setPathURL()+"/Donation/admin/actionInfoDonation",
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
				html += "<tr><td style='display:none;'>"+value.donationId+"</td>"                                                          
	                    +"<td>"+value.userId+"</td>"
	                    +"<td>"+value.fullName+"</td>"
	                    +"<td>"+value.username+"</td>"
	                    //+"<td>"+formatInteger(value.amount,'de-DE')+"</td>"
	                    +"<td><input type='text' value='"+value.amount+"' disabled></td>"
	                    +"<td>"+value.dateTime+"</td>"     
	                    +"<td class='td-action'><div class='div-action'>"
	                    +"<button class='bt-edit' title='Chỉnh sửa' "+((value.campaignStatus == 1) ? '' : 'disabled')+">Edit</button>"
	                    +"<button class='bt-cancel' title='Hủy'>Cancel</button></td>"
	                    +"</div></tr>";
				
			});
			
			$('#mTableInfoDonation').find('tbody').html(html);
			$('#mTableInfoDonation tbody tr td .bt-cancel').css("display", "none");
			$('.hint-text').html('Hiển thị <b>'+(limitItem*current > totalRecords ? totalRecords : limitItem*current)+'</b> trong số <b>'+totalRecords+'</b> mục được tìm thấy');
		},
		error: function(e){
			console.log(e);
		}
	});
}