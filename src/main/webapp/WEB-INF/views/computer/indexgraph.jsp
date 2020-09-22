<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"/>
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<title>自助充电平台</title>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<link rel="stylesheet" href="${hdpath}/css/base.css"/>
<link rel="stylesheet" href="${hdpath}/hdfile/css/index_copy.css">
<title>首页</title>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=wvGuSDMA4xjj0t1dPqEtibPvXY5gyN4U"></script>
<script src="${hdpath}/hdfile/js/echarts.js"></script>
<script src="${hdpath}/hdfile/map/chinaMap.js"></script>
<script src="${hdpath}/hdfile/map/BmapLib.js"></script>
<script src="${hdpath}/hdfile/map/data.js"></script>
<script src="${hdpath}/hdfile/map/echarts.js"></script>
<script src="${hdpath}/js/jquery.js"></script>
</head>
<body data-list="${hebdomadmap }">
<script type="text/javascript">
	var list= $('body').attr('data-list')
	console.log(list)
	// 路径配置
	require.config({
		paths: {
			echarts: 'http://echarts.baidu.com/build/dist'
		}
	});
	require(
			[
				'echarts',
				'echarts/chart/map'
			],
			function (ec) {
				var myChart2 = ec.init(document.querySelector('.ct2'));
				$('.ct2 div').on('mousewheel',function(e){
					return false
				})
				$('.ct2 div').on('mousemove',function(e){
					return false
				})
				option2 = {
//					backgroundColor: '#000',
					geo: {
						roam: false
					},
					color: [
						'rgba(255, 255, 255, 0.5)',
						'rgba(14, 241, 242, 0.5)',
						'rgba(37, 140, 249, 0.5)'
					],
					title: {
						text: '设备站点分布',
//						subtext: '历史最高访客量：230987643',
						x: 'center',
						textStyle: {
							color: '#F8A1A4',
							fontSize: 14
						}
					},
					legend: {
						orient: 'vertical',
						x: 'left',
						data: ['强', '中', '弱'],
						textStyle: {
							color: '#f8f8f8'
						}
					},
					toolbox: {
						show: false
					},
					series: [
						{
							name: '弱',
							type: 'map',
							mapType: 'china',
							itemStyle: {
								normal: {
									borderColor: 'rgba(100,149,237,1)',
									borderWidth: 1.5,
									areaStyle: {
//										color: '#1b1b1b'
//										color: 'transparent'
										color:'rgba(0,0,0,.1)'
									}
								}
							},
							data: [],
							markPoint: {
								symbolSize: 2,
								large: true,
								effect: {
									show: true
								},
								data: (function () {
									var data = [];
									var len = 3000;
									var geoCoord
									while (len--) {
										geoCoord = placeList[len % placeList.length].geoCoord;
										var x = geoCoord[0] + Math.random() * 1 - 0.5;
										var y = geoCoord[1] + Math.random() * 2 - 0;
										if (ptInPolygon(x, y)) {
											data.push({
												name: placeList[len % placeList.length].name + len,
												value: 50,
												geoCoord: [
													x,
													y
												]
											})
										}
									}
									return data;
								})()
							}
						},
						{
							name: '中',
							type: 'map',
							mapType: 'china',
							data: [],
							markPoint: {
								symbolSize: 3,
								large: true,
								effect: {
									show: true
								},
								data: (function () {
									var data = [];
									var len = 1000;
									var geoCoord
									while (len--) {
										geoCoord = placeList[len % placeList.length].geoCoord;
										var x = geoCoord[0] + Math.random() * 5 - 2.5;
										var y = geoCoord[1] + Math.random() * 3 - 1.5;
										if (ptInPolygon(x, y)) {
											data.push({
												name: placeList[len % placeList.length].name + len,
												value: 50,
												geoCoord: [
													x,
													y
												]
											})
										}
									}
									return data;
								})()
							}
						},
						{
							name: '强',
							type: 'map',
							mapType: 'china',
							hoverable: false,
							roam: true,
							data: [],
							markPoint: {
								symbol: 'diamond',
								symbolSize: 6,
								large: true,
								effect: {
									show: true
								},
								data: (function () {
									var data = [];
									var len = placeList.length;
									while (len--) {
										data.push({
											name: placeList[len].name,
											value: 90,
											geoCoord: placeList[len].geoCoord
										})
									}
									return data;
								})()
							}
						}
					]
				};
				myChart2.setOption(option2);
			}
	);

	//判断点在多边形内还是外
	function ptInPolygon(lng, lat) {
		var pts = CreateChinaMapLine();

		var ply = new BMap.Polygon(pts);

		var pt = new BMap.Point(lng, lat);

		var result = BMapLib.GeoUtils.isPointInPolygon(pt, ply);

		if (result == true) {
			return true;
		} else {
			return false;
		}
	}

</script>
	<div class="app">
		<header>
			<h1>自助充电平台</h1>
			<h5>ZI ZHU CHONG DIAN PING TAI</h5>
		</header>
		<div class="disData">
			<ul>
				<li>
					<a href="javascript:;">
						<span>${tourist.alltourist}</span>
						<span>用户总数/人</span>
					</a>
				</li>
				<li>
					<a href="javascript:;">
						<span>${tourist.monthtourist}</span>
						<span>本月新增用户/人</span>
					</a>
				</li>
				<li>
					<a href="javascript:;">
						<span>${tourist.daytourist}</span>
						<span>今日新增用户/人</span>
					</a>
				</li>
				<li>
					<a href="javascript:;">
						<span>${tourist.ispeople}</span>
						<span>今日充电/人次</span>
					</a>
				</li>
				<li>
					<a href="javascript:;">
						<span>${totalmap.totalmoney}</span>
						<span>总的营业额/元</span>
					</a>
				</li>
				<li>
					<a href="javascript:;">
						<span>${earningsyest.moneytotal}</span>
						<span>昨日营业额/元 </span>
					</a>
				</li>
				<li>
					<a href="javascript:;">
						<span>${earningsnow.incomemoney}</span>
						<span>今日营业额/元</span>
					</a>
				</li>
				<li>
					<a href="javascript:;">
						<span>${tourist.online}</span>
						<span>在线设备数/台</span>
					</a>
				</li>
				
			</ul>
		</div>
		<section>
						<div class="left">
				<div class="ct1"></div>
				<div class="ct3">
					<div class="title">昨日设备收入排行</div>
					<div class="top">
						<c:forEach items="${codeincomefirst}" var="order"  varStatus="as">
						<li>
							<span><i class="iconfont icon-chongdianzhuang"></i></span>
							<span>${order.code}</span>
							<span>
								<span>${order.moneytotal}</span><span class="activeUp"><!-- <i class="iconfont icon-arrowUp-copy"></i> --></span>
							</span>
						</li>
						</c:forEach>
					</div>
					<ul>
						<c:forEach items="${codeincome}" var="order"  varStatus="as">
						<li>
							<p>
								<span><i class="iconfont icon-chongdianzhuang"></i></span>
								<span>${order.code}</span>
							</p>
							<p>
								<span>${order.moneytotal}</span>
								<!-- <span class="activeUp"><i class="iconfont icon-arrowUp-copy"></i></span> -->
							</p>
							<p>
								<span><fmt:formatDate value="${order.count_time}" pattern="yyyy-MM-dd"/></span>
								<!-- <span class="activeUp"><i class="iconfont icon-arrowUp-copy"></i></span> -->
							</p>
						</li>
						</c:forEach>
					</ul>
				</div>
			</div>
<!-- 			<div class="left">
				<div class="ct1"></div>
				<div class="ct3">
					<div class="title">近七天站点收入排行</div>
					<div class="top">
						<li>
							<span><i class="iconfont icon-chongdianzhuang"></i></span>
							<span>公园西路</span>
							<span>
								<span>9472.39</span><span class="activeUp"><i class="iconfont icon-arrowUp-copy"></i></span>
							</span>
						</li>
						<li>
							<span><i class="iconfont icon-chongdianzhuang"></i></span>
							<span>公园西路</span>
							<span>
								<span>9472.39</span><span class="activeUp"><i class="iconfont icon-arrowUp-copy"></i></span>
							</span>
						</li>
						<li>
							<span><i class="iconfont icon-chongdianzhuang"></i></span>
							<span>公园西路</span>
							<span>
								<span>9472.39</span><span class="activeUp"><i class="iconfont icon-arrowUp-copy"></i></span>
							</span>
						</li>
					</div>
					<ul>
						<li>
							<p>
								<span><i class="iconfont icon-chongdianzhuang"></i></span>
								<span>西沈庄社区</span>
							</p>
							<p>
								<span>33553.81</span>
								<span class="activeUp"><i class="iconfont icon-arrowUp-copy"></i></span>
							</p>
						</li>
						<li>
							<p>
								<span><i class="iconfont icon-chongdianzhuang"></i></span>
								<span>西沈庄社区</span>
							</p>
							<p>
								<span>33553.81</span>
								<span class="activeUp"><i class="iconfont icon-arrowUp-copy"></i></span>
							</p>
						</li>
						<li>
							<p>
								<span><i class="iconfont icon-chongdianzhuang"></i></span>
								<span>西沈庄社区</span>
							</p>
							<p>
								<span>33553.81</span>
								<span class="activeUp"><i class="iconfont icon-arrowUp-copy"></i></span>
							</p>
						</li>
						<li>
							<p>
								<span><i class="iconfont icon-chongdianzhuang"></i></span>
								<span>西沈庄社区</span>
							</p>
							<p>
								<span>33553.81</span>
								<span class="activeDown"><i class="iconfont icon-arrowDown-copy"></i></span>
							</p>
						</li>
						<li>
							<p>
								<span><i class="iconfont icon-chongdianzhuang"></i></span>
								<span>西沈庄社区</span>
							</p>
							<p>
								<span>33553.81</span>
								<span class="activeDown"><i class="iconfont icon-arrowDown-copy"></i></span>
							</p>
						</li>
						<li>
							<p>
								<span><i class="iconfont icon-chongdianzhuang"></i></span>
								<span>西沈庄社区</span>
							</p>
							<p>
								<span>33553.81</span>
								<span class="activeDown"><i class="iconfont icon-arrowDown-copy"></i></span>
							</p>
						</li>
					</ul>
				</div>
			</div> -->
			<div class="center">
				<div class="ct2"></div>
			</div>
			<div class="right">
				<div class="ct4"></div>
				<div class="ct5">
					<div class="title">最近订单</div>
					<ul>
						<li class="top"><span>设备号</span> <span>金额</span> <span>支付方式</span> <span>状态</span> <span>来源</span></li>
					</ul>
					<div class="scrollView">
						<ul>
							<c:forEach items="${sometimeorder}" var="order"  varStatus="as">
							<li>
								<span>${order.code}</span> <span>${order.money}</span> 
								<span>
								${order.paytype==1 ? "钱包" : order.paytype==2 ? "微信" : order.paytype==3 ? "支付宝" : order.paytype==4 ? "微信小程序" : order.paytype==5 ? "支付宝小程序" : "其它" }
								</span> 
								<span>
								${order.status==1 ? "正常" : order.status==2 ? "退款" : "其它" }
								</span>
								<span>
								${order.paysource==1 ? "充电" : order.paysource==2 ? "脉冲" : order.paysource==3 ? "充值机" : order.paysource==4 ? "钱包" : order.paysource==5 ? "在线卡" : "其它" }
								</span>
							</li>
							</c:forEach>
						</ul>
					</div>
				</div>
			</div>
		</section>
		<!-- <div class="time">
			<span><b class="year">2019</b>年<b class="month">5</b>月<b class="day">17</b>日</span><span> <b class="time_hms">16:55:12</b></span>
		</div> -->
	</div>
	<script src="${hdpath}/hdfile/js/main_copy.js"></script>
</body>
<script type="text/javascript">
$(document).ready(function(){	
	$('#10'+' a').addClass('at');
	$('#10').parent().parent().parent().prev().removeClass("collapsed");
	$('#10').parent().parent().parent().prev().attr("aria-expanded",true)
	$('#10').parent().parent().parent().addClass("in");
	$('#10').parent().parent().parent().prev().attr("aria-expanded",true)
	})
</script>
</html>