package net.zypro.feed.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zypro.feed.domain.Feed;
import net.zypro.feed.domain.Hot;
import net.zypro.feed.repository.FeedRepository;
import net.zypro.feed.repository.HotRepository;
import net.zypro.feed.util.FeedUtil;
import net.zypro.feed.util.KeyWordUtil;
import net.zypro.feed.vo.FeedVO;

@Service
public class HotService {
	@Autowired
	private HotRepository hotRepository;
	@Autowired
	private FeedRepository feedRepository;

	public HotRepository getHotRepository() {
		return hotRepository;
	}

	public void setHotRepository(HotRepository hotRepository) {
		this.hotRepository = hotRepository;
	}

	public FeedRepository getFeedRepository() {
		return feedRepository;
	}

	public void setFeedRepository(FeedRepository feedRepository) {
		this.feedRepository = feedRepository;
	}

	public  void saveHotFeeds() throws Exception { // 保存热点新闻
		List<String> words = new ArrayList<String>();
		List<Feed> feeds = new ArrayList<Feed>();
		words = KeyWordUtil.getKeyWords(); // 找到所有关键字
		for (String word : words) {
			feeds = FeedUtil.getFeeds(word);

			int index = 0;
			Feed fe = new Feed();
			for (Feed f : feeds) {
				f=FeedUtil.feedDeal(f);
				index++;
				fe = feedRepository.save(f); // 将新闻保存到feed表
				if (index == 1) {
					Hot hot = new Hot();
					hot.setFeed(fe);
					hot = hotRepository.save(hot);
				}
			}
		}
	}

	public List<FeedVO> findAll() throws Exception { // 查找所有热点
		List<FeedVO> feedVOs = new ArrayList<FeedVO>();
		List<Hot> hots = new ArrayList<Hot>();
		hots = hotRepository.findAll();
		for (Hot h : hots) {
			if (h != null) {
				FeedVO fv = new FeedVO();
				fv.id = h.getFeed().getId();
				fv.link = h.getFeed().getLink();
				fv.pubDate = h.getFeed().getPubDate();
				fv.title = h.getFeed().getTitle();
				fv.description = h.getFeed().getDescription();
				fv.loveNumber = h.getFeed().getLoveNumber();
				fv.collectionNumber = h.getFeed().getCollectionNumber();
				feedVOs.add(fv);
			}
		}
		return feedVOs;
	}

	public  void deleteHotFeeds() {
		hotRepository.deleteAll();
	}
}
