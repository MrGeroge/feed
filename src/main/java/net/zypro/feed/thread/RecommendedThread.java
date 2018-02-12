package net.zypro.feed.thread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.zypro.feed.domain.User;
import net.zypro.feed.service.RecommendedService;

@Component
@Scope("prototype")
public class RecommendedThread extends Thread{
	@Autowired
	private RecommendedService recommendedService;
	private User user;
	public void setRecommendedService(RecommendedService recommendedService) {
		this.recommendedService = recommendedService;
	}
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public  void run(){
			visitRecommended();
	}
	public synchronized void visitRecommended(){
		recommendedService.deleteRecommendedFeeds(user);
		try {
			recommendedService.saveRecommenedFeeds(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	}
