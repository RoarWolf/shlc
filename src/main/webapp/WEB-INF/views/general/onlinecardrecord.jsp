<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<meta charset="UTF-8">
	<title>IC卡消费信息</title>
	<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
	<style>
@font-face {font-family: "iconfont";
  src: url('//at.alicdn.com/t/font_1221168_a2sbmjqs6uf.eot?t=1559285176791'); /* IE9 */
  src: url('//at.alicdn.com/t/font_1221168_a2sbmjqs6uf.eot?t=1559285176791#iefix') format('embedded-opentype'), /* IE6-IE8 */
  url('data:application/x-font-woff2;charset=utf-8;base64,d09GMgABAAAAAAWIAAsAAAAACoAAAAU5AAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHEIGVgCDXgqHQIYxATYCJAMcCxAABCAFhG0HcBsZCRGVpAuQ/Rxw26OnvuTLqV9/TmxhZyauG1XUSUoyM0PQf43pvuP8CxSIZMaXxgM6Wc1G16gaw7IsVHUOctODUAupmDrZHKrqL3t3aq85PFS3f3t7GKchRRaHCdVE/hdMpID7v7V2qCdIg8UzrVhVndv7szvrb/bvWUMTTUM6iEQInUc7riGe8OnvVc1YOID2IorZ+1l7cS6UC1AuNM9zARq7c9gCKmgLUq6oVgF2toQ/gQ9DAAYzoAIEGzsXHygQQNULbSmJ8ZFQIg4IBQRBMaeMC0mcqsEAhfi8XADcOz+vj2gRmAEHEQH1UQdxtjGw/Ybzo4hQfgqEIAsedOcD2MvAA2wBEAA9yQxcAuIVtgABk/8mCU4A2lDAQXrT/x56t9/gb75/FNHpRDvVQUFb+wcPAiTwEMGBADECDE0KfVFMwC+nwUMAfrUUWcYM2PAw4rARYfwOHhzwV6Q6QkO2LmgD9AFIBVAz1bTlGNwh4wP498IEBll1x8VYOPowLnENcBSXX/ncvPa9e+t/+yZkRMnvBB+n8frWUKRRIPo3vyWFx0cjNz5+c9c8p3N2B9e8J7ca5Vtf9PrJ5+bG/+bO//Y2gNZIaUBRMEnUJgIBTePNZDIFdWbjb2agbnCtraaWav6afVfP4NeGztc60ntjy378Kl2pFKNtF2qwbtQVAZR6wBZewdh5yrIP3+JM+BVB2+zZoG7h24kNDTxY01Cb8ZWftXHhJTTMKEaN4smsNGNxp4oBEoEotS2X20cB6nJ+Z4SneHOAKhd1hsv701GacvTcIMUvITtIkz8afU6TjSxRxBCLcxIbG+lcAxZkaoIbzb7Fl5sbzwWkBFoqKhfZOKc42RZdXUtU+zztZtDSdlUTatTn8L/PNSzjetjAYzk05N/ob9RmxfCp93XfaOkl0lBmv/Y9ggYs4FmzIXH78FDs8/hXa1kL65exr7aWCfsy/tn6sH+I8Utbo0NSCrgxx5fTH2jArvkZ0o/SowuhqkddyuCgwlVoYwt8ick1NcmmlsgAROE0HM9YeiB6fE2q7Lynv298h1bcB+GvAUbz7r0JGcKTDoNSzPYSthd7oYhdB+q3H9/WQEdirVlaSArh7HJpcxHzyP97Wmf+Y0KVPtPaQ0ffVTq5taFjgoau9bcU5hCOB+T+AxnJUcsxigCY2hG8UPTymsnr4TR6PVj1nXt+gVVrUmvHY7pTqv3GojpeA1Fhs1aIrvY4zAA31SzcN5FKRIUbxGmHbVgTdlxrZExQlaXWW40ZANAr8wuJO2QKMp4U8gPkJ7JA/u5rkqM0inz25m/vNq6Ovv7dYDf5L+QPeuSd7+uKjPfKFKqnmeIVNFJZ+a4ma9bj+UD7UJ25ZnUYPWOgqBjMTiVFd3OMgTc0MuAe7mFdl6QYDuhONk33iiV4QG/Ro6qbTIqltoLZNIHLq0FCDdUGsGGPoEufC7rT9kj3+tzAA/qOHo34pCd9UUM3Z9Fzw2olePOIAIqQIekC4ZJlMt2NzW77NMiuTkVY7ZDmQTg8TYK+QLw8ASaILk44WTmEyAgTlkHGiceBrlvEFpYKEvoURDvl97OsT/JJluFptkcAFAIZRFoBwUksJmYNR82J758GZC4dJQq6ytLzgODgixNBPoEK8AmDWanrVFY7smQhCDFqFyNYDMQ4fEDXChbCzt6kAhLkozTI2VL8ZD9W1eAb3mSc5xnAqK/I4eQVFJWUVSgD5QWXmqGWQ8g5rmHjoZzoLFimrLhe5NSUlEBfe0S5BkfhasLXVFRK18lIuccDAAAA') format('woff2'),
  url('//at.alicdn.com/t/font_1221168_a2sbmjqs6uf.woff?t=1559285176791') format('woff'),
  url('//at.alicdn.com/t/font_1221168_a2sbmjqs6uf.ttf?t=1559285176791') format('truetype'), /* chrome, firefox, opera, Safari, Android, iOS 4.2+ */
  url('//at.alicdn.com/t/font_1221168_a2sbmjqs6uf.svg?t=1559285176791#iconfont') format('svg'); /* iOS 4.1- */
}

.iconfont {
  font-family: "iconfont" !important;
  font-size: 16px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.icon-laiyuan2:before {
  content: "\e77a";
}

.icon-consumption:before {
  content: "\e6f1";
}

.icon-yonghu-tianchong:before {
  content: "\e670";
}

.icon-icon:before {
  content: "\e611";
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
		li {
			list-style: none;
		}
		body {
			background-color: #efeff4;
		}
		header h3 {
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
			background-color: #f5f7fa;
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
		.bottomTip {
			text-align: center;
			color: #666;
			font-size: 14px;
			padding: 10px 0;
		}
	</style>
</head>
<body>
	<div class="userDetail">
		<header>
			<h3>IC卡&nbsp;&nbsp;${cardID}消费</h3>
		</header>
		<ul>
			<c:choose>
			  <c:when test="${size==0}">
			  		<div class="noData">暂无数据</div>
			  </c:when>
			  <c:otherwise>
				<c:forEach items="${iccardrecord}" var="consume">
					<c:if test="${relevawalt==2}">
					<li>
						<div class="left">
							<span class="title"><i class="iconfont icon-icon" style="font-size: 14px;"></i>订单号：</span>
							<span>${consume.ordernum}</span>
						</div>
						<div class="right">
							<span class="title"><i class="iconfont icon-zhuangtai"></i>类型：</span>
							<span>
							  <c:choose>
								<c:when test="${consume.type == 1||consume.type == 5||consume.type == 7||consume.type == 9}"><span class="spanNegative">${consume.type == 1 ? "消费" : consume.type == 5 ? "微信退费": consume.type == 7 ? "支付宝退费": consume.type == 9 ? "虚拟退款" : "— —"}</span></c:when>
								<c:when test="${consume.type == 2||consume.type == 3||consume.type == 6||consume.type == 8}"><span class="spanJust">${consume.type == 2 ? "余额回收" : consume.type == 3 ? "微信充值": consume.type == 6 ? "支付宝充值": consume.type == 8 ? "虚拟充值" : "— —"}</span></c:when>
								<c:otherwise><font color='#ada98b'>卡操作</font></c:otherwise>
							  </c:choose>
							</span>
						</div>
						<c:if test="${consume.type==3 || consume.type==5 || consume.type==6 || consume.type==7 || consume.type==8 || consume.type==9}">
						<div class="right">
							<c:if test="${consume.type==3 || consume.type==6  || consume.type==8 }">
							<span class="title"><i class="iconfont icon-consumption"></i>付款金额：</span>
							<span class="spanJust">+<fmt:formatNumber value="${consume.money}" pattern="0.00"/></span>
							</c:if>
							<c:if test="${consume.type==5 || consume.type==7 || consume.type==9}">
							<span class="title"><i class="iconfont icon-consumption"></i>退款金额：</span>
							<span class="spanNegative">-<fmt:formatNumber value="${consume.money}" pattern="0.00"/></span>
							</c:if>
							<strong>元</strong>
						</div>
						</c:if>
						<div class="left">
							<span class="title"><i class="iconfont icon-consumption"></i>到账金额
							<%-- ${consume.type==1 ? "消费金额" : consume.type==2? "回收金额" : consume.type==3 ? "充卡金额" : consume.type==4 ? "卡操作" : consume.type==5 ? "扣卡金额" : consume.type==6 ? "充卡金额" : consume.type==7 ? "扣卡金额" : consume.type==8 ? "虚拟充值金额" : consume.type==9 ? "虚拟退款金额":"--"}：</span> --%>
							<c:choose>
								<c:when test="${consume.type == 1||consume.type == 5||consume.type == 7||consume.type == 9}"><span class="spanNegative">-<fmt:formatNumber value="${consume.accountmoney}" pattern="0.00"/></span></c:when>
								<c:when test="${consume.type == 2||consume.type == 3||consume.type == 6||consume.type == 8}"><span class="spanJust">+<fmt:formatNumber value="${consume.accountmoney}" pattern="0.00"/></span></c:when>
								<c:otherwise><font color='#ada98b'>  ${order.accountmoney} </font></c:otherwise>
							</c:choose>
							<strong>元</strong>
						</div>
						<div class="right">
							<span class="title"><i class="iconfont icon-consumption"></i>充值余额：</span>
							<span class="spanJust"><fmt:formatNumber value="${consume.topupbalance}" pattern="0.00"/></span>
							<strong>元</strong>
						</div>
						<div class="right">
							<span class="title"><i class="iconfont icon-consumption"></i>赠送余额：</span>
							<span class="spanJust"><fmt:formatNumber value="${consume.sendbalance}" pattern="0.00"/></span>
							<strong>元</strong>
						</div>
						<div class="left">
							<span class="title"><i class="iconfont icon-consumption" ></i>余额：</span>
							<span><fmt:formatNumber value="${consume.accountbalance}" pattern="0.00"/></span>
							<strong>元</strong>
						</div>
						<div class="right">
							<span class="title"><i class="iconfont icon-shijian" style="font-size: 17px;"></i>时间：</span>
							<span><fmt:formatDate value="${consume.create_time}" pattern="yyyy-MM-dd HH:mm:ss" /></span>
						</div>
					</li>
					</c:if>
					<c:if test="${relevawalt==1}">
					<li>
						<div class="left">
							<span class="title"><i class="iconfont icon-icon" style="font-size: 14px;"></i>订单号：</span>
							<span>${consume.ordernum}</span>
						</div>
						<div class="right">
							<span class="title"><i class="iconfont icon-zhuangtai"></i>类型：</span>
							<span>
							  <c:choose>
								<c:when test="${consume.paysource == 1}"><span class="spanJust">充值钱包</span></c:when>
								<c:when test="${consume.paysource == 2||consume.paysource == 3||consume.paysource == 4}"><span class="spanNegative">钱包消费</span></c:when>
								<c:when test="${consume.paysource == 5}"><span class="spanJust">退费到钱包</span></c:when>
								<c:when test="${consume.paysource == 6}"><span class="spanNegative">钱包退款</span></c:when>
								<c:when test="${consume.paysource == 7}"><span class="spanJust">虚拟充值</span></c:when>
								<c:when test="${consume.paysource == 8}"><span class="spanNegative">虚拟退款</span></c:when>
								<c:when test="${consume.paysource == 9}"><span class="spanNegative">在线卡记录</span></c:when>
								<c:otherwise><span class="spanJust">"--"</span></c:otherwise>
							  </c:choose>
							</span>
						</div>
						<div class="right">
							<span class="title"><i class="iconfont icon-consumption"></i>
							${consume.paysource==1 ? "充值" : consume.paysource==2? "充电" : consume.paysource==3 ? "投币" : consume.paysource==4 ? "离线卡充值" : consume.paysource==5 ? "退费到钱包" : 
							consume.paysource==6 ? "钱包退款" : consume.paysource==7 ? "虚拟充值" : consume.paysource==8 ? "虚拟退款": consume.paysource==9 ? "在线卡" : "--"}：
							</span>
							<c:choose>
								<c:when test="${consume.paysource == 1 || consume.paysource == 5 || consume.paysource == 7}"><span class="spanJust">+<fmt:formatNumber value="${consume.money}" pattern="0.00"/></span></c:when>
								<c:when test="${consume.paysource == 2 || consume.paysource == 3||consume.paysource == 4||consume.paysource == 6 || consume.paysource == 8}"><span class="spanNegative">-<fmt:formatNumber value="${consume.money}" pattern="0.00"/></span></c:when>
								<c:otherwise><font color='#ada98b'> ${consume.money} </font></c:otherwise>
							</c:choose>
							<strong>元</strong>
						</div>
						<!-- 这里添加充值金额 -->
						<div class="right">
							<span class="title"><i class="iconfont icon-consumption"></i>充值余额：</span>
							<span class="spanJust"><fmt:formatNumber value="${consume.topupbalance}" pattern="0.00"/></span>
							<strong>元</strong>
						</div>
						<div class="right">
							<span class="title"><i class="iconfont icon-consumption"></i>赠送余额：</span>
							<span class="spanJust"><fmt:formatNumber value="${consume.sendbalance}" pattern="0.00"/></span>
							<strong>元</strong>
						</div>
						<div class="left">
							<span class="title"><i class="iconfont icon-consumption" ></i>余额：</span>
							<span><fmt:formatNumber value="${consume.accountbalance}" pattern="0.00"/></span>
							<strong>元</strong>
						</div>
						<c:if test="${consume.remark != null}">
						  <div class="left">
							<span class="title"><i class="iconfont icon-zhuangtai" ></i>备注：</span>
							<span>${consume.remark}</span>
						  </div>
						</c:if>
						<div class="right">
							<span class="title"><i class="iconfont icon-shijian" style="font-size: 17px;"></i>时间：</span>
							<span><fmt:formatDate value="${consume.create_time}" pattern="yyyy-MM-dd HH:mm:ss" /></span>
						</div>
					</li>
					</c:if>
				</c:forEach>
			  </c:otherwise>
			</c:choose>
		</ul>
	</div>
	<div class="bottomTip">仅支持查看最近6个月的记录</div>
</body>
</html>