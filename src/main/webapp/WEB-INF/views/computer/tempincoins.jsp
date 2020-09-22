	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>模板</title>
	<%@ include file="/WEB-INF/views/public/commons.jspf"%>
	<link rel="stylesheet" href="${hdpath}/hdfile/css/base.css">	
	<link rel="stylesheet" href="${hdpath}/css/admin.css">
	<link rel="stylesheet" href="${hdpath}/css/bootstrap.min.css">
	<link rel="stylesheet" href="${hdpath}/hdfile/css/toastr.min.css">
	<link rel="stylesheet" href="${hdpath}/hdfile/css/pctemp.css">
	<script src="${hdpath}/js/bootstrap.min.js"></script>
	<script src="${hdpath}/js/jquery.js"></script>
	<script src="${hdpath}/hdfile/js/toastr.min.js"></script>
</head>
<body class="pcTem1 tem3"  data-arecode="${arecode}"  data-source="${source}" data-merid="${merid}" >
<div class="header bg-main">
	<%@ include file="/WEB-INF/views/navigation.jsp"%>
</div>
<div class="leftnav" id="lefeMenu">
	<%@ include file="/WEB-INF/views/menu.jsp"%>
</div>
<ul class="bread">
  <li><a href="javascript:void(0)" target="right" class="icon-home">模板查看</a></li>
</ul>
	<div class="admin">
		<!-- 离线卡模板 tem2 开始-->
		<div class="temDiv tem1">
			<div class="top">
				<h3>模拟投币模板 <button type="button" class="btn btn-info addTemplate"  data-tem="1" >添加主模板</button></h3>
			</div>
			<div class="tableContent">
		    <c:forEach items="${templatelist}" var="tempparent">
		    <input type="hidden" value="${tempparent.merchantid}"  />
		    <c:if test="${tempparent.merchantid==0}">
            <div class="temList">
                    <input type="hidden" class="common2" value="1">
                    <input type="hidden" class="default" value="0">
                        <table class="table  faTem">
                                <thead>
                                    <tr class="title">
                                        <td class="left_td">
                                            <span><b>模板名称:</b> <span class="temNmae">${tempparent.name}</span></span>
                                            <!--<span><b>品牌名称:</b> <span class="brandName"></span></span>
                                            <span><b>售后电话：</b> <span class="telephone"></span></span>-->
                                        </td>
                                        
                                        <td colspan="2">操作</td>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td class="left_td">
                                        </td>
                                        <td class="setWidth">
                                            <button type="button" class="btn btn-info tem-title-edit"  disabled="disabled">编辑</button>
                                        </td>
                                        <td class="setWidth">
                                            <button type="button" class="btn btn-danger tem-title-delete"  disabled="disabled">删除</button>
                                        </td>
                                    </tr>
                                </tbody>
                            
                        </table>
                        <table class="table table-bordered chTem">
                            <thead>
                                <tr class="title">
                                    <td>显示名称</td>
                                    <td>投币个数</td>
                                    <td>付款金额</td>
                                    <td colspan="2">操作</td>
                                </tr>
                            </thead>
                            <tbody> 
                            <c:forEach items="${tempparent.gather}" var="tempson">                              
                                <tr>
                                    <td>${tempson.name}</td>
                                    <td><span><fmt:formatNumber value="${tempson.remark}" pattern="0" /></span>个</td>
                                    <td><span>${tempson.money}</span>元</td>
                                    <td class="setWidth">
                                        <button type="button" class="btn btn-info tem-edit"  disabled="disabled">编辑</button>
                                    </td>
                                    <td class="setWidth">
                                        <button type="button" class="btn btn-danger tem-delete"  disabled="disabled">删除</button>
                                    </td>
                                </tr>                            
                            </c:forEach>                          
                                <tr>
                                   <td colspan="5" style="text-align: left; padding-left: 5%;">
                                   		<ol class="ol-red">注:</ol>
	                                   <ol><span>充卡金额：</span> 该金额为向卡内实际充值的金额（即用户卡中增加的金额）</ol>
	                                   <ol><span>付款金额：</span> 该金额为用户实际支付的金额（即商户收到的金额）</ol>
                                   </td>	
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </c:if>
                <c:if test="${tempparent.merchantid!=0}">
                <c:if test="${tempparent.pitchon == 1 }">
                   
                        <div class="temList borShadow">
                                <input type="hidden" class="common2" value="1">
                                <input type="hidden" class="default" value="0">
                                    <table class="table  faTem">
                                            <thead>
                                                <tr class="title">
                                                    <td class="left_td">
                                                        <span><b>模板名称:</b> <span class="temNmae">${tempparent.name}</span></span>
                                                        <span><b>品牌名称:</b> <span class="brandName">${tempparent.remark}</span></span>
                                                        <span><b>售后电话：</b> <span class="telephone">${tempparent.common1}</span></span>
                                                    </td>
                                                    
                                                    <td colspan="2">操作</td>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr>
                                                    <td class="left_td">
                                                    </td>
                                                    <td class="setWidth">
                                                        <button type="button" class="btn btn-info tem-title-edit" data-id="${tempparent.id}" >编辑</button>
                                                    </td>
                                                    <td class="setWidth">
                                                        <button type="button" class="btn btn-danger tem-title-delete" data-id="${tempparent.id}" disabled>删除</button>
                                                    </td>
                                                </tr>
                                            </tbody>
                                        
                                    </table>
                                    <table class="table table-bordered chTem">
                                        <thead>
                                            <tr class="title">
                                                <td>显示名称</td>
                                                <td>投币个数</td>
                                                <td>付款金额</td>
                                                <td colspan="2">操作</td>
                                            </tr>
                                        </thead>
                                        <tbody> 
                                        <c:forEach items="${tempparent.gather}" var="tempson">                              
                                            <tr>
                                                <td>${tempson.name}</td>
                                                <td><span><fmt:formatNumber value="${tempson.remark}" pattern="0" /></span>个</td>
                                                <td><span>${tempson.money}</span>元</td>
                                                <td class="setWidth">
                                                    <button type="button" class="btn btn-info tem-edit" data-id="${tempson.id}">编辑</button>
                                                </td>
                                                <td class="setWidth">
                                                    <button type="button" class="btn btn-danger tem-delete" data-id="${tempson.id}" disabled >删除</button>
                                                </td>
                                            </tr>                            
                                        </c:forEach>                          
                                            <tr>
                                                <td colspan="6" class="lastTd">                                                   
                                                        <button type="button" class="btn btn-info selectTem active" data-id="${tempparent.id}" disabled >选择模板</button>
                                                        <p class="pText" style="display: block;">选中模板</p>
                                                        <button type="button" class="btn btn-info addBut" data-id="${tempparent.id}">添加模板</button>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>      
                </c:if>
                <c:if test="${tempparent.pitchon != 1 }">

                        <div class="temList">
                                <input type="hidden" class="common2" value="1">
                                <input type="hidden" class="default" value="0">
                                    <table class="table  faTem">
                                            <thead>
                                                <tr class="title">
                                                    <td class="left_td">
                                                        <span><b>模板名称:</b> <span class="temNmae">${tempparent.name}</span></span>
                                                        <span><b>品牌名称:</b> <span class="brandName">${tempparent.remark}</span></span>
                                                        <span><b>售后电话：</b> <span class="telephone">${tempparent.common1}</span></span>
                                                    </td>
                                                    
                                                    <td colspan="2">操作</td>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr>
                                                    <td class="left_td">
                                                    </td>
                                                    <td class="setWidth">
                                                        <button type="button" class="btn btn-info tem-title-edit" data-id="${tempparent.id}">编辑</button>
                                                    </td>
                                                    <td class="setWidth">
                                                        <button type="button" class="btn btn-danger tem-title-delete" data-id="${tempparent.id}">删除</button>
                                                    </td>
                                                </tr>
                                            </tbody>
                                        
                                    </table>
                                    <table class="table table-bordered chTem">
                                        <thead>
                                            <tr class="title">
                                                <td>显示名称</td>
                                                <td>投币个数</td>
                                                <td>付款金额</td>
                                                <td colspan="2">操作</td>
                                            </tr>
                                        </thead>
                                        <tbody> 
                                        <c:forEach items="${tempparent.gather}" var="tempson">                              
                                            <tr>
                                                <td>${tempson.name}</td>
                                                <td><span><fmt:formatNumber value="${tempson.remark}" pattern="0" /></span>个</td>
                                                <td><span>${tempson.money}</span>元</td>
                                                <td class="setWidth">
                                                    <button type="button" class="btn btn-info tem-edit" data-id="${tempson.id}">编辑</button>
                                                </td>
                                                <td class="setWidth">
                                                    <button type="button" class="btn btn-danger tem-delete" data-id="${tempson.id}">删除</button>
                                                </td>
                                            </tr>                            
                                        </c:forEach>                          
                                            <tr>
                                                <td colspan="6" class="lastTd">                                                   
                                                        <button type="button" class="btn btn-info selectTem" data-id="${tempparent.id}">选择模板</button>
                                                        <p class="pText">选中模板</p>
                                                        <button type="button" class="btn btn-info addBut" data-id="${tempparent.id}">添加模板</button>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>      
                   	 </c:if>
                	</c:if>
                </c:forEach>
            </div>
	</div>
</div>
	<div class="tem-mask2" >
        <div class="list-center2">
            <span class="mui-icon mui-icon-closeempty close"></span>
            <h3>修改投币模板</h3>
            <form action="#">
                <div class="inp">
                    <label for="temNmae">模板名称</label>
                    <br>
                    <input type="text" id="temNmae" name="temNmae" placeholder="请填写模板名称">
                </div>
                <div class="inp">
                    <label for="brandName">品牌名称</label>
                    <br>
                    <input type="text" id="brandName" name="brandName" placeholder="请填写品牌名称">
                </div>
                <div class="inp">
                    <label for="telephone">售后电话</label>
                    <br>
                    <input type="text" id="telephone" name="telephone" placeholder="请填写售后电话">
                </div>
            </form>
            <div class="bottom">
                <button type="button" class="btn btn-info close2">关闭</button>
                <button type="button" class="btn btn-info submit">提交</button>
            </div>
        </div>
    </div>
    <div class="tem-mask1" >
        <div class="list-center1">
            <span class="mui-icon mui-icon-closeempty close"></span>
            <h3>修改投币子模板</h3>
            <form action="#">
                <div class="inp">
                    <label for="name2">显示名称</label>
                    <br>
                    <input type="text" id="name2" name="name" placeholder="请填写显示名称">
                </div>
                <div class="inp">
                    <label for="coinNum">投币个数</label>
                    <br>
                    <input type="text" id="coinNum" name="coinNum" placeholder="请填写投币个数">
                    <div class="btn">个</div>
                </div>
                <div class="inp">
                    <label for="totalParse">付款金额</label>
                    <br>
                    <input type="text" id="totalParse" name="totalParse" placeholder="请填写付款金额">
                    <div class="btn">元</div>
                </div>
                <div class="inp">
                    <ol class="ol-red">注:</ol>
                    <ol><span>投币个数：</span> 指投币的数量（单位为元）</ol>
                    <ol><span>付款金额：</span> 该金额为用户实际支付的金额（即商户收到的金额）</ol>
                </div>

            </form>
            <div class="bottom">
                <button type="button" class="btn btn-info close2">关闭</button>
                <button type="button" class="btn btn-info submit">提交</button>
            </div>
        </div>
    </div>
<!-- 离线卡模板 tem2 结束-->
	<script>
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
				
				$('.addTemplate').click(function(){ //添加主模板
					var obj2= {
			                title: '新增投币模板',
			                temNmae: '',
			                brandName: '',
			                telephone: '',
			                isRef: true,
			                isWalletPay: false
			            }
			            renderList2(obj2)
				})
			var targetEle= null
			$('.admin').click(function (e) {
			       e =e || window.event
			       var target= e.target || e.srcElement
			       if(! $(target).hasClass('addTemplate')){
			           targetEle= target
			       }
			       if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('tem-edit')){ //点击编辑子模板
			           //这一步发送ajax获取数据,或者在元素身上找到绑定的数据，讲数据处理为下面的obj格式的，并将数据穿进去
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
			                       url : "/wctemplate/allowdeletesubclass",
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
			          //发送ajax将新增子模板的数据传输到服务器=====================
			           $.ajax({  //添加子模板
			               data:{
			                   id: id,
			                   name: nextName,
			                   money: nextTotalParse,
			                   remark: nextCoinNum
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
			                       var str= '<td>'+nextName+'</td> <td><span>'+nextCoinNum+'</span>个</td><td><span>'+nextTotalParse+'</span>元</td><td class="setWidth"><button type="button" class="btn btn-info tem-edit" data-id="'+e.ctemid+'">编辑</button></td><td class="setWidth"><button type="button" class="btn btn-danger tem-delete" data-id="'+e.ctemid+'" '+disableStr+'>删除</button></td>'
			                      var list= $('<tr></tr>')
			                      list.html(str)
			                      $(targetEle).parent().parent().parent()[0].insertBefore(list[0],$(targetEle).parent().parent()[0])
			                       toastr.success('添加成功')
			                   }

			               },//返回数据填充
			           });
			  

			       }else if(target.nodeName.toLowerCase() === 'button' && $(target).hasClass('tem-title-edit')){ //点击主模板编辑按钮
			           //这一步发送ajax获取数据,或者在元素身上找到绑定的数据，讲数据处理为下面的obj2格式的，并将数据穿进去
			           var temNmae= $(target).parent().parent().parent().parent().find('.temNmae').html().trim()
			           var brandName= $(target).parent().parent().parent().parent().find('.brandName').html().trim()
			           var telephone= $(target).parent().parent().parent().parent().find('.telephone').html().trim()
			           var isRef= 1;
           			   var isWalletPay= 2;
			           var obj2= {
			        		   title: '修改投币模板',
			                   temNmae: temNmae,
			                   brandName: brandName,
			                   telephone: telephone,
			                   isRef: isRef,
			                   isWalletPay: isWalletPay
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
			                       url : "/wctemplate/allowdeletestair",
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
			                        	  console.log(parent)
			                        	   
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
				 	$('.tem3 .list-center1').mousedown(function(e){
					       e= e || window.event
					       e.stopPropagation()
					   })
					   $('.tem3 .tem-mask1').mousedown(close)
					   $('.tem3 .list-center1 .close').mousedown(close)
					   $('.tem3 .list-center1 .close2').mousedown(close)
					   function close (e) {
					       e= e || window.event
					       e.stopPropagation()
					       console.log('点击了')
					       $('.tem3 .tem-mask1').fadeOut()
					   }
					
					   $('.tem3 .list-center2').mousedown(function(e){ //组织阻止冒泡，防止点击了自身隐藏
					       e= e || window.event
					       e.stopPropagation()
					   })
					   $('.tem3 .tem-mask2').mousedown(close2)
					   $('.tem3 .list-center2 .close').mousedown(close2)
					   $('.tem3 .list-center2 .close2').mousedown(close2)
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
					        //修改离线子模板
					            //发送ajax讲修改之后的数据传输到服务器=====================
					            
					            var id= parseInt($(targetEle).attr('data-id'))
					            console.log(targetEle)
					             $.ajax({
					                data:{
					                    id: id,
					                    name:nameVal,
					                    money: totalParseVal,
					                    remark: coinNumVal
					                },
					                url : "/wctemplate/allowupdatesubclassincoins",
					                type : "POST",
					                cache : false,
					                success : function(e){
					                	 var parentEle= $(targetEle).parent().parent()
					                     parentEle.find('td').eq(0).html(nameVal)
					                     parentEle.find('td').eq(1).find('span').html(coinNumVal)
					                     parentEle.find('td').eq(2).find('span').html(totalParseVal)
					                     $('.tem-mask1').fadeOut()
					                      toastr.success("修改成功")
					                }
					            });
					    })
					   
					   
					   $('.tem3 .list-center2 .submit').click(function(e){ //添加或修改主模板
					        e =e || window.event
					        e.stopPropagation()
					        var temNmaeVal= $('.tem3 .list-center2 input[name=temNmae]').val().trim()
					        var brandNameseVal= $('.tem3 .list-center2 input[name=brandName]').val().trim()
					        var telephoneVal= $('.tem3 .list-center2 input[name=telephone]').val().trim()
					        if(temNmaeVal.length <= 0){
					           toastr.warning('请输入模板名称')
					            return false
					        }
					        
					        var flag= $('.tem3 .list-center2 h3').html().trim() === '修改投币模板' ? true : false
					        if(flag){ // 修改离线模板
					            //发送ajax将修改的数据传输到服务器=====================
					            var id= parseInt($(targetEle).attr('data-id'))
					             $.ajax({
					                data:{
					                    id: id,
					                    name:temNmaeVal,
					                    remark: brandNameseVal,
					                    common1: telephoneVal,
					                    permit: 1,
					                    walletpay: 2
					                },
					                url : "/wctemplate/allowupdatestairincoins",
					                type : "POST",
					                cache : false,
					                success : function(e){
					                	 	var parentEle= $(targetEle).parent().parent().parent().parent()
								            parentEle.find('.temNmae').html(temNmaeVal)
							                parentEle.find('.brandName').html(brandNameseVal)
								            parentEle.find('.telephone').html(telephoneVal)
								            toastr.success('主模板编辑成功！')
					                }
					            });
					            
					           
					        }else { //添加新模板
					            //发送ajax将新增的数据传输到服务器=====================
					            var merid= $('body').attr('data-merid').trim()
					             $.ajax({
					                data:{
					                    name:temNmaeVal,
					                    remark: brandNameseVal,
					                    common1: telephoneVal,
					                    permit: 1,
					                    walletpay: 2,
					                    merid: merid
					                },
					                url : "/wctemplate/allowaddstairincoins",
					                type : "POST",
					                cache : false,
					                success : function(e){
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
		                        		var defauleOrSelectTem= '<td colspan="6" class="lastTd"><button type="button" class="btn btn-info selectTem" data-id="'+id+'">选择模板</button><p class="pText">选中模板</p><button type="button" class="btn btn-info addBut" data-id="'+id+'">添加模板</button></td>'
		                                var str= '<table class="table  faTem"><thead><tr class="title"><td class="left_td"><span><b>模板名称:</b> <span class="temNmae">'+temNmaeVal+'</span></span><span><b>品牌名称:</b> <span class="brandName">'+brandNameseVal+'</span></span><span><b>售后电话：</b> <span class="telephone">'+telephoneVal+'</span></span></td><td colspan="2">操作</td></tr></thead><tbody><tr><td class="left_td"></td><td class="setWidth"><button type="button" class="btn btn-info tem-title-edit" data-id="'+id+'">编辑</button></td><td class="setWidth"><button type="button" class="btn btn-danger tem-title-delete" data-id="'+id+'">删除</button></td></tr></tbody></table><table class="table table-bordered chTem"><thead><tr class="title"><td>显示名称</td><td>投币个数</td><td>付款金额</td><td colspan="2">操作</td></tr></thead><tbody><tr>'+defauleOrSelectTem+'</tr></tbody></table>'
		                                var div= $('<div class="temList"></div>')
		                                div.html(str)
		                                $('.tem3 .tableContent').append(div)
		                                toastr.success('主模板添加成功！')
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
					        $('.tem3 .tem-mask2').fadeIn()
					    }
		})
	</script>
</body>
</html>