<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"/>
    <title>模拟投币模板管理</title>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
    <link rel="stylesheet" href="${hdpath}/css/base.css"/>
    <link rel="stylesheet" href="${hdpath}/mui/css/mui.min.css"/>
    <link rel="stylesheet" href="${hdpath}/hdfile/css/tem.css"/>
    <%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
    <script src="${hdpath}/js/jquery.js"></script>
    <script src="${hdpath}/mui/js/mui.min.js"></script>
    <script src="${hdpath}/hdfile/js/tem.js"></script>
</head>
<body class="tem3" data-arecode="${arecode}"  data-source="${source}">
    <div class="tem">
    	<c:forEach items="${templatelist}" var="tempparent">
    	<c:set var="isSysTem" value="${tempparent.merchantid==0 ? 'disabled' : null}" />
    	<%-- <c:if test="${tempparent.merchantid==0}">
        <div class="list-div">
            <li>
                <div class="title">
                    <p>模板名称：<span>${tempparent.name}</span></p>
                </div>
                <ul class="mui-table-view">
                	<c:forEach items="${tempparent.gather}" var="tempson">
                    <li class="mui-table-view-cell">
                        <p>显示名称：<span>${tempson.name}</span></p>
                        <p>投币个数：<span><fmt:formatNumber value="${tempson.remark}" pattern="0" /><b>个</b></span></p>
                        <p>付款金额：<span>${tempson.money}<b>元</b></span></p>
                    </li>
                    </c:forEach>
                    <li class="mui-table-view-cell">
                        <ol class="ol-red">注:</ol>
                        <ol><span>投币个数：</span> 指投币的数量（单位为元）</ol>
                        <ol><span>付款金额：</span> 该金额为用户实际支付的金额（即商户收到的金额）</ol>
                    </li>
                </ul>
            </li>
        </div>
        </c:if> --%>
        <c:if test="${tempparent.pitchon == 1 }">
        <div class="list-div borShadow">
        	<c:if test="${tempparent.merchantid==0}">
        		<h3 class="sysTemTitle">此模板为系统模板（不能修改）</h3>
        	</c:if>
            <li>
                <div class="title">
                    <p>模板名称：<span>${tempparent.name}</span></p>
                    <p>品牌名称：<span>${tempparent.remark}</span></p>
                    <p>售后电话：<span>${tempparent.common1}</span></p>
                    <p>是否钱包强制支付：<c:if test="${tempparent.walletpay==1}"><span class="span-green">是</span></c:if>
                    				<c:if test="${tempparent.walletpay==2}"><span class="span-red">否</span></c:if>
                    </p>
                    <%-- <p>是否支持退费：<c:if test="${tempparent.permit==1}"><span class="span-green">是</span></c:if>
                    				<c:if test="${tempparent.permit==2}"><span class="span-red">否</span></c:if></p> --%>
                    <div>
                        <button type="button" class="mui-btn mui-btn-success tem-title-edit" data-id="${tempparent.id}" ${isSysTem}>编辑</button>
                        <button type="button" class="mui-btn mui-btn-success tem-title-delete" data-id="${tempparent.id}" ${isSysTem}>删除</button>
                    </div>
                </div>
                <ul class="mui-table-view">
                	<c:forEach items="${tempparent.gather}" var="tempson">
                    <li class="mui-table-view-cell">
                        <p>显示名称：<span>${tempson.name}</span></p>
                        <p>投币个数：<span><fmt:formatNumber value="${tempson.remark}" pattern="0" /><b>个</b></span></p>
                        <p>付款金额：<span>${tempson.money}<b>元</b></span></p>
                        <div>
                            <button type="button" class="mui-btn mui-btn-success tem-edit" data-id="${tempson.id}" ${isSysTem}>编辑</button>
                            <button type="button" class="mui-btn mui-btn-success tem-delete" data-id="${tempson.id}" ${isSysTem}>删除</button>
                        </div>
                    </li>
                    </c:forEach>
                    <li class="mui-table-view-cell bottom">
                    	<c:if test="${source!=0}">
                    	<div class="isChecked">
                            <button type="button" class="mui-btn mui-btn-success selectTem active" data-id="${tempparent.id}">选择模板</button>
                        </div>
                        <p>选中模板</p>
                        </c:if>
                        <c:if test="${source==0}">
                    	<div class="isChecked">
                            <button type="button" class="mui-btn mui-btn-success defaultTem active" data-id="${tempparent.id}">设为默认</button>
                        </div>
                        <p>默认模板</p>
                        </c:if>
                        <button type="button" class="mui-btn mui-btn-success mui-btn-outlined addBut" data-id="${tempparent.id}" ${isSysTem}>添加</button>
                    </li>
                </ul>
            </li>
        </div>
        </c:if>
        <c:if test="${tempparent.pitchon != 1 }">
        <div class="list-div">
        	<c:if test="${tempparent.merchantid==0}">
        		<h3 class="sysTemTitle">此模板为系统模板（不能修改）</h3>
        	</c:if>
            <li>
                <div class="title">
                    <p>模板名称：<span>${tempparent.name}</span></p>
                    <p>品牌名称：<span>${tempparent.remark}</span></p>
                    <p>售后电话：<span>${tempparent.common1}</span></p>
                    <p>是否钱包强制支付：<c:if test="${tempparent.walletpay==1}"><span class="span-green">是</span></c:if>
                    				<c:if test="${tempparent.walletpay==2}"><span class="span-red">否</span></c:if>
                    </p>
                    <%-- <p>是否支持退费：<c:if test="${tempparent.permit==1}"><span class="span-green">是</span></c:if>
                    				<c:if test="${tempparent.permit==2}"><span class="span-red">否</span></c:if></p>
                    <p>是否钱包强制支付：<c:if test="${tempparent.walletpay==1}"><span class="span-green">是</span></c:if>
                    				<c:if test="${tempparent.walletpay==2}"><span class="span-red">否</span></c:if> --%>
                    </p>
                    <div>
                        <button type="button" class="mui-btn mui-btn-success tem-title-edit" data-id="${tempparent.id}" ${isSysTem}>编辑</button>
                        <button type="button" class="mui-btn mui-btn-success tem-title-delete" data-id="${tempparent.id}" ${isSysTem}>删除</button>
                    </div>
                </div>
                <ul class="mui-table-view">
                	<c:forEach items="${tempparent.gather}" var="tempson">
                    <li class="mui-table-view-cell">
                        <p>显示名称：<span>${tempson.name}</span></p>
                        <p>投币个数：<span><fmt:formatNumber value="${tempson.remark}" pattern="0" /><b>个</b></span></p>
                        <p>付款金额：<span>${tempson.money}<b>元</b></span></p>
                        <div>
                            <button type="button" class="mui-btn mui-btn-success tem-edit" data-id="${tempson.id}" ${isSysTem}>编辑</button>
                            <button type="button" class="mui-btn mui-btn-success tem-delete" data-id="${tempson.id}" ${isSysTem}>删除</button>
                        </div>
                    </li>
                    </c:forEach>
                    <li class="mui-table-view-cell bottom">
                    	<c:if test="${source!=0}">
                    	<div class="isChecked">
                            <button type="button" class="mui-btn mui-btn-success selectTem" data-id="${tempparent.id}" >选择模板</button>
                        </div>
                        <p>选中模板</p>
                        </c:if>
                        <c:if test="${source==0}">
                    	<div class="isChecked">
                            <button type="button" class="mui-btn mui-btn-success defaultTem" data-id="${tempparent.id}">设为默认</button>
                        </div>
                        <p>默认模板</p>
                        </c:if>
                        <button type="button" class="mui-btn mui-btn-success mui-btn-outlined addBut" data-id="${tempparent.id}" ${isSysTem}>添加</button>
                    </li>
                </ul>
            </li>
        </div>
        </c:if>
		</c:forEach>
        <nav class="mui-bar mui-bar-tab">
            <div>
                <a href="${hdpath}/merchant/index"><button type="button" class="mui-btn mui-btn-success">首页</button></a>
                <button type="button" class="mui-btn mui-btn-success addTemplate">添加模板</button>
            </div>
        </nav>
    </div>
    <div class="tem-mask2">
        <div class="list-center2">
            <span class="mui-icon mui-icon-closeempty close"></span>
            <h3>新增投币模板</h3>
            <form action="#">
                <div class="inp">
                    <label for="temNmae">模板名称</label>
                    <br/>
                    <input type="text" id="temNmae" name="temNmae" placeholder="请填写模板名称"/>
                </div>
                <div class="inp">
                    <label for="brandName">品牌名称</label>
                    <br/>
                    <input type="text" id="brandName" name="brandName" placeholder="请填写品牌名称"/>
                </div>
                <div class="inp">
                    <label for="telephone">售后电话</label>
                    <br/>
                    <input type="text" id="telephone" name="telephone" placeholder="请填写售后电话"/>
                </div>
              <!--   <div class="inp radio-inp">
                    <h5>是否支持退费</h5>
                    <br/>
                    <label>
                        <input type="radio" name="isRef" value="1" />是
                    </label>
                    <label>
                        <input type="radio" name="isRef" value="2" />否
                    </label>
                </div>
                -->
                <div class="inp radio-inp">
                    <h5>是否钱包强制支付</h5>
                    <br/>
                    <label>
                        <input type="radio" name="isWalletPay" value="1" />是
                    </label>
                    <label>
                        <input type="radio" name="isWalletPay" value="2" />否
                    </label>
                </div> 
            </form>
            <div class="bottom">
                <button type="button" class="mui-btn mui-btn-success close2">关闭</button>
                <button type="button" class="mui-btn mui-btn-success submit">提交</button>
            </div>
        </div>
    </div>
    <div class="tem-mask1">
        <div class="list-center1">
            <span class="mui-icon mui-icon-closeempty close"></span>
            <h3>修改投币子模板</h3>
            <form action="#">
                <div class="inp">
                    <label for="name2">显示名称</label>
                    <br/>
                    <input type="text" id="name2" name="name" placeholder="请填写显示名称"/>
                </div>
                <div class="inp">
                    <label for="coinNum">投币个数</label>
                    <br/>
                    <input type="text" id="coinNum" name="coinNum" placeholder="请填写投币个数"/>
                    <div class="btn">个</div>
                </div>
                <div class="inp">
                    <label for="totalParse">付款金额</label>
                    <br/>
                    <input type="text" id="totalParse" name="totalParse" placeholder="请填写付款金额"/>
                    <div class="btn">元</div>
                </div>
                <div class="inp ol">
                    <ol class="ol-red">注:</ol>
                    <ol><span>投币个数：</span> 指投币的数量（单位为元）</ol>
                    <ol><span>付款金额：</span> 该金额为用户实际支付的金额（即商户收到的金额）</ol>
                </div>

            </form>
            <div class="bottom">
                <button type="button" class="mui-btn mui-btn-success close2">关闭</button>
                <button type="button" class="mui-btn mui-btn-success submit">提交</button>
            </div>
        </div>
    </div>

</body>
</html>