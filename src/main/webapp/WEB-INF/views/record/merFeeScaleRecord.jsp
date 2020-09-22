<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设备缴费记录</title>
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<link rel="stylesheet" href="${hdpath}/css/base.css">
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<script src="${hdpath}/mui/js/mui.min.js"></script>
<script type="text/javascript" src="${hdpath }/js/jquery-2.1.0.js"></script>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<style>
@font-face {font-family: "iconfont";
  src: url('//at.alicdn.com/t/font_1731523_0yo3b8nl9n4.eot?t=1585818972309'); /* IE9 */
  src: url('//at.alicdn.com/t/font_1731523_0yo3b8nl9n4.eot?t=1585818972309#iefix') format('embedded-opentype'), /* IE6-IE8 */
  url('data:application/x-font-woff2;charset=utf-8;base64,d09GMgABAAAAAAXYAAsAAAAAC5QAAAWKAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHEIGVgCDXgqJYId2ATYCJAMcCxAABCAFhG0HZBvSCVGULk6V7Gdh7LwII25UbuS5sTlN0QRpv/2wYl/yP3NW70sytGQ7ThMkQwDHWXB6CMipPQWGCJNFHiC+LWD+dy5zY7T/+WM5ILLFn1z62qRJOlJjY2bE9nxnT84DPo9CHQ/1N3rb/gk88yzxKGsm3popCoL41A/k5HR4khR0ETratoH0/j/3e/VHYSTmtW/fJatXRcN3Rgk0oOgi2h8oTiM8m+uER6nMvB0IIIVkZcZua3ukhaC0hKZlNHNIS85GtFCIadRrTm2gBxqzi7GfACwJ/57+aBQmwGgJdaf35nmfTP3Ys9HczxT6pQ9/ex4Ag7MADqiDQABdtT4fPM7VUU/OthY8BA4To29ruOpZrWd1nnV/NjqTIT9WIYmjs4BGrP2fh3CgHqKzgO+V1+EZs0LQ4JkSgoBntbRo09eBjNV3hwUE/Wjo8SxgjV4HUITQ/bTIlanJvrYQNIwgRlpQu9ocdKEptp9jNXVsKa2gZQ2+tuX+PfkimBkVX+mvUiqxJrp6uL8qMBNFsfhKzPV1KNkY73vYsJWL5QUj1wzlv2Lu1gvSOn0pxLdcbm6fulg2SZ68UMo2XWxqn7kc5tFL0o5PadsuKtRm/Zw2MTyJywR/s7H2/nz51sp9PXmihL+3irV7P5E8mS7VN33cNHDm07Cx+ZN+/l7pxOt+Hz2gAol9IH9vrrM6r2wjFwRWReZeGe/8JGeZ0fa7+7bYIeGv3RnfvX6znbv3Rw9UVyTj4dFKla/ecuBMXqTnmsQ+P9dT/lX2Sonxa/fG9xfThgOKdh0qGl7WuV0/PZjYNzf6STP3ZBosOMv/vGnwzJdE7tgr8MiI3VPqXfHV+bRmTWIVoDykv3LAgURmSeWEhNsUyMx3hMPihrjua673JakEYvRMVTXM/cH9pk9ZeDVtWdVLCDZ1YK3PPOgud5cVKkTc865XterIQ3UYbXX+ymi/XM++WNW50PdojnehL3j9xakf7Ptgyj2vywP7va9CdgOvMFSsOjb3G+sPuji207V++bIkLgt0qd9ZP2IvUy/yhUrf8+qdmmy93Xp+61tr8vyqnb3V0eG3986/d6eg9WpPimwP7lk3HVamPT4LdAnkiEHHiItu56+cB53dvPFCm6KunUCOuyDy8ho89YW+BKr0FtmlqbRmqsPU6a6vxxWHVafj1PxLdsxgWiuVN+Ds7C5nfcO//Zo//6faT9XuLVhgV379lG1SHhpTdVLaYy2dn/XeSW190N3v2e6dh/ne7Z79uo8+rFLr0GmqVuXEm3afzaz6L5v/V631jf+3UXXxOBulxb7EU7mXr152ax4w1pmvCv7Omjmz5XDBkbXVb2ihUCMy10PhJw3ucOP1YLNQqHNF1yRPpXhSs1PpJBP7MnfrFBYgADI7z2SvInPyv3JWyC1+gPvcl7/h47gP/yMneA7+H3N6BkBQ8Onayepu85upfjPLbh34pZ/0ZEqAF/Al14OW/xvk5BwxVyq70aaTWxnJN/TRlGEAqRcB/4FQto/tGfLoRoCY8lJgDGXAmaqgBb4OaJQmoDO1B6m2xbOVHFo9alkAarlxgLjugXF8CpzrES3w76DJ9wM6NzhI5ZFzSaVajJadp6CToA/uKXBPCmqcSVvUvIPsMHR0VhlyDlAbnoRSvlgsLqJAvY0jzBErJwkFqmUEC87dMAwlxFoG6CX5SpLEI4UCrfuhvCcj0qYzDTkSyAdcpwDnkQTqL51oKw1/B2IOhRy6ZcKZ6AGkGXzjoCSvSKAXdYKa8Fg6jSNMWSIeCuygJkXAgnhQqHMSiOt3CiBPIq+yRiY2oiALUVaX378hepzXAUjq8kYw4iRII50MMmGso3jAHdFkpMHRzcNGC+Y7omXY2GMr4AILFY4QjmxhSF7FywgBAAA=') format('woff2'),
  url('//at.alicdn.com/t/font_1731523_0yo3b8nl9n4.woff?t=1585818972309') format('woff'),
  url('//at.alicdn.com/t/font_1731523_0yo3b8nl9n4.ttf?t=1585818972309') format('truetype'), /* chrome, firefox, opera, Safari, Android, iOS 4.2+ */
  url('//at.alicdn.com/t/font_1731523_0yo3b8nl9n4.svg?t=1585818972309#iconfont') format('svg'); /* iOS 4.1- */
}

.iconfont {
  font-family: "iconfont" !important;
  font-size: 16px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.icon-shijian:before {
  content: "\e647";
}

.icon-anjian:before {
  content: "\e62a";
}

.icon-dingdan:before {
  content: "\e669";
}

.icon-dkw_jine:before {
  content: "\e60b";
}

.icon-diannao:before {
  content: "\e602";
}

.icon-shichang:before {
  content: "\e62c";
}

		.app {
			font-size: 13px;
			color: #666;
			padding-top: 55px;
			padding-bottom: 30px;
		}
		li {
			padding: 15px 10px;
			background: #f5f7fa;
			margin: 0 10px 15px 10px;
			/*margin-bottom: 15px;*/
			border: 1px solid #98E0AD;
			border-radius: 5px;
			/*border-bottom: 1px solid #98E0AD;*/
		}
		li i {
			color: #22B14C;
			margin-right: 5px;
			font-size: 14px !important;
		}
		li>div {
			min-width: 50%;
			float: left;
			padding: 3px 5px;
			box-sizing: border-box;

		}
		li>div>span:nth-child(1) {
			float: left;
			color: #333;
		}
		li>div>span:nth-child(2) {
			overflow: hidden;
			display: block;
			color: #777;
		}
		.noTip {
			margin-top: 30vh;
			text-align: center;
			font-size: 14px;
			color: #999;
		}
	</style>
</head>
<body>
	<header class="mui-bar mui-bar-nav">
	    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
	    <h1 class="mui-title">缴费记录</h1>
	</header>
	<div class="app">
		<ul>
			<c:if test="${fn:length(listMap) <= 0}">
				<div class="noTip">
					暂无缴费记录
				</div>
			</c:if>
			<c:forEach items="${ listMap }" var="item">
				<li class="clearfix">
					<div><span><i class="iconfont icon-dingdan"></i>订单号：</span><span>${item.ordernum}</span></div>
					<div><span><i class="iconfont icon-dkw_jine"></i>缴费金额：</span><span>${item.money}元</span></div>
					<div><span><i class="iconfont icon-anjian"></i>缴费方式：</span>
					<span>${item.paytype == 4 ? '微信支付' : '钱包支付'}</span>
					</div>
					<div><span><i class="iconfont icon-diannao"></i>缴费设备：</span><span>${item.code}</span></div>
					<div><span><i class="iconfont icon-shichang"></i>续费时长：</span><span>${item.renewal}个月</span></div>
					<div><span><i class="iconfont icon-shijian"></i>缴费时间：</span><span>${item.createtime}</span></div>
				</li>
			</c:forEach>
		</ul>
	</div>
	
</body>
</html>