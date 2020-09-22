<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="hdpath" value="${pageContext.request.contextPath }" />
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>订单功率曲线</title>
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!--标准mui.css-->
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/icons-extra.css" />
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<script src="${hdpath }/mui/js/mui.min.js"></script>
<!--App自定义的css-->
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script src="${hdpath}/js/jquery.min.js"></script>
<script src="${hdpath}/js/echarts.min.js" defer></script>

<style type="text/css">
@font-face {font-family: "iconfont";
  src: url('//at.alicdn.com/t/font_1221168_150zdehnmop.eot?t=1587975801296'); /* IE9 */
  src: url('//at.alicdn.com/t/font_1221168_150zdehnmop.eot?t=1587975801296#iefix') format('embedded-opentype'), /* IE6-IE8 */
  url('data:application/x-font-woff2;charset=utf-8;base64,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') format('woff2'),
  url('//at.alicdn.com/t/font_1221168_150zdehnmop.woff?t=1587975801296') format('woff'),
  url('//at.alicdn.com/t/font_1221168_150zdehnmop.ttf?t=1587975801296') format('truetype'), /* chrome, firefox, opera, Safari, Android, iOS 4.2+ */
  url('//at.alicdn.com/t/font_1221168_150zdehnmop.svg?t=1587975801296#iconfont') format('svg'); /* iOS 4.1- */
}

.iconfont {
  font-family: "iconfont" !important;
  font-size: 16px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.icon-xiaoquguanli:before {
  content: "\e60e";
}

.icon-shebeimingcheng:before {
  content: "\e6de";
}

.icon-gonglvpower99:before {
  content: "\e646";
}

.icon-dianliang:before {
  content: "\e612";
}

.icon-icon-test:before {
  content: "\e601";
}

.icon-ziyuanxhdpi:before {
  content: "\e639";
}

.icon-yonghu-tianchong:before {
  content: "\e670";
}

.icon-laiyuan2:before {
  content: "\e77a";
}

.icon-icon:before {
  content: "\e611";
}

.icon-consumption:before {
  content: "\e6f1";
}

.icon-shijian:before {
  content: "\e681";
}

.icon-zhuangtai:before {
  content: "\e632";
}



* {
		padding: 0;
		margin: 0;
	}
	.mui-title {
	line-height: 1.878rem; 
	}
	li {
		list-style: none;
	}
	.powerLine {
		font-size: 0.512rem;
		margin-top: 1.707rem;
		padding: 0.64rem 0.4268rem 0;
		color: #666;
	}
	.mui-bar {
		height: 1.707rem;
		background-color: #f5f7fa;
		
	}
	.mui-bar h1 {
		font-size: 0.512rem;
		color: #666;
	}

	.powerLine .top li {
		border: 0.0427rem solid #add9c0;
		overflow: hidden;
		padding:0.4268rem;
		border-radius: 0.2134rem;
		background-color: #f5f7fa;
		margin-bottom: 0.64rem;
	}
	.powerLine li:active {
		background-color: #f5f5f373;
	}
	.powerLine .top li h3 {
		font-size: 0.597rem;
		font-weight: 400;
		color: #666;
	}
	.powerLine .top li div{
		min-width: 50%;
		float: left;
	}
	.powerLine .top li div:nth-child(1){
		width: 100%;
	}
	
	.powerLine .top li div span {
		line-height: 1.28rem;
		text-align: center;
	}
	.powerLine .top li div span .mui-icon-help {
		font-size: 0.68267rem;
	}
	.powerLine .top li div .title{
		color: #999;
	}
	.powerLine .top li div span i {
		color: #22B14C;
		font-size: 0.64rem;
		margin-right: 0.2134rem;
	}
	.powerLine .bottom ul {
		display: table;
		width: 100%;
		border-left: 0.0427rem solid #add9c0;
		border-top: 0.0427rem solid #add9c0;
		border-radius: 0.341rem;
		overflow: hidden;
	}
	.powerLine .bottom ul li{
		display:table-row;
		width: 100%;
		background-color: #f5f7fa;

	}

	.powerLine .bottom ul li.title {
		font-weight: 600;
		background-color: #C8EFD4;
		color: #333;
	}
	.powerLine .bottom ul li span {
		display:table-cell;
		text-align: center;
		border-bottom: 0.0427rem solid #add9c0;
		border-right: 0.0427rem solid #add9c0;
		min-height: 1.493rem;
		vertical-align:middle;
	}
		
	.powerLine .top .infoTop{
      padding: 0.8536rem 0;
      background-color: #fff;
      border-top:0.0427rem solid #ccc;
      border-bottom:0.0427rem solid #ccc;
      margin-bottom: 0.8536rem;
    }
    .powerLine .top .left p,
    .powerLine .top .right p{
      text-align: center;
       color: #666;
       font-size: 0.597rem;
       margin: 0;
    }
    .powerLine .top div.left p:last-child,
    .powerLine .top div.right p:last-child{
      font-size: 0.939rem;
      color: #666;
      line-height: 1.5em;
    }
   .powerLine .top  .left {
    width: 50% !important;
    float: left;
   }
   .powerLine h3{
    font-size: 0.597rem;
    font-weight: 400;
    color: #666;
    margin: 0;
	}
	.mui-bar .mui-icon {
		padding-top: 0.4268rem;
		padding-bottom: 0.4268rem;
		font-size: 1.024rem;
		
	}
	.charge_table {
		display: none;
		margin: 0.6402rem 0;
	}
	.charge_but {
		text-align: center;
	}
	.charge_but button {
		background: #f5f7fa;
		border: 0.0427rem solid #add9c0;
		font-size: 0.597rem;
	}
#popover li a {
	font-size: 12px;
	oveflow: hidden;
}
.mui-popover li a div {
	min-width: 50%;
	float: left;
}
.mui-popover li a div span:last-child {
	color: #777;
}

</style>

</head>
<body data-orderId= "${orderId}">
	<div class="powerLine">
		<header class="mui-bar mui-bar-nav">
			<a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left" onclick="window.history.go(-1)"></a>
			<h1 class="mui-title">功率曲线</h1>
		</header>
		<main>
				<div class="top">
					<li>
					
			              <div class="left">
			                <p>已充时长</p>
			                <p class="timePEle"  data-begin-time="${mapinfo.begintime}" data-end-time="${mapinfo.endtime}">${mapinfo.usetime}</p>
			              </div>
			              <div class="right">
			                <p>消耗电量(度)</p>
			                <p>${mapinfo.useelec/100}</p>
			              </div>
			  
					</li>
					<script>
					 /* 
					 	var beginTime= $('.timePEle').attr('data-begin-time').trim()
					    var endTime= $('.timePEle').attr('data-end-time').trim()
					    if(endTime == '' || endTime== null){
					    	var time= ${mapinfo.usetime} //分钟
					    	handleTime(time)
					    }else {
					    	var timeDur= Math.round((new Date(endTime)- new Date(beginTime))/60000)
					    	if(timeDur>=0) {
					    		handleTime(timeDur);
					    	}else {
					    		handleTime(0);
					    	}
					    } 
					 */
					    var time= ${mapinfo.usetime} //分钟
				    	handleTime(time)
					    function handleTime(time){
					    	var h= parseInt(time/60)
							var mi= time%60 >= 10 ? time%60 : '0'+(time%60)
							var se= time%3600 >= 10 ? time%60 : '0'+(time%60)
							var timeStr= h+':'+mi
							$('.timePEle').text(timeStr)
					    }
						
						
					</script>
					<li>
						<h3>已使用信息：</h3>
						<div>
							<span class="title"><i class="iconfont icon-icon"></i>设备编号：</span>
							<span>${mapinfo.devicenum}${chargecode}</span>
						</div>
						<div>
							<span class="title"><i class="iconfont icon-ziyuanxhdpi"></i>充电端口：</span>
							<span>${mapinfo.port}</span>
						</div>
						<div>
							<span class="title"><i class="iconfont icon-shebeimingcheng"></i>设备名称：</span>
							<span>
							<c:if test="${mapinfo.devicename == '' || mapinfo.devicename == null}">— —</c:if>
							<c:if test="${mapinfo.devicename != '' && mapinfo.devicename != null}">${mapinfo.devicename}</c:if>
							</span>
						</div>
						<div>
							<span class="title"><i class="iconfont icon-xiaoquguanli"></i>小区名称：</span>
							<span>
							<c:if test="${mapinfo.areaname == '' || mapinfo.areaname == null}">— —</c:if>
							<c:if test="${mapinfo.areaname != '' && mapinfo.areaname != null}">${mapinfo.areaname}</c:if>
							</span>
						</div>
						<div>
							<span class="title"><i class="iconfont icon-consumption"></i>付款金额：</span>
							<span>${mapinfo.paymoney}元  
							<c:if test="${include == 1}">
								<a href="#popover" id="popoverA" class="mui-icon mui-icon-help"></a>
							</c:if>
						</div>
						<div>
							<span class="title"><i class="iconfont icon-consumption"></i>退费金额：</span>
							<c:choose>
								<c:when test="${number == 1 && mapinfo.refundMoney == 0}">
									<span>${mapinfo.paymoney}元</span>
								</c:when>
								<c:otherwise>
									<span>${mapinfo.refundMoney}元</span>
								</c:otherwise>
							</c:choose>
						</div>
						<div>
							<span class="title"><i class="iconfont icon-gonglvpower99"></i>最大功率：</span>
							<span>${mapfunc.maxpower}</span>瓦
						</div>
						<div>
							<span class="title"><i class="iconfont icon-gonglvpower99"></i>支付方式：</span>
							<span>${mapinfo.paytype == 1 ? "钱包" : mapinfo.paytype == 2 ? "微信" : mapinfo.paytype == 3 ? "支付宝": mapinfo.paytype == 4 ? "包月下发数据" : mapinfo.paytype == 5 ? "投币" : mapinfo.paytype == 6 ? "离线卡" : mapinfo.paytype == 7 ? "在线卡" : "— —"}</span>
						</div>
						
						<div>
							<span class="title"><i class="iconfont icon-icon"></i>订单编号：</span>
							<span>${ordernum}</span>
						</div>
						<div>
							<span class="title"><i class="iconfont icon-shijian"></i>开始时间：</span>
							<span>
								<c:if test="${mapinfo.begintime!=null}"><fmt:formatDate value="${mapinfo.begintime}" pattern="yyyy-MM-dd HH:mm:ss"/></c:if>
								<c:if test="${mapinfo.begintime==null}">— —</c:if>
							</span>
						</div>
						<div>
							<span class="title"><i class="iconfont icon-shijian"></i>结束时间：</span>
							<span>
								<c:if test="${mapinfo.endtime!=null}"><fmt:formatDate value="${mapinfo.endtime}" pattern="yyyy-MM-dd HH:mm:ss"/></c:if>
								<c:if test="${mapinfo.endtime==null}">— —</c:if>
							</span>
							
						</div>
					</li>
				</div>
				<h3>充电功率曲线</h3>	
				<div id="echarts-3" style="margin: auto;padding-bottom:0.341rem;"></div>
				<div class="charge_but"><button type="button" class="mui-btn">查看充电记录</button></div>
				<div class="bottom charge_table">
					<ul>
						<li class="title">
							<span>记录时间</span>
							<span>剩余时间（分钟）</span>
							<span>剩余电量（千瓦·时）</span>
							<span>实时功率（瓦）</span>
							<c:if test="${realrecord[0].portV != -1 && realrecord[0].portA != -1 }">
								<span>实时电压/电流</span>
							</c:if>
						</li>
						<c:forEach items="${realrecord}" var="info"  varStatus="as">
						  <li id="name${info.id}" >
						    <span> 
						    <c:if test="${info.createtime!=null}"><fmt:formatDate value="${info.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/></c:if>
						    <c:if test="${info.createtime==null}">— —</c:if>
						    </span>
							<span>${info.chargetime}</span>
							<span>${info.surpluselec/100}</span>
							<span>${info.power}</span>
							<c:if test="${realrecord[0].portV != -1 && realrecord[0].portA != -1 }">
								<span>
									<div>
										<c:if test="${info.portV == -1}">— —</c:if>
										<c:if test="${info.portV != -1}">${info.portV}V</c:if>
									</div> 
									<div>
										<c:if test="${info.portA == -1}">— —</c:if>
										<c:if test="${info.portA != -1}">${info.portA}A</c:if>
									</div>
								</span>
							</c:if>
						   </li>
						</c:forEach>
					</ul>
				</div>
			</main>
	</div>
	<div id="popover" class="mui-popover">
	  <ul class="mui-table-view">
	  	<c:forEach items="${listcharge}" var="item">
		    <li class="mui-table-view-cell">
		    	<a href="#">
			    	<div><span>订单编号：</span><span>${item.ordernum}</span></div>
		    		<div><span>支付金额：</span><span>${item.moany}元</span></div>
		    		<div><span>支付时间：</span><span><fmt:formatDate value="${item.begintime}" pattern="yyyy-MM-dd HH:mm:ss"/></span></div>
		    	</a>
		    </li>
	    </c:forEach>
	  </ul>
	</div>
</body>
</html>
<script> 
	var htmlwidth = document.documentElement.clientWidth || document.body.clientWidth;
    var fontSize= htmlwidth/16
    var style= document.createElement('style')
    style.innerHTML= 'html { font-size: '+fontSize+'px !important;}'
    var head= document.getElementsByTagName('head')[0]
    head.insertBefore(style,head.children[0])
    
    $('.charge_but button').click(function(){
    	$('.charge_but').fadeOut(0)
    	$('.charge_table').fadeIn(0)
    })
    
    var rate= 0.45
    if($(window).height()>= 800){
    	rate= 0.45
    }else if($(window).height()<= 800 && $(window).height()>= 700){
    	rate= 0.36
    }else{
    	rate= 0.30
    }
    var _height=$(window).height()*rate	;//获取当前窗口的高度
    var _width=$(window).width()*0.95;//获取当前窗口的宽度
    console.log($(window).height())
    $('#echarts-3').css('width',_width+'px').css('height',_height+'px');//调整列表的宽高
</script>
<script type="text/javascript" src="${hdpath}/hdfile/js/powerbrokenline.js" defer></script>