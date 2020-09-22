<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"/>
	<meta http-equiv="X-UA-Compatible" content="ie=edge">
	<title>商户缴费列表</title>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- 	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%> --%>
	<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
	<link rel="stylesheet" href="/css/base.css">
	<link rel="stylesheet" href="${hdpath}/mui/css/mui.min.css">
	<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
	<script type="text/javascript" src="${hdpath}/mui/js/mui.min.js"></script>
	<script src="${hdpath}/js/jquery.js"></script>

 <style>
 @font-face {font-family: "iconfont";
  src: url('//at.alicdn.com/t/font_1565272_vjyqkx60fci.eot?t=1576467664559'); /* IE9 */
  src: url('//at.alicdn.com/t/font_1565272_vjyqkx60fci.eot?t=1576467664559#iefix') format('embedded-opentype'), /* IE6-IE8 */
  url('data:application/x-font-woff2;charset=utf-8;base64,d09GMgABAAAAAAKcAAsAAAAABkwAAAJPAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHEIGVgCCcApUaAE2AiQDCAsGAAQgBYRtBzQbkgXIHpIkAUAEMXxwCgwBiYf/36/fPu/ebyJJFbJomoyXMGsaCUKBUolaxRqre0j+9n8uczhAIfOmD0idGpFCW8xvOX1pxpqcY3XCD4TZhJtcW4B1NPSJe8f/tzmQ+YByWnPQ2hhQF3c+QPeyKOBAirFh7AKXcJ9As3EtlfPbhycwLbOWBeJ+FDgwnYvIMss3CvU1e7N4pE5j8aB4jYfh7+OntWikUJdYFZdubvmw+oW9IFXLqkqqqc7nAjtEwiqQiWu1mQtqgrFrmjN102BfpcEXZenbxF7Ngv11VnErGIXZ4AtXdqv5Am5rokFgZNRnEIO3y+/fo/nxh802rTqiNdqefpqoe/Jx91XttFFzdWrdlklf5bFiIm1MrTbPwypQ/i/v0q9UE6s+s4UVa+ngf7olg88f+w7F/ViZj8b9eIP/ocqzDJB1uWUpsipqGR8oDJ6npVkzSjjQ7Xesoe/khdCob4SvwViKpNEskWmrqNNiA/UaHaPZip3DLXqImshNWHYHEDo9odDuGZJOH0Sm/aBOvz/U64yEZjei58wW83Fn9pyQo+Sje42Rx7lx7DoblR5RcJ46kpWHnDOSMopjv9Mr5ncoJ5ljQ3kRDFQNGuEMt8FjlKaMhXBMnnZC1WLc7Zq6N3U8zmDWOYI4FPEh1zUU8bCc8Yer2dL3j5DAuZRDWnpqwmeIKEXTo76OXg9iR5P36rmXV0oXAgNKGcgQLIO2wSySSjFU1M+KEY/qCEekCmNdu5/pq+msr8u+7gg0s4xF2JbkdWouo9znSwAAAAA=') format('woff2'),
  url('//at.alicdn.com/t/font_1565272_vjyqkx60fci.woff?t=1576467664559') format('woff'),
  url('//at.alicdn.com/t/font_1565272_vjyqkx60fci.ttf?t=1576467664559') format('truetype'), /* chrome, firefox, opera, Safari, Android, iOS 4.2+ */
  url('//at.alicdn.com/t/font_1565272_vjyqkx60fci.svg?t=1576467664559#iconfont') format('svg'); /* iOS 4.1- */
}

.iconfont {
  font-family: "iconfont" !important;
  font-size: 0.6956521739130435rem ;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
 .icon-iconwindow:before {
     content: "\e616";
}
 html {
     height: 100%;
     overflow: hidden;
}
 body {
     font-size: 0.5217391304347826rem;
     color: #666;
     padding: 0.6521739130434783rem 0.43478260869565216rem;
     position: fixed;
     left: 0;
     right: 0;
     bottom: 0;
     top: 0;
     overflow: hidden;
}
 .app h4 {
     color: #999;
     font-size: 0.6086956521739131rem;
     margin: 0.8695652173913043rem 0 0.43478260869565216rem;
}
 .app h4.first{
     margin-top: 0;
}
 .app .deviceList2 table {
     table-layout:fixed;
     width: 100%;
     border-left: 0.043478260869565216rem solid #add9c0;
     border-top: 0.043478260869565216rem solid #add9c0;
     border-radius: 0.17391304347826086rem;
     overflow: hidden;
     margin-top: 0.6521739130434783rem;
     border-collapse: inherit;
}
 .app .main {
     position: absolute;
     left: 0.43478260869565216rem ;
     right: 0.43478260869565216rem;
     bottom: 2.1739130434782608rem;
     width: auto;
}
 .app .main table {
     margin-top: 0;
}
 .app .deviceList2 table td {
     text-align: center;
     line-height: 1.5217391304347827rem;
     border-bottom: 0.043478260869565216rem solid #add9c0;
     border-right: 0.043478260869565216rem solid #add9c0;
     overflow: hidden;
     text-overflow: ellipsis;
     white-space: nowrap;
}
 .app .deviceList2 table td.percentTd {
     color: #337ab7;
}
 .app .deviceList2 table td.percentTd:hover {
     text-decoration: underline;
}
 .app .deviceList2 .lastTr i{
     position: relative;
     top: 0.08695652173913043rem;
}
 .app .deviceList2 table thead{
     background-color: #C8EFD4;
     color: #333;
     font-weight: 600;
}
 .app .deviceList2 table tbody {
     background-color: #f5f7fa;
}
 .app .deviceList2 table tr td:nth-child(2){
    /* width: 25%;
     */
     overflow: hidden;
     text-overflow:ellipsis;
     white-space: nowrap;
}
/* 弹出层 */
 .mask {
     position: absolute;
     left: 0;
     right: 0;
     bottom: 0;
     top: 0;
     background-color: rgba(0,0,0,.5);
     z-index: 2;
     display: none;
}
 .orderList {
     position: absolute;
     left: 0;
     bottom: 2.1739130434782608rem;
     right: 0;
     min-height: 6.521739130434782rem;
     background-color: #fff;
     border-top: 0.043478260869565216rem solid #eee;
     border-radius: 0.34782608695652173rem 0.34782608695652173rem 0 0;
     padding: 0.6521739130434783rem 0.6521739130434783rem;
     font-size: 0.6086956521739131rem;
     transition: all .4s;
     transform: translateY(100%);
     z-index: 3;
}
 .orderList .title_p{
     font-size: 0.6086956521739131rem;
     margin-bottom: 0.30434782608695654rem;
}
 .deviceInfo {
     display: flex;
    /* flex-wrap: wrap;
     */
     margin-bottom: 0.6521739130434783rem;
     color: #333;
     word-break: break-all;
}
 .parInfo_ul li {
     display: flex;
     justify-content: space-between;
     font-size: 0.6086956521739131rem;
     color: #333;
     line-height: 2em;
}
 .parInfo_ul li span:nth-child(2){
     color: #22B14C;
     font-weight: 700;
}
 .parInfo_ul li span:nth-child(2) strong {
     font-size: 0.6956521739130435rem;
}
/* 弹出层 结束*/
/* 底部菜单开始 */
 nav.navBar{
     background-color: #141C26;
     z-index: 4;
}
 nav.navBar .icon_con{
     width: 2.608695652173913rem;
     height: 2.608695652173913rem;
     border-radius: 50%;
     background-color: #141C26;
     position: absolute;
     left: 0.43478260869565216rem;
     bottom: 0;
     display: flex;
     justify-content: center;
     align-items: center;
}
 nav.navBar .icon_con .list_i{
     width: 2.1739130434782608rem;
     height: 2.1739130434782608rem;
     border-radius: 50%;
     background-color: #2f3338;
     display: flex;
     justify-content: center;
     align-items: center;
}
 nav.navBar .icon_con .list_i i{
     font-size: 1.1304347826086956rem;
     color: #424242;
}
 nav.navBar .icon_con .list_i span {
     position: absolute;
     right: 0;
     top: 0;
     height: 0.9565217391304348rem;
     line-height: 0.9565217391304348rem;
     padding: 0 0.17391304347826086rem;
     background-color: #ED1C24;
     color: #fff;
     border-radius: 0.9565217391304348rem;
     display: none;
}
 nav.navBar .icon_con .list_i.active {
     background-color: #007AAE;
}
 nav.navBar .icon_con .list_i.active span {
     display: block;
}
 nav.navBar .icon_con .list_i.active i{
     color: #fff;
}
 .merPay {
     margin-left: 3.4782608695652173rem;
     font-size: 0.6956521739130435rem;
     font-weight: 700;
     color: #fff;
     float: left;
     height: 100%;
     display: flex;
     align-items: center;
}
 .goPay {
     /* float: right; */
     position: absolute;
     right: 0;
     top: 0;
     background-color: #22B14C;
     height:100%;
     display: flex;
     align-items: center;
}
 .goPay button {
     background-color: transparent;
     border: none;
     color: #fff;
     height: 100%;
     padding: 0 1.3043478260869565rem;
}
 .goPay button:active {
     background-color: transparent;
}
.mui-bar-tab {
	height: 2.1739130434782608rem;
}
.app .mui-checkbox input{
	 top: 0.17391304347826086rem;
	 right: 0.8695652173913043rem;
	 width: 1.2173913043478262rem;
	 height: 1.1304347826086956rem;
}
.mui-popover.mui-popover-action .mui-table-view {
	margin: 8px 0;
}
/* 底部菜单结束 */

.mui-popover.mui-popover-action {
	width: calc(100% - 2*0.43478260869565216rem);
}
.app .deviceList2 table tr.red {
	color: #f55d63;
}
.app .deviceList2 table tr.origin {
	color: #ec8842;
}
 </style>
</head>
<body>
<input type="hidden" id="openid" value="${ mer.openid }" />
<div class="app" data-openpartpay="${authority.apportion}"  data-areaname="${area.name}" data-id="${area.id}" data-merid="${merid}" data-dealer="${ mername }">
<input type="hidden" id="aid" value="${ mer.aid }" />
<form id="from">
	<input type="hidden" id="appId" name="appId" value="" />
	<input type="hidden" id="paySign" name="paySign"  value="" />
	<input type="hidden" id="timeStamp" name="timeStamp"  value="" />
	<input type="hidden" id="nonceStr" name="nonceStr"  value="" />
	<input type="hidden" id="package" name="package"  value="" />
	<input type="hidden" id="signType" name="signType"  value="" />
</form>	
		<div class="deviceList2 parInfo">
			<h4 class="first">合伙人信息</h4>
				<table cellspacing="0">
					<thead>
						<tr>
							<td>昵称</td>
							<td>电话</td>
							<td>分成比</td>
							<td>应交金额</td>
						</tr>
					</thead>
					<tbody>
						<c:if test="${fn:length(listscanpa) >0 }">
							<c:forEach items="${listscanpa}" var="listPa">
								<tr data-partId="${listPa.partid}" data-percent="${listPa.percent*100}" data-username="${listPa.nickname}">
									<td>
										<c:if test="${listPa.nickname == null}">— —</c:if>
										<c:if test="${listPa.nickname != null}">${listPa.nickname}</c:if>
									</td>
									<td>${listPa.phone}</td>
									<td>${listPa.percent*100}%</td>
									<td class="par_money">0</td>
								</tr>
							</c:forEach>
						</c:if>
						<c:if test="${fn:length(listscanpa) <=0 }">
							<tr class="noPart">
								<td colspan="4" style="text-aligin: center;">暂无合伙人</td>
							</tr>	
						</c:if>
					</tbody>
				</table>
		</div>
		<div class="deviceList2">
				<h4>设备列表</h4>
				<div class="mui-scroll-wrapper main">
					<div class="mui-scroll">
						<table cellspacing="0">
							<thead>
								<tr>
									<td>设备号</td>
									<td>设备名称</td>
									<td>到期时间</td>
									<td>应交金额</td>
									<td>选择</td>
								</tr>
							</thead>
							<tbody>
								<c:if test="${fn:length(equiresult) > 0 }">
									<c:forEach items="${equiresult}" var="equ">
										<tr
											<c:choose>
												<c:when test="${equ.differnum <= 0}">class="red"</c:when>
												<c:when test="${equ.differnum <= 15}">class="origin"</c:when>
											</c:choose>
										>
											<td>${equ.code}</td>
											<td>
												<c:if test="${equ.remark == null}">— —</c:if>
												<c:if test="${equ.remark != null}">${equ.remark}</c:if>
											</td>
											<td>
												<c:if test="${equ.expiratimes == null}">— —</c:if>
												<c:if test="${equ.expiratimes != null}">${equ.expiratimes}</c:if>
											</td>
											<td>
												<c:set var="key"  value="${equ.hardversion}" />
												<!-- hardversionnum == '02' 蓝牙版本 -->
												<c:if test="${equ.hardversionnum == '02'}">
													${ blue[key] }
												</c:if>
												<!-- hardversionnum != '02' 网络版本 -->
												<c:if test="${equ.hardversionnum != '02'}">
													${ net[key] }
												</c:if>
											</td>
											<td>
												<div class="mui-input-row mui-checkbox">
												  <label></label>
												  <input data-parse="
												  	<c:set var="key"  value="${equ.hardversion}" />
													<c:if test="${equ.hardversionnum == '02'}">${ blue[key] }</c:if>
													<c:if test="${equ.hardversionnum != '02'}">${ net[key] }</c:if>"
													<c:if test="${equ.expirationTime == null}">disabled</c:if>
													data-code="${equ.code}" name="checkbox1" value="Item 1" type="checkbox"
													<c:choose>
														<c:when test="${equ.differnum <= 0}">checked</c:when>
														<c:when test="${equ.differnum <= 15}">checked</c:when>
													</c:choose>
												   >
												</div>
											</td>
										</tr>
									</c:forEach>
								</c:if>
								<c:if test="${fn:length(equlistByAid) <=0 }">
									<tr class="noPart">
										<td colspan="5" style="text-aligin: center;">暂无设备</td>
									</tr>	
								</c:if>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<!-- 缴费说明：
				设备自绑定日起我们为每台设备免费提供为期一年的流量服务，为确保您的设备能正常时候，请在服务到期之前为设备续缴费用。（推荐使用自动缴费）
				查看设备到期时间： 
				1、登录“自助充电平台”（www.he360.cn）
				2、选择“设备管理”，可以查看每台设备的到期时间
				缴费方式：
				目前缴费方式有两种：自动和手动
				自动： 设备到期时会自动在商户收益中扣除（商户余额>=缴费金额）
				手动： 商户需要手动选择设备进行自行缴费
				手动缴费步骤：
				1、进入微信“自助充电平台”公众 -> 选择商家登录 -> 管理 -> 缴费管理
				2、设备分为两种： 绑定到小区的设备分类和未绑定小区的设备,
				3、点击小区中的“缴费”进入缴费界面，选择缴费设备，点击缴费即可。(绑定到小区的设备缴费的时候是根据合伙人的分成比来进行缴费的，如添加合伙人，则忽略此条注释)
				4、
			 -->

			<div class="mask"></div>
			<div class="orderList" data-show="2">
				<p class="title_p">缴费小区：<span id="areaName">回忆小区</span></p>
				<p class="title_p">已选择设备</p>
				<div class="deviceInfo">
					<span>
					<!-- 000001,000002,000003,000004,000005,000006,000007,
					000008,000009, -->
					</span>
				</div>
				<p class="title_p">缴费信息</p>
				<ul class="parInfo_ul">
					<!-- <li><span>合伙人1应缴</span><span>￥<strong>100.33</strong></span></li>
					<li><span>合伙人2应缴</span><span>￥<strong>99.23</strong></span></li>
					<li><span>合伙人3应缴</span><span>￥<strong>105.21</strong></span></li>
					<li><span>合伙人4应缴</span><span>￥<strong>109.86</strong></span></li>
					<li><span>商户应缴</span><span>￥<strong>232.6</strong></span></li> -->
				</ul>
			</div>

			<nav class="mui-bar mui-bar-tab navBar">
				<div class="icon_con">
					<div class="list_i ">
						<i class="iconfont icon-iconwindow"></i>
						<span>1</span>
					</div>
				</div>
				<div class="merPay">
					商户缴费：¥<span id="merPayMoney">0</span>
				</div>
				<div class="goPay">
					<button>去结算</button>
				</div>
			</nav>
	</div>
	
	<div id="sheet1" class="mui-popover mui-popover-bottom mui-popover-action ">
	    <ul class="mui-table-view">
	      <li class="mui-table-view-cell">
	        <a href="#" class="pay wechart">微信支付</a>
	      </li>
	      <li class="mui-table-view-cell">
	        <a href="#" class="pay wallet">钱包支付</a>
	      </li>
	    </ul>
	    <!-- 取消菜单 -->
	    <ul class="mui-table-view">
	      <li class="mui-table-view-cell">
	        <a href="#sheet1"><b>取消</b></a>
	      </li>
	    </ul>
	</div>
<script>
	var htmlwidth = document.documentElement.clientWidth || document.body.clientWidth;
    var fontSize= htmlwidth/16
    var style= document.createElement('style')
    style.innerHTML= 'html { font-size: '+fontSize+'px !important;}'
    var head= document.getElementsByTagName('head')[0]
    head.insertBefore(style,head.children[0])
</script>
<script>
$(function(){
	mui('.mui-scroll-wrapper').scroll({
		deceleration: 0.0005 //flick 减速系数，系数越大，滚动速度越慢，滚动距离越小，默认值0.0006
	});
	var id= $('.app').attr('data-id').trim() //小区id
	var merid= $('.app').attr('data-merid').trim() //商户merid
	var dealer= $('.app').attr('data-dealer').trim() //小区id
	var areaName= $('.app').attr('data-areaname').trim() //小区id 
	var openpartpay= $('.app').attr('data-openpartpay').trim() == 0 ? true : false //是否开启合伙人的分摊缴费 0默认不分摊  1分摊
	var list= [] //存放选中设备的容器
	var partInfo= [] //合伙人应缴金额
	/* 页面加载完毕就判断是否有选中的设备，有选中的设备就将选中的设备放进list中 */
	$('.main tbody input[type="checkbox"]').each(function(index,item){
		if($(item).prop('checked')){
			var code= $(item).attr('data-code').trim()
			var parse=  parseFloat($(item).attr('data-parse'))
			list.push({
				code:code,
				totalMoney: parse
			})
		}
	})
	showInfo(list)
	/*点击选择设备复选框*/
	$('.main tbody input[type="checkbox"]').click(function(){
		var code= $(this).attr('data-code').trim()
		if($(this).prop('checked')){ //选中push
			var parse=  parseFloat($(this).attr('data-parse'))
			list.push({
				code:code,
				totalMoney: parse
			})
		}else{//取消选中splice
			for(var i=0; i< list.length; i++){
				if(list[i].code== code){
					list.splice(i,1)
					break
				}
			}
		}
		showInfo(list)
	})
	//每次点击复选框，自动计算选择数量
	function showInfo(list){
		partInfo= []
		var length= list.length
		if(length > 0){
			$('.list_i span').text(length)
			$('.list_i').addClass('active')
		}else{
			$('.list_i span').text(0)
			$('.list_i').removeClass('active')
		}

		// 动态修改个合伙人及商户应缴金额
		var totalMoney= 0
		for(var i=0; i< list.length; i++){
			totalMoney+=list[i].totalMoney
		}
		var totalPercent= 0 //所有合伙人的分成比之和
		var merPercent= 1
		if($('.parInfo .noPart').length <= 0 && !openpartpay){  //存在合伙人
			$('.parInfo tbody tr').each(function(j,jtem){
				var partid= $(jtem).attr('data-partid').trim()
				var percent= parseFloat($(jtem).attr('data-percent').trim())/100
				var username= $(jtem).attr('data-username').trim()
				var partParse= Math.round(totalMoney*percent*100)/100 //合伙人应缴的金额
				totalPercent += percent*100
				partInfo.push({
					id: partid,
					earnings: percent,
					username: username,
					payMonet: partParse,
					rank: -1 //-1代表合伙人  2代表商户
				})
				/*修改页面显示*/
				$(jtem).find('.par_money').text(partParse)
			})
			 merPercent= 1- totalPercent/100 //商家分成比
		}
		
		var merParse= Math.round(totalMoney*merPercent*100)/100 //商家应缴的金额
		partInfo.push({
				id: merid, //商户/合伙人id 
				earnings: merPercent,  //分成比
				username: dealer,  //真实姓名
				payMonet: merParse, //支付金额
				rank: 2 //-1代表合伙人  2代表商户
		})
		/*显示商户需要交的钱数*/
		$('#merPayMoney').text(merParse)
	}

	$('.main').css("top",$('.parInfo').height()+44+15);
	/*点击显示缴费详情*/
	$('.icon_con').click(function(){
		if(list.length <= 0) return
		if($('.orderList').attr('data-show').trim() == 2){ //弹出缴费选择信息
			/*渲染缴费信息弹框*/
			var codeStr= ''
			$(list).each(function(j,jtem){
				codeStr+= jtem.code+','
			})
			codeStr= codeStr.slice(0,codeStr.length-1)
			$('.deviceInfo span').text(codeStr)

			$('.parInfo_ul').html('')
			$(partInfo).each(function(i,item){
				var $li= $('<li><span>'+item.username+'</span><span>￥<strong>'+item.payMonet+'</strong></span></li>')
				$('.parInfo_ul').append($li)
			})	
			$('#areaName').text(areaName)

			$('.mask').show()
			$('.orderList').css({
				"transform": "translateY(0px)"
			})
			$('.orderList').attr('data-show',1)
		}else{
			$('.mask').hide()
			$('.orderList').css({
				"transform": "translateY(100%)"
			})
			$('.orderList').attr('data-show',2)
		}
		
	})
	
	
	$('.mask').click(function(){
		$('.mask').hide()
		$('.orderList').css({
			"transform": "translateY(100%)"
		})
		$('.orderList').attr('data-show',2)
	})

/* ================================================= */

var tapFlag= false  //默认是false,当为true时，禁止点击缴费
//点击缴费按钮
$('.goPay button').click(function(){
	if(tapFlag){ 
		return 
	}
	if(list.length <= 0){
		mui.toast('请先选择设备再进行缴费')
		return
	}
	mui('#sheet1').popover('toggle');
})
	
//点击选择支付方式
$('#sheet1 .pay').on('tap',function(){ 
	if(tapFlag){
		return //当tapFlag为true时，不往下走
	}
	var from= $(this).hasClass('wechart') ? 1 : 2 //1为微信支付， 2为钱包支付
	mui('#sheet1').popover('toggle');
	tapFlag= true  //选择支付方式之后不能再次点击，防止误点
	if(from === 1){
		console.log('微信支付')
		if(partInfo.length > 1){ //当有合伙人分摊时，禁止使用微信支付，可以再次选择支付方式
			tapFlag= false 
			return mui.toast('对不起，您当前存在合伙人分摊缴费，暂不支持微信缴费！')
		}
		
		var item= partInfo[0]
		item.aid= $('#aid').val().trim()
		item.openid= $('#openid').val().trim()
		
		var data= {
				openid: $('#openid').val().trim(),
				payMoney: partInfo[0].payMonet,
				devices: JSON.stringify(list),
				users: JSON.stringify(item),
				id: id  //小区id
		}
		getWXReqInfo(data)
		
	}else {  //钱包支付
		var paytype
		if(partInfo.length <=1){ //无合伙人
			if(list.length <= 1){ //选择一台设备
				paytype= 0
			}else{ //选择多台设备
				paytype= 1
			}
		}else{ //有合伙人
			if(list.length <= 1){ //选择一台设备
				paytype= 2
			}else{ //选择多台设备
				paytype= 3
			}
		}
		$.ajax({
			url: '/merchant/merPayment',
			type: 'post',
			dataType: 'json',
			data: {
				devices: JSON.stringify(list),
				users: JSON.stringify(partInfo),
				paytype: paytype,
				id: id  //小区id
			},
			success: function(res){
				console.log(res)
				if(res.code == 200){
					mui.confirm('设备缴费成功',function(){
						window.location.reload()
					})
				}else if(res.code == 100 || res.code == 400 || res.code == 401 || res.code == 402 || res.code == 405){
					mui.confirm(res.message)
				}else{
					mui.confirm('缴费失败')
				}
			},
			error:function(err){
				mui.confirm('缴费失败')
			},
			complete: function(){
				isPaying= false
			}
		})
	}
	
})
	
/*获取微信支付的信息*/
function getWXReqInfo(data){ 
	$.ajax({
		url: '/wxpay/merWxPayment',
		type: 'post',
		data: data,
		success: function(res){
			if(res.wolferror === 1 ){
				tapFlag= false //获取信息失败时，可以再次点击选择缴费
				return mui.toast(res.wolferrorinfo)
			}
			$('#appId').val(res.appId);
			$('#paySign').val(res.paySign);
			$('#timeStamp').val(res.date);
			$('#nonceStr').val(res.nonceStr);
			$('#package').val(res.packagess);
			$('#signType').val(res.signType);
			callpay()
		},
		complete: function(){
			
		}
	})
}



	
	
/* 微信支付函数 */
function onBridgeReady() {
		var appId= $('#appId').val().trim();
		var paySign= $('#paySign').val().trim();
		var timeStamp= $('#timeStamp').val().trim();
		var nonceStr= $('#nonceStr').val().trim();
		var packageStr= $('#package').val().trim();
		var signType= $('#signType').val().trim();
	WeixinJSBridge.invoke('getBrandWCPayRequest', {
		"appId" : appId, //公众号名称，由商户传入
		"paySign" : paySign, //微信签名
		"timeStamp" : timeStamp, //时间戳，自1970年以来的秒数
		"nonceStr" : nonceStr, //随机串
		"package" : packageStr, //预支付交易会话标识
		"signType" : signType
	//微信签名方式
	}, function(res) {
		if (res.err_msg == "get_brand_wcpay_request:ok") {
			mui.confirm('缴费成功！',function(){
				window.location.reload()
			})
		} else if (res.err_msg == "get_brand_wcpay_request:cancel") {
			mui.toast('取消支付')
		} else if (res.err_msg == "get_brand_wcpay_request:fail") {
			mui.toast('支付失败')
		} //使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
		/* 回调成功之后就可以，再次点击支付了 */
		tapFlag= false
	});
}
/* 调起微信支付 */
function callpay() {
	if (typeof WeixinJSBridge == "undefined") {
		if (document.addEventListener) {
			document.addEventListener('WeixinJSBridgeReady', onBridgeReady,
					false);
		} else if (document.attachEvent) {
			document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
			document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
		}
	} else {
		onBridgeReady();
	}
}
})
</script>
</body>
</html>