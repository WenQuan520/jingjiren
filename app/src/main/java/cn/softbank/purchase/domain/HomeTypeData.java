package cn.softbank.purchase.domain;

import java.util.List;

public class HomeTypeData {
	private List<String> region;
	private List<String> buildingType;
	private List<String> area;
	private List<String> price;
	private List<String> bannerList;
	public List<String> getRegion() {
		return region;
	}
	public void setRegion(List<String> region) {
		this.region = region;
	}
	public List<String> getBuildingType() {
		return buildingType;
	}
	public void setBuildingType(List<String> buildingType) {
		this.buildingType = buildingType;
	}
	public List<String> getArea() {
		return area;
	}
	public void setArea(List<String> area) {
		this.area = area;
	}
	public List<String> getPrice() {
		return price;
	}
	public void setPrice(List<String> price) {
		this.price = price;
	}
	public List<String> getBannerList() {
		return bannerList;
	}
	public void setBannerList(List<String> bannerList) {
		this.bannerList = bannerList;
	}
	
}
