<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<title>详情查询</title>
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
		<input type="hidden" id="orderid" value="${order.id}">
		<input type="hidden" id="refundnum" value="${refundnum}">
		<table border="1" class="table table-bordered">
			<tr align="center">
				<td>订单号</td>
				<td>${order.ordernum}</td>
			</tr>
			<!-- /** 支付来源1、充电模块-2、脉冲模块-3、离线充值机-4、用户充值钱包-5、用户在线卡充值*/ -->
			<c:choose>
				<c:when test="${source == 1}">
					<tr align="center">
						<td>付款金额（元）</td>
						<td><fmt:formatNumber value="${order.expenditure }" pattern="0.00" /></td>
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
						<td>${order.paytype == 1 ? "钱包" : order.paytype == 2 ? "微信" : order.paytype == 3 ? "支付宝" : order.paytype == 4 ? "微信小程序" : "— —" }</td>
					</tr>
					<tr align="center">
						<td>创建时间</td>
						<td><fmt:formatDate value="${order.begintime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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
								<font color="gray">部分退款</font>(<font color="red">${order.refundMoney }</font>)
							</c:if>
					</tr>
					<c:if test="${order.number == 0}">
					<tr align="center">
						<td>退费</td>
						<td>
							<c:if test="${order.paytype == 1}"><button class="btn btn-success" id="refund" value="5">退款</button></c:if>
							<c:if test="${order.paytype == 2}"><button class="btn btn-success" id="refund" value="1">退款</button></c:if>
							<c:if test="${order.paytype == 3}"><button class="btn btn-success" id="refund" value="2">退款</button></c:if>
						</td>
					</tr>
					</c:if>
				</c:when>
				<c:when test="${source == 2}">
					<tr align="center">
						<td>付款金额</td>
						<td><fmt:formatNumber value="${order.money }" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<td>付款方式</td>
						<td>${order.handletype == 1 ? "微信" : order.handletype == 2 ? "支付宝" : order.handletype == 3 ? "投币器"
						 : order.handletype == 4 ? "微信" : order.handletype == 5 ? "支付宝" : order.handletype == 8 ? "微信小程序" : 
						 order.handletype == 9 ? "微信小程序" : order.handletype == 10 ? "支付宝小程序" : order.handletype == 11 ? "支付宝小程序" : "钱包" }</td>
					</tr>
					<tr align="center">
						<td>创建时间</td>
						<td><fmt:formatDate value="${order.beginTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr align="center">
						<td>订单状态</td>
						<td>
							<c:if test="${order.handletype == 4 || order.handletype == 5 || order.handletype == 7 || order.handletype == 9 || order.handletype == 11}">
								<font color="gray">已退款</font>
							</c:if>
							<c:if test="${order.handletype == 1 || order.handletype == 2 || order.handletype == 3 || order.handletype == 6 || order.handletype == 8 || order.handletype == 10}">
								<font color="#5cb85c">正常</font>
							</c:if>
						</td>
					<c:if test="${order.handletype == 1 || order.handletype == 2 || order.handletype == 8 || order.handletype == 10}">
					<tr align="center">
						<td>退费</td>
						<td>
							<c:if test="${order.handletype == 1}"><button class="btn btn-success" id="refund" value="1">退款</button></c:if>
							<c:if test="${order.handletype == 2}"><button class="btn btn-success" id="refund" value="2">退款</button></c:if>
							<c:if test="${order.handletype == 8}"><button class="btn btn-success" id="refund" value="3">退款</button></c:if>
							<c:if test="${order.handletype == 10}"><button class="btn btn-success" id="refund" value="4">退款</button></c:if>
							<c:if test="${order.handletype == 6}"><button class="btn btn-success" id="refund" value="5">退款</button></c:if>
						</td>
					</tr>
					</c:if>
					</tr>
				</c:when>
				<c:when test="${source == 3}">
					<tr align="center">
						<td>卡号</td>
						<td>${order.cardID }</td>
					</tr>
					<tr align="center">
						<td>余额</td>
						<td>${order.balance }</td>
					</tr>
					<tr align="center">
						<td>付款金额（元）</td>
						<td><fmt:formatNumber value="${order.chargemoney }" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<td>充卡金额</td>
						<td><fmt:formatNumber value="${order.accountmoney }" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<td>付款方式</td>
						<td>${order.paytype == 1 ? "微信" : order.paytype == 2 ? "支付宝" : order.paytype == 3 ? "微信" : order.paytype == 4 ? "支付宝" : "—— ——"}</td>
					</tr>
					<tr align="center">
						<td>创建时间</td>
						<td><fmt:formatDate value="${order.beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					</tr>
					<tr align="center">
						<td>订单状态</td>
						<td>
						${order.paytype == 1 ? "正常" : order.paytype == 2 ? "正常" : order.paytype == 3 ? "全额退款" : order.paytype == 4 ? "全额退款" : "钱包消费"}
						</td>
					</tr>
					<c:if test="${order.paytype == 1 || order.paytype == 2}">
					<tr align="center">
						<td>退费</td>
						<td>
							<c:if test="${order.paytype == 1}"><button class="btn btn-success" id="refund" value="1">退款</button></c:if>
							<c:if test="${order.paytype == 2}"><button class="btn btn-success" id="refund" value="2">退款</button></c:if>
							<c:if test="${order.paytype == 5}"><button class="btn btn-success" id="refund" value="5">退款</button></c:if>
						</td>
					</tr>
					</c:if>
				</c:when>
				<c:when test="${source == 4}">
					<tr align="center">
						<td>金额</td>
						<td>${order.money}</td>
					</tr>
					<tr align="center">
						<td>余额</td>
						<td>${order.balance}</td>
					</tr>
					<tr align="center">
						<td>操作时间</td>
						<td><fmt:formatDate value="${order.paytime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					</tr>
					<tr align="center">
						<td>订单状态</td>
						<td>${order.paytype==0 ? "充值钱包" : order.paytype==2 ? "钱包退费" : "退款到钱包"}</td>
					</tr>
					<c:if test="${order.paytype == 0}">
					<tr align="center">
						<td>退费</td>
						<td><button class="btn btn-success" id="refund" value="1">退款</button></td>
					</tr>
					</c:if>
				</c:when>
				<c:when test="${source == 5}">
					<tr align="center">
						<td>卡号</td>
						<td>${order.cardID}</td>
					</tr>
					<tr align="center">
						<td>充值金额（元）</td>
						<td><fmt:formatNumber value="${order.money}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<td>到账金额</td>
						<td><fmt:formatNumber value="${order.accountmoney}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<td>余额</td>
						<td>${order.balance}</td>
					</tr>
					<tr align="center">
						<td>操作类型</td>
						<td>${order.type==1 ? "消费" : order.type==2 ? "余额回收" : order.type==3 ? "微信充值" : order.type==4 ? "卡操作" : order.type==5 ? "微信退款" : order.type==6 ? "支付宝充值" : order.type==7 ? "支付宝退款" : "— —"}</td>
					</tr>
					<tr align="center">
						<td>订单状态</td>
						<c:choose>
						   <c:when test="${order.flag==1}">
							<td><font>${order.status==1 ? "正常" : order.status==2 ? "激活" : order.status==3 ? "绑定" : order.type==4 ? "删除" : order.status==5 ? "挂失" : order.status==6 ? "解挂" : order.status==7 ? "卡号修改" : "修改备注"}</font></td>
						   </c:when>
						   <c:when test="${order.flag==2}"><td>已退款</td></c:when>
						</c:choose>
					</tr>
					<c:if test="${order.flag==1}">
					  <tr align="center">
						<td>退费</td>
						<td>
							<c:if test="${order.type == 3}"><button class="btn btn-success" id="refund" value="1">退款</button></c:if>
							<c:if test="${order.type == 6}"><button class="btn btn-success" id="refund" value="2">退款</button></c:if>
					  	</td>
					  </tr>
					</c:if>
				</c:when>
				<c:when test="${source == 6}">
					<tr align="center">
						<td>付款金额</td>
						<td>${order.money}</td>
					</tr>
					<tr align="center">
						<td>包月时常</td>
						<td>${order.changemonth}</td>
					</tr>
					<tr align="center">
						<td>创建时间</td>
						<td><fmt:formatDate value="${order.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					</tr>
					<tr align="center">
						<td>订单状态</td>
						<td>
							<c:if test="${order.ifrefund == 1 }">
								已退款
							</c:if>
							<c:if test="${order.ifrefund == 0 }">
								${order.status == 2 ? '已退款' : '正常' }
							</c:if>
						</td>
					</tr>
					<c:if test="${order.status != 2 && order.ifrefund == 0}">
					<tr align="center">
						<td>退费</td>
						<td><button class="btn btn-success" id="refund" value="1">退款</button></td>
					</tr>
					</c:if>
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
	var wechatUrl = '${hdpath}/wxpay/wxDoRefund';
	var alipayUrl = '${hdpath}/alipay/aliDoRefund';
	var walletUrl = "${hdpath}/wxpay/doWalletReturn";
	var refundLen= $('#refund').length
	if(!refundLen){
		return false
	}
	document.getElementById("refund").addEventListener('tap', function() {
		var refundtype = $(this).val();
		var btnArray = ['取消', '确认'];
		<!-- /** 支付来源1、充电模块-2、脉冲模块-3、离线充值机-4、用户充值钱包-5、用户在线卡充值*/ -->
		mui.confirm('确认退款吗 ？', '退款', btnArray, function(e) {
			if (e.index == 1) {
				var orderid = $("#orderid").val();
				var refundnum = $("#refundnum").val();
				var refundUrl;
				if(refundtype==1){
					refundUrl = wechatUrl;
				}else if(refundtype==2){
					refundUrl = alipayUrl;
				}else if(refundtype==3){
					refundUrl = wechatUrl;
				}else if(refundtype==4){
					refundUrl = alipayUrl;
				}else if(refundtype==5){
					refundUrl = walletUrl;
				}
				$.bootstrapLoading.start({ loadingTips: "正在退款..." });
				$.ajax({
					url : refundUrl,
					data : {
						id : orderid,
						refundState : refundnum,
						wolfkey : refundtype,
						utype: 0
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