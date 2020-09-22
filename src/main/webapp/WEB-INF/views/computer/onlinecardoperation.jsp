<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>在线卡操作</title>
<script type="text/javascript" src="${hdpath}/jedate/jedate.js"></script>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link rel="stylesheet" href="${hdpath}/css/admin.css">
<link rel="stylesheet" href="${hdpath}/css/jquery-ui.css">
<script type="text/javascript" src="${hdpath}/js/jquery-ui.js"></script>
<style type="text/css">
.dialog-htm{ padding: 18px;}
.dialog .dialog-htm label{float: left; width: 15%; margin:0;line-height: 2; font-size: 16px;}
.dialog .dialog-htm input{line-height: 2; width: 75%;}
.dialog .dialog-htm select{padding: 8px 0; width: 75%;}
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
	  <li><a href="javascript:void(0)" target="right" class="icon-home">在线卡操作</a></li>
	</ul>
</div>
<div class="admin">
   <div class="panel admin-panel" id="adminPanel">
     <div class="conditionsd">
	   <form method="post" action="${hdpath}/pccardrecord/selectcardoperation" id="listform">
		   <div class="searchdiv">
		      <ul class="search" style="padding-left:10px;">
				<li>用户&nbsp;&nbsp; 
					<input type="text" placeholder="输入卡号" name="nickname" class="frame2"  value="${nickname}"/>
				</li>
				<li>卡号&nbsp;&nbsp; 
					<input type="text" placeholder="输入卡号" name="cardID" class="frame2"  value="${cardID}"/>
				</li>
				<li>状态&nbsp;&nbsp; 
					<select name="status" class="frame1">
						<option value="">请选择</option>
						<option value="2" <c:if test="${status == 2}">selected="selected"</c:if>>激活</option>
						<option value="3" <c:if test="${status == 3}">selected="selected"</c:if>>绑定</option>
						<option value="4" <c:if test="${status == 4}">selected="selected"</c:if>>解绑</option>
						<option value="5" <c:if test="${status == 5}">selected="selected"</c:if>>挂失</option>
						<option value="6" <c:if test="${status == 6}">selected="selected"</c:if>>解挂</option>
					</select>
				</li>
	     		<li>时间:&nbsp;&nbsp;<input type="text" name="startTime" id="startTime" placeholder="请选择时间" value="${startTime}"
	     							onClick="jeDate({dateCell:'#startTime',isTime:true,format:'YYYY-MM-DD hh:mm:ss'})" readonly="readonly">
					    &nbsp;&nbsp; —
						&nbsp;&nbsp;<input type="text" name="endTime" id="endTime" placeholder="请选择时间" value="${endTime}"
	     							onClick="jeDate({dateCell:'#endTime',isTime:true,format:'YYYY-MM-DD hh:mm:ss'})" readonly="readonly">
				</li>
				<li class="cmbquery"><input type="submit" style="width: 80px;" value="搜索">
				</li>
			</ul>
		</div>
	</form>
   </div>
 <div class="table table-div">
  <table class="table table-hover">
	<thead>
		<tr>
			<th>序号</th>
			<th>单号</th>
			<th>用户</th>
			<th>卡号</th>
			<th>操作人</th>
			<th>余额</th>
			<th>设备</th>
			<th>状态</th>
			<th>创建时间</th>
			<!-- <th>操作</th> -->
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${cardoperation}" var="order" varStatus="as">
			<tr>
				<td>${as.count+current}</td>
				<td><a href="/pcorder/TraderecordDetails?paysource=5&ordernum=${order.ordernum}&orderid=${order.id}">${order.ordernum}</a></td>
				<td>${order.nickname}</td>
				<td>${order.cardID}</td>
				<td>${order.operanick}</td>
				<td>${order.balance}</td>
				<td>${order.code != null ? order.code : "— —"}</td>
				<td>${order.status == 0 ? "支付未成功" : order.status == 1 ? "成功" : order.status == 2 ? "激活" : order.status == 3 ? "绑定" : order.status == 4 ? "解绑" : order.status == 5 ? "挂失" : order.status == 6 ? "解挂" : "其它"}</td>
				<td><fmt:formatDate value="${order.create_time}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<%-- <td><button onClick="operation('${order.cardID}')" class="btn btn-success">操作</button></td> --%>
			</tr>
		</c:forEach>
		<c:if test="${cardoperation==null}"><tr><td colspan="8">暂无信息</td></tr></c:if>
	</tbody>
  </table>
	<div align="center">
	<%-- 构建分页导航 --%>
		共有${pageBean.totalRows}条数据，共${pageBean.totalPages}页，当前为${pageBean.currentPage}页
		<br/>
		 <span class="btn btn-success" onclick="currentPage(0)">首页</span>
		 <c:if test="${pageBean.currentPage >1}">
		 	<span class="btn btn-success" onclick="currentPage(2)">上一页</span>
		 </c:if>
		 
		 <c:forEach begin="${pageBean.start}" end="${pageBean.end}" step="1" var="i">
	              	<span onclick="currentPagenum(${i})">${i} </span>
		 </c:forEach>
		 
		 <c:if test="${pageBean.currentPage < pageBean.totalPages}">
		 	<span class="btn btn-success" onclick="currentPage(3)">下一页</span>
		 </c:if>
		<span class="btn btn-success" onclick="currentPage(1)">尾页</span>
		<form style="display: inline;">
			<input type="text" name="pageNumber" style="width: 50px"> 
			<input class="btn btn-info" type="button" onclick="currentPage(4)" value="go">
		</form>
	</div>
	 <!-- ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ -->
 </div>
 <div id="dialogId" title="修改" class="dialog" style="display:none">
  <form method="post" id="dialogform">
	<input name="id" type="hidden" />
	<div class="dialog-htm">
		<label>卡号：</label> <input name="cardID" type="text" />
	</div>
	<div class="dialog-htm">
		<label>金额：</label> <input name="cardmoney" type="text" />
	</div>
	<div class="dialog-htm">
		<label>状态：</label> 
		<select name="status">
			<option value="">请选择</option>
			<option value="0">初始</option>
			<option value="1">正常</option>
			<option value="2">挂失</option>
			<option value="3">注销</option>
		</select>
	</div>
	<div class="dialog-htm">
		<label>备注：</label> <input name="remark" type="text" />
	</div>
  </form>
 </div>
</div>
</div>
</body>
<script type="text/javascript">
function operation(cardID){//查询
	$.ajax({
		data:{cardID:cardID},
        type : "POST",
        url : "${hdpath}/pccardrecord/onlinecardBycardID",
        timeout : 3000, //超时时间设置，单位毫秒
        success : function(e) {
        	alert("e:    "+e)
    		$("#dialogform input[name='id']").val(e.id);
    		$("#dialogform input[name='cardID']").val("'"+e.cardID+"'");
    		$("#dialogform input[name='cardmoney']").val(e.money);
    		$("#dialogform input[name='remark']").val(e.remark);
    		$("#dialogform input[name='cardID']").attr("disabled","disabled");
    		$("#dialogform input[name='cardmoney']").attr("disabled","disabled");
    		var url = "${hdpath}/pccardrecord/editonlinecard";
    		dialogclick(url);
        }
    });
}
function dialogclick(url){
	var status = $("#dialogform input[name='status']").val();
	if(status==""){
		alert("请选择状态！")
		return false;
	}
    $("#dialogId").dialog({
        height: 400,
        width: 450,
        // 模态开启  
        modal: true,
        // 是否可拖拽  
        draggable: true,
        // 最小宽度  
        minWidth: 300,
        buttons:{
                "提交": function(){
            		pare = $("#dialogform").serialize();
                	$.ajax({
                		data:pare,
                        type : "POST",
                        url : url,
                        timeout : 3000, //超时时间设置，单位毫秒
                        success : function(res) {
                        	if(res == 1){
                        		window.location.reload();
                        	}else{
                        		alert("操作失败");
                        		return false;
                        	}
                        }
                    });
                },
			    "取消": function(){
			    	$(this).dialog("close");
			     }
        }
    });  
}
$(document).ready(function(){	
	$('#51'+' a').addClass('at');
	$('#51').parent().parent().parent().prev().removeClass("collapsed");
	$('#51').parent().parent().parent().prev().find('span').css('class', 'pull-right glyphicon glyphicon-chevron-toggle glyphicon-minus');
	$('#51').parent().parent().parent().prev().attr("aria-expanded",true)
	$('#51').parent().parent().parent().addClass("in");
	$('#51').parent().parent().parent().prev().attr("aria-expanded",true)
})
function currentPagenum(mark){//指定页
	var arguments = $("#listform").serialize()+"&currentPage="+mark;
	window.location.href="${pageContext.request.contextPath}/pccardrecord/selectcardoperation?"+arguments; 
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
	window.location.href="${pageContext.request.contextPath}/pccardrecord/selectcardoperation?"+arguments; 
}


</script>
</html>