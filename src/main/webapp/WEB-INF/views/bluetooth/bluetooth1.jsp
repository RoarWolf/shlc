<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html class="ie">
<!--<![endif]-->
<head>
<meta charset="utf-8">
<title>微信蓝牙设备</title>
<meta name="viewport"
	content="width=320.1,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no">
<script type="text/javascript" src="/js/jquery-2.1.0.js"></script>
<script type="text/javascript" src="/js/base64.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.1.0.js">
	
</script>
<!--标准mui.css-->
<link rel="stylesheet" href="/mui/css/mui.min.css">
<script type="text/javascript" src="/mui/js/mui.min.js"></script>
</head>
<body ontouchstart>
	<input type="hidden" id="openid" value="${openid }">
	<!--标题行-->
	<h2
		style="color: white; background-color: green; text-align: center; background-position: center;">蓝牙设备</h2>
	<div class="page">
		<div class="bd spacing">
			<div class="weui_cells weui_cells_form">
				<div class="weui_cell">
					<div class="weui_cell_hd">
						<label class="weui_label" style="width: auto;">当前设备:&nbsp</label>
					</div>
					<div class="weui_cell_bd weui_cell_primary">
						<label id="lbdeviceid" class="weui_label" style="width: auto;"></label>
					</div>
				</div>
				<div class="weui_cell">
						<label class="weui_label" style="width: auto;">状态信息:&nbsp</label>
						<label id="lbInfo" class="weui_label" style="width: auto;"></label>
				</div>
				<div class="weui_cell">
					<div class="weui_cell_hd">
						<label class="weui_label">日志: </label>
					</div>
					<div>
						<textarea id="logtext" placeholder="等待接收..."
							rows="2" style="width: 100%" readonly="readonly"></textarea>
					</div>
					<div class="weui_cell_hd">
						<label class="weui_label">发送: </label>
					</div>
					<div>
						<textarea id="sendtext" placeholder="发送信息"
							rows="2" style="width: 100%"></textarea>
					</div>
				</div>
			</div>

			<div class="weui_btn_area weui">
				<button class="mui-btn mui-btn-success mui-btn-block"
					id="CallGetWXrefresh">获取设备</button>
				<br>
			</div>
			<div class="weui_btn_area weui_btn_area_inline">
				<button class="mui-btn mui-btn-success mui-btn-block" id="icFuWei">发送</button>
			</div>
			<div class="weui_btn_area weui_btn_area_inline">
				<button class="mui-btn mui-btn-success mui-btn-block" onclick="walletpay();">支付</button>
			</div>
		</div>

		<div class="weui_dialog_alert" id="Mydialog" style="display: none;">
			<div class="weui_mask"></div>
			<div class="weui_dialog">
				<div class="weui_dialog_hd" id="dialogTitle">
					<strong class="weui_dialog_title">着急啦</strong>
				</div>
				<div class="weui_dialog_bd" id="dialogContent">亲,使用本功能,请先打开手机蓝牙！</div>
				<div class="weui_dialog_ft">
					<a href="#" class="weui_btn_dialog primary">确定</a>
				</div>
			</div>
		</div>

	</div>

	<div id="myparams" style="display: none">
		<span id="timestamp">${timestamp }</span> <span id="nonceStr">${nonceStr }</span>
		<span id="signature">${signature }</span> <span id="appId">${appId }</span>
		<span id="trueCardRandom"></span>

	</div>



</body>
<script type="text/javascript" src="/js/bluetooth.js"></script>
<script>

var prepay_id;
var paySign;
var appId;
var timeStamp;
var nonceStr;
var packageStr;
var signType;
var ordernum;
var attention;
function walletpay() {
	var url = '${hdpath}/wxpay/testpay';
	
	$.ajax({
		type : "post",
		url : url,
		dataType : "json",
		data : {
			openId : $("#openid").val()
		},
		success : function(data) {
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
	});
}
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
				var sendData = $('#sendtext').val();
				senddataBytes(sendData + "\r\n", C_DEVICEID)
			} else if (res.err_msg == "get_brand_wcpay_request:cancel") {
				mui.alert('支付取消', '蓝牙测试');
			} else if (res.err_msg == "get_brand_wcpay_request:fail") {
				mui.alert('支付失败', '蓝牙测试');
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
</html>