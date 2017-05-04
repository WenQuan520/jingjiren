package cn.softbank.purchase.domain;

public class SortItem {
	private String title;
	private boolean isChoosed;

	public SortItem(String title, boolean isChoosed) {
		super();
		this.title = title;
		this.isChoosed = isChoosed;
	}

	public SortItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isChoosed() {
		return isChoosed;
	}

	public void setChoosed(boolean isChoosed) {
		this.isChoosed = isChoosed;
	}

}
