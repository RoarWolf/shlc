
/*设备管理js入口文件*/
$(function(){
	var showincoins= $('body').attr('data-showincoins').trim() //是否显示投币收益 2为不显示，
	var flag1= false //没有到底部
	var flag2= false //没有到底部
	var flag3= false //没有到底部
	var equnum1= 5  //偏移量1
	var equnum2= 5  //偏移量1
	var equnum3= 5  //偏移量1
	var querynum= 1 //设备分类，也就是 在线设备、离线设备、全部设备默认是1
	var source= 1 //搜索分类，也就是 设备编号、设备名称、小区名称默认是1
	var parameter= '' //搜索内容 默认是 ''
	var uid= $('#uid').val().trim()  // 获取id
	var isHaveData1= true //是否还存在数据 ；true有数据； false没数据了
	var isHaveData2= true
	var isHaveData3= true
	var ulList1= $('.dm .ul1 .divLi>li').length
	var ulList2= $('.dm .ul2 .divLi>li').length
	var ulList3= $('.dm .ul3 .divLi>li').length
	isHaveData1= ulList1<5 ? false : true //是否还有数据
	isHaveData2= ulList2<5 ? false : true
	isHaveData3= ulList3<5 ? false : true
	var urlFrom= 1 // sessionStorage 记录第几个ul
	var scrollTop= 0 //记录线上滚动的距离
    
			var dataStr= sessionStorage.getItem('data') || "{}"
			var data= JSON.parse(dataStr)
			console.log(data)
			if(data.querynum){
				flag1= data.flag1
				flag2= data.flag2
				flag3= data.flag3
				equnum1= data.equnum1
				equnum2= data.equnum2
				equnum3= data.equnum3
				querynum= data.querynum
				parameter= data.parameter
				isHaveData1= data.isHaveData1
				isHaveData2= data.isHaveData2
				isHaveData3= data.isHaveData3
			}
			
			 $('body').click(function(e){
					e= e || window.event
					e.preventDefault()
					var target= e.target || e.srcElement
					var ulObj= $(target).parent().parent().parent().parent()
					if(target.nodeName.toLowerCase() == 'a' && ulObj.get(0).nodeName.toLowerCase() == 'ul'){
						scrollTop= ulObj.scrollTop()
						if(ulObj.attr('class').trim() == 'ul2'){
							urlFrom= 2
						}else if(ulObj.attr('class').trim() == 'ul3'){
							urlFrom= 3
						}else{
							urlFrom= 1
						}
						setSessionStroageFn()
						setTimeout(function(){
							location.href= $(target).attr('href')
						},50)
					}
					
				}) 

				 window.onpageshow= function(type){
					var ulHtml= sessionStorage.getItem('ulStr') || "{}"
					var ulObj= JSON.parse(ulHtml)
					if(typeof ulObj.ul1Str == 'undefined'){
						console.log(111)
						return
					}
					 $('main .content .ul1').html(ulObj.ul1Str)
					 $('main .content .ul2').html(ulObj.ul2Str)
					 $('main .content .ul3').html(ulObj.ul3Str)
					var numStr= sessionStorage.getItem('urlFrom') || "{}" 
					var obj= JSON.parse(numStr)
					var num= parseInt(obj.urlFrom)
					var scrollTop= obj.scrollTop
					$('.dm header li').removeClass('active')
					var ulWidth= $('.dm .content ul').eq(0).width()
					if(num === 2){
						$('.dm header li').eq(1).addClass('active')
						$('.dm .content').css({left:"-"+ulWidth+"px"})
						$('.dm .content .ul2').animate({scrollTop:scrollTop},0)
					}else if(num === 3){
						$('.dm header li').eq(2).addClass('active')
						$('.dm .content').css({left:-ulWidth*2+"px"})
						$('.dm .content .ul3').animate({scrollTop:scrollTop},0)
					}else {
						$('.dm header li').eq(0).addClass('active')
						$('.dm .content').css({left:"0px"})
						$('.dm .content .ul1').animate({scrollTop:scrollTop},0);
					}
					
				}	
			 /*window.addEventListener(function(){
				 console.log(1111)
				 setTimeout(function(){
					 location.href= location.origin+"/merchant/manage" ;
				 },50)
			 },false)
			 pushHistory();
			 function pushHistory() {
		         var url = "#";
		         var state = {
		             title: "title",
		             url: "#"
		         };
		         window.history.pushState(state, "title", "#");
				
			 }*/
			
			
			
			
			
			
			
			
			
			
			

	
	$('.dm header li').click(function(e){
		e= e || window.event
		var target= e.target || e.srcElement
		$('.dm header li').removeClass('active')
		$(target).addClass('active')
		// 获取ul宽度
		var ulWidth= $('.dm .content ul').eq(0).width()
		
		if($(target).index() === 0){
			querynum= 1 //给设备分类赋值，当前选择的是那个
			$('.dm .content').animate({left:"0px"},300)
		}else if($(target).index() === 1){
			querynum= 2
			$('.dm .content').animate({left:"-"+ulWidth+"px"},300)
		}else {
			querynum= 3
			$('.dm .content').animate({left:"-"+ulWidth*2+"px"},300)
		}
	})

	$('.searchBut').click(function(e){
		// console.log(deviceCategory,searchCategory)
		
		// 获取选择的设备分类
		parameter= $('.searchInp input').eq(0).val().trim()// 获取搜索内容
		sAjax(1,uid) //设备分类 id
		sAjax(2,uid)
		sAjax(3,uid)

	})
	

	function sAjax(querynum,uid){ //这里发送ajax是根据条件获取，只获取5条，替换div下面的所有元素,需要发送 3个ajax数据
		$.ajax({ 
				url: '/equipment/getAjaxEquList',
				data: {
					uid : uid,
					equnum : 0,
					querynum : querynum,
					source : source,
					parameter : parameter
				},
				type: 'POST',
				dataType : "json",
				success: function(data){
					console.log(data)
					var data= handleData(data)
					render(querynum,data,uid)
				}
		})
	}
	
	function render(querynum,data,uid){
		var divObj= null
		var bgGreen= '' //在线设备wifi显示绿色
		if(querynum == 1){
			bgGreen= 'bgGreen' //在线设备wifi显示绿色
			divObj= $('.dm .divLi').eq(0)
			equnum1= data.equnum
			isHaveData1= data.equlist.length<5 ? false : true
		}else if(querynum == 2){
			divObj= $('.dm .divLi').eq(1)
			equnum2= data.equnum
			isHaveData2= data.equlist.length<5 ? false : true
		}else if(querynum == 3){
			divObj= $('.dm .divLi').eq(2)
			equnum3= data.equnum
			isHaveData3= data.equlist.length<5 ? false : true
		}
			var frame= document.createDocumentFragment()
			var htmlStr= ''
			
			for(var i= 0; i< data.equlist.length; i++){
				var portStatusStr= '' //定义端口状态字符串
				var str= ''
				/*var orderHref= '' //订单地址
				var tjHref= '/equipment/codeDayEarn?code='+data.equlist[i].code //统计地址
				var ztDisable= '' //状态class
				var ycDisable= '' //远程class
				var manageHref= '' //管理href
				var mgDisable= '' //管理class
				var ztHref= ''
				var remoteHref= ''*/
						
				/*var wifiIcon= 'icon-WIFIxinhao-ji'
				if(data.equlist[i].csq >=0 && data.equlist[i].csq <= 5){
					wifiIcon= 'icon-WIFIxinhao-ji3'
				}else if(data.equlist[i].csq > 5 &&data.equlist[i].csq <= 10){
					wifiIcon= 'icon-WIFIxinhao-ji2'
				}else if(data.equlist[i].csq > 10 && data.equlist[i].csq <= 20){
					wifiIcon= 'icon-WIFIxinhao-ji1'
				}else if(data.equlist[i].csq > 20){
					wifiIcon= 'icon-WIFIxinhao-ji'
				}*/
				var deviceBindOwn= data.equlist[i].classify == 1 ? true : false //当前设备是否属于自己，（1为自己，2不是自己）
				if (data.equlist[i].hardversion != '03' && data.equlist[i].hardversion != '04') {
					if(data.equlist[i].state == 1){
						portStatusStr= '<p>端口状态： <span class="span_right_fr5">空闲('+data.equlist[i].freenum+')</span><span class="span_right_use5">使用('+data.equlist[i].usenum+')</span><span class="span_right_fi5">故障('+data.equlist[i].failnum+')</span></p>'
					}
				}
				var iconStr= '' //投币字符串
				if(showincoins != 2){
					iconStr= '<p>投币收益：<span>'+data.equlist[i].totalCoinsEarn+'<b>元</b></span></p>'
				}
				
				str= 
				`<li class="${deviceBindOwn ? '' : 'active'}">
					<p>设备编号：<span>${data.equlist[i].code}</span></p>
	                <p>线上收益：<span>${data.equlist[i].totalOnlineEarn} <b>元</b></span></p>
	                ${iconStr}
	                <p class="d-name">设备名称：<span>${data.equlist[i].remark}</span><i class="editName">编辑</i></p>
	                <p class="d-name">小区名称：<span>${data.equlist[i].name}</span><i class="editAreaName">编辑</i></p>
	                ${portStatusStr}
	                <div class="bottomBut">
		                <a href="/merchant/charge?code=${data.equlist[i].code}" class="statisticBut ${(data.equlist[i].hardversion != '03' && data.equlist[i].hardversion != '04' && deviceBindOwn && data.equlist[i].device_type != 2) ? '' : 'disable'}">状态</a>
	                    <a href="/equipment/equinfo?code=${data.equlist[i].code}" class="manageBut ${deviceBindOwn ? '' : 'disable'}">管理</a>
	                    <a href="/merchant/remotechargechoose?code=${data.equlist[i].code}" class="remoteBut ${(deviceBindOwn && data.equlist[i].state == 1 && data.equlist[i].device_type != 2) ? '' : 'disable'}">远程</a>
	                    <a href="javascript:;" class="wifi ${data.equlist[i].state == 1 ? 'bgGreen' : ''} ${data.equlist[i].device_type != 2 ? '' : 'disable'}">
	                    	<i class="iconfont ${(data.equlist[i].csq >= 0 && data.equlist[i].csq <= 5) ? 'icon-WIFIxinhao-ji3' : (data.equlist[i].csq > 5 && data.equlist[i].csq <= 10) ? 'icon-WIFIxinhao-ji2' : (data.equlist[i].csq > 10 && data.equlist[i].csq <= 20) ? 'icon-WIFIxinhao-ji1' : 'icon-WIFIxinhao-ji' }"></i>
	                    </a>
	                    <a href="/equipment/codeDayEarn?code=${data.equlist[i].code}">统计</a>
	                    <a href="/equipment/codetotrade?souce=${deviceBindOwn ? 1 : 2}&amp;code=${data.equlist[i].code}">订单</a>
	                </div>
	            </li>`
				/*if(!deviceBindOwn){ //不是自己的设备
					if (data.equlist[i].hardversion != '03' && data.equlist[i].hardversion != '04') {
						ztHref= '/merchant/charge?code='+ data.equlist[i].code
					} else {
						ztHref= 'javascript:;'
						ztDisable='disable'
					}
					
					if (data.equlist[i].state == 1 && deviceBindOwn) {
						remoteHref= '/merchant/remotechargechoose?code='+data.equlist[i].code
					} else {
						remoteHref= 'javascript:;'
						ycDisable= 'disable'
					}
					
					//判断管理按钮					
					if (deviceBindOwn) {
						manageHref= '/merchant/remotechargechoose?code='+data.equlist[i].code
					} else {
						manageHref= 'javascript:;'
						mgDisable= 'disable'
					}
					var manageHref= '/equipment/equinfo?code='+ data.equlist[i].code
					orderHref= '/equipment/codetotrade?souce=1&code=' + data.equlist[i].code
					str= `<li class="active">
			                <p>设备编号：<span>${data.equlist[i].code}</span></p>
			                <p>线上收益：<span>${data.equlist[i].totalOnlineEarn} <b>元</b></span></p>
			                ${iconStr}
			                <p class="d-name">设备名称：<span>${data.equlist[i].remark}</span><i class="editName">编辑</i></p>
			                <p class="d-name">小区名称：<span>${data.equlist[i].name}</span><i class="editAreaName">编辑</i></p>
			                ${portStatusStr}
			                <div class="bottomBut">
				                <a href="${ztHref}" class="statisticBut ${ztDisable}">状态</a>
		                        <a href="${manageHref}" class="manageBut ${mgDisable}">管理</a>
		                        <a href="${remoteHref}" class="remoteBut ${ycDisable}">远程</a>
		                        <a href="javascript:;" class="wifi ${bgGreen}"><i class="iconfont ${wifiIcon}"></i></a>
		                        <a href="${tjHref}">统计</a>
		                        <a href="${orderHref}">订单</a>
			                </div>
			            </li>` //这个是带active的
				}else{
					orderHref= '/equipment/codetotrade?souce=2&code='+ data.equlist[i].code
					str= `<li>
		                <p>设备编号：<span>${data.equlist[i].code}</span></p>
		                <p>线上收益：<span>${data.equlist[i].totalOnlineEarn} <b>元</b></span></p>
		                ${iconStr}
		                <p class="d-name">设备名称：<span>${data.equlist[i].remark}</span><i class="editName">编辑</i></p>
		                <p class="d-name">小区名称：<span>${data.equlist[i].name}</span><i class="editAreaName">编辑</i></p>
		                ${portStatusStr}
		                <div class="bottomBut">
			                <a href="javascript:;" class="statisticBut">状态</a>
	                        <a href="javascript:;" class="manageBut">管理</a>
	                        <a href="javascript:;" class="remoteBut">远程</a>
	                        <a href="javascript:;" class="wifi ${bgGreen}"><i class="iconfont ${wifiIcon}"></i></a>
	                        <a href="${tjHref}">统计</a>
	                        <a href="${orderHref}">订单</a>
		                </div>
	                </li>` //这个是不带active的
				}*/
				 htmlStr += str 
			}
			$(frame).append($(htmlStr))
			 divObj.find('li').remove();
			 divObj.append($(frame))
			 if(data.equlist < 5){
				 divObj.find('.mui-pull-caption-nomore').text('没有更多数据了')
			 }else{
				 divObj.find('.mui-pull-caption-nomore').text('正在加载...')
			 }
		
	}



	$('.dm main ul').scroll(function(e){
		e= e || window.event
		// 获取当前ul的高度
		var ulWidth= $(this).height()
		var divLi= $(this).find('.divLi').height()
		var index= $(this).index() //判断当前滑动ul的索引 index
		var rate= $(this).scrollTop() / (divLi-ulWidth)
//		console.log(isHaveData1,isHaveData2,isHaveData3)
		if(rate*10 >= 10){
			// 发送ajax请求
			if(index=== 0 && !flag1){
				if(isHaveData1){
					sendAjax(index)
					$('.dm .ul1').find('.mui-pull-caption-nomore').text('正在加载...')
				}else{
					$('.dm .ul1').find('.mui-pull-caption-nomore').text('没有更多数据')
				}
				
			}
			if(index=== 1 && !flag2){
				if(isHaveData2){
					sendAjax(index)
					$('.dm .ul2').find('.mui-pull-caption-nomore').text('正在加载...')
				}else{
					$('.dm .ul2').find('.mui-pull-caption-nomore').text('没有更多数据')
				}
			}
			if(index=== 2 && !flag3){
				if(isHaveData3){
					sendAjax(index)
					$('.dm .ul3').find('.mui-pull-caption-nomore').text('正在加载...')
				}else{
					$('.dm .ul3').find('.mui-pull-caption-nomore').text('没有更多数据')
				}
			}
			
		}
	})

	function sendAjax(from){ //这个是滚动发送ajax
		var ulObj= null
		var divObj= null
		var equnum= 0 //偏移量
			switch(from){
				case 0: flag1= true ; divObj= $('.dm main .divLi').eq(0);  equnum= equnum1;  break;
				case 1: flag2= true ; divObj= $('.dm main .divLi').eq(1);  equnum= equnum2; break;
				case 2: flag3= true ; divObj= $('.dm main .divLi').eq(2);  equnum= equnum3; break;
			}
		querynum= $('.dm header li.active').index()+1
			$.ajax({
				url: '/equipment/getAjaxEquList',
				data: {
					uid : uid,
					equnum : equnum,
					querynum : querynum,
					source : source,
					parameter : parameter
				},
				type: 'POST',
				dataType : "json",
				success: function(data){
//					$('#showTip').fadeOut() //关掉提示文字
					// 这里判断res的数据是为空，为空的话提示没有更多数据，后面不执行
					// $('#showTip').find('.mui-pull-caption').text('暂无更多数据...')
					// $('#showTip').fadeIn()
					var data= handleData(data)
					switch(from){
						case 0: isHaveData1= data.equlist.length<5 ? false : true ;  break;
						case 1: isHaveData2= data.equlist.length<5 ? false : true ; break;
						case 2: isHaveData3= data.equlist.length<5 ? false : true ; break;
					}

					var frame= document.createDocumentFragment()
					var htmlStr= ''
						
					for(var i=0; i< data.equlist.length; i++){
						var deviceBindOwn= data.equlist[i].classify == 1 ? true : false //当前设备是否属于自己，（1为自己，2不是自己）
						var portStatusStr= ''
						var str= ''
						var iconStr2= '' //投币字符串
						if (data.equlist[i].hardversion != '03' && data.equlist[i].hardversion != '04') {
							if(data.equlist[i].state == 1){
								portStatusStr= '<p>端口状态： <span class="span_right_fr5">空闲('+data.equlist[i].freenum+')</span><span class="span_right_use5">使用('+data.equlist[i].usenum+')</span><span class="span_right_fi5">故障('+data.equlist[i].failnum+')</span></p>'
							}
						}
						if(showincoins != 2){
							iconStr2= '<p>投币收益：<span>'+data.equlist[i].totalCoinsEarn+'<b>元</b></span></p>'
						}
						str= 
							`<li class="${deviceBindOwn ? '' : 'active'}">
								<p>设备编号：<span>${data.equlist[i].code}</span></p>
						        <p>线上收益：<span>${data.equlist[i].totalOnlineEarn} <b>元</b></span></p>
						        ${iconStr2}
						        <p class="d-name">设备名称：<span>${data.equlist[i].remark}</span><i class="editName">编辑</i></p>
						        <p class="d-name">小区名称：<span>${data.equlist[i].name}</span><i class="editAreaName">编辑</i></p>
						        ${portStatusStr}
						        <div class="bottomBut">
						            <a href="/merchant/charge?code=${data.equlist[i].code}" class="statisticBut ${(data.equlist[i].hardversion != '03' && data.equlist[i].hardversion != '04' && deviceBindOwn && data.equlist[i].device_type != 2) ? '' : 'disable'}">状态</a>
						            <a href="/equipment/equinfo?code=${data.equlist[i].code}" class="manageBut ${deviceBindOwn ? '' : 'disable'}">管理</a>
						            <a href="/merchant/remotechargechoose?code=${data.equlist[i].code}" class="remoteBut ${(deviceBindOwn && data.equlist[i].state == 1 && data.equlist[i].device_type != 2) ? '' : 'disable'}">远程</a>
						            <a href="javascript:;" class="wifi ${data.equlist[i].state == 1 ? 'bgGreen' : ''} ${data.equlist[i].device_type != 2 ? '' : 'disable'}">
						            	<i class="iconfont ${(data.equlist[i].csq >= 0 && data.equlist[i].csq <= 5) ? 'icon-WIFIxinhao-ji3' : (data.equlist[i].csq > 5 && data.equlist[i].csq <= 10) ? 'icon-WIFIxinhao-ji2' : (data.equlist[i].csq > 10 && data.equlist[i].csq <= 20) ? 'icon-WIFIxinhao-ji1' : 'icon-WIFIxinhao-ji' }"></i>
						            </a>
						            <a href="/equipment/codeDayEarn?code=${data.equlist[i].code}">统计</a>
						            <a href="/equipment/codetotrade?souce=${deviceBindOwn ? 1 : 2}&amp;code=${data.equlist[i].code}">订单</a>
						        </div>
						    </li>`
							
							/*if(parseInt(uid) != parseInt(data.equlist[i].manid)){
								var ztHref= ''
								var remoteHref= ''
									
								if (data.equlist[i].hardversion != '03' && data.equlist[i].hardversion != '04') {
									ztHref= '/merchant/charge?code='+ data.equlist[i].code
								} else {
									ztHref= 'javascript:;'
									ztDisable= 'disable'
								}
								
								if (data.equlist[i].state == 1) {
									remoteHref= '/merchant/remotechargechoose?code='+data.equlist[i].code
								} else {
									remoteHref= 'javascript:;'
									ycDisable= 'disable'
								}
								var manageHref= '/equipment/equinfo?code='+ data.equlist[i].code
									orderHref= '/equipment/codetotrade?souce=1&code=' + data.equlist[i].code
								str= `<li class="active">
				                <p>设备编号：<span>${data.equlist[i].code}</span></p>
				                <p>线上收益：<span>${data.equlist[i].totalOnlineEarn} <b>元</b></span></p>
				                ${iconStr2}
				                <p class="d-name">设备名称：<span>${data.equlist[i].remark}</span><i class="editName">编辑</i></p>
				                <p class="d-name">小区名称：<span>${data.equlist[i].name}</span><i class="editAreaName">编辑</i></p>
				                ${portStatusStr}
				                <div class="bottomBut">
					                <a href="${ztHref}" class="statisticBut ${ztDisable}">状态</a>
			                        <a href="${manageHref}" class="manageBut">管理</a>
			                        <a href="${remoteHref}" class="remoteBut ${ycDisable}">远程</a>
			                        <a href="javascript:;" class="wifi ${wifiColor}"><i class="iconfont ${wifiIcon}"></i></a>
			                        <a href="${tjHref}">统计</a>
			                        <a href="${orderHref}">订单</a>
				                </div>
				            </li>` //这个是带active的
							}else{
								orderHref= '/equipment/codetotrade?souce=2&code='+ data.equlist[i].code
								str= `<li>
					                <p>设备编号：<span>${data.equlist[i].code}</span></p>
					                <p>线上收益：<span>${data.equlist[i].totalOnlineEarn} <b>元</b></span></p>
					                ${iconStr2}
					                <p class="d-name">设备名称：<span>${data.equlist[i].remark}</span><i class="editName">编辑</i></p>
					                <p class="d-name">小区名称：<span>${data.equlist[i].name}</span><i class="editAreaName">编辑</i></p>
					                ${portStatusStr}
					                <div class="bottomBut">
						                <a href="javascript:;" class="statisticBut">状态</a>
				                        <a href="javascript:;" class="manageBut">管理</a>
				                        <a href="javascript:;" class="remoteBut">远程</a>
				                        <a href="javascript:;" class="wifi ${wifiColor}"><i class="iconfont ${wifiIcon}"></i></a>
				                        <a href="${tjHref}">统计</a>
				                        <a href="${orderHref}">订单</a>
					                </div>
				                </li>` //这个是不带active的
							}*/
						htmlStr += str         
					}
					$(frame).append($(htmlStr))
					divObj.append($(frame))
					switch(from){ //打开开关，可以触发
						case 0: flag1= false ; equnum1= data.equnum; break; 
						case 1: flag2= false ; equnum2= data.equnum; break;
						case 2: flag3= false ; equnum3= data.equnum; break;
					}
//					console.log(divObj)
					 if(data.equlist < 5){
						 divObj.parent().find('.mui-pull-caption-nomore').text('没有更多数据了')
					 }else{
						 divObj.parent().find('.mui-pull-caption-nomore').text('正在加载...')
					 }
				}
			})
	
	}

	 var picker = new mui.PopPicker();
	 picker.setData([
		 	{value:'1',text:'设备编号'},
		 	{value:'2',text:'设备名称'},
		 	{value:'3',text:'小区名称'}
	 	]);
	$('.deviceName').click(function(){
		picker.show(function (selectItems) {
		    // console.log(selectItems[0].text);
		    // console.log(selectItems[0].value);//zz 
		   		 switch(parseInt(selectItems[0].value)){ //打开开关，可以触发
						case 1: source= 1 ; break; 
						case 2: source= 2 ; break;
						case 3: source= 3 ; break;
					}
		    $('.deviceName .name').text(selectItems[0].text)
	  	})
	})
	
	//点击编辑设备的名称

	var picker2 = new mui.PopPicker();
	//监听选中的小区	
	
	$('body').on('click',function(e){
		e= e || window.event
		var target= e.target || e.srcElement
		if($(target).hasClass('editName')){
			var code= $(target).parents('li').find('p').eq(0).find('span').text()
			var deviceName= $(target).parent().find('span').text().trim()
			mui.prompt('编辑'+code+'的设备名称','请输入设备名','提示',['取消','提交'],function(options){
				var name= options.value.trim()
				if(options.index===1){
					$.ajax({
						url: '/equipment/redactEquipentName',
						type: 'post',
						data: {
							code: code,
							name: name
						},
						success: function(res){
							if(res.code === 200){
								$(target).parent().find('span').text(name)
							}
							mui.toast(res.message)
						},
						error: function(){
							mui.toast('修改失败！')
						}
					})
				}
			})
			$('.mui-popup-input input').val(deviceName)[0].blur()
//			document.querySelector('.mui-popup-input input').value=deviceName
		}else if($(target).hasClass('editAreaName')){
			handleSelectArea(target)
		}
	})
	
	function handleSelectArea(target){
		var code= $(target).parents('li').find('p').eq(0).find('span').text()
		$.ajax({
			url: '/equipment/getEquipAidInfo',
			type: 'post',
			data: {
				codenum: code
			},
			success: function(res){
				if(res.code === 200){
					picker2.setData(fmtAreaData(res.arealist));
					if(res.nowcodeaid){
						picker2.pickers[0].setSelectedValue(res.nowcodeaid, 0);
					}else{
						picker2.pickers[0].setSelectedIndex(0);
					}
					picker2.show(function (selectItems) {
					    var id= selectItems[0].value
					    var name= selectItems[0].text
					    $.ajax({
					    	url: '/equipment/updataEquipAidInfo',
					    	data: {
					    		aid: id,
					    		codenum: code
					    	},
					    	success: function(res){
					    		if(res.code == 200){
					    			console.log('id',id)
					    			if(id == 0){
					    				$(target).parents('.d-name').find('span').text('')
					    				mui.toast('设备未指定小区成功')
					    			}else{
					    				$(target).parents('.d-name').find('span').text(name)
					    				mui.toast('设备绑定小区成功')
					    			}
					    			setSessionStroageFn()
					    		}else{
					    			mui.toast(res.message)
					    		}
					    	},
					    	error: function(){
					    		mui.toast('访问出错')
					    	}
					    })
					})
				}
			},
			error: function(){
				mui.toast('访问出错')
			}
		})
	}
	
	
	function handleData(data){
		var obj= {}
		obj.equlist=[]
		obj.equnum=data.equnum
		obj.listnum= data.listnum
		for(var i=0 ; i< data.equlist.length; i++){
			var obj2= {}
			obj2.code= data.equlist[i].code
			obj2.ccid= data.equlist[i].ccid
			obj2.totalCoinsEarn= data.equlist[i].totalCoinsEarn
			obj2.deviceType= data.equlist[i].deviceType
			obj2.hardversion= data.equlist[i].hardversion
			obj2.hardversionnum= data.equlist[i].hardversionnum
			obj2.name= data.equlist[i].name == null ? '' : data.equlist[i].name
			obj2.remark= data.equlist[i].remark == null ? '' : data.equlist[i].remark
			var tMoney= data.equlist[i].totalOnlineEarn == null ? 0 : data.equlist[i].totalOnlineEarn
			obj2.totalOnlineEarn= handleTotalMoney(tMoney)
			obj2.manid= data.equlist[i].manid
			obj2.state= data.equlist[i].state
			obj2.csq= data.equlist[i].csq
			obj2.freenum= data.equlist[i].freenum
			obj2.failnum= data.equlist[i].failnum
			obj2.usenum= data.equlist[i].usenum
			obj2.classify= data.equlist[i].classify
			obj2.device_type= data.equlist[i].device_type
			obj.equlist.push(obj2)
		}
		return obj
	}
	
	function fmtAreaData(areaList){
		var arr= [{value: 0,text: '不绑定小区'}]
		for(var i=0; i< areaList.length; i++){
			var obj= {
				value: areaList[i].id,
				text: areaList[i].name,
			}
			arr.push(obj)
		}
		return arr
	}
	
	function handleTotalMoney(momey){
		var moneyStr= momey.toString()
		var monStr
		var reg1= /(-)?\d+\.\d+/g 
		if(reg1.test(moneyStr)){
			moneyStr += '00'
			var reg=/^(-)?\d+\.\d{2}/ 
				monStr= moneyStr.match(reg)[0]
		}else{
			monStr= moneyStr+'.00'
		}
		return monStr
		
	}
	
	function setSessionStroageFn(){
		/*获取content的innerHTML,保存到本地*/
		var ul1Str= $('main .content .ul1').html()
		var ul2Str= $('main .content .ul2').html()
		var ul3Str= $('main .content .ul3').html()
		 	sessionStorage.setItem('ulStr',JSON.stringify({
		 		ul1Str: ul1Str,
		 		ul2Str: ul2Str,
		 		ul3Str: ul3Str
		 	}))
		 	sessionStorage.setItem('data',JSON.stringify({
		 		flag1:flag1,
		 		flag2:flag2,
		 		flag3: flag3,
		 		equnum1: equnum1,
		 		equnum2: equnum2,
		 		equnum3: equnum3,
		 		querynum: querynum,
		 		parameter: parameter,
		 		source: source,
		 		isHaveData1: isHaveData1,
		 		isHaveData2: isHaveData2,
		 		isHaveData3: isHaveData3
		 	}))
			sessionStorage.setItem('urlFrom',JSON.stringify({urlFrom:urlFrom,scrollTop:scrollTop}))
	}
	 

})