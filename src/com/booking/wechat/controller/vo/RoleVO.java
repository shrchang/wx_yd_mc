package com.booking.wechat.controller.vo;

/**
 * 页面用户角色选择
 * @author xixi_
 *
 */
public class RoleVO {

	private Long id;
	
	private String roleName;
	
	private boolean checked = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}
