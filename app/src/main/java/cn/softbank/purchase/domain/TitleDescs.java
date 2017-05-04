package cn.softbank.purchase.domain;

import java.io.Serializable;
import java.util.List;

public class TitleDescs implements Serializable{
	private List<TitleDesc> titleDescs;
	
	public TitleDescs(List<TitleDesc> titleDescs) {
		super();
		this.titleDescs = titleDescs;
	}

	public TitleDescs() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<TitleDesc> getTitleDescs() {
		return titleDescs;
	}

	public void setTitleDescs(List<TitleDesc> titleDescs) {
		this.titleDescs = titleDescs;
	}
	
}
