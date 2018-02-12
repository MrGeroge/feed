package net.zypro.feed.util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import net.zypro.feed.domain.Feed;

public class FeedUtil {
	public static List<Feed> getFeeds(String keyword) throws Exception { // 根据关键字来搜索热点Feed
		URL url = null;
		HttpURLConnection conn = null;
		List<Feed> feeds = new ArrayList<Feed>();
		String u = URLEncoder.encode("http", "UTF-8") + ":" + "//"
				+ URLEncoder.encode("news", "UTF-8") + "."
				+ URLEncoder.encode("baidu", "UTF-8") + "."
				+ URLEncoder.encode("com", "UTF-8") + "/"
				+ URLEncoder.encode("ns", "UTF-8") + "?"
				+ URLEncoder.encode("sr", "UTF-8") + "="
				+ URLEncoder.encode("0", "UTF-8") + "&"
				+ URLEncoder.encode("cl", "UTF-8") + "="
				+ URLEncoder.encode("2", "UTF-8") + "&"
				+ URLEncoder.encode("tn", "UTF-8") + "="
				+ URLEncoder.encode("newsrss", "UTF-8") + "&"
				+ URLEncoder.encode("word", "UTF-8") + "="
				+ URLEncoder.encode(keyword, "UTF-8") + "&"
				+ URLEncoder.encode("rn", "UTF-8") + "="
				+ URLEncoder.encode("20", "UTF-8") + "&"
				+ URLEncoder.encode("ct", "UTF-8") + "="
				+ URLEncoder.encode("0", "UTF-8");
		url = new URL(u);
		conn = (HttpURLConnection) url.openConnection();
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			RssUtil mParser = new RssUtil();
			feeds = mParser.parser(conn.getInputStream());
			for (Feed f : feeds) {
				String str = f.getPubDate();
				f.setPubDate(timeDeal(str));
			}
		}
		return feeds;
	}

	public static List<Feed> getNearbyFeeds(String address) throws Exception { // 获得附近的新闻
		String location = ""; // 若用户未定位，则定位在湖北
		URL url = null;
		HttpURLConnection conn = null;
		List<Feed> feeds = new ArrayList<Feed>();
		if (address.equals("北京市")) {
			location = "0";
		} else if (address.equals("上海市")) {
			location = "2354";
		} else if (address.equals("天津市")) {
			location = "125";
		} else if (address.equals("重庆市")) {
			location = "6425";
		} else if (address.equals("广东省")) {
			location = "5495";
		} else if (address.equals("河北省")) {
			location = "250";
		} else if (address.equals("辽宁省")) {
			location = "1481";
		} else if (address.equals("吉林省")) {
			location = "1783";
		} else if (address.equals("甘肃省")) {
			location = "8534";
		} else if (address.equals("山西省")) {
			location = "812";
		} else if (address.equals("四川省")) {
			location = "6692";
		} else if (address.equals("陕西省")) {
			location = "8205";
		} else if (address.equals("河南省")) {
			location = "4371";
		} else if (address.equals("山东省")) {
			location = "3996";
		} else if (address.equals("湖南省")) {
			location = "5161";
		} else if (address.equals("湖北省")) {
			location = "4811";
		} else if (address.equals("江西省")) {
			location = "3636";
		} else if (address.equals("江苏省")) {
			location = "2493";
		} else if (address.equals("浙江省")) {
			location = "2809";
		} else if (address.equals("安徽省")) {
			location = "3072";
		} else if (address.equals("福建省")) {
			location = "3372";
		} else if (address.equals("广西省")) {
			location = "5886";
		} else if (address.equals("贵州省")) {
			location = "7230";
		} else if (address.equals("香港")) {
			location = "9337";
		} else if (address.equals("澳门")) {
			location = "9436";
		} else if (address.equals("海南省")) {
			location = "6245";
		} else if (address.equals("台湾省")) {
			location = "9442";
		} else if (address.equals("云南省")) {
			location = "7527";
		} else if (address.equals("内蒙古")) {
			location = "1167";
		} else if (address.equals("青海省")) {
			location = "8782";
		} else if (address.equals("宁夏省")) {
			location = "8907";
		} else if (address.equals("新疆省")) {
			location = "9001";
		} else if (address.equals("西藏省")) {
			location = "7915";
		} else if (address.equals("黑龙江省")) {
			location = "1967";
		} else {
			location = "4811";
		}
		String u = URLEncoder.encode("http", "UTF-8") + ":" + "//"
				+ URLEncoder.encode("news", "UTF-8") + "."
				+ URLEncoder.encode("baidu", "UTF-8") + "."
				+ URLEncoder.encode("com", "UTF-8") + "/"
				+ URLEncoder.encode("n", "UTF-8") + "?"
				+ URLEncoder.encode("cmd", "UTF-8") + "="
				+ URLEncoder.encode("7", "UTF-8") + "&"
				+ URLEncoder.encode("loc", "UTF-8") + "="
				+ URLEncoder.encode(location, "UTF-8") + "&"
				+ URLEncoder.encode("tn", "UTF-8") + "="
				+ URLEncoder.encode("rss", "UTF-8");
		url = new URL(u);
		conn = (HttpURLConnection) url.openConnection();
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			RssUtil mParser = new RssUtil();
			feeds = mParser.parser(conn.getInputStream());
			for (Feed f : feeds) {
				String s = f.getPubDate();
				String date1 = s.substring(0, 4);
				String date2 = s.substring(5, 7);
				String date3 = s.substring(8, 10);
				String date4 = s.substring(11, 16);
				f.setPubDate((date1 + "年" + date2 + "月" + date3 + "日" + " " + date4));
			}
		}
		return feeds;
	}

	private static String timeDeal(String str) {
		Scanner scan = new Scanner(str);
		String date1 = scan.next().substring(0, 3); // 星期,不用管
		String date2 = scan.next(); // 日期
		String date3 = scan.next(); // 月份
		if (date3.equals("Jan")) {
			date3 = "1";
		} else if (date3.equals("Feb")) {
			date3 = "2";
		} else if (date3.equals("Mar")) {
			date3 = "3";
		} else if (date3.equals("Apr")) {
			date3 = "4";
		} else if (date3.equals("May")) {
			date3 = "5";
		} else if (date3.equals("Jun")) {
			date3 = "6";
		} else if (date3.equals("Jul")) {
			date3 = "7";
		} else if (date3.equals("Aug")) {
			date3 = "8";
		} else if (date3.equals("Sep")) {
			date3 = "9";
		} else if (date3.equals("Oct")) {
			date3 = "10";
		} else if (date3.equals("Nov")) {
			date3 = "11";
		} else if (date3.equals("Dec")) {
			date3 = "12";
		}
		String date4 = scan.next(); // 年份
		String date5 = scan.next().substring(0, 5); // 时间
		return (date4 + "年" + date3 + "月" + date2 + "日" + " " + date5);
	}
	public static Feed feedDeal(Feed f){
		String html=f.getDescription();	
		Document doc = Jsoup.parseBodyFragment(html);
		Elements eles = doc.getElementsByTag("img");
		for (Element ele:eles) {
			f.setCoverUrl(ele.attr("src"));
		}
		final String regEx_html = "<[^>]+>";
		final String regEx_space = "\\s*|\t|\r|\n";
		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);  
        Matcher m_html = p_html.matcher(html);  
        html  = m_html.replaceAll(""); // 过滤html标签  
        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);  
        Matcher m_space = p_space.matcher(html);  
        html = m_space.replaceAll(""); 
        f.setDescription(html);
		f.setLoveNumber(0);
		f.setCollectionNumber(0);
		return f;
	}
}
