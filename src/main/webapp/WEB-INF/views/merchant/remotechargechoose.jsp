<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>选择下列参数进行充电</title>
<link rel="stylesheet" href="/css/base.css">
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<%-- <link href="${hdpath}/css/ui-choose.css" rel="stylesheet" /> --%>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/mui.min.css"/>
	<script type="text/javascript" src="${hdpath }/js/jquery-2.1.0.js"></script>
	<script type="text/javascript" src="${hdpath }/mui/js/mui.min.js"></script>
<%-- <script src="${hdpath}/js/ui-choose.js"></script> --%>
<script src="${hdpath }/js/my.js"></script>
<!-- <style type="text/css">
.demo-table {
	margin: 1rem 0 1.5rem 0;
}

#notarize {
	float: right;
	margin-right: 2rem;
}
</style> -->
<!-- </head> -->
<%-- <body>
	<div class="demo-box">
		<table class="demo-table">
			<caption></caption>
			<tr>
				<td>
					<ul class="chargechoose">请选择端口</ul>
	        		<ul class="ui-choose" id="uc_01">
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
			<tr>
				<td style="height: 25px;"></td>
			</tr>
			<tr>
				<td>
					<ul class="chargechoose">请选择充电时间</ul>
					<ul class="ui-choose" id="uc_02">
						<c:forEach items="${templatelist}" var="temp">
							<li class="equtemp">${temp.name}<span style="display: none;">_${temp.id}</span></li>
						</c:forEach>
					</ul>
				</td>
			</tr>
		</table>
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
</script>
	<div class="container">
		<form action="${hdpath }/merchant/remotechargeaccess" method="post">
			<input type="hidden" id="code" name="code" value="${code}"> <input
				type="hidden" id="portchoose" name="portchoose" value=""> <input
				type="hidden" id="chargeparam" name="chargeparam" value="">
			<div>
				<input id="notarize" class="btn btn-success" type="button"
					value="确认提交">
			</div>
		</form>
		<div style="padding-top: 20px"></div>
		<hr>
		<p style="color: red">
			注意：<br /> 绿色端口为空闲端口，可使用。<br /> 红色端口为正在使用，可加钱。<br /> 灰色端口为端口故障，不可用。<br />
		</p>
		<nav class="navbar navbar-default navbar-fixed-bottom"
			role="navigation">
			<div align="center">
				<a href="${hdpath }/merchant/manage" class="btn btn-success">回管理页面</a>
			</div>
		</nav>
	</div>
</body>
<script type="text/javascript">
$(function() {
	$("#notarize").click(function() {
		var portchooseval = $("#portchoose").val();
		var chargeparamval = $("#chargeparam").val();
		if (portchooseval == null || portchooseval == "") {
			alert("充电端口未选择");
			return ;
		}
		if (chargeparamval == null || chargeparamval == "") {
			alert("充电时间未选择");
			return ;
		}
		$.bootstrapLoading.start({ loadingTips: "正在处理数据，请稍候..." });
		$.ajax({
			url : '${hdpath}/merchant/remotechargeaccess',
			data : {
				code : $("#code").val(),
				portchoose : portchooseval,
				chargeparam : chargeparamval,
			},
			type : "POST",
			cache : false,
			success : function(data) {
				if (data == "0") {
					alert("充电端口或时间未选择");
				} else if (data == "1") {
					alert('充电信息下发成功');
				}
			},//返回数据填充
			complete: function () {
                $.bootstrapLoading.end();
            },
		});
	})
	setTimeout(paystate1, 10); //页面加载完1秒后执行
	debugger;
	function paystate1() {
		$.ajax({
			url : "${hdpath}/portstate1",
			data : {
				code : $("#code").val(),
				nowtime : $("#nowtime").val()
			},
			type : "post",
			dataType : "json",
			beforeSend: function() {
				$.bootstrapLoading.start({ loadingTips: "正在获取端口状态..." });
		    },
			success : function(data) {
				if (data.state == "error") {
					mui.alert('获取失败，可能设备信号不稳定或网络模块供电异常', '错误', function() {
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
})
</script> --%>
<style>
		body {
			/*background-color: #fff;*/
			background-color: rgba(239,239,244,.4);
			/*rgba(239,239,244,.4)*/
		}
		.app {
			font-size: 14px;
			color: #666;
			padding: 15px 0 55px 0;
		}
		
		.app h5 {
			padding-left: 2%;
			color: #000;
			font-size: 16px;
		}
		.title {
			display: flex;
			justify-content: space-between;
			align-items: center;
			margin-bottom: 12px;
		}
		.app .portStatus {
			display: flex;
			justify-content: flex-end;
		}
		.app .portStatus .p_item {
			display: flex;
			align-items: center;
			margin-right: 15px;
		}
		.app .portStatus .p_item .square {
			width: 16px;
			height: 16px;
			box-shadow: 0 0 4px #999;
			border-radius: 2px;
			margin-right: 5px;
		}
		.app .portStatus .p_item .square.default {
			background-color: #fff;
		}
		.app .portStatus .p_item .square.use {
			background-color: #D43024;
			color: #fff !important;
		}
		.app .portStatus .p_item .square.fi {
			background-color: #A6A6A6;
			color: #fff !important;
		}
		.portList {
			margin-bottom:  25px;
		}
		.portList ul{
			display: flex;
			flex-wrap: wrap;
			padding: 0 1%;
		}
		.portList ul li {
			width: 17%;
			position: relative;
			padding-top: 17%;
			margin-left: 2.5%;
			margin-bottom: 2.5%
		}
		.portList ul li.use div{
			background-color: #D43024;
			color: #fff !important;
		}
		.portList ul li.fi div{
			background-color: #A6A6A6;
			color: #fff !important;
		}
		.portList ul li.select div{
			background-color: #22B14C;
			color: #fff !important;
		}
		.portList ul li div {
			position: absolute;
			left: 0;
			right: 0;
			bottom: 0;
			top: 0;
			box-sizing: border-box;
			background-color: #fff;
			box-shadow: 3px 3px 8px #dedede;
			border-radius: 7px;
			display: flex;
			justify-content: center;
			align-items: center;
			font-size: 18px;
			color: #000;
			border: 0.5px solid #efefef;
		}
	/*	.portList ul li:nth-child(5n-4){
			margin-left: 0;
		}*/

		.chargeList ul {
			display: flex;
			flex-wrap: wrap;
			padding: 0 2.5%;
		}
		.chargeList ul li{
			width: 30%;
			background-color: #999;
			height: 40px;
			line-height: 38px;
			text-align: center;
			border-radius: 4px;
			border: 1px solid #ddd;
			background-color: #f5f7fa;
			margin-bottom: 2.5%;
			margin-left: 2.5%;
			overflow: hidden;
			white-space: nowrap;
			text-overflow: ellipsis;
		}
		.chargeList ul li.select {
			background-color: #22B14C;
			color: #fff !important;
		}
		.submitBtn {
			text-align: center;
			margin-top: 20px;
			padding: 0 5%;
		}
		.submitBtn button {
			width: 100%;
			height: 40px;
			background-color: #22B14C;
			color: #fff;
		}
		.submitBtn button:active {
			background-color: #1D9E43;
		}
		.mui-bar {
			display: flex;
			justify-content: center;
			align-items: center;
		}
		.mui-bar button {
			width: 75%;
			background-color: #22B14C;
			color: #fff;
			height: 35px;
			border-radius: 35px;
		}
		.mui-bar button:active {
			background-color: #1D9E43;
		}
	</style>
</head>
<body>
	<input type="hidden" value="${code}" id="code">
	<input type="hidden" value="" id="port">
	<input type="hidden" value="" id="tem">
	<div class="app">
		<div class="title">
			<h5>请选择端口</h5>
			<div class="portStatus">
				<div class="p_item">
					<div class="square default"></div>
					<span>空闲</span>
				</div>
				<div class="p_item">
					<div class="square use"></div>
					<span>占用</span>
				</div>
				<div class="p_item">
					<div class="square fi"></div>
					<span>故障</span>
				</div>
			</div>
		</div>
		<div class="portList">
		<c:choose>
			<c:when test="${hardversion == '05'}">
			<ul>
				<li>
					<div>1号</div>
				</li>
				<li>
					<div>2号</div>
				</li>
				<li>
					<div>3号</div>
				</li>
				<li>
					<div>4号</div>
				</li>
				<li>
					<div>5号</div>
				</li>
				<li>
					<div>6号</div>
				</li>
				<li>
					<div>7号</div>
				</li>
				<li>
					<div>8号</div>
				</li>
				<li>
					<div>9号</div>
				</li>
				<li>
					<div>10号</div>
				</li>
				<li>
					<div>11号</div>
				</li>
				<li>
					<div>12号</div>
				</li>
				<li>
					<div>13号</div>
				</li>
				<li>
					<div>14号</div>
				</li>
				<li>
					<div>15号</div>
				</li>
				<li>
					<div>16号</div>
				</li>
			</ul>
			</c:when>
			<c:when test="${hardversion == '06'}">
			<ul>
				<li>
					<div>1号</div>
				</li>
				<li>
					<div>2号</div>
				</li>
				<li>
					<div>3号</div>
				</li>
				<li>
					<div>4号</div>
				</li>
				<li>
					<div>5号</div>
				</li>
				<li>
					<div>6号</div>
				</li>
				<li>
					<div>7号</div>
				</li>
				<li>
					<div>8号</div>
				</li>
				<li>
					<div>9号</div>
				</li>
				<li>
					<div>10号</div>
				</li>
				<li>
					<div>11号</div>
				</li>
				<li>
					<div>12号</div>
				</li>
				<li>
					<div>13号</div>
				</li>
				<li>
					<div>14号</div>
				</li>
				<li>
					<div>15号</div>
				</li>
				<li>
					<div>16号</div>
				</li>
				<li>
					<div>17号</div>
				</li>
				<li>
					<div>18号</div>
				</li>
				<li>
					<div>19号</div>
				</li>
				<li>
					<div>20号</div>
				</li>
			</ul>
			</c:when>
			<c:when test="${hardversion == '02'}">
			<ul>
				<li>
					<div>1号</div>
				</li>
				<li>
					<div>2号</div>
				</li>
			</ul>
			</c:when>
			<c:otherwise>
			<ul>
				<li>
					<div>1号</div>
				</li>
				<li>
					<div>2号</div>
				</li>
				<li>
					<div>3号</div>
				</li>
				<li>
					<div>4号</div>
				</li>
				<li>
					<div>5号</div>
				</li>
				<li>
					<div>6号</div>
				</li>
				<li>
					<div>7号</div>
				</li>
				<li>
					<div>8号</div>
				</li>
				<li>
					<div>9号</div>
				</li>
				<li>
					<div>10号</div>
				</li>
			</ul>
			</c:otherwise>
		</c:choose>
		</div>
		<div class="title">
			<h5>请选择充电时间</h5>
		</div>
		<div class="chargeList">
			<ul>
				<c:forEach items="${templatelist}" var="temp">
					<li data-id="${temp.id}">
						<div>${temp.name}</div>
					</li>
				</c:forEach>
			</ul>
		</div>
		<div class="submitBtn">
			<button>确认远程充电</button>
		</div>
	</div>
	<nav class="mui-bar mui-bar-tab">
		<button id="goBack">返回上一页</button>
	</nav>
<script>
$(function(){
	// 选择端口充电
	$('.portList li').on('click',function(){
		var result= chenckPort(this,1)
		if(result instanceof Array){
			return mui.alert(result[1],'提示','我知道了')
		}
		$('#port').val(result)
	})
	// 选择充电模板
	$('.chargeList li').on('click',function(){
		var id= chenckPort(this,2)
		$('#tem').val(id)
	})
	// 点击远程充电
	$('.submitBtn button').on('click',function(){
		var code= $('#code').val().trim()
		var port= $('#port').val().trim()
		var tem= $('#tem').val().trim()
		if(port == ''){
			return mui.alert('请先选择充电端口','提示','我知道了')
		}
		if(tem == ''){
			return mui.alert('请先选择充电模板','提示','我知道了')
		}
		$.bootstrapLoading.start({ loadingTips: "正在处理数据，请稍候..." });
		$.ajax({
			url: '${hdpath}/merchant/remotechargeaccess',
			data : {
				code : code,
				portchoose : port,
				chargeparam : tem,
			},
			type : "POST",
			cache : false,
			success : function(data) {
				if (data == "0") {
					mui.alert('充电端口或时间未选择','提示','我知道了')
				} else if (data == "1") {
					mui.alert('充电信息下发成功','提示','我知道了')
				}
			},//返回数据填充
			complete: function () {
                $.bootstrapLoading.end();
            },
		})

	})

	function chenckPort(target,type){
		var $tar= $(target)
		$tar.siblings().removeClass('select')
		if($tar.hasClass('fi')){
			return [ $tar.index()+1,'当前端口为故障端口' ]
		}else if($tar.hasClass('use')){
			return [ $tar.index()+1,'当前端口为故障已被占用' ]
		}
		$tar.addClass('select')
		return  type== 1 ? $tar.index()+1 : $tar.attr('data-id').trim() 
	}
	setTimeout(paystate1, 10); //页面加载完10后执行
	function paystate1() {
		$.bootstrapLoading.start({ loadingTips: "正在获取端口状态..." });
		$.ajax({
			url : "${hdpath}/portstate1",
			data : {
				code : $('#code').val().trim(),
				nowtime : new Date().getTime()
			},
			type : "post",
			dataType : "json",
			success : function(data) {
				if (data.state == "error") {
					mui.alert('获取失败，可能设备信号不稳定或网络模块供电异常', '错误', function() {
				});
				} else {
					var reg= /^\w+(\d+)$/
					var arr1= ['空闲','使用','故障']
					var arr2= ['','use','fi']
					for(var key in data){
						if(reg.test(key)){
							var port= parseInt(key.match(reg)[1])
							var $port= $('.portList li').eq(port-1)
							if(data[key] == '空闲'){

							}else if(data[key] == '使用'){
								$port.addClass('use')
							}else{
								$port.addClass('fi')
							}
						}
					}
				}
			},
	        complete: function () {
	            $.bootstrapLoading.end();
	        }
		});
	}

	$('#goBack').on('click',function(){
		window.history.go(-1)
	})
})
</script>
</body>
</html>