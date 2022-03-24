var amount = 0;
$(document).ready(function(){
	$('#mTableInfoDonation tbody tr td .bt-cancel').css("display", "none");
	
	
	//button edit
	$('#mTableInfoDonation').on('click', 'tbody td .bt-edit', function (e) {    
	      
	    var clickedRow = $($(this).closest('td')).closest('tr');   
	    amount = clickedRow.find('input').val();
	    $('#mTableInfoDonation tbody tr td .bt-update').removeClass('bt-update').addClass('bt-edit').html('Edit');    
	    $('#mTableInfoDonation tbody tr td .bt-cancel').css("display", "none");	      
	    $('#mTableInfoDonation tbody tr td input').prop('disabled', true);
	    $(clickedRow).find('td .bt-edit').removeClass('bt-edit').addClass('bt-update').html('Update');    
	    $(clickedRow).find('td .bt-cancel').css("display", "block");    
	    $(clickedRow).find('input').prop('disabled', false);
	});
	
	//button cancel
	$('#mTableInfoDonation').on('click', 'tbody td .bt-cancel', function (e) {    
		var clickedRow = $($(this).closest('td')).closest('tr');
        $('#mTableInfoDonation tbody tr td .bt-update').removeClass('bt-update').addClass('bt-edit').html('Edit');    
        $('#mTableInfoDonation tbody tr td .bt-cancel').css("display", "none");
        
        $(clickedRow).find('input').prop('disabled', true);
        clickedRow.find('input').val(amount);
    });
	
	//button update
	$('#mTableInfoDonation').on('click', 'tbody td .bt-update', function (e) {    
	    
		var openedTextBox = $('#mTableInfoDonation').find('input');
		var clickedRow = $($(this).closest('td')).closest('tr');
		var a = clickedRow.find('input').val();
		if(isNumber(a)){
			fnUpdateDataTableValue(clickedRow);			
		}else{			
			
			showBoxMsg("Lỗi! Kiểm tra nhập liệu.", 1);
			return;
		}
		
		$('#mTableInfoDonation tbody tr td .bt-update').removeClass('bt-update').addClass('bt-edit').html('Edit');    
		$('#mTableInfoDonation tbody tr td .bt-cancel').css("display", "none"); 
		$(clickedRow).find('input').prop('disabled', true);
	});
	
	//select an option from server	
	$('#d_userid').select2({
		
        placeholder: 'Nhập tên đăng nhập',
        width: '333',        
        minimumInputLength: 2,
        placeholder: "Chọn tên đăng nhập",
        ajax: {
            url: setPathURL()+"/Donation/admin/searchDonor",
            data: function (params) {
                var query = {
                    search: params.term
                }
                return query;
            },
            processResults: function (data) {
            	console.log(data['results']);
                return {
                    results: data['results']
                };
            }
        }
        
	});
	/*
	// this part is responsible for setting last search when select2 is opening
    var last_search = '';
    $('#select').on('select2-open', function () {
        if (last_search) {
            $('.select2-search').find('input').val(last_search).trigger('paste');
        }
    });
    $('#select').on('select2-loaded', function () {
        last_search = $('.select2-search').find('input').val();
    });
    */
	
});

//phương thức update amount trong bảng donation
function fnUpdateDataTableValue(e) {    
	const campaignId = $('#cID').val();
	var amount = e.find('input').val();
	var donationId = e.find('td:eq(0)').html();
	
	var objectData = {};		
	objectData['id'] = donationId;
	objectData['campaignId'] = campaignId;
	objectData['amount'] = amount;
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: setPathURL()+"/Donation/admin/updateInfoDonation",
		data: JSON.stringify(objectData),
		dataType: "json",
		cache: false,
		timeout: 60000,
		success: function(data){
			
			var donation = data['donation'];
			var amount = formatInteger(donation.amount,'de-DE');
			$('#tblInfoCmapign tbody tr').find('td:eq(2)').html(amount);
			showBoxMsg("Cập nhật thành công!", 0);
		},
		error: function(e){
			showBoxMsg("Lỗi! Kiểm tra nhập liệu.", 1);
			clickedRow.find('input').val(amount);
			console.log(e);
		}
	});  
	
} 


