/* check button submit create user form*/
$("#f-user-create").submit(function(e){
	//e.preventDefault();
    var username = $("#username"),
        password = $("#password"),
        repassword = $("#repassword"),
        email = $("#email"),
        phone = $('#phone'),
        msg = $("#msg");
    //var regex = /^([_a-zA-Z0-9-]+)(\.[_a-zA-Z0-9-]+)*@([a-zA-Z0-9-]+\.)+([a-zA-Z]{2,3})$/;
    
    if(username.val().trim().length < 5 || username.val().trim().length > 20){
        msg.html("Tên đăng nhập phải có độ dài lớn hơn 5 và nhỏ hơn 20 ký tự.");
        username.focus();
        return false;
    }
    
    if(!isSpecial(username.val())){
    	msg.html("Tên đăng nhập chỉ chứa các ký tự a-z, A-Z, 0-9.");
        username.focus();
        return false;
    }
    /*
    if(password.val().trim().length < 8){
        msg.html("Mật khẩu phải có độ dài lớn hơn 8 ký tự.");
        password.focus();
        return false;
    }
    
    if(repassword.val() !== password.val()){
    	msg.html("Mật khẩu không giống nhau.");
        repassword.focus();
        return false;
    }
    */
    if(email.val().trim().length < 1){
        msg.html("Mời địa chỉ email.");
        email.focus();
        return false;
    }
    
    if(!isEmail(email.val().trim())){
    	msg.html("Email không hợp lệ.");
    	email.focus();
        return false;
    }
        
    
    if(phone.val().trim().length < 10 || phone.val().trim().length > 10 || !isPhone(phone.val().trim())){
    	msg.html("Số điện thoại không hợp lệ.");
        phone.focus()
        return false;
    }
    
    return true;
});

/*ajax get all user*/
function getUsers(){
	
	const limitItem = $('#limitShowItemUser').val();
	
	const current = 1;
	var objectData = {};
	objectData['action'] = 'get';
	objectData['limitItem'] = limitItem;
	objectData['current'] = current;
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: setPathURL()+"/Donation/admin/actionUser",
		data: JSON.stringify(objectData),
		dataType: "json",
		cache: false,
		timeout: 60000,
		success: function(data){
			//console.log(data);
			var html = "", htmlpag = "";
			var users = data['users'];
			const totalRecords = data['totalRecords'];
			
			const avgPage = totalRecords / limitItem;
			const totalPage = Math.ceil(avgPage);
			
			/*load data*/
			
			$.each(users, function(i, value){
				
				html += "<tr>" +                
	                "<td class='td-style-user'>"+value.username+"</td>"+
	                "<td>"+value.email+"</td>"+
	                "<td>"+value.phone+"</td>"+
	                "<td>"+setRole(value.role)+"</td>"+
	                "<td>"+setStatus(value.status)+"</td>"+
	                "<td class='td-action'>"+
	                	"<a href='"+setPathURL()+"/Donation/admin/editUser/" + value.id +"' class='"+setActionRoleClass(value.role,"disabled", "edit")+"' title='Chỉnh sửa'><i class='ti-pencil'></i></a>"+
	                	"<a href='#' class='"+setActionRoleClass(value.status,"disabled", "reset")+"' title='Reset mật khẩu' data-name='"+value.username+"' status='"+value.status+"' data='"+value.id+"' onclick='resetPassword(this);return false;'><i class='ti-eraser'></i></a>"+
	                	"<a href='#' class='"+setActionRoleClass(value.role,"disabled", "delete")+"' data-name='"+value.username+"' status='"+value.status+"' title='Trạng thái' data='"+value.id+"' onclick='setActiveUser(this);return false;'><i class='"+setActionRoleClass(value.status,"ti-lock", "ti-unlock")+"'></i></a>"+
	                	"<a href='#' class='"+setActionRoleClass(value.role,"disabled", "delete")+"' title='Xóa' data-name='"+value.username+"' data='"+value.id+"' onclick='delUser(this);return false;'><i class='ti-trash'></i></a>"+
	                "</td></tr>";
				
			});
			$('#mTableUser').find('tbody').html(html);
									
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
			setActionUserPagination();
			
			const cur = $('.pagination-body').find('.pageNumber.pactive a').text();
			$('.hint-text').html('Hiển thị <b>'+(limitItem*cur > totalRecords ? totalRecords : limitItem*cur)+'</b> trong số <b>'+totalRecords+'</b> mục');
			
		},
		error: function(e){
			console.log(e);
		}
	});
}

/*get page change*/
function getUserPageChange(){
	
	const limitItem = $('#limitShowItemUser').val();
	const current = $('.pagination-body').find('.pageNumber.pactive a').text();
	var objectData = {};
	objectData['action'] = 'get';	
	objectData['limitItem'] = limitItem;
	objectData['current'] = current;
	
	//var style = setStyleStatus(index);
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: setPathURL()+"/Donation/admin/actionUser",
		data: JSON.stringify(objectData),
		dataType: "json",
		cache: false,
		timeout: 60000,
		success: function(data){
			//console.log(data['campaigns']);
			var html = "";
			var campaigns = data['users'];
			const totalRecords = data['totalRecords'];
			
			/*load data*/
			$.each(campaigns, function(i, value){
				
				html += "<tr>" +                
                "<td class='td-style-user'>"+value.username+"</td>"+
                "<td>"+value.email+"</td>"+
                "<td>"+value.phone+"</td>"+
                "<td>"+setRole(value.role)+"</td>"+
                "<td>"+setStatus(value.status)+"</td>"+
                "<td class='td-action'>"+
                	"<a href='"+setPathURL()+"/Donation/admin/editUser/" + value.id +"' class='"+setActionRoleClass(value.role,"disabled", "edit")+"' title='Chỉnh sửa'><i class='ti-pencil'></i></a>"+
                	"<a href='#' class='"+setActionRoleClass(value.status,"disabled", "reset")+"' title='Reset mật khẩu' data-name='"+value.username+"' status='"+value.status+"' data='"+value.id+"' onclick='resetPassword(this);return false;'><i class='ti-eraser'></i></a>"+
                	"<a href='#' class='"+setActionRoleClass(value.role,"disabled", "delete")+"' data-name='"+value.username+"' status='"+value.status+"' title='Trạng thái' data='"+value.id+"' onclick='setActiveUser(this);return false;'><i class='"+setActionRoleClass(value.status,"ti-lock", "ti-unlock")+"'></i></a>"+
                	"<a href='#' class='"+setActionRoleClass(value.role,"disabled", "delete")+"' title='Xóa' data-name='"+value.username+"' data='"+value.id+"' onclick='delUser(this);return false;'><i class='ti-trash'></i></a>"+
                "</td></tr>";
				
			});
			$('#mTableUser').find('tbody').html(html);
			
			$('.hint-text').html('Hiển thị <b>'+(limitItem*current > totalRecords ? totalRecords : limitItem*current)+'</b> trong số <b>'+totalRecords+'</b> mục');
		},
		error: function(e){
			console.log(e);
		}
	});
}

/*ajax serach user*/
function searchUser(){
	
	const limitItem = $('#limitShowItemUser').val();
	const current = 1;	
	var keySearch = $('#iSearchUser').val().trim();
	
	var objectData = {};
	objectData['action'] = 'search';
	objectData['keySearch'] = keySearch;
	objectData['limitItem'] = limitItem;
	objectData['current'] = current;
	
	
	//e.preventDefault();
	if(keySearch.length > 0){
		$.ajax({
			type: "POST",
			contentType: "application/json",
			url: setPathURL()+"/Donation/admin/actionUser",
			data: JSON.stringify(objectData),
			dataType: "json",
			cache: false,
			timeout: 60000,
			success: function(data){
				//console.log(data['campaigns']);
				var html = "", htmlpag = "";
				var campaigns = data['users'];
				const totalRecords = data['totalRecords'];
				const avgPage = totalRecords / limitItem;
				const totalPage = Math.ceil(avgPage);
				
				$.each(campaigns, function(i, value){
					
					html += "<tr>" +                
	                "<td class='td-style-user'>"+value.username+"</td>"+
	                "<td>"+value.email+"</td>"+
	                "<td>"+value.phone+"</td>"+
	                "<td>"+setRole(value.role)+"</td>"+
	                "<td>"+setStatus(value.status)+"</td>"+
	                "<td class='td-action'>"+
	                	"<a href='"+setPathURL()+"/Donation/admin/editUser/" + value.id +"' class='"+setActionRoleClass(value.role,"disabled", "edit")+"' title='Chỉnh sửa'><i class='ti-pencil'></i></a>"+
	                	"<a href='#' class='"+setActionRoleClass(value.status,"disabled", "reset")+"' title='Reset mật khẩu' data-name='"+value.username+"' status='"+value.status+"' data='"+value.id+"' onclick='resetPassword(this);return false;'><i class='ti-eraser'></i></a>"+
	                	"<a href='#' class='"+setActionRoleClass(value.role,"disabled", "delete")+"' data-name='"+value.username+"' status='"+value.status+"' title='Trạng thái' data='"+value.id+"' onclick='setActiveUser(this);return false;'><i class='"+setActionRoleClass(value.status,"ti-lock", "ti-unlock")+"'></i></a>"+
	                	"<a href='#' class='"+setActionRoleClass(value.role,"disabled", "delete")+"' title='Xóa' data-name='"+value.username+"' data='"+value.id+"' onclick='delUser(this);return false;'><i class='ti-trash'></i></a>"+
	                "</td></tr>";
					
				});
				$('#mTableUser').find('tbody').html(html);
				
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
				setActionUserPagination();
				
				const cur = $('.pagination-body').find('.pageNumber.pactive a').text();
				$('.hint-text').html('Hiển thị <b>'+(limitItem*cur > totalRecords ? totalRecords : limitItem*cur)+'</b> trong số <b>'+totalRecords+'</b> mục được tìm thấy');
			},
			error: function(e){
				console.log(e);
			}
		});
	}else
		getUsers();
}

/*ajax get user next page search*/
function getUserNextPageSearch(){
	const limitItem = $('#limitShowItemUser').val();
	const current = $('.pagination-body').find('.pageNumber.pactive a').text();
	var keySearch = $('#iSearchUser').val().trim();
	
	var objectData = {};
	objectData['action'] = 'search';
	objectData['keySearch'] = keySearch;
	objectData['limitItem'] = limitItem;
	objectData['current'] = current;
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: setPathURL()+"/Donation/admin/actionUser",
		data: JSON.stringify(objectData),
		dataType: "json",
		cache: false,
		timeout: 60000,
		success: function(data){
			//console.log(data['campaigns']);
			var html = "";
			var campaigns = data['users'];
			const totalRecords = data['totalRecords'];
			
			$.each(campaigns, function(i, value){
				
				html += "<tr>" +                
                "<td class='td-style-user'>"+value.username+"</td>"+
                "<td>"+value.email+"</td>"+
                "<td>"+value.phone+"</td>"+
                "<td>"+setRole(value.role)+"</td>"+
                "<td>"+setStatus(value.status)+"</td>"+
                "<td class='td-action'>"+
                	"<a href='"+setPathURL()+"/Donation/admin/editUser/" + value.id +"' class='"+setActionRoleClass(value.role,"disabled", "edit")+"' title='Chỉnh sửa'><i class='ti-pencil'></i></a>"+
                	"<a href='#' class='"+setActionRoleClass(value.status,"disabled", "reset")+"' title='Reset mật khẩu' data-name='"+value.username+"' status='"+value.status+"' data='"+value.id+"' onclick='resetPassword(this);return false;'><i class='ti-eraser'></i></a>"+
                	"<a href='#' class='"+setActionRoleClass(value.role,"disabled", "delete")+"' data-name='"+value.username+"' status='"+value.status+"' title='Trạng thái' data='"+value.id+"' onclick='setActiveUser(this);return false;'><i class='"+setActionRoleClass(value.status,"ti-lock", "ti-unlock")+"'></i></a>"+
                	"<a href='#' class='"+setActionRoleClass(value.role,"disabled", "delete")+"' title='Xóa' data-name='"+value.username+"' data='"+value.id+"' onclick='delUser(this);return false;'><i class='ti-trash'></i></a>"+
                "</td></tr>";
				
			});
			$('#mTableUser').find('tbody').html(html);
			
			$('.hint-text').html('Hiển thị <b>'+(limitItem*current > totalRecords ? totalRecords : limitItem*current)+'</b> trong số <b>'+totalRecords+'</b> mục được tìm thấy');
		},
		error: function(e){
			console.log(e);
		}
	});
}

/*set action button pagination*/
function setActionUserPagination(){
	/*button page number*/
	   $('.pageNumber').click(function(){
		   $('.pagination-body').find('.pageNumber.pactive').removeClass('pactive');	   
		   $(this).addClass('pactive');
		   var keySearch = $('#iSearchUser').val().trim();
		   if(keySearch.length > 0){
			   getUserNextPageSearch();
		   }else
			   getUserPageChange();		   
	   });
	   
	   /*button next page*/
	   $('.next').click(function(){	   
	       $('.pagination-body').find('.pageNumber.pactive').next().addClass('pactive');
	       $('.pagination-body').find('.pageNumber.pactive').prev().removeClass('pactive');
	       if($('.pagination-body').find('.pageNumber').length > 0){  
	    	   
	    	   //if($('.pagination-body').attr('ul-click') == 'user'){
	    	   var keySearch = $('#iSearchUser').val().trim();
			   if(keySearch.length > 0){
				   getUserNextPageSearch();
			   }else{
				   getUserPageChange();
			   }
	       }
	    	   
	   });
	   /*button prev page*/
	   $('.prev').click(function(){	   
	       $('.pagination-body').find('.pageNumber.pactive').prev().addClass('pactive');
	       $('.pagination-body').find('.pageNumber.pactive').next().removeClass('pactive');
	       if($('.pagination-body').find('.pageNumber').length > 0){    	   
	    	   var keySearch = $('#iSearchUser').val().trim();
			   if(keySearch.length > 0){
				   getUserNextPageSearch();
			   }else{
				   getUserPageChange();
			   }
	       }
	   });
}

/*action delete or active user*/
function btnActionUser(){
	var action = $('.modal-title').attr('data-title');
	if(action === 'Active'){
		activeUser();
	}else if(action === 'Delete'){
		deleteUser();
	}
}

/*show box when submit delete user */
function delUser(e){	
	$('.modal-title').html('<i class="fa fa-exclamation-triangle"></i>Delete');
	$('.modal-title').attr('data-title', 'Delete');
	$("div.modal-body").html("Bạn có thực sự muốn xóa tài khoản <b>"+e.getAttribute('data-name')+"</b> ?");
	$('#mdata').val(e.getAttribute('data'));
	$('#mstatus').val(e.getAttribute('status'));
	$('#myModal').addClass("show");
}

/*action delete user */
function deleteUser(){
	const limitItem = $('#limitShowItemUser').val();
	const id = $('#mdata').val();
	var current = $('.pagination-body').find('.pageNumber.pactive a').text();
	var objectData = {};
	objectData['action'] = 'deleteUser';
	objectData['id'] = id;
	objectData['limitItem'] = limitItem;
	objectData['current'] = current;
	
	$('#myModal').removeClass("show");
	$('#mdata').val("");
	$('#mstatus').val("");
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: setPathURL()+"/Donation/admin/actionUser",
		data: JSON.stringify(objectData),
		dataType: "json",
		cache: false,
		timeout: 60000,
		success: function(data){
			
			var html = "", htmlpag = "";
			var campaigns = data['users'];
			const totalRecords = data['totalRecords'];
			
			const avgPage = totalRecords / limitItem;
			const totalPage = Math.ceil(avgPage);
			
			var message = data['message'];
			
			$.each(campaigns, function(i, value){
				
				html += "<tr>" +                
                "<td class='td-style-user'>"+value.username+"</td>"+
                "<td>"+value.email+"</td>"+
                "<td>"+value.phone+"</td>"+
                "<td>"+setRole(value.role)+"</td>"+
                "<td>"+setStatus(value.status)+"</td>"+
                "<td class='td-action'>"+
                	"<a href='"+setPathURL()+"/Donation/admin/editUser/" + value.id +"' class='"+setActionRoleClass(value.role,"disabled", "edit")+"' title='Chỉnh sửa'><i class='ti-pencil'></i></a>"+
                	"<a href='#' class='"+setActionRoleClass(value.status,"disabled", "reset")+"' title='Reset mật khẩu' data-name='"+value.username+"' status='"+value.status+"' data='"+value.id+"' onclick='resetPassword(this);return false;'><i class='ti-eraser'></i></a>"+
                	"<a href='#' class='"+setActionRoleClass(value.role,"disabled", "delete")+"' data-name='"+value.username+"' status='"+value.status+"' title='Trạng thái' data='"+value.id+"' onclick='setActiveUser(this);return false;'><i class='"+setActionRoleClass(value.status,"ti-lock", "ti-unlock")+"'></i></a>"+
                	"<a href='#' class='"+setActionRoleClass(value.role,"disabled", "delete")+"' title='Xóa' data-name='"+value.username+"' data='"+value.id+"' onclick='delUser(this);return false;'><i class='ti-trash'></i></a>"+
                "</td></tr>";
				
			});
			$('#mTableUser').find('tbody').html(html);
						
			showBoxMsg(message, 0);
						
			/*load pagination*/
			htmlpag += "<li><a href='#' onclick='return false' class='prev'><<</a></li>";
			if(totalPage < current) current = totalPage;
			for(var i = 1; i <= totalPage; i++){
				
				if(i == current)
					htmlpag += "<li class='pageNumber pactive'><a href='#' onclick='return false'>"+i+"</a></li>";				
				else
                    htmlpag += "<li class='pageNumber'><a href='#' onclick='return false'>"+i+"</a></li>";                          
			}
			
			htmlpag += "<li><a href='#' onclick='return false' class='next'>>></a></li>";
			$('.pagination-body').html(htmlpag);
			setActionUserPagination();
			const cur = $('.pagination-body').find('.pageNumber.pactive a').text();
			$('.hint-text').html('Hiển thị <b>'+(limitItem*cur > totalRecords ? totalRecords : limitItem*cur)+'</b> trong số <b>'+totalRecords+'</b> mục');
		},
		error: function(e){			
			showBoxMsg("Lỗi kết nối.", 1);			
			
		}
	});
}

/*open box active user*/
function setActiveUser(e){
	$('.modal-title').html('<i class="fa fa-exclamation-triangle"></i>Active');
	$('.modal-title').attr('data-title', 'Active');
	$('#mdata').val(e.getAttribute('data'));
	$('#mstatus').val(e.getAttribute('status'));
	
	var ms = "";
	if(e.getAttribute('status') == 0)
		ms = "mở khóa";
	else ms = "khóa";
	$("div.modal-body").html("Bạn có thực sự muốn "+ms+" tài khoản <b>"+e.getAttribute('data-name')+"</b> ?");
	
	$('#myModal').addClass("show");
}

/*active user*/
function activeUser(){	
	const limitItem = $('#limitShowItemUser').val();
	const id = $('#mdata').val();
	const status = $('#mstatus').val();
	const current = $('.pagination-body').find('.pageNumber.pactive a').text();
	var objectData = {};
	objectData['action'] = 'activeUser';
	objectData['id'] = id;
	objectData['limitItem'] = limitItem;
	objectData['userStatus'] = status;
	objectData['current'] = current;
	
	$('#myModal').removeClass("show");
	$('#mdata').val("");
	$('#mstatus').val("");
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: setPathURL()+"/Donation/admin/actionUser",
		data: JSON.stringify(objectData),
		dataType: "json",
		cache: false,
		timeout: 60000,
		success: function(data){
			
			var html = "", htmlpag = "";
			var campaigns = data['users'];
			const totalRecords = data['totalRecords'];
			
			const avgPage = totalRecords / limitItem;
			const totalPage = Math.ceil(avgPage);
			
			var message = data['message'];
			
			$.each(campaigns, function(i, value){
				
				html += "<tr>" +                
                "<td class='td-style-user'>"+value.username+"</td>"+
                "<td>"+value.email+"</td>"+
                "<td>"+value.phone+"</td>"+
                "<td>"+setRole(value.role)+"</td>"+
                "<td>"+setStatus(value.status)+"</td>"+
                "<td class='td-action'>"+
                	"<a href='"+setPathURL()+"/Donation/admin/editUser/" + value.id +"' class='"+setActionRoleClass(value.role,"disabled", "edit")+"' title='Chỉnh sửa'><i class='ti-pencil'></i></a>"+
                	"<a href='#' class='"+setActionRoleClass(value.status,"disabled", "reset")+"' title='Reset mật khẩu' data-name='"+value.username+"' status='"+value.status+"' data='"+value.id+"' onclick='resetPassword(this);return false;'><i class='ti-eraser'></i></a>"+
                	"<a href='#' class='"+setActionRoleClass(value.role,"disabled", "delete")+"' data-name='"+value.username+"' status='"+value.status+"' title='Trạng thái' data='"+value.id+"' onclick='setActiveUser(this);return false;'><i class='"+setActionRoleClass(value.status,"ti-lock", "ti-unlock")+"'></i></a>"+
                	"<a href='#' class='"+setActionRoleClass(value.role,"disabled", "delete")+"' title='Xóa' data-name='"+value.username+"' data='"+value.id+"' onclick='delUser(this);return false;'><i class='ti-trash'></i></a>"+
                "</td></tr>";
				
			});
			$('#mTableUser').find('tbody').html(html);
						
			showBoxMsg(message, 0);
						
			/*load pagination*/
			htmlpag += "<li><a href='#' onclick='return false' class='prev'><<</a></li>";
			for(var i = 1; i <= totalPage; i++){				
				if(i == current)
					htmlpag += "<li class='pageNumber pactive'><a href='#' onclick='return false'>"+i+"</a></li>";
				else
                    htmlpag += "<li class='pageNumber'><a href='#' onclick='return false'>"+i+"</a></li>";                          
			}
			
			htmlpag += "<li><a href='#' onclick='return false' class='next'>>></a></li>";
			$('.pagination-body').html(htmlpag);
			setActionUserPagination();
			const cur = $('.pagination-body').find('.pageNumber.pactive a').text();
			$('.hint-text').html('Hiển thị <b>'+(limitItem*cur > totalRecords ? totalRecords : limitItem*cur)+'</b> trong số <b>'+totalRecords+'</b> mục');
		},
		error: function(e){			
			showBoxMsg("Lỗi kết nối.", 1);			
			
		}
	});
}

/*reset mật khẩu*/
function resetPassword(e){	
	
	const limitItem = $('#limitShowItemUser').val();
	const id = e.getAttribute('data');
	const status = e.getAttribute('status');
	const current = $('.pagination-body').find('.pageNumber.pactive a').text();
	var objectData = {};
	objectData['action'] = 'resetPassword';
	objectData['id'] = id;
	objectData['limitItem'] = limitItem;
	objectData['userStatus'] = status;
	objectData['current'] = current;
	
	$('#myModal').removeClass("show");
	$('#mdata').val("");
	$('#mstatus').val("");
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: setPathURL()+"/Donation/admin/actionUser",
		data: JSON.stringify(objectData),
		dataType: "json",
		cache: false,
		timeout: 60000,
		success: function(data){
			
			var html = "", htmlpag = "";
			var campaigns = data['users'];
			const status = data['status'];
			const totalRecords = data['totalRecords'];
			
			const avgPage = totalRecords / limitItem;
			const totalPage = Math.ceil(avgPage);
			
			var message = data['message'];
			
			$.each(campaigns, function(i, value){
				
				html += "<tr>" +                
                "<td class='td-style-user'>"+value.username+"</td>"+
                "<td>"+value.email+"</td>"+
                "<td>"+value.phone+"</td>"+
                "<td>"+setRole(value.role)+"</td>"+
                "<td>"+setStatus(value.status)+"</td>"+
                "<td class='td-action'>"+
                	"<a href='"+setPathURL()+"/Donation/admin/editUser/" + value.id +"' class='"+setActionRoleClass(value.role,"disabled", "edit")+"' title='Chỉnh sửa'><i class='ti-pencil'></i></a>"+
                	"<a href='#' class='"+setActionRoleClass(value.status,"disabled", "reset")+"' title='Reset mật khẩu' data-name='"+value.username+"' status='"+value.status+"' data='"+value.id+"' onclick='resetPassword(this);return false;'><i class='ti-eraser'></i></a>"+
                	"<a href='#' class='"+setActionRoleClass(value.role,"disabled", "delete")+"' data-name='"+value.username+"' status='"+value.status+"' title='Trạng thái' data='"+value.id+"' onclick='setActiveUser(this);return false;'><i class='"+setActionRoleClass(value.status,"ti-lock", "ti-unlock")+"'></i></a>"+
                	"<a href='#' class='"+setActionRoleClass(value.role,"disabled", "delete")+"' title='Xóa' data-name='"+value.username+"' data='"+value.id+"' onclick='delUser(this);return false;'><i class='ti-trash'></i></a>"+
                "</td></tr>";
				
			});
			$('#mTableUser').find('tbody').html(html);
						
			showBoxMsg(message, status);
						
			/*load pagination*/
			htmlpag += "<li><a href='#' onclick='return false' class='prev'><<</a></li>";
			for(var i = 1; i <= totalPage; i++){				
				if(i == current)
					htmlpag += "<li class='pageNumber pactive'><a href='#' onclick='return false'>"+i+"</a></li>";
				else
                    htmlpag += "<li class='pageNumber'><a href='#' onclick='return false'>"+i+"</a></li>";                          
			}
			
			htmlpag += "<li><a href='#' onclick='return false' class='next'>>></a></li>";
			$('.pagination-body').html(htmlpag);
			setActionUserPagination();
			const cur = $('.pagination-body').find('.pageNumber.pactive a').text();
			$('.hint-text').html('Hiển thị <b>'+(limitItem*cur > totalRecords ? totalRecords : limitItem*cur)+'</b> trong số <b>'+totalRecords+'</b> mục');
		},
		error: function(e){			
			showBoxMsg("Lỗi kết nối.", 1);			
			
		}
	});
}

/*set class role*/
function setRole(role){
	var cl = "";
	switch(role){
		case 0:
			cl = "<span class='td-border-root'>root admin</span>";
			break;
		case 1:
			cl = "<span class='td-border-admin'>admin</span>";
			break;
		default:
			cl = "<span class='td-border-user'>user</span>";
			break;			
	}
	return cl;
}

function setStatus(status){
	var sp = "";
	switch(status){
		case 0:
			sp = "<span class='label label-danger m-status'>Tạm khóa</span>";
			break;
		case 1:
			sp = "<span class='label label-primary m-status'>Đang hoạt động</span>";
			break;
	}
	return sp;
}

function setActionRoleClass(role, cl1, cl2){
	return role == 0 ? cl1 : cl2;
}