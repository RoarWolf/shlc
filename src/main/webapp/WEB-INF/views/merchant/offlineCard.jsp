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
							<c:forEach items="${templateLists }" var="temp">
								<li id="port${temp.id }" value="${temp.money }">${temp.name }</li>
							</c:forEach>
					</ul>
				</td>
			</tr>
		</table>
	</div>
	
	<input type="hidden" id="cardSurp" value="">
	<div class="mui-row pay">
		<button id="wolfsubmitbtn" class="mui-btn btn-pay" onclick="chargeCard();" >确认充值</button>
	</div><br>
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
</html>
<script>
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
					mui.alert(data.res);
				} else if (data.err == "1") {
					mui.alert(data.errinfo);
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