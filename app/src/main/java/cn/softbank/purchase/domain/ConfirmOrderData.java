package cn.softbank.purchase.domain;

import java.io.Serializable;
import java.util.List;

public class ConfirmOrderData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private float price;
	private List<ShoppingCartGoods> seledData;
	private int type;//1 普通商品 2预约商品 3 定制商品 
	private String appointment; //预约时间
	private String production_time;//生产时间
	
	public ConfirmOrderData(int type,float price, List<ShoppingCartGoods> seledData) {
		super();
		this.type = type;
		this.price = price;
		this.seledData = seledData;
	}
	public ConfirmOrderData() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public List<ShoppingCartGoods> getSeledData() {
		return seledData;
	}
	public void setSeledData(List<ShoppingCartGoods> seledData) {
		this.seledData = seledData;
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
	
}
