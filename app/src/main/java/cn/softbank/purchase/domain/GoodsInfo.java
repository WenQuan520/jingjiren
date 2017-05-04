package cn.softbank.purchase.domain;

import java.util.List;

public class GoodsInfo{
	private String id;
	private List<String> img_path;
	private String title;
	private String sub_title;
	private float discount_price;
	private float original_price;
	private String appointment; //预约时间
	private String production_time;//生产时间 
	private String status;//1上架2下架
	private String purchase_num;//限购数量
	private String stock;//库存
	private long end_time;//抢购剩余时间
	private String info;
	private String range;
	private int total_count;//单量
	private float postage;
	private String supplier;

	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getImg_path() {
		return img_path;
	}
	public void setImg_path(List<String> img_path) {
		this.img_path = img_path;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSub_title() {
		return sub_title;
	}
	public void setSub_title(String sub_title) {
		this.sub_title = sub_title;
	}
	public float getMin_price() {
		return discount_price;
	}
	public void setMin_price(float min_price) {
		this.discount_price = min_price;
	}
	public float getOriginalPrice() {
		return original_price;
	}
	public void setOriginalPrice(float originalPrice) {
		this.original_price = originalPrice;
	}
	public String getAppointment() {
		return appointment;
	}
	public void setAppointment(String appointment) {
		this.appointment = appointment;
	}
	public String getProduction_time() {
		return production_time;
	}
	public void setProduction_time(String production_time) {
		this.production_time = production_time;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPurchase_num() {
		return purchase_num;
	}
	public void setPurchase_num(String purchase_num) {
		this.purchase_num = purchase_num;
	}
	public String getStock() {
		return stock;
	}
	public void setStock(String stock) {
		this.stock = stock;
	}
	public long getEnd_time() {
		return end_time;
	}
	public void setEnd_time(long end_time) {
		this.end_time = end_time;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public int getTotal_count() {
		return total_count;
	}
	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}
	public float getPostage() {
		return postage;
	}
	public void setPostage(float postage) {
		this.postage = postage;
	}
	
}
