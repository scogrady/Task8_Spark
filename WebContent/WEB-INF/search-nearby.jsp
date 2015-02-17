<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="databeans.TwitterBean"%>

<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript">

	google.load('visualization', '1.1', {
		packages : [ 'map' ]
	});
	google.setOnLoadCallback(drawMap);

	function drawMap() {
		var data = new google.visualization.DataTable();
		data.addColumn('string', 'Address');
		data.addColumn('string', 'Location');

		var data = google.visualization.arrayToDataTable([
				[ 'Lat', 'Long', 'Name' ],
		        <c:forEach var="location" items="${locationList}">
					[${location.x}, ${location.y}, '${location.description}'],
				</c:forEach>
		 	]);
				
			//	[ 37.4232, -122.0853, 'Me' ],
			  //  [ 37.4289, -122.1697, 'User one' ],
			//	[ 37.6153, -122.3900, 'User two' ],
			//	[ 37.4422, -122.1731, 'User three' ] ]);
		
		var options = {
			mapType : 'styledMap',
			zoomLevel : 11,
			showTip : true,
			useMapTypeControl : true,
			maps : {
				// Your custom mapTypeId holding custom map styles.
				styledMap : {
					name : 'Styled Map', // This name will be displayed in the map type control.
					styles : [ {
						featureType : 'poi.attraction',
						stylers : [ {
							color : '#fce8b2'
						} ]
					}, {
						featureType : 'road.highway',
						stylers : [ {
							hue : '#0277bd'
						}, {
							saturation : -50
						} ]
					}, {
						featureType : 'road.highway',
						elementType : 'labels.icon',
						stylers : [ {
							hue : '#000'
						}, {
							saturation : 100
						}, {
							lightness : 50
						} ]
					}, {
						featureType : 'landscape',
						stylers : [ {
							hue : '#259b24'
						}, {
							saturation : 10
						}, {
							lightness : -22
						} ]
					} ]
				}
			}
		};

		var map = new google.visualization.Map(document
				.getElementById('map_div'));

		map.draw(data, options);
	}
</script>

<jsp:include page="template-top.jsp" />
<div class="row">

	<div id="map_div" style="height: 500px; width: 900px"></div>
</div>

<jsp:include page="template-bottom.jsp" />