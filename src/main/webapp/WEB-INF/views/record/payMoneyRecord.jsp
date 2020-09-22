<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>充值记录</title>
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/icons-extra.css" />
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="${hdpath}/mui/js/mui.min.js">
<script type="text/javascript" src="${hdpath}/js/jquery-2.1.0.js"></script>

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
	<h1 class="mui-title">充值记录</h1>
</header>
<div class="contentinfo">
	<c:forEach items="${payMoneyRecord}" var="paymoney">
		<ul class="mui-table-view">
			<li class="mui-table-view-cell">
				<a href="/money/payMoneyinfo?id=${paymoney.id}" class="mui-navigate-right">
					<%-- <span>${paymoney.ordernum}</span> --%>
					<span>充值金额：${paymoney.money}</span>
					<span class="span-right">充值方式：
						<c:choose>
							<c:when test="${paymoney.paytype == 0}"><font>钱包充值</font></c:when>
							<c:when test="${paymoney.paytype == 1}"><font>卡充值</font></c:when>
							<c:when test="${paymoney.paytype == 2}"><font>卡退费</font></c:when>
 						</c:choose>
 					</span><br>
					<span>充值时间：<fmt:formatDate value="${paymoney.paytime}" pattern="yyyy-MM-dd HH:mm:ss" /></span>
				</a>
			</li>
		</ul>
	</c:forEach>
   </div>
   </div>
</body>
</html>