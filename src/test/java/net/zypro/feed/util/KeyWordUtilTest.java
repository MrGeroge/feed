package net.zypro.feed.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class KeyWordUtilTest {

	@Test
	public void testGetKeyWords() {
		List<String> keywords=new ArrayList<String>();
		keywords=KeyWordUtil.getKeyWords();
		for(String s:keywords){
			System.out.println(s);
		}
	}

}
