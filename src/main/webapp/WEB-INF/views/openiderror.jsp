<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>提示</title>
<%@ include file="/WEB-INF/views/public/commons.jspf" %>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
</head>
<body>
	<div class="container">
		<div align="center">
			<h3>${openiderror }</h3>
			<div align="center" style="<c:if test='${name == null }'>display:none</c:if>" >敬请关注<h4 style="color: green">${name }</h4></div>
		</div>
		<div align="center" style="padding-top: 20px; 
			<c:if test='${name == null }'>display:none</c:if>"
		>
			<div>
				长按下方二维码
			</div>
			<img src="${hdpath }/images/heteng.jpg">
		</div>
	</div>
</body>
</html>