<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
<title>充电付款</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!--标准mui.css-->
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link rel="stylesheet" href="${hdpath}/mui/css/mui.min.css">
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/icons-extra.css" />
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="${hdpath}/mui/js/mui.min.js"></script>
<script type="text/javascript" src="${hdpath }/js/jquery-2.1.0.js"></script>
</head>
<body>
	<form id="payfrom" action="/wxpay/pay">
		<input type="hidden" id="code" name="code" value="${code}">
		<input type="hidden" id="openid" name="openid" value="${openid}">
		<input type="hidden" id="portchoose" name="portchoose" value="${port}">
		<input type="hidden" id="chargeparam" name="chargeparam" value="25">
		<input type="hidden" id="ifcontinue" name="ifcontinue" value="">
	</form>
	<br>
	<div>
		<span>当前设备号：${code}</span>
		<ul class="mui-table-view">
					<c:forEach items="${tempinfo.gather}" var="temp">
					    <li class="mui-table-view-cell" data-temsonid=${temp.id}>${temp.name}</li>
					</c:forEach>
				</ul>
	</div>
	<div><button id="but">确认支付</button></div>
	<%@ include file="/WEB-INF/views/public/generalBtnNav.jsp"%>
</body>
<script src="${hdpath }/js/my.js"></script>
<script>
	$(function() {
		var appId;
		var paySign;
		var timeStamp;
		var nonceStr;
		var packageStr;
		var signType;
		var ordernum;
		var attention;
		$('#but').click(chargepay)
		function chargepay() {
			$.ajax({
				type : "post",
				url : '${hdpath}/wxpay/pay',
				dataType : "json",
				data : $('#payfrom').serialize(),
				success : function(data) {
					if (data.wolferror == 1) {
						mui.alert(data.wolferrorinfo);
					} else if (data.wolferror == 2) {
						mui.alert(data.wolferrorinfo);
					} else {
						appId = data.appId;
						paySign = data.paySign;
						timeStamp = data.date;
						nonceStr = data.nonceStr;
						packageStr = data.packagess;
						signType = data.signType;
						ordernum = data.out_trade_no;
						attention = data.attention;
						callpay();
					}
				}
			});
		}
		
		$("#inCommonElecStation").click(function() {
			window.location.href = "${hdpath}/charge/numCharge";
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
					WeixinJSBridge.call('closeWindow');
					//window.location.replace("${hdpath}/general/payaccess");
				} else if (res.err_msg == "get_brand_wcpay_request:cancel") {
					mui.alert('支付取消', '', function() {
					});
				} else if (res.err_msg == "get_brand_wcpay_request:fail") {
					mui.alert('支付失败', '', function() {
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
		
	})
</script>
</html>