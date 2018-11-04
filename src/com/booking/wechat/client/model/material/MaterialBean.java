package com.booking.wechat.client.model.material;

public class MaterialBean {

	private Integer total_count;
	
	private Integer item_count;
	
	private MaterialItemBean[] item;

	public Integer getTotal_count() {
		return total_count;
	}

	public void setTotal_count(Integer totalCount) {
		total_count = totalCount;
	}

	public Integer getItem_count() {
		return item_count;
	}

	public void setItem_count(Integer itemCount) {
		item_count = itemCount;
	}

	public MaterialItemBean[] getItem() {
		return item;
	}

	public void setItem(MaterialItemBean[] item) {
		this.item = item;
	}
}
