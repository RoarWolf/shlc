<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${code}设备状态</title>
<link rel="stylesheet" href="/css/base.css">
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/mui.min.css"/>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="${hdpath }/mui/js/mui.min.js"></script>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script src="${hdpath }/js/my.js"></script>
<style type="text/css">
html,body {
	width:100%;
	height: 100%;
}
body{
	background-color: #efeff4;
	overflow: auto;
}
.app {
	padding: 15px 0 70px;
}
.container-fluid {
    padding-right: 10px;
    padding-left: 10px;
}
.title_h1 {
	font-size: 14px;
}
table {
	width: 100%;
	/* table-layout:fixed; */
	border-left: 1px solid #add9c0;
	border-top: 1px solid #add9c0;
	border-radius: 4px;
	overflow: hidden;
	margin-top: 7px;
	border-collapse: inherit;
}
table thead {
	background-color: #C8EFD4;
	font-weight: bold;
}
table thead td {
	height: 41px;
	border-bottom: 1px solid #add9c0;
	border-right: 1px solid #add9c0;
	text-align: center;
	font-size: 12px;
	padding: 3px 0;
}
table tbody{
	background-color: #f5f7fa;
}
table tbody td {
	text-align: center;
	height: 35px;
	border-bottom: 1px solid #add9c0;
	border-right: 1px solid #add9c0;
	font-size: 12px;
	/* overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap; */
}
table tbody td  button {
	padding: 3px 10px !important;
}
table tbody td input {
 	height: 28px;
    border-radius: 5px;
    outline: none;
    border: 1px solid #ccc;
    text-align: center;
    color: #666;
    font-size: 12px;
}
.goBack {
	text-align: center;
	height: 50px;
	line-height: 50px;
}
.goBack a {
	width:75%;
}
.remoteCharge {
	table-layout: fixed;
}
.remoteCharge , .remoteCharge tbody, .remoteCharge thead .remoteCharge tr {
	width:100%;
}
.remoteCharge tr td {
	width: 20%;
	position: relative;
}
.remoteCharge tr td .mui-input-row {
	width: 100%;
	position: absolute;
	left: 50%;
	top: 50%;
	transform: translate(-50%,-50%);
	margin: 0;
}
.remoteCharge tr td input {
	width:80%;
	-webkit-appearance: none;
	height: 28px;
	line-height: 28px;
	margin: 0 !important;
	padding: 10px 0 !important;
}
table tbody td a.disabled {
	color: rgba(51,122,183,.5);
}
table tbody td a.disabled:active,
table tbody td a.disabled: {
	text-decoration: none;
}
.alarmStatus tr td:nth-child(2) {
	width: 15%;
}
</style>
</head>
<!-- 端口数量 -->
<body > 
	<div class='app'>
		<input type="hidden" id="code" value="${code }">
		<div class="container-fluid">
			<div class="title_h1">设备端口状态</div>
			<div>
				<table>
					<thead>
						<tr >
							<td><div>端口号</div></td>
							<td><div>端口状态</div></td>
							<td>
								<div>充电时间</div>
								<div>（分钟）</div>
							</td>
							<td>
								<div>充电功率</div>
								<div>（W）</div>
							</td>
							<td>
								<div>剩余电量</div>
								<div>（度）</div>
							</td>
							<!-- <td><font size="1px">锁定</font></td>
							<td><font size="1px">解锁</font></td> -->
							<td><div>操作</div></td>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${allPortStatusList }" var="codeport">
							<tr align="center" id="portstate${codeport.port }" class="portstate">
								<td>${codeport.port }</td>
								<td>${codeport.portStatus == 2 ? "使用" : codeport.portStatus == 1 ? "空闲" : "故障" }</td>
								<td>${codeport.time }</td>
								<td>${codeport.power }</td>
								<td>${codeport.elec / 100 }</td>
								<%-- <td style="font-size: 1px">
									<button class="btn btn-info btn-sm" id="lock${codeport.port }" value="0">锁定</button>
								</td>
								<td style="font-size: 1px" id="">
									<button class="btn btn-info btn-sm" id="debloack${codeport.port }" value="1">解锁</button>
								</td> --%>
								<td style="font-size: 1px">
									<button class="btn btn-success btn-sm"  value="${codeport.port}">更新</button>
								</td>
							</tr>
						</c:forEach>
						<tr class="last_tr">
							<td colspan="9">数据更新时间：${updateTime }</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div style="height: 25px;"></div>
			<div class="title_h1">设备远程充电/断电</div>
			<div>
				<table class="remoteCharge">
					<thead>
						<tr>
							<td><div>端口号</div></td>
							<td><div>充电时间</div><div>（分钟）</div></td>
							<td><div>充电电量</div><div>（度）</div></td>
							<td><div>远程充电</div></td>
							<td><div>远程断电</div></td>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${allPortStatusList }" var="codeport">
							<tr>
								<td>
									<div class="mui-input-row">
								        <%-- <input type="text"   placeholder="" value="1" data-port_length="${fn:length(allPortStatusList)}"> --%>
								    	<div class="portNum">${codeport.port}</div>
								    </div>
							    </td>
								<td>
									<div class="mui-input-row">
								        <input type="text" class="time" placeholder="" value="240">
								    </div>
								</td>
								<td>
									<div class="mui-input-row">
								        <input type="text" class="power" placeholder="" value="1">
								    </div>
								</td>
								<td><button class="btn btn-success btn-sm payport"  >开始</button></td>
								<td><button class="btn btn-success btn-sm stopport" >断电</button></td>
							</tr>
						</c:forEach>
						<%-- <c:forEach items="${allPortStatusList }" var="codeport">
							<tr>
								<td>${codeport.port }</td>
								<td><input type="text" id="chargetime${codeport.port }" value="240" size="5"></td>
								<td><input type="text" id="chargeelec${codeport.port }" value="1.0" size="5"></td>
								<td><button class="btn btn-success btn-sm" id="payport${codeport.port }">开始</button></td>
								<td><button class="btn btn-success btn-sm" id="stopport${codeport.port }">断电</button></td>
							</tr>
						</c:forEach> --%>
					</tbody>
				</table>
			</div>
			<!-- 设置报警系统 -->
			<div style="height: 25px;"></div>
			<div class="title_h1">设备报警系统状态</div>
			<div>
				<table class="alarmStatus">
					<thead>
						<tr>
							<td><div>类型</div></td>
							<td><div>值</div></td>
							<td><div>更新</div></td>
							<td><div>更新时间</div></td>
							<td><div>历史</div></td>
						</tr>
					</thead>
					<tbody>
						<tr data-type="1">
							<td>
								<div class="mui-input-row">
							    	<div>当前温度</div>
							    </div>
						    </td>
						    <td>
								<div class="mui-input-row">
							        <span>96℃</span>
							    </div>
						    </td>
						    <td><button class="btn btn-success btn-update">更新</button></td>
							<td><span class="update_time">2020-03-06 15:15:13</span></td>
							<td><a href="/equipment/histemper">查看历史</a></td>
						</tr>
						<tr data-type="2">
							<td>
								<div class="mui-input-row">
							    	<div>当前烟感</div>
							    </div>
						    </td>
						    <td>
								<div class="mui-input-row">
							        <span>96</span>
							    </div>
						    </td>
						    <td><button class="btn btn-success btn-update">更新</button></td>
							<td><span class="update_time">2020-03-06 15:15:13</span></td>
							<td><a href="javascript:void(0);" class="disabled">查看历史</a></td>
						</tr>
						<tr data-type="3">
							<td>
								<div class="mui-input-row">
							    	<div>当前总功率</div>
							    </div>
						    </td>
						    <td>
								<div class="mui-input-row">
							        <span>96W</span>
							    </div>
						    </td>
						    <td><button class="btn btn-success btn-update">更新</button></td>
							<td><span class="update_time">2020-03-06 15:15:13</span></td>
							<td><a href="javascript:void(0);" class="disabled">查看历史</a></td>
						</tr>
					</tbody>
				</table>
			</div>
			<nav class="navbar navbar-default navbar-fixed-bottom"
				role="navigation">
				<div class="goBack">
					<a href="javascript:void(0)" class="btn btn-success" >返回</a>
				</div>
			</nav>
		</div>
	</div>
</body>
<script>
	/* $(function(){
	    pushHistory();
	    window.addEventListener("popstate", function(e) {
			location.replace('/equipment/list?wolfparam=2');
		}, false);
	    function pushHistory() {
	        var state = {
	            title: "title",
	            url: "#"
	        };
	        window.history.pushState(state, "title", "#");
	    }
	}); */
	Date.prototype.Formit= function(fmt){
		var o= {
			'M+': this.getMonth()+1,
			'D+': this.getDate(),
			'h+': this.getHours(),
			'm+': this.getMinutes(),
			's+': this.getSeconds()
		}
		if(typeof fmt == 'undefined'){
			return this.Formit('YYYY-MM-DD hh:mm:ss')
		}
		var reg= /(Y+)/g
		if(reg.test(fmt)){
			fmt= fmt.replace(/(Y+)/g,this.getFullYear().toString().substr(4-RegExp.$1.length))
		}
		for(var key in o){
			var regKey= new RegExp('('+key+')','g')
			if(regKey.test(fmt)){	
				fmt= RegExp.$1.length==2 ?  fmt.replace(regKey,('00'+o[key]).substr(-2)) : fmt.replace(regKey,o[key]+'') 
			}
		}
		
		return fmt	
		
	}
</script>
<script>
	$(function() {
		$(".portstate button").click(function() {
			var portval = $(this).val(); //端口号
			var that= this
			$.bootstrapLoading.start({ loadingTips: "正在更新..." });
			$.ajax({
				url : '${hdpath}/querystate',
				data : {
					port : portval,
					code : $("#code").val()
				},
				type : "POST",
				cache : false,
				success : function(data) {
					if (data.wolfcode == "1001") {
						mui.confirm(portval+'端口，更新失败！')
					} else {
						var portStatusStr = '';
						if (data.portstatus == 1) {
							portStatusStr = "空闲";
						} else if (data.portstatus == 2) {
							portStatusStr = "使用";
						} else {
							portStatusStr = "故障";
						}
						$(that).parents('.portstate').find('td').eq(1).text(portStatusStr)
						$(that).parents('.portstate').find('td').eq(2).text(data.time)
						$(that).parents('.portstate').find('td').eq(3).text(data.power)
						$(that).parents('.portstate').find('td').eq(4).text(data.elec / 100)
						$('.last_tr td').text("数据更新时间："+fmt(parseInt(data.updatetime)))
					}
				},//返回数据填充
		        complete: function () {
		            $.bootstrapLoading.end();
		        }
			});
		})
		$("button[id^='lock']").click(function() {
			var wolf = $(this).parent().prev().prev().prev().prev()
					.prev().prev().text();
			$.bootstrapLoading.start({ loadingTips: "正在锁定端口，请稍后..." });
			$.ajax({
				url : '${hdpath}/lock',
				data : {
					port : wolf,
					status : $(this).val(),
					code : $("#code").val()
				},
				type : "POST",
				cache : false,
				success : function(data) {
					if (data.err == "0") {
						alert(data.errinfo);
					} else if (data.err == "1") {
						alert(data.errinfo);
					} else {
						var portStatusStr = "";
						var portStatusHtml = $("#portstate" + wolf).text();
						if(data.status == "0") {
							portStatusStr = "锁定";
						} else if (portStatusHtml == "锁定") {
							portStatusStr = "空闲";
						}
						$("#portstate" + wolf).html(portStatusStr);
					}
				},//返回数据填充
				complete: function () {
		            $.bootstrapLoading.end();
		        }
			});
		})
		$("button[id^='debloack']").click(function() {
			var wolf = $(this).parent().prev().prev().prev().prev()
					.prev().prev().prev().text();
			$.bootstrapLoading.start({ loadingTips: "正在锁定端口，请稍后..." });
			$.ajax({
				url : '${hdpath}/lock',
				data : {
					port : wolf,
					status : $(this).val(),
					code : $("#code").val()
				},
				type : "POST",
				cache : false,
				success : function(data) {
					if (data.err == "0") {
						alert(data.errinfo);
					} else if (data.err == "1") {
						alert(data.errinfo);
					} else {
						var portStatusStr = "";
						var portStatusHtml = $("#portstate" + wolf).text();
						if(data.status == "1") {
							portStatusStr = "空闲";
						} else if (portStatusHtml == "锁定") {
							portStatusStr = "空闲";
						}
						$("#portstate" + wolf).html(portStatusStr);
					}
				},//返回数据填充
				complete: function () {
		            $.bootstrapLoading.end();
		        }
			});
		})
		
		/* 远程充电 */
		$(".payport").click(function() {
			var payportval =parseInt($(this).parents('tr').find('.portNum').text());
			var timeval = $(this).parents('tr').find('.time').val().trim();
			var elecval = $(this).parents('tr').find('.power').val().trim();
			$.bootstrapLoading.start({ loadingTips: "正在设置远程充电..." });
			$.ajax({
				url : '${hdpath}/testpaytoport',
				data : {
					payport : payportval,
					time : timeval,
					elec : elecval,
					code : $("#code").val()
				},
				type : "POST",
				cache : false,
				success : function(res) {
					if(res.wolfcode == 1000){
						$('#portstate' + payportval).find('td').eq(1).text('使用');
						mui.confirm(payportval+'端口，远程充电成功！')
					}else{
						mui.confirm(payportval+'端口，远程充电失败！')
					}
					
				},//返回数据填充
				complete: function(){
					$.bootstrapLoading.end()
				}
			});
		})
		/* 远程断电 */
		$(".stopport").click(function() {
			var stopportval = parseInt($(this).parents('tr').find('.portNum').text());
			$.bootstrapLoading.start({ loadingTips: "正在远程断电..." });
			$.ajax({
				url : '${hdpath}/stopRechargeByPort',
				data : {
					port : stopportval,
					code : $("#code").val()
				},
				type : "POST",
				cache : false,
				success : function(res) {
					if(res.wolfcode == 1000){
						$('#portstate' + stopportval).find('td').eq(1).text('空闲');
						mui.confirm(stopportval+'端口，远程断电成功！')
					}else{
						mui.confirm(stopportval+'端口，远程断电失败！')
					}
				},//返回数据填充
				complete: function(){
					$.bootstrapLoading.end()
				}
			});
		})
		
		$('.goBack').click(function(){
			window.history.go(-1)
		})
		
		/* 格式化时间 */
	
		function fmt(date){
		    return new Date(date).Formit() 		
		}
		
		/*更新设备报警状态*/
		$('.btn-update').on('click',function(e){
			var parTr= $(this).parents('tr')
			var type= parTr.attr('data-type')
			$.ajax({
				url: '/getDeviceNowArgument',
				type: 'post',
				data: {
					code: $("#code").val(),
					type: type
				},
				success: function(res){
					colsole.log(res)
				},
				error: function(err){
					console.log(err)
				}
			})
		})
		
		
	})
</script>
</html>