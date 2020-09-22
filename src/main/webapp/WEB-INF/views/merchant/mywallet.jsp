<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>我的钱包</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<!--App自定义的css-->
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/app.css" />
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="${hdpath }/js/jquery-2.1.0.js"></script>
<script type="text/javascript" src="${hdpath }/mui/js/mui.min.js"></script>
<style>
.title {
	margin: 20px 15px 10px;
	color: #6d6d72;
	font-size: 15px;
}
body{
	color: #666;
	font-size: 12px;
	background-color: #e3e3e6;
}
.mui-content{
	background-color: #e3e3e6;
}
.mui-table-view-cell  p{
	color: #666;
	font-size: 12px;
}

.mui-table-view-cell p .left {
	float: left;
}
.mui-table-view-cell p .right {
	display: block;
	overflow: hidden
}

.mui-table-view-cell.mui-collapse .mui-table-view .mui-table-view-cell {
	padding: 10px 30px;
}
.midLine {
height: 20px;
width:100%;
background-color: #e3e3e6;	
}
</style>
</head>
<body>
	<div class="mui-content">
		<ul class="mui-table-view">
			<li class="mui-table-view-cell"><a href="${hdpath }/merchant/merEarningsDetail?merid=${user.id}" class="mui-navigate-right">
					<span style="font-size: 14px;">账户余额（元）</span>
					<br><br>
					<font size="6px">${user.earnings == null ? "0.0" : user.earnings}</font>
					 </a>
			</li>
		</ul>
		<br/>
		<ul class="mui-table-view">
			<li class="mui-table-view-cell"><a href="${hdpath }/merchant/mybankcard" class="mui-navigate-right">
					我的银行卡 </a>
			</li>
		</ul>
		<br/>
		<%-- <ul class="mui-table-view">
			<li class="mui-table-view-cell"><a href="${hdpath }/merchant/withdrawrecord" class="mui-navigate-right">
					提现记录 </a>
			</li>
		</ul> <br/>--%>
		<ul id="list" class="mui-table-view mui-table-view-chevron">
			<li class="mui-table-view-cell mui-collapse">
				<a class="mui-navigate-right" href="#">
					提现
				</a>
				<ul class="mui-table-view mui-table-view-chevron">
					<li class="mui-table-view-cell"><a href="${hdpath }/merchant/weChatWithdraw" class="mui-navigate-right">
							提现到微信零钱 </a>
					</li>
				</ul>
				<ul class="mui-table-view mui-table-view-chevron">
					<li class="mui-table-view-cell"><a href="${hdpath }/merchant/withdraw?type=1" class="mui-navigate-right">
							提现到银行卡 </a>
					</li>
				</ul>
				<ul class="mui-table-view mui-table-view-chevron">
					<li class="mui-table-view-cell"><a href="${hdpath }/merchant/withdraw?type=2" class="mui-navigate-right">
							提现到对公账户 </a>
					</li>
				</ul>
				<ul class="mui-table-view mui-table-view-chevron">
					<li class="mui-table-view-cell"><a href="${hdpath }/merchant/withdrawrecord" class="mui-navigate-right">
							提现记录 </a>
					</li>
				</ul>
			</li>
			<li class="mui-table-view-cell mui-collapse">
				<a class="mui-navigate-right" href="#">
					提现说明
				</a>
				<ul class="mui-table-view mui-table-view-chevron">
					<li id="innerli" class="mui-table-view-cell " style="font-size: 14px">
						<strong>注意：</strong></strong><br>
						<p class="PDetail1">
							1.提现到零钱：需微信实名认证，实时到账，限额5000<br>
							2.提现到银行卡：需要审核，到账时间1天之内<br>
							3.提现到对公账户：需要开发票<br>
						</p>
						<p class="PDetail2">
							<strong>发票抬头如下：</strong><br>
							<p><span class="left">公司名称：</span><span class="right">郑州和腾信息技术有限公司</span></p>
							<p><span class="left">纳税人号：</span><span class="right">91410100MA44BL3G5L</span></p>
							<p><span class="left">地址电话：</span><span class="right">郑州高新技术开发区莲花街338号12号楼2层09号 &nbsp;0371-56788915</span></p>
							<p><span class="left">开户银行及账号：</span><span class="right">中国民生银行郑州航海路支行 &nbsp; 153715248</span></p>
						</p>
					</li>
				</ul>
			</li>
		</ul>
		<%-- <ul class="mui-table-view">
			<li class="mui-table-view-cell"><a href="${hdpath }/merchant/withdraw" class="mui-navigate-right">
					提现 </a>
			</li>
		</ul> --%>
		<br />
		<ul class="mui-table-view ">
			<li class="mui-table-view-cell mui-collapse">
					<a class="mui-navigate-right" href="javascript:;">
						设置
					</a>
					<ul class="mui-table-view " data-id="${messdata.id}"  data-merid="${user.id}" id="infoUl">
						<li class="mui-table-view-cell">
						<strong>消息通知设置</strong>
						</li>
						<li class="mui-table-view-cell">
								<span>提现通知   </span>
								<div class="mui-switch mui-switch-mini <c:if test="${messdata.withmess == 1}" > mui-active </c:if>" data-name="with" c="${messdata.withmess}">
								  <div class="mui-switch-handle"></div>
								</div>
						</li>
						<li class="mui-table-view-cell">
								<span>订单通知   </span>
								<div class="mui-switch mui-switch-mini <c:if test="${messdata.ordermess == 1}" > mui-active </c:if>" data-name="order">
								  <div class="mui-switch-handle"></div>
								</div>
						</li>
						<li class="mui-table-view-cell">
								<span>设备上下线通知 </span>
								<div class="mui-switch mui-switch-mini <c:if test="${messdata.equipmess == 1}" > mui-active </c:if>" data-name="equip">
								  <div class="mui-switch-handle"></div>
								</div>
						</li>
						<div class="midLine"></div>
						<li class="mui-table-view-cell">
						<strong>退款设置</strong>
						</li>
						<li class="mui-table-view-cell" >
								<span>是否开通脉冲模块自动退费 </span>
								<div class="mui-switch mui-switch-mini <c:if test="${messdata.incoinrefund != 2}" > mui-active </c:if>" data-name="incoinrefund">
								  <div class="mui-switch-handle"></div>
								</div>
						</li>
					</ul>
				</li>
		</ul>
	</div>
<script type="text/javascript">
	$(function(){
	    pushHistory();
	    window.addEventListener("popstate", function(e) {
	    	window.location.href = "${hdpath}/merchant/personcenter";
		}, false);
	    function pushHistory() {
	        var state = {
	            title: "title",
	            url: "#"
	        };
	        window.history.pushState(state, "title", "#");
	    }
	    
	    //设置消息通知
	   
	    $('.mui-switch').each(function(i,item){
	    		item.addEventListener("toggle",function(event){
	    		 handleGetToggle()
	    		})
	    	
	    	})
	    	
	    	function handleGetToggle(){ //获取切换开关
	    		var data= {}
	    		$('.mui-switch').each(function(i,item){
	    			var status= $(item).hasClass('mui-active') ? 1 : 2 // 1是开启， 2是关闭
	    			var key=  $(item).attr('data-name').trim()
	    			data[key]= status
	    		})
	    		var merid= $('#infoUl').attr('data-merid').trim()
	    		data.merid= merid
	    		console.log(data)
	    		$.ajax({
	    			url: '/allowAuthority/messSwitch',
	    			data: data,
	    			type: 'POST',
	    			success: function(e){
	    				console.log(e)
	    				if(e.code == 200){
	    					mui.toast('设置成功！',{duration: 1500})
	    				}
	    			},
	    			error: function(){}
	    		})
	    	}
	    })
</script>
</body>
</html>