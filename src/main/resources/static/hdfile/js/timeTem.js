$(function(){
	window.onpageshow = function(event) { //监听页面返回
		if (event.persisted || window.performance &&
				window.performance.navigation.type == 2){
				window.location.reload()
			}
	};
	var screenWidth = document.documentElement.offsetWidth ||  document.body.offsetWidth
	var fontSize= screenWidth/16
	var StyleStr= '<style>html{ font-size: '+fontSize+'px } </style>'
	$('head').children().eq(0).before(StyleStr)
	var timeTem= new TimeTem()

	$('body').on('tap',function(e){
		e= e || window.event
		var target= e.target || e.srcElement
		if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('deleteItem')){ //删除子元素
			timeTem.deleteItem(target)
		}else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('addItem')){ //添加子元素
			timeTem.addItem(target)
		}else if($(target).hasClass('.isSupCha') || $(target).parents('.isSupCha').length > 0){ //点击触发临时充电按钮
			var isalipay= $('.div_list [data-name="isalipay"]')
			var ref= $('.div_list [data-name="ref"]')
			var hasDisabled = $('.div_list [data-name="temporaryc"]').hasClass('mui-disabled')
			if(hasDisabled){ return }
			setTimeout(function(){
				var temporarycVal= $('.div_list [data-name="temporaryc"]').hasClass('mui-active') ? 1 : 2 //是否支持临时充电，1是，2否
				if(temporarycVal == 1){
					$(target).parents('.div_list').find('.mid_mask').hide()	
					timeTem.switchPack({
						el: '[data-name="isalipay"]',
						disabled: false
					})
					timeTem.switchPack({
						el: '[data-name="ref"]',
						disabled: false
					})
				}else if(temporarycVal == 2){
					$(target).parents('.div_list').find('.mid_mask').show()
					timeTem.switchPack({
						el: '[data-name="isalipay"]',
						active: false,
						disabled: true
					})
					timeTem.switchPack({
						el: '[data-name="ref"]',
						active: false,
						disabled: true
					})
				}
			})
		}else if($(target).hasClass('.isSupCha') || $(target).hasClass('saveTem')){ //点击保存按钮
			var saveObj= new TimeTem().getTemData(target)
			$.ajax({
				url: '/wctemplate/insertAmendTemp',
				type: 'post',
				data: {paratem : JSON.stringify(saveObj)},
				success: function(res){
					if(res.code === 200){
						mui.toast('保存成功！')
						// 成功之后将获取的数据，重新赋值给data-value 或 data-checked
						var $div_list= $(target).parents('body').find('.div_list')
						$div_list.find('input').each(function(i,item){ 
							$(item).attr('data-value',$(item).val().trim())
						})
						$div_list.find('.mui-switch').each(function(i,item){ 
							if($(item).attr('data-checked')){ //这个是判断是否是单选框，单选框执行
								$(item).attr('data-checked',$(item).hasClass('mui-active'))
							}
						})
					}else{
						mui.toast('保存失败！')
					}
				},
				error: function(err){
					mui.toast('保存错误！')
				}
			})
			
		}else if($(target).hasClass('.isSupCha') || $(target).hasClass('goBack')){ //返回
			var from= $('body').attr('data-from').trim()
			if(from == 1){ //如果是新增模板则不判断就返回
				return window.history.go(-1)
			}
			
			var isResetVal= false //是否修改了
			var $div_list= $(target).parents('body').find('.div_list')
			$div_list.find('input').each(function(i,item){ 
				isResetVal= $(item).attr('data-value') == $(item).val().trim() ? false : true
				if(isResetVal){
					console.log(item)
					return false
				}
			})
			if(!isResetVal){
				$div_list.find('.mui-switch').each(function(i,item){
					isResetVal= eval($(item).attr('data-checked')) == $(item).hasClass('mui-active') ? false : true
					if(isResetVal){
						console.log(item)
						return false
					}
				})
			}
			if(isResetVal){ //被修改了
				mui.toast('请先保存修改的模板再退出！')
			}else{
//				var code= $('#code').val().trim()
//				window.location.replace('/merchant/devicetemdata?code='+code)
				window.history.go(-1)
			}
		}else if($(target).hasClass('.isSupCha') || $(target).hasClass('cancelSave')){ //取消保存
			var $div_list= $(target).parents('.div_list')
			$div_list.find('input').each(function(i,item){ 
				if($(item).attr('data-checked')){ //这个是判断是否是单选框，单选框执行
					$(item).prop('checked',eval($(item).attr('data-checked')))
				}else {

					$(item).val($(item).attr('data-value').trim())
				}
			})
			if($div_list.find('.isSupCha input[name="temporaryc"]:checked').val().trim() == 1 ){
				$div_list.find('.mid_mask').hide()
			}else{
				$div_list.find('.mid_mask').show()
			}
		}else if($(target).hasClass('toggleShow')){ //切换收费说明，显示/隐藏
			if($(target).hasClass('mui-icon-arrowdown')){
				$(target).parent().css({height: 'auto'})
				$(target).removeClass('mui-icon-arrowdown')
				$(target).addClass('mui-icon-arrowup')
			}else{
				var lineHeight= $(target).parent().css('lineHeight')
				$(target).parent().css({height: lineHeight})
				$(target).removeClass('mui-icon-arrowup')
				$(target).addClass('mui-icon-arrowdown')
			}
		}else if($(target).hasClass('edit-chargeinfo')){ //切换收费说明，显示/隐藏
			if($(target).hasClass('mui-icon-compose')){
				$('.textarea-box').slideDown(300)
				$(target).removeClass('mui-icon-compose').addClass('mui-icon-closeempty').css({fontSize: '1rem'})
			}else{
			/*	var lineHeight= $(target).parent().css('lineHeight')*/
				/*$(target).parent().css({height: lineHeight})*/
				$(target).removeClass('mui-icon-closeempty').addClass('mui-icon-compose').css({fontSize: '0.7826rem'})
				$('.textarea-box').slideUp(300)
			}
		}else if($(target).hasClass('addNewTem')){ //添加主模板
			var code= $('#code').val().trim()
			var newData= new TimeTem().getTemData(target)
			if(newData.temName.trim() == '' || newData.temName== null){
				return mui.toast('请输入模板名称！')
			}
			$.ajax({
				url: '/wctemplate/insertAmendTemp',
				type: 'post',
				data: {paratem : JSON.stringify(newData)},
				success: function(res){
					if(res.code === 200){
						mui.alert('新增成功！',function(){
							window.location.replace('/merchant/timetemdata?code='+code+'&tempid='+res.tempid)
						})
					}else{
						mui.toast('新增成功！')
					}
				},
				error: function(err){
					mui.toast('新增错误！')
				}
			})
			// 发送请求，生成新的模板
		}else if($(target).hasClass('delTem')){ //删除主模板
			var $div_list= $(target).parents('body').find('.div_list').eq(0)
			var temid= $div_list.attr('data-id').trim()
			mui.confirm('确认删除此模板吗？',function(options){
				if(options.index){
					$.ajax({
						url: '/wctemplate/deleteMainDeviceTemp',
						data: {id: temid},
						type: 'post',
						success: function(res){
							if(res.code === 200){
								mui.alert('删除成功！',function(){
									window.history.go(-1)
								})
							}else{
								mui.toast('删除模板失败！')
							}
						},
						error(err){
							mui.toast('删除模板出错！')
						}
					})
				}
			}) //confirm
		}else if($(target).hasClass('prevScan')){ //预览界面
			mui.alert('确认预览模板吗？',function(){
				var saveObj= new TimeTem().getTemData(target)
				$.ajax({
					url: '/wctemplate/insertAmendTemp',
					type: 'post',
					data: {paratem : JSON.stringify(saveObj)},
					success: function(res){
						if(res.code === 200){
							// 成功之后将获取的数据，重新赋值给data-value 或 data-checked
							var $div_list= $(target).parents('body').find('.div_list')
							$div_list.find('input').each(function(i,item){ 
								$(item).attr('data-value',$(item).val().trim())
							})
							$div_list.find('.mui-switch').each(function(i,item){ 
								if($(item).attr('data-checked')){ //这个是判断是否是单选框，单选框执行
									$(item).attr('data-checked',$(item).hasClass('mui-active'))
								}
							})
							$('#paratem').val(JSON.stringify(saveObj));
							$('#prevForm').submit()
						}else{
							mui.toast('保存失败！')
						}
					},
					error: function(err){
						mui.toast('保存错误！')
					}
				})
				
			})
		}
			
	})


	// 时间模板的构造函数
	function TimeTem(){
		this.checkHasLiEle= function(addPar,from){ //检查添加一行上面时候有li元素 addPar是button.addItem的父元素的jq对象, from是来自哪一个
			if(from === 1){
				if(addPar.prev().prop('nodeName').toLowerCase() !== 'li'){
					return {
						money: 0.25,
						powerStart: 0,
						powerEnd: 200
					}
				}
				var $li= addPar.prev()
				var lastMoney= parseFloat($li.find('.everymoney').val())
				var lastPowerStart= parseFloat($li.find('.powerstart').val())
				var lastPowerEnd= parseFloat($li.find('.powerend').val())
				var rate1= lastPowerEnd-lastPowerStart //这个是获取功率间隔
				var newPowerEnd= Math.round((lastPowerEnd+rate1)*10)/10
				var rate2= lastMoney/lastPowerEnd //这个是1W多少钱
				var newMoney= Math.round((rate1*rate2+lastMoney)*100)/100//这个新收费钱数
				return {
					money: newMoney,
					powerStart: lastPowerEnd,
					powerEnd: newPowerEnd
				}
			}else if(from === 2){
				if(addPar.prev().prop('nodeName').toLowerCase() !== 'li'){
					return {
						showTitle: '1小时',
						chargeTime: 60
					}
				}
				var $li= addPar.prev()
				var lastShowTitle= $li.find('.showtitle').val().trim()
				var lastChargeTime= parseFloat($li.find('.chargetime').val())

				var newChargeTime= lastChargeTime+60
				var newShowTitle= (newChargeTime/60)+'小时'
				return {
					showTitle: newShowTitle,
					chargeTime: newChargeTime
				}
			}else if(from === 3){
				if(addPar.prev().prop('nodeName').toLowerCase() !== 'li'){
					return {
						showMoney: '1元',
						money: 1
					}
				}
				var $li= addPar.prev()
				var lastShowMoney= $li.find('.showmoney').val().trim()
				var lastMoney= parseFloat($li.find('.money').val())
				var newMoney= lastMoney+1
				var newShowMoney= newMoney+'元'
				
				return {
					showMoney: newShowMoney,
					money: newMoney
				}
			}
		}
	}
	TimeTem.prototype.deleteItem= function(el){
		// 发送ajxa
		var ctmpid= $(el).attr('data-id').trim() //子模板id
		var mid1= $(el).parents('.mid1')
		var chargeStr= ''
		var isUpdateChargeInfo
		var tempid= $(el).parents('.div_list').attr('data-id')
		if(mid1.length > 0){ //删除的第mid1下的子模板
			isUpdateChargeInfo= 1
			var $li= mid1.find('.mui-table-view-cell')
			$li.each(function(i,item){
				var id= $(item).find('.deleteItem').attr('data-id').trim()
				if($(item).index() != $(el).parents('.mui-table-view-cell').index()){
					var everymoney= $(item).find('.everymoney').val().trim()
					var powerstart= $(item).find('.powerstart').val().trim()
					var powerend= $(item).find('.powerend').val().trim()
					chargeStr+= everymoney+'元/小时，功率区间：'+powerstart+'-'+powerend+'瓦 \n'
				}
			})
		}
		mui.confirm('确认删除吗？',function(options){
			if(options.index){
				$.ajax({
					url: '/wctemplate/deleteVDeviceTem',
					data: {id:ctmpid,tempid: tempid,isUpdateChargeInfo: isUpdateChargeInfo,chargeInfo: chargeStr },
					type: 'post',
					success: function(res){
						if(res.code === 200 || res.code === 210){ 
							var $mid= $(el).parents('.mid') //删除之前先存储一下mid
							$(el).parent().remove()
							if($mid.hasClass('mid1')){ //删除的是收费标准的子元素，所以收费说明也需要变化
								var info= ''
								$mid.find('li.mui-table-view-cell').each(function(i,item){
									var everymoney= $(item).find('.everymoney').val().trim()
									var powerstart= $(item).find('.powerstart').val().trim()
									var powerend= $(item).find('.powerend').val().trim()
									info += `${everymoney}元/小时，功率区间：${powerstart}-${powerend}瓦</br>` 
								})
								$mid.parents('.div_list').find('.chargeins .ins').html(info)
								$mid.parents('.div_list').find('.textarea-box textarea').val(chargeStr.trim())
							}
							if(res.code === 200){
								mui.toast('删除成功！')
							}
						}else{
							mui.toast('删除失败！')
						}
					},
					error: function(err){
						mui.toast('删除出错！')
					}
				})
			}
		})
	}
	TimeTem.prototype.addItem= function(el){
		// 发送ajax
		var addPar= $(el).parents('.bottom_bun')
		var from= $(el).parents('.mid').hasClass('mid1') ? 1 :  $(el).parents('.mid').hasClass('mid2') ? 2 : 3
		var str= ''
		var newItem= this.checkHasLiEle(addPar,from)
		var parid= $(el).parents('.div_list').attr('data-id').trim()
		var data= {}
		if(from === 1){
			var chargeStr= ''
			var $li= $(el).parents('.div_list').find('.mid1 .mui-table-view-cell')
			$li.each(function(i,item){
				var everymoney= $(item).find('.everymoney').val().trim()
				var powerstart= $(item).find('.powerstart').val().trim()
				var powerend= $(item).find('.powerend').val().trim()
				chargeStr+= everymoney+'元/小时，功率区间：'+powerstart+'-'+powerend+'瓦\n'
			})
			/*chargeStr+='功率区间：'+newItem.powerStart+'-'+newItem.powerEnd+'瓦，每小时收费：'+newItem.money+'元'*/
			chargeStr+= newItem.money+'元/小时，功率区间：'+newItem.powerStart+'-'+newItem.powerEnd+'瓦'
			data= {
				parid: parid,
				type: from,
				money: newItem.money,
				common1: newItem.powerStart,
				common2: newItem.powerEnd,
				isUpdateChargeInfo: 1,
				chargeInfo: chargeStr
			}
		}else if(from === 2){
			data= {
				parid: parid,
				type: from,
				name: newItem.showTitle,
				chargetime: newItem.chargeTime
			}
		}else if(from === 3){
			data= {
				parid: parid,
				type: from,
				name: newItem.showMoney,
				money: newItem.money
			}
		}
		$.ajax({
			url: '/wctemplate/additionAssignTemp',
			data:data,
			type: 'post',
			success: function(res){
				if(res.code === 200){
					renderItem(newItem,res.template.id)
					mui.toast('添加成功！')
				}else if(res.code === 210){
					renderItem(newItem,-1)
				}else{
					mui.toast('添加失败！')
				}
				
			},
			error: function(err){
				mui.toast('添加出错！')
			}
		})

		//渲染頁面開始
		function renderItem(newItem,id){
			if(from === 1){ //收费标准
				str=`
					<li class="mui-table-view-cell">
							<div class="item">
								<p class="item_p">
									<span class="item_p_title">每小时：</span>
									<span class="item_span_inp">
										<input class="mui-numbox-input everymoney" data-value="${newItem.money}" value="${newItem.money}" type="number">
									</span>
								</p>
								<p class="item_p">
									<span class="item_p_title">功率区间：</span>
									<span class="item_span_inp c_inp">
										<input class="mui-numbox-input powerstart" data-value="${newItem.powerStart}" value="${newItem.powerStart}" type="number">~<input class="mui-numbox-input powerend" data-value="${newItem.powerEnd}" value="${newItem.powerEnd}" type="number">
									</span>
								</p>
							</div>
							<button class="mui-btn mui-btn-danger mui-icon mui-icon-trash deleteItem" data-id="${id}"></button>
					    </li>
				`
				var $ins= $(el).parents('.div_list').find('.chargeins .ins')
				var $textareaInfo= $(el).parents('.div_list').find('.textarea-box textarea')
				var strItem= `${newItem.money}元/小时，功率区间：${newItem.powerStart}-${newItem.powerEnd}瓦`
				$ins.html( $ins.html()+(strItem+'<br>'))
				$textareaInfo.val(($textareaInfo.val()+('\n'+strItem)).trim())
			}else if(from === 2){ //按照时间充电

				str=`
					<li class="mui-table-view-cell">
					    	<div class="item">
						    	<p class="item_p">
									<span class="item_p_title">显示名称：</span>
									<span class="item_span_inp">
										<input type="text" class="showtitle" data-value="${newItem.showTitle}" value="${newItem.showTitle}">
									</span>
								</p>
								<p class="item_p">
									<span class="item_p_title">充电时间：</span>
									<span class="item_span_inp">
										<input class="mui-numbox-input chargetime" data-value="${newItem.chargeTime}" value="${newItem.chargeTime}" type="number" />
									</span>
								</p>
							</div>
							<button class="mui-btn mui-btn-danger mui-icon mui-icon-trash deleteItem" data-id="${id}"></button>
					    </li>
				`
			}else if(from === 3){ //按照金额充电/临时充电
				str= `
					<li class="mui-table-view-cell">
					    	<div class="item">
						    	<p class="item_p">
									<span class="item_p_title">显示名称：</span>
									<span class="item_span_inp">
										<input type="text" class="showmoney" data-value="${newItem.showMoney}" value="${newItem.showMoney}">
									</span>
								</p>
								<p class="item_p">
									<span class="item_p_title">付款金额：</span>
									<span class="item_span_inp">
										<input class="mui-numbox-input money" data-value="${newItem.money}" value="${newItem.money}" type="number" />
									</span>
								</p>
							</div>
							<button class="mui-btn mui-btn-danger mui-icon mui-icon-trash deleteItem" data-id="${id}"></button>
					    </li>
				`
			}
			var newItem= $(str)
			newItem.insertBefore(addPar)
		}
		
		//渲染頁面結束	
	}
	TimeTem.prototype.getTemData= function(target){ //获取主模板数据，target是触发元素
		var $div_list= $(target).parents('body').find('.div_list').eq(0)
		var temid= $div_list.attr('data-id').trim()
		var temName= $div_list.find('.top p').eq(0).find('input').val().trim()
		var loName= $div_list.find('.top p').eq(1).find('input').val().trim()
		var tel= $div_list.find('.top p').eq(2).find('input').val().trim()
		var ifalipay= $('.div_list [data-name="isalipay"]').hasClass('mui-active') ? 1 : 2
		var temporarycVal= $('.div_list [data-name="temporaryc"]').hasClass('mui-active') ? 1 : 2
		var refVal= $('.div_list [data-name="ref"]').hasClass('mui-active') ? 1 : 2 
		var rank= $('.div_list .oc-time').val().trim() /*刷卡最大时间*/
		var id= $div_list.attr('data-id').trim()
		var chargeInfo= $div_list.find('.textarea-box textarea').val().trim()
		// 获取是否支持临时充电
		var saveObj= {}
		saveObj.id= id
		saveObj.temName= temName
		saveObj.loName= loName
		saveObj.tel= tel
		saveObj.ifalipay= ifalipay
		saveObj.chargeInfo= chargeInfo
		saveObj.temporaryc= temporarycVal
		saveObj.ref= refVal
		saveObj.rank= rank
		$div_list.find('.mid').each(function(i,item){
			if($(item).hasClass('mid1')){
				saveObj.mid1= []
				$(item).find('li.mui-table-view-cell').each(function(j,jtem){
					var everymoney= $(jtem).find('.everymoney').val().trim()
					var powerstart= $(jtem).find('.powerstart').val().trim()
					var powerend= $(jtem).find('.powerend').val().trim()
					var ctemid= $(jtem).find('.deleteItem').attr('data-id').trim()
					saveObj.mid1.push({
						everymoney: everymoney,
						powerstart: powerstart,
						powerend: powerend,
						id: ctemid
					})
				})
			}else if($(item).hasClass('mid2')){
				saveObj.mid2= []
				$(item).find('li.mui-table-view-cell').each(function(j,jtem){
					var showtitle= $(jtem).find('.showtitle').val().trim()
					var chargetime= $(jtem).find('.chargetime').val().trim()
					var ctemid= $(jtem).find('.deleteItem').attr('data-id').trim()
					saveObj.mid2.push({
						showtitle: showtitle,
						chargetime: chargetime,
						id: ctemid
					})
				})
//				// 获取是否支持临时充电
//				var temporarycVal= $(item).find('.isSupCha input[name="temporaryc"]:checked').val().trim()
//				saveObj.temporaryc= temporarycVal
//				var refVal= $(item).find('.ref_radio input[name="ref"]:checked').val().trim()
//				saveObj.ref= refVal

			}else if($(item).hasClass('mid3')){
				saveObj.mid3= []
				$(item).find('li.mui-table-view-cell').each(function(j,jtem){
					var showmoney= $(jtem).find('.showmoney').val().trim()
					var money= $(jtem).find('.money').val().trim()
					var ctemid= $(jtem).find('.deleteItem').attr('data-id').trim()
					saveObj.mid3.push({
						showmoney: showmoney,
						money: money,
						id: ctemid
					})
				})
			}
		})//遍历子元素结束
		return saveObj
	}
	/*
	 * {
	 * 	el: 'selector',
	 * 	active: true / false,
	 * 	disabled: true
	 * }
	 * 
	 * */
	TimeTem.prototype.switchPack= function(options){
		var isActive = $(options.el).hasClass("mui-active");
		if(typeof options.active == 'boolean'){
			if(isActive){
				if(!options.active){
					mui(options.el).switch().toggle();
				}
			}else{
				if(options.active){
					mui(options.el).switch().toggle();
				}
			}
		}
		if(typeof options.disabled == 'boolean'){
			if(options.disabled){
				$(options.el).addClass('mui-disabled')
			}else{
				$(options.el).removeClass('mui-disabled')
			}
		}
	}
})