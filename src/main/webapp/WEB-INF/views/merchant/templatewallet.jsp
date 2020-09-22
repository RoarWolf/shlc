<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"/>
<title>钱包模板管理</title>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" href="${hdpath}/css/base.css"/>
<link rel="stylesheet" href="${hdpath}/mui/css/mui.min.css"/>
<link rel="stylesheet" href="${hdpath}/hdfile/css/tem.css"/>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script src="${hdpath}/js/jquery.js"></script>
<script src="${hdpath}/mui/js/mui.min.js"></script>
<script src="${hdpath}/hdfile/js/tem.js"></script>
</head>
<body class="wallet" data-arecode="${arecode}"  data-source="${source}">
    <div class="tem">
        <c:forEach items="${templatelist}" var="tempparent">
        <c:if test="${tempparent.merchantid==0}">
        <div class="list-div">
            <li>
                <div class="title">
                    <p>模板名称：<span>${tempparent.name}</span></p>
                </div>
                <ul class="mui-table-view">
                    <c:forEach items="${tempparent.gather}" var="tempson">
                    <li class="mui-table-view-cell">
                        <p>显示名称：<span>${tempson.name}</span></p>
                        <p>付款金额：<span>${tempson.money}<b>元</b></span></p>
                        <p>到账金额：<span>${tempson.remark}<b>元</b></span></p>
                    </li>
                    </c:forEach>
                    <li class="mui-table-view-cell">
                        <ol class="ol-red">注:</ol>
                        <ol><span>到账金额：</span> 该金额为向钱包内实际充值的金额（即用户钱包中增加的金额）</ol>
                        <ol><span>付款金额：</span> 该金额为用户实际支付的金额（即商户收到的金额）</ol>
                    </li>
                </ul>
            </li>
        </div>
        </c:if>
        <c:if test="${tempparent.merchantid!=0}">
        <c:if test="${tempparent.pitchon == 1 }">
        <div class="list-div borShadow">
            <li>
                <div class="title">
                    <p>模板名称：<span>${tempparent.name}</span></p>
                    <p>品牌名称：<span>${tempparent.remark}</span></p>
                    <p>售后电话：<span>${tempparent.common1}</span></p>
                    <div>
                        <button type="button" class="mui-btn mui-btn-success tem-title-edit" data-id="${tempparent.id}">编辑</button>
                        <button type="button" class="mui-btn mui-btn-success tem-title-delete" data-id="${tempparent.id}">删除</button>
                    </div>
                </div>
                <ul class="mui-table-view">
                     <c:forEach items="${tempparent.gather}" var="tempson">
                    <li class="mui-table-view-cell">
                        <p>显示名称：<span>${tempson.name}</span></p>
                        <p>付款金额：<span>${tempson.money}<b>元</b></span></p>
                        <p>到账金额：<span>${tempson.remark}<b>元</b></span></p>
                        <div>
                            <button type="button" class="mui-btn mui-btn-success tem-edit" data-id="${tempson.id}">编辑</button>
                            <button type="button" class="mui-btn mui-btn-success tem-delete" data-id="${tempson.id}">删除</button>
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
                        <button type="button" class="mui-btn mui-btn-success mui-btn-outlined addBut" data-id="${tempparent.id}">添加</button>
                    </li>
                </ul>
            </li>
        </div>
        </c:if>
        <c:if test="${tempparent.pitchon != 1 }">
        <div class="list-div">
            <li>
                <div class="title">
                    <p>模板名称：<span>${tempparent.name}</span></p>
                    <p>品牌名称：<span>${tempparent.remark}</span></p>
                    <p>售后电话：<span>${tempparent.common1}</span></p>
                    <div>
                        <button type="button" class="mui-btn mui-btn-success tem-title-edit" data-id="${tempparent.id}">编辑</button>
                        <button type="button" class="mui-btn mui-btn-success tem-title-delete" data-id="${tempparent.id}">删除</button>
                    </div>
                </div>
                <ul class="mui-table-view">
                     <c:forEach items="${tempparent.gather}" var="tempson">
                    <li class="mui-table-view-cell">
                        <p>显示名称：<span>${tempson.name}</span></p>
                        <p>付款金额：<span>${tempson.money}<b>元</b></span></p>
                        <p>到账金额：<span>${tempson.remark}<b>元</b></span></p>
                        <div>
                            <button type="button" class="mui-btn mui-btn-success tem-edit" data-id="${tempson.id}">编辑</button>
                            <button type="button" class="mui-btn mui-btn-success tem-delete" data-id="${tempson.id}">删除</button>
                        </div>
                    </li>
                    </c:forEach>
                    <li class="mui-table-view-cell bottom">
                    	<c:if test="${source!=0}">
                    	<div class="isChecked">
                            <button type="button" class="mui-btn mui-btn-success selectTem" data-id="${tempparent.id}">选择模板</button>
                        </div>
                        <p>选中模板</p>
                        </c:if>
                        <c:if test="${source==0}">
                    	<div class="isChecked">
                            <button type="button" class="mui-btn mui-btn-success defaultTem" data-id="${tempparent.id}">设为默认</button>
                        </div>
                        <p>默认模板</p>
                        </c:if>
                        <button type="button" class="mui-btn mui-btn-success mui-btn-outlined addBut" data-id="${tempparent.id}">添加</button>
                    </li>
                </ul>
            </li>
        </div>
        </c:if>
        </c:if>
        </c:forEach>
        <c:if test="${wolftemp == 1 }">
        	<div align="center" class="list-div">
	        	<a href="/merchant/getMerAllWalletTemp?merid=${user.id }&aid=${aid}">查看所有钱包模板</a>
        	</div>
        </c:if>
        <c:if test="${wolftemp == 2 }">
        	<div align="center" class="list-div">
	        	<a href="/merchant/getMerAllOnlineCardTemp?merid=${user.id }&aid=${aid}">查看所有在线卡模板</a>
        	</div>
        </c:if>

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
            <h3>新增钱包模板</h3>
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
            <h3>新增钱包子模板</h3>
            <form action="#">
                <div class="inp">
                    <label for="name2">显示名称</label>
                    <br/>
                    <input type="text" id="name2" name="name" placeholder="请填写显示名称"/>
                </div>
                <div class="inp">
                    <label for="parse2">付款金额</label>
                    <br/>
                    <input type="text" id="parse2" name="parse" placeholder="请填写付款金额"/>
                    <div class="btn">元</div>
                </div>
                <div class="inp">
                    <label for="totalParse">到账金额</label>
                    <br/>
                    <input type="text" id="totalParse" name="totalParse" placeholder="请填写充卡金额"/>
                    <div class="btn">元</div>
                </div>
                <div class="inp ol">
                    <ol class="ol-red">注:</ol>
                    <ol><span>到账金额：</span> 该金额为向卡内实际充值的金额（即用户卡中增加的金额）</ol>
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