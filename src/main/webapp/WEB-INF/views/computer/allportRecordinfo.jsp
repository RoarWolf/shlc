<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设备端口状态日志</title>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link rel="stylesheet" href="${hdpath}/css/admin.css">
<script type="text/javascript" src="${hdpath}/laydate/laydate.js"></script>
<style type="text/css">
html,body,table{width:100%;}
td {
	text-align: center;
}
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
	  <li><a href="javascript:void(0)" target="right" class="icon-home">设备日志</a></li>
	</ul>
	<span style="margin-left: 20%;">当前设备编号：${equipmentnum} — — 端口：${port}</span>
</div>
<div class="admin">
<div class="conditionsd">
  <form method="post" action="${hdpath}/pcequipment/selectEquipmentLog" id="listform">
    <div class="searchdiv">
    		<input type="hidden" name="equipmentnum" value="${equipmentnum}"/>
     		<input type="hidden" name="port" value="${port}"/>
      <ul class="search" style="padding-left:10px;">
      		<li>
     		实时功率&nbsp;&nbsp;
     		<input type="text" placeholder="输入最大实时功率" name="power" class="input" value="${power}"/>
      		<input type="hidden" name="parm" value="0"/>
     		</li>
       		<li>
     		剩余电量&nbsp;&nbsp;
     		<input type="text" placeholder="输入最大剩余电量" name="elec" class="input" value="${elec}"/>
     		</li>
     		 <li>
     		剩余时间&nbsp;&nbsp;
     		<input type="text" placeholder="输入最大剩余时间" name="time" class="input" value="${time}"/>
     		</li>
     		<li>端口状态&nbsp;&nbsp;
     		<select name="status">
				<option value="0">请选择</option>
				<option value="1" <c:if test="${status == 1}"> selected="selected"</c:if> >空闲</option>
				<option value="2" <c:if test="${status == 2}"> selected="selected"</c:if> >使用</option>
				<option value="3" <c:if test="${status == 3}"> selected="selected"</c:if> >禁用</option>
				<option value="4" <c:if test="${status == 4}"> selected="selected"</c:if> >故障</option>
			</select>
     		</li>    		
     		<li> 时间:&nbsp;&nbsp;<input type="text" placeholder="请输入日期" name="startTime" id="startTime" value="${startTime}" class="inline laydate-icon">
				  —
				<input type="text" placeholder="请输入日期" name="endTime" id="endTime" value="${endTime}" class="inline laydate-icon">							 
			</li>
       		<li class="cmbquery">
            	<input type="submit" style="width: 80px;" value="搜索">
      	 	</li>
        </ul>
     </div>
   </form>
</div>
<div class="table table-div">
	<table class="table table-hover" >
	    <thead>
			<tr>
			   <th>记录时间</th>
			   <th>端口状态</th>
			   <th>剩余时间</th>
			   <th>实时功率</th>
			   <th>剩余电量</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${portRecord }" var="record">
				<tr>
					<td><fmt:formatDate value="${record.record_time }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td>
						<c:choose>
							<c:when test="${record.status == 1}">空闲</c:when>
							<c:when test="${record.status == 2}">使用</c:when>
							<c:when test="${record.status == 3}">禁用</c:when>
							<c:when test="${record.status == 4}">故障</c:when>
							<c:otherwise>— —</c:otherwise>
						</c:choose>
					</td>
					<td>${record.time }</td>
					<td>${record.power }</td>
					<td>${record.elec /100 }</td>
				</tr>
			</c:forEach>
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
 </div>
</body>
<script type="text/javascript">
$(document).ready(function(){	
	$('#42'+' a').addClass('at');
	$('#42').parent().parent().parent().prev().removeClass("collapsed");
	$('#42').parent().parent().parent().prev().find('span').css('class', 'pull-right glyphicon glyphicon-chevron-toggle glyphicon-minus');
	$('#42').parent().parent().parent().prev().attr("aria-expanded",true)
	$('#42').parent().parent().parent().addClass("in");
	$('#42').parent().parent().parent().prev().attr("aria-expanded",true)
	})
</script>
<script type="text/javascript">
!function(){
	laydate.skin('molv');//切换皮肤，请查看skins下面皮肤库
	}();
	//日期范围限制
	var start = {
	    elem: '#startTime',
	    format: 'YYYY-MM-DD hh:mm:ss',
	    min: '1900-01-01 00:00:00', //设定最小日期为当前日期
	    max: '2099-06-16 23:59:59', //最大日期
	    istime: true,
	    istoday: false,
	    choose: function(datas){
	         end.min = datas; //开始日选好后，重置结束日的最小日期
	         end.start = datas //将结束日的初始值设定为开始日
	    }
	};
	var end = {
	    elem: '#endTime',
	    format: 'YYYY-MM-DD hh:mm:ss',
	    min: '1900-01-01 00:00:00', //设定最小日期为当前日期
	    max: '2099-06-16 23:59:59', //最大日期
	    istime: true,
	    istoday: false,
	    choose: function(datas){
	        //start.max = datas; //结束日选好后，充值开始日的最大日期
	    }
	};
	laydate(start);
	laydate(end);
 </script>
<script type="text/javascript">
function currentPagenum(mark){//指定页<input type="hidden" name="parm" value="0"/>
	var arguments = $("#listform").serialize()+"&currentPage="+mark+"&parm=0";
	window.location.href="${pageContext.request.contextPath}/pcequipment/selectEquipmentLog?"+arguments; 
}
function  currentPage(mark){
	var arguments = $("#listform").serialize()+"&parm=0";
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
	window.location.href="${pageContext.request.contextPath}/pcequipment/selectEquipmentLog?parm=0&"+arguments; 
}
</script>
</html>