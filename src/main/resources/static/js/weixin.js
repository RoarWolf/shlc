//调用微信扫一扫功能所在的页面
var url = window.location.href;

//服务端提供的接口。HOST是服务器的地址，在项目中定义为全局变量；
var url11 = $
{
	hdpath
}
+"/scancode";

//ajax注入权限验证 
$.ajax({
	//与服务器交互
	url : url11,
	dataType : 'json',
	data : {
		"url" : url
	},
	complete : function(XMLHttpRequest, textStatus) {

	},
	error : function(XMLHttpRequest, textStatus, errorThrown) {
		alert("发生错误：" + errorThrown);
	},
	success : function(res) {
		var appId = '${appId}';
		var noncestr = '${nonceStr}';
		var timestamp = ${timestamp};
		var signature = '${signature}';

		wx.config({
			debug : true, //开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。 
			appId : appId, //必填，公众号的唯一标识 
			timestamp : timestamp, // 必填，生成签名的时间戳 
			nonceStr : noncestr, //必填，生成签名的随机串 
			signature : signature,// 必填，签名，见附录1 
			jsApiList : [ 'checkJsApi', 'scanQRCode' ]
		//必填，需要使用的JS接口列表，所有JS接口列表 见附录2 
		});
	}
});

wx.error(function(res) {
	alert("出错了：" + res.errMsg);
});

//通过ready接口处理成功验证
wx.ready(function() {
	// config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，
	//config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，
	//则须把相关接口放在ready函数中调用来确保正确执行。
	//对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。

	wx.checkJsApi({
		jsApiList : [ 'scanQRCode' ],
		success : function(res) {
			alert(JSON.stringify(res));
		}
	});

	//扫描二维码 
	document.querySelector('#scanQRCode').onclick = function() {
		wx.scanQRCode({
			needResult : 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果， 
			scanType : [ "qrCode", "barCode" ], // 可以指定扫二维码还是一维码，默认二者都有 
			success : function(res) {
				var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果 
				//document.getElementById("wm_id").value = result; //将扫描的结果赋予到jsp对应值上
				alert("扫描成功::扫描码=" + result);
			}
		});
	}; //end_document_scanQRCode 
});