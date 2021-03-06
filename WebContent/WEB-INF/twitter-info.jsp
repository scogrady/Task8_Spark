<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="databeans.TwitterBean"%>


<jsp:include page="template-top.jsp" />
<div class="row">
	<div class="col-md-5">
		<a class="twitter-timeline" href="/task8team2/timelines/566006363294564353" data-widget-id="566020333258690562">custom timeline</a>
		<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+"://platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");</script>
		<%TwitterBean[] tweets = (TwitterBean[]) request.getAttribute("tweets"); 
  			String[] htmls = (String[]) request.getAttribute("htmls");
		%>	
	</div>
	

	<div class="col-md-6" style="border: 1px solid #e8e8e8; border-radius: 5px margin-right: 20px">
		<h4>All Tweets from our site :</h4>
		<a href="https://twitter.com/intent/tweet?button_hashtag=love_adventure2" class="twitter-hashtag-button" data-related="ezna_Hux">Tweet #love_adventure2</a>
		
		<div>
			<form role="form" method="GET" action="twitterInfo.do">
				<div class="input-group">
					<input type="text" name="searchKey" class="form-control" placeholder="Search">
	      			<span class="input-group-btn">
    	    			<input class="btn btn-primary" type="submit" name="action" value="Search">
	      		 	</span>
    	  		 </div>
    	  	</form>
	   	</div>
	   	<hr>
			
		<ul id="my-tweet" style="height: 600px; overflow:auto">
			<c:forEach var="embedTweet" items="${allTweetsHtml}">
				<li style="list-style-type: none">
					<div>
						${embedTweet}
					</div>
					<hr>
				</li> 
			</c:forEach>
		</ul>	
	</div>
</div>

<jsp:include page="template-bottom.jsp" />

