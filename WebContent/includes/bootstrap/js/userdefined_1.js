	$("html").click(function(e) {
var subject = $("#sidebarDiv"); 
	if (!$(e.target).hasClass('fa fa-gears') && !subject.has(e.target).length) {
	if($('.control-sidebar').hasClass('control-sidebar-open')){
	$(".control-sidebar").removeClass("control-sidebar-open")
	}
    }
	
});