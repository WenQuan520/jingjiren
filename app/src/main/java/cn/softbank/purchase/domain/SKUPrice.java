package cn.softbank.purchase.domain;

import java.util.List;

public class SKUPrice {
	private String id;
	private List<String> category_id;
	private float original_price;
	private float discount_price;
	private int stock;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getCategory_id() {
		return category_id;
	}
	public void setCategory_id(List<String> category_id) {
		this.category_id = category_id;
	}
	public float getOriginal_price() {
		return original_price;
	}
	public void setOriginal_price(float original_price) {
		this.original_price = original_price;
	}
	public float getDiscount_price() {
		return discount_price;
	}
	public void setDiscount_price(float discount_price) {
		this.discount_price = discount_price;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	
}
