package cn.softbank.purchase.domain;

import java.util.List;

public class ScreenCat2 {
	private String type_id;
	private String type_name;
	private List<ScreenCat3> three_sub_type_list;
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
	public List<ScreenCat3> getTwoCat() {
		return three_sub_type_list;
	}
	public void setTwoCat(List<ScreenCat3> twoCat) {
		this.three_sub_type_list = twoCat;
	}
	
}
