package cn.softbank.purchase.domain;

import java.util.List;

public class ScreenCat {
	private String type_id;
	private String type_name;
	private boolean isOpen;
	private List<ScreenCat2> two_sub_type_list;
	public String getId() {
		return type_id;
	}
	public void setId(String id) {
		this.type_id = id;
	}
	public String getTitle() {
		return type_name;
	}
	public void setTitle(String title) {
		this.type_name = title;
	}
	public List<ScreenCat2> getTwoCat() {
		return two_sub_type_list;
	}
	public void setTwoCat(List<ScreenCat2> twoCat) {
		this.two_sub_type_list = twoCat;
	}
	public boolean isOpen() {
		return isOpen;
	}
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	
}
