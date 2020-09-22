<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设备未绑定</title>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<link href="${hdpath}/css/ui-choose.css" rel="stylesheet" />
<script src="${hdpath}/js/jquery.js"></script>
<script src="${hdpath}/js/ui-choose.js"></script>
<style type="text/css">
.demo-table {
	margin: 1rem 0 1.5rem 0;
	width: 100%;
}
.demo-table	tbody,.demo-table tbody>tr{
	width: 100%;
}
.demo-table tbody>tr>td {
	padding: 0 10px;
}

.ui-choose .equtemp {
	width: 98%;
	margin: 0.1rem 0 0 0.1rem;
}

#chargechoose {
	margin: 0;
	padding: 0 0 0 0.1rem;
}

#submitbtn {
	float: right;
	margin-right: 2rem;
}
ul.ui-choose>li {
	margin-top: 10px;
}
ul.ui-choose>li {
	min-width: auto;
}

hr {
	margin-top: 5px;
	margin-bottom: 0px;
	border: 0;
	border-top: 1px solid #eee;
}
.width_50 { /*这个是百分之50,一行占2个*/
	min-width: 50% !important;
	margin: 0 !important;
	margin-top: 10px !important;
}
.width_20 { /*这个是百分之20,一行占5个*/
	min-width: 20% !important;
	margin: 0 !important;
	margin-top: 10px !important;
}
#commitBtn {
	display: flex;
	justify-content: space-around;
}
#commitBtn button {
	margin: 0; 
}
</style>
</head>
<body>
	<div align="center">
		<font size="4px" style="color: #666; font-size: 16px;">当前设备编号为：${code }</font>
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
								<li class="width_20" id="port1">1号</li>
								<li class="width_20" id="port2">2号</li>
								<li class="width_20" id="port3">3号</li>
								<li class="width_20" id="port4">4号</li>
								<li class="width_20" id="port5">5号</li>
								<li class="width_20" id="port6">6号</li>
								<li class="width_20" id="port7">7号</li>
								<li class="width_20" id="port8">8号</li>
								<li class="width_20" id="port9">9号</li>
								<li class="width_20" id="port10">10号</li>
								<li class="width_20" id="port11">11号</li>
								<li class="width_20" id="port12">12号</li>
								<li class="width_20" id="port13">13号</li>
								<li class="width_20" id="port14">14号</li>
								<li class="width_20" id="port15">15号</li>
								<li class="width_20" id="port16">16号</li>
							</c:when>
							<c:when test="${hardversion == '06' || hardversion == '10'}">
								<li class="width_20" id="port1">1号</li>
								<li class="width_20" id="port2">2号</li>
								<li class="width_20" id="port3">3号</li>
								<li class="width_20" id="port4">4号</li>
								<li class="width_20" id="port5">5号</li>
								<li class="width_20" id="port6">6号</li>
								<li class="width_20" id="port7">7号</li>
								<li class="width_20" id="port8">8号</li>
								<li class="width_20" id="port9">9号</li>
								<li class="width_20" id="port10">10号</li>
								<li class="width_20" id="port11">11号</li>
								<li class="width_20" id="port12">12号</li>
								<li class="width_20" id="port13">13号</li>
								<li class="width_20" id="port14">14号</li>
								<li class="width_20" id="port15">15号</li>
								<li class="width_20" id="port16">16号</li>
								<li class="width_20" id="port17">17号</li>
								<li class="width_20" id="port18">18号</li>
								<li class="width_20" id="port19">19号</li>
								<li class="width_20" id="port20">20号</li>
							</c:when>
				        	<c:when test="${hardversion == '02' || hardversion == '09'}">
				        		<li class="width_50" id="port1">1号</li>
				        		<li class="width_50" id="port2">2号</li>
				        	</c:when>
				        	<c:when test="${allPortSize == null || allPortSize == 0 || hardversion == '01'}">
								<li class="width_20" id="port1">1号</li>
								<li class="width_20" id="port2">2号</li>
								<li class="width_20" id="port3">3号</li>
								<li class="width_20" id="port4">4号</li>
								<li class="width_20" id="port5">5号</li>
								<li class="width_20" id="port6">6号</li>
								<li class="width_20" id="port7">7号</li>
								<li class="width_20" id="port8">8号</li>
								<li class="width_20" id="port9">9号</li>
								<li class="width_20" id="port10">10号</li>
				        	</c:when>
				        	<c:when test="${allPortSize == 2 || hardversion != '01'}">
				        		<c:forEach items="${allPortStatusList }" var="allport">
				        			<li class="width_20" id="port${allport.port }">${allport.port }号</li>
				        		</c:forEach>
				        	</c:when>
				        	<c:otherwise>
				        		<li class="width_20" id="port1">1号</li>
								<li class="width_20" id="port2">2号</li>
								<li class="width_20" id="port3">3号</li>
								<li class="width_20" id="port4">4号</li>
								<li class="width_20" id="port5">5号</li>
								<li class="width_20" id="port6">6号</li>
								<li class="width_20" id="port7">7号</li>
								<li class="width_20" id="port8">8号</li>
								<li class="width_20" id="port9">9号</li>
								<li class="width_20" id="port10">10号</li>
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
					<ul id="chargechoose">请选择充电时间</ul>
					<ul class="ui-choose" id="uc_02">
						<c:choose>
							<c:when test="${hardversion == '02'}">
								<c:forEach items="${templateLists}" var="temp">
									<li class="equtemp">${temp.name}<span style="display: none;">_${temp.id}</span></li>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<c:forEach items="${templatelist}" var="temp">
								    <c:if test="${hardversion != '08'}">
								    	<li class="equtemp">${temp.name}<span style="display: none;">_${temp.id}</span></li>
								    </c:if>
									<c:if test="${hardversion == '08' && temp.type == 2}">
								    	<li class="equtemp">${temp.name}<span style="display: none;">_${temp.id}</span></li>
								    </c:if>
								</c:forEach>
							</c:otherwise>
						</c:choose>
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
		uc_02.click = function(index, item) {
			var id = item.text().split("_")[1];
			$('input[name="chargeparam"]').val(id)
		}
		$(function() {
			var defaultval = $("#defaultindex").val();
			if (defaultval == null || defaultval == "") {
				uc_02._val_ul(1);
			} else {
				uc_02._val_ul(defaultval);
			}
		})
	</script>
	<div class="container">
		<input hidden="existuser" value="${existuser }">
		<input hidden="hardversion" id="hardversion" value="${hardversion }">
		<form method="post">
			<input type="hidden" id="code" name="code" value="${code}"> 
			<input type="hidden" id="wxpaycode" name="wxpaycode" value="${wxpaycode}">
			<input type="hidden" id="portchoose" name="portchoose" value="">
			<input type="hidden" id="chargeparam" name="chargeparam" value="${defaultchoose }">
			<input type="hidden" id="several" name="several" value="${several}"> 
		</form>
		<div id="commitBtn">
			<a class="btn btn-info"
				href="${hdpath}/merchant/infoverdict?openid=${openid}&code=${code}&existuser=${existuser}">登陆绑定</a>
			<button class="btn btn-success" id="submitbtn">测试提交</button>
		</div>
		<div style="padding-top: 15px"></div>
		<hr>
		<p style="color: #e80808">
			注意：<br /> 白色端口为空闲端口，可使用。<br /> 红色端口为正在使用，可加钱。<br /> 灰色端口为端口故障，不可用。<br />
		</p>
		<hr />
	</div>
</body>
</html>
<script>
	$(function() {
		pushHistory();
		window.addEventListener("popstate", function(e) {
			WeixinJSBridge.call('closeWindow');
		}, false);
		function pushHistory() {
			var state = {
				title : "title",
				url : null
			};
			window.history.pushState(state, "title", null);
		}
	});
	$(function() {
		$("#submitbtn").click(function() {
			var portchooseval = $("#portchoose").val();
			var chargeparamval = $("#chargeparam").val();
			var several = Number($("#several").val()) + Number(1);
			var hardversion = $("#hardversion").val();
			console.log('hardversion===' + hardversion);
			if (several>10) {
				alert("测试次数超限，如需继续测试，请联系管理员");
				return false;
			} else if (portchooseval == null || portchooseval == "") {
				alert("充电端口未选择");
				return false;
			} else if (chargeparamval == null || chargeparamval == "") {
				alert("充电时间未选择");
				return false;
			} else {
				$.ajax({
					url : '${hdpath}/equipment/paytest',
					data : {
						code : $("#code").val(),
						portchoose : portchooseval,
						chargeparam : chargeparamval,
						several : several,
						hardversion : $("#hardversion").val(),
					},
					type : "POST",
					cache : false,
					success : function(data) {
						if (data == "success") {
							$("#several").val(several);
							alert("测试数据发送成功");
						} else {
							alert("测试数据发送失败");
						}
					},//返回数据填充
				});
			}
		})

		setInterval(paystate, 1000); //每1秒刷新一次
		//debugger;
		function paystate() {
			$.ajax({
				url : "${hdpath}/portstate",
				data : {
					code : $("#code").val()
				},
				type : "post",
				dataType : "json",
				success : function(data) {
					if (data.param1 == "空闲") {
					} else if (data.param1 == "使用") {
						$("#port1").css("background", "#e80808");
					} else {
						$("#port1").css("background", "gray");
					}
					if (data.param2 == "空闲") {
					} else if (data.param2 == "使用") {
						$("#port2").css("background", "#e80808");
					} else {
						$("#port2").css("background", "gray");
					}
					if (data.param3 == "空闲") {
					} else if (data.param3 == "使用") {
						$("#port3").css("background", "#e80808");
					} else {
						$("#port3").css("background", "gray");
					}
					if (data.param4 == "空闲") {
					} else if (data.param4 == "使用") {
						$("#port4").css("background", "#e80808");
					} else {
						$("#port4").css("background", "gray");
					}
					if (data.param5 == "空闲") {
					} else if (data.param5 == "使用") {
						$("#port5").css("background", "#e80808");
					} else {
						$("#port5").css("background", "gray");
					}
					if (data.param6 == "空闲") {
					} else if (data.param6 == "使用") {
						$("#port6").css("background", "#e80808");
					} else {
						$("#port6").css("background", "gray");
					}
					if (data.param7 == "空闲") {
					} else if (data.param7 == "使用") {
						$("#port7").css("background", "#e80808");
					} else {
						$("#port7").css("background", "gray");
					}
					if (data.param8 == "空闲") {
					} else if (data.param8 == "使用") {
						$("#port8").css("background", "#e80808");
					} else {
						$("#port8").css("background", "gray");
					}
					if (data.param9 == "空闲") {
					} else if (data.param9 == "使用") {
						$("#port9").css("background", "#e80808");
					} else {
						$("#port9").css("background", "gray");
					}
					if (data.param10 == "空闲") {
					} else if (data.param10 == "使用") {
						$("#port10").css("background", "#e80808");
					} else {
						$("#port10").css("background", "gray");
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
			});
		}
	})
</script>