<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>选择模块类型</title>
<%@ include file="/WEB-INF/views/public/commons.jspf" %>
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<link href="${hdpath}/css/ui-choose.css" rel="stylesheet" />
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script src="${hdpath}/js/jquery.js"></script>
<script src="${hdpath}/js/ui-choose.js"></script>
<script src="${hdpath }/mui/js/mui.min.js"></script>
<script src="${hdpath }/js/my.js"></script>
<style type="text/css">
a:hover {
	text-decoration: none;
}
</style>
</head>
<body>
	<header class="mui-bar mui-bar-nav">
		<a class="mui-action-back mui-icon mui-icon-back mui-pull-left"></a>
		<h1 class="mui-title">当前设备号：${code }</h1>
	</header>
	<div class="mui-content">
		<h5 class="mui-content-padded">请选择支付方式</h5>
		<ul class="mui-table-view mui-table-view-radio">
			<li <c:if test="${balance >= money }"> class="mui-table-view-cell mui-selected" </c:if> 
				<c:if test="${balance < money }"> class="mui-table-view-cell" </c:if> >
				<a class="mui-navigate-right" data-myselectval="1" id="pay1">
					钱包支付&nbsp;&nbsp;(<font color="red">${balance}</font>元)
				</a>
			</li>
			<li <c:if test="${balance < money }"> class="mui-table-view-cell mui-selected" </c:if> 
				<c:if test="${balance >= money }"> class="mui-table-view-cell" </c:if> >
				<a class="mui-navigate-right" data-myselectval="2" id="pay2">
					微信支付
				</a>
			</li>
		</ul><br>
		<input type="hidden" id="payWay" value="">
		<input type="hidden" id="balance" value="${balance}">
		<input type="hidden" id="money" value="${money}">
		<input type="hidden" id="ifwallet" value="${ifwallet}">
		<form id="payform" action="${hdpath }/charge/walletCharge" method="post">
			<input type="hidden" id="code" name="code" value="${code}">
			<input type="hidden" id="openid" name="openid" value="${openid}">
			<input type="hidden" id="portchoose" name="portchoose" value="${portchoose}">
			<input type="hidden" id="chargeparam" name="chargeparam" value="${chargeparam }">
		</form>
			<button class="mui-btn mui-btn-success mui-btn-block" id="submitbtn">确认选择</button>
	</div>
</body>
<script type="text/javascript">
$(function() {
	var balanceVal = $("#balance").val();
	var moneyVal = $("#money").val();
	if (parseFloat(moneyVal) > parseFloat(balanceVal)) {
		$("#payWay").val(2);
	}
})
</script>
</html>
<script>
	var prepay_id;
	var paySign;
	var appId;
	var timeStamp;
	var nonceStr;
	var packageStr;
	var signType;
	mui.init({
		swipeBack:true //启用右滑关闭功能
	});
	$(function() {
		$("a[id^='pay']").click(function() {
			var payVal = $(this).attr("data-myselectval");
			$("#payWay").val(payVal);
		})
	})
	$("#submitbtn").click(function() {
		var payWayVal = $("#payWay").val();
		var balanceVal = $("#balance").val();
		var moneyVal = $("#money").val();
		var ifwallet = $("#ifwallet").val();
		if (payWayVal == 2) {
			if (ifwallet == 2) {
				$.ajax({
					type : "post",
					url : "/charge/codePaywx",
					dataType : "json",
					data : {
						openId : $("#openid").val(),
						code : $("#code").val(),
						portchoose : $("#portchoose").val(),
						chargeparam : $("#chargeparam").val(),
					},
					success : function(data) {
						if (data.wolferror == 1) {
							mui.alert("系统异常，请重新返回选择端口后进入")
						} else if (data.wolferror == 2) {
							mui.alert("系统异常，请重新返回选择充电时间后进入")
						} else {
							appId = data.appId;
							paySign = data.paySign;
							timeStamp = data.date;
							nonceStr = data.nonceStr;
							packageStr = data.packagess;
							signType = data.signType;
							callpay();
						}
					}
				});
			} else {
				mui.alert("此设备不可用微信支付，请使用钱包支付");
			}
		} else {
			if (parseFloat(moneyVal) > parseFloat(balanceVal)) {
				mui.alert('钱包余额不足,请及时充值');
				return false;
			} else {
				$.bootstrapLoading.start({ loadingTips: "支付中..." });
				$.ajax({
					type : "post",
					url : "/charge/walletCharge",
					dataType : "json",
					data : $("#payform").serialize(),
					success : function(data) {
						if (data == 0) {
							mui.alert("用户登陆过期，请重新登陆");
						} else if (data == 1) {
							mui.alert('支付成功',function() {
								window.location.replace("${hdpath}/general/payaccess");
							});
						} else if (data == 2) {
							mui.alert("此设备暂时不可钱包充电，如有疑问请联系商家");
						}
					},
			        complete: function () {
			            $.bootstrapLoading.end();
			        }
				});
			}
		}
	})
	function onBridgeReady() {
		WeixinJSBridge.invoke('getBrandWCPayRequest', {
			"appId" : appId, //公众号名称，由商户传入
			"paySign" : paySign, //微信签名
			"timeStamp" : timeStamp, //时间戳，自1970年以来的秒数
			"nonceStr" : nonceStr, //随机串
			"package" : packageStr, //预支付交易会话标识
			"signType" : signType
		//微信签名方式
		}, function(res) {
			if (res.err_msg == "get_brand_wcpay_request:ok") {
				window.location.replace("${hdpath}/general/payaccess");
			} else if (res.err_msg == "get_brand_wcpay_request:cancel") {
				mui.alert('支付取消', '钱包充值', function() {
				});
			} else if (res.err_msg == "get_brand_wcpay_request:fail") {
				mui.alert('支付失败', '钱包充值', function() {
				});
			} //使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
		});
	}
	function callpay() {
		if (typeof WeixinJSBridge == "undefined") {
			if (document.addEventListener) {
				document.addEventListener('WeixinJSBridgeReady', onBridgeReady,
						false);
			} else if (document.attachEvent) {
				document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
				document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
			}
		} else {
			onBridgeReady();
		}
	}
</script>