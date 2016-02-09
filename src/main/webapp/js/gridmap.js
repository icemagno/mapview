
var lat = 0;  
var lon = 0; 
var zoom = 0;
var logRefreshInterval = 0;

var map;
var vector_layer_units;
var unitsLayer;
var citiesLayer;

var wayPointsLayer;
var loaderStack = 0;
var markers;
var destinyMarker;
var selectedunit = "";
var canMarkTarget = false;
var targetLonLat;
var fromProjection = new OpenLayers.Projection("EPSG:4326");   
var toProjection   = new OpenLayers.Projection("EPSG:900913"); 
var nextETA = "";
var selectEt;

//	========================================================================================================
//	Atualiza as unidades no mapa a cada 5 segundos ( se o usuário ativar o recurso ).
var canAnimateUnits = false;
var intervalo;
function animateUnits() {
	if ( canAnimateUnits ) {
		loadUnits();
	}
}

//	========================================================================================================
//	Envia uma requisicao AJAX para carregar as unidades no mapa.
function loadCities() {
	var mapbounds = map.getExtent();
	mapbounds.transform(toProjection, fromProjection );
	bbox = mapbounds.toArray();
	var bleft = bbox[0];
	var bbottom = bbox[1];
	var bright = bbox[2];
	var btop = bbox[3];
	showLoader();
	var origem = "getCities?bleft=" + bleft + "&bbottom=" + bbottom + "&bright=" + bright + "&btop=" + btop;
	$.ajax({
			url: origem,
			dataType: "json"
	}).always(function() { hideLoader(); }).done(function(data) {
		data = eval( data );
	    var geojson_format = new OpenLayers.Format.GeoJSON({
	        'internalProjection': map.baseLayer.projection,
	        'externalProjection': new OpenLayers.Projection("EPSG:4326")
	    });
	    citiesLayer.removeAllFeatures();
	    citiesLayer.addFeatures(geojson_format.read(data));
	});		
}


//	========================================================================================================
//	Envia uma requisicao AJAX para carregar as unidades no mapa.
function loadUnits() {
	var mapbounds = map.getExtent();
	mapbounds.transform(toProjection, fromProjection );
	bbox = mapbounds.toArray();
	var bleft = bbox[0];
	var bbottom = bbox[1];
	var bright = bbox[2];
	var btop = bbox[3];
	showLoader();
	var origem = "getUnits?bleft=" + bleft + "&bbottom=" + bbottom + "&bright=" + bright + "&btop=" + btop;
	$.ajax({
			url: origem,
			dataType: "json"
	}).always(function() { hideLoader(); }).done(function(data) {
		data = eval( data );
		var geojson_format = new OpenLayers.Format.GeoJSON({
		    'internalProjection': map.baseLayer.projection,
		    'externalProjection': new OpenLayers.Projection("EPSG:4326")
		});
		
		unitsLayer.removeAllFeatures();
	    unitsLayer.addFeatures(geojson_format.read(data));
	    
	});		
}



//	========================================================================================================
//	Funcao principal : Ponto de entrada. Cria o mapa e inicializa todo o cenario ( OnLoad do BODY )
function init(){
	showLoader();
	$.ajax({
			url: "getMapConfig",
			dataType: "json"
	}).always(function() { hideLoader(); }).done(function(data) {
		data = eval( data );
		lat = data.mapCenterLat;
		lon = data.mapCenterLon;
		zoom = data.zoom;
		unitRefreshInterval = data.refreshInterval;
		logRefreshInterval = data.logRefreshInterval;
		
		intervalo = window.setInterval(animateUnits, unitRefreshInterval);
		initMap();
		initLogSystem();
	});
}

// ========================================================================================================
// Carrega o mapa apos o recebimento das configurações iniciais
function initMap() {
    map = new OpenLayers.Map( 'map',  {units:"km", controls: [
                                                                  new OpenLayers.Control.Navigation(),
                                                                  new OpenLayers.Control.PanZoomBar(),
                                                                  new OpenLayers.Control.LayerSwitcher({'ascending':false}),
                                                                  new OpenLayers.Control.Permalink(),
                                                                  new OpenLayers.Control.ScaleLine(),
                                                                  new OpenLayers.Control.Permalink('permalink'),
                                                                  new OpenLayers.Control.MousePosition(),
                                                                  new OpenLayers.Control.OverviewMap(),
                                                                  new OpenLayers.Control.KeyboardDefaults()
                                                              ],
    } );
    map.addLayer(new OpenLayers.Layer.OSM("Mapa Principal"));
	map.addControl( new OpenLayers.Control.ScaleLine() );
	centerMap();
	markers = new OpenLayers.Layer.Markers( "Marcadores" );
	map.addLayer(markers);
	bindClickButton();

	createCitiesLayer();
	createUnitsLayer();
	
	createWayPointsLayer();
	bindFeaturesEventHandler();
	
	loadCities();
	loadUnits();
	
	// Recarregar a cada MOVE / ZOOM / PAN
	map.events.register("moveend", map, function() {
		cancelSelectTarget();
		reload();
    });
	
	// Desabilitar o zoom usando a roda do mouse.
	controls = map.getControlsByClass('OpenLayers.Control.Navigation');
	for(var i = 0; i < controls.length; ++i)  controls[i].disableZoomWheel();	

}

// ========================================================================================================
// Cancela a seleção de ponto de destino da unidade ( bandeira verde )
function cancelSelectTarget() {
	if ( canMarkTarget ) {
		canMarkTarget = false;
		$("#targdetails").css("display","none");
		$('#btn_wp_select').toggle();
		$('#btn_wp_cancel_select').toggle();
		markers.removeMarker(destinyMarker);
	}
}

// ========================================================================================================
// Cria o layer de cidades
function createCitiesLayer() {
	var def_styleC = new OpenLayers.Style({
		  fillOpacity: 1,
		  strokeWidth: 1,
		  pointRadius : "${size}",
		  label: "${name}",
		  fontSize: 7,
		  cursor: "pointer",
		  fontColor: "red",
		  labelYOffset : -3,
		  labelXOffset : 6,
		  externalGraphic: "${image}"
	});
	var sel_styleC = new OpenLayers.Style({
		fillOpacity: 1
	});
	var styleMapC = new OpenLayers.StyleMap({"default" : def_styleC, "select": sel_styleC});
	citiesLayer = new OpenLayers.Layer.Vector("Cidades",  {"styleMap": styleMapC} );
	map.addLayer(citiesLayer);
}


//========================================================================================================
//Cria o layer de waypoints
function createWayPointsLayer() {
	var def_styleC = new OpenLayers.Style({
		  fillOpacity: 1,
		  strokeWidth: 1,
		  pointRadius : "${size}",
		  label: "${order}",
		  fontSize: 7,
		  cursor: "pointer",
		  fontColor: "white",
		  labelYOffset : -3,
		  labelXOffset : 6,
		  externalGraphic: "${image}"
	});

	var styleMapC = new OpenLayers.StyleMap({"default" : def_styleC, "select": def_styleC});
	wayPointsLayer = new OpenLayers.Layer.Vector("Waypoints",  {"styleMap": styleMapC} );
	map.addLayer(wayPointsLayer);
}

// ========================================================================================================
// Cria o layer de unidades
function createUnitsLayer() {
	var def_style = new OpenLayers.Style({
		pointRadius : "${size}",
		label: "${name}",
		labelAlign: "ct",
		fontSize: 9,
		labelYOffset : 17,
		cursor: "pointer",
		//rotation: "${bearing}",
		rotation : 0,
		fillOpacity: 0.5,
		//backgroundGraphic : "${pin_image}",
		externalGraphic: "${pin_image}"
	});
	var sel_style = new OpenLayers.Style({
		fillOpacity: 1
	});
	var styleMap = new OpenLayers.StyleMap({"default" : def_style, "select": sel_style});
	unitsLayer = new OpenLayers.Layer.Vector("Unidades",  {styleMap: styleMap} );
	map.addLayer(unitsLayer);
}


// ========================================================================================================
// Centraliza o mapa nas configurações do usuário
function centerMap() {
	var position = new OpenLayers.LonLat(lon,lat).transform( fromProjection, toProjection );
	map.setCenter( position, zoom);
}


//	========================================================================================================
//	Limpa e regarrega os vetores de informacao
function reload() { 
	loadCities();
	loadUnits();
}


//	========================================================================================================
//	Envia uma requisicao AJAX adicionando um waypoint para a unidade clicada
function addWayPoint() {
	if ( selectedunit != null) {
		var lon = targetLonLat.lon;
		var lat = targetLonLat.lat;
		showLoader();
		var origem = "addWayPoint?lon=" + lon + "&lat=" + lat + "&serial=" + selectedunit;
		$.ajax({
			url: origem,
			dataType: "json"
		}).always(function() { hideLoader(); }).done(function(data) {
			data = eval( data );
			if ( data.retorno == "erro") { 
			} else {
				getWayPointsToUnit(selectedunit);
			}
		});
		
	}
}


// ========================================================================================================
// Exibe um dialog box com uma mensagem.
function showError(titulo, mensagem) {
	$("#dialog-modal").attr("title",titulo);
	$("#dialog-modal").text(mensagem);
    $("#dialog-modal").dialog({
        height: 140,
        modal: true,
        buttons: {
            Ok: function() {
                $( this ).dialog( "close" );
            }
    }});
}

// ========================================================================================================
// Processa o clique de mouse ( um clique ) no mapa.
function bindClickButton() {
    OpenLayers.Control.Click = OpenLayers.Class(OpenLayers.Control, {                
        defaultHandlerOptions: {
            'single': true,
            'double': false,
            'pixelTolerance': 0,
            'stopSingle': false,
            'stopDouble': false
        },

        initialize: function(options) {
            this.handlerOptions = OpenLayers.Util.extend(
                {}, this.defaultHandlerOptions
            );
            OpenLayers.Control.prototype.initialize.apply(
                this, arguments
            ); 
            this.handler = new OpenLayers.Handler.Click(
                this, {
                    'click': this.trigger
                }, this.handlerOptions
            );
        }, 

        trigger: function(e) {
            var lonlat = map.getLonLatFromPixel(e.xy);
            lonlat.transform( map.projection,map.displayProjection);
            processMouseClick(lonlat);
        }

    });
    
    var click = new OpenLayers.Control.Click();
    map.addControl(click);
    click.activate();
    
}

// 	========================================================================================================
//	Processa as funcoes de clique de acordo com o contexto.
function processMouseClick(lonlat) {
	if ( canMarkTarget == true) {
		putDestinationSymbol(lonlat);
		getDestinyDetails(lonlat);
	}
}


// ========================================================================================================
// Coloca um marcador indicando o destino da unidade
function putDestinationSymbol(lonlat) {
	markers.removeMarker(destinyMarker);
	var size = new OpenLayers.Size(32,32);
	var offset = new OpenLayers.Pixel(-(size.w/2), -(size.h)+2);
	var icon = new OpenLayers.Icon('img/buttons/32/green_flag.png', size, offset);
	destinyMarker = new OpenLayers.Marker(lonlat,icon);
	markers.addMarker(destinyMarker);
}

// ========================================================================================================
// Mapeia os eventos de seleção nas cidades e unidades
function bindFeaturesEventHandler() {
	var options = {
			clickout: true, 
			toggle: false,
		    multiple: false, 
		    hover: false
		};
		selectEt = new OpenLayers.Control.SelectFeature([unitsLayer, citiesLayer, wayPointsLayer], options);
		
		// Evento para UNIDADE
		unitsLayer.events.on({
		    "featureselected": function(e) {
		    	cancelSelectTarget();
		    	getUnitDetails(e);
		    	getWayPoints(e);
		    },
		    "featureunselected": function(e) {
		    	// Ao descelecionar uma unidade
		    }});
		
		// Evento para CIDADE
		citiesLayer.events.on({
		    "featureselected": function(e) {
		    	getCityDetails(e);
		    },
		    "featureunselected": function(e) {
		    	// Ao descelecionar uma cidade
		    }
		});

		// Evento para WAYPOINT
		wayPointsLayer.events.on({
		    "featureselected": function(e) {
		    	alert("Não implementado ainda.");
		    },
		    "featureunselected": function(e) {
		    	// Ao descelecionar um waypoint
		    }
		});		
		
		map.addControl(selectEt);	
		selectEt.activate();	
	
}


// ========================================================================================================
// Envia uma requisicao AJAX solicitando os waypoints de uma unidade
function getWayPointsToUnit(serial) {
	showLoader();
	var origem = "getWayPoints?serial=" + serial;
	$.ajax({
			url: origem,
			dataType: "json"
	}).always(function() { hideLoader(); }).done(function(data) {
		data = eval( data );
		var geojson_format = new OpenLayers.Format.GeoJSON({
		    'internalProjection': map.baseLayer.projection,
		    'externalProjection': new OpenLayers.Projection("EPSG:4326")
		});
		
		wayPointsLayer.removeAllFeatures();
		wayPointsLayer.addFeatures(geojson_format.read(data));
		
		updateUnitDisplayData(data);
		
	});			
}


// ========================================================================================================
// Atualiza o mostrador de dados da Unidade na tela, se estiver selecionada e movendo-se
function updateUnitDisplayData(data) {
	if ( data.features.length > 0 ) {
		var wpETA = data.features[0].properties.ETA;
		if ( wpETA > 0 ) { 
			$('#stopUnit').css("display","block");
			$('#moveUnit').css("display","none");
			$('#stopUnit').css("opacity", 1);
		} else {
			$('#moveUnit').css("display","block");
			$('#stopUnit').css("display","none");
		} 
		$("#unitETA").text( wpETA +" h" );
		$("#unitbearing").text( data.features[0].properties.bearing + " °");
		$("#unitdistance").text( data.features[0].properties.distance + " Km" );
		$("#unitvelocity").text( data.features[0].properties.velocity + " Km/h" );
	} else {
		$('#moveUnit').css("display","block");
		$('#stopUnit').css("display","none");
		$("#unitETA").text( "0 h" );
		$("#unitbearing").text( "0 °");
		$("#unitdistance").text( "0 Km" );
		$("#unitvelocity").text( "0 Km/h" );
	}
}

// ========================================================================================================
// Solicita os WayPoints da unidade clicada
function getWayPoints(e) {
	var unitAttributes = e.feature.data; 
	var serial = unitAttributes.serial;
	getWayPointsToUnit(serial);
}

// ========================================================================================================
// Exibe informações da UNIDADE clicada
function getUnitDetails(e) {

	var unitAttributes = e.feature.data; 
	var serial = unitAttributes.serial;
	var rumo = unitAttributes.bearing;
	var veloc = unitAttributes.velocity;
	var nome = unitAttributes.name;
	selectedunit = serial;

	if ( veloc > 0 ) { 
		$('#stopUnit').css("display","block");
		$('#moveUnit').css("display","none");
		$('#stopUnit').css("opacity", 1);
	} else {
		$('#moveUnit').css("display","block");
		$('#stopUnit').css("display","none");
	} 	
	
	
	$("#unitname").text(nome);
	$("#unitowner").text(serial); 
	$("#unitbearing").text(rumo + "°");
	$("#unitvelocity").text(veloc);
	$("#unitdetaildisplay").css("display","block");
}

// ========================================================================================================
// Envia uma requisicao AJAX solicitando as informacoes da CIDADE clicada
function getCityDetails(e) {
	var cityAttributes = e.feature.data;
	var gid = cityAttributes.serial;
	var origem = "getCityDetail?gid=" + gid;
	showLoader();
	$.ajax({
			url: origem,
			dataType: "json"
	}).always(function() { hideLoader(); }).done(function(data) {
		data = eval( data );
		if ( data.features.length > 0 ) {
			var dist = data.features[0].properties.distance;
			var bearing = data.features[0].properties.direction;
			var name = data.features[0].properties.name;
			$("#citydist").text(dist + ' Km');
			$("#cityname").text(name);
			$("#citybearing").text(bearing + "°");
		} else {
		}
		$("#citydetails").css("display","block");
		
	});

}

// ========================================================================================================
// Mais de uma requisição AJAX pode estar em andamento. Preciso controlar para esconder o loader somente 
// quando não houver nenhuma requisição.
function showLoader() {
	loaderStack++;
	$("#loading-gif").css("display","block");
}

function hideLoader() {
	if (loaderStack > 0) {
		loaderStack--;
	} 
	if (loaderStack == 0) {
		$("#loading-gif").css("display","none");
	}
}


// ========================================================================================================
// Envia uma requisicao AJAX solicitando as informacoes do destino
function getDestinyDetails(lonlat) {	
	lonlat.transform(toProjection, fromProjection );
	targetLonLat = lonlat;
	var lon = lonlat.lon;
	var lat = lonlat.lat;
	var origem = "getDestinyDetails?lon=" + lon + "&lat=" + lat + "&serial=" + selectedunit;
	$.ajax({
		url: origem,
		dataType: "json"
	}).done(function(data) {
		data = eval( data );
		
		if ( data.features.length > 0 ) {
			var dist = data.features[0].properties.distance;
			var direction = data.features[0].properties.direction;
			$("#targdist").text(dist + ' Km');
			$("#targlong").text(lon);
			$("#targlat").text(lat);
			$("#targbearing").text(direction + "°");
		} else {
		}
		$("#targdetails").css("display","block");
	
	});
}


