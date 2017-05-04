package cn.softbank.purchase.domain;

import java.util.List;

public class HomeGoodsDatas {
	protected String id;
	protected String image;
	protected String name;
	protected String price;
	private String lon; //经度
	private String lat;//纬度
	private List<String> flag;//写字楼
	private String area;//区域
	private String distance;//区域
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public List<String> getFlag() {
		return flag;
	}
	public void setFlag(List<String> flag) {
		this.flag = flag;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	
}
