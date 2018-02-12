package net.zypro.feed.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity(name = "rss")
public class Rss {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column(name = "url")
	private String url;
	@Column(name = "name")
	private String name;
	@ManyToOne(fetch = FetchType.LAZY)
	private Admin admin;
	@OneToMany(mappedBy = "rss", fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	private List<RssCollection> rssCollections;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public List<RssCollection> getRssCollections() {
		return rssCollections;
	}

	public void setRssCollections(List<RssCollection> rssCollections) {
		this.rssCollections = rssCollections;
	}

}
