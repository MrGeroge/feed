package net.zypro.feed.service;

import static org.junit.Assert.*;
import net.zypro.feed.Application;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes =Application.class)
@WebAppConfiguration
public class HotServiceTest   extends AbstractJUnit4SpringContextTests {
@Autowired
private HotService hotService;

	public HotService getHotService() {
	return hotService;
}

public void setHotService(HotService hotService) {
	this.hotService = hotService;
}
	@Test
	public void testSaveHot() throws Exception {
		hotService.saveHotFeeds();
	}
   /* @Test
    public void testFindAll() throws Exception{
    	hotService.findAll();
    }
    */
}
