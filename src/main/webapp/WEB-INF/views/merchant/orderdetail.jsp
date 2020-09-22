<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<title>订单详情</title>
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<!--App自定义的css-->
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/app.css" />
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script src="${pageContext.request.contextPath }/mui/js/mui.min.js"></script>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<script src="${hdpath }/js/my.js"></script>
</head>
<body>
	<div class="container" style="padding-top: 30px">
		<input type="hidden" id="orderid" value="${chargeRecord.id }">
		<input type="hidden" id="refundnum" value="${hardversion }">
		<table border="1" class="table table-bordered">
			<tr align="center">
				<td>订单号</td>
				<td>${chargeRecord.ordernum }</td>
			</tr>
			<c:choose>
				<c:when test="${hardversion == 1 }">
					<tr align="center">
						<td>付款金额（元）</td>
						<td><fmt:formatNumber value="${chargeRecord.expenditure }" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<td>时间（分钟）</td>
						<td>${chargeRecord.durationtime }</td>
					</tr>
					<tr align="center">
						<td>电量（度）</td>
						<td>${chargeRecord.quantity/100 }</td>
					</tr>
					<tr align="center">
						<td>付款方式</td>
						<td>${chargeRecord.paytype == 1 ? "钱包" : chargeRecord.paytype == 2 ? "微信" : chargeRecord.paytype == 3 ? "支付宝" : "— —" }</td>
					</tr>
					<tr align="center">
						<td>创建时间</td>
						<td><fmt:formatDate value="${chargeRecord.begintime }"
								pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr align="center">
						<td>订单状态</td>
						<td>
							<c:if test="${chargeRecord.number == 1}">
								<font color="gray">全额退款</font>
							</c:if>
							<c:if test="${chargeRecord.number == 0}">
								<font color="#5cb85c">正常</font>
								<c:if test="${chargeRecord.paytype == 2 }">
									<button class="btn btn-success" id="refund" value="1">退款</button>
								</c:if>
								<c:if test="${chargeRecord.paytype == 3 }">
									<button class="btn btn-success" id="refund" value="2">退款</button>
								</c:if>
							</c:if>
							<c:if test="${chargeRecord.number == 2}">
								<font color="gray">部分退款</font>(<font color="red">${chargeRecord.refundMoney }</font>)
							</c:if>
					</tr>
				</c:when>
				<c:when test="${hardversion == 2 }">
					<tr align="center">
						<td>卡号</td>
						<td>${chargeRecord.cardID }</td>
					</tr>
					<tr align="center">
						<td>余额</td>
						<td>${chargeRecord.balance }</td>
					</tr>
					<tr align="center">
						<td>付款金额（元）</td>
						<td><fmt:formatNumber value="${chargeRecord.chargemoney }" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<td>充卡金额</td>
						<td><fmt:formatNumber value="${chargeRecord.accountmoney }" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<td>付款方式</td>
						<td>${chargeRecord.paytype == 1 ? "微信" : chargeRecord.paytype == 2 ? "支付宝" : chargeRecord.paytype == 3 ? "微信" : chargeRecord.paytype == 4 ? "支付宝" : "—— ——"}</td>
					</tr>
					<tr align="center">
						<td>创建时间</td>
						<td><fmt:formatDate value="${chargeRecord.beginTime }"
								pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr align="center">
						<td>订单状态</td>
						<td>
							<c:if test="${chargeRecord.paytype == 3 || chargeRecord.paytype == 4}">
								<font color="gray">已退款</font>
							</c:if>
							<c:if test="${chargeRecord.paytype == 1 || chargeRecord.paytype == 2}">
								<font color="#5cb85c">正常</font>
								<c:if test="${chargeRecord.paytype == 1 }">
									<button class="btn btn-success" id="refund" value="1">退款</button>
								</c:if>
								<c:if test="${chargeRecord.paytype == 2 }">
									<button class="btn btn-success" id="refund" value="2">退款</button>
								</c:if>
							</c:if>
						</td>
					</tr>
				</c:when>
				<c:when test="${hardversion == 3 }">
					<tr align="center">
						<td>付款金额</td>
						<td><fmt:formatNumber value="${chargeRecord.money }" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<td>付款方式</td>
						<td>${chargeRecord.handletype == 1 ? "微信" : chargeRecord.handletype == 2 ? "支付宝" : chargeRecord.handletype == 3 ? "投币器"
						 : chargeRecord.handletype == 4 ? "微信" : chargeRecord.handletype == 5 ? "支付宝" : chargeRecord.handletype == 8 ? "微信小程序" : 
						 chargeRecord.handletype == 9 ? "微信小程序" : chargeRecord.handletype == 12 ? "银联" : "— —" }</td>
					</tr>
					<tr align="center">
						<td>创建时间</td>
						<td><fmt:formatDate value="${chargeRecord.beginTime }"
								pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr align="center">
						<td>订单状态</td>
						<td>
							<c:if test="${chargeRecord.handletype == 4 || chargeRecord.handletype == 5 || chargeRecord.handletype == 9 || chargeRecord.handletype == 13}">
								<font color="gray">已退款</font>
							</c:if>
							<c:if test="${chargeRecord.handletype == 1 || chargeRecord.handletype == 2 || chargeRecord.handletype == 3 || chargeRecord.handletype == 8 || chargeRecord.handletype == 12}">
								<font color="#5cb85c">正常</font>
								<c:if test="${chargeRecord.handletype == 1 }">
									<button class="btn btn-success" id="refund" value="1">退款</button>
								</c:if>
								<c:if test="${chargeRecord.handletype == 2 }">
									<button class="btn btn-success" id="refund" value="2">退款</button>
								</c:if>
								<c:if test="${chargeRecord.handletype == 8 }">
									<button class="btn btn-success" id="refund" value="3">退款</button>
								</c:if>
								<c:if test="${chargeRecord.handletype == 12 }">
									<button class="btn btn-success" id="refund" value="4">退款</button>
								</c:if>
							</c:if>
						</td>
					</tr>
				</c:when>
			</c:choose>
		</table>
		<nav class="navbar navbar-default navbar-fixed-bottom" role="navigation">
			<div align="center"><a href="${hdpath }/merchant/manage" class="btn btn-info">回管理页面</a></div>
		</nav>
	</div>
</body>
<script>
$(function() {
	document.getElementById("refund").addEventListener('tap', function() {
		var refundtype = $(this).val();
		var btnArray = ['取消', '确认'];
		mui.confirm('确认退款吗 ？', '退款', btnArray, function(e) {
			if (e.index == 1) {
				var orderid = $("#orderid").val();
				var refundnum = $("#refundnum").val();
				var refundUrl;
				if (refundtype == 1) {
					refundUrl = '${hdpath}/wxpay/wxDoRefund';
				} else if (refundtype == 2) {
					refundUrl = '${hdpath}/alipay/aliDoRefund';
				} else if (refundtype == 3) {
					refundUrl = '${hdpath}/wxpay/wxDoRefund';
				} else if (refundtype == 4) {
					refundUrl = '${hdpath}/unionpay/doRefund';
				}
				$.bootstrapLoading.start({ loadingTips: "正在退款..." });
				$.ajax({
					url : refundUrl,
					data : {
						id : orderid,
						refundState : refundnum,
						wolfkey : refundtype
					},
					type : "POST",
					cache : false,
					success : function(data) {
						if (data.ok == 'error') {
							mui.alert('退款失败','提示',function(){
								window.location.reload();
							})
						} else if (data.ok == 'usererror') {
							mui.alert('用户金额不足','提示',function(){
								window.location.reload();
							})
						} else if (data.ok == 'moneyerror') {
							mui.alert('商户或合伙人金额不足','提示',function(){
								window.location.reload();
							})
						} else if (data.ok == 'ok') {
							mui.alert('退款成功','提示',function(){
								window.location.reload();
							})
						}
					},//返回数据填充
					complete: function () {
			            $.bootstrapLoading.end();
			        }
				});
			} else {
				return ;
			}
		})
	})
})
</script>
</html>