package com.booking.wechat.persistence.bean.room;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="wcob_room_picture")
public class RoomPicture {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	
	@Column
	private Long roomId; //场地ID
	
	
	@Column
	private String pictureSrc; //图片路径 访问路径
	
	@Column
	private String pictureName;//图片名称
	
	@Column
	private String pictureRealName;//真实名称
	
	@Column
	private String pictureRealPath;//真实路径
	
	@Column
	private String pictureDesc; //图片描述
	
	public String getPictureName() {
		return pictureName;
	}

	public String getPictureRealName() {
		return pictureRealName;
	}

	public String getPictureRealPath() {
		return pictureRealPath;
	}

	public void setPictureName(String pictureName) {
		this.pictureName = pictureName;
	}

	public void setPictureRealName(String pictureRealName) {
		this.pictureRealName = pictureRealName;
	}

	public void setPictureRealPath(String pictureRealPath) {
		this.pictureRealPath = pictureRealPath;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public String getPictureSrc() {
		return pictureSrc;
	}

	public void setPictureSrc(String pictureSrc) {
		this.pictureSrc = pictureSrc;
	}

	public String getPictureDesc() {
		return pictureDesc;
	}

	public void setPictureDesc(String pictureDesc) {
		this.pictureDesc = pictureDesc;
	}
}
