$(function(){
	var $we= {
		$dialog2: $('#js_dialog_2'),
		$iosDialog2: $('#iosDialog2'),
		$mask: $('.weui-mask')
	}
	var $tb_code= $('.tb .code')
	var $tb_areaname= $('.tb .area-name')
	
	var htmlwidth = document.documentElement.clientWidth || document.body.clientWidth;
    var fontSize= htmlwidth/16 > 40 ? 40 : htmlwidth/16
    var styleStr= "<style>html { font-size: "+fontSize+"px !important;}</style>"
    $('head').children().eq(0).before(styleStr)
    
    //获取主模板id,模板所属设备的设备号code
    var id= $('.tem').attr('data-id').trim()
    var code= $('body').attr('data-code').trim()
    var isSysTem= $('.tem').attr('data-merchantid').trim() == 0 ? true : false
    isSysTem && $('.save-tem').remove() /*当系统模板时移除*/
	var temObject= new Template({id: id,code: code,isSysTem: isSysTem}) /*创建模板实例对象*/
	var picker = new mui.PopPicker();
	picker.setData([{text:'按照时间和电量最小',value:1},{text:'时间最小',value:2},{text:'电量最小',value:3}]);
	$('body').on('click',function(ev){
		ev= ev || window.event
		var target= ev.target || ev.srcElement
		if($(target).hasClass('box-logo') || $(target).hasClass('delete-text')){ //删除子模板
			if(temObject.isSysTem) return 
			return temObject.deleteChildTem($(target).parent())
		}
		if($(target).hasClass('add-cbtn')){ //添加子模板
			return temObject.addChildTem($(target).parent())
		}
		if($(target).hasClass('explain')){ //修改说明
			var type= $(target).hasClass('default-explain') ? 1 : 2 /*type: 1为默认说明，2为自动说明*/
			mui.confirm(type== 1 ? '确定设备默认说明吗？' : '确定设备自动说明吗？','提示',function(options){
				return temObject.changeChargeInfo(type,target)
			})
		}
		if($(target).hasClass('t-switch') || $(target).parents('.t-switch').length > 0){ //点击switch开关
			target= $(target).hasClass('t-switch') ? $(target) : $(target).parents('.t-switch')
			return temObject.changeSwitch(target)
		}
		if($(target).hasClass('refType')){ //修改退费标准
			if(temObject.isSysTem) return 
			return temObject.changeRefType(target)
		}
		if($(target).hasClass('save-tem')){ //保存模板
			// return temObject.changeRefType(target)
			var data= temObject.getData(target)
			return temObject.saveTemData(data)
		}
		if($(target).hasClass('add-tem')){ //新增主模板
			// return temObject.changeRefType(target)
			var data= temObject.getData(target)
			return temObject.saveTemData(data)
		}
		if($(target).hasClass('delete-tem')){ //删除主模板
			 return temObject.deleteTem()
		}
		if($(target).hasClass('go-back')){ //返回
			// return temObject.changeRefType(target)
			var type= $(target).attr('data-type').trim() //新增  1  保存 2
			if(temObject.checkHasSaveOrSetVal(1) && type== 2 ){
				mui.toast('请先保存后再退出！')
			}else{
				window.history.go(-1)
			}
		}
		if($(target).hasClass('prev-tem')){ //预览模板
			return temObject.prevTem()
		}
		if($(target).hasClass('delete-device')){ //删除设备使用此模板
			return temObject.deleteDeviceTem(target)
		}
		if($(target).hasClass('add-device')){ //添加设备
			  return temObject.getAndRenderDevice()
		}
		if($(target).hasClass('weui-mask') || $(target).hasClass('close-device')){ //隐藏弹框设备
	          $we.$mask.hide();
			  $we.$dialog2.removeClass('show')
	          $we.$dialog2.removeClass('weui-half-screen-dialog_show');
	          $('html').css({overflow: 'auto'})
		}
		if($(target).hasClass('submit-device')){ //开始复用此模板
			 return temObject.sendAjaxToUseTem()
		}
		if($(target).hasClass('sort-box') || $(target).parents('.sort-box').length >0){ //开始对设备进行排序
			var type= ($(target).hasClass('code') || $(target).parents('.code').length) ? 1 : 2 //1按照设备号排序  2 按照小区id排序
			 return temObject.sortDevice(type)
		}
		
	})
	
	// 主模板构造函数
	function Template(options){
		this.id= options.id //主模板id
		this.code= options.code //模板所属设备
		this.deviceList= [] //复用设备列表
		this.isSysTem= options.isSysTem //是否是系统模板
	}
	// 获取元素身上的data-id
	Template.prototype.getId= function(el){
		return $(el).attr('data-id').trim()
	}
	// 删除子模板
	Template.prototype.deleteChildTem= function(el){
		var cid= this.getId(el)
		mui.confirm('确定删除子模板吗？','提示',function(option){
			if(option.index === 1 ){
				$.ajax({
					url: '/merchant/deleteTemplateSonData',
					type: 'post',
					data: { id:cid },
					success: function(res){
						if(res.code == 200){
							$(el).parents('.box-list').remove()
							mui.toast('删除成功')
						}else{
							mui.toast(res.message)
						}
					},
					error: function(err){
						mui.toast('删除失败哦，请退出后重试！')
					}
				})
			}
		})
	}
	// 添加子模板
	Template.prototype.addChildTem= function(el){
		var $ul= el.prev()
		var $boxList= $ul.find('.box-list')
		console.log($boxList.eq($boxList.length-1))
		var inputData= {
			name: '1元4小时',
			price: 1,
			time: 240,
			power: 1
		}
		if($boxList.length > 0){
			// 上一次的数据
			var prevPrice= $boxList.eq($boxList.length-1).find('.c-price').val().trim()-0
			var prevTime= $boxList.eq($boxList.length-1).find('.c-time').val().trim()-0
			var prevPower= $boxList.eq($boxList.length-1).find('.c-power').val().trim()-0
			var rate1= prevTime / prevPrice //一元充多久
			var rate2= prevPower / prevPrice //一元充多少度电
			// 要生成的数据源
			var price= prevPrice+1
			var time= Math.ceil(price*rate1*10)/10
			var power= Math.ceil(price*rate2*10)/10
			var hour= time/60
			var name= price+'元'+hour+'小时'
			inputData= {
				name: name,
				price: price,
				time: time,
				power: power
			}
		}
		$.ajax({
			url: '/merchant/additionTempSon',
			type: 'post',
			data: {
				tempid: this.id,
				name: inputData.name,
				money: inputData.price,
				chargetime: inputData.time,
				quantity: inputData.power
			},
			success: function(res){
				if(res.code == 200){
					addChildRender(inputData,res.tempid)
				}else{
					mui.toast(res.message)
				}
			},
			error: function(err){
				mui.toast('添加出错，请退出重试！')
			}
		})
		
		// //渲染页面数据
		function addChildRender(inputData,id){
			var str= '<li class="box-list">\n'+
					'<div class="list-item">\n'+
						'<div class="list-item-left">显示名称</div>\n'+
						'<div class="list-item-right">\n'+
							'<input type="text" class="select-down c-name" value="'+inputData.name+'" data-val="'+inputData.name+'">\n'+
						'</div>\n'+
					'</div>\n'+
					'<div class="list-item">\n'+
						'<div class="list-item-left">充电价格</div>\n'+
						'<div class="list-item-right">\n'+
							'<input type="text" class="select-down c-price" value="'+inputData.price+'" data-val="'+inputData.price+'"><span class="unit">元</span>\n'+
						'</div>\n'+
					'</div>\n'+
					'<div class="list-item">\n'+
						'<div class="list-item-left">充电时间</div>\n'+
						'<div class="list-item-right">\n'+
							'<input type="text" class="select-down c-time" value="'+inputData.time+'" data-val="'+inputData.time+'" ><span class="unit">分钟</span>\n'+
						'</div>\n'+
					'</div>\n'+
					'<div class="list-item">\n'+
						'<div class="list-item-left">消耗电量</div>\n'+
						'<div class="list-item-right">\n'+
							'<input type="text" class="select-down c-power" value="'+inputData.power+'" data-val="'+inputData.power+'"><span class="unit">度</span>\n'+
						'</div>\n'+
					'</div>\n'+
					'<div class="delete-box" data-id="'+id+'">\n'+
						'<div class="box-logo"></div>\n'+
						'<div class="delete-text iconfont icon-lajixiang"></div>\n'+
					'</div>\n'+
				'</li>'
			$ul.append($(str))
		}
	}

	// 修改说明 type: 1 默认说明  2 自动说明
	Template.prototype.changeChargeInfo= function(type,el){
		if(type === 1){
			var str= '选择的充电时间为小功率电动车充电时间，仅供参考。\n'+
			'大功率电动车充电时间智能动态计算，以实际为准。'
			$(el).parents('.tem-header-list').find('textarea').val(str)
		}else if(type === 2){
			//向后台获取充电说明			
			$.ajax({
				url: '/merchant/editTempForInfo',
				type: 'post',
				data: {
					tempid: this.id,
					code: this.code,
				},
				success: function(res){
					if(res.code == 200){
						$(el).parents('.tem-header-list').find('textarea').val(res.resultinfo)
					}else{
						mui.toast(res.message)
					}
				},
				error: function(err){
					mui.toast('获取充电说明出错，请退出再试！')
				}
			})
		}
		
	}
	// 修改退费标准
	Template.prototype.changeRefType= function(el){
		console.log(el)
		var type= $(el).attr('data-type').trim()-0
		picker.pickers[0].setSelectedValue(type, 0)
		picker.show(function (selectItems) {
		    var item= selectItems[0]
		    $(el).val(item.text).attr('data-type',item.value)
		})
	}
	// 修改switch开关
	Template.prototype.changeSwitch= function(el){
		if(el.hasClass('mui-active')){//打开开关
			if(el.hasClass('isRef')){
				// $('.refType-box').slideDown(200)
				$('.refType-box').css({display: 'flex'})
			}
		}else{
			if(el.hasClass('isRef')){
				// $('.refType-box').slideUp(200)
				$('.refType-box').css({display: 'none'})
			}
		}
	}
	// 获取模板上的数据
	Template.prototype.getData= function(el){
		var temData= {}
		var id= $('.tem').attr('data-id').trim()
		var name= $('.tem .name').val().trim()
		var brankname= $('.tem .brankname').val().trim()
		var phone= $('.tem .phone').val().trim()
		var isRef= $('.tem .isRef').hasClass('mui-active') ? 1 : 2
		var refType= $('.tem .refType').attr('data-type').trim()
		var isWalletPay= $('.tem .isWalletPay').hasClass('mui-active') ? 1 : 2
		var isAliPay= $('.tem .isAliPay').hasClass('mui-active') ? 1 : 2
		var chargeinfo= $('.tem textarea').val().trim()

		var gather= []
		$('.tem .tem-main-box .box-list').map(function(index,item){
			var obj= {}
			var a= {
				name: 'name',
				price: 'money',
				time: 'chargetime',
				power: 'quantity',
			}
			var cid= $(item).find('.delete-box').attr('data-id').trim()
			obj.tempsonid= cid
			$(item).find('[class*=c-]').each(function(jndex,jtem){
				var key= $(jtem).attr('class').match(/c-(\w+)/)[1]
				obj[a[key]]= $(jtem).val().trim()
			})
			gather.push(obj) 
		})
		temData= {
			tempid: id,
			tempname: name,
			paysource: 1,
			type: 0,
			brandname: brankname,
			servicecall: phone,
			ispermit: isRef,
			returnnorm: refType,
			walletpay: isWalletPay,
			ifalipay: isAliPay,
			hintinfo: chargeinfo,
			tempson: gather
		}
		return temData
	}
	
	// 保存数据
	Template.prototype.saveTemData= function(data){
		var that= this
		$.ajax({
			url: '/merchant/additionAndEditTemp',
			type: 'post',
			data: {
				parameter: JSON.stringify(data)
			},
			success: function(res){
				if(res.code == 200){
					mui.toast('保存成功')
					if(data.tempid == -1){ //新增主模板
						var deviceNum= $('body').attr('data-code').trim()
						mui.alert('主模板新增成功',function(){
							window.location.replace(window.location.pathname+'?code='+deviceNum+'&tempid='+res.tempid)
						})
					}else{ //保存主模板信息
						that.checkHasSaveOrSetVal(2)
						mui.alert('主模板保存成功')
					}
					
				}else{
					mui.toast(res.message)
				}
			},
			error:  function(err){
				mui.toast('保存出错！')
			}
		})
	}
	//删除主模板
	Template.prototype.deleteTem= function(){
		
		mui.confirm('确定删除当前主模板吗?',function(options){
			if(options.index === 1){
				$.ajax({
					url: '/merchant/deleteTemplateData',
					type: 'post',
					data: {
						id: this.id
					},
					success: function(res){
						if(res.code === 200){
							mui.alert('删除成功',function(){
								window.history.go(-1)
							})
						}else{
							mui.toast(res.message)
						}
					},
					error: function(err){
						mui.toast('解除出错、请稍后重试！')
					}
				})
			}
		}.bind(this))
	}
	
	// type 1检验保存是否完成  2、设置data-val的值
	Template.prototype.checkHasSaveOrSetVal= function(type){
		var $inputList= $('.tem input')
		var $switchList= $('.tem .t-switch')
		var $textarea= $('.tem textarea')
		var checkResult= false //核对结果 true 失败 false 成功
		if(type == 1){
			$inputList.each(function(index,item){
				if($(item).hasClass('refType')){
					checkResult= $(item).attr('data-type').trim() !== $(item).attr('data-val').trim()
				}else{
					checkResult= $(item).val().trim() !== $(item).attr('data-val').trim()
				}
				if(checkResult){
					return false
				}
			})
			if(!checkResult){
				$switchList.each(function(index,item){
					checkResult= $(item).hasClass('mui-active') ? ($(item).attr('data-val').trim() == 1 ? false : true ) :
					($(item).attr('data-val').trim() != 1 ? false : true )
					if(checkResult){
						return false
					}
				})
			}
			if(!checkResult){
				checkResult= $textarea.val().trim() != $textarea.attr('data-val').trim()
			}
			console.log(JSON.stringify(checkResult))
			return checkResult
		}else{ //设置保存data-val
			$inputList.each(function(index,item){
				if($(item).hasClass('refType')){
					checkResult= $(item).attr('data-type',$(item).attr('data-val').trim())
				}else{
					checkResult= $(item).attr('data-val',$(item).val().trim())
				}
			})
			
			$switchList.each(function(index,item){
				var val= $(item).hasClass('mui-active') ? 1 : 2
				$(item).attr('data-val', val)
			})
			$textarea.attr('data-val',$textarea.val().trim())
		}
		
	}
	
	//获取并展示未使用此模板的同类型设备	
	Template.prototype.getAndRenderDevice= function(){
		var that= this
		$.ajax({
			url: '/merchant/getDeviceData',
			type: 'post',
			data: {
				code: this.code,
				tempid: this.id,
				type: 1, //1、返回未使用此模板的同类型设备  0、返回所有同类型设备
			},
			success: function(res){
				if(res.code === 200){
					that.deviceList= res.noresult /*保存设备列表*/
					$('.sort-box').attr('data-sort',0).find('i').removeClass('active') /*每次请求之后都初始化排序数据*/
					that.handleRender()
				}else{
					mui.toast(res.message)
				}
			},
			error: function(err){
				mui.toast('获取设备信息出错！')
			}
		})
   /*   //处理渲染	
    	function handleRender(list){
			$('.tb .tb-tbody').remove()
    		var tbody= $('<div class="tb-tbody"></div>')
    		if(list.length > 0){ //存在设备list
    			$(list).each(function(index,item){
        			var areaname= item.areaname == null ? '— —' : item.areaname
        			var devicename= item.devicename == null ? '— —' : item.devicename		
        			var str= 
        			'<div class="row">\n'+
        				'<div class="code">'+item.code+'</div>\n'+
        				'<div class="device-name">'+devicename+'</div>\n'+
        				'<div class="area-name">'+areaname+'</div>\n'+
        				'<div class="contral">\n'+
        					'<label class="weui-cell weui-cell_active weui-check__label" for="s11">\n'+
        		                '<div class="weui-cell__hd">\n'+
        		                    '<input type="checkbox" class="weui-check" name="checkbox1" data-code="'+item.code+'">\n'+
        		                    '<i class="weui-icon-checked"></i>\n'+
        		                '</div>\n'+
        		            '</label>\n'+
        				'</div>\n'+
        			'</div>'
        			tbody.append($(str))
        		})
    		}else{
    			var info= $('<div class="tb-info">暂无此类型设备、或此类型设备已全部使用此模板</div>')
    			tbody.append(info)
    		}
    		
    		tbody.insertAfter($('.tb .tb-thead'))
    		$we.$mask.show();
    		$we.$dialog2.addClass('show')
	        $we.$dialog2.addClass('weui-half-screen-dialog_show');
	        $('html').css({overflow: 'hidden'})
    	}*/
	}
	//发送请求，通过设备号list复用模板	
	Template.prototype.sendAjaxToUseTem= function(){
		var list= [] //存放选中的设备号
		var rowList= [] //存放当前一列的jq对象，为了后面成功之后渲染“正在使用当前模板的设备”
		var $list= $('.tb-tbody [name="checkbox1"]:checked') //.row元素jq对象input
		$list.each(function(index,item){
			var $row= $(item).parents('.row')
			rowList.push({
				code: $row.find('.code').text().trim(),
				deviceName: $row.find('.device-name').text().trim(),
				areaName: $row.find('.area-name').text().trim()
			})
			list.push($(item).attr('data-code').trim())
		})
		console.log('rowList',rowList)
		if(list.length < 0){ return mui.toast('你未选中设备!') }
		$.ajax({
			url: '/merchant/updateDeviceTemplate',
			type: 'post',
			data: {
				tempid: this.id,
				deviceList: JSON.stringify(list)
			},
			success: function(res){
				if(res.code == 200){
					mui.toast('选中设备已成功使用此模板')
					renderNowUsingTem(rowList)
					$we.$mask.hide();
					$we.$dialog2.removeClass('show')
		            $we.$dialog2.removeClass('weui-half-screen-dialog_show');
		            $('html').css({overflow: 'auto'})
				}else{
					mui.toast(res.message)
				}
			},
			error: function(err){
				mui.toast('设置出错、请稍后重试！')
			}
		})
		// 添加成功之后更新页面上的“正在使用此模板的设备数据”	
		function renderNowUsingTem(rowList){
			var codeList= [] //存放当前显示的设备数组
			var fragment= document.createDocumentFragment() //文档碎片
			var lastLiClass= $('.now-use-ul>li').last().attr('class') //最后一个li的color
			var colorId=  lastLiClass == void 0 ? 0 : lastLiClass.match(/color(\d+)/)[1]-0
			$('.now-use-ul>li').each(function(index,item){
				codeList.push($(item).find('.code').text().trim())
			})
			$(rowList).each(function(index,item){
				var num= (colorId+index)%7+1 > 7 ? 0 : (colorId+index)%7+1
				var code= $(item).find('.code').text().trim()
				if(codeList.indexOf(code) === -1){ //说明没在页面上更新
					var str= 
						'<li class="color'+num+'"><a href="javascript:void(0);">\n'+
							'<div class="code">'+item.code+'</div> <div class="name text-ellipsis">'+item.deviceName+'</div> <div class="areaname text-ellipsis">'+item.areaName+'</div> <div class="mui-icon mui-icon-closeempty delete-device" data-code="'+item.code+'"></div>\n'+
						'</a></li>'
					fragment.appendChild($(str)[0])
				}
			})
			$('.now-use-ul')[0].appendChild(fragment)
		}
		
	}
	//解除点击设备与此模板的使用关系	
	Template.prototype.deleteDeviceTem= function(target){
		mui.confirm('是否解除此设备使用当前模板？','提示',function(options){
			if(options.index == 1){
				var deviceCode= $(target).attr('data-code').trim()
				$.ajax({
					url: '/merchant/removeDeviceTemplate',
					type: 'post',
					data: {
						code: deviceCode
					},
					success: function(res){
						if(res.code === 200){
							mui.toast('此设备使用当前模板解除成功')
						}else{
							mui.toast(res.message)
						}
					},
					error: function(err){
						mui.toast('解除出错、请稍后重试！')
					}
				})
				var code= $(target).attr('data-code').trim()
				$(target).parents('li').remove()
			}
		})
	}
	// 排序设备
	Template.prototype.sortDevice= function(type){
		setSort.call(this,type)
		function setSort(type){
			
			var $target, //当前点击触发源
				$otherTarget //另一个触发源
			if(type == 1){
				$target=  $tb_code
				$otherTarget= $tb_areaname
			}else{
				$target=  $tb_areaname
				$otherTarget= $tb_code
			}
			var $target= type == 1 ? $tb_code : $tb_areaname
			var sortNum= $target.attr('data-sort').trim()-0 
			var newSortNum= 1 //新的排序 
			switch(sortNum){  //0、 初始化  1、
				case 0:  newSortNum= 1;break;
				case 1:  newSortNum= 2;break;
				case 2:  newSortNum= 1;break;
			}
			$otherTarget.attr('data-sort',0).find('i').removeClass('active') /*移除其他的排序*/
			$target.attr('data-sort',newSortNum).find('i').removeClass('active') 
			$target.find('i').eq(newSortNum-1).addClass('active')
			this.deviceList= this.deviceList.sort(function(a,b){
				if(type == 1){ //排序设备号
					if(newSortNum == 1){ /*排序小区id*/
						return parseInt(a.code)- parseInt(b.code)
					}else{
						return parseInt(b.code)- parseInt(a.code)
					}
				}else{ /*排序小区id*/
					if(newSortNum == 1){ /*排序小区id*/
						return parseInt(a.areaidid)- parseInt(b.areaidid)
					}else{
						return parseInt(b.areaidid)- parseInt(a.areaidid)
					}
				}
			})
			//重新渲染排序列表		
			this.handleRender()
		}
	}
	
	//处理渲染	要选中的设备
	Template.prototype.handleRender= function (){
		var list=  this.deviceList
		$('.tb .tb-tbody').remove()
		var tbody= $('<div class="tb-tbody"></div>')
		if(list.length > 0){ //存在设备list
			$(list).each(function(index,item){
    			var areaname= item.areaname == null ? '— —' : item.areaname
    			var devicename= item.devicename == null ? '— —' : item.devicename		
    			var str= 
    			'<div class="row">\n'+
    				'<div class="code">'+item.code+'</div>\n'+
    				'<div class="device-name">'+devicename+'</div>\n'+
    				'<div class="area-name">'+areaname+'</div>\n'+
    				'<div class="contral">\n'+
    					'<label class="weui-cell weui-cell_active weui-check__label" for="s11">\n'+
    		                '<div class="weui-cell__hd">\n'+
    		                    '<input type="checkbox" class="weui-check" name="checkbox1" data-code="'+item.code+'">\n'+
    		                    '<i class="weui-icon-checked"></i>\n'+
    		                '</div>\n'+
    		            '</label>\n'+
    				'</div>\n'+
    			'</div>'
    			tbody.append($(str))
    		})
		}else{
			var info= $('<div class="tb-info">暂无此类型设备、或此类型设备已全部使用此模板</div>')
			tbody.append(info)
		}
		
		tbody.insertAfter($('.tb .tb-thead'))
		$we.$mask.show();
		$we.$dialog2.addClass('show')
        $we.$dialog2.addClass('weui-half-screen-dialog_show');
        $('html').css({overflow: 'hidden'})
	}
	/*预览模板*/
	Template.prototype.prevTem= function(){
		var data= this.getData()
		$('#paratem').val(JSON.stringify(data));
		$('#prevForm').submit()
	}
})

