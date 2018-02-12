package net.zypro.feed.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;



@Entity(name="collection")
public class Collection {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
private int id;
	@ManyToOne(fetch=FetchType.EAGER)
private User user;
	@ManyToOne(fetch=FetchType.EAGER)
	
private Feed feed;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public User getUser() {
	return user;
}
public void setUser(User user) {
	this.user = user;
}
public Feed getFeed() {
	return feed;
}
public void setFeed(Feed feed) {
	this.feed = feed;
}

}
