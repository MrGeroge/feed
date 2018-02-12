package net.zypro.feed.controller;

import java.io.IOException;
import java.net.BindException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zypro.feed.exception.AuthenticationException;
import net.zypro.feed.exception.InterfaceChangedNotUsedException;
import net.zypro.feed.exception.MissIdException;
import net.zypro.feed.exception.SessionInvalidateException;
import net.zypro.feed.exception.TokenException;
import net.zypro.feed.vo.Result;

import org.apache.log4j.Logger;
import org.hibernate.TypeMismatchException;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

@RestController
public class BaseController { // 异常处理的类
	private static final Logger logger = Logger.getLogger(BaseController.class);
	private Result result = new Result();

	@ExceptionHandler
	// 在Controller类中添加该注解方法即可，只针对该controller起作用
	public Result exceptionHandler(Exception ex, HttpServletResponse response,
			HttpServletRequest request) throws IOException {
		logger.error(ex.getMessage(), ex);
		if (ex.getClass() == NoSuchRequestHandlingMethodException.class) {
			result.error_code = "3001";
			result.msg = "没找到对应请求处理方法";
		} // 找不到匹配的url
		else if (ex.getClass() == NoHandlerFoundException.class) {
			result.error_code = "3002";
			result.msg = "没找到处理";
		} else if (ex.getClass() == TokenException.class) {
			result.error_code = "2001";
			result.msg = "缺少认证Token";
		} // 找不到请求参数
		else if (ex.getClass() == MissIdException.class) {
			result.error_code = "2002";
			result.msg = "缺少用户ID";
		} // 找不到请求参数
		else if (ex.getClass() == AuthenticationException.class) {
			result.error_code = "2003";
			result.msg = "认证失败";
		} // 找不到请求参数
		else if (ex.getClass() == MissingServletRequestParameterException.class) {
			result.error_code = "2006";
			result.msg = "缺少请求参数";
		} // 找不到请求参数
		else if (ex.getClass() == BindException.class) {
			result.error_code = "2007";
			result.msg = "端口绑定错误";
		} else if (ex.getClass() == HttpMessageNotReadableException.class) {
			result.error_code = "2008";
			result.msg = "Http信息不可读";
		} else if (ex.getClass() == MethodArgumentNotValidException.class) {
			result.error_code = "2009";
			result.msg = "方法参数无效";
		} else if (ex.getClass() == MissingServletRequestPartException.class) {
			result.error_code = "2010";
			result.msg = "丢失servlet请求部分";
		} else if (ex.getClass() == TypeMismatchException.class) {
			result.error_code = "2011";
			result.msg = "类型错误匹配";
		} else if (ex.getClass() == ConversionNotSupportedException.class) {
			result.error_code = "1003";
			result.msg = "转化不被支持";
		} else if (ex.getClass() == HttpMessageNotWritableException.class) {
			result.error_code = "1004";
			result.msg = "Http信息不能写入";
		} else if (ex.getClass() == SessionInvalidateException.class) {
			result.error_code = "1005";
			result.msg = "session已失效，请重新登陆";
		} else if (ex.getClass() == InterfaceChangedNotUsedException.class) {
			result.error_code = "1002";
			result.msg = "此接口暂停使用";
		} else {
			result.error_code = "1001";
			result.msg = "服务器未知异常";
		}
		return result;
	}
}
