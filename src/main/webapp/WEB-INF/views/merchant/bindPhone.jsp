<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<title>绑定手机号</title>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<link rel="stylesheet" type="text/css" href="${hdpath}/css/base.css">
<link rel="stylesheet" type="text/css" href="${hdpath}/mui/css/mui.min.css">
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="${hdpath}/mui/js/mui.min.js"></script>
<script type="text/javascript" src="${hdpath}/js/jquery.js"></script>
<style>
		body {
			padding: 15px 10px;
		} 
		.mui-input-group {
			background-color: transparent;
		}
		.app .title{
			font-size: 16px;
			color: #333;
		}
		.app .tip {
			margin-bottom: 25px;
		} 
		.app .tip_info {
			color: #FF7F27;
		}
		.app .mui-input-row {
			height: 45px;
			line-height: 45px;
			background-color: #f5f7fa !important;
			border-radius: 6px;
			border: 1px solid #ccc;
			margin-bottom: 10px;
		}
		.app .mui-input-row input {
			height: 45px;
			font-size: 14px;
			color: #666;
			line-height: 45px;
		}
		.mui-input-group .mui-input-row:after {
			height: 0;
		}
		.app .mui-input-row label {
			font-size: 14px;
			height: 45px;
			line-height: 45px;
			padding: 0 15px;
		}
		input::-webkit-input-placeholder {
		    font-size: 14px;
		    color: #999;
		}
		input:-moz-placeholder {
		    font-size: 14px;
		    color: #999;
		}
		input:-ms-input-placeholder {
		    font-size: 14px;
		    color: #999;
		}
		.code_div {
			position: relative;
		}
		.code_div .getCode {
			position: absolute;
			right: 0;
			bottom: 0;
			font-size: 14px;
			color: #22B14C;
			padding: 5px;
			line-height: 1.1em;
		}
		.code_div .getCode.bTime {
			display: none;
		}
		.sub_con {
			margin-top: 25px;
			text-align: center;
		}
		.sub_con button {
			width: 85%;
			height: 40px;
		}
</style>
</head>
<body data-id="${user.id}" data-source="${source}" data-openid="${user.openid}" data-cardID="${cardID}">
<div class="app">
		<div class="tip">
			<p class="title">提示：</p>
			<p class="tip_info">你的账户暂未绑定手机号，请先绑定手机号再进行充值！</p>
		</div>
		<div class="form">
			<p class="title">绑定手机号</p>
			 <input type="hidden" name="time" id="time" value="">
             <input type="hidden" name="captchaNum" id="captchaNum" value="">
			<div class="mui-input-group">
			    <div class="mui-input-row">
			        <label>手机号</label>
			        <input type="text" class="mui-input-clear" id="mobile" placeholder="请输入手机号">
			    </div>
			    <div class="mui-input-row">
			        <label>确认手机号</label>
			        <input type="text" class="mui-input-clear" id="conMobile" placeholder="再次确认手机号">
			    </div>
			    <!-- <div class="mui-input-row code_div">
			        <label>验证码：</label>
			         <input type="text" class="mui-input-clear" placeholder="请输入验证码" id="captcha" name="captcha">
			         <div class="getCode reNum">获取验证码</div>
			         <div class="getCode bTime">倒计时120s</div>
			    </div> -->
			</div>
		</div>
		<div class="sub_con">
			<button type="button" id="submit" class="mui-btn mui-btn-success">提交</button>
		</div>
	</div>
<script>
$(function(){
	var timer= null

	/* $('.reNum').on('touchstart',function(e){ //点击发送验证码
		var mobileVal= $('#mobile').val().trim()
		if(!checkPhone()){
			return false
		}
	 	$.ajax({
		   url: '/general/existaccount',
		   data: {mobile: mobileVal},
		   type : "post",
		   dataType : "json",
		   success: function(e){
		      if(e.code == 201){
		    	    //按钮倒计时
		    	    sendDownTime()
		    	    $.ajax({
		    	        type : 'POST',
		    	        url : "/general/getCaptchaData",//获取验证码
		    	        data : {captcha : 1,mobile : mobileVal},
		    	        success : function(e){
		    	            if(e.code == 200){
		    	                $("#captchaNum").val(e.captcha);
		    	                $("input[name='time']").val(e.sendtime);
		    	            }else{
		    	            	showToast('异常错误');
		    	            }
		    	        },
		    	        error : function() {
		    	            reShow() //显示获取验证码
		    	            showToast('验证发送失败');
		    	        }
		    	    });
		      }else if(e.code==200){
		    	  showToast('该手机号已存在，不能注册'); 
		      }
		   },
		   error: function(e){
			   showToast('注册异常'); 
		   }
		 })
	}) */
	/*提交，验证手机号/验证码*/
/* 	$('#submit').click(function(){
		if(!checkPhone()){
			return false
		}
		var captcha= $('#captcha').val().trim()
		if(!testReg(/^\d{6}$/,captcha)){
	        showToast('请输入正确的验证码')
	        return false
	    }
		var phone= $('#mobile').val().trim()
		var authcode= $('#captchaNum').val().trim()
		var captchaNum= $('#captcha').val().trim() //输入的验证码
		var time= $('#time').val().trim()
		var uid= $('body').attr('data-id').trim()

	    $.ajax({
	    	url: '/general/savePhone',
	    	type: 'post',
	    	data: {
	    		uid: uid,
	    		phone: phone,
	    		checkcode: captchaNum,
	    		authcode: authcode,
	    		sendtime: time
	    	},
	    	success: function(res){
	    		var source= $('body').attr('data-source').trim() // 1 钱包  2在线卡
	    		if(res.code == 200){
	    			 showToast('绑定成功')
	    			 setTimeout(function(){
	    				 var url =""
	    				 var openid= $('body').attr('data-openid').trim()
	    				 if(source == 1){
	    					 if(GetRequest().from == 1){
	    						 url= "/general/walletcharge?from=1&openid="+openid
	    					 }else{
	    						 url= "/general/walletcharge?openid="+openid
	    					 }
	    					 	 
	    				 }else if(source == 2){
	    					 var cardnum= $('body').attr('data-cardID').trim()
	    					 url="/general/iccharge?openid="+openid+'&cardID='+cardnum
	    				 }
	    				 window.location.replace(url);
	    			 },400)
	    		}else{
	    			 showToast(res.message)
	    		}
	    	},
	    	error: function(err){
	    		
	    	}
	    })

	}) */
	
	$('#submit').click(function(){
		if(!checkPhone()){
			return false
		}
		var phone= $('#mobile').val().trim()
		var confphone= $("#conMobile").val().trim()
		if(phone != confphone){
			return showToast('两次输入的手机号不一致，请重新输入')
		}
		var uid= $('body').attr('data-id').trim()
	    $.ajax({
	    	url: '/general/savePhone2',
	    	type: 'post',
	    	data: {
	    		uid: uid,
	    		phone: phone,
	    	},
	    	success: function(res){
	    		var source= $('body').attr('data-source').trim() // 1 钱包  2在线卡
	    		if(res.code == 200){
	    			 showToast('绑定成功')
	    			 setTimeout(function(){
	    				 var url =""
	    				 var openid= $('body').attr('data-openid').trim()
	    				 if(source == 1){
	    					 if(GetRequest().from == 1){
	    						 url= "/general/walletcharge?from=1&openid="+openid
	    					 }else{
	    						 url= "/general/walletcharge?openid="+openid
	    					 }
	    					 	 
	    				 }else if(source == 2){
	    					 var cardnum= $('body').attr('data-cardID').trim()
	    					 url="/general/iccharge?openid="+openid+'&cardID='+cardnum
	    				 }
	    				 window.location.replace(url);
	    			 },400)
	    		}else{
	    			 showToast(res.message)
	    		}
	    	},
	    	error: function(err){
	    		showToast('绑定失败')
	    	}
	    })

	})

	function sendDownTime(){
	    clearInterval(timer)
	    $('.reNum').fadeOut('300')
	    $('.bTime').fadeIn('300')
	    var str= ''
	    var num= 120
	    timer= setInterval(function () {
	        num--
	        str= '倒计时'+ num +'s'
	        $('.bTime').html(str)
	        if(num === 0){
	            clearInterval(timer)
	            $('.bTime').fadeOut('300')
	            $('.reNum').fadeIn('300')
	        }
	    },1000)
	}

	function checkPhone(){  //检查手机号
	    var mobileVal= $('#mobile').val().trim()
	    if(mobileVal.length <= 0){
	        //调用mui
	        showToast('请输入手机号')
	        return false
	    }
	    if(!testReg(/^1[3|4|5|6|7|8|9]\d{9}$/,mobileVal)){
	        showToast('请正确输入手机号')
	        return false
	    }
	    return true
	}
	function testReg(reg,str){  //正则判断
	    return reg.test(str)
	}
	function showToast(content,time){ //调用Toast提示框
	    content= content || ''
	    time= time || '1500'
	    mui.toast(content,{ duration:time, type:'div' })
	}
	function reShow(){ //重新显示获取验证码
	    clearInterval(timer)
	    $('b.bTime').fadeOut('300')
	    $('b.reNum').fadeIn('300') 
	}
	
	/* 解析url */
	function GetRequest() {
	    var url = location.search; //获取url中"?"符后的字串
	    var theRequest = new Object();
	    if (url.indexOf("?") != -1) {
	        var str = url.substr(1);
	        strs = str.split("&");
	        for(var i = 0; i < strs.length; i ++) {
	            theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]);
	        }
	    }
	    return theRequest;
	}

})
</script>
</body>
</html>