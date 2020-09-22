<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Insert title here</title>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
</head>
<body>
	<div class="container">
		<input type="hidden" id="code" value="${code }">
		<div style="padding-top: 20px">
			<div class="radio" style="padding-top: 20px">
				<label> <input type="radio" name="portchoose" value="1">
					<button id="port1" class="btn btn-success" value="1">1号端口</button>
				</label>
			</div>
			<div class="radio" style="padding-top: 20px">
				<label> <input type="radio" name="portchoose" value="2">
					<button id="port2" class="btn btn-success" value="2">2号端口</button>
				</label>
			</div>
			<div class="radio" style="padding-top: 20px">
				<label> <input type="radio" name="portchoose" value="3">
					<button id="port3" class="btn btn-success" value="3">3号端口</button>
				</label>
			</div>
			<div class="radio" style="padding-top: 20px">
				<label> <input type="radio" name="portchoose" value="4">
					<button id="port4" class="btn btn-success" value="4">4号端口</button>
				</label>
			</div>
			<div class="radio" style="padding-top: 20px">
				<label> <input type="radio" name="portchoose" value="5">
					<button id="port5" class="btn btn-success" value="5">5号端口</button>
				</label>
			</div>
			<div class="radio" style="padding-top: 20px">
				<label> <input type="radio" name="portchoose" value="6">
					<button id="port6" class="btn btn-success" value="6">6号端口</button>
				</label>
			</div>
			<div class="radio" style="padding-top: 20px">
				<label> <input type="radio" name="portchoose" value="7">
					<button id="port7" class="btn btn-success" value="7">7号端口</button>
				</label>
			</div>
			<div class="radio" style="padding-top: 20px">
				<label> <input type="radio" name="portchoose" value="8">
					<button id="port8" class="btn btn-success" value="8">8号端口</button>
				</label>
			</div>
			<div class="radio" style="padding-top: 20px">
				<label> <input type="radio" name="portchoose" value="9">
					<button id="port9" class="btn btn-success" value="9">9号端口</button>
				</label>
			</div>
			<div class="radio" style="padding-top: 20px">
				<label> <input type="radio" name="portchoose" value="10">
					<button id="port10" class="btn btn-success" value="10">10号端口</button>
				</label>
			</div>
		</div>
		<div style="padding-top: 30px"></div>
	</div>
</body>
</html>
<script>
$(function() {
	setInterval(paystate, 1000); //每1秒刷新一次
	debugger;
	function paystate() {
		$.ajax({
			url : "${hdpath}/portstate",
			data : {
				code : $("#code").val()
			},
			type : "post",
			dataType : "json",
			success : function(data) {
				if (data.param1 == "端口空闲") {
					$("#port1").css("background","green");
				} else if (data.param1 == "端口正在使用") {
					$("#port1").css("background","red");
				} else {
					$("#port1").css("background","gray");
				}
				if (data.param2 == "端口空闲") {
					$("#port2").css("background","green");
				} else if (data.param2 == "端口正在使用") {
					$("#port2").css("background","red");
				} else {
					$("#port2").css("background","gray");
				}
				if (data.param3 == "端口空闲") {
					$("#port3").css("background","green");
				} else if (data.param3 == "端口正在使用") {
					$("#port3").css("background","red");
				} else {
					$("#port3").css("background","gray");
				}
				if (data.param4 == "端口空闲") {
					$("#port4").css("background","green");
				} else if (data.param4 == "端口正在使用") {
					$("#port4").css("background","red");
				} else {
					$("#port4").css("background","gray");
				}
				if (data.param5 == "端口空闲") {
					$("#port5").css("background","green");
				} else if (data.param5 == "端口正在使用") {
					$("#port5").css("background","red");
				} else {
					$("#port5").css("background","gray");
				}
				if (data.param6 == "端口空闲") {
					$("#port6").css("background","green");
				} else if (data.param6 == "端口正在使用") {
					$("#port6").css("background","red");
				} else {
					$("#port6").css("background","gray");
				}
				if (data.param7 == "端口空闲") {
					$("#port7").css("background","green");
				} else if (data.param7 == "端口正在使用") {
					$("#port7").css("background","red");
				} else {
					$("#port7").css("background","gray");
				}
				if (data.param8 == "端口空闲") {
					$("#port8").css("background","green");
				} else if (data.param8 == "端口正在使用") {
					$("#port8").css("background","red");
				} else {
					$("#port8").css("background","gray");
				}
				if (data.param9 == "端口空闲") {
					$("#port9").css("background","green");
				} else if (data.param9 == "端口正在使用") {
					$("#port9").css("background","red");
				} else {
					$("#port9").css("background","gray");
				}
				if (data.param10 == "端口空闲") {
					$("#port10").css("background","green");
				} else if (data.param10 == "端口正在使用") {
					$("#port10").css("background","red");
				} else {
					$("#port10").css("background","gray");
				}
			}
		});
	}
	
})
</script>