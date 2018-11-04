package com.booking.wechat.persistence.bean.room;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 附加场地与场地的关联关系
 * @author Luoxx
 *
 */
@Entity
@Table(name="wcob_room_added_relation")
public class RoomAddedRelation {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	
	@Column
	private Long roomId;
	
	
	@Column
	private Long roomAddedId;
	
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

	public Long getRoomAddedId() {
		return roomAddedId;
	}

	public void setRoomAddedId(Long roomAddedId) {
		this.roomAddedId = roomAddedId;
	}
}
