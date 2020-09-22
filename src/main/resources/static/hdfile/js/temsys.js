
$(function(){
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
                
            $('.goToTemPage').click(function(e){
                var uid= $(this).attr('data-uid').trim()
                if(!uid){
                    toastr.warning('此设备未绑定商户！')
                }
            })
            
                function tem1(){

                   var targetEle= null
                    $('.tem1 #isRefInp').click(function(){
                        var common2= parseInt($(targetEle).parent().parent().parent().parent().find('.isRef').next().attr('data-val'))
                        $('.tem1.popover input').removeAttr('checked')
                        if(common2 === 2){
                            $('.tem1 #refReg2').prop('checked',true)
                        }else if(common2 === 3){
                            $('.tem1 #refReg3').prop('checked',true)
                        }else {
                            $('.tem1 #refReg1').prop('checked',true)
                        }

                        $('.tem1.popover').fadeIn()
                     })
                     $('.tem1 #exitBut').click(function(){
                         $('.tem1.popover').fadeOut()
                     })
                      $('.tem1 #confirmBut').click(function(){
                          var regReg= parseInt($('.tem1 input[name="refReg"]:checked').val()) //退费标准
                          var str= ''
                          switch(regReg){
                                 case 1: str= '(默认)'; break;
                                 case 2: str= '(时间)'; break;
                                 case 3: str= '(电量)'; break;
                             }
                              $('.tem1 #spanList').text(str)
                         $('.tem1.popover').fadeOut()
                     })
                   
                             
                    $('.defaultTemDiv.tem1').click(function(e){
                        e= e || window.event
                        var target= e.target || e.srcElement
                        targetEle= target
                        console.log(target)
                        if($(target).hasClass('tem-title-edit')){ //编辑主模板
                            var temNmae= $(target).parent().parent().parent().parent().find('.temNmae').html().trim()
                            var brandName= $(target).parent().parent().parent().parent().find('.brandName').html().trim()
                            var telephone= $(target).parent().parent().parent().parent().find('.telephone').html().trim()
                            var isRef= $(target).parent().parent().parent().parent().find('.isRef').html().trim() === '是' ? true : false
                            var isWalletPay= $(target).parent().parent().parent().parent().find('.isWalletPay').html().trim() === '是' ? true : false
                            var regVal= ''
                            if(isRef){
                                regVal=  parseInt($(target).parent().parent().parent().parent().find('.isRef').next().attr('data-val'))
                             }
                            console.log(temNmae,brandName,telephone,isRef,isWalletPay,regVal)
                            /*=========== 将原来的值赋给input框*/
                            $('.tem1 .list-center2 h3').html('修改充电模板')
                            $('.tem1 .list-center2 input[name=temNmae]').val(temNmae)
                            $('.tem1 .list-center2 input[name=brandName]').val(brandName)
                            $('.tem1 .list-center2 input[name=telephone]').val(telephone)
                            if(isRef){
                                $('.tem1 .list-center2 input[name=isRef]').eq(0).prop('checked',true)
                                var str= ''
                                    switch(regVal){
                                        case 1: str= '(默认)'; break;
                                        case 2: str= '(时间)'; break;
                                        case 3: str= '(电量)'; break;
                                    }
                                    $('.tem1 #spanList').text(str)
                            }else{
                                 $('.tem1 #spanList').text('')
                                $('.tem1 .list-center2 input[name=isRef]').eq(1).prop('checked',true)
                            }
                            if(isWalletPay){
                                $('.tem1 .list-center2 input[name=isWalletPay]').eq(0).prop('checked',true)
                            }else{
                                $('.tem1 .list-center2 input[name=isWalletPay]').eq(1).prop('checked',true)
                            }
                            $('.tem1.tem-mask2').fadeIn()
                             
                        }
                        if($(target).hasClass('tem-edit')){ //编辑子模板
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
                            
                        }
                        if($(target).hasClass('addBut')){ //添加子模板
                                var $list= $(target).parent().parent().prev()
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
                                console.log(id,nextName,nextParse,nextTime,nextPower)
                                 $.ajax({  //添加子模板
                                        data:{
                                            id: id,
                                            name: nextName,
                                            money: nextParse, 
                                            chargeTime: nextTime, 
                                            chargeQuantity: nextPower
                                        },
                                        url : "/wctemplate/allowaddsubclasscharge",
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
                                                var str= '<td>'+nextName+'</td><td><span>'+nextParse+'</span>元</td><td><span>'+nextTime+'</span>分钟</td><td><span>'+nextPower+'</span>度</td><td class="setWidth"><button type="button" class="btn btn-info tem-edit" data-id="'+e.ctemid+'">编辑</button></td><td class="setWidth"><button type="button" class="btn btn-danger tem-delete" data-id="'+e.ctemid+'">删除</button></td>'
                                               var list= $('<tr></tr>')
                                               list.html(str)
                                               $(targetEle).parent().parent().parent()[0].insertBefore(list[0],$(targetEle).parent().parent()[0])
                                                toastr.success('添加成功')
                                                handleRemoveDelete(3,target)
                                            }
        
                                        },//返回数据填充
                                    });
                        }else if( $(target).hasClass('tem-delete')){ //删除子模板
                        	var $tbody= $(target).parent().parent().parent()
			               if(confirm('确定删除子模板吗？')){
			                   // =============================发送 ajax 提交数据 提交删除元素的数据
			                   var id= parseInt($(targetEle).attr('data-id'))
			                   $.ajax({
			                       data:{
			                           id: id
			                       },
			                       url : "/wctemplate/allowdeletesubclass",
			                       type : "POST",
			                       cache : false,
			                       success : function(e){
			                            $(target).parent().parent().remove()
			                            toastr.success('删除成功')
			                            handleAddDelete(3,$tbody)
			                            
			                       },//返回数据填充
			                   });
			               }
                        }
                    })
                    
                    //主模板编辑部分开始
                    /*================== 点击关闭弹框*/
                    $('.tem1 .list-center2').mousedown(function(e){ //组织阻止冒泡，防止点击了自身隐藏
                        e= e || window.event
                        e.stopPropagation()
                    })
                    $('.tem1.tem-mask2').mousedown(close2)
                    $('.tem1 .list-center2 .close').mousedown(close2)
                    $('.tem1 .list-center2 .close2').mousedown(close2)
                    function close2 (e) {
                        e= e || window.event
                        e.stopPropagation()
                        $('.tem1.tem-mask2').fadeOut()
                    }
                   /*================= 编辑主模板*/
                  $('.tem1 .list-center2 .submit').click(function(e){ 
                        e =e || window.event
                        e.stopPropagation()
                        var temNmaeVal= $('.tem1 .list-center2 input[name=temNmae]').val().trim()
                        var brandNameseVal= $('.tem1 .list-center2 input[name=brandName]').val().trim()
                        var telephoneVal= $('.tem1 .list-center2 input[name=telephone]').val().trim()
                        var isRefVal= $('.tem1 .list-center2 input[name="isRef"]:checked').val()
                        var isWalletPayVal= $('.tem1 .list-center2 input[name="isWalletPay"]:checked').val()
                        
                        var refReg= $('.tem1.popover input[name="refReg"]:checked').val() //退费标准
                        var str1= ''
                            switch(parseInt(refReg)){
                                case 1: str1= '(退费标准：时间和电量最小)'; break;
                                case 2: str1= '(退费标准：根据时间)'; break;
                                case 3: str1= '(退费标准：根据电量)'; break;
                            }
                        var permit= parseInt(isRefVal) === 0 ? 2 :  parseInt(isRefVal)
                        var walletpay = parseInt(isWalletPayVal) === 0 ? 2 :  parseInt(isWalletPayVal)
                        if(temNmaeVal.length <= 0){
                            toastr.warning('请输入模板名称')
                            return false
                        }
                        var id= parseInt($(targetEle).attr('data-id'))
                      $.ajax({
                           data:{
                               id: id,
                               name:temNmaeVal,
                               remark: brandNameseVal,
                               permit: permit,
                               walletpay: walletpay,
                               common1: telephoneVal,
                               common2  : refReg
                           },
                           url : "/wctemplate/allowupdatestaircharge",
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
                             $('.tem1.tem-mask2').fadeOut()
                        }
                  }) 
                  //主模板编辑部分结束
                  //编辑子模板开始
                  function renderList(obj){ //渲染list-content
                      $('.tem1 .list-center1 h3').html(obj.title)
                      $('.tem1 .list-center1 input[name=name]').val(obj.name)
                      $('.tem1 .list-center1 input[name=parse]').val(obj.parse)
                      $('.tem1 .list-center1 input[name=time]').val(obj.time)
                      $('.tem1 .list-center1 input[name=power]').val(obj.power)
                      $('.tem1.tem-mask1').fadeIn()
                  }
                   
                  $('.tem1 .list-center1').mousedown(function(e){
                      e= e || window.event
                      e.stopPropagation()
                  })
                  $('.tem1.tem-mask1').mousedown(close)
                  $('.tem1 .list-center1 .close').mousedown(close)
                  $('.tem1 .list-center1 .close2').mousedown(close)
                  function close (e) {
                      e= e || window.event
                      e.stopPropagation()
                      console.log('点击了')
                      $('.tem1.tem-mask1').fadeOut()
                  }
        
                  $('.tem1 .list-center2').mousedown(function(e){ //组织阻止冒泡，防止点击了自身隐藏
                      e= e || window.event
                      e.stopPropagation()
                  })
                  $('.tem1.tem-mask2').mousedown(close2)
                  $('.tem1 .list-center2 .close').mousedown(close2)
                  $('.tem1 .list-center2 .close2').mousedown(close2)
                  
                   $('.tem1 .list-center1 .submit').click(function (e) { //点击修改电子模板提交
                    e =e || window.event
                    e.stopPropagation()
                    var reg= /^\d+(\.\d+)?$/
                    var nameVal= $('.tem1 .list-center1 input[name=name]').val().trim()
                    var parseVal= $('.tem1 .list-center1 input[name=parse]').val().trim()
                    var timeVal= $('.tem1 .list-center1 input[name=time]').val().trim()
                    var powerVal= $('.tem1 .list-center1 input[name=power]').val().trim()
                    if(nameVal.length <= 0){
                        toastr.warning("请输入模板名称")
                        return false
                    }
                    if(parseVal.length <= 0){
                        toastr.warning("请输入充电价格")
                        return false
                    }
                    if(!reg.test(parseVal)) {
                        toastr.warning("充电价格请输入数字")
                        return false
                    }
                    if(timeVal.length <= 0){
                        toastr.warning("请输入充电时间")
                        return false
                    }
                    if(!reg.test(timeVal)) {
                        toastr.warning("充电时间请输入数字")
                        return false
                    }
                    if(powerVal.length <= 0){
                        toastr.warning("请输入消耗电量")
                        return false
                    }
                    if(!reg.test(powerVal)) {
                        toastr.warning("消耗电量请输入数字")
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
                            url : "/wctemplate/allowupdatesubclasscharge",
                            type : "POST",
                            cache : false,
                            success : function(e){
                                var parentEle= $(targetEle).parent().parent()
                                parentEle.find('td').eq(0).html(nameVal)
                                parentEle.find('td').eq(1).find('span').html(parseVal)
                                parentEle.find('td').eq(2).find('span').html(timeVal)
                                parentEle.find('td').eq(3).find('span').html(powerVal)
                                $('.tem1.tem-mask1').fadeOut()
                                 toastr.success("修改成功")
                            },//返回数据填充
                        });
        
                })     
                  //编辑子模板结束
            }
            tem1()
        /*tem2开始=============================*/
            function tem2(){

                   var targetEle= null
                    $('.defaultTemDiv.tem2').click(function(e){
                        e= e || window.event
                        var target= e.target || e.srcElement
                        targetEle= target
                        if($(target).hasClass('tem-title-edit')){ //编辑主模板
                            var temNmae= $(target).parent().parent().parent().parent().find('.temNmae').html().trim()
                            var brandName= $(target).parent().parent().parent().parent().find('.brandName').html().trim()
                            var telephone= $(target).parent().parent().parent().parent().find('.telephone').html().trim()
                            /*=========== 将原来的值赋给input框*/
                            $('.tem2 .list-center2 h3').html('修改充电模板')
                            $('.tem2 .list-center2 input[name=temNmae]').val(temNmae)
                            $('.tem2 .list-center2 input[name=brandName]').val(brandName)
                            $('.tem2 .list-center2 input[name=telephone]').val(telephone)
                            $('.tem2.tem-mask2').fadeIn()
                        }
                        if($(target).hasClass('tem-edit')){ //编辑子模板
                                var name= $(target).parent().parent().find('td').eq(0).html().trim()
                                var parse= $(target).parent().parent().find('td').eq(1).find('span').html().trim()
                                var totalParse= $(target).parent().parent().find('td').eq(2).find('span').html().trim()
                                var obj= {  //这里是从后台获取的数据或者从元素上获取的（这里模拟后台数据）
                                     title: '修改离线子模板',
                                     name: name,
                                     parse: parse,
                                     totalParse: totalParse
                                }
                                console.log(obj)
                                renderList(obj)
                            
                        }
                        if($(target).hasClass('addBut')){ //添加子模板
                            var $list= $(target).parent().parent().prev()
                             if($list.length <= 0){ //没有子节点
                                    //这里是默认设置
                                    var nextParse= 30
                                    var nextTotalParse= 31
                                    var nextSendParse= nextTotalParse- nextParse
                                    var nextName= nextParse+'元送'+nextSendParse
        
                                }else { //找到上一个子节点
                                        var reg= /(?!(0[0-9]{0,}$))[0-9]{1,}[.]{0,}[0-9]{0,}/g
                                         var parse= $list.find('td').eq(1).find('span').text().match(reg)[0]-0
                                         var TotalParse= $list.find('td').eq(2).find('span').text().match(reg)[0]-0
                                        
                                         var nextParse= parse * 2
                                         var nextSendParse= (TotalParse- parse) * 2
                                         var nextTotalParse= nextParse+ nextSendParse
                                         var nextName= nextParse+'元送'+nextSendParse
                                }
                            
                                var id= parseInt($(targetEle).attr('data-id'))
                                 $.ajax({  //添加子模板
                                        data:{
                                            id: id,
                                            name: nextName,
                                            money: nextParse,
                                            remark: nextTotalParse
                                        },
                                        url : "/wctemplate/allowaddsubclassoffline",
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
                                                var str= '<td>'+nextName+'</td><td><span>'+nextParse+'</span>元</td><td><span>'+nextTotalParse+'</span>元</td><td class="setWidth"><button type="button" class="btn btn-info tem-edit" data-id="'+e.ctemid+'">编辑</button></td><td class="setWidth"><button type="button" class="btn btn-danger tem-delete" data-id="'+e.ctemid+'">删除</button></td>'
                                               var list= $('<tr></tr>')
                                               list.html(str)
                                               $(targetEle).parent().parent().parent()[0].insertBefore(list[0],$(targetEle).parent().parent()[0])
                                                toastr.success('添加成功')
                                                 handleRemoveDelete(3,target)
                                            }
        
                                        },//返回数据填充
                                    });
                        }else if( $(target).hasClass('tem-delete')){ //删除子模板
                        	var $tbody= $(target).parent().parent().parent()
			               if(confirm('确定删除子模板吗？')){
			                   // =============================发送 ajax 提交数据 提交删除元素的数据
			                   var id= parseInt($(targetEle).attr('data-id'))
			                   $.ajax({
			                       data:{
			                           id: id
			                       },
			                       url : "/wctemplate/allowdeletesubclass",
			                       type : "POST",
			                       cache : false,
			                       success : function(e){
			                            $(target).parent().parent().remove()
			                            toastr.success('删除成功')
			                             handleAddDelete(3,$tbody)
			                          
			                       },//返回数据填充
			                   });
			               }  
			             }
                    })
                    
                    //主模板编辑部分开始
                    /*================== 点击关闭弹框*/
                    $('.tem2 .list-center2').mousedown(function(e){ //组织阻止冒泡，防止点击了自身隐藏
                        e= e || window.event
                        e.stopPropagation()
                    })
                    $('.tem2.tem-mask2').mousedown(close2)
                    $('.tem2 .list-center2 .close').mousedown(close2)
                    $('.tem2 .list-center2 .close2').mousedown(close2)
                    function close2 (e) {
                        e= e || window.event
                        e.stopPropagation()
                        $('.tem2.tem-mask2').fadeOut()
                    }
                   /*================= 编辑主模板*/
                  $('.tem2 .list-center2 .submit').click(function(e){ 
                        e =e || window.event
                        e.stopPropagation()
                        var temNmaeVal= $('.tem2 .list-center2 input[name=temNmae]').val().trim()
                        var brandNameseVal= $('.tem2 .list-center2 input[name=brandName]').val().trim()
                        var telephoneVal= $('.tem2 .list-center2 input[name=telephone]').val().trim()
                        if(temNmaeVal.length <= 0){
                            toastr.warning('请输入模板名称')
                            return false
                        }
                        var id= parseInt($(targetEle).attr('data-id'))
                      $.ajax({
                           data:{
                                id: id,
                                name:temNmaeVal,
                                remark: brandNameseVal,
                                common1: telephoneVal
                           },
                           url : "/wctemplate/allowupdatestairoffline",
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
                           toastr.success('主模板编辑成功！')
                             $('.tem2.tem-mask2').fadeOut()
                        }
                  }) 
                  //主模板编辑部分结束
                  //编辑子模板开始
                  function renderList(obj){ //渲染list-content
                      $('.tem2 .list-center1 h3').html(obj.title)
                      $('.tem2 .list-center1 input[name=name]').val(obj.name)
                      $('.tem2 .list-center1 input[name=parse]').val(obj.parse)
                      $('.tem2 .list-center1 input[name=totalParse]').val(obj.totalParse)
                      $('.tem2.tem-mask1').fadeIn()
                  }
                   
                  $('.tem2 .list-center1').mousedown(function(e){
                      e= e || window.event
                      e.stopPropagation()
                  })
                  $('.tem2.tem-mask1').mousedown(close)
                  $('.tem2 .list-center1 .close').mousedown(close)
                  $('.tem2 .list-center1 .close2').mousedown(close)
                  function close (e) {
                      e= e || window.event
                      e.stopPropagation()
                      console.log('点击了')
                      $('.tem2.tem-mask1').fadeOut()
                  }
        
                  $('.tem2 .list-center2').mousedown(function(e){ //组织阻止冒泡，防止点击了自身隐藏
                      e= e || window.event
                      e.stopPropagation()
                  })
                  $('.tem2.tem-mask2').mousedown(close2)
                  $('.tem2 .list-center2 .close').mousedown(close2)
                  $('.tem2 .list-center2 .close2').mousedown(close2)
                  
                   $('.tem2 .list-center1 .submit').click(function (e) { //点击修改电子模板提交
                    e =e || window.event
                    e.stopPropagation()
                    var reg= /^\d+(\.\d+)?$/
                    var nameVal= $('.tem2 .list-center1 input[name=name]').val().trim()
                    var parseVal= $('.tem2 .list-center1 input[name=parse]').val().trim()
                    var totalParseVal= $('.tem2 .list-center1 input[name=totalParse]').val().trim()
                    if(nameVal.length <= 0){
                            toastr.warning('请输入显示名称')
                            return false
                    }
                     if(parseVal.length <= 0){
                            toastr.warning('请输入付款金额')
                            return false
                     }
                     if(!reg.test(parseVal)){
                         toastr.warning('付款金额请输入数字')
                          return false
                     }
                     if(totalParseVal.length <= 0){
                         toastr.warning('请输入充卡金额')
                            return false
                     }
                     if(!reg.test(totalParseVal)){
                         toastr.warning('充卡金额请输入数字')
                            return false
                     }
                     var flag= $('.tem2 .list-center1 h3').html().trim() === '新增离线子模板' ? true : false
                        //修改离线子模板
                    
                    //修改子模板
                        //发送ajax讲修改之后的数据传输到服务器=====================
        
                        var id= parseInt($(targetEle).attr('data-id'))
                        $.ajax({
                            data:{
                                 id: id,
                                 name:nameVal,
                                 money: parseVal,
                                 remark: totalParseVal
                            },
                            url : "/wctemplate/allowupdatesubclassoffline",
                            type : "POST",
                            cache : false,
                            success : function(e){
                                var parentEle= $(targetEle).parent().parent()
                                console.log(parentEle)
                                parentEle.find('td').eq(0).html(nameVal)
                                parentEle.find('td').eq(1).find('span').html(parseVal)
                                parentEle.find('td').eq(2).find('span').html(totalParseVal)
                                $('.tem2.tem-mask1').fadeOut()
                                 toastr.success("修改成功")
        
                            },//返回数据填充
                        });
        
                })     
                  //编辑子模板结束
            }
            tem2()
            
            /*tem3开始=============================*/
            function tem3(){

                   var targetEle= null
                    $('.defaultTemDiv.tem3').click(function(e){
                        e= e || window.event
                        var target= e.target || e.srcElement
                        targetEle= target
                        if($(target).hasClass('tem-title-edit')){ //编辑主模板
                            var temNmae= $(target).parent().parent().parent().parent().find('.temNmae').html().trim()
                            var brandName= $(target).parent().parent().parent().parent().find('.brandName').html().trim()
                            var telephone= $(target).parent().parent().parent().parent().find('.telephone').html().trim()
                            /*=========== 将原来的值赋给input框*/
                            $('.tem3 .list-center2 h3').html('修改投币模板')
                            $('.tem3 .list-center2 input[name=temNmae]').val(temNmae)
                            $('.tem3 .list-center2 input[name=brandName]').val(brandName)
                            $('.tem3 .list-center2 input[name=telephone]').val(telephone)
                            $('.tem3.tem-mask2').fadeIn()
                        }
                        if($(target).hasClass('tem-edit')){ //编辑子模板
                               var name= $(target).parent().parent().find('td').eq(0).html().trim()
                               var coinNum= $(target).parent().parent().find('td').eq(1).find('span').html().trim()
                               var totalParse= $(target).parent().parent().find('td').eq(2).find('span').html().trim()
                                
                                var obj= {  //这里是从后台获取的数据或者从元素上获取的（这里模拟后台数据）
                                     title: '修改投币子模板',
                                     name: name,
                                     coinNum: coinNum,
                                     totalParse: totalParse
                                }
                                renderList(obj)
                            
                        }
                        if($(target).hasClass('addBut')){ //添加子模板
                            var $list= $(target).parent().parent().prev()
                             var $list= $(target).parent().parent().prev()
                               if($list.length <= 0){ //没有子节点
                                    //这里是默认设置
                                    var nextCoinNum= 1
                                    var nextTotalParse= 1
                                    var rate= nextCoinNum / nextTotalParse  //得到的是一元几个币
                                    var nextName= nextCoinNum+'元'+nextTotalParse+'个币'

                                }else { //找到上一个子节点
                                    var reg= /(?!(0[0-9]{0,}$))[0-9]{1,}[.]{0,}[0-9]{0,}/g
                                    var coinNum= $list.find('td').eq(1).find('span').text().match(reg)[0]-0
                                    var totalParse= $list.find('td').eq(2).find('span').text().match(reg)[0]-0
                                    var rate= totalParse/coinNum  //得到的是一个币几元
                                    var nextCoinNum= coinNum+1
                                    var nextTotalParse= nextCoinNum * rate
                                    var nextName= nextTotalParse+'元'+nextCoinNum+'个币'
                                }
                                var id= parseInt($(targetEle).attr('data-id'))
                                 $.ajax({  //添加子模板
                                        data:{
                                           id: id,
                                           name: nextName,
                                           remark: nextCoinNum,
                                           money: nextTotalParse
                                        },
                                        url : "/wctemplate/allowaddsubclassincoins",
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
                                                var str= '<td>'+nextName+'</td> <td><span>'+nextCoinNum+'</span>个</td><td><span>'+nextTotalParse+'</span>元</td><td class="setWidth"><button type="button" class="btn btn-info tem-edit" data-id="'+e.ctemid+'">编辑</button></td><td class="setWidth"><button type="button" class="btn btn-danger tem-delete" data-id="'+e.ctemid+'">删除</button></td>'
                                               var list= $('<tr></tr>')
                                               list.html(str)
                                               $(targetEle).parent().parent().parent()[0].insertBefore(list[0],$(targetEle).parent().parent()[0])
                                                toastr.success('添加成功')
                                                 handleRemoveDelete(3,target)
                                            }
        
                                        },//返回数据填充
                                    });
                        }else if( $(target).hasClass('tem-delete')){ //删除子模板
                        	var $tbody= $(target).parent().parent().parent()
			               if(confirm('确定删除子模板吗？')){
			                   // =============================发送 ajax 提交数据 提交删除元素的数据
			                   var id= parseInt($(targetEle).attr('data-id'))
			                   $.ajax({
			                       data:{
			                           id: id
			                       },
			                       url : "/wctemplate/allowdeletesubclass",
			                       type : "POST",
			                       cache : false,
			                       success : function(e){
			                            $(target).parent().parent().remove()
			                            toastr.success('删除成功')
			                             handleAddDelete(3,$tbody)
			                          
			                       },//返回数据填充
			                   });
			               } 
			            }
                    })
                    
                    //主模板编辑部分开始
                    /*================== 点击关闭弹框*/
                    $('.tem3 .list-center2').mousedown(function(e){ //组织阻止冒泡，防止点击了自身隐藏
                        e= e || window.event
                        e.stopPropagation()
                    })
                    $('.tem3.tem-mask2').mousedown(close2)
                    $('.tem3 .list-center2 .close').mousedown(close2)
                    $('.tem3 .list-center2 .close2').mousedown(close2)
                    function close2 (e) {
                        e= e || window.event
                        e.stopPropagation()
                        $('.tem3.tem-mask2').fadeOut()
                    }
                   /*================= 编辑主模板*/
                  $('.tem3 .list-center2 .submit').click(function(e){ 
                        e =e || window.event
                        e.stopPropagation()
                        var temNmaeVal= $('.tem3 .list-center2 input[name=temNmae]').val().trim()
                        var brandNameseVal= $('.tem3 .list-center2 input[name=brandName]').val().trim()
                        var telephoneVal= $('.tem3 .list-center2 input[name=telephone]').val().trim()
                        if(temNmaeVal.length <= 0){
                            toastr.warning('请输入模板名称')
                            return false
                        }
                        var id= parseInt($(targetEle).attr('data-id'))
                      $.ajax({
                           data:{
                                id: id,
                                name:temNmaeVal,
                                remark: brandNameseVal,
                                common1: telephoneVal
                           },
                           url : "/wctemplate/allowupdatestairincoins",
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
                           toastr.success('主模板编辑成功！')
                             $('.tem3.tem-mask2').fadeOut()
                        }
                  }) 
                  //主模板编辑部分结束
                  //编辑子模板开始
                  function renderList(obj){ //渲染list-content
                        $('.tem3 .list-center1 h3').html(obj.title)
                        $('.tem3 .list-center1 input[name=name]').val(obj.name)
                        $('.tem3 .list-center1 input[name=coinNum]').val(obj.coinNum)
                        $('.tem3 .list-center1 input[name=totalParse]').val(obj.totalParse)
                        $('.tem3.tem-mask1').fadeIn()
                    }
                   
                  $('.tem3 .list-center1').mousedown(function(e){
                      e= e || window.event
                      e.stopPropagation()
                  })
                  $('.tem3.tem-mask1').mousedown(close)
                  $('.tem3 .list-center1 .close').mousedown(close)
                  $('.tem3 .list-center1 .close2').mousedown(close)
                  function close (e) {
                      e= e || window.event
                      e.stopPropagation()
                      console.log('点击了')
                      $('.tem3.tem-mask1').fadeOut()
                  }
        
                  $('.tem3 .list-center2').mousedown(function(e){ //组织阻止冒泡，防止点击了自身隐藏
                      e= e || window.event
                      e.stopPropagation()
                  })
                  $('.tem3.tem-mask2').mousedown(close2)
                  $('.tem3 .list-center2 .close').mousedown(close2)
                  $('.tem3 .list-center2 .close2').mousedown(close2)
                  
                   $('.tem3 .list-center1 .submit').click(function (e) { //点击修改电子模板提交
                    e =e || window.event
                    e.stopPropagation()
                    var reg= /^\d+(\.\d+)?$/
                    var nameVal= $('.tem3 .list-center1 input[name=name]').val().trim()
                    var coinNumVal= $('.tem3 .list-center1 input[name=coinNum]').val().trim()
                    var totalParseVal= $('.tem3 .list-center1 input[name=totalParse]').val().trim()
                    if(nameVal.length <= 0){
                          toastr.warning("请输入显示名称")
                        return false
                    }
                    if(coinNumVal.length <= 0){
                        toastr.warning("请输入投币个数")
                        return false
                    }
                    if(!reg.test(coinNumVal)){
                        toastr.warning("投币个数请输入数字")
                        return false
                    }
                    if(totalParseVal.length <= 0){
                        toastr.warning("请输入付款金额")
                        return false
                    }
                    if(!reg.test(totalParseVal)){
                        toastr.warning("付款金额请输入数字")
                        return false
                    }
                    //修改子模板
                        //发送ajax讲修改之后的数据传输到服务器=====================
        
                        var id= parseInt($(targetEle).attr('data-id'))
                        $.ajax({
                            data:{
                                id: id,
                                name:nameVal,
                                remark: coinNumVal,
                                money: totalParseVal
                            },
                            url : "/wctemplate/allowupdatesubclassincoins",
                            type : "POST",
                            cache : false,
                            success : function(e){
                                 var parentEle= $(targetEle).parent().parent()
                                 parentEle.find('td').eq(0).html(nameVal)
                                 parentEle.find('td').eq(1).find('span').html(coinNumVal)
                                 parentEle.find('td').eq(2).find('span').html(totalParseVal)
                                 $('.tem3.tem-mask1').fadeOut()
                                  toastr.success("修改成功")
                            },//返回数据填充
                        });
        
                })     
                  //编辑子模板结束
            }
            tem3()
        // tem4模板开始
         function tem4(){
                   var targetEle= null
                    $('.defaultTemDiv.tem4').click(function(e){
                        e= e || window.event
                        var target= e.target || e.srcElement
                        targetEle= target
                        if($(target).hasClass('tem-title-edit')){ //编辑主模板
                            var temNmae= $(target).parent().parent().parent().parent().find('.temNmae').html().trim()
                            var brandName= $(target).parent().parent().parent().parent().find('.brandName').html().trim()
                            var telephone= $(target).parent().parent().parent().parent().find('.telephone').html().trim()
                            /*=========== 将原来的值赋给input框*/
                            $('.tem4 .list-center2 h3').html('修改钱包主模板')
                            $('.tem4 .list-center2 input[name=temNmae]').val(temNmae)
                            $('.tem4 .list-center2 input[name=brandName]').val(brandName)
                            $('.tem4 .list-center2 input[name=telephone]').val(telephone)
                            $('.tem4.tem-mask2').fadeIn()
                        }
                        if($(target).hasClass('tem-edit')){ //编辑子模板
                                var name= $(target).parent().parent().find('td').eq(0).html().trim()
                                var parse= $(target).parent().parent().find('td').eq(1).find('span').html().trim()
                                var totalParse= $(target).parent().parent().find('td').eq(2).find('span').html().trim()
                                var obj= {  //这里是从后台获取的数据或者从元素上获取的（这里模拟后台数据）
                                     title: '修改钱包子模板',
                                     name: name,
                                     parse: parse,
                                     totalParse: totalParse
                                }
                                renderList(obj)
                            
                        }
                        if($(target).hasClass('addBut')){ //添加子模板
                            var $list= $(target).parent().parent().prev()
                             if($list.length <= 0){ //没有子节点
                                    //这里是默认设置
                                    var nextParse= 30
                                    var nextTotalParse= 31
                                    var nextSendParse= nextTotalParse- nextParse
                                    var nextName= nextParse+'元送'+nextSendParse
        
                                }else { //找到上一个子节点
                                        var reg= /(?!(0[0-9]{0,}$))[0-9]{1,}[.]{0,}[0-9]{0,}/g
                                         var parse= $list.find('td').eq(1).find('span').text().match(reg)[0]-0
                                         var TotalParse= $list.find('td').eq(2).find('span').text().match(reg)[0]-0
                                        
                                         var nextParse= parse * 2
                                         var nextSendParse= (TotalParse- parse) * 2
                                         var nextTotalParse= nextParse+ nextSendParse
                                         var nextName= nextParse+'元送'+nextSendParse
                                }
                            
                                var id= parseInt($(targetEle).attr('data-id'))
                                 $.ajax({  //添加子模板
                                        data:{
                                            id: id,
                                            name: nextName,
                                            money: nextParse,
                                            remark: nextTotalParse
                                        },
                                        url : "/wctemplate//allowaddsubclasswallet",
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
                                             var str= '<td>'+nextName+'</td><td><span>'+nextParse+'</span>元</td><td><span>'+nextTotalParse+'</span>元</td><td class="setWidth"><button type="button" class="btn btn-info tem-edit" data-id="'+e.ctemid+'">编辑</button></td><td class="setWidth"><button type="button" class="btn btn-danger tem-delete" data-id="'+e.ctemid+'">删除</button></td>'
                                               var list= $('<tr></tr>')
                                               list.html(str)
                                               $(targetEle).parent().parent().parent()[0].insertBefore(list[0],$(targetEle).parent().parent()[0])
                                                toastr.success('添加成功')
                                                 handleRemoveDelete(3,target)
                                            }
        
                                        },//返回数据填充
                                    });
                        }else if( $(target).hasClass('tem-delete')){ //删除子模板
                        	var $tbody= $(target).parent().parent().parent()
			               if(confirm('确定删除子模板吗？')){
			                   // =============================发送 ajax 提交数据 提交删除元素的数据
			                   var id= parseInt($(targetEle).attr('data-id'))
			                   $.ajax({
			                       data:{
			                           id: id
			                       },
			                       url : "/wctemplate/allowdeletesubclass",
			                       type : "POST",
			                       cache : false,
			                       success : function(e){
			                            $(target).parent().parent().remove()
			                            toastr.success('删除成功')
			                             handleAddDelete(3,$tbody)
			                       }
			                   });
                        	}  
			              }
                    })
                    
                    //主模板编辑部分开始
                    /*================== 点击关闭弹框*/
                    $('.tem4 .list-center2').mousedown(function(e){ //组织阻止冒泡，防止点击了自身隐藏
                        e= e || window.event
                        e.stopPropagation()
                    })
                    $('.tem4.tem-mask2').mousedown(close2)
                    $('.tem4 .list-center2 .close').mousedown(close2)
                    $('.tem4 .list-center2 .close2').mousedown(close2)
                    function close2 (e) {
                        e= e || window.event
                        e.stopPropagation()
                        $('.tem4.tem-mask2').fadeOut()
                    }
                   /*================= 编辑主模板*/
                  $('.tem4 .list-center2 .submit').click(function(e){ 
                        e =e || window.event
                        e.stopPropagation()
                        var temNmaeVal= $('.tem4 .list-center2 input[name=temNmae]').val().trim()
                        var brandNameseVal= $('.tem4 .list-center2 input[name=brandName]').val().trim()
                        var telephoneVal= $('.tem4 .list-center2 input[name=telephone]').val().trim()
                        if(temNmaeVal.length <= 0){
                            toastr.warning('请输入模板名称')
                            return false
                        }
                        var id= parseInt($(targetEle).attr('data-id'))
                      $.ajax({
                           data:{
                                id: id,
                                name:temNmaeVal,
                                remark: brandNameseVal,
                                common1: telephoneVal
                           },
                           url : "/wctemplate//allowupstairwallet",
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
                           toastr.success('主模板编辑成功！')
                             $('.tem4.tem-mask2').fadeOut()
                        }
                  }) 
                  //主模板编辑部分结束
                  //编辑子模板开始
                  function renderList(obj){ //渲染list-content
                      $('.tem4 .list-center1 h3').html(obj.title)
                      $('.tem4 .list-center1 input[name=name]').val(obj.name)
                      $('.tem4 .list-center1 input[name=parse]').val(obj.parse)
                      $('.tem4 .list-center1 input[name=totalParse]').val(obj.totalParse)
                      $('.tem4.tem-mask1').fadeIn()
                  }
                   
                  $('.tem4 .list-center1').mousedown(function(e){
                      e= e || window.event
                      e.stopPropagation()
                  })
                  $('.tem4.tem-mask1').mousedown(close)
                  $('.tem4 .list-center1 .close').mousedown(close)
                  $('.tem4 .list-center1 .close2').mousedown(close)
                  function close (e) {
                      e= e || window.event
                      e.stopPropagation()
                      console.log('点击了')
                      $('.tem4.tem-mask1').fadeOut()
                  }
        
                  $('.tem4 .list-center2').mousedown(function(e){ //组织阻止冒泡，防止点击了自身隐藏
                      e= e || window.event
                      e.stopPropagation()
                  })
                  $('.tem4.tem-mask2').mousedown(close2)
                  $('.tem4 .list-center2 .close').mousedown(close2)
                  $('.tem4 .list-center2 .close2').mousedown(close2)
                  
                   $('.tem4 .list-center1 .submit').click(function (e) { //点击修改电子模板提交
                    e =e || window.event
                    e.stopPropagation()
                    var reg= /^\d+(\.\d+)?$/
                    var nameVal= $('.tem4 .list-center1 input[name=name]').val().trim()
                    var parseVal= $('.tem4 .list-center1 input[name=parse]').val().trim()
                    var totalParseVal= $('.tem4 .list-center1 input[name=totalParse]').val().trim()
                    if(nameVal.length <= 0){
                            toastr.warning('请输入显示名称')
                            return false
                    }
                     if(parseVal.length <= 0){
                            toastr.warning('请输入付款金额')
                            return false
                     }
                     if(!reg.test(parseVal)){
                         toastr.warning('付款金额请输入数字')
                          return false
                     }
                     if(totalParseVal.length <= 0){
                         toastr.warning('请输入到账金额')
                            return false
                     }
                     if(!reg.test(totalParseVal)){
                         toastr.warning('到账金额请输入数字')
                            return false
                     }
                     var flag= $('.tem4 .list-center1 h3').html().trim() === '新增离线子模板' ? true : false
                        //修改离线子模板
                    
                    //修改子模板
                        //发送ajax讲修改之后的数据传输到服务器=====================
        
                        var id= parseInt($(targetEle).attr('data-id'))
                        $.ajax({
                            data:{
                                 id: id,
                                 name:nameVal,
                                 money: parseVal,
                                 remark: totalParseVal
                            },
                            url : "/wctemplate/allowupsubclasswallet",
                            type : "POST",
                            cache : false,
                            success : function(e){
                                var parentEle= $(targetEle).parent().parent()
                                console.log(parentEle)
                                parentEle.find('td').eq(0).html(nameVal)
                                parentEle.find('td').eq(1).find('span').html(parseVal)
                                parentEle.find('td').eq(2).find('span').html(totalParseVal)
                                $('.tem4.tem-mask1').fadeOut()
                                 toastr.success("修改成功")
        
                            },//返回数据填充
                        });
        
                })     
                  //编辑子模板结束
            }
            tem4()
        // tem4模板结束
    
                // tem5模板开始
         function tem5(){
                   var targetEle= null
                    $('.defaultTemDiv.tem5').click(function(e){
                        e= e || window.event
                        var target= e.target || e.srcElement
                        targetEle= target
                        if($(target).hasClass('tem-title-edit')){ //编辑主模板
                            var temNmae= $(target).parent().parent().parent().parent().find('.temNmae').html().trim()
                            var brandName= $(target).parent().parent().parent().parent().find('.brandName').html().trim()
                            var telephone= $(target).parent().parent().parent().parent().find('.telephone').html().trim()
                            /*=========== 将原来的值赋给input框*/
                            $('.tem5 .list-center2 h3').html('修改在线卡主模板')
                            $('.tem5 .list-center2 input[name=temNmae]').val(temNmae)
                            $('.tem5 .list-center2 input[name=brandName]').val(brandName)
                            $('.tem5 .list-center2 input[name=telephone]').val(telephone)
                            $('.tem5.tem-mask2').fadeIn()
                        }
                        if($(target).hasClass('tem-edit')){ //编辑子模板
                                var name= $(target).parent().parent().find('td').eq(0).html().trim()
                                var parse= $(target).parent().parent().find('td').eq(1).find('span').html().trim()
                                var totalParse= $(target).parent().parent().find('td').eq(2).find('span').html().trim()
                                var obj= {  //这里是从后台获取的数据或者从元素上获取的（这里模拟后台数据）
                                     title: '修改在线卡子模板',
                                     name: name,
                                     parse: parse,
                                     totalParse: totalParse
                                }
                                renderList(obj)
                            
                        }
                        if($(target).hasClass('addBut')){ //添加子模板
                            var $list= $(target).parent().parent().prev()
                             if($list.length <= 0){ //没有子节点
                                    //这里是默认设置
                                    var nextParse= 30
                                    var nextTotalParse= 31
                                    var nextSendParse= nextTotalParse- nextParse
                                    var nextName= nextParse+'元送'+nextSendParse
        
                                }else { //找到上一个子节点
                                        var reg= /(?!(0[0-9]{0,}$))[0-9]{1,}[.]{0,}[0-9]{0,}/g
                                         var parse= $list.find('td').eq(1).find('span').text().match(reg)[0]-0
                                         var TotalParse= $list.find('td').eq(2).find('span').text().match(reg)[0]-0
                                        
                                         var nextParse= parse * 2
                                         var nextSendParse= (TotalParse- parse) * 2
                                         var nextTotalParse= nextParse+ nextSendParse
                                         var nextName= nextParse+'元送'+nextSendParse
                                }
                            
                                var id= parseInt($(targetEle).attr('data-id'))
                                 $.ajax({  //添加子模板
                                        data:{
                                            id: id,
                                            name: nextName,
                                            money: nextParse,
                                            remark: nextTotalParse
                                        },
                                        url : "/wctemplate/allowaddsubclassonline",
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
                                             var str= '<td>'+nextName+'</td><td><span>'+nextParse+'</span>元</td><td><span>'+nextTotalParse+'</span>元</td> <td class="setWidth"><button type="button" class="btn btn-info tem-edit" data-id="'+e.ctemid+'">编辑</button></td><td class="setWidth"> <button type="button" class="btn btn-danger tem-delete" data-id="'+e.ctemid+'">删除</button></td>'
                                               var list= $('<tr></tr>')
                                               list.html(str)
                                               $(targetEle).parent().parent().parent()[0].insertBefore(list[0],$(targetEle).parent().parent()[0])
                                                toastr.success('添加成功')
                                                 handleRemoveDelete(3,target)
                                            }
        
                                        },//返回数据填充
                                    });
                        }else if( $(target).hasClass('tem-delete')){ //删除子模板
                        	var $tbody= $(target).parent().parent().parent()
			               if(confirm('确定删除子模板吗？')){
			                   // =============================发送 ajax 提交数据 提交删除元素的数据
			                   var id= parseInt($(targetEle).attr('data-id'))
			                   $.ajax({
			                       data:{
			                           id: id
			                       },
			                       url : "/wctemplate/allowdeletesubclass",
			                       type : "POST",
			                       cache : false,
			                       success : function(e){
			                            $(target).parent().parent().remove()
			                            toastr.success('删除成功')
			                             handleAddDelete(3,$tbody)
			                          
			                       },//返回数据填充
			                   });
			               	} 
			            }
                    })
                    
                    //主模板编辑部分开始
                    /*================== 点击关闭弹框*/
                    $('.tem5 .list-center2').mousedown(function(e){ //组织阻止冒泡，防止点击了自身隐藏
                        e= e || window.event
                        e.stopPropagation()
                    })
                    $('.tem5.tem-mask2').mousedown(close2)
                    $('.tem5 .list-center2 .close').mousedown(close2)
                    $('.tem5 .list-center2 .close2').mousedown(close2)
                    function close2 (e) {
                        e= e || window.event
                        e.stopPropagation()
                        $('.tem5.tem-mask2').fadeOut()
                    }
                   /*================= 编辑主模板*/
                  $('.tem5 .list-center2 .submit').click(function(e){ 
                        e =e || window.event
                        e.stopPropagation()
                        var temNmaeVal= $('.tem5 .list-center2 input[name=temNmae]').val().trim()
                        var brandNameseVal= $('.tem5 .list-center2 input[name=brandName]').val().trim()
                        var telephoneVal= $('.tem5 .list-center2 input[name=telephone]').val().trim()
                        if(temNmaeVal.length <= 0){
                            toastr.warning('请输入模板名称')
                            return false
                        }
                        var id= parseInt($(targetEle).attr('data-id'))
                      $.ajax({
                           data:{
                                id: id,
                                name:temNmaeVal,
                                remark: brandNameseVal,
                                common1: telephoneVal
                           },
                           url : "/wctemplate/allowupstaironline",
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
                           toastr.success('主模板编辑成功！')
                             $('.tem5.tem-mask2').fadeOut()
                        }
                  }) 
                  //主模板编辑部分结束
                  //编辑子模板开始
                  function renderList(obj){ //渲染list-content
                      $('.tem5 .list-center1 h3').html(obj.title)
                      $('.tem5 .list-center1 input[name=name]').val(obj.name)
                      $('.tem5 .list-center1 input[name=parse]').val(obj.parse)
                      $('.tem5 .list-center1 input[name=totalParse]').val(obj.totalParse)
                      $('.tem5.tem-mask1').fadeIn()
                  }
                   
                  $('.tem5 .list-center1').mousedown(function(e){
                      e= e || window.event
                      e.stopPropagation()
                  })
                  $('.tem5.tem-mask1').mousedown(close)
                  $('.tem5 .list-center1 .close').mousedown(close)
                  $('.tem5 .list-center1 .close2').mousedown(close)
                  function close (e) {
                      e= e || window.event
                      e.stopPropagation()
                      console.log('点击了')
                      $('.tem5.tem-mask1').fadeOut()
                  }
        
                  $('.tem5 .list-center2').mousedown(function(e){ //组织阻止冒泡，防止点击了自身隐藏
                      e= e || window.event
                      e.stopPropagation()
                  })
                  $('.tem5.tem-mask2').mousedown(close2)
                  $('.tem5 .list-center2 .close').mousedown(close2)
                  $('.tem5 .list-center2 .close2').mousedown(close2)
                  
                   $('.tem5 .list-center1 .submit').click(function (e) { //点击修改电子模板提交
                    e =e || window.event
                    e.stopPropagation()
                    var reg= /^\d+(\.\d+)?$/
                    var nameVal= $('.tem5 .list-center1 input[name=name]').val().trim()
                    var parseVal= $('.tem5 .list-center1 input[name=parse]').val().trim()
                    var totalParseVal= $('.tem5 .list-center1 input[name=totalParse]').val().trim()
                    if(nameVal.length <= 0){
                            toastr.warning('请输入显示名称')
                            return false
                    }
                     if(parseVal.length <= 0){
                            toastr.warning('请输入付款金额')
                            return false
                     }
                     if(!reg.test(parseVal)){
                         toastr.warning('付款金额请输入数字')
                          return false
                     }
                     if(totalParseVal.length <= 0){
                         toastr.warning('请输入充卡金额')
                            return false
                     }
                     if(!reg.test(totalParseVal)){
                         toastr.warning('充卡金额请输入数字')
                            return false
                     }
                     var flag= $('.tem5 .list-center1 h3').html().trim() === '新增离线子模板' ? true : false
                        //修改离线子模板
                    
                    //修改子模板
                        //发送ajax讲修改之后的数据传输到服务器=====================
        
                        var id= parseInt($(targetEle).attr('data-id'))
                        $.ajax({
                            data:{
                                 id: id,
                                 name:nameVal,
                                 money: parseVal,
                                 remark: totalParseVal
                            },
                            url : "/wctemplate/allowupsubclassonline",
                            type : "POST",
                            cache : false,
                            success : function(e){
                                var parentEle= $(targetEle).parent().parent()
                                parentEle.find('td').eq(0).html(nameVal)
                                parentEle.find('td').eq(1).find('span').html(parseVal)
                                parentEle.find('td').eq(2).find('span').html(totalParseVal)
                                $('.tem5.tem-mask1').fadeOut()
                                 toastr.success("修改成功")
        
                            },//返回数据填充
                        });
        
                })     
                  //编辑子模板结束
            }
            tem5()
        // tem5模板结束
            
           function handleAddDelete(num,$tbody){ //处理当子模板小于等于num时，不能删除
            	var length= $tbody.find('tr').length-1
            	if(length <= num){
            		$tbody.find('.tem-delete').prop('disabled',true)
            	}
            }
            function handleRemoveDelete(num,target){ //在添加成功之后调用
            	var $tbody=  $(target).parent().parent().parent()
            	var length= $tbody.find('tr').length-1
            	if(length > num){
            		$tbody.find('.tem-delete').prop('disabled',false)
            	}
            }

})