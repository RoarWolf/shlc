<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<title>自助充电平台</title>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<link href="${hdpath}/css/ui-choose.css" rel="stylesheet" />
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script src="${hdpath}/js/jquery.js"></script>
<script src="${hdpath}/js/ui-choose.js"></script>
<script src="${hdpath }/js/my.js"></script>
<style type="text/css">
.demo-table{margin: 1rem 0 1.5rem 0;}
.ui-choose .equtemp{ width: 98%; margin: 0.1rem 0 0 0.1rem;}
#chargechoose{
	margin: 0; 
	padding: 0 0 0 0.1rem;
	margin-bottom: 6px;
    font-size: 16px;
}
hr {
    margin-top: 20px;
    margin-bottom: 0px;
    border: 0;
    border-top: 1px solid #eee;
}
.pay {
	width: 100%;
}

.btn-pay {
	display: block;
	width: 94%;
	height: 40px;
	margin: 0 auto;
	background: #1AAD19;
	color: #fff;
	margin-top: 20px;
	border-radius: 5px;
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
.demo-table {
	width: 100%;
}
ul.ui-choose {
	display: flex;
	flex-wrap: wrap;
	
}
ul.ui-choose .equtemp {
	width: 30%;
	margin-bottom: 10px;
	margin-left: 2.5%;
	text-align: center;
}

#walletpay span {
	margin-left: 10px;
}
</style>
</head>
<body id="body">
	<hr>
	<input type="hidden" id="code" value="${chargeRecord.equipmentnum }">
	<input type="hidden" id="nowtime" value="${nowtime }">
	<div class="payfor">
		<div class="payfor-left">收款方</div>
		<div class="payfor-right">自助充电平台</div>
	</div>
	<div class="payfor">
		<div class="payfor-left">设备编号</div>
		<div class="payfor-right">${chargeRecord.equipmentnum }</div>
	</div>
	<div class="payfor">
		<div class="payfor-left">端口号码</div>
		<div class="payfor-right">${chargeRecord.port }</div>
	</div>
	<div class="demo-box">
  <table class="demo-table">
    <caption></caption>
    <tr>
     <c:choose>
     	 <c:when test="${temptype == 0}">
		      <td>
		        <ul id="chargechoose">请选择充电时间</ul>
		        <ul class="ui-choose" id="uc_02">
		        
		         <c:forEach items="${templatelist}" var="temp">
		          	<li class="equtemp" id="temp${temp.id}">${temp.name}<span style="display:none;">_${temp.id}</span></li>
		         </c:forEach>
		        </ul>
		      </td>
	      </c:when>
	      <c:when test="${temptype == 1}">
	      	 <td>
		        <ul id="chargechoose">请选择充电金额</ul>
		        <ul class="ui-choose" id="uc_02">
		         <c:forEach items="${temmoney}" var="temp">
		          	<li class="equtemp" id="temp${temp.id}">${temp.name}<span style="display:none;">_${temp.id}</span></li>
		         </c:forEach>
		        </ul>
		      </td>
	      </c:when>
	      <c:when test="${temptype == 2}">
	      	 <td>
		        <ul id="chargechoose">请选择充电时间</ul>
		        <ul class="ui-choose" id="uc_02">
		         <c:forEach items="${temtime}" var="temp">
		          	<li class="equtemp" id="temp${temp.id}">${temp.name}<span style="display:none;">_${temp.id}</span></li>
		         </c:forEach>
		        </ul>
		      </td>
	      </c:when>
      </c:choose>
    </tr>
  </table>
</div>
	<c:if test="${ifwallet == 2 }">
		<div class="mui-row pay">
			<button class="mui-btn btn-pay" onclick="continuechargepay();">微信支付</button>
		</div>
	</c:if>
	<div class="mui-row pay">
		<button class="mui-btn btn-pay" id="walletpay">钱包支付  <span>(充：${user.balance}&nbsp;&nbsp;&nbsp; 赠：${user.sendmoney})</span></button>
	</div>
  <input type="hidden" id="tempsonid" value="${tempid }">
  <input type="hidden" id="tempindex" value="${tempindex }">
<script>
// 将所有.ui-choose实例化
$('.ui-choose').ui_choose();

var uc_02 = $('#uc_02').data('ui-choose');

uc_02.click = function(index, item){
	var id = item.text().split("_")[1];
	$('input[name="chargeparam"]').val(id)
}
$(function() {
	var tempsonidval = $("#tempsonid").val();
	var tempindexval = $("#tempindex").val();
	if (tempsonidval != null && tempsonidval != "") {
		uc_02._val_ul(tempindexval);
	}
})
</script>
<div class="container">
	<form>
		<input id="chargeRecordID" type="hidden" value="${chargeRecord.id }">
		<input type="hidden" id="chargeparam" name="chargeparam" value="${tempid}">
		<input type="hidden" id="uid" name="uid" value="${uid}">
		<input type="hidden" id="openid" name="" value="${openid}">
	</form>
</div>
<script src="${pageContext.request.contextPath }/mui/js/mui.min.js"></script>
</body>
</html>
<script>
$(function() {
	setTimeout(paystate1, 10); //0.01秒后执行
	function paystate1() {
		$.bootstrapLoading.start({ loadingTips: "正在连接，请稍后..." });
		$.ajax({
			url : "${hdpath}/portstate1",
			data : {
				code : $("#code").val(),
				nowtime : $("#nowtime").val()
			},
			type : "post",
			dataType : "json",
			success : function(data) {
				if (data.state == "error") {
					mui.alert('连接失败，请确认设备是否在线', '错误', function() {
						WeixinJSBridge.call('closeWindow');
					});
				}
			},
	        complete: function () {
	            $.bootstrapLoading.end();
	        }
		});
	}
	$("#walletpay").click(function() {
		$("#walletpay").attr("disabled",true);
		$.ajax({
			url : "${hdpath}/charge/walletContinuePay",
			data : {
				chargeparam : $("#chargeparam").val(),
				chargeRecordId : $("#chargeRecordID").val()
			},
			type : "post",
			dataType : "json",
			success : function(data) {
				if (data == "0") {
					mui.alert("余额不足，使用钱包支付请先充值！");
				} else if (data == "1") {
					mui.alert('支付成功', '端口续充', function() {
						window.location.replace("${hdpath}/charge/queryCharging?uid=" + $("#uid").val());
					});
				} else if (data == "2") {
					mui.alert("此设备暂时不支持钱包续充");
				}
			},
		});
	})
})
var prepay_id;
var paySign;
var appId;
var timeStamp;
var nonceStr;
var packageStr;
var signType;
function continuechargepay() {
	var url = '${hdpath}/charge/chargeContinueAccess';
	$.ajax({
		type : "post",
		url : url,
		dataType : "json",
		data : {
			chargeparam : $("#chargeparam").val(),
			chargeRecordId : $("#chargeRecordID").val(),
			openid:$("#openid").val()
		},
		success : function(data) {
			appId = data.appId;
			paySign = data.paySign;
			timeStamp = data.date;
			nonceStr = data.nonceStr;
			packageStr = data.packagess;
			signType = data.signType;
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
				mui.alert('支付成功', '端口续充', function() {
					window.location.replace("${hdpath}/charge/queryCharging?uid=" + $("#uid").val());
				});
			} else if (res.err_msg == "get_brand_wcpay_request:cancel") {
				mui.alert('支付取消', '端口续充', function() {
					window.location.replace("${hdpath}/charge/queryCharging?uid=" + $("#uid").val());
				});
			} else if (res.err_msg == "get_brand_wcpay_request:fail") {
				mui.alert('支付失败', '端口续充', function() {
					window.location.replace("${hdpath}/charge/queryCharging?uid=" + $("#uid").val());
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