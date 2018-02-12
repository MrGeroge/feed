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
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;



@Entity
@Table(name="tag")
public class Tag {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
private int id;
	@Column(name="name")
	//@Index(name="tagNameIndex")
private String name;
	@OneToMany(mappedBy="tag", fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	private List<TagCollection> tagCollections;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public List<TagCollection> getTagCollections() {
	return tagCollections;
}
public void setTagCollections(List<TagCollection> tagCollections) {
	this.tagCollections = tagCollections;
}
@Override
public boolean equals(Object obj) {
	if(obj==null)
		return false;
	else{
		if(obj instanceof Tag){
			Tag t=(Tag)obj;
			if(this.id==t.getId()&&this.name==t.getName()){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
	}
}


}
