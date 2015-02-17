<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="databeans.FlickrBean"%>

<jsp:include page="template-top.jsp" />
		 <div class="col-md-6" style="margin-left: 10px">
		 	<div>
			 	<form role="form" method="post" action="searchFlickr.do">
			 		<div class="input-group">
      				<input id="search-key" type="text" name="searchKey" class="form-control" placeholder="Input tags for search...">
	      			<span class="input-group-btn">
    	    			<input id="search-btn" class="btn btn-primary" type="submit" name="action" value="Search">
	      		 	</span>
    	  		 	</div>
	    		 </form>
	    	 </div>
	    	 <a class="btn btn-primary" href="loginFlickr.do">Login With Flickr</a>
	    	 <div>
	    	 	<h3>Hot Tags:</h3>
    		 	<a class="btn btn-default hot-tag" href="searchFlickr.do?searchKey=sport">sport</a>
    		 	<a class="btn btn-default hot-tag">Skiing</a>
    		 	<a class="btn btn-default hot-tag">BMX</a>
    		 	<a class="btn btn-default hot-tag">Skateboard</a>
    		 	<a class="btn btn-default hot-tag">Sky Jumping</a>
    		 </div>
    	 </div>
		 <div class="Collage col-md-12">
        	<c:forEach var="pic" items="${flickr}">
        		<div class="Image_Wrapper" data-caption="Want to know more about this? <a class='btn btn-primary btn-xs view-comment' data-toggle='modal' data-target='#myModal${pic.id }'>View Detail</a>">
        			<a>
        				<img src="${pic.url}">

        			</a>
        		</div>
        		<!-- Modal -->
    		</c:forEach>
    	 </div>
    	 	<c:forEach var="pic" items="${flickr}">   	 
    	 		<div class="modal fade" id="myModal${pic.id }" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
				</div>				
    		</c:forEach>

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
	        $(".hot-tag").on("click", function(event){
	        	$("#search-key").text($(this).text());
	        })
	        $(window).load(function () {
		        $(".view-comment").on("click", function(){
		        	var modalId = $(this).attr("data-target");
		        	$.ajax({
		        		type: "GET",
		        		url: "GetFlickrDetail",
		        		data: {id : $(this).attr("data-target")},
		        		success: function(data) {
		        			$(modalId).add("in");
		        			$(modalId).html(data);
		    	        	$(modalId).modal();
		        		}
		        	});
	        		return false;
		        });
	    	});	    

	    </script>

<jsp:include page="template-bottom.jsp" />
