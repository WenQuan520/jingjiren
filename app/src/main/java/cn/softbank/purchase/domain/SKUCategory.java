package cn.softbank.purchase.domain;

public class SKUCategory {
	private String id;//类别id
	private String name;//类别名称 颜色
	private String value;//类别的值 黑色
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
