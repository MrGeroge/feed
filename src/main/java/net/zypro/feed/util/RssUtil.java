package net.zypro.feed.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.zypro.feed.domain.Feed;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class RssUtil {
	private SAXParserFactory saxFactory;
	private SAXParser saxParser;

	public RssUtil() {
		initSaxParser();
	}

	public List<Feed> parser(InputStream inStream) {
		RssHandler mHandler = new RssHandler();

		try {
			saxParser.parse(inStream, mHandler);

			return mHandler.getFeeds();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private void initSaxParser() {
		try {

			this.saxFactory = SAXParserFactory.newInstance();
			this.saxParser = saxFactory.newSAXParser();

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	class RssHandler extends DefaultHandler {
		private final static String ITEM_TAG = "item";
		private final static String TITLE_TAG = "title";
		private final static String LINK_TAG = "link";
		private final static String DATE_TAG = "pubDate";
		private final static String SOURCE_TAG = "source";
		private final static String AUTHOR_TAG = "author";
		private final static String DESC_TAG = "description";

		private StringBuilder textBuffer;
		private Feed newEntry;
		private List<Feed> feeds = new ArrayList<Feed>();

		public List<Feed> getFeeds() {
			return feeds;
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {

			if (qName.equals(ITEM_TAG)) {
				newEntry = new Feed();
			}
			if ((qName == ITEM_TAG) || (qName == TITLE_TAG)
					|| (qName == LINK_TAG) || (qName == DATE_TAG)
					|| (qName == SOURCE_TAG) || (qName == AUTHOR_TAG)
					|| (qName == DESC_TAG)) {
				textBuffer = new StringBuilder();
			} else {
				textBuffer = null;
			}

		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {

			if (textBuffer != null) {
				String htmlContent = new String(ch, start, length);
				textBuffer.append(htmlContent);
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if (newEntry != null) {
				if (qName == ITEM_TAG) {
					feeds.add(newEntry);
				} else if (qName == TITLE_TAG) {
					newEntry.setTitle(textBuffer.toString());
				} else if (qName == LINK_TAG) {
					newEntry.setLink(textBuffer.toString());
				} else if (qName == DATE_TAG) {
					newEntry.setPubDate(textBuffer.toString());
				} else if (qName == SOURCE_TAG) {
					newEntry.setSource(textBuffer.toString());
				} else if (qName == AUTHOR_TAG) {
					newEntry.setAuthor(textBuffer.toString());
				} else if (qName == DESC_TAG) {
					newEntry.setDescription(textBuffer.toString());
				}
			}
		}
	}

}
