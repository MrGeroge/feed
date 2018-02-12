package net.zypro.feed.repository;

import static org.junit.Assert.*;

import java.util.List;

import net.zypro.feed.Application;
import net.zypro.feed.domain.TagGroup;

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
public class TagGroupTest extends AbstractJUnit4SpringContextTests{
@Autowired
private TagGroupRepository tagGroupRepository;

public void setTagGroupRepository(TagGroupRepository tagGroupRepository) {
	this.tagGroupRepository = tagGroupRepository;
}

	@Test
	public void test() {
	    List<TagGroup> list=tagGroupRepository.findByTag1OrTag2("中国","中国");
	    for(TagGroup tg:list){
	    	System.out.println(tg.getTag1()+"----------------"+tg.getTag2());
	    }
	    
	}

}
