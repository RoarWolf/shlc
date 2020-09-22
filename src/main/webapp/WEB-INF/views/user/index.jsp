<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Insert title here</title>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
</head>
<body>
<div class="container">
	<div align="center" style="padding-top: 5px">
		<a href="${hdpath }/user/list" class="btn btn-success" style="height: 100px; width: 200px;">登陆</a>
	</div>
	<div align="center" style="padding-top: 5px">
		<a href="${hdpath }/merchant/index" class="btn btn-success" style="height: 100px; width: 200px;">进入商户页面</a>
	</div>
	<div align="center" style="padding-top: 5px">
		<a href="${hdpath }/agency/index" class="btn btn-success" style="height: 100px; width: 200px;">进入代理商页面</a>
	</div>
	<div align="center" style="padding-top: 5px">
		<a href="${hdpath }/general/index" class="btn btn-success" style="height: 100px; width: 200px;">用户个人中心</a>
	</div>
	<!-- <div>
		<a href="" class="btn btn-primary" style="height: 100px; width: 200px;">测试</a>
	</div> -->
</div>
</body>
</html>