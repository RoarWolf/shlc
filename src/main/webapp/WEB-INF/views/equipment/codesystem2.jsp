<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
   <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"/>
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
	<title>设备系统参数</title>
	<link rel="stylesheet" type="text/css" href="/css/base.css">
	<link rel="stylesheet" type="text/css" href="/css/parameterTable.css">
	<!--标准mui.css-->
	<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
	<link rel="stylesheet" href="/mui/css/mui.min.css">
	<script src="/mui/js/mui.min.js"></script>
	<script type="text/javascript" src="/js/jquery.js"></script>
	<script src="/js/my.js"></script>
	<style>
		
		.mask {
			position: fixed;
			left: 0;
			right: 0;
			bottom: 0;
			top: 0;
			background-color: rgba(0,0,0,.45);
			z-index: 850;
			display: none;
		}
		.more-device-wapper {
			position: fixed;
			z-index: 900;
			width: 85%;
			left: 50%;
			top: 45%;
			transform: translate(-50%,-50%);
			background-color: #fff;
			padding: 15px 10px;
			border-radius: 4px;
			display: none;
		}
		.more-device-wapper h3{ 
		    font-size: 15px;
		    text-align: center;
		    margin-top: 0px;
		    margin-bottom: 12px;
		    color: #333;
		    font-family: cursive;
		}
		.more-device-use {
			position: relative;
		}
		.more-device-use table {
			width: 100%;
			border-top: 1px solid #add9c0;
			border-left: 1px solid #add9c0;
		}
		.more-device-use tr {
			display: flex;
			text-align: center;
			font-size: 14px;
		}
		.t-header {
			position: absolute;
			left: 0;
			top: 0;
			right: 0;
			z-index: 1001;
		}
		.t-body {
			padding-top: 36px;
			max-height: 60vh;
			overflow-y: auto;
		}
		.more-device-use tr th:nth-child(1),
		.more-device-use tr th:nth-child(2),
		.more-device-use tr th:nth-child(3),
		.more-device-use tr td:nth-child(1),
		.more-device-use tr td:nth-child(2),
		.more-device-use tr td:nth-child(3){
			/* flex: 5; */
			width: 27.7777778%;
			overflow: hidden;
			white-space: nowrap;
    		text-overflow: ellipsis;
			
		}
		.more-device-use tr th:nth-child(4),
		.more-device-use tr td:nth-child(4),
		.more-device-use tr th:nth-child(5),
		.more-device-use tr td:nth-child(5){
			flex: 1;
			white-space: nowrap;
		    overflow: hidden;
		    text-overflow: ellipsis;
		}
		.more-device-use tr th {
			font-weight: bold;
			border-right: 1px solid #add9c0;
			border-bottom: 1px solid #add9c0;
			padding: 7px 0;
			background-color: #C8EFD4;
			color: #333;
		}
		.more-device-use tr td {
			border-right: 1px solid #add9c0;
			border-bottom: 1px solid #add9c0;
			padding: 7px 0;
			color: #666;
			background-color: #f5f7fa;
		}
		.more-device-use tr td .mui-checkbox{
	 	    margin: -7px 0;
			height: calc(100% + 12px);
		}
		.more-device-use tr td .mui-checkbox [name="checkbox1"] {
			top: 50%;
			right: 50%;
			transform: translate(50%, -50%);
		}
		.more-device-use .bottom-div {
			display: flex;
		    justify-content: space-around;
		    margin-top: 10px;
		}
		.more-device-use .bottom-div button {
			width:45%;
			padding: 5px 12px;
			font-size: 14px;
		}
		.more-device-wapper .t-status {
			display: none;
		}
		.more-device-wapper .t-status .t-success {
			color: #22B14C;
		}
		.more-device-wapper .t-status .t-error {
			color: #E0252C;
		}
		.more-device-wapper .t-loading {
			font-size: 18px;
			color: #4cd964;
			transform-origin: 8.5px 8.5px;
    		animation: T-Loading 2s linear infinite;
		}
		
		.device-end-wapper {
			position: fixed;
		    z-index: 900;
		    width: 85%;
		    left: 50%;
		    top: 45%;
		    transform: translate(-50%,-50%);
		    background-color: #fff;
		    padding: 15px 10px;
		    border-radius: 4px;
		    display: none;
		}
		.device-end-wapper .use-title {
			font-size: 15px;
		    text-align: center;
		    margin-top: 0px;
		    margin-bottom: 12px;
		    color: #333;
		    font-family: cursive;
		}
		.device-end-wapper .end-item {
			padding: 5px 0;
			margin-bottom: 15px;
			font-size: 14px;
		}
		.device-end-wapper .end-item .end-success {
			color: #22B14C;
		}
		.device-end-wapper .end-item .end-error {
			color: #E0252C;
		}
		@keyframes T-Loading{
			0% {
				transform: rotate(0deg);
			}
			100% {
				transform: rotate(360deg);
			}
		}
	</style>
</head>
<body>
	<input type="hidden" id="code" value="${code }">
	<input type="hidden" id="hwVerson" value="${hwVerson }">
	<input type="hidden" id="merid" value="${merid }">
	<div class="ptb">
		<div class="switchContent">
			<h3>功能开关</h3>
			<ul class="mui-table-view">
			    <li class="mui-table-view-cell">
			    	<span>是否余额回收</span>
				    <div class="mui-switch mui-switch-mini ${sysparam.spRecMon == 1 ? 'mui-active' : ''}" id="isBalanceRec">
					  <div class="mui-switch-handle"></div>
					</div>
			    </li>
			    <li class="mui-table-view-cell">
			    	<span>是否断电自停</span>
				    <div class="mui-switch mui-switch-mini ${sysparam.spFullEmpty == 1 ? 'mui-active' : ''}" id="isPowerFailure">
					  <div class="mui-switch-handle"></div>
					</div>
			    </li>
			</ul>
		</div>
		<div class="detail">
			<h3>参数配置</h3>
			<ul class="mui-table-view">
			    <li class="mui-table-view-cell"> <a class="mui-navigate-right">刷卡投币规则</a></li>
			   		<div class="icon-reg">
			   			<ol>
			   				<span>投币充电时间</span> 
			   				<div class="ol-div">
			   					<input type="search" name="coinMin" data-min="0" data-max="999" value='${sysparam.coinMin == null ? 240 : sysparam.coinMin}'>分钟
			   				</div>
			   			</ol>
			   			<ol>
			   				<span>刷卡充电时间</span> 
			   				<div class="ol-div">
			   					<input type="search" name="cardMin" data-min="0" data-max="999" value='${sysparam.cardMin == null ? 240 : sysparam.cardMin}'>分钟
			   				</div>
			   			</ol>
			   			<ol>
			   				<span>单次投币最大用电量</span> 
			   				<div class="ol-div">
			   					<input type="search" name="coinElec" data-min="0.1" data-max="15" value='${sysparam.coinElec == null ? 1 : sysparam.coinElec}'>度
			   				</div>
			   			</ol>
			   			<ol>
			   				<span>单次刷卡最大用电量</span> 
			   				<div class="ol-div">
			   					<input type="search" name="cardElec" data-min="0.1" data-max="15" value='${sysparam.cardElec == null ? 1 : sysparam.cardElec}'>度
			   				</div>
			   			</ol>
			   			<ol>
			   				<span>刷卡扣费金额</span> 
			   				<div class="ol-div">
			   					<input type="search" name="cst" data-min="0.1" data-max="15" value='${sysparam.cst == null ? 1 : sysparam.cst}'>元
			   				</div>
			   			</ol>
			   			<!-- <ol class="last-ol"><div class="confirm">确认</div><div>取消</div></ol> -->
			   		</div>
			   <li class="mui-table-view-cell"> <a class="mui-navigate-right">功率档位匹配</a></li>
			   		<div class="icon-reg">
			   			<ol>
			   				<span>一档最大充电功率</span> 
			   				<div class="ol-div">
			   					<input type="search" name="powerMax1" data-min="50" data-max="8000" value='${sysparam.powerMax1 == null ? 200 : sysparam.powerMax1}'>瓦
			   				</div>
			   			</ol>
			   			<ol>
			   				<span>二档最大充电功率</span> 
			   				<div class="ol-div">
			   					<input type="search" name="powerMax2" data-min="50" data-max="8000" value='${sysparam.powerMax2 == null ? 400 : sysparam.powerMax2}'>瓦
			   				</div>
			   			</ol>
			   			<ol>
			   				<span>三档最大充电功率</span> 
			   				<div class="ol-div">
			   					<input type="search" name="powerMax3" data-min="50" data-max="8000" value='${sysparam.powerMax3 == null ? 600 : sysparam.powerMax3}'>瓦
			   				</div>
			   			</ol>
			   			<ol>
			   				<span>四档最大充电功率</span> 
			   				<div class="ol-div">
			   					<input type="search" name="powerMax4" data-min="50" data-max="8000" value='${sysparam.powerMax4 == null ? 800 : sysparam.powerMax4}'>瓦
			   				</div>
			   			</ol>
			   			<ol>
			   				<span>二档充电时间百分比</span> 
			   				<div class="ol-div">
			   					<input type="search" name="powerTim2" data-min="1" data-max="100" value='${sysparam.powerTim2 == null ? 75 : sysparam.powerTim2}'>%
			   				</div>
			   			</ol>
			   			<ol>
			   				<span>三档充电时间百分比</span> 
			   				<div class="ol-div">
			   					<input type="search" name="powerTim3" data-min="1" data-max="100" value='${sysparam.powerTim3 == null ? 50 : sysparam.powerTim3}'>%
			   				</div>
			   			</ol>
			   			<ol>
			   				<span>四档充电时间百分比</span> 
			   				<div class="ol-div">
			   					<input type="search" name="powerTim4" data-min="1" data-max="100" value='${sysparam.powerTim4 == null ? 25 : sysparam.powerTim4}'>%
			   				</div>
			   			</ol>
			   			<!-- <ol class="last-ol"><div class="confirm">确认</div><div>取消</div></ol> -->
			   		</div>
			   		
			
			   <li class="mui-table-view-cell"> <a class="mui-navigate-right">其他配置</a></li>
			   		<div class="icon-reg">
			   			<ol>
			   				<span>是否初始显示电量</span> 
			   				 <div class="mui-switch mui-switch-mini ${sysparam.elecTimeFirst == 1 ? 'mui-active' : ''}" data-min="0" data-max="1" id="isShowFirstPower">
								 <div class="mui-switch-handle"></div>
							 </div>
			   			</ol>
			   			<ol>
			   				<span>最大浮充功率</span> 
			   				<div class="ol-div">
			   					<input type="search" name="fullPowerMin" data-min="10" data-max="200" value='${sysparam.fullPowerMin == null ? 30 : sysparam.fullPowerMin}'>瓦
			   				</div>
			   			</ol>
			   			<ol>
			   				<span>浮充时间</span> 
			   				<div class="ol-div">
			   					<input type="search" name="fullChargeTime" data-min="30" data-max="240" value='${sysparam.fullChargeTime == null ? 120 : sysparam.fullChargeTime}'>分钟
			   				</div>
			   			</ol>
			   			
			   		</div>
			  
			</ul>
		</div>
	</div>
	
	<div class="mask"></div>
	<div class="more-device-wapper">
		<h3 class="t-select">选中设备复用此系统参数</h3>
		<h3 class="t-status">正在依次复用此系统参数</h3>
		<div class="more-device-use">
			<div class="t-header">
				<table>
					<thead>
						<tr>
							<th>设备号</th>
							<th>设别名</th>
							<th>所属小区</th>
							<th class="t-select">选择</th>
							<th class="t-status">状态</th>
						</tr>
					</thead>
				</table>
			</div>
			<div class="t-body scroll">
				<table>
					<tbody>
						
					</tbody>
				</table>
			</div>
			<div class="bottom-div t-select">
				<button type="button" class="mui-btn" id="cancelUse">取消复用</button>
				<button type="button" class="mui-btn mui-btn-success" id="handleUse">立即复用</button>
			</div>
		</div>
	</div>
	<div class="device-end-wapper">
		<div class="use-title">批量设置系统参数完成</div>
		<div class="end-item">
			<strong>设置成功：</strong>
			<span class="end-success"></span>
		</div>
		<div class="end-item">
			<strong>设置失败：</strong>
			<span class="end-error"></span>
		</div>
	</div>
	
	<footer>
		<div class="getDataBut">获取参数</div>
		<div class="more-device-par">复用参数</div>
		<div class="setDataBut">保存参数</div>
	</footer>	
	<script type="text/javascript" src="/js/parameterTable.js"></script>
</body>
</html>
<script>
$(function() {
	var overscroll = function(el) {
	  el.addEventListener('touchstart', function() {
	    var top = el.scrollTop
	      , totalScroll = el.scrollHeight
	      , currentScroll = top + el.offsetHeight;

	    if(top === 0) {
	      el.scrollTop = 1;
	    } else if(currentScroll === totalScroll) {
	      el.scrollTop = top - 1;
	    }
	  });
	  el.addEventListener('touchmove', function(evt) {
	    if(el.offsetHeight < el.scrollHeight)
	      evt._isScroller = true;
	  });
	}
	overscroll(document.querySelector('.scroll'));
	document.body.addEventListener('touchmove', function(evt) {
	  if(!evt._isScroller) {
	    evt.preventDefault();
	  }
	});
	$('.getDataBut').click(function(e){
		$.bootstrapLoading.start({ loadingTips: "正在获取数据..." });
		$.ajax({
			url : '${hdpath}/readsysteminfo',
			data : {
				code : $("#code").val()
			},
			type : "POST",
			cache : false,
			success : function(data) {
				if (data.wolfcode == "1001") {
					mui.alert(data.wolfmsg);
				} else {
					// 这一步所有的input都已经通过验证了，然后需要获取是否显示电量的switch
					var elecTimeFirst= $("#isShowFirstPower").hasClass('mui-active') ? 1 : 0;   //是否初始显示电量 
					/*发送ajax向后台传输数据,(获取每个input的数据)*/
					var spRecMon= $("#isBalanceRec").hasClass('mui-active') ? 1 : 0;  //是否回收
					var spFullEmpty= $("#isPowerFailure").hasClass('mui-active') ? 1 : 0; //获取 是否断电自停
					//这一步要获取所有的input 的数据进行上传数据操作
					
					$('input[name="coinMin"]').val(data.param1);
					$('input[name="cardMin"]').val(data.param2);
					$('input[name="coinElec"]').val(data.param3/10);
					$('input[name="cardElec"]').val(data.param4/10);
					$('input[name="cst"]').val(data.param5/10);
					$('input[name="fullPowerMin"]').val(data.param15);
					$('input[name="fullChargeTime"]').val(data.param16);
					$('input[name="powerMax1"]').val(data.param6);
					$('input[name="powerMax2"]').val(data.param7);
					$('input[name="powerMax3"]').val(data.param8);
					$('input[name="powerMax4"]').val(data.param9);
					$('input[name="powerTim2"]').val(data.param10);
					$('input[name="powerTim3"]').val(data.param11);
					$('input[name="powerTim4"]').val(data.param12);
					if (data.param13 == 1) {
						$("#isBalanceRec").removeClass("mui-active");
						$("#isBalanceRec").addClass("mui-active");
					} else {
						$("#isBalanceRec").removeClass("mui-active");
					}
					if (data.param14 == 1) {
						$("#isPowerFailure").removeClass("mui-active");
						$("#isPowerFailure").addClass("mui-active");
					} else {
						$("#isPowerFailure").removeClass("mui-active");
					}
					if (data.param17 == 1) {
						$("#isShowFirstPower").removeClass("mui-active");
						$("#isShowFirstPower").addClass("mui-active");
					} else {
						$("#isShowFirstPower").removeClass("mui-active");
					}
					$("#timespan").html(data.readtime);
				}
			},//返回数据填充
			complete: function () {
	            $.bootstrapLoading.end();
	        }
		}); 
	}).trigger('click')
})
</script>