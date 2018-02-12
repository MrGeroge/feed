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
	private static JedisPool pool = null; // jedis��һ�����ӳ�
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

	// �Զ�װ�����Dao,��Ҫset����
	@RequestMapping("/user/login")
	// �û��������ڣ���Ҫ��ӵ����ݿ�
	public Object login_register(
			@RequestParam(value = "username", required = true) String username,
			@RequestParam(value = "source", required = true) String source,
			@RequestParam(value = "nickname", required = true) String nickname,
			@RequestParam(value = "address", required = false) String address,
			@RequestParam(value = "sex", required = false) String sex,
			@RequestParam(value = "age", required = false) Integer age,
			@RequestParam(value="avatar_url",required=false) String avatar_url )
			
			throws Exception { // ͨ�����ݿ���Ҵ��û�����������������û���Ϣ�����ݿ⣻��������������������Ϣ�û���ע��;
		User user = new User();
		ResultVO rv = new ResultVO();
		Jedis jedis = null;
		int user_id = 0;
		user = userRepository.findByUsername(username); // �鿴���û��Ƿ����
		if (user != null) { // ע��ʧ�ܣ����ڴ��û���,ֱ�ӵ�¼
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
		} else {// ���û������ڣ�����ֱ��ע��,��Ӵ��û�
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
	// ��Ҫtoken��֤
	public void logout(@RequestParam("userid") String id,
			@RequestParam("token") String token) throws Exception {
		Jedis jedis = null;
		jedis = getPool().getResource();
		jedis.set(id, "null");
	}

	@RequestMapping("/tag/random")
	public void test() throws InterfaceChangedNotUsedException { // �ӿڸı����
		throw new InterfaceChangedNotUsedException();
	}

	@RequestMapping("/user/feed/hot")  //��Ҫ��feed�������һ��isLoved��isCollected
	// ��Ҫ����token��������֤,��ʾ������Ϣ���ղ���Ϣ��
	public List<FeedVO> hotByUser(@RequestParam("userid")int user_id,HttpServletRequest request,
			HttpServletResponse response) throws Exception { // �����ȵ���ص���������,�û���½��Ҫ���е�½token��֤
		Date now = new Date();
		Date before = new Date(); // ��ʼ��
		List<FeedVO> feedVOs = new ArrayList<FeedVO>();
		before = (Date) request.getServletContext().getAttribute("before");
		//if ((before == null)&&(hotThread.isAlive()==false)) { // ��һ�η���
		if(before==null){
			hotThread.start(); 
			before = new Date();
			request.getServletContext().setAttribute("before", before); // ������ǰ����ʱ��
		}
		long hour = timeDifference(now, before);
		//if ((hour >= 1)&&(hotThread.isAlive()==false)) { // ��˵����Ҫ��������£�����Ҫ���feed���hot��,�������feed��hot,������
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
				if(l.getFeed().getId()==fvo.id){  //˵���������ѱ����û�����
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
	// ����Ҫ��ȡ���������ղ���,����״̬���ղ�״̬
	public List<SimpleFeedVO> hotByVisitor(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Date now = new Date();
		Date before = new Date(); // ��ʼ��
		List<FeedVO> feedVOs = new ArrayList<FeedVO>();
		before = (Date) request.getServletContext().getAttribute("before");
		//if ((before == null)&&(hotThread.isAlive()==false)) { // ��һ�η���
		if(before==null){
			hotThread.start(); 
			before = new Date();
			request.getServletContext().setAttribute("before", before); // ������ǰ����ʱ��
		}
		long hour = timeDifference(now, before);
		//if ((hour >= 1)&&(hotThread.isAlive()==false)) { // ��˵����Ҫ��������£�����Ҫ���feed���hot��,�������feed��hot,�����߳�
		if(hour>=1){
			hotThread.start(); 
			Date date = new Date();
			request.getServletContext().setAttribute("before", date);
		}
		feedVOs = hotService.findAll(); // ������ζ����hot����ȡ���ݣ���ǰ�Ӻ�����
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
			throws Exception {// ���û���λ����Ϣ�ı���ߵ�һ�η���ʱ�����½���¼��ɾ���������
		String address=URLDecoder.decode(city, "utf-8");   //��ǰ��λ
		Date locationTime = new Date(); // �ɵĶ�λ�r�g
		Date now = new Date();   //��ǰ�r�g
		List<FeedVO> feedVOs = new ArrayList<FeedVO>();
		locationTime = (Date) request.getServletContext().getAttribute(address);   //�õ���ǰ�r�g��
		//if ((locationTime == null)&&(nearbyThread.isAlive()==false)) { // ˵���ǵ�һ�ε�½
			if(locationTime==null){
		    nearbyThread.setAddress(address);
		    nearbyThread.start();
			locationTime=new Date();
			request.getServletContext().setAttribute(address, locationTime); // ��һ��Ĭ�ϵĵ�ַ�ĕr�g

		}
		long hour = timeDifference(now, locationTime);
		//if ((hour >=1)&&(nearbyThread.isAlive()==false)) { // ��˵����Ҫ��������£�����Ҫ���feed���hot��,�������feed��hot,�����߳�
			if(hour>=1){
		    nearbyThread.setAddress(address);
		    nearbyThread.start(); // ִ������һ���û��ĸ��²���
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
				if(l.getFeed().getId()==fvo.id){  //˵���������ѱ����û�����
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
		String address=URLDecoder.decode(city, "utf-8");   //��ǰ��λ
		Date locationTime = new Date(); // �ɵĶ�λ�r�g
		Date now = new Date();   //��ǰ�r�g
		List<FeedVO> feedVOs = new ArrayList<FeedVO>();
		locationTime = (Date) request.getServletContext().getAttribute(address);   //�õ���ǰ�r�g��
		//if ((locationTime == null)&&(nearbyThread.isAlive()==false)) { // ˵���ǵ�һ�ε�½
			if(locationTime==null){
		    nearbyThread.setAddress(address);
		    nearbyThread.start();
			locationTime=new Date();
			request.getServletContext().setAttribute(address, locationTime); // ��һ��Ĭ�ϵĵ�ַ�ĕr�g

		}
		long hour = timeDifference(now, locationTime);
		//if ((hour >= 1)&&(nearbyThread.isAlive()==false)) { // ��˵����Ҫ��������£�����Ҫ���feed���hot��,�������feed��hot,�����߳�
			if(hour>=1){
		    nearbyThread.setAddress(address);
		    nearbyThread.start(); // ִ������һ���û��ĸ��²���
			Date current = new Date();
			request.getServletContext().setAttribute(address, current);
		}
		feedVOs = nearbyService.findAll(address); // ��ǰ�Ӻ�����
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

	@RequestMapping("/user/feed/love")   //��feed��isLoved Ϊ�ѵ���,���޲���Ҫ��ʾ�б�
	public String love(@RequestParam("userid") int user_id,
			@RequestParam("feedid") long feed_id) throws Exception {
		String result=new String();
		Feed feed = new Feed();
		User user = new User();
		Love love = new Love();
		//List<FeedVO> feedVOs = new ArrayList<FeedVO>();
		if (feed_id > 0) { // �����������Ч
			user = userRepository.findById(user_id);
			feed = feedRepository.findById(feed_id);
			int loveNumber = feed.getLoveNumber(); // ��ʱ���������ݿ��еĵ�����
			try {
				love = loveRepository.findByUserAndFeed(user, feed); // ȷʵ�ҵ��˴˵�����Ϣ��ȷʵɾ����
				System.out.println(love.getId());
				if (love != null) { // ���û��ѶԴ����ŵ���ޣ�ȡ������
					loveRepository.delete(love);
					feed.setLoveNumber(loveNumber - 1);
					feedRepository.save(feed);
				}
			} catch (NullPointerException npe) {
				love = new Love();
				love.setUser(user);
				love.setFeed(feed);
				loveRepository.save(love); // ���޳ɹ�
				feed.setLoveNumber(loveNumber + 1);
				feedRepository.save(feed);
			}
		}
		result="success";
		return result; // ��ȡ�ȵ������б��������������ղ���,�������ŵĵ���״̬���ղ�״̬
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
				if (collection != null) { // ˵��֮ǰ�Ѿ��ղع�����ȡ���ղ�
					collectionRepository.delete(collection);
					System.out.println(feed.getCollectionNumber());
					feed.setCollectionNumber(collectionNumber - 1);
					feedRepository.save(feed);
				}
			} catch (NullPointerException npe) {
				collection = new Collection();
				collection.setUser(user);
				collection.setFeed(feed);
				collectionRepository.save(collection); // �����ղؼ�¼
				feed.setCollectionNumber(collectionNumber + 1);
				feedRepository.save(feed);

			}
		}
	    result="success";
		return result; // ��ȡ�ȵ������б��������������ղ���
	}*/

	@RequestMapping("/user/saveRss")
	// ����rssԴ
	public Object saveRss(@RequestParam("userid") int user_id,
			@RequestParam("rssid") int rss_id) { // rssԴ����
		Rss rss = new Rss();
		User user = new User();
		RssCollection rssCollection = new RssCollection();
		ObjectVO objectVO = new ObjectVO();
		if (rss_id > 0) { // rssԴ���Զ�α����ģ����
			user = userRepository.findById(user_id);
			rss = rssRepository.findById(rss_id);
			rssCollection.setUser(user);
			rssCollection.setRss(rss);
			rssCollectionRepository.save(rssCollection); // ����һ���û�����rss��Ϣ
			objectVO.status = "success";
			objectVO.msg = "RSSԴ���ĳɹ�";
		} else {
			objectVO.status = "failed";
			objectVO.msg = "RSSԴ����ʧ��";
		}
		return objectVO;
	}

	@RequestMapping("/user/deleteRss")
	// ȡ��rssԴ����
	public Object deleteRss(
			@RequestParam("rssCollection_id") int rssCollection_id) {
		ObjectVO objectVO = new ObjectVO();
		if (rssCollection_id > 0) { // ��Ч
			rssCollectionRepository.delete(rssCollection_id);
			objectVO.status = "success";
			objectVO.msg = "RSSԴ������ȡ��";
		} else {
			objectVO.status = "failed";
			objectVO.msg = "RSSԴ����ȡ��ʧ��";
		}
		return objectVO;
	}

	@RequestMapping("/user/rss/list")
	// �û��ղص�rssԴ�б�
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
		if(tag_id>0){  //��˵���б�ǩ����
			tag=tagRepository.findById(tag_id);
		}
		else{   //��˵�����Ի���ǩ�����ܴ���ͬ����ǩ��������ͬ������ӣ�����������ݿ�
			tag=tagRepository.findByName(name);
			if(tag==null){  //˵�����ݿ��в����ڴ˱�ǩ
				Tag t=new Tag();
				t.setName(name);
				tag=tagRepository.save(t);
			}else{   //˵�����ݿ��д��ڴ˱�ǩ	
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
	public List<TagVO> randomTags(@RequestParam("userid")int user_id){   //�ж��Ƿ�Ϊ�Լ��ı�ǩ����Ϊ�Լ��ı�ǩ�����
		List<Tag> tags=new ArrayList<Tag>();        //���ݿ��е����б�ǩ
		List<Tag> user_tags=new ArrayList<Tag>();   //�Լ��ı�ǩ
		List<TagVO> tagVOs=new ArrayList<TagVO>();
	    List<TagCollection> tagCollections=new ArrayList<TagCollection>();
		Tag tag=new Tag();
		User user=new User();
		user=userRepository.findById(user_id);
		tags=tagRepository.findTags();  //�������������,���б�ǩ
		tagCollections=tagCollectionRepository.findByUser(user);  //���û��Լ��ı�ǩ
		if(tagCollections.size()>0){
		for(TagCollection tg:tagCollections){
			tag=tg.getTag();
			user_tags.add(tag);
		}
		    int index=0;
			for(Tag t:tags){   //�������б�ǩ
				if(index<8){
				if(user_tags.contains(t)==false){  //�û���ǩ�ղ����޴˱�ǩ
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
		else{  //û���û���ǩ
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
			@RequestParam("content") String content,@RequestParam("app_version") String appVersion) throws Exception { // �����ӿ�
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
		Date reTime = new Date(); // �ɵ��Ƽ��r�g
		Date now = new Date();   //��ǰ�r�g
		reTime=(Date) request.getServletContext().getAttribute(user.getUsername()); //�û���Ψһ
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
			// ����һ��pool�ɷ�����ٸ�jedisʵ����ͨ��pool.getResource()����ȡ��
			// �����ֵΪ-1�����ʾ�����ƣ����pool�Ѿ�������maxActive��jedisʵ�������ʱpool��״̬Ϊexhausted(�ľ�)��
			config.setMaxActive(500);
			// ����һ��pool����ж��ٸ�״̬Ϊidle(���е�)��jedisʵ����
			config.setMaxIdle(5);
			// ��ʾ��borrow(����)һ��jedisʵ��ʱ�����ĵȴ�ʱ�䣬��������ȴ�ʱ�䣬��ֱ���׳�JedisConnectionException��
			config.setMaxWait(1000 * 100);
			// ��borrowһ��jedisʵ��ʱ���Ƿ���ǰ����validate���������Ϊtrue����õ���jedisʵ�����ǿ��õģ�
			config.setTestOnBorrow(true);
			pool = new JedisPool(config, parseApplicationXmlToAddress(), 6379); // ���ݱ���ip���ı�
		}
		return pool;
	}

	private static String parseApplicationXmlToAddress() throws Exception {
		SAXBuilder builder = new SAXBuilder(); // ����һ��xml����
		Document document = builder.build(Application.class.getClassLoader()
				.getResourceAsStream("application.xml"));// ���xml�ĵ��ڵ�
		Element e = document.getRootElement(); // �õ�app
		String address = e.getAttributeValue("address");
		return address;
	}
	private static String parseVersionXmlToId() throws Exception{
		SAXBuilder builder = new SAXBuilder(); // ����һ��xml����
		Document document = builder.build(Application.class.getClassLoader()
				.getResourceAsStream("version.xml"));// ���xml�ĵ��ڵ�
		Element e = document.getRootElement();
		String id=e.getAttributeValue("id");
		return id;
	}
}