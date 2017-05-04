package cn.softbank.purchase.domain;

import java.util.List;

public class House {
	private String id;// "id": "3",
	private String name;// "name": "户型201",
	private String area;// "area": "201",
	private List<String> pic;// "pic": []

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public List<String> getPic() {
		return pic;
	}

	public void setPic(List<String> pic) {
		this.pic = pic;
	}

}
