$(document).ready(function(){
	/*open dialog donation news*/	
	setViewNewsClick();
	/*
	$('.donation-item').click(function(){
		var getClass = this.className;
		var go_to_url = $(this).find("a").attr('href');
        console.log(go_to_url);
	});
	*/
});

function setViewNewsClick(){
	$('.viewNews').off('click');
	$('.viewNews').on('click', function(e){
		const id = $(this).attr('data-id');
		viewNews(id);
	});
}

