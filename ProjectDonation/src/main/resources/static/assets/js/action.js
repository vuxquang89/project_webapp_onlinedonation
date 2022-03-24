/*check button submit create ampaign form*/
$("#f-campaign-create").submit(function(){
    var name = $("#campaign-name"),
        summary = $("#campaign-summary"),
        avatar = $("#campaign-avatar"),
        goal = $("#campaign-goal"),
        to_date = $("#to-date"),
        from_date = $('#from-date'),
        content = $('#campaign-content'),
        msg = $("#campaign-msg");

    if(name.val().trim().length < 1){
        msg.html("Mời nhập tên chiến dịch.");
        name.focus();
        return false;
    }
    
    if(name.val().trim().length > 200){
        msg.html("Tên chiến dịch không dài quá 200 ký tự.");
        name.focus();
        return false;
    }
    
    if(summary.val().trim().length < 1){
        msg.html("Mời nhập tóm tắt chiến dịch.");
        summary.focus();
        return false;
    }
    if(avatar.val().trim().length < 1){
        msg.html("Mời chọn hình đại diện cho chiến dịch.");
        name.focus();
        return false;
    }
    if(goal.val().trim().length < 1){
        msg.html("Mời nhập mức tiền quyên góp cho chiến dịch.");
        goal.focus();
        return false;
    }
    /*
    if(!isNumber(goal.val().trim())){
    	msg.html("Giá trị không hợp lệ.");
        goal.focus();
        return false;
    }
    */
    if(parseFloat(goal.val().trim()) <= 0){
    	msg.html("Mức tiền quyên góp phải > 0.");
        goal.focus();
        return false;
    }
    
    if(from_date.val().trim().length < 1){
        msg.html("Mời chọn ngày bắt đầu chiến dịch.");
        name.focus();
        return false;
    }

    if(to_date.val().trim().length < 1){
        msg.html("Mời chọn ngày kết thúc chiến dịch.");
        name.focus();
        return false;
    }
    
   
   
    if(content.val().trim().length <= 50){
        msg.html("Mời nhập thêm nội dung chiến dịch.");
        name.focus();        
        return false;
    }
    
    
    return true;
});

/*=======ajax load more active campaign=========*/
function loadMoreActive(){
	
	const currentItem = $('.donation-item').length;	
	
	var objectData = {};
	objectData['action'] = 'viewMoreActive';	
	objectData['currentItem'] = currentItem;	
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: setPathURL()+"/Donation/actionViewMoreCampaign",
		data: JSON.stringify(objectData),
		dataType: "json",
		cache: false,
		timeout: 60000,
		success: function(data){
			
			var html = "";
			var campaigns = data['campaigns'];
			const showMore = data['show'];
			
			$.each(campaigns, function(i, value){
				
				html += "<div onclick='directUrl(event)' class='donation-item relative overflow-hidden flex flex-col flex-nowrap text-gray-700 hover:text-momo bg-white border border-gray-200 rounded-xl'>"
    						//+"<a href='/view/" + value.campaignId+"'>"
							+"<div class='dn-img flex aspect-w-15 aspect-h-8'>"
    						+"<span class='border-box'>"
    						+"<img class='img-avatar-donation' alt='"+value.name+"' src='"+setPathURL()+value.urlAvatar+"'>"
    						+"	</span></div>"
    						+"<div class='dn-body pt-4 pb-3 flex-1 min-h-1 px-4'>"
    						+"<h5 class='dn-title text-lg transition font-bold leading-snug'>"+value.name+"</h5></div>"
    						+"<div class='dn-footer pt-0 mb-4 px-4'>"
    						+"<div class='flex flex-nowrap space-x-2 items-center mb-3'>"
    						+"<div class='shrink-0'></div>"
    						+"<div></div>"
    						+"<div class='shrink-0'>"
    						+"<span class='text-xs px-2 py-0.5 rounded-2xl lowercase badge-date-left'>Còn "+value.countDay+" ngày</span>"
							+"</div></div>"
							+"<div class='dn-info flex justify-between items-center'>"
							+"<div class='dn-money flex-1 flex items-end'>"
							+"<strong class='flex item-end text-gray-700 leading-5'>"+formatInteger(value.amount, 'de-DE')+"đ</strong>"
							+"<span class='pl-2 text-sm text-gray-500'>/ "+formatInteger(value.goal, 'de-DE')+"đ</span>"
							+"</div></div>"
							+"<div class='dn-progress flex overflow-hidden h-1.5 w-full bg-gray-200 rounded-lg my-1'>" 
							+"<div class='dn-progress-bar h-1.5 rounded-lg "+setClassProgressBar(value.avg)+"' style='width:"+value.avg+"%'></div>"
							+"</div>"
							+"<div class='flex flex-nowrap justify-between space-x-2  md:space-x-3 items-center mt-3'>"
							+"<div class='grow'>"
							+"<div class='text-xs text-gray-500'>Lượt quyên góp</div>"
							+"<div class='text-sm font-bold text-gray-600'>"+value.countDonation+"</div>"
							+"</div>"
							+"<div class='grow'>"
							+"<div class='text-xs text-gray-500'>Đạt được</div>"
							+"<div class='text-sm font-bold text-gray-600'>"+formatInteger(value.avg, 'de-DE')+"%</div>"
							+"</div>"
							+"<div class='grow flex items-center justify-end'>"
							+"<input type='hidden' value='"+setPathURL()+"/Donation/view/" + value.campaignId +"'>"
					if(value.status == 3)
						html+= "<span class='text-xs text-gray-400 border border-gray-400 rounded-md flex items-center justify-center font-bold h-7 px-3 md:group-hover:bg-gray-50'>Đạt mục tiêu</span>";
					else
						html += "<a class='text-xs text-pink border-pink-600 rounded flex items-center justify-center font-semibold h-7 px-2' href='"+setPathURL()+"/Donation/view/"+value.campaignId+"'>Quyên góp</a>";
				
						html+="</div></div></div>" +
									//"</a>" +
									"</div> ";
				
			});
			$('.grid-campaign').append(html);
			
			if(!showMore){
				$('.box-btn-more-active').remove();
			}
		},
		error: function(e){
			console.log(e);
		}
	});
	
}

/*direct url view info campaign*/
function directUrl(event){
	
	//var go_to_url = event.currentTarget.querySelector('a').getAttribute("href");
	var go_to_url = event.currentTarget.querySelector('input').getAttribute("value");
	console.log(go_to_url);
	//this will redirect us in same window
	document.location.href = go_to_url;
}

/*=======ajax load more news campaign===========*/
function loadMoreNews(){
	const currentItem = $('.box-dn-news-item').length;	
	var objectData = {};
	objectData['action'] = 'viewMoreNews';	
	objectData['currentItem'] = currentItem;
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: setPathURL()+"/Donation/actionViewMoreCampaign",
		data: JSON.stringify(objectData),
		dataType: "json",
		cache: false,
		timeout: 60000,
		success: function(data){
			var html = "";
			var campaigns = data['campaigns'];
			const showMore = data['show'];
			
			$.each(campaigns, function(i, value){
				
				html += "<div class='box-dn-news-item h-full group transition-shadow cursor-pointer md:overflow-hidden md:bg-white md:rounded-lg md:shadow-md md:hover:shadow-lg'>"
    						+"<div><div class='viewNews flex flex-wrap pbb-3 border-b border-gray-200 sm:border-none sm:pb-0' data-id='"+value.campaignId+"'>"
	    					
	    					+"			<div class='order-2 ptt-3 sm:flex-auto sm:w-full w-28 sm:order-1 sm:pt-0'>"
	    					+"				<div class='overflow-hidden bg-gray-100 rounded flex md:rounded-none relative'>"
	    					+"					<span class='border-box-news'>"
	    					+"						<img class='img-avatar-news' alt='"+value.name+"' src='"+setPathURL()+value.urlAvatar+"'>"
	    					+"					</span>"
	    					+"				</div>"
	    					+"			</div>"	    								
	    					+"			<div class='flex-1 order-1 pr-6 sm:pr-0 sm:flex-auto sm:w-full sm:order-2 md:pb-2 md:px-4'>"
	    					+"				<div class='text-news-title ptt-3 pb-2 font-semibold leading-snug text-md'>"+value.name+"</div>"
	    					+"				<div class='text-xs text-gray-500'>"+value.dateEnd+"</div>"
	    					+"			</div>"    						
    						+"	</div></div></div>";
				
			});
			
			$('.grid-campaign-news').append(html);
			setViewNewsClick();
			if(!showMore){
				$('.box-btn-more-news').remove();
			}
			
		},
		error: function(e){
			console.log(e);
		}
	});
}

/*ajax load view news dialog*/
function viewNews(id){
	//const id = $('.viewNews').attr('data-id');
	var objectData = {};
	objectData['id'] = id;
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: setPathURL()+"/Donation/actionViewCampaign",
		data: JSON.stringify(objectData),
		dataType: "json",
		cache: false,
		timeout: 60000,
		success: function(data){
			
			var html = "";
			var campaign = data['campaign'];
			
			html += "<div id='dialog-dn-news'>"
			    		+"<div id='dialog' class='relative flex flex-col w-full max-w-full max-h-full bg-white shadow-xl modalFadeUp modal-cluster md:rounded-xl rounded-t-xl h-full md:max-w-3xl'>"
			    		+"	<button id='close-dn-news' onclick='close_dialog_news()' class='z-20 absolute flex items-center justify-center w-12 h-12 top-0 mt-1 left-0 right-auto md:left-auto md:right-3 rounded-full'>"
			    		+"		<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 20 20' fill='currentColor' class='w-6 h-6 block'>"
			    		+"			<path fill-rule='evenodd' d='M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z' clip-rule='evenodd' class='jsx-ec3d9023a8eed023'></path>"
			    		+"		</svg>"
			    		+"	</button>"
			    		+"	<header class='modal-header ptt-4 pbb-4 pl-12 pr-12  font-semibold flex items-center justify-center border-b border-gray-200'>"
			    		+"		<h3 class='overflow-hidden text-ellipsis whitespace-nowrap'>Tin tức</h3>"
			    		+"	</header>"
			    		+"	<div class='modal-body  h-full overflow-auto p-0 bg-white rounded'>"
			    		+"		<div class='p-4 md:p-7'>"
			    		+"			<div class='overflow-hidden round pointer-events-none select-none md:rounded-lg mb-3'>"
			    		+"				<span style='box-sizing: border-box; display: block; overflow: hidden; width: initial; height: initial; background: none; opacity: 1; border: 0px; margin: 0px; padding: 0px; position: relative;'>"
			    		+"					<img style='inset: 0px; box-sizing: border-box; padding: 0px; border: none; margin: auto; display: block; min-width: 100%; max-width: 100%; min-height: 100%; max-height: 100%;' alt='"+campaign.name+"' src='"+setPathURL()+campaign.urlAvatar+"'>"
			    		+"				</span>"
			    		+"			</div>"
			    		+"			<h3 class='mb-2 text-lg font-bold text-pink-600 md:text-2xl'>"+campaign.name+"</h3>"
			    		+"			<div class='flex flex-row items-center mb-2'>"
			    		+"				<div class='flex-auto'>"
			    		+"					<div class='mt-0 text-gray-500 text-tiny first-letter:uppercase flex flex-wrap items-center '>"
			    		+"						<span class=''>"+campaign.dateCreated+"</span>"
			    		+"					</div>"
			    		+"				</div>"
			    		+"				<div class='flex-0 shrink-0'></div>"
			    		+"			</div>"
			    		+"			<div class='mtt-5 soju__prose small'>"
			    		+"				<p class='article-desc'>"+campaign.summary+"</p>"
			    		+"				<div class='mx-auto leading-normal tracking-tight lg:mx-0 lg:w-full lg:max-w-full md:leading-relaxed'>"
			    		+"					<p>"+campaign.content+"</p>"
			    		+"					<ul><li aria-level='1'>Số Heo Vàng quyên góp thành công: <strong>"+formatInteger(campaign.amount, 'de-DE')+" VNĐ / "+formatInteger(campaign.goal, 'de-DE')+" VNĐ&nbsp;</strong></li>"
			    		+"						<li aria-level='1'>Số lượt quyên góp: <strong>"+formatInteger(campaign.countDonation, 'de-DE')+"</strong> lượt - đạt <strong>"+formatInteger(campaign.avg, 'de-DE')+"%</strong></li></ul>"
			    		+"				</div>"
			    		+"			</div>"
			    		+"		</div>"
			    		+"	</div>"
			    		+"	<footer class='modal-footer flex border-t border-gray-200 py-2 md:py-4 px-5 items-center justify-end space-x-3 justify-center'>"
			    		+"		<div class='flex items-start justify-center flex-1 md:flex-none '>"
			    		+"			<a href='javascript:close_dialog_news()' class='btn-primary w-full whitespace-nowrap flex md:grow-0'>ĐÓNG</a>"
			    		+"		</div>"
			    		+"	</footer>"
			    		+"</div>"
			    		+"<div class='fixed inset-0 overflow-y-auto bg-black bg-opacity-50 modal-overlay'></div></div>";
				
			
			$('.dialog-news').html(html);
			$('body').css('overflow', 'hidden');
			
		},
		error: function(e){
			console.log(e);
		}
	});
}

function close_dialog_news(){	
	$('#dialog-dn-news').remove();
	$('body').css('overflow', 'scroll');
}

/*========ajax seach =====================*/
function searchCampaign(){
	const index = $('#campaignStatus :selected').val();
	var nameStatus = $('#campaignStatus :selected').text();
	const limitItem = $('#limitShowItem').val();
	const current = 1;	
	var keySearch = $('#iSearch').val().trim();
	
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
			url: setPathURL()+"/Donation/admin/actionCampaign",
			data: JSON.stringify(objectData),
			dataType: "json",
			cache: false,
			timeout: 60000,
			success: function(data){
				//console.log(data['campaigns']);
				var html = "", htmlpag = "";
				var campaigns = data['campaigns'];
				const totalRecords = data['totalRecords'];
				const avgPage = totalRecords / limitItem;
				const totalPage = Math.ceil(avgPage);
				
				$.each(campaigns, function(i, value){
					
					html += "<tr>" +
				            "    <td>" +
				            "       <div class='checkbox-del'>" +
				            "          <input class='checkbox-del-group check-del' type='checkbox' onclick='onClickItemCheckBox()'>" +
				            "			<input type='hidden' feild='"+value.campaignId +"'></div>" +
				            "    </td><td class='td-style'>"+value.name+"</td><td>"+formatInteger(value.goal,'de-DE')+"</td>"+
				            "    <td>"+value.dateStart+"</td>"+
				            "    <td>"+value.dateEnd+"</td>"+
				            "    <td><span class='label "+setStyleStatus(index)+"'>"+nameStatus+"</span></td>"+
				            "    <td class='td-action'>"+
				            "        <a href='"+setPathURL()+"/Donation/admin/editCampaign/"+value.campaignId+"' class='edit' title='Chỉnh sửa'><i class='ti-pencil'></i></a>"+
				            "        <a href='#' class='delete' title='Xóa' data='"+value.campaignId+"' onclick='aDelCampaign(this);return false;'><i class='ti-trash'></i></a>"+
				            "    </td></tr>";
					
				});
				$('#mTableCampaign').find('tbody').html(html);
				setActionStatus(index);
				
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
				setActionPagination();
				const cur = $('.pagination-body').find('.pageNumber.pactive a').text();
				$('.hint-text').html('Hiển thị <b>'+(limitItem*cur > totalRecords ? totalRecords : limitItem*cur)+'</b> trong số <b>'+totalRecords+'</b> mục được tìm thấy');
			},
			error: function(e){
				console.log(e);
			}
		});
	}else
		getCampaignOnChange();
	
}

/*get next page result search*/
function getNextPageSearchCampaign(){
	const index = $('#campaignStatus :selected').val();
	var nameStatus = $('#campaignStatus :selected').text();
	const limitItem = $('#limitShowItem').val();
	const current = $('.pagination-body').find('.pageNumber.pactive a').text();
	var keySearch = $('#iSearch').val().trim();
	
	var objectData = {};
	objectData['action'] = 'search';
	objectData['keySearch'] = keySearch;
	objectData['limitItem'] = limitItem;
	objectData['campaignStatus'] = index;
	objectData['current'] = current;
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: setPathURL()+"/Donation/admin/actionCampaign",
		data: JSON.stringify(objectData),
		dataType: "json",
		cache: false,
		timeout: 60000,
		success: function(data){
			//console.log(data['campaigns']);
			var html = "";
			var campaigns = data['campaigns'];
			const totalRecords = data['totalRecords'];
			
			$.each(campaigns, function(i, value){
				
				html += "<tr>" +
			            "    <td>" +
			            "       <div class='checkbox-del'>" +
			            "          <input class='checkbox-del-group check-del' type='checkbox' onclick='onClickItemCheckBox()'>" +
			            "			<input type='hidden' feild='"+value.campaignId +"'></div>" +
			            "    </td><td class='td-style'>"+value.name+"</td><td>"+formatInteger(value.goal,'de-DE')+"</td>"+
			            "    <td>"+value.dateStart+"</td>"+
			            "    <td>"+value.dateEnd+"</td>"+
			            "    <td><span class='label "+setStyleStatus(index)+"'>"+nameStatus+"</span></td>"+
			            "    <td class='td-action'>"+
			            "        <a href='"+setPathURL()+"/Donation/admin/editCampaign/"+value.campaignId+"' class='edit' title='Chỉnh sửa'><i class='ti-pencil'></i></a>"+
			            "        <a href='#' class='delete' title='Xóa' data='"+value.campaignId+"' onclick='aDelCampaign(this);return false;'><i class='ti-trash'></i></a>"+
			            "    </td></tr>";
				
			});
			$('#mTableCampaign').find('tbody').html(html);
			setActionStatus(index);
			
			$('.hint-text').html('Hiển thị <b>'+(limitItem*current > totalRecords ? totalRecords : limitItem*current)+'</b> trong số <b>'+totalRecords+'</b> mục được tìm thấy');
		},
		error: function(e){
			console.log(e);
		}
	});
}

/*========ajax select status campaign=====*/
function getCampaignOnChange(){
	const index = $('#campaignStatus :selected').val();
	var nameStatus = $('#campaignStatus :selected').text();
	const limitItem = $('#limitShowItem').val();
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
		url: setPathURL()+"/Donation/admin/actionCampaign",
		data: JSON.stringify(objectData),
		dataType: "json",
		cache: false,
		timeout: 60000,
		success: function(data){
			//console.log(data['campaigns']);
			var html = "", htmlpag = "";
			var campaigns = data['campaigns'];
			const totalRecords = data['totalRecords'];
			
			const avgPage = totalRecords / limitItem;
			const totalPage = Math.ceil(avgPage);
			
			/*load data*/
			$.each(campaigns, function(i, value){
				
				html += "<tr>" +
			            "    <td>" +
			            "       <div class='checkbox-del'>" +
			            "          <input class='checkbox-del-group check-del' type='checkbox' onclick='onClickItemCheckBox()'>" +
			            "          <input type='hidden' feild='"+value.campaignId +"'></div>" +
			            "    </td><td class='td-style'>"+value.name+"</td><td>"+formatInteger(value.goal,'de-DE')+"</td>"+
			            "    <td>"+value.dateStart+"</td>"+
			            "    <td>"+value.dateEnd+"</td>"+
			            "    <td><span class='label "+setStyleStatus(index)+"'>"+nameStatus+"</span></td>"+
			            "    <td class='td-action'>"+
			            "        <a href='"+setPathURL()+"/Donation/admin/editCampaign/"+value.campaignId+"' class='edit' title='Chỉnh sửa'><i class='ti-pencil'></i></a>"+
			            "        <a href='#' class='delete' title='Xóa' data='"+value.campaignId+"' onclick='aDelCampaign(this);return false;'><i class='ti-trash'></i></a>"+
			            "    </td></tr>";
				
			});
			$('#mTableCampaign').find('tbody').html(html);
			setActionStatus(index);
									
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
			setActionPagination();
			const cur = $('.pagination-body').find('.pageNumber.pactive a').text();
			$('.hint-text').html('Hiển thị <b>'+(limitItem*cur > totalRecords ? totalRecords : limitItem*cur)+'</b> trong số <b>'+totalRecords+'</b> mục');
		},
		error: function(e){
			console.log(e);
		}
	});
}

/*get next / prev page campaign */
function getCampaignPageChange(){
	
	const index = $('#campaignStatus :selected').val();
	var nameStatus = $('#campaignStatus :selected').text();
	const limitItem = $('#limitShowItem').val();
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
		url: setPathURL()+"/Donation/admin/actionCampaign",
		data: JSON.stringify(objectData),
		dataType: "json",
		cache: false,
		timeout: 60000,
		success: function(data){
			//console.log(data['campaigns']);
			var html = "";
			var campaigns = data['campaigns'];
			const totalRecords = data['totalRecords'];
			
			/*load data*/
			$.each(campaigns, function(i, value){
				
				html += "<tr>" +
			            "    <td>" +
			            "       <div class='checkbox-del'>" +
			            "          <input class='checkbox-del-group check-del' type='checkbox' onclick='onClickItemCheckBox()'>" +
			            "          <input type='hidden' feild='"+value.campaignId +"'></div>" +
			            "    </td><td class='td-style'>"+value.name+"</td><td>"+formatInteger(value.goal,'de-DE')+"</td>"+
			            "    <td>"+value.dateStart+"</td>"+
			            "    <td>"+value.dateEnd+"</td>"+
			            "    <td><span class='label "+setStyleStatus(index)+"'>"+nameStatus+"</span></td>"+
			            "    <td class='td-action'>"+
			            "        <a href='"+setPathURL()+"/Donation/admin/editCampaign/"+value.campaignId+"' class='edit' title='Chỉnh sửa'><i class='ti-pencil'></i></a>"+
			            "        <a href='#' class='delete' title='Xóa' data='"+value.campaignId+"' onclick='aDelCampaign(this);return false;'><i class='ti-trash'></i></a>"+
			            "    </td></tr>";
				
			});
			$('#mTableCampaign').find('tbody').html(html);
			setActionStatus(index);
			$('.hint-text').html('Hiển thị <b>'+(limitItem*current > totalRecords ? totalRecords : limitItem*current)+'</b> trong số <b>'+totalRecords+'</b> mục');
		},
		error: function(e){
			console.log(e);
		}
	});
}

/* set reaction of button pagination after load ajax*/
function setActionPagination(){
	
	/*button page number*/
	$('.pageNumber').click(function(){
		$('.pagination-body').find('.pageNumber.pactive').removeClass('pactive');	   
		$(this).addClass('pactive');
		var keySearch = $('#iSearch').val().trim();
		if(keySearch.length > 0){
			getNextPageSearchCampaign();
		}else{
			getCampaignPageChange();
		}
	});
	/*button next page*/
	$('.next').click(function(){	   
		$('.pagination-body').find('.pageNumber.pactive').next().addClass('pactive');
		$('.pagination-body').find('.pageNumber.pactive').prev().removeClass('pactive');
		if($('.pagination-body').find('.pageNumber').length > 0){ 
			var keySearch = $('#iSearch').val().trim();
			if(keySearch.length > 0){
				getNextPageSearchCampaign();
			}else{
				getCampaignPageChange();
			}						
		}
	    	   
	});
	/*button prev page*/
	$('.prev').click(function(){	   
		$('.pagination-body').find('.pageNumber.pactive').prev().addClass('pactive');
		$('.pagination-body').find('.pageNumber.pactive').next().removeClass('pactive');
		if($('.pagination-body').find('.pageNumber').length > 0){    	   
			var keySearch = $('#iSearch').val().trim();
			if(keySearch.length > 0){
				getNextPageSearchCampaign();
			}else{
				getCampaignPageChange();
			}
		}
	});
}

/*set style label status*/
function setStyleStatus(status){
	var mClass;
	switch(status){
	case '0': //chưa kích hoạt
		mClass = "label-warning";
		break;
	case '1': //đang hoạt động
		mClass = "label-primary";
		
		break;
	case '2': //đã hết hạn
		mClass = "label-danger";
		
		break;
	default: //đã hoàn thành
		mClass = "label-success";
		
		break;
	}
	return mClass;
}

function setActionStatus(status){
	switch(status){
	case '0': //chưa kích hoạt		
		$("#checkDelGroup").prop("disabled", false);
		$("#btnDelGroup").prop("disabled", true);
		$(".check-del").prop("disabled", false);
		break;
	case '1': //đang hoạt động
		
		$("#checkDelGroup").prop("disabled", true);
		$("#btnDelGroup").prop("disabled", true);
		$(".check-del").prop("disabled", true);
		$(".delete").removeAttr("href");
		$(".delete").attr('class', 'disabled');
		break;
	case '2': //đã hết hạn		
		$("#checkDelGroup").prop("disabled", true);
		$("#btnDelGroup").prop("disabled", true);
		$("#checkboxdel").prop("disabled", true);
		$(".check-del").prop("disabled", true);
		$(".delete").removeAttr("href");
		$(".delete").attr('class', 'disabled');
		break;
	default: //đã hoàn thành		
		$("#checkDelGroup").prop("disabled", true);
		$("#btnDelGroup").prop("disabled", true);
		$("#checkboxdel").prop("disabled", true);
		$(".check-del").prop("disabled", true);
		$(".edit").removeAttr("href");
		$(".edit").attr('class', 'disabled');
		$(".delete").removeAttr("href");
		$(".delete").attr('class', 'disabled');
		break;
	}
	
	
}

/*a element delete in table*/
function aDelCampaign(e){
	$("div.modal-body").text("Bạn có thực sự muốn xóa?");
	$('#mdata').val(e.getAttribute('data'));
	$('#myModal').addClass("show");
	//$("#model-delete-btn").css({"display": "inline"});
}
/*button delete group*/
function btnDelCampaign(){
	var idData = $('#mdata').val();
	delCampaign(idData);
}

/* ajax delete campaign*/
function delCampaign(id){
	
	$('#myModal').removeClass("show");
	
	const index = $('#campaignStatus :selected').val();
	var nameStatus = $('#campaignStatus :selected').text();
	const limitItem = $('#limitShowItem').val();
	var objectData = {};
	objectData['action'] = 'delete';
	objectData['id'] = id;
	objectData['limitItem'] = limitItem;
	objectData['campaignStatus'] = index;
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: setPathURL()+"/Donation/admin/actionCampaign",
		data: JSON.stringify(objectData),
		dataType: "json",
		cache: false,
		timeout: 60000,
		success: function(data){
			
			var html = "", htmlpag = "";
			var campaigns = data['campaigns'];
			const totalRecords = data['totalRecords'];
			
			const avgPage = totalRecords / limitItem;
			const totalPage = Math.ceil(avgPage);
			
			var message = data['message'];
			var status = data['status'];
			
			$.each(campaigns, function(i, value){
				
				html += "<tr>" +
			            "    <td>" +
			            "       <div class='checkbox-del'>" +
			            "          <input class='checkbox-del-group check-del' type='checkbox' onclick='onClickItemCheckBox(this)'>" +
			            "		   <input type='hidden' feild='"+value.campaignId +"'></div>"+	
			            "    </td><td class='td-style'>"+value.name+"</td><td>"+formatInteger(value.goal,'de-DE')+"</td>"+
			            "    <td>"+value.dateStart+"</td>"+
			            "    <td>"+value.dateEnd+"</td>"+
			            "    <td><span class='label "+setStyleStatus(index)+"'>"+nameStatus+"</span></td>"+
			            "    <td class='td-action'>"+
			            "        <a href='"+setPathURL()+"/Donation/admin/editCampaign/"+value.campaignId+"' class='edit' title='Chỉnh sửa'><i class='ti-pencil'></i></a>"+
			            "        <a href='#' class='delete' title='Xóa' data='"+value.campaignId+"' data-toggle='modal' onclick='aDelCampaign(this);return false;'><i class='ti-trash'></i></a>"+
			            "    </td></tr>";
				
			});
			$('#mTableCampaign').find('tbody').html(html);
			setActionStatus(index);
						
			showBoxMsg(message, status);
			$("#btnDelGroup").prop('disabled', true);			
			
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
			setActionPagination();
			const cur = $('.pagination-body').find('.pageNumber.pactive a').text();
			$('.hint-text').html('Hiển thị <b>'+(limitItem*cur > totalRecords ? totalRecords : limitItem*cur)+'</b> trong số <b>'+totalRecords+'</b> mục');
		},
		error: function(e){			
			showBoxMsg("Lỗi! Không thể xóa.", 1);			
			$("#btnDelGroup").prop('disabled', true);
		}
	});
	$("#checkDelGroup").prop('checked', false);
	
}

/*show alert massage*/
function showBoxMsg(msg, status){
	$("h5.msg-content").text(msg);
	$('div.modal-msg-header i').removeClass();
	if(status == 0)
		$('div.modal-msg-header i').addClass('fa fa-check fa-color-green');
	else
		$('div.modal-msg-header i').addClass('fa fa-times fa-color-red');
	$('#modalMsg').removeAttr("style");
	$('#modalMsg').addClass('mshow');
	setTimeout('$("#modalMsg").fadeOut(500);',2000);
}

/*show alert massage*/
function showBoxMsg_(){	
	$('#msgBox').removeAttr("style");
	$('#msgBox').addClass('mshow');
	setTimeout('$("#msgBox").fadeOut(500);',2000);
}

function onClickItemCheckBox(e){
	
	//var isChecked = $(this).prop("checked");
	var isChecked = e.checked;
    var isbtnChecked = true;
    
    var isHeaderChecked = $("#checkDelGroup").prop("checked");
    if (isChecked == false && isHeaderChecked){
    	$("#checkDelGroup").prop('checked', isChecked);
    	
    }else {
    	$('#mTableCampaign tr:has(td)').find('input[type="checkbox"]').each(function() {
    		if ($(this).prop("checked") == false)
    			isChecked = false;
    	});
    	
    	$("#checkDelGroup").prop('checked', isChecked);
    }
    
    $('#mTableCampaign tr:has(td)').find('input[type="checkbox"]').each(function() {
    	
		if ($(this).prop("checked") == true){
			isbtnChecked = false;    			
		}
	});
	
	$("#btnDelGroup").prop('disabled', isbtnChecked);
}

/*get id campaign in table*/
function getDelCampaignId(){
	var id = "";
	$('#mTableCampaign tbody tr:has(td)').find('input[type=checkbox]:checked').each(function () {		
        var row = $(this).closest("tr").find("td:eq(0) input[type='hidden']").attr('feild');  
        id += row;        
        id += " ";
    });
	return id;
}

/*set class progress bar*/
function setClassProgressBar(w){
	return w >= 100 ? 'bg-donation-completed' : 'bg-donation';
}

/*check email address*/
function isEmail(email) {
	var regex = /^([_a-zA-Z0-9-]+)(\.[_a-zA-Z0-9-]+)*@([a-zA-Z0-9-]+\.)+([a-zA-Z]{2,3})$/;	
	return regex.test(email);
}

/*kiem tra ky tu dac biet*/
function isSpecial(str){
	var chr = /^[a-zA-Z0-9-_]*$/;
	return chr.test(str);
}

/*check number*/
function isNumber(value){
	var filter = /^\d*$/;
	return filter.test(value);
}

/*check phone*/
function isPhone(phone){
	var filter = /^((\+[1-9]{1,4}[ \-]*)|(\([0-9]{2,3}\)[ \-]*)|([0-9]{2,4})[ \-]*)*?[0-9]{3,4}?[ \-]*[0-9]{3,4}?$/;
    return filter.test(phone);
}

/*format integer for number style dos*/
function formatInteger(x, format){
	//new Intl.NumberFormat('de-DE', { style: 'currency', currency: 'EUR' }).format(number));
	//new Intl.NumberFormat('en-IN', { maximumSignificantDigits: 3 }).format(number))
	//new Intl.NumberFormat('ja-JP', { style: 'currency', currency: 'JPY' }).format(number));
	if (typeof Intl === "undefined" || !Intl.NumberFormat) {
		  console.log("This browser doesn't support Intl.NumberFormat");
	} else {
		  var nf = Intl.NumberFormat(format);
		  return nf.format(x); // 42.000.000 in many other locales
	}
}

//set path URL for Ajax when run localhost Tomcat 
function setPathURL(){
	var pathname = window.location.pathname;
	var pt = pathname.split("/");
	var path = "";
	if(pt[1].indexOf(pt[2]) >= 0){
        path = "/"+pt[1];
	}
	return path;
}




