package cn.softbank.purchase.domain;

import java.io.Serializable;


public class ShoppingCartGoods implements Serializable{
	private static final long serialVersionUID = 1L;
	private String good_id;
	private String id;
	private String title;
	private String sub_title;
	private String img_path;
	private float min_price;
	private float original_price;
	private String status;//1上架2下架
	private String purchase_num;//限购数量
	private String stock;//库存
	private String sku;
	private String skuId;
	private boolean isSel;
	private int count;
	
	public ShoppingCartGoods(String good_id, String id, String title,
			String img_path, float min_price, float originalPrice,
			String sku,String skuId, int count) {
		super();
		this.good_id = good_id;
		this.id = id;
		this.title = title;
		this.img_path = img_path;
		this.min_price = min_price;
		this.original_price = originalPrice;
		this.sku = sku;
		this.skuId = skuId;
		this.count = count;
	}
	public ShoppingCartGoods() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getGood_id() {
		return good_id;
	}
	public void setGood_id(String good_id) {
		this.good_id = good_id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImg_path() {
		return img_path;
	}
	public void setImg_path(String img_path) {
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
		return min_price;
	}
	public void setMin_price(float min_price) {
		this.min_price = min_price;
	}
	public float getOriginalPrice() {
		return original_price;
	}
	public void setOriginalPrice(float originalPrice) {
		this.original_price = originalPrice;
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
	public boolean isSel() {
		return isSel;
	}
	public void setSel(boolean isSel) {
		this.isSel = isSel;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	
}
