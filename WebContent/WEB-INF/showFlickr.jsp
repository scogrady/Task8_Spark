<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="template-top.jsp" />

<div class="row">
	<%
  			String url[] = (String[]) request.getAttribute("urlList");
		%>	
		 <div class="col-md-6" style="margin-left: 10px">
		 	<div>
			 	<form role="form" method="post" action="searchFlickr.do">
			 		<div class="input-group">
      				<input type="text" name="searchKey" class="form-control" placeholder="Input tags for search...">
	      			<span class="input-group-btn">
    	    			<input class="btn btn-primary" type="submit" name="action" value="Search">
	      		 	</span>
    	  		 	</div>
	    		 </form>
	    	 </div>
	    	 <div>
	    	 	<h3>Hot Tags:</h3>
    		 	<a class="btn btn-default">Diving</a>
    		 	<a class="btn btn-default">Skiing</a>
    		 	<a class="btn btn-default">BMX</a>
    		 	<a class="btn btn-default">Skateboard</a>
    		 	<a class="btn btn-default">Sky Jumping</a>
    		 </div>
    	 </div>
		 <div class="Collage">
        	<c:forEach var="url" items="${urlList}">
        		<div class="Image_Wrapper" data-caption="Caption">
        			<a>
        				<img src="${url}">
        			</a>
        		</div>
    		</c:forEach>
    	</div>
	    <script type="text/javascript">
	    	$(window).load(function () {
	        	$('.Collage').removeWhitespace().collagePlus({
	        		'allowPartialLastRow'       : true,
	                'effect'          : 'effect-3',
	        	});
	        	$('.Collage').collageCaption();
	    	});	    	
	        // This is just for the case that the browser window is resized
	        var resizeTimer = null;
	        $(window).bind('resize', function() {
	            // hide all the images until we resize them
	            $('.Collage .Image_Wrapper').css("opacity", 0);
	            // set a timer to re-apply the plugin
	            if (resizeTimer) clearTimeout(resizeTimer);
	            resizeTimer = setTimeout(collage, 200);
	        });
	    </script>

</div>

<jsp:include page="template-bottom.jsp" />
