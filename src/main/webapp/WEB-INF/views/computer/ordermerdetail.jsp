<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>商户收益订单详情查看</title>
<script type="text/javascript" src="${hdpath}/js/calendar.js"></script>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link rel="stylesheet" href="${hdpath}/css/admin.css">
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
				<!-- //商户 paysource  金额来源 1、充电模块-2、脉冲模块-3、离线充值机-4、提现-5、用户充值钱包', -->
			    <c:choose>
				<c:when test="${paysource == 1}">
					<tr>
						<th>订单号</th><td>${order.ordernum}</td>
					</tr>
					<tr align="center">
						<th>付款金额（元）</th>
						<td><fmt:formatNumber value="${order.expenditure}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>充电时间（分钟）</th>
						<td>${order.durationtime}</td>
					</tr>
					<tr align="center">
						<th>充电电量（度）</th>
						<td>${order.quantity/100}</td>
					</tr>
					<tr align="center">
						<th>付款方式</th>
						<td>${order.paytype == 1 ? "钱包" : order.paytype == 2 ? "微信" : order.paytype == 3 ? "支付宝" : "— —" }</td>
					</tr>
					<tr align="center">
						<th>充电时间</th>
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
				<c:when test="${paysource == 2 }">
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
						<th>交易时间</th>
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
				<c:when test="${paysource == 3 }">
					<tr>
						<th>订单号</th><td>${order.ordernum}</td>
					</tr>
					<tr align="center">
						<th>卡号</th>
						<td>${order.cardID}"</td>
					</tr>
					<tr align="center">
						<th>卡余额</th>
						<td><fmt:formatNumber value="${order.balance }" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>付款金额（元）</th>
						<td><fmt:formatNumber value="${order.accountmoney}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>充卡金额（元）</th>
						<td><fmt:formatNumber value="${order.chargemoney }" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>支付方式</th>
						<td>
							<c:if test="${order.paytype == 1 || order.paytype == 3}"><font>微信</font></c:if>
							<c:if test="${order.paytype == 2 || order.paytype == 4}"><font>支付宝</font></c:if>
						</td>
					</tr>
					<tr align="center">
						<th>充值时间</th>
						<td><fmt:formatDate value="${order.beginTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr align="center">
						<th>订单状态</th>
						<td>
							<c:if test="${order.paytype == 1 || order.paytype == 2 }"><font>正常</font></c:if>
							<c:if test="${order.paytype == 3 || order.paytype == 4 }"><font>退款</font></c:if>
						</td>
					</tr>
				</c:when>
				<c:when test="${paysource == 4 }"> 
					<tr>
						<th>订单号</th><td>${order.withdrawnum}</td>
					</tr>
					<tr align="center">
						<th>提现金额</th>
						<td><fmt:formatNumber value="${order.money}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>手续费</th>
						<td><fmt:formatNumber value="${order.servicecharge}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>提现前金额</th>
						<td><fmt:formatNumber value="${order.userMoney}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>银行</th>
						<td>${order.bankname}</td>
					</tr>
					<tr align="center">
						<th>银行卡号</th>
						<td>${order.bankcardnum}</td>
					</tr>
					<tr align="center">
						<th>订单状态</th>
						<td>
							<c:if test="${order.status == 0}">
								<font color="gray">待处理</font>
							</c:if>
							<c:if test="${order.status == 1}">
								<font color="#5cb85c">已通过</font>
							</c:if>
							<c:if test="${order.status == 2}">
								<font color="#5cb85c">被拒绝</font>
							</c:if>
						</td>
					</tr>
					<tr align="center">
						<th>创建时间</th>
						<td><fmt:formatDate value="${order.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
				</c:when>
				<c:when test="${paysource == 5 }">
					<tr>
						<th>订单号</th><td>${order.ordernum}</td>
					</tr>
					<tr align="center">
						<th>付款金额</th>
						<td>
							<fmt:formatNumber value="${order.money}" pattern="0.00"/>
						</td>
					</tr>
					<tr align="center">
						<th>钱包金额（元）</th>
						<td><fmt:formatNumber value="${order.balance}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>支付方式</th>
						<td>
							<font>微信</font>
						</td>
					</tr>
					<tr align="center">
						<th>操作类型</th>
						<td>${order.paytype == 0 ? "钱包充值" : order.paytype == 1 ? "充值卡" : order.paytype == 2 ? "退款" : order.paytype == 3 ? "充电记录退费" : order.paytype == 4 ? "离线卡充值退费" : order.paytype == 5 ? "模拟投币退费" : "— —" }</td>
					</tr>
					<tr align="center">
						<th>交易时间</th>
						<td><fmt:formatDate value="${order.paytime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr align="center">
						<th>订单状态</th>
						<td>
						  <c:choose>
							<c:when test="${order.paytype == 0 || order.paytype == 1}">正常</c:when>
							<c:when test="${order.paytype == 2}"><font style="color: #ec2a2a;">钱包退款</font></c:when>
							<c:when test="${order.paytype == 3 || order.paytype == 4 || order.paytype == 5}"><font style="color: #ec2a2a;">退费到钱包</font></c:when>
						  </c:choose>
						</td>
					</tr>
				</c:when>
				<c:when test="${paysource == 6 }"> 
					<tr>
						<th>订单号</th><td>${order.ordernum}</td>
					</tr>
					<tr align="center">
						<th>卡号</th>
						<td>${order.cardID}</td>
					</tr>
					<tr align="center">
						<th>余额</th>
						<td><fmt:formatNumber value="${order.balance}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>充值金额</th>
						<td><fmt:formatNumber value="${order.money}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>到账金额</th>
						<td><fmt:formatNumber value="${order.accountmoney}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>类型</th>
						<td>${order.type == 1 ? "消费" : order.type == 2 ? "退款": order.type == 3 ? "充值" : order.type == 4 ? "卡操作"  : "其它"}</td>
					</tr>
					<tr align="center">
						<th>订单状态</th>
						<td>
						   ${order.status == 0 ? "<font color='gray'>未成功</font>" : order.status == 1 ? "<font color='#5cb85c'>成功</font>" : order.status == 2 ? 
						   "激活": order.status == 3 ? "绑定" : order.status == 4 ? "解绑" : order.status == 5 ? "挂失" : order.status == 6 ? "解挂"  : "注销"}
						</td>
					</tr>
					<tr align="center">
						<th>创建时间</th>
						<td><fmt:formatDate value="${order.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
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
	$('#21'+' a').addClass('at');
	$('#21').parent().parent().parent().prev().removeClass("collapsed");
	$('#21').parent().parent().parent().prev().attr("aria-expanded",true)
	$('#21').parent().parent().parent().addClass("in");
	$('#21').parent().parent().parent().prev().attr("aria-expanded",true)
	})
</script>
</html>