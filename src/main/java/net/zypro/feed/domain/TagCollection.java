package net.zypro.feed.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity(name="tagCollection")
public class TagCollection {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
private int id;
	@ManyToOne(fetch=FetchType.EAGER)
private User user;
	@ManyToOne(fetch=FetchType.EAGER)
private Tag tag;
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
public Tag getTag() {
	return tag;
}
public void setTag(Tag tag) {
	this.tag = tag;
}


}
