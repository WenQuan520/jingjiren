package cn.softbank.purchase.domain;

import java.io.Serializable;
import java.util.List;

public class HouseAsset implements Serializable{
	private List<AssetList> assetList;
	private List<String> houseImg;
	
	public List<AssetList> getAssetList() {
		return assetList;
	}
	public void setAssetList(List<AssetList> assetList) {
		this.assetList = assetList;
	}
	public List<String> getHouseImg() {
		return houseImg;
	}
	public void setHouseImg(List<String> houseImg) {
		this.houseImg = houseImg;
	}
	
}
