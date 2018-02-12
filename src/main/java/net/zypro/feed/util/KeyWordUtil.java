package net.zypro.feed.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class KeyWordUtil { // ץȡ�ؼ��ֵĹ�����
	public static List<String> getKeyWords() {
		Document doc;
		List<String> words = new ArrayList<String>();
		try {
			doc = Jsoup.connect("http://top.baidu.com/buzz.php?p=top10").get(); // ����Ͳ���debug��
			Elements keywords = doc.select("a.list-title");
			for (Element key : keywords) {
				String word = key.text();
				words.add(word);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return words;
	}
}
