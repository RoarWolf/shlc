/*var orderId=${orderId};*/
var orderId= $('body').attr('data-orderId').trim()

var myChart = echarts.init(document.getElementById('echarts-3'));
myChart.setOption({
	 grid: {
        left: '5%',
        right: '10%',
        bottom: '3%',
        top: '17%',
        containLabel: true,
        show: false
    },
});
myChart.showLoading({text:'正在努力加载数据中... ...'}); 
graph(myChart);
function graph(myChart){
    var names=[];
    var nums=[]; //功率list
    var listV=[]; //电压list
    var listA=[]; //电流list
    $.ajax({
    	type : "post",
    	async : true, 
    	url : "/merchant/powerdrawing",
    	data : {orderId:orderId},
    	dataType : "json", 
    	success : function(result){
        	if (result) {
               for(var i=0;i<result.length;i++){
                   names.push(result[i].minuteTime); 
                   nums.push(result[i].power);
                   listV.push(result[i].portV);
                   listA.push(result[i].portA);
               }
               var yAxis= []
               var series= []
               if(listV[0] == -1 || listA[0] == -1){
            	   yAxis= [{
             	    	name: '功率 W',
              	        type: 'value',
              	        axisLabel:{color:'#666'},   
              	        axisLine:{
              	           lineStyle:{color:'#666'}    
              	        },
              	        axisTick:{
              	            lineStyle:{color:'#666'}    
              	        }
              	     }
            	   ]
            	   
            	   series= [
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
                          	data: nums
                      	 },
            	       ]
            	   
               }else {
            	   yAxis= [
						{
						  	name: '功率 W',
						      type: 'value',
						      axisLabel:{color:'#666'},   
						      axisLine:{
						              lineStyle:{color:'#666'}    
						              },
						      axisTick:{
						               lineStyle:{color:'#666'}    
						               },
						   },
							{
							 type: 'value',
							 name: '电压/ 电流',
							 axisLabel: {
							     formatter: function (value, index) {
							    	if(index > 0){
							    		return value+'V'
							    	}else{
							    		return ''
							    	}
							     },
							     inside: true,
							     textStyle: {
							         color: '#007AAE'
							     }
							 },
							 axisLine:{
							        lineStyle:{color:'#666'}    
							        },
							 axisTick:{
							         lineStyle:{color:'#666'}    
							         }
							}, 
							{
							 type: 'value',
							 name: '',
							 axisLabel: {        
							     show: true,
							     formatter: function (value, index) {
							     	if(index > 0){
							     		return value+'A'
							     	}else{
							     		return ''
							     	}
							      },
							     textStyle: {
							         color: '#B21016'
							     }
							 },
							 axisLine:{
							        lineStyle:{color:'#666'}    
							        },
							axisTick:{
							         lineStyle:{color:'#666'}    
							       }
							},
            	   ]
            	   
            	   series= [
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
                               	data: nums
                           	 },
                           	{
                             	name: '电压',
                             	type: 'line',
                             	smooth:true,
                             	yAxisIndex: 1,
                             	lineStyle: {
                             		normal: {
                             			color: '#007AAE',
                             			width: 2
                             		}
                             	},
                             	 itemStyle: { //拐点样式
                     					color: '#007AAE'
                     				  },
                             	data: listV
                         	 },
                         	{
                             	name: '电流',
                             	type: 'line',
                             	smooth:true,
                             	yAxisIndex: 2,
                             	lineStyle: {
                             		normal: {
                             			color: '#B21016',
                             			width: 2
                             		}
                             	},
                             	 itemStyle: { //拐点样式
                     					color: '#B21016'
                     				  },
                             	data: listA
                         	 }
                 	       ]
            	   
               }
               
               myChart.hideLoading();
               myChart.setOption({
            	   tooltip: {
            	        trigger: 'axis'
            	    },
            	    legend: {
            	        data: ['功率', '电压', '电流']
            	    },
            	    xAxis: {
            	    	name: '分钟',
            	    	nameGap: 5,
            	    	 axisLabel:{color:'#666'},   // x轴字体颜色 
            	         axisLine:{
            	                 lineStyle:{color:'#666'}    // x轴坐标轴颜色
            	                 },
            	         axisTick:{
            	                  lineStyle:{color:'#666'}    // x轴刻度的颜色
            	                  },

            	        data: names
            	    },
                  /* xAxis: {
                       data: names
                   },*/
                   yAxis: yAxis,
                   series: series
               });
        }
   },
    error : function(errorMsg) {
   	 	alert("图表请求数据失败!");
    	myChart.hideLoading();
    }
});
}