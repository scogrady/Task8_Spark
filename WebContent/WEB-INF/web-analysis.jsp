<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="databeans.TwitterBean"%>

<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript">
	google.load("visualization", "1.1", {
		packages : [ "bar", "corechart" ]
	});

	google.setOnLoadCallback(drawPie);
	google.setOnLoadCallback(drawChart);
	google.setOnLoadCallback(drawPoint);
	function drawPoint() {
		var dataPoint = google.visualization.arrayToDataTable([
				['Tweet',''],
				
				[ 8, 12 ], [ 4, 5.5 ], [ 11, 14 ],
				[ 4, 5 ], [ 3, 3.5 ], [ 6.5, 7 ]
				<c:forEach var="point" items="${}">
				[${location.x}, ${location.y}, '${location.description}'],
			    </c:forEach>
				]);

		var optionsPoint = {
			width : 600,
			height : 400,
			title : 'Popular Tweets',
			hAxis : {
				title : 'Retweet',
				minValue : 0,
				maxValue : 15
			},
			vAxis : {
				title : 'Favorite',
				minValue : 0,
				maxValue : 15
			},
			legend : 'none'
		};

		var chartPoint = new google.visualization.ScatterChart(document
				.getElementById('chart_div'));

		chartPoint.draw(dataPoint, optionsPoint);
	}

	function drawChart() {
		var data = google.visualization.arrayToDataTable([
				[ 'Genre', 'Fantasy & Sci Fi', 'Romance', 'Mystery/Crime',
						'General', 'Western', 'Literature', {
							role : 'annotation'
						} ], [ '2010', 10, 24, 20, 32, 18, 5, '' ],
				[ '2020', 16, 22, 23, 30, 16, 9, '' ],
				[ '2030', 28, 19, 29, 30, 12, 13, '' ],[ '2030', 28, 19, 29, 30, 12, 13, '' ] ]);

		var options = {
			width : 600,
			height : 400,
			legend : {
				position : 'top',
				maxLines : 3
			},
			bar : {
				groupWidth : '75%'
			},
			isStacked : true,
		};

		var view = new google.visualization.DataView(data);
		view.setColumns([ 0, 1, 2, 3, 4, 5, 6 ]);

		var chart = new google.visualization.ColumnChart(document
				.getElementById("columnchart_values"));
		chart.draw(view, options);
	}

	function drawPie() {

		var dataPie = google.visualization.arrayToDataTable([
				[ 'Task', 'Hours per Day' ], [ 'TAG ONE', 11 ],
				[ 'TAG TWO', 2 ], [ 'TAG THREE', 2 ], [ 'TAG FOUR', 2 ],
				[ 'TAG FIVE', 7 ], [ 'OTHERS', 7 ] ]);

		var optionsPie = {
			width : 600,
			height : 400,
			title : 'Popular Hash Tag'
		};

		var chartPie = new google.visualization.PieChart(document
				.getElementById('piechart'));

		chartPie.draw(dataPie, optionsPie);
	}
</script>

<jsp:include page="template-top.jsp" />
<div class="row">
	<div class="col-md-4"></div>

	<div class="col-md-8">
		<div role="tabpanel">

			<!-- Nav tabs -->
			<ul class="nav nav-tabs" role="tablist">
				<li role="presentation" class="active"><a href="#home"
					aria-controls="home" role="tab" data-toggle="tab">Bar</a></li>
				<li role="presentation"><a href="#profile"
					aria-controls="profile" role="tab" data-toggle="tab">Pie</a></li>
				<li role="presentation"><a href="#point"
					aria-controls="profile" role="tab" data-toggle="tab">Point</a></li>
			</ul>

			<!-- Tab panes -->
			<div class="tab-content">
				<div role="tabpanel" class="tab-pane active" id="home">
					<p>aAAA</p>
					<div id="columnchart_values" style="width: 900px; height: 300px;"></div>
					<!--<div id="columnchart_material"
						style="min-width: 310px; height: 400px; margin: 0 auto"></div>-->
				</div>
				<div role="tabpanel" class="tab-pane" id="profile">
					<p>BBBBB</p>
					<div id="piechart"
						style="min-width: 310px; height: 400px; margin: 0 auto"></div>
				</div>

				<div role="tabpanel" class="tab-pane" id="point">
					<p>CCC</p>
					<div id="chart_div"
						style="min-width: 310px; height: 400px; margin: 0 auto"></div>
				</div>
			</div>
		</div>

	</div>
</div>

<jsp:include page="template-bottom.jsp" />

