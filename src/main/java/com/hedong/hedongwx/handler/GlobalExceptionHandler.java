package com.hedong.hedongwx.handler;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类
 * 
 * @author xiangze
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = HDUserException.class)
	@ResponseBody
	public Map<String, Object> exceptionHandler(HttpServletRequest req, HDUserException e) throws Exception {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("regist fail", false);
		modelMap.put("errMsg", e.getErrMsg());
		return modelMap;
	}

}