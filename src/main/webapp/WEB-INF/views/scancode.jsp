<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Insert title here</title>
<%@ include file="/WEB-INF/views/public/commons.jspf" %>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="${hdpath }/js/weixin.js"></script>
</head>
<body>
<div class="container">
	<div align="center">
		<button style="padding-top: 10px;width: 100px;height: 50px" class="btn btn-info" id="scanQRCode">扫码</button>
	</div>
</div>
</body>
</html>