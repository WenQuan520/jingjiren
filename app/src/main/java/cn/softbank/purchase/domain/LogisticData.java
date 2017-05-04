package cn.softbank.purchase.domain;

import java.io.Serializable;

public class LogisticData implements Serializable{
	private String time;
	private String description;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDescrip() {
		return description;
	}

	public void setDescrip(String description) {
		this.description = description;
	}

}
