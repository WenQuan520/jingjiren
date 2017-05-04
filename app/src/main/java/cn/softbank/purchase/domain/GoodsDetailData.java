package cn.softbank.purchase.domain;

import java.util.List;

public class GoodsDetailData {
	private String id;// "id": "2",
	private String houseName;// "name": "楼盘2",
	private String housePrice;// "price": "20000",
	private String houseLocation;// 杭州市上城区
	private CommissionInfo commissionInfo;
	private String lat;// "gps_x": "1",
	private String lon;// "gps_y": "1",
	private HouseAsset houseAsset;
	private List<String> flags;// 学区房，地铁房
	private List<TitleDesc> house_selling;// 学区房，地铁房
	private List<TitleDesc> house_params;// 学区房，地铁房
	private List<Param> param;
	private List<House> house;
	private String isFavorite;
	private Lingkan lingkan;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHouseName() {
		return houseName;
	}

	public void setHouseName(String houseName) {
		this.houseName = houseName;
	}

	public String getHousePrice() {
		return housePrice;
	}

	public void setHousePrice(String housePrice) {
		this.housePrice = housePrice;
	}

	public String getHouseLocation() {
		return houseLocation;
	}

	public void setHouseLocation(String houseLocation) {
		this.houseLocation = houseLocation;
	}

	public CommissionInfo getCommissionInfo() {
		return commissionInfo;
	}

	public void setCommissionInfo(CommissionInfo commissionInfo) {
		this.commissionInfo = commissionInfo;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public HouseAsset getHouseAsset() {
		return houseAsset;
	}

	public void setHouseAsset(HouseAsset houseAsset) {
		this.houseAsset = houseAsset;
	}

	public List<String> getFlags() {
		return flags;
	}

	public void setFlags(List<String> flags) {
		this.flags = flags;
	}

	public List<TitleDesc> getHouse_selling() {
		return house_selling;
	}

	public void setHouse_selling(List<TitleDesc> house_selling) {
		this.house_selling = house_selling;
	}

	public List<TitleDesc> getHouse_params() {
		return house_params;
	}

	public void setHouse_params(List<TitleDesc> house_params) {
		this.house_params = house_params;
	}

	public List<Param> getParam() {
		return param;
	}

	public void setParam(List<Param> param) {
		this.param = param;
	}

	public List<House> getHouse() {
		return house;
	}

	public void setHouse(List<House> house) {
		this.house = house;
	}

	public String getIsFavorite() {
		return isFavorite;
	}

	public void setIsFavorite(String isFavorite) {
		this.isFavorite = isFavorite;
	}

	public Lingkan getLingkan() {
		return lingkan;
	}

	public void setLingkan(Lingkan lingkan) {
		this.lingkan = lingkan;
	}

	public class Param {
		private String key;
		private String value;

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}

	public class Lingkan {
		private String name;
		private String phone;

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

	}

}
