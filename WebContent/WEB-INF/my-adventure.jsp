<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="databeans.FlickrBean"%>

<jsp:include page="template-top.jsp" />
<script src="js/d3.js"></script>
<script src="js/d3.layout.cloud.js"></script>
<div class="row">
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
	<div class="col-md-6 col-md-offset-1">
		<div style="margin-bottom: 50px">
			<c:set var="user" value="${userBean}"/>
			<h1>Hi, ${user.screen_name }</h1>
			<h3>You Have ${user.statuses_count } Friends on Twitter,</h3>
			<h3>Why not Try to Find the ${user.statuses_count + 1} Here?</h3>
			<h3>You Have Already Post ${user.statuses_count } Tweets on Our Twiter</h3>
			<h3>Why not Post the ${user.statuses_count + 1} One for Your Adventure Here?</h3>
			<h3>You Have Got:</h3>
			<c:set var="good" value="${positive}"/>
			<c:set var="bad" value="${negative}"/>
			<h3><i class="glyphicon glyphicon-thumbs-up"></i>${good }<i class="glyphicon glyphicon-thumbs-down"></i>${bad }</h3>						
		</div>
		
		<h1>Your Key Words</h1>
		<div id="key-word">
			<script>
				<c:set var="name" value="${hashUserTag_name}"/>
				<c:set var="count" value="${hashUserTag_count}"/>
				var fill = d3.scale.category20();
				var i = -1;
				var a = new Array();
				<c:forEach var="tag" items="${hashUserTag_name}">
				i++;
				a[i] = '${tag}';
				</c:forEach>

				var b = new Array();
				i = -1;
				<c:forEach var="count" items="${hashUserTag_count}">
				i++;
				b[i] = '${count}';
				</c:forEach>
				d3.layout.cloud().size([ 400, 400 ]).words(
						a.map(function(d, idx) {
							return {
								text : d,
								size : 10 + b[idx] / 10 * 200
							};
						})).padding(5).rotate(function() {
					return (Math.random() * 12) * 30;
				}).font("Impact").fontSize(function(d) {
					return d.size;
				}).on("end", draw).start();
				function draw(words) {
					d3.select("#key-word").append("svg").attr("width", 400)
							.attr("height", 400).append("g").attr("transform",
									"translate(200,200)").selectAll("text")
							.data(words).enter().append("text").style(
									"font-size", function(d) {
										return d.size + "px";
									}).style("font-family", "Impact").style(
									"fill", function(d, i) {
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
</div>
<jsp:include page="template-bottom.jsp" />
