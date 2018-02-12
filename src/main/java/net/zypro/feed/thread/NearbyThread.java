package net.zypro.feed.thread;

import net.zypro.feed.service.NearbyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class NearbyThread extends Thread {
private String address;
@Autowired
private NearbyService nearbyService;

public void setNearbyService(NearbyService nearbyService) {
	this.nearbyService = nearbyService;
}
public void setAddress(String address) {
	this.address = address;
}
@Override
public  void run(){
	    visitNearby();
}
public synchronized void visitNearby(){
	nearbyService.deleteNearbyFeeds(address);
	try {
		nearbyService.saveNearbyFeeds(address);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}
