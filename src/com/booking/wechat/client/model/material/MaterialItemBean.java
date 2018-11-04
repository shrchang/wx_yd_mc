package com.booking.wechat.client.model.material;

public class MaterialItemBean {

	private String media_id;
	
	private String update_time;
	
	private MaterialContentBean content;

	public String getMedia_id() {
		return media_id;
	}

	public void setMedia_id(String mediaId) {
		media_id = mediaId;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String updateTime) {
		update_time = updateTime;
	}

	public MaterialContentBean getContent() {
		return content;
	}

	public void setContent(MaterialContentBean content) {
		this.content = content;
	}
	
	
}
