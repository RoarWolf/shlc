<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>充电记录</title>
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath}/mui/css/mui.min.css">
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/icons-extra.css" />
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="${hdpath}/mui/js/mui.min.js">
<script type="text/javascript" src="${hdpath}/js/jquery-2.1.0.js"></script>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<style type="text/css">
html,body,.body{width:100%;height:100%;font-size: 12px; color: #666;}
body {
	background: #e3e3e6;
}
.nametitle{ padding: 8px 0 8px 15px; background: aquamarine;}
.contentinfo{overflow-y:auto; padding: 59px 10px 15px;}
.contentinfo>ul {
	background: #f5f7fa;
	border-radius: 6px;
    border: 1px solid #ccc;
    margin-bottom: 10px;
}
.contentinfo:after,.contentinfo:before {
	height: 0;
}
.contentinfo>ul>li>a>span{ min-width: 50%;display: inline-block; line-height: 2em;}
hr{margin:0}
</style>
</head>
<body>
<header class="mui-bar mui-bar-nav">
	<a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
	<h1 class="mui-title">充电记录</h1>
</header>
<div class="body">
<div class="contentinfo">
	<c:forEach items="${chargeRecord}" var="charge">
		<ul class="mui-table-view">
			<li class="mui-table-view-cell">
				<a href="${hdpath}/charge/chargeRecordOne?id=${charge.id}" class="mui-navigate-right">
					<span>支付金额：${charge.expenditure}</span>
					<span class="span-right">支付方式：
						<c:choose>
							<c:when test="${charge.paytype == 1}"><font>钱包支付</font></c:when>
							<c:when test="${charge.paytype == 2}"><font>微信支付</font></c:when>
							<c:when test="${charge.paytype == 3}"><font>支付宝支付</font></c:when>
						</c:choose>
 					</span><br>
 					<c:if test="chargeinfo.paytype != 1">
						<span>充电时长：${charge.durationtime}</span><br>
 					</c:if>
					<span>充电时间：<fmt:formatDate value="${charge.begintime}" pattern="yyyy-MM-dd HH:mm:ss" /></span>
				</a>
			</li>
		</ul>
	</c:forEach>
	</div>
   </div>
</body>
</html>