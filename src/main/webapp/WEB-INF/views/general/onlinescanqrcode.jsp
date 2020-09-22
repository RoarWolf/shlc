<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="hdpath" value="${pageContext.request.contextPath }" />
<!DOCTYPE html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta charset="UTF-8">
    <title>扫一扫</title>
    <%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
    <script type="text/javascript" src="${hdpath}/js/jquery.js"></script>
    <script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    <script  type="text/javascript">
    var timestampstr = $("#timestamp").val();
	var nonceStrstr = '${ nonceStr }';
	var signaturestr = '${ signature }';
	var appIdstr = '${appId}';
	wx.config({
		debug : true,
		appId : appIdstr,
		timestamp : timestampstr,
		nonceStr : nonceStrstr,
		signature : signaturestr,
		jsApiList : [ 'checkJsApi', 'scanQRCode' ]
	});//end_config
        //步骤五
        wx.error(function(res) {
            alert("出错了：" + res.errMsg);
        });
        //步骤四
         wx.ready(function() {
            wx.checkJsApi({
                jsApiList : ['scanQRCode'],
                success : function(res) {
                }
            });
 
            //扫描二维码
            document.querySelector('#scanQRCode').onclick = function() {
                wx.scanQRCode({
                    needResult : 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
                    scanType : [ "qrCode", "barCode" ], // 可以指定扫二维码还是一维码，默认二者都有
                    success : function(res) {
                        var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
                        //document.getElementById("wm_id").value = result;//将扫描的结果赋予到jsp对应值上
                        alert("扫描成功::扫描码=" + result);
                    }
                });
            };//end_document_scanQRCode
 
        });//end_ready
    </script>
</head>
<body>
<input id="timestamp" type="hidden" value="${timestamp}">
<button id="scanQRCode" >扫描二维码</button>
</body>
</html>