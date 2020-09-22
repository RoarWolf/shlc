<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link rel="stylesheet" href="${hdpath}/css/admin.css">
<title>收益结算</title>
<script type="text/javascript" src="${hdpath}/js/calendar.js"></script>
	<%-- <link rel="stylesheet" href="${hdpath}/layui/css/layui.css">
	<script src="${hdpath}/layui/layui.all.js"></script> --%>
	<script src="${hdpath}/js/jquery.min.js"></script>
	<style>
		.conInfo {
			width: 500px;
			box-shadow: 4px 4px 10px 10px #eee;
			margin: 50px auto;
			padding: 20px;
			background-color: #f8f8f8;
		}
		.conInfo h1 {
			text-align: center;
			font-size: 18px;
			margin-bottom: 15px;
			font-weight: bold;
		}
		.conInfo form {
			padding: 27px 40px;
			width: 90%;
			margin: 0 auto;
		}
		.conInfo form input[type="text"] {
			width:100%;
			border: 1px solid #ccc;
			height: 38px;
			padding: 0 15px;
		}
		.conInfo form input[type="radio"] {
			zoom: 130%;
		}
		.layui-form-item .layui-inline {
			width: 220px;
			margin-bottom: 20px;
		}
		.selectType:after  {
			content: '';
			display: block;
			visibility: hidden;
			height: 0;
			clear: both;
		}
		.selectType li {
			width: 50%;
			line-height: 40px;
			text-align: center;
			float: left;
		}
		.selectType li.active {
			background-color: #fff;
		}
		.allSettlement {
			display: none;
		}
		.layui-card {
			background-color: #f8f8f8;
		}
		.layui-card-body {
			background-color: #fff;
		}
		.layui-form-label {
			width: 90px;
		}
		.layui-layer {
			top: 700px !important;
			left: 50% !important;
		}
		
		.alert-layui {
			width: 300px;
			background-color: rgba(96,197,241,.4);
			border-radius: 5px;
			box-shadow: 4px 4px 10px 10px #eee;
			position: fixed;
			left: 50%;
			margin-left: -46px;
			bottom: 50px;
			z-index: 99;
			transition: .5s all;
			display: none;
		}
		.alert-layui  .con {
			height: 70px;
			line-height: 40px;
			padding: 15px;
		}
		.alert-layui  p {
			display: inline-block;
			color: #333;
			font-size: 16px;
			margin-left: 10px;
			line-height: 1.1em;
		}
		.alert-layui  i {
			font-size: 18px;
			color: #00A2E8;
		}
		.dangerAlert {
			background-color: rgba(237,28,36,.3);
		}
		.dangerAlert i{
			color: #ED1C24;
		}
		.bottomDiv {
			margin-top: 15px;
		}
	</style>
</head>
<body  style="background-color:#f2f9fd;">
<div class="header bg-main">
	<%@ include file="/WEB-INF/views/navigation.jsp"%>
</div>
<div class="leftnav" id="lefeMenu">
	<%@ include file="/WEB-INF/views/menu.jsp"%>
</div>
<ul class="bread">
  <li><a href="javascript:void(0)" target="right" class="icon-home">商户汇总结算</a></li>
</ul>
<div class="admin">
	<div class="content">
		<div class="conInfo">
			<div class="layui-card">
			  <div class="layui-card-header">
					<h1>结算收益</h1>
			  </div>
			 <div class="selectType">
				<li class="active" data-val="1">结算个人</li>
				<li data-val="2">结算全部</li>
			 </div>
			  <div class="layui-card-body personSettlement">
				    <form class="layui-form form1" action="">
					  <div class="layui-form-item">
					    <label class="layui-form-label">手机号</label>
					    <div class="layui-inline">
					      <input type="text" name="phone" required  lay-verify="phone" placeholder="请输入手机号" autocomplete="off" class="layui-input">
					    </div>
					  </div>
					  <div class="layui-form-item">
					    <label class="layui-form-label">选择时间</label>
					    <div class="layui-inline">
							<input type="text" class="layui-input time1" lay-verify="required" name="time1" readonly required placeholder="请选择时间" onclick="new Calendar().show(this);">
						</div>
					  </div>
					  
					  <div class="layui-form-item">
					    <label class="layui-form-label">结算种类</label>
					    <div class="layui-input-block">
					      <label>收益</label>
					      <input type="radio" name="type" value="1" title="收益" checked>
					      &nbsp; &nbsp; &nbsp; 
					       <label>设备</label>
					      <input type="radio" name="type" value="2" title="设备" >
					    </div>
					  </div>

					  <div class="layui-form-item bottomDiv">
					    <div class="layui-input-block">
					      <button type="reset" class="layui-btn layui-btn-primary">重置</button>
					      <button class="layui-btn submitButton submitButton1" lay-submit lay-filter="formDemo1">立即提交</button>
					    </div>
					  </div>
				</form>
			  </div>
				<!-- 全部结算 -->
				<div class="layui-card-body allSettlement">
				    <form class="layui-form form2">
					  <div class="layui-form-item">
					    <label class="layui-form-label">选择时间</label>
					    <div class="layui-inline"> 
							<input type="text" class="layui-input time2" lay-verify="required" name="time2" readonly required placeholder="请选择时间" onclick="new Calendar().show(this);">
						</div>
					  </div>
					  
					  <div class="layui-form-item">
					    <label class="layui-form-label">结算种类</label>
					    <div class="layui-input-block">
					      <label>收益</label>
					      <input type="radio" name="type" value="1" title="收益" checked>
					      &nbsp; &nbsp; &nbsp; 
					      <label>设备</label>
					      <input type="radio" name="type" value="2" title="设备" >
					    </div>
					  </div>

					  <div class="layui-form-item bottomDiv">
					    <div class="layui-input-block">
					      <button type="reset" class="layui-btn layui-btn-primary">重置</button>
					      <button class="layui-btn submitButton submitButton2" lay-submit lay-filter="formDemo2">立即提交</button>
					    </div>
					  </div>
				</form>
			  </div>

			</div>
			
		</div>
	</div>
</div>

<div class="alert-layui  alertInfo">
	<div class="con">
		<i class="layui-icon layui-icon-face-smile"></i>
		<p>结算完成</p>
	</div>
</div>
<div class="alert-layui  dangerAlert">
	<div class="con">
		<i class="layui-icon layui-icon-tips"></i>
		<p>结算失败</p>
	</div>
</div>
<script>
$(function(){
	$('.selectType li').on('click',function(){
		$(this).siblings().removeClass('active')
		$(this).addClass('active')
		if($(this).attr('data-val').trim() == 1){
			$('.personSettlement').show()
			$('.allSettlement').hide()
		}else{
			$('.allSettlement').show()
			$('.personSettlement').hide()
		}
	})
	$('.submitButton').click(function(e){
		e= e || window.event
		e.preventDefault()
		var data= {}
		var url= ''
		if($(this).hasClass('submitButton1')){
			var phone= $('.form1 input[name="phone"]').val().trim()
			var time= $('.form1 input[name="time1"]').val().trim()
			var type= $('.form1 input[type="radio"]:checked').val()
			data= {
				phone: phone,
				time: time,
				type: type
			}
			url= '/system/calculateAloneCollect'
		}else {
			var time= $('.form2 input[name="time2"]').val().trim()
			var type= $('.form2 input[type="radio"]:checked').val()
			data= {
				time: time,
				type: type
			}
			url= "/system/calculateTotalCollect"
		}
		$.ajax({
	  		url: url,
	  		data: data,
	  		success: function(res){
	  			if(res.code == 200){
	  				handleAlert(true)
	  			}else{
	  				handleAlert(false)
	  			}
	  		},
	  		error: function(err){
	  			handleAlert(false)
	  		}
	  	})
	   
	})
	
})
	function handleAlert(flag){
			if(flag){
				var timer= null
				$('.alertInfo').fadeIn(500)
				clearTimeout(timer)
			  	timer= setTimeout(function(){
			  		$('.alertInfo').fadeOut(500)	
			  	},1500)
			  }else {
			  	var timer= null
				$('.dangerAlert').fadeIn(500)
				clearTimeout(timer)
			  	timer= setTimeout(function(){
			  		$('.dangerAlert').fadeOut(500)	
			  	},1500)
			  }
			
		}
</script>
</body>
</html>