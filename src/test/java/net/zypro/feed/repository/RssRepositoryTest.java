package net.zypro.feed.repository;

import java.util.ArrayList;
import java.util.List;

import net.zypro.feed.Application;
import net.zypro.feed.domain.Rss;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class RssRepositoryTest extends AbstractJUnit4SpringContextTests {
	@Autowired
	private RssRepository rssRepository;

	public void setRssRepository(RssRepository rssRepository) {
		this.rssRepository = rssRepository;
	}

	@Test
	public void test() {
		List<Rss> rsses = new ArrayList<Rss>();
		rsses = rssRepository.findByNameLike("%baidu%");
		for (Rss r : rsses) {
			System.out.println(r.getName());
			System.out.println(r.getUrl());
		}
	}

}
