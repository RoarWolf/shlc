<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>用户钱包订单详情查看</title>
<script type="text/javascript" src="${hdpath}/js/calendar.js"></script>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link rel="stylesheet" href="${hdpath}/css/admin.css">
<script src="${hdpath }/js/my.js"></script>
</head>
<body style="background-color:#f2f9fd;">
<div class="header bg-main">
	<%@ include file="/WEB-INF/views/navigation.jsp"%>
</div>
<div class="leftnav" id="lefeMenu">
	<%@ include file="/WEB-INF/views/menu.jsp"%>
</div>
<ul class="bread">
  <li><a href="javascript:void(0)" target="right" class="icon-home">订单详情查看</a></li>
</ul>
<div class="admin">
  <div class="panel admin-panel" id="adminPanel">
    <div class="conditionsd"></div>
  	<div class="table table-div">
		<table class="table table-hover">
			<tbody>
			    <c:choose>
				<c:when test="${paysource == 1||paysource == 5||paysource == 6||paysource == 7||paysource == 8}">
					<tr>
						<th>订单号</th><td>${order.ordernum}</td>
					</tr>
					<tr align="center">
						<th>付款金额</th>
						<td><fmt:formatNumber value="${order.money}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<c:if test="${genre == 0}"><th>欠账金额（元）</th></c:if>
						<c:if test="${genre == 1}"><th>余额（元）</th></c:if>
						<td><fmt:formatNumber value="${order.balance}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>支付方式</th>
						<td>
						  <c:choose>
							<c:when test="${order.paytype == 0 || order.paytype == 2}">微信</c:when>
							<c:when test="${order.paytype == 1 || order.paytype == 6}">虚拟操作</c:when>
							<c:when test="${order.paytype == 3 || order.paytype == 4 || order.paytype == 5}">自动退费</c:when>
						    <c:when test="${order.paytype == 7 || order.paytype == 8}">支付宝</c:when>
						  </c:choose>
						</td>
					</tr>
					<tr align="center">
						<th>操作类型</th>
						<td>${order.paytype == 0 ? "微信钱包充值" : order.paytype == 1 ? "虚拟充值钱包" : order.paytype == 2 ? "钱包退款" : order.paytype == 3 ? "充电记录退费" : 
						order.paytype == 4 ? "离线卡充值退费" : order.paytype == 5 ? "模拟投币退费" : order.paytype == 6 ? "虚拟钱包退费" : 
						order.paytype == 7 ? "支付宝钱包充值" : order.paytype == 8? "钱包退款" : "— —" }</td>
					</tr>
					<tr align="center">
						<th>创建时间</th>
						<td><fmt:formatDate value="${order.paytime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
				</c:when>
				<c:when test="${paysource == 5}">
					<tr>
						<th>订单号</th><td>${order.ordernum}</td>
					</tr>
					<tr align="center">
						<th>退款金额</th>
						<td><fmt:formatNumber value="${order.money}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<c:if test="${genre == 0}"><th>欠账金额（元）</th></c:if>
						<c:if test="${genre == 1}"><th>余额（元）</th></c:if>
						<td><fmt:formatNumber value="${order.balance}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>操作类型</th>
						<td>${order.paytype == 0 ? "钱包充值" : order.paytype == 1 ? "充值卡" : order.paytype == 2 ? "卡退费" : order.paytype == 3 ? "充电记录退费" : order.paytype == 4 ? "离线卡充值退费" : order.paytype == 5 ? "模拟投币退费"
						 : "— —" }</td>
					</tr>
					<tr align="center">
						<th>创建时间</th>
						<td><fmt:formatDate value="${order.paytime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
				</c:when>
				<c:when test="${paysource == 2 }">
					<tr>
						<th>订单号</th><td>${order.ordernum}</td>
					</tr>
					<tr align="center">
						<th>付款金额（元）</th>
						<td><fmt:formatNumber value="${order.expenditure}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>时间（分钟）</th>
						<td>${order.durationtime}</td>
					</tr>
					<tr align="center">
						<th>电量（度）</th>
						<td>${order.quantity/100}</td>
					</tr>
					<tr align="center">
						<th>付款方式</th>
						<td>${order.paytype == 1 ? "钱包" : order.paytype == 2 ? "微信" : order.paytype == 3 ? "支付宝" : "— —" }</td>
					</tr>
					<tr align="center">
						<th>创建时间</th>
						<td><fmt:formatDate value="${order.begintime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr align="center">
						<th>订单状态</th>
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
				<c:when test="${paysource == 3 }">
					<tr>
						<th>订单号</th><td>${order.ordernum}</td>
					</tr>
					<tr align="center">
						<th>付款金额（元）</th>
						<td><fmt:formatNumber value="${order.money}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>投币个数</th>
						<td>${order.coinNum}</td>
					</tr>
					<tr align="center">
						<th>支付方式</th>
						<td>
							<c:choose>
								<c:when test="${order.handletype == 1 || order.handletype == 4}">微信</c:when>
								<c:when test="${order.handletype == 2 || order.handletype == 5}">支付宝</c:when>
								<c:when test="${order.handletype == 3}">投币</c:when>
								<c:when test="${order.handletype == 6 || order.handletype == 7}">钱包</c:when>
								<c:when test="${order.handletype == 8 || order.handletype == 9}">微信小程序</c:when>
								<c:when test="${order.handletype == 10 || order.handletype == 11}">支付宝小程序</c:when>
							</c:choose>
						</td>
					</tr>
					<tr align="center">
						<th>创建时间</th>
						<td><fmt:formatDate value="${order.beginTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr align="center">
						<th>订单状态</th>
						<td>
							<c:choose>
								<c:when test="${order.handletype == 1 || order.handletype == 2 || order.handletype == 3 || order.handletype == 6 || order.handletype == 8 || order.handletype == 10}">正常</c:when>
								<c:when test="${order.handletype == 4}"><font style="color: #ec2a2a;">微信已退费</font></c:when>
								<c:when test="${order.handletype == 5}"><font style="color: #ec2a2a;">支付宝已退费</font></c:when>
								<c:when test="${order.handletype == 7}"><font style="color: #ec2a2a;">已退款到钱包</font></c:when>
								<c:when test="${order.handletype == 9}"><font style="color: #ec2a2a;">微信小程序已退费</font></c:when>
								<c:when test="${order.handletype == 11}"><font style="color: #ec2a2a;">支付宝小程序已退费</font></c:when>
							</c:choose>
						</td>
					</tr>
				</c:when>
				<c:when test="${paysource == 4 }">
					<tr>
						<th>订单号</th><td>${order.ordernum}</td>
					</tr>
					<tr align="center">
						<th>卡号</th>
						<td>${order.cardID }"</td>
					</tr>
					<tr align="center">
						<th>余额</th>
						<td><fmt:formatNumber value="${order.balance }" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>付款金额（元）</th>
						<td><fmt:formatNumber value="${order.accountmoney }" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>充卡金额（元）</th>
						<td><fmt:formatNumber value="${order.chargemoney }" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>创建时间</th>
						<td><fmt:formatDate value="${order.beginTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr align="center">
						<th>订单状态</th>
						<td>
							<c:if test="${order.paytype == 1}"><font>微信</font></c:if>
							<c:if test="${order.paytype == 2}"><font>支付宝</font></c:if>
							<c:if test="${order.paytype == 3}"><font>微信退款</font></c:if>
							<c:if test="${order.paytype == 4}"><font>支付宝退款</font></c:if>
						</td>
					</tr>
				</c:when>
				<c:when test="${paysource == 9}">
					<tr>
						<th>订单号</th><td>${order.ordernum}</td>
					</tr>
					<tr align="center">
						<th>卡号</th>
						<td>${order.cardID}"</td>
					</tr>
					<tr align="center">
						<th>余额</th>
						<td><fmt:formatNumber value="${order.balance}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>付款金额（元）</th>
						<td><fmt:formatNumber value="${order.money}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>到账金额（元）</th>
						<td><fmt:formatNumber value="${order.accountmoney}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>创建时间</th>
						<td><fmt:formatDate value="${order.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr align="center">
						<th>订单类型</th>
						<td>
							<c:if test="${order.type == 1}"><font>消费</font></c:if>
							<c:if test="${order.type == 2}"><font>余额回收</font></c:if>
							<c:if test="${order.type == 3 || order.type == 5}"><font>微信</font></c:if>
							<c:if test="${order.type == 6 || order.type == 7}"><font>支付宝</font></c:if>
							<c:if test="${order.type == 8 || order.type == 9}"><font>虚拟操作</font></c:if>
						</td>
					</tr>
				</c:when>
			</c:choose>
			</tbody>
		</table>
	</div>
  </div>
  </div>
</body>
<script type="text/javascript">
$(document).ready(function(){	
	$('#22'+' a').addClass('at');
	$('#22').parent().parent().parent().prev().removeClass("collapsed");
	$('#22').parent().parent().parent().prev().attr("aria-expanded",true)
	$('#22').parent().parent().parent().addClass("in");
	$('#22').parent().parent().parent().prev().attr("aria-expanded",true)
	})
</script>
</html>