<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="hdpath" value="${pageContext.request.contextPath }" />
<!DOCTYPE html>
<html>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/mui/css/iconfont.css">
<head>
</head>
<body>
	<nav class="mui-bar mui-bar-tab">
		<a  <c:if test="${pitchonstation == 1 }"> class="mui-tab-item mui-active" </c:if>
			<c:if test="${pitchonstation != 1 }"> class="mui-tab-item" </c:if>
			href="${hdpath }/general/nearbystation" style="text-decoration: none;">
			<span class="mui-icon iconfont icon-mycharge"></span>
			<span class="mui-tab-label">常用电站</span>
		</a>
		<a  <c:if test="${myclick == 1 }"> class="mui-tab-item mui-active" </c:if>
			<c:if test="${myclick != 1 }"> class="mui-tab-item" </c:if>
			href="${hdpath }/general/wantToRecharge" style="text-decoration: none;">
			<span class="mui-icon iconfont icon-mycharge"></span>
			<span class="mui-tab-label">我要充电</span>
		</a>
		<a  <c:if test="${manageclick == 1 }"> class="mui-tab-item mui-active" </c:if>
			<c:if test="${manageclick != 1 }"> class="mui-tab-item" </c:if>
			href="${hdpath }/charge/queryCharging?uid=${user.id}" style="text-decoration: none;">
			<span class="mui-icon iconfont icon-mycharging"></span>
			<span class="mui-tab-label">正在充电</span>
		</a>
		<a  <c:if test="${homeclick == 1 }"> class="mui-tab-item mui-active" </c:if>
			<c:if test="${homeclick != 1 }"> class="mui-tab-item" </c:if>
			href="${hdpath }/general/index" style="text-decoration: none;">
			<span class="mui-icon iconfont icon-geren"></span>
			<span class="mui-tab-label">个人中心</span>
		</a>
	</nav>
</body>
<script type="text/javascript" id="Sc">
	mui('nav').on('tap','a',function(){
	    window.top.location.href=this.href;
	});
</script>
</html>