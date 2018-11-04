package com.booking.wechat.client.model.material;

public class MaterialNewsItem {

	private String title;
	
	private String author;
	
	private String digest;
	
	private String content;
	
	private String content_source_url;
	
	private String thumb_media_id;
	
	private String show_cover_pic;
	
	private String url;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent_source_url() {
		return content_source_url;
	}

	public void setContent_source_url(String contentSourceUrl) {
		content_source_url = contentSourceUrl;
	}

	public String getThumb_media_id() {
		return thumb_media_id;
	}

	public void setThumb_media_id(String thumbMediaId) {
		thumb_media_id = thumbMediaId;
	}

	public String getShow_cover_pic() {
		return show_cover_pic;
	}

	public void setShow_cover_pic(String showCoverPic) {
		show_cover_pic = showCoverPic;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
