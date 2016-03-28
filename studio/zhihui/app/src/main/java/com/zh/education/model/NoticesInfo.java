package com.zh.education.model;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author 作者：zoc
 * @date 创建时间：2016-1-24 下午1:55:27
 * @Description 类描述：
 */
public class NoticesInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String id;
	String noticeType;
	String content;
	String title;
	String createTime;

	public NoticesInfo() {
		super();
	}

	public NoticesInfo(String id,String noticeType, String content, String title,
			String createTime) {
		super();
		this.id = id;
		this.noticeType = noticeType;
		this.content = content;
		this.title = title;
		this.createTime = createTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
