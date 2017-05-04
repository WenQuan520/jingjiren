package cn.softbank.purchase.domain;

public class ConfirmOrderPrice{
	public static final String MAIL_TITLE = "运费";
	
	private String name;
	private float price;
	private boolean isReduce;

	public ConfirmOrderPrice() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ConfirmOrderPrice(String name, float price) {
		super();
		this.name = name;
		this.price = price;
	}
	
	public ConfirmOrderPrice(String name, float price, boolean isReduce) {
		super();
		this.name = name;
		this.price = price;
		this.isReduce = isReduce;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public boolean isReduce() {
		return isReduce;
	}

	public void setReduce(boolean isReduce) {
		this.isReduce = isReduce;
	}

}
