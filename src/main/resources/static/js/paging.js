
//菜单定位
function menulocation(tagmenu){	
	$('#'+tagmenu+' a').addClass('at');
	$('#'+tagmenu).parent().parent().parent().prev().removeClass("collapsed");
	$('#'+tagmenu).parent().parent().parent().prev().attr("aria-expanded",true)
	$('#'+tagmenu).parent().parent().parent().addClass("in");
	$('#'+tagmenu).parent().parent().parent().prev().attr("aria-expanded",true)
}

