package net.zypro.feed.redis;

import static org.junit.Assert.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import net.zypro.feed.Application;
import net.zypro.feed.controller.UserController;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import redis.clients.jedis.Jedis;
/*@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes =Application.class)
@WebAppConfiguration
public class RedisTest extends AbstractJUnit4SpringContextTests{
@Autowired
private RedisTemplate<String, String> redisTemplate;

	public RedisTemplate<String, String> getRedisTemplate() {
	return redisTemplate;
}

public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
	this.redisTemplate = redisTemplate;
}

	@Test
	public void test() {
		if(redisTemplate!=null){
			 System.out.println("success");
		}
		else
			System.out.println("failed");
//		Jedis jedis=new Jedis("116.211.156.2");
//		jedis.set("caiqi", "123");
//		System.out.println(jedis.get("caiqi"));
	}

}
*/
//public class RedisTest{
//	@Test
//	public void test(){
//		  try {
//
//	            InetAddress inetAddr = InetAddress.getLocalHost();
//
//	            byte[] addr = inetAddr.getAddress();
//
//	            // Convert to dot representation
//	            String ipAddr = "";
//	            for (int i = 0; i < addr.length; i++) {
//	                if (i > 0) {
//	                    ipAddr += ".";
//	                }
//	                ipAddr += addr[i] & 0xFF;
//	            }
//
//	            String hostname = inetAddr.getHostName();
//
//	            System.out.println("IP Address: " + ipAddr);
//	            System.out.println("Hostname: " + hostname);
//
//	        }
//	        catch (UnknownHostException e) {
//	            System.out.println("Host not found: " + e.getMessage());
//	        }
//
//	}
//}
public class RedisTest{
	public static void main(String[] args) throws Exception{
		UserController uc=new UserController();
		Date now=new Date();
		Date before=new Date(2015,3,16,12,01,11);
		long date1=before.getTime();
		long date2=now.getTime();
		Date time1=new Date(date1);
		Date time2=new Date(date2);
		int hour=uc.timeDifference(time1,time2 );
		System.out.println(hour);
	}
}
