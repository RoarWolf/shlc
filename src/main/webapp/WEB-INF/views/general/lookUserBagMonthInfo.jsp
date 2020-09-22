<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"/>
<title>包月支付信息</title>
<link rel="stylesheet" href="${hdpath }/css/base.css">
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<script type="text/javascript" src="${hdpath }/js/jquery-2.1.0.js"></script>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<style>
 @font-face {font-family: "iconfont";
  src: url('//at.alicdn.com/t/font_1304153_bo1ydadfrto.eot?t=1563517000184'); /* IE9 */
  src: url('//at.alicdn.com/t/font_1304153_bo1ydadfrto.eot?t=1563517000184#iefix') format('embedded-opentype'), /* IE6-IE8 */
  url('data:application/x-font-woff2;charset=utf-8;base64,d09GMgABAAAAAAMcAAsAAAAABsgAAALNAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHEIGVgCCcAqBTIIKATYCJAMICwYABCAFhG0HOBs4BhHVk71kXxzYxsoDfwMJ0oWLCMdtcjA4ps99jof/3+/bPu/e/+eOGOoJfmLRRT1NwqOYlTqwunhjEsmjWKTyBk7XfX/nd+4ByeP0kQcacIDlkInDtmm9lxgVobpkz7f+l2ckQFe4kP4IgD/unf4J9s9b77zLce3Nn7oA40AKdKxBCbdBAvOGsZsIHMJmAh0maYlFeL1PKzLQKxD7L6cNreT0cktpKEKzZmIWdzSU6mr1Drf59+O32VGoNBJQsW4A29PmfRuEGelMfcYuihDMZ4U1jYQWMrGp1r5GkvGWpCOIZ9PYV2nwbbCuKX+3fzyCaALFXWCCyr8nARbeKncFMs1Zhcuj2pDGTxp+XID2AT4gRviQEqY3E31iKGgeuBi97w/PjNj0UI24NmrL65Y7bOTk9yNvG2e6YbSKZYbmBWcjXXon5Va7MveunfvkzCz9FEbFx7oFfaOcBidwo1uYO+ZZ5pG5cuJtFaFmZKZdM9byhXuU79SRUXRlWLOXkpmzm0OriaFpyV0Ll/Z5pkcSg+TCivI+TwaG/4fH79CifSD/ulyflL/hRJsH6mfpZfLLf+P/28df9/aW/pXODD5sPUtyb/0s6+ek3Jgj+MGCnlU5FAFDlX1BbbqySrpxaQcMUQPuYb6OVY98m3QoRr6Ts83EFJJiBprJFjR0mgNNxVzoMMvS6Z2GOyqJ3I6ZLioQ+jyCSo/nkPQZQjP5BRpG+QtNfZGgw5ZQLtiJPLIhaBUDuD9kzychkxpResd47VayIs54o9QcgqHti6kFT5Q5htRPHFUJSPiAWdkO952hCK/otU2qZeo6qrtR6/loDIJWMQD3B9nzSbS+mtLn7xiv3UoLbcD7Rqm5dTBo9T3QRX72ot3LNfUTR6oESPgAs2LFHcYZlPp2K3ptpRHRMum0WtRX3q6vPL5uA3QAmirgoKRjKdPlE54xMtY1DQAAAA==') format('woff2'),
  url('//at.alicdn.com/t/font_1304153_bo1ydadfrto.woff?t=1563517000184') format('woff'),
  url('//at.alicdn.com/t/font_1304153_bo1ydadfrto.ttf?t=1563517000184') format('truetype'), /* chrome, firefox, opera, Safari, Android, iOS 4.2+ */
  url('//at.alicdn.com/t/font_1304153_bo1ydadfrto.svg?t=1563517000184#iconfont') format('svg'); /* iOS 4.1- */
}

.iconfont {
  font-family: "iconfont" !important;
  font-size: 16px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.icon-zhifuchenggong:before {
  content: "\e6ae";
}

.monthlyPay{
    width: 100%;
    height: 100%;
    color: #666;
    font-size: 14px;    
}
.monthlyPay .iconDiv{
    display: flex;
    justify-content: center;  
}
.monthlyPay .iconDiv i {
    margin-top: 13vh;
    font-size: 27vw;
    font-weight: 700;
    color: #44AF3D;
}
.monthlyPay .payText{
    margin-top: 50px;
    font-size: 9vw;
    text-align: center;
    line-height: 30px;
}
.monthlyPay .payInfo{
    margin: 10% 15px 0;
    background-color: #fff;
    border-radius: 5px;
    padding: 15px 10px;
    border: 1px solid #ccc;
}
.monthlyPay .payInfo li {
    line-height: 35px;
}
.monthlyPay .payInfo span {
    float: right;
}
.monthlyPay .payInfo h3 {
    font-size: 16px;
    color: #999;
    margin-bottom: 10px;
}
.btnDiv {
    margin-top: 5vh;
    text-align: center;
}
.btnDiv button {
    width: calc(100% - 30px);
    line-height: 30px;
    background-color: #4dc145;
}

</style>
</head>
<body>
	<div class="monthlyPay">
        <div class="iconDiv">
            <i class="iconfont icon-zhifuchenggong"></i>
        </div>
        <div class="payText">支付成功</div>
        <ul class="payInfo">
         <h3>包月信息</h3>
         <li>剩余总次数：<span>${packageMonth.everymonthnum == 0 ? '--' : packageMonth.surpnum}次</span></li>
         <li>今日剩余次数：<span>${packageMonth.everydaynum == 0 ? '--' :  packageMonth.todaysurpnum }次</span></li>
         <li>包月到期时间：<span><fmt:formatDate value="${packageMonth.endTime}" pattern="yyyy-MM-dd HH:mm:ss" /></span></li>
        </ul>
        <div class="btnDiv">
            <button type="button" class="mui-btn mui-btn-success" id="wolfaccess">完成</button>
        </div>
    </div>  
</body>
<script type="text/javascript">
$(function() {
	pushHistory();
    window.addEventListener("popstate", function(e) {
    	WeixinJSBridge.call('closeWindow');
	}, false);
    function pushHistory() {
        var state = {
            title: "title",
            url: "#"
        };
        window.history.pushState(state, "title", "#");
    }
	$("#wolfaccess").click(function() {
		WeixinJSBridge.call('closeWindow');
	})
})
</script>
</html>