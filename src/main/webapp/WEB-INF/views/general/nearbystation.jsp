<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
<title>附近电站</title>
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<link rel="stylesheet" href="${hdpath }/css/base.css">
<script type="text/javascript" src="${hdpath}/js/jquery.js"></script>
<script src="${hdpath }/mui/js/mui.min.js"></script>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<%-- <%@ include file="/WEB-INF/views/public/generalBtnNav.jsp"%> --%>
<style type="text/css">
@font-face {font-family: "iconfont";
  src: url('//at.alicdn.com/t/font_1348509_3qqrma7c2wn.eot?t=1568775338768'); /* IE9 */
  src: url('//at.alicdn.com/t/font_1348509_3qqrma7c2wn.eot?t=1568775338768#iefix') format('embedded-opentype'), /* IE6-IE8 */
  url('data:application/x-font-woff2;charset=utf-8;base64,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') format('woff2'),
  url('//at.alicdn.com/t/font_1348509_3qqrma7c2wn.woff?t=1568775338768') format('woff'),
  url('//at.alicdn.com/t/font_1348509_3qqrma7c2wn.ttf?t=1568775338768') format('truetype'), /* chrome, firefox, opera, Safari, Android, iOS 4.2+ */
  url('//at.alicdn.com/t/font_1348509_3qqrma7c2wn.svg?t=1568775338768#iconfont') format('svg'); /* iOS 4.1- */
}

.iconfont {
  font-family: "iconfont" !important;
  font-size: 16px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.icon-iconset0247:before {
  content: "\e692";
}

.icon-gerenzhongxin:before {
  content: "\e659";
}

.icon-jiazai:before {
  content: "\e62c";
}

.icon-ditu:before {
  content: "\e60b";
}

.icon-saoma:before {
  content: "\e600";
}

.icon-map:before {
  content: "\e60f";
}

.icon-port:before {
  content: "\e750";
}

.icon-ERP_xieyibianhao:before {
  content: "\e65c";
}

.icon-xiaoquguanli:before {
  content: "\e60e";
}

.icon-bianhao:before {
  content: "\e62b";
}

.icon-ziyuanxhdpi:before {
  content: "\e639";
}

.icon-xiaoqu-xianxing:before {
  content: "\e60d";
}

.icon-shebeimingcheng:before {
  content: "\e6c5";
}

.icon-shebeimingcheng1:before {
  content: "\e6de";
}

.icon-bianhao-copy:before {
  content: "\e751";
}

.icon-shebeimingcheng1-copy:before {
  content: "\e752";
}

.icon-ziyuanxhdpi-copy:before {
  content: "\e753";
}

.icon-xiaoquguanli-copy:before {
  content: "\e754";
}



	body{
		background: #efeff4;
	}
	.app {
		position: absolute;
		left: 0;
		top: 0;
		bottom: 50px;
		right: 0;
		color: #666;
		font-size: 12px;
		overflow: auto;
	}
	ol{
		margin: 0;
	}
	.itemArea {
		background-color: #f5f7fa;
		margin-bottom: 20px;
	}
	.info {
		padding: 15px;
		border-bottom: 1px solid #ccc;
	}
	.info>li{
		line-height: 1.7em;
	}
	 .info>li.fLi,.info>li.sLi{
		min-width: 49%;
		display: inline-block;
	}
	.info li>i {
		margin-right: 6px;
		color: #22B14C;
	}
	.info li>span:nth-child(2) {
		color: #000;
		/* font-weight: 500; */
	}
	.protsStatus {
		display: inline-block;
		vertical-align: middle;
	}
	.protsStatus li {
		float: left;
		padding-right: 18px;
	}
	.protsStatus li span {
		display: inline-block;
		padding: 0 8px;
		line-height: 1.3em;
		border-radius: 4px;
	}
	.protsStatus li span.green {
		background-color: #B3E0B9;
		border: 1px solid #22B14C;
	}
	.protsStatus li span.red {
		background-color: #FFE0CC;
		border: 1px solid #DDA989;
	}
	.portDiv ul {
		padding: 15px;
		display: flex;
		justify-content: flex-start;
		flex-wrap: wrap;
	}
	.portDiv ul li {
		width: 18%;
		height: 35px;
		margin-bottom: 10px;
		margin-left: calc(4% - 6px);
	}
	.portDiv ul li:nth-child(5n-4){
		margin-left: 0;
	}
	.portDiv ul li a {
		text-align: center;
		display: block;
		border: 1px solid #ccc;
		width: 100%;
		height: 100%;
		border-radius: 4px;
		line-height: 35px;
		background-color: #efeff4;
		color:#666;
		text-decoration: none;
	}
	.portDiv ul li a.use {
		background-color: #E8090A;
		color: #333;
	}
	.portDiv ul li a.fi {
		background-color: #a6a6a6ad;
		color: #333;
	}
	.loading,.noData {
		width: 100vw;
		height: 35px;
		line-height: 35px;
		font-size: 14px;
		color: #999;
		text-align: center;
	}
	.loading i {
		margin-left: 8px;
		color: #666;
		display: inline-block;
		animation: Load 1.5s linear infinite;
		-webkit-animation: Load 1.5s linear infinite;
		-moz-animation: Load 1.5s linear infinite;
		-o-animation: Load 1.5s linear infinite;
	}
	@keyframes Load {
		from {
			transform: rotate(0deg);
		}
		to {
			transform: rotate(360deg);
		}
	}

</style>
</head>
<body>
<%@ include file="/WEB-INF/views/public/generalBtnNav.jsp"%>
	<div class="app">
		<ol>
			<c:choose>
				<c:when test="${fn:length(listmap)<=0}">暂无数据</c:when>
				<c:otherwise>
					<c:forEach items="${listmap}" var="order">
					<div class="itemArea">
						<ul class="info">	
							<li class="fLi"><i class="iconfont icon-bianhao-copy" ></i> <span>设备编号：</span><span>${order.code}</span></li>
							<c:if test="${order.remark != null && order.remark != ''}">
							<li class="sLi"><i class="iconfont icon-shebeimingcheng1-copy"></i> <span>设备名称：</span><span>${order.remark}</span></li>
							</c:if>
							<c:if test="${order.areaname != null && order.areaname != ''}">
							<li><i class="iconfont icon-xiaoquguanli-copy"></i> <span>小区名称：</span><span>${order.areaname}</span></li>
							</c:if>
							<li><i class="iconfont icon-ziyuanxhdpi-copy"></i> <span> 端口状态：</span>
								<ul class="protsStatus">
									<li>共${order.portamount}个</li>
									<li style="color: #22B14C;">${order.portusable}个可用</li>
								</ul>
							</li>
						</ul>
						<div class="portDiv">
					
						<ul data-forbid="${order.forbidport }" data-userport="${order.userport}" data-failureport="${order.failureport}">
							<c:forEach  begin="1" end="10" varStatus="loop">
							<!-- 端口被禁用 -->
							  <c:set var="flag1" value="false"></c:set>
							  <c:forEach items="${order.forbidport }" var="forbid">
								   <c:if test="${forbid == loop.count}">
								   		<c:set var="flag1" value="true"></c:set>   
								   </c:if>
							  </c:forEach>
							  <!-- 端口被禁用 -->
							  <!-- 端口被占用 -->
							  <c:set var="flag2" value="false"></c:set>
							  <c:forEach items="${order.userport }" var="use">
								   <c:if test="${use == loop.count}">
								   		<c:set var="flag2" value="true"></c:set>   
								   </c:if>
							  </c:forEach>
							  <!-- 端口被占用 -->
							  <!-- 端口被损坏 -->
							  <c:set var="flag3" value="false"></c:set>
							  <c:forEach items="${order.failureport }" var="fail">
								   <c:if test="${fail == loop.count}">
								   		<c:set var="flag3" value="true"></c:set>   
								   </c:if>
							  </c:forEach>
							  <!-- 端口被损坏 -->
								<li>
									<a 
									class='<c:if test="${flag1==true}">use</c:if><c:if test="${flag2==true}">use</c:if><c:if test="${flag3==true}">fi</c:if>' 
									href='<c:choose><c:when test="${ !flag1 && !flag2 && !flag3}">/equipment/nearbyChargePort?codeAndPort=${order.code}${loop.count}</c:when><c:otherwise>javascript:void(0);</c:otherwise></c:choose>'
									>
									${loop.count}号
									</a>
								</li>
							</c:forEach>				
						</ul>
							<%-- <ul data-forbid="${order.forbidport }" data-userport="${order.userport}" data-failureport="${order.failureport}">
								<li><a href="javascript:void(0);" data-href="/general/chargepay?code=${order.code}&port=1">1号</a></li>
								<li><a href="javascript:void(0);" data-href="/general/chargepay?code=${order.code}&port=2">2号</a></li>
								<li><a href="javascript:void(0);" data-href="/general/chargepay?code=${order.code}&port=3">3号</a></li>
								<li><a href="javascript:void(0);" data-href="/general/chargepay?code=${order.code}&port=4">4号</a></li>
								<li><a href="javascript:void(0);" data-href="/general/chargepay?code=${order.code}&port=5">5号</a></li>
								<li><a href="javascript:void(0);" data-href="/general/chargepay?code=${order.code}&port=6">6号</a></li>
								<li><a href="javascript:void(0);" data-href="/general/chargepay?code=${order.code}&port=7">7号</a></li>
								<li><a href="javascript:void(0);" data-href="/general/chargepay?code=${order.code}&port=8">8号</a></li>
								<li><a href="javascript:void(0);" data-href="/general/chargepay?code=${order.code}&port=9">9号</a></li>
								<li><a href="javascript:void(0);" data-href="/general/chargepay?code=${order.code}&port=10">10号</a></li>
							</ul> --%>
						</div>
					</div>
					</c:forEach>
				</c:otherwise>
			</c:choose>
			<!-- <div class="loading">
				<span>正在加载</span><i class="iconfont icon-jiazai"></i>
				</div> -->
				<c:if test="${fn:length(listmap)>0 && fn:length(listmap)< 5}"><div class="noData">暂无更多数据</div></c:if>
			
		</ol>
	</div>
</body>
</html>
<script>
	$(function() {
		var currentPage= 2 //请求页码
		var flag= false //是否到底部
		var totalRows= 5 //页面总条数
		var isHasData= true //是否还有数据
		isHasData= $('.app ol .itemArea').length <5 ? false : true
		$('ol .itemArea:last').css('margin-bottom',0)
		/* reEachPort()
		function reEachPort(){
			$('.itemArea').each(function(i,item){
				var userportList= eval($(item).find('.portDiv>ul').attr('data-userport')) //已用
				var forbidList= eval($(item).find('.portDiv>ul').attr('data-forbid')) //指定
				var failureport= eval($(item).find('.portDiv>ul').attr('data-failureport')) //故障端口
				var list= forbidList.concat(userportList)
				
				$(failureport).each(function(k,ktem){
					$(item).find('.portDiv>ul>li').eq(ktem-1).find('a').addClass('fi')
				})
				$(list).each(function(j,jtem){
					$(item).find('.portDiv>ul>li').eq(jtem-1).find('a').addClass('use')
				})
			})
		} */
		$('.app').click(function(e){
			e= e || window.event
			var target= e.target || e.srcElement
			if(target.nodeName.toLowerCase()== 'a'){
				return //关闭a链接的跳转
				if($(target).hasClass('use')){
					return
				}
				if($(target).hasClass('fi')){
					return
				}
				window.location.href= $(target).attr('data-href').trim()
			}
		})
		
		/* $('ol').on('click','a',function(e){
			e= e || window.event()
			e.preventDefault()
			console.log(1)
			if($(this).hasClass('use')){
				return
			}
			if($(this).hasClass('fi')){
				return
			}
			
			window.location.href= $(this).attr('href').trim()
		})  */
		var obj= {
			url: '/general/nearbystationajax',
			error: function(err){
			}
		}
		
		scrollFn($('.app')[0],$('.app ol')[0],0.98,obj)
		
		
		/**
		@parames ele <Dom> 滚动父元素
		@parames cEle <Dom> 滚动子元素
		@parames rate <Number> 比值，当大于这个比值时请求数据 [0,1]
		@parames obj <Object> {url:String,success:Function,error:Function} //ajax数据
		*/
		function scrollFn(ele,cEle,rate,obj){
			$(ele).scroll(function(e){
				var cEleHeight= $(cEle).height()
				var target= e.srcElement || e.target
				var targetHeight= $(target).height()
				if(!isHasData){
					return 
				}
				if((target.scrollTop+targetHeight)/cEleHeight >= rate && !flag){
					$('.app ol').append($('<div class="loading"><span>正在加载</span><i class="iconfont icon-jiazai"></i></div>'))
					$.ajax({
						url: obj.url,
						data:{
							currentPage: currentPage,
							numPerPage: 5
						},
						type: 'post',
						success: function(res){
							var data= JSON.parse(res)
							var list= data.listMap
							var Fragmeng = document.createDocumentFragment();
							$(list).each(function(i,item){
	
								var portList= item.forbidport.concat(item.userport)
								var fiList= item.failureport
								var pStr= ''
								for(var k= 0; k< 10; k++){
									var portStr= '<li> <a href="/equipment/nearbyChargePort?codeAndPort='+item.code+''+(k+1)+'">'+(k+1)+'号</a></li>'
									$(fiList).each(function(m,mtem){
										if((mtem-1)== k){
											portStr= '<li><a class="fi" href="javascript:void(0);">'+(k+1)+'号</a></li>'
										} 
									})
									$(portList).each(function(j,jtem){
										if((jtem -1) == k){
											portStr= '<li><a class="use" href="javascript:void(0);" >'+(k+1)+'号</a></li>'
										}
									})
									
									pStr+=portStr
								}
								item.forbidport= '['+item.forbidport.join(',')+']'
								item.userport= '['+item.userport.join(',')+']'
								item.failureport= '['+item.failureport.join(',')+']'
								var remarkStr= ''
								if(item.remark != null && item.remark != '' && item.remark != undefined){
									remarkStr= '<li class="sLi"><i class="iconfont icon-shebeimingcheng1-copy"></i> <span>设备名称：</span><span>'+item.remark+'</span></li>'
								}
								var areaNameStr= ''
								if(item.areaname != null && item.areaname != '' && item.areaname != undefined){
									areaNameStr= '<li><i class="iconfont icon-xiaoquguanli-copy"></i> <span>小区名称：</span><span>'+item.areaname+'</span></li>'
								}
								var str= '<div class="itemArea"><ul class="info"><li class="fLi"><i class="iconfont icon-bianhao-copy" ></i> <span>设备编号：</span><span>'+item.code+'</span></li>'+remarkStr+areaNameStr+'<li><i class="iconfont icon-ziyuanxhdpi-copy"></i> <span> 端口状态：</span><ul class="protsStatus"><li>共'+item.portamount+'个</li><li style="color: #22B14C;">'+item.portusable+'个可用</li></ul></li></ul><div class="portDiv"><ul data-forbid="'+item.forbidport+'" data-userport="'+item.userport+'" data-failureport="'+item.failureport+'">'+pStr+'</ul></div></div>'	
								Fragmeng.appendChild($(str)[0])
							})
							$('.app ol')[0].appendChild($(Fragmeng)[0])
							flag= false
							currentPage++
							totalRows+=5
							$('ol .itemArea').css("margin-bottom",20)
							$('ol .itemArea:last').css('margin-bottom',0)
							if(data.totalRows<=totalRows){
								$('.app ol').append($('<div class="noData">暂无更多数据</div>'))
								isHasData= false
							}
						},
						error: obj.error,
						complete:function(){
							$('.app ol .loading').remove()
						}
					})
					flag= true
				}
			})
		}
		
	})
</script>