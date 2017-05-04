package cn.softbank.purchase.domain;

public class HotWord {
	private String wid;
	private String wcatname;
	
	public HotWord(String cart_id, String value) {
		super();
		this.wid = cart_id;
		this.wcatname = value;
	}
	public HotWord() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getCart_id() {
		return wid;
	}
	public void setCart_id(String cart_id) {
		this.wid = cart_id;
	}
	public String getValue() {
		return wcatname;
	}
	public void setValue(String value) {
		this.wcatname = value;
	}
	
}
