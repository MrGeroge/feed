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
	public void testGetFeeds() throws Exception {   //���ݹؼ��ֵõ���������Զ�Ǵ���̸����
		List<Feed> feeds=new ArrayList<Feed>();
		feeds=FeedUtil.getNearbyFeeds("����");   //���۹ؼ�����ʲô,��ľ�У��õ����б���Զ��һ��
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
//		   System.out.println(""+day+"��"+hour+"Сʱ"+min+"��"+s+"��");
//	}

}
