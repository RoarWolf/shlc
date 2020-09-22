<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>卡记录查看</title>
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath}/mui/css/mui.min.css">
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/icons-extra.css" />
<script type="text/javascript" src="${hdpath}/mui/js/mui.min.js">
<script type="text/javascript" src="${hdpath}/js/jquery-2.1.0.js"></script>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<script src="${hdpath }/js/my.js"></script>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<style type="text/css">
html,body,.body{width:100%;height:100%}
.nametitle{ padding: 8px 0 8px 15px; background: aquamarine;}
.contentinfo{overflow-y:auto}
.span-right{ width: 50%;float: right;}
hr{margin:0}
</style>
</head>
<body>
<div class="body">
<div class="nametitle">
 <span>卡记录查看</span>
</div><hr>
<div class="contentinfo">
	<c:forEach items="${cardOrder}" var="order">
		<ul class="mui-table-view">
			<li class="mui-table-view-cell">
					<span>卡&nbsp;&nbsp;&nbsp;&nbsp;号：${order.cardID}</span>
					<c:if test="${order.ordernum!=null}">
						<span class="span-right">
						<c:choose>
							<c:when test="${order.type == 1}"><font style="color: red;">消费</font></c:when>
							<c:when test="${order.type == 2}"><font style="color: green;">余额回收</font></c:when>
							<c:when test="${order.type == 3}"><font style="color: green;">充值</font></c:when>
							<c:when test="${order.type == 4}">
								<c:if test="${order.status == 2}">激活</c:if>
								<c:if test="${order.status == 3}">绑定</c:if>
								<c:if test="${order.status == 4}">删除</c:if>
								<c:if test="${order.status == 5}">挂失</c:if>
								<c:if test="${order.status == 6}">解挂</c:if>
								<c:if test="${order.status == 7}">卡号修改</c:if>
								<c:if test="${order.status == 8}">修改备注</c:if>
							</c:when>
							<c:when test="${order.type == 5}"><font style="color: red;">退费</font></c:when>
						</c:choose>
 					</span><br>
						<span>消费额：${order.money}</span>
						<span style="margin-left: 25%;">余额：${order.balance}</span>
						<c:if test="${type==3}">
							<button onclick="refundMoney('${order.id}','${order.cardID}','${order.status}')" class="mui-badge mui-badge-success" style="float: right; padding:4px 12px;">退费</button>
						</c:if>
					</c:if>
					<br><span>时&nbsp;&nbsp;&nbsp;&nbsp;间：<fmt:formatDate value="${order.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></span>
				<c:if test="${order.code != null}">
					<span class="span-right">设备号：${order.code}</span>
				</c:if>
			</li>
		</ul>
	</c:forEach>
	</div>
   </div>
</body>

<script type="text/javascript">
function refundMoney(orderId,cardID,status){
	$.ajax({
		url : "${hdpath}/general/selonline",
		data : { cardID : cardID},
		type : "POST",
		success : function(e) {
			if(e.status==0){
				mui.alert("未激活卡不能退款")
				return false;
			}else{
				refund(orderId,cardID,status)
			}
		}
	});
}
function refund(orderId,cardID,status){
	var statu = confirm("是否确认退款？");
	if(!statu){
	  return false;
	}
	$.bootstrapLoading.start({ loadingTips: "正在退款，请稍后..." });
	$.ajax({
		url : "${hdpath}/wxpay/doRefund",
		data : {
			id : orderId,
			refundState : 5,
			pwd : status,
			utype : 2,
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
			}else{
				mui.alert('退款异常失败','提示',function(){
					window.location.reload();
				})
			}
		},//返回数据填充
		complete: function () {
            $.bootstrapLoading.end();
        }
	});
}

</script>

</html>