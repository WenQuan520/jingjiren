package cn.softbank.purchase.domain;

public class PayInfo {
	private String msg;
	private float price;
	private String goodName;
	private String desc;
	private String goodCount;
	private String orderBatchNo;
	private String payWay;
	
	public PayInfo(float price, String goodName, String desc, String orderBatchNo) {
		super();
		this.price = price;
		this.goodName = goodName;
		this.desc = desc;
		this.orderBatchNo = orderBatchNo;
	}
	public PayInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getGoodName() {
		return goodName;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getGoodCount() {
		return goodCount;
	}
	public void setGoodCount(String goodCount) {
		this.goodCount = goodCount;
	}
	public String getOrderBatchNo() {
		return orderBatchNo;
	}
	public void setOrderBatchNo(String orderBatchNo) {
		this.orderBatchNo = orderBatchNo;
	}
	public String getPayWay() {
		return payWay;
	}
	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}
	

}
