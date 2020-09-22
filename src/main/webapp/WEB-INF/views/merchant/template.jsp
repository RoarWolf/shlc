<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<title>模板管理</title>
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<!--App自定义的css-->
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/app.css" />
<!--js-->
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script src="${hdpath}/js/jquery.js"></script>
<script type="text/javascript" src="${hdpath }/mui/js/mui.min.js"></script>

<style type="text/css">
.mui-table-view-cell.mui-active{
	background-color: #0062CC;
}
</style>
</head>
<body>

<ul class="mui-table-view">

   <%--  <li class="mui-table-view-cell">
       <a href="${hdpath}/wctemplate/selectstairtempval?status=0" class="mui-navigate-right">充电模板</a>
    </li>
    <li class="mui-table-view-cell">
        <a href="${hdpath}/wctemplate/getstairoffline" class="mui-navigate-right">离线卡充值模板</a>
    </li>
    <li class="mui-table-view-cell">
        <a href="${hdpath}/wctemplate/getstairincoins" class="mui-navigate-right">模拟投币模板</a>
    </li> --%>
    <li class="mui-table-view-cell">
        <a href="${hdpath}/wctemplate/getstairwallet" class="mui-navigate-right">钱包模板</a>
    </li>
    <li class="mui-table-view-cell">
        <a href="${hdpath}/wctemplate/getstaironline" class="mui-navigate-right">在线卡模板</a>
    </li>
    <li class="mui-table-view-cell">
        <a href="${hdpath}/wctemplate/getPackageMonth" class="mui-navigate-right">包月模板</a>
    </li>
    
    
</ul>
</body>
</html>
<script>
$(function(){
    pushHistory();
    window.addEventListener("popstate", function(e) {
    	location.replace('/merchant/manage');
	}, false);
    function pushHistory() {
        var state = {
            title: "title",
            url: "#"
        };
        window.history.pushState(state, "title", "#");
    }
});
</script>
