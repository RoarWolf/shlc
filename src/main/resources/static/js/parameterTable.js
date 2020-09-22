
$(function(){
	var index
	$('.detail .mui-table-view .mui-table-view-cell').click(function(e){
		if($(this).index() != index){
			index= $(this).index()
			$('.detail .mui-table-view>div').slideUp(300)
			$(this).parent().children().eq($(this).index()+1).slideDown(300)
		}else{
			$(this).parent().children().eq($(this).index()+1).slideUp(300)
			index= undefined
		}
	})

	$('.setDataBut').click(function(e){ //设置数据
		var testResult= testVal()
		if(typeof testResult != 'string'){ //返回数据成功
			var code= $('#code').val().trim()
			testResult.code= code //将code添加到对象中
			sendAjaxSaveData(testResult)
		}
	})

	function testVal(){ //校验输入框中的数据，并返回
		  var flag= false //默认为false 当所有的元素都通过验证之后再变为rue
		  $('.detail input').each(function(i,item){
				if(!handleReg($(item))){
					var minVal= parseFloat($(item).attr('data-min'))
					var maxVal= parseFloat($(item).attr('data-max'))
					var textTxt= $(item).parent().parent().find('span').text()
					mui.toast(textTxt+'的范围是'+minVal+'~'+maxVal+'，请重新输入',{ duration:'2000', type:'div' }) 
					return false
				}
				if(i === $('.detail input').length-1){
					flag= true
				}
				
			})
			if(flag){
				// 这一步所有的input都已经通过验证了，然后需要获取是否显示电量的switch
					var elecTimeFirst= $("#isShowFirstPower").hasClass('mui-active') ? 1 : 0    //是否初始显示电量 
					/*发送ajax向后台传输数据,(获取每个input的数据)*/
					var spRecMon= $("#isBalanceRec").hasClass('mui-active') ? 1 : 0  //是否回收
					var spFullEmpty= $("#isPowerFailure").hasClass('mui-active') ? 1 : 0 //获取 是否断电自停
					//这一步要获取所有的input 的数据进行上传数据操作
					var coinMin= $('input[name="coinMin"]').val().trim()
					var cardMin= $('input[name="cardMin"]').val().trim()
					var coinElec= $('input[name="coinElec"]').val().trim()
					var cardElec= $('input[name="cardElec"]').val().trim()
					var cst= $('input[name="cst"]').val().trim()
					var fullPowerMin= $('input[name="fullPowerMin"]').val().trim()
					var fullChargeTime= $('input[name="fullChargeTime"]').val().trim()
					var powerMax1= $('input[name="powerMax1"]').val().trim()
					var powerMax2= $('input[name="powerMax2"]').val().trim()
					var powerMax3= $('input[name="powerMax3"]').val().trim()
					var powerMax4= $('input[name="powerMax4"]').val().trim()
					var powerTim2= $('input[name="powerTim2"]').val().trim()
					var powerTim3= $('input[name="powerTim3"]').val().trim()
					var powerTim4= $('input[name="powerTim4"]').val().trim()
					return {
						elecTimeFirst: elecTimeFirst,
						spRecMon: spRecMon,
						spFullEmpty: spFullEmpty,
						coinMin: coinMin,
						cardMin: cardMin,
						coinElec: coinElec,
						cardElec: cardElec,
						cst: cst,
						fullPowerMin: fullPowerMin,
						fullChargeTime: fullChargeTime,
						powerMax1: powerMax1,
						powerMax2: powerMax2,
						powerMax3: powerMax3,
						powerMax4: powerMax4,
						powerTim2: powerTim2,
						powerTim3: powerTim3,
						powerTim4: powerTim4,
					}
					
			}else{
				return '验证失败'
			} 
	} 
	
	function sendAjaxSaveData(data){
		$.bootstrapLoading.start({ loadingTips: "正在设置..." });
		$.ajax({
			url: '/equipment/setSysParam',
			data: data,
			success: function(res){
				if(res.wolfcode == 1001){
					mui.toast(res.wolfmsg);
				}else{
					mui.toast("设置成功");
				}
				
			},
			error: function(){
				mui.toast("设置出错");
			},
			complete: function (e) {
				console.log(e)
	            $.bootstrapLoading.end();
	        }
		})
	}
	
	//复用模板系统参数
	//点击模板复用
	var testResultObj
	var k= 0
	var isNowUse= false //是否正在复用
	var isCancel= false //是否取消以下请求
	var saveResult= {} //复用保存结果
	var ajaxObj= null // ajax对象，调用abort方法取消请求
	$('.more-device-par').on('click',function(e){
		k= 0
		saveResult= {}
		isCancel= false
		toggleUse(true) //点击的时候就设置t-select显示 t-status不显示
		testResultObj= testVal() //点击复用的时候，检测参数是否符合规则，不符合return
		if(typeof testResultObj == 'string'){ //返回数据失败
			return 
		}
		var code= $('#code').val().trim()
		var merid= $('#merid').val().trim()
		var hwVerson= $('#hwVerson').val().trim()
		$.ajax({
			url: '/equipment/searchDeviceData',
			type: 'post',
			data: {
				code: code,
				merid: merid,
				hwVerson: hwVerson
			},
			success: function(res){
				if(res.code === 200){
					if(res.devicelist.length > 0){
						$('.t-body tbody').html(renderUse(res.devicelist))
						$('.mask').fadeIn(300)
						$('.more-device-wapper').fadeIn(300)
					
					}else{
						mui.toast('暂无可复用的设备！')
					}
				}else{
					mui.toast(res.message)
				}
			},
			error: function(e){
				mui.toast('访问出错，请稍后重试！')
			}
		})
	})
	//点击遮罩层隐藏
	$('.mask').click(function(){
		if(isNowUse){ //说明此时在复用界面
			mui.confirm('当前正在设置状态，此时退出后面的设备将不再复用此系统参数！，是否继续复用？','提示',['取消','继续'],function(options){
				if(options.index == 0){
					isCancel= true
					ajaxObj && ajaxObj.abort()
				}
			})
		}else{
			$(this).fadeOut(300)
			$('.more-device-wapper').fadeOut(300,function(){
				$('.t-body').scrollTop(0)
			})
			$('.device-end-wapper').fadeOut(300,function(){
				$('.t-body').scrollTop(0)
			})
		}
		
	})
    // 点击取消点击复用
	$('#cancelUse').on('click',function(){
		$('.mask').fadeOut(300)
		$('.more-device-wapper').fadeOut(300,function(){
			$('.t-body').scrollTop(0)
		})
		$('.device-end-wapper').fadeOut(300,function(){
			$('.t-body').scrollTop(0)
		})
	})
	$('#handleUse').on('click',function(){
		var inputList= $('.t-body [name="checkbox1"]:checked')
		var deviceInfoList= []
		if(inputList.length <= 0){
			return mui.toast('请先选择要复用的设备！')
		}
		inputList.each(function(i,item){
			deviceInfoList.push(JSON.parse($(item).parents('tr').attr('data-item')))
		})
		$('.t-body tbody').html(renderUse(deviceInfoList))
		$('.t-body').scrollTop(0)
		toggleUse(false)
		useStart(deviceInfoList,k)
	})
	
	// 开始使用	
	function useStart(deviceInfoList,k){
		if(k < deviceInfoList.length){
			isNowUse= true
			var tStatus= $('.t-body tr .t-status').eq(k)
			tStatus.html('<span class="mui-icon mui-icon-spinner-cycle mui-spin t-loading"></span>')
			testResultObj.code= deviceInfoList[k].code
			// 每完成一个就向上滚动一格			
			$('.t-body').animate({
				scrollTop: k*$('.t-body tr').eq(0).height()
			},400)
			sendAjaxSaveDataByCodeList(testResultObj,function(flag){
				var str= flag ? '<span class="t-success">成功<span>' : '<span class="t-error">失败<span>'
				saveResult[deviceInfoList[k].code]= flag
				tStatus.html(str)
				k+=1
				useStart(deviceInfoList,k)
			})
			if(isCancel){ //如果设置了删除，ajax就取消请求
				ajaxObj.abort()
			}
		}else{
			isNowUse= false
			console.log('执行完了')
			handleUseEnd()
		}
	}
	
	// 当复用结束后执行该方法
	function handleUseEnd(){
		var successStr= ''
		var errorStr= ''
		for(var key in saveResult){
			if(saveResult.hasOwnProperty(key)){
				if(saveResult[key]){
					successStr += (key+',')
				}else{
					errorStr += (key+',')
				}
			}
		}
		$('.end-success').text(successStr.substr(0,successStr.length-1))
		$('.end-error').text(errorStr.substr(0,errorStr.length-1))
		$('.more-device-wapper').hide()
		$('.t-body').scrollTop(0)
		$('.device-end-wapper').fadeIn()
	}
	
	/* 切换复用参数是否显示，true select显示，false status显示 */
	function toggleUse(flag){
		if(flag){
			$('.t-select').show()
			$('.t-status').hide()
		}else{
			$('.t-status').show()
			$('.t-select').hide()
		}
	}
	// 渲染设备列表	
	function renderUse(devicelist){
		var htmlStr= ''
			for(var i=0; i< devicelist.length; i++){
				var areaName = devicelist[i].areaname || '— —'
				var deviceName= devicelist[i].devicename || '— —'
				htmlStr += 
					'<tr data-item='+JSON.stringify(devicelist[i])+'>\n'+
						'<td>'+devicelist[i].code+'</td>\n'+
						'<td>'+deviceName+'</td>\n'+
						'<td>'+areaName+'</td>\n'+
						'<td class="t-select">\n'+
							'<div class="mui-input-row mui-checkbox">\n'+
									'<input name="checkbox1" value="'+devicelist[i].code+'" type="checkbox">\n'+
							'</div>\n'+
						'</td>\n'+
						'<td class="t-status"></td>\n'+
					'</tr>'
			}
		return htmlStr
	}
	//复用系统参数	
	
	function handleReg(jqEle) { //判断输入的值是否满足匹配规则（最大值最小值）
		var val= parseFloat(jqEle.val().trim())
		var minVal= parseFloat(jqEle.attr('data-min'))
		var maxVal= parseFloat(jqEle.attr('data-max'))
		if(val >= minVal && val <= maxVal){
			return true
		}
		return false
	}
	
	// 复用参数ajax请求	
	function sendAjaxSaveDataByCodeList(data,fn){
		ajaxObj= $.ajax({
			url: '/equipment/setSysParam',
			data: data,
			complete: function (res) {
				console.log(res)
				if(res.statusText == 'success' && res.responseJSON.wolfcode != '1001'){ //成功
					fn && fn(true) //将成功的信息回调出去
				}else{ //失败
					fn && fn(false) //将失败的信息回调出去
				}
	      
	        }
		})
	}
	
})