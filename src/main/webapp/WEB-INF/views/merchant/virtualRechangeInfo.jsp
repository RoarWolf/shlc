<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<title>充值信息</title>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="${hdpath}/js/jquery.js"></script>
<style>
		* {
			padding: 0;
			margin: 0;
		}
		li {
			list-style: none;
		}
		body {
			background-color: #efefef;
			padding: 15px 10px;
		}
		.app .top h3 {
			text-align: center;
			color: #22B14C;
			font-size: 20px;
			margin-bottom: 20px;
		}
		.app .top .pay_i {
			text-align: center;
			font-size: 16px;
			margin-bottom: 20px;
		}

		.app .detail li {
			border: 0.0427rem solid #add9c0;
			overflow: hidden;
			padding:0.4268rem;
			border-radius: 0.2134rem;
			background-color: #f5f7fa;
			margin-bottom: 0.64rem;
			font-size: 0.597rem;
			color: #666;
		}
		.app li:active {
			background-color: #f5f5f373;
		}
		.app .detail .firstLi {
			/*display: flex;*/

		}
		.app .detail .firstLi>div{
			width: 50%;
			display: flex;
			flex-direction: column;
			justify-content: center;
		}
		.app .detail .firstLi>div p {

		}

		.app .detail li div{
			width: 100%;
			display: flex;
			justify-content: space-between;	
		}

		.app .detail li div span {
			line-height: 1.28rem;
			text-align: center;
		}
		.app .detail li div .title{
			color: #999;
		}
		.app .detail li div span i {
			color: #22B14C;
			font-size: 0.64rem;
			margin-right: 0.2134rem;
		}
		.app .detail .infoTop{
	      padding: 0.8536rem 0;
	      background-color: #fff;
	      border-top:0.0427rem solid #ccc;
	      border-bottom:0.0427rem solid #ccc;
	      margin-bottom: 0.8536rem;
	    }
	    .app .detail .left p,
	    .app .detail .right p{
	      text-align: center;
	       color: #666;
	       font-size: 0.597rem;
	       margin: 0;
	    }
	    .app .detail div.left p:last-child,
	    .app .detail div.right p:last-child{
	      font-size: 0.8rem;
	      color: #666;
	      line-height: 1.5em;
	    }
	   .app .detail  .left {
	    width: 50% !important;
	    float: left;
	   }
	   .money {
	   	 color: #22B14C;
	   	 font-weight: bold;
	   }
	   .back_con {
	   	text-align: center;
	   	margin-top: 2rem;
	   }
	   .back {
	   	width: 75%;
	   	height: 1.6rem;
	   	line-height: 1.6rem;
   	    border: 1px solid #4cd964;
		background-color: #4cd964;
	   	color: #fff;
	   	text-align: center;
	   	outline: none;
	   	border-radius: 0.2rem;
	   	transition: all 0.4s;
	    font-size: 0.65rem;
	   }
	   .back:active {
	   	/* border: 1px solid #4cd964; */
		background-color: #22B14C;
	   }
	</style>
</head>
<body>
	<div class="app">
		<div class="top">
			<h3>充值成功</h3>
			<div class="pay_i">
			充值金额： <span class="money">${result.paymoney}</span>元 
			 &nbsp;&nbsp;&nbsp;&nbsp;
			 赠送金额： <span class="money">${result.sendmoney}</span>元
			 </div>

		</div>
		<div class="detail">
			<c:if test="${result.type == 2}">
				<li >
					<div>
						<span class="title"><i class="iconfont icon-fukuanfangshisel"></i>IC卡号</span>
						<span>${result.cardID}</span>
					</div>
					<div>
						<span class="title"><i class="iconfont icon-icon" style="font-size: 0.5548rem;"></i>用户昵称</span>
						<span>
							<c:if test="${result.nick == null}">--</c:if>
							<c:if test="${result.nick != null}">${result.nick}</c:if>
						</span>
					</div>
	
					<div>
						<span class="title"><i class="iconfont icon-fukuanfangshisel"></i>IC卡余额</span>
						<span>${result.balance}元</span>
					</div>
	
					<div>
						<span class="title"><i class="iconfont icon-yonghuming"></i>用户姓名</span>
						<span>
							<c:if test="${result.realname == null}">--</c:if>
							<c:if test="${result.realname != null}">${result.realname}</c:if>
						</span>
					</div>
	
					<div>
						<span class="title"><i class="iconfont icon-ID"></i>充值方式</span>
						<span>
							<c:if test="${result.type == 2}">在线卡虚拟充值</c:if>
							<c:if test="${result.type == 1}">钱包虚拟充值</c:if>
						</span>
					</div>
					<div>
						<span class="title"><i class="iconfont icon-ID"></i>充值时间</span>
						<span>
							<fmt:formatDate value="${result.paytime}" pattern="yyyy-MM-dd HH:mm:ss" />
						</span>
					</div>
					
				</li>
			</c:if>	
			<c:if test="${result.type == 1}">
			
				<li>
					<div>
						<span class="title"><i class="iconfont icon-fukuanfangshisel"></i>会员号</span>
						<span><fmt:formatNumber value="${result.uid}" pattern="00000000"/></span>
					</div>
					<div>
						<span class="title"><i class="iconfont icon-icon" style="font-size: 0.5548rem;"></i>用户昵称</span>
						<span>
							<c:if test="${result.nick == null}">--</c:if>
							<c:if test="${result.nick != null}">${result.nick}</c:if>
						</span>
					</div>
	
					<div>
						<span class="title"><i class="iconfont icon-fukuanfangshisel"></i>钱包余额</span>
						<span>${result.balance}元</span>
					</div>
	
					<div>
						<span class="title"><i class="iconfont icon-yonghuming"></i>用户姓名</span>
						<span>
							<c:if test="${result.realname == null}">--</c:if>
							<c:if test="${result.realname != null}">${result.realname}</c:if>
						</span>
					</div>
	
					<div>
						<span class="title"><i class="iconfont icon-ID"></i>充值方式</span>
						<span>
							<c:if test="${result.type == 2}">在线卡虚拟充值</c:if>
							<c:if test="${result.type == 1}">钱包虚拟充值</c:if>
						</span>
					</div>
					<div>
						<span class="title"><i class="iconfont icon-ID"></i>充值时间</span>
						<span>
							<fmt:formatDate value="${result.paytime}" pattern="yyyy-MM-dd HH:mm:ss" />
						</span>
					</div>
				</li>
			</c:if>
		</div>
		<div class="back_con">
			<button class="back" data-type="${result.type}">
				<c:if test="${result.type == 1}">返回至会员管理</c:if>
				<c:if test="${result.type == 2}">返回至IC卡管理</c:if>
			</button>
		</div>
	</div>
	<script>
		var htmlwidth = document.documentElement.clientWidth || document.body.clientWidth;
	    var fontSize= htmlwidth/16
	    var style= document.createElement('style')
	    style.innerHTML= 'html { font-size: '+fontSize+'px !important;}'
	    var head= document.getElementsByTagName('head')[0]
	    head.insertBefore(style,head.children[0])
	    
	    $('.back').click(function(){
	    	window.history.go(-2)
	    })
	</script>
</body>
</html>