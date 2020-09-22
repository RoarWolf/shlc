<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>提现管理</title>
<script type="text/javascript" src="${hdpath}/jedate/jedate.js"></script>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link rel="stylesheet" href="${hdpath}/css/admin.css">
<link rel="stylesheet" href="${hdpath}/hdfile/css/toastr.min.css">
<script src="${hdpath }/hdfile/js/toastr.min.js"></script>
<script src="${hdpath }/js/my.js"></script>
<script src="${hdpath }/js/md5.js"></script>
<script type="text/javascript">
	$(function() {
		
		$("button[id^='withdrawpass']").click(function() {
			var message = confirm("确认提现通过吗？");
			if (message == false) {
				return false;
			} else {
				$.ajax({
					url : '${hdpath}/pcmoney/setWithdrawStatusPass',
					data : {
						id : $(this).val(),
						status : 1,
					},
					type : "POST",
					cache : false,
					success : function(data) {
						if(data.code == 200){
							alert(data.messg);
							window.location.href = "${hdpath}/pcorder/selectWithdrawRecord";
						}else{
							alert(data.messg);
							return;
						}
					},
				});
			}
		})
		$("button[id^='withdrawfail']").click(function() {
			var message = confirm("确认拒绝此提现吗？");
			if (message == false) {
				return false;
			} else {
				$.ajax({
					url : '${hdpath}/pcmoney/setWithdrawStatusFail',
					data : {
						id : $(this).val(),
						status : 2,
						money : $("#money" + $(this).val()).html(),
						userId : $("#userid" + $(this).val()).val(),
					},
					type : "POST",
					cache : false,
					success : function(data) {
						if (data == 0) {
							alert("此条提现记录已处理，不需再处理");
						} else if (data == 1) {
							alert("处理成功");
							window.location.href = "${hdpath}/pcorder/selectWithdrawRecord";
						}
					},//返回数据填充
				});
			}
		})
	})
</script>
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
	  <li><a href="javascript:void(0)" target="right" class="icon-home">提现管理</a></li>
	</ul>
</div>
 <div class="admin">
	 <div class="panel admin-panel" id="adminPanel">
   		<div class="conditionsd">
		  <form action="${hdpath}/pcorder/selectWithdrawRecord" method="post" id="listform">
		    <div class="searchdiv">
		      <ul class="search" style="padding-left:10px;">
	       		<li>单号&nbsp;&nbsp;
	     		<input type="text" placeholder="请输入提现单号" name="withdrawnum" value="${withdrawnum}" class="frame6"/>
	     		</li>
	            <li>申请人&nbsp;&nbsp;
	     		<input type="text" placeholder="请输入姓名" name="realname" value="${realname}" />
	     		</li>
	       		<li>手机号&nbsp;&nbsp;
	     		<input type="text" placeholder="请输入电话号" name="phoneNum" value="${phoneNum}" class="frame2" />
	     		</li>
	       		<li>银行卡号&nbsp;&nbsp;
	     		<input type="text" placeholder="请输入银行卡号" name="bankcardnum" value="${bankcardnum}" class="frame5" />
	     		</li>
	       		<li>银行名称&nbsp;&nbsp;
	     		<input type="text" placeholder="请输入银行名字" name="bankname" value="${bankname}" class="frame1"/>
	     		</li>
	     		<li>类型&nbsp;&nbsp;
				<select name="type" style="height: 25px;">
					<option value="0">请选择</option>
					<option value="1" <c:if test="${type == 1}"> selected="selected"</c:if> >个人银行卡</option>
					<option value="2" <c:if test="${type == 2}"> selected="selected"</c:if> >对公账户</option>
				</select>
	     		<li>状态&nbsp;&nbsp;
				<select name="status" style="height: 25px;">
					<option value="-1">请选择</option>
					<option value="0" <c:if test="${status == 0}"> selected="selected"</c:if> >提现待处理</option>
					<option value="1" <c:if test="${status == 1}"> selected="selected"</c:if> >提现已到账</option>
					<option value="2" <c:if test="${status == 2}"> selected="selected"</c:if> >提现被拒绝</option>
					<option value="3" <c:if test="${status == 3}"> selected="selected"</c:if> >提现到零钱</option>
				</select>
	     		</li>
	     		<li>时间:&nbsp;&nbsp;<input type="text" name="startTime" id="startTime" placeholder="请选择时间" value="${startTime}"
									 onClick="jeDate({dateCell:'#startTime',isTime:true,format:'YYYY-MM-DD hh:mm:ss'})" readonly="readonly">
					    &nbsp;&nbsp; —
						&nbsp;&nbsp;<input type="text" name="endTime" id="endTime" placeholder="请选择时间" value="${endTime}"
									onClick="jeDate({dateCell:'#endTime',isTime:true,format:'YYYY-MM-DD hh:mm:ss'})" readonly="readonly">
				</li>
	       		<li class="cmbquery"> <input type="submit" style="width: 80px;" value="搜索"> </li>
		      </ul>
		   </div>
		</form>
   </div>
  <div class="table table-div">
		<table class="table table-hover" >
			<thead>
			<tr align="center">
				<th>提现单号</th>
				<th>申请人</th>
				<th>手机号</th>
				<th>账户与开户行</th>
				<th>类型</th>
				<th>现有金额</th>
				<th>提现金额</th>
				<th>到账金额</th>
				<th>手续费</th>
				<th>申请时间</th>
				<th>当前状态</th>
				<th>操作</th>
			</tr>
			</thead>
			<c:forEach items="${withdrawList}" var="order" varStatus="as">
				<tr align="center">
					<td>${order.withdrawnum}</td>
					<td>${order.realname}</td>
					<td>${order.phone_num}</td>
					<td>
							<div style="text-align: left; display: inline-block;">
									  <c:if test="${order.company!=null}">公司名称：${order.company}<br></c:if> 
							  银行名称：${order.bankname !=null ? order.bankname:"— —"}<br>
							  银行账号： <span class="bankNumSpan">${order.bankcardnum !=null ? order.bankcardnum:"— —"}</span>
							</div>
						<%-- ${order.bankcardnum !=null ? order.bankcardnum:"— —"}<br>
						${order.bankname !=null ? order.bankname:"— —"}
						<c:if test="${order.company!=null}"><br>${order.company}</c:if>  --%>
					</td>
					<td>${order.type == 1 ? "个人" : order.type==2 ? "对公" : "个人"}</td>
					<td>${order.user_money}</td>
					<td id="money${order.id}">${order.money}</td>
					<td style="color: #52cc45; font-weight: 700;"><fmt:formatNumber value="${order.money - order.servicecharge}" pattern="0.00" /></td>
					<td>${order.servicecharge }</td>
					<td><fmt:formatDate value="${order.create_time }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td>${order.status == 0 ? "<font color='gray'>提现待处理</font>" : order.status==1?"<font color='#00CC99'>提现已到账</font>":order.status==3?
					"<font color='#00CC99'>提现已到账</font>": order.status == 4 ? "<font color='gray'>待开发票</font>" : "<font color='#ea1111'>提现被拒绝</font>"}</td>
					<td>
						<button class="btn btn-success" id="withdrawpass${order.id }"
							value="${order.id }" <c:if test="${order.status != 0 && order.status != 4}">disabled="disabled" style="backgroundcolor: gray"</c:if> >通过</button>
						<button class="btn btn-danger" id="withdrawfail${order.id }"
							value="${order.id}" <c:if test="${order.status != 0 && order.status != 4}">disabled="disabled" style="backgroundcolor: gray"</c:if> >拒绝</button>
				</tr>
			</c:forEach>
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
  </div>
  
   <!-- 弹框 -->
   <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">修改费率</h4>
                </div>
                <div class="modal-body">
 						<input type="hidden" name="id" class="form-control" >
 						<input type="hidden" name="type" class="form-control" >
 						<input type="hidden" name="sort" class="form-control" >
 						<input type="hidden" name="pmerid" class="form-control" >
                    <div class="form-group">
                        <label for="txt_departmentname">费率</label>
                        <input type="text" name="rate" class="form-control" placeholder="请输入费率">
                        <p style="margin-top: 15px"><strong>注意：</strong>输入的范围为0~100之间的整数！</p>	 
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span>关闭</button>
                    <button type="button" id="btn_submit" class="btn btn-primary" data-dismiss="modal"><span class="glyphicon glyphicon-floppy-disk" aria-hidden="true"></span>保存</button>
                </div>
            </div>
        </div>
    </div>
    <!--  -->
</body>
<script type="text/javascript">
$(document).ready(function(){	
	$('#32'+' a').addClass('at');
	$('#32').parent().parent().parent().prev().removeClass("collapsed");
	$('#32').parent().parent().parent().prev().attr("aria-expanded",true)
	$('#32').parent().parent().parent().addClass("in");
	$('#32').parent().parent().parent().prev().attr("aria-expanded",true)
	})
</script>
<script type="text/javascript">
function currentPagenum(mark){//指定页
	var arguments = $("#listform").serialize()+"&currentPage="+mark;
	window.location.href="${pageContext.request.contextPath}/pcorder/selectWithdrawRecord?"+arguments; 
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
	window.location.href="${pageContext.request.contextPath}/pcorder/selectWithdrawRecord?"+arguments; 
}
</script>
</html>