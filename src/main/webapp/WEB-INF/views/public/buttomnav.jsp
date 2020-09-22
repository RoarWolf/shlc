<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="hdpath" value="${pageContext.request.contextPath }" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<!--App自定义的css-->
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/app.css" />
<script src="${pageContext.request.contextPath }/mui/js/mui.min.js"></script>
<script src="${hdpath}/js/jquery.js"></script>
</head>
<body>
	<nav class="mui-bar mui-bar-tab">
		<a style="text-decoration: none;" <c:if test="${homeclick == 1 }"> class="mui-tab-item mui-active" </c:if>
			<c:if test="${homeclick != 1 }"> class="mui-tab-item" </c:if>
			href="${hdpath }/merchant/index">
			<span class="mui-icon mui-icon-home"></span>
			<span class="mui-tab-label">首页</span>
		</a>
		<a style="text-decoration: none;" <c:if test="${manageclick == 1 }"> class="mui-tab-item mui-active" </c:if>
			<c:if test="${manageclick != 1 }"> class="mui-tab-item" </c:if>
			href="${hdpath }/merchant/manage">
			<span class="mui-icon mui-icon-list"></span>
			<span class="mui-tab-label">管理</span>
		</a>
		<a style="text-decoration: none;" <c:if test="${myclick == 1 }"> class="mui-tab-item mui-active" </c:if>
			<c:if test="${myclick != 1 }"> class="mui-tab-item" </c:if>
			href="${hdpath }/merchant/personcenter">
			<span class="mui-icon mui-icon-contact"></span>
			<span class="mui-tab-label">我的</span>
		</a>
	</nav>
</body>
<script type="text/javascript">
	mui('body nav').on('tap','a',function(){
	    window.top.location.href=this.href;
	});
</script>
</html>