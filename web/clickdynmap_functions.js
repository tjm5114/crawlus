/*
-------- Drawing Draggable and Resizable shape functions ---------
 */

var shape;
var whichrb;

function leftClick(event) {
	var shpe = getShape();
	if(shpe ==""){
		alert("Please select either circle, polygon, square or rectangle");
	}else{
		if(shpe == "circ"){
			leftClickCircle(event);
		}
		if(shpe == "poly"){
			leftClickPoly(event);
		}
		if(shpe == "rect"){
			leftClickRect(event);
		}
		if(shpe == "square"){
			leftClickSquare(event);
		}
	}
}

function getShape() {
	var shp = "";
	len = document.forms[1].shape.length;
	for (i = 0; i <len; i++) {
		if (document.forms[1].shape[i].checked) {
			shp = document.forms[1].shape[i].value;
		}
	}
	return shp;
}

function subhid() { 
	document.forms[0].circrad.value = "";
	document.forms[0].circpos.value = "";
	document.forms[0].roishape.value = "";
	document.forms[0].mappars.value = "";
	document.forms[0].polylats.value = null;
	document.forms[0].polylngs.value = null;
	document.forms[0].rectlats.value = null;
	document.forms[0].rectlngs.value = null;
	var whichshape = getShape();
	if(whichshape == "circ" && pos != null){
		document.forms[0].circrad.value = rad.toFixed(3);
		document.forms[0].circpos.value = pos.toUrlValue();
		document.forms[0].roishape.value = "circ";
	}
	if(whichshape == "rect" && rectShape != null){
		var rbds = rectShape.getBounds().toUrlValue(6).split(",");
		document.forms[0].rectlats.value = rbds[0] + "," + rbds[2];
		document.forms[0].rectlngs.value = rbds[1] + "," + rbds[3];
		document.forms[0].roishape.value = "rect";
	}
	if(whichshape == "poly" && markers.length > 2){
		for(ki = 0; ki < (markers.length-1); ki++) {
			document.forms[0].polylats.value += markers[ki].getPosition().toUrlValue().split(",")[0] + ",";
			document.forms[0].polylngs.value += markers[ki].getPosition().toUrlValue().split(",")[1] + ",";
		}
		document.forms[0].polylats.value += markers[(markers.length-1)].getPosition().toUrlValue().split(",")[0];
		document.forms[0].polylngs.value += markers[(markers.length-1)].getPosition().toUrlValue().split(",")[1];
		document.forms[0].roishape.value = "poly";
	}
	if(whichshape == "square" && sqShape != null){
		var sarray = sqShape.getPath();
		var s_sw = sarray.getAt(0).toUrlValue(6).split(",");
		var s_ne = sarray.getAt(2).toUrlValue(6).split(",");
		document.forms[0].sqlats.value = s_sw[0] + "," + s_ne[0];
		document.forms[0].sqlngs.value = s_sw[1] + "," + s_ne[1];
		document.forms[0].roishape.value = "square";
	}
	document.forms[0].mappars.value = map.getCenter().toUrlValue() + "," + map.getZoom();
}


function clearOld(whichrb) {
	if(oldrb == "poly"){
		clearPoly();
	}
	if(oldrb == "circ"){
		clearCircle();
	}
	if(oldrb == "square"){
		clearSq();
	}
	if(oldrb == "rect"){
		clearRect();
	}
	if(whichrb == "poly"){
		document.getElementById('polyinfo').style.display = 'block';
	}
	if(whichrb == "circ"){
		document.getElementById('circleinfo').style.display = 'block';
	}
	if(whichrb == "square"){
		document.getElementById('squareinfo').style.display = 'block';
	}
	if(whichrb == "rect"){
		document.getElementById('rectinfo').style.display = 'block';
	}
	oldrb = whichrb;
}

/*
Drawing Draggable and Resizable Polygon functions by Wolfgang Pichler at http://www.wolfpil.de/v3/flexible-polygon.html, with some additions.
 */

var polyShape;
var markers = [];
var drag_poly = false;
var mouseDownPos;
var oldVertexPos;


function setShape() {

	var g = google.maps;
	var opts_poly = {
			strokeColor: "#3355ff",
			strokeOpacity: .8,
			strokeWeight: 3,
			fillColor: "#335599",
			fillOpacity: .3,
			map: map
	};
	polyShape = new g.Polygon(opts_poly);

	// Register event listeners for dragging the polygon
	polyShape.listeners = [
	                       g.event.addListener(polyShape, "mousedown", startDrag),
	                       g.event.addListener(polyShape, "dblclick", clearPoly),
	                       g.event.addListener(map, "mousemove", movePoly)
	                       ];
	document.onmouseup = mouseUp;
}

function setShapeRes() {

	var g = google.maps;
	var opts_poly = {
			strokeColor: "#3355ff",
			strokeOpacity: .8,
			strokeWeight: 3,
			fillColor: "#335599",
			fillOpacity: .3,
			map: map
	};
	polyShape = new g.Polygon(opts_poly);
}

function reloadPoly(){
	var mklatlng;
	setShape();
	for(j = 0; j < mklats.length; j++) {
		mklatlng = new google.maps.LatLng(mklats[j], mklngs[j]);
		createMarker(mklatlng);
		polyShape.getPath().push(mklatlng);
		showValues();
	}
}

function reloadPolyRes(){
	var mklatlng;
	setShapeRes();
	for(j = 0; j < mklats.length; j++) {
		mklatlng = new google.maps.LatLng(mklats[j], mklngs[j]);
		createMarkerRes(mklatlng);
		polyShape.getPath().push(mklatlng);
		showValues();
	}
}

function showValues() {

	var report = document.getElementById("report");
	var g = google.maps.geometry.spherical;
	var pathref = polyShape.getPath().getArray();
	// We want km�, so we use 6371 as mean radius of earth in km
	var area = g.computeArea(pathref)/1000000;

	if(markers.length <= 2 ) {
		report.innerHTML = "&nbsp;";
	}
	else if(markers.length > 2 ) {
		report.innerHTML = area.toFixed(3)+ " km&sup2;";
	}
	document.getElementById("coords").innerHTML = null;
	for(i = 0; i < markers.length; i++) {
		document.getElementById("coords").innerHTML += i+": "+markers[i].getPosition().toUrlValue(6)+"<br>";
	}
}


function createMarker(point) {

	var g = google.maps;
	var image = function(src) {
		return new g.MarkerImage(src,
				new g.Size(11, 11),
				null,
				new g.Point(5, 5));
	};

	var marker = new g.Marker({
		position: point, map: map,
		icon: image("square.png"),
		raiseOnDrag:false,
		draggable: true
	});
	markers.push(marker);

	g.event.addListener(marker, "mouseover", function() {
		marker.setIcon(image("m-over-square.png"));
	});

	g.event.addListener(marker, "mouseout", function() {
		marker.setIcon(image("square.png"));
	});

	// Marker drag listener
	g.event.addListener(marker, "drag", function() {
		for (var m = 0, r; r = markers[m]; m++) {
			if (r == marker) {
				var newpos = marker.getPosition();
				break;
			}
		}

		// Update MVCArray
		polyShape.getPath().setAt(m, newpos);
		showValues();
	});

	// Click listener to remove a marker
	g.event.addListener(marker, "rightclick", function() {
		// Find out removed marker
		for (var n = 0, s; s = markers[n]; n++) {
			if (s == marker) {
				s.setMap(null);
				markers.splice(n, 1);
				break;
			}
		}

		// Remove removed point from the poly's MVCArray
		polyShape.getPath().removeAt(n);
		showValues();
	});
	return marker;
}

function createMarkerRes(point) {

	var g = google.maps;
	var image = function(src) {
		return new g.MarkerImage(src,
				new g.Size(11, 11),
				null,
				new g.Point(5, 5));
	};

	var marker = new g.Marker({
		position: point, map: map,
		icon: image("square.png"),
		raiseOnDrag:false,
		draggable: false
	});
	markers.push(marker);

	return marker;
}


function leftClickPoly(event) {

	if (event.latLng) {
		if (!polyShape) setShape();

		// Add a marker at the clicked point
		createMarker(event.latLng);

		// Add the point to the path of the poly
		polyShape.getPath().push(event.latLng);
		showValues();
	}
}


function zoomToPoly() {

	if (polyShape && polyShape.getPath().getLength() > 1) {
		var shapeBounds = new google.maps.LatLngBounds();

		// Iterate through MVCArray
		polyShape.getPath().forEach(function(point) {
			shapeBounds.extend(point);
		});
		map.fitBounds(shapeBounds);
	}
}


function clearPoly() {

	var g = google.maps;
	// Remove polygon, markers, unregister old listeners and reset globals
	for (var i = 0, m; m = markers[i]; i++) {
		g.event.clearInstanceListeners(m);
		m.setMap(null);
	}

	if (polyShape) {
		for (var j = 0, n; n = polyShape.listeners[j]; j++) {
			g.event.removeListener(n);
		}
		polyShape.setMap(null);
		polyShape = null;
	}
	markers.length = 0;
	document.getElementById("report").innerHTML = null;
	document.getElementById("coords").innerHTML = null;
	document.getElementById('polyinfo').style.display = 'none'; 
}


function startDrag(event) {

	drag_poly = true;
	mouseDownPos = event.latLng;
	oldVertexPos = [];

	// Disable dragging of the map
	map.setOptions({ draggable: false });

	// Store position of markers and remove them afterwards.
	// There are no markers involved while dragging the poly.
	for (var i = 0, n; n = markers[i]; i++) {
		oldVertexPos[i] = n.getPosition();
		// Clear old marker listeners
		google.maps.event.clearInstanceListeners(n);
		// Clear old markers
		n.setMap(null);
	}
	markers.length = 0;

	// Avoid selecting the page's content while dragging the polygon
	if (window.event) { window.event.returnValue = false; }
}


function movePoly(event) {

	if (drag_poly) {
		var newlatlng = event.latLng;

		// Changes the path of the poly
		// Set the new vertices while dragging
		var path = polyShape.getPath();
		for (var i = 0, n; n = oldVertexPos[i]; i++) {
			var lat = n.lat() + newlatlng.lat() - mouseDownPos.lat();
			var lng = n.lng() + newlatlng.lng() - mouseDownPos.lng();
			var newVertexPos = new google.maps.LatLng(lat, lng);
			path.setAt(i, newVertexPos);
		}
	}
}


function mouseUp() {

	if (drag_poly) {
		drag_poly = false;

		// Enable dragging of the map again
		map.setOptions({ draggable: true });

		// Takes the poly's new path to create new markers
		var path = polyShape.getPath();
		for (var i = 0; i < path.getLength(); i++) {
			createMarker(path.getAt(i));
		}
		showValues();
	}
}



/*
Drawing Draggable and Resizable Circle functions by Luke Mahe (Google Geo Team) at https://developers.google.com/maps/articles/mvcfun, with some additions.
 */

var one_circle = false;
var dragh;
var dragsh;
var drageh;
var distchg;
var poschg;
var pos=null;
var rad="";
var marker;


function leftClickCircle(event) {

	if (event.latLng && !one_circle) {
		one_circle = true;
		distanceWidget = new DistanceWidget({
			map: map,
			position: event.latLng,
			distance: RadiusWidget.prototype.distanceBetweenPoints_(map.getBounds().getSouthWest(), map.getBounds().getNorthEast())/10, 
			color: '#000000',
			activeColor: '#5599bb',
			sizerIcon: new google.maps.MarkerImage('resize-off.png'),
			activeSizerIcon: new google.maps.MarkerImage('resize.png')
		});
		distchg = google.maps.event.addListener(distanceWidget, 'distance_changed', updateArea_Radius);
		poschg = google.maps.event.addListener(distanceWidget, 'position_changed', updateCoords);
		updateArea_Radius();
		updateCoords();
	}
}

function reloadCircle() {
	//google.maps.event.addListener(map, "click", leftClickCircle);
	one_circle = true;
	distanceWidget = new DistanceWidget({
		map: map,
		position: new google.maps.LatLng(circpos[0], circpos[1]),
		distance: circrad*1, 
		color: '#000000',
		activeColor: '#5599bb',
		sizerIcon: new google.maps.MarkerImage('resize-off.png'),
		activeSizerIcon: new google.maps.MarkerImage('resize.png')
	});
	updateArea_Radius();
	updateCoords();
	distchg = google.maps.event.addListener(distanceWidget, 'distance_changed', updateArea_Radius);
	poschg = google.maps.event.addListener(distanceWidget, 'position_changed', updateCoords);
}


function reloadCircleRes() {
	var pos = new google.maps.LatLng(circpos[0]*1, circpos[1]*1);
	circlepos.innerHTML = pos.toUrlValue();
	var  rd = circrad*1;
	circleradius.innerHTML = rd.toFixed(3)+ " km;";
	var ar = Math.PI*rd*rd;
	circlearea.innerHTML = ar.toFixed(3)+ " km&sup2;";
	var rCircleOpts = {
			fillColor: "#000000",
			strokeWeight: 2,
			map: map,
			center: pos,
			radius: rd*1000};
	var rCircle = new google.maps.Circle(rCircleOpts);
}


function DistanceWidget(opt_options) {

	var options = opt_options || {};

	this.setValues(options);

	if (!this.get('position')) {
		this.set('position', map.getCenter());
	}

	// Add a marker to the page at the map center or specified position
	marker = new google.maps.Marker({
		draggable: true,
		title: 'Move me!'
	});

	marker.bindTo('map', this);
	marker.bindTo('zIndex', this);
	marker.bindTo('position', this);
	marker.bindTo('icon', this);

	// Create a new radius widget
	var radiusWidget = new RadiusWidget(options['distance']);

	// Bind the radius widget properties.
	radiusWidget.bindTo('center', this, 'position');
	radiusWidget.bindTo('map', this);
	radiusWidget.bindTo('zIndex', marker);
	//radiusWidget.bindTo('maxDistance', this);
	radiusWidget.bindTo('minDistance', this);
	radiusWidget.bindTo('color', this);
	radiusWidget.bindTo('activeColor', this);
	radiusWidget.bindTo('sizerIcon', this);
	radiusWidget.bindTo('activeSizerIcon', this);

	// Bind to the radius widget distance property
	this.bindTo('distance', radiusWidget);
	// Bind to the radius widget bounds property
	this.bindTo('bounds', radiusWidget);

	var me = this;
	google.maps.event.addListener(marker, 'dblclick', function() {
		// When a user double clicks on the icon fit to the map to the bounds
		map.fitBounds(me.get('bounds'));
	});
	var me = this;
	google.maps.event.addListener(marker, 'rightclick', function() {
		// When a user right clicks on the icon remove the circle
		// Remove polygon, markers, unregister old listeners and reset globals
		clearCircle();
	});
}
DistanceWidget.prototype = new google.maps.MVCObject();


function RadiusWidget(opt_distance) {
	var circle = new google.maps.Circle({
		strokeWeight: 2
	});

	this.set('distance', opt_distance);
	this.set('active', false);
	this.bindTo('bounds', circle);

	circle.bindTo('center', this);
	circle.bindTo('zIndex', this);
	circle.bindTo('map', this);
	circle.bindTo('strokeColor', this);
	circle.bindTo('radius', this);

	this.addSizer_();


}
RadiusWidget.prototype = new google.maps.MVCObject();



RadiusWidget.prototype.addSizer_ = function() {
	sizer = new google.maps.Marker({
		draggable: true,
		title: 'Drag me!',
		raiseOnDrag: false
	});

	sizer.bindTo('zIndex', this);
	sizer.bindTo('map', this);
	sizer.bindTo('icon', this);
	sizer.bindTo('position', this, 'sizer_position');

	var me = this;
	dragsh = google.maps.event.addListener(sizer, 'dragstart', function() {
		me.set('active', true);
	});

	dragh = google.maps.event.addListener(sizer, 'drag', function() {
		// Set the circle distance (radius)
		me.setDistance_();
	});

	drageh = google.maps.event.addListener(sizer, 'dragend', function() {
		me.set('active', false);
	});

};


/**
 * Update the radius when the distance has changed.
 */
RadiusWidget.prototype.distance_changed = function() {
	this.set('radius', this.get('distance') * 1000);
};

/**
 * Update the radius when the min distance has changed.
 */
RadiusWidget.prototype.minDistance_changed = function() {
	if (this.get('minDistance') &&
			this.get('distance') < this.get('minDistance')) {
		this.setDistance_();
	}
};


/**
 * Update the radius when the max distance has changed.
 */
//RadiusWidget.prototype.maxDistance_changed = function() {
//if (this.get('maxDistance') &&
//this.get('distance') > this.get('maxDistance')) {
//this.setDistance_();
//}
//};


/**
 * Update the stroke color when the color is changed.
 */
RadiusWidget.prototype.color_changed = function() {
	this.active_changed();
};


/**
 * Update the active stroke color when the color is changed.
 */
RadiusWidget.prototype.activeColor_changed = function() {
	this.active_changed();
};


/**
 * Update the active stroke color when the color is changed.
 */
RadiusWidget.prototype.sizerIcon_changed = function() {
	this.active_changed();
};


/**
 * Update the active stroke color when the color is changed.
 */
RadiusWidget.prototype.activeSizerIcon_changed = function() {
	this.active_changed();
};


/**
 * Update the center of the circle and position the sizer back on the line.
 *
 * Position is bound to the DistanceWidget so this is expected to change when
 * the position of the distance widget is changed.
 */
RadiusWidget.prototype.center_changed = function() {
	var sizerPos = this.get('sizer_position');
	var position;
	if (sizerPos) {
		position = this.getSnappedPosition_(sizerPos);
	} else {
		var bounds = this.get('bounds');
		if (bounds) {
			var lng = bounds.getNorthEast().lng();
			position = new google.maps.LatLng(this.get('center').lat(), lng);
		}
	}

	if (position) {
		this.set('sizer_position', position);
	}
};

/**
 * Update the center of the circle and position the sizer back on the line.
 */
RadiusWidget.prototype.active_changed = function() {
	var strokeColor;
	var icon;

	if (this.get('active')) {
		if (this.get('activeColor')) {
			strokeColor = this.get('activeColor');
		}

		if (this.get('activeSizerIcon')) {
			icon = this.get('activeSizerIcon');
		}
	} else {
		strokeColor = this.get('color');

		icon = this.get('sizerIcon');
	}

	if (strokeColor) {
		this.set('strokeColor', strokeColor);
	}

	if (icon) {
		this.set('icon', icon);
	}
};


/**
 * Set the distance of the circle based on the position of the sizer.
 * @private
 */
RadiusWidget.prototype.setDistance_ = function() {
	// As the sizer is being dragged, its position changes.  Because the
	// RadiusWidget's sizer_position is bound to the sizer's position, it will
	// change as well.
	var pos = this.get('sizer_position');
	var center = this.get('center');
	var distance = this.distanceBetweenPoints_(center, pos);

	//if (this.get('maxDistance') && distance > this.get('maxDistance')) {
	//  distance = this.get('maxDistance');
	//}

	if (this.get('minDistance') && distance < this.get('minDistance')) {
		distance = this.get('minDistance');
	}

	// Set the distance property for any objects that are bound to it
	this.set('distance', distance);

	var newPos = this.getSnappedPosition_(pos);
	this.set('sizer_position', newPos);
};


/**
 * Finds the closest left or right of the circle to the position.
 *
 * @param {google.maps.LatLng} pos The position to check against.
 * @return {google.maps.LatLng} The closest point to the circle.
 * @private.
 */
RadiusWidget.prototype.getSnappedPosition_ = function(pos) {
	var bounds = this.get('bounds');
	var center = this.get('center');
	var left = new google.maps.LatLng(center.lat(),
			bounds.getSouthWest().lng());
	var right = new google.maps.LatLng(center.lat(),
			bounds.getNorthEast().lng());

	var leftDist = this.distanceBetweenPoints_(pos, left);
	var rightDist = this.distanceBetweenPoints_(pos, right);

	if (leftDist < rightDist) {
		return left;
	} else {
		return right;
	}
};


/**
 * Calculates the distance between two latlng points in km.
 * @see http://www.movable-type.co.uk/scripts/latlong.html
 *
 * @param {google.maps.LatLng} p1 The first lat lng point.
 * @param {google.maps.LatLng} p2 The second lat lng point.
 * @return {number} The distance between the two points in km.
 * @private
 */

RadiusWidget.prototype.distanceBetweenPoints_ = function(p1, p2) {
	if (!p1 || !p2) {
		return 0;
	}

	var R = 6378.137; // Radius of the Earth in km
	var dLat = (p2.lat() - p1.lat()) * Math.PI / 180;
	var dLon = (p2.lng() - p1.lng()) * Math.PI / 180;
	var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
	Math.cos(p1.lat() * Math.PI / 180) * Math.cos(p2.lat() * Math.PI / 180) *
	Math.sin(dLon / 2) * Math.sin(dLon / 2);
	var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	var d = R * c;
	return d;
};

function updateCoords() {
	pos = distanceWidget.get('position');
	circlepos.innerHTML = pos.toUrlValue();
}

function updateArea_Radius() {
	rad = distanceWidget.get('distance');
	circleradius.innerHTML = rad.toFixed(3)+ " km;";
	var ar = Math.PI*rad*rad;
	circlearea.innerHTML = ar.toFixed(3)+ " km&sup2;";
}


function clearCircle(){
	google.maps.event.removeListener(dragh);
	google.maps.event.removeListener(dragsh);
	google.maps.event.removeListener(drageh);
	google.maps.event.removeListener(distchg);
	google.maps.event.removeListener(poschg);
	google.maps.event.clearInstanceListeners(marker);
	marker.setMap(null);
	one_circle = false;
	pos = null;
	rad = "";
	circlepos.innerHTML = "";
	circleradius.innerHTML = "";
	circlearea.innerHTML = "";
	document.getElementById('circleinfo').style.display = 'none'; 
}

/*
Drawing Draggable and Resizable Rectangle functions.
 */

var one_rect = false;
var drag_rect = false;
var rectmouseDownPos;
var rectShape;
var oldbnds;

function leftClickRect(event) {

	if (event.latLng && !one_rect) {
		one_rect = true;
		var g = google.maps;
		mb = map.getBounds();
		mb_spn = mb.toSpan();
		r_sw = new g.LatLng(event.latLng.lat()-mb_spn.lat()/10, event.latLng.lng()-mb_spn.lng()/10);
		r_ne = new g.LatLng(event.latLng.lat()+mb_spn.lat()/10, event.latLng.lng()+mb_spn.lng()/10);
		bnds = new g.LatLngBounds(r_sw,r_ne);
		var opts_rect = {
				strokeColor: "#3355ff",
				strokeOpacity: .8,
				strokeWeight: 3,
				fillColor: "#335599",
				fillOpacity: .3,
				map: map,
				editable: true,
				bounds: bnds
		};
		rectShape = new g.Rectangle(opts_rect);
		rectShape.listeners = [
		                       g.event.addListener(rectShape, "mousedown", rectstartDrag),
		                       g.event.addListener(rectShape, "bounds_changed", showRectValues),
		                       g.event.addListener(rectShape, "rightclick", clearRect),
		                       g.event.addListener(map, "mousemove", moveRect)
		                       ];
		document.onmouseup = rectmouseUp;
		showRectValues();
	} 
}  


function showRectValues() {

	var rectcoords = document.getElementById("rectcoords");	 
	var rbnds = rectShape.getBounds();
	var rbds = rbnds.toUrlValue(6).split(",");
	rectcoords.innerHTML = "SW: " + rbds[0] +"," + rbds[1]+"<br>";
	rectcoords.innerHTML += "NE: " + rbds[2] +"," + rbds[3]+"<br>";

	var g = google.maps;
	var rectreport = document.getElementById("rectreport");

	var lat_s = rbnds.getSouthWest().lat();
	var lat_n = rbnds.getNorthEast().lat();
	var lng_w = rbnds.getSouthWest().lng();
	var lng_e = rbnds.getNorthEast().lng();
	var rarray = new g.MVCArray();
	rarray.push(new g.LatLng(lat_s,lng_w));
	rarray.push(new g.LatLng(lat_n,lng_w));
	rarray.push(new g.LatLng(lat_n,lng_e));
	rarray.push(new g.LatLng(lat_s,lng_e));
	// We want km�, so we use 6371 as mean radius of earth in km
	var rectarea = g.geometry.spherical.computeArea(rarray)/1000000;
	rectreport.innerHTML = rectarea.toFixed(3)+ " km&sup2;"; 

	var rectdim = document.getElementById("rectdim");
	var rectx = g.geometry.spherical.computeDistanceBetween(new g.LatLng(lat_s,lng_w), new g.LatLng(lat_s,lng_e));
	var recty = g.geometry.spherical.computeDistanceBetween(new g.LatLng(lat_s,lng_w), new g.LatLng(lat_n,lng_w));
	rectx = rectx/1000;
        recty = recty/1000;
	rectdim.innerHTML = recty.toFixed(3) + " (ns) " + " x " + rectx.toFixed(3) + " (we) " + " km";
}

function rectmouseUp() {

	if (drag_rect) {
		drag_rect = false;

		// Enable dragging of the map again
		map.setOptions({ draggable: true });
	}	  
}

function moveRect(event) {
	var g = google.maps;
	if (drag_rect) {
		var newlatlng = event.latLng;

		// Changes the path of the poly
		// Set the new vertices while dragging
		var lat_s = oldbnds.getSouthWest().lat();
		var lat_n = oldbnds.getNorthEast().lat();
		var lng_w = oldbnds.getSouthWest().lng();
		var lng_e = oldbnds.getNorthEast().lng();
		lat_s += newlatlng.lat() - rectmouseDownPos.lat();
		lng_w +=  newlatlng.lng() - rectmouseDownPos.lng();
		lat_n += newlatlng.lat() - rectmouseDownPos.lat();
		lng_e +=  newlatlng.lng() - rectmouseDownPos.lng();
		var nbnds = new g.LatLngBounds(new g.LatLng(lat_s,lng_w), new g.LatLng(lat_n,lng_e));
		rectShape.setBounds(nbnds);	
		showRectValues();
	}
}	 

function rectstartDrag(event) {

	drag_rect = true;
	rectmouseDownPos = event.latLng;
	oldbnds = rectShape.getBounds();

	// Disable dragging of the map
	map.setOptions({ draggable: false });

	// Avoid selecting the page's content while dragging the polygon
	if (window.event) { window.event.returnValue = false; }
}

function clearRect() {

	var g = google.maps;
	// Remove polygon, markers, unregister old listeners and reset globals

	if (rectShape) {
		for (var j = 0, n; n = rectShape.listeners[j]; j++) {
			g.event.removeListener(n);
		}
		rectShape.setMap(null);
		rectShape = null;
	}
	document.getElementById("rectreport").innerHTML = null;
	document.getElementById("rectcoords").innerHTML = null;
	document.getElementById("rectdim").innerHTML = null;
	document.getElementById('rectinfo').style.display = 'none';
	one_rect = false;
}


function reloadRect(){
	one_rect = true;
	var g = google.maps;
	var r_sw = new google.maps.LatLng(rectlats[0], rectlngs[0]);
	var r_ne = new google.maps.LatLng(rectlats[1], rectlngs[1]);
	var bnds = new g.LatLngBounds(r_sw,r_ne);
	var opts_rect = {
			strokeColor: "#3355ff",
			strokeOpacity: .8,
			strokeWeight: 3,
			fillColor: "#335599",
			fillOpacity: .3,
			map: map,
			editable: true,
			bounds: bnds
	};
	rectShape = new g.Rectangle(opts_rect);
	rectShape.listeners = [
	                       g.event.addListener(rectShape, "mousedown", rectstartDrag),
	                       g.event.addListener(rectShape, "bounds_changed", showRectValues),
	                       g.event.addListener(rectShape, "rightclick", clearRect),
	                       g.event.addListener(map, "mousemove", moveRect)
	                       ];
	document.onmouseup = rectmouseUp;
	showRectValues();
}

function reloadRectRes(){
	one_rect = true;
	var g = google.maps;
	var r_sw = new google.maps.LatLng(rectlats[0], rectlngs[0]);
	var r_ne = new google.maps.LatLng(rectlats[1], rectlngs[1]);
	var bnds = new g.LatLngBounds(r_sw,r_ne);
	var opts_rect = {
			strokeColor: "#3355ff",
			strokeOpacity: .8,
			strokeWeight: 3,
			fillColor: "#335599",
			fillOpacity: .3,
			map: map,
			editable: false,
			bounds: bnds
	};
	rectShape = new g.Rectangle(opts_rect);
	showRectValues();
}


/*
Drawing Draggable and Resizable Square functions.
 */

var sqShape;
var oldLatLngs = [];
var szmk;
var drag_sq = false;
var sqmouseDownPos;
var one_sq = false;
var sqoldPos;
var oldcentre;

function leftClickSquare(event) {
	if (event.latLng && !one_sq) {
		one_sq = true;
		var g = google.maps;
		var mb = map.getBounds();
		var mb_spn = mb.toSpan();
		var sarray = new g.MVCArray();
		var diam = g.geometry.spherical.computeDistanceBetween(mb.getSouthWest(), mb.getNorthEast());
		sarray.push(g.geometry.spherical.computeOffset(event.latLng, diam/10, 225));
		sarray.push(g.geometry.spherical.computeOffset(event.latLng, diam/10, 315));
		sarray.push(g.geometry.spherical.computeOffset(event.latLng, diam/10, 45));
		sarray.push(g.geometry.spherical.computeOffset(event.latLng, diam/10, 135));

		/*sarray.push(new g.LatLng(event.latLng.lat()-mb_spn.lat()/10, event.latLng.lng()-mb_spn.lat()/10));
                sarray.push(new g.LatLng(event.latLng.lat()+mb_spn.lat()/10, event.latLng.lng()-mb_spn.lat()/10));
		sarray.push(new g.LatLng(event.latLng.lat()+mb_spn.lat()/10, event.latLng.lng()+mb_spn.lat()/10));
                sarray.push(new g.LatLng(event.latLng.lat()-mb_spn.lat()/10, event.latLng.lng()+mb_spn.lat()/10));  */
		var opts_sq = {
				strokeColor: "#3355ff",
				strokeOpacity: .8,
				strokeWeight: 3,
				fillColor: "#335599",
				fillOpacity: .3,
				map: map,
				paths: sarray
		};
		sqShape = new g.Polygon(opts_sq);

		sqShape.listeners = [
		                     g.event.addListener(sqShape, "mousedown", sqstartDrag),
		                     g.event.addListener(sqShape, "rightclick", clearSq),
		                     g.event.addListener(map, "mousemove", moveSq)
		                     ];
		document.onmouseup = sqmouseUp;
		showSqValues();
		createSzMarker(sarray.getAt(2));
	} 
}


function createSzMarker(point) {

	var g = google.maps;
	szmk = new g.Marker({
		position: point, map: map,
		icon: new g.MarkerImage('resize-off.png'),
		raiseOnDrag:false,
		draggable: true
	});


	g.event.addListener(szmk, "mouseout", function() {
		szmk.setIcon(new g.MarkerImage('resize-off.png'));
	});

	g.event.addListener(szmk, "dragstart", startSqResize);


	// Marker drag listener

	g.event.addListener(szmk, "drag", sqResize);

}

function startSqResize(event){
	var g = google.maps;
	sqoldPos = event.latLng;
	for(var i=0; i<4; i++){
		oldLatLngs[i] = sqShape.getPath().getAt(i);
	}
	oldcentre = new g.LatLng((oldLatLngs[0].lat()+oldLatLngs[1].lat())/2, (oldLatLngs[0].lng()+oldLatLngs[3].lng())/2);
	szmk.setIcon(new google.maps.MarkerImage('resize.png'));
}


function sqResize(event){
	var g = google.maps;
	var sqnewPos = event.latLng;
	sqShape.getPath().setAt(2, sqnewPos);
	var newdiam = g.geometry.spherical.computeDistanceBetween(oldcentre, sqnewPos);
	var newcentre = g.geometry.spherical.computeOffset(event.latLng, newdiam, 225);
	sqShape.getPath().setAt(0, g.geometry.spherical.computeOffset(newcentre, newdiam, 225));
	sqShape.getPath().setAt(1, g.geometry.spherical.computeOffset(newcentre, newdiam, 315));
	sqShape.getPath().setAt(3, g.geometry.spherical.computeOffset(newcentre, newdiam, 135));

	/*var sx =  sqnewPos.lat()-sqoldPos.lat();
     		var sy =  sqnewPos.lng()-sqoldPos.lng();
                var dlat = oldLatLngs[0].lat()-sy;
                var dlng = oldLatLngs[0].lng()-sx;            
                sqShape.getPath().setAt(0, new g.LatLng(dlat, dlng));
                dlat = oldLatLngs[1].lat()+sx;
                dlng = oldLatLngs[1].lng()-sx;
                sqShape.getPath().setAt(1, new g.LatLng(dlat, dlng));
                dlat =oldLatLngs[3].lat()-sy;
                dlng =oldLatLngs[3].lng()+sy;
                sqShape.getPath().setAt(3, new g.LatLng(dlat, dlng));*/
	showSqValues();
}


function reloadSquare(){
	one_sq = true;
	var g = google.maps;
	var sqlatlng;
	var sarray = new g.MVCArray();
	sqlatlng = new google.maps.LatLng(sqlats[0], sqlngs[0]);
	sarray.push(sqlatlng);
	sqlatlng = new google.maps.LatLng(sqlats[1], sqlngs[0]);
	sarray.push(sqlatlng);
	sqlatlng = new google.maps.LatLng(sqlats[1], sqlngs[1]);
	sarray.push(sqlatlng);
	sqlatlng = new google.maps.LatLng(sqlats[0], sqlngs[1]);
	sarray.push(sqlatlng);
	var opts_sq = {
			strokeColor: "#3355ff",
			strokeOpacity: .8,
			strokeWeight: 3,
			fillColor: "#335599",
			fillOpacity: .3,
			map: map,
			paths: sarray
	};
	sqShape = new g.Polygon(opts_sq);
	showSqValues();
	sqShape.listeners = [
	                     g.event.addListener(sqShape, "mousedown", sqstartDrag),
	                     g.event.addListener(sqShape, "rightclick", clearSq),
	                     g.event.addListener(map, "mousemove", moveSq)
	                     ];
	document.onmouseup = sqmouseUp;
	createSzMarker(sarray.getAt(2));
}

function reloadSquareRes(){
	var g = google.maps;
	var sqlatlng;
	var sarray = new g.MVCArray();
	sqlatlng = new google.maps.LatLng(sqlats[0], sqlngs[0]);
	sarray.push(sqlatlng);
	sqlatlng = new google.maps.LatLng(sqlats[1], sqlngs[0]);
	sarray.push(sqlatlng);
	sqlatlng = new google.maps.LatLng(sqlats[1], sqlngs[1]);
	sarray.push(sqlatlng);
	sqlatlng = new google.maps.LatLng(sqlats[0], sqlngs[1]);
	sarray.push(sqlatlng);
	var opts_sq = {
			strokeColor: "#3355ff",
			strokeOpacity: .8,
			strokeWeight: 3,
			fillColor: "#335599",
			fillOpacity: .3,
			map: map,
			paths: sarray
	};
	sqShape = new g.Polygon(opts_sq);
	showSqValues();
}



function showSqValues() {
	var sqcoords = document.getElementById("sqcoords");	 
	var sarray = sqShape.getPath();
	sqcoords.innerHTML = "SW: " + sarray.getAt(0).toUrlValue(6)+"<br>";
	sqcoords.innerHTML += "NE: " + sarray.getAt(2).toUrlValue(6)+"<br>";

	var g = google.maps;
	var sqreport = document.getElementById("sqreport");

	// We want km�, so we use 6371 as mean radius of earth in km
	var sqarea = g.geometry.spherical.computeArea(sarray)/1000000;
	sqreport.innerHTML = sqarea.toFixed(3)+ " km&sup2;"; 

	var sqdim = document.getElementById("sqdim");
	var sqs = g.geometry.spherical.computeDistanceBetween(sarray.getAt(0), sarray.getAt(1));
	sqs = sqs/1000;
	sqdim.innerHTML = sqs.toFixed(3) + " km";
}


function clearSq() {

	var g = google.maps;
	g.event.clearInstanceListeners(szmk);
	szmk.setMap(null);
	if (sqShape) {
		for (var j = 0, n; n = sqShape.listeners[j]; j++) {
			g.event.removeListener(n);
		}
		sqShape.setMap(null);
		sqShape = null;
	}
	document.getElementById("sqreport").innerHTML = null;
	document.getElementById("sqcoords").innerHTML = null;
	document.getElementById("sqdim").innerHTML = null;
	document.getElementById('squareinfo').style.display = 'none'; 
	one_sq = false;
}


function sqstartDrag(event) {

	drag_sq = true;
	sqmouseDownPos = event.latLng;
	for(var i=0; i<4; i++){
		oldLatLngs[i] = sqShape.getPath().getAt(i);
	}
	// Disable dragging of the map
	map.setOptions({ draggable: false });


	// Clear old marker listeners
	google.maps.event.clearInstanceListeners(szmk);
	// Clear old markers
	szmk.setMap(null);


	// Avoid selecting the page's content while dragging the polygon
	if (window.event) { window.event.returnValue = false; }
}


function moveSq(event) {

	if (drag_sq) {
		var newlatlng = event.latLng;

		// Changes the path of the poly
		// Set the new vertices while dragging
		var sqpath = sqShape.getPath();
		for (var i = 0; i < 4; i++) {
			var lat = oldLatLngs[i].lat() + newlatlng.lat() - sqmouseDownPos.lat();
			var lng = oldLatLngs[i].lng() + newlatlng.lng() - sqmouseDownPos.lng();
			var newVertexPos = new google.maps.LatLng(lat, lng);
			sqpath.setAt(i, newVertexPos);
			showSqValues();
		}
	}
}


function sqmouseUp() {

	if (drag_sq) {
		drag_sq = false;

		// Enable dragging of the map again
		map.setOptions({ draggable: true });

		// Takes the poly's new path to create new markers
		createSzMarker(sqShape.getPath().getAt(2));
	}
}





