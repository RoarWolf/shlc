
 //转格式
  Date.prototype.Format = function(fmt){ //author: meizz   
      var o = {   
        "M+" : this.getMonth()+1,                 //月份   
        "d+" : this.getDate(),                    //日   
        "h+" : this.getHours(),                   //小时   
        "m+" : this.getMinutes(),                 //分   
        "s+" : this.getSeconds(),                 //秒   
        "q+" : Math.floor((this.getMonth()+3)/3), //季度   
        "S"  : this.getMilliseconds()             //毫秒   
      };   
      if(/(y+)/.test(fmt))   
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
      for(var k in o)   
        if(new RegExp("("+ k +")").test(fmt))   
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
      return fmt;   
    };


  //开始时间默认15天前的日期
  var startTime=new Date((new Date()-15*24*60*60*1000)).Format("yyyy/MM/dd"); //
  var endTime= new Date().Format("yyyy/MM/dd");
  //获取$("#date")的值

  var m=new Date().getMonth()+1;
  var y=new Date().getFullYear();

  //格式化
  function setm(){
        m<10?startTime=y+"/0"+m+'/01':startTime=y+"/"+m+'/01';
        if(m==1 || m==3 || m==5 || m==7 || m==8 || m==10 || m==12){
          m<10?endTime=y+"/0"+m+"/31":endTime=y+"/"+m+"/31"
        }else if(m==4 || m==6 || m==9 || m==11){
          m<10?endTime=y+"/0"+m+"/30":endTime=y+"/-"+m+"/30"
        }else if(m==2){
          if (((y % 4)==0) && ((y % 100)!=0) || ((y % 400)==0)) {
          endTime=y+"/0"+m+"/29"
          }else{
            endTime=y+"/0"+m+"/28"
          }
        }
  }

  //获取本周
  function ThisWeekStr() {
    var returnStr = "";
    var date = new Date();      //当前时间
    var week = date.getDay();   //获取今天星期几
    var monday = GetDate2(week - 1, 1, date);      //获取星期一
    var sunday = GetDate2(7 - week, 2, date);   //获取星期天
    //起始时间的年月日
    var year1 = monday.getFullYear();
    var month1 = monday.getMonth() + 1;
    var day1 = monday.getDate();
    //结束时间的年月日
    var year2 = sunday.getFullYear();
    var month2 = sunday.getMonth() + 1;
    var day2 = sunday.getDate();
    //处理起始时间小于10的追加"0"在前面
    month1 = month1 < 10 ? "0" + month1 : month1;
    day1 = day1 < 10 ? "0" + day1 : day1;
    //处理结束时间小于10的追加"0"在前面
    month2 = month2 < 10 ? "0" + month2 : month2;
    day2 = day2 < 10 ? "0" + day2 : day2;
 
    returnStr = year1 + "/" + month1 + "/" + day1;       //起始时间
    returnStr +="&&"+year2 + "/" + month2 + "/" + day2;      //结束时间
    return returnStr;
  }
  //获取本月
  function ThisMonthStr() {
    var returnStr = "";
    var date = new Date();      //当前时间
    var year = date.getFullYear();
    var month = date.getMonth();
 
    var min = new Date(year, month, 1);                 //本月月初
    var max = new Date(year, month + 1, 0);             //本月月底
 
    //起始时间的年月日
    var year1 = min.getFullYear();
    var month1 = min.getMonth() + 1;
    var day1 = min.getDate();
    //结束时间的年月日
    var year2 = max.getFullYear();
    var month2 = max.getMonth() + 1;
    var day2 = max.getDate();
    //处理起始时间小于10的追加"0"在前面
    month1 = month1 < 10 ? "0" + month1 : month1;
    day1 = day1 < 10 ? "0" + day1 : day1;
    //处理结束时间小于10的追加"0"在前面
    month2 = month2 < 10 ? "0" + month2 : month2;
    day2 = day2 < 10 ? "0" + day2 : day2;
 
    returnStr = year1 + "/" + month1 + "/" + day1;       //起始时间
    returnStr += "&&"+year2 + "/" + month2 + "/" + day2;      //结束时间
    return returnStr;
  }

  function GetDate2(day, type, date) {
    var zdate;
    if (date === null || date === undefined) {
        zdate = new Date();
    } else {
        zdate = date;
    }
    var edate;
    if (type === 1) {
        edate = new Date(zdate.getTime() - (day * 24 * 60 * 60 * 1000));
    } else {
        edate = new Date(zdate.getTime() + (day * 24 * 60 * 60 * 1000));
    }
    return edate;
  }

  //日期控件
          function rangeBasicInit(a) {
            $(function () {
                // Mobiscroll Range initialization
                $('.inpDiv').mobiscroll().range(a); 
              
                $('#rangeBasic-show').click(function () {
                    $('#rangeBasic-demo').mobiscroll('show');
                    return false;
                });
            
                $('#rangeBasic-clear').click(function () {
                    $('#rangeBasic-demo').mobiscroll('clear');
                    return false;
                }); 
            
            });
        }