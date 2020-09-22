<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>支付错误</title>
<%@ include file="/WEB-INF/views/public/commons.jspf" %>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
</head>
<body>
<div class="container">
		<div align="center">
			<font size="10px" style="margin: auto;">选取参数错误信息</font>
		</div>
		<div style="padding-top: 30px" align="center">
			<font color="red" size="20px" style="margin: auto;">${errorinfo }</font>
		</div>
	</div>
</body>
</html>