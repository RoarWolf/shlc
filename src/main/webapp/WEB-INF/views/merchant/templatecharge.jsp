<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"/>
    <title>收费模板管理</title>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <link rel="stylesheet" href="${hdpath}/css/base.css"/>
    <link rel="stylesheet" href="${hdpath}/mui/css/mui.min.css"/>
    <link rel="stylesheet" href="${hdpath}/hdfile/css/tem.css"/>
   <%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
    <script src="${hdpath}/js/jquery.js"></script>
    <script src="${hdpath}/mui/js/mui.min.js"></script>
    <script src="${hdpath}/hdfile/js/tem.js"></script>
</head>
<body class="tem1"  data-arecode="${arecode}"  data-source="${source}">
	<div class="tem">
	
	<c:forEach items="${templatelist}" var="tempparent">
	<c:if test="${tempparent.grade == 2 }">  <!-- 过滤掉分类模板，因为在templatelist中存在分类模板 -->
	<c:if test="${tempparent.pitchon == 1 }">
	        <div class="list-div borShadow">
	    	<input type="hidden" class="common2" value="${tempparent.common2}">
	    	<input type="hidden" class="default" value="${tempparent.common3}">
            <li>
                <div class="title">
                    <p>模板名称：<span>${tempparent.name}</span></p>
                    <p>品牌名称：<span>${tempparent.remark}</span></p>
                    <p>售后电话：<span>${tempparent.common1}</span></p>
                    <p>是否支持退费：<c:if test="${tempparent.permit==1}"><span class="span-green">是</span>
                    				<c:if test="${tempparent.common2==1}"><span data-val="1">(退费标准：时间和电量最小)</span></c:if>
                    				<c:if test="${tempparent.common2==2}"><span data-val="2">(退费标准：根据时间)</span></c:if>
                    				<c:if test="${tempparent.common2==3}"><span data-val="3">(退费标准：根据电量)</span></c:if>
                    			</c:if>
                    			 <c:if test="${tempparent.permit==2}"><span class="span-red">否</span><span></span></c:if>
                   				 
                   </p>
                    <p>是否钱包强制支付：<c:if test="${tempparent.walletpay==1}"><span class="span-green">是</span></c:if>
                    				<c:if test="${tempparent.walletpay==2}"><span class="span-red">否</span></c:if></p>
                    <p class="pTextInfo pTextInfo${tempparent.id}" data-text="${tempparent.chargeInfo}">收费说明： <button type="button" class="mui-btn mui-btn-success tem-title-text" data-id="${tempparent.id}" data-id="${tempparent.id}" ${tempparent.merchantid == 0 ? 'disabled' : ''} >查看</button>	
                   	</p>
                   	<p class="ifalipay">是否支持支付宝充电：<c:if test="${tempparent.ifalipay==1}"><span class="span-green">是</span></c:if>
                    				<c:if test="${tempparent.ifalipay==2}"><span class="span-red">否</span></c:if></p>
                    <div>
                        <button type="button" class="mui-btn mui-btn-success tem-title-edit" data-id="${tempparent.id}" ${tempparent.merchantid == 0 ? 'disabled' : ''}>编辑</button>
                        <button type="button" class="mui-btn mui-btn-success tem-title-delete" data-id="${tempparent.id}" ${tempparent.merchantid == 0 ? 'disabled' : ''}>删除</button>
                    </div>
                </div>
                <ul class="mui-table-view">
                	<c:forEach items="${tempparent.gather}" var="tempson">
                    <li class="mui-table-view-cell">
                        <p>显示名称：<span>${tempson.name}</span></p>
                        <p>充电价格：<span>${tempson.money} <b>元</b></span></p>
                        <p>充电时间：<span>${tempson.chargeTime} <b>分钟</b></span></p>
                        <p>消耗电量：<span>${tempson.chargeQuantity/100} <b>度</b></span></p>
                        <div>
                            <button type="button" class="mui-btn mui-btn-success tem-edit" data-id="${tempson.id}"  ${tempparent.merchantid == 0 ? 'disabled' : ''} >编辑</button>
                            <button type="button" class="mui-btn mui-btn-success tem-delete" data-id="${tempson.id}" ${tempparent.merchantid == 0 ? 'disabled' : ''} >删除</button>
                        </div>
                    </li>
                    </c:forEach>
                    <li class="mui-table-view-cell bottom">
                    	<%-- <c:if test="${source!=0}">
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
                        </c:if> --%>
                        <button type="button" class="mui-btn mui-btn-success mui-btn-outlined addBut" data-id="${tempparent.id}" ${tempparent.merchantid == 0 ? 'disabled' : ''}>添加</button>
                    	
                    </li>
                </ul>          
            </li>
        </div>
        </c:if>
        <c:if test="${tempparent.pitchon != 1 }">
	        <div class="list-div">	
	    	<input type="hidden" class="common2" value="${tempparent.common2}">
	    	<input type="hidden" class="default" value="${tempparent.common3}">
            <li>
                <div class="title">
                    <p>模板名称：<span>${tempparent.name}</span></p>
                    <p>品牌名称：<span>${tempparent.remark}</span></p>
                    <p>售后电话：<span>${tempparent.common1}</span></p>
                    <p>是否支持退费：
                    	<c:if test="${tempparent.permit==1}"><span class="span-green">是</span>
               				<c:if test="${tempparent.common2==1 || tempparent.common2== null}"><span data-val="1">(退费标准：时间和电量最小)</span></c:if>
               				<c:if test="${tempparent.common2==2}"><span data-val="2">(退费标准：根据时间)</span></c:if>
               				<c:if test="${tempparent.common2==3}"><span data-val="3">(退费标准：根据电量)</span></c:if>
                    	</c:if>
                    	<c:if test="${tempparent.permit==2}"><span class="span-red">否</span><span></span></c:if>
                   				 
                   </p>
                    <p>是否钱包强制支付：
                   		<c:if test="${tempparent.walletpay==1}"><span class="span-green">是</span></c:if>
                   		<c:if test="${tempparent.walletpay==2}"><span class="span-red">否</span></c:if>
                   	</p>
                   	<p class="pTextInfo pTextInfo${tempparent.id}" data-text="${tempparent.chargeInfo}">收费说明： <button type="button" class="mui-btn mui-btn-success tem-title-text" data-id="${tempparent.id}" data-id="${tempparent.id}" ${tempparent.merchantid == 0 ? 'disabled' : ''}>查看</button>	
                   	</p>
                   	<p class="ifalipay">是否支持支付宝充电：<c:if test="${tempparent.ifalipay==1}"><span class="span-green">是</span></c:if>
                    				<c:if test="${tempparent.ifalipay==2}"><span class="span-red">否</span></c:if></p>
                    <div>
                        <button type="button" class="mui-btn mui-btn-success tem-title-edit" data-id="${tempparent.id}" ${tempparent.merchantid == 0 ? 'disabled' : ''} >编辑</button>
                        <button type="button" class="mui-btn mui-btn-success tem-title-delete" data-id="${tempparent.id}" ${tempparent.merchantid == 0 ? 'disabled' : ''} >删除</button>
                    </div>
                </div>
                <ul class="mui-table-view">
                	<c:forEach items="${tempparent.gather}" var="tempson">
                    <li class="mui-table-view-cell">
                        <p>显示名称：<span>${tempson.name}</span></p>
                        <p>充电价格：<span>${tempson.money} <b>元</b></span></p>
                        <p>充电时间：<span>${tempson.chargeTime} <b>分钟</b></span></p>
                        <p>消耗电量：<span>${tempson.chargeQuantity/100} <b>度</b></span></p>
                        <div>
                            <button type="button" class="mui-btn mui-btn-success tem-edit" data-id="${tempson.id}"  ${tempparent.merchantid == 0 ? 'disabled' : ''} >编辑</button>
                            <button type="button" class="mui-btn mui-btn-success tem-delete" data-id="${tempson.id}" ${tempparent.merchantid == 0 ? 'disabled' : ''} >删除</button>
                        </div>
                    </li>
                    </c:forEach>
                    <li class="mui-table-view-cell bottom">
                    	<%-- <c:if test="${source!=0}">
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
                        </c:if> --%>
                        <button type="button" class="mui-btn mui-btn-success mui-btn-outlined addBut" data-id="${tempparent.id}" ${tempparent.merchantid == 0 ? 'disabled' : ''} >添加</button>
                    	
                    </li>
                </ul>     
            </li>
        </div>
       </c:if>
       </c:if>
       
   </c:forEach>
   <c:forEach items="${tempgather}" var="temggather">
   			<c:if test="${temggather.rank == 1 }">
   				<c:if test="${temggather.pitchon == 1 }">
   				<div class="list-div gradTem borShadow">
   				</c:if>
   				<c:if test="${temggather.pitchon != 1 }">
   				<div class="list-div gradTem">
   				</c:if>
   			</c:if>
	   		
					<div class="list-div">
						<c:if test="${temggather.rank == 1 }">	
							<div class="temGradName">分等级模板一</div>
						</c:if>
						<c:if test="${temggather.rank == 2 }">	
							<div class="temGradName">分等级模板二</div>
						</c:if>
						<c:if test="${temggather.rank == 3 }">	
							<div class="temGradName">分等级模板三</div>
						</c:if>
					    	<input type="hidden" class="common2" value="1">
					    	<input type="hidden" class="default" value="0">
				            <li>
				           	  <div class="title">
				                 <p>模板名称：<span>${temggather.name}</span></p>
			                     <p>品牌名称：<span>${temggather.remark}</span></p>
			                     <p>售后电话：<span>${temggather.common1}</span></p>
			                     <p>是否支持退费：<c:if test="${temggather.permit==1}"><span class="span-green">是</span>
			                    				<c:if test="${temggather.common2==1}"><span data-val="1">(退费标准：时间和电量最小)</span></c:if>
			                    				<c:if test="${temggather.common2==2}"><span data-val="2">(退费标准：根据时间)</span></c:if>
			                    				<c:if test="${temggather.common2==3}"><span data-val="3">(退费标准：根据电量)</span></c:if>
			                    			</c:if>
			                    			 <c:if test="${temggather.permit==2}"><span class="span-red">否</span><span></span></c:if>
			                   				 
			                     </p>
			                     <p>是否钱包强制支付：<c:if test="${temggather.walletpay==1}"><span class="span-green">是</span></c:if>
			                    				<c:if test="${temggather.walletpay==2}"><span class="span-red">否</span></c:if></p>
			                    <p class="pTextInfo pTextInfo${temggather.id}" data-text="${temggather.chargeInfo}">收费说明： <button type="button" class="mui-btn mui-btn-success tem-title-text" data-id="${temggather.id}" data-id="${temggather.id}">查看</button>	
                   	</p>
				                  <div>
				                        <button type="button" class="mui-btn mui-btn-success tem-title-edit" data-id="${temggather.id}">编辑</button>
				                        <button type="button" class="mui-btn mui-btn-success tem-title-delete" data-id="${temggather.id}" disabled>删除</button>
				                  </div>
				                </div>
				                <ul class="mui-table-view">
				                    <c:forEach items="${temggather.gather}" var="gardChildTem">
				                    <li class="mui-table-view-cell">
				                        <p>显示名称：<span>${gardChildTem.name}</span></p>
				                        <p>充电价格：<span>${gardChildTem.money} <b>元</b></span></p>
				                        <p>充电时间：<span>${gardChildTem.chargeTime} <b>分钟</b></span></p>
				                        <p>消耗电量：<span>${gardChildTem.chargeQuantity/100} <b>度</b></span></p>
				                        <div>
				                            <button type="button" class="mui-btn mui-btn-success tem-edit" data-id="${gardChildTem.id}"  >编辑</button>
				                            <button type="button" class="mui-btn mui-btn-success tem-delete" data-id="${gardChildTem.id}">删除</button>
				                        </div>
				                    </li>
				                    </c:forEach>
				                    <li class="mui-table-view-cell bottom">
				                        <button type="button" class="mui-btn mui-btn-success  mui-btn-outlined addBut" data-id="${temggather.id}">添加</button>
				                    </li>
				                </ul>     
				            </li>
				        </div>
				        <!-- 这一部分的动态数据的显示，用js控制，控制data-id的数据，控制 button是否加active -->
				     	<c:if test="${temggather.rank ==3 }" >
				     	   <li class="mui-table-view-cell bottom">
				     	   <%-- <c:if test="${source==0}">
					    	<div class="isChecked">
					            <button type="button" class="mui-btn mui-btn-success defaultTem" data-id="3">设为默认</button>
					        </div>
					        <p>默认模板</p> 
					       </c:if>
					       <c:if test="${source!=0}">
					    	<div class="isChecked">
					            <button type="button" class="mui-btn mui-btn-success selectTem" data-id="3">选择模板</button>
					        </div>
					        <p>选中模板</p> 
					       </c:if> --%>     
				         </li>
				     	</c:if>
		      <c:if test="${temggather.rank == 3 }"></div></c:if>
   </c:forEach>
		<c:if test="${templatelist[0].merchantid == 0}">
			<div class="dis-tip" style="font-size: 14px; color:#777;"><strong>提示：</strong>当前模板为系统默认模板，不允许被修改</div>
		</c:if>
        <nav class="mui-bar mui-bar-tab">
            <div>
                <a href="${hdpath}/merchant/devicetemdata?code=${code}" style="width: 60%;"><button type="button" class="mui-btn mui-btn-success">模板列表</button></a>
                <!-- <button type="button" class="mui-btn mui-btn-success addTemplate">添加模板</button> -->
            </div>
        </nav>
    </div>
    <div class="tem-mask2">
        <div class="list-center2">
            <span class="mui-icon mui-icon-closeempty close"></span>
            <h3>修改电子模板</h3>
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
                 <div class="inp chargeInfoTe">
                   <h5>收费说明</h5>
                  <button type="button" class="mui-btn mui-btn-success scanTextInfo" data-text="选择的充电时间为小功率电动车充电时间，仅供参考。
大功率电动车充电时间智能动态计算，以实际为准。">查看</button> 
                </div>
                <div class="inp radio-inp">
                    <h5>是否支持退费</h5>
                    <br/>
                    <label>
                        <input type="radio" name="isRef" value="1" id="isRefInp" />是
                    </label>
                     <span style="font-size: 12px; line-height: 24px; margin-left: 5px; color: #54C1F0;" id="spanList">(默认)</span>
                    <label>
                        <input type="radio" name="isRef" value="0" />否
                    </label>
                </div>
                <div class="inp radio-inp">
                    <h5>是否钱包强制支付</h5>
                    <br/>
                    <label>
                        <input type="radio" name="isWalletPay" value="1" />是
                    </label>
                    <label>
                        <input type="radio" name="isWalletPay" value="0" checked />否
                    </label>
                </div>
                <div class="inp radio-inp">
                    <h5>是否支持支付宝充电</h5>
                    <br/>
                    <label>
                        <input type="radio" name="isIfalipay" value="1" />是
                    </label>
                    <label>
                        <input type="radio" name="isIfalipay" value="0" checked />否
                    </label>
                </div>
                
                 <div class="inp radio-inp">
                    <h5>是否创建分等级模板</h5>
                    <br/>
                    <label>
                        <input type="radio" name="isGrad" value="1" />是
                    </label>
                    <label>
                        <input type="radio" name="isGrad" value="0" />否
                    </label>
                </div>
                <div class="inp ol">
                    <ol class="ol-red">注:</ol>
                    <ol>充电模板中只能创建1个分等级模板</ol>
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
            <h3>修改电子模板</h3>
            <form action="#">
                <div class="inp">
                    <label for="name2">显示名称</label>
                    <br/>
                    <input type="text" id="name2" name="name" placeholder="请填写显示名称"/>
                </div>
                <div class="inp">
                    <label for="parse2">充电价格</label>
                    <br/>
                    <input type="text" id="parse2" name="parse" placeholder="请填写充电价格"/>
                    <div class="btn">元</div>
                </div>
                <div class="inp">
                    <label for="time2">充电时间</label>
                    <br/>
                    <input type="text" id="time2" name="time" placeholder="请填写充电时间"/>
                    <div class="btn">分钟</div>
                </div>
                <div class="inp">
                    <label for="power2">消耗电量</label>
                    <br/>
                    <input type="text" id="power2" name="power" placeholder="请填写消耗电量"/>
                    <div class="btn">度</div>
                </div>
            </form>
            <div class="bottom">
                <button type="button" class="mui-btn mui-btn-success close2">关闭</button>
                <button type="button" class="mui-btn mui-btn-success submit">提交</button>
            </div>
        </div>
    </div>
	<div id="popover">
      <ul class="mui-table-view">
        <li class="mui-table-view-cell">
                   退费标准
        </li>
        <li class="mui-table-view-cell">
            <label>
                 <input type="radio" name="refReg" value="1" checked="checked" id="refReg1" />时间和电量最小
            </label>
        </li>
        <li class="mui-table-view-cell">
            <label>
                 <input type="radio" name="refReg" value="2" id="refReg2"/>根据时间
            </label>
        </li>
        <li class="mui-table-view-cell">
            <label>
                 <input type="radio" name="refReg" value="3" id="refReg3"/>根据电量
            </label>
        </li>
         <li class="mui-table-view-cell lastButton">
           <button type="button" class="mui-btn mui-btn-success" id="exitBut">关闭</button>
           <button type="button" class="mui-btn mui-btn-success" id="confirmBut">确定</button>
        </li>
      </ul>
    </div> 
    
   <div class="chargeTextMask">
   		<div class="ctm">
   			<h3>请填写收费说明</h3>
   			<div class="areaText">
   				<textarea id="textArea"></textarea>
   			</div>
   			<div class="selectText">
   				<button type="button" class="mui-btn mui-btn-success chargetemR" data-text="选择的充电时间为小功率电动车充电时间，仅供参考。
大功率电动车充电时间智能动态计算，以实际为准。" >默认说明</button>
   				<button type="button" class="mui-btn mui-btn-success chargetemo" data-text="1元/4小时  (0W~200W)
1元/3小时  (200W~400W)
1元/2小时  (400W~600W)
1元/1小时  (600W~800W)

   				">自动说明</button>
   			</div>
   			<div class="tipDiv">
  					<span style="font-size: 14px; font-weight: bold; color:#333;">提示：</span>
   					<p>如果想要在页面中分行显示，则只需在要换行的文字后面按下“换行符”即可（“换行符”在手机输入法中）</p>
   					<p><strong style="color:#333;">默认说明：</strong>是通用的收费说明</p>
   					<p><strong style="color:#333;">自动说明：</strong>是根据设备的系统参数中设置的分档功率和设备所使用的充电模板自动生成的收费说明</p>
   			</div>
   			<div class="bottom">
   				<button type="button" class="mui-btn mui-btn-success closeText">关闭</button>
   				<button type="button" class="mui-btn mui-btn-success submitText">确认</button>
   			</div>
   		</div>
   </div>
</body>

</html>