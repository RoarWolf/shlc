<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>登录</title>
<link rel="stylesheet" href="${hdpath}/css/logstyles.css">
<script type="text/javascript" src="${hdpath}/js/jquery.min.js"></script>
<script src="http://res.wx.qq.com/connect/zh_CN/htmledition/js/wxLogin.js"></script>
</head>
<body>
<div class="jq22-container" style="padding-top:8px">
	<div class="nva">
	  <img alt="自助充电平台" src="${hdpath}/images/tengfuchong.jpg">
	  <span>自助充电平台</span>
	</div>
	<div class="login-wrap">
		<div class="login-html">
			<input id="tab-1" type="radio" name="tab" class="sign-in" checked><label for="tab-1" class="tab">扫码登录</label>
			<input id="tab-2" type="radio" name="tab" class="sign-up"><label for="tab-2" class="tab">账号登录</label>
			<input id="tab-3" type="radio" name="tab" class="sign-note"><label for="tab-3" class="tab">短信登录</label>
			<div class="login-form">
				<div class="sign-in-htm">
					<div id="login_ewm">
					</div>
				</div>
				<div class="sign-up-htm">
					<div class="group">
						<label for="account" class="label">账号：</label>
						<input id="account" type="text" class="input">
					</div>
					<div class="group">
						<label for="password" class="label">密 码：</label>
						<input id="password" type="password" class="input" data-type="password">
					</div>
					<!-- <div class="group">
						<input id="check" type="checkbox" class="check" checked>
						<label for="check"><span class="icon"></span> 保存密码</label>
					</div> -->
					<div class="group">
						<input type="button" id="loginup" class="button" value="登录">
					</div>
					<div class="group">
						<span id="hintinfo" style="color: red;margin: 3px 0 0 150px;"></span>
					</div>
				</div>
				<div class="sign-note-htm">
					<div class="group">
						<label for="mobile" class="label">手机号：</label>
						<input id="mobile" type="text" class="input">
					</div>
					<div class="group">
						<label for="security" class="label">验证码：</label>
						<input id="security" type="text" class="input" data-type="security">
						<input id="auth" type="button" class="input" value="验证码">
						<input id="authcode" type="hidden" class="input">
						<input id="authtime" name="authtime" type="hidden" class="input">
					</div>
					<div class="group">
						<input type="button" id="notelogin" class="button" value="登录">
					</div>
					<div class="group">
						<span id="notehint" style="color: red;margin: 3px 0 0 150px;"></span>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
<script type="text/javascript" src="${hdpath}/js/wxlogin.js"></script>
<script type="text/javascript">
$(function() {
	$("#auth").click(function() {//获取验证码
		var obj = document.getElementById("auth");
		obj.style.backgroundColor= "#7ae67f";
		$("#auth").attr("disabled", true); 
		$(".sign-note-htm input[name='authtime']").val(new Date().getTime());
		var phone = $("#mobile").val().replace(/\s+/g, "");;
		if(phone==""){
			$("#notehint").html("请输入手机号!");
			return false;
		}
		var bool = mobile(phone);//判断电话是否合法
		if(!bool){
			$("#notehint").html("手机号不合法!");
			return false;
		}else{
			timedCount();
		}
		$.ajax({
		    type:'POST',
		    url:"${hdpath}/pcadminlogin/getauthcode",//获取验证码
		    data : {mobile : phone,},
		    success:function(e){
		    	if(e==1){
		    		$("#notehint").html("该手机号不存在！");
		    	}else{
			    	$("#authcode").val(e);
		    		//$("#notehint").html(e);
		    	}
		    },
		    error:function(){
		      	alert( '验证发送失败' );
		    } 
		});
	});
	
	$("#notelogin").click(function() {//验证码登录
		var mobile = $("#mobile").val().replace(/\s+/g, "");
		var security = $("#security").val().replace(/\s+/g, "");
		var authcode = $("#authcode").val().replace(/\s+/g, "");
		var authtime = $("#authtime").val();
		if(mobile==""){
			alert("手机号不能为空!");
			$("#notehint").html("请输入手机号!");
			return false;
		}else if(security==""){
			alert("验证码不能为空!");
			$("#notehint").html("请输入验证码!");
			return false;
		} 
		$.ajax({
		    type: "POST",
		    url: "${hdpath}/pcadminlogin/codelogin",
		    data: {mobile:mobile,security:security,authcode:authcode,authtime:authtime,},
		    success: function(e){
		    	if(e == 0){
		        	window.location.href = "${hdpath}/pcequipment/selectEquList";
		        }else if(e == 1){
		        	window.location.href = "${hdpath}/pcequipment/selectEquList";
		        }else {
		        	$("#notehint").html(e);
		        } 
		    },
		    error : function(){
		        alert("异常！");
		    }
		});
	});
	
	$("#loginup").click(function() {//账号登录
		var account = $("#account").val().replace(/\s+/g, "");
		var password = $("#password").val().replace(/\s+/g, "");
		if(account==""){
			$("#hintinfo").html("请输入账号!");
			return false;
		}else if(password==""){
			$("#hintinfo").html("请输入密码!");
			return false;
		}
		var bool = mobile(account);//判断电话是否合法
		if(!bool){
			$("#hintinfo").html("账号不合法!");
			return false;
		}
		$.ajax({
		    type: "POST",
		    url: "${hdpath}/pcadminlogin/login",
		    data: {phoneNum:account,Password:password},
		    success: function(e){
		    	if(e == 0){
		        	window.location.href = "${hdpath}/pcstatistics/collectinfo";
		        }else if(e == 1){
		        	window.location.href = "${hdpath}/pcstatistics/agentdatacollect";
		        }else {
		        	$("#hintinfo").html(e);
		        } 
		    },
		    error : function(){
		        alert("异常！");
		        $("#hintinfo").html("异常!");
		    }
		});
	});
});


var c=120
var t
function timedCount(){
	var authobj = document.getElementById("auth");
	authobj.value=c
	c=c-1
	if(c==0){
		$("#auth").attr("disabled", false); 
		authobj.style.backgroundColor= "#44b549";
		authobj.value="重新获取";
		c=120;
		return false;
	}else{
		t=setTimeout("timedCount()",1000)
	}
}
function mobile(n){
    var reg = /^1[3|4|5|7|8|9]\d{9}$/;
    if(!reg.test(n)){
		//alert("手机号不合法!");
		return false;
    }
    return true;
}
</script>
</html>