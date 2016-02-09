$(document).ready(function() {
	
	// ======  Auto resize width Right Div ==============
	var right_width = $("#wrap").width() - 712;
	
	$('#right_div').css("width",right_width + "px");
	
	$(window).resize(function(){
		var right_width = $("#wrap").width() - 712;
		$('#right_div').css("width",right_width + "px");
	});  
	// ==================================================
  
	$('.smallbtn').mouseenter(function() {
		var canop = $(this).attr('canop');
		if ( canop == 'N') { return false; }
		$(this).animate({opacity: 1}, 100);
	});	

	$('#btn_addwaypoint').click(function(){
		addWayPoint();
	});
	
	$('#moveUnit').click(function(){
		$('#stopUnit').toggle();
		$('#stopUnit').css("opacity", 1);
		$(this).toggle();
		showLoader();
		var origem = "StartStop?serial=" + selectedunit + "&action=start";
		$.ajax({
				url: origem,
				dataType: "json"
		}).always(function() { hideLoader(); });
	});

	$('#stopUnit').click(function(){
		$(this).toggle();
		$('#moveUnit').toggle();
		showLoader();
		var origem = "StartStop?serial=" + selectedunit + "&action=stop";
		$.ajax({
				url: origem,
				dataType: "json"
		}).always(function() { hideLoader(); });
	});

	
	
	$('#btn_wp_select').click(function(){
		canMarkTarget = true;
		$('#btn_wp_cancel_select').toggle();
		$("#btn_wp_cancel_select").css("opacity", 1);
		$(this).toggle();
		
	});
	
	$('#btn_wp_cancel_select').click(function(){
		cancelSelectTarget();
	});
	
	$('.smallbtn').mouseleave(function() {
		var canop = $(this).attr('canop');
		if ( canop == 'N') { return false; }
		$(this).animate({opacity: 0.6}, 100);
	});	
	
	$('.dicas').tipTip({maxWidth: "auto", defaultPosition: "top", delay: 500});	
	
	$('#btn_reload').click(function(){
		reload();
	});

	$('#btn_centermap').click(function(){
		centerMap() ;
	});
	
	$('#btn_play').click(function(){
		canAnimateUnits = true;
		$('#btn_stop').toggle();
		$(this).toggle();
		$('#btn_stop').css('opacity','1');
		$(this).attr('canop','S');
		$('#btn_stop').attr('canop','N');

	});
	
	$('#btn_stop').click(function(){
		canAnimateUnits = false;
		$('#btn_play').toggle();
		$(this).toggle();
		$(this).attr('canop','S');
	});
	
	
	$('#closeUnitDetails').click(function(){
		cancelSelectTarget();
		$('#unitdetaildisplay').toggle();
		wayPointsLayer.removeAllFeatures();
		selectEt.unselectAll();
	});
	
	$('#closeCityDetails').click(function(){
		$('#citydetails').toggle();
		selectEt.unselectAll();
	});

	$('#closeLogScreen').click(function(){
		$('#logsystem').toggle();
		$('#showLogScreen').toggle();
		workingLog = true;
	});	
	
	
	$('#showLogScreen').click(function(){
		$('#logsystem').toggle();
		$('#showLogScreen').toggle();
		workingLog = false;
	});	

	
	$('#refreshLogData').click(function(){
		workingLog = false;
		getLog();
	});	
	
	
});



