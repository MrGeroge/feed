package net.zypro.feed.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import net.zypro.feed.Application;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ClientPost extends AbstractJUnit4SpringContextTests{   //用Post测试
    @Test    
	public void testPost() throws Exception{
		while(true){
    	List<NameValuePair> values = new ArrayList<NameValuePair>();
		//CreateTableVO ctv = new CreateTableVO();
		values.add(new BasicNameValuePair("userid", "1"));
		HttpPost post = new HttpPost("http://localhost:8080/feed/user/feed/hot");
		post.setHeader("Accept", "application/json"); // 传content_type
		post.setEntity(new UrlEncodedFormEntity(values, HTTP.UTF_8)); // 穿参数进去啦
		HttpClient httpClient=new DefaultHttpClient();
		HttpResponse httpResponse = httpClient.execute(post);
	    Thread.sleep(3000);
		}
	}

}
