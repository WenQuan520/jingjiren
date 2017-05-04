package cn.softbank.purchase.domain;

import java.io.Serializable;

/**
 * @描述:收货地址
 * @version 1.0
 */
public class DeliveryAddr implements Serializable{

	
	private static final long serialVersionUID = 1471961701048350192L;
	private String id;
	private String province;
	private String city;
	private String area;
	private String address;
	private String phone;
	private String username;
	private String isdefault;//1默认，2菲默认
	
    public DeliveryAddr() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DeliveryAddr(String deliveryAddrId, String province, String city,
			String area, String addrDetail, String consignee, String phone) {
		super();
		this.id = deliveryAddrId;
		this.province = province;
		this.city = city;
		this.area = area;
		this.address = addrDetail;
		this.username = consignee;
		this.phone = phone;
	}

	public String getDeliveryAddrId() {
        return id;
    }

    public void setDeliveryAddrId(String deliveryAddrId) {
        this.id = deliveryAddrId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddrDetail() {
        return address;
    }

    public void setAddrDetail(String addrDetail) {
        this.address = addrDetail;
    }

    public String getConsignee() {
        return username;
    }

    public void setConsignee(String consignee) {
        this.username = consignee;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isDefault() {
		return "1".equals(isdefault);
	}

	public void setDefault(String isDefault) {
		this.isdefault = isDefault;
	}

}
