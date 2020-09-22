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
<script src="${hdpath}/js/jquery.js"></script>
<script src="${hdpath}/js/ui-choose.js"></script>
<style type="text/css">
.demo-table{margin: 1rem 0 1.5rem 0;}
.ui-choose .equtemp{ width: 98%; margin: 0.1rem 0 0 0.1rem;}
#chargechoose{margin: 0; padding: 0 0 0 0.1rem;}
#submitbtn{float: right;  margin-right: 2rem;}
</style>
<script type="text/javascript">
$(function() {

    FastClick.attach(document.body);

});
</script>
</head>
<body>
	<div align="center">
		<font size="4px">当前设备编号为：${code }</font>
	</div>
	<hr>
	<div class="demo-box">
  <table class="demo-table">
    <caption></caption>
      <tr>
      <td>
        <ul id="chargechoose">请选择端口</ul>
        <ul class="ui-choose" id="uc_01" style="width: 100%">
          <c:choose>
          	<c:when test="${hardversion == '05'}">
				<li id="port1">1号</li>
				<li id="port2">2号</li>
				<li id="port3">3号</li>
				<li id="port4">4号</li>
				<li id="port5">5号</li>
				<li id="port6">6号</li>
				<li id="port7">7号</li>
				<li id="port8">8号</li>
				<li id="port9">9号</li>
				<li id="port10">10号</li>
				<li id="port11">11号</li>
				<li id="port12">12号</li>
				<li id="port13">13号</li>
				<li id="port14">14号</li>
				<li id="port15">15号</li>
				<li id="port16">16号</li>
			</c:when>
			<c:when test="${hardversion == '06'}">
				<li id="port1">1号</li>
				<li id="port2">2号</li>
				<li id="port3">3号</li>
				<li id="port4">4号</li>
				<li id="port5">5号</li>
				<li id="port6">6号</li>
				<li id="port7">7号</li>
				<li id="port8">8号</li>
				<li id="port9">9号</li>
				<li id="port10">10号</li>
				<li id="port11">11号</li>
				<li id="port12">12号</li>
				<li id="port13">13号</li>
				<li id="port14">14号</li>
				<li id="port15">15号</li>
				<li id="port16">16号</li>
				<li id="port17">17号</li>
				<li id="port18">18号</li>
				<li id="port19">19号</li>
				<li id="port20">20号</li>
			</c:when>
          	<c:when test="${hardversion == '02'}">
	       		<li style="min-width: 50%;margin: 0;" id="port1">1号</li>
	       		<li style="min-width: 50%;margin: 0;" id="port2">2号</li>
	       	</c:when>
        	<c:when test="${allPortSize == null || allPortSize == 0}">
				<li id="port1">1号</li>
				<li id="port2">2号</li>
				<li id="port3">3号</li>
				<li id="port4">4号</li>
				<li id="port5">5号</li>
				<li id="port6">6号</li>
				<li id="port7">7号</li>
				<li id="port8">8号</li>
				<li id="port9">9号</li>
				<li id="port10">10号</li>
        	</c:when>
        	<c:when test="${allPortSize == 2}">
        		<c:forEach items="${allPortStatusList }" var="allport">
        			<li style="min-width: 50%;margin: 0;" id="port${allport.port }">${allport.port }号</li>
        		</c:forEach>
        	</c:when>
        	<c:otherwise>
        		<li id="port1">1号</li>
				<li id="port2">2号</li>
				<li id="port3">3号</li>
				<li id="port4">4号</li>
				<li id="port5">5号</li>
				<li id="port6">6号</li>
				<li id="port7">7号</li>
				<li id="port8">8号</li>
				<li id="port9">9号</li>
				<li id="port10">10号</li>
        	</c:otherwise>
        </c:choose>
        </ul>
     </td>
    </tr>
    <tr><td style="height:25px;"></td></tr>
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

// uc_01 ul 单选
var uc_01 = $('#uc_01').data('ui-choose'); 
uc_01.click = function(index, item) {
	//var name = item.text().replace(/号端口/g, '');
	var name = item.text().replace('号', '');
	$('input[name="portchoose"]').val(name)
}
/* uc_01.change = function(index, item) {
    console.log('change', index, item.text())
} */
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
		<input type="hidden" id="portchoose" name="portchoose" value="">
		<input type="hidden" id="chargeparam" name="chargeparam" value="${defaultchoose }">
	</form>
		<div>
			<button class="btn btn-success" id="payButton">确认付款</button>
		</div>
	<div style="padding-top:15px"></div>
	<hr>
	<p style="color: #e80808">
		注意：<br/>
			白色端口为空闲端口，可使用。<br/>
			红色端口为正在使用，可加钱。<br/>
			灰色端口为端口故障，不可用。<br/>
	</p>
	<hr/>
	<div style="position: absolute;padding-top: 50px">
		<span>如有疑问，敬请联系：</span>
		<a href="tel:${phonenum }">${phonenum }</a>
	</div>
</div>
<script src="${hdpath }/js/my.js"></script>
<script src="${pageContext.request.contextPath }/mui/js/mui.min.js"></script>
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
    		} else if (document.getElementById("port" + portchoose).style.backgroundColor == "gray") {
    			mui.alert(portchoose + '号端口不可用，请重新选择', '提示', function() {
    			});
    			return false;
    		} else if (document.getElementById("port" + portchoose).style.backgroundColor == "rgb(232, 8, 8)") {
    			mui.alert(portchoose + '号端口正在使用，如需续充可关注微信公众号"自助充电平台"，可查看正在充电信息，以及续充等操作', '提示', function() {
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
		} else if (document.getElementById("port" + portchoose).style.backgroundColor == "gray") {
			mui.alert(portchoose + '号端口不可用，请重新选择', '提示', function() {
			});
			return false;
		} else if (document.getElementById("port" + portchoose).style.backgroundColor == "rgb(232, 8, 8)") {
			mui.alert(portchoose + '号端口正在使用，如需续充可关注微信公众号"自助充电平台"，可查看正在充电信息，以及续充等操作', '提示', function() {
			});
			return false;
		} else {
			$(this).attr("disabled","disabled");
			$("#payfrom").submit();
		}
	})
	
	//setTimeout(paystate1, 500); //页面加载完1秒后执行
	//debugger;
	/* function paystate1() {
		$.ajax({
			url : "${hdpath}/portstate1",
			data : {
				code : $("#code").val(),
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
					if (data.param1 == "空闲") {
					} else if (data.param1 == "使用") {
						$("#port1").css("background","#e80808");
					} else {
						$("#port1").css("background","gray");
					}
					if (data.param2 == "空闲") {
					} else if (data.param2 == "使用") {
						$("#port2").css("background","#e80808");
					} else {
						$("#port2").css("background","gray");
					}
					if (data.param3 == "空闲") {
					} else if (data.param3 == "使用") {
						$("#port3").css("background","#e80808");
					} else {
						$("#port3").css("background","gray");
					}
					if (data.param4 == "空闲") {
					} else if (data.param4 == "使用") {
						$("#port4").css("background","#e80808");
					} else {
						$("#port4").css("background","gray");
					}
					if (data.param5 == "空闲") {
					} else if (data.param5 == "使用") {
						$("#port5").css("background","#e80808");
					} else {
						$("#port5").css("background","gray");
					}
					if (data.param6 == "空闲") {
					} else if (data.param6 == "使用") {
						$("#port6").css("background","#e80808");
					} else {
						$("#port6").css("background","gray");
					}
					if (data.param7 == "空闲") {
					} else if (data.param7 == "使用") {
						$("#port7").css("background","#e80808");
					} else {
						$("#port7").css("background","gray");
					}
					if (data.param8 == "空闲") {
					} else if (data.param8 == "使用") {
						$("#port8").css("background","#e80808");
					} else {
						$("#port8").css("background","gray");
					}
					if (data.param9 == "空闲") {
					} else if (data.param9 == "使用") {
						$("#port9").css("background","#e80808");
					} else {
						$("#port9").css("background","gray");
					}
					if (data.param10 == "空闲") {
					} else if (data.param10 == "使用") {
						$("#port10").css("background","#e80808");
					} else {
						$("#port10").css("background","gray");
					}
					if (data.param11 == "空闲") {
					} else if (data.param11 == "使用") {
						$("#port11").css("background","#e80808");
					} else {
						$("#port11").css("background","gray");
					}
					if (data.param12 == "空闲") {
					} else if (data.param12 == "使用") {
						$("#port12").css("background","#e80808");
					} else {
						$("#port12").css("background","gray");
					}
					if (data.param13 == "空闲") {
					} else if (data.param13 == "使用") {
						$("#port13").css("background","#e80808");
					} else {
						$("#port13").css("background","gray");
					}
					if (data.param14 == "空闲") {
					} else if (data.param14 == "使用") {
						$("#port14").css("background","#e80808");
					} else {
						$("#port14").css("background","gray");
					}
					if (data.param15 == "空闲") {
					} else if (data.param15 == "使用") {
						$("#port15").css("background","#e80808");
					} else {
						$("#port15").css("background","gray");
					}
					if (data.param16 == "空闲") {
					} else if (data.param16 == "使用") {
						$("#port16").css("background","#e80808");
					} else {
						$("#port16").css("background","gray");
					}
					if (data.param17 == "空闲") {
					} else if (data.param17 == "使用") {
						$("#port17").css("background","#e80808");
					} else {
						$("#port17").css("background","gray");
					}
					if (data.param18 == "空闲") {
					} else if (data.param18 == "使用") {
						$("#port18").css("background","#e80808");
					} else {
						$("#port18").css("background","gray");
					}
					if (data.param19 == "空闲") {
					} else if (data.param19 == "使用") {
						$("#port19").css("background","#e80808");
					} else {
						$("#port19").css("background","gray");
					}
					if (data.param20 == "空闲") {
					} else if (data.param20 == "使用") {
						$("#port20").css("background","#e80808");
					} else {
						$("#port20").css("background","gray");
					}
				}
			},
	        complete: function () {
	            $.bootstrapLoading.end();
	        }
		});
	} */
	
})
</script>