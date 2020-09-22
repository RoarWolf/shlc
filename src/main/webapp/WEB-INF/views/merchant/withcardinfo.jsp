<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>提现详情</title>
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
<body style="background-color:#f2f9fd;">
	<div class="container">
	  <div>
	  	<p><img src="${hdpath}/images/jinbi.jpg"  alt="金币" /></p>
	    <p class="withdraw">提现到-${withdraw.bankname} (${withdraw.bankcardnum})</p>
	    <p class="money">- <fmt:formatNumber value="${withdraw.money}" pattern="0.00" /></p>
	    <hr>
	  </div>
	  <div>
	  	<ul style="list-sytle:none;">
	  		<li><span>当前状态</span><span>${withdraw.status == 0 ? "<font color='gray'>提现待处理</font>" : withdraw.status==1?"<font color='#00CC99'>提现已到账</font>":withdraw.status==3?"<font color='#00CC99'>提现已到账</font>":"<font color='#ea1111'>提现被拒绝</font>"}</span></li>
	  		<li><span>提现金额</span><span>￥${withdraw.money-withdraw.servicecharge}</span></li>
	  		<li><span>手续费&nbsp;&nbsp;&nbsp;</span><span>￥${withdraw.servicecharge}</span></li>
	  		<li><span>申请时间</span><span><fmt:formatDate value="${withdraw.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></span></li>
	  		<c:if test="${withdraw.bankname != '微信零钱'}">
	  			<li><span>到账时间</span><span><fmt:formatDate value="${withdraw.accountTime}" pattern="yyyy-MM-dd HH:mm:ss" /></span></li>
	  		</c:if>
	  		<li><span>提现银行</span><span>${withdraw.bankname}</span></li>
	  	<%-- 	<li><span>提现账号</span><span>${withdraw.bankcardnum}</span></li> --%>
	  		<li><span>提现单号</span><span>${withdraw.withdrawnum}</span></li>
	  	</ul>
	  </div>
<%-- 		<c:if test="${withdraw.status}==1">
			<button type="submit" class="btn btn-default">提交</button>
		</c:if> --%>
	</div>
</body>
</html>