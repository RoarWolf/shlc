<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>origin测试</title>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<link href="${hdpath}/css/ui-choose.css" rel="stylesheet" />
<script src="${hdpath}/js/jquery.js"></script>
<script src="${hdpath}/js/ui-choose.js"></script>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>

</head>
<body id="body">
<form id="payfrom" action="${hdpath }/ceshiwxpay/pay" method="post">
	<div>设备号：<input type="text" id="code" name="code" value="000001"></div>
	<div>openid：<input type="text" id="openid" name="openid" value="odVHPwsQE-vkZnp697UUPslMZN6k"></div>
	<div>端口号：<input type="text" id="portchoose" name="portchoose" value="1"></div>
	<div>续充：<input type="text" id="ifcontinue" name="ifcontinue" value=""></div>
	<div>金额：<input type="text" id="paymoney" name="paymoney" value="1"></div>
<!-- 	<button type="submit">提交</button>	 -->
</form>
<button type="button" id="originu">提交</button>
</body>
<script type="text/javascript">
$('#originu').click(function(){
	var url = '${hdpath}/ceshiwxpay/pay';
	$.ajax({
		type : "post",
		url : url,
		dataType : "json",
		data : $('#payfrom').serialize(),
		success : function(data) {
			if (data.err == "0") {
				mui.alert('当前未绑定商家，请重新扫描设备二维码后再充值', '钱包充值', function() {
				});
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
	})
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
			mui.alert('支付成功',function() {
				window.location.replace("${hdpath}/general/payaccess");
			});
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
			document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
		} else if (document.attachEvent) {
			document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
			document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
		}
	} else {
		onBridgeReady();
	}
}

</script>
</html>