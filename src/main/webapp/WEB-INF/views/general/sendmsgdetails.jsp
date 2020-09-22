<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<title>推送详情</title>
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<!--App自定义的css-->
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/app.css" />
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script src="${pageContext.request.contextPath }/mui/js/mui.min.js"></script>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
</head>
<body>
	<div class="container" style="padding-top: 30px">
		<table border="1" class="table table-bordered">
			<c:choose>
				<c:when test="${source == 1}">
					<tr align="center">
						<td>订单号</td>
						<td>${order.ordernum}</td>
					</tr>
					<tr align="center">
						<td>付款金额（元）</td>
						<td><fmt:formatNumber value="${order.expenditure}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<td>时间（分钟）</td>
						<td>${order.durationtime}</td>
					</tr>
					<tr align="center">
						<td>电量（度）</td>
						<td>${order.quantity/100}</td>
					</tr>
					<tr align="center">
						<td>付款方式</td>
						<td>${order.paytype == 1 ? "钱包" : order.paytype == 2 ? "微信" : order.paytype == 3 ? "支付宝" : "— —" }</td>
					</tr>
					<tr align="center">
						<td>创建时间</td>
						<td><fmt:formatDate value="${order.begintime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr align="center">
						<td>订单状态</td>
						<td>
							<c:if test="${order.number == 1}">
								<font color="gray">全额退款</font>
							</c:if>
							<c:if test="${order.number == 0}">
								<font color="#5cb85c">正常</font>
							</c:if>
							<c:if test="${order.number == 2}">
								<font color="gray">部分退款</font>(<font color="red">${order.refundMoney}</font>)
							</c:if>
					</tr>
				</c:when>
				<c:when test="${source == 2}">
					<tr align="center">
						<td>订单号</td>
						<td>${order.ordernum}</td>
					</tr>
					<tr align="center">
						<td>卡号</td>
						<td>${order.cardID}</td>
					</tr>
					<tr align="center">
						<td>余额</td>
						<td>${order.balance}</td>
					</tr>
					<tr align="center">
						<td>付款金额（元）</td>
						<td><fmt:formatNumber value="${order.chargemoney}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<td>充卡金额</td>
						<td><fmt:formatNumber value="${order.accountmoney}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<td>付款方式</td>
						<td>${order.paytype == 1 ? "微信" : order.paytype == 2 ? "支付宝" : order.paytype == 3 ? "微信" : order.paytype == 4 ? "支付宝" : "—— ——"}</td>
					</tr>
					<tr align="center">
						<td>创建时间</td>
						<td><fmt:formatDate value="${order.beginTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr align="center">
						<td>订单状态</td>
						<td>
							<c:if test="${order.paytype == 3 || order.paytype == 4}">
								<font color="gray">已退款</font>
							</c:if>
							<c:if test="${order.paytype == 1 || order.paytype == 2}">
								<font color="#5cb85c">正常</font>
							</c:if>
						</td>
					</tr>
				</c:when>
				<c:when test="${source == 3 }">
					<tr align="center">
						<td>订单号</td>
						<td>${order.ordernum}</td>
					</tr>
					<tr align="center">
						<td>付款金额</td>
						<td><fmt:formatNumber value="${order.money}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<td>付款方式</td>
						<td>${order.handletype == 1 ? "微信" : order.handletype == 2 ? "支付宝" : order.handletype == 3 ? "投币器"
						 : order.handletype == 4 ? "微信" : order.handletype == 5 ? "支付宝"
						 : order.handletype == 6 ? "钱包" : order.handletype == 7 ? "钱包" : "— —" }</td>
					</tr>
					<tr align="center">
						<td>创建时间</td>
						<td><fmt:formatDate value="${order.beginTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr align="center">
						<td>订单状态</td>
						<td>
							<c:if test="${order.handletype == 4 || order.handletype == 5 || order.handletype == 7}">
								<font color="gray">已退款</font>
							</c:if>
							<c:if test="${order.handletype == 1 || order.handletype == 2 || order.handletype == 3 || order.handletype == 6}">
								<font color="#5cb85c">正常</font>
							</c:if>
						</td>
					</tr>
				</c:when>
				<c:when test="${source == 4 }">
					<tr align="center">
						<td>订单号</td>
						<td>${order.ordernum}</td>
					</tr>
					<tr align="center">
						<td>充值金额</td>
						<td><fmt:formatNumber value="${order.money}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<td>冲后余额</td>
						<td><fmt:formatNumber value="${order.balance}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<td>充值时间</td>
						<td><fmt:formatDate value="${order.paytime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr align="center">
						<td>订单状态</td>
						<td>
							<c:if test="${order.paytype == 0}">
								<font color="gray">充值成功</font>
							</c:if>
							<c:if test="${order.paytype == 2 || order.paytype == 3 || order.paytype == 4 || order.paytype == 5}">
								<font color="#5cb85c">退费</font>
							</c:if>
						</td>
					</tr>
				</c:when>
				<c:when test="${source == 5 }">
					<tr align="center">
						<td>卡号</td>
						<td>${order.cardID}</td>
					</tr>
					<tr align="center">
						<td>订单号</td>
						<td>${order.ordernum}</td>
					</tr>
					<tr align="center">
						<td>充值金额</td>
						<td><fmt:formatNumber value="${order.money}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<td>到账金额</td>
						<td><fmt:formatNumber value="${order.accountmoney}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<td>余额</td>
						<td><fmt:formatNumber value="${order.balance}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<td>创建时间</td>
						<td><fmt:formatDate value="${order.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr align="center">
						<td>类型</td>
						<td>${order.type==1 ? "消费" : order.type==2 ? "余额回收" : order.type==3 ? "充值" : order.type==4 ? "卡操作" : order.type==5 ? "退款" : "— —"}</td>
					</tr>
					<tr align="center">
						<td>订单状态</td>
						<td><font>${order.status==1 ? "正常" : order.status==2 ? "激活" : order.status==3 ? "绑定" : order.type==4 ? "删除" : order.status==5 ? "挂失" : order.status==6 ? "解挂" : order.status==7 ? "卡号修改" : "修改备注"}</font></td>
					</tr>
				</c:when>
			</c:choose>
		</table>
	</div>
</body>
</html>