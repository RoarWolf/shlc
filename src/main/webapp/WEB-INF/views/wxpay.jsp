<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>确认支付信息</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<!--App自定义的css-->
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/app.css" />
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<style type="text/css">
.title {
	width: 100%;
	text-align: center;
	height: 50px;
	line-height: 50px;
	font-size: 22px;
	color: #000;
}

.pay {
	width: 100%;
}

.btn-pay {
	display: block;
	width: 90%;
	height: 40px;
	margin: 0 auto;
	background: #1AAD19;
	color: #fff;
	margin-top: 20px;;
}

.need {
	width: 100%;
	height: 60px;
	line-height: 40px;;
	font-size: 30px;
	text-align: center;
}

.payfor {
	width: 100%;
	height: 40px;
	line-height: 40px;;
	background: #fff;
	color: #999;
}

.payfor-left {
	float: left;
	margin-left: 5%;
}

.payfor-right {
	float: right;
	margin-right: 5%;
	color: #000;
}
</style>
</head>
<body>
	<input type="hidden" id="prepay_id" value="${prepay_id}">
	<input type="hidden" id="date" value="${date }">
	<input type="hidden" id="paySign" value="${paySign }">
	<input type="hidden" id="packagess" value="${packagess }">
	<input type="hidden" id="nonceStr" value="${nonceStr }">
	<input type="hidden" id="out_trade_no" value="${out_trade_no }">
	<input type="hidden" id="appId" value="${appId }">
	<input type="hidden" id="attention" value="${attention }">
	<div class="title">自助充电平台</div>
	<div class="need">
		￥<span>${money / 100 }元</span>
	</div>

	<div class="payfor">
		<div class="payfor-left">收款方</div>
		<div class="payfor-right">自助充电平台</div>
	</div>
	<div class="mui-row pay">
		<button class="mui-btn btn-pay" onclick="pay();">立即支付</button>
	</div>

	<script src="${pageContext.request.contextPath }/mui/js/mui.min.js"></script>
	<script type="text/javascript">
		function onBridgeReady() {
			var prepay_id = $("#prepay_id").val();
			var date = $("#date").val();
			var paySign = $("#paySign").val();
			var packagess = $("#packagess").val();
			var nonceStr = $("#nonceStr").val();
			var out_trade_no = $("#out_trade_no").val();
			var appId = $("#appId").val();
			WeixinJSBridge.invoke('getBrandWCPayRequest', {
				"appId" : '${appId}', //公众号名称，由商户传入     
				"timeStamp" : date, //时间戳，自1970年以来的秒数     
				"nonceStr" : nonceStr, //随机串     
				"package" : packagess,
				"signType" : "MD5", //微信签名方式：     
				"paySign" : paySign
			//微信签名 
			}, function(res) {
				if (res.err_msg == "get_brand_wcpay_request:ok") {
					//alert("成功");
					/* window.location.href = "${hdpath}/wxpay/wxpayaccess?out_trade_no=" + out_trade_no; */
					if ($("#attention").val() == 1) {
						window.location.href = "https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=MzU4MDY5OTg5MQ==&scene=126&subscene=0#wechat_redirect";
					} else {
						WeixinJSBridge.call('closeWindow');
					}
				} else {
					if ($("#attention").val() == 1) {
						window.location.href = "https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=MzU4MDY5OTg5MQ==&scene=126&subscene=0#wechat_redirect";
					} else {
						WeixinJSBridge.call('closeWindow');
					}
					/* window.location.href = "${hdpath}/wxpay/payfail?out_trade_no=" + out_trade_no; */
				} // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。 
			});
		}
		$(function(){  
		    pushHistory();  
		    window.addEventListener("popstate", function(e) {
		    	mui.alert('点击返回需重新扫面二维码', '自助充电平台', function() {
			        WeixinJSBridge.call('closeWindow');
				});
		}, false);  
		    function pushHistory() {  
		        var state = {  
		            title: "title",  
		            url: "#"  
		        };
		        window.history.pushState(state, "title", "#");  
		    }  
		}); 
		function pay() {
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
</body>
</html>