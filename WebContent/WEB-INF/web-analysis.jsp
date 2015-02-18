<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="databeans.TwitterBean"%>

<jsp:include page="template-top.jsp" />
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script src="http://code.highcharts.com/highcharts.js"></script>
					<script src="http://code.highcharts.com/modules/exporting.js"></script>
<script type="text/javascript">
	google.load("visualization", "1.1", {
		packages : [ "bar", "corechart" ]
	});

	google.setOnLoadCallback(drawPie);
	google.setOnLoadCallback(drawChart);
	//google.setOnLoadCallback(drawPoint);
/*	function drawPoint() {
		var dataPoint = google.visualization.arrayToDataTable([
				['Tweet',''],
				
	
				<c:forEach var="point" items="${popularTweetList}">
				[${point.retweet_count}, ${point.favorite_count}],
			    </c:forEach>
				]);

		var optionsPoint = {
			width : 600,
			height : 400,
			title : 'Popular Tweets',
			hAxis : {
				title : 'Retweet',
				minValue : 0,
				maxValue : 5
			},
			vAxis : {
				title : 'Favorite',
				minValue : 0,
				maxValue : 5
			},
			legend : 'none'
		};

		var chartPoint = new google.visualization.ScatterChart(document
				.getElementById('chart_div'));

		chartPoint.draw(dataPoint, optionsPoint);
	}*/



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
	
	
	
	$(function () {
	    $('#point_chart').highcharts({
	        chart: {
	            type: 'scatter',
	            zoomType: 'xy'
	        },
	        title: {
	            text: 'Popular Tweets'
	        },
	        subtitle: {
	            text: 'past one month'
	        },
	        xAxis: {
	            title: {
	                enabled: true,
	                text: 'Favorite Number'
	            },
	            startOnTick: true,
	            endOnTick: true,
	            showLastLabel: true
	        },
	        yAxis: {
	            title: {
	                text: 'Retweet Number'
	            }
	        },
	        legend: {
	            enabled:false,
	            layout: 'vertical',
	            align: 'left',
	            verticalAlign: 'top',
	            x: 100,
	            y: 70,
	            floating: true,
	            backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF',
	            borderWidth: 1
	        },
	        plotOptions: {
	            scatter: {
	                marker: {
	                    radius: 5,
	                    states: {
	                        hover: {
	                            enabled: true,
	                            lineColor: 'rgb(100,100,100)'
	                        }
	                    }
	                },
	                states: {
	                    hover: {
	                        marker: {
	                            enabled: false
	                        }
	                    }
	                },
	                tooltip: {
	                    headerFormat: '',
	                    pointFormat: '{point.x} , {point.y} '
	                }
	            }
	        },
	        series: [
	        { 
				name: 'Tweet',
	            color: 'rgba(119, 152, 191, .5)',
	            data: [ 
	                    <c:forEach var="point" items="${popularTweetList}">
						[${point.retweet_count}, ${point.favorite_count}],	
				    	</c:forEach>
	            ]},
	        
	        ]
	    });
	});
</script>

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
					
					<div id="piechart"
						style="min-width: 310px; height: 400px; margin: 0 auto"></div>
				</div>

				<div role="tabpanel" class="tab-pane" id="point">
					
					
					<div id="point_chart" style="width: 600px; height: 400px;  margin: 0 auto"></div>
					
					<!-- <div id="chart_div"
						style="min-width: 310px; height: 400px; margin: 0 auto"></div> -->
				</div>
			</div>
		</div>

	</div>
</div>

<jsp:include page="template-bottom.jsp" />

