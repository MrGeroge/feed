package net.zypro.feed.controller;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.zypro.feed.test.JUnitActionBase;
import net.zypro.feed.vo.FeedVO;
import net.zypro.feed.vo.ResultVO;
import net.zypro.feed.vo.SimpleFeedVO;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class UserControllerTest extends JUnitActionBase {  //用junit测试Controller相当于先添加数据再删除  
	  
    // 模拟request,response  
    private MockHttpServletRequest request;  
    private MockHttpServletResponse response;   
      
    // 注入userController  
    @Autowired  
    private UserController userController ; 
    
    public void setUserController(UserController userController) {
		this.userController = userController;
	}

	// 执行测试方法之前初始化模拟request,response  
    @Before    
    public void setUp(){    
        request = new MockHttpServletRequest();      
        request.setCharacterEncoding("UTF-8");      
        response = new MockHttpServletResponse();      
    }    
    /*@Test  
    public void testShare() throws Exception {  
        	//ResultVO result=new ResultVO();
            String result=(String) userController.share(1, 1L);
            assertEquals(true,result!=null) ;  
    }
    */
    /*@Test
    public void testLogin() throws Exception{
    	ResultVO resultVO=new ResultVO();
    	resultVO=(ResultVO) userController.login_register("xinghai801", "sina", "en", "xiantao", "e", 12,"www.baidu.com");
    	 assertEquals(true,resultVO!=null);
    }*/
    /*@Test
    public void testHotByUser() throws Exception{
    	int userid=1;
    	List<FeedVO> feedVOs=new ArrayList<FeedVO>();
    	feedVOs=userController.hotByUser(userid, request, response);
    	assertEquals(true,feedVOs!=null);
    }*/
    /*@Test
    public void testHot() throws Exception{
    	List<SimpleFeedVO> feedVOs=new ArrayList<SimpleFeedVO>();
    	feedVOs=userController.hotByVisitor(request, response);
    	assertEquals(true,feedVOs!=null);
    }*/
    /*@Test
    public void testNearbyUser() throws Exception{
    	List<FeedVO> feedVOs=new ArrayList<FeedVO>();
    	feedVOs=userController.nearbyUser("黑龙江省", 1, request, response);
    	assertEquals(true,feedVOs!=null);
    }*/
    /*@Test
    public void testHotByUser() throws Exception{
    	List<FeedVO> feedVOs=new ArrayList<FeedVO>();
    	feedVOs=userController.hotByUser(1, request, response);
    	assertEquals(true,feedVOs!=null);
    }*/
    /*@Test
    public void testHot() throws Exception{
    	List<SimpleFeedVO> feedVOs=new ArrayList<SimpleFeedVO>();
    	feedVOs=userController.hotByVisitor(request, response);
    }*/
    /*@Test
    public void testNearByUser() throws Exception{
    	List<FeedVO> feedVOs=new ArrayList<FeedVO>();
    	feedVOs=userController.nearbyUser("黑龙江省", 1, request, response);
    }*/
   /* @Test
    public void testLove() throws Exception{
    	userController.love(1, 1950);
    }*/
    
}  