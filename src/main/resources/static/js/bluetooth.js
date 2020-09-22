jQuery(document).ready(function() {
	//初始化库 
	loadXMLDoc();
	//初始化库结束

	$("#CallGetWXrefresh").on("click", function(e) {
		//showdialog();

		//1. 打开微信设备 
		my_openWXDeviceLib();

		//2. 安装设备事件
		my_installwxEvents();

		//2.1安装状态改变事件 暂时不检测
		// my_onWXDeviceStateChange(); 

		//3. 安装接收到数据事件 暂时 
		my_onReceiveDataFromWXDevice();

		//4. 刷新设备信息
		my_getWXDeviceInfos();

	});

});
//微信硬件jsapi库
function loadXMLDoc() {
	var appId = jQuery("#appId").text();
	var timestamp = jQuery("#timestamp").text();
	var nonceStr = jQuery("#nonceStr").text();
	var signature = jQuery("#signature").text();
	wx.config({
		beta : true,
		debug : false,// 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
		appId : appId,
		timestamp : timestamp,
		nonceStr : nonceStr,
		signature : signature,
		jsApiList : [ 'openWXDeviceLib', 'closeWXDeviceLib',
				'getWXDeviceInfos', 'getWXDeviceBindTicket',
				'getWXDeviceUnbindTicket', 'startScanWXDevice',
				'stopScanWXDevice', 'connectWXDevice',
				'disconnectWXDevice', 'sendDataToWXDevice',
				'onWXDeviceBindStateChange', 'onWXDeviceStateChange',
				'onScanWXDeviceResult', 'onReceiveDataFromWXDevice',
				'onWXDeviceBluetoothStateChange', ]
	});
	mui.alert("初始化库结束");
}
//判断调用jsapi返回状态 true表示成功
wx.error(function(res) {
	mui.alert("调用微信jsapi返回的状态:" + res.errMsg);
});

/******************************分割线************************************************/
/*********************************************************
 * 打开微信设备
 * my_openWXDeviceLib
 * 入口参数：无
 * 出口参数：0表示打开成功；1表示打开失败
 *********************************************************/
function my_openWXDeviceLib() {
	var x = 0;
	WeixinJSBridge.invoke('openWXDeviceLib', {}, function(res) {
		if (res.err_msg == 'openWXDeviceLib:ok') {
			if (res.bluetoothState == 'off') {
				showdialog("太着急啦", "亲,使用前请先打开手机蓝牙！");
				$("#lbInfo").innerHTML = "请打开手机蓝牙";
				$("#lbInfo").css({
					color : "red"
				});
				x = 1;
				isOver();
			};
			if (res.bluetoothState == 'unauthorized') {
				showdialog("出错啦", "亲,请授权微信蓝牙功能并打开蓝牙！");
				$("#lbInfo").html("请授权蓝牙功能");
				$("#lbInfo").css({
					color : "red"
				});
				x = 1;
				isOver();
			};
			if (res.bluetoothState == 'on') {
				//showdialog("太着急啦","亲,请查看您的设备是否打开！");   
				$("#lbInfo").html("蓝牙已打开,未找到设备");
				$("#lbInfo").css({
					color : "red"
				});
				//$("#lbInfo").attr(("style", "background-color:#000");
				x = 0;
				//isOver();
			};
		} else {
			$("#lbInfo").html("1.微信蓝牙打开失败");
			x = 1;
			showdialog("微信蓝牙状态", "亲,请授权微信蓝牙功能并打开蓝牙！");
		}
	});
	return x; //0表示成功 1表示失败
}

/*********************************************************
 * 装载微信事件处理
 * my_installwxEvents
 * 入口参数：无
 * 出口参数：无
 *********************************************************/
function my_installwxEvents() {
	//1. 安装微信绑定事件
	WeixinJSBridge.on('onWXDeviceBindStateChange', function(argv) {
		//todo
	});
	//2. 扫描到某个设备      
	WeixinJSBridge.on('onScanWXDeviceResult', function(argv) {
		//todo
	});
	//3. 手机蓝牙状态改变事件    
	WeixinJSBridge.on('onWXDeviceBluetoothStateChange', function(argv) {
		//todo
	});
}

/*********************************************************
 * 接收到数据事件
 * my_onReceiveDataFromWXDevice
 * 入口参数：无
 * 出口参数：无
 *********************************************************/
function my_onReceiveDataFromWXDevice() {

	WeixinJSBridge.on('onReceiveDataFromWXDevice', function(argv) {
		var b = new Base64();  
        var str = b.decode(argv.base64Data);
		mlog(str + " , ");
	});
}

/**********************************************
 * 取得微信设备信息
 * my_getWXDeviceInfos
 * 入口参数：无
 * 出口参数：返回一个已经链接的设备的ID
 **********************************************/
function my_getWXDeviceInfos() {

	WeixinJSBridge.invoke('getWXDeviceInfos', {}, function(res) {
		var len = res.deviceInfos.length; //绑定设备总数量
		for (i = 0; i <= len - 1; i++) {
			//alert(i + ' ' + res.deviceInfos[i].deviceId + ' ' +res.deviceInfos[i].state); 
			if (res.deviceInfos[i].state === "connected") {
				$("#lbdeviceid").html(res.deviceInfos[i].deviceId);
				C_DEVICEID = res.deviceInfos[i].deviceId;
				$("#lbInfo").html("设备已成功连接");
				$("#lbInfo").css({
					color : "green"
				});

				break;
			}
		}

	});
	return;
}

function mlog(m) {
	var log = $('#logtext').val();
	log=log+m;
	$('#logtext').val(log);
}

/***************************************************************
 * 显示提示信息
 ***************************************************************/
function showdialog(DialogTitle, DialogContent) {
	var $dialog = $("#Mydialog");
	$dialog.find("#dialogTitle").html(DialogTitle);
	$dialog.find("#dialogContent").html(DialogContent);
	$dialog.show();
	$dialog.find(".weui_btn_dialog").one("click", function() {
		$dialog.hide();
	});
}

/***
 * 
 */
$("#icFuWei").on("click", function(e) {
	var sendData = $('#sendtext').val();
	var x = senddataBytes(sendData + "\r\n", C_DEVICEID);

	if (x === 0) {
		$("#lbInfo").html('x.发送完成')
	} else {
		$("#lbInfo").html('x.发送失败')
	};
});

/*******************************************************************
 * 发送数据函数
 * 入口参数：
 *     cmdBytes: 需要发送的命令字节
 *     selDeviceID: 选择的需要发送设备的ID 
 * 出口参数：
 *     返回: 0表示发送成功；1表示发送失败
 *     如果成功，则接收事件应该能够收到相应的数据
 *******************************************************************/
function senddataBytes(cmdBytes, selDeviceID) {
	//1. 如果输入的参数长度为零，则直接退出
	if (cmdBytes.length <= 0) {
		return 1
	};
	// alert("向微信发送指令数据");
	//1.1 如果设备ID为空，则直接返回
	if (selDeviceID.length <= 0) {
		return 1
	};
	//2. 发送数据
	var x = 0;
	var b = new Base64();
	WeixinJSBridge.invoke('sendDataToWXDevice', {
		"deviceId" : selDeviceID,
		"base64Data" : b.encode(cmdBytes)
	}, function(res) {
		//alert("向微信发送指令数据返回的状态"+res.err_msg);
		if (res.err_msg == 'sendDataToWXDevice:ok') {
			x = 0;
			mui.alert("数据发送成功");
		} else {
			x = 1;
			mui.alert("数据发送失败");
		}
	});
	return x;
}