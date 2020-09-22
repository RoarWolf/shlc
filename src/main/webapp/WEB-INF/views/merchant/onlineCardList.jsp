<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"/>
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<title>IC卡列表</title>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<link rel="stylesheet" type="text/css" href="${hdpath}/css/base.css">
<link rel="stylesheet" type="text/css" href="${hdpath}/mui/css/mui.min.css">
<link rel="stylesheet" type="text/css" href="${hdpath}/mui/css/mui.picker.min.css">
<link rel="stylesheet" type="text/css" href="${hdpath}/hdfile/css/icPage.css">
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="${hdpath}/mui/js/mui.min.js"></script>
<script type="text/javascript" src="${hdpath}/mui/js/mui.picker.min.js"></script>
<script type="text/javascript" src="${hdpath}/js/jquery.js"></script>
<script type="text/javascript" src="${hdpath}/js/art-template.js"></script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="https://unpkg.com/better-scroll@1.0.1/dist/bscroll.min.js"></script>
<style>
	.icPage {
		position: fixed;
		left: 0;
		top: 0;
		background-color: #efeff4;
	}
	.icPage main {
		position: fixed;
		top: 113px;
		left: 10px;
		right: 10px;
		bottom: 50px;
/* 		overflow: auto; */
	}
	#content {
		width: 100%;
		height: 100%;
		overflow: auto;
	}
	.icPage main .btn_status {
		position: relative;
	}
	.icPage main .btn_status button{
		padding: 3px 0;
	    width: 80%;
	    min-width: 46px;
	    text-align: center;
	    position: absolute;
	    left: 50%;
	    top: 50%;
	    transform: translate(-50%,-50%);
	    -webkit-transform: translate(-50%,-50%);
	    -o-transform: translate(-50%,-50%);
	    -ms-transform: translate(-50%,-50%);
		-moz-transform: translate(-50%,-50%);
	    font-size: 12px;
	}
	.icPage .area_con {
		margin-top: 5px;
		display: flex;
		align-items: center;
	}
	.icPage .area_con .area_info {
		height: 36px;
		display: flex;
		justify-content: space-around;
	}
	.icPage .area_con .area_info>div {
		/* display: flex;
		flex-direction: column;
		justify-content: center;
		align-content: center; */
		text-align: center;
	}
	.icPage .area_con .area_info>div>span {
		/* display: flex;
		flex-direction: column;
		align-content: center; */
		color: #666;
		font-size: 12px;
		text-align: center;
	}
	.icPage header span {
		width: auto;
	}
	.icPage .area_con> span , .icPage .ic_title{
	    width: 56px;
	    font-size: 14px;
	    display: inline-flex;
	    justify-content: space-between;
	}
	.icPage .ic_title .mui-icon-arrowright{
		font-size: 17px;
		margin-top: 2px;
	}
	.icPage .ic_title .search-type {
		font-style: normal;
	}
	.icPage .area_info i {
		font-style: normal;
	}
	.icPage .area_con select {
		width: 50%;
		max-width: calc(100% - 65px);
		margin: 0;
		height: 36px;
		background-color: #f7f8f9;
    	border: 1px solid #BFBFBF !important;
    	border-radius: 6px;
    	padding: 8px 15px;
    	font-size: 12px;
    	color: #666;
    	margin-left: 3px;
    	text-align-last: center
	}
	.nav_list {
		position: fixed;
		left: 0;
		bottom: 0;
		right: 0;
		height: 50px;
		background-color: #f7f7f7;
		display: flex;
		justify-content: center;
		align-items: center;
	}
	.nav_list button {
		width: 75%;
		text-align: center;
	}
	.drawer_mask {
		position: fixed;
		left: 0;
		top: 0;
		right: 0;
		bottom: 0;
		background-color: rgba(0,0,0,.4);
		display: none;
	}
	.drawer_mask .drawer {
		min-height: 200px;
		background-color: #fff;
		position: absolute;
		bottom: 0;
		left: 0;
		width: 100%;
		transform: translateY(100%);
		animation: Slide .3s;
		padding: 15px 10px 30px;
	}
	.drawer_mask .drawer h3 {
		font-size: 16px;
		color: #666;
		font-weight:400;
		text-align: center;
	}
	.drawer_mask .drawer .select_a {
		margin-top: 20px;
	}
	.drawer_mask .drawer .select_a span {
		font-size: 14px;
		width: 85px;
		display: inline-block
	}
	.drawer_mask .drawer .select_a input {
		width: calc(100% - 91px);
		font-size: 14px;
		color: #333;
		background-color: #f5f7fa;
		margin: 0;
	}
	.drawer_mask .drawer .select_a input::-webkit-input-placeholder{
		font-size: 14px;
		color:#999;
	}
	.bindNum {
		margin-top: 20px;
		display: flex;
		justify-content: space-between;
		font-size: 14px;
	}
	.bindNum span.firSpan {
		margin-left: 5px;
		color: #4cd964;
		font-size: 14px;
		font-weight: bold;
	}
	.icon_qr {
		margin-top: 40px;
		text-align: center;
	}
	.icon_qr i {
		font-size: 40px;
		font-weight: bold;
		color: #4cd964;
	}
	.mui-toast-container {top: 40% !important;}
	.mui-poppicker-header {
	    padding: 6px;
	    font-size: 14px;
	    color: #666;
	    position: relative;
	}
	.mui-poppicker-header .select_info {
		position: absolute;
		left: 50%;
		top: 50%;
		transform-origin:center center;
		transform: translate(-50%,-50%);
		-ms-transform: translate(-50%,-50%);
		-moz-transform: translate(-50%,-50%);
		-webkit-transform: translate(-50%,-50%);
		-o-transform: translate(-50%,-50%);
	}
	@keyframes Slide {
		from {
			transform: translateY(100%);
		}
		to{
			transform: translateY(0);
		}
	}
	
.QRCodeAlert {
	position: fixed;
	width: 270px;
	position: absolute;
	left: 50%;
	top: 50%;
	animation: showAni .3s;
	transform-origin:center center;
	transform: translate(-50%,-50%);
	-ms-transform: translate(-50%,-50%);
	-moz-transform: translate(-50%,-50%);
	-webkit-transform: translate(-50%,-50%);
	-o-transform: translate(-50%,-50%);
	z-index: 9999;
    display: none;
}
.QRCodeAlert .top {
	position: relative;
    padding: 15px;
    border-radius: 13px 13px 0 0;
    background: rgba(255,255,255,.95);
}
.QRCodeAlert .top::after {
    position: absolute;
    top: auto;
    right: auto;
    bottom: 0;
    left: 0;
    z-index: 15;
    display: block;
    width: 100%;
    height: 1px;
    content: '';
    background-color: rgba(0,0,0,.2);
    -webkit-transform: scaleY(.5);
    transform: scaleY(.5);
    -webkit-transform-origin: 50% 100%;
    transform-origin: 50% 100%;
}
.QRCodeAlert .top h3 {
	font-size: 18px;
    font-weight: 500;
    text-align: center;
    margin-bottom: 12px;
}
.QRCodeAlert .QR_tips {
	margin-bottom: 0;
}
.QRCodeAlert .info {
	font-size: 13px;
	color: #333;
}
.QRCodeAlert .info .info_item {
	padding: 5px 15px;
	display: flex;
	-webkit-justify-content: space-between;
    justify-content: space-between;
}
.QRCodeAlert .info .info_item >span:nth-child(2){
	max-width: 50%;
	overflow: hidden;
	text-overflow:ellipsis;
	white-space: nowrap;
}
.QRCodeAlert .info .info_item strong.totalSpan,
.QRCodeAlert .info .info_item strong.nowSpan{
	font-size: 15px;
	color: #4cd964;
}
.QRCodeAlert .info >div:nth-child(3) {
	text-align: center;
    color: #4cd964;
    font-size: 16px;
}
.QRCodeAlert .bottom {
	position: relative;
    display: -webkit-box;
    display: -webkit-flex;
    display: flex;
    height: 44px;
    -webkit-box-pack: center;
    -webkit-justify-content: center;
    justify-content: center;
}
.QRCodeAlert .bottom span {
	position: relative;
    display: block;
    width: 100%;
    height: 44px;
    box-sizing: border-box;
    padding: 0 5px;
    overflow: hidden;
    font-size: 17px;
    line-height: 44px;
    color: #007aff;
    text-align: center;
    text-overflow: ellipsis;
    white-space: nowrap;
    cursor: pointer;
    background: rgba(255,255,255,.95);
    -webkit-box-flex: 1;
}
.QRCodeAlert .bottom span:first-child {
  		border-radius: 0 0 0 13px;

  	}
  	.QRCodeAlert .bottom span:first-child::after {
	position: absolute;
    top: 0;
    right: 0;
    bottom: auto;
    left: auto;
    z-index: 15;
    display: block;
    width: 1px;
    height: 100%;
    content: '';
    background-color: rgba(0,0,0,.2);
    -webkit-transform: scaleX(.5);
    transform: scaleX(.5);
    -webkit-transform-origin: 100% 50%;
    transform-origin: 100% 50%;
  	}
  	.QRCodeAlert .bottom span:last-child{
  	    border-radius: 0 0 13px;
  	    font-weight: 600;
  	}
  	.QRCodeAlert  .inputDiv {
	position: relative;
	margin: 15px 0 0;
  	}
  	#searchQRCode {
  		position: absolute;
	width: 26px;
	height: 26px;
	right: 5px;
	top: 0;
	display: flex;
	align-items: center;
	justify-content: center;
  	}
  	#searchQRCode i {
  		font-size: 25px;
  		color: #007aff;
  	}
  	.copymark {
  		position: fixed;
 			left: 0;
 			right: 0;
 			bottom: 0;
 			top: 0;
 			background-color: rgba(0,0,0,.4);
 			z-index: 101;
 			display: none;
  	}
  	.table_top {
  		background: #C8EFD4;
  		position: absolute;
  		left: 0;
  		top: 0;
  		z-index: 99;
  		overflow: hidden;
  	}
  	.table_top li {
  		border-radius: 8px 8px 0 0;	
  	}

	.bot_info {
		position: absolute;
		width: 100%;
		bottom: -35px;
		left: 0;
		text-align: center;
		/* background-color: #f5f7fa;
		border-left: 1px solid #add9c0;
		border-right: 1px solid #add9c0;
		border-bottom: 1px solid #add9c0; */
		height: 35px;
		line-height: 35px;
		color: #999;
	}
	.icPage main ul li span .phoneAndRemark {
		line-height:1.2em;
	}
	.icPage main ul li span .moneyHeight a{
		line-height: 1.2em;
	}
	.icPage main ul li span.green {
		color: #22B14C;
	}
</style>
</head>
<body>
	<div class="icPage">
		<header class="clearfix">
			<div>
				<span class="ic_title"><em class="search-type">电话</em><i class="mui-icon mui-icon-arrowright"></i></span>
				<input type="search" name="icCode" placeholder="请输入电话" >
				<button type="button" class="mui-btn mui-btn-success" id="searchBut">查询</button>
			</div>
			<div class="area_con">
				<span>小区名称</span>
				<select>
						<option data-id="" value="">所有</option>
						<option data-id="-1" value="-1" data-name="未选择" >未绑定小区 </option>
					<c:forEach items="${areaData}" var="item">
						<option 
						data-id="${item.id}" 
						value="${item.id}" 
						data-name="${item.name}" 
						>${item.name}</option>
	                </c:forEach>
				</select>
				<div class="area_info">
					<div>
						<span>数量 / 充值  / 赠送</span>
						<span>
						<i class="card_num" data-num="${datasize}">${datasize}</i> /
						<i class="card_money" data-money="${datatopupmoney}"><fmt:formatNumber value="${datatopupmoney}" pattern="0.00"/></i> /
						<i class="card_sendmoney" data-sendmoney="${datasendmoney}"><fmt:formatNumber value="${datasendmoney}" pattern="0.00"/></i> 
						</span>
					</div>
				</div>
			</div>
		</header>
		<main>
			 <!--  <ul class="table_top">
					<li class="title table_header">
						<span>卡号</span>
						<span>余额</span>
						<span>电话/备注</span>
						<span>状态</span>
					</li>
				</ul> -->
			<div id="content"  class="wrapper">
				<ul class="content">
					<li class="title table_header2">
						<span>卡号</span>
						<span>充值 / 赠送</span>
						<span>电话 / 备注</span>
						<span>关联钱包</span>
						<span>状态</span>
					</li>
					<c:forEach items="${resultdata}" var="card">
					<li>
						<span><a href="/merchant/mercVirtualPayMoney?type=2&id=${card.id}">${card.cardID}</a></span>
						<span>
							<div class='moneyHeight'><a href="${hdpath }/general/onlinecardrecord?cardID=${card.cardID}&uid=${card.uid}"><fmt:formatNumber value="${card.money}" pattern="0.00"/> / <fmt:formatNumber value="${card.sendmoney}" pattern="0.00"/></a></div>
						</span>
						<span>
							<div class="phoneAndRemark">${card.touristphone}</div>
							<div class="phoneAndRemark">${card.remark}</div>
						</span>
						<span class="${card.relevawalt == 1 ? 'green' : ''}">
							${card.relevawalt == 1 ? '是' : '否'}
						</span>
						<span class="btn_status">
						 	<c:choose>
								<c:when test="${card.status == 0}">
									<button type="button" class="mui-btn mui-btn-outlined" value="0" data-setdata="${card.id}" disabled>未激活</button>
									<!-- <font color="gray">未激活</font> -->
								</c:when>
								<c:when test="${card.status == 1}">
									<!-- <font color="green">正常</font> -->
									<button type="button" class="mui-btn mui-btn-success mui-btn-outlined" value="2" data-setdata="${card.id}" >正常</button>
								</c:when>
								<c:when test="${card.status == 2}">
									<!-- <font color="red">挂失</font> -->
									<button type="button" class="mui-btn mui-btn-danger mui-btn-outlined" value="1" data-setdata="${card.id}">挂失</button>
								</c:when>
							</c:choose>
						 </span>
					</li>
					</c:forEach>
					<c:if test="${fn:length(resultdata) < 35 }">
						<div class="bot_info">暂无更多数据</div>
					</c:if>
				</ul>
			</div>
		</main>
		<nav class="nav_list">
			<button type="button" class="mui-btn mui-btn-success" id="addCradBtn">
			<span class="mui-icon mui-icon mui-icon-plusempty "></span>
			添加IC卡
			</button>
		</nav>
		
		<div class="drawer_mask">
			<div class="drawer">
				<h3>添加IC卡</h3>
				<div class="select_a">
					<span>已选择小区： </span>
					<input type="search" class="mui-input-clear" placeholder="未选择小区" disabled id="select_area">
				</div>
				<div class="bindNum">
					<div>总绑定： <span class="firSpan"><span id="totalNum">0</span>张</span></div>
					<div>本次绑定： <span class="firSpan"><span id="onceNum">0</span>张</span></div>
				</div>
				<div class="icon_qr">
					<i class="iconfont icon-saoma"></i>
				</div>
			</div>
		</div>
	</div>
	<div class="copymark"></div>
	<div class="QRCodeAlert">
		<div class="popur">
			<div class="top">
				<h3 class="QR_title">IC卡绑定成功</h3>
				<p class="QR_tips" style="color: #F47378; text-align: center;">在线卡已被绑定</p>
				<div class="info">
					<div class="info_item"><span class="bind_name">绑定小区：</span><span class=area_name></span></div>
					<div  class="info_item"><span>总绑定：<strong class="totalSpan">0</strong> 张</span><span>本次绑定：<strong class="nowSpan">0</strong> 张</span></div>
					<div class="tip_text" style="font-size: 14px;">是否继续绑定IC卡到此小区</div>
				</div>
			</div>
			<div class="bottom">
				<span class="closeBtn">取消</span>
				<span class="sureBtn">继续</span>
			</div>
		</div>
	</div>
<body>
<script id="data-tpl" type="text/html">
	<ul class="content">
		<li class="title table_header2">
			<span>卡号</span>
			<span>充值 / 赠送</span>
			<span>电话 / 备注</span>
			<span>关联钱包</span>
			<span>状态</span>
		</li>
	{{each result}}
		<li>
			<span><a href="/merchant/mercVirtualPayMoney?type=2&id={{$value.id}}">{{$value.cardID}}</a></span>
			<span><div class='moneyHeight'><a href="${hdpath }/general/onlinecardrecord?cardID={{$value.cardID}}&uid={{$value.uid}}">{{$value.money.toFixed(2)}} / {{ $value.sendmoney.toFixed(2) }}</a></div></span>
			<span>
				<div class="phoneAndRemark">{{$value.touristphone}}</div>
				<div class="phoneAndRemark">{{$value.remark}}</div>
			</span>
			<span class="{{ $value.relevawalt == 1 ? 'green' : '' }}">
				{{ if $value.relevawalt == 1 }} 
				是
				{{ else }}
				否
				{{ /if }}
			</span>
			<span class="btn_status">
				{{if $value.status == 0}}
					<button type="button" class="mui-btn mui-btn-outlined" value="0" data-setdata="{{$value.id}}" disabled>未激活</button>
				{{else if $value.status == 1}}
					<button type="button" class="mui-btn mui-btn-success mui-btn-outlined" value="2" data-setdata="{{$value.id}}" >正常</button>
				{{else if $value.status == 2}}
					<button type="button" class="mui-btn mui-btn-danger mui-btn-outlined" value="1" data-setdata="{{$value.id}}">挂失</button>
				{{/if}}
			</span>
		</li>
	{{/each}}
	{{ if result.length < 25 }}
		<div class="bot_info">暂无更多数据</div>
	{{ else}}
		<div class="bot_info">正在加载数据</div>
	{{ /if }}
	</ul>
</script>

<script id="data-tpl2" type="text/html">
{{ each result }}
<li>
	<span><a href="/merchant/mercVirtualPayMoney?type=2&id={{ $value.id }}">{{ $value.cardID }}</a></span>
	<span><div class='moneyHeight'><a href="${hdpath}/general/onlinecardrecord?cardID={{ $value.cardID }}&uid={{ $value.uid }}">{{ $value.money.toFixed(2) }} / {{ $value.sendmoney.toFixed(2) }}</a></div></span>
	<span>
		<div class="phoneAndRemark">{{$value.touristphone}}</div>
		<div class="phoneAndRemark">{{$value.remark}}</div>
	</span>
	<span class="{{ $value.relevawalt == 1 ? 'green' : '' }}">
	    {{ if $value.relevawalt == 1 }} 
		 是
	    {{ else }}
	            否
	    {{ /if }}
	</span>
	<span class="btn_status">
		{{ if $value.status == 0 }}
			<button type="button" class="mui-btn mui-btn-outlined" value="0" data-setdata="{{ $value.id }}" disabled>未激活</button>
		{{ else if $value.status == 1 }}
			<button type="button" class="mui-btn mui-btn-success mui-btn-outlined" value="2" data-setdata="{{ $value.id }}" >正常</button>
		{{ else if $value.status == 2 }}
			<button type="button" class="mui-btn mui-btn-danger mui-btn-outlined" value="1" data-setdata="{{ $value.id }}">挂失</button>
		{{ /if }}
	</span>
</li>
{{ /each }}
</script>

<script>
window.onpageshow = function(event) {
	if (event.persisted || window.performance && window.performance.navigation.type == 2){
			location.reload();
	}
	//window.location.reload();
};

$(function(){
	var flag= false
	var currentPage= 1 //页码
	var limit= 35 //每次取的条数
	var totalPages //总页数
	var type= 2 //搜索类型 1 按照卡号搜索 2、按照手机号搜索
	document.body.addEventListener('touchmove', function (e) {
	  e.preventDefault();
	}, {passive: false});
	$('main')[0].addEventListener('touchmove', function (e) {
		e= e || window.event()
		e.stopPropagation()
	});
	var selectAreaNum= 0 //扫码的时候，选中当前小区在在线卡数量
	var picker = new mui.PopPicker();
	/* renderTable() */
	$('.mui-poppicker-header')[0].insertBefore($('<span class="select_info">请先选择IC卡绑定的小区</span>')[0],$('.mui-poppicker-header').children()[1])
	$('main').on('click','.btn_status button',function(){
		var $that= $(this) 
		var status = $(this).val();
		var id = $(this).attr("data-setdata");
		var setinfo;
		if (status == 1) {
			setinfo = "确认解挂该IC卡？";
		} else {
			setinfo = "确认挂失该IC卡？";
		}
		var btnArray = ['取消', '确认'];
		mui.confirm(setinfo, '在线卡设置', btnArray, function(e) {
			if (e.index == 1) {
				$.ajax({
					url : "${hdpath}/merchant/setCardStatus",
					data : {
						id : id,
						status : status
					},
					type : "post",
					dataType : "json",
					success : function(e) {
						
						if (e.code == 200) {
							mui.alert("设置成功");
							var value= $that.attr('value').trim()
							console.log(value)
							if(value == 1){
								$that.attr('value',2)
								$that.text('正常');
								$that.removeClass('mui-btn-danger');
								$that.addClass('mui-btn-success')
							}else if(value == 2){
								$that.attr('value',1)
								$that.text('挂失');
								$that.removeClass('mui-btn-success');
								$that.addClass('mui-btn-danger')
							}
							
						} else if (data == 0) {
							mui.alert("设置失败");
						}
					}
				});
			} else {
				return ;
			}
		})
	})
	
	/* 点击添加IC卡按钮 */
	$('#addCradBtn').click(function(){
		
		var _this= this
	  	  picker.show(function (selectItems) {
		    $.ajax({
		    	url: '/merchant/inquireOnlineInfo',
		    	type: 'post',
		    	data: {
		    		aid: selectItems[0].value.id
		    	},
		    	success: function(res){
		    		if(res.code == 200){
		    			$('#select_area').val(selectItems[0].text); //弹框中显示小区名称
		    			$('#select_area').attr("data-id",selectItems[0].value.id)
		    			selectAreaNum= res.datasize
		    			$('#totalNum').text(selectAreaNum)
		    			getCode()
		    			/* $('.drawer').css('transform','translateY(0)')
						$('.drawer_mask').show() */
		    		}else{
		    			  mui.toast('小区信息获取失败',{ duration:'1000', type:'div' }) 
		    		}
		    	},
		    	error:function(){
		    		 mui.toast('小区信息获取失败',{ duration:'1000', type:'div' }) 
		    	}
		    })
		    
		  })
		
		selectAreaNum= 0 //每次点击添加ic卡的时候都置空
		/* 获取select选择框中小区的信息 */
		var areaList= []
		$('.area_con select option').each(function(i,item){
			if($(item).attr('data-id') !== ''){
				var obj= {
					value: {
						id: $(item).attr('data-id'),
					},
					text: $(item).attr('data-name')
				}
				areaList.push(obj)
			}
		})
		/* areaList.unshift({value: {id: '',num: 0},text: '未选择'}) */
		picker.setData(areaList);
	})
	$('.drawer_mask').click(function(){
		/* 点击关闭之后，重新发送请求 ，更新最新数据*/
		sendAjaxAndRender()
		$(this).hide()
	})
	$('.drawer').click(function(e){
		e= e || window.event
		e.stopPropagation()
	})
	
	/* 选择搜索类型 */
	var picker2 = new mui.PopPicker();
	picker2.setData([{ text: '电话',value: 2 },{ text: '卡号',value: 1 }])
	$('.ic_title').on('click',function(){
		picker2.show(function(selectItem){
			 $('.search-type').text(selectItem[0].text)
			 type= selectItem[0].value
			 $('input[name="icCode"]').attr('placeholder','请输入'+selectItem[0].text)
		}) 
	})
	
	/* 通过在线卡/小区搜索 */
	$('.area_con select').change(function(){
		 flag= true
		 totalPages= undefined
		 currentPage= 1
		 sendAjaxAndRender(true)
	})
	$('#searchBut').click(function(){
		 flag= true
		 totalPages= undefined
		 currentPage= 1
		 sendAjaxAndRender(true)
	})
	function sendAjaxAndRender(from){
		var keywords= $('input[name="icCode"]').val().trim()
		var areaId= $('.area_con select').val().trim()
		var data= {type: type,keywords: keywords,areaId: areaId,currentPage: currentPage,limit:limit}
		$.ajax({
			url: '/merchant/inquireOnlineData',
			type: 'post',
			data:data,
			success: function(res){
			  if(res.code == 200){
				  totalPages= res.totalPages
				  currentPage= res.currentPage
				  if(from){
						 $('#content')[0].scrollTop= 0
						 $('#content').html('')
						 var html = template('data-tpl', {result: res.datalist});
						$('#content').html(html)
						 /* 每次搜索之后都置空 */
					 }else{
						 var fragment= document.createDocumentFragment()
						 for(var i= 0; i< res.datalist.length; i++){
							 var html2= template('data-tpl2',{result: [res.datalist[i]]}) 
							 $(fragment).append($(html2))
						 }
						 var text= currentPage <= totalPages ? '正在加载更多' : '暂无更多数据'
						 $(fragment).append($('<div class="bot_info">'+text+'</div>'))
						 $('.bot_info').remove()
						 $('#content .content').append($(fragment))
					 }
				  
				 var datasize= res.datasize || 0
				 var tatolmoney= res.datatopupmoney || 0
				 var sendmoney= res.datasendmoney || 0
				 $('.card_num').attr('data-num',datasize).text(datasize)
				 $('.card_money').attr('data-money',tatolmoney).text(tatolmoney.toFixed(2))
				 $('.card_sendmoney').attr('data-sendmoney',sendmoney).text(sendmoney.toFixed(2))
			  }
			},
			error: function(err){
				
			},
			complete: function(){
				flag= false
			}
		})
	} 
	
	/* 调取扫码 */
	function getCode() {
			var pageUrl = window.location.href;
			$.ajax({
				url : '${hdpath}/merchant/jssdkWxGet',
				type : "POST",
				data : {pageUrl : pageUrl},
				cache : false,
				success : function(data) {
					var timestamp = data.timestamp;
					timestamp = parseInt(timestamp);
					console.log(timestamp);
					wx.config({
						debug : false,
						appId : data.appId,
						timestamp : timestamp,
						nonceStr : data.nonceStr,
						signature : data.signature,
						jsApiList : [ 'scanQRCode' ]
					});
					wx.ready(function(){
						wx.scanQRCode({
							needResult : 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
							desc: 'scanQRCode desc',
							scanType : [ 'qrCode' , 'barCode' ], // 可以指定扫二维码还是一维码，默认二者都有
							success : function(res) {
								var url = res.resultStr;
								var origin= window.location.hostname
								var card
								if(url.indexOf('www.tengfuchong')!= -1){
									card= url.split(/\=/g)[1]
								}
								sendCode(card)
							}
						});
					})

					wx.error(function(res) {
						alert("错误：" + res.errMsg);
					});
				},//返回数据填充
			});
	}
	
	function sendCode(card){ //微信扫码成功之后，获取code发送给后台
		var areaid= $('#select_area').attr('data-id')
		$.ajax({
			url: '/merchant/dealerBindingOnline',
			type: 'post',
			data:{cardnum: card,areaid:areaid},
			success: function(res){
			  if(res.code == 200){
				 $('#totalNum').text(res.datasize)
				 $('#onceNum').text(res.datasize-selectAreaNum)
				 var areaORmername= res.areaname
				 var hasArea= true
				 if(res.areaname == null){
					 areaORmername= '未选择'
					 hasArea= false
				 }
				 if(hasArea){ //绑定在小区
					 $('.tip_text').text('是否继续绑定IC卡到此小区')
				 }else{
					 $('.tip_text').text('是否继续绑定IC卡到商户')
				 }
				 $('.QR_title').text('IC卡绑定成功')
				 $('.QR_tips').hide() 
				 $('.area_name').text(areaORmername)
				 $('.totalSpan').text(res.datasize)
				 $('.nowSpan').text(res.datasize-selectAreaNum)
				 $('.copymark').show()
				 $('.QRCodeAlert').fadeIn(200)
			  }else if(res.code == 106){
				 if(res.areaname == null){
					 areaORmername= '未选择'
					 hasArea= false
				 }
				 if(hasArea){ //绑定在小区
					 $('.tip_text').text('是否继续绑定IC卡到此小区')
				 }else{
					 $('.tip_text').text('是否继续绑定IC卡到商户')
				 }
				 $('.QR_title').text('IC卡绑定失败')
				 $('.QR_tips').text(res.message)
				 $('.QR_tips').show()
				 $('.area_name').text(areaORmername)
				 $('.totalSpan').text(res.datasize)
				 $('.nowSpan').text(res.datasize-selectAreaNum)
				  $('.copymark').show()
				  $('.QRCodeAlert').fadeIn(200)
			  }else {
				  mui.toast(res.message,{ duration:'1000', type:'div' }) 
			  }
			},
			error: function(err){
				mui.toast('添加失败,请稍后重试',{ duration:'1000', type:'div' }) 
			}
		})
	}
	

	/* 上啦加载 */
	$('#content').scroll(function(e){
		e= e || window.event
		var target= e.target || e.currentTarget
		var tbHeight= $(this).height()
		var contentHeight= $('#content .content').height()
		if((contentHeight - target.scrollTop)*0.9 <= tbHeight && !flag){
			flag= true
			console.log(11111)
			console.log(currentPage,totalPages)
			//请求数据
			currentPage++ //页码加一
			if(typeof totalPages == 'undefined' || currentPage <= totalPages){
				sendAjaxAndRender()
			}else{
				$('.bot_info').text('暂无更多数据')
			}
		}
		
	})
	
	$('.copymark').click(function(){  //点击遮挡层关闭
		$('.copymark').fadeOut(200)
		$('.QRCodeAlert').fadeOut(200)
		 flag= true
		 totalPages= undefined
		 currentPage= 1
		sendAjaxAndRender(true) //点击关闭之后重新请求
	})

	$('.QRCodeAlert .closeBtn').click(function(){ //点击取消
		$('.copymark').fadeOut(200)
		$('.QRCodeAlert').fadeOut(200)
		 flag= true
		 totalPages= undefined
		 currentPage= 1
		sendAjaxAndRender(true)
		
	})

	$('.QRCodeAlert .sureBtn').click(function(){
		$('.copymark').fadeOut(200)
		$('.QRCodeAlert').fadeOut(200)
		getCode()
	})
	
	/* function renderTable(){
		$('.table_header span').each(function(i,item){
			console.log($('.table_header2 span').eq(i))
			$(item).width($('#content ul li').eq(0).find('span').eq(i).outerWidth())
		})
	}
 */
})
</script>
</html>