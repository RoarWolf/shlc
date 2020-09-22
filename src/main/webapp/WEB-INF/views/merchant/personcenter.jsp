<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>个人中心</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath}/css/base.css"/>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script src="${hdpath}/js/jquery.js"></script>
<link rel="stylesheet" href="/css/weui.css"></link> 
<!-- <link rel="stylesheet" href="//at.alicdn.com/t/font_1911488_oh5yyx2de5e.css"/> -->
<style>
@font-face {font-family: "iconfont";
  src: url('//at.alicdn.com/t/font_1911488_oh5yyx2de5e.eot?t=1593416432760'); /* IE9 */
  src: url('//at.alicdn.com/t/font_1911488_oh5yyx2de5e.eot?t=1593416432760#iefix') format('embedded-opentype'), /* IE6-IE8 */
  url('data:application/x-font-woff2;charset=utf-8;base64,d09GMgABAAAAAAO8AAsAAAAACBgAAANvAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHEIGVgCDHAqDXIMEATYCJAMQCwoABCAFhG0HQBvuBhHVm7vIfhy4sdP8p/+woibpn4MHyvN7k0y7afuFk+yVlL+U73QW8K0JEIC3OOcJ5IWJuhzJW/ToCymPspz+53J6d40pML/Vdhxj057Pj3oBxgEFNNamgAItkALiHuKtHWQn90MAVnyJRhSXVrdAQ2IOE4AY0Ld3V7SMHdlQLdAQmCvmSsQiTGjKbOUKsDD4vPhEotBAwaRijq3vVdKD/LfJ75qrul2ng4egGM8L8ItABaIBCaJPpb8bapSKRsWq/kaSCmhoKPA2+e3Bd83tdlDuRa9Zevr+8RQQZsxKBwCSIa3C22RHnhgOAh8C3jWvQAF4IXM0wAbMAEeZTA/EJBI12MWB6mY63yLHOp245WvaeDvNee5ufZfVac4upL7b03nlXm9T0965UWe57ka/Zdt2rFm707v3yuZ8jx7LrNYVe6q9NttytK2lQ0L3dpw0/u6blndet7j3ttX9dx1br1QTTV6NK+bQ8R4tlu3uLRqyG1u0bmjRYnSrHsGF/Vu0bH3xfuSFO2NccUms11fe+OJkOH65IZ2LfUucvTbOhkvR85Xfk5wrweJXBGv4GubymcPaT28Tc2lg/90+LXzXcFP37DS+J/kluxjPg3yDrhpnUt7vZ573boO/qj+U/v7yoWX193fVySu6/F+V7x+oiGsh1xKNUEOIQPf8VdWJbTeRNDPo3r3jx4OOHWsbwuXoiWNHj+lmGF0XzezUqWl8u3bFLhVXi2psUUn9ujbvSj0Adl19rupBZot6TO3/F7+Dd/czTp2ds/9oNgnAY7+z3yFo1yV4xa9QqIp/pkDWSE1lOqRU1I5cD0iqUwUogNVywZ/KgRtPxzB6oitBoOGZgEIzfEBFIxiVpGgwYSMZzGjkgZUoyhbbcGcYKkJagEimExC4cBAUnLgIKi5cRSXJABNefAEzLnxT2Hi1W3Nrn98cCQWjBvkHr9rG2C7ehNU3tO8kKK0KWC+kwUehzIp89ooN0hgTho+tmA0Yamu4kMMwpRY6agMqzhxzd8hzU/WkTLX1aOOIIIEhDUh/wFNaDeM1ZpvC+2+Q9ZYI1ICryr4gMvCtg1KmQKBXfYPCXcsjgw+rwpgBDNKqgQvxomSlWqCr7hUghWVcj0TnIKf1DFafzW+sn3ATYGVuIFOEKqQwsdi/rfBTx7CMfeVoBAA=') format('woff2'),
  url('//at.alicdn.com/t/font_1911488_oh5yyx2de5e.woff?t=1593416432760') format('woff'),
  url('//at.alicdn.com/t/font_1911488_oh5yyx2de5e.ttf?t=1593416432760') format('truetype'), /* chrome, firefox, opera, Safari, Android, iOS 4.2+ */
  url('//at.alicdn.com/t/font_1911488_oh5yyx2de5e.svg?t=1593416432760#iconfont') format('svg'); /* iOS 4.1- */
}

.iconfont {
  font-family: "iconfont" !important;
  font-size: 16px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.icon-xiugai:before {
  content: "\e6af";
}

.icon-gai:before {
  content: "\e753";
}

.icon-xiugai1:before {
  content: "\e634";
}

	html, body {
		width: 100%;
		overflow-x: hidden;
	}
		.app {
			color: #666;
			/* padding-top:59px; */
			padding-bottom: 50px;
			overflow-y: auto;
		}
		.per>ul {
			margin-bottom: 10px;
		}
		.per>ul a {
			text-decoration: none;
			font-size: 14px;
		}
		.per>ul:after, .per>ul:before{
			height: 0;
		}
		.walletUl a p:nth-child(1){
			font-size: 14px;
			color: #666;
			padding-bottom: 5px;
		}
		.walletUl a p:nth-child(3) {
			font-size: 14px;
			color: #666;
		}
		.walletUl a p:nth-child(4) {
			font-size: 28px;
			color: #565656;
			line-height: 1.5em;
		}
		.walletUl .realname {
			font-size: 14px;
			padding-bottom: 5px;
		}
		.walletUl .editrealname {
			padding:10px;
			color: #4cd964;
		}
		.cashOut .mui-collapse-content >.mui-table-view:nth-child(1){
			margin-top: 0;
		}
		.cashOut .mui-collapse-content >.mui-table-view span {
			font-size: 12px;
			color: #999;
		}
		
		.mui-table-view-cell p {
			font-size: 12px;
			color: #666;
		}
		.mui-table-view-cell p .left {
			float: left;
		}
		.mui-table-view-cell p .right {
			display: block;
			overflow: hidden
		}
		.cashOutP {
			position: relative;
		}
		 .cashOutBtn {
			position: absolute;
			padding: 3px 8px;
			border: 1px solid #4cd964;
			color: #4cd964;
			border-radius: 3px;
			right: 20px;
			font-size: 14px;
			top: 50%;
			transform: translateY(-50%);
			-webkit-transform: translateY(-50%);
			-moz-transform: translateY(-50%);
			-o-transform: translateY(-50%);
			-ms-transform: translateY(-50%);
		}
		.cashOutBtn:active {
			background-color: #98E0AD !important;
		}
		.mui-table-view-cell.mui-collapse .mui-table-view .mui-table-view-cell {
			font-size: 14px;
		}
		.app .set .mui-table-view-cell .mui-collapse-content .mui-table-view-cell {
			padding-left: 42px;
		}
		.app .set .midLine {
			width: 100%;
			height: 1px;
			webkit-transform: scaleY(.5);
		    transform: scaleY(.5);
		    background-color: #c8c7cc;
		}
		
		/* switch按钮 */
		.mySwitch {
			width: 70px;
			height: 30px;
			line-height: 30px;
			border-radius: 30px;
			border: 2px solid #ddd;
			position: relative;
			transition: all 0.2s;
			/* background-color: #61CC80; */
		}
		.mySwitch:before {
			content: 'on';
			display: inline;
			font-size: 16px;
			margin-left: 7px;
			color: #fff;
			float: left;
		}
		
		.mySwitch:after {
			content: 'off';
			display: inline;
			font-size: 16px;
			margin-right: 7px;
			color: #666;
			float: right;
		}
		.mySwitch .myItem {
			width: 28px;
			height: 28px;
			background-color: #fff;
			border-radius: 50%;
			/* border: 1px solid #ccc; */
			box-sizing: border-box;
			position: absolute;
			left: 0;
			top: -1px;
			transition: all 0.2s;
			box-shadow: 0 2px 5px rgba(0,0,0,.4);
		}
		.sw_active {
			background-color: #4cd964;
			border: 2px solid #4cd964
		} 
		.sw_active .myItem {
			/* border: 1px solid #4cd964; */
		}
		
		.noText:after, .noText:before{
			content: '' !important;
		}
		.tel_span .edit {
		    padding: 2px 12px;
		    margin-left: 10px;
		    margin-top: -2px;
		    margin-bottom: -2px;
		}
		
		.edit_tel {
			display: none;
		}
		.edit_tel button {
			padding: 3px 12px;
			margin-left: 5px;
		}
		.edit_tel input {
		    width: calc(100% - 200px);
		    font-size: 14px;
		    padding: 3px 5px;
		    border-radius: 3px;
		    outline: none;
		    height: 27px;
		    border: 1px solid #ccc;
		    -webkit-user-select: text !important;
		    user-select: text !important;
		    -moz-user-select: text !important;
		    -o-user-select: text !important;
		    -ms-user-select: text !important;
		}
		/* 子账户 */
		.manage-item {
			position: relative;
			margin-bottom: 15px;
		}
		.manage-item::after {
			content:'';
			position: absolute;
			left: -15px;
			right: -15px;
			bottom: 0;
			height: 1px;
			background: #c8c7cc;
		}
		.child-content .child-wrapper{
			display: flex;
			font-size: 14px;
			padding: 5px;
			line-height: 32px;
		}
		.child-content .child-wrapper .left {
			width: 110px;
			flex-shrink: 0;
		}
		.child-content .child-wrapper .right {
			flex: 1
		}
		.child-content .child-wrapper .right input {
			display:  none;
			height: 32px;
			font-size: 14px;
			margin-bottom: 0;
		}
		.child-manage .delete,.child-manage .add {
			width: 100%;
		}
		.child-manage .child-edit {
			display: flex;
			justify-content: space-between;
			padding-bottom: 10px;
		}
		.child-manage .child-handle {
			padding-bottom: 10px;
		}
		.child-manage .child-edit {
			display: none;
		}
		.child-manage .child-edit  button {
		  	width: 49%;
		}
		.child-manage strong{
			font-size: 14px;
		}
		.child-manage .child-tip {
			margin: 15px 0;
		}
		.page {
			position: fixed;
			top: 0;
		    right: 0;
		    bottom: 0;
		    left: 0;
		    overflow-y: auto;
		    -webkit-overflow-scrolling: touch;
		    box-sizing: border-box;
		    z-index: 99;
		   	transform: translateX(100%);
		   	-webkit-transform: translateX(100%);
		   	-o-transform: translateX(100%);
		   	-ms-transform: translateX(100%);
		   	-moz-transform: translateX(100%);
		   	transition: .3s ease-in;
		}
		.page.show {
			transform: translateX(0);
		   	-webkit-transform: translateX(0);
		   	-o-transform: translateX(0);
		   	-ms-transform: translateX(0);
		   	-moz-transform: translateX(0);
		}
</style>
</head>
<body>
	<%@ include file="/WEB-INF/views/public/buttomnav.jsp"%>
		<div class="app">
			<div class="per">
			<ul class="mui-table-view walletUl">
			    <li class="mui-table-view-cell">
			        <a class="mui-navigate-right" href="${hdpath }/merchant/merEarningsDetail?merid=${user.id}">
			        	<p>当前用户：${username}</p>
			        	<p class="realname">真实姓名：<span class="realnameSpan">${user.realname}</span> 
			        		<span class="editrealname iconfont icon-xiugai1"></span>
			        	</p>
						<p>账户余额（元）</p>
						<p class="cashOutP">
						 ${user.earnings == null ? "0.0" : user.earnings} 
						 <button class="cashOutBtn">提现到微信</button>
						</p>
			        </a>
			    </li>
			</ul>
			
			<ul class="mui-table-view">
			    <li class="mui-table-view-cell">
			        <a >注册电话：
			        	${user.phoneNum}
			        </a>
			    </li>
			    <li class="mui-table-view-cell">
			        <a>客服电话：
			        	<span class="tel_span"><span class="phone_s">${user.servephone}</span>
			        	<button type="button" class="mui-btn mui-btn-success mui-btn-outlined edit">
			         		<c:if test="${user.servephone == null}">新增</c:if>
			        		<c:if test="${user.servephone != null}">修改</c:if>
			        	</button>
			        	</span>
			        	<span class="edit_tel">
			        		<input placeholder="请输入客服电话" value="${user.servephone}"></input>
				        	<button type="button" class="mui-btn mui-btn-success submit" data-id="${user.id}">提交</button>
				        	<button type="button" class="mui-btn close">取消</button>
			        	</span>
			        </a>
			    </li>
			</ul>

			<ul class="mui-table-view">
			    <li class="mui-table-view-cell">
			        <a class="mui-navigate-right" href="/merchant/mybankcard">我的银行卡</a>
			    </li>
			</ul>

			<ul class="mui-table-view">
				 <ul class="mui-table-view cashOut"> 
			        <li class="mui-table-view-cell mui-collapse">
			            <a class="mui-navigate-right" href="#">提现</a>
			            <div class="mui-collapse-content">
						  	<ul class="mui-table-view">
							    <li class="mui-table-view-cell">
							        <a class="mui-navigate-right" href="/merchant/weChatWithdraw">提现到微信零钱 <span>（实时到账）</span>
							        </a>
							    </li>
							</ul>
							<ul class="mui-table-view">
							    <li class="mui-table-view-cell">
							        <a class="mui-navigate-right" href="/merchant/withdraw?type=1">
							        提现到银行卡 <span>（第二个工作日到账）</span>
							    	</a>
							    </li>
							</ul>
							<ul class="mui-table-view">
							    <li class="mui-table-view-cell">
							        <a class="mui-navigate-right" href="/merchant/withdraw?type=2">
							        	提现到对公账户 <span>（七个工作日内到账）</span>
							        </a>
							    </li>
							</ul>
			            </div>
			        </li>
			    </ul>
			    <li class="mui-table-view-cell">
			        <a class="mui-navigate-right" href="/merchant/withdrawrecord">提现记录</a>
			    </li>
			     <ul class="mui-table-view"> 
			        <li class="mui-table-view-cell mui-collapse">
			            <a class="mui-navigate-right" href="#">提现说明</a>
			            <div class="mui-collapse-content">
			            <strong style="font-size: 14px;">注意：</strong><br>
						<p class="PDetail1">
							1.提现到零钱：需微信实名认证，实时到账，限额10000<br>
							2.提现到银行卡：需要审核，第二个工作到账<br>
							3.提现到对公账户：七个工作日内到账<br>
						</p>
			            </div>
			        </li>
			    </ul>
			</ul>
			
		   <ul class="mui-table-view"> 
		        <li class="mui-table-view-cell mui-collapse">
		            <a class="mui-navigate-right" href="#">子账户管理</a>
		            <div class="mui-collapse-content child-manage">
		            <c:forEach  items="${childUserList}" var="userInfo">
		            	<div class="manage-item">
		            		<div class="child-content">
			            		<div class="child-wrapper child-name">
			            			<div class="left">账号昵称：</div> 
			            			<div class="right">
			            				<span>${userInfo.username}</span>
			            			</div>
			            		</div>
			            		<div class="child-wrapper child-phone">
				            		<div class="left">账号电话：</div> 
				            		<div class="right">
			            				<span>${userInfo.phoneNum}</span>
			            				<input type="text" class="mui-input-clear" placeholder="请输入子账号电话">
			            			</div>
				            	</div>
			            	</div>
			            	<div class="child-handle">
			            		<button type="button" class="mui-btn mui-btn-danger delete" >删除</button>
			            	</div>
			            	<div class="child-edit">
			            		<button type="button" class="mui-btn cancel">取消</button>
			            		<button type="button" class="mui-btn mui-btn-success submit">提交</button>
			            	</div>
		            	</div>
		            </c:forEach>
		            	<button type="button" class="mui-btn mui-btn-success add">添加</button>
		            	<div class="child-tip"> 
		            		<div><strong>绑定子账号流程：</strong></div>
		            		<p>1、注册的微信需关注”自助充电平台“公众号，进入公众号，点击”后台管理“-”商家登录“</p>
		            		<p>2、进入注册页面（注：此微信未在本平台注册过商户/商户子账号），依次输入手机号、邀请码、验证码。<strong>注意： 邀请码一栏要输入商户的手机号</strong></p>
		            		<p>3、输入完成点击 ”登录“即可完成注册子账号</strong></p>
		            		<p>4、商户进入“后台管理”-“我的”，找到子账号管理，点击“添加”，输入子账号的手机号，点击“提交”，即可完成子账号的绑定</p>
		            	</div>
		            </div>
		         </li>
		    </ul>
			
			 <ul class="mui-table-view set"> 
		        <li class="mui-table-view-cell mui-collapse">
		            <a class="mui-navigate-right" href="#">设置</a>
		            <div class="mui-collapse-content" data-id="${messdata.id}"  data-merid="${user.id}" id="infoUl">
		                <ul class="mui-table-view">
						    <li class="mui-table-view-cell">
							<strong>消息通知设置</strong>
							</li>
						</ul>
						<ul class="mui-table-view">
						    <li class="mui-table-view-cell">
								<span>提现通知   </span>
								<div class="mui-switch mui-switch-mini mui-switch1 <c:if test="${messdata.withmess == 1}" > mui-active </c:if>" data-name="with" >
								  <div class="mui-switch-handle"></div>
								</div>
							</li>
						</ul>
						<ul class="mui-table-view">
						    <li class="mui-table-view-cell">
								<span>订单通知   </span>
								<div class="mui-switch mui-switch-mini mui-switch1 <c:if test="${messdata.ordermess == 1}" > mui-active </c:if>" data-name="order">
								  <div class="mui-switch-handle"></div>
								</div>
							</li>
						</ul>
						<%-- <ul class="mui-table-view">
						    <li class="mui-table-view-cell">
								<span>设备上下线通知 </span>
								<div class="mui-switch mui-switch-mini mui-switch1 <c:if test="${messdata.equipmess == 1}" > mui-active </c:if>" data-name="equip">
								  <div class="mui-switch-handle"></div>
								</div>
							</li>
						</ul>
						<ul class="mui-table-view">
						    <li class="mui-table-view-cell">
								<span>收益通知 </span>
								<div class="mui-switch mui-switch-mini mui-switch1 <c:if test="${messdata.incomemess == 1}" > mui-active </c:if>" data-name="incomemess">
								  <div class="mui-switch-handle"></div>
								</div>
							</li>
						</ul> --%>
						<ul class="mui-table-view">
						   <li class="mui-table-view-cell">
							<strong>退款设置</strong>
							</li>
						</ul>
						<ul class="mui-table-view">
						   <li class="mui-table-view-cell">
								<span>是否开通脉冲模块自动退费 </span>
								<div class="mui-switch mui-switch-mini mui-switch1 <c:if test="${messdata.incoinrefund != 2}" > mui-active </c:if>" data-name="incoinrefund">
								  <div class="mui-switch-handle"></div>
								</div>
							</li>
						</ul>
						<ul class="mui-table-view">
						    <li class="mui-table-view-cell">
							<strong>提现设置</strong>
							</li>
						</ul>
						<ul class="mui-table-view">
						    <li class="mui-table-view-cell">
								<span>是否开启自动提现</span>
								<%-- <div class="mui-switch mui-switch-mini mui-switch1 <c:if test="${messdata.autoWithdraw == 1}" > mui-active </c:if>" data-name="autowithdraw" >
								  <div class="mui-switch-handle"></div>
								</div> --%>
								<div style="float: right;">
									<div class='mySwitch autoreflect<c:if test="${messdata.autoWithdraw == 1}" > sw_active </c:if>' data-name="autowithdraw" >
										<div class="myItem"></div>
									</div>
								</div>
							</li>
						</ul>
						<ul class="mui-table-view">
						   <li class="mui-table-view-cell">
							<strong>缴费设置</strong>
							</li>
						</ul>
						<%-- <ul class="mui-table-view">
						   <li class="mui-table-view-cell">
								<span>是否开通自动缴费 </span>
								<div class="mui-switch mui-switch-mini mui-switch2 autoPay <c:if test="${messdata.autopay != 0}" > mui-active </c:if>" data-name="autoPay">
								  <div class="mui-switch-handle"></div>
								</div>
							</li>
						</ul> --%>
						<ul class="mui-table-view">
						   <li class="mui-table-view-cell">
								<span>是否开通合伙人自动分摊缴费</span>
								<div style="float: right;">
									<div class='mySwitch apportion<c:if test="${messdata.apportion == 1}" > sw_active </c:if>' >
										<div class="myItem"></div>
									</div>
								</div>
								<%-- <div class="mui-switch mui-switch-mini mui-switch2 apportion <c:if test="${messdata.apportion != 0}" > mui-active </c:if>" data-name="apportion">
								  <div class="mui-switch-handle"></div>
								</div> --%>
							</li>
						</ul>
						<ul class="mui-table-view">
							<div style="height: 20px;"></div>
						</ul>
		            </div>
		        </li>
		    </ul>
		</div>
	</div>
<!-- 修改真实姓名开始 -->
<div class="page">
<div class="weui-form">
  <div class="weui-form__text-area">
    <h2 class="weui-form__title">编辑个人信息</h2>
    <div class="weui-form__desc">为确保能提现成功、输入的真实姓名要和微信实名认证的姓名一致。</div>
  </div>
  <div class="weui-form__control-area">
    <div class="weui-cells__group weui-cells__group_form">
      <div class="weui-cells__title">填写信息</div>
      <div class="weui-cells weui-cells_form">
        <div class="weui-cell weui-cell_active" id="js_cell">
          <div class="weui-cell__hd"><label class="weui-label">真实姓名</label></div>
          <div class="weui-cell__bd weui-flex">
              <input id="js_input" class="weui-input" autofocus type="text"  placeholder="请输入真实姓名" maxlength="16" />
              <button id="js_input_clear" class="weui-btn_reset weui-btn_icon weui-btn_input-clear">
                <i class="weui-icon-clear"></i>
              </button>
          </div>
        </div>
      </div>
    </div>
    <div class="weui-cells__group weui-cells__group_form">
      <div class="weui-cells__title">默认信息</div>
      <div class="weui-cells weui-cells_form">
        <div class="weui-cell weui-cell_active weui-cell_readonly">
          <div class="weui-cell__hd"><label class="weui-label">用户昵称</label></div>
          <div class="weui-cell__bd">
              <input class="weui-input" placeholder="请输入用户昵称" value="${user.username}" readonly/>
          </div>
        </div>
        <div class="weui-cell weui-cell_active weui-cell_disabled">
          <div class="weui-cell__hd"><label class="weui-label">用户电话</label></div>
          <div class="weui-cell__bd">
              <input class="weui-input" placeholder="请输入用户电话" value="${user.phoneNum}" readonly/>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="weui-form__opr-area weui-half-screen-dialog__ft">
  	<a class="weui-btn weui-btn_default" href="javascript:" id="closeTolltips">关闭</a>
    <a class="weui-btn weui-btn_primary" href="javascript:" id="showTooltips">确定</a>
  </div>
</div>
</div>
<!-- 修改真实姓名结束 -->
</body>
</html>
<script>
function MySwitch(argu,callback){
	argu= argu || {}
	this.mySwitch= argu.wrapper || document.getElementsByClassName('mySwitch')[0],
	this.myItem= this.mySwitch.getElementsByClassName('myItem')[0],
	this.largeWidth= 70,
	this.largeHeigth= 30
	this.smallWidht= 47,
	this.smallHeight= 30,//28
	this.width= this.largeWidth, //实际宽度
	this.height= this.largeHeigth,
	
	this.callback= callback || function(){}
	this.text= argu.text || false, //是否有文字
	this.size= argu.size || 'large'
}
	MySwitch.prototype.init= function(){
		this.switch()
		if(!this.text){
			this.mySwitch.classList.add('noText')
		}
		if(this.size =='small'){
			this.mySwitch.style.width= this.smallWidht+'px'
			this.mySwitch.style.height= this.smallHeight+'px'
			this.mySwitch.style.lineHeight= this.smallHeight+'px'
			this.mySwitch.borderRadius= this.smallHeight+'px'
			this.myItem.style.width= (this.smallHeight-2)+'px'
			this.myItem.style.height= (this.smallHeight-2)+'px'
			this.width= this.smallWidht
			this.height= this.smallHeight
		}
		if(this.mySwitch.classList.contains('sw_active')){
			this.myItem.style.left= (this.width-this.height-2)+'px';
			this.mySwitch.classList.add('sw_active')
		}
	}
	MySwitch.prototype.switch= function(fn){
		var that=this
		this.mySwitch.addEventListener('click',function(){
		fn && fn.call(that)
	})
}

</script>
<script>
$(function(){
	var merid= $('#infoUl').attr('data-merid').trim()
    var swObj= new MySwitch({
    	wrapper: $('.apportion').get(0),
		text: false, //默认false
		size: 'small' //默认large
	})
    swObj.init()
    swObj.switch(function(){
    	var apportion= $('.apportion').hasClass('sw_active') ? 0 : 1 //有sw_active，即将关闭 0，否则开启1
    	var that= this
    	sendAjax({
    		merid:merid,
   	 		apportion: apportion
    	},function(res){
    		if(res.code == 200){
    			if(!that.mySwitch.classList.contains('sw_active')){
					that.myItem.style.left= (that.width-that.height-2)+'px';
					that.mySwitch.classList.add('sw_active')
					that.callback({active: true})
				}else{
					that.myItem.style.left= '0px';
					that.mySwitch.classList.remove('sw_active')
					that.callback({active: false})
				}
    			mui.toast(res.message,{duration: 1500})
   	 		}else{
   	 			mui.toast(res.message,{duration: 1500})
   	 		}
    	})
    })
    /* 开启自动提现 */
    var autoreflectSwitch= new MySwitch({
    	wrapper: $('.autoreflect').get(0),
		text: false, //默认false
		size: 'small' //默认large
	})
	autoreflectSwitch.init()
    autoreflectSwitch.switch(function(){
    	var autoreflect= $('.autoreflect').hasClass('sw_active') ? 2 : 1 //有sw_active，即将关闭 2，否则开启1
    	var that= this
    	sendAjax({
    		merid: merid,
    		autowithdraw: autoreflect
    	},function(res){
    		if(res.code == 200){
    			if(!that.mySwitch.classList.contains('sw_active')){
					that.myItem.style.left= (that.width-that.height-2)+'px';
					that.mySwitch.classList.add('sw_active')
					that.callback({active: true})
				}else{
					that.myItem.style.left= '0px';
					that.mySwitch.classList.remove('sw_active')
					that.callback({active: false})
				}
    			mui.toast(res.message,{duration: 1500})
   	 		}else{
   	 			mui.toast(res.message,{duration: 1500})
   	 			mui.prompt( "", "请输入真实姓名，与微信实名一致", "实名认证", ['取消', '确认'],  function(options){
   	 				if(options.index === 1 ){
   	 					var realname= $('.mui-popup-input input').val().trim()
   	 					$.ajax({
   	 						url: '/merchant/setMername',
   	 						type: 'post',
   	 						data: {
	   	 						merId: merid,
								realName: realname
   	 						},
   	 						success: function(res){
   	 							if(res.code == 200){
   	 								$('.autoreflect').click()
   	 							}else{
   	 								mui.toast(res.message)
   	 							}
   	 						},
   	 						error: function(){
   	 							mui.toast('设置真实姓名出错！')
   	 						}
   	 					})
   	 				}
   	 			})
   	 		}
    	})
    })
    
 	$('.cashOutBtn').on('click',function(e){
    	e= e || window.event
    	e.stopPropagation()
    	location.href= '/merchant/weChatWithdraw'
    	e.preventDefault()
    }) 
  //设置消息通知
    $('.mui-switch1').each(function(i,item){
    		item.addEventListener("toggle",function(event){
    		 handleGetToggle()
    		})
    	
    })
    	
    	function handleGetToggle(){ //获取切换开关
    		var data= {}
    		$('.mui-switch1').each(function(i,item){
    			var status= $(item).hasClass('mui-active') ? 1 : 2 // 1是开启， 2是关闭
    			var key=  $(item).attr('data-name').trim()
    			data[key]= status
    		})
    		var merid= $('#infoUl').attr('data-merid').trim()
    		data.merid= merid
    		console.log(data)
    		$.ajax({
    			url: '/allowAuthority/messSwitch',
    			data: data,
    			type: 'POST',
    			success: function(e){
    				if(e.code == 200){
    					mui.toast('设置成功！',{duration: 1500})
    				}
    			},
    			error: function(){}
    		})
    	}
   
    /*$(".apportion")[0].addEventListener("tap",function(event){
    	 $(this).addClass('mui-disabled')
	   	var apportion= 0  
    	if(event.detail.isActive){
	   	    console.log("你启动了开关");
	   	 	apportion= 1
	   	  }else{
	   	    console.log("你关闭了开关");
	   	 	apportion= 0
	   	  }
	   	sendAjax({
   	 		merid:merid,
   	 		apportion: apportion
   	 	},function(res){
   	 		$(".apportion").removeClass('mui-disabled')
   	 		if(res.code == 200){
   	 			if(apportion ==1){
   	 				mui.toast('设置成功！',{duration: 1500})
   	 			}
   	 		}else{
   	 			mui(".apportion").switch().toggle();
   	 			mui.toast(res.message,{duration: 1500})
   	 		}
   	 	})
    })*/
     
      /* $(".autoPay")[0].addEventListener("toggle",function(event){
    	 $(this).addClass('mui-disabled')
	   	var autoPay= 0  
    	if(event.detail.isActive){
	   	    console.log("你启动了开关");
	   	 	autoPay= 1
	   	  }else{
	   	    console.log("你关闭了开关");
	   		 autoPay= 0
	   	  }
	   	sendAjax({
   	 		merid:merid,
   	 		autoPay: autoPay
   	 	},function(res){
   	 		$(".autoPay").removeClass('mui-disabled')
   	 		if(res.code == 200){
   	 			if(autoPay ==1){
   	 				mui.toast('设置成功！',{duration: 1500})	
   	 			}
   	 		}else{
   	 			mui(".autoPay").switch().toggle()
   	 			mui.toast(res.message,{duration: 1500})
   	 		}
   	 	})
    }) */
    
    function sendAjax(data,callback){
    	$.ajax({
				url: '/allowAuthority/Switch',
				data: data,
				type: 'POST',
				success: function(res){
					callback && callback(res)
				},
				error: function(){
					
				}
			})
    }
    
    /* 点击编辑商户的客服电话 */
    $('.tel_span .edit').on('click',function(){
    	$('.edit_tel input').val($('.phone_s').text())
    	$('.tel_span').hide()
    	$('.edit_tel').show()
    })
    
    $('.edit_tel .submit').on('click',function(){
    	var phone= $('.edit_tel input').val().trim()
    	var uid= $(this).attr('data-id').trim()
    	var reg= /^1(3|4|5|6|7|8|9)\d{9}$/g
    	var reg2= /^\d{2,4}\-\d{6,9}$/g
    	var reg3= /^\d{6,13}$/g
    	if(!(reg.test(phone) || reg2.test(phone) || reg3.test(phone)) && phone != ''){
    		return mui.toast('请输入正确的手机号！',{duration: 1500})
    	}
    	$.ajax({
    		url: '/merchant/redactAccountInfo',
    		type: 'post',
    		data: {merid: uid,servephone:phone},
    		success: function(res){
    			if(res.code === 200){
    				mui.toast('修改成功！',{duration: 1500});
    				$('.tel_span .phone_s').text(phone);
    				var text= phone.length > 0 ? '修改': '新增'
    				$('.tel_span .edit').text(text)
    				$('.edit_tel').hide();
    		    	$('.tel_span').show();
    			}else{
    				mui.toast('修改失败！',{duration: 1500});
    			}
    		},
    		error: function(err){
    			mui.toast('修改失败！',{duration: 1500});
    		}
    	})
    })
    
	$('.edit_tel .close').on('click',function(){
    	$('.edit_tel input').val('')
    	$('.edit_tel').hide()
    	$('.tel_span').show()
    })
    
    /* 子账户逻辑 */
    $('.child-manage .add').on('click',function(){ //添加子账号
    	var str= 
    		'<div class="manage-item">\n'+
			    	'<div class="child-content">\n'+
					'<div class="child-wrapper child-name">\n'+
						'<div class="left">账号昵称：</div>\n'+
						'<div class="right">\n'+
							'<span></span>\n'+
						'</div>\n'+
					'</div>\n'+
					'<div class="child-wrapper child-phone">\n'+
			    		'<div class="left">账号电话：</div>\n'+
			    		'<div class="right">\n'+
							'<span style="display: none;"></span>\n'+
							'<input type="text" class="mui-input-clear" placeholder="请输入子账号电话" style="display: inline-block;">\n'+
						'</div>\n'+
			    	'</div>\n'+
				'</div>\n'+
				'<div class="child-handle">\n'+
					'<button type="button" class="mui-btn mui-btn-danger delete" style="display: none;">删除</button>\n'+
				'</div>\n'+
				'<div class="child-edit" style="display: block;">\n'+
					'<button type="button" class="mui-btn cancel">取消</button>\n'+
					'<button type="button" class="mui-btn mui-btn-success submit">提交</button>\n'+
				'</div>\n'+
			'</div>'
		$(str).insertBefore('.child-manage .add')
		$(this).hide()
    })
    $('.child-manage').on('click','.child-edit .submit',function(){
    	var manageItem= $(this).parents('.manage-item') //点击元素的父元素
    	var phone= manageItem.find('.child-content .child-phone input').val().trim()
    	if(phone == '' || phone== null){
    		return mui.toast('请输入手机号')
    	}
    	if(!/^1[3|4|5|6|7|8|9|]\d{9}$/.test(phone)){
    		return mui.toast('请输入正确的手机号')
    	}
    	/* ajax请求,成功之后将返回的结果显示出来 */
  		$.ajax({
   				url: '/merchant/addOrDelChileUser',
   				type: 'post',
   				data: {
   					phoneNum: phone,
   					type: 1
   				},
   				success:function(res){
   					if(res.code === 200){
   						manageItem.find('.child-name .right span').text(res.username)
   				    	manageItem.find('.child-phone .right span').text(res.phoneNum).show()
   				    	manageItem.find('.child-content .child-phone input').val('').hide()
   				    	manageItem.find('.child-edit').hide()
   				    	manageItem.find('.child-handle .delete').show();
   				    	$('.child-manage .add').show()
   					}
   					mui.alert(res.message)
   				},
   				erro:function(err){
   					console.log(err);
   					mui.alert('失败')
   				}
   			})
  		
    })
    $('.child-manage').on('click','.child-edit .cancel',function(){ //点击取消添加子账户
    	$(this).parents('.manage-item').remove()
    	$('.child-manage .add').show()
    })
    $('.child-manage').on('click','.child-handle .delete',function(){ //点击删除子账户
    	var phone= $(this).parents('.manage-item').find('.child-phone .right span').text().trim()
    	mui.confirm('确实删除子账户吗？','提示',function(options){
    		var that= this
    		if(options.index == 1){
    			/* 发送ajax请求，成功之后删除元素 */
    			$.ajax({
    				url: '/merchant/addOrDelChileUser',
    				type: 'post',
    				data: {
    					phoneNum: phone,
    					type: 2
    				},
    				success:function(res){
    					if(res.code === 200){
    						$(that).parents('.manage-item').remove()
    					}
    					mui.alert(res.message)
    				},
    				erro:function(err){
    					console.log(err);
    					mui.alert('失败')
    				}
    			})
    			
    		}
    	}.bind(this))
    })
    handleChangUserInfo() //修改个人信息
    function handleChangUserInfo(){
    	$('.editrealname').on('click',function(e){
    		e= e || window.event
        	e.stopPropagation()
        	$('#js_input').val($('.realnameSpan').text().trim())
        	$('.page').addClass('show')
        	e.preventDefault()
    	})
    	$('#closeTolltips').on('click',function(){ //关闭编辑个人信息
    		$('.page').removeClass('show')
    	})
    	$('#showTooltips').on('click',function(){ //提交个人信息
    		var realname= $('#js_input').val().trim()
   			$.ajax({
 	 			url: '/merchant/setMername',
 	 			type: 'post',
 	 			data: {
	  	 			merId: merid,
					realName: realname
 	 			},
 	 			success: function(res){
 	 				if(res.code == 200){
 	 					$('.realnameSpan').text(realname);
 	 					$('.page').removeClass('show')
 	 					mui.toast('设置成功');
 	 				}else{
 	 					mui.toast(res.message)
 	 				}
 	 			},
 	 			error: function(){
 	 				mui.toast('设置真实姓名出错！')
 	 			}
 	 		})			
    	})
    }
});
</script>