package net.zypro.feed.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.zypro.feed.domain.Feed;
import net.zypro.feed.domain.Nearby;
import net.zypro.feed.repository.FeedRepository;
import net.zypro.feed.repository.NearbyRepository;
import net.zypro.feed.util.FeedUtil;
import net.zypro.feed.vo.FeedVO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NearbyService {
	@Autowired
	private NearbyRepository nearbyRepository;
	@Autowired
	private FeedRepository feedRepository;

	public void setNearbyRepository(NearbyRepository nearbyRepository) {
		this.nearbyRepository = nearbyRepository;
	}

	public void setFeedRepository(FeedRepository feedRepository) {
		this.feedRepository = feedRepository;
	}

	public void saveNearbyFeeds(String address) throws Exception { // 添加附近新闻
		List<Feed> feeds = new ArrayList<Feed>();
		Feed feed = new Feed();
		feeds = FeedUtil.getNearbyFeeds(address); // 把该地址下的所有新闻保存在新闻表和记录表
		for (Feed f : feeds) {
			f=FeedUtil.feedDeal(f);
			Nearby nearby = new Nearby();
			feed = feedRepository.save(f);
			nearby.setFeed(feed);
			nearby.setAddress(address);
			nearbyRepository.save(nearby);
		}
	}

	public List<FeedVO> findAll(String address) {   //查找此地区的全部新闻
		List<FeedVO> feedVOs = new ArrayList<FeedVO>();
		List<Nearby> nearbys = new ArrayList<Nearby>();
		nearbys = nearbyRepository.findByAddress(address);
		for (Nearby n : nearbys) {
			if (n != null) {
				FeedVO fv = new FeedVO();
				fv.id = n.getFeed().getId();
				fv.link = n.getFeed().getLink();
				fv.pubDate = n.getFeed().getPubDate();
				fv.title = n.getFeed().getTitle();
				fv.description = n.getFeed().getDescription();
				fv.loveNumber = n.getFeed().getLoveNumber();
				fv.collectionNumber = n.getFeed().getCollectionNumber();
				feedVOs.add(fv);
			}
		}
		return feedVOs;
	}

	public void deleteNearbyFeeds(String address) { // 每次更新时将附近表清空，增加新的定位的新闻
		List<Nearby> nearbys=new ArrayList<Nearby>();
		nearbys=nearbyRepository.findByAddress(address);
		for(Nearby n:nearbys){
			nearbyRepository.delete(n);
		}
	}
}
