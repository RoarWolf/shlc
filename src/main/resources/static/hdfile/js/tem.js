
//这里是tem 模板也得js逻辑处理

$(function () {
	var htmlwidth = document.documentElement.clientWidth || document.body.clientWidth;
    var fontSize= htmlwidth/16
    var style= document.createElement('style')
    style.innerHTML= 'html { font-size: '+fontSize+'px !important;}'
    var head= document.getElementsByTagName('head')[0]
    head.insertBefore(style,head.children[0])
   function tem1(){
//	   将一个模板的id传给选择按钮
	   if($('.isChecked button').parents('.gradTem').length > 0){
		   var firId= $('.isChecked button').parents('.gradTem').find('.title').eq(0).find('.tem-title-edit').attr('data-id').trim()
		   $('.gradTem .isChecked button').attr('data-id',firId)
		   $('.isChecked button').parents('.gradTem.borShadow').find('.isChecked button').addClass('active')  
	   }
	   
	   var sourceNum= parseInt($('body').attr('data-source'))
	   var targetEle= null
	    $('#isRefInp').click(function(){
	    	var common2= parseInt($(targetEle).parent().parent().find('p').eq(3).find('span').eq(1).attr('data-val'))
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
	    	  var regReg= parseInt($('.tem1 input[name="refReg"]:checked').val()) //退费标准
	          var str= ''
	          switch(regReg){
	                 case 1: str= '(默认)'; break;
	                 case 2: str= '(时间)'; break;
	                 case 3: str= '(电量)'; break;
	             }
	              $('#spanList').text(str)
	         $('#popover').fadeOut()
	     })
	     
	    
	    var reqParmasFromScan= null //设置请求参数为null， 点击查看的之后将值赋给reqParmasFromScan，然后进行请求 
	    
    $('.tem1 .tem').click(function (e) {
        e =e || window.event
        var target= e.target || e.srcElement
        targetEle= target
        if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('tem-edit')){ //点击编辑子模板
            //这一步发送ajax获取数据,或者在元素身上找到绑定的数据，讲数据处理为下面的obj格式的，并将数据穿进去
            var name= $(target).parent().parent().find('p').eq(0).find('span').html().trim()
            var parse= $(target).parent().parent().find('p').eq(1).find('span')[0].childNodes[0].textContent.trim()
            var time= $(target).parent().parent().find('p').eq(2).find('span')[0].childNodes[0].textContent.trim()
            var power= $(target).parent().parent().find('p').eq(3).find('span')[0].childNodes[0].textContent.trim()
            var title= '修改电子模板'
            var obj= {  //这里是从后台获取的数据或者从元素上获取的（这里模拟后台数据）
                title: title,
                name: name,
                parse: parse,
                time: time,
                power: power
            }
            renderList(obj);
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('tem-delete')){
	 		 var flag= $(target).parent().parent().parent().parent().parent().parent().hasClass('list-div') ? true : false //判断这个时候不是分类模板
        	 var isSelectTem
        	 if(flag){
        		isSelectTem= $(target).parent().parent().parent().parent().parent().parent().hasClass('borShadow')
        	 }else{
        		 isSelectTem= $(target).parent().parent().parent().parent().parent().hasClass('borShadow') 
        	 }
        	 
             if(isSelectTem){
               mui.toast('被选择的模板不能删除',{ duration:'1500', type:'div' })
               return false
             }
        	
            mui.confirm('确定删除?', function (type) {
                if(type.index){ //删除子模板
                    $(target).parent().parent().remove()
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
                        	console.log()
                        },//返回数据填充
                    });
        
                }
            })
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('addBut')){ //添加子模板
        	 var $list= $(target).parent().prev()
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
                var parse= $list.find('p').eq(1).find('span').text().match(reg)[0]-0
                var time= $list.find('p').eq(2).find('span').text().match(reg)[0]-0
                var power= $list.find('p').eq(3).find('span').text().match(reg)[0]-0
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
                	console.log(e)
                	if(e.code=== 100){
                		 mui.toast('登录过期，请重新登录',{ duration:'1500', type:'div' }) 
                	}else if(e.code=== 101){
                		mui.toast('显示名称重复，请修改后重试',{ duration:'1500', type:'div' })
                	}else if(e.code=== 102){
                		mui.toast('金额过大，请修改后重试',{ duration:'1500', type:'div' })
                	}else if(e.code === 200){
                		 var str= '<p>显示名称：<span>'+nextName+'</span></p><p>充电价格：<span>'+nextParse+' <b>元</b></span></p><p>充电时间：<span>'+nextTime+' <b>分钟</b></span></p> <p>消耗电量：<span>'+nextPower+'<b>度</b></span></p> <div> <button type="button" class="mui-btn mui-btn-success tem-edit" data-id='+e.ctemid+'>编辑</button> <button type="button" class="mui-btn mui-btn-success tem-delete" data-id='+e.ctemid+'>删除</button> </div>'
                       var list= $('<li class="mui-table-view-cell"></li>')
                       list.html(str)
                       $(targetEle).parent().parent()[0].insertBefore(list[0],$(targetEle).parent()[0])
                	}

                },//返回数据填充
            });
   

        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('tem-title-edit')){ //点击模板编辑按钮
            //$('.tem-mask2').fadeIn()
            //这一步发送ajax获取数据,或者在元素身上找到绑定的数据，讲数据处理为下面的obj2格式的，并将数据穿进去

            var temNmae= $(target).parent().parent().find('p').eq(0).find('span').html().trim()
            var brandName= $(target).parent().parent().find('p').eq(1).find('span').html().trim()
            var telephone= $(target).parent().parent().find('p').eq(2).find('span').html().trim()
            var isRef= $(target).parent().parent().find('p').eq(3).find('span').html().trim() === '是' ? true : false
            var isWalletPay= $(target).parent().parent().find('p').eq(4).find('span').html().trim() === '是' ? true : false
            var textInfo= $(target).parents('.list-div').find('.pTextInfo').attr('data-text').trim() //获取充电说明信息
            var ifalipay= $(target).parent().parent().find('.ifalipay').find('span').html().trim() === '是' ? true : false
            var isGrad= true //为true 的时候隐藏	
            var regVal= ''
            if(isRef){
                regVal=  parseInt($(target).parent().parent().find('p').eq(3).find('span').eq(1).attr('data-val'))
             }
            
            var obj2= {
                title: '修改充电模板',
                temNmae: temNmae,
                brandName: brandName,
                telephone: telephone,
                isRef: isRef,
                isWalletPay: isWalletPay,
                regVal: regVal,
                isGrad: isGrad,
                textInfo: textInfo,
                ifalipay: ifalipay
            }
            renderList2(obj2);
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('addTemplate')){ 
        	/*判断页面中是否已经存在分类模板了，已存在则isGrad为true 不显示*/
        	var isGrad= $('.tem .gradTem').length >0 ? true : false
        	var obj2= {
                title: '新增充电模板',
                temNmae: '',
                brandName: '',
                telephone: '',
                isRef: true,
                isWalletPay: false,
                regVal: 1, //默认是1 ======================
                isGrad: isGrad,
                textInfo: '',
                ifalipay: true
            }
            renderList2(obj2);
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('tem-title-delete')){
        	 var flag= $(target).parent().parent().parent().parent().parent().hasClass('list-div') ? true : false //判断这个时候不是分类模板
        	 var isSelectTem
        	 if(flag){
        		 console.log( $(target).parent().parent().parent().parent().parent())
        		isSelectTem= $(target).parent().parent().parent().parent().parent().hasClass('borShadow')
        	 }else{
        		 isSelectTem= $(target).parent().parent().parent().parent().hasClass('borShadow') 
        	 }
        	 
             if(isSelectTem){
               mui.toast('被选择的模板不能删除',{ duration:'1500', type:'div' })
               return false
             }
            mui.confirm('确定删除模板?', function (type) {
                if(type.index){ //删除
                    $(target).parent().parent().parent().parent().remove()
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
                           
                        },//返回数据填充
                    });

                }
            })
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('selectTem')){
            // 点击选择模板
            var parent= $(target).parent().parent().parent().parent().parent()
            if(!parent.hasClass('borShadow')){
                // 发送ajax，成功之后执行下面的 ()
                // 数据来源
                var arecode= $('body').attr('data-arecode').trim()
                var source= $('body').attr('data-source').trim()
               /* var id= $(target).attr('data-id').trim() //模板id
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
                            	console.log("执行")
                                parent.siblings().removeClass('borShadow') //移除所有的兄弟节点的选择
                                parent.siblings().find('.bottom p').fadeOut()
                                parent.siblings().find('.selectTem').removeClass('active')
                                parent.addClass('borShadow')  //给当前元素添加节点
                                $(target).parent().parent().find('p').fadeIn();
                                $(target).addClass('active')
                                 //mui.toast('已选择当前模板',{ duration:'1500', type:'div' })
                            }
                         },//返回数据填充
                         error: function(){
                             mui.toast('选择充电模板失败，请稍后再试！',{ duration:'1500', type:'div' })
                         }
                     });

               
             }else{
                mui.toast('你已选择当前模板',{ duration:'1500', type:'div' })
             }*/
             var parent= $(target).parent().parent().parent().parent().parent()
        	 var flag= parent[0].nodeName.toLowerCase()==='body' ? true : false //判断这个时候不是分类模板
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
                	 if(e===1){
                		 if(flag){ //点击的是分类模板
                			 $('.tem>.list-div').removeClass('borShadow');
                			 $('.tem>.list-div').find('.bottom p').fadeOut();
                			 $('.tem>.list-div').find('.selectTem').removeClass('active');
                			 $(target).parent().parent().parent().addClass('borShadow');
                			 $(target).addClass('active');
                			 $(target).parent().parent().find('p').fadeIn()
                		 }else{//点击的不是是分类模板
                			 parent.siblings().removeClass('borShadow') //移除所有的兄弟节点的选择
                			 $('.tem .gradTem').removeClass('borShadow')//移除分类节点的选择
                             parent.siblings().find('.bottom p').fadeOut()
                             $('.tem .gradTem').find('.bottom p').fadeOut()
                             parent.siblings().find('.selectTem').removeClass('active')
                             $('.tem .gradTem').find('.selectTem').removeClass('active')
                             parent.addClass('borShadow')  //给当前元素添加节点
                             $(target).parent().parent().find('p').fadeIn();
                             $(target).addClass('active') 
                		 }
                		
                	 }else{
                		 mui.toast('选中失败，请稍后再试！',{ duration:'1500', type:'div' }) 
                	 }
                   
                 },//返回数据填充
                 error: function(){
                     mui.toast('选中失败，请稍后再试！',{ duration:'1500', type:'div' })
                 }
             });
            }
            
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('defaultTem')){ 
        	 var parent= $(target).parent().parent().parent().parent().parent()
        	 var flag= parent[0].nodeName.toLowerCase()==='body' ? true : false //判断这个时候不是分类模板
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
                	 console.log(e)
                	 if(e.code=== 100){
                		 mui.toast('登录过期，请重新登录',{ duration:'1500', type:'div' })
                	 }else if(e.code=== 200){
                		 if(flag){ //点击的是分类模板
                			 $('.tem>.list-div').removeClass('borShadow');
                			 $('.tem>.list-div').find('.bottom p').fadeOut();
                			 $('.tem>.list-div').find('.defaultTem').removeClass('active');
                			 $(target).parent().parent().parent().addClass('borShadow');
                			 $(target).addClass('active');
                			 $(target).parent().parent().find('p').fadeIn()
                		 }else{//点击的不是是分类模板
                			 parent.siblings().removeClass('borShadow') //移除所有的兄弟节点的选择
                			 $('.tem .gradTem').removeClass('borShadow')//移除分类节点的选择
                             parent.siblings().find('.bottom p').fadeOut()
                             $('.tem .gradTem').find('.bottom p').fadeOut()
                             parent.siblings().find('.defaultTem').removeClass('active')
                             $('.tem .gradTem').find('.defaultTem').removeClass('active')
                             parent.addClass('borShadow')  //给当前元素添加节点
                             $(target).parent().parent().find('p').fadeIn();
                             $(target).addClass('active') 
                		 }
                		
                	 }
                   
                 },//返回数据填充
                 error: function(){
                     mui.toast('设为默认失败，请稍后再试！',{ duration:'1500', type:'div' })
                 }
             });
        
        }else if($(target).hasClass('tem-title-text')){ //点击查看
        	var temNmae= $(target).parents('.list-div .title').find('p').eq(0).find('span').html().trim()
            var brandName= $(target).parents('.list-div .title').find('p').eq(1).find('span').html().trim()
            var telephone= $(target).parents('.list-div .title').find('p').eq(2).find('span').html().trim()
            var isRef= $(target).parents('.list-div .title').find('p').eq(3).find('span').eq(0).html().trim() === '是' ? 1 : 2
            var isWalletPay= $(target).parents('.list-div .title').find('p').eq(4).find('span').html().trim() === '是' ? 1 : 2
            var textInfo= $(target).parents('.list-div .title').find('.pTextInfo').attr('data-text').trim() //获取充电说明信息
            var isGrad= true //为true 的时候隐藏	
            var common2= $(target).parents('.list-div .title').find('p').eq(3).find('span').eq(1).attr('data-val')
            
        	var permit= parseInt(isRef) === 0 ? 2 :  parseInt(isRef)
        	var walletpay = parseInt(isWalletPay) === 0 ? 2 :  parseInt(isWalletPay)
            var id= $(target).attr('data-id').trim()
        	reqParmasFromScan= {
        			id: id,
	                name: temNmae,
	                remark: brandName,
	                permit: isRef,
	                walletpay: walletpay,
	                common1: telephone,
	                common2: common2,
	                chargeInfo: '' //后面赋值给他
	            }
            console.log($('.pTextInfo'+id))
            $('#textArea').val( $('.pTextInfo'+id).attr('data-text'))
            $('.chargeTextMask').fadeIn()
        	
        }

    })
    
    
     /**点击充电说明，弹出弹出框*/
	     $('.scanTextInfo').click(function(e){
	    	 $('#textArea').val($(this).attr('data-text').trim())
	    	 $('.chargeTextMask').fadeIn()
	    	 
	     })
	     /*点击选择模板*/
	     $('.chargetemo').click(function(){
	    	 var _this= this
	    	 //点击的时候发送请求，获取后台返回过来的模板
	    	 var code= $('body').attr('data-arecode').trim()
	    	 var id= parseInt($(targetEle).attr('data-id'))
	    	 if(code && code.length > 0){ //存在code时走去请求后台，获取模板
	    		 $.ajax({
		    		 url: '/merchant/editTempForInfo',
		    		 type: 'post',
		    		 data: {
		    			tempid: id,
		    			code: code
		    		 },
		    		 success: function(res){
		    			 if(res.code == 200){
		    				 mui.confirm('自动说明会覆盖当前正在使用的说明，确定使用自动说明吗？','提示',function(opations){
		    		    		 if(opations.index === 1){
		    		    			 $('#textArea').val(res.resultinfo)
		    		    		 }
		    		    	 }) 
		    			 }else{
		    				 mui.toast(res.message,{ duration:'1500', type:'div' })
		    			 }
		    		 },
		    		 error: function(err){
		    			 mui.toast('获取分功率模板失败！',{ duration:'1500', type:'div' })
		    		 }
		    	 })
	    	 }else{ //不存在code是走默认的
	    		 mui.confirm('自动说明会覆盖当前正在使用的说明，确定使用自动说明吗？','提示',function(opations){
		    		 if(opations.index === 1){
		    			 $('#textArea').val($(_this).attr('data-text').trim()) 
		    		 }
		    	 }) 
	    	 }
	    	
	     })
	     $('.chargetemR').click(function(){
	    	  var _this= this
	    	  mui.confirm('默认说明会覆盖当前正在使用的说明，确定使用默认说明吗？','提示',function(opations){
	    		 if(opations.index === 1){
	    			 $('#textArea').val($(_this).attr('data-text').trim()) 
	    		 }
	    	 }) 
	    	  
	     })
	     /*点击关闭弹框*/
	     $('.closeText').click(function(){
	    	 if($('.tem-mask2').css('display')=='block'){ //这个是在点击主模板编辑的时候取消的
	    		 $('#textArea').val($('.scanTextInfo').attr('data-text').trim())
	    	 }else{//这个是在点击最外层查看的时候取消的
	    		 /*不做处理*/
	    	 }
	    	 $('.chargeTextMask').fadeOut()
	     })
	     /*点击提交弹框*/
	     $('.submitText').click(function(){ //点击提交输入的文本域信息
	    	 var textInfo= $('#textArea').val().trim()
	    	 if(textInfo.length >= 100){
	    		 mui.alert('输入的文字不能超过100个字符','提示',function(){
	    			 
	    		 })
	    		 return
	    	 }
	    	 textInfo= textInfo.replace(/\</g,'&lt') //过滤到<
			 textInfo= textInfo.replace(/\>/g,'&gt')//过滤到>
			 console.log(textInfo)
	    	 if($('.tem-mask2').css('display')=='block'){ //这个是在点击主模板编辑的时候取消的
	    		 $('.scanTextInfo').attr('data-text',textInfo)
	    	 }else{//这个是在点击最外层查看的时候取消的
	    		 /*发送ajax,成功的时候，修改标签上的data-text*/
	    		 reqParmasFromScan.chargeInfo= textInfo
	    		 $.ajax({
	                 data: reqParmasFromScan,
	                 url : "/wctemplate/updatestaircharge",
	                 type : "POST",
	                 cache : false,
	                 success : function(e){
	                    console.log(e)
	                    if(e.code === 200){
	                    	$('.pTextInfo'+reqParmasFromScan.id).attr('data-text',reqParmasFromScan.chargeInfo);
	                    }else {
	                    	mui.toast('编辑失败')
	                    }
	                 },//返回数据填充
	             });
	    		 
	    	 }
	    	 /*这里避免XSS攻击，进行标签过滤*/
			
			 /*value= value.replace(/\n/ig,'<br/>')
			 value= value.replace(/\r\n/ig,'<br/>')*/
	    	 $('.chargeTextMask').fadeOut()
	     })
	     
	     
    
    

    $('.tem1 .list-center1').click(function(e){
        e= e || window.event
        e.stopPropagation()
    })
    $('.tem1 .tem-mask1').click(close)
    $('.tem1 .list-center1 .close').click(close)
    $('.tem1 .list-center1 .close2').click(close)
    function close (e) {
        e= e || window.event
        e.stopPropagation()
        console.log('点击了')
        $('.tem1 .tem-mask1').fadeOut()
    }

    $('.tem1 .list-center2').click(function(e){ //组织阻止冒泡，防止点击了自身隐藏
        e= e || window.event
        e.stopPropagation()
    })
    $('.tem1 .tem-mask2').click(close2)
    $('.tem1 .list-center2 .close').click(close2)
    $('.tem1 .list-center2 .close2').click(close2)
    function close2 (e) {
        e= e || window.event
        e.stopPropagation()
        $('.tem1 .tem-mask2').fadeOut()
    }

    $('.tem1 .list-center1 .submit').click(function (e) { //点击修改电子模板提交
        e =e || window.event
        e.stopPropagation()
        var reg= /^\d+(\.\d+)?$/
        var nameVal= $('.tem1 .list-center1 input[name=name]').val().trim()
        var parseVal= $('.tem1 .list-center1 input[name=parse]').val().trim()
        var timeVal= $('.tem1 .list-center1 input[name=time]').val().trim()
        var powerVal= $('.tem1 .list-center1 input[name=power]').val().trim()
        if(nameVal.length <= 0){
            mui.toast('请输入模板名称',{ duration:'1500', type:'div' })
            return false
        }
        if(parseVal.length <= 0){
            mui.toast('请输入充电价格',{ duration:'1500', type:'div' })
            return false
        }
        if(!reg.test(parseVal)) {
            mui.toast('充电价格请输入数字',{ duration:'1500', type:'div' })
            return false
        }
        if(timeVal.length <= 0){
            mui.toast('请输入充电时间',{ duration:'1500', type:'div' })
            return false
        }
        if(!reg.test(timeVal)) {
            mui.toast('充电时间请输入数字',{ duration:'1500', type:'div' })
            return false
        }
        if(powerVal.length <= 0){
            mui.toast('请输入消耗电量',{ duration:'1500', type:'div' })
            return false
        }
        if(!reg.test(powerVal)) {
            mui.toast('消耗电量请输入数字',{ duration:'1500', type:'div' })
            return false
        }
        //修改子模板
            //发送ajax讲修改之后的数据传输到服务器=====================
//        var flag= $('.tem-mask1 h3').text().trim() === '修改电子模板' ?  true :  false
      
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
                    
                 },//返回数据填充
             });

             
             var parentEle= $(targetEle).parent().parent()
             console.log(parentEle)
             parentEle.find('p').eq(0).find('span').html(nameVal)
             parentEle.find('p').eq(1).find('span').html(parseVal+'<b>元</b>')
             parentEle.find('p').eq(2).find('span').html(timeVal+'<b>分钟</b>')
             parentEle.find('p').eq(3).find('span').html(powerVal+'<b>度</b>')
        
           
        $('.tem1 .tem-mask1').fadeOut()
    })

    $('.tem1 .list-center2 .submit').click(function(e){
        e =e || window.event
        e.stopPropagation()
        var temNmaeVal= $('.tem1 .list-center2 input[name=temNmae]').val().trim()
        var brandNameseVal= $('.tem1 .list-center2 input[name=brandName]').val().trim()
        var telephoneVal= $('.tem1 .list-center2 input[name=telephone]').val().trim()
        var isRefVal= $('.tem1 .list-center2 input[name="isRef"]:checked').val()
        var isWalletPayVal= $('.tem1 .list-center2 input[name="isWalletPay"]:checked').val()
        var isIfalipay= $('.tem1 .list-center2 input[name="isIfalipay"]:checked').val() 
        var refReg= $('.tem1 input[name="refReg"]:checked').val() //退费标准
        var isgradVal= $('.tem1 input[name="isGrad"]:checked').val() 
        var grad= parseInt(isgradVal) ? parseInt(isgradVal) : 2
        var chargeInfo= $('.tem1 .list-center2 .scanTextInfo').attr('data-text').trim() 
        var str1= ''
            switch(parseInt(refReg)){
                case 1: str1= '(退费标准：时间和电量最小)'; break;
                case 2: str1= '(退费标准：根据时间)'; break;
                case 3: str1= '(退费标准：根据电量)'; break;
            }
       
        var permit= parseInt(isRefVal) === 0 ? 2 :  parseInt(isRefVal)
        var walletpay = parseInt(isWalletPayVal) === 0 ? 2 :  parseInt(isWalletPayVal)
        var ifalipay = parseInt(isIfalipay) === 0 ? 2 :  parseInt(isIfalipay)
        if(temNmaeVal.length <= 0){
            mui.toast('请输入模板名称',{ duration:'1500', type:'div' })
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
                    common2	: refReg,
                    chargeInfo: chargeInfo,
                    ifalipay:ifalipay
                },
                url : "/wctemplate/updatestaircharge",
                type : "POST",
                cache : false,
                success : function(e){
                   
                },//返回数据填充
            });


            var parentEle= $(targetEle).parent().parent()
            parentEle.find('p').eq(0).find('span').html(temNmaeVal)
            parentEle.find('p').eq(1).find('span').html(brandNameseVal)
            parentEle.find('p').eq(2).find('span').html(telephoneVal)
            var isRefHtml= parseInt(isRefVal) ? '是' : '否'
            parentEle.find('p').eq(3).find('span').eq(0).html(isRefHtml)
            
            parentEle.find('p').eq(3).find('span').eq(1).text(str1)
            parentEle.find('p').eq(3).find('span').eq(1).attr('data-val',refReg)
            console.log(isRefVal)
            if(!parseInt(isRefVal)){
               parentEle.find('p').eq(3).find('span').eq(1).fadeOut()
            }else{
                parentEle.find('p').eq(3).find('span').eq(1).fadeIn()
            }
            
            parentEle.find('p').eq(3).find('span').eq(0).removeClass('span-green span-red')
            if(parseInt(isRefVal)){
                parentEle.find('p').eq(3).find('span').eq(0).addClass('span-green')
            }else {
                parentEle.find('p').eq(3).find('span').eq(0).addClass('span-red')
            }

            var isWalletPayHtml= parseInt(isWalletPayVal) ? '是' : '否'
            parentEle.find('p').eq(4).find('span').html(isWalletPayHtml)
            parentEle.find('p').eq(4).find('span').removeClass('span-green span-red')
            if(parseInt(isWalletPayVal)){
                parentEle.find('p').eq(4).find('span').addClass('span-green')
            }else {
                parentEle.find('p').eq(4).find('span').addClass('span-red')
            }
            
            var isIfalipayHtml= parseInt(isIfalipay) ? '是' : '否'
            parentEle.find('.ifalipay').find('span').html(isIfalipayHtml)
            parentEle.find('.ifalipay').find('span').removeClass('span-green span-red')
            if(parseInt(isIfalipay)){
            	parentEle.find('.ifalipay').find('span').addClass('span-green')
            }else {
            	parentEle.find('.ifalipay').find('span').addClass('span-red')
            }
            
            parentEle.find('.pTextInfo').attr('data-text',chargeInfo)
            
        }else { //添加新模板
            //发送ajax将新增的数据传输到服务器=====================

           
            $.ajax({
                data:{
                    name:temNmaeVal,
                    remark: brandNameseVal,
                    permit: permit,
                    walletpay: walletpay,
                    common1: telephoneVal,
                    common2	: refReg,
                    status: 0,
                    grade: grad,
                    chargeInfo: chargeInfo,
                    ifalipay: ifalipay
                },
          /*   url : "/wctemplate/addstaircharge",*/
                url : "/wctemplate/insertstairtempval",
                type : "POST",
                cache : false,
                success : function(e){
                	if(e.code === 101){
                		mui.toast('添加的模板名称已存在，请更改！',{ duration:'1500', type:'div' })
                    }else if(e.code === 100){
                    	mui.toast('登录已过期，请重新登录！',{ duration:'1500', type:'div' });
                    	handleAddTem(e)
                    }else if(e.code === 200){
                    	if(e.code == 200){
                    		console.log($('body').attr('data-reload').trim())
                    		if($('body').attr('data-reload').trim() == 1){ //说明从/merchant/addchargeTem页面进来，添加充公之后需要重新刷新页面
                    			var tempid= e.data.length <= 1 ? e.data[0].id : e.data;
                    			window.location.href= window.location.pathname+window.location.search+'&tempid='+tempid
                    			
                    		}else{ //不需要重新加载页面
                    			if(e.data.length <=1){
                        			handleAddTem(e.data[0].id)
                        		}else{
                        			 handleAddTem2(e.data)
                        		}
                    		}
                    	}else if(e.code === 101){
                    		toastr.warning('添加的模板名称已存在，请更改！')
                    	}else if(e.code === 100){
                    		 toastr.warning('登录已过期，请重新登录！')
                    	}else if(e.code== 402){
                    		toastr.warning('模板名字重复!')
                    	}
                    	/*handleAddTem(e.temid)*/
                    }
                },//返回数据填充
            });

            function handleAddTem2(list){
            	var strHtml= ''
            	var firstId
            	$(list).each(function(i,item){
            		if(i=== 0){
            			firstId= item.id
            		}
            		var gradChildTem= ''
            		if(item.gather.length >0){
            			$(item.gather).each(function(j,jtem){
        				var liStr= `<li class="mui-table-view-cell">
	                        			<p>显示名称：<span>${jtem.name}</span></p>
				                        <p>充电价格：<span>${jtem.money} <b>元</b></span></p>
				                        <p>充电时间：<span>${jtem.chargeTime} <b>分钟</b></span></p>
				                        <p>消耗电量：<span>${jtem.chargeQuantity/100} <b>度</b></span></p>
				                        <div>
				                            <button type="button" class="mui-btn mui-btn-success tem-edit" data-id="${jtem.id}">编辑</button>
				                            <button type="button" class="mui-btn mui-btn-success tem-delete" data-id="${jtem.id}">删除</button>
				                        </div>
			                        </li>`
			                        gradChildTem+= liStr
            			})
            		}
            		
            		var str= `
		            		<div class="list-div">
            					<div class="temGradName">分等级模板${item.rank == 1? '一' : item.rank == 2 ? '二' : '三'}</div>
		            	    	<input type="hidden" class="common2" value="1">
		            	    	<input type="hidden" class="default" value="0">
		                        <li>
		                            <div class="title">
		                                <p>模板名称：<span>${item.name}</span></p>
		                                <p>品牌名称：<span>${item.remark}</span></p>
		                                <p>售后电话：<span>${item.common1}</span></p>
		                                <p>是否支持退费：
		                                	<span class="${item.permit == 1 ? 'span-green' : 'span-red'}">${item.permit == 1 ? '是' : '否'}</span>
		                           			<span data-val="${item.common2}" style="${item.permit == 1 ? '': 'display: none;'}">${item.common2 == 1 ? '(退费标准：时间和电量最小)' : item.common2 == 2 ? '(退费标准：根据时间)': item.common2 == 3 ? '(退费标准：根据电量)' : ''}</span>
		                               </p>
		                                <p>是否钱包强制支付：
		                                	<span class="${item.walletpay == 1 ? 'span-green' : 'span-red'}">${item.walletpay == 1 ? '是' : '否'}</span>
		                               	</p>
		                               	<p class="pTextInfo pTextInfo${item.id}" data-text="${item.chargeInfo}">充电说明： <button type="button" class="mui-btn mui-btn-success tem-title-text" data-id="${item.id}">查看</button>	
		                               		                   	</p>
		                                <div>
		                                    <button type="button" class="mui-btn mui-btn-success tem-title-edit" data-id="${item.id}">编辑</button>
		                                    <button type="button" class="mui-btn mui-btn-success tem-title-delete" data-id="${item.id}" disabled>删除</button>
		                                </div>
		                            </div>
		                            <ul class="mui-table-view">
		                            	${gradChildTem}
		                                <li class="mui-table-view-cell bottom">
		                                    <button type="button" class="mui-btn mui-btn-success  mui-btn-outlined addBut" data-id="${item.id}">添加</button>
		                                </li>
		                            </ul>     
		                        </li>
		                    </div>`
		                    
		                    strHtml+= str
            		
            	})
            	var liStr= ''
    			if(sourceNum != 0){
    				liStr= `<li class="mui-table-view-cell bottom">
	        	    	<div class="isChecked">
	        	            <button type="button" class="mui-btn mui-btn-success selectTem" data-id="${firstId}">选择模板</button>
	        	        </div>
	        	        <p>选中模板</p>      
	                 </li>`
    			}else{
    				liStr= `<li class="mui-table-view-cell bottom">
	        	    	<div class="isChecked">
	        	            <button type="button" class="mui-btn mui-btn-success defaultTem" data-id="${firstId}">设为默认</button>
	        	        </div>
	        	        <p>默认模板</p>      
	                 </li>`
    			}
            		var divObj= $('<div class="list-div gradTem"></div>')
            		divObj.html(strHtml+liStr)
				    $('.tem1 .tem')[0].insertBefore(divObj[0],$('.tem nav')[0])
            }
            
            function handleAddTem(id){
            		var isRefHtml= parseInt(isRefVal) ? '是' : '否'
                    var isWalletPayHtml= parseInt(isWalletPayVal) ? '是' : '否'
                    var isIfalipayHtml= parseInt(isIfalipay) ? '是' : '否'
                    var isRefClass= parseInt(isRefVal) ? 'span-green' : 'span-red'
                    var isWalletPayClass= parseInt(isWalletPayVal) ? 'span-green' : 'span-red'
                    var isIfalipayClass= parseInt(isIfalipay) ? 'span-green' : 'span-red'
                    		var defauleOrSelectTem= ''
                    		if(sourceNum != 0){//判断是从哪进模板的
                    			defauleOrSelectTem=`<div class="isChecked">
                                <button type="button" class="mui-btn mui-btn-success selectTem" data-id="${id}">选择模板</button>
	                            </div>
	                            <p>选中模板</p>`
	                    		}else{
	                    			defauleOrSelectTem= `<div class="isChecked">
	                                <button type="button" class="mui-btn mui-btn-success defaultTem" data-id="${id}">设为默认</button>
	                                </div>
	                                <p>默认模板</p>`
	                    		}
                    	
                    	if(!parseInt(isRefVal)){ //当为否定是
                            var str= `
                           <li>
                           <div class="title">
                           <p>模板名称：<span>${temNmaeVal}</span></p>
                       <p>品牌名称：<span>${brandNameseVal}</span></p>
                       <p>售后电话：<span>${telephoneVal}</span></p>
                       <p>是否支持退费：<span class="${isRefClass}">${isRefHtml}</span><span></span></p>
                       <p>是否钱包强制支付：<span class="${isWalletPayClass}">${isWalletPayHtml}</p>
                       <p class="pTextInfo pTextInfo${id}" data-text="${chargeInfo}">充电说明： <button type="button" class="mui-btn mui-btn-success tem-title-text" data-id="${id}">查看</button>	
                       <p class="ifalipay">是否支持支付宝充电：<span class="${isIfalipayClass}">${isIfalipayHtml}</span></p>
                       <div>
                       <button type="button" class="mui-btn mui-btn-success tem-title-edit" data-id="${id}">编辑</button>
                           <button type="button" class="mui-btn mui-btn-success tem-title-delete" data-id="${id}">删除</button>
                           </div>
                           </div>
                           <ul class="mui-table-view">
                           <li class="mui-table-view-cell bottom">${defauleOrSelectTem}
                           <button type="button" class="mui-btn mui-btn-success mui-btn-outlined addBut" data-id="${id}">添加</button>
                           </li>
                           </ul>
                           </li>
                           `
                       }else{
                            var str= `
                           <li>
                           <div class="title">
                           <p>模板名称：<span>${temNmaeVal}</span></p>
                       <p>品牌名称：<span>${brandNameseVal}</span></p>
                       <p>售后电话：<span>${telephoneVal}</span></p>
                       <p>是否支持退费：<span class="${isRefClass}">${isRefHtml}</span><span data-val= ${refReg} >${str1}</span></p>
                       <p>是否钱包强制支付：<span class="${isWalletPayClass}">${isWalletPayHtml}</p>
                       <p class="pTextInfo pTextInfo${id}" data-text="${chargeInfo}">充电说明： <button type="button" class="mui-btn mui-btn-success tem-title-text" data-id="${id}">查看</button>	
                       <p class="ifalipay">是否支持支付宝充电：<span class="${isIfalipayClass}">${isIfalipayHtml}</span></p>
                       <div>
                       <button type="button" class="mui-btn mui-btn-success tem-title-edit" data-id="${id}">编辑</button>
                           <button type="button" class="mui-btn mui-btn-success tem-title-delete" data-id="${id}">删除</button>
                           </div>
                           </div>
                           <ul class="mui-table-view">
                           <li class="mui-table-view-cell bottom">${defauleOrSelectTem}
                           <button type="button" class="mui-btn mui-btn-success mui-btn-outlined addBut" data-id="${id}">添加</button>
                           </li>
                           </ul>
                           </li>
                           `
                       }
                    var div= $('<div class="list-div"></div>')
                    div.html(str)
                    $('.tem1 .tem')[0].insertBefore(div[0],$('.tem nav')[0])
            }
        }
        $('.tem1 .tem-mask2').fadeOut()
//        window.location.href= window.location.href
    })


    function renderList(obj){ //渲染list-content
    	console.log(obj)
        $('.tem1 .list-center1 h3').html(obj.title)
        $('.tem1 .list-center1 input[name=name]').val(obj.name)
        $('.tem1 .list-center1 input[name=parse]').val(obj.parse)
        $('.tem1 .list-center1 input[name=time]').val(obj.time)
        $('.tem1 .list-center1 input[name=power]').val(obj.power)
        $('.tem1 .tem-mask1').fadeIn()
    }
    function renderList2(obj){
        $('.tem1 .list-center2 h3').html(obj.title)
        $('.tem1 .list-center2 input[name=temNmae]').val(obj.temNmae)
        $('.tem1 .list-center2 input[name=brandName]').val(obj.brandName)
        $('.tem1 .list-center2 input[name=telephone]').val(obj.telephone)
        $('.tem1 .list-center2 .scanTextInfo').attr('data-text',obj.textInfo)
        if(obj.isRef){
            $('.tem1 .list-center2 input[name=isRef]').eq(0).prop('checked',true)
            console.log( $('.tem1 input[name=refReg]').eq(0))
            var str= ''
                switch(obj.regVal){
                    case 1: str= '(默认)'; break;
                    case 2: str= '(时间)'; break;
                    case 3: str= '(电量)'; break;
                }
                $('#spanList').text(str)
        }else{
        	 $('#spanList').text('')
            $('.tem1 .list-center2 input[name=isRef]').eq(1).prop('checked',true)
        }
        if(obj.isWalletPay){
            $('.tem1 .list-center2 input[name=isWalletPay]').eq(0).prop('checked',true)
        }else{
            $('.tem1 .list-center2 input[name=isWalletPay]').eq(1).prop('checked',true)
        }
        
        if(obj.ifalipay){
            $('.tem1 .list-center2 input[name=isIfalipay]').eq(0).prop('checked',true)
        }else{
            $('.tem1 .list-center2 input[name=isIfalipay]').eq(1).prop('checked',true)
        }
        
        $('.tem1 .list-center2 input[name=isGrad]').eq(1).prop('checked',true)
         $('.tem1 .list-center2 input[name=isGrad]').prop('disabled',false)
        if(obj.isGrad){
            $('.tem1 .list-center2 input[name=isGrad]').prop('disabled',true)
        }
        $('.tem1 .tem-mask2').fadeIn()
    }
   }
   tem1()

// =======
    function tem2 (){
	   var sourceNum= parseInt($('body').attr('data-source'))
         var targetEle= null
    $('.tem2 .tem').click(function (e) {
        e =e || window.event
        var target= e.target || e.srcElement
        targetEle= target
      
        if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('tem-edit')){ //点击编辑
            //这一步发送ajax获取数据,或者在元素身上找到绑定的数据，讲数据处理为下面的obj格式的，并将数据穿进去
            var name= $(target).parent().parent().find('p').eq(0).find('span').html().trim()
            var parse= $(target).parent().parent().find('p').eq(1).find('span')[0].childNodes[0].textContent.trim()
            var totalParse= $(target).parent().parent().find('p').eq(2).find('span')[0].childNodes[0].textContent.trim()
            var obj= {  //这里是从后台获取的数据或者从元素上获取的（这里模拟后台数据）
                title: '修改离线子模板',
                name: name,
                parse: parse,
                totalParse: totalParse
           }
            renderList(obj)
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('tem-delete')){
        	   var isSelectTem= $(target).parent().parent().parent().parent().parent().hasClass('borShadow')
               if(isSelectTem){
                 mui.toast('被选择的子模板不能删除',{ duration:'1500', type:'div' })
                 return false
               }
            mui.confirm('确定删除?', function (type) {
                if(type.index){ //删除
                    $(target).parent().parent().remove()
                    // =============================发送 ajax 提交数据 提交删除元素的数据
                    
                    var id= parseInt($(targetEle).attr('data-id'))
                     $.ajax({
                        data:{
                            id: id
                        },
                        url : "/wctemplate/deletesubclassoffline",
                        type : "POST",
                        cache : false,
                        success : function(e){
                           
                        }
                    });
                }
            })
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('addBut')){
            var $list= $(target).parent().prev()
            if($list.length <= 0){ //没有子节点
                //这里是默认设置
                var nextParse= 30
                var nextTotalParse= 31
                var nextSendParse= nextTotalParse- nextParse
                var nextName= nextParse+'元送'+nextSendParse

            }else { //找到上一个子节点
                var reg= /(?!(0[0-9]{0,}$))[0-9]{1,}[.]{0,}[0-9]{0,}/g
                var parse= $list.find('p').eq(1).find('span').text().match(reg)[0]-0
                var TotalParse= $list.find('p').eq(2).find('span').text().match(reg)[0]-0
                console.log(TotalParse, parse)
                var nextParse= parse * 2
                var nextSendParse= (TotalParse- parse) * 2
                var nextTotalParse= nextParse+ nextSendParse
                var nextName= nextParse+'元送'+nextSendParse
            }
            //发送ajax将新增的数据传输到服务器=====================
            
            var id= parseInt($(targetEle).attr('data-id'))
             $.ajax({
                data:{
                    id: id,
                    name: nextName,
                    money: nextParse,
                    remark: nextTotalParse
                },
                url : "/wctemplate/addsubclassoffline",
                type : "POST",
                cache : false,
                success : function(e){
                	if(e.code=== 100){
               		 mui.toast('登录过期，请重新登录',{ duration:'1500', type:'div' }) 
	               	}else if(e.code=== 101){
	               		mui.toast('显示名称重复，请修改后重试',{ duration:'1500', type:'div' })
	               	}else if(e.code=== 102){
	               		mui.toast('金额过大，请修改后重试',{ duration:'1500', type:'div' })
	               	}else if(e.code === 200){
	               		var str= '<p>显示名称：<span>'+ nextName +'</span></p> <p>付款金额：<span>'+nextParse+'<b>元</b></span></p> <p>充卡金额：<span>'+nextTotalParse+'<b>元</b></span></p> <div> <button type="button" class="mui-btn mui-btn-success tem-edit" data-id='+e.ctemid+'>编辑</button> <button type="button" class="mui-btn mui-btn-success tem-delete" data-id='+e.ctemid+'>删除</button> </div>'
	                    var list= $('<li class="mui-table-view-cell"></li>')
	                    list.html(str)
	                    $(targetEle).parent().parent()[0].insertBefore(list[0],$(targetEle).parent()[0])
	               	}
                }
            });
            
            
           
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('tem-title-edit')){ //点击模板编辑按钮
            //$('.tem-mask2').fadeIn()
            //这一步发送ajax获取数据,或者在元素身上找到绑定的数据，讲数据处理为下面的obj2格式的，并将数据穿进去

            var temNmae= $(target).parent().parent().find('p').eq(0).find('span').html().trim()
            var brandName= $(target).parent().parent().find('p').eq(1).find('span').html().trim()
            var telephone= $(target).parent().parent().find('p').eq(2).find('span').html().trim()


            var obj2= {
                title: '修改离线模板',
                temNmae: temNmae,
                brandName: brandName,
                telephone: telephone
            }
            renderList2(obj2)
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('addTemplate')){
            var obj2= {
                title: '新增离线模板',
                temNmae: '',
                brandName: '',
                telephone: ''
            }
            renderList2(obj2)
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('tem-title-delete')){
        	 var isSelectTem= $(target).parent().parent().parent().parent().hasClass('borShadow')
             if(isSelectTem){
               mui.toast('被选择的模板不能删除',{ duration:'1500', type:'div' })
               return false
             }
            mui.confirm('确定删除模板?', function (type) {
                if(type.index){ //删除
                    $(target).parent().parent().parent().parent().remove()
                    // =============================发送 ajax 提交数据 提交删除元素的数据
                    
                    var id= parseInt($(targetEle).attr('data-id'))
                     $.ajax({
                        data:{
                            id: id
                        },
                        url : "/wctemplate/deletestairoffline",
                        type : "POST",
                        cache : false,
                        success : function(e){
                           
                        }
                    });
                    
                }
            })
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('selectTem')){
            // 点击选择模板
        	console.log(11)
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
                            if(e == 1){
                                parent.siblings().removeClass('borShadow') //移除所有的兄弟节点的选择
                                parent.siblings().find('.bottom p').fadeOut()
                                parent.siblings().find('.selectTem').removeClass('active')
                                parent.addClass('borShadow')  //给当前元素添加节点
                                $(target).parent().parent().find('p').fadeIn()
                                $(target).addClass('active')
                                 //mui.toast('已选择当前模板',{ duration:'1500', type:'div' })
                            }
                         },//返回数据填充
                         error: function(){
                             mui.toast('选择离线模板失败，请稍后再试！',{ duration:'1500', type:'div' })
                         }
                     });

               
             }else{
                mui.toast('你已选择当前模板',{ duration:'1500', type:'div' })
             } 
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('defaultTem')){ 
       	 var parent= $(target).parent().parent().parent().parent().parent()
    	 var id= $(target).attr('data-id').trim() //模板id
    	 $.ajax({
             data:{
                 source: 1,
            	 temid: id
             },
             url : "/wctemplate/templatedefault",
             type : "POST",
             cache : false,
             success : function(e){
            	 console.log(e)
            	 if(e.code=== 100){
            		 mui.toast('登录过期，请重新登录',{ duration:'1500', type:'div' })
            	 }else if(e.code=== 200){
            		 parent.siblings().removeClass('borShadow') //移除所有的兄弟节点的选择
                     parent.siblings().find('.bottom p').fadeOut()
                     parent.siblings().find('.defaultTem').removeClass('active')
                     parent.addClass('borShadow')  //给当前元素添加节点
                     $(target).parent().parent().find('p').fadeIn()
                     $(target).addClass('active')
            	 }
               
             },//返回数据填充
             error: function(){
                 mui.toast('设为默认失败，请稍后再试！',{ duration:'1500', type:'div' })
             }
         });
    
    } 
    })

    $('.tem2 .list-center1').click(function(e){
        e= e || window.event
        e.stopPropagation()
    })
    $('.tem2 .tem-mask1').click(close)
    $('.tem2 .list-center1 .close').click(close)
    $('.tem2 .list-center1 .close2').click(close)
    function close (e) {
        e= e || window.event
        e.stopPropagation()
        console.log('点击了')
        $('.tem2 .tem-mask1').fadeOut()
    }

    $('.tem2 .list-center2').click(function(e){ //组织阻止冒泡，防止点击了自身隐藏
        e= e || window.event
        e.stopPropagation()
    })
    $('.tem2 .tem-mask2').click(close2)
    $('.tem2 .list-center2 .close').click(close2)
    $('.tem2 .list-center2 .close2').click(close2)
    function close2 (e) {
        e= e || window.event
        e.stopPropagation()
        $('.tem2 .tem-mask2').fadeOut()
    }

    $('.tem2 .list-center1 .submit').click(function (e) { //点击修改电子模板提交/添加的电子模板提交
        e =e || window.event
        e.stopPropagation()
        var reg= /^\d+(\.\d+)?$/
        var nameVal= $('.tem2 .list-center1 input[name=name]').val().trim()
        var parseVal= $('.tem2 .list-center1 input[name=parse]').val().trim()
        var totalParseVal= $('.tem2 .list-center1 input[name=totalParse]').val().trim()
        if(nameVal.length <= 0){
            mui.toast('请输入显示名称',{ duration:'1500', type:'div' })
            return false
        }
        if(parseVal.length <= 0){
            mui.toast('请输入付款金额',{ duration:'1500', type:'div' })
            return false
        }
        if(!reg.test(parseVal)){
            mui.toast('付款金额请输入数字',{ duration:'1500', type:'div' })
            return false
        }
        if(totalParseVal.length <= 0){
            mui.toast('请输入充卡金额',{ duration:'1500', type:'div' })
            return false
        }
        if(!reg.test(totalParseVal)){
            mui.toast('充卡金额请输入数字',{ duration:'1500', type:'div' })
            return false
        }
        var flag= $('.tem2 .list-center1 h3').html().trim() === '新增离线子模板' ? true : false
        //修改离线子模板
            //发送ajax讲修改之后的数据传输到服务器=====================
            
            var id= parseInt($(targetEle).attr('data-id'))
             $.ajax({
                data:{
                    id: id,
                    name:nameVal,
                    money: parseVal,
                    remark: totalParseVal
                },
                url : "/wctemplate/updatesubclassoffline",
                type : "POST",
                cache : false,
                success : function(e){
                   
                }
            });
            
            
            var parentEle= $(targetEle).parent().parent()
            console.log(parentEle)
            parentEle.find('p').eq(0).find('span').html(nameVal)
            parentEle.find('p').eq(1).find('span').html(parseVal+'<b>元</b>')
            parentEle.find('p').eq(2).find('span').html(totalParseVal+'<b>元</b>')
        $('.tem2 .tem-mask1').fadeOut()
    })

    $('.tem2 .list-center2 .submit').click(function(e){
        e =e || window.event
        e.stopPropagation()
        var temNmaeVal= $('.tem2 .list-center2 input[name=temNmae]').val().trim()
        var brandNameseVal= $('.tem2 .list-center2 input[name=brandName]').val().trim()
        var telephoneVal= $('.tem2 .list-center2 input[name=telephone]').val().trim()
        if(temNmaeVal.length <= 0){
            mui.toast('请输入模板名称',{ duration:'1500', type:'div' })
            return false
        }
        /*if(brandNameseVal.length <= 0){
            mui.toast('请输入品牌名称',{ duration:'1500', type:'div' })
            return false
        }
        if(telephoneVal.length <= 0){
            mui.toast('请输入售后电话',{ duration:'1500', type:'div' })
            return false
        }*/
        var flag= $('.tem2 .list-center2 h3').html().trim() === '修改离线模板' ? true : false
        if(flag){ // 修改离线模板
            //发送ajax将修改的数据传输到服务器=====================
            var id= parseInt($(targetEle).attr('data-id'))
             $.ajax({
                data:{
                    id: id,
                    name:temNmaeVal,
                    remark: brandNameseVal,
                    common1: telephoneVal
                },
                url : "/wctemplate/updatestairoffline",
                type : "POST",
                cache : false,
                success : function(e){
                   
                }
            });
            
            var parentEle= $(targetEle).parent().parent()
            parentEle.find('p').eq(0).find('span').html(temNmaeVal)
            parentEle.find('p').eq(1).find('span').html(brandNameseVal)
            parentEle.find('p').eq(2).find('span').html(telephoneVal)
        }else { //添加新模板
            //发送ajax将新增的数据传输到服务器=====================
            
             $.ajax({
                data:{
                    name:temNmaeVal,
                    remark: brandNameseVal,
                    common1: telephoneVal
                },
                url : "/wctemplate/addstairoffline",
                type : "POST",
                cache : false,
                success : function(e){
                	if(e.code === 101){
                		mui.toast('添加的模板名称已存在，请更改！',{ duration:'1500', type:'div' })
                    }else if(e.code === 100){
                    	mui.toast('登录已过期，请重新登录！',{ duration:'1500', type:'div' })
                    	handleAddTem(e)
                    }else if(e.code === 200){
                    			var defauleOrSelectTem= ''
                    			if(sourceNum != 0){//判断是从哪进模板的
	                    			defauleOrSelectTem=`<div class="isChecked">
	                                <button type="button" class="mui-btn mui-btn-success selectTem" data-id="${e.temid}">选择模板</button>
		                            </div>
		                            <p>选中模板</p>`
	                    		}else{
	                    			defauleOrSelectTem= `<div class="isChecked">
	                                <button type="button" class="mui-btn mui-btn-success defaultTem" data-id="${e.temid}">设为默认</button>
	                                </div>
	                                <p>默认模板</p>`
	                    		}
                    	 var str='<li><div class="title"><p>模板名称：<span>'+temNmaeVal+'</span></p><p>品牌名称：<span>'+brandNameseVal+'</span></p><p>售后电话：<span>'+ telephoneVal +'</span></p><div> <button type="button" class="mui-btn mui-btn-success tem-title-edit" data-id='+e.temid+'>编辑</button> <button type="button" class="mui-btn mui-btn-success tem-title-delete" data-id='+e.temid+'>删除</button> </div> </div> <ul class="mui-table-view"> <li class="mui-table-view-cell bottom">'+defauleOrSelectTem+' <button type="button" class="mui-btn mui-btn-success mui-btn-outlined addBut" data-id='+e.temid+'>添加</button> </li> </ul> </li> '
                         var div= $('<div class="list-div"></div>')
                         div.html(str)
                         $('.tem2 .tem')[0].insertBefore(div[0],$('.tem nav')[0])
                    }
                },//返回数据填充
            });
            
           
        }
        $('.tem2 .tem-mask2').fadeOut()
    })


    function renderList(obj){ //渲染list-content
        $('.tem2 .list-center1 h3').html(obj.title)
        $('.tem2 .list-center1 input[name=name]').val(obj.name)
        $('.tem2 .list-center1 input[name=parse]').val(obj.parse)
        $('.tem2 .list-center1 input[name=totalParse]').val(obj.totalParse)
        $('.tem2 .tem-mask1').fadeIn()
    }
    function renderList2(obj){
        $('.tem2 .list-center2 h3').html(obj.title)
        $('.tem2 .list-center2 input[name=temNmae]').val(obj.temNmae)
        $('.tem2 .list-center2 input[name=brandName]').val(obj.brandName)
        $('.tem2 .list-center2 input[name=telephone]').val(obj.telephone)
        $('.tem2 .tem-mask2').fadeIn()
    }
}
tem2()

// ================模板三

function tem3(){
	var sourceNum= parseInt($('body').attr('data-source'))
     var targetEle= null
    $('.tem3 .tem').click(function (e) {
        e =e || window.event
        var target= e.target || e.srcElement
        targetEle= target
        if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('tem-edit')){ //点击编辑
            //这一步发送ajax获取数据,或者在元素身上找到绑定的数据，讲数据处理为下面的obj格式的，并将数据穿进去
                
                var name= $(target).parent().parent().find('p').eq(0).find('span').html().trim()
                var coinNum= $(target).parent().parent().find('p').eq(1).find('span')[0].childNodes[0].textContent.trim()
                var totalParse= $(target).parent().parent().find('p').eq(2).find('span')[0].childNodes[0].textContent.trim()
            var obj= {  //这里是从后台获取的数据或者从元素上获取的（这里模拟后台数据）
                title: '修改投币子模板',
                name,
                coinNum,
                totalParse
            }
            renderList(obj)
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('tem-delete')){
        	   var isSelectTem= $(target).parent().parent().parent().parent().parent().hasClass('borShadow')
               if(isSelectTem){
                 mui.toast('被选择的子模板不能删除',{ duration:'1500', type:'div' })
                 return false
               }
            mui.confirm('确定删除?', function (type) {
                if(type.index){ //删除
                    $(target).parent().parent().remove()
                    // =============================发送 ajax 提交数据 提交删除元素的数据
                    
                    var id= parseInt($(targetEle).attr('data-id'))
                    $.ajax({
                        data:{
                            id: id
                        },
                        url : "/wctemplate/deletesubclassincoins",
                        type : "POST",
                        cache : false,
                        success : function(e){
                           
                        },//返回数据填充
                    });
                }
            })
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('addBut')){
            var $list= $(target).parent().prev()
            if($list.length <= 0){ //没有子节点
                //这里是默认设置
                var nextCoinNum= 1
                var nextTotalParse= 1
                var rate=  nextCoinNum / nextTotalParse  //得到的是一元几个币
      
                var nextName= nextCoinNum+'元'+nextTotalParse+'个币'

            }else { //找到上一个子节点
                var reg= /(?!(0[0-9]{0,}$))[0-9]{1,}[.]{0,}[0-9]{0,}/g
                var coinNum= $list.find('p').eq(1).find('span').text().match(reg)[0]-0
                var totalParse= $list.find('p').eq(2).find('span').text().match(reg)[0]-0
                var rate=   totalParse/coinNum  //得到的是一个币几元
                console.log(coinNum,totalParse,rate)
                var nextCoinNum= coinNum+1
                var nextTotalParse=  nextCoinNum * rate
                
                var nextName= nextTotalParse+'元'+nextCoinNum+'个币'
            }
            console.log(nextCoinNum,nextTotalParse,nextName)
             //发送ajax将新增的数据传输到服务器=====================
             
            var id= parseInt($(targetEle).attr('data-id'))
            $.ajax({
                data:{
                    id: id,
                    name:nextName,
                    remark: nextCoinNum,
                    money: nextTotalParse,
                },
                url : "/wctemplate/addsubclassincoins",
                type : "POST",
                cache : false,
                success : function(e){
                	if(e.code=== 100){
                  		 mui.toast('登录过期，请重新登录',{ duration:'1500', type:'div' }) 
   	               	}else if(e.code=== 101){
   	               		mui.toast('显示名称重复，请修改后重试',{ duration:'1500', type:'div' })
   	               	}else if(e.code=== 102){
   	               		mui.toast('金额过大，请修改后重试',{ duration:'1500', type:'div' })
   	               	}else if(e.code === 200){
   	               	 var str= '<p>显示名称：<span>'+ nextName +'</span></p><p>投币个数：<span>'+ nextCoinNum +'<b>个</b></span></p> <p>付款金额：<span>'+ nextTotalParse +'<b>元</b></span></p> <div> <button type="button" class="mui-btn mui-btn-success tem-edit" data-id='+e.ctemid+'>编辑</button> <button type="button" class="mui-btn mui-btn-success tem-delete"  data-id='+e.ctemid+'>删除</button> </div>'
	   	              var list= $('<li class="mui-table-view-cell"></li>')
	   	              list.html(str)
	   	              $(targetEle).parent().parent()[0].insertBefore(list[0],$(targetEle).parent()[0])
   	               	}
                },//返回数据填充
            });
             
           
           
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('tem-title-edit')){ //点击模板编辑按钮
            //$('.tem-mask2').fadeIn()
            //这一步发送ajax获取数据,或者在元素身上找到绑定的数据，讲数据处理为下面的obj2格式的，并将数据穿进去
            var temNmae= $(target).parent().parent().find('p').eq(0).find('span').html().trim()
            var brandName= $(target).parent().parent().find('p').eq(1).find('span').html().trim()
            var telephone= $(target).parent().parent().find('p').eq(2).find('span').html().trim()
            var isRef= 1;
            var isWalletPay= $(target).parent().parent().find('p').eq(3).find('span').html().trim() === '是' ? true : false
            /*var isRef= $(target).parent().parent().find('p').eq(3).find('span').html().trim() === '是' ? true : false*/
            var obj2= {
                title: '修改投币模板',
                temNmae,
                brandName,
                telephone,
                isRef,
                isWalletPay
            }
            renderList2(obj2)
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('addTemplate')){
            
            var obj2= {
                title: '新增投币模板',
                temNmae: '',
                brandName: '',
                telephone: '',
                isRef: true,
                isWalletPay: false
            }
            renderList2(obj2)
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('tem-title-delete')){
        	 var isSelectTem= $(target).parent().parent().parent().parent().hasClass('borShadow')
             if(isSelectTem){
               mui.toast('被选择的模板不能删除',{ duration:'1500', type:'div' })
               return false
             }
            mui.confirm('确定删除模板?', function (type) {
                if(type.index){ //删除
                    $(target).parent().parent().parent().parent().remove()
                    // =============================发送 ajax 提交数据 提交删除元素的数据
                var id= parseInt($(targetEle).attr('data-id'))
                $.ajax({
                    data:{
                        id: id
                    },
                    url : "/wctemplate/deletestairincoins",
                    type : "POST",
                    cache : false,
                    success : function(e){
                       
                    },//返回数据填充
                });
                }
            })
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
                            if(e == 1){
                                parent.siblings().removeClass('borShadow') //移除所有的兄弟节点的选择
                                parent.siblings().find('.bottom p').fadeOut()
                                parent.siblings().find('.selectTem').removeClass('active')
                                parent.addClass('borShadow')  //给当前元素添加节点
                                $(target).parent().parent().find('p').fadeIn()
                                $(target).addClass('active')
                                 //mui.toast('已选择当前模板',{ duration:'1500', type:'div' })
                            }
                         },//返回数据填充
                         error: function(){
                             mui.toast('选择脉冲模板失败，请稍后再试！',{ duration:'1500', type:'div' })
                         }
                     });

               
             }else{
                mui.toast('你已选择当前模板',{ duration:'1500', type:'div' })
             }
            
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('defaultTem')){ 
       	 var parent= $(target).parent().parent().parent().parent().parent()
    	 var id= $(target).attr('data-id').trim() //模板id
    	 $.ajax({
             data:{
                 source: 2,
            	 temid: id
             },
             url : "/wctemplate/templatedefault",
             type : "POST",
             cache : false,
             success : function(e){
            	 console.log(e)
            	 if(e.code=== 100){
            		 mui.toast('登录过期，请重新登录',{ duration:'1500', type:'div' })
            	 }else if(e.code=== 200){
            		 parent.siblings().removeClass('borShadow') //移除所有的兄弟节点的选择
                     parent.siblings().find('.bottom p').fadeOut()
                     parent.siblings().find('.defaultTem').removeClass('active')
                     parent.addClass('borShadow')  //给当前元素添加节点
                     $(target).parent().parent().find('p').fadeIn()
                     $(target).addClass('active')
            	 }
               
             },//返回数据填充
             error: function(){
                 mui.toast('设为默认失败，请稍后再试！',{ duration:'1500', type:'div' })
             }
         });
    
    } 
    })

    $('.tem3 .list-center1').click(function(e){
        e= e || window.event
        e.stopPropagation()
    })
    $('.tem3 .tem-mask1').click(close)
    $('.tem3 .list-center1 .close').click(close)
    $('.tem3 .list-center1 .close2').click(close)
    function close (e) {
        e= e || window.event
        e.stopPropagation()
        console.log('点击了')
        $('.tem-mask1').fadeOut()
    }

    $('.tem3 .list-center2').click(function(e){ //组织阻止冒泡，防止点击了自身隐藏
        e= e || window.event
        e.stopPropagation()
    })
    $('.tem3 .tem-mask2').click(close2)
    $('.tem3 .list-center2 .close').click(close2)
    $('.tem3 .list-center2 .close2').click(close2)
    function close2 (e) {
        e= e || window.event
        e.stopPropagation()
        $('.tem3 .tem-mask2').fadeOut()
    }

    $('.tem3 .list-center1 .submit').click(function (e) { //点击修改电子模板提交/添加的电子模板提交
        e =e || window.event
        e.stopPropagation()
        var reg= /^\d+(\.\d+)?$/
        var nameVal= $('.tem3 .list-center1 input[name=name]').val().trim()
        var coinNumVal= $('.tem3 .list-center1 input[name=coinNum]').val().trim()
        var totalParseVal= $('.tem3 .list-center1 input[name=totalParse]').val().trim()
        if(nameVal.length <= 0){
            mui.toast('请输入显示名称',{ duration:'1500', type:'div' })
            return false
        }
        if(coinNumVal.length <= 0){
            mui.toast('请输入投币个数',{ duration:'1500', type:'div' })
            return false
        }
        if(!reg.test(coinNumVal)){
            mui.toast('投币个数请输入数字',{ duration:'1500', type:'div' })
            return false
        }
        if(totalParseVal.length <= 0){
            mui.toast('请输入付款金额',{ duration:'1500', type:'div' })
            return false
        }
        if(!reg.test(totalParseVal)){
            mui.toast('付款金额请输入数字',{ duration:'1500', type:'div' })
            return false
        }
        //修改离线子模板
            //发送ajax讲修改之后的数据传输到服务器=====================
            
            var id= parseInt($(targetEle).attr('data-id'))
            $.ajax({
                data:{
                    id: id,
                    name:nameVal,
                    money: totalParseVal,
                    remark: coinNumVal,
                },
                url : "/wctemplate/updatesubclassincoins",
                type : "POST",
                cache : false,
                success : function(e){
                   
                },//返回数据填充
            });
            
            var parentEle= $(targetEle).parent().parent()
            console.log(parentEle)
            parentEle.find('p').eq(0).find('span').html(nameVal)
            parentEle.find('p').eq(1).find('span').html(coinNumVal)
            parentEle.find('p').eq(2).find('span').html(totalParseVal)
        $('.tem-mask1').fadeOut()
    })

    $('.tem3 .list-center2 .submit').click(function(e){
        e =e || window.event
        e.stopPropagation()
        var temNmaeVal= $('.tem3 .list-center2 input[name=temNmae]').val().trim()
        var brandNameseVal= $('.tem3 .list-center2 input[name=brandName]').val().trim()
        var telephoneVal= $('.tem3 .list-center2 input[name=telephone]').val().trim()
        var isWalletPayVal= $('.tem3 .list-center2 input[name="isWalletPay"]:checked').val()
        var walletpay = parseInt(isWalletPayVal)
        /*var isRefVal= $('.tem3 .list-center2 input[name="isRef"]:checked').val()
        var permit= parseInt(isRefVal) === 0 ? 2 :  parseInt(isRefVal)*/
        if(temNmaeVal.length <= 0){
            mui.toast('请输入模板名称',{ duration:'1500', type:'div' })
            return false
        }
        var flag= $('.list-center2 h3').html().trim() === '修改投币模板' ? true : false
        if(flag){ // 修改主模板
            //发送ajax将修改的数据传输到服务器=====================
            
               var id= parseInt($(targetEle).attr('data-id'))
            $.ajax({
                data:{
                    id: id,
                    name:temNmaeVal,
                    remark: brandNameseVal,
                    permit: 1,
                    walletpay: walletpay,
                    common1: telephoneVal
                },
                url : "/wctemplate/updatestairincoins",
                type : "POST",
                cache : false,
                success : function(e){
                   
                },//返回数据填充
            });
            
            var parentEle= $(targetEle).parent().parent()
            parentEle.find('p').eq(0).find('span').html(temNmaeVal)
            parentEle.find('p').eq(1).find('span').html(brandNameseVal)
            parentEle.find('p').eq(2).find('span').html(telephoneVal)
            var isWalletPayHtml= parseInt(isWalletPayVal) == 1 ? '是' : '否'
            parentEle.find('p').eq(3).find('span').html(isWalletPayHtml)
            parentEle.find('p').eq(3).find('span').removeClass('span-green span-red')
            if(parseInt(isWalletPayVal) == 1){
                parentEle.find('p').eq(3).find('span').addClass('span-green')
            }else {
                parentEle.find('p').eq(3).find('span').addClass('span-red')
            }
           /* var isRefHtml= parseInt(isRefVal) ? '是' : '否'
            parentEle.find('p').eq(3).find('span').html(isRefHtml)
            parentEle.find('p').eq(3).find('span').removeClass('span-green span-red')
            if(parseInt(isRefVal)){
                parentEle.find('p').eq(3).find('span').addClass('span-green')
            }else {
                parentEle.find('p').eq(3).find('span').addClass('span-red')
            }*/
        }else { //添加主模板
            //发送ajax将新增的数据传输到服务器=====================
            
              $.ajax({
                data:{
                    name:temNmaeVal,
                    remark: brandNameseVal,
                    permit: 1,
                    walletpay: walletpay,
                    common1: telephoneVal
                },
                url : "/wctemplate/addstairincoins",
                type : "POST",
                cache : false,
                success : function(e){
                	if(e.code === 101){
                		mui.toast('添加的模板名称已存在，请更改！',{ duration:'1500', type:'div' })
                    }else if(e.code === 100){
                    	mui.toast('登录已过期，请重新登录！',{ duration:'1500', type:'div' })
                    	handleAddTem(e)
                    }else if(e.code === 200){
                    	handleAddTem(e.temid)
                    }
                },//返回数据填充
            });
            
         function handleAddTem(id){
        	 var defauleOrSelectTem= ''
     			if(sourceNum != 0){//判断是从哪进模板的
         			defauleOrSelectTem=`<div class="isChecked">
                     <button type="button" class="mui-btn mui-btn-success selectTem" data-id="${id}">选择模板</button>
                     </div>
                     <p>选中模板</p>`
         		}else{
         			defauleOrSelectTem= `<div class="isChecked">
                     <button type="button" class="mui-btn mui-btn-success defaultTem" data-id="${id}">设为默认</button>
                     </div>
                     <p>默认模板</p>`
         		}
        	  var walletpayStr= '否',
        	  	  walletpayClass='span-red'
        	  if(walletpay == 1){
        		  walletpayStr= '是'
        		  walletpayClass= 'span-green'	  
        	  }
        	  var str= `
		              <li>
		              <div class="title">
		              <p>模板名称：<span>${temNmaeVal}</span></p>
		          <p>品牌名称：<span>${brandNameseVal}</span></p>
		          <p>售后电话：<span>${telephoneVal}</span></p>
		          <p>是否钱包强制支付：<span class="${walletpayClass}">${walletpayStr}</span></p>
		          <div>
		          <button type="button" class="mui-btn mui-btn-success tem-title-edit" data-id="${id}">编辑</button>
		              <button type="button" class="mui-btn mui-btn-success tem-title-delete" data-id="${id}">删除</button>
		              </div>
		              </div>
		              <ul class="mui-table-view">
		              <li class="mui-table-view-cell bottom">${defauleOrSelectTem}
		              <button type="button" class="mui-btn mui-btn-success mui-btn-outlined addBut" data-id="${id}">添加</button>
		              </li>
		              </ul>
		              </li>
		              `
		          var div= $('<div class="list-div"></div>')
		          div.html(str)
		          $('.tem3 .tem')[0].insertBefore(div[0],$('.tem nav')[0])
	        }
        }
        $('.tem3 .tem-mask2').fadeOut()
    })


    function renderList(obj){ //渲染list-content
        $('.tem3 .list-center1 h3').html(obj.title)
        $('.tem3 .list-center1 input[name=name]').val(obj.name)
        $('.tem3 .list-center1 input[name=coinNum]').val(obj.coinNum)
        $('.tem3 .list-center1 input[name=totalParse]').val(obj.totalParse)
        $('.tem3 .tem-mask1').fadeIn()
    }
    function renderList2(obj){
        $('.tem3 .list-center2 h3').html(obj.title)
        $('.tem3 .list-center2 input[name=temNmae]').val(obj.temNmae)
        $('.tem3 .list-center2 input[name=brandName]').val(obj.brandName)
        $('.tem3 .list-center2 input[name=telephone]').val(obj.telephone)
        if(obj.isWalletPay){
            $('.tem3 .list-center2 input[name=isWalletPay]').eq(0).prop('checked',true)
        }else{
            $('.tem3 .list-center2 input[name=isWalletPay]').eq(1).prop('checked',true)
        }
        $('.tem3 .tem-mask2').fadeIn()
    }
}
 tem3()  
 // ==============下面是钱包模板js
 function wallet (){
	 var sourceNum= parseInt($('body').attr('data-source'))
         var targetEle= null
    $('.wallet .tem').click(function (e) {
    	console.log(256)
        e =e || window.event
        var target= e.target || e.srcElement
        targetEle= target
        if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('tem-edit')){ //点击编辑
            //这一步发送ajax获取数据,或者在元素身上找到绑定的数据，讲数据处理为下面的obj格式的，并将数据穿进去
            var name= $(target).parent().parent().find('p').eq(0).find('span').html().trim()
            var parse= $(target).parent().parent().find('p').eq(1).find('span')[0].childNodes[0].textContent.trim()
            var totalParse= $(target).parent().parent().find('p').eq(2).find('span')[0].childNodes[0].textContent.trim()
            var obj= {  //这里是从后台获取的数据或者从元素上获取的（这里模拟后台数据）
                title: '修改钱包子模板',
                name: name,
                parse: parse,
                totalParse: totalParse
           }
            renderList(obj)
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('tem-delete')){
        	   var isSelectTem= $(target).parent().parent().parent().parent().parent().hasClass('borShadow')
               if(isSelectTem){
                 mui.toast('被选择的子模板不能删除',{ duration:'1500', type:'div' })
                 return false
               }
            mui.confirm('确定删除?', function (type) {
                if(type.index){ //删除
                    $(target).parent().parent().remove()
                    // =============================发送 ajax 提交数据 提交删除元素的数据
                    
                    var id= parseInt($(targetEle).attr('data-id'))
                     $.ajax({
                        data:{
                            id: id
                        },
                        url : "/wctemplate/deletesubclasswwallet",
                        type : "POST",
                        cache : false,
                        success : function(e){
                           
                        }
                    });
                }
            })
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('addBut')){
            var $list= $(target).parent().prev()
            if($list.length <= 0){ //没有子节点
                //这里是默认设置
                var nextParse= 30
                var nextTotalParse= 31
                var nextSendParse= nextTotalParse- nextParse
                var nextName= nextParse+'元送'+nextSendParse

            }else { //找到上一个子节点
                var reg= /(?!(0[0-9]{0,}$))[0-9]{1,}[.]{0,}[0-9]{0,}/g
                var parse= $list.find('p').eq(1).find('span').text().match(reg)[0]-0
                var TotalParse= $list.find('p').eq(2).find('span').text().match(reg)[0]-0
                console.log(TotalParse, parse)
                var nextParse= parse * 2
                var nextSendParse= (TotalParse- parse) * 2
                var nextTotalParse= nextParse+ nextSendParse
                var nextName= ''
                if(nextSendParse){
                     nextName= nextParse+'元'+'送'+nextSendParse
                }else{
                     nextName= nextParse+'元'
                }
            }
            //发送ajax将新增的数据传输到服务器=====================
            
            var id= parseInt($(targetEle).attr('data-id'))
             $.ajax({
                data:{
                    id: id,
                    name: nextName,
                    money: nextParse,
                    remark: nextTotalParse
                },
                url : "/wctemplate/addsubclasswwallet",
                type : "POST",
                cache : false,
                success : function(e){
                	if(e.code=== 100){
                 		 mui.toast('登录过期，请重新登录',{ duration:'1500', type:'div' }) 
  	               	}else if(e.code=== 101){
  	               		mui.toast('显示名称重复，请修改后重试',{ duration:'1500', type:'div' })
  	               	}else if(e.code=== 102){
  	               		mui.toast('金额过大，请修改后重试',{ duration:'1500', type:'div' })
  	               	}else if(e.code === 200){
  	               	var str= '<p>显示名称：<span>'+ nextName +'</span></p> <p>付款金额：<span>'+nextParse+'<b>元</b></span></p> <p>到账金额：<span>'+nextTotalParse+'<b>元</b></span></p> <div> <button type="button" class="mui-btn mui-btn-success tem-edit" data-id='+e.ctemid+'>编辑</button> <button type="button" class="mui-btn mui-btn-success tem-delete" data-id='+e.ctemid+'>删除</button> </div>'
  	              var list= $('<li class="mui-table-view-cell"></li>')
  	              list.html(str)
  	              $(targetEle).parent().parent()[0].insertBefore(list[0],$(targetEle).parent()[0])
  	               	}
                }
            });
            
            
           
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('tem-title-edit')){ //点击模板编辑按钮
            //$('.tem-mask2').fadeIn()
            //这一步发送ajax获取数据,或者在元素身上找到绑定的数据，讲数据处理为下面的obj2格式的，并将数据穿进去

            var temNmae= $(target).parent().parent().find('p').eq(0).find('span').html().trim()
            var brandName= $(target).parent().parent().find('p').eq(1).find('span').html().trim()
            var telephone= $(target).parent().parent().find('p').eq(2).find('span').html().trim()


            var obj2= {
                title: '修改钱包主模板',
                temNmae: temNmae,
                brandName: brandName,
                telephone: telephone
            }
            renderList2(obj2)
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('addTemplate')){
            var obj2= {
                title: '新增钱包主模板',
                temNmae: '',
                brandName: '',
                telephone: ''
            }
            renderList2(obj2)
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('tem-title-delete')){
        	 var isSelectTem= $(target).parent().parent().parent().parent().hasClass('borShadow')
             if(isSelectTem){
               mui.toast('被选择的模板不能删除',{ duration:'1500', type:'div' })
               return false
             }
            mui.confirm('确定删除模板?', function (type) {
                if(type.index){ //删除
                    $(target).parent().parent().parent().parent().remove()
                    // =============================发送 ajax 提交数据 提交删除元素的数据
                    
                    var id= parseInt($(targetEle).attr('data-id'))
                     $.ajax({
                        data:{
                            id: id
                        },
                        url : "/wctemplate/deletestairwallet",
                        type : "POST",
                        cache : false,
                        success : function(e){
                           
                        }
                    });
                    
                }
            })
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
                            if(e == 1){
                                parent.siblings().removeClass('borShadow') //移除所有的兄弟节点的选择
                                parent.siblings().find('.bottom p').fadeOut()
                                parent.siblings().find('.selectTem').removeClass('active')
                                parent.addClass('borShadow')  //给当前元素添加节点
                                $(target).parent().parent().find('p').fadeIn()
                                $(target).addClass('active')
                                 //mui.toast('已选择当前模板',{ duration:'1500', type:'div' })
                            }
                         },//返回数据填充
                         error: function(){
                             mui.toast('选择钱包模板失败，请稍后再试！',{ duration:'1500', type:'div' })
                         }
                     });

               
             }else{
                mui.toast('你已选择当前模板',{ duration:'1500', type:'div' })
             }
            
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('defaultTem')){ 
       	 var parent= $(target).parent().parent().parent().parent().parent()
    	 var id= $(target).attr('data-id').trim() //模板id
    	 $.ajax({
             data:{
                 source: 3,
            	 temid: id
             },
             url : "/wctemplate/templatedefault",
             type : "POST",
             cache : false,
             success : function(e){
            	 console.log(e)
            	 if(e.code=== 100){
            		 mui.toast('登录过期，请重新登录',{ duration:'1500', type:'div' })
            	 }else if(e.code=== 200){
            		 parent.siblings().removeClass('borShadow') //移除所有的兄弟节点的选择
                     parent.siblings().find('.bottom p').fadeOut()
                     parent.siblings().find('.defaultTem').removeClass('active')
                     parent.addClass('borShadow')  //给当前元素添加节点
                     $(target).parent().parent().find('p').fadeIn()
                     $(target).addClass('active')
            	 }
               
             },//返回数据填充
             error: function(){
                 mui.toast('设为默认失败，请稍后再试！',{ duration:'1500', type:'div' })
             }
         });
    
    } 
    })

    $('.wallet .list-center1').click(function(e){
        e= e || window.event
        e.stopPropagation()
    })
    $('.wallet .tem-mask1').click(close)
    $('.wallet .list-center1 .close').click(close)
    $('.wallet .list-center1 .close2').click(close)
    function close (e) {
        e= e || window.event
        e.stopPropagation()
        console.log('点击了')
        $('.wallet .tem-mask1').fadeOut()
    }

    $('.wallet .list-center2').click(function(e){ //组织阻止冒泡，防止点击了自身隐藏
        e= e || window.event
        e.stopPropagation()
    })
    $('.wallet .tem-mask2').click(close2)
    $('.wallet .list-center2 .close').click(close2)
    $('.wallet .list-center2 .close2').click(close2)
    function close2 (e) {
        e= e || window.event
        e.stopPropagation()
        $('.wallet .tem-mask2').fadeOut()
    }

    $('.wallet .list-center1 .submit').click(function (e) { //点击修改电子模板提交/添加的电子模板提交
        e =e || window.event
        e.stopPropagation()
        var reg= /^\d+(\.\d+)?$/
        var nameVal= $('.wallet .list-center1 input[name=name]').val().trim()
        var parseVal= $('.wallet .list-center1 input[name=parse]').val().trim()
        var totalParseVal= $('.wallet .list-center1 input[name=totalParse]').val().trim()
        if(nameVal.length <= 0){
            mui.toast('请输入显示名称',{ duration:'1500', type:'div' })
            return false
        }
        if(parseVal.length <= 0){
            mui.toast('请输入付款金额',{ duration:'1500', type:'div' })
            return false
        }
        if(!reg.test(parseVal)){
            mui.toast('付款金额请输入数字',{ duration:'1500', type:'div' })
            return false
        }
        if(totalParseVal.length <= 0){
            mui.toast('请输入到账金额',{ duration:'1500', type:'div' })
            return false
        }
        if(!reg.test(totalParseVal)){
            mui.toast('到账金额请输入数字',{ duration:'1500', type:'div' })
            return false
        }
        var flag= $('.wallet .list-center1 h3').html().trim() === '新增钱包子模板' ? true : false
            //发送ajax讲修改之后的数据传输到服务器=====================
            
            var id= parseInt($(targetEle).attr('data-id'))
             $.ajax({
                data:{
                    id: id,
                    name:nameVal,
                    money: parseVal,
                    remark: totalParseVal
                },
                url : "/wctemplate/updatesubclasswwallet",
                type : "POST",
                cache : false,
                success : function(e){
                   
                }
            });
            
            
            var parentEle= $(targetEle).parent().parent()
            console.log(parentEle)
            parentEle.find('p').eq(0).find('span').html(nameVal)
            parentEle.find('p').eq(1).find('span').html(parseVal+'<b>元</b>')
            parentEle.find('p').eq(2).find('span').html(totalParseVal+'<b>元</b>')
        $('.wallet .tem-mask1').fadeOut()
    })

    $('.wallet .list-center2 .submit').click(function(e){
        e =e || window.event
        e.stopPropagation()
        var temNmaeVal= $('.wallet .list-center2 input[name=temNmae]').val().trim()
        var brandNameseVal= $('.wallet .list-center2 input[name=brandName]').val().trim()
        var telephoneVal= $('.wallet .list-center2 input[name=telephone]').val().trim()
        if(temNmaeVal.length <= 0){
            mui.toast('请输入模板名称',{ duration:'1500', type:'div' })
            return false
        }
        /*if(brandNameseVal.length <= 0){
            mui.toast('请输入品牌名称',{ duration:'1500', type:'div' })
            return false
        }
        if(telephoneVal.length <= 0){
            mui.toast('请输入售后电话',{ duration:'1500', type:'div' })
            return false
        }*/
        var flag= $('.wallet .list-center2 h3').html().trim() === '修改钱包主模板' ? true : false
        if(flag){ // 修改离线模板
            //发送ajax将修改的数据传输到服务器=====================
            var id= parseInt($(targetEle).attr('data-id'))
             $.ajax({
                data:{
                    id: id,
                    name:temNmaeVal,
                    remark: brandNameseVal,
                    common1: telephoneVal
                },
                url : "/wctemplate/updatestairwallet",
                type : "POST",
                cache : false,
                success : function(e){
                   
                }
            });
            
            var parentEle= $(targetEle).parent().parent()
            parentEle.find('p').eq(0).find('span').html(temNmaeVal)
            parentEle.find('p').eq(1).find('span').html(brandNameseVal)
            parentEle.find('p').eq(2).find('span').html(telephoneVal)
        }else { //添加新模板
            //发送ajax将新增的数据传输到服务器=====================
            
             $.ajax({
                data:{
                    name:temNmaeVal,
                    remark: brandNameseVal,
                    common1: telephoneVal
                },
                url : "/wctemplate/addstairwallet",
                type : "POST",
                cache : false,
                success : function(e){
                	if(e.code === 101){
                		mui.toast('添加的模板名称已存在，请更改！',{ duration:'1500', type:'div' })
                    }else if(e.code === 100){
                    	mui.toast('登录已过期，请重新登录！',{ duration:'1500', type:'div' })
                    	handleAddTem(e)
                    }else if(e.code === 200){
                    		var defauleOrSelectTem= ''
                			if(sourceNum != 0){//判断是从哪进模板的
                    			defauleOrSelectTem=`<div class="isChecked">
                                <button type="button" class="mui-btn mui-btn-success selectTem" data-id="${e.temid}">选择模板</button>
	                            </div>
	                            <p>选中模板</p>`
                    		}else{
                    			defauleOrSelectTem= `<div class="isChecked">
                                <button type="button" class="mui-btn mui-btn-success defaultTem" data-id="${e.temid}">设为默认</button>
                                </div>
                                <p>默认模板</p>`
                    		}
                    	var str='<li><div class="title"><p>模板名称：<span>'+temNmaeVal+'</span></p><p>品牌名称：<span>'+brandNameseVal+'</span></p><p>售后电话：<span>'+ telephoneVal +'</span></p><div> <button type="button" class="mui-btn mui-btn-success tem-title-edit" data-id='+e.temid+'>编辑</button> <button type="button" class="mui-btn mui-btn-success tem-title-delete" data-id='+e.temid+'>删除</button> </div> </div> <ul class="mui-table-view"> <li class="mui-table-view-cell bottom">'+defauleOrSelectTem+' <button type="button" class="mui-btn mui-btn-success mui-btn-outlined addBut" data-id='+e.temid+'>添加</button> </li> </ul> </li> '
			            var div= $('<div class="list-div"></div>')
			            div.html(str)
			            $('.wallet .tem')[0].insertBefore(div[0],$('.tem nav')[0])
                    }
                },//返回数据填充
            });
            
            
        }
        $('.wallet .tem-mask2').fadeOut()
    })


    function renderList(obj){ //渲染list-content
        $('.wallet .list-center1 h3').html(obj.title)
        $('.wallet .list-center1 input[name=name]').val(obj.name)
        $('.wallet .list-center1 input[name=parse]').val(obj.parse)
        $('.wallet .list-center1 input[name=totalParse]').val(obj.totalParse)
        $('.wallet .tem-mask1').fadeIn()
    }
    function renderList2(obj){
        $('.wallet .list-center2 h3').html(obj.title)
        $('.wallet .list-center2 input[name=temNmae]').val(obj.temNmae)
        $('.wallet .list-center2 input[name=brandName]').val(obj.brandName)
        $('.wallet .list-center2 input[name=telephone]').val(obj.telephone)
        $('.wallet .tem-mask2').fadeIn()
    }
}
wallet()
// =============================下面是在线卡模板js
 function onlineCard (){
	var sourceNum= parseInt($('body').attr('data-source'))
         var targetEle= null
    $('.onlineCard .tem').click(function (e) {
        e =e || window.event
        var target= e.target || e.srcElement
        targetEle= target
        if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('tem-edit')){ //点击编辑
            //这一步发送ajax获取数据,或者在元素身上找到绑定的数据，讲数据处理为下面的obj格式的，并将数据穿进去
            var name= $(target).parent().parent().find('p').eq(0).find('span').html().trim()
            var parse= $(target).parent().parent().find('p').eq(1).find('span')[0].childNodes[0].textContent.trim()
            var totalParse= $(target).parent().parent().find('p').eq(2).find('span')[0].childNodes[0].textContent.trim()
            var obj= {  //这里是从后台获取的数据或者从元素上获取的（这里模拟后台数据）
                title: '修改在线子模板',
                name: name,
                parse: parse,
                totalParse: totalParse
           }
            renderList(obj)
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('tem-delete')){
        	   var isSelectTem= $(target).parent().parent().parent().parent().parent().hasClass('borShadow')
               if(isSelectTem){
                 mui.toast('被选择的子模板不能删除',{ duration:'1500', type:'div' })
                 return false
               }
            mui.confirm('确定删除?', function (type) {
                if(type.index){ //删除
                    $(target).parent().parent().remove()
                    // =============================发送 ajax 提交数据 提交删除元素的数据
                    
                    var id= parseInt($(targetEle).attr('data-id'))
                     $.ajax({
                        data:{
                            id: id
                        },
                        url : "/wctemplate/deletesubclassonline",
                        type : "POST",
                        cache : false,
                        success : function(e){
                           
                        }
                    });
                }
            })
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('addBut')){
            var $list= $(target).parent().prev()
            if($list.length <= 0){ //没有子节点
                //这里是默认设置
                var nextParse= 30
                var nextTotalParse= 31
                var nextSendParse= nextTotalParse- nextParse
                var nextName= nextParse+'元送'+nextSendParse

            }else { //找到上一个子节点
                var reg= /(?!(0[0-9]{0,}$))[0-9]{1,}[.]{0,}[0-9]{0,}/g
                var parse= $list.find('p').eq(1).find('span').text().match(reg)[0]-0
                var TotalParse= $list.find('p').eq(2).find('span').text().match(reg)[0]-0
                console.log(TotalParse, parse)
                var nextParse= parse * 2
                var nextSendParse= (TotalParse- parse) * 2
                var nextTotalParse= nextParse+ nextSendParse
                var nextName= ''
                if(nextSendParse){
                     nextName= nextParse+'元'+'送'+nextSendParse
                }else{
                     nextName= nextParse+'元'
                }
            }
            //发送ajax将新增的数据传输到服务器=====================
            
            var id= parseInt($(targetEle).attr('data-id'))
             $.ajax({
                data:{
                    id: id,
                    name: nextName,
                    money: nextParse,
                    remark: nextTotalParse
                },
                url : "/wctemplate/addsubclassonline",
                type : "POST",
                cache : false,
                success : function(e){
                	if(e.code=== 100){
                		 mui.toast('登录过期，请重新登录',{ duration:'1500', type:'div' }) 
 	               	}else if(e.code=== 101){
 	               		mui.toast('显示名称重复，请修改后重试',{ duration:'1500', type:'div' })
 	               	}else if(e.code=== 102){
 	               		mui.toast('金额过大，请修改后重试',{ duration:'1500', type:'div' })
 	               	}else if(e.code === 200){
	 	               	 var str= '<p>显示名称：<span>'+ nextName +'</span></p> <p>付款金额：<span>'+nextParse+'<b>元</b></span></p> <p>到账金额：<span>'+nextTotalParse+'<b>元</b></span></p> <div> <button type="button" class="mui-btn mui-btn-success tem-edit" data-id='+e.ctemid+'>编辑</button> <button type="button" class="mui-btn mui-btn-success tem-delete" data-id='+e.ctemid+'>删除</button> </div>'
	 	                var list= $('<li class="mui-table-view-cell"></li>')
	 	                list.html(str)
	 	                $(targetEle).parent().parent()[0].insertBefore(list[0],$(targetEle).parent()[0])
 	               	}
                }
            });
            
            
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('tem-title-edit')){ //点击模板编辑按钮
            //$('.tem-mask2').fadeIn()
            //这一步发送ajax获取数据,或者在元素身上找到绑定的数据，讲数据处理为下面的obj2格式的，并将数据穿进去

            var temNmae= $(target).parent().parent().find('p').eq(0).find('span').html().trim()
            var brandName= $(target).parent().parent().find('p').eq(1).find('span').html().trim()
            var telephone= $(target).parent().parent().find('p').eq(2).find('span').html().trim()


            var obj2= {
                title: '修改在线卡主模板',
                temNmae: temNmae,
                brandName: brandName,
                telephone: telephone
            }
            renderList2(obj2)
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('addTemplate')){
            var obj2= {
                title: '新增在线卡主模板',
                temNmae: '',
                brandName: '',
                telephone: ''
            }
            renderList2(obj2)
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('tem-title-delete')){
        	 var isSelectTem= $(target).parent().parent().parent().parent().hasClass('borShadow')
             if(isSelectTem){
               mui.toast('被选择的模板不能删除',{ duration:'1500', type:'div' })
               return false
             }
            mui.confirm('确定删除模板?', function (type) {
                if(type.index){ //删除
                    $(target).parent().parent().parent().parent().remove()
                    // =============================发送 ajax 提交数据 提交删除元素的数据
                    
                    var id= parseInt($(targetEle).attr('data-id'))
                     $.ajax({
                        data:{
                            id: id
                        },
                        url : "/wctemplate/deletestaironline",
                        type : "POST",
                        cache : false,
                        success : function(e){
                           
                        }
                    });
                    
                }
            })
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
                            if(e == 1){
                                parent.siblings().removeClass('borShadow') //移除所有的兄弟节点的选择
                                parent.siblings().find('.bottom p').fadeOut()
                                parent.siblings().find('.selectTem').removeClass('active')
                                parent.addClass('borShadow')  //给当前元素添加节点
                                $(target).parent().parent().find('p').fadeIn()
                                $(target).addClass('active')
                                 //mui.toast('已选择当前模板',{ duration:'1500', type:'div' })
                            }
                         },//返回数据填充
                         error: function(){
                             mui.toast('选择在线卡模板失败，请稍后再试！',{ duration:'1500', type:'div' })
                         }
                     });

               
             }else{
                mui.toast('你已选择当前模板',{ duration:'1500', type:'div' })
             }
            
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('defaultTem')){ 
       	 var parent= $(target).parent().parent().parent().parent().parent()
    	 var id= $(target).attr('data-id').trim() //模板id
    	 $.ajax({
             data:{
                 source: 4,
            	 temid: id
             },
             url : "/wctemplate/templatedefault",
             type : "POST",
             cache : false,
             success : function(e){
            	 console.log(e)
            	 if(e.code=== 100){
            		 mui.toast('登录过期，请重新登录',{ duration:'1500', type:'div' })
            	 }else if(e.code=== 200){
            		 parent.siblings().removeClass('borShadow') //移除所有的兄弟节点的选择
                     parent.siblings().find('.bottom p').fadeOut()
                     parent.siblings().find('.defaultTem').removeClass('active')
                     parent.addClass('borShadow')  //给当前元素添加节点
                     $(target).parent().parent().find('p').fadeIn()
                     $(target).addClass('active')
            	 }
               
             },//返回数据填充
             error: function(){
                 mui.toast('设为默认失败，请稍后再试！',{ duration:'1500', type:'div' })
             }
         });
    
        } 
    })

    $('.onlineCard .list-center1').click(function(e){
        e= e || window.event
        e.stopPropagation()
    })
    $('.onlineCard .tem-mask1').click(close)
    $('.onlineCard .list-center1 .close').click(close)
    $('.onlineCard .list-center1 .close2').click(close)
    function close (e) {
        e= e || window.event
        e.stopPropagation()
        console.log('点击了')
        $('.onlineCard .tem-mask1').fadeOut()
    }

    $('.onlineCard .list-center2').click(function(e){ //组织阻止冒泡，防止点击了自身隐藏
        e= e || window.event
        e.stopPropagation()
    })
    $('.onlineCard .tem-mask2').click(close2)
    $('.onlineCard .list-center2 .close').click(close2)
    $('.onlineCard .list-center2 .close2').click(close2)
    function close2 (e) {
        e= e || window.event
        e.stopPropagation()
        $('.onlineCard .tem-mask2').fadeOut()
    }

    $('.onlineCard .list-center1 .submit').click(function (e) { //点击修改电子模板提交/添加的电子模板提交
        e =e || window.event
        e.stopPropagation()
        var reg= /^\d+(\.\d+)?$/
        var nameVal= $('.onlineCard .list-center1 input[name=name]').val().trim()
        var parseVal= $('.onlineCard .list-center1 input[name=parse]').val().trim()
        var totalParseVal= $('.onlineCard .list-center1 input[name=totalParse]').val().trim()
        if(nameVal.length <= 0){
            mui.toast('请输入显示名称',{ duration:'1500', type:'div' })
            return false
        }
        if(parseVal.length <= 0){
            mui.toast('请输入付款金额',{ duration:'1500', type:'div' })
            return false
        }
        if(!reg.test(parseVal)){
            mui.toast('付款金额请输入数字',{ duration:'1500', type:'div' })
            return false
        }
        if(totalParseVal.length <= 0){
            mui.toast('请输入充卡金额',{ duration:'1500', type:'div' })
            return false
        }
        if(!reg.test(totalParseVal)){
            mui.toast('充卡金额请输入数字',{ duration:'1500', type:'div' })
            return false
        }
        var flag= $('.onlineCard .list-center1 h3').html().trim() === '新增在线卡子模板' ? true : false
        //修改离线子模板
            //发送ajax讲修改之后的数据传输到服务器=====================
            
            var id= parseInt($(targetEle).attr('data-id'))
             $.ajax({
                data:{
                    id: id,
                    name:nameVal,
                    money: parseVal,
                    remark: totalParseVal
                },
                url : "/wctemplate/updatesubclassonline",
                type : "POST",
                cache : false,
                success : function(e){
                   
                }
            });
            
            
            var parentEle= $(targetEle).parent().parent()
            console.log(parentEle)
            parentEle.find('p').eq(0).find('span').html(nameVal)
            parentEle.find('p').eq(1).find('span').html(parseVal+'<b>元</b>')
            parentEle.find('p').eq(2).find('span').html(totalParseVal+'<b>元</b>')
        $('.onlineCard .tem-mask1').fadeOut()
    })

    $('.onlineCard .list-center2 .submit').click(function(e){
        e =e || window.event
        e.stopPropagation()
        var temNmaeVal= $('.onlineCard .list-center2 input[name=temNmae]').val().trim()
        var brandNameseVal= $('.onlineCard .list-center2 input[name=brandName]').val().trim()
        var telephoneVal= $('.onlineCard .list-center2 input[name=telephone]').val().trim()
        if(temNmaeVal.length <= 0){
            mui.toast('请输入模板名称',{ duration:'1500', type:'div' })
            return false
        }
        /*if(brandNameseVal.length <= 0){
            mui.toast('请输入品牌名称',{ duration:'1500', type:'div' })
            return false
        }
        if(telephoneVal.length <= 0){
            mui.toast('请输入售后电话',{ duration:'1500', type:'div' })
            return false
        }*/
        var flag= $('.onlineCard .list-center2 h3').html().trim() === '修改在线卡主模板' ? true : false
        if(flag){ // 修改离线模板
            //发送ajax将修改的数据传输到服务器=====================
            var id= parseInt($(targetEle).attr('data-id'))
             $.ajax({
                data:{
                    id: id,
                    name:temNmaeVal,
                    remark: brandNameseVal,
                    common1: telephoneVal
                },
                url : "/wctemplate/updatestaironline",
                type : "POST",
                cache : false,
                success : function(e){
                   
                }
            });
            
            var parentEle= $(targetEle).parent().parent()
            parentEle.find('p').eq(0).find('span').html(temNmaeVal)
            parentEle.find('p').eq(1).find('span').html(brandNameseVal)
            parentEle.find('p').eq(2).find('span').html(telephoneVal)
        }else { //添加新模板
            //发送ajax将新增的数据传输到服务器=====================
            
             $.ajax({
                data:{
                    name:temNmaeVal,
                    remark: brandNameseVal,
                    
                },
                url : "/wctemplate/addstaironline",
                type : "POST",
                cache : false,
                success : function(e){
                	if(e.code === 101){
                		mui.toast('添加的模板名称已存在，请更改！',{ duration:'1500', type:'div' })
                    }else if(e.code === 100){
                    	mui.toast('登录已过期，请重新登录！',{ duration:'1500', type:'div' })
                    	handleAddTem(e)
                    }else if(e.code === 200){
                    		var defauleOrSelectTem= ''
                			if(sourceNum != 0){//判断是从哪进模板的
                    			defauleOrSelectTem=`<div class="isChecked">
                                <button type="button" class="mui-btn mui-btn-success selectTem" data-id="${e.temid}">选择模板</button>
	                            </div>
	                            <p>选中模板</p>`
                    		}else{
                    			defauleOrSelectTem= `<div class="isChecked">
                                <button type="button" class="mui-btn mui-btn-success defaultTem" data-id="${e.temid}">设为默认</button>
                                </div>
                                <p>默认模板</p>`
                    		}
                    	var str='<li><div class="title"><p>模板名称：<span>'+temNmaeVal+'</span></p><p>品牌名称：<span>'+brandNameseVal+'</span></p><p>售后电话：<span>'+ telephoneVal +'</span></p><div> <button type="button" class="mui-btn mui-btn-success tem-title-edit" data-id='+e.temid+'>编辑</button> <button type="button" class="mui-btn mui-btn-success tem-title-delete" data-id='+e.temid+'>删除</button> </div> </div> <ul class="mui-table-view"> <li class="mui-table-view-cell bottom">'+defauleOrSelectTem+' <button type="button" class="mui-btn mui-btn-success mui-btn-outlined addBut" data-id='+e.temid+'>添加</button> </li> </ul> </li> '
			            var div= $('<div class="list-div"></div>')
			            div.html(str)
			            $('.onlineCard .tem')[0].insertBefore(div[0],$('.tem nav')[0])
                    }
                },//返回数据填充
            });
            
            
        }
        $('.onlineCard .tem-mask2').fadeOut()
    })


    function renderList(obj){ //渲染list-content
        $('.onlineCard .list-center1 h3').html(obj.title)
        $('.onlineCard .list-center1 input[name=name]').val(obj.name)
        $('.onlineCard .list-center1 input[name=parse]').val(obj.parse)
        $('.onlineCard .list-center1 input[name=totalParse]').val(obj.totalParse)
        $('.onlineCard .tem-mask1').fadeIn()
    }
    function renderList2(obj){
        $('.onlineCard .list-center2 h3').html(obj.title)
        $('.onlineCard .list-center2 input[name=temNmae]').val(obj.temNmae)
        $('.onlineCard .list-center2 input[name=brandName]').val(obj.brandName)
        $('.onlineCard .list-center2 input[name=telephone]').val(obj.telephone)
        $('.onlineCard .tem-mask2').fadeIn()
    }
}
onlineCard()

})
