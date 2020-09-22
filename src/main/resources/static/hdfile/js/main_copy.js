// 主页入口文件
$(function(){

	// 发送ajax获取数据
	/*1、饼状图数据格式
		data= [
		        {value:335, name:'微信'},
		        {value:310, name:'支付宝'},
		        {value:234, name:'投币'},
		        {value:135, name:'小程序'}
	     	]
	  2、折线图,一周营业额趋势
		data: ['20190507', '20190508', '20190509', '20190510', '20190511', '20190512', '20190513'] 日期
	    data: [820, 932, 901, 934, 1290, 1330, 1320]  交易额

	  3、 近七天站点收入排行
	  
   */




	var titleColor= '#F8A1A4' //标题颜色
		resizeHandle(false)
	$(window).on('resize',resizeHandle)

	function resizeHandle(isResize){
		if(isResize){
			// var reg=/^./+/
			window.location.href= window.location.href 
		}
		if($(document.body).width()>= 700 && $(document.body).width() < 1200){
			var frame= document.createDocumentFragment()
			var center= $('section .center')[0]
			var left= $('section .left')[0]
			var right= $('section .right')[0]
			// console.log(frame,center,left,right)
			var divObj= $('<div class="left-right"></div>')[0]
			divObj.appendChild(left)
			divObj.appendChild(right)
			frame.appendChild(center)
			frame.appendChild(divObj)
			// console.log($('section').children())
			$('section').html('')
			$('section')[0].appendChild(frame)
			// if(isResize){
			// 	flag= true
			// }
		}else if($(document.body).width() > 1200){
			var frame= document.createDocumentFragment()
			var center= $('section .center')[0]
			var left= $('section .left')[0]
			var right= $('section .right')[0]
			frame.appendChild(left)
			frame.appendChild(center)
			frame.appendChild(right)
			$('section').html('')
			$('section')[0].appendChild(frame)
		}else if($(document.body).width() < 700){
			var frame= document.createDocumentFragment()
			var center= $('section .center')[0]
			var left= $('section .left')[0]
			var right= $('section .right')[0]
			frame.appendChild(center)
			frame.appendChild(left)
			frame.appendChild(right)
			$('section').html('')
			$('section')[0].appendChild(frame)
		}
		
	}


	 var myChart1 = echarts.init($('.ct1').get(0));
	//$('.ct1').click(function(){
	//	console.log(1)
	//})
        // 指定图表的配置项和数据
	var option1 ={
	  title: {
	  	text: '系统交易统计',
	  	textStyle: {
	  		color: titleColor,
	  		fontSize: 14,
	  	},
	  	left: '3%',
	  	top: '3%'
	  },
    tooltip: {
        trigger: 'item',
        formatter: "{a} <br/>{b}: {c} ({d}%)"
    },
    legend: {
        top: '15%',
        data:['微信','支付宝','投币','小程序'],
        textStyle: {
        	color: '#BFBFBF'
        }
    },
    series: [
        {
            name:'系统交易统计',
            type:'pie',
            selectedMode: 'single',
            center: ['50%', '60%'],
            radius: [0, '30%'],

            label: {
                normal: {
                    position: 'inner'
                }
            },
            labelLine: {
                normal: {
                    show: false
                }
            },
            data:[
                // {value:300, name:'直达', selected:true},
                // {value:679, name:'营销广告'},
                // {value:1548, name:'搜索引擎'}
                {value:780, name:'线上'},
                {value:234, name:'线下'},
     
            ]
        },
        {
            name:'系统交易统计',
            type:'pie',
            // center: {['50%', '55%'],['50%', '55%']},
            center: ['50%', '60%'],
            radius: ['40%', '55%'],
            data:[
                {value:335, name:'微信'},
                {value:310, name:'支付宝'},
                {value:234, name:'投币'},
                {value:135, name:'小程序'}
            ]
        }
    ]
}
		myChart1.on('click',function(params){
			console.log(params.name)
		})
        // 使用刚指定的配置项和数据显示图表。
        myChart1.setOption(option1);
		// ===========第二张表
		var myChart4 = echarts.init($('.ct4').get(0));

		var option4 = {
			title: {
				text: '近一周营业额趋势',
				textStyle: {
			  		color: titleColor,
			  		fontSize: 14,
			  	},
			  	left: '3%',
			  	top: '3%'
			},
			tooltip: {
		        
		        	formatter: '{a}:<br /> 日期：{b}  <br /> 营业额：{c}元'
		        },
			 legend: {
				top: '5%',
				right: '3%',
				data: ['近一周'],
				textStyle: {
				  fontSize: 10,
				  fontWeight: 'normal',
				  color: '#C8EBFA'
				},
			  },
   			 xAxis: {
		        type: 'category',
		        boundaryGap: false,
		        data: ['20190507', '20190508', '20190509', '20190510', '20190511', '20190512', '20190513'],
		        axisLine: {
		        	lineStyle: {
		        		color: '#B1F9EC'
		        	}
		        },
		        axisLabel: {
		        	formatter: function(parames){
		        		// console.log(parames)
		        		var reg= /.{4}/
		        		var str1= parames.substr(0,4)
		        		var str2= parames.substr(4)
		        		return str1+'\n'+str2
		        	}
		        }
		    },
		    yAxis: {
		        type: 'value',
		        axisLine: {
		        	lineStyle: {
		        		color: '#B1F9EC'
		        	}
		        }
		    },
		    series: [{
		    	name: '近一周',
		        data: [820, 932, 901, 934, 1290, 1330, 1320],
		        type: 'line',
		        smooth:true,  //这个是把线变成曲线
		        areaStyle: {
		        	color: 'rgba(161,105,223,.9)'
		        },
		        lineStyle: {
		        	color: "#AD85D8"
		        },
		       
		    }]
		}
				
		myChart4.setOption(option4);
		myChart4.on('click',function(params){
			// console.log(params.name)
		})

		// ===
	
	//  处理时间
		handleTime()
		setInterval(handleTime,1000)
	function handleTime(){
		var dt= new Date()
		var year= dt.getFullYear()
		var month= dt.getMonth()+1 <= 9 ?  '0'+(dt.getMonth()+1) : dt.getMonth()+1
		var day= dt.getDate() <= 9 ?  '0'+(dt.getDate()) : dt.getDate()
		var hours= dt.getHours() <= 9 ?  '0'+(dt.getHours()) : dt.getHours()
		var min= dt.getMinutes() <= 9 ?  '0'+(dt.getMinutes()) : dt.getMinutes()
		var second= dt.getSeconds() <= 9 ?  '0'+(dt.getSeconds()) : dt.getSeconds()
		//console.log(year,month,day,hours,min,second)
		$('.time .year').html(year)
		$('.time .month').html(month)
		$('.time .day').html(day)
		$('.time .time_hms').text(hours+':'+min+':'+second)
		//var str= '<span><b class="year">'+year+'</b>年<b class="month">'+month+'</b>月<b class="day">'+day+'</b>日</span><span> <b class="time_hms">'+hours+':'+min+':'+second+'</b></span>'
		//$('.time').html(str)
	}
})

