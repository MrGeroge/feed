package net.zypro.feed.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity(name = "feedback")
// ����
public class Feedback {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id; // ����id
	@Lob
	@Column(name = "content")
	private String content; // ��������
	@Column(name = "contact")
	private String contact; // ��ϵ��ʽ
	@Column(name = "createTime")
	private String createTime; // ���һ��ʱ���
	@Column(name = "status")
	private String status;
	@Column(name="app_version")
    private String appVersion;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

}
