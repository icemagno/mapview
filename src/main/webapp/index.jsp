<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
	<head>
		<title>The Grid v1.0.0.1</title>
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


	</head>
	 
	<body id="type-d" onload="init()">
		
		<div id="wrap">
			<div id="header">
				<div id="site-name_l">The Grid</div>
				<div id="site-name_r">v1.0.0</div>
			</div>


			<div id="content-wrap">
				<div id="content">

					<div class="buttons-bar">
						<img id="btn_reload" class="smallbtn dicas" title="Recarregar Mapa" src="img/buttons/32/refresh.png" />		
						<img id="btn_centermap" class="smallbtn dicas" title="Centralizar Mapa" src="img/buttons/32/globe.png" />			
						<img id="btn_play" class="smallbtn dicas" title="Atualizar automaticamente" src="img/buttons/32/play.png" />
						<img id="btn_stop" style="display:none" class="smallbtn dicas" title="Parar atualização automática" src="img/buttons/32/stop.png" />
						
						<img class="smallbtn dicas" id="showLogScreen" title="Exibir Log" src="img/buttons/32/terminal.png" />
						
						<div id="loading-gif"><img src="img/loading.gif" /></div>
					</div>
					
					<div id="left_div">
						<div id="map"></div>
						<div style="clear:both;height:5px;"></div>
													
						<div id="targdetails" style="display:none">
							<div class="buttons-bar"> 
								<img id="btn_addwaypoint" class="smallbtn dicas" title="Adicionar como destino" src="img/buttons/32/map_add.png" />
							</div>
							<div class="orange_title">Ponto de Destino</div>
							<table style="width:100%;">
								<tr>
									<th width="150">Distância</th>
									<th width="60">Direção</th>
									<th width="60">Longitude</th>
									<th width="60">Latitude</th>
								</tr>
								<tr>
									<td id="targdist"></td>
									<td id="targbearing"></td>
									<td id="targlong"></td>
									<td id="targlat"></td>
								</tr>
							</table>
						</div>
						
					</div>
					
					<div id="right_div" >
						<div style="height:452px">


							<div style="display:none" id="logsystem">
								<div class="buttons-bar">
									<div class="orange_title" style="width:130px;margin-top:4px;float:left" >Log do Sistema</div> 
									<img class="smallbtn dicas" id="closeLogScreen" style="float:right" title="Fechar" src="img/buttons/32/delete.png" />
									<img class="smallbtn dicas" id="refreshLogData" style="float:right" title="Atualizar Log" src="img/buttons/32/refresh.png" />
								</div>
								<table id="logtable" style="width:100%;">
									<tbody></tbody>
								</table>
							</div>	

					
							<div id="unitdetaildisplay" style="display:none">
								
								<div class="buttons-bar"> 
									<img class="smallbtn dicas" id="btn_wp_select" title="Adicionar Waypoint" src="img/buttons/32/green_flag.png" />
									<img class="smallbtn dicas" style="display:none;" canop="N" id="btn_wp_cancel_select" title="Cancelar Seleção de Destino" src="img/buttons/32/red_flag.png" />
	
									<img class="smallbtn dicas" id="moveUnit" title="Movimentar Unidade" src="img/buttons/32/process_next.png" />
									<img class="smallbtn dicas" style="display:none;" canop="N" id="stopUnit" title="Parar Unidade" src="img/buttons/32/process_remove.png" />
									
									<img class="smallbtn dicas" id="closeUnitDetails" style="float:right" title="Fechar" src="img/buttons/32/delete.png" />
									<img class="smallbtn dicas" id="showUnitData" style="float:right" title="Mais Detalhes" src="img/buttons/32/search.png" />
									
								</div>
							
							
								<div id="unitname">Nome da Unidade</div>
								<table style="width:100%;">
									<tr>
										<th width="150">Proprietário</th>
										<th width="60">Rumo</th>
										<th width="60">Velocidade</th>
										<th width="60">Distância</th>
										<th width="60">ETA</th>
									</tr>
									<tr>
										<td id="unitowner"></td>
										<td id="unitbearing"></td>
										<td id="unitvelocity"></td>
										<td id="unitdistance"></td>
										<td id="unitETA"></td>
									</tr>
								</table>
							</div>	
											
							<div id="citydetails" style="display:none">
								<div class="buttons-bar">
									<img class="smallbtn dicas" id="closeCityDetails" style="float:right" title="Fechar" src="img/buttons/32/delete.png" />
								</div>
							
							
								<div class="orange_title" id="cityname"></div>
								<table style="width:100%;">
									<tr>
										<th width="150">Nome</th>
										<th width="60">Distância</th>
										<th width="60">Direção</th>
									</tr>
									<tr>
										<td id="cityname_old"></td>
										<td id="citydist"></td>
										<td id="citybearing"></td>
									</tr>
								</table>
							</div>


						</div>
						
					</div>
					
					

					
				</div>

				
<%@ include file="footer.jsp" %>		