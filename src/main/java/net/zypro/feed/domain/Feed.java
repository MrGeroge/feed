package net.zypro.feed.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity(name = "feed")
public class Feed {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(name = "title")
	private String title;
	@Column(name = "link")
	private String link;
	@Column(name = "pubDate")
	private String pubDate;
	@Column(name = "source")
	private String source;
	@Column(name = "author")
	private String author;
	@Column(name = "description")
	@Lob
	private String description;
	@Column(name = "coverUrl")
	private String coverUrl;
	private int loveNumber; // 点赞数目
	@Column(name = "collectionNumber")
	private int collectionNumber; // 收藏数目
	@ManyToOne(fetch = FetchType.LAZY)
	private Admin admin;
	@OneToMany(mappedBy = "feed", fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	private List<Love> loves;
	@OneToMany(mappedBy = "feed", fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	private List<Collection> collections;
	@OneToMany(mappedBy = "feed" ,fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	private List<Recommended> reCommendeds;
	@OneToMany(mappedBy="feed",fetch=FetchType.LAZY)
	@NotFound(action=NotFoundAction.IGNORE)
	private List<ShareRecord> shareRecords;
	

	public List<ShareRecord> getShareRecords() {
		return shareRecords;
	}

	public void setShareRecords(List<ShareRecord> shareRecords) {
		this.shareRecords = shareRecords;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public List<Love> getLoves() {
		return loves;
	}

	public void setLoves(List<Love> loves) {
		this.loves = loves;
	}

	public List<Collection> getCollections() {
		return collections;
	}

	public void setCollections(List<Collection> collections) {
		this.collections = collections;
	}

	public List<Recommended> getReCommendeds() {
		return reCommendeds;
	}

	public void setReCommendeds(List<Recommended> reCommendeds) {
		this.reCommendeds = reCommendeds;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public int getLoveNumber() {
		return loveNumber;
	}

	public void setLoveNumber(int loveNumber) {
		this.loveNumber = loveNumber;
	}

	public int getCollectionNumber() {
		return collectionNumber;
	}

	public void setCollectionNumber(int collectionNumber) {
		this.collectionNumber = collectionNumber;
	}
	@Override
	public String toString() {
		return String.format("title=%s", title);
	}

}
