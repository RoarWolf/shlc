
//这里是tem 模板也得js逻辑处理

$(function () {
	var htmlwidth = document.documentElement.clientWidth || document.body.clientWidth;
    var fontSize= htmlwidth/16
    var style= document.createElement('style')
    style.innerHTML= 'html { font-size: '+fontSize+'px !important;}'
    var head= document.getElementsByTagName('head')[0]
    head.insertBefore(style,head.children[0])
    
    var targetEle= null 
    $('.tem').click(function (e) {
        e =e || window.event
        var target= e.target || e.srcElement
        targetEle= target
        if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('tem-edit')){ //点击编辑子模板
            //这一步发送ajax获取数据,或者在元素身上找到绑定的数据，讲数据处理为下面的obj格式的，并将数据穿进去
            var parentEle= $(target).parent().parent()
            var name= parentEle.find('.name').text().trim()
            var chargeParse= parentEle.find('.chargeParse').text().trim()
            var time= parentEle.find('.time').text().trim()
            var obj= {  //这里是从后台获取的数据或者从元素上获取的（这里模拟后台数据）
                title: '修改包月子模板',
                name: name,
                chargeParse: chargeParse,
                time: time
            }
            renderList(obj)
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('tem-delete')){
            var $list= $(targetEle).parent().prev()
            var id= $(target).attr('data-id').trim()
            mui.confirm('确定删除?', function (type) {
                if(type.index){ //删除
                    $.ajax({
                         url: '/wctemplate/deletesubclassPackageMonth',
                        data: {
                             id: id
                        },
                        type: "post",
                        success: function(e){
                        	console.log(e)
                        	if(e == 1){
                        		 $(target).parent().parent().remove()
                        		  mui.toast('子模板删除成功！',{ duration:'1500', type:'div' })
                        	}else{
                        		 mui.toast('子模板删除失败！',{ duration:'1500', type:'div' })
                        	}
                        },
                        error: function(){
                        	 mui.toast('子模板删除失败,请重试！',{ duration:'1500', type:'div' })
                        }
                     })
                   
                    // =============================发送 ajax 提交数据 提交删除元素的数据
                }
            })
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('addBut')){
             var $list= $(target).parent().prev()
            if($list.length <= 0){ //没有子节点
                //这里是默认设置
                var nextChargeParseVal= 30  //每月的价格
                var nextTimeVal= 1   //包月时间
                var nextNameVal= nextChargeParseVal+'元'+nextTimeVal+'月'

            }else { //找到上一个子节点
               
                var reg= /(?!(0[0-9]{0,}$))[0-9]{1,}[.]{0,}[0-9]{0,}/g
                var chargeParseVal= parseFloat($list.find('.chargeParse').text().trim())
                var timeVal=  parseFloat($list.find('.time').text().trim())
                var rate1= Math.round(chargeParseVal / timeVal)  //得到的比例是1月多少钱
                var nextTimeVal= timeVal+1
                var nextChargeParseVal= (nextTimeVal * rate1) % 1 === 0 ? (nextTimeVal * rate1) : (nextTimeVal * rate1).toFixed(2)
                var nextNameVal= nextChargeParseVal+'元'+nextTimeVal+'月'
            }

             var id= parseInt($(targetEle).attr('data-id'))

           //发送ajax将新增子模板的数据传输到服务器=====================
            $.ajax({  //添加子模板
                 data:{
                     id: id,
                     name: nextNameVal,
                     money: nextChargeParseVal, 
                     common1: nextTimeVal, 
                 },
                 url : "/wctemplate/addSubclassPackageMonth",
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
                    	 var str= ' <p>显示名称：<span class="name">'+nextNameVal+'</span></p><p>充值金额：<span><span class="chargeParse">'+nextChargeParseVal+'</span> <b>元</b></span></p><p>包月时间：<span><span class="time">'+nextTimeVal+'</span><b>月</b></span></p><div><button type="button" class="mui-btn mui-btn-success tem-edit" data-id="'+e.ctemid+'">编辑</button><button type="button" class="mui-btn mui-btn-success tem-delete" data-id="'+e.ctemid+'">删除</button></div>'
	                       var list= $('<li class="mui-table-view-cell"></li>')
	                       list.html(str)
	                       $(targetEle).parent().parent()[0].insertBefore(list[0],$(targetEle).parent()[0])
	                       mui.toast('子模板添加成功',{ duration:'1500', type:'div' })
                     }

                 },//返回数据填充
             });
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('tem-title-edit')){ //点击模板编辑按钮
            /**
             * 获取主模板的信息
             */
            var temName= $('.tem span.temName').text().trim()
            var dayChargeTimes= $('.tem span.dayChargeTimes').text().trim()
            var maxPower= $('.tem span.maxPower').text().trim()
            var longestTime= $('.tem span.longestTime').text().trim()
            var monthChargeTimes= $('.tem span.monthChargeTimes').text().trim()
            var isMonthly= $(target).parent().parent().find('p').eq(5).find('span').html().trim() === '是' ? true : false
            var obj2= {
                title: '修改包月主模板',
                temName: temName,
                dayChargeTimes: dayChargeTimes,
                maxPower: maxPower,
                longestTime: longestTime,
                monthChargeTimes: monthChargeTimes,
                isMonthly: isMonthly
            }
            console.log(obj2)
            renderList2(obj2)
        }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('addTemplate')){
        	 var obj2= {
                     title: '新增包月主模板',
                     temName: '',
                     dayChargeTimes: '',
                     maxPower: '',
                     longestTime: '',
                     monthChargeTimes: '',
                     isMonthly: false,
                 }
                 renderList2(obj2)
        }
    })

    $('.list-center1').click(function(e){
        e= e || window.event
        e.stopPropagation()
    })
    $('.tem-mask1').click(close)
    $('.list-center1 .close').click(close)
    $('.list-center1 .close2').click(close)
    function close (e) {
        e= e || window.event
        e.stopPropagation()
        console.log('点击了')
        $('.tem-mask1').fadeOut()
    }

    $('.list-center2').click(function(e){ //组织阻止冒泡，防止点击了自身隐藏
        e= e || window.event
        e.stopPropagation()
    })
    $('.tem-mask2').click(close2)
    $('.list-center2 .close').click(close2)
    $('.list-center2 .close2').click(close2)
    function close2 (e) {
        e= e || window.event
        e.stopPropagation()
        $('.tem-mask2').fadeOut()
    }

    $('.list-center1 .submit').click(function (e) { //点击修改电子模板提交/添加的电子模板提交
        e =e || window.event
        e.stopPropagation()
        var nameVal= $('.list-center1 input[name=name]').val().trim()
        var chargeParseVal= $('.list-center1 input[name=chargeParse]').val().trim()
        var timeVal= $('.list-center1 input[name=time]').val().trim()
        if(nameVal.length <= 0){
            mui.toast('请输入显示名称',{ duration:'1500', type:'div' })
            return false
        }
        if(chargeParseVal.length <= 0){
            mui.toast('请输入充值金额',{ duration:'1500', type:'div' })
            return false
        }
        if(!(/^[0-9]+(.[0-9]+)?$/.test(chargeParseVal))){
            mui.toast('充值金额为数字',{ duration:'1500', type:'div' })
            return false
        }
        if(timeVal.length <= 0){
            mui.toast('请输入充电时间',{ duration:'1500', type:'div' })
            return false
        }
         if(!(/^[1-9]\d*$/.test(timeVal))){
            mui.toast('充电时间为正整数',{ duration:'1500', type:'div' })
            return false
        }
         var id= $(targetEle).attr('data-id').trim()
         $.ajax({
             url: '/wctemplate/updatesubclassPackageMonth',
             data:{
            	 id: id,
            	 name: nameVal,
                 money: chargeParseVal, 
                 common1: timeVal, 
             },
             type: 'POST',
             success: function(e){
                 if(e.code == 200){
                	 var parentEle= $(targetEle).parent().parent()
                     parentEle.find('.name').html(nameVal)
                     parentEle.find('.chargeParse').html(chargeParseVal)
                     parentEle.find('.time').html(timeVal)
                     mui.toast('子模板修改成功！',{ duration:'1500', type:'div' })
                 }else{
                	 mui.toast('子模板修改失败！',{ duration:'1500', type:'div' })
                 }
             },
             error: function(){
            	 mui.toast('子模板修改失败,请稍后重试！',{ duration:'1500', type:'div' })
             }
         })
       //修改包月子模板
            //发送ajax讲修改之后的数据传输到服务器=====================
           
        $('.tem-mask1').fadeOut()
    })

    $('.list-center2 .submit').click(function(e){
        e =e || window.event
        e.stopPropagation()
        var temNameVal= $('.list-center2 input[name=temName]').val().trim()
        var dayChargeTimesVal= $('.list-center2 input[name=dayChargeTimes]').val().trim()
        var maxPowerVal= $('.list-center2 input[name=maxPower]').val().trim()
        var longestTimeVal= $('.list-center2 input[name=longestTime]').val().trim()
        var monthChargeTimesVal= $('.list-center2 input[name="monthChargeTimes"]').val().trim()
        var isMonthlyVal= $('.list-center2 input[name="isMonthly"]:checked').val()
        
        if(temNameVal.length <= 0){
            mui.toast('请输入模板名称',{ duration:'1500', type:'div' })
            return false
        }
        if(dayChargeTimesVal.length <= 0){
            mui.toast('请输入每日最大充电次数',{ duration:'1500', type:'div' })
            return false
        }
        if(!(/^[0-9]\d*$/.test(dayChargeTimesVal))){
             mui.toast('每日最大充电次数为整数',{ duration:'1500', type:'div' })
             return
        }
        if(maxPowerVal.length <= 0){
            mui.toast('请输入每次最大用电量',{ duration:'1500', type:'div' })
            return false
        }
         if( !(/^[0-9]+(.[0-9]+)?$/.test(maxPowerVal))){
            mui.toast('每次最大用电量为数字',{ duration:'1500', type:'div' })
            return false
        }
        if(longestTimeVal.length <= 0){
            mui.toast('请输入每次最长充电时间',{ duration:'1500', type:'div' })
            return false
        }
        if( !(/^[0-9]+(.[0-9]+)?$/.test(longestTimeVal))){
            mui.toast('每次最长充电时间为数字',{ duration:'1500', type:'div' })
            return false
        }
        if(monthChargeTimesVal.length <= 0){
            mui.toast('请输入每月最大充电次数',{ duration:'1500', type:'div' })
            return false
        }
         if(!(/^[0-9]\d*$/.test(monthChargeTimesVal))){
             mui.toast('每月最大充电次数为整数',{ duration:'1500', type:'div' })
             return
        }
         var flag= $('.list-center2 h3').text().trim() === '修改包月主模板' ? true : false
        if(flag){
        	//发送ajax将修改的数据传输到服务器=====================
        	var id= $(targetEle).attr('data-id').trim()
             $.ajax({
                url: '/wctemplate/updatePackageMonth',
                data: {
                	id: id,
                	name: temNameVal,
        			remark: dayChargeTimesVal,
        			common3: maxPowerVal,
        			common2: longestTimeVal,
        			common1: monthChargeTimesVal,
        			ifmonth: isMonthlyVal
                 },
                 type: 'POST',
                 success: function(e){
                    if(e.code === 200){
                    	var parentEle= $(targetEle).parent().parent()
                        parentEle.find('.temName').html(temNameVal)
                        parentEle.find('.dayChargeTimes').html(dayChargeTimesVal)
                        parentEle.find('.maxPower').html(maxPowerVal)
                        parentEle.find('.longestTime').html(longestTimeVal)
                        parentEle.find('.monthChargeTimes').html(monthChargeTimesVal)
                        parentEle.find('p').eq(5).find('span').removeClass('span-green span-red')
                        if(isMonthlyVal==1){
                        	parentEle.find('p').eq(5).find('span').addClass('span-green')
                        	parentEle.find('p').eq(5).find('span').text('是')
                        }else{
                        	parentEle.find('p').eq(5).find('span').addClass('span-red')
                        	parentEle.find('p').eq(5).find('span').text('否')
                        }
                        mui.toast('主模板修改成功！',{ duration:'1500', type:'div' })
                   }
                 }
             })
            
        }else {
        	$.ajax({
        		url: '/wctemplate/addPackageMonth',
        		data: {
        			name: temNameVal,
        			remark: dayChargeTimesVal,
        			common3: maxPowerVal,
        			common2: longestTimeVal,
        			common1: monthChargeTimesVal,
        			ifmonth: isMonthlyVal
        		},
        		type: 'POST',
        		success: function(e){
        			if(e.code == 200){
        				$('.noData').remove()
        				$('.addTemplate').remove()
        				$('nav').addClass('removeTem')
        				var monthlyClass
        				var monthlyHtml
        				if(isMonthlyVal==1){
        					monthlyClass= "span-green"
        					monthlyHtml= "是"
        				}else {
        					monthlyClass= "span-red"
        					monthlyHtml= "否"
        				}
            			var str1= '<li><div class="title"><p>包月模板名称：<span class="temName">'+temNameVal+'</span></p><p>每日最大充电次数<span><span class="dayChargeTimes">'+dayChargeTimesVal+'</span><strong>次</strong></span></p><p>每次最大用电量：<span><span class="maxPower">'+maxPowerVal+'</span><strong>度</strong></span></p><p>每次最长充电时间:<span><span class="longestTime">'+longestTimeVal+'</span><strong>分钟</strong></span></p><p>每月最大充电次数：<span><span class="monthChargeTimes">'+monthChargeTimesVal+'</span><strong>次</strong></span></p> <p>是否开通包月功能：<span class="'+monthlyClass+'">'+monthlyHtml+'</span></p><div><button type="button" class="mui-btn mui-btn-success tem-title-edit" data-id="'+e.temid+'">编辑</button></div></div><ul class="mui-table-view"><li class="mui-table-view-cell bottom"><button type="button" class="mui-btn mui-btn-success mui-btn-outlined addBut" data-id="'+e.temid+'">添加</button></li></ul></li>'
    		        	var div= $('<div class="list-div"></div>');
		        	 	div.html(str1);
		                $('.tem')[0].insertBefore(div[0],$('.tem nav')[0]);
		                mui.toast('主模板添加成功',{ duration:'1500', type:'div' })
        			}
        			
		        },
        		error: function(err){
        			mui.toast('添加失败，请重试！',{ duration:'1500', type:'div' })
        		}
        	})
        	
        }

         $('.tem-mask2').fadeOut()
    })


    function renderList(obj){ //渲染list-content
        $('.list-center1 h3').html(obj.title)
        $('.list-center1 input[name=name]').val(obj.name)
        $('.list-center1 input[name=chargeParse]').val(obj.chargeParse)
        $('.list-center1 input[name=time]').val(obj.time)
        $('.tem-mask1').fadeIn()
    }
    function renderList2(obj){
        $('.list-center2 h3').html(obj.title)
        $('.list-center2 input[name=temName]').val(obj.temName)
        $('.list-center2 input[name=dayChargeTimes]').val(obj.dayChargeTimes)
        $('.list-center2 input[name=maxPower]').val(obj.maxPower)
         $('.list-center2 input[name=longestTime]').val(obj.longestTime)
        $('.list-center2 input[name=monthChargeTimes]').val(obj.monthChargeTimes)
        if(obj.isMonthly){
            $('.list-center2 input[name=isMonthly]').eq(0).prop('checked',true)
        }else{
            $('.list-center2 input[name=isMonthly]').eq(1).prop('checked',true)
        }
        $('.tem-mask2').fadeIn()
    }
})

