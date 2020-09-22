<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>小区管理</title>
<script type="text/javascript" src="${hdpath}/jedate/jedate.js"></script>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link rel="stylesheet" href="${hdpath}/css/admin.css">
<style type="text/css">
.modal-backdrop {
    position: relative; 
}
.form-group label{width: 12%;}
.form-group input{width: 50%;}
</style>
</head>
<body style="background-color:#f2f9fd;">
<div class="header bg-main">
	<%@ include file="/WEB-INF/views/navigation.jsp"%>
</div>
<div class="leftnav" id="lefeMenu">
	<%@ include file="/WEB-INF/views/menu.jsp"%>
</div>
<div>
	<ul class="bread">
	  <li><a href="javascript:void(0)" target="right" class="icon-home">小区管理</a></li>
	</ul>
</div>
<div class="admin">
	 <div class="panel admin-panel" id="adminPanel">
    		<div class="conditionsd">
			  <form method="post" action="${hdpath}/pcHousing/housingAdress" id="listform">
			    <div class="searchdiv">
			     <ul class="search" style="padding-left:10px;">
 					<li>
		     		小区名称:&nbsp;&nbsp;
		     		<input type="text" placeholder="请输入区域名" name="name" class="input" value="${name}"  />
		     		</li>
		       		<li>
		     		地址:&nbsp;&nbsp;
		     		<input type="text" placeholder="请输入地址" name="address" class="input" value="${address}"  />
		     		</li>
		       		<li>
		     		商户名:&nbsp;&nbsp;
		     		<input type="text" placeholder="请输入商户名" name="dealer" class="input" value="${dealer}"  />
		     		</li>
		       		<li>
		     		商户电话:&nbsp;&nbsp;
		     		<input type="text" placeholder="请输入商户电话" name="phoneNum" class="input" value="${phoneNum}"  />
		     		</li>
		       		<li>
		     		合伙人:&nbsp;&nbsp; 
		     		<input type="text" placeholder="请输入副号名" name="manarealname" class="input" value="${manarealname}"  />
		     		</li>
		       		<li>
		     		合伙人电话:&nbsp;&nbsp;
		     		<input type="text" placeholder="请输入副号电话" name="manaphonenum" class="input" value="${manaphonenum}"  />
		     		</li>
		     		<li>创建时间:&nbsp;&nbsp;<input type="text" name="startTime" id="startTime" placeholder="请选择时间" value="${startTime}"
		     								onClick="jeDate({dateCell:'#startTime',isTime:true,format:'YYYY-MM-DD hh:mm:ss'})" readonly="readonly">
											&nbsp;&nbsp; —
							   &nbsp;&nbsp;<input type="text" name="endTime" id="endTime" placeholder="请选择时间" value="${endTime}"
		     								onClick="jeDate({dateCell:'#endTime',isTime:true,format:'YYYY-MM-DD hh:mm:ss'})" readonly="readonly">
					</li>
		       		<li>
		            	<input type="submit" style="width: 80px;" value="搜索">
		      	 	</li>
      			</ul>
			   </div>
			</form>
  		 </div>
  		 <hr>
		 <div class="table table-div">
			<table class="table table-hover" >
			    <thead>
				   <tr>
					<th>序号</th>
					<th>小区名称</th>
					<th>地址</th>
					<th>设备数量</th>
					<th>在线卡数量</th>
					<th>商户名</th>
					<th>商户电话</th>
					<th>合伙人</th>
					<th>合伙人电话</th>
					<!-- <th>模板名</th> -->
					<th>创建时间</th>
					<!-- <th>修改时间</th> -->
				  </tr>
				</thead>
				<tbody>
				   <c:forEach items="${traderecord}" var="order"  varStatus="as">
					  <tr id="name${order.id}" >
						<td >${as.count+current}</td>
						<td>${order.name}</td>
						<td>${order.address !=null ? order.address:"— —"}</td>
						<td>
							<c:if test="${order.tnumber==0}">${order.tnumber}</c:if>
							<c:if test="${order.tnumber>0}"><a href="/pcequipment/selectEquList?aid=${order.id}&areaname=${order.name}">${order.tnumber}</a></c:if>
						</td>
						<td>
							<c:if test="${order.onlincount==0}">${order.onlincount}</c:if>
							<c:if test="${order.onlincount>0}"><a href="/pccardrecord/selectonlinecard?areaname=${order.name}">${order.tnumber}</a></c:if>
						</td>
						<td>${order.dealer!=null ? order.dealer : "— —"}</td>
						<td>
							<c:if test="${order.uphonenum==null}">— —</c:if>
							<c:if test="${order.uphonenum!=null}"><a href="/pcequipment/selectEquList?phoneNum=${order.uphonenum}">${order.uphonenum}</a></c:if>
						</td>
						<td>${order.manarealname!=null ? order.manarealname : "— —"}</td>
						<td>
							<c:if test="${order.manaphonenum==null}">— —</c:if>
							<c:if test="${order.manaphonenum!=null}"><a href="/pcequipment/selectEquList?phoneNum=${order.manaphonenum}">${order.manaphonenum}</a></c:if>
						</td>
						<%-- <td>${order.temname !=null ? order.temname:"— —"}</td> --%>
					    <td><fmt:formatDate value="${order.create_time}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					    <%-- <td><fmt:formatDate value="${order.update_time}" pattern="yyyy-MM-dd HH:mm:ss" /></td> --%>
					   </tr>
					   <!-- 弹框 -->
					   <div class="modal fade${order.id}" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
					        <div class="modal-dialog" role="document">
					            <div class="modal-content">
					                <div class="modal-header">
					                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					                    <h4 class="modal-title" id="myModalLabel"></h4>
					                </div>
					                <form id="dialogform">
					                <div class="modal-body">
										<input type="hidden" name="source">
										<input type="hidden" name="id">
					                    <div class="form-group">
					                        <label for="txt_parentdepartment">小区名称</label>
					                        <input type="text" name="name"  placeholder="请输入区域名">
					                    </div>
					                    <div class="form-group">
					                        <label for="txt_parentdepartment">商户名</label>
					                        <input type="text" name="dealer"  placeholder="请输入商户名" disabled="disabled" >
					                    </div>
					                    <div class="form-group">
					                        <label for="txt_parentdepartment">商户电话</label>
					                        <input type="text" name="uphonenum"  placeholder="请输入商户电话">
					                    </div>
					                    <div class="form-group">
					                        <label for="txt_parentdepartment">副号名字</label>
					                        <input type="text" name="manage"  placeholder="请输入副号名字" disabled="disabled" >
					                    </div>
					                    <div class="form-group">
					                        <label for="txt_parentdepartment">副号电话</label>
					                        <input type="text" name="managephone"  placeholder="请输入副号电话">
					                    </div>
					                    <!-- <div class="form-group">
					                        <label for="txt_parentdepartment">模板名</label>
					                        <input type="text" name="temname"  placeholder="请输入模板名">
					                        <input type="hidden" name="tempid" >
					                    </div> -->
					                    <div class="form-group">
					                        <label for="txt_parentdepartment">地址</label>
					                        <input type="text" name="address"  placeholder="请输入地址">
					                    </div>
					                </div>
					                </form>
					                <div class="modal-footer">
					                    <button type="button" class="btn btn-default" data-dismiss="modal"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span>取消</button>
					                    <button type="button" class="btn btn-primary" data-dismiss="modal"><span class="glyphicon glyphicon-ok" aria-hidden="true"></span>确认</button>
					                </div>
					            </div>
					        </div>
					    </div>
					    <!--  -->
					</c:forEach>
				 </tbody>
			   </table>
			   <!-- 页码内容显示 -->
			   <div align="center">
				<%@ include file="/WEB-INF/views/public/pagearea.jsp"%>
			   </div>
			  <!-- ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ -->
			   </div>
   			</div>
</div>
</body>

<script src="${hdpath}/js/paging.js"></script>
<script type="text/javascript">
$(document).ready(function(){ menulocation(61); });
function muiareaedit(id){//编辑
	$.ajax({
        type : "POST",
		url : '${hdpath}/pcHousing/selectByIdArea', //处理页面的路径
		data : {id:id},                
	    success:function(e){ //服务器返回响应，根据响应结果，分析是否登录成功； dealer uphonenum temname address
	    	$("#myModal").find("input[name='source']").attr("value","2");
	    	$("#myModal").find("input[name='id']").attr("value",e.id);
	        $("#myModal").find("input[name='name']").attr("value",e.name);
	        $("#myModal").find("input[name='dealer']").attr("value",e.dealer);
	        $("#myModal").find("input[name='uphonenum']").attr("value",e.uphonenum);
	        $("#myModal").find("input[name='manage']").attr("value",e.manarealname);
	        $("#myModal").find("input[name='managephone']").attr("value",e.manaphonenum);
	        /* $("#myModal").find("input[name='temname']").attr("value",e.temname); */
	        $("#myModal").find("input[name='address']").attr("value",e.address);
	    	$("#myModal").modal();
	    },
	    error:function(){//异常处理；
	    	 mui.toast('错误!')
	    }
	})
}
$(".btn-primary").click(function(){
	var source = $("#myModal").find("input[name='source']").val();
	var url;
	if(source==1){//添加
		url = "${hdpath}/pcHousing/insertByParame";
	}else if(source==2){//修改
		url = "${hdpath}/pcHousing/updateByParame";
	}
	var arguments = $("#dialogform").serialize();
	$.ajax({
        type : "POST",
		url : url, //处理页面的路径
		data : arguments,                
	    success:function(e){ //服务器返回响应，根据响应结果，分析是否登录成功； dealer uphonenum temname address
	    	if(e==2){
	    		alert("商户手机号不存在，请重新输入！")
	    	}else if(e==3){
	    		alert("副号手机号不存在，请重新输入！")
	    	}else{
	    		alert("success")
	    	}
	    },
	    error:function(){//异常处理；
	    	 mui.toast('错误!')
	    }
	})
	
})

function muiareadelete(id){//删除
	var statu = confirm("确认删除？");
	if(!statu){
	  return false;
	}
	$.ajax({
        type : "POST",
		url : '${hdpath}/pcHousing/deleteByArea', //处理页面的路径
		data : {id:id},                
		success:function(e){ //服务器返回响应，根据响应结果，分析是否登录成功；
			 alert("成功");
	    	 location.reload();
	    },
	    error:function(){//异常处理；
	    	 mui.toast('错误!')
	    }
	})
}

</script>
<script type="text/javascript">
function currentPagenum(mark){//指定页
	var arguments = $("#listform").serialize()+"&currentPage="+mark; 
	window.location.href="${pageContext.request.contextPath}/pcHousing/housingAdress?"+arguments; 
}
function  currentPage(mark){
	var arguments = $("#listform").serialize();
	if(mark==0){//首页
		arguments += "&currentPage=1"; 
	}else if(mark==1){//末页
		arguments += "&currentPage="+${pageBean.totalPages}; 
	}else if(mark==2){//上一页
		arguments += "&currentPage="+${pageBean.currentPage-1};
	}else if(mark==3){//下一页
		arguments += "&currentPage="+${pageBean.currentPage+1}; 
	}else if(mark==4){//go跳转页
		var currentPage = $("input[name='pageNumber']").val();
		if(currentPage==null ||currentPage==""){
			alert("请输入页码。");
			return;
		}
		arguments += "&currentPage="+currentPage; 
	}
	window.location.href="${pageContext.request.contextPath}/pcHousing/housingAdress?"+arguments; 
}
</script>
</html>
