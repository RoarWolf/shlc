<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport"
	content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<!--App自定义的css-->
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/app.css" />
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="${hdpath }/mui/js/mui.min.js"></script>
<script type="text/javascript" src="/js/jquery.qrcode.js"></script> 
<script type="text/javascript" src="/js/qrcode.js"></script> 
<title>设备管理</title>
<style>
@font-face {font-family: "iconfont";
  src: url('//at.alicdn.com/t/font_1648193_pm6tcos5y8e.eot?t=1582275759742'); /* IE9 */
  src: url('//at.alicdn.com/t/font_1648193_pm6tcos5y8e.eot?t=1582275759742#iefix') format('embedded-opentype'), /* IE6-IE8 */
  url('data:application/x-font-woff2;charset=utf-8;base64,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') format('woff2'),
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
.change_IMEI {
	text-align: center;
}
.change_IMEI p {
	font-size: 13px;
	color: #666;
	text-align: left;
}
.change_IMEI i{
	font-size: 35px;
	margin-top: 15px;
	color: #007bdc;
	padding: 10px;
}
</style>
</head>
<body>
	<div style="padding-top: 10px" class="mui-content">
		<input type="hidden" id="code" value="${equipment.code }">
		<ul class="mui-table-view">
			<%-- <li class="mui-table-view-cell" id="remark">
				设备名称：<span id="remarkinfo">${equipment.remark }<span style="position: absolute; right: 27px;" class="mui-icon mui-icon-compose"></span></span>
			</li> --%>
			<li class="mui-table-view-cell" id="unbind">
				<font color="blue">解除绑定</font>
			</li>
			<li class="mui-table-view-cell">
				<a href="${hdpath }/merchant/devicetemdata?code=${equipment.code}"><font color="blue">收费模板</font></a>
			</li>
			<c:if test="${equipment.deviceType != 2 }">
				<li class="mui-table-view-cell">
					<a href="${hdpath }/equipment/codesystem?code=${equipment.code}"><font color="blue">系统参数</font></a>
				</li>
				<li class="mui-table-view-cell">
					<a href="${hdpath }/equipment/alarmsystem?code=${equipment.code}"><font color="blue">报警系统</font></a>
				</li>
			</c:if>
			<li class="mui-table-view-cell">
				<a href="${hdpath }/equipment/deviceDetail?code=${equipment.code}"><font color="blue">设备详情</font></a>
			</li>
			<c:if test="${equipment.hardversion == 01 }">
				<li class="mui-table-view-cell">
					<a href="${hdpath }/equipment/specifieduseportinfo?code=${equipment.code}"><font>端口指定</font></a>
				</li>
			</c:if>
			<c:if test="${equipment.deviceType == 1 }">
				<li class="mui-table-view-cell mui-collapse" id="qrcodeOper0" data-bluetooth1="0">
					<a class="mui-navigate-right" href="#">
						设备二维码
					</a>
					<ul class="mui-table-view mui-table-view-chevron">
						<li id="innerli" class="mui-table-view-cell" style="font-size: 14px;text-align: center;">
							<div id="qrcode0"></div>
							<font>设备编号：${equipment.code }</font>
						</li>
					</ul>
				</li>
				<c:if test="${equipment.hardversion != '03' && equipment.hardversion != '04'}">
				<li class="mui-table-view-cell">
					<a href="/merchant/devicePortQRCode?code=${equipment.code}">
						设备端口二维码
					</a>
				</li>
				</c:if>
				<li class="mui-table-view-cell mui-collapse">
					<a class="mui-navigate-right" href="#">
						更换模块
					</a>
					<ul class="mui-table-view mui-table-view-chevron change_IMEI">
						<li id="innerli" class="mui-table-view-cell">
							<p><strong>提示：</strong>此功能用于更换2G或者4G模块后，可以不用更换主机和插座二维码。</p>
							<p><strong>更换步骤：</strong></p>
							<p>1、将设备断电</p>
							<p>2、扫新模块二维码，并互换模块IMEI号</p>
							<p>3、拆掉老模块、装上新模块</p>
							<p>4、设备通电</p>
							<i class="iconfont icon-saomiao"></i>
						</li>
					</ul>
				</li>
			</c:if>
			<c:if test="${equipment.deviceType == 2 }">
				<li class="mui-table-view-cell mui-collapse" id="qrcodeOper1" data-bluetooth1="1">
					<a class="mui-navigate-right" href="#">
						设备二维码(投币器1)
					</a>
					<ul class="mui-table-view mui-table-view-chevron">
						<li id="innerli" class="mui-table-view-cell" style="font-size: 14px;text-align: center;">
							<div id="qrcode1"></div>
							<font>设备编号：${equipment.code }-1</font>
						</li>
					</ul>
				</li>
				<li class="mui-table-view-cell mui-collapse" id="qrcodeOper2" data-bluetooth1="2">
					<a class="mui-navigate-right" href="#">
						设备二维码(投币器2)
					</a>
					<ul class="mui-table-view mui-table-view-chevron">
						<li id="innerli" class="mui-table-view-cell" style="font-size: 14px;text-align: center;">
							<div id="qrcode2"></div>
							<font>设备编号：${equipment.code }-2</font>
						</li>
					</ul>
				</li>
				<li class="mui-table-view-cell mui-collapse" id="qrcodeOper3" data-bluetooth1="3">
					<a class="mui-navigate-right" href="#">
						设备二维码(投币器3)
					</a>
					<ul class="mui-table-view mui-table-view-chevron">
						<li id="innerli" class="mui-table-view-cell" style="font-size: 14px;text-align: center;">
							<div id="qrcode3"></div>
							<font>设备编号：${equipment.code }-3</font>
						</li>
					</ul>
				</li>
				<li class="mui-table-view-cell mui-collapse" id="qrcodeOper4" data-bluetooth1="4">
					<a class="mui-navigate-right" href="#">
						设备二维码(投币器4)
					</a>
					<ul class="mui-table-view mui-table-view-chevron">
						<li id="innerli" class="mui-table-view-cell" style="font-size: 14px;text-align: center;">
							<div id="qrcode4"></div>
							<font>设备编号：${equipment.code }-4</font>
						</li>
					</ul>
				</li>
			</c:if>
		</ul>
	</div>
</body>
</html>
<script>
$(function() {
	var domain = window.location.host;
	var code = $("#code").val();
	$("#qrcode0").qrcode({ 
	    render: "canvas", //table方式 
	    width: 200, //宽度 
	    height:200, //高度 
	    text: "http://" + domain + "/oauth2pay?code=" + code //任意内容 
	});
	$("#qrcode1").qrcode({ 
	    render: "canvas", //table方式 
	    width: 200, //宽度 
	    height:200, //高度 
	    text: "http://" + domain + "/applet/" + code + 1//任意内容 
	});
	$("#qrcode2").qrcode({ 
	    render: "canvas", //table方式 
	    width: 200, //宽度 
	    height:200, //高度 
	    text: "http://" + domain + "/applet/" + code + 2//任意内容 
	});
	$("#qrcode3").qrcode({ 
	    render: "canvas", //table方式 
	    width: 200, //宽度 
	    height:200, //高度 
	    text: "http://" + domain + "/applet/" + code + 3//任意内容 
	});
	$("#qrcode4").qrcode({ 
	    render: "canvas", //table方式 
	    width: 200, //宽度 
	    height:200, //高度 
	    text: "http://" + domain + "/applet/" + code + 4//任意内容 
	});
	$("#remark").click(function() {
		var codeVal = $("#code").val();
		var remark = $("#remarkinfo").text().trim();
		mui.prompt("", remark, "设备名称修改", [ '取消', '确定' ], function(e) {
			if (e.index == 1) {
				$.ajax({
					type : "POST",//方法类型
					dataType : "json",//预期服务器返回的数据类型
					url : "/equipment/editRemark",//url
					data : {
						code : codeVal,
						remark : e.value
					},
					success : function(e) {
						if (e == 1) {
							mui.toast("修改成功");
							window.location.reload();
						} else if (e == 0) {
							window.location.reload();
						}
						;
					},
					error : function() {
						mui.toast("异常错误！");
						window.location.reload();
					}
				});
			}
		})
	})
	$("#unbind").click(function() {
		var codeVal = $("#code").val();
			mui.confirm('确认解绑设备吗 ？', '解除绑定', [ '取消', '确定' ], function(e) {
			if (e.index == 1) {
				$.ajax({
					type : "POST",//方法类型
					dataType : "json",//预期服务器返回的数据类型
					url : "/equipment/useronbound",//url
					data : {
						code : codeVal,
					},
					success : function(e) {
						if (e == 1) {
							mui.toast("解绑成功");
							location.href = "/equipment/list?wolfparam=2";
						} else if (e == 0) {
							window.location.reload();
						}
						;
					},
					error : function() {
						mui.toast("异常错误！");
						window.location.reload();
					}
				});
			}
		})
	})
	
/* 扫描更换设备的IMEI号 */
$('.change_IMEI i').on('tap',function(){
	getCode()
})

/* 调取扫码 */
function getCode() {
	var pageUrl = window.location.href;
	$.ajax({
		url : '${hdpath}/merchant/jssdkWxGet',
		type : "POST",
		data : {pageUrl : pageUrl},
		cache : false,
		success : function(data) {
			var timestamp = data.timestamp;
			timestamp = parseInt(timestamp);
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
						var origin= window.location.hostname
						var code
						var baseUrl= window.location.hostname.indexOf('www.tengfuchong') != -1 ? 'www.tengfuchong' : 'www.he360'
						if(url.indexOf(baseUrl)!= -1){
							code= url.split(/\=/g)[1]
						}
						sendCode(code)
					}
				});
			})
	
			wx.error(function(res) {
				alert("错误：" + res.errMsg);
			});
		},//返回数据填充
	});
}
function sendCode(codeB){ 
	var codeA= $("#code").val().trim() //A设备的设备号
	$.ajax({
		url: '/merchant/merTranspositionImei',
		data: {
			code1: codeA, //自己的设备code
			code2: codeB //扫码获取的设备code
		},
		type: 'post',
		success: function(res){
			mui.toast(res.msg)
		},
		error: function(err){
			mui.toast('更换IMEI号失败！')
		}
	})
}

})
</script>