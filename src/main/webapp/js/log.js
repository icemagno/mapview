var countLog = 0;
var workingLog = true;

function initLogSystem() {
	window.setInterval(getLog, logRefreshInterval);
}

function addLog(message) {
	countLog++;
	if ( countLog == 10 ) {
		countLog--;
		$('#logtable tr:first').remove();
	}
	$("<tr><td><img src='img/log/" + message.type + ".png'> " + message.message + "</td></tr>").appendTo('#logtable tbody').hide().fadeIn(2000);
}

function getLog() {
	if ( workingLog ) { return; }
	workingLog = true;
	$.ajax({
		url: "getLog",
		dataType: "json"
	}).always(function() { workingLog = false; }).done(function(data) {
		data = eval( data );
		var quant = data.messages.length;
		for ( var x=0; x<quant; x++){
			addLog(data.messages[x]);			
		}
	});
	

}