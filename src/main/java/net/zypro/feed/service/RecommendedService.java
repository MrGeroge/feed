package net.zypro.feed.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.zypro.feed.domain.Feed;
import net.zypro.feed.domain.Recommended;
import net.zypro.feed.domain.TagCollection;
import net.zypro.feed.domain.TagGroup;
import net.zypro.feed.domain.User;
import net.zypro.feed.repository.FeedRepository;
import net.zypro.feed.repository.RecommendedRepository;
import net.zypro.feed.repository.TagCollectionRepository;
import net.zypro.feed.repository.TagGroupRepository;
import net.zypro.feed.util.FeedUtil;
import net.zypro.feed.vo.SimpleFeedVO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecommendedService {
@Autowired
private RecommendedRepository recommendedRepository;
@Autowired
private FeedRepository feedRepository;
@Autowired
private TagCollectionRepository tagCollectionRepository;
@Autowired
private TagGroupRepository tagGroupRepository;
public void setRecommendedRepository(RecommendedRepository recommendedRepository) {
	this.recommendedRepository = recommendedRepository;
}
public void setFeedRepository(FeedRepository feedRepository) {
	this.feedRepository = feedRepository;
}
public void setTagCollectionRepository(
		TagCollectionRepository tagCollectionRepository) {
	this.tagCollectionRepository = tagCollectionRepository;
}
public void setTagGroupRepository(TagGroupRepository tagGroupRepository) {
	this.tagGroupRepository = tagGroupRepository;
}
public void saveRecommenedFeeds(User user) throws Exception{   //根据用户个性化推荐，包括用户自己收藏的标签和推荐用户的标签
	List<TagCollection> tagCollections=new ArrayList<TagCollection>();
	List<Feed> feeds=new ArrayList<Feed>();
	List<Feed> feeds1=new ArrayList<Feed>();
	List<Feed> feeds2=new ArrayList<Feed>();
	List<TagGroup> tagGroups=new ArrayList<TagGroup>();
	tagCollections=tagCollectionRepository.findByUser(user);
	for(TagCollection tc:tagCollections){
		String user_tag=tc.getTag().getName();
		tagGroups=tagGroupRepository.findByTag1OrTag2(user_tag, user_tag);
		for(TagGroup tg:tagGroups){
			String tag1=tg.getTag1();
			String tag2=tg.getTag2();
		    if(tag1.equals(user_tag)){
		    	feeds1=FeedUtil.getFeeds(tag2);
		    	feeds.addAll(feeds1);
		    }
		    else if(tag2.equals(user_tag)){
				feeds2=FeedUtil.getFeeds(tag1);
				feeds.addAll(feeds2);
		    }
		}
		    feeds.addAll(FeedUtil.getFeeds(user_tag));
		    for(Feed f:feeds){
		    	f=FeedUtil.feedDeal(f);
		    	Recommended recommended=new Recommended();
		    	f=feedRepository.save(f);
		    	recommended.setUser(user);
		    	recommended.setFeed(f);
		    	recommendedRepository.save(recommended);
		    }
}
}
   //保存了关于user用户的所有推荐信息
public List<SimpleFeedVO> findAll(User user){
	List<SimpleFeedVO> simpleFeedVOs=new ArrayList<SimpleFeedVO>();
	List<Recommended> recommendeds=new ArrayList<Recommended>();
	recommendeds=recommendedRepository.findByUser(user);
	for(Recommended r:recommendeds){
		if(r!=null){
			SimpleFeedVO simpleFeedVO=new SimpleFeedVO();
			simpleFeedVO.id=r.getFeed().getId();
			simpleFeedVO.description=r.getFeed().getDescription();
			simpleFeedVO.link=r.getFeed().getLink();
			simpleFeedVO.pubDate=r.getFeed().getPubDate();
			simpleFeedVO.title=r.getFeed().getTitle();
			simpleFeedVOs.add(simpleFeedVO);
		}
	}
	return simpleFeedVOs;
}
public void deleteRecommendedFeeds(User user){
	List<Recommended> recommendeds=new ArrayList<Recommended>();
	recommendeds=recommendedRepository.findByUser(user);
	for(Recommended r:recommendeds){
		recommendedRepository.delete(r);
	}
}
}
