package net.zypro.feed.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.zypro.feed.domain.Feed;

import org.junit.Test;

public class FeedUtilTest {

	@Test
	public void testGetFeeds() throws Exception {   //根据关键字得到的新闻永远是代表谈军队
		List<Feed> feeds=new ArrayList<Feed>();
		feeds=FeedUtil.getNearbyFeeds("湖南");   //无论关键字是什么,有木有，得到的列表永远是一个
		for(Feed f:feeds){
			System.out.println(f.getLink());
			System.out.println(f.getAuthor());
			System.out.println(f.getTitle());
			System.out.println(f.getCoverUrl());
			System.out.println(f.getSource());
			System.out.println(f.getPubDate());
			System.out.println(f.getDescription());
		}
	}
//	@Test
//	public void test() throws Exception{
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		   java.util.Date now = df.parse("2004-03-26 13:31:40");
//		   java.util.Date date=df.parse("2004-01-02 11:30:24");
//		//Date now=new Date();
//		//Date date=new Date();
//		   long l=now.getTime()-date.getTime();
//		   long day=l/(24*60*60*1000);
//		   long hour=(l/(60*60*1000)-day*24);
//		   long min=((l/(60*1000))-day*24*60-hour*60);
//		   long s=(l/1000-day*24*60*60-hour*60*60-min*60);
//		   System.out.println(""+day+"天"+hour+"小时"+min+"分"+s+"秒");
//	}

}
