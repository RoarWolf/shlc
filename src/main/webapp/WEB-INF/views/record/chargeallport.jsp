<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<style type="text/css">
html,body{width: 100%;height: 100%}
.demo-table{margin: 1rem 0 1.5rem 0;}
.ui-choose .equtemp{ width: 98%; margin: 0.1rem 0 0 0.1rem;}
#chargechoose{margin: 0; padding: 0 0 0 0.1rem;}
#submitbtn{float: right;  margin-right: 2rem;}
hr {
    margin-top: 0px;
    margin-bottom: 0px;
    border: 0;
    border-top: 1px solid #eee;
}
/* #submitbtn {
	background-color: #44b549;
} */
</style>
</head>
<body id="body" data-hardversion="${hardversion}">
	<c:if test="${brandname != null }">
		<div align="center" style="font-size: 15px" >
			欢迎使用${brandname}电动车充电设备
		</div>
	</c:if>
	<div align="center" style="font-size: 15px">
		<font>当前设备编号为：${code }</font>
	</div>
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
	var name = item.text().replace('号', '');
	$('input[name="portchoose"]').val(name)
}
var uc_02 = $('#uc_02').data('ui-choose'); 
uc_02.click = function(index, item){
	var id = item.text().split("_")[1];
	$('input[name="chargeparam"]').val(id)
}
$(function() {
	var defaultval = $("#defaultindex").val();
	uc_02._val_ul(defaultval);
})
</script>
<div class="container">
		<input type="hidden" id="nowtime" value="${nowtime}">
	<form id="payfrom" action="${hdpath }/charge/choosepay" method="post">
		<input type="hidden" id="code" name="code" value="${code}">
		<input type="hidden" id="openid" name="openid" value="${openid}">
		<input type="hidden" id="portchoose" name="portchoose" value="">
		<input type="hidden" id="chargeparam" name="chargeparam" value="${defaultchoose }">
		<div>
			<button class="btn btn-success" id="submitbtn">确认付款</button>
		</div>
	</form>
	<div style="padding-top:35px"></div>
	<hr>
	<p style="color: #e80808">
		注意：<br/>
			白色端口为空闲端口，可使用。<br/>
			红色端口为正在使用，可加钱。<br/>
			灰色端口为端口故障，不可用。<br/>
	</p>
	<p style="color: black;">
		友情提醒： <br>
		选择的充电时间为小功率电动车充电时间,仅供参考。大功率电动车充电时间智能动态计算，以实际为准。
	</p>
	<hr/>
	<div style="font-size: 14px;">
		<span>如有疑问，敬请联系：</span>
		<a href="tel:${phonenum }">${phonenum }</a>
	</div>
</div>
<script src="${hdpath }/js/my.js"></script>
<script src="${pageContext.request.contextPath }/mui/js/mui.min.js"></script>
</body>
</html>
<script>

$(function() {
	function handlePay(){
		//var forbidport= ${forbidport}
		var hardversion= $('body').attr('data-hardversion').trim()
		if(hardversion != '01'){
			return false
		}
		var forbidport= [].concat(${forbidport})
		var portNum=$('#uc_01 li.selected').index()+1 ;
		return forbidport.some(function(item,i){
			return parseInt(item) == portNum ;
		})
	};
	
	setTimeout(paystate1, 10); //0.01秒后执行
	function paystate1() {
		$.bootstrapLoading.start({ loadingTips: "正在连接..." });
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
	}
	$("#submitbtn").click(function() {
		if(handlePay()){
			mui.alert('此端口已被指定', '', function() {});
			return
		}
		
		var code = $("#code").val();
		var openid = $("#openid").val();
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
			mui.alert(portchoose + '号端口正在使用，如需续充可关注公众号"自助充电平台"，可查看正在充电信息，以及续充等操作', '提示', function() {
			});
			return false;
		} else if (wxpaycode == null || wxpaycode == "" || code == null || code == "") {
			mui.alert('退回操作，需重新扫码', '', function() {
			});
			WeixinJSBridge.call('closeWindow');
		} else {
			$(this).attr("disabled","disabled");
			$("#payfrom").submit();
		}
	})
})
</script>