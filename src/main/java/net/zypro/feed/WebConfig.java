package net.zypro.feed;

import net.zypro.feed.interceptor.CharInterceptor;
import net.zypro.feed.interceptor.TokenInterceptor;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {
	private static final Logger logger = Logger.getLogger(WebConfig.class);

	@Override
	public void addInterceptors(InterceptorRegistry registry) { // ÅäÖÃÀ¹½ØÆ÷
		logger.info("BeginRegInterceptor");
		// registry.addInterceptor(new TokenInterceptor()).addPathPatterns("/user/feed/hot","/user/test","/user/logout","/user/love","/user/collection","/user/saveRss","/user/deleteRss","/user/nearby");//ÓÃ»§¶ËµÄÀ¹½ØÆ÷
		// registry.addInterceptor(new SessionInterceptor()).addPathPatterns("/admin/logout","/admin/addRss","/admin/deleteRss","/admin/updateRss","/admin/deleteFeed","/admin/findRss/keyword","/admin/feedback/list","/admin/feedback/status");
		//registry.addInterceptor(new TokenInterceptor()).addPathPatterns("/user/logout","/user/feed/hot","/user/feed/near","/user/feed/collect/list","/user/feed/collect","/user/feed/love","/user/feed/collect/num","/app/feedback/android/add");
		//registry.addInterceptor(new CharInterceptor()).addPathPatterns("/*");
	}
}
