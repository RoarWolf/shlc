<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport"
	content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<!--App自定义的css-->
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/app.css" />
<script type="text/javascript" src="${hdpath }/mui/js/mui.min.js"></script>
<title>余额明细</title>
</head>
<body>
	<div style="padding-top: 10px" class="mui-content">
		<ul class="mui-table-view">
			<c:forEach items="${merEarnList}" var="earn">
				<li class="mui-table-view-cell" id="wolfli${earn.id }" value="${earn.id }">
					<c:if test="${earn.status == 1 }">
						<font size="3px">
							<strong>
								${earn.paysource == 1 ? "收入" : earn.paysource == 2 ? "收入"
								: earn.paysource == 3 ? "收入" : earn.paysource == 4 ? "提现"
								: earn.paysource == 5 ? "收入" : earn.paysource == 6 ? "收入" :
								  earn.paysource == 7 ? "收入" : earn.paysource == 8 ? "缴费收入" : "未知收益"}
							</strong>
						</font>
						<span style="position: absolute; right: 27px;">
								<font size="4px" color="#44b549">+ 
								<fmt:formatNumber value="${earn.money}" pattern="0.00" />
								</font>
						</span>
					</c:if>
					<c:if test="${earn.status == 2 }">
						<font size="3px">
							<strong>
								<c:choose>
									<c:when test="${earn.paysource == 1}">退款</c:when>
									<c:when test="${earn.paysource == 2}">退款</c:when>
									<c:when test="${earn.paysource == 3}">退款</c:when>
									<c:when test="${earn.paysource == 4}">提现</c:when>
									<c:when test="${earn.paysource == 5}">退款</c:when>
									<c:when test="${earn.paysource == 6}">收入</c:when>
									<c:when test="${earn.paysource == 7}">退款</c:when>
									<c:when test="${earn.paysource == 8}">
										<c:if test="${earn.paytype == 1}">钱包</c:if><c:if test="${earn.paytype == 2}">微信</c:if>缴费
									</c:when>
									<c:otherwise>未知收益</c:otherwise>
								</c:choose>
								<%-- ${earn.paysource == 1 ? "退款" : earn.paysource == 2 ? "退款"
								: earn.paysource == 3 ? "退款" : earn.paysource == 4 ? "提现"
								: earn.paysource == 5 ? "退款" : earn.paysource == 6 ? "收入" :
								  earn.paysource == 7 ? "退款" : earn.paysource == 8 ? "缴费" : "未知收益"} --%>
							</strong>
						</font>
						<span style="position: absolute; right: 27px;">
							<font size="4px" color="#F47378">- 
								<fmt:formatNumber value="${earn.money}" pattern="0.00" />
							</font>
						</span>
					</c:if>
				<br> 
				<font size="1px">
					<fmt:formatDate value="${earn.createTime }" pattern="yyyy-MM-dd HH:mm:ss" />
				</font> 
				<span style="position: absolute; right: 27px;">
					<font size="1px">余额：${earn.balance }</font>
				</span>
				</li>
			</c:forEach>
		</ul>
	</div>
</body>
</html>
<script>
$(function() {
	$("li[id^=wolfli]").click(function() {
		window.location.href = "${hdpath}/merchant/merEarnDetailInfo?id=" + $(this).val();
	});
})
</script>