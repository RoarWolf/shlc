<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>小区管理</title>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/mui.min.css"/>
<link rel="stylesheet" type="text/css" href="${hdpath}/hdfile/css/base.css"/>
<link rel="stylesheet" type="text/css" href="${hdpath}/hdfile/css/areainfo.css"/>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="${hdpath }/mui/js/mui.min.js"></script>
<script type="text/javascript" src="${hdpath }/js/jquery.js"></script>
</head>
<body>
	<div class="areaManage">
		<div class="list">
			<ul>
				<c:forEach items="${areaSelf}" var="areaSelfItem">
					<li>
						<h3>小区名称：${areaSelfItem.name}</h3>
						<div class="listDiv clearfix"> 
							<div><span><i class="iconfont icon-bianhao"></i></span>设备数量：<span>${areaSelfItem.equcount}台</span></div>
							<div><span><i class="iconfont icon-consumption"></i></span>总在线收益：<span>${areaSelfItem.totalOnlineEarn == null ? 0 : areaSelfItem.totalOnlineEarn}元</span></div>
							<div><span><i class="iconfont icon-consumption" style="font-size: 14px;" ></i></span>今日在线收益：<span>${areaSelfItem.nowOnlineEarn == null ? 0 : areaSelfItem.nowOnlineEarn}元</span></div>
							<c:if test="${showincoins != 2}">
								<div><span><i class="iconfont icon-consumption"></i></span>总投币收益：<span>${areaSelfItem.totalCoinsEarn == null ? 0 : areaSelfItem.totalCoinsEarn}元</span></div>
								<div><span><i class="iconfont icon-consumption"></i></span>今日投币收益：<span>${areaSelfItem.nowCoinsEarn == null ? 0 : areaSelfItem.nowCoinsEarn}元</span></div>
							</c:if>
							<div>
								<a href="/merchant/areaBelongInfo?id=${areaSelfItem.id }">管理</a>
								<a href="/merchant/queryAreaEarnRecord?aid=${areaSelfItem.id}">统计</a>
							</div>
						</div>
					</li>
				</c:forEach>
				<c:forEach items="${areaPart}" var="areaPartItem">
					<li>
					<h3>小区名称： ${areaPartItem.name}<span style="margin-left: 15px;">(合伙小区)</span></h3>
					<div class="listDiv clearfix"> 
						<div><span><i class="iconfont icon-bianhao"></i></span>设备数量：<span>${areaPartItem.equcount}台</span></div>
						<div><span><i class="iconfont icon-weibiaoti16" style="font-size: 14px;" ></i></span>分成比：<span>${areaPartItem.divideinto*100}%</span></div>
						<div><span><i class="iconfont icon-consumption"></i></span>总在线收益：<span>${areaPartItem.totalOnlineEarn == null ? 0 : areaPartItem.totalOnlineEarn}元</span></div>
						<div><span><i class="iconfont icon-consumption" style="font-size: 14px;" ></i></span>今日在线收益：<span>${areaPartItem.nowOnlineEarn == null ? 0 : areaPartItem.nowOnlineEarn}元</span></div>
						<c:if test="${showincoins != 2}">
							<div><span><i class="iconfont icon-consumption"></i></span>总投币收益：<span>${areaPartItem.totalCoinsEarn == null ? 0 : areaPartItem.totalCoinsEarn}元</span></div>
							<div><span><i class="iconfont icon-consumption"></i></span>今日投币收益：<span>${areaPartItem.nowCoinsEarn == null ? 0 : areaPartItem.nowCoinsEarn}元</span></div>
						</c:if>
						<div>
							<a href="/merchant/queryAreaEarnRecord?aid=${areaPartItem.id}">统计</a>
						</div>
					</div>
				</li>
				</c:forEach>
				
			</ul>
			<a href="/merchant/lookUnbindAreaEqu" style="display:block; text-align: center; margin-bottom: 20px;">未绑定设备：${unbingnum }</a>
		</div>
	</div>
	<div class="areaBelongInfo2">
				<h5>添加小区</h5>
				<form class="mui-input-group" id="dialoarea">
					<div class="mui-input-row">
						<label>小区名称</label>
						<input type="text" placeholder="请输入小区名称" id="name" name="name">
					</div>
					<div class="mui-input-row">
						<label>小区地址</label>
						<input type="text" class="mui-input-clear" placeholder="请输入小区地址" id="address" name="address"><span class="mui-icon mui-icon-clear mui-hidden"></span>
					</div>
					<div class="mui-button-row formBottom">
						<button type="button" class="mui-btn mui-btn-primary submit" onclick="return false;">确认</button>&nbsp;&nbsp;
						<button type="button" class="mui-btn mui-btn-danger closeBtn" >取消</button>
					</div>
				</form>
	</div>
	<nav class="mui-bar mui-bar-tab">
		<a href="javascript:void(0)" id="addArea">添加小区</a>
	</nav>
</body>
<script type="text/javascript">
	$('#addArea').click(function(){
		$('.list').slideUp()
		$('.areaBelongInfo2').slideDown()
	})
	$('.closeBtn').click(function(){
		$('.areaBelongInfo2').slideUp()
		$('.list').slideDown()
	})
	$('.submit').click(function(){
		$('.areaBelongInfo2').slideUp()
		$('.list').slideDown()
	})

//mui初始化
/*mui.init({
    swipeBack: true //启用右滑关闭功能
});*/
$("#uphonenum").blur(function(){
	var mobile = $("#uphonenum").val();
	if(mobile=="" ){
		mui.toast('请输入合伙人手机号。')
		return false;
	}
	$.ajax({
        type : "POST",
		url : "${hdpath}/merchant/checkAccount", //处理页面的路径
		data : {
			mobile : mobile,
		},
	    success:function(e){ //服务器返回响应，根据响应结果，分析是否登录成功；
	    	if (e != null && e!="") {
	    		$("#partner").val(e.realname);
	    		$("#nick").val(e.username);
	    	} else {
	    		mui.alert("该手机号用户不存在！");
	    		$("#uphonenum").val("");
	    	}
	    },
	    error:function(){//异常处理；
	    	 mui.toast('错误!')
	    }
	})
});
var info = document.getElementById("divides");
$('.submit')[0].addEventListener('tap', function() {
            var nameval = $("#name").val();
        	if(nameval == null || nameval == "") {
        		mui.alert("请填写小区名称");
        		return
        	}
       		var url;
       		var data = $("#dialoarea").serialize();
       		console.log(data)
       		url = "${hdpath}/merchant/insertArea";//添加
       		$.ajax({
       	        type : "POST",
       			url : url,
       			data : data,
       		    success:function(e){
       		    	if (e.code == 200) {
       		    		mui.toast(e.messg,{ duration:'1500', type:'div' })
       					location.reload();
       				}else{
       					mui.toast(e.messg,{ duration:'1500', type:'div' })
       				}
       		    },
       		    error:function(){
       		    	 mui.toast('异常传输!')
       		    }
       		}) 
});
</script>
</html>
<script>
$(function(){
    pushHistory();
    window.addEventListener("popstate", function(e) {
    	location.replace('/merchant/manage');
	}, false);
    function pushHistory() {
        var state = {
            title: "title",
            url: "#"
        };
        window.history.pushState(state, "title", "#");
    }
});
</script>