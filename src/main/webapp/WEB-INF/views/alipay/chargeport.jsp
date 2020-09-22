<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>自助充电平台</title>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<script type="text/javascript" src="${hdpath }/js/fastclick.js"></script>
<link href="${hdpath}/css/ui-choose.css" rel="stylesheet" />
<script src="${hdpath }/mui/js/mui.min.js"></script>
<script src="${hdpath}/js/jquery.js"></script>
<script src="${hdpath}/js/ui-choose.js"></script>
<style type="text/css">
body{
padding-top : 15px;
color: #666;
font-size: 14px;
}
table,tbody,tr,td {
	width: 100%;
}
.demo-table{margin: 1rem 0 1.5rem 0;}
.ui-choose .equtemp{ width: 98%; margin: 0.1rem 0 0 0.1rem;}
#chargechoose{margin: 0; padding: 0 0 0 0.1rem;margin-bottom: 10px; color: #999;}
#submitbtn{float: right;  margin-right: 2rem;}
.telDiv {
	position: absolute;
	left: 4%;
	bottom: 25%;
	font-size: 14px
}

</style>
<script type="text/javascript">
$(function() {

    FastClick.attach(document.body);

});
</script>
</head>
<body>
	<div align="center">
		<font style="font-size: 16px;">当前设备编号为：${code }-${port }</font>
	</div>
	<hr>
	<div class="demo-box">
  <table class="demo-table">
    <caption></caption>
    <tr>
      <td>
        <ul id="chargechoose">请选择充电时间</ul>
        <ul class="ui-choose" id="uc_02">
        <c:forEach items="${templatelist}" var="temp">
          <li class="equtemp">${temp.name}<span style="display:none;">_${temp.id}</span></li>
         </c:forEach>
        </ul>
      </td>
    </tr>
  </table>
  <input type="hidden" id="defaultindex" value="${defaultindex }">
</div>
<script>
// 将所有.ui-choose实例化
$('.ui-choose').ui_choose();
var uc_02 = $('#uc_02').data('ui-choose'); 
uc_02.click = function(index, item){
	var id = item.text().split("_")[1];
	$('input[name="chargeparam"]').val(id)
    //console.log('click', index, item.text())
}
/* uc_02.change = function(index, item) {
    console.log('change', index, item.text())
} */
$(function() {
	uc_02._val_ul($("#defaultindex").val());
})
</script>
<div class="container">
	<input type="hidden" id="nowtime" value="${nowtime}">
	<input type="hidden" id="userid" value="${userid}">
	<form id="payfrom" action="${hdpath }/alipay/createOrder" method="post">
		<input type="hidden" id="code" name="code" value="${code}">
		<input type="hidden" id="portchoose" name="portchoose" value="${port }">
		<input type="hidden" id="chargeparam" name="chargeparam" value="${defaultchoose }">
		<div>
			<button class="btn btn-success" id="payButton">确认付款</button>
		</div>
	</form>
	<hr/>
	<c:if test="${phonenum != null}"> 
		<div class="telDiv">
			<span>如有疑问，敬请联系：</span>
			<a href="tel:${phonenum }">${phonenum }</a>
		</div>
	 </c:if>
	
</div>
<script src="${hdpath }/js/my.js"></script>
</body>
<script type="application/javascript">

    $(document).ready(function(){

        // 点击payButton按钮后唤起收银台
        $("#payButton").click(function() {
        	var code = $("#code").val();
    		var portchoose = $("#portchoose").val();
    		var chargeparam = $("#chargeparam").val();
    		if (portchoose == null || portchoose == "") {
    			mui.alert('未选择充电端口', '', function() {
    			});
    			return false;
    		} else if (chargeparam == null || chargeparam == "") {
    			mui.alert('未选择充电时间', '', function() {
    			});
    			return false;
    		} else {
    			$(this).attr("disabled","disabled");
    			$.ajax({
        			url : "${hdpath}/alipay/userchargepay",
        			data : {
        				code : code,
        				port : portchoose,
        				tempid : chargeparam,
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
	$("#submitbtn").click(function() {
		var code = $("#code").val();
		var portchoose = $("#portchoose").val();
		var chargeparam = $("#chargeparam").val();
		if (portchoose == null || portchoose == "") {
			mui.alert('未选择充电端口', '', function() {
			});
			return false;
		} else if (chargeparam == null || chargeparam == "") {
			mui.alert('未选择充电时间', '', function() {
			});
			return false;
		} else {
			$(this).attr("disabled","disabled");
			$("#payfrom").submit();
		}
	})
	
	setTimeout(paystate1, 500); //页面加载完1秒后执行
	function paystate1() {
		$.ajax({
			url : "${hdpath }/portstate2",
			data : {
				code : $("#code").val(),
				port : $("#portchoose").val(),
				nowtime : $("#nowtime").val()
			},
			type : "post",
			dataType : "json",
			beforeSend: function() {
				$.bootstrapLoading.start({ loadingTips: "正在连接..." });
		    },
			success : function(data) {
				if (data.state == "error") {
					mui.alert('连接失败，请确认设备是否在线', '错误', function() {
						AlipayJSBridge.call('closeWebview');
					});
				} else {
					if (data.portstatus != "空闲") {
						mui.alert('此端口不可用', '提示', function() {
							AlipayJSBridge.call('closeWebview');
						});
					}
				}
			},
	        complete: function () {
	            $.bootstrapLoading.end();
	        }
		});
	}
	
})
</script>