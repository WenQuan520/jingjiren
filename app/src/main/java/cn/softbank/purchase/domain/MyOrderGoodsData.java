package cn.softbank.purchase.domain;

public class MyOrderGoodsData {
	private String id;
	private String good_name;
	private String img_path;
	private float min_price;
	private String sku;
	private int count;
	private int type;
	private int status;
	private float amount;
	private int posType;
	private String order_no;
	private String orderId;
	private String posttype;
	private String postid;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return good_name;
	}
	public void setTitle(String title) {
		this.good_name = title;
	}
	public String getImg_path() {
		return img_path;
	}
	public void setImg_path(String img_path) {
		this.img_path = img_path;
	}
	public float getMin_price() {
		return min_price;
	}
	public void setMin_price(float min_price) {
		this.min_price = min_price;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public int getPosType() {
		return posType;
	}
	public void setPosType(int posType) {
		this.posType = posType;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getPosttype() {
		return posttype;
	}
	public void setPosttype(String posttype) {
		this.posttype = posttype;
	}
	public String getPostid() {
		return postid;
	}
	public void setPostid(String postid) {
		this.postid = postid;
	}
	
}
