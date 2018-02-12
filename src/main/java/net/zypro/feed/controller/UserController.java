package net.zypro.feed.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zypro.feed.Application;
import net.zypro.feed.vo.FeedListVO;
import net.zypro.feed.vo.FeedVO;
import net.zypro.feed.vo.ObjectVO;
import net.zypro.feed.vo.ResultVO;
import net.zypro.feed.vo.RssListVO;
import net.zypro.feed.vo.SimpleFeedVO;
import net.zypro.feed.vo.TagVO;
import net.zypro.feed.vo.VersionVO;
import net.zypro.feed.domain.Collection;
import net.zypro.feed.domain.Feed;
import net.zypro.feed.domain.Feedback;
import net.zypro.feed.domain.Love;
import net.zypro.feed.domain.Rss;
import net.zypro.feed.domain.RssCollection;
import net.zypro.feed.domain.ShareRecord;
import net.zypro.feed.domain.Tag;
import net.zypro.feed.domain.TagCollection;
import net.zypro.feed.domain.TagGroup;
import net.zypro.feed.domain.User;
import net.zypro.feed.exception.InterfaceChangedNotUsedException;
import net.zypro.feed.repository.CollectionRepository;
import net.zypro.feed.repository.FeedRepository;
import net.zypro.feed.repository.FeedbackRepository;
import net.zypro.feed.repository.LoveRepository;
import net.zypro.feed.repository.RssCollectionRepository;
import net.zypro.feed.repository.RssRepository;
import net.zypro.feed.repository.ShareRecordRepository;
import net.zypro.feed.repository.TagCollectionRepository;
import net.zypro.feed.repository.TagGroupRepository;
import net.zypro.feed.repository.TagRepository;
import net.zypro.feed.repository.UserRepository;
import net.zypro.feed.service.HotService;
import net.zypro.feed.service.NearbyService;
import net.zypro.feed.service.RecommendedService;
import net.zypro.feed.thread.HotThread;
import net.zypro.feed.thread.NearbyThread;
import net.zypro.feed.thread.RecommendedThread;
import net.zypro.feed.util.EncryptUtil;
import net.zypro.feed.util.FeedUtil;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@RestController
public class UserController extends BaseController {
	private static final Logger logger = Logger.getLogger(UserController.class);
	private static JedisPool pool = null; // jedis的一个连接池
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FeedRepository feedRepository;
	@Autowired
	private LoveRepository loveRepository;
	@Autowired
	private CollectionRepository collectionRepository;
	@Autowired
	private RssRepository rssRepository;
	@Autowired
	private RssCollectionRepository rssCollectionRepository;
	@Autowired
	private HotService hotService;
	@Autowired
	private NearbyService nearbyService;
	@Autowired
	private FeedbackRepository feedbackRepository;
	@Autowired
	private HotThread hotThread;
	@Autowired
	private NearbyThread nearbyThread;
	@Autowired
	private TagRepository tagRepository;
	@Autowired
	private TagCollectionRepository tagCollectionRepository;
	@Autowired
	private TagGroupRepository tagGroupRepository;
	@Autowired
	private RecommendedThread  recommendedThread;
	@Autowired
	private RecommendedService recommendedService;
    @Autowired
    private ShareRecordRepository shareRecordRepository;
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void setHotService(HotService hotService) {
		this.hotService = hotService;
	}

	public void setFeedRepository(FeedRepository feedRepository) {
		this.feedRepository = feedRepository;
	}

	public void setLoveRepository(LoveRepository loveRepository) {
		this.loveRepository = loveRepository;
	}

	public void setCollectionRepository(
			CollectionRepository collectionRepository) {
		this.collectionRepository = collectionRepository;
	}

	public void setRssRepository(RssRepository rssRepository) {
		this.rssRepository = rssRepository;
	}

	public void setRssCollectionRepository(
			RssCollectionRepository rssCollectionRepository) {
		this.rssCollectionRepository = rssCollectionRepository;
	}

	public void setNearbyService(NearbyService nearbyService) {
		this.nearbyService = nearbyService;
	}

	public void setFeedbackRepository(FeedbackRepository feedbackRepository) {
		this.feedbackRepository = feedbackRepository;
	}
	public void setHotThread(HotThread hotThread) {
		this.hotThread = hotThread;
	}
	public void setNearbyThread(NearbyThread nearbyThread) {
		this.nearbyThread = nearbyThread;
	}
	public void setTagRepository(TagRepository tagRepository) {
		this.tagRepository = tagRepository;
	}
	public void setTagCollectionRepository(
			TagCollectionRepository tagCollectionRepository) {
		this.tagCollectionRepository = tagCollectionRepository;
	}
	public void setTagGroupRepository(TagGroupRepository tagGroupRepository) {
		this.tagGroupRepository = tagGroupRepository;
	}
	public void setRecommendedThread(RecommendedThread recommendedThread) {
		this.recommendedThread = recommendedThread;
	}
	public void setRecommendedService(RecommendedService recommendedService) {
		this.recommendedService = recommendedService;
	}
	public void setShareRecordRepository(ShareRecordRepository shareRecordRepository) {
		this.shareRecordRepository = shareRecordRepository;
	}

	// 自动装配各种Dao,需要set方法
	@RequestMapping("/user/login")
	// 用户名不存在，需要添加到数据库
	public Object login_register(
			@RequestParam(value = "username", required = true) String username,
			@RequestParam(value = "source", required = true) String source,
			@RequestParam(value = "nickname", required = true) String nickname,
			@RequestParam(value = "address", required = false) String address,
			@RequestParam(value = "sex", required = false) String sex,
			@RequestParam(value = "age", required = false) Integer age,
			@RequestParam(value="avatar_url",required=false) String avatar_url )
			
			throws Exception { // 通过数据库查找此用户，若不存在则添加用户信息进数据库；若存在则不作处理，返回信息用户已注册;
		User user = new User();
		ResultVO rv = new ResultVO();
		Jedis jedis = null;
		int user_id = 0;
		user = userRepository.findByUsername(username); // 查看此用户是否存在
		if (user != null) { // 注册失败（存在此用户）,直接登录
			if (address != null) {
				user.setAddress(address);
			} else {

			}
			if (sex != null) {
				user.setSex(sex);
			} else {

			}
			if (age != null) {
				user.setAge(age);
			} else {

			}
			if (source != null) {
				user.setSource(source);
			} else {

			}
			if (nickname != null) {
				user.setNickname(nickname);
			} else {

			}
			if(avatar_url != null){
				user.setAvatar(avatar_url);
			} else{
				
			}
			userRepository.save(user);
			user_id = user.getId();
		} else {// 此用户不存在，可以直接注册,添加此用户
			User u = new User();
			if (address != null) {
				u.setAddress(address);
			} else {
				u.setAddress("null");
			}
			if (sex != null) {
				u.setSex(sex);
			} else {
				u.setSex("null");
			}
			if (age != null) {
				u.setAge(age);
			} else {
				u.setAge(0);
			}
			if (source != null) {
				u.setSource(source);
			} else {
				u.setSource("null");
			}
			if (nickname != null) {
				u.setNickname(nickname);
			} else {
				u.setNickname("null");
			}
			if(avatar_url != null){
				u.setAvatar(avatar_url);
			} else{
				u.setAvatar("null");
			}
			u.setUsername(username);
			User u1 = userRepository.save(u);
			user_id = u1.getId();
		}
		rv.id = user_id;
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMDDhhmmss");
		rv.token = EncryptUtil.md5(String.valueOf(rv.id)
				+ sdf.format(new Date()));
		jedis = getPool().getResource();
		jedis.set("" + rv.id, rv.token);
		return rv;
	}

	@RequestMapping("/user/logout")
	// 需要token验证
	public void logout(@RequestParam("userid") String id,
			@RequestParam("token") String token) throws Exception {
		Jedis jedis = null;
		jedis = getPool().getResource();
		jedis.set(id, "null");
	}

	@RequestMapping("/tag/random")
	public void test() throws InterfaceChangedNotUsedException { // 接口改变情况
		throw new InterfaceChangedNotUsedException();
	}

	@RequestMapping("/user/feed/hot")  //需要在feed表中添加一个isLoved，isCollected
	// 需要增加token拦截器验证,显示点赞信息，收藏信息，
	public List<FeedVO> hotByUser(@RequestParam("userid")int user_id,HttpServletRequest request,
			HttpServletResponse response) throws Exception { // 返回热点相关的所有新闻,用户登陆需要进行登陆token验证
		Date now = new Date();
		Date before = new Date(); // 初始化
		List<FeedVO> feedVOs = new ArrayList<FeedVO>();
		before = (Date) request.getServletContext().getAttribute("before");
		//if ((before == null)&&(hotThread.isAlive()==false)) { // 第一次访问
		if(before==null){
			hotThread.start(); 
			before = new Date();
			request.getServletContext().setAttribute("before", before); // 保留当前更新时间
		}
		long hour = timeDifference(now, before);
		//if ((hour >= 1)&&(hotThread.isAlive()==false)) { // 则说明需要从网络更新，且需要清空feed表和hot表,重新添加feed和hot,放入线
			if(hour>=1){
				hotThread.start(); 
			Date date = new Date();
			request.getServletContext().setAttribute("before", date);
		}
	    feedVOs = hotService.findAll(); 
	    /*  List<Love> loves=new ArrayList<Love>();
		    List<Collection> collections=new ArrayList<Collection>();
		 User user=new User();
		user=userRepository.findById(user_id);
		loves=loveRepository.findByUser(user);
		collections=collectionRepository.findByUser(user);
		
		for(Love l:loves){
			for(FeedVO fvo:feedVOs){
				if(l.getFeed().getId()==fvo.id){  //说明此新闻已被此用户点赞
					fvo.isLoved=true;
				}
				else{
					fvo.isLoved=false;
				}
			}
		}
		for(Collection c:collections){
			for(FeedVO fvo:feedVOs){
				if(c.getFeed().getId()==fvo.id){
					fvo.isCollected=true;
				}
				else{
					fvo.isCollected=false;
				}
			}
		}
		 
		 */
		return feedVOs;
	}

	@RequestMapping("/feed/hot")
	// 不需要获取点赞数和收藏数,点赞状态和收藏状态
	public List<SimpleFeedVO> hotByVisitor(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Date now = new Date();
		Date before = new Date(); // 初始化
		List<FeedVO> feedVOs = new ArrayList<FeedVO>();
		before = (Date) request.getServletContext().getAttribute("before");
		//if ((before == null)&&(hotThread.isAlive()==false)) { // 第一次访问
		if(before==null){
			hotThread.start(); 
			before = new Date();
			request.getServletContext().setAttribute("before", before); // 保留当前更新时间
		}
		long hour = timeDifference(now, before);
		//if ((hour >= 1)&&(hotThread.isAlive()==false)) { // 则说明需要从网络更新，且需要清空feed表和hot表,重新添加feed和hot,放入线程
		if(hour>=1){
			hotThread.start(); 
			Date date = new Date();
			request.getServletContext().setAttribute("before", date);
		}
		feedVOs = hotService.findAll(); // 无论如何都会从hot表中取数据，提前加好数据
		List<SimpleFeedVO> simpleFeedVOs = new ArrayList<SimpleFeedVO>();
		for (FeedVO fvo : feedVOs) {
			SimpleFeedVO sfvo = new SimpleFeedVO();
			sfvo.id = fvo.id;
			sfvo.link = fvo.link;
			sfvo.pubDate = fvo.pubDate;
			sfvo.title = fvo.title;
			sfvo.description = fvo.description;
			simpleFeedVOs.add(sfvo);
		}
		return simpleFeedVOs;
	}

	@RequestMapping("/user/feed/near")
	public List<FeedVO> nearbyUser(@RequestParam("city") String city,@RequestParam("userid")int user_id,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {// 当用户的位置信息改变或者第一次访问时，更新将记录表删除后在添加
		String address=URLDecoder.decode(city, "utf-8");   //當前定位
		Date locationTime = new Date(); // 旧的定位時間
		Date now = new Date();   //當前時間
		List<FeedVO> feedVOs = new ArrayList<FeedVO>();
		locationTime = (Date) request.getServletContext().getAttribute(address);   //得到當前時間戳
		//if ((locationTime == null)&&(nearbyThread.isAlive()==false)) { // 说明是第一次登陆
			if(locationTime==null){
		    nearbyThread.setAddress(address);
		    nearbyThread.start();
			locationTime=new Date();
			request.getServletContext().setAttribute(address, locationTime); // 第一次默认的地址的時間

		}
		long hour = timeDifference(now, locationTime);
		//if ((hour >=1)&&(nearbyThread.isAlive()==false)) { // 则说明需要从网络更新，且需要清空feed表和hot表,重新添加feed和hot,放入线程
			if(hour>=1){
		    nearbyThread.setAddress(address);
		    nearbyThread.start(); // 执行完了一个用户的更新操作
			Date current = new Date();
			request.getServletContext().setAttribute(address, current);
		}
	    feedVOs = nearbyService.findAll(address);
		/*List<Love> loves=new ArrayList<Love>();
		List<Collection> collections=new ArrayList<Collection>();
		User user=new User();
		user=userRepository.findById(user_id);
		loves=loveRepository.findByUser(user);
		collections=collectionRepository.findByUser(user);
		 
		for(Love l:loves){
			for(FeedVO fvo:feedVOs){
				if(l.getFeed().getId()==fvo.id){  //说明此新闻已被此用户点赞
					fvo.isLoved=true;
				}
				else{
					fvo.isLoved=false;
				}
			}
		}
		for(Collection c:collections){
			for(FeedVO fvo:feedVOs){
				if(c.getFeed().getId()==fvo.id){
					fvo.isCollected=true;
				}else{
					fvo.isCollected=false;
				}
			}
		}
		
		*/
		return feedVOs;

	}

	@RequestMapping("/feed/near")
	public List<SimpleFeedVO> nearbyVisitor(
			@RequestParam("city") String city,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String address=URLDecoder.decode(city, "utf-8");   //當前定位
		Date locationTime = new Date(); // 旧的定位時間
		Date now = new Date();   //當前時間
		List<FeedVO> feedVOs = new ArrayList<FeedVO>();
		locationTime = (Date) request.getServletContext().getAttribute(address);   //得到當前時間戳
		//if ((locationTime == null)&&(nearbyThread.isAlive()==false)) { // 说明是第一次登陆
			if(locationTime==null){
		    nearbyThread.setAddress(address);
		    nearbyThread.start();
			locationTime=new Date();
			request.getServletContext().setAttribute(address, locationTime); // 第一次默认的地址的時間

		}
		long hour = timeDifference(now, locationTime);
		//if ((hour >= 1)&&(nearbyThread.isAlive()==false)) { // 则说明需要从网络更新，且需要清空feed表和hot表,重新添加feed和hot,放入线程
			if(hour>=1){
		    nearbyThread.setAddress(address);
		    nearbyThread.start(); // 执行完了一个用户的更新操作
			Date current = new Date();
			request.getServletContext().setAttribute(address, current);
		}
		feedVOs = nearbyService.findAll(address); // 提前加好数据
		List<SimpleFeedVO> simpleFeedVOs = new ArrayList<SimpleFeedVO>();
		for (FeedVO fvo : feedVOs) {
			SimpleFeedVO sfvo = new SimpleFeedVO();
			sfvo.id = fvo.id;
			sfvo.link = fvo.link;
			sfvo.pubDate = fvo.pubDate;
			sfvo.title = fvo.title;
			sfvo.description = fvo.description;
			simpleFeedVOs.add(sfvo);
		}
		return simpleFeedVOs;
	}

	@RequestMapping("/user/feed/love")   //此feed的isLoved 为已点赞,点赞不需要显示列表
	public String love(@RequestParam("userid") int user_id,
			@RequestParam("feedid") long feed_id) throws Exception {
		String result=new String();
		Feed feed = new Feed();
		User user = new User();
		Love love = new Love();
		//List<FeedVO> feedVOs = new ArrayList<FeedVO>();
		if (feed_id > 0) { // 传入的新闻有效
			user = userRepository.findById(user_id);
			feed = feedRepository.findById(feed_id);
			int loveNumber = feed.getLoveNumber(); // 此时保存在数据库中的点赞数
			try {
				love = loveRepository.findByUserAndFeed(user, feed); // 确实找到了此点赞信息，确实删除了
				System.out.println(love.getId());
				if (love != null) { // 则用户已对此新闻点过赞，取消点赞
					loveRepository.delete(love);
					feed.setLoveNumber(loveNumber - 1);
					feedRepository.save(feed);
				}
			} catch (NullPointerException npe) {
				love = new Love();
				love.setUser(user);
				love.setFeed(feed);
				loveRepository.save(love); // 点赞成功
				feed.setLoveNumber(loveNumber + 1);
				feedRepository.save(feed);
			}
		}
		result="success";
		return result; // 获取热点新闻列表，包括点赞量，收藏量,所有新闻的点赞状态，收藏状态
	}

	/*@RequestMapping("/user/feed/collect")
	public String collection(@RequestParam("userid") int user_id,
			@RequestParam("feedid") int feed_id) throws Exception {
		String result=new String();
		Feed feed = new Feed();
		User user = new User();
		Collection collection = new Collection();
		if (feed_id > 0) {
			user = userRepository.findById(user_id);
			feed = feedRepository.findById(feed_id);
			int collectionNumber = feed.getCollectionNumber();
			try {
				collection = collectionRepository.findByUserAndFeed(user, feed);
				System.out.println(collection.getId());
				if (collection != null) { // 说明之前已经收藏过，则取消收藏
					collectionRepository.delete(collection);
					System.out.println(feed.getCollectionNumber());
					feed.setCollectionNumber(collectionNumber - 1);
					feedRepository.save(feed);
				}
			} catch (NullPointerException npe) {
				collection = new Collection();
				collection.setUser(user);
				collection.setFeed(feed);
				collectionRepository.save(collection); // 增加收藏记录
				feed.setCollectionNumber(collectionNumber + 1);
				feedRepository.save(feed);

			}
		}
	    result="success";
		return result; // 获取热点新闻列表，包括点赞量，收藏量
	}*/

	@RequestMapping("/user/saveRss")
	// 订阅rss源
	public Object saveRss(@RequestParam("userid") int user_id,
			@RequestParam("rssid") int rss_id) { // rss源订阅
		Rss rss = new Rss();
		User user = new User();
		RssCollection rssCollection = new RssCollection();
		ObjectVO objectVO = new ObjectVO();
		if (rss_id > 0) { // rss源可以多次被订阅，因此
			user = userRepository.findById(user_id);
			rss = rssRepository.findById(rss_id);
			rssCollection.setUser(user);
			rssCollection.setRss(rss);
			rssCollectionRepository.save(rssCollection); // 产生一条用户订阅rss信息
			objectVO.status = "success";
			objectVO.msg = "RSS源订阅成功";
		} else {
			objectVO.status = "failed";
			objectVO.msg = "RSS源订阅失败";
		}
		return objectVO;
	}

	@RequestMapping("/user/deleteRss")
	// 取消rss源订阅
	public Object deleteRss(
			@RequestParam("rssCollection_id") int rssCollection_id) {
		ObjectVO objectVO = new ObjectVO();
		if (rssCollection_id > 0) { // 有效
			rssCollectionRepository.delete(rssCollection_id);
			objectVO.status = "success";
			objectVO.msg = "RSS源订阅已取消";
		} else {
			objectVO.status = "failed";
			objectVO.msg = "RSS源订阅取消失败";
		}
		return objectVO;
	}

	@RequestMapping("/user/rss/list")
	// 用户收藏的rss源列表
	public List<RssListVO> getRssList(@RequestParam("userid") int user_id) {
		User user = new User();
		Rss rss = new Rss();
		List<RssListVO> rssListVOs = new ArrayList<RssListVO>();
		List<RssCollection> rssCollections = new ArrayList<RssCollection>();
		user = userRepository.findById(user_id);
		rssCollections = rssCollectionRepository.findByUser(user);
		if (rssCollections != null) {
			for (RssCollection rc : rssCollections) {
				RssListVO rssListVO = new RssListVO();
				rss = rc.getRss();
				rssListVO.name = rss.getName();
				rssListVO.url = rss.getUrl();
				rssListVOs.add(rssListVO);
			}
		}
		return rssListVOs;
	}

	/*@RequestMapping("/user/feed/collect/list")
	public List<FeedListVO> getFeedList(@RequestParam("userid") int user_id) {
		User user = new User();
		Feed feed = new Feed();
		List<FeedListVO> feedListVOs = new ArrayList<FeedListVO>();
		List<Collection> collections = new ArrayList<Collection>();
		user = userRepository.findById(user_id);
		collections = collectionRepository.findByUser(user);
		if (collections != null) {
			for (Collection c : collections) {
				FeedListVO feedListVO = new FeedListVO();
				feed = c.getFeed();
				feedListVO.link = feed.getLink();
				feedListVO.pubDate = feed.getPubDate();
				feedListVO.title = feed.getTitle();
				feedListVO.description = feed.getDescription();
				feedListVO.loveNumber = feed.getLoveNumber();
				feedListVO.collectionNumber = feed.getCollectionNumber();
				feedListVOs.add(feedListVO);
			}
		}
		return feedListVOs;
	}*/
	/*@RequestMapping("/user/feed/collect/num")
	public 	String collectNum(@RequestParam("userid")int user_id){
		String data=new String();
		User user=new User();
		List<Collection> collections=new ArrayList<Collection>();
		user=userRepository.findById(user_id);
		collections=collectionRepository.findByUser(user);
		data=""+collections.size();
		return data;
	}*/
	@RequestMapping("/user/tag/add")
	public Object addTag(@RequestParam("userid")int user_id,@RequestParam("tag")String name,@RequestParam("tag_id")int tag_id) throws Exception{
		String result="";
		Tag tag=new Tag();     //tag
		User user=new User();
		TagCollection tagCollection=new TagCollection();
		TagCollection tc=new TagCollection();
		user=userRepository.findById(user_id);
		if(name!=null){
	    name=URLDecoder.decode(name, "UTF-8");
		if(tag_id>0){  //则说明有标签过来
			tag=tagRepository.findById(tag_id);
		}
		else{   //则说明个性化标签，可能存在同名标签，若存在同名则不添加，否则添加数据库
			tag=tagRepository.findByName(name);
			if(tag==null){  //说明数据库中不存在此标签
				Tag t=new Tag();
				t.setName(name);
				tag=tagRepository.save(t);
			}else{   //说明数据库中存在此标签	
			}	
		}
		tc=tagCollectionRepository.findByUserAndTag(user, tag);
		if(tc==null){
		tagCollection.setTag(tag);
		tagCollection.setUser(user);
		tagCollectionRepository.save(tagCollection);
		}
		else{
			
		}
		}
		result="success";
		return result;
	}
	@RequestMapping("/user/tag/delete")
    public Object deleteTag(@RequestParam("userid")int user_id,@RequestParam("tag_id")int tag_id){
		String result="";
		User user=new User();
		Tag tag=new Tag();
		TagCollection tagCollection=new TagCollection();
		user=userRepository.findById(user_id);
		tag=tagRepository.findById(tag_id);
		tagCollection=tagCollectionRepository.findByUserAndTag(user, tag);
		tagCollectionRepository.delete(tagCollection);
		result="success";
		return result;
	}
	@RequestMapping("/user/tag/list")
	public List<TagVO> listTags(@RequestParam("userid")int user_id){
		User user=new User();
		List<TagCollection> tagCollections=new ArrayList<TagCollection>();
		List<TagVO> tagVOs=new ArrayList<TagVO>();
		user=userRepository.findById(user_id);
		tagCollections=tagCollectionRepository.findByUser(user);
		if(tagCollections!=null){
		for(TagCollection tc:tagCollections){
			TagVO tagVO=new TagVO();
			tagVO.id=tc.getTag().getId();
			tagVO.tag=tc.getTag().getName();
			tagVOs.add(tagVO);
		}
		}
		return tagVOs;
	}
	@RequestMapping("/user/tag/random")
	public List<TagVO> randomTags(@RequestParam("userid")int user_id){   //判断是否为自己的标签，若为自己的标签则忽略
		List<Tag> tags=new ArrayList<Tag>();        //数据库中的所有标签
		List<Tag> user_tags=new ArrayList<Tag>();   //自己的标签
		List<TagVO> tagVOs=new ArrayList<TagVO>();
	    List<TagCollection> tagCollections=new ArrayList<TagCollection>();
		Tag tag=new Tag();
		User user=new User();
		user=userRepository.findById(user_id);
		tags=tagRepository.findTags();  //随机产生的排序,所有标签
		tagCollections=tagCollectionRepository.findByUser(user);  //此用户自己的标签
		if(tagCollections.size()>0){
		for(TagCollection tg:tagCollections){
			tag=tg.getTag();
			user_tags.add(tag);
		}
		    int index=0;
			for(Tag t:tags){   //遍历所有标签
				if(index<8){
				if(user_tags.contains(t)==false){  //用户标签收藏中无此标签
					index++;
					TagVO tagVO=new TagVO();
					tagVO.id=t.getId();
					tagVO.tag=t.getName();
					tagVOs.add(tagVO);
					
				}
				else{
					continue;
				}
				}
				else{
					break;
			}
			}
		}
		else{  //没有用户标签
			for(int i=0;i<8;i++){
			    TagVO tagVO=new TagVO();
				Tag t=tags.get(i);
				tagVO.id=t.getId();
				tagVO.tag=t.getName();
				tagVOs.add(tagVO);
			}
			
		}
		return tagVOs;
	}
	@RequestMapping("/app/feedback/android/add")
	public Object feedback(@RequestParam("contact") String contact,
			@RequestParam("content") String content,@RequestParam("app_version") String appVersion) throws Exception { // 反馈接口
		Feedback feedback = new Feedback();
		String result="";
		content=URLDecoder.decode(content,"UTF-8");
		contact=URLDecoder.decode(contact,"UTF-8");
		if ((contact == null) || (contact.equals("")) || (content == null)
				|| (content.equals("") ||(appVersion == null) || (appVersion.equals("")))) {
			throw new NullPointerException();
		}
		if ((contact != null) && (content != null)&&(appVersion != null)) {
			feedback.setContact(contact);
			feedback.setContent(content);
			feedback.setAppVersion(appVersion);
			feedback.setStatus("no");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
			Date date = new Date();
			String time = sdf.format(date);
			feedback.setCreateTime(time);
			feedbackRepository.save(feedback);
		}
		result="success";
		return result;
	}
	@RequestMapping("/app/version/android/latest")
	public Object version() throws Exception{
		String version=parseVersionXmlToId();
		VersionVO versionVO=new VersionVO();
		versionVO.data=version;
		return versionVO;
	}
	@RequestMapping("/user/feed/recommand")
	public List<SimpleFeedVO> recommand(@RequestParam("userid")int user_id,HttpServletRequest request) throws Exception{
		User user=new User();
		List<SimpleFeedVO> simpleFeedVOs=new ArrayList<SimpleFeedVO>();
		user=userRepository.findById(user_id);
		Date reTime = new Date(); // 旧的推荐時間
		Date now = new Date();   //當前時間
		reTime=(Date) request.getServletContext().getAttribute(user.getUsername()); //用户名唯一
		//if((reTime==null)&&(recommendedThread.isAlive()==false)){
			if(reTime==null){
		    recommendedThread.setUser(user);
		    recommendedThread.start();
			reTime=new Date();
			request.getServletContext().setAttribute(user.getUsername(), reTime);
		}
		long hour = timeDifference(now, reTime);
		//if((hour>=1)&&(recommendedThread.isAlive()==false)){
			if(hour>=1){
		    recommendedThread.setUser(user);
		    recommendedThread.start();
			Date current = new Date();
			request.getServletContext().setAttribute(user.getUsername(),current);
		}
		simpleFeedVOs=recommendedService.findAll(user);
		return simpleFeedVOs;
	}
	@RequestMapping("/user/feed/share")
	public Object share(@RequestParam("userid")int user_id,@RequestParam("feedid")long feed_id){
		User user=new User();
		Feed feed=new Feed();
		ShareRecord shareRecord=new ShareRecord();
		user=userRepository.findById(user_id);
		feed=feedRepository.findById(feed_id);
		shareRecord.setUser(user);
		shareRecord.setFeed(feed);
		shareRecordRepository.save(shareRecord);
		String result="success";
		return result;
	}

	public int timeDifference(Date now, Date begin) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date1 = sdf.format(now);
		String date2 = sdf.format(begin);
		Date time1 = sdf.parse(date1);
		Date time2 = sdf.parse(date2);
		DateTime dt1 = new DateTime(time1);
		DateTime dt2 = new DateTime(time2);
		int hour = Hours.hoursBetween(dt2, dt1).getHours();
		return hour;
	}

	public static JedisPool getPool() throws Exception {
		if (pool == null) {
			JedisPoolConfig config = new JedisPoolConfig();
			// 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
			// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
			config.setMaxActive(500);
			// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
			config.setMaxIdle(5);
			// 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
			config.setMaxWait(1000 * 100);
			// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
			config.setTestOnBorrow(true);
			pool = new JedisPool(config, parseApplicationXmlToAddress(), 6379); // 根据本机ip来改变
		}
		return pool;
	}

	private static String parseApplicationXmlToAddress() throws Exception {
		SAXBuilder builder = new SAXBuilder(); // 创建一个xml解析
		Document document = builder.build(Application.class.getClassLoader()
				.getResourceAsStream("application.xml"));// 获得xml文档节点
		Element e = document.getRootElement(); // 得到app
		String address = e.getAttributeValue("address");
		return address;
	}
	private static String parseVersionXmlToId() throws Exception{
		SAXBuilder builder = new SAXBuilder(); // 创建一个xml解析
		Document document = builder.build(Application.class.getClassLoader()
				.getResourceAsStream("version.xml"));// 获得xml文档节点
		Element e = document.getRootElement();
		String id=e.getAttributeValue("id");
		return id;
	}
}