package cn.softbank.purchase.domain;

import com.google.gson.annotations.SerializedName;

public class WeiXinPayReq {
	private String sign;
	private String noncestr;
	private String timestamp;
	private String prepayid;
	@SerializedName(value = "package")
	private String packages;
	private String partnerid;
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getNoncestr() {
		return noncestr;
	}
	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}
	public String getTimeStamp() {
		return timestamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timestamp = timeStamp;
	}
	public String getPrepayId() {
		return prepayid;
	}
	public void setPrepayId(String prepayId) {
		this.prepayid = prepayId;
	}
	public String getPackages() {
		return packages;
	}
	public void setPackages(String packages) {
		this.packages = packages;
	}
	public String getPartnerId() {
		return partnerid;
	}
	public void setPartnerId(String partnerId) {
		this.partnerid = partnerId;
	}
	
}
