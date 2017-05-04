package cn.softbank.purchase.domain;

import java.util.List;

public class SKUData {
	private String title;
	private List<SKUCategory> values;
	
	public SKUData() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SKUData(String title, List<SKUCategory> values) {
		super();
		this.title = title;
		this.values = values;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<SKUCategory> getValues() {
		return values;
	}
	public void setValues(List<SKUCategory> values) {
		this.values = values;
	}
	
}
