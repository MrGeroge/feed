package net.zypro.feed.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zypro.feed.controller.BaseController;
import net.zypro.feed.domain.Admin;
import net.zypro.feed.exception.SessionInvalidateException;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class SessionInterceptor extends BaseController implements
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
		// TODO Auto-generated method stub

	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object arg2) throws Exception {
		Admin admin = new Admin();
		admin = (Admin) request.getSession().getAttribute("admin");
		if (admin != null) { // session中任然保存了admin，说明已登陆
			return true;
		} else {
			throw new SessionInvalidateException(); // 抛出session已失效的异常
		}
	}

}
