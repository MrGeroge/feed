package net.zypro.feed.thread;

import net.zypro.feed.service.HotService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class HotThread extends Thread {  //���Æ���ģʽ,��ֻ֤��һ���س�=
@Autowired
private HotService hotService;

public void setHotService(HotService hotService) {
	this.hotService = hotService;
}

@Override
public void run(){
	visitHot();
}
public synchronized void visitHot(){
	hotService.deleteHotFeeds();
	try {
		hotService.saveHotFeeds();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

}
