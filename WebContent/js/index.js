$(function(){
	var listItems = $("#group-function li");
	listItems.each(function(idx, li) {
	    var product = $(li);
	    if (window.location.pathname.indexOf(product.attr("name")) == -1) {
	    	product.removeClass("active");
	    } else {
	    	product.addClass("active");
	    }
	});
}) 
