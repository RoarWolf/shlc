<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>设备列表</title>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link rel="stylesheet" href="${hdpath}/css/admin.css">
<script src="${hdpath }/js/my.js"></script>
<style type="text/css">
.modal-backdrop {
    position: relative; 
}
</style>
</head>
<body style="background-color:#f2f9fd;">
<div class="header bg-main">
	<%@ include file="/WEB-INF/views/navigation.jsp"%>
</div>
<div class="leftnav" id="lefeMenu">
	<%@ include file="/WEB-INF/views/menu.jsp"%>
	<script type="text/javascript" src="/js/jquery.qrcode.js"></script> 
<script type="text/javascript" src="/js/qrcode.js"></script> 
</div>
<div>
	<ul class="bread">
	  <li><a href="javascript:void(0)" target="right" class="icon-home">设备列表</a></li>
	</ul>
</div>
 <div class="admin">
	 <div class="panel admin-panel" id="adminPanel">
    		<div class="conditionsd">
    			<c:if test="${admin.rank==0}">
    		     <div style="margin-left: 1%; border: 2px solid #f7efef;"> 
			     	<span>汇总：&nbsp;&nbsp;</span>
			     	<span>&nbsp;&nbsp;设备总数：${equparam.totalRows}</span> 
			     	<span>&nbsp;&nbsp;&nbsp;&nbsp;在线总数：${equparam.online}</span>
			     	<span>&nbsp;&nbsp;&nbsp;&nbsp;离线总数：${equparam.disonline}</span>
			     	<span>&nbsp;&nbsp;&nbsp;&nbsp;绑定总数：${equparam.binding}</span>
			     	<span>&nbsp;&nbsp;&nbsp;&nbsp;未绑定数：${equparam.disbinding}</span>
			     </div>
			    </c:if>
			  <form method="post" action="${hdpath}/pcequipment/selectEquList" id="listform" >
			    <div class="searchdiv">
			      <ul class="search" style="padding-left:10px;">
            <li>
      		编号&nbsp;&nbsp;
      		<input type="text" value="${code}" placeholder="输入编号" name="code" id="code" class="frame1" />
      		</li>
      		<li>
      		所有人&nbsp;&nbsp;
      		<input type="text" value="${username}" placeholder="请输入所有人" name="username" />
      		</li>
      		<li>
      		电话&nbsp;&nbsp;
      		<input type="text" value="${phoneNum}" placeholder="请输入电话" name="phoneNum" />
      		</li>
        	<li>
      		IMEI&nbsp;&nbsp;
      		<input type="text" value="${imei}" placeholder="请输入imei" name="imei" class="frame4" />
      		</li>
        	<li>
      		CCID&nbsp;&nbsp;
      		<input type="text" value="${ccid}" placeholder="请输入ccid" name="ccid" class="frame6" />
      		</li>
        	<li>
      		硬件版本&nbsp;&nbsp;
      		<input type="text" value="${hardversion}" placeholder="硬件版本" name="hardversion" class="frame1"/>
      		</li>
        	<li>
      		软件版本号&nbsp;&nbsp;
      		<input type="text" value="${softversionnum}" placeholder="软件版本号" name="softversionnum" class="frame1"/>
      		</li>
            <li>
      		模块版本&nbsp;&nbsp;
      		<input type="text" value="${hardversionnum}" placeholder="模块版本" name="hardversionnum" class="frame1"/>
      		</li>
            <li>
     		小区名称:&nbsp;&nbsp;
     		<input type="text" value="${areastate}" placeholder="输入小区名称" name="areastate" class="frame2" />
     		</li>
      		<li>
      		信号强度&nbsp;&nbsp;
      		<select name="csq">
					<option value="-1" <c:if test="${csq == -1}">selected="selected"</c:if>>请选择</option>
					<option value="0" <c:if test="${csq == 0}">selected="selected"</c:if>>从小到大</option>
					<option value="1" <c:if test="${csq == 1}">selected="selected"</c:if>>从大到小</option>
			</select>
      		</li>
      		<li>
      		状态&nbsp;&nbsp;
      		<select name="state">
					<option value="-1" <c:if test="${state == -1}">selected="selected"</c:if>>请选择</option>
					<option value="1" <c:if test="${state == 1}">selected="selected"</c:if>>在线</option>
					<option value="0" <c:if test="${state == 0}">selected="selected"</c:if>>离线</option>
			</select>
      		</li>
      		<li>
      		绑定&nbsp;&nbsp;
      		<select name="line">
					<option value="-1" <c:if test="${line == -1}">selected="selected"</c:if>>请选择</option>
					<option value="1" <c:if test="${line == 1}">selected="selected"</c:if>>已绑定</option>
					<option value="0" <c:if test="${line == 0}">selected="selected"</c:if>>未绑定</option>
			</select>
      		</li>
      		<li>
      		测试状态&nbsp;&nbsp;
      		<select name="testnum">
					<option value="0" <c:if test="${testnum == 0}">selected="selected"</c:if>>请选择</option>
					<option value="1" <c:if test="${testnum == 1}">selected="selected"</c:if>>设备不可测试</option>
					<option value="2" <c:if test="${testnum == 2}">selected="selected"</c:if>>设备可测试</option>
					<option value="3" <c:if test="${testnum == 3}">selected="selected"</c:if>>设备测试达限</option>
			</select>
      		</li>
        	<li class="cmbquery">
             <input type="submit" style="width: 80px;" value="搜索">
       	 	</li>
     	 </ul>
       </div>
	 </form>
   </div>
				<div class="table table-div">

					<table class="table table-hover">
						<thead>
							<tr>
								<th>设备编号</th>
								<th>设备名</th>
								<th>归属小区</th>
								<th>所有人</th>
								<th>到期时间</th>
								<th>修改</th>
								<th>电话</th>
								<th>IMEI</th>
								<th>CCID</th>
								<th>硬件版本</th>
								<th>模块版本</th>
								<th>软件版本号</th>
								<th>信号强度</th>
								<th>状态</th>
								<c:if test="${admin.rank==0}">
								<th>操作</th>
								<th>测试状态</th>
								</c:if>
								<th>生成二维码</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${equipmentList}" var="equ">
								<tr>
									<td><a href="/pcequipment/selectEquipmentLog?equipmentnum=${equ.code}">${equ.code}</a></td>
									<td>${equ.remark != null ?equ.remark:"— —"}</td>
									<td>${equ.name != null ? equ.name:"— —"}</td>
									<td>${equ.username != null ?equ.username:"— —"}</td>
									<td>${equ.expiration_time != null ?equ.expiration_time:"— —"}</td>
									<td><a href="">修改</a></td>
									<td>
									<c:if test="${equ.phone_num != null}"><a href="/pcadminiStrator/selectDealerUserInfo?phoneNum=${equ.phone_num}">${equ.phone_num}</a></c:if>
									<c:if test="${equ.phone_num == null}"> — — </c:if>
									</td>
									<td>${equ.imei}</td>
									<td>${equ.ccid}</td>
									<td>${equ.hardversion}&nbsp;&nbsp;<span onclick="changesql('${equ.code}','${equ.hardversion}')" class="glyphicon glyphicon-pencil"></span></td>
									<td>${equ.hardversionnum}</td>
									<td>${equ.softversionnum}</td>
									<td>${equ.csq}</td>
									<td>${equ.state == 1 ? "在线" : "离线" }</td> 
									<c:if test="${admin.rank==0}">
									<td><a href="${hapath }/pcequipment/pctest?code=${equ.code}" class="btn btn-success">详情</a></td>
									<td>
										<c:if test="${equ.bindtype==1}"><button disabled="disabled" class="btn btn-default active">重置&nbsp;${10-equ.several}</button></c:if>
										<c:if test="${equ.bindtype==0}"><button onclick="reset('${equ.code}')" class="btn btn-success">重置&nbsp;${10-equ.several}</button></c:if>
									</td>
									</c:if>
									<td>
										<button id="create${equ.code }" type="button" class="btn btn-primary" data-toggle="modal"
											data-target=".${equ.code }" data-wolfdata="${equ.code }">生成</button>
										<%-- 	data-target=".${equ.code }" data-wolfdata="${equ.code }">${equ.code }号机生成</button> --%>
										<div class="modal fade ${equ.code }" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
											<div class="modal-dialog modal-sm" role="document">
												<div align="center" id="wolf${equ.code }" class="modal-content">
													<div align="center" id="wolfQrcode${equ.code }" style="margin-top: 10px">
														<%-- <img align="middle" src="${hdpath }/equipment/hdqrcode${equ.code}.jpg"> --%>
													</div>
													<font size="5px">设备编号：${equ.code }</font>
												</div>
											</div>
										</div> 
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<div align="center">
						<%-- 构建分页导航 --%>
						共有${pageBean.totalRows}台设备，共${pageBean.totalPages}页，当前为${pageBean.currentPage}页
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
				   <!-- 修改弹框  -->
				   <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
				        <div class="modal-dialog" role="document">
				            <div class="modal-content">
				                <div class="modal-header">
				                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				                    <h4 class="modal-title" id="myModalLabel"></h4>
				                </div>
				                <div class="modal-body">
				                    <div class="form-group">
				                        <label for="txt_departmentname">设备号</label>
				                        <input type="text" name="code" class="form-control" Readonly="Readonly">
				                    </div>
				                    <!-- <div class="form-group">
				                        <label for="txt_departmentlevel">硬件版本</label>
				                        <input type="text" name="hardversion" class="form-control" >
				                    </div> -->
									<div class="form-group">
										<label for="txt_departmentlevel">硬件版本</label>
										<select class="form-control input-lg" id="hardversion" name="hardversion" style="font-size: 15px;">
											<option value="00">00  出厂默认设置</option>
											<option value="01">01  十路智慧款</option>
											<option value="02">02  电轿款</option>
											<option value="03">03  脉冲板子</option>
											<option value="04">04  离线充值机</option>
											<option value="05">05  十六路智慧款</option>
											<option value="06">06 二十路智慧款</option>
										</select>
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
				    <!-- 密码弹框 -->
				   <div class="modal fade" id="pwdModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
				        <div class="modal-dialog" role="document">
				            <div class="modal-content">
				                <div class="modal-header">
				                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				                    <h4 class="modal-title" id="pwdModalLabel"></h4>
				                </div>
				                <div class="modal-body">
				                    <div class="form-group">
				                        <label for="txt_departmentlevel">密码</label>
				                        <input type="hidden" name="sort" class="form-control" >
				                        <input type="password" name="password" class="form-control"  value="" >
				                    </div>
				                </div>
				                <div class="modal-footer">
				                    <button type="button" class="btn btn-default" data-dismiss="modal"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span>关闭</button>
				                    <button type="button" id="btn_submitped" class="btn btn-primary" data-dismiss="modal"><span class="glyphicon glyphicon-floppy-disk" aria-hidden="true"></span>保存</button>
				                </div>
				            </div>
				        </div>
				    </div>
				</div>
			</div>
	</div>
</body>
<script type="text/javascript">
$(document).ready(function(){	
	$('#41'+' a').addClass('at');
	$('#41').parent().parent().parent().prev().removeClass("collapsed");
	$('#41').parent().parent().parent().prev().find('span').css('class', 'pull-right glyphicon glyphicon-chevron-toggle glyphicon-minus');
	$('#41').parent().parent().parent().prev().attr("aria-expanded",true)
	$('#41').parent().parent().parent().addClass("in");
	$('#41').parent().parent().parent().prev().attr("aria-expanded",true)
	})
</script>
<script type="text/javascript">
function reset(code){
	$("#pwdModalLabel").text("请输入密码");
	$("#pwdModal").find("input[ name='sort']").val(code);
	$("#pwdModal").modal();
}
function resetTestSeveral(code){
	$.ajax({
		data:{code:code,},
		url : "${hdpath}/pcequipment/resetTestSeveral",
		type : "POST",
		cache : false,
		success : function(e){
			if(e=="succeed"){//通过
				alert("重置完成！");
				location.reload();
			}else if(e==1){//不通过
				alert("重置失败！");
			}
		},
	}); 
}

function changesql(code,hardversion){
	$("#pwdModalLabel").text("请输入密码");
	$("#pwdModal").find("input[ name='sort']").val("0");
	$("#myModal").find("input[ name='code']").val(code);
	var hardversion = $("#myModal").find("select[ name='hardversion']").val(hardversion);
	//$("#hardversion").val(hardversion);
	$("#pwdModal").modal();
}
$("#btn_submitped").click(function () {
	var sort = $("#pwdModal").find("input[ name='sort']").val();
	var password = $("#pwdModal").find("input[ name='password']").val();
	$.ajax({
		data:{password:password,},
		url : "${hdpath}/pcequipment/pwdjudge",
		type : "POST",
		cache : false,
		success : function(e){
			if(e==0){//通过
				if(sort==0){
					$("#myModalLabel").text("修改硬件版本");
					$('#myModal').modal();
				}else{
					resetTestSeveral(sort);
				}
			}else if(e==1){//不通过
				alert("密码错误")
				$("#pwdModal").modal();
			}
		},
	}); 
});

$("#btn_submit").click(function () {
	var code = $("#myModal").find("input[ name='code']").val();
	var hardversion = $("#myModal").find("select[ name='hardversion']").val();
	//var hardversion = $("#hardversion").val();
	$.ajax({
		data:{code:code,hardversion:hardversion,},
		url : "${hdpath}/pcequipment/hardware",
		type : "POST",
		cache : false,
		success : function(e){
			location.reload();
		},
	});  
});
</script>
<script type="text/javascript">
$(function() {
	$("button[id^='create']").click(function() {
		var domain = window.location.host;
		var code = $(this).attr("data-wolfdata");
		$("#wolfQrcode" + code).html("");
		$("#wolfQrcode" + code).qrcode({ 
		    render: "canvas", //table方式 
		    width: 245, //宽度 
		    height:245, //高度 
		    text: "http://" + domain + "/oauth2pay?code=" + code //任意内容 
		});
	})
})
function currentPagenum(mark){//指定页
	var arguments = $("#listform").serialize()+"&currentPage="+mark;
	window.location.href="${pageContext.request.contextPath}/pcequipment/selectEquList?"+arguments; 
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
	window.location.href="${pageContext.request.contextPath}/pcequipment/selectEquList?"+arguments; 
}
</script>
</html>