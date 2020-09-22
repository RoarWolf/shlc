<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>充值详情</title>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<style type="text/css">
img{height:70px; width:70px; margin-top: 3rem; margin-bottom: 0.8rem;}
p{text-align: center;}
.withdraw{font-size: 17px;}
.money{font-size: 32px; font-weight: 600;line-height: 65px;}
li{list-style: none; margin-top: 8px; margin-bottom: 12px}  
li span{margin: 12px 15px 8px 0; font-size: 14px;}  
</style>

</head>
<body>
	<div class="container">
	 <div>
	  	<p><img src="${hdpath}/images/jinbi.jpg"  alt="金币" /></p>
	  	<!-- 商户端 -->
	  	<c:if test="${rank != 1}">
	  		<p class="withdraw">${payMoneyinfo.paytype == 0 ? "充值" : payMoneyinfo.paytype == 3 ? "退费" : "--" }金额 ${payMoneyinfo.money}</p>
	  	</c:if>
	  	<!-- 用户端 -->
	  	<c:if test="${rank == 1}">
	  		<p class="withdraw">充值金额 ${payMoneyinfo.money}</p>
	  		<p class="withdraw">赠送金额 ${payMoneyinfo.sendmoney}</p>
	  	</c:if>
	    
	    <hr>
	  </div>
	  <div>
	  	<ul style="list-sytle:none;">
	  		<li><span>订单编号：&nbsp;</span><span>${payMoneyinfo.ordernum}</span></li>
	  		<li><span>充值方式：</span>
	  		<span>
				<c:choose>
					<c:when test="${payMoneyinfo.paytype == 0}"><font>钱包充值</font></c:when>
					<c:when test="${payMoneyinfo.paytype == 1}"><font>卡充值</font></c:when>
					<c:when test="${payMoneyinfo.paytype == 2}"><font>卡退费</font></c:when>
					<c:when test="${payMoneyinfo.paytype == 3}"><font>充电退费</font></c:when>
				</c:choose>
 			</span></li>
 			<c:if test="${rank != 2 }">
		  		<li><span>充值余额：</span><span>￥${payMoneyinfo.topupbalance}</span></li>
		  		<li><span>赠送余额：</span><span>￥${payMoneyinfo.givebalance}</span></li>
 			</c:if>
	  		<li><span>${payMoneyinfo.paytype == 0 ? "充值" : payMoneyinfo.paytype == 3 ? "退费" : "--" }时间：&nbsp;</span><span><fmt:formatDate value="${payMoneyinfo.paytime}" pattern="yyyy-MM-dd HH:mm:ss" /></span></li>
	  		<%-- <li><span>备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</span><span>${payMoneyinfo.remark}</span></li> --%>
	  	</ul>
	  </div>
	</div>
</body>
</html>