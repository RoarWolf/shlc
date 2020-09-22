<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="hdpath" value="${pageContext.request.contextPath }" />
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"/>
<title>设备管理</title>
<link rel="stylesheet" type="text/css" href="${hdpath}/mui/css/mui.min.css">
<link rel="stylesheet" type="text/css" href="${hdpath}/mui/css/mui.picker.css">
<link rel="stylesheet" type="text/css" href="${hdpath}/mui/css/mui.poppicker.css">
<link rel="stylesheet" href="${hdpath}/css/base.css"/>
<link rel="stylesheet" type="text/css" href="${hdpath }/css/iconfont.css" />
<link rel="stylesheet" type="text/css" href="${hdpath}/hdfile/css/equip.css">
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="${hdpath}/mui/js/mui.min.js"></script>
<script type="text/javascript" src="${hdpath}/mui/js/mui.picker.js"></script>
<script type="text/javascript" src="${hdpath}/mui/js/mui.poppicker.js"></script>
<script type="text/javascript" src="${hdpath}/js/jquery.js"></script>
</head>
<body data-showincoins="${showincoins}">
	<div class="dm">
		<input type="hidden" id="wolfparam" value="${wolfparam }"> 
		<input type="hidden" id="uid" value="${uid }">
		<div class="top">
			<header>
				<ul class="clearfix">
					<li class="active">在线设备&nbsp;${onNum}</li>
					<li>离线设备 &nbsp;${offNum}</li>
					<li>全部设备 &nbsp;${allNum}</li>
				</ul>
			</header>
			<!-- <div class="ceil"></div> -->
			<div class="search">
				<ol class="clearfix">
					<li class="deviceName"><span class="name">设备编号</span> <span class="mui-icon mui-icon-arrowright"></span></li>
					<li class="searchInp"><input type="search" name="" placeholder="请输入查询设备"><button type="button" class="mui-btn mui-btn-success searchBut">搜索</button></li>
				</ol>
			</div>
		</div>
		<!-- <div class="ceil"></div> -->
		<main>
			<div class="content clearfix">
			   <ul class="ul1">
				<div class="divLi">
				  <c:forEach items="${onlineList}" var="equ">
                    <li <c:if test="${equ.classify != 1}">class="active"</c:if>>
                        <p>设备编号：<span>${equ.code }</span></p>
                        <p>线上收益：<span><fmt:formatNumber type="number" value="${equ.totalOnlineEarn == null ? 0 : equ.totalOnlineEarn }" maxFractionDigits="2" pattern="0.00" /></span> <b>元</b></span></p>
                        <c:if test="${showincoins != 2}">
                        	<p>投币收益：<span>${equ.totalCoinsEarn == null ? 0 : equ.totalCoinsEarn }<b>元</b></span></p>
                        </c:if>
                        <p class="d-name">设备名称：<span>${equ.remark}</span>
							<c:if test="${equ.classify == 1}"><i class="editName">编辑</i></c:if>
						</p>
                        <p class="d-name">小区名称：<span>${equ.name}</span>
                        	<c:if test="${equ.classify == 1}"><i class="editAreaName">编辑</i></c:if>
                        </p>
                        <c:if test="${equ.hardversion != '03' && equ.hardversion != '04'}">
		                    <c:if test="${equ.state == 1 }">
		                    	<p>端口状态： <span class="span_right_fr5">空闲(${equ.freenum})</span><span class="span_right_use5">使用(${equ.usenum})</span><span class="span_right_fi5">故障(${equ.failnum})</span></p>
		                    </c:if>
                   		 </c:if>
                        <div class="bottomBut">
                            <c:choose>
							 <c:when test="${equ.hardversion != '03' && equ.hardversion != '04' && equ.classify == 1 && equ.device_type != 2}">
								<a href="${hdpath}/merchant/charge?code=${equ.code}" class="statisticBut">状态</a>
							 </c:when>
							 <c:otherwise>
								<a class="statisticBut disable">状态</a>
							 </c:otherwise>
						   </c:choose>
						   <c:if test="${equ.classify == 1}">
                           	  <a href="${hdpath }/equipment/equinfo?code=${equ.code}" class="manageBut">管理</a>
                           </c:if>
                           <!-- 判断蓝牙设备不显示 远程、信号 -->
                           <c:if test="${equ.device_type != 2}"> 
	                           <c:choose>
								  <c:when test="${equ.state == 1 }">
								  	<c:if test="${equ.classify == 1 && equ.device_type != 2}">
										<a href="${hdpath }/merchant/remotechargechoose?code=${equ.code}" class="remoteBut">远程</a>
									</c:if>
									<a href="javascript:void(0);" class="wifi bgGreen">
									<c:choose>
									 	<c:when test="${equ.csq >= 0 && equ.csq <= 5}">
											<i class="iconfont icon-WIFIxinhao-ji3"></i>
									 	</c:when>
									 	<c:when test="${equ.csq > 5 && equ.csq <= 10}">
											<i class="iconfont icon-WIFIxinhao-ji2"></i>
									 	</c:when>
									 	<c:when test="${equ.csq > 10 && equ.csq <= 20}">
											<i class="iconfont icon-WIFIxinhao-ji1"></i>
									 	</c:when>
										<c:otherwise>
											<i class="iconfont icon-WIFIxinhao-ji"></i>
									 	</c:otherwise>
									 </c:choose>
									</a>
								  </c:when>
								  <c:otherwise>
									<a class="remoteBut disable">远程</a>
									<a href="javascript:void(0);" class="wifi">
									 <c:choose>
									 	<c:when test="${equ.csq >= 0 && equ.csq <= 5}">
											<i class="iconfont icon-WIFIxinhao-ji3"></i>
									 	</c:when>
									 	<c:when test="${equ.csq > 5 && equ.csq <= 10}">
											<i class="iconfont icon-WIFIxinhao-ji2"></i>
									 	</c:when>
									 	<c:when test="${equ.csq > 10 && equ.csq <= 20}">
											<i class="iconfont icon-WIFIxinhao-ji1"></i>
									 	</c:when>
										<c:otherwise>
											<i class="iconfont icon-WIFIxinhao-ji"></i>
									 	</c:otherwise>
									 </c:choose>
									</a>
								  </c:otherwise>
								</c:choose>
							</c:if>
							 <!-- 判断蓝牙设备不显示 远程、信号 结束 -->
                      	   <a href="${hdpath}/equipment/codeDayEarn?code=${equ.code}" >统计</a>
                           <c:if test="${uid!=equ.manid}">
                           <a href="${hdpath }/equipment/codetotrade?souce=1&code=${equ.code}">订单</a>
                           </c:if>
                           <c:if test="${uid==equ.manid}">
                           <a href="${hdpath }/equipment/codetotrade?souce=2&code=${equ.code}">订单</a>
                           </c:if>
                        </div>
                    </li>
				</c:forEach>
                </div>
                <div class="mui-pull-bottom-pocket mui-block mui-visibility" id="showTip">
                    <div class="mui-pull">
                        <div class="mui-pull-loading mui-icon mui-spinner mui-hidden"></div>
                        <div class="mui-pull-caption mui-pull-caption-nomore" >没有更多数据了</div>
                    </div>
                </div>
			 </ul>
			<ul class="ul2">
			 <div class="divLi">
			   <c:forEach items="${offlineList}" var="equ">
                <li <c:if test="${equ.classify != 1}">class="active"</c:if>>
                   <p>设备编号：<span>${equ.code }</span></p>
                   <p>线上收益：<span><fmt:formatNumber type="number" value="${equ.totalOnlineEarn == null ? 0 : equ.totalOnlineEarn }" maxFractionDigits="2" pattern="0.00" /></span> <b>元</b></span></p>
                   <c:if test="${showincoins != 2}">
                   		<p>投币收益：<span>${equ.totalCoinsEarn == null ? 0 : equ.totalCoinsEarn }<b>元</b></span></p>
                   </c:if>
                   <p class="d-name">设备名称：<span>${equ.remark}</span>
                   		<!-- 合伙人不显示 -->
                  		<c:if test="${equ.classify == 1}"><i class="editName">编辑</i></c:if>
                   </p>
                   <p class="d-name">小区名称：<span>${equ.name}</span>
                  	 	<c:if test="${equ.classify == 1}"><i class="editAreaName">编辑</i></c:if>
                   </p>
                   <c:if test="${equ.hardversion != '03' && equ.hardversion != '04'}">
	                    <c:if test="${equ.state == 1 }">
	                    	<p>端口状态： <span class="span_right_fr5">空闲(${equ.freenum})</span><span class="span_right_use5">使用(${equ.usenum})</span><span class="span_right_fi5">故障(${equ.failnum})</span></p>
	                    </c:if>
                   </c:if>
                   <div class="bottomBut">
                           <c:choose>
							 <c:when test="${equ.hardversion != '03' && equ.hardversion != '04' && equ.classify == 1 && equ.device_type != 2}">
								<a href="${hdpath}/merchant/charge?code=${equ.code}" class="statisticBut">状态</a>
							 </c:when>
							 <c:otherwise>
								<a class="statisticBut disable">状态</a>
							 </c:otherwise>
						   </c:choose>
						   <c:if test="${equ.classify == 1}">
						   		<a href="${hdpath }/equipment/equinfo?code=${equ.code}" class="manageBut">管理</a>
						   </c:if>
						   <!-- 判断蓝牙设备不显示 远程、信号 开始 -->
						   <c:if test="${equ.device_type != 2}">
	                           <c:choose>
								  <c:when test="${equ.state == 1 }">
								    <c:if test="${equ.classify == 1}">
										<a href="${hdpath }/merchant/remotechargechoose?code=${equ.code}" class="remoteBut">远程</a>
									</c:if>
									<a href="javascript:void(0);" class="wifi bgGreen">
										<c:if test="${equ.csq >= 0 && equ.csq <= 5}">
										<i class="iconfont icon-WIFIxinhao-ji3"></i>
										</c:if> 
										<c:if test="${equ.csq > 5 && equ.csq <= 10}">
											<i class="iconfont icon-WIFIxinhao-ji2"></i>
										</c:if> 
										<c:if test="${equ.csq > 10 && equ.csq <= 20}">
											<i class="iconfont icon-WIFIxinhao-ji1"></i>
										</c:if> 
										<c:if test="${equ.csq > 20}">
											<i class="iconfont icon-WIFIxinhao-ji"></i>
										</c:if>
									</a>
								  </c:when>
								  <c:otherwise>
									<a class="remoteBut disable">远程</a>
									<a href="javascript:void(0);" class="wifi">
										<c:if test="${equ.csq >= 0 && equ.csq <= 5}">
										<i class="iconfont icon-WIFIxinhao-ji3"></i>
										</c:if> 
										<c:if test="${equ.csq > 5 && equ.csq <= 10}">
											<i class="iconfont icon-WIFIxinhao-ji2"></i>
										</c:if> 
										<c:if test="${equ.csq > 10 && equ.csq <= 20}">
											<i class="iconfont icon-WIFIxinhao-ji1"></i>
										</c:if> 
										<c:if test="${equ.csq > 20}">
											<i class="iconfont icon-WIFIxinhao-ji"></i>
										</c:if>
									</a>
								  </c:otherwise>
								</c:choose>
							</c:if>
							<!-- 判断蓝牙设备不显示 远程、信号 结束 -->
                      	   <a href="${hdpath}/equipment/codeDayEarn?code=${equ.code}" >统计</a>
                           <c:if test="${uid!=equ.manid}">
                           <a href="${hdpath }/equipment/codetotrade?souce=1&code=${equ.code}">订单</a>
                           </c:if>
                           <c:if test="${uid==equ.manid}">
                           <a href="${hdpath }/equipment/codetotrade?souce=2&code=${equ.code}">订单</a>
                           </c:if>
                   </div>
                </li>
                </c:forEach>
             </div>
              <div class="mui-pull-bottom-pocket mui-block mui-visibility" id="showTip">
                    <div class="mui-pull">
                        <div class="mui-pull-loading mui-icon mui-spinner mui-hidden"></div>
                        <div class="mui-pull-caption mui-pull-caption-nomore" >没有更多数据了</div>
                    </div>
                </div>
			</ul>
			<ul class="ul3">
			 <div class="divLi">
			 <c:forEach items="${allList}" var="equ">
                <li <c:if test="${equ.classify != 1}">class="active"</c:if>>
                    <p>设备编号：<span>${equ.code }</span></p>
                    <p>线上收益：<span><fmt:formatNumber type="number" value="${equ.totalOnlineEarn == null ? 0 : equ.totalOnlineEarn }" maxFractionDigits="2" pattern="0.00" /></span> <b>元</b></span></p>
                    <c:if test="${showincoins != 2}">
                    	<p>投币收益：<span>${equ.totalCoinsEarn == null ? 0 : equ.totalCoinsEarn }<b>元</b></span></p>
                    </c:if>
                    <p class="d-name">设备名称：<span>${equ.remark}</span> 
						<c:if test="${equ.classify == 1}"><i class="editName">编辑</i></c:if>
					</p>
                    <p class="d-name">小区名称：<span>${equ.name}</span>
                    	<c:if test="${equ.classify == 1}"><i class="editAreaName">编辑</i></c:if>
                    </p>
                     <c:if test="${equ.hardversion != '03' && equ.hardversion != '04'}">
		                    <c:if test="${equ.state == 1 }">
		                    	<p>端口状态： <span class="span_right_fr5">空闲(${equ.freenum})</span><span class="span_right_use5">使用(${equ.usenum})</span><span class="span_right_fi5">故障(${equ.failnum})</span></p>
		                    </c:if>
                   		 </c:if>
                    <div class="bottomBut">
                        <c:choose>
							 <c:when test="${equ.hardversion != '03' && equ.hardversion != '04' && equ.classify == 1 && equ.device_type != 2}">
								<a href="${hdpath}/merchant/charge?code=${equ.code}" class="statisticBut">状态</a>
							 </c:when>
							 <c:otherwise>
								<a class="statisticBut disable">状态</a>
							 </c:otherwise>
						   </c:choose>
                           <c:if test="${equ.classify == 1}">
						   		<a href="${hdpath }/equipment/equinfo?code=${equ.code}" class="manageBut">管理</a>
						   </c:if>
						   <!-- 判断蓝牙设备不显示 远程、信号 开始 -->
						   <c:if test="${equ.device_type != 2}">
                           <c:choose>
							  <c:when test="${equ.state == 1 }">
								<c:if test="${equ.classify == 1}">
									<a href="${hdpath }/merchant/remotechargechoose?code=${equ.code}" class="remoteBut">远程</a>
								</c:if>
								<a href="javascript:void(0);" class="wifi bgGreen">
	                         	<c:choose>
								 	<c:when test="${equ.csq >= 0 && equ.csq <= 5}">
										<i class="iconfont icon-WIFIxinhao-ji3"></i>
								 	</c:when>
								 	<c:when test="${equ.csq > 5 && equ.csq <= 10}">
										<i class="iconfont icon-WIFIxinhao-ji2"></i>
								 	</c:when>
								 	<c:when test="${equ.csq > 10 && equ.csq <= 20}">
										<i class="iconfont icon-WIFIxinhao-ji1"></i>
								 	</c:when>
									<c:otherwise>
										<i class="iconfont icon-WIFIxinhao-ji"></i>
								 	</c:otherwise>
								 </c:choose>
							</a>
							  </c:when>
							  <c:otherwise>
								<a class="remoteBut disable">远程</a>
								<a href="javascript:void(0);" class="wifi">
	                         	<c:choose>
								 	<c:when test="${equ.csq >= 0 && equ.csq <= 5}">
										<i class="iconfont icon-WIFIxinhao-ji3"></i>
								 	</c:when>
								 	<c:when test="${equ.csq > 5 && equ.csq <= 10}">
										<i class="iconfont icon-WIFIxinhao-ji2"></i>
								 	</c:when>
								 	<c:when test="${equ.csq > 10 && equ.csq <= 20}">
										<i class="iconfont icon-WIFIxinhao-ji1"></i>
								 	</c:when>
									<c:otherwise>
										<i class="iconfont icon-WIFIxinhao-ji"></i>
								 	</c:otherwise>
								 </c:choose>
							     </a>
							  </c:otherwise>
							</c:choose>
							</c:if>
							<!-- 判断蓝牙设备不显示 远程、信号 结束 -->
                      	   <a href="${hdpath}/equipment/codeDayEarn?code=${equ.code}">统计</a>
                           <c:if test="${uid!=equ.manid}">
                           <a href="${hdpath }/equipment/codetotrade?souce=1&code=${equ.code}">订单</a>
                           </c:if>
                           <c:if test="${uid==equ.manid}">
                           <a href="${hdpath }/equipment/codetotrade?souce=2&code=${equ.code}">订单</a>
                           </c:if>
                           
                    </div>
                </li>
                </c:forEach>
                </div>
                 <div class="mui-pull-bottom-pocket mui-block mui-visibility" id="showTip">
                    <div class="mui-pull">
                        <div class="mui-pull-loading mui-icon mui-spinner mui-hidden"></div>
                        <div class="mui-pull-caption mui-pull-caption-nomore" >没有更多数据了</div>
                    </div>
                </div>
			</ul>
			</div>
		</main>
	</div>
	<script>
	$('.content a').click(function(e){
		e= e || window.event
		e.preventDefault()
		var target= e.target || e.srcElement
		var ulObj= $(target).parent().parent().parent().parent()
		console.log()
		var scrollTop= ulObj.scrollTop()
		var urlFrom= 1
		if(ulObj.attr('class').trim() == 'ul2'){
			urlFrom= 2
		}else if(ulObj.attr('class').trim() == 'ul3'){
			urlFrom= 3
		}else{
			urlFrom= 1
		}
		/* sessionStorage.setItem('urlFrom',urlFrom) */
		sessionStorage.setItem('urlFrom',JSON.stringify({urlFrom:urlFrom,scrollTop:scrollTop}))
		setTimeout(function(){
			 location.href= $(target).attr('href')
		},50)
		
	})

	 window.onpageshow= function(type){
		var numStr= sessionStorage.getItem('urlFrom') || "{}" 
		var obj= JSON.parse(numStr)
		var num= parseInt(obj.urlFrom)
		var scrollTop= obj.scrollTop
		$('.dm header li').removeClass('active')
		var ulWidth= $('.dm .content ul').eq(0).width()
		if(num === 2){
			$('.dm header li').eq(1).addClass('active')
			$('.dm .content').css({left:"-"+ulWidth+"px"})
			$('.dm .content .ul2').animate({scrollTop:scrollTop},0)
		}else if(num === 3){
			$('.dm header li').eq(2).addClass('active')
			$('.dm .content').css({left:-ulWidth*2+"px"})
			$('.dm .content .ul3').animate({scrollTop:scrollTop},0)
		}else {
			$('.dm header li').eq(0).addClass('active')
			$('.dm .content').css({left:"0px"})
			$('.dm .content .ul1').animate({scrollTop:scrollTop},0)
		}
	} 
	</script>
	<script type="text/javascript" src="${hdpath}/hdfile/js/equip.js" ></script>
</body>
</html>