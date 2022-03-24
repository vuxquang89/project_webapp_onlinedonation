$(document).ready(function(){
    var date = new Date();
    var $window = $(window);
    //add id to main menu for mobile menu start
    var getBody = $("body");
    var bodyClass = getBody[0].className;
    $(".main-menu").attr('id', bodyClass);
    //add id to main menu for mobile menu end


    $(".mobile-options").on('click', function() {
        $(".navbar-container .nav-right").slideToggle('slow');
    });

    $(".ti-angle-down").on('click', function(){
        $(".navbar-container .show-notification").slideToggle('slow');
    });

    $("#mobile-collapse").on('click', function(){
        $(this).parent().find(".menu-icon").toggleClass("is-clicked");
        $(".div-navbar").toggle("slide");
        //$(".div-navbar").css("display", "block");
        //$(".div-navbar").toggleClass("showActive");
        var d = $(".mcontainer").attr("vertical-effect");
        switch(d){
            case 'overlay':
                $(".mcontainer").attr("vertical-effect", "pink");
                break;
            case 'pink':
                $(".mcontainer").attr("vertical-effect", "overlay");
                break;
        }
        //$(".overlay-box").removeClass( "hiddenActive" ).addClass("showActive");
        
    });

    /*===========datepicker==============*/
    var day = ("0" + date.getDate()).slice(-2);
    var month = ("0" + (date.getMonth() + 1)).slice(-2);
    var today = day + "-" + month + "-" + date.getFullYear();
    var d = new Date();
    //$("#from-date").val(today);
    $("#from-datepicker").datepicker({ 
        autoclose: true, 
        todayHighlight: true,
        startDate: date      
    }).on('changeDate', function(selected){
        startDate = new Date(selected.date.valueOf());
        startDate.setDate(startDate.getDate(new Date(selected.date.valueOf())));
        $("#to-datepicker").datepicker('setStartDate', startDate);
    });
    d.setDate(date.getDate() + 1);
    
    $("#to-datepicker").datepicker({
        format: "dd-mm-yyyy",
        autoclose: true, 
        todayHighlight: true,
        startDate: d
    }).on('changeDate', function(selected){
        fromEndDate = new Date(selected.date.valueOf());
        fromEndDate.setDate(fromEndDate.getDate(new Date(selected.date.valueOf())));
        $("#from-datepicker").datepicker("setEndDate", fromEndDate);
    });
    
    /*set datetime donation*/
    $("#update-from-datepicker").datepicker({ 
        autoclose: true, 
        todayHighlight: true,
        //startDate: date      
    });
    
    /*set datetime history donation*/
    //$("#aDonation_date").val(today);
    $("#his-from-datepicker").datepicker({ 
        autoclose: true, 
        todayHighlight: true
    }).on('changeDate', function(selected){
    	getHisDonationOnChange();
    });

    /*===========datepicker end==============*/

    $(window).resize(function() {
        totalwidth = $(window)[0].innerWidth;
        if (totalwidth >= 768 && totalwidth <= 992) {
            $(".mcontainer").attr("device-type", "tablet");
        } else if (totalwidth < 768) {
            $(".mcontainer").attr("device-type", "phone");
        } else {
            $(".mcontainer").attr("device-type", "desktop");
            $(".navbar-container .show-notification").removeAttr("style");
            $(".navbar-container .nav-right").removeAttr("style");
        }

        if($(".menu-icon").hasClass("is-clicked")){
            var type = $(".mcontainer").attr("device-type");
            switch(type){
                case 'tablet':
                case 'phone':
                    $(".mcontainer").attr("vertical-effect", "overlay");
                    break;
                default:
                    $(".mcontainer").attr("vertical-effect", "pink");
                    break;
            }
        }else if($(".mcontainer").attr("device-type") == "desktop"){
            $(".div-navbar").removeAttr("style");
        }
        
        /*
        dt = $('#' + oid).attr('pcoded-device-type')
        if (dt == 'desktop' && tw < 992) {
            devicesize();
        } else if (dt == 'phone' && tw > 768) {
            devicesize();
        } else if (dt == 'tablet' && tw < 768) {
            devicesize();
        } else if (dt == 'tablet' && tw > 992) {
            devicesize();
        }
        */
    });

    /*============upload image==========*/
    $(document).on('change', '.btn-file :file', function(){
        var input = $(this),
            label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
        input.trigger('fileselect', [label]);
    });

    $('.btn-file :file').on('fileselect', function(event, label){
        var input = $(this).parents('.input-group').find(':text'),
            log = label;
            
        if(input.length){
            
            input.val(log);
        }else{
            if(log) alert(log);
        }
    });

    function readURL(input){
        if(input.files && input.files[0]){
            var reader = new FileReader();
            reader.onload = function(e){
                $('#img-upload').attr('src', e.target.result);
            }

            reader.readAsDataURL(input.files[0]);
        }
    }

    $('#imgInp').change(function(){
        readURL(this);
    });

   /*option campaign status change*/
   $('#campaignStatus').on('change', function(){
	   var keySearch = $('#iSearch').val().trim();
		if(keySearch.length > 0){
			searchCampaign();
		}else{
			getCampaignOnChange();
		}
   });
   
   /*option campaign status of donation change*/
   $('#campaignStatus_Donation').on('change', function(){
	   var keySearch = $('#iSearchDonation').val().trim();
		if(keySearch.length > 0){
			searchDonation();
		}else{
			getDonationOnChange();
		}
   });
   
   /*option show limit item donation change*/
   $('#limitShowItemDonation').on('change', function(){
	   var keySearch = $('#iSearchDonation').val().trim();
		if(keySearch.length > 0){
			searchDonation();
		}else{
			getDonationOnChange();
		}
   });
   
   /*option show limit item campaign change*/
   $('#limitShowItem').on('change', function(){
	   var keySearch = $('#iSearch').val().trim();
		if(keySearch.length > 0){
			searchCampaign();
		}else{
			getCampaignOnChange();
		}
   });
   
   /*option show limit item user change*/
   $('#limitShowItemUser').on('change', function(){
	   var keySearch = $('#iSearchUser').val().trim();
		if(keySearch.length > 0){
			searchUser();
		}else{
			getUsers();
		}
   });
   
   /*option show limit item donation change*/
   $('#limitShowItemHisDonation').on('change', function(){
	   var keySearch = $('#iSearchHisDonation').val().trim();
		if(keySearch.length > 0){
			searchHistoryDonation();
		}else{
			getHisDonationOnChange();
		}
   });
   /**==============table manager campaign =========**/
   /*
   $('#mTableCampaign').on('click','.td-style', function(){
	   var tr = $(this).closest("tr"); //truy cập đến dòng đc chọn
	   tr.find("input[type=checkbox]").attr("checked", true);
	   //$(this).find("input[type=checkbox]").attr("checked", true);
   });
   */
   /*choose all item*/
   $('#checkDelGroup').click(function() {
	    var isChecked = $(this).prop("checked");
	    $('#mTableCampaign tr:has(td)').find('input[type="checkbox"]').prop('checked', isChecked);
	    if(isChecked)
	    	$("#btnDelGroup").prop('disabled', false);
	    else
	    	$("#btnDelGroup").prop('disabled', true);
   });
   /*click check box tren tung dong cua table*/
   $('#mTableCampaign tr:has(td)').find('input[type="checkbox"]').click(function() {
	    var isChecked = $(this).prop("checked");
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
   });
   
   /*open box modal delete campaign*/
   $('#btnDelGroup').on('click', function(){
	   
	   $("div.modal-body").text("Bạn có thực sự muốn xóa?");
       $('#mdata').val(getDelCampaignId());
	   $('#myModal').addClass("show");
	   
   });
   
   /*close box modal delete campaign*/
   $('.close').on('click', function(){
	   $('#myModal').removeClass("show");
	   $('#mdata').val("");
	   $('#mstatus').val("");
   });
   
   /*close box modal delete campaign*/
   $('#modal-cancel-btn').on('click', function(){
	   $('#myModal').removeClass("show");
	   $('#mdata').val("");
	   $('#mstatus').val("");
   });
   
   /*
   $('#model-delete-btn').on('click', function(){
	  delCampaign(); 
   });
   */
   
   /**===========pagination============== */
   /*button page number*/
   $('.pageNumber').click(function(){
	   $('.pagination-body').find('.pageNumber.pactive').removeClass('pactive');	   
	   $(this).addClass('pactive');
	   if($('.pagination-body').attr('ul-click') == 'user'){
		   getUserPageChange();
	   }else if($('.pagination-body').attr('ul-click') == 'donation'){
		   getDonationPageChange();
	   }else if($('.pagination-body').attr('ul-click') == 'idonation'){
		   getInfoDonationPageChange();
	   }else if($('.pagination-body').attr('ul-click') == 'hdonation'){
		   getAccountDonationPageChange();
	   }else{
		   getCampaignPageChange();
	   }
   });
   /*button next page*/
   $('.next').click(function(){	   
       $('.pagination-body').find('.pageNumber.pactive').next().addClass('pactive');
       $('.pagination-body').find('.pageNumber.pactive').prev().removeClass('pactive');
       if($('.pagination-body').find('.pageNumber').length > 0){  
    	   
    	   if($('.pagination-body').attr('ul-click') == 'user'){
    		   getUserPageChange();
    	   }else if($('.pagination-body').attr('ul-click') == 'donation'){
    		   getDonationPageChange();
    	   }else if($('.pagination-body').attr('ul-click') == 'idonation'){
    		   getInfoDonationPageChange();
    	   }else if($('.pagination-body').attr('ul-click') == 'hdonation'){
    		   getAccountDonationPageChange();
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
    	   if($('.pagination-body').attr('ul-click') == 'user'){
    		   getUserPageChange();
    	   }else if($('.pagination-body').attr('ul-click') == 'donation'){
    		   getDonationPageChange();
    	   }else if($('.pagination-body').attr('ul-click') == 'idonation'){
    		   getInfoDonationPageChange();
    	   }else if($('.pagination-body').attr('ul-click') == 'hdonation'){
    		   getAccountDonationPageChange();
    	   }else{
    		   getCampaignPageChange();
    	   }
       }
   });
   
     
   //show msg box
   showBoxMsg_();
   
   
   /*set active for path*/
   (function(){
       let pathname = window.location.pathname;
       let pathnames = pathname.split("/");
       var i = 2;
       if(setPathURL() != ""){
    	   i = 3;
       }
       
       if(pathnames[i] == 'admin'){
    	   //console.log(pathnames[2]);
    	   if(pathnames[i+1] == undefined || pathnames[i+1] == ''){    		   
    		   $('.mCB_container').find('li[data="dashboard"]').addClass('active');
    	   }else{	    	   
	    	   $('.mCB_container').find('li').each(function(){
	    		   var data = $(this).attr('data');
	    		   if(pathnames[i+1].indexOf(data) != -1){
	    			   $(this).addClass('active');
	    			   return false;
	    		   }
	    		    		   
	    	   });
    	   }   	   
    	  
       }
   })();
   
   
// Restricts input for each element in the set of matched elements to the given inputFilter.
   
   (function($) {
     $.fn.inputFilter = function(inputFilter) {
       return this.on("input keydown keyup mousedown mouseup select contextmenu drop", function() {
         if (inputFilter(this.value)) {
           this.oldValue = this.value;
           this.oldSelectionStart = this.selectionStart;
           this.oldSelectionEnd = this.selectionEnd;
         } else if (this.hasOwnProperty("oldValue")) {
           this.value = this.oldValue;
           this.setSelectionRange(this.oldSelectionStart, this.oldSelectionEnd);
         } else {
           this.value = "";
         }
       });
     };
   }(jQuery));
   /*
   $("#campaign-goal").inputFilter(function(value) { //input gola >= 0
  		return /^\d*$/.test(value); 
  });
  */
});