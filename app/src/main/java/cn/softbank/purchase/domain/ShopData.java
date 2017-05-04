package cn.softbank.purchase.domain;

import java.io.Serializable;

public class ShopData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String wid;
	private String wshopname;
	private String logo;
	private String wtel;
	private String wcontent;
	private boolean isSel;
	
	public String getWid() {
		return wid;
	}
	public void setWid(String wid) {
		this.wid = wid;
	}
	public String getWshopname() {
		return wshopname;
	}
	public void setWshopname(String wshopname) {
		this.wshopname = wshopname;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getWtel() {
		return wtel;
	}
	public void setWtel(String wtel) {
		this.wtel = wtel;
	}
	public String getWcontent() {
		return wcontent;
	}
	public void setWcontent(String wcontent) {
		this.wcontent = wcontent;
	}
	public boolean isSel() {
		return isSel;
	}
	public void setSel(boolean isSel) {
		this.isSel = isSel;
	}
	
	
}
