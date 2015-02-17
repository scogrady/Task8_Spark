<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="template-top.jsp" />

<div class="row">

		
		<form method="post" action="searchFlickr.do" >
   		 <div class="input-group col-md-6" style="margin-left: 10px">
      		<input name="searchKey" type="text" class="form-control" placeholder="Input tags for search...">
      		<span class="input-group-btn">
        		<button class="btn btn-primary" type="submit">Search</button>
      	 	</span>
    	 </div>	
    	 </form>
    	 <a href="loginFlickr.do">Login to flickr and upload ur own photos</a>	
    	 <div>
    	 	<h5>Try this hot tags:</h5>
    	 	<a></a>

    	 </div>
		 <div class="Collage">
        	<c:forEach var="url" items="${flickr}">
        	<img src="${url.getUrl()}">
        		<div class="Image_Wrapper" data-caption="Caption">
        			<a>
        				<img src="${url.getUrl()}">
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
