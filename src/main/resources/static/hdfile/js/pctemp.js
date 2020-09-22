// 模板入口文件

$(function(){
	 //============   
	   toastr.options = {
		  "closeButton": false,
		  "debug": false,
		  "positionClass": "toast-top-right",
		  "onclick": null,
		  "showDuration": "300",
		  "hideDuration": "1000",
		  "timeOut": "1500",
		  "extendedTimeOut": "1000",
		  "showEasing": "swing",
		  "hideEasing": "linear",
		  "showMethod": "fadeIn",
		  "hideMethod": "fadeOut"
		}
	   $('.addTemplate').click(function(){
		   var tem= $(this).attr('data-tem').trim()
		   if(tem==1){
			   var obj2= {
		                title: '新增充电模板',
		                temNmae: '',
		                brandName: '',
		                telephone: '',
		                isRef: true,
		                isWalletPay: false,
		                regVal: 1 //默认是1 ======================
		            }
		            renderList2(obj2)
		   }
	   })
	
	   var sourceNum= parseInt($('body').attr('data-source'))
	   var targetEle= null
	    $('#isRefInp').click(function(){
	    	var common2= parseInt($(targetEle).parent().parent().parent().parent().find('.isRef').next().attr('data-val'))
	    	$('#popover input').removeAttr('checked')
	    	if(common2 === 2){
	    		$('#refReg2').prop('checked',true)
	    	}else if(common2 === 3){
	    		$('#refReg3').prop('checked',true)
	    	}else {
	    		$('#refReg1').prop('checked',true)
	    	}

	        $('#popover').fadeIn()
	     })
	     $('#exitBut').click(function(){
	         $('#popover').fadeOut()
	     })
	      $('#confirmBut').click(function(){
	    	  var regReg= parseInt($('.pcTem1 input[name="refReg"]:checked').val()) //退费标准
	          var str= ''
	          switch(regReg){
	                 case 1: str= '(默认)'; break;
	                 case 2: str= '(时间)'; break;
	                 case 3: str= '(电量)'; break;
	             }
	              $('#spanList').text(str)
	         $('#popover').fadeOut()
	     })
       
      
/**
 * 
 */   
      
    $('.admin').click(function (e) {
        e =e || window.event
        var target= e.target || e.srcElement
        if(! $(target).hasClass('addTemplate')){
        	targetEle= target
        }
        if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('tem-edit')){ //点击编辑子模板
            //这一步发送ajax获取数据,或者在元素身上找到绑定的数据，讲数据处理为下面的obj格式的，并将数据穿进去
            var name= $(target).parent().parent().find('td').eq(0).html().trim()
            var parse= $(target).parent().parent().find('td').eq(1).find('span').html().trim()
            var time= $(target).parent().parent().find('td').eq(2).find('span').html().trim()
            var power= $(target).parent().parent().find('td').eq(3).find('span').html().trim()

            var obj= {  //这里是从后台获取的数据或者从元素上获取的（这里模拟后台数据）
                title: '修改电子模板',
                name: name,
                parse: parse,
                time: time,
                power: power,
            }
            renderList(obj)
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('tem-delete')){ //删除子模板
        	   var isSelectTem= $(target).parent().parent().parent().parent().parent().hasClass('borShadow')
               if(isSelectTem){
               	 toastr.warning('默认模板不能删除')
                 return false
               }
                if(confirm('确定删除子模板吗？')){
                    // =============================发送 ajax 提交数据 提交删除元素的数据
                    var id= parseInt($(targetEle).attr('data-id'))
                    $.ajax({
                        data:{
                            id: id
                        },
                        url : "/wctemplate/deletesubclasscharge",
                        type : "POST",
                        cache : false,
                        success : function(e){
                             $(target).parent().parent().remove()
                             toastr.success('删除成功')
                           
                        },//返回数据填充
                    });
                    
                }
              
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('addBut')){ //添加子模板
            var $list= $(target).parent().parent().prev()
            var parent= $(target).parent().parent().parent().parent().parent()
            var flag= parent.hasClass('borShadow') //判断当前是不是选中的模板
            var disableStr= flag ? 'disabled="disabled"' : ''
            if($list.length <= 0){ //没有子节点
                //这里是默认设置
                var nextParse= 1
                var nextTime= 60
                var nextPower= 1
                var rate1= Math.round(time / parse)  //得到的比例是1元充电多久
                var rate2= Math.round(time / power)  //得到的比例是消耗1度电充电多久
                var houer= (nextTime / 60) % 1 === 0 ? (nextTime / 60) : (nextTime / 60).toFixed(2)
                var nextName= nextParse+'元'+houer+'小时'

            }else { //找到上一个子节点
                var reg= /(?!(0[0-9]{0,}$))[0-9]{1,}[.]{0,}[0-9]{0,}/g
                var parse= $list.find('td').eq(1).find('span').text().match(reg)[0]-0
                var time= $list.find('td').eq(2).find('span').text().match(reg)[0]-0
                var power= $list.find('td').eq(3).find('span').text().match(reg)[0]-0
                var rate1= Math.round(time / parse)  //得到的比例是1元充电多久
                var rate2= Math.round(time / power)  //得到的比例是消耗1度电充电多久
                var nextParse= parse+1
                var nextTime= (nextParse * rate1) % 1 === 0 ? (nextParse * rate1) : (nextParse * rate1).toFixed(2)
                var nextPower= (nextTime / rate2) % 1 === 0 ? (nextTime / rate2) : (nextTime / rate2).toFixed(2)
                var houer= (nextTime / 60) % 1 === 0 ? (nextTime / 60) : (nextTime / 60).toFixed(2)
                var nextName= nextParse+'元'+houer+'小时'
            }
            var id= parseInt($(targetEle).attr('data-id'))
           //发送ajax将新增子模板的数据传输到服务器=====================
            $.ajax({  //添加子模板
                data:{
                    id: id,
                    name: nextName,
                    money: nextParse, 
                    chargeTime: nextTime, 
                    chargeQuantity: nextPower
                },
                url : "/wctemplate/addsubclasscharge",
                type : "POST",
                cache : false,
                success : function(e){
                	if(e.code=== 100){
                         toastr.warning('登录过期，请重新登录')
                	}else if(e.code=== 101){
                        toastr.warning('显示名称重复，请修改后重试')
                	}else if(e.code=== 102){
                        toastr.warning('金额过大，请修改后重试')
                	}else if(e.code === 200){
                		var str= '<td>'+nextName+'</td> <td><span>'+nextParse+'</span>元</td><td><span>'+nextTime+'</span>分钟</td><td><span>'+nextPower+'</span>度</td><td class="setWidth"><button type="button" class="btn btn-info tem-edit" data-id="'+e.ctemid+'">编辑</button></td><td class="setWidth"><button type="button" class="btn btn-danger tem-delete" data-id="'+e.ctemid+'" '+disableStr+' >删除</button></td>'
                       var list= $('<tr></tr>')
                       list.html(str)
                       $(targetEle).parent().parent().parent()[0].insertBefore(list[0],$(targetEle).parent().parent()[0])
                        toastr.success('添加成功')
                	}

                },//返回数据填充
            });
   

        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('tem-title-edit')){ //点击主模板编辑按钮
            //$('.tem-mask2').fadeIn()            var telephone= $(target).parent().parent().find('p').eq(2).find('span').html().trim()

            //这一步发送ajax获取数据,或者在元素身上找到绑定的数据，讲数据处理为下面的obj2格式的，并将数据穿进去

            var temNmae= $(target).parent().parent().parent().parent().find('.temNmae').html().trim()
            var brandName= $(target).parent().parent().parent().parent().find('.brandName').html().trim()
            var telephone= $(target).parent().parent().parent().parent().find('.telephone').html().trim()
            var isRef= $(target).parent().parent().parent().parent().find('.isRef').html().trim() === '是' ? true : false
            var isWalletPay= $(target).parent().parent().parent().parent().find('.isWalletPay').html().trim() === '是' ? true : false
            var regVal= ''
            console.log(temNmae,brandName,telephone,isRef,isWalletPay)
            if(isRef){
                regVal=  parseInt($(target).parent().parent().parent().parent().find('.isRef').next().attr('data-val'))
             }
            
            var obj2= {
                title: '修改充电模板',
                temNmae: temNmae,
                brandName: brandName,
                telephone: telephone,
                isRef: isRef,
                isWalletPay: isWalletPay,
                regVal: regVal
            }
            renderList2(obj2)
        //}
        /*else if( $(target).hasClass('addTemplate')){ //添加主模板
            var obj2= {
                title: '新增充电模板',
                temNmae: '',
                brandName: '',
                telephone: '',
                isRef: true,
                isWalletPay: false,
                regVal: 1 //默认是1 ======================
            }
            renderList2(obj2)*/
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('tem-title-delete')){ //删除主模板
        	 var isSelectTem= $(target).parent().parent().parent().parent().parent().hasClass('borShadow')
             if(isSelectTem){
               toastr.warning('默认模板不能删除！')
               return false
             }
             if(confirm('确定删除模板？')){
                    // =============================发送 ajax 提交数据 提交删除元素的数据
                    var id= parseInt($(targetEle).attr('data-id'))
                    $.ajax({
                        data:{
                            id: id
                        },
                        url : "/wctemplate/deletestaircharge",
                        type : "POST",
                        cache : false,
                        success : function(e){
                            $(target).parent().parent().parent().parent().parent().remove()
                            toastr.success('主模板删除成功')
                        },//返回数据填充
                    });
             }
            
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('selectTem')){  
            // 点击选择模板
            var parent= $(target).parent().parent().parent().parent().parent()
            if(!parent.hasClass('borShadow')){
                // 发送ajax，成功之后执行下面的 ()
                // 数据来源
                var arecode= $('body').attr('data-arecode').trim()
                var source= $('body').attr('data-source').trim()
                var id= $(target).attr('data-id').trim() //模板id
                  $.ajax({
                         data:{
                             source: source,
                             obj:arecode,
                        	 temid: id
                         },
                         url : "/wctemplate/templatechoice",
                         type : "POST",
                         cache : false,
                         success : function(e){
                        	 console.log(e)
                            if(e == 1){
                            	 parent.siblings().removeClass('borShadow') //移除所有的兄弟节点的选择
                            	 parent.siblings().find('.selectTem').prop('disabled',false)
                            	 parent.siblings().find('.tem-title-delete').prop('disabled',false)
                            	 parent.siblings().find('.tem-delete').prop('disabled',false)
                                 parent.siblings().find('.lastTd p').fadeOut()
                                 parent.siblings().find('.selectTem').removeClass('active')
                                 parent.addClass('borShadow')  //给当前元素添加节点
                                 $(target).parent().find('p').fadeIn()
                                 $(target).addClass('active')
                                 $(target).prop('disabled',true)
                                 parent.find('.tem-title-delete').prop('disabled',true)
                                 parent.find('.tem-delete').prop('disabled',true)
                                  toastr.success('已选择当前模板！')
                                 //mui.toast('已选择当前模板',{ duration:'1500', type:'div' })
                            }
                         },//返回数据填充
                         error: function(){
                             toastr.error('选择充电模板失败，请稍后再试！')
                         }
                     });
             }
            
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('defaultTem')){ 
        	 var parent= $(target).parent().parent().parent().parent().parent()
        	 var id= $(target).attr('data-id').trim() //模板id
        	 $.ajax({
                 data:{
                     source: 0,
                	 temid: id
                 },
                 url : "/wctemplate/templatedefault",
                 type : "POST",
                 cache : false,
                 success : function(e){
                	 if(e.code=== 100){
                         toastr.warning('登录过期，请重新登录')
                	 }else if(e.code=== 200){
                		 parent.siblings().removeClass('borShadow') //移除所有的兄弟节点的选择
                         parent.siblings().find('.lastTd p').fadeOut()
                         parent.siblings().find('.defaultTem').removeClass('active')
                         parent.addClass('borShadow')  //给当前元素添加节点
                         $(target).parent().find('p').fadeIn()
                         $(target).addClass('active')
                          toastr.success('默认模板设置成功！')
                	 }
                   
                 },//返回数据填充
                 error: function(){
                    toastr.error('设为默认失败，请稍后再试！')
                 }
             });
        
        }
    })

    $('.pcTem1 .list-center1').mousedown(function(e){
        e= e || window.event
        e.stopPropagation()
    })
    $('.pcTem1 .tem-mask1').mousedown(close)
    $('.pcTem1 .list-center1 .close').mousedown(close)
    $('.pcTem1 .list-center1 .close2').mousedown(close)
    function close (e) {
        e= e || window.event
        e.stopPropagation()
        console.log('点击了')
        $('.pcTem1 .tem-mask1').fadeOut()
    }

    $('.pcTem1 .list-center2').click(function(e){ //组织阻止冒泡，防止点击了自身隐藏
        e= e || window.event
        e.stopPropagation()
    })
    $('.pcTem1 .tem-mask2').click(close2)
    $('.pcTem1 .list-center2 .close').click(close2)
    $('.pcTem1 .list-center2 .close2').click(close2)
    function close2 (e) {
        e= e || window.event
        e.stopPropagation()
        $('.pcTem1 .tem-mask2').fadeOut()
    }

    $('.pcTem1 .list-center1 .submit').click(function (e) { //点击修改电子模板提交
        e =e || window.event
        e.stopPropagation()
        var reg= /^\d+(\.\d+)?$/
        var nameVal= $('.pcTem1 .list-center1 input[name=name]').val().trim()
        var parseVal= $('.pcTem1 .list-center1 input[name=parse]').val().trim()
        var timeVal= $('.pcTem1 .list-center1 input[name=time]').val().trim()
        var powerVal= $('.pcTem1 .list-center1 input[name=power]').val().trim()
        if(nameVal.length <= 0){
        	toastr.warning("请输入模板名称")
           // mui.toast('请输入模板名称',{ duration:'1500', type:'div' })
            return false
        }
        if(parseVal.length <= 0){
        	toastr.warning("请输入充电价格")
            //mui.toast('请输入充电价格',{ duration:'1500', type:'div' })
            return false
        }
        if(!reg.test(parseVal)) {
        	toastr.warning("充电价格请输入数字")
            //mui.toast('充电价格请输入数字',{ duration:'1500', type:'div' })
            return false
        }
        if(timeVal.length <= 0){
        	toastr.warning("请输入充电时间")
            //mui.toast('请输入充电时间',{ duration:'1500', type:'div' })
            return false
        }
        if(!reg.test(timeVal)) {
        	toastr.warning("充电时间请输入数字")
           // mui.toast('充电时间请输入数字',{ duration:'1500', type:'div' })
            return false
        }
        if(powerVal.length <= 0){
        	toastr.warning("请输入消耗电量")
           // mui.toast('请输入消耗电量',{ duration:'1500', type:'div' })
            return false
        }
        if(!reg.test(powerVal)) {
        	toastr.warning("消耗电量请输入数字")
            //mui.toast('消耗电量请输入数字',{ duration:'1500', type:'div' })
            return false
        }
        //修改子模板
            //发送ajax讲修改之后的数据传输到服务器=====================

            var id= parseInt($(targetEle).attr('data-id'))
            $.ajax({
                data:{
                    id: id,
                    name:nameVal,
                    money: parseVal,
                    chargeTime: timeVal,
                    chargeQuantity: powerVal
                },
                url : "/wctemplate/updatesubclasscharge",
                type : "POST",
                cache : false,
                success : function(e){
                    var parentEle= $(targetEle).parent().parent()
		            console.log(parentEle)
		            parentEle.find('td').eq(0).html(nameVal)
		            parentEle.find('td').eq(1).find('span').html(parseVal)
		            parentEle.find('td').eq(2).find('span').html(timeVal)
		            parentEle.find('td').eq(3).find('span').html(powerVal)
		        	$('.pcTem1 .tem-mask1').fadeOut()
		        	 toastr.success("修改成功")

                },//返回数据填充
            });

            
            
    })

    $('.pcTem1 .list-center2 .submit').click(function(e){
        e =e || window.event
        e.stopPropagation()
        var temNmaeVal= $('.pcTem1 .list-center2 input[name=temNmae]').val().trim()
        var brandNameseVal= $('.pcTem1 .list-center2 input[name=brandName]').val().trim()
        var telephoneVal= $('.pcTem1 .list-center2 input[name=telephone]').val().trim()
        var isRefVal= $('.pcTem1 .list-center2 input[name="isRef"]:checked').val()
        var isWalletPayVal= $('.pcTem1 .list-center2 input[name="isWalletPay"]:checked').val()
        
        var refReg= $('#popover input[name="refReg"]:checked').val() //退费标准
        var str1= ''
            switch(parseInt(refReg)){
                case 1: str1= '(退费标准：时间和电量最小)'; break;
                case 2: str1= '(退费标准：根据时间)'; break;
                case 3: str1= '(退费标准：根据电量)'; break;
            }
        console.log($('#popover input[name="refReg"]'))
        var permit= parseInt(isRefVal) === 0 ? 2 :  parseInt(isRefVal)
        var walletpay = parseInt(isWalletPayVal) === 0 ? 2 :  parseInt(isWalletPayVal)
        if(temNmaeVal.length <= 0){
            toastr.warning('请输入模板名称')
            return false
        }

        var flag= $('.list-center2 h3').html().trim() === '修改充电模板' ? true : false
        if(flag){ // 修改充电模板
            //发送ajax将修改的数据传输到服务器=====================
            // 获取id
            var id= parseInt($(targetEle).attr('data-id'))
            $.ajax({
                data:{
                    id: id,
                    name:temNmaeVal,
                    remark: brandNameseVal,
                    permit: permit,
                    walletpay: walletpay,
                    common1: telephoneVal,
                    common2	: refReg
                },
                url : "/wctemplate/updatestaircharge",
                type : "POST",
                cache : false,
                success : function(e){
                   handleEditTitle()
                },//返回数据填充
            });


            function handleEditTitle(){
                var parentEle= $(targetEle).parent().parent().parent().parent()
                parentEle.find('.temNmae').html(temNmaeVal)
                parentEle.find('.brandName').html(brandNameseVal)
                parentEle.find('.telephone').html(telephoneVal)
                var isRefHtml= parseInt(isRefVal) ? '是' : '否'
                parentEle.find('.isRef').html(isRefHtml)
              
                parentEle.find('.isRef').next().html(str1)
                parentEle.find('.isRef').next().attr('data-val',refReg)
                 if(!parseInt(isRefVal)){
                    parentEle.find('.isRef').next().fadeOut()
                }else{
                    parentEle.find('.isRef').next().fadeIn()
                }
                parentEle.find('.isRef').removeClass('span-green span-red')
                if(parseInt(isRefVal)){
                     parentEle.find('.isRef').addClass('span-green')
                }else {
                    parentEle.find('.isRef').addClass('span-red')
                }

                var isWalletPayHtml= parseInt(isWalletPayVal) ? '是' : '否'
                parentEle.find('.isWalletPay').html(isWalletPayHtml)
                parentEle.find('.isWalletPay').removeClass('span-green span-red')
                if(parseInt(isWalletPayVal)){
                    parentEle.find('.isWalletPay').addClass('span-green')
                }else {
                    parentEle.find('.isWalletPay').addClass('span-red')
                }
                toastr.success('主模板编辑成功！')

            }
        }else { //添加新模板
            //发送ajax将新增的数据传输到服务器=====================

           
            $.ajax({
                data:{
                    name:temNmaeVal,
                    remark: brandNameseVal,
                    permit: permit,
                    walletpay: walletpay,
                    common1: telephoneVal,
                    common2	: refReg
                },
                url : "/wctemplate/addstaircharge",
                type : "POST",
                cache : false,
                success : function(e){
                	console.log(e)
                	if(e.code === 101){
                		 toastr.warning('添加的模板名称已存在，请更改！')
                    }else if(e.code === 100){
                    	 toastr.warning('登录已过期，请重新登录！')
                    	handleAddTem(e)
                    }else if(e.code === 200){
                    	handleAddTem(e.temid)
                    }
                },//返回数据填充
            });
               
            
            function handleAddTem(id){
            	    var isRefHtml= parseInt(isRefVal) ? '是' : '否'
                    var isWalletPayHtml= parseInt(isWalletPayVal) ? '是' : '否'
                    var isRefClass= parseInt(isRefVal) ? 'span-green' : 'span-red'
                    var isWalletPayClass= parseInt(isWalletPayVal) ? 'span-green' : 'span-red'
                    		var defauleOrSelectTem= ''
                    		if(sourceNum != 0){//判断是从哪进模板的
                                defauleOrSelectTem= '<td colspan="6" class="lastTd"><button type="button" class="btn btn-info selectTem" data-id="'+id+'">选择模板</button><p class="pText">选中模板</p><button type="button" class="btn btn-info addBut" data-id="'+id+'">添加模板</button></td>'
	                    		}else{
                                    defauleOrSelectTem= '<td colspan="6" class="lastTd"><button type="button" class="btn btn-info defaultTem" data-id="'+id+'">设为默认</button><p class="pText">默认模板</p><button type="button" class="btn btn-info addBut" data-id="'+id+'">添加模板</button></td>'
	                    		}
                    	
                    	if(!parseInt(isRefVal)){ //当为否定是
                             var str= '<table class="table  faTem"><thead><tr class="title"><td class="left_td"><span><b>模板名称:</b> <span class="temNmae">'+temNmaeVal+'</span></span><span><b>品牌名称:</b> <span class="brandName">'+brandNameseVal+'</span></span><span><b>售后电话：</b> <span class="telephone">'+telephoneVal+'</span></span></td><td colspan="2">操作</td></tr></thead><tbody><tr><td class="left_td"><span><b>是否支持退费:</b> <span class="isRef '+isRefClass+'">'+isRefHtml+'</span> <span></span></span><span><b>是否钱包强制支付:</b> <span class="isWalletPay '+isWalletPayClass+'">'+isWalletPayHtml+'</span></span></td><td class="setWidth"><button type="button" class="btn btn-info tem-title-edit" data-id="'+id+'">编辑</button></td><td class="setWidth"><button type="button" class="btn btn-danger tem-title-delete" data-id="'+id+'">删除</button></td></tr></tbody></table><table class="table table-bordered chTem"><thead><tr class="title"><td>显示名称</td><td>充电价格</td><td>充电时间</td><td>消耗电量</td><td colspan="2">操作</td></tr></thead><tbody><tr>'+defauleOrSelectTem+'</tr></tbody></table>'
                          
                       }else{
                    	   var str= '<table class="table  faTem"><thead><tr class="title"><td class="left_td"><span><b>模板名称:</b> <span class="temNmae">'+temNmaeVal+'</span></span><span><b>品牌名称:</b> <span class="brandName">'+brandNameseVal+'</span></span><span><b>售后电话：</b> <span class="telephone">'+telephoneVal+'</span></span></td><td colspan="2">操作</td></tr></thead><tbody><tr><td class="left_td"><span><b>是否支持退费:</b> <span class="isRef '+isRefClass+'">'+isRefHtml+'</span> <span data-val="'+refReg+'">'+str1+'</span></span><span><b>是否钱包强制支付:</b> <span class="isWalletPay '+isWalletPayClass+'">'+isWalletPayHtml+'</span></span></td><td class="setWidth"><button type="button" class="btn btn-info tem-title-edit" data-id="'+id+'">编辑</button></td><td class="setWidth"><button type="button" class="btn btn-danger tem-title-delete" data-id="'+id+'">删除</button></td></tr></tbody></table><table class="table table-bordered chTem"><thead><tr class="title"><td>显示名称</td><td>充电价格</td><td>充电时间</td><td>消耗电量</td><td colspan="2">操作</td></tr></thead><tbody><tr>'+defauleOrSelectTem+'</tr></tbody></table>'
                       
                       }
                    var div= $('<div class="temList"></div>')
                    div.html(str)
                    console.log(div[0])
                    $('.pcTem1 .tableContent').append(div)
                    toastr.success('主模板添加成功！')
            }
        }
        $('.pcTem1 .tem-mask2').fadeOut()
//        window.location.href= window.location.href
    })


    function renderList(obj){ //渲染list-content
        $('.pcTem1 .list-center1 h3').html(obj.title)
        $('.pcTem1 .list-center1 input[name=name]').val(obj.name)
        $('.pcTem1 .list-center1 input[name=parse]').val(obj.parse)
        $('.pcTem1 .list-center1 input[name=time]').val(obj.time)
        $('.pcTem1 .list-center1 input[name=power]').val(obj.power)
        $('.pcTem1 .tem-mask1').fadeIn()
    }
    function renderList2(obj){
        $('.pcTem1 .list-center2 h3').html(obj.title)
        $('.pcTem1 .list-center2 input[name=temNmae]').val(obj.temNmae)
        $('.pcTem1 .list-center2 input[name=brandName]').val(obj.brandName)
        $('.pcTem1 .list-center2 input[name=telephone]').val(obj.telephone)
        if(obj.isRef){
            $('.pcTem1 .list-center2 input[name=isRef]').eq(0).prop('checked',true)
            console.log( $('.pcTem1 input[name=refReg]').eq(0))
            var str= ''
                switch(obj.regVal){
                    case 1: str= '(默认)'; break;
                    case 2: str= '(时间)'; break;
                    case 3: str= '(电量)'; break;
                }
                $('#spanList').text(str)
        }else{
        	 $('#spanList').text('')
            $('.pcTem1 .list-center2 input[name=isRef]').eq(1).prop('checked',true)
        }
        if(obj.isWalletPay){
            $('.pcTem1 .list-center2 input[name=isWalletPay]').eq(0).prop('checked',true)
        }else{
            $('.pcTem1 .list-center2 input[name=isWalletPay]').eq(1).prop('checked',true)
        }
        $('.pcTem1 .tem-mask2').fadeIn()
    }

})