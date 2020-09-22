/**
 * Created by DELL on 2019/5/11.
 */

$(function(){
    window.addEventListener("popstate", function(e) {
        var existuser = $("#existuser").val();
        if (existuser == 1) {
            WeixinJSBridge.call('closeWindow');
        } else {
            WeixinJSBridge.call('closeWindow');
        }
    }, false);

    $('.mui-icon-clear').click(function(e){
        e =e || window.event
        e.stopPropagation()
    })
});
var timer= null
$('#register_btn').click(function(e){ //点击登录按钮
    e= e || window.event
    e.preventDefault()
    handleLogin()
})

$('b.reNum').on('touchstart',function(e){ //点击发送验证码
		var mobileVal= $('#mobile').val().trim()
		var inCodeWrite= $('#invitationCode').val().trim()//设备号
		if(!checkPhone()){
			return false
		}
		if(inCodeWrite=='' || inCodeWrite == null){
			showToast("请输入设备号，或者商户手机号！");
			return
		}
		
	 	$.ajax({
		   url: '/merchant/existaccount',
		   data: {mobile: mobileVal,invitecode: inCodeWrite,type:1},
		   type : "post",
		   dataType : "json",
		   success: function(e){
		      if(e.code == 201){
		    	    //按钮倒计时
		    	    sendDownTime()
		    	    $.ajax({
		    	        type : 'POST',
		    	        url : "/merchant/captcha",//获取验证码
		    	        data : {captcha : 1,mobile : mobileVal},
		    	        success : function(e){
		    	            if(e.authcode!=""){
		    	                $("#captchaNum").val(e.authcode);
		    	                $("input[name='time']").val(new Date().getTime());
		    	            }else{
		    	            	showToast('异常错误');
		    	            }
		    	        },
		    	        error : function() {
		    	            reShow() //显示获取验证码
		    	            showToast('验证发送失败');
		    	        }
		    	    });
		      }else if(e.code==200){
		    	  showToast('该手机号已存在，不能注册'); 
		      }else if(e.code==202){
		    	  if(testReg(/^1[3|4|5|6|7|8|9]\d{9}$/,inCodeWrite)){
		    		  return showToast('手机号不正确或手机号不是商户！');
		    	  }
		    	  showToast('输入的设备号不存在或已绑定，请输入未绑定的设备号'); 
		      }
		   },
		   error: function(e){
			   showToast('注册异常'); 
		   }
		 })
})


function handleLogin(){ //处理登录
    var captchaVal= $('#captcha').val().trim()
    var invitationCodeVal= $('#invitationCode').val().trim()
    var paramstate= $('#paramstate').val().trim()
    if(!checkPhone()){
       return false
    }
    if(invitationCodeVal== null || invitationCodeVal == ''){
    	showToast('请输入邀请码')
        return false
    }
   /* if(captchaVal.length <= 0){
        showToast('请输入验证码')
        return false
    }
    if(!testReg(/^\d{6}$/,captchaVal)){
        showToast('验证码位数不正确')
        return false
    }
   if( $('#captchaNum').val().trim() != captchaVal){
	   showToast('验证码不正确')
	   return false
   }*/

    var redirUrl = "";
    if (paramstate == 0) {
    	redirUrl = "/merchant/index";
    } else if (paramstate == 1) {
    	redirUrl = "/merchant/manage";
    }
    var data= $('#formht').serialize()
    $.ajax({
        type : "POST",
        url : "/merchant/register",
        data : data,
        success : function(e) {
        	console.log(e)
        	
        	if(e.code == 200){
        		window.location.href = redirUrl;
        	}else{
        		showToast(e.message);
        	}
        },
        error : function() {
            reShow()
            showToast("异常！");
        }
    });
}

function showToast(content,time){ //调用Toast提示框
    content= content || ''
    time= time || '1500'
    mui.toast(content,{ duration:time, type:'div' })
}

function testReg(reg,str){  //正则判断
    return reg.test(str)
}

function checkPhone(){  //检查手机号
    var mobileVal= $('#mobile').val().trim()
    if(mobileVal.length <= 0){
        //调用mui
        showToast('请输入手机号')
        return false
    }
    if(!testReg(/^1[3|4|5|6|7|8|9]\d{9}$/,mobileVal)){
        showToast('请正确输入手机号')
        return false
    }
    return true
}

function sendDownTime(){
    clearInterval(timer)
    $('b.reNum').fadeOut('300')
    $('b.bTime').fadeIn('300')
    var str= ''
    var num= 120
    timer= setInterval(function () {
        num--
        str= '倒计时'+ num +'s'
        $('b.bTime').html(str)
        if(num === 0){
            clearInterval(timer)
            $('b.bTime').fadeOut('300')
            $('b.reNum').fadeIn('300')
        }
    },1000)
}

function reShow(){ //重新显示获取验证码
    clearInterval(timer)
    $('b.bTime').fadeOut('300')
    $('b.reNum').fadeIn('300') 
}

