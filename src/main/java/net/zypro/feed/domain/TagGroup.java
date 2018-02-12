package net.zypro.feed.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity(name="tagGroup")
public class TagGroup {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
private int id;
	@Column(name="tag1")
private String tag1;
	@Column(name="tag2")
private String tag2;
	@Column(name="count")
private long count;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getTag1() {
	return tag1;
}
public void setTag1(String tag1) {
	this.tag1 = tag1;
}
public String getTag2() {
	return tag2;
}
public void setTag2(String tag2) {
	this.tag2 = tag2;
}
public long getCount() {
	return count;
}
public void setCount(long count) {
	this.count = count;
}

}
