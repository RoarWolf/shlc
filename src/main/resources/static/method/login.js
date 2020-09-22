
//==============================================================================================================
//微信扫码登录
var obj = new WxLogin({
    id:"login_ewm", 
    appid: "wx695275de73b7dad4", 
    scope: "snsapi_login", 
    redirect_uri:"http%3A%2F%2Fwww.tengfuchong.com.cn%2Fpcadminlogin%2FscanQRCode",
    state: "2",
    style: "black",
    href: ""
});

//==============================================================================================================
//点击input对话框，触发焦点事件，设置当前点击的对话框背景变为黄色
$("input").focus(function(){
	$(this).css("background-color","#FFFFCC");
 });
//input对话框，触发失去焦点事件，设置当前点击的对话框背景变为黄色
$("input").blur(function(){
	//var bas = $(this).val();
	$(this).css("background-color","#fff");
});

//==============================================================================================================
//验证手机号格式是否
function mobile(n){
    var reg = /^1[3|4|5|7|8]\d{9}$/;
    if(!reg.test(n)){
		alert("手机号不合法!");
		return false;
    }
    return true;
}

document.getElementById("account").blur(function(e){
	var account = $("#account").val();
	alert("账户不能为空。");
	
})

$("#ipt").on('focus',function(){
	var account = $("#account").val();
	if(account==null || account==""){
		alert("账户不能为空。");
		return true;
	}
})
/*
$("#uphonenum").blur(function(){
	var mobile = $("#uphonenum").val();
	if(mobile=="") mui.toast('请输入合伙人手机号。')
	$.ajax({
        type : "POST",
		url : "${hdpath}/merchant/checkAccount", //处理页面的路径
		data : {
			mobile : mobile,
		},
	    success:function(e){ //服务器返回响应，根据响应结果，分析是否登录成功；
	    	if (e != null) {
	    		$("#partner").val(e.realname);
	    		$("#nick").val(e.username);
	    	} else {
	    		mui.alert("该手机号用户不存在！");
	    		$("#uphonenum").val("");
	    	}
	    },
	    error:function(){//异常处理；
	    	 mui.toast('错误!')
	    }
	})
});
*/
//==============================================================================================================
/*
 * <input type="text" id="checkcode" name="checkcode" value=""/> 
 * <input type="text" id="authcode" name="authcode"/> 
 * <canvas id="canvas" width="120" height="40"></canvas>
 * <a href="#" id="refreshdraw">看不清，换一张</a>
 */
//生成验证码
drawPic();

//刷新验证码
document.getElementById("refreshdraw").onclick = function(e){
  e.preventDefault();
  drawPic();
}

/**生成一个随机数**/
function randomNum(min,max){
  return Math.floor( Math.random()*(max-min)+min);
}
/**生成一个随机色**/
function randomColor(min,max){
  var r = randomNum(min,max);
  var g = randomNum(min,max);
  var b = randomNum(min,max);
  return "rgb("+r+","+g+","+b+")";
}

/**绘制验证码图片**/
function drawPic(){
  var canvas=document.getElementById("canvas");
  var width=canvas.width;
  var height=canvas.height;
  var ctx = canvas.getContext('2d');
  ctx.textBaseline = 'bottom';

  /**绘制背景色**/
  ctx.fillStyle = randomColor(180,240); //颜色若太深可能导致看不清
  ctx.fillRect(0,0,width,height);
  
  /**绘制文字**/
  var str = 'ABCEFGHJKLMNPQRSTWXY123456789';
  var authcode = "";
  for(var i=0; i<4; i++){
    var txt = str[randomNum(0,str.length)];
    authcode = securitycode + txt;
    ctx.fillStyle = randomColor(50,160);  //随机生成字体颜色
    ctx.font = randomNum(15,40)+'px SimHei'; //随机生成字体大小
    var x = 10+i*25;
    var y = randomNum(25,45);
    var deg = randomNum(-45, 45);
    //修改坐标原点和旋转角度
    ctx.translate(x,y);
    ctx.rotate(deg*Math.PI/180);
    ctx.fillText(txt, 0,0);
    //恢复坐标原点和旋转角度
    ctx.rotate(-deg*Math.PI/180);
    ctx.translate(-x,-y);
  }
  document.getElementById("authcode").val(authcode)
  
  /**绘制干扰线**/
  for(var i=0; i<8; i++){
    ctx.strokeStyle = randomColor(40,180);
    ctx.beginPath();
    ctx.moveTo( randomNum(0,width), randomNum(0,height) );
    ctx.lineTo( randomNum(0,width), randomNum(0,height) );
    ctx.stroke();
  }
  /**绘制干扰点**/
  for(var i=0; i<100; i++){
    ctx.fillStyle = randomColor(0,255);
    ctx.beginPath();
    ctx.arc(randomNum(0,width),randomNum(0,height), 1, 0, 2*Math.PI);
    ctx.fill();
  }
}
//==============================================================================================================


//==============================================================================================================


//==============================================================================================================


//==============================================================================================================


//==============================================================================================================









