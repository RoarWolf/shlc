<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="hdpath" value="${pageContext.request.contextPath }" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>我的</title>
<link rel="stylesheet" href="${hdpath}/css/base.css">
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<%-- <link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/icons-extra.css" /> --%>
<!--App自定义的css-->
<%-- <link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/app.css" /> --%>
<script src="${pageContext.request.contextPath }/mui/js/iconfont.js"></script>
<script type="text/javascript" src="${hdpath }/js/jquery-2.1.0.js"></script>
<script src="${pageContext.request.contextPath }/mui/js/mui.min.js"></script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<style>
@font-face {font-family: "iconfont";
  src: url('//at.alicdn.com/t/font_1648193_pm6tcos5y8e.eot?t=1582275759742'); /* IE9 */
  src: url('//at.alicdn.com/t/font_1648193_pm6tcos5y8e.eot?t=1582275759742#iefix') format('embedded-opentype'), /* IE6-IE8 */
  url('data:application/x-font-woff2;charset=utf-8;base64,d09GMgABAAAAAAYgAAsAAAAAC5gAAAXTAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHEIGVgCDXgqJTIdtATYCJAMcCxAABCAFhG0HehvfCSMRpoSvTvYXB5lcj53O/b6OvOwQ2WFsVUVDGC7++rQfjfs9H/49689FME1SD+AOvxZZWFlA7dpqV8oDkACA7bTgcE1zLScFAmHIKHIVpgpYwSCxfVciNXiQQ/CAcibq8onbYdSwlHDFHXfHf3YUkisrpf+5n69L3zzy2rO5ZAeOhjvjAQ2s+kdbheA+IP5h7NIybwcCwIYE5IFUr1m/OWRwqKsIANKvd88ukCN68AIigawSMw5ykAUQINOZ9A6A+c7fk29QTGSAQmBQ79TYqNEduuk1/2d+i6I988PfnRUAfQxgAPIAcIBMy7T7wbx4Hhhs4nSO5gDskEEhdnXeGDPRLDN7m/9bFqKd6jCBXasCCBgENP/PAweICCRXIb51QgRMr4EAAcwYBBhgJiIgAGYZAhQwe/ObKf6Hwssll6wDdgBRAJkLkA9QlaWOXQA9jOflEFxut0uNJKqaFSU7SZjqdTp9ceGcj1QQxXQnJwEEHjGexVOhFrI4iWJqmaho710F8d4s3gbaS8jsp0o5Ys0Gy8QM+/IsEWxd55I8uTl4luUwXzmM2Tm/tfcoL6EC5+zpXB5Jxv4+af70aYtgsNWzZ611aUuXG65+8qTHM7Plk0/Nu4nGI2FcziSwYcsqmhcjSzdgdL/kVMay1/IBM3x5H8L5BWTtX5nreDCRBfqz7V0MsHzdW8HHnNt2edhqKenautM9ClNhKj835Af22Qk7JcbZUzppzXI0LS45ElfOVnMCs+0tpoujuvPYuYCw/inrOnrWz9fc92dNEPdP51K4HJSBGR5sTVbGeeQMwhyzubf4lWv4cxjlEfxnp8DfEG70ypfizrv+fnP4l2DTcBnA9WDgWU3C5u08fSnClV//0wEbf/Mq7g8mKqBpW8qTla1PHqY2IQjDuP6Vfw+f6yO+FPuRkDv5sCPiT2nza4/oj9z5Ucfh8CJHni11sUkSf1bnTjpijzR+T9ciaUeMI9+QMfvsRBswWi+Mnwpov9yuUvVXjfco/ktTSzJfp59Jf9W8VmpqGmcgiU3sS+fPo/1o3/nz+9JAOt+U9Etk9j1YnxgGgdEGRo/6BFt6kAb2dxjG/tfuW7HyXR7kCfHiMx4//YGP/PuuWmJ1x6uNn67Erf3m5ha6j/30xrULhGQhvlh7jEZQ+adfs/8d9fP6jWrnq572WqvUMQ5d/zHuR/xyjjFSuS8k6P/3Ta6pTS6hzdluPU7fzcbQM764pB0pvgUXfd5d2buyjG9G6c9J6w44ohzqOtWKstQxyow1a0Z2hjCqSo3XHnhf6zVGiY6ae3WfxQxu+XTd0mBWedOgamkGIKi6hjIiGio9fAmnaA+TX2MUXic/DFrTGlRzH1lT4GVZj3TeSXmoEo17CvYccTevgN/3CT34KN5D2De+WvMher/x0Z+Jno6Ydiu2b0tsWellmwg8KDZrdUvO3FNvaqhBqCzUJIQJAADrHHvDdACgr+jvQDeJvd8cNfoPJ0xCy/9gv7Mw61fLVGUx/8YiBHY5X3RyVPhNVp5+6MXws/sdbjX0JLsfeMEVqCz4E7QcBahfxZJINUhc6DSMluXIf63IlNAbQpQCsGkDwPvvduNNd4rwwfiWVI4IaKCQEA8MMtLQHJ8HAhSUgggZlcGGXNQ6rsCDIWAgPAxADqYCIHBiP1DYcR4YnLiL5vjXICAC34EIJ2FgQ0viuaSCDPIXrx4UzrCn4o/YSxd0k63c0lfEvnEaFjm0T2jmEJ0dn8ZTL+igXWzJ3/HcLFBQaekZ3A1NI5RUKng7Ls3S/clJyHqjYy/tYDWopnCGPRV/xF66YI22VeL8r4h947Sgp8j+Cc08OZ0dn1ZAv8i7Sj2nUp2/47mhaKBAF0tLz8iKRosLpex5Fbwdlw2i6f4E9QtV5cfDK9vzPAZgG6iXlIahkWE5vqTspGVDdXgIV67YdSb91TZC0f2X0sVf7uZfad7CyczhkimHCfxfsvVFPe5e2Y4GAwA=') format('woff2'),
  url('//at.alicdn.com/t/font_1648193_pm6tcos5y8e.woff?t=1582275759742') format('woff'),
  url('//at.alicdn.com/t/font_1648193_pm6tcos5y8e.ttf?t=1582275759742') format('truetype'), /* chrome, firefox, opera, Safari, Android, iOS 4.2+ */
  url('//at.alicdn.com/t/font_1648193_pm6tcos5y8e.svg?t=1582275759742#iconfont') format('svg'); /* iOS 4.1- */
}

.iconfont {
  font-family: "iconfont" !important;
  font-size: 0.68267rem;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.icon-saomiao:before {
  content: "\e614";
}

.icon-iconfontjiantou5:before {
  content: "\e635";
}

.icon-gerenzhongxin:before {
  content: "\e61a";
}

.icon-qianbao:before {
  content: "\e6fe";
}

.icon-qiabao:before {
  content: "\e61f";
}

.icon-peizhitubiaosvg-:before {
  content: "\e669";
}
		body {
			background-color: #f4f4f4;
		}
		a{-webkit-tap-highlight-color:rgba(0,0,0,0);}
		a:hover{text-decoration:none;}
		.app {
			font-size: 0.521rem;
			/* padding-bottom: 2.34667rem; */
			 padding-bottom: 55px;
		}
		.app header {
			height: 7.25rem;
			position: relative;
			background-image: linear-gradient(to right,#09de60,#19a601);
    		background-color: #19a601;
		}
		.app header .userInfo {
			padding: 1.792rem 0.64rem 0 0.64rem;
			color: #fff;
			font-size: 0.64rem;
			line-height: 1.2em;
		}
		.app header .userInfo .name {
			margin-bottom: 0.34133rem;
			width: 75%;
			overflow: hidden;
			text-overflow: ellipsis;
			white-space: nowrap;
		}
		
		.app header .money_de {
			padding: 0 0.64rem;
			position: absolute;
			left: 0;
			top: 4.693rem;
			right: 0;
		}
		.app header .money_de .money_de_con {
			background-color: #DFE5E1;
			height: 4.2667rem;
			border-radius: 0.34133rem;
		}
		.app header .money_de .money_de_con {
			display: flex;
			justify-content: space-around;
			padding: 0.64rem;
			box-sizing: border-box;
		}
		.app header .money_de .money_de_con>div {
			width: 50%;
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: center;
			position: relative;
		}
		.app header .money_de .money_de_con>div .title {
			font-size: 0.597rem;
			color: #000;
			margin-bottom: 0.42667rem;
		}
		.app header .money_de .money_de_con>div .money {
			font-weight: bold;
			font-size: 0.68267rem;
			color: #22B14C;
		}
		.app header .money_de .money_de_con>div.left:after {
			content: '';
			width: 0.04267rem;
			height: 100%;
			position: absolute;
			right: 0;
			top: 0;
			background-color: #ABDBB9;
		}
		.app main {
			padding: 2.34667rem 0.64rem 0.64rem 0.64rem;

		}
		.app main ul {
			margin-bottom: 0.64rem;
		}
		.app main>.ul_list {
			background-color: #fff;
			padding: 0.42667rem 0.64rem;
			border-radius: 0.34133rem;
		}
		.app main>.ul_list li {
			width: 25%;
			float: left;
			padding: 0.128rem 0;
		}
		.app main>.ul_list li:active {
			background-color: #efefef;
		}
		.app main>.ul_list li>a {
			display: flex;
			color: #333;
			align-items: center;
			justify-content: center;
			flex-direction: column;
			font-size: 0.5973rem;
		}
		
		.app main>.ul_list li>a i {
			margin-bottom: 0.42667rem;
			font-size: 0.93867rem;
			color: #A8C6D3;
		}

		.app .ul_info {
			background-color: #fff;
			padding: 0.64rem;
			border-radius: 0.34133rem;
		}
		.app .ul_info li {
			padding: 0.128rem 0;
		}
		.app .ul_info li a {
			color: #333;
			font-size: 0.5973rem;
			display: flex;
			justify-content: space-between;
			padding: 0.29867rem 0;
		}
		.app .ul_info li a:active {
			background-color: #efefef;
		}
		.app main>.ul_info li>a .villSpan {
			width: 100%;
			overflow: hidden;
			text-overflow: ellipsis;
			white-space: nowrap;
			line-height: 1.2em;
		}
		.app main>.ul_info li>a .vill{
			padding-left: 0.42667rem;
			color: #666;
			
		}
		.app main>.ul_info li>a .phone{
			padding-left: 0.42667rem;
			color: #0202d6;
		}
		/* .app main>.ul_info li>a .bindMerSpan{
			color: #666;
			font-size: 0.512rem;
		} */
		.app main>.ul_info li>a .bindMerSpan i{
			vertical-align: middle;
			color:#007bdc;
		}
		.app .ul_info li a .last {
			color: #999;
			font-size: 0.512rem; 
		}
		.app main>.ul_info li {
			border-bottom: 0.04267rem solid #e2e2e2;
		}
		.app .ul_info li a .icon-saomiao {
			font-size: 0.68267rem;
		}
		
</style>
</head>
<body>
	<%@ include file="/WEB-INF/views/public/generalBtnNav.jsp"%>
	<input type="hidden" id="uid" value="${user.id }">
	<input type="hidden" id="merid" value="${user.merid }">
	<input type="hidden" id="openid" value="${user.openid }">
<div class="app">
	<header>
		<div class="userInfo">
			<div class="name">当前用户： ${user.username}</div>
			<div class="user_id">
				<c:choose>
					<c:when test="${user.phoneNum!=null}">电话： ${user.phoneNum}</c:when>
					<c:otherwise>ID： ${numerical}</c:otherwise>
				</c:choose>
			</div>
		</div>
		<div class="money_de">
			<a href="/general/genWalDetail?uid=${user.id}" style="display: block;">
				<div class="money_de_con">
					<div class="left">
						<span class="title">钱包余额</span>
						<span class="money">
							<c:if test="${user.balance == null}">0.00</c:if>
							<c:if test="${user.balance != null}"><fmt:formatNumber value="${user.balance}" pattern="0.00"/></c:if>
						</span>
					</div>
					<div class="right">
						<span class="title">赠送余额</span>
						<span class="money">
							<c:if test="${user.sendmoney == null}">0.00</c:if>
							<c:if test="${user.sendmoney != null}"><fmt:formatNumber value="${user.sendmoney}" pattern="0.00"/></c:if>
						</span>
					</div>
				</div>
			</a>
		</div>
	</header>
	<main>
		<ul class="ul_list clearfix">
			<li>
				<a 
					data-href="${hdpath }/general/generalinfo?uid=${user.id}" 
					href="javascript:void(0);" 
					class="percenter">
					<i class="iconfont icon-gerenzhongxin"></i>
					<span>个人中心</span>
				</a>
			</li>
			<li>
				<a 
					data-href="/general/walletcharge?openid=${user.openid}" 
					href="javascript:void(0);" 
					class="walletcharge" >
					<i class="iconfont icon-qianbao"></i>
					<span>钱包充值</span>
				</a>
			</li>
			<li>
				<a 
					data-href="${hdpath }/general/icoperate?uid=${user.id}" 
					href="javascript:void(0);"
					class="ICCardHander">
					<i class="iconfont icon-qiabao"></i>
					<span>IC卡操作</span>
				</a>
			</li>
			<li>
				<a 
					data-href="/general/packageMonthInfo?uid=${user.id}&openid=${user.openid}" 
					href="javascript:void(0);"
					class="monthlyInfo">
					<i class="iconfont icon-peizhitubiaosvg-"></i>
					<span>包月信息</span>
				</a>
			</li>
		</ul>
		
		<ul class="ul_info">
			<li>
				<a href="/money/payMoneyRecord?uid=${user.id}">
					<span class="title">交易记录</span>
					<span class="last">
						全部订单
						<i class="iconfont icon-iconfontjiantou5"></i>
					</span>
				</a>
			</li>
			<li>
				<a href="/charge/chargeRecordList?uid=${user.id}">
					<span class="title">充电记录</span>
					<span class="last">
						全部订单
						<i class="iconfont icon-iconfontjiantou5"></i>
					</span>
				</a>
			</li>
			<c:choose>
				<c:when test="${areaname!=null}">
				<li>
					<a href="javascript:void(0);">
						<span class="title villSpan" >所属小区: 
							<span class="vill">${areaname}</span>
						</span>
					</a>
				</li>
				</c:when>
			</c:choose>
			
			<li>
			  	<c:choose>
			  		<c:when test="${phoneNum == null && servephone == null }">
			  			<a id="bindmer" href="javascript:void(0);" >
							<span class="bindMerSpan">请扫描设备二维码绑定商家 <i class="iconfont icon-saomiao"></i></span>
						</a>
			  		</c:when>
			  		 <c:otherwise>
			  		 	<c:choose>
			  		 		<c:when test="${servephone != null}">
			  		 			<a href="tel:${servephone}" class="last_a">
									<span class="title">商户电话: <span class="phone">${servephone }</span></span>
								</a>
			  		 		</c:when>
			  		 		<c:otherwise>
			  		 			<a href="tel:${phoneNum}" class="last_a">
									<span class="title">商户电话: <span class="phone">${phoneNum }</span></span>
								</a>
			  		 		</c:otherwise>
			  		 	</c:choose>
				 		
					 </c:otherwise>
			  	</c:choose>
				
			</li>
		</ul>

		
	</main>
</div>
<script>
	var htmlwidth = document.documentElement.clientWidth || document.body.clientWidth;
    var fontSize= htmlwidth/16
    var style= document.createElement('style')
    style.innerHTML= 'html { font-size: '+fontSize+'px !important;}'
    var head= document.getElementsByTagName('head')[0]
    head.insertBefore(style,head.children[0])
</script>
</body>
</html>
<script>
$(function() {
	$("#walletinfo").click(function() {
		window.location.href = "${hdpath}/general/genWalDetail?uid=" + $("#uid").val();
	});
	/* $("#walletcharge").click(function() {
		var merid = $("#merid").val();
		var openid = $("#openid").val();
		if (merid == null || merid == 0) {
			mui.alert("请绑定商户后充值");
		} else {
			window.location.href = "/general/walletcharge?openid=" + openid;
		}
	}); */
	$(".ul_list li a").on('click',function(e){
		var href= $(this).attr('data-href').trim()
		if($(this).hasClass('walletcharge')){
			var merid = $("#merid").val();
			var openid = $("#openid").val();
			if (merid == null || merid == 0) {
				return mui.alert("请绑定商户后充值");
			} 
		}
		window.location.href = href
	})
	$('#bindmer').click(function() {
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
							if (url.indexOf("http://www.tengfuchong.com.cn/oauth2pay?code=",0) != -1) {
								var index = url.indexOf("=",0);
								var code = url.substring(index +1);
								$.ajax({
									type : "post",
									url : '${hdpath }/general/scanQrcodeBingMer',
									dataType : "json",
									data :{code:code},
									success : function(data) {
										if (data == 0) {
											mui.alert("此设备未绑定商户，请重新扫描有绑定商户的设备二维码",function() {
											});
										} else if (data == 1) {
											mui.alert("绑定成功",function() {
												location.reload();
											});
										} else if (data == 2) {
											mui.alert("用户登陆已过期，请关闭页面重新进入");
										} else if (data == 3) {
											mui.alert("钱包暂时不可用");
										}
									}
								});
							}
						}
					});
				})

				wx.error(function(res) {
					alert("错误：" + res.errMsg);
				});
			},//返回数据填充
		});
		
	});
})
</script>