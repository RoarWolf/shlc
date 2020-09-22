<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>模拟投币模板管理</title>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<!--App自定义的css-->
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/app.css" />
<style type="text/css">
.h4, h4 { font-size: 15px; line-height: 2;}
a{color: black;margin-left: 1rem;}
a:hover {color: #1b1e21;margin-left: 0.8rem;}
ul{list-style: none;}
.list-group {padding-left: 5px;margin-bottom:13px;}
.panel-title button{background-color:#44b549;}
.container-fluid div{margin: 1.5rem 0.8rem;}
.panel-title a{float: right; margin-right: 1rem;}
.panel-body{padding: 0;}
.editSonTem{float: right; margin-right: 6.3rem;}
.deleteSonTem{position: fixed; right: 1.8rem;}
.details{padding: 5px 8px;}
.details .btn_addSonTem{ margin-left: 0.7rem;}
.command{border: 1px dotted #ccc; padding: 5px;}
.command .edittem{position: fixed; right: 8.3rem;}
.command .deletetem{float: right;}
.form-group input{width: 80%;}
.input-group-addon{width: 18%; float: right; margin-top: -34px; height: 34px; font-size: 14px;}
</style>
</head>

<body>
	<div class="container">
		<div class="panel-group" id="accordion">
           <c:forEach items="${templatelist}" var="temp">
		    <c:if test="${temp.merchantid==0}">
		    <div class="panel panel-default" style="margin-top:8px;">
				<div class="panel-heading">
					<h4 class="panel-title">
					  <span>${temp.name}</span> 
					   <a data-toggle="collapse" data-parent="#accordion"  href="#collapseOne${temp.id}"><span class="glyphicon glyphicon-plus"></span></a>  
					</h4>
				</div>
				<div id="collapseOne${temp.id}" class="panel-collapse collapse collapse">
					<div class="panel-body">
						<div class="details">
							<ul class="list-group">
							 	<c:forEach items="${temp.gather}" var="tempSon">
							  	  <li class="list-group">
							 		<span>${tempSon.name}</span> 
								    <button style="margin-right: 1.8rem;" type="button" class="btn btn-default btn-sm editSonTem" data-mydata="0" value="${tempSon.id}"><span class="glyphicon glyphicon-pencil">&nbsp;查看</span></button>
							  	  </li>
							 	</c:forEach>
							 </ul>
				   		</div>
					</div>
				</div>
			</div>
		    </c:if>
		    </c:forEach>
		    <c:forEach items="${templatelist}" var="temp">
		    <c:if test="${temp.merchantid!=0}">
			<div class="panel panel-default" style="margin-top:8px;">
				<div class="panel-heading">
					<h4 class="panel-title">
					  <span>${temp.name}</span> 
					  <input type="hidden" id=“name${temp.id}” name="name${temp.id}" value="${temp.name}" />
					  <input type="hidden" id=“remark${temp.id}” name="remark${temp.id}" value="${temp.remark}" />
					  <input type="hidden" id=“common1${temp.id}” name="common1${temp.id}" value="${temp.common1}" />
					  <a data-toggle="collapse" data-parent="#accordion"  href="#collapseOne${temp.id}"><span class="glyphicon glyphicon-plus"></span></a>  
					</h4>
				</div>
				<div id="collapseOne${temp.id}" class="panel-collapse collapse collapse">
					<div class="panel-body">
						<div class="details">
							<ul class="list-group">
							 	<c:forEach items="${temp.gather}" var="tempSon">
							  	  <li class="list-group">
							 		<span>${tempSon.name}</span> 
								    <button type="button" class="btn btn-default btn-sm editSonTem" data-mydata="1" value="${tempSon.id}"><span class="glyphicon glyphicon-pencil">&nbsp;编辑</span></button>
							  		<button type="button" class="btn btn-default btn-sm deleteSonTem" value="${tempSon.id}"><span class="glyphicon glyphicon-trash">&nbsp;删除</span></button>
							  	  </li>
							 	</c:forEach>
							 </ul>
					  		 <button type="button" class="btn btn-default btn-sm btn_addSonTem"  value="${temp.id}">添加</button>
				   		</div>
						<div class="command">
						    <button type="button" class="btn btn-default btn-sm deletetem" value="${temp.id}"><span class="glyphicon glyphicon-trash">&nbsp;删除</span></button> 
						    <button type="button" class="btn btn-default btn-sm edittem" data-mydata="1" value="${temp.id}"><span class="glyphicon glyphicon-pencil">&nbsp;编辑</span></button>
						</div>
					</div>
				</div>
			</div>
			</c:if>
		    </c:forEach>
		  </div>
		<!-- 弹出框 -->
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
              <div class="modal-body">
                <input type="hidden" name="parme" id="parme" value="">
                <form id="parmform">
                  <div id="addTemp">
	                <div class="modal-header">
	                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	                    <h4 class="modal-title" id="myModalLabel">新增模板</h4>
	                </div>
                    <input type="hidden" name="merchantid" value="${user.id}">
                    <div  id="tempconten">
                    <div class="form-group">
                        <label for="name">模板名称</label>
                        <input type="text" name="name" class="form-control" id="name" placeholder="请输入模板名称">
                        <input type="hidden" name="id" id="id" value=""/>
                       </div>
                    <div class="form-group">
                        <label for="money">品牌名称</label>
                        <input type="text" name="remark" class="form-control" id="remark" placeholder="请输入品牌名称">
                    </div>
                    <div class="form-group">
                        <label for="money">售后电话</label>
                        <input type="text" name="common1" class="form-control" id="common1" placeholder="请输入售后电话">
                    </div>
                    </div>
                    <div class="form-group">
					    <label for="permit" class="control-label">退费选择</label><br />
					    <input type="radio" style="width:5%;" class="control-label" value="1" name="permit" checked/>支持
					    <input type="radio" style="margin-left: 25px; width:5%;" class="control-label" value="2" name="permit" />不支持
					</div>
                    <div class="form-group">
					    <label for="walletpay" class="control-label">钱包强制支付</label><br />
					    <input type="radio" style="width:5%;" class="control-label" value="1" name="walletpay"/>是
					    <input type="radio" style="margin-left: 25px; width:5%;" class="control-label" value="2" name="walletpay" checked/>否
					</div>
                  </div>
                </form>
                <form  id="parmsonform">
                  <div  id="addSonTemp" style="display: none;">
	                <div class="modal-header">
	                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	                    <h4 class="modal-title" id="myModalLabelson">添加子模板</h4>
	                </div>
                    <div class="form-group">
                        <label for="name">模板名称</label>
                        <input type="text" name="name" class="form-control" placeholder="请输入模板名称">
                        <input type="hidden" name="id" id="idson" value="">
                    </div>
                    <div class="form-group">
                        <label for="money">投币个数</label>
                        <input type="text" name="money" class="form-control" id="money" placeholder="请输入投币个数">
                    	<span class="input-group-addon">个</span>
                    </div>
                    <div class="form-group">
                        <label for="chargeTime">付款金额</label>
                        <input type="text" name="remark" class="form-control" placeholder="请输入付款金额">
                   	 	<span class="input-group-addon">元</span>
                    </div>
                    <div class="form-group">
                   	 		<span style="color: red;">注：</span>
                   	 		<br><span style="color: #080808; font-weight: 800;">投币个数：</span>指投币的数量（单位为元）
                   	 		<br><span style="color: #080808; font-weight: 800;">付款金额：</span>该金额为用户实际支付的金额（即商户收到的金额）
                    </div>
                  </div>
                </form>
              </div>
              <div class="modal-footer">
                  <button type="button" class="btn btn-default" data-dismiss="modal"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span>关闭</button>
                  <button type="button" id="btn_submit" class="btn btn-primary" data-dismiss="modal"><span class="glyphicon glyphicon-floppy-disk" aria-hidden="true"></span>保存</button>
              </div>
            </div>
        </div>
    </div><!-- 弹出狂 -->
	</div>
 	
 	<nav class="navbar navbar-default navbar-fixed-bottom" role="navigation">
	    <div class="container-fluid">
	    <div style="float:left;">
	       <a href="${hdpath}/merchant/index" class="btn btn-success btn-lg"><span class="glyphicon glyphicon-home"></span> 首页</a>
	    </div>
	    <div>
	     <a href="#" id="btn_add" class="btn btn-success btn-lg"><span class="glyphicon glyphicon-plus"></span>添加模板</a>
	    </div>
	    </div>
	</nav>
</body>

<script>

$(function() {
	
	//添加时弹框（父
	$("#btn_add").click(function(){
		   $("#parme").attr("value","0");
           $("#myModalLabel").text("新增投币模板");
           $("#tempconten").find("input").val("");
           $("#addSonTemp").hide();
           $("#addTemp").show();
           $("input[type=radio]").removeProp("checked");
		   $("input[type=radio][name=permit][value=1]").prop('checked',true);
	       $("input[type=radio][name=walletpay][value=2]").prop('checked',true);
           $('#myModal').modal();
    });	
	//修改（父
    $(".edittem").click(function () {
    	$("#parme").attr("value","1");
        $("#myModalLabel").text("修改投币模板");
        $("#addSonTemp").hide();
        $("#addTemp").show();
        $.ajax({
			url : '${hdpath}/wctemplate/getParentTemplateOne',
			data : {id : $(this).val(),},
			type : "POST",
			cache : false,
			success : function(e){
				$("#id").val(e.id);
				$("#name").val(e.name);
		        $("#remark").val(e.remark);
		        $("#common1").val(e.common1);
		        $("input[type=radio]").removeProp("checked");
		        $("input[type=radio][name=permit][value="+e.permit+"]").prop('checked',true);
		        $("input[type=radio][name=walletpay][value="+e.walletpay+"]").prop('checked',true);
		        $('#myModal').modal();
			},//返回数据填充
		});
        var isSystemTemp = $(this).attr("data-mydata");
        if (isSystemTemp == 1) {
	        $("#btn_submit").show();
        }
    });
	//保存
	$("#btn_submit").click(function(){
		var parme = $("#parme").val();
		operation(parme);
	})
	//删除模板
	$(".deletetem").click(function(){
		var statu = confirm("是否确认删除整个模板？");
        if(!statu){
            return false;
        }
		$.ajax({
			url : '${hdpath}/wctemplate/coinsTemDelete',
			data : {id : $(this).val(),},
			type : "POST",
			cache : false,
			success : function(data){
				location.reload();
			},//返回数据填充
		}); 
	})
	
	//添加时弹框（子
	$(".btn_addSonTem").click(function(){
		$("#parme").attr("value","3");
        $("#myModalLabelson").text("新增投币子模板");
        $("#addTemp").hide();
        $("#addSonTemp").show();
        var temid =$(this).val();
        $("#idson").attr("value", temid);
    	$("#parmsonform").find("input[name='name']").removeAttr("disabled")
        $("#parmsonform").find("input[name='money']").removeAttr("disabled")
        $("#parmsonform").find("input[name='remark']").removeAttr("disabled")
        $("#parmsonform").find("input[name='name']").attr("value","");
        $("#parmsonform").find("input[name='money']").attr("value","");
        $("#parmsonform").find("input[name='remark']").attr("value","");
        $('#myModal').modal();
 	});
	
	//修改时弹框（子
	$(".editSonTem").click(function(){
		var isSystemTemp = $(this).attr("data-mydata")
		$("#parme").attr("value","4");
        $("#myModalLabelson").text("修改投币子模板");
        $("#idson").attr("value", $(this).val());
        $.ajax({
			url : '${hdpath}/wctemplate/selectOneSonCoins',
			data : {id : $(this).val(),},
			type : "POST",
			cache : false,
			success : function(e){
				$("#parmsonform").find("input[name='name']").attr("value",e.name);
		        $("#parmsonform").find("input[name='money']").attr("value",e.money);
		        $("#parmsonform").find("input[name='remark']").attr("value",e.remark);

		        $("#addTemp").hide();
		        $("#addSonTemp").show();
		        $('#myModal').modal();
		        if (isSystemTemp == 0) {
					$("#parmsonform").find("input[name='name']").attr("disabled","disabled");
			        $("#parmsonform").find("input[name='money']").attr("disabled","disabled");
			        $("#parmsonform").find("input[name='remark']").attr("disabled","disabled");
			        $("#btn_submit").hide();
		        } else if (isSystemTemp == 1) {
		        	$("#parmsonform").find("input[name='name']").removeAttr("disabled")
			        $("#parmsonform").find("input[name='money']").removeAttr("disabled")
			        $("#parmsonform").find("input[name='remark']").removeAttr("disabled")
			        $("#btn_submit").show();
		        }
			},//返回数据填充
		}); 
 	});
	

	function operation(parme){
		var url;
		var pare;
		if(parme==0){//主膜板添加
			pare =  $("#parmform").serialize()+"&status=2";
			url = "${hdpath}/wctemplate/coinsTemInsert";
		}else if(parme==1){//主模板修改
			pare =  $("#parmform").serialize()+"&status=2";
			url = "${hdpath}/wctemplate/updateTempmanage";
		}else if(parme==3){//子模板添加
			pare =  $("#parmsonform").serialize()+"&status=2";
			url = "${hdpath}/wctemplate/insertsoncoins";
		}else if(parme==4){//子模板修改
			pare =  $("#parmsonform").serialize()+"&status=2";
			url = "${hdpath}/wctemplate/updateSonCoins";
		}
		$.ajax({
			data:pare,
			url : url,
			type : "POST",
			cache : false,
			success : function(data){
				location.reload();
			},//返回数据填充
		});  
	}
	
	//删除模板（子
	$(".deleteSonTem").click(function(){
		$.ajax({
			url : '${hdpath}/wctemplate/deleteCoins',
			data : {id : $(this).val(),},
			type : "POST",
			cache : false,
			success : function(data){
				location.reload();
			},//返回数据填充
		}); 
	})
})		

</script>
</html>