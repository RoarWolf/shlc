	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
	<link rel="stylesheet" href="${hdpath}/hdfile/css/temsys.css">
	<script src="${hdpath}/js/bootstrap.min.js"></script>
	<script src="${hdpath}/js/jquery.js"></script>
	<script src="${hdpath}/hdfile/js/toastr.min.js"></script>
	<script src="${hdpath}/hdfile/js/temsys.js"></script>
</head>
<body class="bodyEle" >
<div class="header bg-main">
	<%@ include file="/WEB-INF/views/navigation.jsp"%>
</div>
<div class="leftnav" id="lefeMenu">
	<%@ include file="/WEB-INF/views/menu.jsp"%>
</div>
<ul class="bread">
  <li><a href="javascript:void(0)" target="right" class="icon-home">系统模板查看</a></li>
</ul>
	<div class="admin">
		<div class="defaultTemDiv tem1">
				<h3>十路智慧款系统模板</h3>
				<div class="temList">
						<table class="table  faTem">
								<thead>
									<tr class="title">
										<td class="left_td">
											<span><b>模板名称:</b> <span class="temNmae">充电系统默认模板</span></span>
											<span><b>品牌名称:</b> <span class="brandName">和动</span></span>
											<span><b>售后电话:</b> <span class="telephone"></span></span>
										</td>							
										<!-- <td class="setWidth2">操作</td> -->
									</tr>
								</thead>
								<tbody>
									<tr>
										<td class="left_td">		
											<span>
												<b>是否支持退费:</b>
												<span class="isRef span-green">是</span> 
				                    				<span data-val="1">(退费标准：时间和电量最小)</span>
				                    			</span>
											<span>
												<b>是否钱包强制支付:</b>
												<span class="isWalletPay span-red">否</span>
												<span></span>
											</span>
										</td>
										<!-- <td class="setWidth2">
											<button type="button" class="btn btn-info tem-title-edit" data-id="" >编辑</button>
										</td> -->
									</tr>
								</tbody>
						</table>
						<table class="table table-bordered chTem">
							<thead>
								<tr class="title">
									<td>显示名称</td>
									<td>充电价格</td>
									<td>充电时间</td>
									<td>消耗电量</td>
									<td colspan="2">操作</td>
								</tr>
							</thead>
							<tbody>
								<!--<div>${fn:length(chargesontem)}</div>  -->
								<c:forEach items="${chargesontem}" var="tempson" >
								 <tr>
									<td>${tempson.name}</td>
									<td><span>${tempson.money}</span>元</td>
									<td><span>${tempson.chargeTime}</span>分钟</td>
									<td><span>${tempson.chargeQuantity/100}</span>度</td>
									<td class='setWidth'>
										<button type="button" class="btn btn-info tem-edit" data-id="${tempson.id}">编辑</button>
									</td>
									<td class='setWidth'>
										<button type="button" class="btn btn-danger tem-delete" data-id="${tempson.id}" <c:if test="${fn:length(chargesontem) <= 3}">disabled</c:if>>删除</button>
									</td>
								</tr>
							 	</c:forEach>
								<tr>
									<td colspan="6" class="lastTd">
										<button type="button" class="btn btn-info addBut" data-id="0" >添加模板</button>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					<!-- ====== -->
				<h3 style="margin-top:50px;">电轿款系统模板</h3>
					<div class="temList">
						<table class="table  faTem">
								<thead>
									<tr class="title">
										<td class="left_td">
											<span><b>模板名称:</b> <span class="temNmae">${temelectriccar.name}</span></span>
											<span><b>品牌名称:</b> <span class="brandName">${temelectriccar.remark}</span></span>
											<span><b>售后电话:</b> <span class="telephone">${temelectriccar.common1}</span></span>
										</td>							
										<td class="setWidth2">操作</td>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td class="left_td">
				                    	<span>
											<b>是否支持退费:</b> 
											<c:if test="${temelectriccar.permit==1}"><span class="isRef span-green">是</span> 
			                    				<c:if test="${temelectriccar.common2==1}"><span data-val="1">(退费标准：时间和电量最小)</span></c:if>
			                    				<c:if test="${temelectriccar.common2==2}"><span data-val="2">(退费标准：根据时间)</span></c:if>
			                    				<c:if test="${temelectriccar.common2==3}"><span data-val="3">(退费标准：根据电量)</span></c:if>
			                    			</c:if> 
			                    			<c:if test="${temelectriccar.permit==2}"><span class="isRef span-red">否</span><span data-val=''></span></c:if>
										</span>
											<span>
												<b>是否钱包强制支付:</b>
												<c:if test="${temelectriccar.walletpay==1}"><span class="isWalletPay span-green">是</span></c:if>
                    							<c:if test="${temelectriccar.walletpay==2}"><span class="isWalletPay span-red">否</span></c:if>
										</span>
										</td>
										<td class="setWidth2">
											<button type="button" class="btn btn-info tem-title-edit" data-id="${temelectriccar.id}" >编辑</button>
										</td>
									</tr>
								</tbody>
						</table>
						<table class="table table-bordered chTem">
							<thead>
								<tr class="title">
									<td>显示名称</td>
									<td>充电价格</td>
									<td>充电时间</td>
									<td>消耗电量</td>
									<td colspan="2">操作</td>
								</tr>
							</thead>
							<tbody>
							  <c:forEach items="${temelectriccar.gather}" var="tempson">
							  <tr>
								<td>${tempson.name}</td>
								<td><span>${tempson.money}</span>元</td>
								<td><span>${tempson.chargeTime}</span>分钟</td>
								<td><span>${tempson.chargeQuantity/100}</span>度</td>
								<td class='setWidth'>
									<button type="button" class="btn btn-info tem-edit" data-id="${tempson.id}">编辑</button>
								</td>
								<td class='setWidth'>
									<button type="button" class="btn btn-danger tem-delete" data-id="${tempson.id}" <c:if test="${fn:length(temelectriccar.gather) <= 3}">disabled</c:if>>删除</button>
								</td>
							 </tr>
						 	 </c:forEach>
								<tr>
									<td colspan="6" class="lastTd">
										<button type="button" class="btn btn-info addBut" data-id="${temelectriccar.id}" >添加模板</button>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
			</div>
			
			<!-- 离线模板开始 -->
			<div class="defaultTemDiv tem2">
				<h3>离线卡系统模板</h3>
				<div class="temList">
						<table class="table  faTem">
								<thead>
									<tr class="title">
										<td class="left_td">
											<span><b>模板名称:</b> <span class="temNmae">${temoffline.name}</span></span>
											<span><b>品牌名称:</b> <span class="brandName">${temoffline.remark}</span></span>
										</td>
										<td class="setWidth2">操作</td>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td class="left_td">		
											<span><b>售后电话：</b> <span class="telephone">${temoffline.common1}</span></span>	
										</td>
										<td class="setWidth2">
											<button type="button" class="btn btn-info tem-title-edit" data-id="${temoffline.id}" >编辑</button>
										</td>
										
									</tr>
								</tbody>
							
						</table>
						<table class="table table-bordered chTem">
							<thead>
								<tr class="title">
									<td>显示名称</td>
									<td>付款金额</td>
									<td>充卡金额</td>
									<td colspan="2">操作</td>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${temoffline.gather}" var="tempson">                              
                                    <tr>
                                        <td>${tempson.name}</td>
                                        <td><span>${tempson.money}</span>元</td>
                                        <td><span>${tempson.remark}</span>元</td>
                                        <td class="setWidth">
                                            <button type="button" class="btn btn-info tem-edit" data-id="${tempson.id}">编辑</button>
                                        </td>
                                        <td class="setWidth">
                                            <button type="button" class="btn btn-danger tem-delete" data-id="${tempson.id}" <c:if test="${fn:length(temoffline.gather) <= 3}">disabled</c:if> >删除</button>
                                        </td>
                                    </tr>                            
                                </c:forEach>
								<tr>
									<td colspan="6" class="lastTd">
										<button type="button" class="btn btn-info addBut" data-id="${temoffline.id}" >添加模板</button>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
			</div>
			<!-- 离线模板结束 -->

			<!-- 模拟投币开始 -->
				<div class="defaultTemDiv tem3">
				<h3>模拟投币系统模板</h3>
				<div class="temList">
						<table class="table  faTem">
								<thead>
									<tr class="title">
										<td class="left_td">
											<span><b>模板名称:</b> <span class="temNmae">${temincoins.name}</span></span>
											<span><b>品牌名称:</b> <span class="brandName">${temincoins.remark}</span></span>
										</td>
										<td class="setWidth2">操作</td>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td class="left_td">		
											<span><b>售后电话：</b> <span class="telephone">${temincoins.common1}</span></span>	
										</td>
										<td class="setWidth2" >
											<button type="button" class="btn btn-info tem-title-edit" data-id="${temincoins.id}">编辑</button>
											 
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
								<c:forEach items="${temincoins.gather}" var="tempson">                              
	                                <tr>
	                                    <td>${tempson.name}</td>
	                                    <td><span><fmt:formatNumber value="${tempson.remark}" pattern="0" /></span>个</td>
	                                    <td><span>${tempson.money}</span>元</td>
	                                    <td class="setWidth">
	                                        <button type="button" class="btn btn-info tem-edit" data-id="${tempson.id}">编辑</button>
	                                    </td>
	                                    <td class="setWidth">
	                                        <button type="button" class="btn btn-danger tem-delete" data-id="${tempson.id}" <c:if test="${fn:length(temincoins.gather) <= 3}">disabled</c:if> >删除</button>
	                                    </td>
	                                </tr>                            
	                            </c:forEach> 
								<tr>
									<td colspan="6" class="lastTd">
										<button type="button" class="btn btn-info addBut" data-id="${temincoins.id}" >添加模板</button>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
			</div>

			<!-- 模拟投币结束 -->

			<!-- 钱包模板开始 -->
			<div class="defaultTemDiv tem4">
				<h3>钱包系统模板</h3>
				<div class="temList">
						<table class="table  faTem">
								<thead>
									<tr class="title">
										<td class="left_td">
											<span><b>模板名称:</b> <span class="temNmae">${temwallet.name}</span></span>
											<span><b>品牌名称:</b> <span class="brandName">${temwallet.remark}</span></span>
										</td>
										<td class="setWidth2" >操作</td>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td class="left_td">		
											<span><b>售后电话：</b> <span class="telephone">${temwallet.common1}</span></span>	
										</td>
										<td class="setWidth2">
											<button type="button" class="btn btn-info tem-title-edit" data-id="${temwallet.id}" >编辑</button>
										</td>
										
									</tr>
								</tbody>
							
						</table>
						<table class="table table-bordered chTem">
							<thead>
								<tr class="title">
									<td>显示名称</td>
									<td>付款金额</td>
									<td>到账金额</td>
									<td colspan="2">操作</td>
								</tr>
							</thead>
							<tbody>					
								<c:forEach items="${temwallet.gather}" var="tempson">                              
                                    <tr>
                                        <td>${tempson.name}</td>
                                        <td><span>${tempson.money}</span>元</td>
                                        <td><span>${tempson.remark}</span>元</td>
                                        <td class="setWidth">
                                            <button type="button" class="btn btn-info tem-edit" data-id="${tempson.id}">编辑</button>
                                        </td>
                                        <td class="setWidth">
                                            <button type="button" class="btn btn-danger tem-delete" data-id="${tempson.id}" <c:if test="${fn:length(temwallet.gather) <= 3}">disabled</c:if>>删除</button>
                                        </td>
                                    </tr>                            
                                </c:forEach>
								<tr>
									<td colspan="6" class="lastTd">
										<button type="button" class="btn btn-info addBut" data-id="${temwallet.id}" >添加模板</button>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
			</div>
			<!-- 钱包模板结束 -->

			<!-- 在线卡模板开始 -->
							<div class="defaultTemDiv tem5">
				<h3>在线卡系统模板</h3>
				<div class="temList">
						<table class="table  faTem">
								<thead>
									<tr class="title">
										<td class="left_td">
											<span><b>模板名称:</b> <span class="temNmae">${temonline.name}</span></span>
											<span><b>品牌名称:</b> <span class="brandName">${temonline.remark}</span></span>
										</td>
										<td class="setWidth2">操作</td>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td class="left_td">		
											<span><b>售后电话：</b> <span class="telephone">${temonline.common1}</span></span>	
										</td>
										<td class="setWidth2" >
											<button type="button" class="btn btn-info tem-title-edit" data-id="${temonline.id}" >编辑</button>
										</td>
										
									</tr>
								</tbody>
							
						</table>
						<table class="table table-bordered chTem">
							<thead>
								<tr class="title">
									<td>显示名称</td>
									<td>付款金额</td>
									<td>充卡金额</td>
									<td colspan="2">操作</td>
								</tr>
							</thead>
							<tbody>					
								<c:forEach items="${temonline.gather}" var="tempson">                              
                                    <tr>
                                        <td>${tempson.name}</td>
                                        <td><span>${tempson.money}</span>元</td>
                                        <td><span>${tempson.remark}</span>元</td>
                                        <td class="setWidth">
                                            <button type="button" class="btn btn-info tem-edit" data-id="${tempson.id}">编辑</button>
                                        </td>
                                        <td class="setWidth">
                                            <button type="button" class="btn btn-danger tem-delete" data-id="${tempson.id}" <c:if test="${fn:length(temonline.gather) <= 3}">disabled</c:if> >删除</button>
                                        </td>
                                    </tr>                            
                                </c:forEach>
								<tr>
									<td colspan="6" class="lastTd">
										<button type="button" class="btn btn-info addBut" data-id="${temonline.id}" >添加模板</button>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
			</div>
			<!-- 在线卡模板结束 -->
			<!-- redis数据模拟展示 -->
			<style>
				.sqlContent>div{
					float: left;
					margin-right: 30px;
				}
				#redisTab td,
				#mysqlTab td{
					border:  1px solid #ccc;
					line-height: 35px;
					padding: 3px 10px;
					text-align: center;
					color: #666;
				}
			</style>
			<div class="defaultTemDiv tem5">
				<h3 style="color: red;">Redis测试数据</h3>
				<div class="temList">
				请输入设备号：<input id="redis">&nbsp;&nbsp;&nbsp;<button onclick="submitAAA()">提交</button>
				</div>
					
				<div class="sqlContent">
					<div class="left">
						<table id="redisTab">
							<thead>
								<tr>
									<td>设备编号</td>
									<td>端口号</td>
									<td>状态</td>
									<td>端口剩余时间</td>
									<td>端口功率</td>
									<td>端口剩余电量</td>
									<td>端口记录时间</td>
								<tr>
							</thead>
						
						</table>
					</div>
					
					<div class="right">
						<table id="mysqlTab">
							<thead>
								<tr>
									<td>设备编号</td>
									<td>端口号</td>
									<td>状态</td>
									<td>端口剩余时间</td>
									<td>端口功率</td>
									<td>端口剩余电量</td>
									<td>端口记录时间</td>
								<tr>
							</thead>
						
						</table>
					</div>
					
				</div>
				
			</div>
			
		
		
		
		
		</div>


	<div class="tem-mask1 tem1" >
	        <div class="list-center1">
	            <span class="mui-icon mui-icon-closeempty close1"></span>
	            <h3>修改电子模板</h3>
	            <form action="#">
	                <div class="inp">
	                    <label for="name1">显示名称</label>
	                    <br>
	                    <input type="text" id="name1" name="name" placeholder="请填写显示名称">
	                </div>
	                <div class="inp">
	                    <label for="parse1">充电价格</label>
	                    <br>
	                    <input type="text" id="parse1" name="parse" placeholder="请填写充电价格">
	                    <div class="btn">元</div>
	                </div>
	                <div class="inp">
	                    <label for="time1">充电时间</label>
	                    <br>
	                    <input type="text" id="time1" name="time" placeholder="请填写充电时间">
	                    <div class="btn">分钟</div>
	                </div>
	                <div class="inp">
	                    <label for="power1">消耗电量</label>
	                    <br>
	                    <input type="text" id="power1" name="power" placeholder="请填写消耗电量">
	                    <div class="btn">度</div>
	                </div>
	            </form>
	            <div class="bottom">
	                <button type="button" class="btn btn-info close2">关闭</button>
	                <button type="button" class="btn btn-info submit">提交</button>
	            </div>
	        </div>
	    </div>

	    <!-- 修改充电模板 -->

	    <div class="tem-mask2 tem1" >
		    <div class="list-center2">
		            <span class="mui-icon mui-icon-closeempty close"></span>
		            <h3>修改充电模板</h3>
		            <form action="#">
		                <div class="inp">
		                    <label for="temNmae1">模板名称</label>
		                    <br>
		                    <input type="text" id="temNmae1" name="temNmae" placeholder="请填写模板名称">
		                </div>
		                <div class="inp">
		                    <label for="brandName1">品牌名称</label>
		                    <br>
		                    <input type="text" id="brandName1" name="brandName" placeholder="请填写品牌名称">
		                </div>
		                <div class="inp">
		                    <label for="telephone1">售后电话</label>
		                    <br>
		                    <input type="text" id="telephone1" name="telephone" placeholder="请填写售后电话">
		                </div>
		                <div class="inp radio-inp">
		                    <h5>是否支持退费</h5>
		                    <br>
		                    <label>
		                        <input type="radio" name="isRef" value="1" id="isRefInp">是
		                    </label>
		                     <span style="font-size: 12px; line-height: 24px; margin-left: 5px; color: #54C1F0;" id="spanList">(时间)</span>
		                    <label>
		                        <input type="radio" name="isRef" value="0">否
		                    </label>
		                </div>
		                <div class="inp radio-inp">
		                    <h5>是否钱包强制支付</h5>
		                    <br>
		                    <label>
		                        <input type="radio" name="isWalletPay" value="1">是
		                    </label>
		                    <label>
		                        <input type="radio" name="isWalletPay" value="0">否
		                    </label>
		                </div>
		            </form>
		            <div class="bottom">
		                <button type="button" class="btn btn-info close2">关闭</button>
		                <button type="button" class="btn btn-info submit">提交</button>
		            </div>
		            </div>
		        </div>
	     </div>


	<div  class="tem1 popover">
      <ul class="mui-table-view">
        <li class="mui-table-view-cell firstLi">
                   退费标准
        </li>
        <li class="mui-table-view-cell">
            <label>
                 <input type="radio" name="refReg" value="1" id="refReg1" checked="checked">时间和电量最小
            </label>
        </li>
        <li class="mui-table-view-cell">
            <label>
                 <input type="radio" name="refReg" value="2" id="refReg2">根据时间
            </label>
        </li>
        <li class="mui-table-view-cell">
            <label>
                 <input type="radio" name="refReg" value="3" id="refReg3">根据电量
            </label>
        </li>
         <li class="mui-table-view-cell lastButton">
           <button type="button" class="btn btn-info" id="exitBut">关闭</button>
           <button type="button" class="btn btn-info" id="confirmBut">确定</button>
        </li>
      </ul>
    </div>
	<!-- 离线卡模板弹框开始 -->
	<div class="tem-mask1 tem2" >
        <div class="list-center1">
            <span class="mui-icon mui-icon-closeempty close"></span>
            <h3>修改离线子模板</h3>
            <form action="#">
                <div class="inp">
                    <label for="name2">显示名称</label>
                    <br>
                    <input type="text" id="name2" name="name" placeholder="请填写显示名称">
                </div>
                <div class="inp">
                    <label for="parse2">付款金额</label>
                    <br>
                    <input type="text" id="parse2" name="parse" placeholder="请填写付款金额">
                    <div class="btn">元</div>
                </div>
                <div class="inp">
                    <label for="totalParse">充卡金额</label>
                    <br>
                    <input type="text" id="totalParse" name="totalParse" placeholder="请填写充卡金额">
                    <div class="btn">元</div>
                </div>
                <div class="inp">
                    <ol class="ol-red">注:</ol>
                    <ol><span>充卡金额：</span> 该金额为向卡内实际充值的金额（即用户卡中增加的金额）</ol>
                    <ol><span>付款金额：</span> 该金额为用户实际支付的金额（即商户收到的金额）</ol>
                </div>

            </form>
            <div class="bottom">
                <button type="button" class="btn btn-info close2">关闭</button>
                <button type="button" class="btn btn-info  submit">提交</button>
            </div>
        </div>
    </div>
	
	<div class="tem-mask2 tem2" >
        <div class="list-center2">
            <span class="mui-icon mui-icon-closeempty close"></span>
            <h3>修改离线模板</h3>
            <form action="#">
                <div class="inp">
                    <label for="temNmae2">模板名称</label>
                    <br>
                    <input type="text" id="temNmae2" name="temNmae" placeholder="请填写模板名称">
                </div>
                <div class="inp">
                    <label for="brandName2">品牌名称</label>
                    <br>
                    <input type="text" id="brandName2" name="brandName" placeholder="请填写品牌名称">
                </div>
                <div class="inp">
                    <label for="telephone2">售后电话</label>
                    <br>
                    <input type="text" id="telephone2" name="telephone" placeholder="请填写售后电话">
                </div>
            </form>
            <div class="bottom">
                <button type="button" class="btn btn-info  close2">关闭</button>
                <button type="button" class="btn btn-info  submit">提交</button>
            </div>
        </div>
    </div>
	<!-- 离线卡弹框结束 -->
	<!-- tem3 投币模板开始 -->
	
		<div class="tem-mask2 tem3" >
        <div class="list-center2">
            <span class="mui-icon mui-icon-closeempty close"></span>
            <h3>新增投币模板</h3>
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
    
    <div class="tem-mask1 tem3" style="display: none;">
        <div class="list-center1">
            <span class="mui-icon mui-icon-closeempty close"></span>
            <h3>修改投币子模板</h3>
            <form action="#">
                <div class="inp">
                    <label for="name3">显示名称</label>
                    <br>
                    <input type="text" id="name3" name="name" placeholder="请填写显示名称">
                </div>
                <div class="inp">
                    <label for="coinNum3">投币个数</label>
                    <br>
                    <input type="text" id="coinNum3" name="coinNum" placeholder="请填写投币个数">
                    <div class="btn">个</div>
                </div>
                <div class="inp">
                    <label for="totalParse3">付款金额</label>
                    <br>
                    <input type="text" id="totalParse3" name="totalParse" placeholder="请填写付款金额">
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
	<!-- tem3 投币模板结束 -->

	<!-- tem4钱包模板开始 -->
	<div class="tem-mask2 tem4">
        <div class="list-center2">
            <span class="mui-icon mui-icon-closeempty close"></span>
            <h3>修改钱包主模板</h3>
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
    <div class="tem-mask1 tem4">
        <div class="list-center1">
            <span class="mui-icon mui-icon-closeempty close"></span>
            <h3>修改钱包子模板</h3>
            <form action="#">
                <div class="inp">
                    <label for="name2">显示名称</label>
                    <br>
                    <input type="text" id="name2" name="name" placeholder="请填写显示名称">
                </div>
                <div class="inp">
                    <label for="parse2">付款金额</label>
                    <br>
                    <input type="text" id="parse2" name="parse" placeholder="请填写付款金额">
                    <div class="btn">元</div>
                </div>
                <div class="inp">
                    <label for="totalParse">到账金额</label>
                    <br>
                    <input type="text" id="totalParse" name="totalParse" placeholder="请填写充卡金额">
                    <div class="btn">元</div>
                </div>
                <div class="inp">
                    <ol class="ol-red">注:</ol>
                    <ol><span>到账金额：</span> 该金额为向卡内实际充值的金额（即用户卡中增加的金额）</ol>
                    <ol><span>付款金额：</span> 该金额为用户实际支付的金额（即商户收到的金额）</ol>
                </div>

            </form>
            <div class="bottom">
               <button type="button" class="btn btn-info close2">关闭</button>
                <button type="button" class="btn btn-info submit">提交</button>
            </div>
        </div>
    </div>
	<!-- tem4钱包模板结束 -->

	<!-- tem5在线卡模板开始 -->
	<div class="tem-mask2 tem5">
        <div class="list-center2">
            <span class="mui-icon mui-icon-closeempty close"></span>
            <h3>修改在线卡主模板</h3>
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
    <div class="tem-mask1 tem5" >
        <div class="list-center1">
            <span class="mui-icon mui-icon-closeempty close"></span>
            <h3>修改在线子模板</h3>
            <form action="#">
                <div class="inp">
                    <label for="name2">显示名称</label>
                    <br>
                    <input type="text" id="name2" name="name" placeholder="请填写显示名称">
                </div>
                <div class="inp">
                    <label for="parse2">付款金额</label>
                    <br>
                    <input type="text" id="parse2" name="parse" placeholder="请填写付款金额">
                    <div class="btn">元</div>
                </div>
                <div class="inp">
                    <label for="totalParse">充卡金额</label>
                    <br>
                    <input type="text" id="totalParse" name="totalParse" placeholder="请填写充卡金额">
                    <div class="btn">元</div>
                </div>
                <div class="inp">
                    <ol class="ol-red">注:</ol>
                    <ol><span>充卡金额：</span> 该金额为向卡内实际充值的金额（即用户卡中增加的金额）</ol>
                    <ol><span>付款金额：</span> 该金额为用户实际支付的金额（即商户收到的金额）</ol>
                </div>

            </form>
            <div class="bottom">
               <button type="button" class="btn btn-info close2">关闭</button>
                <button type="button" class="btn btn-info submit">提交</button>
            </div>
        </div>
	</div>
	<!--
			<div class="temDiv tem1">
			<div class="top">
				<h3>系统模板 <button type="button" class="btn btn-info addTemplate"  data-tem="1" >添加主模板</button></h3>
			</div>
			<div class="tableContent">
		    <c:forEach items="${templatelist}" var="tempparent">
			     <div class="temList">
                    <input type="hidden" class="common2" value="1">
                    <input type="hidden" class="default" value="0">
                        <table class="table  faTem">
                                <thead>
                                    <tr class="title">
                                        <td class="left_td">
                                            <span><b>模板名称:</b> <span class="temNmae">${tempparent.name}</span></span>
                                            <span><b>品牌名称:</b> <span class="brandName"></span></span>
                                            <span><b>售后电话：</b> <span class="telephone"></span></span>
                                        
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
                                    <td>付款金额</td>
                                    <td>充卡金额</td>
                                    <td colspan="2">操作</td>
                                </tr>
                            </thead>
                            <tbody> 
                            <c:forEach items="${tempparent.gather}" var="tempson">                              
                                <tr>
                                    <td>${tempson.name}</td>
                                    <td><span>${tempson.money}</span>元</td>
                                    <td><span>${tempson.remark}</span>元</td>
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
                    </c:forEach>
                  </div>
              </div>	
	  -->
	
    </div>
    <script>
    Date.prototype.Formit= function(fmt){
		var o= {
			'M+': this.getMonth()+1,
			'D+': this.getDate(),
			'h+': this.getHours(),
			'm+': this.getMinutes(),
			's+': this.getSeconds()
		}
		if(typeof fmt == 'undefined'){
			return this.Formit('YYYY-MM-DD hh:mm:ss')
		}
		var reg= /(Y+)/g
		if(reg.test(fmt)){
			fmt= fmt.replace(/(Y+)/g,this.getFullYear().toString().substr(4-RegExp.$1.length))
		}
		for(var key in o){
			var regKey= new RegExp('('+key+')','g')
			if(regKey.test(fmt)){	
				fmt= RegExp.$1.length==2 ?  fmt.replace(regKey,('00'+o[key]).substr(-2)) : fmt.replace(regKey,o[key]+'') 
			}
		}
		
		return fmt	
		
	}
    
    	function submitAAA(){
   		  $.ajax({
   		        type: "POST",
   		        url: "${hdpath}/pay/PortInfo",
   		        data: {
   		          code: $("#redis").val(),
   		        },
   		        dataType: "JSON",
   		        success: function(result) {
   		        	$('#mysqlTab tbody').remove()
   		        	$('#redisTab tbody').remove()
   		        	var redisResult= result.redisMap
   		        	var mysqlResult= result.mysqlMap
	   		        var $tbody1= $('<tbody></todby>')
	  		        for ( var iterable_element in redisResult) {
						var result1= JSON.parse(redisResult[iterable_element])
						result1.portStatus = result1.portStatus == 1 ? '空闲' : result1.portStatus == 2 ? '占用' : result1.portStatus == 3 ? '禁用' : result1.portStatus== 4 ? '故障' : '— —'
							var tr='<tr><td>'+$("#redis").val()+'</td><td>'+result1.port+'</td><td>'+result1.portStatus+'</td><td>'+result1.time+'</td><td>'+result1.power+'</td><td>'+result1.elec+'</td><td>'+result1.updateTime+'</td></tr>'                                                                
						$tbody1.append($(tr))	
					}
   		         	var $tbody2= $('<tbody></todby>')
   		        	for(var i=0 ; i< mysqlResult.length; i++){
   		        		
   		        		var fmtTime= new Date(mysqlResult[i].updateTime).Formit()
   		        		mysqlResult[i].portStatus = mysqlResult[i].portStatus == 1 ? '空闲' : mysqlResult[i].portStatus == 2 ? '占用' : mysqlResult[i].portStatus == 3 ? '禁用' : mysqlResult[i].portStatus== 4 ? '故障' : '— —'
   		        		var tr2='<tr><td>'+$("#redis").val()+'</td><td>'+mysqlResult[i].port+'</td><td>'+mysqlResult[i].portStatus+'</td><td>'+mysqlResult[i].time+'</td><td>'+mysqlResult[i].power+'</td><td>'+mysqlResult[i].elec+'</td><td>'+fmtTime+'</td></tr>'                                                                
						$tbody2.append($(tr2))
   		        	}
   		         	$('#mysqlTab').append($tbody2)
	   		         $('#redisTab').append($tbody1)
   		        }
   		      });
    	}
    </script>
</body>
</html>