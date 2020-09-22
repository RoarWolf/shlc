<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>端口二维码</title>
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<link rel="stylesheet" href="${hdpath}/css/base.css">
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<script src="${hdpath}/mui/js/mui.min.js"></script>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="${hdpath }/js/jquery-2.1.0.js"></script>
<script type="text/javascript" src="${hdpath }/js/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="${hdpath }/js/html2Canvas.js"></script>
<style>
html,body {
	width: 100%;
	height: 100%;
	overflow: hidden;
}
.app {
	position: absolute;
	left: 0;
	right: 0;
	bottom: 0;
	top: 55px;
	z-index: 9;
	background-color: #efeff4;
	overflow: auto;
}
.grad ul {
	background-color: #f2f2f2;
}
.grad ul li {
	width: 33.33%;
	box-sizing: border-box;
	padding: 10px 15px;
	float: left;
	border: 0.5px solid #eee;
	position: relative;
	text-align: center;
}
.grad ul li .box {
	padding: 5px 0;
}
.grad ul li .box:active {
	background-color: #eee;
}
.grad ul li .l_qr{
	width: 40px;
	height: 40px;
	display: inline-block;
	
}
.grad ul li p {
	margin:0;
}
.grad ul li .qr_c {
	position: absolute;
	left: 0;
	top: 0;
	z-index: -999;
}
.scanCover {
	position: absolute;
	left:0;
	top: 0;
	width: 100vw;
	height: 100vh;
	background-color: #000;
	z-index: 999;
	display: none;
}
.scanCover .b_img {
	width: 80%;
	position: absolute;
	left: 50%;
	top: 50%;
	transform: translate(-50%,-60%);
	-webkit-transform: translate(-50%,-60%);
	-o-transform: translate(-50%,-60%);
	-ms-transform: translate(-50%,-60%);
	-moz-transform: translate(-50%,-60%);

}
.scanCover p {
	position: absolute;
	left: 0;
	right: 0;
	bottom: 15px;
	text-align: center;
}


.bog_qr {
	position: absolute;
	left:0;
	bottom: 0;
	padding: 10px;
	background-color: #fff;
	z-index: -999;
}
.bog_qr p{
	text-align: center;
	color: #000;
	margin: 0;
}
.tip {
	 margin-top: 10px;
	 text-align: center;
	 font-size: 12px;
}
</style>
</head>
<body>
<header class="mui-bar mui-bar-nav">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    <h1 class="mui-title">${code}号设备端口二维码</h1>
</header>
<div class="app">
<input value="${code}" id="code" type="hidden" />
<input value="${version}" id="version" type="hidden" />
<c:choose>
	<c:when test="${version == '02' || version == '07' || version == '09'}">
		<c:set var="portNum" value="1,2" />
	</c:when>
	<c:when test="${version == '05'}">
		<c:set var="portNum" value="1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16" />
	</c:when>
	<c:when test="${version == '06' || version == '10'}">
		<c:set var="portNum" value="1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20" />
	</c:when>
	<c:otherwise>
		<c:set var="portNum" value="1,2,3,4,5,6,7,8,9,10" />
	</c:otherwise>
</c:choose>
		<div class="grad">
			<ul class="clearfix">
				<c:forEach items="${portNum}" var="item" varStatus="info">
					<li>
						<div class="box">
							<div class="l_qr"></div>
							<p>端口${item}</p>
						</div>
					</li>
				</c:forEach>
			</ul>
			<p class="tip">可以点击查看大图</p>
			<br/>
			<p class="tip" style="text-align: left; padding:0 10px; margin-bottom: 0;">批量导出二维码：</p>
			<p class="tip" style="text-align: left; padding:0 10px; margin-top: 0;">登录”自助充电平台（http://www.he360.cn）“->设备管理->设备列表->找到”端口二维码“->点击”查看“并导出即可</p>
		</div>
	</div>
	<div class="bog_qr">
		<div class="b_qr_c"></div>
		<p></p>
	</div>
	<div class="scanCover">
		<img src="" alt="" class='b_img'>
		<p>长按可保存二维码到手机相册</p>
	</div>
<script>
//渲染二维码
$(function(){
	var code=$('#code').val().trim()
	var version=$('#version').val().trim()
	var screenWidth= $('body').width()
	var baseUrl= 'http://www.he360.cn'
	if(window.location.origin.indexOf('www.tengfuchong') != -1){
		baseUrl= 'http://www.tengfuchong.com.cn'
	}
	
	$('.grad li').each(function(i,item){
		$(item).find('.l_qr').qrcode({
			render: "canvas", //也可以替换为table
			width: 40,
			height: 40,
			text: baseUrl+"/oauth2Portpay?codeAndPort="+code+(i+1)
		});
	})

	$('.grad li').on('tap',function(){
		$('.bog_qr canvas').remove()
		var index= $(this).index()
		$('.bog_qr .b_qr_c').qrcode({
			render: "canvas", //也可以替换为table
			width: screenWidth*0.8,
			height: screenWidth*0.8,
			text: baseUrl+"/oauth2Portpay?codeAndPort="+code+(index+1)
		});
		$('.bog_qr p').text(code+'-'+(index+1))
		setTimeout(function(){
			var bog_qr_w= $('.bog_qr').innerWidth()
			var bog_qr_h= $('.bog_qr').innerHeight()
	
			html2canvas(document.querySelector('.bog_qr'),{
				onrendered: function(canvas) {
			    	var bigData= canvas.toDataURL('imgae/png') 
					$('.scanCover .b_img').attr('src',bigData)
					$('.scanCover .b_img').css({
						width: bog_qr_w,
						height: bog_qr_h
					})
					$('.scanCover').fadeIn()
				},
				width: bog_qr_w,
				height: bog_qr_h
		    });
		})
	})

	$('.scanCover').on('tap',function(e){
		e = e || window.event
		var target= e.target || e.srcElement
		if(target.nodeName.toLowerCase() === 'img'){
			return 
		}
		$('.bog_qr canvas').remove()
		$('.scanCover').fadeOut()
	})
})
	
</script>
</body>
</html>