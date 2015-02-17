<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="databeans.TwitterBean"%>

<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript">
	google.load("visualization", "1.1", {
		packages : [ "bar" ]
	});
	google.setOnLoadCallback(drawChart);
	function drawChart() {
		var data = google.visualization.arrayToDataTable([
				[ 'Genre', 'Fantasy & Sci Fi', 'Romance', 'Mystery/Crime',
						'General', 'Western', 'Literature', {
							role : 'annotation'
						} ], [ '2010', 10, 24, 20, 32, 18, 5, '' ],
				[ '2020', 16, 22, 23, 30, 16, 9, '' ],
				[ '2030', 28, 19, 29, 30, 12, 13, '' ] ]);

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

		var chart = new google.charts.Bar(document
				.getElementById('columnchart_material'));

		chart.draw(data, options);
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
					aria-controls="home" role="tab" data-toggle="tab">Graphic</a></li>
				<li role="presentation"><a href="#profile"
					aria-controls="profile" role="tab" data-toggle="tab">Chart</a></li>
			</ul>

			<!-- Tab panes -->
			<div class="tab-content">
				<div role="tabpanel" class="tab-pane active" id="home">
					<p>aAAA</p>
					<div id="columnchart_material" style="width: 900px; height: 500px;"></div>

					<div id="containerChart"
						style="min-width: 310px; height: 400px; margin: 0 auto"></div>
				</div>
				<div role="tabpanel" class="tab-pane" id="profile">

					<div data-spy="scroll" data-offset="50" class="scrollspy-chart">
						<p>BBBBB</p>
					</div>
				</div>
			</div>
		</div>

	</div>
</div>

<jsp:include page="template-bottom.jsp" />

