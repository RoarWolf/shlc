<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="hdpath" value="${pageContext.request.contextPath }" />
<!DOCTYPE html>
<html>
<head>
<title>离线卡充值</title>
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<!--App自定义的css-->
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/app.css" />
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link href="${hdpath}/css/ui-choose.css" rel="stylesheet" />
<script src="${hdpath}/js/jquery.js"></script>
<script src="${hdpath}/js/ui-choose.js"></script>
<script src="${pageContext.request.contextPath }/mui/js/mui.min.js"></script>
<script src="${hdpath }/js/my.js"></script>
<script type="text/javascript" src="${hdpath }/js/fastclick.js"></script>
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
	<input type="hidden" id="nowtime" value="${nowtime}">
	<input type="hidden" id="userid" value="${userid}">
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
								<li style="width: 99%;" id="port${templateSon.id }" value="${templateSon.id }">${templateSon.name }</li>
							</c:when>
							<c:when test="${bindtype == 1 }">
								<c:forEach items="${templatelist}" var="temp">
									<li id="port${temp.id }" value="${temp.id }">${temp.name }</li>
								</c:forEach>
							</c:when>
						</c:choose>
					</ul>
				</td>
			</tr>
		</table>
	</div>
	<form id="payfrom" action="${hdpath }/alipay/offlineCreateOrder" method="post">
		<input type="hidden" id="cardSurp" name="cardSurp" value="">
		<input type="hidden" id="cardnum" name="cardnum" value="">
		<input type="hidden" id="code" name="code" value="${code }">
	</form>
	<div class="mui-row pay">
		<button class="mui-btn btn-pay" id="payButton" >确认充值</button>
	</div><br>
	<div style="font-size: 14px;position: fixed;bottom: 0;padding-bottom: 10px;padding-left: 10px;">
		<span>如有疑问，敬请联系：</span>
		<a href="tel:${phonenum }">${phonenum }</a>
	</div>
</body>
<script type="text/javascript">
// 将所有.ui-choose实例化
$('.ui-choose').ui_choose();
// uc_01 ul 单选
var uc_01 = $('#uc_01').data('ui-choose');
uc_01.click = function(index, item) {
	$("#cardSurp").val(item.val());
}

</script>
<script type="text/javascript">
$(document).ready(function(){

    // 点击payButton按钮后唤起收银台
    $("#payButton").click(function() {
    	var code = $("#code").val();
		var cardnum = $("#cardnum").val();
		var cardSurp = $("#cardSurp").val();
		if (cardnum == null || cardnum == "") {
			mui.alert('无卡', '', function() {
			});
			return false;
		} else if (cardSurp == null || cardSurp == "") {
			mui.alert('未选择充值金额', '', function() {
			});
			return false;
		} else {
			$(this).attr("disabled","disabled");
			$.ajax({
    			url : "${hdpath}/alipay/userofflinecardpay",
    			data : {
    				code : code,
    				cardnum : cardnum,
    				cardSurp : cardSurp,
    				uid : $("#userid").val(),
    			},
    			type : "post",
    			dataType : "json",
    			beforeSend: function() {
    				$.bootstrapLoading.start({ loadingTips: "正在支付..." });
    		    },
    			success : function(data) {
    				if (data.wolfcode == 1) {
		          		 tradePay(data.trade_no);
    				} else if (data.wolfcode == 0) {
    					alert("系统异常");
    				}
    			},
    	        complete: function () {
    	            $.bootstrapLoading.end();
    	        }
        	})
		}
    });

    // 通过jsapi关闭当前窗口，仅供参考，更多jsapi请访问
    // /aod/54/104510
    $("#closeButton").click(function() {
       AlipayJSBridge.call('closeWebview');
    });
 });

// 由于js的载入是异步的，所以可以通过该方法，当AlipayJSBridgeReady事件发生后，再执行callback方法
function ready(callback) {
     if (window.AlipayJSBridge) {
         callback && callback();
     } else {
         document.addEventListener('AlipayJSBridgeReady', callback, false);
     }
}

function tradePay(tradeNO) {
    ready(function(){
         // 通过传入交易号唤起快捷调用方式(注意tradeNO大小写严格)
         AlipayJSBridge.call("tradePay", {
              tradeNO: tradeNO
         }, function (data) {
             if ("9000" == data.resultCode) {
                 alert("支付成功");
             }
             AlipayJSBridge.call('closeWebview');
         });
    });
}
</script>
</html>
<script>
$(function() {
	setTimeout(queryCardinfo,500);
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
						AlipayJSBridge.call('closeWebview');
					});
				} else if (data.err == "1") {
					mui.alert(data.errinfo, '', function() {
						AlipayJSBridge.call('closeWebview');
					});
				} else {
					$("#cardId").val(data.card_id);
					$("#cardMoney").val(data.card_surp / 10);
					$("#cardnum").val(data.card_id);
				}
			},
	        complete: function () {
	            $.bootstrapLoading.end();
	        }
		});
	}
	$("#submitbtn").click(function() {
		var code = $("#code").val();
		var cardnum = $("#cardnum").val();
		var cardSurp = $("#cardSurp").val();
		if (cardnum == null || cardnum == "") {
			mui.alert('无卡', '', function() {
			});
			return false;
		} else if (cardSurp == null || cardSurp == "") {
			mui.alert('未选择充值金额', '', function() {
			});
			return false;
		} else {
			$(this).attr("disabled","disabled");
			$("#payfrom").submit();
		}
	})
})
</script>