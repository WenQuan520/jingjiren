package cn.softbank.purchase.domain;

import java.io.Serializable;
import java.util.List;

public class Pic implements Serializable{
	private List<String> shinei;
	private List<String> shiwai;
	private List<String> huxing;

	public List<String> getShinei() {
		return shinei;
	}

	public void setShinei(List<String> shinei) {
		this.shinei = shinei;
	}

	public List<String> getShiwai() {
		return shiwai;
	}

	public void setShiwai(List<String> shiwai) {
		this.shiwai = shiwai;
	}

	public List<String> getHuxing() {
		return huxing;
	}

	public void setHuxing(List<String> huxing) {
		this.huxing = huxing;
	}

}
