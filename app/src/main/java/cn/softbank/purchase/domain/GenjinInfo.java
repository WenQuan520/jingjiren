package cn.softbank.purchase.domain;

import java.io.Serializable;

import android.text.TextUtils;

public class GenjinInfo implements Serializable{
	private String user;
	private String info;
	private String time;

	public String getGenjinren() {
		return user;
	}

	public void setGenjinren(String genjinren) {
		this.user = genjinren;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public long getTime() {
		if(TextUtils.isEmpty(time))
			time = "0";
		return Long.valueOf(time);
	}

	public void setTime(String time) {
		this.time = time;
	}

}
