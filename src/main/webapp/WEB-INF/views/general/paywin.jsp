<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>充值成功页面</title>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<%@ include file="/WEB-INF/views/public/commons.jspf" %>
</head>
<body>
<div class="container">
	<div align="center">
		<font size="10px">${rechargeforname }充值成功，所支付 ： ${money != null ? money/100 : 0 }&nbsp;元</font>
	</div>
	<div align="center" style="padding-top: 20px">
		<a href="${hdpath }/general/recharge" class="btn btn-success"><font size="20px">继续冲值</font></a>
	</div>
	<div align="center" style="padding-top: 30px">
		<a href="${hdpath }/general/index" class="btn btn-success"><font size="20px">返回首页</font></a>
	</div>
</div>
</body>
</html>