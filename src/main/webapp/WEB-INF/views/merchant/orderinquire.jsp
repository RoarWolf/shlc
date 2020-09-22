<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<title>订单统计</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<link rel="stylesheet" href="${hdpath }/mui/css/mui.picker.min.css">
<link rel="stylesheet" href="${hdpath }/mui/css/mui.poppicker.css">
<link rel="stylesheet" href="${hdpath }/css/base.css">
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="${hdpath}/js/jquery.js"></script>
<script src="${hdpath }/mui/js/mui.min.js"></script>
<script src="${hdpath }/mui/js/mui.picker.min.js"></script>
<script src="${hdpath }/mui/js/mui.poppicker.js"></script>
<style type="text/css">
@font-face {font-family: "iconfont";
  src: url('//at.alicdn.com/t/font_1345561_1k061x5djr1.eot?t=1597300707181'); /* IE9 */
  src: url('//at.alicdn.com/t/font_1345561_1k061x5djr1.eot?t=1597300707181#iefix') format('embedded-opentype'), /* IE6-IE8 */
  url('data:application/x-font-woff2;charset=utf-8;base64,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') format('woff2'),
  url('//at.alicdn.com/t/font_1345561_1k061x5djr1.woff?t=1597300707181') format('woff'),
  url('//at.alicdn.com/t/font_1345561_1k061x5djr1.ttf?t=1597300707181') format('truetype'), /* chrome, firefox, opera, Safari, Android, iOS 4.2+ */
  url('//at.alicdn.com/t/font_1345561_1k061x5djr1.svg?t=1597300707181#iconfont') format('svg'); /* iOS 4.1- */
}

.iconfont {
  font-family: "iconfont" !important;
  font-size: 16px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.icon-yinlian1:before {
  content: "\e646";
}

.icon-wodedingdan:before {
  content: "\e679";
}

.icon-qianbao:before {
  content: "\e613";
}

.icon-shaixuan:before {
  content: "\e686";
}

.icon-seach:before {
  content: "\e624";
}

.icon-qian2:before {
  content: "\e644";
}

.icon-big-Pay:before {
  content: "\e6ec";
}

.icon-weixin:before {
  content: "\e7b4";
}


</style>
<style>
		.app {
			width: 100%;
			height: 100%;
			color: #666;
			font-size: 0.5973rem;
		}
		.app a {
			text-decoration: none;
		}
		.app p {
			font-size: 0.5973rem;
		}
		.mui-off-canvas-wrap.mui-active .mui-off-canvas-backdrop {
			position: fixed;
		}
		.mui-scroll-wrapper {
			overflow-y: auto;
		}
		.mui-content .mui-scroll {
			color: #333;
			padding-top: 1.92rem; 
		}
		.appDiv {
			font-size: 0.5973rem;
			color: #666;
		}
		 .top {
			position: fixed;
			left: 0;
			top: 0;
			width: 100%;
			z-index: 3;
		}
		 .top>div{
			height: 1.92rem;
			background-color: #fff;
			line-height: 1.92rem;
			padding: 0 0.64rem;
			display: flex;
			justify-content: space-between;
			align-items: center;
		}
		 .top p:first-child{
			color: #333;
			font-weight: 700;
		}
		 .top p span{
			display: inline-block;
			padding: 0 0.2133rem;
		}
		 .top p i {
			font-size: 0.8533rem;
			color: #333;
		}
		 .top .se {
			position: absolute;
			width: 100vw;
			right: -100vw;
			top: 0;
		}
		 .top .se input[type=search]{
			width: 85%;
			border-radius: 1.45067rem;
			background-color: #efeff4;
			padding-left: 1.4933rem;
			margin: 0;
		}
		 .top .se input[type=search]::-webkit-input-placeholder {
			color: #c8c7cc;
		}
		 .top .se input[type=search]::-moz-placeholder{
			color: #c8c7cc;
		}
		 .top .se input[type=search]:-ms-input-placeholder{
			color: #c8c7cc;
		}
		 .top .se i.icon-seach {
			position: absolute;
			left: 1.0667rem;
			top: 1px;
			color: #c8c7cc;
			font-size: 0.8533rem;
		}
		 .top .se .closeSpan {
			width: 15%;
			text-align: center;
			color: #2B73D5;
		}

		 .appDiv .content {
			/*padding: 15px 15px 0;*/
			padding-top: 0.64rem;
		}
		.appDiv .content ul li a {
			background-color: #f5f7fa;
			margin-bottom: 0.42667rem;
			padding: 0.42667rem;
			display: flex;
			justify-content: space-between;
			align-items: center;
		}
		.appDiv .content ul li .left {
			width: 1.92rem;
			height: 1.92rem;
			/* background-color: pink; */
		}
		.appDiv .content ul li .left i {
			font-size: 1.92rem;
			line-height: 1.92rem;
		}
		.appDiv .content ul li .left i.icon-weixin {
			color: #22B14C;
		}
		.appDiv .content ul li .left i.icon-big-Pay {
			color: #06B4FD;
		}
		.appDiv .content ul li .left i.icon-qianbao {
			/* color: #e35180; */
			color: #E4BB3C;
		}
		.appDiv .content ul li .right{
			width: calc(100% - 2.3467rem);
			
		}
		.appDiv .content ul li p {
			line-height: 2em;
		}
		.appDiv .content ul li .right .orderInfo {
			float: left;
			max-width: 70%;
		}
		.appDiv .content ul li .right .orderInfo strong {
			color: #333;
		}
		.appDiv .content ul li .right .orderInfo .pOrder {
			color: #666;
			overflow: hidden;
			white-space: nowrap;
			text-overflow: ellipsis;
		}
		.appDiv .content ul li .right .orderInfo .pTime {
			color: #bbb;
			width: 100%;
		}
		.appDiv .content ul li .right .moneyInfo {
			overflow: hidden;
			text-align: right;
		}
		
		.appDiv .content ul li .payMoney span {
			color: #22B14C;
			font-weight: 700;
			font-size: 0.64rem
		}
		.appDiv .content ul li .payMoney span.red {
			color: #F3686D;
		}
		/* .appDiv .content ul li .right .moneyInfo p span {
			color: #22B14C;
			font-weight: 700;
			font-size: 0.64rem;
		}
		.appDiv .content ul li .right .moneyInfo p span.red {
			color: #F3686D;
		} */
		.appDiv .content ul li .right .moneyInfo .payMoney {
			color: #333;
		}
		.appDiv .content ul li .right .moneyInfo .orderStatus {

		}
		.appDiv .noDataTip {
			width: 100%;
			height: 70vh;
			display: flex;
			flex-direction: column;
			justify-content: center;
		}
		.appDiv .noDataTip >div {
			text-align: center;
			font-size: 0.8533rem;
			font-weight: 700;
			color: #333;
			margin-bottom: 0.8533rem;
		}
		.appDiv .noDataTip >div i {
			font-size: 2.1333rem;
			line-height: 2.1333rem;
			color: #51BF72;
		}
		aside .mui-scroll-wrapper {
			overflow-y: auto;
			
		}
		
		aside .mui-scroll-wrapper .mui-scroll {
			padding-bottom: 2.1333rem;
		} 
		.mui-off-canvas-left, .mui-off-canvas-right {
			background-color: #EFEFF4;
		}
		.mui-table-view:before,
		.mui-table-view:after,
		.mui-table-view-cell:after, 
		.mui-table-view-cell>a:after {
			background-color: transparent;
		}
		.mui-table-view-cell>a:after {
			transform: rotate(90deg);
			top: 34%;
		}
		.mui-table-view {
			margin-bottom: 0.42667rem;
		}
		.mui-table-view-cell,.mui-table-view-cell>a {
			font-size: 0.5973rem;
			color: #333;
			font-weight: 700;
		}
		.mui-table-view-cell input {
			color: #666;
			font-weight: 400;
			margin: 0;
			background-color: #f0f0f0;
		}
		.cLi.mui-active {
			background-color: transparent;
		}
		input[type=search]::-webkit-input-placeholder {
			color: #a8a8a8;
			font-size: 0.5973rem;
			font-weight: 400;
		}
		input[type=search]::-moz-placeholder{
			color: #a8a8a8;
			font-size: 0.5973rem;
			font-weight: 400;
		}
		input[type=search]:-ms-input-placeholder{
			color: #a8a8a8;
			font-size: 0.5973rem;
			font-weight: 400;
		}
		.mui-table-view-cell .selectUl {
			display: flex;
			justify-content: flex-start;
			flex-wrap: wrap;
		}
		.mui-table-view-cell .selectUl li{
			width: 30%;
			color: #666;
			text-align: center;
			height: 1.6213rem;
			line-height: 1.6213rem;
			background-color: #F0F0F0;
			border-radius: 0.2133rem;
			margin-left: 5%;
			font-weight: 400;
			color: #000;
			margin-bottom: 0.42667rem;
		}
		.mui-table-view-cell .selectUl li:nth-child(3n-2){
			margin-left: 0;
		}
		.mui-table-view .createTimeLi {
			display: flex;
			justify-content: center;
			align-items: center;
		}
		.mui-table-view .createTimeLi>div {
			width: 100%;
		}
		.confirmUl {
			width: 100%;
			height: 2.1333rem;
			position: absolute;
			left: 0;
			bottom: 0;
			margin-bottom: 0;
			line-height: 2.1333rem;
			display: flex;
			justify-content: center;
			z-index: 2;
		}
		.confirmUl>div {
			width: 50%;
			text-align: center;
			color: #333;
		}
		.confirmUl>div.confirmDiv {
			background-color: #22B14C;
			color: #fff;
		}
		.mui-off-canvas-right .cLi{
			display: none;
		}
		.payStyleUl li i {
			font-size: 1.1093rem;
			/* color: #e35180; */
			color: #E4BB3C;
		}
		.payStyleUl li i.icon-weixin{
			color: #22B14C;
		}
		.payStyleUl li i.icon-big-Pay{
			color: #06B4FD;
		}
		.payStyleUl li.box-center {
			display: flex;
			align-items: center;
			justify-content: center;
		}
		.payStyleUl li .yinlian-select {
			width: 26px;
			height: 26px;
		}
		aside .mui-scroll .selectUl li.active {
			background-color: #22B14C;
			color: #fff;
		}
		aside .mui-scroll .selectUl li.active i {
			color: #fff;
		}
		
		@media screen and (max-width : 300px){
			/* 分辨率小于300隐藏 */
			.el_hide_l30{
				display: none;
			}
			.appDiv .content ul li .right .orderInfo {
				max-width: initial;
			}
		}
		
		@media screen and (min-width : 300px){
			/* 分辨率大于300隐藏 */
			.el_hide_r300{
				display: none;
			}
		}
		.yinlian-box {
			height: 100%;
		}
</style>
<body>
 <div class="app">
		<!-- 侧滑导航根容器 -->
		<div class="mui-off-canvas-wrap mui-draggable mui-slide-in">
		  <!-- 菜单容器 -->
		  <aside class="mui-off-canvas-right">
		    <div class="mui-scroll-wrapper" id="offCanvasSideScroll">
		      <div class="mui-scroll">
		        <!-- 菜单具体展示内容 -->
		        <ul class="mui-table-view">
				    <li class="mui-table-view-cell">
				        <a class="mui-navigate-right orderA">订单号</a>
				    </li>
				    <li class="mui-table-view-cell cLi">
				       <input type="search" placeholder="请输入订单号" name="orderNumInput">
				    </li>
				</ul>
				 <ul class="mui-table-view">
				    <li class="mui-table-view-cell">
				        <a class="mui-navigate-right numA">设备编号</a>
				    </li>
				    <li class="mui-table-view-cell cLi">
				        <input type="search" placeholder="请输入设备编号" name="deviceNumInput">
				    </li>
				</ul> 
				 <ul class="mui-table-view">
				    <li class="mui-table-view-cell">
				        <a class="mui-navigate-right statusA">订单状态</a>
				    </li>
				    <li class="mui-table-view-cell cLi">
				       	<ul class="selectUl orderStatusUl">
				       		<li data-val="" >全部</li>
				       		<li data-val=1 >正常</li>
				       		<li data-val=2 >退款</li>
				       	</ul>
				    </li>
				</ul>

				<ul class="mui-table-view">
				    <li class="mui-table-view-cell">
				        <a class="mui-navigate-right typeA">支付方式</a>
				    </li>
				    <li class="mui-table-view-cell cLi">
				       	<ul class="selectUl payStyleUl">
				       		<li data-val="" >全部</li>
				       		<li data-val=1 ><i class="iconfont icon-qianbao"></i></li>
				       		<li data-val=2 ><i class="iconfont icon-weixin"></i></li>
				       		<li data-val=3 ><i class="iconfont icon-big-Pay"></i></li>
				       		<li data-val=4 class="box-center">
								<img class="yinlian-select" src="${hdpath}/hdfile/images/yinlian.png" />
							</li>
				       		
				       	</ul>
				    </li>
				</ul>

				<ul class="mui-table-view">
				    <li class="mui-table-view-cell">
				        <a class="mui-navigate-right createTimeA">创建时间</a>
				    </li>
				    <li class="mui-table-view-cell cLi createTimeLi">
				       <div><input type="search" placeholder="请选择起始时间"  class="startTime"></div>
				       至
				       <div><input type="search" placeholder="请选择结束时间"  class="endTime"></div>	
				    </li>
				</ul>

		      </div>
		    </div>
		    <ul class="mui-table-view confirmUl">
				<div class="closeDiv">取消</div>
				<div class="confirmDiv">确定</div>
			</ul>
		  </aside>
		  <!-- 主页面容器 -->
		  <div class="mui-inner-wrap">
		    <!-- 主页面标题 -->
		   	<div class="top">
				<div class="fi">
					<p>订单记录</p>
					<p>
						<span id="gradSpan"><i class="iconfont icon-shaixuan"></i></span>
						<span id="searchSpan"><i class="iconfont icon-seach"></i></span>
					</p>
				</div>
				<div class="se">
					 <input type="search" class="mui-input-clear" placeholder="请输入订单号" id="topSearch">
					 <i class="iconfont icon-seach"></i>
					<span class="closeSpan">取消</span>
				</div>
		    </div>
		     <div class="mui-off-canvas-backdrop"></div>
		    <div class="mui-content mui-scroll-wrapper" id="offCanvasContentScroll">
		    	
		      <div class="mui-scroll">
		        <!-- 主界面具体展示内容 -->
		       <div class="appDiv">
		       
		       	<div class="content">

		       		<ul>
		       			<c:if test="${fn:length(tradelist) <= 0}" >
			       			<div class="noDataTip">
   								<div><i class="iconfont icon-wodedingdan"></i></div>
   								<div>暂无订单记录</div>
			       			</div> 
		       			</c:if>
		       			<c:forEach items="${tradelist}" var="item">
						<li>
							<a href="javascript:void(0);" hrefStr="/merchant/orderinquiredetails?ordernum=${item.ordernum}&orderid=${item.id}">
								<div class="left">
									<c:if test="${item.paytype==1 || item.paytype==6}">
										<i class="iconfont icon-qianbao"></i>
									</c:if>
									<c:if test="${item.paytype==2 || item.paytype==4}">
										<i class="iconfont icon-weixin"></i>
									</c:if>
									<c:if test="${item.paytype==3 || item.paytype==5}">
										<i class="iconfont icon-big-Pay"></i>
									</c:if>
									<c:if test="${item.paytype==12 || item.paytype==13}">
										<img class="yinlian-box" src="${hdpath}/hdfile/images/yinlian.png" />
									</c:if>
								</div>
								<div class="right">
									<div class="orderInfo">
										<p class="pOrder">${item.ordernum}</p>
										<strong>${item.code}</strong>
										<!-- 屏幕分辨率小于300显示 -->
										<c:if test="${item.status==1}">
											<p class="payMoney el_hide_r300">支付<span>¥${item.money}元</span></p>
										</c:if>
										<c:if test="${item.status==2}">
											<p class="payMoney el_hide_r300">退款<span class="red">¥${item.money}元</span></p>
										</c:if>
										<c:if test="${item.status==1}">
											<p class="orderStatus el_hide_r300">订单完成</p>
										</c:if>
										<c:if test="${item.status==2}">
											<p class="orderStatus el_hide_r300">退款完成</p>
										</c:if>
										<!-- 屏幕分辨率小于300显示 -->
										<p class="pTime"><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
									</div>
									<div class="moneyInfo el_hide_l30">
										<c:if test="${item.status==1}">
											<p class="payMoney">支付<span>¥${item.money}元</span></p>
										</c:if>
										<c:if test="${item.status==2}">
											<p class="payMoney">退款<span class="red">¥${item.money}元</span></p>
										</c:if>
										
										<br>
										<c:if test="${item.status==1}">
											<p class="orderStatus">订单完成</p>
										</c:if>
										<c:if test="${item.status==2}">
											<p class="orderStatus">退款完成</p>
										</c:if>
									</div>	
							
								</div>
							</a>
						</li>
						</c:forEach>
								
		       		</ul> 
		       	</div>
		       
		       </div>
		    
		      </div>
		    </div>  
		  </div>
		</div>
	
		
	</div>


	<script>
	$(function(){
		var htmlwidth = document.documentElement.clientWidth || document.body.clientWidth;
	    var fontSize= htmlwidth/16
	    /* var style= document.createElement('style')
	    style.innerHTML= 'html { font-size: '+fontSize+'px !important;}'
	    var head= document.getElementsByTagName('head')[0]
	    head.insertBefore(style,head.children[0]) */
	    var styleStr= "<style>html { font-size: "+fontSize+"px !important;}</style>"
	    $('head').children().eq(0).before(styleStr)

		 mui('#offCanvasSideScroll').scroll();    
         mui('#offCanvasContentScroll').scroll();   
		var liStatus= {
			orderA: 2,
			numA: 2,
			statusA: 2,
			typeA: 2,
			createTimeA: 2
		}
		
		var dtPicker = new mui.DtPicker({
			type: 'datetime',
			endDate: new Date()

		}); //初始化时间插件

		//获取屏幕的宽度
		var screenWidth= $('body').width()
		$('#gradSpan').on('tap',function(){
			console.log(11556)
			mui('.mui-off-canvas-wrap').offCanvas().show();
		})
		$('#searchSpan').on('tap',function(){
			$('.top .se').animate({
				left: 0
			})
		})
		$('.closeSpan').on('tap',function(){
			$('#topSearch').val('')
			$('.top .se').animate({
				left: screenWidth
			},0)
		})
		
		$('.createTimeLi input').on('tap',function(e){ //点击时间输入框
			e= e || window.event
			e.preventDefault()
			var that= this
			dtPicker.show(function (selectItems) {
				var time= selectItems.text+':00'
				$(that).val(time)
		    })
		})

		$('aside .mui-scroll>ul>li>a').on('tap',function(){
			if($(this).hasClass('orderA')){
				liStatus.orderA= liStatus.orderA== 1 ? 2 : 1 
			}else if($(this).hasClass('numA')){
				liStatus.numA= liStatus.numA== 1 ? 2 : 1 
			}else if($(this).hasClass('statusA')){
				liStatus.statusA= liStatus.statusA== 1 ? 2 : 1 
			}else if($(this).hasClass('typeA')){
				liStatus.typeA= liStatus.typeA== 1 ? 2 : 1 
			}else if($(this).hasClass('createTimeA')){
				liStatus.createTimeA= liStatus.createTimeA== 1 ? 2 : 1 
			}
			changeStatus(liStatus)
		})
		// 选择搜索选择框 ,选中按钮
		$('.selectUl li').on('tap',function(){
			$(this).siblings().removeClass('active')
			$(this).addClass('active')
		})

		$('.closeDiv').on('tap',function(){
			$('aside input').val('')
			$('aside .selectUl li').removeClass('active')
			liStatus= {
				orderA: 2,
				numA: 2,
				statusA: 2,
				typeA: 2,
				createTimeA: 2
			}
			changeStatus(liStatus)
			mui('.mui-off-canvas-wrap').offCanvas().close();
		})
			/*keypress*/
		$('#topSearch').on('keypress',function(e){
			if(e.keyCode === 13){
				/*获取input中订单号*/
				var orderNum= $('#topSearch').val().trim()
				if(orderNum== null || orderNum== ''){
					mui.toast('输入的订单号不能为空',{ duration:1500, type:'div' }) 
					return 
				}
				if(!/^\d+$/.test(orderNum)){
					mui.toast('请输入正确的订单号',{ duration:1500, type:'div' }) 
					return 
				}
				$('#offCanvasContentScroll .mui-scroll').css("transform","translate3d(0px, 0px, 0px) translateZ(0px);")
				$('.appDiv .content>ul').html('')
				$.ajax({
					url: '/merchant/queryTradeByParam',
					data: {
						ordernum: orderNum,
						code: '',
						status: '',
						paytype: '',
						startTime: '',
						endTime: ''
					},
					type: 'POST',
					success: function(res){
						renderData(res)
					},
					error: function(err){
						console.log(err)
					}
				})
			}
		})
		
		$('.confirmDiv').on('tap',function(){ //点击搜索
			var ordernum= $('aside input[name="orderNumInput"]').val().trim() //订单编号
			var code= $('aside input[name="deviceNumInput"]').val().trim() //设备编号
			var status= ''
			if($('.orderStatusUl li.active').length > 0){
				status= $('.orderStatusUl li.active').attr('data-val').trim()
			}
			var paytype= ''
			if($('.payStyleUl li.active').length > 0){
				paytype= $('.payStyleUl li.active').attr('data-val').trim()
			}
			var date= new Date()
			var year= date.getFullYear()
			var month= date.getMonth()+1 >= 10 ? date.getMonth()+1 : '0'+(date.getMonth()+1)
			var day= date.getDate() >= 10 ? date.getDate() : '0'+date.getDate()
			var hours= date.getHours() >= 10 ? date.getHours() : '0'+date.getHours()
			var mi= date.getMinutes() >= 10 ? date.getMinutes() : '0'+date.getMinutes()
			var s= date.getSeconds() >= 10 ? date.getSeconds() : '0'+date.getSeconds()
			var startTime= year+'-'+month+'-'+day+' 00:00:00'
			var endTime= year+'-'+month+'-'+day+' '+hours+':'+mi+':'+s
			if($('aside .startTime').val().trim() != '' && $('aside .startTime').val().trim() != null){
				startTime= $('aside .startTime').val().trim()
			}
			if($('aside .endTime').val().trim() != '' && $('aside .endTime').val().trim() != null){
				endTime= $('aside .endTime').val().trim()
			}
			mui('.mui-off-canvas-wrap').offCanvas().close();
			$('#offCanvasContentScroll .mui-scroll').css("transform","translate3d(0px, 0px, 0px) translateZ(0px);")
			$('.appDiv .content>ul').html('')
			$.ajax({
				url: '/merchant/queryTradeByParam',
				data: {
					ordernum: ordernum,
					code: code,
					status: status,
					paytype: paytype,
					startTime: startTime,
					endTime: endTime
				},
				type: 'post',
				success: function(res){
					console.log(res)
					renderData(res)
				},
				error: function(err){
					console.log(err)
				}
			})
		})
		
		function renderData(list){
			if(list.length <= 0){
				var tipStr= '<div class="noDataTip"><div><i class="iconfont icon-wodedingdan"></i></div><div>暂无订单记录</div></div>'
				$('.appDiv .content>ul').html($(tipStr))
				return
			}
			var fragment= document.createDocumentFragment()
			$(list).each(function(i,item){
				var paytypeStr= ''
				if(item.paytype == 1 || item.paytype == 6){
					paytypeStr= '<i class="iconfont icon-qianbao"></i>'
				}else if(item.paytype == 2 || item.paytype == 4) {
					paytypeStr= '<i class="iconfont icon-weixin"></i>'
				}else if(item.paytype == 3 || item.paytype == 5){
					paytypeStr= '<i class="iconfont icon-big-Pay"></i>'
				}else if(item.paytype == 12 || item.paytype == 13){
					paytypeStr= '<img class="yinlian-box" src="${hdpath}/hdfile/images/yinlian.png" />'
				}
	
				var payMoneyStr= ''
				var orderStatusStr= ''
				var elHideR300Sstr= '' //这是是屏幕分辨率小于300显示的
				if(item.status == 1) {
					payMoneyStr= '支付<span>¥'+item.money+'元</span></i>'
					orderStatusStr= '<p class="orderStatus">订单完成</p>'
					elHideR300Sstr='<p class="payMoney el_hide_r300">支付<span>¥'+item.money+'元</span></p><p class="orderStatus el_hide_r300">订单完成</p>'
				}else if(item.status == 2){
					payMoneyStr= '退款<span class="red">¥'+item.money+'元</span>'
					orderStatusStr= '<p class="orderStatus">退款完成</p>'
					elHideR300Sstr='<p class="payMoney el_hide_r300">退款<span class="red">¥'+item.money+'元</span></p><p class="orderStatus el_hide_r300">退款完成</p>'
				}
				
				var str= '<li><a href="javascript:void(0);" hrefStr="/merchant/orderinquiredetails?ordernum='+item.ordernum+'&orderid='+item.id+'"><div class="left">'+paytypeStr+'</div><div class="right"><div class="orderInfo"><p class="pOrder">'+item.ordernum+'</p><strong>'+item.code+'</strong>'+elHideR300Sstr+'<p class="pTime">'+item.remark+'</p></div><div class="moneyInfo el_hide_l30"><p class="payMoney">'+payMoneyStr+'</p><br>'+orderStatusStr+'</div></div></a></li>'
				fragment.appendChild($(str)[0])
			})
			$('.appDiv .content>ul')[0].appendChild(fragment)
		}
		
		mui('#offCanvasContentScroll').on('tap','a',function(){document.location.href=this.href;});
		
		$('.appDiv .content ul').on('tap','a',function(e){
			e= e || window.event
			e.returnValue= false
			var target= e.target || e.srcElement
			console.log(window.location.host+$(this).attr('hrefStr'))
			var hr= $(this).attr('hrefStr')
			 if (/(Android)/i.test(navigator.userAgent)){
				 var arrList= [{ulHtml:$('.appDiv .content ul').html(),scrollStyle: $('#offCanvasContentScroll .mui-scroll').css('transform')}]
				 sessionStorage.setItem("ulCon",JSON.stringify(arrList)) 
			 }
			setTimeout(function(){
				window.location.href = hr
			},100)
			
			
		})

		function changeStatus(liStatus){ //改变状态 1是开，2是关
			for(var key in liStatus){
				var $cLi= $('.'+key).parent().parent().find('.cLi')
				if(liStatus[key] == 1){
					$cLi.slideDown(200)
				}else{
					$cLi.slideUp(200)
				}
			}
		}
		window.onpageshow = function(event) {
		  if (event.persisted || window.performance &&
		    window.performance.navigation.type == 2){
			  if (/(Android)/i.test(navigator.userAgent)) {
				  sessionFn("ulCon")
			   }
		    }
		 };
		//缓存 ul的内容
		function sessionFn(key){
			var sessList= sessionStorage.getItem(key) || "[]"
			var jsList= JSON.parse(sessList)
			console.log(jsList)
			if(jsList.length > 0){ //存在数据
				$('.appDiv .content ul').html('')
				$('.appDiv .content ul').html(jsList[0].ulHtml)
				var y= 0
				if(jsList[0].scrollStyle != 'none'){
					var reg= /-?\d+/g
					y= jsList[0].scrollStyle.match(reg)[5] - 0
				}
				mui('#offCanvasContentScroll').scroll().scrollTo(0,y,0)
			}
		}
		
	})
	</script>
</body>
</html>
