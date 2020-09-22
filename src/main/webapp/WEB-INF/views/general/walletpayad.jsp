<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>支付完成</title>
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<link rel="stylesheet" href="${hdpath}/css/base.css">
<script type="text/javascript" src="${hdpath}/js/jquery-2.1.0.js"></script>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<style>
html,body {
   background-color: #F5F5F5;
}
 header {
     background-color: #fff;
     background-image: url(${hdpath}/images/line-bottom.png);
     background-position: left bottom;
     background-repeat: no-repeat;
     -webkit-background-size: 100%;
     background-size: 100%;
}
 header .top {
     display: flex;
     flex-direction: column;
     justify-content: center;
     align-items: center;
}
 header .top .top-img {
     margin-top: 1.1304347826086956rem;
     width: 2.5217391304347827rem;
     height: 2.5217391304347827rem;
     border-radius: 50%;
     border: 1px solid #c9c9c9;
     overflow: hidden;
}
 header .top .top-img img{
     width: 100%;
     height: 100%;
}
 header .top .top-title {
     margin-top: 0.6086956521739131rem;
     font-size: 0.7391304347826086rem;
}
 header .order {
     padding: 0 1.2173913043478262rem 0.9565217391304348rem 1.2173913043478262rem;
     font-size: 0.6956521739130435rem;
     margin-top: 1.3043478260869565rem;
}
 header .order li {
     display: flex;
     justify-content: space-between;
     padding: 0.34782608695652173rem 0;
}
 header .order li span:first-child {
     color: #a7a7a7;
}
 header .order li span.weight {
     font-weight: bold;
}
 .line {
     height: 0.043478260869565216rem;
     background-color: #cecece;
     margin: 0.34782608695652173rem 0;
}
#ad-1 {
	margin: 2rem auto 0.5rem;
	width: 85%;
	height: 10.244rem;
}
</style>
</head>
<body>
	<header>
		<div class="top">
			<div class="top-img">
				<img src="${hdpath}/images/tengfuchong.jpg" alt="自主充电平台">
			</div>
			<div class="top-title">自助充电平台</div>
		</div>
		<ul class="order">
			<li><span>订单尾号</span><span>${orderNum}</span></li>
			<li><span>订单金额</span><span>￥${payMoney}</span></li>
			<div class="line"></div>
			<li><span>支付总额</span><span class="weight">￥${payMoney}</span></li>
		</ul>
	</header>
	<section>
		<div id="ad-1"></div>
	</section>
	<script>
	var htmlwidth = document.documentElement.clientWidth || document.body.clientWidth;
    var fontSize= htmlwidth/16 > 40 ? 40 : htmlwidth/16
    var styleStr= "<style>html { font-size: "+fontSize+"px !important;}</style>"
    $('head').children().eq(0).before(styleStr)
	</script>
	<!-- <script type="text/javascript">
    // H5 SDK 在线文档地址：http://developers.adnet.qq.com/doc/web/js_develop 
    // 全局命名空间申明TencentGDT对象
    window.TencentGDT = window.TencentGDT || [];
    // 广告初始化
    window.TencentGDT.push({
   	    placement_id: '9051626690983123', // {String} - 广告位id - 必填
        app_id: '1110877314', // {String} - appid - 必填
        type: 'native', // {String} - 原生广告类型 - 必填
        //muid_type: '1', // {String} - 移动终端标识类型，1：imei，2：idfa，3：mac号 - 选填    
        //muid: '******', // {String} - 加密终端标识，详细加密算法见API说明 -  选填
        //count: 1, // {Number} - 拉取广告的数量，默认是3，最高支持10 - 选填
        display_type: 'banner',
        containerid: 'ad-1', // 广告容器
        onComplete: function(res) {
            if (res && res.constructor === Array) {
                // 原生模板广告位调用 window.TencentGDT.NATIVE.renderAd(res[0], 'containerId') 进行模板广告的渲染
                // res[0] 代表取广告数组第一个数据
                // containerId：广告容器ID
                window.TencentGDT.NATIVE.renderAd(res[0], 'containerId')
            } else {
                // 加载广告API，如广告回调无广告，可使用loadAd再次拉取广告
                // 注意：拉取广告频率每分钟不要超过20次，否则会被广告接口过滤，影响广告位填充率
                setTimeout(function() {
                    window.TencentGDT.NATIVE.loadAd(placement_id)
                }, 3000)
            }
        }
    });
    // H5 SDK接入全局只需运行一次
    (function() {
        var doc = document, 
        h = doc.getElementsByTagName('head')[0], 
        s = doc.createElement('script');
        s.async = true; 
        s.src = '//qzs.qq.com/qzone/biz/res/i.js';
        h && h.insertBefore(s, h.firstChild);
    })();
</script> -->
<script type="text/javascript">
    // H5 SDK 在线文档地址：http://developers.adnet.qq.com/doc/web/js_develop 
    // 全局命名空间申明TencentGDT对象
    window.TencentGDT = window.TencentGDT || [];
    // 广告初始化
    window.TencentGDT.push({
        placement_id: '6041520605959870', // {String} - 广告位id - 必填
        app_id: '1110877242', // {String} - appid - 必填
        type: 'native', // {String} - 原生广告类型 - 必填
        //muid_type: '1', // {String} - 移动终端标识类型，1：imei，2：idfa，3：mac号 - 选填    
        //muid: '******', // {String} - 加密终端标识，详细加密算法见API说明 -  选填
        //count: 1, // {Number} - 拉取广告的数量，默认是3，最高支持10 - 选填
        display_type: 'banner',
        containerid: 'ad-1', // 广告容器
        onComplete: function(res) {
            if (res && res.constructor === Array) {
                // 原生模板广告位调用 window.TencentGDT.NATIVE.renderAd(res[0], 'containerId') 进行模板广告的渲染
                // res[0] 代表取广告数组第一个数据
                // containerId：广告容器ID
                window.TencentGDT.NATIVE.renderAd(res[0], 'containerId')
            } else {
                // 加载广告API，如广告回调无广告，可使用loadAd再次拉取广告
                // 注意：拉取广告频率每分钟不要超过20次，否则会被广告接口过滤，影响广告位填充率
                setTimeout(function() {
                    window.TencentGDT.NATIVE.loadAd('6041520605959870')
                }, 3000)
            }
        }
    });
    // H5 SDK接入全局只需运行一次
    (function() {
        var doc = document, 
        h = doc.getElementsByTagName('head')[0], 
        s = doc.createElement('script');
        s.async = true; 
        s.src = '//qzs.qq.com/qzone/biz/res/i.js';
        h && h.insertBefore(s, h.firstChild);
    })();
</script>
</body>
</html>