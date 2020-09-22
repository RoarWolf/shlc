<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"/> 
<title>商户信息</title>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link rel="stylesheet" href="${hdpath}/css/admin.css">
<script src="${hdpath }/js/my.js"></script>
<script src="${hdpath }/js/md5.js"></script>
<script type="text/javascript" src="${hdpath}/js/calendar.js"></script>
<style type="text/css">
.modal-backdrop {
    position: relative; 
}
.btn-default {
	background: #e3e3e3;
	color: #333;
	padding: 3px 12px;
	border: 1px solid #ccc;
}
.mask {
		    position: fixed;
		    top: 0;
		    left: 0;
		    right: 0;
		    bottom: 0;
		    background-color: rgba(0, 0, 0, .3);
		    z-index: 8;
		    display: none;
		    font-size: 14px;
		}
		.alertDiv {
		    z-index: 9;
		    width: 400px;
		    /*height: 600px;*/
		    background-color: #fff;
		    border-radius: 10px;
		    position: absolute;
		    left: 50%;
		    top: 50%;
		    transform: translate(-50%,-50%);
		    -webkit-transform:translate(-50%,-50%);
		    -o-transform:translate(-50%,-50%);
		    -moz-transform:translate(-50%,-50%);
		    padding: 20px;
		    color: #333;
		}
		.alertDiv .top {
			text-align: center;
			line-height: 30px;
			padding-bottom: 10px;
			position: relative;
		}
		.alertDiv .top::after {
			content: '';
			position: absolute;
			left: -20px;
			right: -20px;
			bottom: 0;
			height: 1px;
			background-color: #ccc;
		}
		.alertDiv .contentUl {
			padding: 0;
		}
		.alertDiv .contentUl  li {
			border-bottom: 1px solid #ccc;
		}
		.alertDiv .contentUl  li div{
			height: 50px;
			line-height: 50px;
		}
		.alertDiv .contentUl  li div.left  {
			width: 60%;
			float: left;
			padding-left: 20px;
			font-weight: 700;
		}
		.alertDiv .contentUl  li div.right {
			overflow: height;
			padding-right: 20px;
			text-align: center;
		}
		.alertDiv .contentUl  li div.right label {
		    display: inline-block;
		    max-width: 100%;
		    margin-right: 10px;
		    font-weight: 400;
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
	  <li><a href="javascript:void(0)" target="right" class="icon-home">商户信息</a></li>
	</ul>
</div>
 <div class="admin">
	 <div class="panel admin-panel" id="adminPanel">
    		<div class="conditionsd">
			  <form method="post" action="${hdpath}/pcadminiStrator/selectDealerUserInfo" id="listform">
			    <div class="searchdiv">
			      <ul class="search" style="padding-left:10px;">
			       	   <li>
			     		昵称&nbsp;&nbsp;
			     		<input type="text" placeholder="输入昵称" name="username" value="${username}" class="frame2"/>
			     	   </li>
			           <li>
			     		姓名&nbsp;&nbsp;
			     		<input type="text" placeholder="输入姓名" name="realname" value="${realname}" class="frame2"/>
			     		</li>
			       		<li>
			     		电话&nbsp;&nbsp;
			     		<input type="text" placeholder="输入电话号" name="phoneNum" value="${phoneNum}" class="frame2"/>
			     		</li>
			     		<li>收益&nbsp;&nbsp;
			     		<select name="order" class="frame1">
							<option value="0" <c:if test="${order == 0}"> selected="selected"</c:if> >从小到大</option>
							<option value="1" <c:if test="${order == 1}"> selected="selected"</c:if> >从大到小</option>
						</select>
			     		</li>
			     		<li>商户类型&nbsp;&nbsp;
			     		<select name="source" class="frame1">
			     			<option value="" <c:if test="${source != 1 && source != 2 }"> selected="selected"</c:if>>未选择</option>
							<option value="1" <c:if test="${source == 1}"> selected="selected"</c:if>>有效商户</option>
							<option value="2" <c:if test="${source == 2}"> selected="selected"</c:if>>无效商户</option>
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
			<table class="table table-hover" >
			    <thead>
				   <tr>
					<th>序号</th>
					<th>昵称</th>
					<th>姓名</th>
					<th>电话</th>
					<th>总收益</th>
					<th>未提现</th>
					<c:if test="${admin.rank==0}">
					<th>欠款金额</th>
					<th>操作</th>
					<th>费率（‰）</th>
					<th>对公费率（‰）</th>
					</c:if>
					<th>设备数量</th>
					<th>在线数量</th>
					<th>银行卡</th>
					<th>注册时间</th>
				  </tr>
				</thead>
				<tbody>
				   <c:forEach items="${adminiStrator}" var="strator"  varStatus="as">
					  <tr id="name${strator.id}" >
						<td >${as.count+current}</td>
						<td>${strator.username}</td>
						<td>${strator.realname !=null ? strator.realname:"— —"}</td>
						<td>${strator.phone_num !=null ? strator.phone_num:"— —"}</td>
						<td><fmt:formatNumber value="${strator.earntotal !=null ? strator.earntotal: 0}" pattern="0.00"/></td>
						<td><a href="/pcadminiStrator/selecearningsdetail?merid=${strator.id}"><fmt:formatNumber value="${strator.earnings !=null ? strator.earnings: 0}" pattern="0.00"/></a></td>
						<c:if test="${admin.rank==0}">
						<td>
							<form action="/pcadminiStrator/selectGeneralUserInfo" method="post">
					     		<input type="hidden" name="murealname" value="${strator.realname !=null ? strator.realname:''}"/>
					     		<input type="hidden" name="rephoneNum" value="${strator.phone_num !=null ? strator.phone_num:''}"/>
					     		<input type="submit" value="查看详情">
							</form>
							<%-- <a href="/pcadminiStrator/selectMeriAdvance?merid=${strator.id}">查看详情</a> --%>
						</td>
						<td><button type="button" class="btn btn-default setSwitchBtn" data-merid="${strator.id}" data-id="">设置</button></td>
						<td>${strator.feerate}&nbsp;‰&nbsp;&nbsp; <span onclick="changefeerate(1,'${strator.username}','${strator.realname}','${strator.id}','${strator.feerate}')" class="glyphicon glyphicon-pencil"></span></td>
						<td>${strator.rate}&nbsp;‰&nbsp;&nbsp; <span onclick="changefeerate(2,'${strator.username}','${strator.realname}','${strator.bankid}','${strator.rate}')" class="glyphicon glyphicon-pencil"></span></td>
						</c:if>
						<td><a href="/pcequipment/selectEquList?phoneNum=${strator.phone_num}">${strator.totalline}</a></td>
						<td><a href="/pcequipment/selectEquList?phoneNum=${strator.phone_num}&state=1">${strator.onlines}</a></td>
						<td><a href="/pcadminiStrator/selectAdminiStrator?id=${strator.id}">查看银行卡</a></td>
						<td><fmt:formatDate value="${strator.create_time}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
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
			   <!-- 弹框 -->
			   <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
			        <div class="modal-dialog" role="document">
			            <div class="modal-content">
			                <div class="modal-header">
			                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			                    <h4 class="modal-title" id="myModalLabel">费率修改</h4>
			                </div>
			                <div class="modal-body">
			 						<input type="hidden" name="id" class="form-control" >
			                    <div class="form-group">
			                        <label for="txt_departmentname">昵称</label>
			                        <input type="text" name="nick" class="form-control" placeholder="昵称" disabled="disabled">
			                    </div>
			                    <div class="form-group">
			                        <label for="txt_departmentlevel">名字</label>
			                        <input type="text" name="realnames" class="form-control" placeholder="名字" disabled="disabled">
			                    </div>
			                    <div class="form-group">
			                        <label for="txt_parentdepartment">费率(‰)</label>
			                        <input type="text" name="upfeerate" class="form-control" placeholder="费率" value="">
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
			                        <input type="password" name="password" class="form-control" >
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
         
       <div class="mask">
		<div class="alertDiv">
			<div class="top">
				设置
			</div>
			<div class="contentDiv">
				<ul class="contentUl">
				<li>
					<div class="left">提现通知</div>
					<div class="right">
						<label>
							开启 <input type="radio" value=1 name="withmess" />
						</label>
						<label>
							关闭 <input type="radio" value=2 name="withmess" />
						</label>
					</div>
				</li>
				<li>
					<div class="left">订单通知</div>
					<div class="right">
						<label>
							开启 <input type="radio" value=1 name="ordermess" />
						</label>
						<label>
							关闭 <input type="radio" value=2 name="ordermess" />
						</label>
					</div>
				</li>
				
				<li>
					<div class="left">设备上下线通知</div>
					<div class="right">
						<label>
							开启 <input type="radio" value=1 name="equipmess" />
						</label>
						<label>
							关闭 <input type="radio" value=2 name="equipmess" />
						</label>
					</div>
				</li>
				<li>
					<div class="left">是否开通脉冲模块自动退费</div>
					<div class="right">
						<label>
							开启 <input type="radio" value=1 name="incoinrefund" />
						</label>
						<label>
							关闭 <input type="radio" value=2 name="incoinrefund" />
						</label>
					</div>
				</li>
				<li>
					<div class="left">是否显示投币收益</div>
					<div class="right">
						<label>
							开启 <input type="radio" value=1 name="showincoins" />
						</label>
						<label>
							关闭 <input type="radio" value=2 name="showincoins" />
						</label>
					</div>
				</li>
				<li style="height:50px; line-height: 50px; text-align: center;">
					<button type="button" class="btn btn-info submitBtn1" style="padding: 6px 20px;">确认</button>	
				</li>
				</ul>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
$(document).ready(function(){	
	$('#21'+' a').addClass('at');
	$('#21').parent().parent().parent().prev().removeClass("collapsed");
	$('#21').parent().parent().parent().prev().attr("aria-expanded",true)
	$('#21').parent().parent().parent().addClass("in");
	$('#21').parent().parent().parent().prev().attr("aria-expanded",true)
	})
</script>
<script type="text/javascript">
var merid
$('.setSwitchBtn').click(function(e){
	merid= $(this).attr('data-merid').trim()
	$.ajax({
		url: '/allowAuthority/selectMessSwitch',
		data: {merid:merid},
		type: 'post',
		success: function(res){
			if(res != null){
				res.withmess= res.withmess ? res.withmess : 2
				res.ordermess= res.ordermess ? res.ordermess : 2
				res.equipmess= res.equipmess ? res.equipmess : 2
				res.incoinrefund= res.incoinrefund ? res.incoinrefund : 1
			    res.showincoins= res.showincoins ? res.showincoins : 1
				console.log($('.contentUl input[name="withmess"]').eq(res.withmess-1))
				$('.contentUl input[name="withmess"]').eq(res.withmess-1).prop('checked',true);
				$('.contentUl input[name="ordermess"]').eq(res.ordermess-1).prop('checked',true);
				$('.contentUl input[name="equipmess"]').eq(res.equipmess-1).prop('checked',true);
				$('.contentUl input[name="incoinrefund"]').eq(res.incoinrefund-1).prop('checked',true);
				$('.contentUl input[name="showincoins"]').eq(res.showincoins-1).prop('checked',true);
				$('.mask').fadeIn()
			}
		},
		error: function(err){
			
		}
	})
})
$('.submitBtn1').click(function(){
	$.ajax({
		url: '/allowAuthority/messSwitch',
		data: {
			merid: merid,
			with: $('.contentUl input[name="withmess"]:checked').val().trim(),
			order: $('.contentUl input[name="ordermess"]:checked').val().trim(),
			equip: $('.contentUl input[name="equipmess"]:checked').val().trim() ,
			incoinrefund: $('.contentUl input[name="incoinrefund"]:checked').val().trim(),
			showincoins: $('.contentUl input[name="showincoins"]:checked').val().trim()
		},
		type: 'POST',
		success: function(e){
			console.log(e)
			if(e.code == 200){
				$('.mask').fadeOut()
			}
		},
		error: function(){}
	})
})
$('.mask').click(function(){
	$('.mask').fadeOut()
})
$('.alertDiv').click(function(e){
	e= e || window.event
	e.stopPropagation()
})
function changefeerate(from,name,realname,id,feerate){
	if(from === 1){
		$("#myModalLabel").text('修改费率')
	}else{
		$("#myModalLabel").text('修改对公费率')
	}
	$("#pwdModalLabel").text("请输入密码");
	$("#pwdModal").find("input[name='password']").val("");
	$("#myModal").find("input[name='nick']").attr('value',name);
	$("#myModal").find("input[name='realnames']").attr('value',realname);
	$("#myModal").find("input[name='upfeerate']").val(feerate);
	$("#myModal").find("input[name='id']").attr('value',id);
	$("#pwdModal").modal();
}

$("#btn_submitped").click(function () {
	var password = $("#pwdModal").find("input[name='password']").val();
	$.ajax({
		data:{password:password,},
		url : "${hdpath}/pcadminiStrator/verification",
		type : "POST",
		cache : false,
		success : function(e){
			if(e==0){//通过
				$("#pwdModal").find("input[name='password']").val("");
				$('#myModal').modal();
			}else if(e==1){//不通过
				alert("密码错误")
				$("#pwdModal").find("input[name='password']").val("");
				$("#pwdModal").modal();
			}
		},
	}); 
});

$("#btn_submit").click(function () {
	if($("#myModalLabel").text()== '修改费率'){
		var id = $("input[name='id']").val();
		var feerate = $("input[name='upfeerate']").val();
		$.ajax({
			data:{id:id,feerate:feerate,},
			url : "${hdpath}/pcadminiStrator/updateStratorfeerate",
			type : "POST",
			cache : false,
			success : function(data){
				location.reload();
			},
		});  
	}else{
		console.log(225)
		var bankid = $("input[name='id']").val();
		var rate = $("input[name='upfeerate']").val();
		$.ajax({
			data:{bankid:bankid,rate:rate,},
			url : "${hdpath}/pcadminiStrator/updateBankRate",
			type : "POST",
			cache : false,
			success : function(data){
				if(data.code==401){
					alert("该商户没有对公账户")
					return;
				}
				location.reload();
			},
		});  
	}
	
	
});

function currentPagenum(mark){//指定页
	var arguments = $("#listform").serialize()+"&currentPage="+mark;
	window.location.href="${pageContext.request.contextPath}/pcadminiStrator/selectDealerUserInfo?"+arguments; 
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
	window.location.href="${pageContext.request.contextPath}/pcadminiStrator/selectDealerUserInfo?"+arguments; 
}
</script>
</html>