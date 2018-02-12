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
public class BaseController { // �쳣�������
	private static final Logger logger = Logger.getLogger(BaseController.class);
	private Result result = new Result();

	@ExceptionHandler
	// ��Controller������Ӹ�ע�ⷽ�����ɣ�ֻ��Ը�controller������
	public Result exceptionHandler(Exception ex, HttpServletResponse response,
			HttpServletRequest request) throws IOException {
		logger.error(ex.getMessage(), ex);
		if (ex.getClass() == NoSuchRequestHandlingMethodException.class) {
			result.error_code = "3001";
			result.msg = "û�ҵ���Ӧ��������";
		} // �Ҳ���ƥ���url
		else if (ex.getClass() == NoHandlerFoundException.class) {
			result.error_code = "3002";
			result.msg = "û�ҵ�����";
		} else if (ex.getClass() == TokenException.class) {
			result.error_code = "2001";
			result.msg = "ȱ����֤Token";
		} // �Ҳ����������
		else if (ex.getClass() == MissIdException.class) {
			result.error_code = "2002";
			result.msg = "ȱ���û�ID";
		} // �Ҳ����������
		else if (ex.getClass() == AuthenticationException.class) {
			result.error_code = "2003";
			result.msg = "��֤ʧ��";
		} // �Ҳ����������
		else if (ex.getClass() == MissingServletRequestParameterException.class) {
			result.error_code = "2006";
			result.msg = "ȱ���������";
		} // �Ҳ����������
		else if (ex.getClass() == BindException.class) {
			result.error_code = "2007";
			result.msg = "�˿ڰ󶨴���";
		} else if (ex.getClass() == HttpMessageNotReadableException.class) {
			result.error_code = "2008";
			result.msg = "Http��Ϣ���ɶ�";
		} else if (ex.getClass() == MethodArgumentNotValidException.class) {
			result.error_code = "2009";
			result.msg = "����������Ч";
		} else if (ex.getClass() == MissingServletRequestPartException.class) {
			result.error_code = "2010";
			result.msg = "��ʧservlet���󲿷�";
		} else if (ex.getClass() == TypeMismatchException.class) {
			result.error_code = "2011";
			result.msg = "���ʹ���ƥ��";
		} else if (ex.getClass() == ConversionNotSupportedException.class) {
			result.error_code = "1003";
			result.msg = "ת������֧��";
		} else if (ex.getClass() == HttpMessageNotWritableException.class) {
			result.error_code = "1004";
			result.msg = "Http��Ϣ����д��";
		} else if (ex.getClass() == SessionInvalidateException.class) {
			result.error_code = "1005";
			result.msg = "session��ʧЧ�������µ�½";
		} else if (ex.getClass() == InterfaceChangedNotUsedException.class) {
			result.error_code = "1002";
			result.msg = "�˽ӿ���ͣʹ��";
		} else {
			result.error_code = "1001";
			result.msg = "������δ֪�쳣";
		}
		return result;
	}
}
