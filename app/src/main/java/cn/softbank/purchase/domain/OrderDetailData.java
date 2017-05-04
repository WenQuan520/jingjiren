package cn.softbank.purchase.domain;

import java.util.List;

public class OrderDetailData{
	private String id;
	private float amount;
	private String order_no;
	private String create_time;
	private float postage;
	private int status;
	private List<MyOrderGoodsData> goodlist;
	private DeliveryAddr receiptinfo;
	private String posttype;
	private String postid;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public List<MyOrderGoodsData> getGoodlist() {
		return goodlist;
	}
	public void setGoodlist(List<MyOrderGoodsData> goodlist) {
		this.goodlist = goodlist;
	}
	public DeliveryAddr getReceiptinfo() {
		return receiptinfo;
	}
	public void setReceiptinfo(DeliveryAddr receiptinfo) {
		this.receiptinfo = receiptinfo;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public float getPostage() {
		return postage;
	}
	public void setPostage(float postage) {
		this.postage = postage;
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
