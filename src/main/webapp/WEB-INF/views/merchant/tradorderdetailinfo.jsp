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
			    <c:choose>
				<c:when test="${paysource == 1}">
					<tr align="center">
						<th>付款金额（元）</th>
						<td>
						<c:if test="${order.number==0}"><span style="color: #37d825;">+&nbsp;<fmt:formatNumber value="${order.expenditure}" pattern="0.00" /></span></c:if> 
						<c:if test="${order.number==1 || order.number==2}"><span style="color: #d20e0e;">-&nbsp;<fmt:formatNumber value="${order.expenditure}" pattern="0.00" /></span></c:if>
						</td>
					</tr>
					<c:if test="${source==1}">
					<tr align="center">
						<th>商户收入（元）</th>
						<td>
						<c:if test="${partrecord.status==1}"><span style="color: #37d825;">+&nbsp;<fmt:formatNumber value="${partrecord.mermoney}" pattern="0.00" /></span></c:if> 
						<c:if test="${partrecord.status==2}"><span style="color: #d20e0e;">-&nbsp;<fmt:formatNumber value="${partrecord.mermoney}" pattern="0.00" /></span></c:if>
						</td>
					</tr>
					<c:if test="${partrecord.manid!=0}">
					<tr align="center">
						<th>合伙人（元）</th>
						<td>
						<c:if test="${partrecord.status==1}"><span style="color: #37d825;">+&nbsp;<fmt:formatNumber value="${partrecord.manmoney}" pattern="0.00" /></span></c:if> 
						<c:if test="${partrecord.status==2}"><span style="color: #d20e0e;">-&nbsp;<fmt:formatNumber value="${partrecord.manmoney}" pattern="0.00" /></span></c:if>
						</td>
					</tr>
					</c:if>
					</c:if>
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
						<td>${order.paytype == 1 ? "钱包" : order.paytype == 2 ? "微信" : order.paytype == 3 ? "支付宝" : order.paytype == 4 ? "包月" : "— —" }</td>
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
					<c:choose>
						<th>退费</th>
						<c:when test="${order.number == 0}">
						  <c:if test="${order.paytype == 1}"><button class="btn btn-success" id="refund" value="5">退款</button></c:if>
						  <c:if test="${order.paytype == 2}"><button class="btn btn-success" id="refund" value="1">退款</button></c:if>
						  <c:if test="${order.paytype == 3}"><button class="btn btn-success" id="refund" value="2">退款</button></c:if>
						</c:when>
					</c:choose>
				</c:when>
				<c:when test="${paysource == 2 }">
					<tr align="center">
						<th>付款金额（元）</th>
						<td>
						<c:if test="${order.status==1}"><span style="color: #37d825;">+&nbsp;<fmt:formatNumber value="${order.money}" pattern="0.00" /></span></c:if> 
						<c:if test="${order.status==2}"><span style="color: #d20e0e;">-&nbsp;<fmt:formatNumber value="${order.money}" pattern="0.00" /></span></c:if>
						</td>
					</tr>
					<tr align="center">
						<th>投币个数</th>
						<td>${order.coinNum}</td>
					</tr>
					<c:if test="${source==1}">
					<tr align="center">
						<th>商户收入（元）</th>
						<td>
						<c:if test="${partrecord.status==1}"><span style="color: #37d825;">+&nbsp;<fmt:formatNumber value="${partrecord.mermoney}" pattern="0.00" /></span></c:if> 
						<c:if test="${partrecord.status==2}"><span style="color: #d20e0e;">-&nbsp;<fmt:formatNumber value="${partrecord.mermoney}" pattern="0.00" /></span></c:if>
						</td>
					</tr>
					<c:if test="${partrecord.manid!=0}">
					<tr align="center">
						<th>合伙人（元）</th>
						<td>
						<c:if test="${partrecord.status==1}"><span style="color: #37d825;">+&nbsp;<fmt:formatNumber value="${partrecord.manmoney}" pattern="0.00" /></span></c:if> 
						<c:if test="${partrecord.status==2}"><span style="color: #d20e0e;">-&nbsp;<fmt:formatNumber value="${partrecord.manmoney}" pattern="0.00" /></span></c:if>
						</td>
					</tr>
					</c:if>
					</c:if>
					<tr align="center">
						<th>支付方式</th>
						<td>
							<c:choose>
								<c:when test="${order.handletype == 1 || order.handletype == 4}">微信</c:when>
								<c:when test="${order.handletype == 2 || order.handletype == 5}">支付宝</c:when>
								<c:when test="${order.handletype == 3}">投币器</c:when>
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
								<c:when test="${order.handletype == 4 || order.handletype == 5 || order.handletype == 7 || order.handletype == 9 || order.handletype == 11}"><font style="color: #ec2a2a;">已退款</font></c:when>
							<%-- 	<c:when test="${order.handletype == 4}"><font style="color: #ec2a2a;">微信已退费</font></c:when>
								<c:when test="${order.handletype == 5}"><font style="color: #ec2a2a;">支付宝已退费</font></c:when>
								<c:when test="${order.handletype == 7}"><font style="color: #ec2a2a;">已退款到钱包</font></c:when>
								<c:when test="${order.handletype == 9}"><font style="color: #ec2a2a;">微信小程序已退费</font></c:when>
								<c:when test="${order.handletype == 11}"><font style="color: #ec2a2a;">支付宝小程序已退费</font></c:when> --%>
							</c:choose>
						</td>
					</tr>
					<c:if test="${order.handletype == 1 || order.handletype == 2 || order.handletype == 6 || order.handletype == 8 || order.handletype == 10}">
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
				</c:when>
				<c:when test="${paysource == 3 }">
					<tr align="center">
						<th>卡号</th>
						<td>${order.cardID}</td>
					</tr>
					<tr align="center">
						<th>卡余额</th>
						<td><fmt:formatNumber value="${order.balance }" pattern="0.00" /></td>
					</tr>
					<c:if test="${source==1}">
					<tr align="center">
						<th>付款金额（元）</th>
						<td>
						<c:if test="${order.paytype==1 || order.paytype==2 || order.paytype==5}">
						<span style="color: #37d825;">+&nbsp;<fmt:formatNumber value="${order.chargemoney}" pattern="0.00" /></span>
						</c:if> 
						<c:if test="${order.paytype==3 || order.paytype==4 || order.paytype==6}">
						<span style="color: #d20e0e;">-&nbsp;<fmt:formatNumber value="${order.chargemoney}" pattern="0.00" /></span>
						</c:if>
						</td>
					</tr>
					<tr align="center">
						<th>充卡金额（元）</th>
						<td><fmt:formatNumber value="${order.accountmoney }" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>商户收入（元）</th>
						<td>
						<c:if test="${partrecord.status==1}"><span style="color: #37d825;">+&nbsp;<fmt:formatNumber value="${partrecord.mermoney}" pattern="0.00" /></span></c:if> 
						<c:if test="${partrecord.status==2}"><span style="color: #d20e0e;">-&nbsp;<fmt:formatNumber value="${partrecord.mermoney}" pattern="0.00" /></span></c:if>
						</td>
					</tr>
					<c:if test="${partrecord.manid!=0}">
					<tr align="center">
						<th>合伙人（元）</th>
						<td>
						<c:if test="${partrecord.status==1}"><span style="color: #37d825;">+&nbsp;<fmt:formatNumber value="${partrecord.manmoney}" pattern="0.00" /></span></c:if> 
						<c:if test="${partrecord.status==2}"><span style="color: #d20e0e;">-&nbsp;<fmt:formatNumber value="${partrecord.manmoney}" pattern="0.00" /></span></c:if>
						</td>
					</tr>
					</c:if>
					</c:if>
					<tr align="center">
						<th>付款方式</th>
						<td>
						  <c:choose>
							<c:when test="${order.paytype == 1 || order.paytype == 3}"><font>微信</font></c:when>
							<c:when test="${order.paytype == 2 || order.paytype == 4}"><font>支付宝</font></c:when>
						  	<c:otherwise>查询</c:otherwise>
						  </c:choose>
						</td>
					</tr>
					<tr align="center">
						<th>充值时间</th>
						<td><fmt:formatDate value="${order.beginTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr align="center">
						<th>订单状态</th>
						<td>
						 <c:choose>
							<c:when test="${order.paytype == 1 || order.paytype == 2  || order.paytype == 5}"><font>正常</font></c:when>
							<c:when test="${order.paytype == 3 || order.paytype == 4 || order.paytype == 6 }"><font>退款</font></c:when>
						  	<c:otherwise>查询</c:otherwise>
						  </c:choose>
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
				<c:when test="${paysource == 4 }">
					<tr align="center">
						<th>付款金额</th>
						<td>
							<fmt:formatNumber value="${order.money}" pattern="0.00"/>
						</td>
					</tr>
					<tr align="center">
						<th>余额（元）</th>
						<td><fmt:formatNumber value="${order.balance}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>支付方式</th>
						<td>
						  <c:choose>
							<c:when test="${order.paytype == 0 || order.paytype == 2}">微信</c:when>
							<c:when test="${order.paytype == 1 || order.paytype == 6}">虚拟操作</c:when>
							<c:when test="${order.paytype == 3 || order.paytype == 4 || order.paytype == 5}">退款到钱包</c:when>
						    <c:when test="${order.paytype == 7 || order.paytype == 8}">支付宝</c:when>
						  </c:choose>
						</td>
					</tr>
					<tr align="center">
						<th>交易时间</th>
						<td><fmt:formatDate value="${order.paytime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr align="center">
						<th>订单状态</th>
						<td>
							<c:choose>
								<c:when test="${order.paytype == 0 || order.paytype == 1 || order.paytype == 7}">正常</c:when>
								<c:when test="${order.paytype == 2 || order.paytype == 6 || order.paytype == 8}"><font style="color: #ec2a2a;">钱包退款</font></c:when>
								<c:when test="${order.paytype == 3 || order.paytype == 4 || order.paytype == 5}"><font style="color: #ec2a2a;">退费到钱包</font></c:when>
							</c:choose>
						</td>
					</tr>
					<c:if test="${order.paytype == 0 || order.paytype == 1 || order.paytype == 7}">
					<tr align="center">
						<td>退费</td>
						<td><button class="btn btn-success" id="refund" value="1">退款</button></td>
					</tr>
					</c:if>
				</c:when>
				<c:when test="${paysource == 5 }"> 
					<tr align="center">
						<th>卡号</th>
						<td>${order.cardID}</td>
					</tr>
					<tr align="center">
						<th>充值金额（元）</th>
						<td><fmt:formatNumber value="${order.money}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>到账金额（元）</th>
						<td><fmt:formatNumber value="${order.accountmoney}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>余额（元）</th>
						<td><fmt:formatNumber value="${order.balance}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>操作类型</th>
						<td>${order.type == 1 ? "消费" : order.type == 2 ? "余额回收": order.type == 3 ? "微信充值" : order.type == 4 ? "卡操作" : order.type == 5 ? "微信退款" : order.type == 6 ? "支付宝充值" : order.type == 7 ? "支付宝退款" : order.type == 8 ? "虚拟充值" : order.type == 9 ? "虚拟充值退款" : "其它"}</td>
					</tr>
					<tr align="center">
						<th>订单状态</th>
						<c:choose>
						   <c:when test="${order.flag==1}">
							<td><font>${order.status==1 ? "正常" : order.status==2 ? "激活" : order.status==3 ? "绑定" : 
							order.type==4 ? "删除" : order.status==5 ? "挂失" : order.status==6 ? "解挂" : order.status==7 ? "卡号修改" : "修改备注"}</font></td>
						   </c:when>
						   <c:when test="${order.flag==2}"><td style="color: #ef2222;">已退款</td></c:when>
						</c:choose>
					</tr>
					<tr align="center">
						<th>创建时间</th>
						<td><fmt:formatDate value="${order.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<c:if test="${order.flag == 1}">
					  <tr align="center">
						<td>退费</td>
						<td>
							<c:if test="${order.type == 3}"><button class="btn btn-success" id="refund" value="1">退款</button></c:if>
							<c:if test="${order.type == 6}"><button class="btn btn-success" id="refund" value="2">退款</button></c:if>
					  	</td>
					  </tr>
					</c:if>
				</c:when>
				<c:when test="${paysource == 6 }"> 
					<tr align="center">
						<td>付款金额</td>
						<td>${order.money}</td>
					</tr>
					<tr align="center">
						<td>包月时常</td>
						<td>${order.changemonth}</td>
					</tr>
					<tr align="center">
						<th>创建时间</th>
						<td><fmt:formatDate value="${order.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr align="center">
						<th>订单状态</th>
						<td>
							<c:if test="${order.ifrefund == 1 }">
								<font color="#ef2222">已退款</font>
							</c:if>
							<c:if test="${order.ifrefund == 0 }">
								<font color="#ef2222">${order.status == 2 ? '已退款' : '正常' }</font> 
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