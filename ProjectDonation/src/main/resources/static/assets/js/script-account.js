$(document).ready(function(){
	
	$('#pass_new').on('keyup', function(e) {
		if($('#pass_new').val() != '' && $('#pass_new').val().trim().length > 0){
			// Must have capital letter, numbers and lowercase letters
			var strongRegex = new RegExp("^(?=.{8,})(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\\W).*$", "g");
			 
			// Must have either capitals and lowercase letters or lowercase and numbers
			var mediumRegex = new RegExp("^(?=.{8,})(((?=.*[A-Z])(?=.*[a-z]))|((?=.*[A-Z])(?=.*[0-9]))|((?=.*[a-z])(?=.*[0-9]))).*$", "g");
			 
			// Must be at least 6 characters long
			var okRegex = new RegExp("(?=.{8,}).*", "g");
			 
			if (okRegex.test($(this).val()) === false) {
			// If ok regex doesn't match the password
				$('#msg_pass_new').html('Mật khẩu phải có độ dài lớn hơn 7 ký tự.');
				return false;
			} else if (strongRegex.test($(this).val())) {
			// If reg ex matches strong password
				$('#msg_pass_new').html('Mật khẩu tốt!');
			} else if (mediumRegex.test($(this).val())) {
			// If medium password matches the reg ex
				$('#msg_pass_new').html('Làm cho mật khẩu của bạn mạnh hơn với chữ viết hoa, số và ký tự đặc biệt!');
			} else {
			// If password is ok
				$('#msg_pass_new').html('Mật khẩu yếu, hãy thử sử dụng số, chữ viết hoa!');
			}
			return true;
		}else{
			$('#msg_pass_new').html('');
		}
	});
	
	/*choose all item*/
	   $('#checkDelGroup_Donation').click(function() {
		    var isChecked = $(this).prop("checked");
		    $('#mTableHisDonation tr:has(td)').find('input[type="checkbox"]').prop('checked', isChecked);
		    if(isChecked)
		    	$("#btnDelGroup_Donation").prop('disabled', false);
		    else
		    	$("#btnDelGroup_Donation").prop('disabled', true);
	   });
	   /*click check box tren tung dong cua table*/
	   $('#mTableHisDonation tr:has(td)').find('input[type="checkbox"]').click(function() {
		    var isChecked = $(this).prop("checked");
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
	   });
	   
	//button delete group
	$('#btnDelGroup_Donation').on('click', function(){
		deleteGeneralDonation();
	}); 
	
	$('#choces_avatar').click(function(){ $('#imgupload').trigger('click'); });
	

    $('#imgupload').change(function(){
    	$('#update_avatar').css('display', 'block');
    	readURL_img(this, $('#img_avatar'));
    });
	
	/*set active for path*/
	   (function(){
	       let pathname = window.location.pathname;
	       let pathnames = pathname.split("/");
	       var i = 3;
	       if(setPathURL() != ""){
	    	   i = 4;
	       }
	       $('.mCB_container').find('li').each(function(){
    		   var data = $(this).attr('data');
    		   if(pathnames[i].toLowerCase().indexOf(data) != -1){
    			   $(this).addClass('active');
    			   return false;
    		   }
    		    		   
    	   });
	       
	      
	   })();
	
});