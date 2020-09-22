<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport"
	content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<link rel="stylesheet" type="text/css" href="${hdpath}/mui/css/mui.min.css">
<title>提现记录</title>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<style type="text/css">
	@font-face {font-family: "iconfont";
  src: url('//at.alicdn.com/t/font_1238088_icjqlg7eu8f.eot?t=1562308572224'); /* IE9 */
  src: url('//at.alicdn.com/t/font_1238088_icjqlg7eu8f.eot?t=1562308572224#iefix') format('embedded-opentype'), /* IE6-IE8 */
  url('data:application/x-font-woff2;charset=utf-8;base64,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') format('woff2'),
  url('//at.alicdn.com/t/font_1238088_icjqlg7eu8f.woff?t=1562308572224') format('woff'),
  url('//at.alicdn.com/t/font_1238088_icjqlg7eu8f.ttf?t=1562308572224') format('truetype'), /* chrome, firefox, opera, Safari, Android, iOS 4.2+ */
  url('//at.alicdn.com/t/font_1238088_icjqlg7eu8f.svg?t=1562308572224#iconfont') format('svg'); /* iOS 4.1- */
}

.iconfont {
  font-family: "iconfont" !important;
  font-size: 16px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.icon-qiehuan:before {
  content: "\e62a";
}

.icon-xingming:before {
  content: "\e631";
}

.icon-weibiaoti16:before {
  content: "\e622";
}

.icon-consumption:before {
  content: "\e6f1";
}

.icon-xiala:before {
  content: "\e654";
}

.icon-tishi:before {
  content: "\e661";
}

.icon-dianhua:before {
  content: "\e729";
}

.icon-yinhang:before {
  content: "\e66e";
}

.icon-yinhangqia:before {
  content: "\e690";
}

.icon-icon:before {
  content: "\e611";
}

.icon-shijian:before {
  content: "\e681";
}

.icon-tongji:before {
  content: "\e61b";
}

.icon-shouxufei:before {
  content: "\e62c";
}

.icon-bianhao:before {
  content: "\e62b";
}

.icon-shouxufei-copy:before {
  content: "\e72a";
}



		* {
			padding: 0;
			margin: 0;
		}
		li {
			list-style: none;
		}
		body {
			background-color: #efeff4;
		}
		header h3 {
			margin-top: 0;
			text-align: center;
			padding: 5px 0 10px;
			font-size: 14px;
		}
		.noData {
			position: absolute;
			left: 0;
			top: 40px;
			right: 0;
			bottom: 0;
			text-align: center;
			margin-top: 50%;
			color: #999;
			font-size: 14px;
		}
		.userDetail {
			padding: 15px 10px;
			color: #666;
			font-size: 12px;
		}
		.userDetail ul li {
			border: 1px solid #ccc;
			overflow: hidden;
			padding:10px;
			border-radius: 5px;
			background-color: #f8f8f8;
			margin-bottom: 15px;
		}
		.userDetail ul li:active {
			background-color: #EfEfEf;
		}
		.userDetail ul li div{
			min-width: 50%;
			float: left;	
		}
		.userDetail ul li div:nth-child(1){
			width: 100%;
		}
		
		.userDetail ul li div span {
			/*display: block;*/
			line-height: 30px;
		}
		.userDetail ul li div .title{
			color: #999;
		}
		.userDetail ul li div span i {
			color: #22B14C;
			font-size: 15px;
			margin-right: 5px;
		}
		.spanJust {
			color: #22B14C !important;
		}
		.spanNegative{
			color: #F47378 !important;
		}
		
		.past {
			color: #22B14C !important;
		}
		.processed { /*待处理*/
			color: gray !important;
		}
		.reject {
			color: #F47378  !important;
		}
		.withDrawBill i {
			color: #d0561a !important;
		}
		
		#innerli {
			width: 94%;
			position: fixed;
			left: 50%;
			top: 0;
			background-color: #fff;
			transform: translateX(-50%);
			border-radius: 8px 8px 8px 8px;
			box-shadow: 0 5px 15px rgba(0, 0, 0, .5);
			display: none;
			padding: 15px;
		}
		.mui-table-view-cell  p{
			color: #666;
			font-size: 12px;
			line-hieght: 20px !important;
		}

		.mui-table-view-cell p .left {
			float: left;
		}
		.mui-table-view-cell p .right {
			display: block;
			overflow: hidden
		}

		.mui-table-view-cell.mui-collapse .mui-table-view .mui-table-view-cell {
			padding: 10px 30px;
		}
		/*.toChange {
			color: #60C5F1 !important;
		}
*/
</style>
</head>
<body>
	<div class="userDetail">
		<header>
			<h3>提现记录</h3>
		</header>
		<ul>
		<c:if test="${withdrawList == null }">
		<div class="noData">暂无数据</div>
		</c:if>
		<c:if test="${withdrawList != null }">
		<c:forEach items="${withdrawList}" var="withdraw">
			<li>
				<div>
					<span class="title"><i class="iconfont icon-icon" style="font-size: 14px;"></i>提现单号：</span>
					<span>${withdraw.withdrawnum}</span>
				</div>
				<c:if test="${withdraw.bankcardnum !='0' }">	
				<div>
					<span class="title"><i class="iconfont icon-yinhangqia"></i>银行卡号：</span>
					<span>${withdraw.bankcardnum}</span>
				</div>
				</c:if>
				<div>
					<span class="title"><i class="iconfont icon-yinhang"></i>银行卡所属银行：</span>
					<span>${withdraw.bankname}</span>
				</div>
				<div>
					<span class="title"><i class="iconfont icon-bianhao" style="font-size: 16px;"></i>提现状态：</span>
					<c:if test="${withdraw.status ==0 }">
					<span class="processed">待处理</span>
					</c:if>
					<c:if test="${withdraw.status ==1 }">
					<span class="past">已通过</span> 
					</c:if>
					<c:if test="${withdraw.status ==2 }">
					<span class="reject">被拒绝</span>
					</c:if>
					<c:if test="${withdraw.status ==3 }">
					<span class="past">提现至零钱</span>
					</c:if>
					<c:if test="${withdraw.status ==4 }">
					<span class="processed withDrawBill">待开发票 <i class="iconfont icon-tishi"></i> </span>
					</c:if>
				</div>					
				<div>				
					<span class="title"><i class="iconfont icon-consumption"></i>提现金额：</span>
					<span class="spanNegative moneyNum">${withdraw.money-withdraw.servicecharge}</span>					
					<strong>元</strong>
				</div>
				<div>				
					<span class="title"><i class="iconfont icon-shouxufei-copy" style="font-size: 17px;"></i>提现手续费：</span>
					<span class="spanNegative">${withdraw.servicecharge}</span>					
					<strong>元</strong>
				</div>
				<div>				
					<span class="title"><i class="iconfont icon-shijian" style="font-size: 18px;"></i>提现申请时间：</span>
					<span><fmt:formatDate value="${withdraw.createTime}"
							pattern="yyyy-MM-dd HH:mm:ss" />
					</span>					
				</div>
				<div>				
					<span class="title"><i class="iconfont icon-shijian" style="font-size: 18px;"></i>提现到账时间：</span>
					<span>
					<fmt:formatDate value="${withdraw.accountTime}" pattern="yyyy-MM-dd HH:mm:ss" />
					</span>					
				</div>
			</li>
		</c:forEach>
		</c:if>
		</ul>
	</div>
	<div class="tip">
		<li id="innerli" class="mui-table-view-cell " style="font-size: 14px">
		<!-- <strong>提现到对公账户：</strong><br>
			<p class="PDetail2">
				<strong>发票抬头：</strong><br>
				</p><p><span class="left">公司名称：</span><span class="right">郑州和腾信息技术有限公司</span></p>
				<p><span class="left">纳税人号：</span><span class="right">91410100MA44BL3G5L</span></p>
				<p><span class="left">地址电话：</span><span class="right">郑州高新技术开发区莲花街338号12号楼2层09号 &nbsp;0371-56788915</span></p>
				<p><span class="left">开户银行及账号：</span><span class="right">中国民生银行郑州航海路支行 &nbsp; 153715248</span></p>
				<p><span class="left">邮寄地址信息：</span><span class="right">郑州市高新区莲花街电子电器 产业园西区12号楼二单元2楼09(财务部)郑州和腾信息技术有限公司 &nbsp; 赵女士 &nbsp; 19137642510</span></p>
			<p></p> -->
			
			
			<strong>提现到对公账户：</strong><br>
			<p class="PDetail2">
				<strong>1、开票信息：</strong><br>
				<p><span class="left">公司名称：</span><span class="right">郑州和腾信息技术有限公司</span></p>
				<p><span class="left">纳税人号：</span><span class="right">91410100MA44BL3G5L</span></p>
				<p><span class="left">地址电话：</span><span class="right">郑州高新技术开发区莲花街338号12号楼2层09号 &nbsp;0371-56788915</span></p>
				<p><span class="left">开户银行及账号：</span><span class="right">中国民生银行郑州航海路支行 &nbsp; 153715248</span></p>
				<p><strong>2、开票金额：<font id="invoiceMoney">请输入金额</font></strong><br></p>
				<p><span class="left"><strong>3、发票邮寄地址：</strong></span><span class="right">郑州市高新区莲花街电子电器 产业园西区12号楼二单元2楼09(财务部)郑州和腾信息技术有限公司 赵女士  19137642510</span></p>
			</p>			
		</li>
	</div>
	<script>
		$(function(){
			$('.withDrawBill').click(function(e){
				e= e || window.event
				e.stopPropagation()
				var target= e.target || e.srcElement
				var $liObj= null
				if(target.nodeName.toLowerCase() == 'i'){
					$liObj= $(target).parent().parent().parent()
				}else if(target.nodeName.toLowerCase() == 'span') {
					$liObj= $(target).parent().parent()
				}
				var money= $liObj.find('.moneyNum').text()
				$('#invoiceMoney').html(money+'元')
				$('#innerli').slideDown(300)
			})
			$('#innerli').click(function(e){
				e= e || window.event
				e.stopPropagation()
			})
			$('html').click(function(e){
				console.log(11)
				$('#innerli').slideUp(300)
			})
		})	
	</script>
</body>
</html>