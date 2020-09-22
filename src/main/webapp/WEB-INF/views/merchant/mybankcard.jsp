<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport"
	content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!--App自定义的css-->
<%-- <link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/app.css" /> --%>
<title>我的银行卡</title>
<%@ include file="/WEB-INF/views/public/commons.jspf" %>
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<link rel="stylesheet" href="${hdpath}/css/base.css"/>
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/app.css" />
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="${hdpath }/mui/js/mui.min.js"></script> 
<script src="${hdpath}/js/jquery.js"></script>
<style type="text/css">
/* .mui-table-view-cell>a:not(.mui-btn) {
	margin: -3px 15px;
} */
.bankCardCon {
			padding: 10px;
		}
		.bankCardCon .bankCard {
			background-color: #C8EFD4;
			font-size: 14px;
			padding: 15px 20px;
			margin-bottom: 10px;
			border-radius: 4px;	
		}
		.bankCardCon .bankCard>a {
			display: block;
			text-decoration: none;
		}
		.bankCardCon .bankCard p {
			color: #fff;
		}
		.bankCardCon .bankCard .bankName {
			font-weight: bold;
		}
		/*.bankCardCon .bankCard .bankName {
			font-weight: bold;
		}*/
		.bankCardCon .bankCard .bankCardNum {
			font-size: 25px;
			vertical-align: middle;
			line-height: 1.5em;
		}
		.addBankCardCon .addBankCard {
			padding: 0 10px 0 20px;
			background-color: rgba(0,0,0,.05);
			font-size: 14px;
			margin-bottom: 10px;
			height: 45px;
			line-height: 45px;
		}
		.addBankCardCon .addBankCard a {
			display: block;
			text-decoration: none;
			display: flex;
			align-items: center;
			justify-content: space-between;
		}
		.addBankCardCon .addBankCard p {
			display: flex;
			align-items: center;
			color: #666;
		}
		.addBankCardCon .addBankCard .mui-icon-plusempty {
			float: left;
			font-size: 30px;
		}
		.addBankCardCon .addBankCard .mui-icon-arrowright {
			float: right;
			color: #666;
			font-size: 22px;
		}
</style>
</head>
<body>
<%-- <div class="mui-content">
		<ul class="mui-table-view">
			<li id="lili" style="height: 60px" class="mui-table-view-cell"><a href="${hdpath }/merchant/addbankcard" class="mui-navigate-right">
					+添加银行卡</a>
			</li>
			<c:if test="${havCompany == 1 }">
				<li style="height: 60px" class="mui-table-view-cell"><a onclick="mui.alert('对公账户只可添加一个');" class="mui-navigate-right">
						+添加对公账户</a>
				</li>
			</c:if>
			<c:if test="${havCompany == 0 }">
				<li style="height: 60px" class="mui-table-view-cell"><a href="${hdpath }/merchant/addCompanyCard" class="mui-navigate-right">
						+添加对公账户</a>
				</li>
			</c:if>
			</li>
		</ul>
		<br>
		<br>
		<c:forEach items="${userbankcardlist }" var="userbc">
		<ul style="height: 60px" class="mui-table-view">
			<li class="mui-table-view-cell"><a href="${hdpath }/merchant/mybankcardinfo?id=${userbc.id}" class="mui-navigate-right">
					${userbc.bankcardnum }&nbsp;&nbsp;&nbsp;${userbc.bankname}
					&nbsp;&nbsp;&nbsp;<c:if test="${userbc.type == 2 }"><font color="#44b549">对公账户</font></c:if></a>
			</li>
		</ul>
		</c:forEach>
		<br/>
	</div> --%>
	<div class="app">
		<div class="bankCardCon">
		<c:forEach items="${userbankcardlist }" var="userbc">
			<div class="bankCard">
				<a href="${hdpath }/merchant/mybankcardinfo?id=${userbc.id}">
					<p class="bankName">
					${userbc.bankname}
					<c:if test="${userbc.type == 2 }"><span class="otCard">（对公账户）</span></c:if>
					</p>
					<br>
					<!-- <p class="bankType">储蓄卡</p> -->
					<p class="bankCardNum">**** **** **** ${userbc.bankcardnum }</p>
				</a>
			</div>
			<!-- <div class="bankCard">
				<a>
					<p class="bankName">中国银行 <span class="otCard">（对公账户）</span></p>
					<br>
					<p class="bankType">储蓄卡</p>
					<p class="bankCardNum">**** **** **** 3027</p>
				</a>
			</div>	 -->
		</c:forEach>		
		</div>
		<div class="addBankCardCon">
			<div class="addBankCard">
				<a href="${hdpath }/merchant/addbankcard">
					<p>
						<span class="mui-icon mui-icon-plusempty"></span>
						添加银行卡
					</p>
					<span class="mui-icon mui-icon-arrowright"></span>
				</a>
			</div>
			<c:if test="${havCompany == 0 }">
				<div class="addBankCard">
					<a href="${hdpath }/merchant/addCompanyCard">
						<p>
							<span class="mui-icon mui-icon-plusempty"></span>
						添加对公账户
						</p>
						<span class="mui-icon mui-icon-arrowright"></span>
					</a>
				</div>
			</c:if>
			<c:if test="${havCompany == 1 }">
				<div class="addBankCard">
					<a href="javascript:void(0);" class="noGoTo">
						<p>
							<span class="mui-icon mui-icon-plusempty"></span>
						添加对公账户
						</p>
						<span class="mui-icon mui-icon-arrowright"></span>
					</a>
				</div>
			</c:if>
			
		</div>
	</div>
	
<script type="text/javascript">
	$(function(){
	    pushHistory();
	    window.addEventListener("popstate", function(e) {
	    	window.location.href = "${hdpath}/merchant/personcenter";
		}, false);
	    function pushHistory() {
	        var state = {
	            title: "title",
	            url: "#"
	        };
	        window.history.pushState(state, "title", "#");
	    }
	    $('.noGoTo').click(function(){
	    	mui.toast('对公账户只可添加一个')
	    })
	});
</script>
</body>
</html>