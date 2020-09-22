<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>投币</title>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<script type="text/javascript" src="${hdpath }/js/fastclick.js"></script>
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/app.css" />
<link href="${hdpath}/css/ui-choose.css" rel="stylesheet" />
<script src="${hdpath}/js/jquery.js"></script>
<script src="${hdpath}/js/ui-choose.js"></script>
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
	float: none;
    width: 33%;
    height: 34px;
    line-height: 32px;
    margin: 0 auto;
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

.payfor {
	width: 100%;
	height: 40px;
	line-height: 40px;;
	background: #fff;
	color: #999;
}
</style>
</head>
<body>
	<input type="hidden" id="code" value="${code }">
	<input type="hidden" id="openid" value="${openid}">
	<input type="hidden" id="nowtime" value="${nowtime}">
	<input type="hidden" id="userid" value="${userid}">
	<c:if test="${brandname != null }">
		<div align="center" style="font-size: 15px" >
			欢迎使用${brandname}电动车充电设备
		</div>
	</c:if>
	<div style="font-size: 15px;padding-top: 10px" align="center">
		当前设备编号为：${code }
	</div>
	<div class="demo-box">
	<table class="demo-table">
		<caption></caption>
		<tr>
			<td>
				<ul id="chargechoose"><li style="font-size: 16px;">请选择投币金额</li></ul>
				<ul class="ui-choose" id="uc_02">
					<c:forEach items="${templatelist}" var="temp">
						<li style="width: 100%">${temp.name }<span style="display:none;">_${temp.id}</span></li>
					</c:forEach>
				</ul>
			</td>
		</tr>
	</table>
	</div>
	<form id="payfrom" action="${hdpath }/alipay/inCoinsCreateOrder" method="post">
		<input type="hidden" id="code" name="code" value="${code}">
		<input type="hidden" id="port" name="port" value="1">
		<input type="hidden" id="chargeparam" name="chargeparam" value="${defaultTemp }">
	</form>
	<div class="mui-row pay" align="center">
		<button class="mui-btn btn-pay" id="payButton">确认付款</button>
	</div>
	<div style="font-size: 14px;position: fixed;bottom: 0;padding-bottom: 10px;padding-left: 10px;">
		<span>如有疑问，敬请联系：</span>
		<a href="tel:${phonenum }">${phonenum }</a>
	</div>
<script type="text/javascript">
// 将所有.ui-choose实例化
$('.ui-choose').ui_choose();
// uc_01 ul 单选
var uc_02 = $('#uc_02').data('ui-choose'); 
uc_02.click = function(index, item){
	var id = item.text().split("_")[1];
	$('input[name="chargeparam"]').val(id);
}
$(function() {
	uc_02._val_ul(1);
})
</script>
<script src="${pageContext.request.contextPath }/mui/js/mui.min.js"></script>
<script src="${hdpath }/js/my.js"></script>
</body>
<script type="application/javascript">

    $(document).ready(function(){

        // 点击payButton按钮后唤起收银台
        $("#payButton").click(function() {
        	var code = $("#code").val();
    		var chargeparam = $("#chargeparam").val();
    		if (chargeparam == null || chargeparam == "") {
    			mui.alert('未选择投币金额', '', function() {
    			});
    			return false;
    		} else {
    			$(this).attr("disabled","disabled");
    			$.ajax({
        			url : "${hdpath}/alipay/userincoinspay",
        			data : {
        				code : code,
        				port : $("#port").val(),
        				chargeparam : chargeparam,
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
	/* setTimeout(connectInCoins,500);
	function connectInCoins() {
		$.bootstrapLoading.start({ loadingTips: "正在连接..." });
		$.ajax({
			url : '/connectInCoins',
			type : "POST",
			dataType : "json",
			data : {
				code : $("#code").val(),
				nowtime : $("#nowtime").val(),
			},
			async : true,
			success : function(data) {
				if (data.err == "1") {
					mui.alert(data.errinfo, '', function() {
						AlipayJSBridge.call('closeWebview');
					});
				}
			},
	        complete: function () {
	            $.bootstrapLoading.end();
	        },
	        error : function(XMLHttpRequest, textStatus, errorThrown) {
	        	console.log(XMLHttpRequest.readyState + "---" + XMLHttpRequest.status
	        			+ "---" + textStatus);
	        	console.log(XMLHttpRequest.responseText);
	        }
		});
	} */
	$("#submitbtn").click(function() {
		var code = $("#code").val();
		var chargeparam = $("#chargeparam").val();
		if (chargeparam == null || chargeparam == "") {
			mui.alert('未选择投币金额', '', function() {
			});
			return false;
		} else {
			$(this).attr("disabled","disabled");
			$("#payfrom").submit();
		}
	})
})
</script>