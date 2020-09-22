<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"/> 
<title>用户信息</title>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link rel="stylesheet" href="${hdpath}/css/admin.css">
<script type="text/javascript" src="${hdpath}/js/calendar.js"></script>
<style type="text/css">
.modal-backdrop {
    position: relative; 
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
		    padding-bottom: 40px;
		    /*padding: 20px 40px 40px 40px;*/
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
			width: 40%;
			float: left;
			padding-left: 20px
		}
		.alertDiv .contentUl  li div.right {
			overflow: height;
			padding-right: 20px;
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
	  <li><a href="javascript:void(0)" target="right" class="icon-home">用户信息</a></li>
	</ul>
</div>
 <div class="admin">
	 <div class="panel admin-panel" id="adminPanel">
    		<div class="conditionsd">
			  <form method="post" action="${hdpath}/pcadminiStrator/selectGeneralUserInfo" id="listform">
			    <div class="searchdiv">
			      <ul class="search" style="padding-left:10px;">
			      		<li>
			     		ID&nbsp;&nbsp;
			     		<input type="text" placeholder="输入ID" name="orderID" class="frame1" value="${orderID}"/>
			     		</li>
			     		<li>钱包&nbsp;&nbsp;
			     		<select name="sort" class="frame1">
							<option value="0" <c:if test="${sort == 0}"> selected="selected"</c:if> >从大到小</option>
							<option value="1" <c:if test="${sort == 1}"> selected="selected"</c:if> >从小到大</option>
						</select>
			     		</li>
			       		<li>
			     		昵称&nbsp;&nbsp;
			     		<input type="text" placeholder="输入昵称" name="username" class="frame2" value="${username}"/>
			     		</li>
			           <li>
			     		姓名&nbsp;&nbsp;
			     		<input type="text" placeholder="输入姓名" name="realname" class="frame2" value="${realname}"/>
			     		</li>
			       		<li>
			     		用户电话&nbsp;&nbsp;
			     		<input type="text" placeholder="输入用户电话号" name="phoneNum" class="frame2" value="${phoneNum}"/>
			     		</li>
			     		<li>
			     		商户名&nbsp;&nbsp;
			     		<input type="text" placeholder="输入商户名" name="murealname" class="frame2" value="${murealname}"/>
			     		</li>
			       		<li>
			     		商户电话&nbsp;&nbsp;
			     		<input type="text" placeholder="输入商户电话号" name="rephoneNum" class="frame2" value="${rephoneNum}"/>
			     		</li>
			       		<li>
			     		归属小区&nbsp;&nbsp;
			     		<input type="text" placeholder="输入小区" name="areaname" class="frame2" value="${areaname}"/>
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
					<th>ID</th>
					<th>昵称</th>
					<th>姓名</th>
					<th>电话</th>
					<th>钱包</th>
					<th>在线卡</th>
					<th>是否包月</th>
					<th>所属商户</th>
					<th>商户电话</th>
					<th>归属小区</th>
					<th>解绑</th>
				  </tr>
				</thead>
				<tbody>
				   <c:forEach items="${adminiStrator}" var="strator"  varStatus="as">
					  <tr id="name${strator.id}" >
						<td >${as.count+current}</td>
						<td>${strator.numerical}</td>
						<td>${strator.username}</td>
						<td>${strator.realname !=null ? strator.realname:"— —"}</td>
						<td>${strator.phone_num !=null ? strator.phone_num:"— —"}</td>
						<td><a href="/pcadminiStrator/selecwalletdetail?uid=${strator.id}">${strator.balance !=null ? strator.balance:"0.00"}</a></td>
						<td><a href="/pccardrecord/selectonlinecardbyuid?uid=${strator.id}">查看在线卡</a></td>
					    <td class="IsMonthlyTd" data-id="${strator.montuid}" data-nickname="${strator.username}">
					    	<c:choose>
					    	  <c:when test="${strator.montuid==null}"><button disabled="disabled" style="color: #fff;background-color: #afabab;border-color: #afabab;">否</button></c:when>
					    	  <c:when test="${strator.montuid!=null}"><button style="color: #fff;background-color: #5cb85c; border-color: #4cae4c;">是</button></c:when>
					    	</c:choose>
					    </td>
					    <td>${strator.murealname !=null ? strator.murealname:"— —"}</td>
						<td>
							<c:if test="${strator.muphone_num != null}"><a href="/pcadminiStrator/selectDealerUserInfo?phoneNum=${strator.muphone_num}">${strator.muphone_num}</a></c:if>
							<c:if test="${strator.muphone_num == null}"> — — </c:if>
						</td>
						<td>${strator.arename !=null ? strator.arename:"— —"}</td>
						<td>
							<c:if test="${strator.muphone_num !=nul}"><button onclick="unbind('${strator.id}',1)" class="btn btn-success">解绑商户</button></c:if>
							<c:if test="${strator.muphone_num ==nul}"><button onclick="binding('${strator.id}','${strator.merid}',1)" class="btn btn-info">绑定商户</button></c:if>
							<c:if test="${strator.aid !='0'}"><button onclick="unbind('${strator.id}',2)" class="btn btn-success">解绑小区</button></c:if>
							<c:if test="${strator.aid =='0'}"><button onclick="binding('${strator.id}','${strator.merid}',2)" class="btn btn-info">绑定小区</button></c:if>
						</td>
					   </tr>
					</c:forEach>
					   <c:if test="${walletmoney != null}">
					   		<tr><td colspan="4"></td><th>总计：</th><td><span style="color: #337ab7">${ walletmoney}</span></td><td colspan="4"></td></tr>
					   </c:if>
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
			                    <h4 class="modal-title" id="myModalLabel">密码</h4>
			                </div>
			                <div class="modal-body">
			 						<input type="hidden" name="id" class="form-control" >
			 						<input type="hidden" name="type" class="form-control" >
			 						<input type="hidden" name="sort" class="form-control" >
			 						<input type="hidden" name="pmerid" class="form-control" >
			                    <div class="form-group">
			                        <label for="txt_departmentname">密码</label>
			                        <input type="password" name="password" class="form-control" placeholder="密码">
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
   			</div>
         </div>
         <!-- 弹框开始 -->
         
         	<div class="mask">
		<div class="alertDiv">
			<div class="top">
				用户名：<span id="spanName" ></span>
			</div>
			<div class="contentDiv">
				<ul class="contentUl">
					<li>
						<div class="left">总剩余次数</div>
						<div class="right">56次</div>
					</li>
					<li>
						<div class="left">今日剩余次数</div>
						<div class="right">2次</div>
					</li>
					<li>
						<div class="left">每日指定总次数</div>
						<div class="right">3次</div>
					</li>
					<li>
						<div class="left">每月限制次数</div>
						<div class="right">23次</div>
					</li>
					<li>
						<div class="left">充电时间</div>
						<div class="right">480分钟</div>
					</li>
					<li>
						<div class="left">消耗电量</div>
						<div class="right">2度</div>
					</li>
					<li>
						<div class="left">到期时间</div>
						<div class="right">2019-08-25 12：16：32</div>
					</li>
				</ul>
			</div>
		</div>
	</div>
         
         <!-- 弹框结束 -->
</body>
<script type="text/javascript">
$(document).ready(function(){	
	$('#22'+' a').addClass('at');
	$('#22').parent().parent().parent().prev().removeClass("collapsed");
	$('#22').parent().parent().parent().prev().attr("aria-expanded",true)
	$('#22').parent().parent().parent().addClass("in");
	$('#22').parent().parent().parent().prev().attr("aria-expanded",true)
	})
	
	/* 在这里点击包月为 ”是“，弹出框 */
	$('.IsMonthlyTd').click(function(){
		var flag= $(this).text().trim() == "是" ? true : false
		if(!flag) return
		/* 获取id */
		var id= $(this).attr('data-id').trim()
		var name= $(this).attr('data-nickname').trim()
		$.ajax({
			url: '/pcadminiStrator/selectPackagemonth',
			data: {uid: id},
			type: "POST",
			success: function(res){
				var dt= new Date(res.endTime)
				var y= dt.getFullYear()
				var m= dt.getMonth()+1 >= 10 ? (dt.getMonth()+1) : '0'+(dt.getMonth()+1)
				var d= dt.getDate()	>= 10 ? dt.getDate() : '0'+dt.getDate()
				var h= dt.getHours()	>= 10 ? dt.getHours() : '0'+dt.getHours()
				var mi= dt.getMinutes()	>= 10 ? dt.getMinutes() : '0'+dt.getMinutes()
				var s= dt.getSeconds()	>= 10 ? dt.getSeconds() : '0'+dt.getSeconds()	
				res.endTime= y+'-'+m+'-'+d+' '+h+':'+mi+':'+s
				$('#spanName').text(name)
			var str= '<ul class="contentUl"><li><div class="left">总剩余次数</div><div class="right">'+res.surpnum+'次</div></li><li><div class="left">今日剩余次数</div><div class="right">'+res.todaysurpnum+'次</div></li><li><div class="left">每日指定总次数</div><div class="right">'+res.everydaynum+'次</div></li><li><div class="left">每月限制次数</div><div class="right">'+res.everymonthnum+'次</div></li><li><div class="left">充电时间</div><div class="right">'+res.time+'分钟</div></li><li><div class="left">消耗电量</div><div class="right">'+res.elec+'度</div></li><li><div class="left">到期时间</div><div class="right">'+res.endTime+'</div></li></ul>';
				$('.mask .contentDiv').html(str);
				$('.mask').fadeIn()
			}
		})
	})
	
	$('.mask').click(function(){
		$('.mask').fadeOut()
	})
	$('.alertDiv').click(function(e){
		e= e || window.event
		e.stopPropagation()
	})
</script>
<script type="text/javascript">
function unbind(unbind,type){//id
	$("#myModalLabel").text("修改");
	$("input[ name='id']").attr('value', unbind);
	$("input[ name='type']").attr('value', type);
	$("input[ name='sort']").attr('value', 1);
	$('#myModal').modal();
}
function binding(uid,merid,source){
	if(source==2 && merid==0){
		alert("请先绑定商户。");
		return;
	}
	$("#myModalLabel").text("绑定");
	$("input[ name='id']").attr('value', uid);
	$("input[ name='type']").attr('value', source);
	$("input[ name='sort']").attr('value', 2);
	$("input[ name='pmerid']").attr('value', merid);
	$('#myModal').modal();
}
$("#btn_submit").click(function (){
	var id = $("input[name='id']").val();
	var type = $("input[name='type']").val();
	var sort = $("input[ name='sort']").val();
	var password = $("input[name='password']").val();
	var merid = $("input[ name='pmerid']").val();
	
	$.ajax({
		data:{id:id,password:password,},
		url : "${hdpath}/pcadminiStrator/verification",
		type : "POST",
		cache : false,
		success : function(e){
			if(e==0){
				if(sort==1){
					unbindmercha(id,type);
				}else if(sort==2){
					bindingobj(id,merid,type);
				}
			}else if(e==1){
				alert("密码错误！");
			}
		},
	});  
});  	
function unbindmercha(unbind,type){
    var statu = confirm("是否确认解绑?");
    if(!statu){
        return false;
    }
    var url;
	if(type==1){
		url = "${hdpath}/pcadminiStrator/genunbindmer";
	} else if(type==2){
		url = "${hdpath}/pcadminiStrator/unbindarea";
	}
    $.ajax({
            type:'POST',
            url:url,
            data:{uid:unbind},
            success:function(e){
				$("input[name='password']").val("");
         		window.location.reload();
            },
            error:function(){
                    alert('操作有误!');
            } 
    }); 
}
function bindingobj(uid,merid,source){
    var url;
    var megsse;
	if(source==1){
		megsse = "是否确认绑定商户！";
		url = "${hdpath}/pcadminiStrator/selectbindinginfo?uid="+uid+"&source="+source;
	} else if(source==2){
		megsse = "是否确认绑定小区！";
		url = "${hdpath}/pcadminiStrator/selectbindinginfo?uid="+uid+"&source="+source+"&merid="+merid;
	}
	var statu = confirm(megsse);
    if(!statu){
        return false;
    }
    window.location.href =url;
}

function currentPagenum(mark){//指定页
	var arguments = $("#listform").serialize()+"&currentPage="+mark;
	window.location.href="${pageContext.request.contextPath}/pcadminiStrator/selectGeneralUserInfo?"+arguments; 
}
function currentPage(mark){
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
	window.location.href="${pageContext.request.contextPath}/pcadminiStrator/selectGeneralUserInfo?"+arguments; 
}
</script>
</html>