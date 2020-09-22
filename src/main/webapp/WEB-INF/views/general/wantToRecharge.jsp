<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>我要充电</title>
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath}/mui/css/mui.min.css">
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/icons-extra.css" />
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="${hdpath}/mui/js/mui.min.js">
<script type="text/javascript" src="${hdpath}/js/jquery-2.1.0.js"></script>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<style type="text/css">
body{
	background-color: #e3e3e6;
}
.mui-content{
	background-color: #e3e3e6;
}
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
	<input id="appId" type="hidden" value="${appId }">
	<input id="nonceStr" type="hidden" value="${nonceStr }">
	<input id="timestamp" type="hidden" value="${timestamp }">
	<input id="signature" type="hidden" value="${signature }">
	<%@ include file="/WEB-INF/views/public/generalBtnNav.jsp"%>
	<br>
	<div class="mui-content">
		<div>
			<button id="scanQRcodeCharge" class="miu-btn btn-pay">扫码充电</button>
		</div><br>
		<div>
			<button class="miu-btn btn-pay" id="inCommonElecStation">常用电站(编号充电)</button>
		</div>
	</div>
</body>
</html>
<script>
	$(function() {
		$("#inCommonElecStation").click(function() {
			window.location.href = "${hdpath}/charge/numCharge";
		})
		$('#scanQRcodeCharge').click(function() {
			var timestampstr = $("#timestamp").val();
			var nonceStrstr = '${ nonceStr }';
			var signaturestr = '${ signature }';
			var appIdstr = '${appId}';
			wx.config({
				debug : false,
				appId : appIdstr,
				timestamp : timestampstr,
				nonceStr : nonceStrstr,
				signature : signaturestr,
				jsApiList : [ 'scanQRCode' ]
			});
			wx.ready(function(){
				wx.scanQRCode({
					needResult : 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
					desc: 'scanQRCode desc',
					scanType : [ 'qrCode' , 'barCode' ], // 可以指定扫二维码还是一维码，默认二者都有
					success : function(res) {
						var url = res.resultStr;
						if (url.indexOf("http://www.tengfuchong.com.cn/oauth2pay?code=",0) != -1) {
							var index = url.indexOf("=",0);
							var code = url.substring(index +1);
							window.location.href = "${hdpath }/charge/allChargePort?deviceNum=" + code;
						} else if (url.indexOf("http://www.tengfuchong.com.cn/oauth2Portpay",0) != -1) {
							var index = url.indexOf("=",0);
							var code = url.substring(index +1);
							window.location.href = "${hdpath }/charge/chargePort?codeAndPort=" + code;
						} else {
							mui.alert("此二维码请退出公众号，使用微信扫一扫功能扫码");
						}
					}
				});
			})
	
			wx.error(function(res) {
				alert("错误：" + res.errMsg);
				// config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
			});
		});
	})
</script>