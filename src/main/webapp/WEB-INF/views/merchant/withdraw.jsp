<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="hdpath" value="${pageContext.request.contextPath }" />
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<title>
<c:if test="${withdrawWay == 1}">提现到微信零钱</c:if>
<c:if test="${withdrawWay != 1}">提现到银行卡</c:if>
</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script src="${hdpath }/js/my.js"></script>
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<link rel="stylesheet" href="${hdpath}/css/base.css"/>
<script type="text/javascript" src="${hdpath }/mui/js/mui.min.js"></script>
<style type="text/css">
		.app {
			padding: 15px 0;
		}
		h1 {
			font-size: 18px;
			text-align: center;
			margin-bottom: 20px;
			color: #666;
			font-weight: bold;
		}
		.cashType {
			background-color: #f5f7fa;
			padding: 15px;
		}
		.cashType .left {
			width: 20%;
			height: 50px;
			font-size: 14px;
			float: left;
		}
		.cashType .right {
			overflow: hidden;
			font-size: 14px;
			padding-left: 15px;
		}
		.cashType .right p {
			line-height: 2em;
		}
		.cashType .right .right_arr{
			float: right;
			color: #999;
			font-size: 22px;
		}
		.cashInfo {
			background-color: #fff;
			padding: 15px;
		}
		.cashInfo h3 {
			font-size: 14px;
			color: #777;
			margin-bottom: 5px;
			font-weight: normal;
		}
		.cashInfo form {
			position: relative;

		}
		.cashInfo form input {
			height: 50px;	
			font-size: 28px;
			color: #333;
			margin: 0;
			padding-left: 35px;
			outline: none;
			border: none;
			border-bottom: 1px solid #f7f7f7;
			margin-bottom: 5px;
		}
		.iconMon {
			position: absolute;
			height: 100%;
			line-height: 50px;
			font-size: 40px;
			color: #333;
		}
		
		.btnDiv {
			text-align: center;
			margin-top: 35px;
		}
		.btnDiv button{
			width: 92%;
			height: 40px;
		}
		.otherType {
			font-size: 14px;
			color: #0984B5;
			text-align: center;
			text-decoration: underline;
			position: absolute;
			width: 100%;
			left: 0;
			bottom: 20%;
		}
		.allMoneySpan {
			margin-left: 5px;
			color: #0984B5;
			font-size: 14px;
		}
		strong {
			color: #999;
		}
		/* 其他的充值方式 */
	/* 	.otherTypeCon {
		width: 100%;
		height: 100%;
		background-color: rgba(0,0,0,.4);
		position: absolute;
		left: 0;
		bottom: 0;
		z-index: 9;
		display: none;
		
	}
	.otherTypeCon .selectRefund {
		position: absolute;
		left: 0;
		bottom: 0;
		width: 100%;
		height: 50%;
		animation: slide .4s;
		background-color: #fff;
		padding:15px;
	} */
		@keyframes slide{
			from {
				transform: translateY(100%);
			}
			to {
				transform: translateY(0%);
			}
		}	
		.bankList li{
			padding: 20px 15px;
			border: 1px solid #f7f7f7;
			background-color: #f5f7fa;
			border-radius: 4px;
			font-size: 14px;
			margin-bottom: 10px;
			position:relative;
		}
		.bankList li .bankItemName {
			color: #666;
			margin-right: 8px;
		}
		.bankList li .bankItemNum {
			color: #999;
		}
		.bankList li .selectIcon {
			font-size: 36px;
			position: absolute;
			right: 20px;
			top: 50%;
			margin-top: -18px;
			color: #22B14C;
			display: none;
		}
		.bankList li.active .selectIcon {
			display: block;
		}
		.extraCharging {
			display: none;
		}
		.tipInfo {
			display: none;
		}
		.tipInfo p{
			color: #D14B50;
		}
		.tip {
			left: 0.64rem;
			right: 0.64rem;
			bottom: 0.4267rem;
			background: rgba(0,0,0,.06);
			padding: 0.4267rem 0.4rem;
			border-radius: 0.256rem;
			text-align: left;
		}
</style>
</head>
<body>
	<div class="app">
		<input type="hidden" id="bankcardlist" value="${userbankcardlist}">
		<input type="hidden" id="withdrawWay" value="${withdrawWay}">
		<input type="hidden" id="balance" value="${user.earnings}">
		<input type="hidden" id="realname" value="${user.realname}">
		<input type="hidden" id="company" value="${company}">
		<input type="hidden" id="feerate" value="${rate}">
	<script type="text/javascript">
		var withdrawWay= $('#withdrawWay').val().trim()
		var bankcardlist= $('#bankcardlist').val().trim()
		if(withdrawWay != '1'){
			if(bankcardlist == '' || bankcardlist.length <=0){
				mui.confirm('对不起，你未绑定银行卡，请先绑定银行卡！',function(opation){
					if(opation.index===1){
						window.location= '/merchant/addbankcard'
					}else {
						window.location= '/merchant/personcenter'
					}
				})
			}
		}
	</script>
		<c:if test="${withdrawWay != 1 }">
			<ul class="bankList">
				<c:forEach items="${userbankcardlist }" var="bankcard" varStatus="status">
					<li 
					<c:if test="${status.index == 0}">class="active" </c:if>
					data-id="${bankcard.id}">
						<span class="bankItemName">${bankcard.bankname}</span>
						<span class="bankItemNum">（${bankcard.bankcardnum}）</span>
						<%-- <span class="bankRealName">（${bankcard.realname}）</span> --%>
						<i class="mui-icon mui-icon-checkmarkempty selectIcon"></i>
					</li>
				</c:forEach>
			</ul>
		</c:if>
		<div class="cashType">
			<div class="left">到账方式</div>
			<div class="right">
			<c:if test="${userbankcardlist != null && fn:length(userbankcardlist) > 0}">
				<c:forEach items="${userbankcardlist}" var="bankcard" varStatus="status">
					<c:if test="${status.index == 0}">
						<div class="typeName"><span class="typeSpan">${bankcard.bankname}</span> <strong class="bankNumStr">（${bankcard.bankcardnum}）</strong> <span class="right_arr mui-icon mui-icon mui-icon-forward"></span></div>
						<p>第二个工作日到账</p>
					</c:if>
				</c:forEach>
			</c:if>
			<c:if test="${userbankcardlist == null || fn:length(userbankcardlist) <= 0}">
				<div class="typeName"><span class="typeSpan">微信零钱</span> <strong class="bankNumStr"></strong> <span class="right_arr mui-icon mui-icon mui-icon-forward"></span></div>
				<p>实时到账</p>
			</c:if>	
			</div>
		</div>
		<div class="cashInfo">
			<h3>提现金额</h3>
			<form action="">
				<span class="iconMon">¥</span>
				<input type="text" name="cashOut">
			</form>
			<div class="totalMonInfo"><p class="pLast">总金额：<span class="totalMonNum">
			<font id="moneyval">${user.earnings == null ? "0.0" : user.earnings }</font>
			</span>元  <span class="allMoneySpan">全部提现</span></p></div>
			<div class="extraCharging"><p>额外扣除￥<span></span>服务费（费率${rate/10}%）</p></div>
			<div class="tipInfo"><p>提现金额小于5元</p></div>
		</div>
		<div class="btnDiv">
			<button type="button" class="mui-btn mui-btn-success">
			<c:if test="${withdrawWay == 1}">实时到账，确认提现</c:if>
			<c:if test="${withdrawWay != 1}">第二个工作日到账，确认提现</c:if>
			</button>
		</div>
		<!-- <div class="otherType">
			其他提现方式
		</div> -->
	</div>
</body>
<script type="text/javascript">
$(function(){
	var clickFlag= true //是否能点击
	var feeRateMon= parseFloat($('#feerate').val())/1000
	var isCheckCashOut= false //是否核对isCheckCashOut
	var cashOutType= $("#withdrawWay").val() == 1 ? 1 : 2  //1是退费到微信零钱 ， 2是颓废到银行卡
	$('.bankList li').on('click',function(){
		$(this).siblings().removeClass('active')
		$(this).addClass('active')
		/*点击的时候获取选择项的银行名及卡号，替换cashInfo的信息*/
		$('.typeSpan').text($(this).find('.bankItemName').text().trim())
		$('.bankNumStr').text($(this).find('.bankItemNum').text().trim())
	})
	/*点击全部提现*/
	$('.allMoneySpan').on('click',function(){
		var totalMoney= parseFloat($('.totalMonNum').text())
		if(totalMoney < 5){
			$('.totalMonInfo').hide()
			$('.extraCharging').hide()
			$('.tipInfo p').text('提现金额不能小于5元')
			$('.tipInfo').show()
			isCheckCashOut= false
		}else if(totalMoney >= 10000 && cashOutType == 1){
			$('input[name="cashOut"]').val(10000)
			extraChargingFn((10000*feeRateMon).toFixed(2))
			isCheckCashOut= true
		}else{
			$('input[name="cashOut"]').val(totalMoney)
			extraChargingFn((totalMoney*feeRateMon).toFixed(2))
			isCheckCashOut= true
		}
	})

	/*input的抬起时间*/
	$('input[name="cashOut"]').on('keyup',function(e){
		e= e || window.event
		var totalMoney= parseFloat($('.totalMonNum').text())
			if($(this).val() == '' || $(this).val() == null){
				$('.tipInfo').hide()
				$('.extraCharging').hide()
				$('.totalMonInfo').show()
				isCheckCashOut= false
				return
			}
			var money= parseFloat($(this).val())
			if(typeof money == 'number' && parseFloat(money)< 5){
				$('.totalMonInfo').hide()
				$('.extraCharging').hide()
				$('.tipInfo p').text('提现金额不能小于5元')
				$('.tipInfo').show()
				isCheckCashOut= false
				return
			}
			if(typeof money == 'number' && parseFloat(money)> 10000 && cashOutType == 1){
				$('.totalMonInfo').hide()
				$('.extraCharging').hide()
				$('.tipInfo p').text('单日提现金额不能大于10000元')
				$('.tipInfo').show()
				isCheckCashOut= false
				return
			}
			if(typeof money == 'number' && parseFloat(money)> totalMoney){
				$('.totalMonInfo').hide()
				$('.extraCharging').hide()
				$('.tipInfo p').text('提现金额不能超过账户余额')
				$('.tipInfo').show()
				isCheckCashOut= false
				return
			}
			money= isNaN(money) ? 0 : money
			var extraChargingMoney= (money*feeRateMon).toFixed(2)
			extraChargingFn(extraChargingMoney)
			isCheckCashOut= true
	})

	/*更改并显示退款扣费钱数*/
	function extraChargingFn(extraChargingMoney){
		$('.extraCharging span').text(extraChargingMoney)
		$('.totalMonInfo').hide()
		$('.tipInfo').hide()
		$('.extraCharging').show()
	}
	//点击提现
	$('.btnDiv button').on('click',function(){
		if(!isCheckCashOut){  return } //如果没有检验提现金额没通过，阻止发送请求
		var withdrawmoneyval= $('.cashInfo form input').val().trim()
		if (cashOutType == 1) {
			url = "${hdpath}/merchant/weChatWithdrawaccess";
			dataval = {money : withdrawmoneyval};
		} else {
			var bankcardid = $(".bankList li.active").attr('data-id').trim();
			dataval = {
					bankcardid : bankcardid,
					money : withdrawmoneyval,
				};
			url = '${hdpath}/merchant/withdrawaccess';
		}
		var realname = $("#realname").val();
		if (realname != null && realname != "") {
			mui.confirm('确认提现吗 ？', '提现', ['取消', '确认'], function(e) {
				if (e.index == 1) {	
					if(!clickFlag){return} //阻止多次误点
						console.log(11111)
						toggleBtnClick(true)
						clickFlag= false
						$.bootstrapLoading.start({ loadingTips: "提现中..." });
						$.ajax({
							url : url,
							data : dataval,
							type : "POST",
							cache : false,
							success : function(data) {
								if (data == "0") {
									toggleBtnClick(false)
									mui.alert('非法用户或银行卡和提现金额有误', '提现', function() {
									});
								} else if (data == "1") {
									toggleBtnClick(true)
									mui.alert('操作成功', '提现', function() {
										location.replace("${hdpath}/merchant/personcenter");
									});
								} else if (data == "2") {
									toggleBtnClick(false)
									mui.alert('提现失败，请重新登陆后操作', '提现', function() {
									});
								} else if (data == "3") {
									toggleBtnClick(false)
									mui.alert('提现失败，需实名绑定银行卡后再提现，实名必须和此微信相同', '提现', function() {
									});
								} else if (data == "4") {
									toggleBtnClick(false)
									mui.alert('提现总额超出10000，请明日再次操作', '提现', function() {
									});
								} else if (data == "5") {
									toggleBtnClick(false)
									mui.alert('提现提交重复，请稍后再试', '提现', function() {
									});
								} else {
									toggleBtnClick(false)
									if(data.indexOf('真实姓名不一致') != -1 ){
										var confirmStr= '<div><h3 style="text-align: center;margin: 15px;font-size: 16px;">'+data+'</h3><div class="tip">你当前的真实姓名为“${user.realname}”，请检查当前真实姓名与微信实名认证的真实姓名是否一致</div><div class="tip">查看微信实名认证： 进入“微信”-“我的”-“支付”-点击右上角三个点“...”-点击“实名认证”</div><div class="tip">修改真实姓名的方法： 进入“商户平台”-“我的”-在顶部点击编辑“真实姓名”,填写并确定即可</div></div>'
										mui.alert(confirmStr,'提示','我知道了')	
									}else{
										mui.alert("系统错误！！！", '提现', function() {
										});
									}
								}
							},//返回数据填充
							complete: function () {
					            $.bootstrapLoading.end();
					            clickFlag= true
					        },
					        error : function() {
					        	mui.alert("系统错误！！！", '提现', function() {
								});
					        }
						});
					} //e.index
			}) //confirm
		}else {
			mui.prompt( "", "请输入真实姓名，与微信实名一致", "提现", ['取消', '确认'],  function(e){
				if (e.index == 1) {
					if(!clickFlag){return} //阻止多次误点
					clickFlag= false
					$.bootstrapLoading.start({ loadingTips: "提现中..." });
					$.ajax({
						url : url,
						data : {
							money : withdrawmoneyval,
							realname : e.value
						},
						type : "POST",
						cache : false,
						success : function(data) {
							if (data == "0") {
								toggleBtnClick(false)
								mui.alert('非法用户或银行卡和提现金额有误', '提现', function() {
								});
							} else if (data == "1") {
								toggleBtnClick(true)
								mui.alert('操作成功', '提现', function() {
									location.replace("${hdpath}/merchant/mywallet");
								});
							} else if (data == "2") {
								toggleBtnClick(false)
								mui.alert('提现失败，请重新登陆后操作', '提现', function() {
								});
							} else if (data == "3") {
								toggleBtnClick(false)
								mui.alert('提现失败，需实名绑定银行卡后再提现，实名必须和此微信相同', '提现', function() {
								});
							} else if (data == "4") {
								toggleBtnClick(false)
								mui.alert('提现总额超出10000，请明日再次操作', '提现', function() {
								});
							} else if (data == "5") {
								toggleBtnClick(false)
								mui.alert('提现提交重复，请稍后再试', '提现', function() {
								});
							} else {
								toggleBtnClick(false)
								if(data.indexOf('真实姓名不一致') != -1 ){
									var confirmStr= '<div><h3 style="text-align: center;margin: 15px;font-size: 16px;">'+data+'</h3><div class="tip">你当前的真实姓名为“${user.realname}”，请检查当前真实姓名与微信实名认证的真实姓名是否一致</div><div class="tip">查看微信实名认证： 进入“微信”-“我的”-“支付”-点击右上角三个点“...”-点击“实名认证”</div><div class="tip">修改真实姓名的方法： 进入“商户平台”-“我的”-在顶部点击编辑“真实姓名”,填写并确定即可</div></div>'
									mui.alert(confirmStr,'提示','我知道了')	
								}else{
									mui.alert("系统错误！！！", '提现', function() {
									});
								}
							}
						},//返回数据填充
						complete: function () {
				            $.bootstrapLoading.end();
				            clickFlag= true
				        },
				        error : function() {
				        	mui.alert("系统错误！！！", '提现', function() {
							});
				        }
					});
				} else {
					return ;
				}
			})
		}
	})
	function toggleBtnClick(flag){
		$('.cashInfo form input').prop('disabled',flag)
	}
})
</script>
</html>