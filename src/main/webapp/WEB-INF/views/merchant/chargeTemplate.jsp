<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<title>模板管理</title>
<link rel="stylesheet" href="${hdpath }/css/weui.min.css">
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<link rel="stylesheet" href="${hdpath }/mui/css/mui.picker.css">
<link rel="stylesheet" href="${hdpath }/mui/css/mui.poppicker.css">
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<link rel="stylesheet" href="${hdpath}/css/base.css">
<link rel="stylesheet" href="${hdpath}/hdfile/css/chargeTemplate.css">
<script src="${hdpath}/js/jquery.js"></script>
<script src="${hdpath}/js/weui.min.js"></script>
<script src="${hdpath}/mui/js/mui.min.js"></script>
<script src="${hdpath}/mui/js/mui.picker.min.js"></script>
<script src="${hdpath}/mui/js/mui.poppicker.js"></script>
</head>
<body data-code="${arecode}">
<form action="/merchant/templataPreview" method="post" id="prevForm">
  <input type="hidden" value="" name="paratem" id="paratem" />
  <input type="hidden" value="${arecode}" name="code" id="code" />
</form>
<c:if test="${!empty tempid}" >
<c:forEach items="${templatelist}" var="tempparent">
	<c:if test="${tempparent.grade != 1}">
	<c:set var="isSysTem"  value="${tempparent.merchantid == 0 ? 'disabled' : null}"/>
	<c:set var="isSysTemSwitch"  value="${tempparent.merchantid == 0 ? 'mui-disabled' : null}"/>
	<div class="tem" data-id="${tempparent.id}" data-merchantid=${tempparent.merchantid }>
		<div class="tem-header">
			<c:if test="${tempparent.merchantid == 0}">
				<h5 class="systemtip">当前模板为系统模板（不能修改）</h5>
			</c:if>
			<div class="tem-header-list">
				<div class="tem-header-name">模板名称</div>
				<div class="tem-header-box">
					<input type="text" class="name" value="${tempparent.name}" data-val="${tempparent.name}" placeholder="请输入充电名称" ${isSysTem} >
				</div>
			</div>
			<div class="tem-header-list">
				<div class="tem-header-name">品牌名称</div>
				<div class="tem-header-box">
					<input type="text" class="brankname" value="${tempparent.remark}" data-val="${tempparent.remark}" placeholder="请输入品牌名称" ${isSysTem} >
				</div>
			</div>
			<div class="tem-header-list">
				<div class="tem-header-name">售后电话</div>
				<div class="tem-header-box">
					<input type="text" class="phone" value="${tempparent.common1}" data-val="${tempparent.common1}" placeholder="请输入售后电话" ${isSysTem} >
				</div>
			</div>
			<div class="tem-header-list">
				<div class="tem-header-name w-large">是否支持退费</div>
				<div class="tem-header-box">
					<div class="mui-switch mui-switch-mini t-switch isRef ${tempparent.permit==1 ? 'mui-active' : ''} ${isSysTemSwitch }" data-val="${tempparent.permit==1 ? 1 : 2}">
					  <div class="mui-switch-handle"></div>
					</div>
				</div>
			</div>
			<div class="tem-header-list refType-box" style="${tempparent.permit==1 ? 'display: flex' : 'display: none'}">
				<div class="tem-header-name w-large">退费标准</div>
				<div class="tem-header-box">
					<input type="text" class="select-down refType" readonly value="${tempparent.common2 == 2 ? '时间最小' : tempparent.common2 == 3 ? '电量最小' : '按照时间和电量最小'}" data-val="${tempparent.common2}" data-type="${tempparent.common2}" ${isSysTem} >
					<span class="select-arrow mui-icon mui-icon-arrowdown"></span>
				</div>
			</div>
			<div class="tem-header-list">
				<div class="tem-header-name w-large">是否强制钱包支付</div>
				<div class="tem-header-box">
					<div class="mui-switch mui-switch-mini t-switch isWalletPay ${tempparent.walletpay==1 ? 'mui-active' : ''} ${isSysTemSwitch }" data-val="${tempparent.walletpay==1 ? 1 : 2}">
					  <div class="mui-switch-handle"></div>
					</div>
				</div>
			</div>
			<div class="tem-header-list">
				<div class="tem-header-name w-large">是否支持支付宝充电</div>
				<div class="tem-header-box">
					<div class="mui-switch mui-switch-mini t-switch isAliPay ${tempparent.ifalipay==1 ? 'mui-active' : ''} ${isSysTemSwitch }" data-val="${tempparent.ifalipay == 1 ? 1 : 2}">
					  <div class="mui-switch-handle"></div>
					</div>
				</div>
			</div>
			<div class="tem-header-list flex-cloums">
				<div class="w-100">
				<div class="tem-header-name w-large">收费说明</div>
				<div class="area-contral">
					<button type="button" class="mui-btn mui-btn-success default-explain explain" ${isSysTem}>默认说明</button>
					<button type="button" class="mui-btn mui-btn-success auto-explain explain" ${isSysTem}>自动说明</button>
					<a href="#areaInfo" class="iconfont icon-wenhao textarea-req"></a>
				</div>
				</div>
				<div class="tem-header-box">
					<textarea name="" id="" cols="30" rows="5" placeholder="收费说明" data-val="${tempparent.chargeInfo}" ${isSysTem}>${tempparent.chargeInfo}</textarea>
				</div>
			</div>
		</div>
		<div class="tem-main">
			<h4 class="h4">收费标准</h4 >
			<ul class="tem-main-box">
				<c:forEach items="${tempparent.gather}" var="tempson">
					<li class="box-list">
						<div class="list-item">
							<div class="list-item-left">显示名称</div>
							<div class="list-item-right">
								<input type="text" class="select-down c-name" value="${tempson.name}" data-val="${tempson.name}" ${isSysTem}>
							</div>
						</div>
						<div class="list-item">
							<div class="list-item-left">充电价格</div>
							<div class="list-item-right">
								<input type="text" class="select-down c-price" value="${tempson.money}" data-val="${tempson.money}" ${isSysTem}><span class="unit">元</span>
							</div>
						</div>
						<div class="list-item">
							<div class="list-item-left">充电时间</div>
							<div class="list-item-right">
								<input type="text" class="select-down c-time" value="${tempson.chargeTime}" data-val="${tempson.chargeTime}" ${isSysTem}><span class="unit">分钟</span>
							</div>
						</div>
						<div class="list-item">
							<div class="list-item-left">消耗电量</div>
							<div class="list-item-right">
								<input type="text" class="select-down c-power" value="${tempson.chargeQuantity / 100}" data-val="${tempson.chargeQuantity / 100}" ${isSysTem}><span class="unit">度</span>
							</div>
						</div>
						<div class="delete-box" data-id="${tempson.id}" >
							<div class="box-logo"></div>
							<div class="delete-text iconfont icon-lajixiang"></div>
						</div>
					</li>
				</c:forEach>
			</ul>
			<div class="add-ctem">
				<button type="button" class="mui-icon mui-icon-plusempty mui-btn mui-btn-success add-cbtn" ${isSysTem}>新增一行</button>
			</div>
			<div class="delete-tem-box">
				<button type="button" class="mui-icon mui-icon-trash mui-btn mui-btn-danger delete-tem" ${isSysTem}>删除当前模板</button>
			</div>
		</div>
		<section class="now-use">
			<h5>正在使用此模板的设备</h5>
			<ul class="now-use-ul">
				<c:forEach items="${employresult}" var="item" varStatus="idxStatus">
					<li class="color${idxStatus.index % 7 == 0 ? 7 : idxStatus.index % 7}" ><a href="javascript:void(0);">
						<div class="code">${item.code}</div> <div class="name text-ellipsis">${item.devicename == null ? '— —' : item.devicename}</div> <div class="areaname text-ellipsis">${item.areaname == null ? '— —' : item.areaname}</div> <div class="mui-icon mui-icon-closeempty delete-device" data-code="${item.code}"></div>
					</a></li>
				</c:forEach>
			</ul>
			<button type="button" class="mui-icon mui-icon-plusempty mui-btn mui-btn-success add-device">选中设备使用此模板</button>
		</section>
	</div>

	<div id="areaInfo" class="mui-popover">
	  <ul class="mui-table-view">
	  	
	  	<ul class="pop-content">
	  		<h5>关于收费说明</h5>
	  		<li>在此设置的收费说明会在用户扫码界面展示，告知用户充电的收费标准</li>
	  	</ul>
	  	<ul class="pop-content">
	  		<h5>何为默认说明？</h5>
	  		<li>为通用的收费说明</li>
	  	</ul>
	  	<ul class="pop-content">
	  		<h5>何为自动说明？</h5>
	  		<li>是根据设备的系统参数中设置的分档功率和设备所使用的充电模板自动生成的收费说明</li>
	  	</ul>
	  	<ul class="pop-content">
	  		<h5>如何自定义输入？</h5>
	  		<li>如果想要在扫码界面分行显示，只需在要换行的文字后面按下“换行符”即可（“换行符”在手机输入法中）</li>
	  	</ul>
	  </ul>
	</div>
	</c:if>
</c:forEach>
</c:if>
<!-- 添加充电模板 -->
<c:if test="${empty tempid}" >
	<div class="tem" data-id="-1" data-merchantid="-1">
		<div class="tem-header">
			<div class="tem-header-list">
				<div class="tem-header-name">模板名称</div>
				<div class="tem-header-box">
					<input type="text" class="name" value="${tempname}" data-val="${tempname}" placeholder="请输入充电名称">
				</div>
			</div>
			<div class="tem-header-list">
				<div class="tem-header-name">品牌名称</div>
				<div class="tem-header-box">
					<input type="text" class="brankname" value="" data-val="" placeholder="请输入品牌名称">
				</div>
			</div>
			<div class="tem-header-list">
				<div class="tem-header-name">售后电话</div>
				<div class="tem-header-box">
					<input type="text" class="phone" value="" data-val="" placeholder="请输入售后电话">
				</div>
			</div>
			<div class="tem-header-list">
				<div class="tem-header-name w-large">是否支持退费</div>
				<div class="tem-header-box">
					<div class="mui-switch mui-switch-mini t-switch isRef mui-active" data-val="1">
					  <div class="mui-switch-handle"></div>
					</div>
				</div>
			</div>
			<div class="tem-header-list refType-box">
				<div class="tem-header-name w-large">退费标准</div>
				<div class="tem-header-box">
					<input type="text" class="select-down refType" readonly value="按照时间和电量最小" data-val="1" data-type="1">
					<span class="select-arrow mui-icon mui-icon-arrowdown"></span>
				</div>
			</div>
			<div class="tem-header-list">
				<div class="tem-header-name w-large">是否强制钱包支付</div>
				<div class="tem-header-box">
					<div class="mui-switch mui-switch-mini t-switch isWalletPay mui-active" data-val="1">
					  <div class="mui-switch-handle"></div>
					</div>
				</div>
			</div>
			<div class="tem-header-list">
				<div class="tem-header-name w-large">是否支持支付宝充电</div>
				<div class="tem-header-box">
					<div class="mui-switch mui-switch-mini t-switch isAliPay mui-active" data-val="1">
					  <div class="mui-switch-handle"></div>
					</div>
				</div>
			</div>
			<div class="tem-header-list flex-cloums">
				<div class="w-100">
				<div class="tem-header-name w-large">收费说明</div>
				<div class="area-contral">
					<button type="button" class="mui-btn mui-btn-success default-explain explain">默认说明</button>
					<button type="button" class="mui-btn mui-btn-success auto-explain explain">自动说明</button>
					<a href="#areaInfo" class="iconfont icon-wenhao textarea-req"></a>
				</div>
				</div>
				<div class="tem-header-box">
					<textarea name="" id="" cols="30" rows="5" placeholder="收费说明" data-val=""></textarea>
				</div>
			</div>
		</div>
		<div class="tem-main">
			<h4 class="h4">收费标准</h4 >
			<ul class="tem-main-box">
					<li class="box-list">
						<div class="list-item">
							<div class="list-item-left">显示名称</div>
							<div class="list-item-right">
								<input type="text" class="select-down c-name" value="1元4小时" data-val="1元4小时">
							</div>
						</div>
						<div class="list-item">
							<div class="list-item-left">充电价格</div>
							<div class="list-item-right">
								<input type="text" class="select-down c-price" value="1" data-val="1"><span class="unit">元</span>
							</div>
						</div>
						<div class="list-item">
							<div class="list-item-left">充电时间</div>
							<div class="list-item-right">
								<input type="text" class="select-down c-time" value="240" data-val="240" ><span class="unit">分钟</span>
							</div>
						</div>
						<div class="list-item">
							<div class="list-item-left">消耗电量</div>
							<div class="list-item-right">
								<input type="text" class="select-down c-power" value="1" data-val="1"><span class="unit">度</span>
							</div>
						</div>
						<div class="delete-box" data-id="-1" >
							<div class="box-logo"></div>
							<div class="delete-text iconfont icon-lajixiang"></div>
						</div>
					</li>
					<li class="box-list">
						<div class="list-item">
							<div class="list-item-left">显示名称</div>
							<div class="list-item-right">
								<input type="text" class="select-down c-name" value="2元8小时" data-val="2元8小时">
							</div>
						</div>
						<div class="list-item">
							<div class="list-item-left">充电价格</div>
							<div class="list-item-right">
								<input type="text" class="select-down c-price" value="2" data-val="2"><span class="unit">元</span>
							</div>
						</div>
						<div class="list-item">
							<div class="list-item-left">充电时间</div>
							<div class="list-item-right">
								<input type="text" class="select-down c-time" value="480" data-val="480" ><span class="unit">分钟</span>
							</div>
						</div>
						<div class="list-item">
							<div class="list-item-left">消耗电量</div>
							<div class="list-item-right">
								<input type="text" class="select-down c-power" value="2" data-val="2"><span class="unit">度</span>
							</div>
						</div>
						<div class="delete-box" data-id="-1" >
							<div class="box-logo"></div>
							<div class="delete-text iconfont icon-lajixiang"></div>
						</div>
					</li>
					<li class="box-list">
						<div class="list-item">
							<div class="list-item-left">显示名称</div>
							<div class="list-item-right">
								<input type="text" class="select-down c-name" value="3元12小时" data-val="3元12小时">
							</div>
						</div>
						<div class="list-item">
							<div class="list-item-left">充电价格</div>
							<div class="list-item-right">
								<input type="text" class="select-down c-price" value="3" data-val="3"><span class="unit">元</span>
							</div>
						</div>
						<div class="list-item">
							<div class="list-item-left">充电时间</div>
							<div class="list-item-right">
								<input type="text" class="select-down c-time" value="720" data-val="720" ><span class="unit">分钟</span>
							</div>
						</div>
						<div class="list-item">
							<div class="list-item-left">消耗电量</div>
							<div class="list-item-right">
								<input type="text" class="select-down c-power" value="3" data-val="3"><span class="unit">度</span>
							</div>
						</div>
						<div class="delete-box" data-id="-1" >
							<div class="box-logo"></div>
							<div class="delete-text iconfont icon-lajixiang"></div>
						</div>
					</li>
			</ul>
			<div class="add-ctem">
				<button type="button" class="mui-icon mui-icon-plusempty mui-btn mui-btn-success add-cbtn">新增一行</button>
			</div>
		</div>
	</div>

	<div id="areaInfo" class="mui-popover">
	  <ul class="mui-table-view">
	  	
	  	<ul class="pop-content">
	  		<h5>关于收费说明</h5>
	  		<li>在此设置的收费说明会在用户扫码界面展示，告知用户充电的收费标准</li>
	  	</ul>
	  	<ul class="pop-content">
	  		<h5>何为默认说明？</h5>
	  		<li>为通用的收费说明</li>
	  	</ul>
	  	<ul class="pop-content">
	  		<h5>何为自动说明？</h5>
	  		<li>是根据设备的系统参数中设置的分档功率和设备所使用的充电模板自动生成的收费说明</li>
	  	</ul>
	  	<ul class="pop-content">
	  		<h5>如何自定义输入？</h5>
	  		<li>如果想要在扫码界面分行显示，只需在要换行的文字后面按下“换行符”即可（“换行符”在手机输入法中）</li>
	  	</ul>
	  </ul>
	</div>
</c:if>
<!--BEGIN dialog2-->
 <div class="js_dialog" id="iosDialog2" style="display:block">
     <div class="weui-mask" style="display:none"></div>
     <div id="js_dialog_2" class="weui-half-screen-dialog">
         <div class="weui-half-screen-dialog__hd">
           <div class="weui-half-screen-dialog__hd__side">
             <button style="display: none;" class="weui-icon-btn">返回<i class="weui-icon-back-arrow-thin"></i></button>
             <button class="weui-icon-btn">关闭<i class="weui-icon-close-thin"></i></button>
           </div>
           <div class="weui-half-screen-dialog__hd__main">
             <strong class="weui-half-screen-dialog__title">提示</strong>
             <span class="weui-half-screen-dialog__subtitle">选中下面要使用此模板的设备</span>
           </div>
           <div class="weui-half-screen-dialog__hd__side">
             <button class="weui-icon-btn">更多<i class="weui-icon-more"></i></button>
           </div>
         </div>
         <div class="weui-half-screen-dialog__bd">
          	<div class="tb weui-cells_checkbox">
				<div class="tb-thead row">
					<div class="code sort-box" data-sort="0">设备号
						<div class="sort code-sort">
							<i class="iconfont icon-paixu"></i>
							<i class="iconfont icon-paixu-copy"></i>
						</div>
					</div>
					<div class="device-name">设备名称</div>
					<div class="area-name sort-box" data-sort="0">所属小区 
						<div class="sort area-sort">
							<i class="iconfont icon-paixu"></i>
							<i class="iconfont icon-paixu-copy"></i>
						</div>
					</div>
					<div class="contral">选中</div>
				</div>
				<!-- tbody开始
				<div class="tb-tbody">
					<div class="row">
						<div class="code">000001</div>
						<div class="device-name">测试名称</div>
						<div class="area-name">测试小区</div>
						<div class="contral">
							<label class="weui-cell weui-cell_active weui-check__label" for="s11">
				                <div class="weui-cell__hd">
				                    <input type="checkbox" class="weui-check" name="checkbox1" id="s11" checked="checked"/>
				                    <i class="weui-icon-checked"></i>
				                </div>
				            </label>
						</div>
					</div>
				</div>
				tbody结束 -->
			</div>
         </div>
         <div class="weui-half-screen-dialog__ft">
             <a href="javascript:" class="weui-btn weui-btn_default close-device">关闭</a>
             <a href="javascript:" class="weui-btn weui-btn_primary submit-device">确定</a>
         </div>
     </div>
 </div>
 <!--END dialog2-->
<nav class="tem-nav">
	<button type="button" class="mui-btn mui-btn-success go-back" data-type="${empty tempid ? 1 : 2}">返回</button>
	<c:if test="${!empty tempid}" >
		<button type="button" class="mui-btn mui-btn-success prev-tem">预览</button>
		<button type="button" class="mui-btn mui-btn-success save-tem">保存</button>
	</c:if>
	<c:if test="${empty tempid}" >
		<button type="button" class="mui-btn mui-btn-success save-tem">新增</button>
	</c:if>
</nav>
	<script src="${hdpath}/hdfile/js/chargeTemplate.js"></script>
</body>
</html>
