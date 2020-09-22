<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<title>历史电量</title>
	<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
	<link rel="stylesheet" href="/css/base.css">
	<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/mui.min.css"/>
	<script type="text/javascript" src="${hdpath }/js/jquery-2.1.0.js"></script>
	<script type="text/javascript" src="${hdpath }/mui/js/mui.min.js"></script>
	<script type="text/javascript" src="${hdpath }/js/echarts.min.js"></script>
	<style>
		/*body {
			background-color: #fff;
		}*/
		.top {
			background-color: #f5f7fa;
			margin-top: 55px;
		}
		#echart1 {
			widows: 100%;
			height: 280px;
		}
		h5 {
			margin: 10px 0 10px 10px;
		}
		.table_ul {
			/*margin-top: 20px;*/
			background-color: #f5f7fa;
			border-top: 1px solid rgba(0,0,0,.2);
		}
		.table_ul .item_li{
			display: flex;
		}
		.table_ul .item_li.title {
			text-align: center;
			color: #000;
			font-weight: bold;
		}
		.table_ul .item_div{
			flex: 1;
			border-right: 1px solid rgba(0,0,0,.2);
			border-bottom: 1px solid rgba(0,0,0,.2);
			padding: 8px 10px;
			color: #666;
			font-size: 14px;
			text-align: center;
		}
		.table_ul .item_li .item_div:last-child {
			border-right: none;
		}
	</style>
</head>
<body>
	<header class="mui-bar mui-bar-nav">
		<a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
		<h1 class="mui-title">历史温度</h1>
	</header>
	<div class="top">
		<div class="app">
			<div id="echart1"></div>
		</div>
	</div>
	<h5>温度历史记录</h5>
	<div class="content">
		<div class="tem_table">
			<ul class="table_ul">
				<li class="item_li title">
					<div class="item_div">查询时间</div>
					<div class="item_div">当时温度</div>
				</li>
				<li class="item_li">
					<div class="item_div">2020-05-06 15:13:02</div>
					<div class="item_div">56.3℃</div>
				</li>
				<li class="item_li">
					<div class="item_div">2020-05-06 15:13:02</div>
					<div class="item_div">56.3℃</div>
				</li>
				<li class="item_li">
					<div class="item_div">2020-05-06 15:13:02</div>
					<div class="item_div">56.3℃</div>
				</li>
			</ul>
		</div>
	</div>
<script>
$(function(){
	var myChart = echarts.init(document.getElementById('echart1'));
	myChart.setOption({
			grid: {
		        left: '5%',
		        right: '12%',
		        bottom: '3%',
		        top: '12%',
		        containLabel: true,
		        show: false
		    },
    	    xAxis: {
		        type: 'category',
		        name: '时间',
		        axisLine:{
                    lineStyle:{
                        color:'#666',
                    }
                },
		        data: ['2020-05-01', '2020-05-02', '2020-05-03', '2020-05-04', '2020-05-05', '2020-05-06', '2020-05-07']
		    },
		    yAxis: {
		        type: 'value',
		        name: '温度',
		       	axisLine:{
                    lineStyle:{
                        color:'#666',
                    }
                } 
		    },
           series: [
	            {
			        data: [45, 32, 68, 36, 53, 23, 43],
			        type: 'line',
			        smooth: true,
			        itemStyle: {
						normal: {
							color: '#8cd5c2', //改变折线点的颜色
							lineStyle: {
								color: '#8cd5c2' //改变折线颜色
							}
						}
					},
			    }
		    ]
       });
})
</script>	
</body>
</html>