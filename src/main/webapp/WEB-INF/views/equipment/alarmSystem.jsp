<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<title>设置报警系统</title>
	<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
	<link rel="stylesheet" href="/css/base.css">
	<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/mui.min.css"/>
	<script type="text/javascript" src="${hdpath }/js/jquery-2.1.0.js"></script>
	<script type="text/javascript" src="${hdpath }/mui/js/mui.min.js"></script>
	<style>
		.app {
			padding-top: 55px;
		}
		.app_grad ul {
			width: 100%;
			display: flex;
			flex-wrap: wrap;
		}
		.app_grad ul .grad_item {
			width: 30%;
			margin-bottom: 15px;
			border-radius: 4px;
	   		overflow: hidden;
	   		position: relative;
	   		margin-left: 2.5%;
		}
		.app_grad ul .grad_item:nth-child(3n){
			margin-right: 2.5%;
		}
		.app_grad ul .grad_item .loading {
			position: absolute;
			right: 0;
			top: 0;
			padding: 10px;
			transform-origin: center center;
			font-size: 0;
		}
		.app_grad ul .grad_item .loading img {
			width: 16px;
			height: 16px;
			display: block;
		}
		.app_grad ul .grad_item .item_div {
			padding: 10px;
			display: flex;
			justify-content: center;
			flex-wrap: wrap;
			background-color: rgba(255,255,255,0.5);
		}
		.app_grad ul .grad_item .item_div:active {
			background-color: rgba(255,255,255,0.3);
		}
		.app_grad ul .grad_item .img_icon{
			width: 40%;
			height: 40%;
			margin-bottom: 5px;
		}
		.item_info {
			flex-shrink: 0;
			width: 100%;
		}
		.item_info .info_val{
			display: flex;
			flex-direction: column;
			align-items: center;
			font-size: 16px;
		}
		.item_info .info_title {
			font-size: 14px;
			color: #777;
			display: flex;
			flex-direction: column;
			align-items: center;
		}
		.app_grad ul .grad_item .loading.active {
			animation: Loading_Rotate 1.2s linear infinite;
	
		}
		.tip {
			font-size: 14px;
		    color: #999;
		    padding: 2.5%;
		}
		.tip strong {
			color: #666;
		}
		@keyframes Loading_Rotate {
			0% {
				transform: rotate(0deg);
			}
			100% {
				transform: rotate(360deg);
			}
		}
		@-webkit-keyframes Loading_Rotate {
			0% {
				transform: rotate(0deg);
			}
			100% {
				transform: rotate(360deg);
			}
		}
		@-mos-keyframes Loading_Rotate {
			0% {
				transform: rotate(0deg);
			}
			100% {
				transform: rotate(360deg);
			}
		}
		@-ms-keyframes Loading_Rotate {
			0% {
				transform: rotate(0deg);
			}
			100% {
				transform: rotate(360deg);
			}
		}
		.tip-val {
			background: rgba(0,0,0,.2);
		    padding: 5px 10px;
		    color: #333;
		    text-align: left;
		    border-radius: 4px;
		    font-size: 12px;
		    margin-bottom: 5px;
		}
		.mui-popup-input input {
		    margin: 5px 0 0;
		}
		
		main {
			margin: 1vh 15px;
			background-color: rgba(255,255,255,.5);
			border-radius: 5px;
			padding: 8px 10px;
			font-size: 14px;
		}
		main .history-ul .history-item {
			border-bottom: 1px solid #ddd;
			padding: 6px 8px;
			display: flex;
			justify-content: space-between;
			align-items: center;
			transition: .3s;
		}
		main .history-ul .history-item:active,
		main .history-ul .history-item:focus {
			background-color: #efefef;
		}
		main .history-ul .history-item:last-child{
			border-bottom-color: transparent;
		}
		main .history-ul .history-item .history-title {
			color: #000;
		}
		main .history-ul .history-item .history-tip {
			font-size: 12px;
			color: #999;
		}
		main .history-ul .history-item .history-tip .mui-icon-arrowright {
			font-size: 18px;
		}
	</style>
</head>
<body data-code="${code}">
<header class="mui-bar mui-bar-nav">
		<a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
		<h1 class="mui-title">报警系统</h1>
</header>
<div class="app">
	<div class="app_grad">
		<ul>
			<li class="grad_item" data-type="1" data-critical="${hotDoorsill}">
				<div class="loading">
					<img class="loading_img" src="${hdpath}/images/Loading.png" alt="">
				</div>
				<div class="item_div">
					<img class="img_icon" src="${hdpath}/images/温度报警.png" alt="">
					<div class="item_info">
						<span class="info_val">${hotDoorsill}℃</span>
						<span class="info_title">报警温度</span>
					</div>
				</div>
			</li>
			<li class="grad_item" data-type="2" data-critical="${smokeDoorsill}">
				<div class="loading">
					<img class="loading_img" src="${hdpath}/images/Loading.png" alt="">
				</div>
				<div class="item_div">
					<img class="img_icon" src="${hdpath}/images/烟雾告警.png" alt="">
					<div class="item_info">
						<span class="info_val">${smokeDoorsill}&nbsp;</span>
						<span class="info_title">烟感阈值</span>
					</div>
				</div>
			</li>
			<li class="grad_item" data-type="3" data-critical="${powerTotal}">
				<div class="loading">
					<img class="loading_img" src="${hdpath}/images/Loading.png" alt="">
				</div>
				<div class="item_div">
					<img class="img_icon" src="${hdpath}/images/过载报警.png" alt="">
					<div class="item_info">
						<span class="info_val">${powerTotal}W</span>
						<span class="info_title">设备总功率</span>
					</div>
				</div>
			</li>
			<li class="grad_item" data-type="4" >
				<div class="loading">
					<img class="loading_img" src="${hdpath}/images/Loading.png" alt="">
				</div>
				<div class="item_div">
					<img class="img_icon" src="${hdpath}/images/过流报警.png" alt="">
					<div class="item_info">
						<span class="info_val">0度</span>
						<span class="info_title">远程抄表</span>
					</div>
				</div>
			</li>
		</ul>
	</div>
	<h5 style="margin-left: 15px;">历史查询数据</h5>
	<main>
		<ul class="history-ul">
			<li class="history-item">
				<div class="history-title">历史查询温度</div>
				<a class="history-tip" href="/equipment/alarmtemperatureline?code=${code}">查看记录 <span class="mui-icon mui-icon-arrowright"></span></a>
			</li>
			<li class="history-item">
				<div class="history-title">历史查询电量</div>
				<div class="history-tip">查看记录 <span class="mui-icon mui-icon-arrowright"></span></div>
			</li>
		</ul>
	</main>
	<div class="tip">
		<strong>提示：</strong>
		点击“报警温度”、“烟感阈值”、“设备总功率”设置对应的报警值，点击“远程抄表”查看历史抄表记录
	</div>
</div>
<script>
$(function(){
	var CODE= $('body').data('code').trim()
	
	$('.grad_item').on('click',function(e){
		e= e || window.event
		var currentTarget= e.currentTarget
		var target= e.target || e.srcElement
		var type= $(this).attr('data-type').trim()
		if($(target).hasClass('loading') || $(target).hasClass('loading_img')){
			getNowAlarmParmas(type)
			$(currentTarget).find('.loading').addClass('active')
			setTimeout(function(){
				$(currentTarget).find('.loading').removeClass('active')
			},2100)
			return
		}
		  if(['1','2','3'].indexOf(type) != -1){
			$.ajax({
				url: '/getDeviceSetArgument',
				type: 'post',
				data: {
					code: CODE,
					type: type,
				},
				success: function(res){
					console.log(res)
				},
				error: function(err){
					console.log(err)
				}
			})
			var obj= {}
			var critical= $(this).data('critical') //获取设置的阈值
			switch(type){
				case '1': 
					obj= {
						message: '<div class="tip-val">当前设置的报警温度：'+critical+'℃</div>请输入报警温度',
						placeholder: '请输入报警温度',
						title: '修改报警温度',
						critical: critical,
						callback: function(options){
							setCallBack(1,options)
						}
					}
				break
				case '2': 
					obj= {
						message: '<div class="tip-val">当前设置的烟感阈值：'+critical+'</div>请输入烟感阈值',
						placeholder: '请输入烟感阈值',
						title: '修改烟感阈值',
						critical: critical,
						callback: function(options){
							setCallBack(2,options)
						}
					}
				break
				case '3': 
					obj= {
						message: '<div class="tip-val">当前设置的设备总功率：'+critical+'w</div>请输入设备总功率',
						placeholder: '请输入设备总功率',
						title: '修改设备总功率',
						critical: critical,
						callback: function(options){
							setCallBack(3,options)
						}
					}
				break
			}
			promptFn(obj)
		}
		
	})

	function setCallBack(type,options){ //设置报警参数
		var value= $('.mui-popup-input input').val()//要提交设置的阈值
		if(options.index){
			$.ajax({
				url: '/setDeviceArgument',
				type: 'post',
				data: {
					code: CODE,
					type: type,
					value: value
				},
				success: function(res){
					console.log(res)
				},
				error: function(err){
					console.log(err)
				}
			})
		}
	}

	
	function getNowAlarmParmas(type){ //获取当前实时数据
		$.ajax({
			url: '/getDeviceNowArgument',
			type: 'post',
			data: {
				code: CODE,
				type: type
			},
			success: function(res){
				colsole.log(res)
			},
			error: function(err){
				console.log(err)
			}
		})
	}

	function promptFn(obj){
		var message= obj.message || ''
		var placeholder= obj.placeholder || '请输入'
		var title= obj.title || '提示'
		var btnValue= obj.btnValue || ['取消','确认']
		var callback= obj.callback || function(){}
		mui.prompt(message,placeholder,title,btnValue,callback)
		var $popupInput= $('.mui-popup-input input')
		$popupInput.val(obj.critical).removeAttr('autofocus').blur();
	}
})
</script>
</body>
</html>