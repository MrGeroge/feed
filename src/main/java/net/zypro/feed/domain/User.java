package net.zypro.feed.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity(name = "User")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column(name = "username")
	private String username;
	@Column(name = "source")
	private String source;
	@Column(name = "nickname")
	private String nickname;
	@Column(name = "age")
	private int age;
	@Column(name = "sex")
	private String sex;
	@Column(name = "address")
	private String address;
	@Column(name="avatar")
	private String avatar;
	@OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	private List<Recommended> recommendeds;
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	private List<Collection> collections;
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	private List<RssCollection> rssCollections;
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	private List<Love> loves;
	@OneToMany(mappedBy="user", fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	private List<TagCollection> tagCollections;
	@OneToMany(mappedBy="user",fetch=FetchType.LAZY)
	@NotFound(action=NotFoundAction.IGNORE)
	private List<ShareRecord> shareRecords;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public List<Recommended> getRecommendeds() {
		return recommendeds;
	}

	public void setRecommendeds(List<Recommended> recommendeds) {
		this.recommendeds = recommendeds;
	}

	public List<Collection> getCollections() {
		return collections;
	}

	public void setCollections(List<Collection> collections) {
		this.collections = collections;
	}

	public List<RssCollection> getRssCollections() {
		return rssCollections;
	}

	public void setRssCollections(List<RssCollection> rssCollections) {
		this.rssCollections = rssCollections;
	}

	public List<Love> getLoves() {
		return loves;
	}

	public void setLoves(List<Love> loves) {
		this.loves = loves;
	}

	public List<TagCollection> getTagCollections() {
		return tagCollections;
	}

	public void setTagCollections(List<TagCollection> tagCollections) {
		this.tagCollections = tagCollections;
	}

}
