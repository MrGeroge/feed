package net.zypro.feed.repository;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import net.zypro.feed.Application;
import net.zypro.feed.domain.Tag;

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
public class TagRepositoryTest extends AbstractJUnit4SpringContextTests{
    @Autowired
	private TagRepository tagRepository;
public void setTagRepository(TagRepository tagRepository) {
	this.tagRepository = tagRepository;
}

	@Test
	public void test() {
		List<Tag> tags=new ArrayList<Tag>();
		tags=tagRepository.findTags();
		for(Tag t:tags){
			System.out.println(t.getId());
		}
	}

}
