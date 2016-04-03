
var lat = 0;  
var lon = 0; 
var zoom = 0;
var logRefreshInterval = 2000;

var map;
var unitsLayer;

var loaderStack = 0;
var selectedunit = "";
var fromProjection = new OpenLayers.Projection("EPSG:4326");   
var toProjection   = new OpenLayers.Projection("EPSG:900913"); 
var selectEt;


function updateUnitDisplay( properties ) {
	var serial = properties.serial;
	var rumo = properties.bearing;
	var nome = properties.name;
	var position = properties.position;

	$("#unitname").text(nome);
	$("#unitowner").text(serial); 
	$("#unitbearing").text(rumo + "°");
	$("#unitposition").text(position);
}

//	========================================================================================================
//	AJAX request to load units

// Using websockets now...

/*
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
		
		
		for ( var x=0; x< data.features.length; x++  ) {
			if ( data.features[x].properties.serial == selectedunit ) {
				updateUnitDisplay( data.features[x].properties );
			}
		}
		
		
		var geojson_format = new OpenLayers.Format.GeoJSON({
		    'internalProjection': map.baseLayer.projection,
		    'externalProjection': new OpenLayers.Projection("EPSG:4326")
		});
		
		unitsLayer.removeAllFeatures();
	    unitsLayer.addFeatures(geojson_format.read(data));
	    
	});		
}
*/


//	========================================================================================================
//	Entry point ( BODY OnLoad  )
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
		
		initMap();
		initLogSystem();
	});
}


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
    map.addLayer(new OpenLayers.Layer.OSM("Main Map"));
	map.addControl( new OpenLayers.Control.ScaleLine() );
	centerMap();

	bindClickButton();

	createUnitsLayer();
	
	bindFeaturesEventHandler();
	
	//loadUnits();
	
	// Reload map every MOVE / ZOOM / PAN
	map.events.register("moveend", map, function() {
		reload();
    });
	
	// Disable zoom on mouse wheel
	controls = map.getControlsByClass('OpenLayers.Control.Navigation');
	for(var i = 0; i < controls.length; ++i)  controls[i].disableZoomWheel();	

}


// ========================================================================================================
// Cria o layer de unidades
function createUnitsLayer() {
	var def_style = new OpenLayers.Style({
		//fillColor: "${color}",
		//strokeColor : "${color}",
		//rotation: "${bearing}",
		pointRadius : "${size}",
		label: "${name}",
		labelAlign: "ct",
		fontSize: 9,
		labelYOffset : 26,
		cursor: "pointer",
		rotation : 0,
		fillOpacity: 1,
		externalGraphic: "${pin_image}"
	});

	// If we want to change opacity when selected. Change fillOpacity to 0.5 to start transparent.
	var sel_style = new OpenLayers.Style({
		fillOpacity: 1
	});
	var styleMap = new OpenLayers.StyleMap({"default" : def_style, "select": sel_style});
	// ============================================================================================
	
	unitsLayer = new OpenLayers.Layer.Vector("Mobile",  {styleMap: styleMap} );
	map.addLayer(unitsLayer);
}


function centerMap() {
	var position = new OpenLayers.LonLat(lon,lat).transform( fromProjection, toProjection );
	map.setCenter( position, zoom);
}


function reload() { 
	//loadUnits();
}


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
// Map mouse click
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


function bindFeaturesEventHandler() {
	var options = {
			clickout: true, 
			toggle: false,
		    multiple: false, 
		    hover: false
		};
		selectEt = new OpenLayers.Control.SelectFeature([unitsLayer], options);
		
		// Unit event
		unitsLayer.events.on({
		    "featureselected": function(e) {
		    	getUnitDetails(e);
		    },
		    "featureunselected": function(e) {
		    	// when unselect unit
		    }});
		
		map.addControl(selectEt);	
		selectEt.activate();	
	
}

function getUnitDetails(e) {

	alert("wewe");
	
	var unitAttributes = e.feature.data; 
	var serial = unitAttributes.serial;
	var rumo = unitAttributes.bearing;
	var nome = unitAttributes.name;
	var position = unitAttributes.position;
	selectedunit = serial;

	$("#unitname").text(nome);
	$("#unitowner").text(serial); 
	$("#unitbearing").text(rumo + "°");
	$("#unitposition").text(position);
	$("#unitdetaildisplay").css("display","block");
}


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




