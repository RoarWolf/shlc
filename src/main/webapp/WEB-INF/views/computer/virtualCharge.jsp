<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>虚拟充值</title>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link rel="stylesheet" href="${hdpath}/css/admin.css">
<script type="text/javascript" src="${hdpath }/js/jquery.js"></script>
<style>
		.admin {
			background: #fff;
    		position: fixed;
		    border-left: solid 1px #b5cfd9;
		    right: 0;
		    bottom: 0;
		    top: 110px;
		    left: 180px;
		    padding: 15px;
		    padding-right: 0px;
		    padding-bottom: 0px;
		    overflow: auto;
		    border-top: 1px solid #b5cfd9;
		    padding-right: 15px;
		    overflow: auto;
		}
		.vir {
			width: 35%;
			min-width: 400px;
			padding: 20px;
			box-shadow: 10px 10px 20px #ddd;
			border: 1px solid #eee; 
			background-color: #f5f7fa;
			position: absolute;
			left: 50%;
			top: 20%;
			transform: translate(-50%,0);
			-webkit-transform: translate(-50%,0);
			-moz-transform: translate(-50%,0);
			-o-transform: translate(-50%,0);
			-ms-transform: translate(-50%,0);
		}
		.vir h1 {
			text-align: center;
			color: #595959;
			font-size: 18px;
			padding: 10px 0 20px;
			border-bottom: 1px solid #ccc;
		}
		.inpDiv {
			padding: 20px 0;
			text-align: center;
		}
		.inpDiv label {
			display: block;
			text-align: center;
			padding-bottom: 15px;
			color: #595959;
		}
		.inpDiv input{
			width: 70%;
			line-height: 38px;
			margin-bottom: 30px;
			border: none;
			background-color: rgba(0,0,0,.1);
			border-radius: 6px;
			outline-style: none;
			padding: 0 20px;
			color: #595959;
		}
		.inpDiv .bottom .btnClose{
			float: left;
			margin-left: 32%;
		}
		.inpDiv .bottom .btcSure{
			float: right;
			margin-right: 32%;
		}
	</style>
</head>
<div class="header bg-main">
	<%@ include file="/WEB-INF/views/navigation.jsp"%>
</div>
<div class="leftnav" id="lefeMenu">
	<%@ include file="/WEB-INF/views/menu.jsp"%>
	<script type="text/javascript" src="/js/jquery.qrcode.js"></script> 
<script type="text/javascript" src="/js/qrcode.js"></script> 
</div>
<div>
	<ul class="bread">
	  <li><a href="javascript:void(0)" target="right" class="icon-home">设备列表</a></li>
	</ul>
</div>
<div class="admin">
<input type="hidden" class="receivedata" data-type="${type}" data-objid="${order.id}" data-uid="${user.id}">
	<c:if test="${type==1}">
		<h3>充值帐号：<fmt:formatNumber value="${order.id}" pattern="00000000"/></h3>
		<h5>会员信息</h5>
		<div class="showInfo">
			<div>
				<span>金额</span>
				<span class="unActivation">
					<span><a href="${hdpath }/general/touristmoneychange?uid=${order.id}">
					<fmt:formatNumber value="${order.balance}" pattern="0.00"/></a> 元</span>
				</span>
			</div>
			<div>
				<span>昵称</span>
				<span>
					<c:if test="${order.username == null || order.username == ''}">---</c:if>
					<c:if test="${order.username != null && order.username != ''}">${order.username}</c:if>
				</span>
			</div>
		</div>
	</c:if>
	<c:if test="${type==2}">	
		<h3>充值在线卡号：${order.cardID}</h3>
		<h5>在线卡信息</h5>
		<div class="showInfo">
			<div>
				<span>金额</span>
				<span class="unActivation">	
					<span><a href="${hdpath }/general/onlinecardrecord?cardID=${order.cardID}&uid=${order.uid}">
					<c:if test="${order.relevawalt==2}"><fmt:formatNumber value="${order.money}" pattern="0.00"/></c:if>
					<c:if test="${order.relevawalt==1}"><fmt:formatNumber value="${useronline.balance}" pattern="0.00"/></c:if>
					</a> 元</span>
				</span>
			</div>
			<div>
				<span>昵称</span>
				<span>
					<c:if test="${useronline.username == null || useronline.username == ''}">---</c:if>
					<c:if test="${useronline.username != null && useronline.username != ''}">${useronline.username}</c:if>
				</span>
			</div>
		</div>	
	</c:if>
	<div class="vir">
		<h1>虚拟充值</h1>
		<div class="inpDiv">
			<label for="userId">请输入用户id</label>
			<input type="text" id="userId" name="userId">
			<label for="virMoney">请输入充值金额</label>
			<input type="text" id="virMoney" name="virMoney">
			<div class="bottom">
				<button type="button" class="btn btn-info btnClose">取消</button>
				<button type="button" class="btn btn-info btcSure">确认</button>
			</div>
		</div>
	</div>
</div>
</body>
<script type="text/javascript">
$(".btcSure").click(function (){
	var type= $('.receivedata').attr('data-type');
	var uid= $('#userId').val().trim();
	var money = $("#virMoney").val().trim();
	$.ajax({
	    type : "POST",
		url : "${hdpath}/pcadminiStrator/virtualPay",
		data : {
			money : money,
			type : ${type},
			id : uid,
		},
	    success:function(e){
	    	console.log(1)
	    	if (e.code==200) {
	    		alert('充值成功')
	    	} else {
	    		alert('充值失败')
	            return false
	    	}
	    },
	    error:function(){
	    	alert('充值失败')
	    }
	});
	$('.btnClose').click(function(){
		$('#userId').val('')
		$("#virMoney").val('')
	})
})
</script>
</html>