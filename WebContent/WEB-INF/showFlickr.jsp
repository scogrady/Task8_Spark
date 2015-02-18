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
	    	 <a class="btn btn-primary" href="getRamdomTags.do">Sysout Flickr Photo location</a>
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
        		<div class="Image_Wrapper" data-caption="${pic.description } <a class='btn btn-primary btn-xs' data-toggle='modal' data-target='#myModal${pic.id }'>View Comments</a>">
        			<a>
        				<img src="${pic.url}">

        			</a>
        		</div>
        		<!-- Modal -->
    		</c:forEach>
    	 </div>
         	<c:forEach var="pic" items="${flickr}">   	 
    	 		<div class="modal fade" id="myModal${pic.id }" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  					<div class="modal-dialog">
    					<div class="modal-content">
					    	<div class="modal-header">
        						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        						<h4 class="modal-title" id="myModalLabel">${pic.title }</h4>
        						<h6 class="modal-title" id="myModalLabel">${pic.description }</h6>
        						
      						</div>
      						<div class="modal-body">
        						<c:forEach var="comment" items="${pic.comment}">
        							<div>
        								${comment }
        							</div>
        						</c:forEach>   	 
      						</div>
      						<div class="modal-footer">
        						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      						</div>
    					</div>
  					</div>
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
	    </script>

<jsp:include page="template-bottom.jsp" />
