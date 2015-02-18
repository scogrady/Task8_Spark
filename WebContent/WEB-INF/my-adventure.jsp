<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="databeans.FlickrBean"%>

<jsp:include page="template-top.jsp" />
<script src="js/d3.js"></script>
<script src="js/d3.layout.cloud.js"></script>
<div>
	<div class="col-md-5">
		<h3>Your Tweets from our site:</h3>
		<ul id="my-tweet" style="height: 600px; overflow: auto">
			<c:forEach var="embedUserTweet" items="${userTweetsHtml}">
				<li style="list-style-type: none">
					<div>${embedUserTweet}</div>
					<hr>
				</li>
			</c:forEach>
		</ul>
	</div>
	<div id="key-word">


		<script>
			<%String[] tagName = (String[])request.getAttribute("hashUserTag_name");
		  		Integer[] tagCount = (Integer[])request.getAttribute("hashUserTag_count");
			%>
			<c:set var="name" value="${hashUserTag_name}"/>
			<c:set var="count" value="${hashUserTag_count}"/>			
			var fill = d3.scale.category20();
			var i = -1;
			var a = new Array();
    		<c:forEach var="tag" items="${hashUserTag_name}">
    			i++;
    			a[i] = '${tag}';
			</c:forEach>
			alert(a);
			d3.layout.cloud().size([ 300, 300 ]).words(
					a.map(function(d, idx) {
						return {
							text : idx,
							size : 10 + Math.random() * 90
						};
					})).padding(5).rotate(function() {
				return ~~(Math.random() * 2) * 90;
			}).font("Impact").fontSize(function(d) {
				return d.size;
			}).on("end", draw).start();
			function draw(words) {
				d3.select("#key-word").append("svg").attr("width", 300).attr(
						"height", 300).append("g").attr("transform",
						"translate(150,150)").selectAll("text").data(words)
						.enter().append("text").style("font-size", function(d) {
							return d.size + "px";
						}).style("font-family", "Impact").style("fill",
								function(d, i) {
									return fill(i);
								}).attr("text-anchor", "middle").attr(
								"transform",
								function(d) {
									return "translate(" + [ d.x, d.y ]
											+ ")rotate(" + d.rotate + ")";
								}).text(function(d) {
							return d.text;
						});
			}
		</script>
	</div>
</div>
<jsp:include page="template-bottom.jsp" />
