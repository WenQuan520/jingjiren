package cn.softbank.purchase.domain;

import java.io.Serializable;
import java.util.List;

public class Customer implements Serializable{
	private String customerId;
	private String name;
	private String phone;
	private String gender;
	private String yixiang;
	private String price;
	private String area;
	private String region;
	private String type;
	private String ask;
	private long zuihougenjin;
	private List<GenjinInfo> genjinInfo;
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getYixiang() {
		return yixiang;
	}
	public void setYixiang(String yixiang) {
		this.yixiang = yixiang;
	}
	public long getZuihougenjin() {
		return zuihougenjin;
	}
	public void setZuihougenjin(long zuihougenjin) {
		this.zuihougenjin = zuihougenjin;
	}
	public List<GenjinInfo> getGenjinInfo() {
		return genjinInfo;
	}
	public void setGenjinInfo(List<GenjinInfo> genjinInfo) {
		this.genjinInfo = genjinInfo;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAsk() {
		return ask;
	}
	public void setAsk(String ask) {
		this.ask = ask;
	}
	
}
