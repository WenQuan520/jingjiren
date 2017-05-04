package cn.softbank.purchase.domain;

import java.io.Serializable;

public class AssetList implements Serializable{
	private String name;
	private String area;
	private Pic pic;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public Pic getPic() {
		return pic;
	}
	public void setPic(Pic pic) {
		this.pic = pic;
	}
	
	
}
