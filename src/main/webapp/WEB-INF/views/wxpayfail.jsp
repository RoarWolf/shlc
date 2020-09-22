<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>支付失败</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<!--App自定义的css-->
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/app.css" />
<%@ include file="/WEB-INF/views/public/commons.jspf" %>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
</head>
<body>
<div class="container">
	<div align="center" style="padding-top: 30px">
		<font size="30px">支付失败</font>
	</div>
	<br><br><br>
	<div>
		<a id="backbtn" href="javascript:WeixinJSBridge.call('closeWindow');" class="mui-btn mui-btn-success mui-btn-block">返回</a>
	</div>
</div>
<script type="text/javascript">
$(function(){  
    pushHistory();  
    window.addEventListener("popstate", function(e) {
        WeixinJSBridge.call('closeWindow');
	}, false);  
    function pushHistory() {  
        var state = {  
            title: "title",  
            url: "#"  
        };
        window.history.pushState(state, "title", "#");  
    }  
      
});
/* $(function() {
	$("#backbtn").click(function() {
		WeixinJSBridge.call('closeWindow');
	})
}) */
</script>
</body>
</html>