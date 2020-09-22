<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>消费信息</title>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link rel="stylesheet" href="${hdpath}/css/admin.css">
<script src="${hdpath}/js/echarts.min.js"></script>
<script src="${hdpath}/js/paging.js"></script>
<script type="text/javascript">
$(document).ready(function(){menulocation(33);})
</script>
<style type="text/css">
.modal-backdrop {
    position: relative; 
}
.chargeInfo{
	width: 100%;
}
.chargeInfo:after {
	content:'';
	height: 0;
	visibility: hidden;
	display: block;
	clear: both;
	overflow: hidden;
}
.echartContent {
	width: 75%;
	height: 0;
	padding-top: 40%;
    position: relative;
    float: left;
}
#echart1 {
	width: 100%;
    height: 100%;
    position: absolute;
    left: 0;
    top: 0;
}
.chargeInfo .chargeInfoDetail {
	width: 25%;
	float: left;
	padding: 20px;
	color: #666;
	border-radius:5px;
	border: 1px solid #dedede;
}
.chargeInfo .chargeInfoDetail h1 {
	font-size: 18px;
	text-align: center;
	padding-bottom:20px;
	border-bottom: 1px solid #f0f0f0;
	font-weight: 700;
	
}
.chargeInfo .chargeInfoDetail ul li>div {
	line-height: 48px;
}
.chargeInfo .chargeInfoDetail ul {
	padding: 0;
}
.chargeInfo .chargeInfoDetail ul li:nth-child(odd){
	background: #f8f8f8;
}
.chargeInfo .chargeInfoDetail ul li>div.left {
	width: 36%;
	min-width: 100px;
	float: left;
	text-align: right;
	padding-right: 15px;
	font-weight: bold;
}
.chargeInfo .chargeInfoDetail ul li>div.right {
	overflow: hidden;
	text-align: center;
	height: 48px;
} 

#echarts-3 {
	width: 75%;
	height: 500px;
}
.tableContent .table {
	margin: 0;
}
.tableContent .table th, .tableContent .table td{
	width: 14.2857%;
	line-height: 28px;
	padding: 5px 0;
}
.tableContent .table>thead:first-child>tr:first-child>th {
	border: 1px solid #c3b9b9;
	border-bottom: none;
	background-color: #f2f2f2;
}
.chargeRecord {
	text-align: center;
	font-size: 18px;
	color: #666;
	margin: 38px 0 20px;
	font-weight: 600;
	font-family: "微软雅黑", "宋体", "Helvetica Neue", Arial, Helvetica, sans-serif;
}
</style>
</head>
<body style="background-color:#f2f9fd;">
<div class="header bg-main">
	<%@ include file="/WEB-INF/views/navigation.jsp"%>
</div>
<div class="leftnav" id="lefeMenu">
	<%@ include file="/WEB-INF/views/menu.jsp"%>
</div>
<div>
	<ul class="bread">
	  <li><a href="javascript:void(0)" target="right" class="icon-home">消费信息记录</a></li>
	</ul>
</div>
<div class="admin">
	 <div class="panel admin-panel" id="adminPanel">
  		 <div class="chargeInfo" >
  		 	<div class="echartContent">
	  		 	<div id="echart1"></div>
	  		 </div>
	  		 <div class="chargeInfoDetail">
	  		 	<h1>已使用信息</h1>
	  		 	<ul>
	  		 		<li><div class="left">订单号</div><div class="right">${mapinfo.ordernum}</div></li>
	  		 		<li><div class="left">付款金额</div><div class="right">${mapinfo.paymoney}元</div></li>
	  		 		<li><div class="left">使用时间</div><div class="right">${mapinfo.usetime}分钟</div></li>
	  		 		<li><div class="left">使用电量</div><div class="right">${mapinfo.useelec/100}度</div></li>
	  		 		<li><div class="left">最大功率</div><div class="right">${mapfunc.maxpower}瓦</div></li>
	  		 		<li><div class="left">设备号</div><div class="right">${mapinfo.devicenum}</div></li>
	  		 		<li><div class="left">端口号</div><div class="right">${mapinfo.port}</div></li>
	  		 		<li><div class="left">开始时间</div><div class="right">
	  		 		<c:if test="${mapinfo.begintime!=null}"><fmt:formatDate value="${mapinfo.begintime}" pattern="yyyy-MM-dd HH:mm:ss"/></c:if>
					<c:if test="${mapinfo.begintime==null}">— —</c:if>
					</div></li>
	  		 		<li><div class="left">结束时间</div><div class="right">
	  		 		<c:if test="${mapinfo.endtime!=null}"><fmt:formatDate value="${mapinfo.endtime}" pattern="yyyy-MM-dd HH:mm:ss"/></c:if>
					<c:if test="${mapinfo.endtime==null}">— —</c:if>
	  		 		</div></li>
	  		 	</ul>
	  		 </div>
  		 </div>	
  		 	<div class="chargeRecord">
  		 		充电日志
  		 	</div>
	  		<div class="tableContent">
				   <div class="table table-div tdTable">
					<table class="table table-hover" >
						 <thead>
						   <tr>
							<th>序号</th>
							<th>设备号</th>
							<th>端口号</th>
							<th>剩余时间(min)</th>
							<th>剩余电量(kW·h)</th>
							<th>实时功率(W)</th>
							<th>记录时间</th>
						</thead>
						<tbody>
						   <c:forEach items="${realrecord}" var="info"  varStatus="as">
							  <tr id="name${info.id}" >
								<td >${as.count+current}</td>
								<td>${info.code}</td>
								<td>${info.port}</td>
								<td>${info.chargetime}</td>
								<td>${info.surpluselec/100}</td>
								<td>${info.power}</td>
							    <td> 
							    <c:if test="${info.createtime!=null}"><fmt:formatDate value="${info.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/></c:if>
							    <c:if test="${info.createtime==null}">— —</c:if>
							    </td>
							   </tr>
							</c:forEach>
						 </tbody>
					   </table>
				   </div>
	  		</div>
		 
   		</div>
	</div>
</body>
</html>
<script type="text/javascript">
var orderId=${orderId};
var myChart = echarts.init(document.getElementById('echart1'));
myChart.setOption({
	    xAxis: {
	        type: 'category',
	        name: '分钟',
	    	nameGap: 5,
	    	 axisLabel:{color:'#666'},   // x轴字体颜色 
	         axisLine:{
	                 lineStyle:{color:'#666'}    // x轴坐标轴颜色
	                 },
	         axisTick:{
	                  lineStyle:{color:'#666'}    // x轴刻度的颜色
	                  },
	    },
	    yAxis: {
	        name: '功率（W）',
	        type: 'value',
	        axisLabel:{color:'#666'},   
	        axisLine:{
	                lineStyle:{color:'#666'}    
	                },
	        axisTick:{
	                 lineStyle:{color:'#666'}    
	                 },
	    },
	    title: {
	        text: '充电功率曲线',
	         textStyle : {
	           fontSize: 18,
	           color: '#333',
	           fontWeight: 'bold',
	           fontFamily: 'monospace'
	        },
	        left: '3%'
	    },
	   grid: {
	       left: '7%',
	       bottom: 30,
	       right: '5%'
	   },
	    series: [
	        {
	        	name: '功率',
	        	type: 'line',
	        	smooth:true,
	        	lineStyle: {
	        		normal: {
	        			color: '#6BD089',
	        			width: 2
	        		}
	        	},
	        	 itemStyle: { //拐点样式
						color: '#6BD089'
					  },
	        
	    	 }
	   ]
	   
	}
)
/* myChart.setOption({
	    title: {
	        text: '充电功率曲线'
	    },
    tooltip: {
        trigger: 'axis'
    },
    legend: {
        data:['功率']
    },
    xAxis: {
        data: []
    },
    yAxis: {
        type: 'value'
    },
    series: [
        {
        	name: '功率',
        	type: 'line',
        	data: []
    	}
   ]
}); */

myChart.showLoading({text:'正在努力加载数据中... ...'});
graph(myChart);

function graph(myChart){
     var names=[];
     var nums=[];
     $.ajax({
     	type : "post",
     	async : true, 
     	url : "/pcequipment/graphjson",
     	data : {orderId:orderId},
     	dataType : "json", 
     	success : function(result){
         	if (result) {
                for(var i=0;i<result.length;i++){
                    names.push(result[i].minuteTime); 
                    nums.push(result[i].power);
                }
                myChart.hideLoading();
                myChart.setOption({
                    xAxis: {
                        data: names
                    },
                    series: [
                        {
                        	name: '功率',
                        	type:'line',
                        	data: nums
                        	
                    	}
                    ]
                });
         	}
    	},
	     error : function(errorMsg) {
	     	alert("图表请求数据失败!");
	     	myChart.hideLoading();
	     }
	})
}
</script>
