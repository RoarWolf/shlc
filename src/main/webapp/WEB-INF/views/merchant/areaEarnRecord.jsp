<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport"
	content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<!--App自定义的css-->
<%-- <link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/app.css" /> --%>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="${hdpath }/mui/js/mui.min.js"></script>
<script type="text/javascript" src="${hdpath }/js/jquery.js"></script>
<title>小区每日收益统计</title>
 <style>
    .app {
      font-size: 14px;
    }
    main {
      position: absolute;;
      left: 0;
      right: 0;
      bottom: 0;
      top: 0;
      overflow-y: auto;
      padding: 44px 15px 0;
    }
    .scrollDiv {
      position: absolute;
      top: 59px;
      bottom: 0;
      left: 15px;
      right: 15px;
      overflow-y: auto;
    }
    .app .tb {
      border-radius: 5px;
      overflow: hidden;
/*      margin-top: 15px;*/
      border: 1px solid #add9c0;
      box-sizing: border-box;
      
    }

     .app table {
        width: 100%;    
        border-radius: 8px;
      }
      .app table td {
        text-align: center;
        line-height: 35px;
        border-bottom: 1px solid #add9c0;
        border-right: 1px solid #add9c0;
      }
      .app table tr td:last-child {
        border-right: none;
      }
      .app table tbody tr:last-child td{
        border-bottom: none;
      }
      .app table thead{
        background-color: #C8EFD4;
        color: #333;
        font-weight: 600;
      }
      .app table tbody {
         background-color: #f5f7fa;
      }
  </style>
</head>
<body data-showincoins="${showincoins}">
<div class="app">
  <header class="mui-bar mui-bar-nav">
      <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
      <h1 class="mui-title" data-aid="${aid}">小区每日收益统计</h1>
    </header>
    <main>
      <div class="scrollDiv">
           <div class="tb">
            <table cellspacing="0">
              <thead>
                <tr>
                  <td>时间</td>
                  <td>线上收益</td>
                  <c:if test="${showincoins != 2}">
                  	<td>投币收益</td>
                  </c:if>
                </tr>
              </thead>
                <tbody>
                	<%-- <tr>
                      <td>
                       <fmt:formatDate value="${today }" pattern="yyyy-MM-dd" />
                      </td>
                      <td><fmt:formatNumber type="number" value="${equipment.nowOnlineEarn == null ? 0 : equipment.nowOnlineEarn }" maxFractionDigits="2" pattern="0.00" /></td>
                      <td><fmt:formatNumber type="number" value="${equipment.nowCoinsEarn == null ? 0 : equipment.nowCoinsEarn }" maxFractionDigits="0" pattern="0" /></td>
                    </tr> --%>
                	<c:forEach items="${areaEarnlist}" var="earn">
                    <tr>
                      <td>
                       <fmt:formatDate value="${earn.createTime }" pattern="yyyy-MM-dd" />
                      </td>
                      <td><fmt:formatNumber type="number" value="${earn.onlineEarn == null ? 0 : earn.onlineEarn }" maxFractionDigits="2" pattern="0.00" /></td>
                      <c:if test="${showincoins != 2}">
                      	<td><fmt:formatNumber type="number" value="${earn.coinEarn == null ? 0 : earn.coinEarn }" maxFractionDigits="0" pattern="0" /></td>
                   	  </c:if>
                    </tr>
                    </c:forEach>
                    <tr class="lastTr">
                     <td colspan="3" id="loadText">
                     	暂无更多数据
                     </td>
                    </tr>
                 </tbody>
            </table>
          </div>
      </div>
      
    </main>
  </div>  
 <script>
 $(function(){
	 var showincoins= $('body').attr('data-showincoins').trim() //是否显示投币收益 2为不显示，
	  var aid= $('h1').attr('data-aid').trim()
	  var beginnum= 31 //起始位置为31
	  var flag= false //滚动是否到达底部
	  var isMoreData= true
	  $('.scrollDiv').scroll(function(e){
	    e= e || window.event
	    var scrollTop= e.target.scrollTop
	    var targetHeight= $(this).height() //容器高度
	    var tbHeight= $('.tb').height() // 总高度
	    if(isMoreData){
	    	 $('#loadText').text('正在加载数据···')
	    }
	    if(scrollTop >= (tbHeight-targetHeight)){
	      if(!flag && isMoreData){
	          $.ajax({
	        	  url: '/equipment/ajaxCodeDayEarn',
	        	  type: 'POST',
	        	  data: {
	        		  aid: aid,
	        		  beginnum: beginnum
	        	  },
	        	  success: function(res){
	        		  handleData(res.earnlist)
	        	  },
	        	  error: function(err){
	        		  mui.toast('获取失败，请重试')
	        	  }
	          })
	          flag= true
	      }// if(!flag)
	      
	    }//if(scrollTop)
	  }) //$('.scrollDiv').scroll
	  function handleData(list){
		  if(list.length < 25){
			  //没有更多数据了，
			  isMoreData= false
			  $('#loadText').text('没有更多数据了')
		  }
		  var htmlStr= ''
		  var fragme= document.createDocumentFragment(); 
		  $(list).each(function(i,item){
			  list[i].moneytotal= handleTotalMoney(list[i].moneytotal)
			  list[i].countTime= list[i].countTime.substring(0,10)
			  var iconStr= showincoins != 2 ? '<td>'+list[i].incoinsmoney+'</td>' : ''
			  var str= '<tr><td>'+list[i].countTime+'</td> <td>'+list[i].moneytotal+'</td>'+iconStr+'</tr>'
			  var lastTr= $('tbody tr:last')[0]
			  $('tbody')[0].insertBefore($(str)[0],lastTr) 
		  })
		  flag= false //渲染之后就改为false
		  beginnum += list.length
	  }
	  
	 
	  function handleTotalMoney(momey){
			var moneyStr= momey.toString()
			var monStr
			var reg1= /(-)?\d+\.\d+/g 
			if(reg1.test(moneyStr)){
				moneyStr += '00'
				var reg=/^(-)?\d+\.\d{2}/ 
					monStr= moneyStr.match(reg)[0]
			}else{
				monStr= moneyStr+'.00'
			}
			return monStr
			
		}
	  
	})
</script>
</body>
</html>