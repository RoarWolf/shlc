<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="hdpath" value="${pageContext.request.contextPath }" />
<!DOCTYPE html>
<html>
<head>
<title>离线卡充值</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<!--App自定义的css-->
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/app.css" />
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link href="${hdpath}/css/ui-choose.css" rel="stylesheet" />
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script src="${hdpath}/js/jquery.js"></script>
<script src="${hdpath}/js/ui-choose.js"></script>
<script src="${pageContext.request.contextPath }/mui/js/mui.min.js"></script>
<script src="${hdpath }/js/my.js"></script>
<style type="text/css">
.demo-table {
	margin: 1rem 0 1.5rem 0;
	width: 100%;
}

#chargechoose {
	margin: 0;
	padding: 0 0 0 0.1rem;
}
ul{width: 100%;}
ul.ui-choose>li {
    box-sizing: border-box;
    border: 1px solid #ccc;
    float: left;
    width: 33%;
    height: 34px;
    line-height: 32px;
    margin: 0.1rem 0 0 0.1rem;
    cursor: pointer;
    position: relative;
    z-index: 1;
    min-width: 75px;
    text-align: center;
}
#submitbtn {
	float: right;
	margin-right: 2rem;
}

hr {
	margin-top: 20px;
	margin-bottom: 0px;
	border: 0;
	border-top: 1px solid #eee;
}
</style>
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
	width: 100%;
	height: 40px;
	margin: 0 auto;
	background: #1AAD19;
	color: #fff;
	margin-top: 20px;;
}

.btn-info {
	display: block;
	width: 100%;
	height: 40px;
	margin: 0 auto;
	background: #31b0d5;
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
	<input type="hidden" id="code" value="${code }">
	<input type="hidden" id="openid" value="${openid}">
	<input type="hidden" id="nowtime" value="${nowtime}">
	<div style="font-size: 15px;padding-top: 10px" align="center">
		当前设备编号为：${code }
	</div>
	<div align="center" style="padding-top: 30px">
		<div class="input-group">
			<div class="input-group-addon">卡号</div>
			<input type="text" style="text-align: center;" class="form-control" id="cardId" disabled="disabled" value="">
			<div class="input-group-addon">余额</div>
			<input type="text" style="text-align: center;" class="form-control" id="cardMoney" disabled="disabled" value="">
			<div class="input-group-addon">元</div>
		</div>
		<div class="input-group">
		</div>
	</div>
	<div class="demo-box">
		<table class="demo-table">
			<caption></caption>
			<tr>
				<td align="center">
					<ul id="chargechoose"><li style="font-size: 18px;">请选择充值金额</li></ul>
					<ul class="ui-choose" id="uc_01">
						<c:choose>
							<c:when test="${bindtype == 0 }">
								<c:forEach items="${templateLists}"  var="temp">
									<li id="port${temp.id}" value="${temp.money}">${temp.name}</li>
								</c:forEach>
							</c:when>
							<c:when test="${bindtype == 1 }">
								<c:forEach items="${templateLists}" var="temp">
									<li id="port${temp.id }" value="${temp.id}">${temp.name }</li>
								</c:forEach>
							</c:when>
						</c:choose>
						<%-- <c:choose>
							<c:when test="${bindtype == 0 }">
								<li style="width: 99%;" id="port${templateSon.id }" value="${templateSon.money }">${templateSon.name }</li>
							</c:when>
							<c:when test="${bindtype == 1 }">
								<c:forEach items="${templateLists }" var="temp">
									<li id="port${temp.id }" value="${temp.id }">${temp.name }</li>
								</c:forEach>
							</c:when>
						</c:choose> --%>
					</ul>
				</td>
			</tr>
		</table>
	</div>
	
	<input type="hidden" id="cardSurp" value="">
	<div class="mui-row pay">
		<button id="wolfsubmitbtn" class="mui-btn btn-pay" 
		<c:if test="${bindtype == 0 }">onclick="chargeCard();"</c:if> 
		<c:if test="${bindtype == 1 }">onclick="wechatpay();"</c:if> >微信支付</button>
	</div><br>
	<%-- <div class="mui-row pay">
		<button id="wolfwalletbtn" class="mui-btn btn-pay" 
		<c:if test="${bindtype == 1 }">onclick="walletpay();"</c:if> >钱包支付<font color="#ecfd08">（充值有优惠）</font></button>
	</div><br> --%>
	<div <c:if test="${bindtype == 1 }"> style="display: none;" </c:if> align="center">
		<a class="mui-btn btn-info" href="${hdpath}/merchant/infoverdict?openid=${openid}&code=${code}&existuser=${existuser}">登陆绑定</a>
	</div>
	<c:if test="${bindtype == 1 }">
		<div style="font-size: 14px;position: fixed;bottom: 0;padding-bottom: 10px;padding-left: 10px;">
			<div>
				注：手机支付完成之前请不要移出卡片
			</div>
			<span>如有疑问，敬请联系：</span>
			<a href="tel:${phonenum }">${phonenum }</a>
		</div>
	</c:if>
</body>
<script type="text/javascript">
// 将所有.ui-choose实例化
$('.ui-choose').ui_choose();
// uc_01 ul 单选
var uc_01 = $('#uc_01').data('ui-choose');
uc_01.click = function(index, item) {
	$("#cardSurp").val(item.val());
}

/* function onchangesd(){
	var money = $("#money").val();
	$("#cardSurp").val(money);
} */
</script>
<script>

var prepay_id;
var paySign;
var appId;
var timeStamp;
var nonceStr;
var packageStr;
var signType;
var ordernum;
function wechatpay() {
	var url = '${hdpath}/wxpay/offlineCharge';
	var cardid = $("#cardId").val();
	var cardSurp = $("#cardSurp").val();
	var paymoneyinfo = '请选择支付金额';
	var paymoneystate = '正在充值...';
	if (cardid == "" || cardid == null) {
		mui.alert('无卡', '', function() {
		});
		return false;
	}
	if (cardSurp == "" || cardSurp == null) {
		mui.alert(paymoneyinfo);
		return false;
	}
	$("#wolfsubmitbtn").attr("disabled",true);
	$.ajax({
		type : "post",
		url : url,
		dataType : "json",
		data : {
			openId : $("#openid").val(),
			code : $("#code").val(),
			card_id : cardid,
			tempid : cardSurp,
			card_ope : 1
		},
		success : function(data) {
			appId = data.appId;
			paySign = data.paySign;
			timeStamp = data.date;
			nonceStr = data.nonceStr;
			packageStr = data.packagess;
			signType = data.signType;
			ordernum = data.out_trade_no;
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
				WeixinJSBridge.call('closeWindow');
			} else if (res.err_msg == "get_brand_wcpay_request:cancel") {
				mui.alert('支付取消', '充值卡', function() {
					$.ajax({
						type : "post",
						url : '${hdpath}/wxpay/offlinePayfail',
						dataType : "json",
						data : {
							ordernum : ordernum
						},
						success : function(data) {
							WeixinJSBridge.call('closeWindow');
						}
					});
				});
			} else if (res.err_msg == "get_brand_wcpay_request:fail") {
				mui.alert('支付失败', '充值卡', function() {
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
</html>
<script>
function walletpay() {
	var cardid = $("#cardId").val();
	var cardSurp = $("#cardSurp").val();
	var paymoneyinfo = '请选择支付金额';
	var paymoneystate = '正在充值...';
	if (cardid == "" || cardid == null) {
		mui.alert('无卡', '', function() {
		});
	}
	if (cardSurp == "" || cardSurp == null) {
		mui.alert(paymoneyinfo);
	} else {
		$("#wolfwalletbtn").attr("disabled",true);
		$.ajax({
			type : "post",
			url : '${hdpath}/general/offlineWalletPay',
			dataType : "json",
			data : {
				openId : $("#openid").val(),
				code : $("#code").val(),
				card_id : cardid,
				tempid : cardSurp,
				card_ope : 1
			},
			success : function(data) {
				if (data == 1) {
					mui.alert("充值成功");
					$("#wolfwalletbtn").removeAttr("disabled");
				} else if (data == 2) {
					mui.alert("钱包余额不足");
					$("#wolfwalletbtn").removeAttr("disabled");
				} else if (data == 3) {
					mui.alert("充值异常，请重新支付");
					$("#wolfwalletbtn").removeAttr("disabled");
				} else if (data == 4) {
					mui.alert("你的钱包不可支付当前设备，请使用微信支付");
					$("#wolfwalletbtn").removeAttr("disabled");
				}
			},
			error : function(e) {
				mui.alert("支付异常！");
			}
		});
	}
}
function chargeCard() {
	var cardid = $("#cardId").val();
	var cardSurp = $("#cardSurp").val();
	var paymoneyinfo = '请选择支付金额';
	var paymoneystate = '正在充值...';
	if (cardid == "" || cardid == null) {
		mui.alert('无卡', '', function() {
		});
		return false;
	}
	if (cardSurp == "" || cardSurp == null) {
		mui.alert(paymoneyinfo);
		return false;
	}
	$.bootstrapLoading.start({ loadingTips: "正在充值..." });
	$.ajax({
		type : "post",
		url : '${hdpath}/chargeCard',
		dataType : "json",
		data : {
			code : $("#code").val(),
			card_id : cardid,
			card_surp : cardSurp,
			card_ope : 1,
		},
		success : function(data) {
			if (data.err == "1") {
				mui.alert(data.errinfo);
			} else {
				mui.toast("充值成功");
				$("#cardId").val(data.card_id);
				$("#cardMoney").val(data.card_surp / 10);
			}
		},
		complete: function () {
            $.bootstrapLoading.end();
        }
	});
}
$(function() {
	setTimeout(queryCardinfo,10);
	function queryCardinfo() {
		$.bootstrapLoading.start({ loadingTips: "正在获取卡号..." });
		$.ajax({
			url : '${hdpath}/queryOfflineCard',
			type : "POST",
			dataType : "json",
			data : {
				code : $("#code").val(),
				openid : $("#openid").val(),
				nowtime : $("#nowtime").val(),
			},
			success : function(data) {
				if (data.res == "无卡") {
					mui.alert(data.res, '', function() {
						WeixinJSBridge.call('closeWindow');
					});
				} else if (data.err == "1") {
					mui.alert(data.errinfo, '', function() {
						WeixinJSBridge.call('closeWindow');
					});
				} else {
					$("#cardId").val(data.card_id);
					$("#cardMoney").val(data.card_surp / 10);
				}
			},
	        complete: function () {
	            $.bootstrapLoading.end();
	        }
		});
	}
})
</script>