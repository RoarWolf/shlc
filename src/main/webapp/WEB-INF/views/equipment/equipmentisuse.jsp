<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>错误页面</title>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
</head>
<body>
	<div class="container">
		<div align="center">
			<h1>${code }</h1>
			<h1>设备已被绑定</h1>
		</div>
		<c:if test="${rank != 6 }">
			<div align="center">
				<a href="${hdpath }/merchant/manage" class="btn btn-primary">返回管理页面</a>
			</div>
		</c:if>
	</div>
</body>
</html>