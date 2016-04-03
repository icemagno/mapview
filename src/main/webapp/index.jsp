<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
	<head>
		<title>Map Viewer v1.0</title>
		<link rel="stylesheet" href="css/style.css" type="text/css"/>
		<link rel="stylesheet" href="css/tipTip.css" type="text/css"/>
		<link rel="stylesheet" href="css/tipTip.css" type="text/css"/>
		<link rel="stylesheet" href="css/redmond/jquery-ui-1.8.23.custom.css" type="text/css" />
		
	    <script src="http://openlayers.org/api/OpenLayers.js"></script>
		<script src="http://www.openstreetmap.org/openlayers/OpenStreetMap.js"></script>		
		<script src="js/jquery.js"></script>	
		<script src="js/jquery.tipTip.minified.js"></script>
		<script src="js/jquery-ui-1.8.23.custom.min.js"></script>
		
		
		<script src="js/log.js"></script>
		<script src="js/gridmap.js"></script>
		
		<script src="js/script.js"></script>
		<script src="js/websocket.js"></script>


	</head>
	 
	<body id="type-d" onload="init()">
		
		<div id="wrap">
			<div id="header">
				<div id="site-name_l">Map Viewer v1.0</div>
				<div id="site-name_r">by Magno Abreu</div>
			</div>


			<div id="content-wrap">
				<div id="content">

					<div class="buttons-bar">
						<img id="btn_reload" class="smallbtn dicas" title="Reload Map" src="img/buttons/32/refresh.png" />		
						<img id="btn_centermap" class="smallbtn dicas" title="Center Map" src="img/buttons/32/globe.png" />			
						<img id="btn_play" style="display:none" class="smallbtn dicas" title="Start Map Update" src="img/buttons/32/play.png" />
						<img id="btn_stop" class="smallbtn dicas" title="Stop Map Update" src="img/buttons/32/stop.png" />
						
						<img class="smallbtn dicas" style="display:none" id="showLogScreen" title="Show Log" src="img/buttons/32/terminal.png" />
						
						<div id="loading-gif"><img src="img/loading.gif" /></div>
					</div>
					
					<div id="left_div">
						<div id="map"></div>
						<div style="clear:both;height:5px;"></div>
					</div>
					
					<div id="right_div" >
						<div style="height:452px">


							<div  id="logsystem">
								<div class="buttons-bar">
									<div class="orange_title" style="width:130px;margin-top:4px;float:left" >System Log</div> 
									<img class="smallbtn dicas" id="closeLogScreen" style="float:right" title="Close" src="img/buttons/32/delete.png" />
									<img class="smallbtn dicas" id="refreshLogData" style="float:right" title="Update Log" src="img/buttons/32/refresh.png" />
								</div>
								<table id="logtable" style="width:100%;">
									<tbody></tbody>
								</table>
							</div>	

					
							<div id="thingsThatHappened"></div>
					
							<div id="unitdetaildisplay" style="display:none">
								
								<div class="buttons-bar"> 
									<img class="smallbtn dicas" id="closeUnitDetails" style="float:right" title="Fechar" src="img/buttons/32/delete.png" />
								</div>
							
							
								<div id="unitname">Unit Name</div>
								<table style="width:100%;">
									<tr>
										<th width="15%">Serial</th>
										<th width="15%">Bearing</th>
										<th width="70%">Position</th>
									</tr>
									<tr>
										<td id="unitowner"></td>
										<td id="unitbearing"></td>
										<td id="unitposition"></td>
									</tr>
								</table>
							</div>	
										
						</div>
						
					</div>
					
				</div>

<script>

	$(document).ready(function() {
		startWebSocket();
	});
	
</script>
				
<%@ include file="footer.jsp" %>		