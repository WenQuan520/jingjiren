package cn.softbank.purchase.domain;

public class LoginData{
	private String userId;
	private String avatar_url;
	private String kehujingliName;
	private String kehujingliPhone;
	private String alipay;
	
	
	public LoginData() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public LoginData(String userId, String avatar_url, String kehujingliName,
			String kehujingliPhone, String alipay) {
		super();
		this.userId = userId;
		this.avatar_url = avatar_url;
		this.kehujingliName = kehujingliName;
		this.kehujingliPhone = kehujingliPhone;
		this.alipay = alipay;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAvatar_url() {
		return avatar_url;
	}
	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}
	public String getKehujingliName() {
		return kehujingliName;
	}

	public void setKehujingliName(String kehujingliName) {
		this.kehujingliName = kehujingliName;
	}

	public String getKehujingliPhone() {
		return kehujingliPhone;
	}

	public void setKehujingliPhone(String kehujingliPhone) {
		this.kehujingliPhone = kehujingliPhone;
	}

	public String getAlipay() {
		return alipay;
	}

	public void setAlipay(String alipay) {
		this.alipay = alipay;
	}
	
	
}
