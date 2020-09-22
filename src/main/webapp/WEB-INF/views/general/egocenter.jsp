<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>个人信息</title>
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/icons-extra.css"/>
<!--App自定义的css-->
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/app.css" />
<script type="text/javascript" src="${hdpath }/js/jquery-2.1.0.js"></script>
<script type="text/javascript" src="${hdpath }/mui/js/mui.js"></script>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<style type="text/css">
html,body{width: 100%; height: 100%;background-color: #fff;}
#secuform .mui-input-row label~input{border-left: 1px solid #e2dcdc; text-align: center;}
/*toast信息提示*/
.mui-toast-container {bottom: 50% !important;}
.mui-toast-message { opacity: 0.6; color: #fff; width: 180px; padding: 10px 5px 10px 5px;}
</style>


</head>
<body>
<header class="mui-bar mui-bar-nav mui-bar-nav-bg">
    <a id="icon-menu" class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left" href="${hdpath }/general/index"></a>
  <%--   <a class="mui-action-back mui-icon mui-icon-bars mui-pull-right mui-a-color" href="${hdpath }/general/index"></a> --%>
    <h1 class="mui-title">个人中心</h1>
</header>
<div class="mui-input-group btnreadonly" style="margin-top: 50px;">
    <div class="mui-input-row">
        <label>账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：</label>
        <input type="text" class="mui-input-clear" value="${numerical}" disabled ="disabled">
    </div>
    <div class="mui-input-row">
        <label>昵&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称：</label>
        <input type="text" class="mui-input-clear" value="${user.username}" disabled ="disabled">
    </div>
    <div class="mui-input-row">
        <label>用&nbsp;&nbsp;户&nbsp;&nbsp;名：</label>
        <input type="text" class="mui-input-clear" value="${user.realname}" disabled ="disabled">
    </div>
    <div class="mui-input-row">
        <label>手&nbsp;&nbsp;机&nbsp;&nbsp;号：</label>
        <input type="text" class="mui-input-clear" value="${user.phoneNum}" disabled ="disabled">
    </div>
    <div class="mui-input-row">
        <label>创建时间：</label>
        <input type="text" class="mui-input-clear" value='<fmt:formatDate value="${user.createTime}" pattern="yyyy-MM-dd HH:mm:ss" />' disabled ="disabled">
    </div>
    <div class="mui-input-row">
        <label>修改时间：</label>
        <input type="text" class="mui-input-clear" value='<fmt:formatDate value="${user.updateTime}" pattern="yyyy-MM-dd HH:mm:ss" />' disabled ="disabled">
    </div>
    <c:if test="${user.phoneNum==null}">
    	<button id="btnedit" type="button" style="width: 60%;margin: 20% 0 0 20%;" class="mui-btn mui-btn-success">绑定手机号</button>
    </c:if>
</div>
<div  class="mui-input-group editin" style="margin-top: 80px;display:none">
	<form id="secuform">
	<input type="hidden" name="id" class="mui-input-clear" value="${user.id}">
    <input type="hidden" name="securities" class="mui-input-clear">
    <input type="hidden" name="securtime" class="mui-input-clear">
    <div class="mui-input-row">
        <label>用&nbsp;&nbsp;户&nbsp;&nbsp;名：</label>
        <input type="text" name="realname" class="mui-input-clear" >
    </div>
    <div class="mui-input-row">
        <label>手&nbsp;&nbsp;机&nbsp;&nbsp;号：</label>
        <input type="text" name="phoneNum" class="mui-input-clear" value="${user.phoneNum}">
    </div>
    <div class="mui-input-row">
        <label>验&nbsp;&nbsp;证&nbsp;&nbsp;码：</label>
        <button type="button" style="width: 25%; padding: 11px 15px;" class="mui-btn mui-btn-success securities">获取验证码</button>
        <input style=" width: 40%;" type="text" name="security" class="mui-input-clear">
    </div>
    </form>
    <div style="margin-top: 10%">
    <button id="btnreturn" type="button"  style="margin-left: 10%;width: 35%;" class="mui-btn mui-btn-success">返回</button>
    <button id="btnsave" type="button" style="margin-left: 10%;width: 35%;" class="mui-btn mui-btn-success">保存</button>
    </div>
</div>

</body>
<script type="text/javascript">
$("#btnedit").click(function(){
	$(".btnreadonly").hide();
	$(".editin").show();
});
$("#btnreturn").click(function(){
	$(".editin").hide();
	$(".btnreadonly").show();
});
//获取验证码 
$(".securities").click(function(){
	var mobile = $("#secuform").find("input[name='phoneNum']").val();
	var bool = isPhoneTel(mobile);//判断电话是否合法
	if (!bool) {
		return false;
	}
	$.ajax({
		data:{mobile : mobile,},
		url : "${hdpath}/pcadminiStrator/captcha",
		type : "POST",
		success:function(e){
			if(e==1){
				mui.toast("该手机号已被注册");
			}else{
				$(".securities").attr("disabled", true);
				$("#secuform").find("input[name='securities']").attr("value",e);
				$("#secuform").find("input[name='securtime']").attr("value",new Date().getTime());
				mui.toast("验证发送成功，请注意查收");
				timedCount();
			}
        },
        error:function(){
        	mui.toast("验证发送失败");
        } 
	});
});
var c = 120;
function timedCount() {
	var con = $(".securities").text("倒计时 "+c);
	c = c - 1
	if (c == 0) {
		$(".securities").text("获取验证码");
		c = 120;
		return false;
	} else {
		setTimeout("timedCount()", 1000)
	}
}

$("#btnsave").click(function(){
	var phone = $("#secuform").find("input[name='phoneNum']").val();
	var security = $("#secuform").find("input[name='security']").val();
	var bool = isPhoneTel(phone);//判断电话是否合法
	if (!bool) {
		return false;
	}else  if (security == null || security == undefined || security == ''){
		mui.toast("请输入验证码!");
		return false;
	}
	$.ajax({
		data:$("#secuform").serialize(),
		url : "${hdpath}/general/saveuser",
		type : "POST",
		success : function(e){
			if(e==0){
				location.reload();
			}else if(e==1){
				mui.toast("该手机号已被注册");
			}else{
				mui.toast(e);
			}
		},
	}); 
});
function isPhoneTel(n) {
	var reg = /^1[3|4|5|7|8]\d{9}$/;
	if (!reg.test(n)) {
		mui.toast("手机号不合法!");
		return false;
	}
	return true;
}
</script>
</html>