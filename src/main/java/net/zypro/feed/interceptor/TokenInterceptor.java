package net.zypro.feed.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zypro.feed.controller.BaseController;
import net.zypro.feed.controller.UserController;
import net.zypro.feed.exception.AuthenticationException;
import net.zypro.feed.exception.MissIdException;
import net.zypro.feed.exception.TokenException;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import redis.clients.jedis.Jedis;

public class TokenInterceptor extends BaseController implements
		HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {

	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object arg2) throws Exception { // preHandle
																			// �ڴ�������ǰ��Ҫ��ɵĲ���,������ֵΪtrue����������
		Jedis jedis = null;
		String id = request.getParameter("userid");
		String client_token = request.getParameter("token"); // ��ÿͻ��˷��͹�����token
		if (id == null) { // ȱ��id
			throw new MissIdException();
		} else if (client_token == null) { // ȱ��token
			throw new TokenException();
		} else {
			jedis = UserController.getPool().getResource(); // ������ӳص�����
			String server_token = jedis.get(id);
			if (client_token.equals(server_token)) {
				return true;
			} else { // ��֤ʧ��
				throw new AuthenticationException();
			}
		}
	}

}
