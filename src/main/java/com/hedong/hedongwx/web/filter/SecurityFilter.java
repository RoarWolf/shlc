package com.hedong.hedongwx.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.DisposeUtil;

@Component
public class SecurityFilter implements Filter {
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("init...");

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
//		System.out.println("this is cross filter");
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpServletRequest servletRequest = (HttpServletRequest) request;
//	    //如果要做细的限制，仅限某域名下的可以进行跨域访问到此，可以将*改为对应的域名
		// resp.setHeader("Access-Control-Allow-Origin", "http://39.97.98.149:8809");
		String originHeader =servletRequest.getHeader("Origin");
		resp.setHeader("Access-Control-Allow-Origin", originHeader);
		resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		resp.setHeader("Access-Control-Allow-Headers", "Authorization,Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		resp.setHeader("Access-Control-Max-Age", "1728000");
		resp.setHeader("Access-Control-Allow-Credentials", "true");
		
		HttpServletRequest req = (HttpServletRequest) request;
		String url = req.getRequestURL().toString();
		String path = req.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";	
		Integer isolate = CommUtil.toInteger(req.getParameter("isolate"));
		if(isolate.equals(1)){
			chain.doFilter(request, response);
			return;
			
		}else{
//		System.out.println("输出：   "+url);
			if (url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".jpg") || url.endsWith(".png") || url.endsWith(".ttf") 
					|| url.endsWith(".icon") || url.endsWith(".md") || url.endsWith(".txt") || url.endsWith(".TTF")) {
				chain.doFilter(request, response);
				return;
			}else if ((url.indexOf("/allow") != -1) || (url.indexOf("/webenter") != -1) || (url.indexOf("/basicsInfo") != -1) || 
					(url.indexOf("/dist") != -1) || (url.indexOf("/login") != -1)) {//url包含“/allow”
				chain.doFilter(request, response);
				return;
			}else if ((url.indexOf("/pc") != -1)) {//url包含“/pc”
				User admin = (User) req.getSession().getAttribute("admin");
				if ( admin !=null ) {//直接运行
					chain.doFilter(request, response);
					return;
				}else {//检测
					if (isPcPassMethod(url)) {
						chain.doFilter(request, response);
						return;
					}
				}
				returnAdminLogin(request, response, basePath);
			}else{
				User user = (User) req.getSession().getAttribute("user");
//				User user = DisposeUtil.getUserBySessionId(req);
				int length = url.length();
				int lastIndexOf = url.lastIndexOf("/");
				if ((length - 1) == lastIndexOf) {
					returnPCAdminLogin(request, response, basePath);
					return;
				}
				if ( user !=null ) {//直接运行
					chain.doFilter(request, response);
					return;
				} else {//检测
					if (isWeChPassMethod(url)) {
						chain.doFilter(request, response);
						return;
					}
				}
				returnWeChatLogin(request, response, basePath);
			}
		}
	}
	
	/**
	 * 微信（WeChat）允许通过路径
	 * @param url
	 * @return
	 */
	private boolean isWeChPassMethod(String url) {
		if (url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".jpg") || url.endsWith(".png")
				|| url.endsWith(".ttf") || url.endsWith(".icon")) {
			return true;
		} else if ((url.indexOf("/permit") != -1)) {
			return true;
		} else if ((url.indexOf("/scan") != -1)) {
			return true;
		} else if ((url.indexOf("/a") != -1)) {
			return true;
		} else if ((url.indexOf("/user/login") != -1)) {
			return true;
		} else if ((url.indexOf("/existaccount") != -1)) {
			return true;
		} else if ((url.indexOf("/captcha") != -1)) {
			return true;
		} else if ((url.indexOf("/oauth2") != -1)) {
			return true;
		} else if ((url.indexOf("/chargingoauth2") != -1)) {
			return true;
		} else if ((url.indexOf("/wxpay") != -1)) {
			return true;
		} else if ((url.indexOf("/alipay") != -1)) {
			return true;
		} else if ((url.indexOf("/equipment/allChargePort") != -1)) {
			return true;
		} else if ((url.indexOf("/hdwx") != -1)) {
			return true;
		} else if ((url.indexOf("/testpay") != -1)) {
			return true;
		} else if ((url.indexOf("/merchant/home") != -1)) {
			return true;
		} else if ((url.indexOf("/merchant/infoverdict") != -1)) {
			return true;
		} else if ((url.indexOf("/merchant/skipRegister") != -1)) {
			return true;
		} else if ((url.indexOf("/merchant/addoppenid") != -1)) {
			return true;
		} else if ((url.indexOf("/portstate") != -1)) {
			return true;
		} else if ((url.indexOf("/generalLogin") != -1)) {
			return true;
		} else if ((url.indexOf("/oauth2login") != -1)) {
			return true;
		} else if ((url.indexOf("/general/check") != -1)) {
			return true;
		} else if ((url.indexOf("/general/check") != -1)) {
			return true;
		} else if ((url.indexOf("/general") != -1)) {
			return true;
		} else if ("http://www.he360.com.cn/".equals(url) || "http://he360.com.cn/".equals(url)
				|| "http://www.he360.com.cn/MP_verify_gHZniEVLVdhKCPS4.txt".equals(url) || 
				"http://www.he360.cn/".equals(url) || "http://he360.cn/".equals(url)) {
			return true;
		} else if ("http://www.tengfuchong.com.cn/".equals(url) || "http://tengfuchong.com.cn/".equals(url)
				|| "http://www.tengfuchong.com.cn/MP_verify_s3D9UPpVFsbqBdZG.txt".equals(url)
				|| "http://www.tengfuchong.com/".equals(url) || "http://tengfuchong.com/".equals(url)) {
			return true;
		} else if (url.indexOf("/load") != -1) {
			return true;
		} else if (url.indexOf("/helpdoc") != -1) {
			return true;
		} else if (url.indexOf("/paytest") != -1) {
			return true;
		} else if (url.indexOf("/connectus") != -1) {
			return true;
		} else if (url.indexOf("/charge/getuserinfo") != -1) {
			return true;
		} else if (url.indexOf("/offlineCard") != -1) {
			return true;
		} else if (url.indexOf("/queryOfflineCard") != -1) {
			return true;
		} else if (url.indexOf("/chargeCard") != -1) {
			return true;
		} else if (url.indexOf("/offlineCardCharge") != -1) {
			return true;
		} else if (url.contains(".html")) {
			return true;
		} else if (url.indexOf("/incoins") != -1) {
			return true;
		} else if (url.indexOf("/connectInCoins") != -1) {
			return true;
		} else if (url.indexOf("/editHardversion") != -1) {
			return true;
		} else if (url.indexOf("/querystate") != -1) {
			return true;
		} else if (url.indexOf("/lock") != -1) {
			return true;
		} else if (url.indexOf("/testTemp") != -1) {
			return true;
		} else if (url.indexOf(".md") != -1) {
			return true;
		} else if (url.indexOf("/testmutual") != -1) {
			return true;
		} else if (url.indexOf("/readsysteminfo") != -1) {
			return true;
		} else if (url.indexOf("/testpaytoport") != -1) {
			return true;
		} else if (url.indexOf("/stopRechargeByPort") != -1) {
			return true;
		} else if (url.indexOf("/setsystem") != -1) {
			return true;
		} else if (url.indexOf("/checkOrderDetail") != -1) {
			return true;
		} else if (url.indexOf("/withcardinfoOauth") != -1) {
			return true;
		} else if (url.indexOf("/chargePort") != -1) {
			return true;
		} else if (url.contains("/equipment/setSysParam")) {
			return true;
		} else if ((url.indexOf("/wctemplate") != -1)) {
			return true;
		} else if ((url.indexOf("/getLocationBySendData") != -1)) {
			return true;
		} else if ((url.indexOf("/jssdkWxGet") != -1)) {
			return true;
		} else if ((url.indexOf("/wolf") != -1)) {
			return true;
		} else if ((url.indexOf("/getNormalAccessToken") != -1)) {
			return true;
		} else if ((url.indexOf("/hdredis") != -1)) {
			return true;
		} else if (url.indexOf("/fileDispose") != -1) {
			return true;
		} else if ((url.indexOf("/unionpay") != -1)) {
			return true;
		}else if (url.toUpperCase().contains("task".toUpperCase())) {
			return true;
		}else if (url.indexOf("/wolfwebsocket") != -1) {
			return true;
		}else if (url.indexOf("/druid") != -1) {
			return true;
		}else {
			return false;
		}
		
	}
	/**
	 * PC端允许通过路径
	 * @param url
	 * @return
	 */
	private boolean isPcPassMethod(String url) {
		if (url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".jpg") || url.endsWith(".png")
				|| url.endsWith(".ttf") || url.endsWith(".icon")) {
			return true;  
		} else if ((url.indexOf("/allow") != -1)) {
			return true;
		} else if ((url.indexOf("/permit") != -1)) {
			return true;
		} else if ((url.indexOf("/scan") != -1)) {
			return true;
		} else if ((url.indexOf("/pcadminlogin") != -1)) {
			return true;
		} else if ((url.indexOf("/captcha") != -1)) {
			return true;
		}  else {
			return false;
		}
	}
	
	//跳转微信
	public void returnWeChatLogin(ServletRequest request, ServletResponse response, String basePath) throws IOException {
		response.getWriter().println("<HTML><HEAD><TITLE></TITLE></HEAD><BODY><script>top.location.href='" + basePath
				+ "merchant/skipRegister'</script></BODY></HTML>");
	}
	
	//跳转pc登录
	public void returnAdminLogin(ServletRequest request, ServletResponse response, String basePath) throws IOException {
		response.getWriter().println("<HTML><HEAD><TITLE></TITLE></HEAD><BODY><script>top.location.href='" + basePath
				+ "pcadminlogin/index'</script></BODY></HTML>");
	}
	
	//跳转pc登录
	public void returnPCAdminLogin(ServletRequest request, ServletResponse response, String basePath) throws IOException {
		response.getWriter().println("<HTML><HEAD><TITLE></TITLE></HEAD><BODY><script>top.location.href='" + basePath
				+ "index.html'</script></BODY></HTML>");
	}
	
	@Override
	public void destroy() {
		System.out.println("销毁...");

	}

}
