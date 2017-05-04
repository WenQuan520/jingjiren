package cn.softbank.purchase.domain;

import java.util.List;


public class OrderGoodsData{
	private String id;
	private float amount;
	private String order_no;
	private List<MyOrderGoodsData> goodlist;
	private int status;
	private int type;
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
	public List<MyOrderGoodsData> getGoodlist() {
		return goodlist;
	}
	public void setGoodlist(List<MyOrderGoodsData> goodlist) {
		this.goodlist = goodlist;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
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
