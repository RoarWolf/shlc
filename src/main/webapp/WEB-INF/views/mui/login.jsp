<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html class="ui-page-login">
<head>
<meta name="viewport"
	content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登陆页面</title>
<link href="${pageContext.request.contextPath }/mui/css/mui.min.css"
	rel="stylesheet" />
<link href="${pageContext.request.contextPath }/mui/css/style.css"
	rel="stylesheet" />
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/jquery-2.1.0.js"></script>
<style>
.area {
	margin: 20px auto 0px auto;
}

.mui-input-group {
	margin-top: 10px;
}

.mui-input-group:first-child {
	margin-top: 20px;
}

.mui-input-group label {
	width: 22%;
}

.mui-input-row label ~input, .mui-input-row label ~select,
	.mui-input-row label ~textarea {
	width: 78%;
}

.mui-checkbox input[type=checkbox], .mui-radio input[type=radio] {
	top: 6px;
}

.mui-content-padded {
	margin-top: 25px;
}

.mui-btn {
	padding: 10px;
}

.link-area {
	display: block;
	margin-top: 25px;
	text-align: center;
}

.spliter {
	color: #bbb;
	padding: 0px 8px;
}

.oauth-area {
	position: absolute;
	bottom: 20px;
	left: 0px;
	text-align: center;
	width: 100%;
	padding: 0px;
	margin: 0px;
}

.oauth-area .oauth-btn {
	display: inline-block;
	width: 50px;
	height: 50px;
	background-size: 30px 30px;
	background-position: center center;
	background-repeat: no-repeat;
	margin: 0px 20px;
	/*-webkit-filter: grayscale(100%); */
	border: solid 1px #ddd;
	border-radius: 25px;
}

.oauth-area .oauth-btn:active {
	border: solid 1px #aaa;
}

.oauth-area .oauth-btn.disabled {
	background-color: #ddd;
}
</style>
</head>
<body>
	<header class="mui-bar mui-bar-nav">
		<h1 class="mui-title">登录</h1>
	</header>
	<div class="mui-content">
		<div class="mui-slider">
			<div class="mui-slider-group">
				<div class="mui-slider-item">
					<img src="images/hd1.jpg" />
				</div>
				<div class="mui-slider-item">
					<img src="images/hd3.jpg" />
				</div>
			</div>
		</div>
		<form action="" id='login-form' class="mui-input-group">
			<div class="mui-input-row">
				<label>账号</label> <input id='phoneNum' type="text"
					class="mui-input-clear mui-input" placeholder="请输入账号">
			</div>
			<div class="mui-input-row">
				<label>密码</label> <input id='password' type="password"
					class="mui-input-clear mui-input" placeholder="请输入密码">
			</div>
		</form>
		<div class="mui-content-padded">
			<button id='login' class="mui-btn mui-btn-block mui-btn-primary">登录</button>
			<div class="link-area">
				<a href="${pageContext.request.contextPath }/mui/reg" id='reg'>注册账号</a> <span class="spliter">|</span> <a
					id='forgetPassword'>忘记密码</a>
			</div>
		</div>
		<div class="mui-content-padded oauth-area"></div>
	</div>
	<script src="${pageContext.request.contextPath }/mui/js/mui.min.js"></script>
	<script
		src="${pageContext.request.contextPath }/mui/js/mui.enterfocus.js"></script>
	<script src="${pageContext.request.contextPath }/mui/js/app.js"></script>
</body>
<script>
	$("#login").click(function() {
		var phoneNumVal = $("#phoneNum").val().trim();
		var pwdVal = $("#password").val().trim();
		if(""!=phoneNum&&""!=pwdVal){
			$.ajax({
	            //几个参数需要注意一下
	                type: "GET",//方法类型
	                dataType: "json",//预期服务器返回的数据类型
	                data: {phoneNum : phoneNumVal, pwd : pwdVal},
	                url: "${pageContext.request.contextPath }/mui/loginverify" ,//url
	                success: function (data) {
	                	console.log("666");
	                    if(data == "1"){
							  window.location.href="${pageContext.request.contextPath }/mui/main"
						  }else if(data == "0"){
							  alert("账号密码错误!");
						  }
	                },
	                error : function(data) {
	                    alert("Exception!!!" + data.message);
	                }
	            });
		}else{
			alert("输入内容不能为空");
		}
	})
</script>
</html>