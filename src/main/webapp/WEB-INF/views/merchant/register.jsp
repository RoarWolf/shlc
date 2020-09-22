<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"/>
    <title>自助充电平台</title>
    <link rel="stylesheet" href="/mui/css/mui.min.css"/>
    <link rel="stylesheet" href="/hdfile/css/register.css"/>
    <%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
    <style>
    	@font-face {font-family: "iconfont";
		  src: url('//at.alicdn.com/t/font_1482138_5y9f0bb3zbb.eot?t=1572335030945'); /* IE9 */
		  src: url('//at.alicdn.com/t/font_1482138_5y9f0bb3zbb.eot?t=1572335030945#iefix') format('embedded-opentype'), /* IE6-IE8 */
		  url('data:application/x-font-woff2;charset=utf-8;base64,d09GMgABAAAAAALQAAsAAAAABowAAAKCAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHEIGVgCCcAqBGIEwATYCJAMICwYABCAFhG0HLxvVBcguIScbXgREup1wnWy8I9NE8gMoqBri4WsNfX9vL0gKAIVLFJJkFZnxjWzZyIAs2BhdoVBX39Za5gGNyvhn+YpQoQLM7u0dZ/MFx05Xqdq6VrjmBM+Q2Q4+XgQTSKQKE48sDIs46a42iySoF3inx6UiYE8DBN9c/5/L6Y0CCmR+kHKZY/JRL8B4a4BjLYqsSCJvGLvAJRwn0KBfRWp9tFhDUWaNC8Sd0Wco5vyyzFJ1Qk3N0iy+yKlLb9MEPoffj39aUSQpC1bF3u3wBp1fbGfgpdrORwRAQKtLFJgFMnFUG9+REYyS0cCaNQxkOfSL/8dGsVedYH+dVdwEeiH/ngSu8FaLE8iwex7YGLUVuampvvniR6JqOifsXRypj7jZq0V87e9fHp7fPZ4bgYeX90/Sbx6rzO9lbY7P9cRpRWNrhR5Q0jAXinF8dYxJO2N+am9BV97r/44OQBVeCyeF4Kulsu1/HRn8vPQj9FYZ/Zyg7lyO4J8JelZlXWSp0tH5yJHMZ7wSkMtdKpa7w99YdfcNWkNdTsAgqdUHhTqjaCbPQqnRaqBGnXMV02DGGK5u1IllRK7HtE8Aoc0XJC1eodDmG83kXyj1+IcabZGBBidhy0bjYcoznYVueHlGc41C5ZikHJUuSd/9M2dFIXVLbE0Im/OLxdSYQuI5ZtgH3RJRqDgKcAROI9+PMObIpavUHJG4XV+4rXtT7RoFULaGkTNBbtDFM2RcRULKry/Kpc8vEe3Od8YthBr7FmGW6R011TR6oGN52ItwL69YD7QWIRSksEgAjcAg4vNFUFw/yEWuRI0zIhprq9u1VF95bX1l8HUboIGlSWH3KZT3xjoGAAAA') format('woff2'),
		  url('//at.alicdn.com/t/font_1482138_5y9f0bb3zbb.woff?t=1572335030945') format('woff'),
		  url('//at.alicdn.com/t/font_1482138_5y9f0bb3zbb.ttf?t=1572335030945') format('truetype'), /* chrome, firefox, opera, Safari, Android, iOS 4.2+ */
		  url('//at.alicdn.com/t/font_1482138_5y9f0bb3zbb.svg?t=1572335030945#iconfont') format('svg'); /* iOS 4.1- */
		}
		
		.iconfont {
		  font-family: "iconfont" !important;
		  font-size: 20px;
		  font-style: normal;
		  -webkit-font-smoothing: antialiased;
		  -moz-osx-font-smoothing: grayscale;
		}
		
		.icon-tishi:before {
		  content: "\e72b";
		}
		.register .content .formdiv > form > .invCode{
			position: relative;
		}
		.icon_tip {
			position: absolute;
			right: 10px;
			top: 50%;
			margin-top: -10px;
			color: #44AF3D;
		}
		#tipCon {
			padding: 15px;
			color: #666
		}
		#tipCon p {
			color: #666
		}
    </style>
    <script src="/mui/js/mui.min.js"></script>
</head>
<body>
    <div class="register">
        <div class="top">
            <img src="/images/htlogo.png" alt="自助充电平台"/>
            <h1>自助充电平台</h1>
        </div>
        <div class="content">
            <input type="hidden" id="paramstate" value="${paramstate}">
            <input type="hidden" id="existuser" value="${existuser}">

            <div class="formdiv">
                <form id="formht" class="mui-input-group inputgroup">
                    <input type="hidden" name="statuere" value="1">
                    <input type="hidden" name="time" id="time" value="">
                    <input type="hidden" name="captchaNum" id="captchaNum" value="">
                    <div class="mui-input-row inputrow inp">
                        <label class="muiabel">手机号：</label>
                        <input id="mobile" name="mobile"  type="text" class="mui-input-clear" placeholder="请输入手机号">
                    </div>
                   <div class="mui-input-row inputrow inp invCode">
                        <label class="muiabel">邀请码：</label>
                        <input id="invitationCode" name="invitecode"  type="text" class="mui-input-clear"   placeholder="请输入未绑定的设备号/商户电话">
                        <div class="icon_tip">
                        	<i class="iconfont icon-tishi"></i>
                        </div>
                    </div>
                    <div class="mui-input-row inputrow inp" id="getNum">
                        <label class="muiabel">验证码：</label>
                        <input id="captcha" name="captcha" type="text" value="" class="mui-input-clear verification" placeholder="请输入验证码">
                        <b class="reNum">获取验证码</b>
                        <b style="display: none" class="bTime">倒计时120s</b>
                    </div>
                    <!--<div class="mui-button-row inputrow">-->
                        <!--<button id="captchabur" name="captchabur" type="button" class="mui-btn mui-btn-success sendcode"  onClick="CaptchaSend();">发送验证码</button>-->
                    <!--</div>-->
                    <div class="mui-button-row inputrow sbumit-button">
                        <button id="register_btn">登录</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <div id="tipCon" class="mui-popover">
	 	<p>注</p>
	 	<p>注册商户：输入的邀请码为未绑定的设备号</p>
	 	<p>注册子账户：输入的邀请码为商户的手机号</p>
	</div>
    <script src="/js/jquery.js"></script>
    <script src="/hdfile/js/register.js"></script>
    <script>
    	$(function(){
    		$('.icon_tip').click(function(){
    			mui('.mui-popover').popover('show',$('.icon_tip').get(0))
    		})
    	})
    	
    </script>
</body>
</html>
