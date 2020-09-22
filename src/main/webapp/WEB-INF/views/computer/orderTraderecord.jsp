<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>交易记录</title>
<script type="text/javascript" src="${hdpath}/jedate/jedate.js"></script>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link rel="stylesheet" href="${hdpath}/css/admin.css">
<style>
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
		.mask .alertDiv {
		    z-index: 9;
		    width: 500px;
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
		.mask .alertDiv .top {
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
		.contentDiv {
			padding-top: 20px;
		}
		.contentDiv table tr th,
		.contentDiv table tr td {
			line-height: 30px;
		}
		.contentDiv table tr th {
			background: #E9F5F8;
		}
		.parMonTd {
			text-decoration: underline;
			cursor: pointer;
		}
		.greenTd {
			color: #00CC99;
		}
		.redTd {
			color: #ea0d3f;
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
	  <li><a href="javascript:void(0)" target="right" class="icon-home">交易记录</a></li>
	</ul>
</div>
<div class="admin">
	 <div class="panel admin-panel" id="adminPanel">
    		<div class="conditionsd">
			  <form method="post" action="${hdpath}/pcorder/selectTradeRecord" id="listform">
			    <div class="searchdiv">
			     <ul class="search" style="padding-left:10px;">
 					<li>
		     		订单:&nbsp;&nbsp;
		     		<input type="text" placeholder="请输入订单" name="ordernum" class="frame7" value="${ordernum}"  />
		     		</li>
		            <li>
		     		用户昵称:&nbsp;&nbsp;
		     		<input type="text" placeholder="请输入用户昵称" name="username" value="${username}"  />
		     		</li>
		       		<li>
		     		商户名:&nbsp;&nbsp;
		     		<input type="text" placeholder="请输入商户名" name="dealer" value="${dealer}"  />
		     		</li>
		       		<li>
		     		商户电话:&nbsp;&nbsp;
		     		<input type="text" placeholder="请输入商户电话" name="mobile" value="${mobile}"  />
		     		</li>
		       		<li>
		     		设备号:&nbsp;&nbsp;
		     		<input type="text" placeholder="请输入设备号" name="code" value="${code}"  />
		     		</li>
		       		<li>
		     		订单状态:&nbsp;&nbsp;
		     		<select name="status">
						<option value="0">请选择</option>
						<option value="1" <c:if test="${status == 1}"> selected="selected"</c:if> >正常</option>
						<option value="2" <c:if test="${status == 2}"> selected="selected"</c:if> >退款</option>
					</select>
		     		</li>
		       		<li>
		     		交易来源:&nbsp;&nbsp;
		     		<select name="paysource">
						<option value="0">请选择</option>
						<option value="1" <c:if test="${paysource == 1}"> selected="selected"</c:if> >充电</option>
						<option value="2" <c:if test="${paysource == 2}"> selected="selected"</c:if> >脉冲</option>
						<option value="3" <c:if test="${paysource == 3}"> selected="selected"</c:if> >离线充值机</option>
						<option value="4" <c:if test="${paysource == 4}"> selected="selected"</c:if> >钱包</option>
						<option value="5" <c:if test="${paysource == 5}"> selected="selected"</c:if> >在线卡</option>
						<option value="5" <c:if test="${paysource == 6}"> selected="selected"</c:if> >虚拟充值</option>
					</select>
		     		</li>
		     		<li>
		     		支付方式:&nbsp;&nbsp;
		     		<select name="paytype">
						<option value="0">请选择</option>
						<option value="1" <c:if test="${paytype == 1}"> selected="selected"</c:if> >钱包</option>
						<option value="2" <c:if test="${paytype == 2}"> selected="selected"</c:if> >微信</option>
						<option value="3" <c:if test="${paytype == 3}"> selected="selected"</c:if> >支付宝</option>
						<option value="4" <c:if test="${paytype == 4}"> selected="selected"</c:if> >微信小程序</option>
						<option value="5" <c:if test="${paytype == 5}"> selected="selected"</c:if> >支付宝小程序</option>
					</select>
		     		</li>
		     		<li>起始时间:&nbsp;&nbsp;<input type="text" name="startTime" id="startTime" placeholder="请选择时间" value="${startTime}"
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
  		 <table class="table" style="background:#95ea9b;">
  		 	  <!-- walletmoney、wechatmoney、alipaymoney
		     refwalletmoney、refwechatmoney、refalipaymoney -->
			<tr>
			   <th>收益：</th>
			   <td>${totalMap.incomemoney}</td>
			   <th>收入：</th>
			   <td>${totalMap.totalmoney}</td>
			   <th>支出：</th>
			   <td>${totalMap.reftotalmoney}</td>
			   <th>钱包消费：</th>
			   <td>${totalMap.walletmoney}</td>
			   <th>钱包退费：</th>
			   <td>${totalMap.refwalletmoney}</td>
			</tr>
  		 </table>
		 <div class="table table-div">
			<table class="table table-hover" >
			    <thead>
				   <tr>
					<th>序号</th>
					<th>订单号</th>
					<th>用户名</th>
					<th>商户名</th>
					<th>设备(卡)号</th>
					<th>设备类型</th>
					<th>交易金额</th>
					<th>商户金额</th>
					<th>合伙人金额</th>
					<th>订单状态</th>
					<th>支付方式</th>
					<th>来源</th>
					<th>交易时间</th>
					<th>详情</th>
				  </tr>
				</thead>
				<tbody>
				   <c:forEach items="${traderecord}" var="order"  varStatus="as">
					  <tr id="name${order.id}" >
						<td >${as.count+current}</td>
						<td>
						<c:choose>
				          	<c:when test="${order.paysource == 1}">
				          		<a href="/pcorder/selectChargeRecord?ordernum=${order.ordernum}">${order.ordernum}</a>
					       	</c:when>
				          	<c:when test="${order.paysource == 2}">
				          		<a href="/pcorder/selectIncoinsRecord?ordernum=${order.ordernum}">${order.ordernum}</a>
					       	</c:when>
				          	<c:when test="${order.paysource == 3}">
				          		<a href="/pcorder/selectOfflineRecord?ordernum=${order.ordernum}">${order.ordernum}</a>
					       	</c:when>
				          	<c:when test="${order.paysource == 4}">
				          		<a href="/pcorder/selectWalletRecord?ordernum=${order.ordernum}">${order.ordernum}</a>
					       	</c:when>
				          	<c:when test="${order.paysource == 5}">
				          		<a href="/pccardrecord/selectonlineconsume?ordernum=${order.ordernum}">${order.ordernum}</a>
					       	</c:when>
				          	<c:when test="${order.paysource == 6}">
				          		<a href="/pcorder/selectPackageMonth?ordernum=${order.ordernum}">${order.ordernum}</a>
					       	</c:when>
						</c:choose>
						</td>
						<td>${order.uusername}</td>
						<td>${order.dealer !=null ? order.dealer:"— —"}</td>
						<td>${order.code !=null ? order.code:"— —"}</td>
						<td>
						   <c:if test="${order.paysource == 5}">— —</c:if>
						   <c:if test="${order.paysource != 5}">${order.hardver=='00' ? "十路智慧款" : order.hardver=='01' ? "十路智慧款" : order.hardver=='02' ? "电轿款" : order.hardver=='03' ? "脉冲板子" :  order.hardver=='04' ? "离线充值机" : "— —"}</c:if>
						</td>
						<td>
							
						  <c:if test="${order.status == 1}"><font style="color: #00CC99;">+${order.money !=null ? order.money:"— —"}</font></c:if>
						  <c:if test="${order.status == 2}"><font style="color: #ea0d3f;">-${order.money !=null ? order.money:"— —"}</font></c:if>
						</td>
						<td>
						  <c:if test="${order.status == 1}"><font style="color: #00CC99;">+${order.mermoney !=null ? order.mermoney:"— —"}</font></c:if>
						  <c:if test="${order.status == 2}"><font style="color: #ea0d3f;">-${order.mermoney !=null ? order.mermoney:"— —"}</font></c:if>
						</td>
						<td data-order-id="${order.id}" <c:if test="${order.manmoney == 0}">style="text-decoration: none;"</c:if> class="parMonTd <c:if test="${order.status == 1}">green</c:if><c:if test="${order.status == 2}">red</c:if>" >
						 <!-- 判断金额为0，则没合伙人，显示 -- -->
						  <c:if test="${order.manmoney == 0}"> 
						  	<font>— —</font>
						  </c:if>
						   <c:if test="${order.manmoney != 0}">
							   <c:if test="${order.status == 1}"><font style="color: #00CC99;">+${order.manmoney !=null ? order.manmoney:"— —"}</font></c:if>
							   <c:if test="${order.status == 2}"><font style="color: #ea0d3f;">-${order.manmoney !=null ? order.manmoney:"— —"}</font></c:if>
						  </c:if>
						  
						</td>
						<td>${order.status == 1 ? "正常" : "退款"}</td>
						<td>
						  <c:choose>
							<c:when test="${order.paytype == 1}">钱包</c:when>
							<c:when test="${order.paytype == 2}">微信</c:when>
							<c:when test="${order.paytype == 3}">支付宝</c:when>
							<c:when test="${order.paytype == 4}">微信小程序</c:when>
							<c:when test="${order.paytype == 5}">支付宝小程序</c:when>
							<c:when test="${order.paytype == 6}">虚拟充值</c:when>
							<c:otherwise>— —</c:otherwise>
						  </c:choose>
						</td>
					    <td><font>${order.paysource == 1 ? "充电" : order.paysource == 2 ? "投币" : order.paysource == 3 ? "离线充值" : order.paysource == 4 ? "钱包" : order.paysource == 5 ? "在线卡" : order.paysource == 6 ? "包月卡" : "— —"}</font></td>
						<td><fmt:formatDate value="${order.create_time}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					   <%--  <td><a href="/pcorder/TraderecordDetails?obje=1&paysource=${order.paysource}&ordernum=${order.ordernum}&orderid=${order.id}"><button class="btn btn-success">详情</button></a></td> --%>
					   	 <td><a href="/pcorder/detailsTradeOrderinfo?orderid=${order.id}"><button class="btn btn-success">详情</button></a></td>
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
</div>
<div class="mask">
		<div class="alertDiv">
			<div class="top">
				合伙人金额明细
				<p>订单号： <span class="orderNumSpan">--</span></p>
			</div>
			<div class="contentDiv">
				<table class="table table-striped">
					<thead>
						<tr>
							<th>合伙人昵称</th>
							<th>合伙人姓名</th>
							<th>合伙人电话</th>
							<th>合伙人金额</th>
						</tr>
					</thead>
					<!-- <tbody>
						<tr>
							<td>测试昵称</td>
							<td>合测试姓名</td>
							<td>15633326563</td>
							<td>5.6</td>
						</tr>
						<tr>
							<td>测试昵称</td>
							<td>合测试姓名</td>
							<td>15633326563</td>
							<td>5.6</td>
						</tr>
					</tbody> -->
				</table>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
$(document).ready(function(){	
	$('#31'+' a').addClass('at');
	$('#31').parent().parent().parent().prev().removeClass("collapsed");
	$('#31').parent().parent().parent().prev().children('span').attr('class', 'pull-right glyphicon glyphicon-chevron-toggle glyphicon-minus');
	$('#31').parent().parent().parent().prev().attr("aria-expanded",true)
	$('#31').parent().parent().parent().addClass("in");
	$('#31').parent().parent().parent().prev().attr("aria-expanded",true)
	
	/* 点击合伙人金额，弹出信息 */
	$('.parMonTd').click(function(){
		var moneyStr= $(this).find('font').text()
		var money= Math.abs(moneyStr)
		if(money != 0 && !isNaN(money)){
			var orderid= $(this).attr('data-order-id').trim()
			$.ajax({
				url: '/pcorder/partnerIncomeDetail',
				type: 'post',
				data: {
					orderid: orderid
				},
				dataType: "json",
				success: function(res){
					if(res.body.length> 0){
						if($('.alertDiv table tbody').length> 0){
							$('.alertDiv table tbody').remove()
						}
						$('.orderNumSpan').text(res.ordernum)
						var $tbody= $('<tbody></tbody>')
						 for(var i= 0; i< res.body.length; i++){
							var className= 'greenTd'
							if(res.body[i].partmoney<0){
								className= 'redTd'
							}
							if(res.body[i].nickname == null || res.body[i].nickname == ''){
								res.body[i].nickname= '——'
							}
							if(res.body[i].realname == null || res.body[i].realname == ''){
								res.body[i].realname= '——'
							}
							var $liObj= '<tr><td>'+res.body[i].nickname+'</td><td>'+res.body[i].realname+'</td><td>'+res.body[i].phone+'</td><td class='+className+'>'+res.body[i].partmoney+'</td></tr>';
							$tbody.append($liObj)
						}
						$('.alertDiv table').append($tbody);
						$('.mask').fadeIn()
					}
				},
				error: function(err){
					console.log(err)
				}
			})
		}
	})
	$('.mask .alertDiv').click(function(e){
		e= e || window.event
		e.stopPropagation()
		
	})
	$('.mask').click(function(){
		$('.mask').fadeOut()
	})
})
</script>
<script type="text/javascript">
function currentPagenum(mark){//指定页
	var arguments = $("#listform").serialize()+"&currentPage="+mark;
	window.location.href="${pageContext.request.contextPath}/pcorder/selectTradeRecord?"+arguments; 
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
	window.location.href="${pageContext.request.contextPath}/pcorder/selectTradeRecord?"+arguments; 
}
</script>
</html>
