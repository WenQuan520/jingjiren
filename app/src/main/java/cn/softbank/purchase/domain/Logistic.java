package cn.softbank.purchase.domain;

import java.io.Serializable;
import java.util.List;


public class Logistic implements Serializable{

	protected String platformName;
	protected String platformCode;
	protected String waybillNumber;
	protected  List<LogisticData> data;
//	public List<ShoppingCartDetails> goodsList;
	
	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public String getPlatformCode() {
		return platformCode;
	}

	public void setPlatformCode(String platformCode) {
		this.platformCode = platformCode;
	}

	public String getWaybillNumber() {
		return waybillNumber;
	}

	public void setWaybillNumber(String waybillNumber) {
		this.waybillNumber = waybillNumber;
	}

	public List<LogisticData> getDatas() {
		return data;
	}

	public void setDatas(List<LogisticData> data) {
		this.data = data;
	}
}
