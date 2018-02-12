package net.zypro.feed.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zypro.feed.domain.Admin;
import net.zypro.feed.domain.Feed;
import net.zypro.feed.domain.Feedback;
import net.zypro.feed.domain.Hot;
import net.zypro.feed.domain.Rss;
import net.zypro.feed.repository.AdminRepository;
import net.zypro.feed.repository.FeedRepository;
import net.zypro.feed.repository.FeedbackRepository;
import net.zypro.feed.repository.HotRepository;
import net.zypro.feed.repository.RssRepository;
import net.zypro.feed.vo.FeedVO;
import net.zypro.feed.vo.ObjectVO;
import net.zypro.feed.vo.RssVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController extends BaseController{  //admin��½��ע������ɾ���RssԴ������ɾ������,�����̨
	@Autowired
	private AdminRepository adminRepository;
	@Autowired
	private RssRepository rssRepository;
	@Autowired
	private FeedRepository feedRepository;
	@Autowired
	private HotRepository hotRepository;
	@Autowired
	private FeedbackRepository feedbackRepository;
	public void setAdminRepository(AdminRepository adminRepository) {
		this.adminRepository = adminRepository;
	}
	public void setRssRepository(RssRepository rssRepository) {
		this.rssRepository = rssRepository;
	}
	public void setFeedRepository(FeedRepository feedRepository) {
		this.feedRepository = feedRepository;
	}
	public void setHotRepository(HotRepository hotRepository) {
		this.hotRepository = hotRepository;
	}
	public void setFeedbackRepository(FeedbackRepository feedbackRepository) {
		this.feedbackRepository = feedbackRepository;
	}
@RequestMapping("/admin/login")
public ObjectVO login(@RequestParam("username")String username,@RequestParam("password")String password,HttpServletRequest request,HttpServletResponse response){
	ObjectVO objectVO=new ObjectVO();
	Admin admin=new Admin();
	if(username==null){
		objectVO.status="failed";
		objectVO.msg="ȱ���û���";
	}else if(password==null){
		objectVO.status="failed";
		objectVO.msg="ȱ������";
	}else{
     admin=adminRepository.findByUsernameAndPassword(username, password);
     if(admin==null){
    	 objectVO.status="failed";
    	 objectVO.msg="�û������������";
     }else{
    	 objectVO.status="success";
    	 objectVO.msg="����Ա��½�ɹ�";    //��session�б���˹���Ա����
    	 request.getSession().setAttribute("admin", admin);
     }
	}
	return objectVO;
}
@RequestMapping("/admin/logout")
public ObjectVO  logout(HttpServletRequest request,HttpServletResponse response){
	ObjectVO objectVO=new ObjectVO();
	request.getSession().invalidate();
	objectVO.status="success";
	objectVO.msg="�û���ע��";
	return objectVO;
}
@RequestMapping("/admin/addRss")   //������ɾ�ľ�����rss�б�
public List<RssVO> addRss(@RequestParam("name")String name,@RequestParam("url")String url,HttpServletRequest request,HttpServletResponse response){
	List<RssVO> rssVOs=new ArrayList<RssVO>();
	List<Rss> rsses=new ArrayList<Rss>();
	Rss rss=new Rss();
	Admin admin=new Admin();
	if((name!=null)&&(url!=null)){   //���֮�󡣺Ͳ���Ӿ��������ݿ�rssԴ�б�
		rss.setName(name);
		rss.setUrl(url);
		admin=(Admin)request.getSession().getAttribute("admin");
		rss.setAdmin(admin);
	    rssRepository.save(rss);   //����µ�rssԴ�����ݿ�
	}
	rsses=rssRepository.findAll();
	for(Rss r:rsses){
		RssVO rssVO=new RssVO();
		rssVO.id=r.getId();
		rssVO.name=r.getName();
		rssVO.url=r.getUrl();
		rssVOs.add(rssVO);
	}
	return rssVOs;
}
@RequestMapping("/admin/deleteRss")
public List<RssVO> deleteRss(@RequestParam("id")int rss_id){   //����idɾ��rss
	List<RssVO> rssVOs=new ArrayList<RssVO>();
	List<Rss> rsses=new ArrayList<Rss>();
	if(rss_id>0){  //ɾ����rssԴ��Ч
		rssRepository.delete(rss_id);
	}
	rsses=rssRepository.findAll();
	for(Rss r:rsses){
		RssVO rssVO=new RssVO();
		rssVO.id=r.getId();
		rssVO.name=r.getName();
		rssVO.url=r.getUrl();
		rssVOs.add(rssVO);
	}
	return rssVOs;
}
@RequestMapping("/admin/updateRss")  //���ܸ�name������url��������������
public List<RssVO> updateRss(@RequestParam("id")int rss_id,@RequestParam("name")String name,@RequestParam("url")String url){
	List<RssVO> rssVOs=new ArrayList<RssVO>();
	List<Rss> rsses=new ArrayList<Rss>();
	Rss rss=new Rss();
	if(rss_id>0){  //����rssԴ��Ч
		rss=rssRepository.findById(rss_id);
		if((name==null)||(name.equals(""))){  //Ϊ�ջ���Ϊ�մ�ʱ��
			
		}else if(name!=null){
			rss.setName(name);
		}
		if((url==null)||(url.equals(""))){
			
		}else if(url!=null){
			rss.setUrl(url);
		}
		rssRepository.save(rss);  //����rss
	}
	rsses=rssRepository.findAll();
	for(Rss r:rsses){
		RssVO rssVO=new RssVO();
		rssVO.id=r.getId();
		rssVO.name=r.getName();
		rssVO.url=r.getUrl();
		rssVOs.add(rssVO);
	}
	return rssVOs;
}
@RequestMapping("/admin/deleteFeed")
public List<FeedVO> deleteFeed(@RequestParam("id")long feed_id){
	List<FeedVO> feedVOs=new ArrayList<FeedVO>();
	List<Feed> feeds=new ArrayList<Feed>();
	if(feed_id>0){   //ɾ����feed��Ч,��ɾ�ȵ㣬��ɾ����
		Feed feed=new Feed();
		Hot hot=new Hot();
		feed=feedRepository.findById(feed_id);
		hot=hotRepository.findByFeed(feed);
		if(hot!=null){
		hotRepository.delete(hot);
		}
		feedRepository.delete(feed);
	}
	feeds=feedRepository.findAll();
	for(Feed f:feeds){
		FeedVO feedVO=new FeedVO();
		feedVO.id=f.getId();
		feedVO.link=f.getLink();
		feedVO.pubDate=f.getPubDate();
		feedVO.title=f.getTitle();
		feedVO.description=f.getDescription();
		feedVO.loveNumber=f.getLoveNumber();
		/*if(feedVO.loveNumber>0){
			feedVO.isLoved=true;
		}
		else{
			feedVO.isLoved=false;
		}
		feedVO.collectionNumber=f.getCollectionNumber();
		if(feedVO.collectionNumber>0){
			feedVO.isCollected=true;
		}else{
			feedVO.isCollected=false;
		}
		*/
		feedVOs.add(feedVO);
	}
	return feedVOs;
}
@RequestMapping("/admin/findRss/keyword")
public List<RssVO> findRssByKeyword(@RequestParam("name")String name){
	List<RssVO> rssVOs=new ArrayList<RssVO>();
	List<Rss> rsses=new ArrayList<Rss>();
	if(name!=null){   //����Ĳ�����Ч
		rsses=rssRepository.findByNameLike("%"+name+"%");
		for(Rss r:rsses){
			RssVO rssVO=new RssVO();
			rssVO.id=r.getId();
			rssVO.name=r.getName();
			rssVO.url=r.getUrl();
			rssVOs.add(rssVO);
		}
	}
	return rssVOs;
}
@RequestMapping("/admin/feedback/list")
public List<Feedback> findAllFeedback(){
	List<Feedback> feedbacks=new ArrayList<Feedback>();
	feedbacks=feedbackRepository.findAll();
	return feedbacks;
}
@RequestMapping("/admin/feedback/status")
public List<Feedback> changeFeedbackStatus(@RequestParam("id") int feedback_id){
	List<Feedback> feedbacks=new ArrayList<Feedback>();
	Feedback feedback=new Feedback();
	if(feedback_id>0){
		feedback=feedbackRepository.findById(feedback_id);
		feedback.setStatus("yes");
		feedbackRepository.save(feedback);
	}
	feedbacks=feedbackRepository.findAll();
	return feedbacks;
}
}
