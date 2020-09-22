<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"/>
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<title>IC卡充值</title>
<link rel="stylesheet" href="${hdpath}/mui/css/mui.min.css">
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="${hdpath}/mui/js/mui.min.js"></script>
<script type="text/javascript" src="${hdpath}/js/jquery.js"></script>
<style type="text/css">
	
	html,body,header,section,div,ul,li,p,button,h1,h2,h3 {margin: 0; padding: 0;}
	li { list-style: none; }
	a { text-decoration: none; }
	body {
		background-color: #f8f8f8;
	}
@font-face {font-family: "iconfont";
  src: url('//at.alicdn.com/t/font_1900608_mj4jx1g3aqe.eot?t=1592819926997'); /* IE9 */
  src: url('//at.alicdn.com/t/font_1900608_mj4jx1g3aqe.eot?t=1592819926997#iefix') format('embedded-opentype'), /* IE6-IE8 */
  url('data:application/x-font-woff2;charset=utf-8;base64,d09GMgABAAAAAAUsAAsAAAAACoAAAATgAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHEIGVgCDSAqHcIZhATYCJAMYCw4ABCAFhG0HWxsaCVGULk6e7AtsG/Y0g4ELn+slUiyMKQOgpoYBAAsAJAAAjHj42vvu3N19m1pcokpVvah0FeESFdkBYVBgXCxlPMaD78Lg/s+3qY/SCZ9URIyKGNkcJgaZmgA5ZUcRv/85vRLovsqTbTeWUSNSCPeSC35oCOysmlEzeg0QMAK/Qs3/536+LmkNlteeDWxgM8sOKpCd0Y/iD9TtBZReIBP0v4hnk10oBvthAp3GucOOE2/uYaigRwXirtkhGDpMSgQttGtrwIVFfISe9vRe7S7wQfv7+AeLTSdpMnri1e2pBQc/NZ9ROP2f9q6gQFycA7fLyFgACvEu0PWoY6QW4Do/u3edAN3aJaWo33DrPyT1/z8OGfWFbmYgw388oZGJClXdgD9TNgMoFgiJn24IhZ/+CMHPEFFSNQUmWdLVAjv7QVwAxCvgF/ZOqiKL7lF7bXo7Q5nGmq6utmXjPKWySJIKBSFfFBt0h8TkPxctgFW7SC12D4bJR9EUOY9kzAroGoJ/IdAcqcMqwhfAU3IFSbIcAL0CIYbH4E3VRzw3agoknTEiObh9a51CabfhPhIEfV5y3X7X/rhgvU7UX7GiPJRGSs4besjqaywS6+7MZ7lURphoBn1UQIDhDbGnME+xhCJ3KORXGxkpWIOCIvnG21bq58PoY/VOxrU2fyvtULONdarbIS/08G5lHLfUbmfsq29PNLpCcN+70WClWCCKYXuUZpclx60Cui1ZrlcaSdL/13dsZAQrZ3xFIMusnR5Nt0zu7Ey2TLXayvmqvrPTflOr1Fy3qSrDi0YqI7dcCPce8DMi7qIokSKMUm4jw4Fue10U/miaSWiGip2WYg9qzJAwQPwWYY7uLNmz5A46WueaNBAbNSgIEoAkyRcGUtnw2Mi6tnh7UtwUnWyuziTx5+ipzzkJ2jaRMXlq9DV0nagkrqNr9AamNnjOQ2yx+qqfrYfxQ/jHdnwV/IyKr07L/hn8c0p3kGmQ7O/s76egAPLv/C0KdJhNgiJ21EqPz9pFOnMN5sRHsJvMMdTCn3IuJrD1//GL4DMHUw/KfL0OmRPcZxNPE9iJMNe+Eu8SfPZC6gXCBwXhi/mC+SKJFrRIZrwh0J6pd/auMKvZU7/yb87ce78fo7cOxsX/Fyiirn5KgM3HrwdU/bCUsVfZqwcNQGtL5vEKAPJ3vAigtSHvxxs0xs38CG/JvMy7cNNd+SA3I76qKq3wr+3k0b3khyEJg60NBZKL9ke1j3jHFSccKV8pNdfyHi46BKr7Nf2NhE6d2F19g54WpRlJrHJmaEYsSNqMQdZuAl3wC9DosgxVuz3oNC/p8i5DenRE6cCc2QRCvwOQ9PoGWb976IJXQWPUW6j6w6HTdejdsMtUzSiuhylhC2tLbDa1srk1GevVt+ykgiK7VKM/chR7UfTx9HZnz1lxVMQpvZmz35fYSEVaYiJ5HAuhsSvSPpvJdlOSrA0vLyrolbbrtISxIxFGEsyCapVQM5Om2Mr8xNjz+7eYIyWQKGLoh/MRi8S87SMfnrwToM9VKtHQpjT2yjj29WHChqhbpSahRHQwwVIa6grez8dMiW1NGRJrBi+cRCVVbVevkXN5DXSiz14jRY4STdRoUx96OdrqnQ41kw5TJyUlvG5Ls+JBbTMpl/RweIu3KE8IAA==') format('woff2'),
  url('//at.alicdn.com/t/font_1900608_mj4jx1g3aqe.woff?t=1592819926997') format('woff'),
  url('//at.alicdn.com/t/font_1900608_mj4jx1g3aqe.ttf?t=1592819926997') format('truetype'), /* chrome, firefox, opera, Safari, Android, iOS 4.2+ */
  url('//at.alicdn.com/t/font_1900608_mj4jx1g3aqe.svg?t=1592819926997#iconfont') format('svg'); /* iOS 4.1- */
}

.iconfont {
  font-family: "iconfont" !important;
  font-size: 16px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.icon-user-s:before {
  content: "\e634";
}

.icon-xiaoquguanli:before {
  content: "\e60e";
}

.icon-jine:before {
  content: "\e666";
}

.icon-bianhao:before {
  content: "\e62b";
}

.icon-tel-fill:before {
  content: "\e639";
}
	
  .card-info {
		padding: 15px 10px;
		color: #666;
		font-size: 12px;
	}
	.card-info ul li {
		border: 1px solid #ccc;
		overflow: hidden;
		padding:10px;
		border-radius: 5px;
		background-color: #f5f7fa;
		margin-bottom: 15px;
	}
	.card-info ul li:active {
		background-color: #EfEfEf;
	}
	.card-info ul li div{
		min-width: 50%;
		float: left;	
	}
	.card-info ul li div span {
		/*display: block;*/
		line-height: 30px;
	}
	.card-info ul li div .title{
		color: #999;
	}
	.card-info ul li div span i {
		color: #22B14C;
		font-size: 15px;
		margin-right: 5px;
	}
	
	.icPage {
		font-size: 12px;
		color: #666;
	}

	.icPage header div {
		width: 90%;
		margin: 0 auto;
		text-align: center;
		padding: 25px 0 20px;
	}
	.icPage header div span {
		color: #999;
	}
	.icPage section h3 {
		margin-left: 5%;
		font-size: 14px;
		font-weight: 400;
		color: #666;
		text-align: center;
		margin: 15px 0 10px;
	}
	.icPage section .selectDiv {
		margin-top: 15px;

	}
	.icPage section .selectDiv ul {
		width: 90%;
		margin: 0 auto;
		padding-bottom: 15px;
		display: flex;
		flex-wrap: wrap;
		border-bottom: 1px solid #D9D9D9;
		justify-content: flex-start;
	}
	.icPage section .selectDiv li {
		width: 30%;
		border: 1px solid #ccc;
		box-sizing: border-box;
		border-radius: 5px;
		margin-bottom: 15px;
		text-align: center;
		height: 35px;
		line-height: 35px;
		margin-left: 2vw;
		font-size: 14px;
		background-color: #f5f7fa;
		transition: all .5s;
	}
	.icPage section .selectDiv li.active {
		background-color: #4cd964;
		color: #fff;
		border: 1px solid #1EC63B;
	}
	.icPage section .selectDiv li:nth-child(3n-2){
		margin-left: 2vw;
	}
	.icPage section .displayDiv {
		margin-top: 32px;
		text-align: center;
		font-size: 35px;
		height: 35px;
	}
	.icPage section .displayDiv .icon_yen {
		margin-right: 10px;
	}
	.icPage section .displayDiv .textSpan {
		font-size: 33px;
	}
	.icPage section .btn {
		width: 70%;
		height: 10vw;
		border: 1px solid #22B14C;
		border-radius: 6px;
		text-align: center;
		line-height: 10vw;
		margin: 15px auto;
		background-color: #4cd964;
		color: #fff;
	}
	.icPage section .btn:active {
		background-color: #1EC63B;
	}
	#tipDiv{
		display: none;
		height: 240px;
		justify-content: center;
		align-items: center; 
	}
	#tipDiv p {
		color: #999;
		padding: 0 10px;
	 }
	 
	
	/* .unActivation {
		color: #225ab1d1 !important;
	}
	.loss {
		color: #F47378 !important;
	} */
		/* .showInfo {
			width: 90%;
			margin: 0 auto;
			overflow: hidden;
		}
		.showInfo>div {
			width: 49.5%;
			overflow: hidden;
			float: left;
			margin-bottom: 15px;
		}
		.showInfo>div:last-child{
			min-width: 49.5%;
			width: auto;
		}
		.showInfo>div:nth-child(2n){
			float: right;
		}
		
		.showInfo>div>span {
			display: block;
			box-sizing: border-box;
			border: 1px solid #ccc;
			border-radius: 5px;
			text-align: center;
			line-height: 35px;
			background-color: rgba(0,0,0,0.01);
			padding: 0 12px;
		}
		.showInfo>div>span:first-child{
			float: left;
		}
		.showInfo>div>span:last-child{
			overflow: hidden
		} */
		#remake {
			/*position: absolute;
			left: 5%;
			bottom: 5%; */
			padding: 30px 15px;
			color: #999;
		}
		.left_right {
			overflow: hidden;
		}
		.left_right .left{
			float: left;
			height: 100%
		}
		.left_right .right{
			overflow: hidden;
		}
		.left_right .right p {
			font-size: 14px;
		} 
		
.QRCodeAlert, .QRCodeAlert1 {
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
	z-index: 99;
	color: #000;
	display: none;
}
.QRCodeAlert .top, .QRCodeAlert1 .top  {
	position: relative;
    padding: 15px;
    border-radius: 13px 13px 0 0;
    background: rgba(255,255,255,.95);
}
.QRCodeAlert .top::after, .QRCodeAlert1 .top::after  {
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
.QRCodeAlert .top h3, .QRCodeAlert1 .top h3 {
	font-size: 18px;
    font-weight: 500;
    text-align: center;
}
.QRCodeAlert .top p,.QRCodeAlert1 .top p{
    margin: 14px 0 12px;
    font-family: inherit;
    font-size: 14px;
    text-align: center;
    color: #333;
}
.QRCodeAlert .top input {
    width: 18px;
    height: 18px;
    margin: 0 0 0 5px;
}
.QRCodeAlert .bottom, .QRCodeAlert1 .bottom {
	position: relative;
    display: -webkit-box;
    display: -webkit-flex;
    display: flex;
    height: 44px;
    -webkit-box-pack: center;
    -webkit-justify-content: center;
    justify-content: center;
}
.QRCodeAlert .bottom span, .QRCodeAlert1 .bottom span {
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
.QRCodeAlert .bottom span:first-child, .QRCodeAlert1 .bottom span:first-child {
  border-radius: 0 0 0 13px;
}
.QRCodeAlert .bottom span:first-child::after,.QRCodeAlert1 .bottom span:first-child::after {
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
.QRCodeAlert .bottom span:last-child,
.QRCodeAlert1 .bottom span:last-child{
    border-radius: 0 0 13px;
    font-weight: 600;
}
.QRCodeAlert  .inputDiv,
.QRCodeAlert1  .inputDiv {
	position: relative;
	margin: 15px 0 0;
}
.QRCodeAlert1  .inputDiv .item:first-child {
	margin-bottom: 10px;
}
.QRCodeAlert  .inputDiv label,
.QRCodeAlert1  .inputDiv label {
  	float: left;
}
.QRCodeAlert1  .inputDiv .ip {
	overflow: hidden;
}
.QRCodeAlert1  .inputDiv .ip input {
	width: 95%;
	height: 18px;
	padding: 0 10px;
	height:24px;
	margin: 0;
	float: right;
}
.QRCodeAlert1  .inputDiv .ip input::-webkit-input-placeholder {
	color: #999;
	font-size: 12px;
}
.QRCodeAlert1  .inputDiv .ip input::placeholder {
	color: #999;
	font-size: 12px;
}
.QRCodeAlert1  .inputDiv .ip input:-moz-input-placeholder {
	color: #999;
	font-size: 12px;
}
.QRCodeAlert1  .inputDiv .ip input:-ms-input-placeholder {
	color: #999;
	font-size: 12px;
}
.QRCodeAlert1  .inputDiv .ip input::-ms-input-placeholder {
	color: #999;
	font-size: 12px;
}
.QRCodeAlert1  .inputDiv .tips{
	color: #999;
	font-size: 12px;
	margin-top: 10px;
}
.QRCodeAlert1  .inputDiv .tips span{
	float: left;
}
.QRCodeAlert1  .inputDiv .tips div {
	overflow: hidden;
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
	z-index: 98;
	display: none;
}
</style>
</head>
<body data-info="${cardID}" id="bodyHtml" data-userId="${user.id}" data-vawalt="${online.relevawalt}">
	<input type="hidden" value='${datamap.code}' id="isIdInp">
	<input type="hidden" value='${datamap.message}' id="isIdmessage">
	<div class="icPage">
		<header>
			<div style="font-size: 14px;"><span>卡号：</span>${cardID}</div>
		</header>
		<div class="card-info">
			<ul>
				<li>
					<div class="left">
						<span class="title"><i class="iconfont icon-user-s"></i>持卡人：</span>
						<span>
							<c:choose>
								<c:when test="${onlineuser.username != null}">${onlineuser.username}</c:when>
								<c:otherwise>— —</c:otherwise>
							</c:choose>
						</span>
					</div>
					<div class="right">
						<span class="title"><i class="iconfont icon-tel-fill"></i>电话：</span>
						<span>
							<span class="spanNegative">
								<c:choose>
									<c:when test="${onlineuser.phoneNum != null}">${onlineuser.phoneNum}</c:when>
									<c:otherwise>— —</c:otherwise>
								</c:choose>
							</span>
						</span>
					</div>
					<div class="left">
						<span class="title"><i class="iconfont icon-bianhao"></i>卡状态：</span>
							<span class="spanNegative">${online.status==0 ? "未激活" : online.status==1 ? "正常" : online.status==2 ? "挂失" : online.status==4 ? "未激活" : "未激活"}</span>
						</span>
					</div>
					<div class="right">
						<span class="title"><i class="iconfont icon-xiaoquguanli"></i>所属小区：</span>
						<span class="spanJust">
							<c:choose>
								<c:when test="${areadata.name != null}">${areadata.name}</c:when>
								<c:otherwise>— —</c:otherwise>
							</c:choose>
						</span>
					</div>
					<div class="right">
						<span class="title"><i class="iconfont icon-jine"></i>充值余额：</span>
						<span class="spanJust">
							<a href="/general/scanonlinedemand?cardnum=${online.cardID}">
								<c:choose>
									<c:when test="${online.money!=null}"><fmt:formatNumber value="${online.money}" pattern="0.00"/></c:when>
									<c:otherwise>0.00</c:otherwise>
								</c:choose>元
								<%-- <c:if test="${online.relevawalt != 1}" >
									<c:choose>
										<c:when test="${online.money!=null}"><fmt:formatNumber value="${online.money}" pattern="0.00"/></c:when>
										<c:otherwise>0.00</c:otherwise>
									</c:choose>元
								</c:if>
								<c:if test="${online.relevawalt ==1}" >
									<c:choose>
										<c:when test="${online.money!=null}"><fmt:formatNumber value="${user.balance}" pattern="0.00"/></c:when>
										<c:otherwise>0.00</c:otherwise>
									</c:choose>元
								</c:if> --%>
							</a>
						</span>
					</div>
					<div class="left">
						<span class="title"><i class="iconfont icon-jine"></i>赠送余额：</span>
						<span>
							<a href="/general/scanonlinedemand?cardnum=${online.cardID}">
							<c:choose>
									<c:when test="${online.sendmoney!=null}"><fmt:formatNumber value="${online.sendmoney}" pattern="0.00"/></c:when>
									<c:otherwise>0.00</c:otherwise>
								</c:choose>元
							<%-- <c:if test="${online.relevawalt != 1}" >
								<c:choose>
									<c:when test="${online.sendmoney!=null}"><fmt:formatNumber value="${online.sendmoney}" pattern="0.00"/></c:when>
									<c:otherwise>0.00</c:otherwise>
								</c:choose>元
							</c:if>
							<c:if test="${online.relevawalt ==1}" >
								<c:choose>
									<c:when test="${online.sendmoney!=null}"><fmt:formatNumber value="${user.sendmoney}" pattern="0.00"/></c:when>
									<c:otherwise>0.00</c:otherwise>
								</c:choose>元
							</c:if> --%>
							</a>
						</span>
					</div>
				</li>
			</ul>
		</div>
		
		
		
		<section id="section1">
			<h3>请选择充值金额</h3>
			<div class="selectDiv">
				<ul>
					<c:if test="${datamap.code==200}">
					<c:forEach items="${datamap.sontemp}" var="sontemp">
						<li data-sontemId= "${sontemp.id}" data-money="${sontemp.money}">${sontemp.name}</li>
					</c:forEach>
					</c:if>
				</ul>
			</div>
			<div class="displayDiv"><span class="icon_yen">¥</span><span id="moneySpan">0</span><span class="textSpan">元</span></div>
			<div class="btn">确认充值</div>
		</section>
		<div id="tipDiv">
			<p>提示内容</p>
		</div>
		<div id="remake">
			<div class="left_right">
				<div class="right">
					<c:choose>
						<c:when test="${datamap.code==200}"><p>如有疑问，敬请联系：<a href="tel:${datamap.ptemphone!=null ? datamap.ptemphone : meruser.phoneNum}">${datamap.ptemphone!=null ? datamap.ptemphone : meruser.phoneNum}</a></p></c:when>
						<c:when test="${datamap.telphone!=null}"><p>如有疑问，敬请联系：<a href="tel:${datamap.telphone}">${datamap.telphone}</a></p></c:when>
						<c:otherwise><p></p></c:otherwise>
					</c:choose>
				</div>	
			</div>
		</div>
		<div class="copymark"></div>
		<div class="QRCodeAlert">
			<div class="popur">
				<div class="top">
					<h3>绑定IC卡</h3>
					<p>此卡是否为本人使用？</p>
				</div>
				<div class="bottom">
					<span class="closeBtn">否</span>
					<span class="sureBtn">是</span>
				</div>
			</div>
		</div>
		<div class="QRCodeAlert1">
			<div class="popur">
				<div class="top">
					<h3>绑定手机号</h3>
					<div class="inputDiv">
						<div>
							<div class="item">
								<label for="phone">绑定手机号</label>
								<div class="ip">
									<input type="number" name="phone" id="phone" value="" placeholder="请输入要绑定的手机号" />
								</div>
							</div>
							<div class="item">
								<label for="checkPhone">确认手机号</label>
								<div class="ip">
									<input type="number" name="checkPhone" id="checkPhone" value="" placeholder="确认手机号" />
								</div>
							</div>
						</div>
						<div class="tips">
							<span>注意：</span>
							<div>请确认输入持卡人的手机号，以便卡丢失快速找回并挂失！</div>
						</div>
					</div>
				</div>
				<div class="bottom">
					<span class="closeBtn">取消</span>
					<span class="sureBtn">绑定</span>
				</div>
			</div>
		</div>
	</div>
	<script>
		$(function(){
			var selectMoney= 0
			var sontemId
			var userId= $('#bodyHtml').attr('data-userId').trim();
			var cardnum = $('#bodyHtml').attr('data-info').trim();
			var vawalt = $('#bodyHtml').attr('data-vawalt').trim();
			var openid = "${user.openid}"
			$('.selectDiv li').click(function(){
				var Semoney= $(this).attr('data-money').trim()
				$('#moneySpan').text(Semoney)
				$(this).addClass('active')
				$(this).siblings().removeClass('active') 
			})
			var code = $('#isIdInp').val().trim();
			var message = $('#isIdmessage').val().trim();
			if(code==101){
				$('.copymark').fadeIn(300)
				$('.QRCodeAlert').fadeIn(300)
				
				$('.QRCodeAlert .closeBtn').click(function(){ //点击取消
					$('.QRCodeAlert').fadeOut(200)
					$('.QRCodeAlert1').fadeIn(200) //绑定手机号的弹框显示
				})
				
				$('.QRCodeAlert .sureBtn').click(function(){
					/* var relevawalt= $('.QRCodeAlert input[name="isContentWetall"]:checked').val().trim() */
					$('.copymark').fadeOut(200)
					$('.QRCodeAlert').fadeOut(200)
					mui.confirm('此卡是否与钱包公用?','提示',['是','否'],function(options){
						var relevawalt= 2
						if(options.index == 0){
							relevawalt= 1
						}
						$.ajax({
					        type: "POST",
					        dataType: "json",
					        url: "/general/bindingonline" ,
					        data: {uid:userId,cardNum:cardnum,relevawalt:relevawalt},
					        success: function (e){
					            if (e.code == 200) {
					            	mui.toast('绑定成功');
					            	window.location= '/general/onlinebingrefresh?card='+cardnum
					            }else {
					            	mui.toast(e.message);
					            	$('#section1').css('display',"none");
					            	$('#tipDiv').css('display',"flex");
					            	$('#tipDiv p').text(e.message);
					            };
					        },
					        error : function(){
					        	mui.toast('异常错误！');
					        }
						});
					})
				})
			}else if(code != 200 && code!=101){
				 mui.toast(message,{ duration:'1500', type:'div' })
				 $('#section1').css('display',"none")
				 $('#tipDiv').css('display',"flex")
				 $('#tipDiv p').text(message)
                 return false
			}

			$('.icPage .btn').click(function(){
				selectMoney= parseFloat($('#moneySpan').text())
				if(!selectMoney || selectMoney == "0"){
					mui.toast('请选择充值金额，且金额大于0',{ duration:'1500', type:'div' })
					return false
				}
				sontemId= $('.selectDiv ul li.active').attr('data-sontemId')
				if(vawalt==1){
					vawalletpay();
				}else{
					icpay();
				}
			})
			$('.QRCodeAlert1 .sureBtn').click(function(){
				var phone= $('.QRCodeAlert1 #phone').val().trim()
				var checkPhone= $('.QRCodeAlert1 #checkPhone').val().trim()
				if(!(/^1(3|4|5|6|7|8|9)\d{9}$/.test(phone))){
					mui.toast('请输入正确的手机号！')
					return false
				}
				if(phone != checkPhone ){
					mui.toast('两次输入的手机号不一致，请重新输入！')
					return false
				}
				$.ajax({
					url: '/general/bindingOnlineByPhone',
					type: 'post',
					data: {
						cardnum: cardnum,
						phone: phone
					},
					success: function(res){
						if(res.code === 200){
							mui.toast('绑定成功！')
							$('#section1').css('display',"none")
			            	$('#tipDiv').css('display',"flex")
			            	$('#tipDiv p').text("已成功将此卡绑定到"+phone+'名下')
						}else {
							$('#section1').css('display',"none")
			            	$('#tipDiv').css('display',"flex")
			            	$('#tipDiv p').text('绑定到'+phone+'下失败')
							mui.toast(res.message)
						}
					},
					error: function(err){ 
						$('#section1').css('display',"none")
		            	$('#tipDiv').css('display',"flex")
		            	$('#tipDiv p').text('绑定失败')
						mui.toast('绑定到'+phone+'下出错')
					},
					complete: function(){
						$('.copymark').fadeOut(200)
						$('.QRCodeAlert1').fadeOut(200)
					}
				})
			})
			$('.QRCodeAlert1 .closeBtn').click(function(){
				$('.copymark').fadeOut(200)
				$('.QRCodeAlert1').fadeOut(200)
				$('#section1').css('display',"none")
            	$('#tipDiv').css('display',"flex")
            	$('#tipDiv p').text("请先绑定IC卡！")
			})
		
	var prepay_id;
	var paySign;
	var appId;
	var timeStamp;
	var nonceStr;
	var packageStr;
	var signType;
	function icpay() {
		$('.icPage .btn').attr("disabled",true);
		var url = '${hdpath}/general/icRecharge';
		$.ajax({
			type : "post",
			url : url,
			dataType : "json",
			data : {
				openId : openid,
				cardnum : cardnum,
				choosemoney : selectMoney,
				tempid : sontemId,
				source : 2
			},
			success : function(data) {
				if (data.cardifexist == "0") {
					mui.alert('输入卡号不存在，请重新输入', 'IC卡充值', function() {
					});
					return ;
				} else if (data.cardifexist == "3") {
					mui.alert('登陆已过期，请重新登陆', 'IC卡充值', function() {
					});
					return ;
				} else if (data.cardifexist == "2") {
					mui.alert('IC卡未激活，只可充值一次', 'IC卡充值', function() {
					});
					return ;
				} else if (data.cardifexist == "1") {
					appId = data.appId;
					paySign = data.paySign;
					timeStamp = data.date;
					nonceStr = data.nonceStr;
					packageStr = data.packagess;
					signType = data.signType;
					callpay();
				}
			}
		});
	}
	
	function vawalletpay() {
		var merid = "${user.merid}";
		if (merid == null || merid == "") {
			mui.alert('当前未绑定商家，钱包充值不开放', '钱包充值', function() {
			});
			return ;
		}
		if ( $("#choosemoney").text().trim() == "0") {
			mui.alert('请选择充值金额，当前未选择', '钱包充值', function() {
			});
			return ;
		}
		$("#wolfsubmit").attr("disabled",true);
		var url = '${hdpath}/general/walletRecharge';
		$.ajax({
			type : "post",
			url : url,
			dataType : "json",
			data : {
				openId : openid,
				choosemoney : selectMoney,
				tempid : sontemId,
				val : cardnum
			},
			success : function(data) {
				if (data.err == "0") {
					mui.alert('当前未绑定商家，请重新扫描设备二维码后再充值', '钱包充值', function() {
					});
				} else {
					appId = data.appId;
					paySign = data.paySign;
					timeStamp = data.date;
					nonceStr = data.nonceStr;
					packageStr = data.packagess;
					signType = data.signType;
					callpay();
				}
			}
		});
	}

	function onBridgeReady() {
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
				WeixinJSBridge.call('closeWindow');
				//window.location.replace("${hdpath}/general/payaccess");
			} else if (res.err_msg == "get_brand_wcpay_request:cancel") {
				mui.alert('支付取消', 'IC卡充值', function() {
				});
			} else if (res.err_msg == "get_brand_wcpay_request:fail") {
				mui.alert('支付失败', 'IC卡充值', function() {
				});
			} //使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
		});
	}
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