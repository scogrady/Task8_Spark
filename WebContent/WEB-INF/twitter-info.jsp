<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="databeans.TwitterBean"%>


<jsp:include page="template-top.jsp" />
<div class="row">
	<div class="col-md-4">
		<a class="twitter-timeline" href="/task8team2/timelines/566006363294564353" data-widget-id="566020333258690562">custom timeline</a>
		<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+"://platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");</script>
		<%TwitterBean[] tweets = (TwitterBean[]) request.getAttribute("tweets"); 
  			String[] htmls = (String[]) request.getAttribute("htmls");
		%>	
	</div>
	
	<div class="col-md-4">
		<h3>Your Tweets from our site:</h3>
		<a href="https://twitter.com/intent/tweet?button_hashtag=love_adventure2" class="twitter-hashtag-button" data-related="ezna_Hux">Tweet #love_adventure2</a>
		<ul id="my-tweet" style="height: 600px; overflow:auto">
			<c:forEach var="embedUserTweet" items="${userTweetsHtml}">
				<li style="list-style-type: none">
					<div>
						${embedUserTweet}
					</div>
					<hr>
				</li> 
			</c:forEach>
		</ul>
	</div>
	
	<div class="col-md-4">
<h3>All Tweets from our site :</h3>
		<a href="https://twitter.com/intent/tweet?button_hashtag=love_adventure2" class="twitter-hashtag-button" data-related="ezna_Hux">Tweet #love_adventure2</a>
		<ul id="my-tweet" style="height: 600px; overflow:auto">
			<c:forEach var="embedTweet" items="${allTweetsHtml}">
				<li style="list-style-type: none">
					<div>
						${embedTweet}
					</div>
					<hr>
				</li> 
			</c:forEach>
		</ul>	</div>
</div>

<jsp:include page="template-bottom.jsp" />

