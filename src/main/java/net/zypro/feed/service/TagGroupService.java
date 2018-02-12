package net.zypro.feed.service;

import net.zypro.feed.domain.TagGroup;
import net.zypro.feed.repository.TagGroupRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagGroupService {
	@Autowired
private TagGroupRepository tagGroupRepository;
public void setTagGroupRepository(TagGroupRepository tagGroupRepository) {
	this.tagGroupRepository = tagGroupRepository;
}
public void update(String tag1,String tag2){
	if(tag1!=null&&tag2!=null){
		TagGroup tg1=new TagGroup();
		TagGroup tg2=new TagGroup();
		tg1=tagGroupRepository.findByTag1AndTag2(tag1, tag2);
		tg2=tagGroupRepository.findByTag1AndTag2(tag2, tag1);
		if(tg1==null&&tg2==null){
			TagGroup tg=new TagGroup();
			tg.setTag1(tag1);
			tg.setTag2(tag2);
			tg.setCount(0);
			tagGroupRepository.save(tg);
		}
		else if(tg1==null&&tg2!=null){
			tg2.setCount(tg2.getCount()+1);
			tagGroupRepository.save(tg2);
		}
		else if(tg1!=null&&tg2==null){
			tg1.setCount(tg1.getCount()+1);
			tagGroupRepository.save(tg1);
		}
	}
}
}
