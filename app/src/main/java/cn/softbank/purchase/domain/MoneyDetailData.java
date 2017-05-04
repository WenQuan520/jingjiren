package cn.softbank.purchase.domain;

public class MoneyDetailData {
//	"title": "提现至支付宝",
//    "detail": "账号：234fere@qq.com",
//    "time": "1473697302",
//    "flowingWater": "-100元"
	private String title;
	private String detail;
	private String time;
	private String flowingWater;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getFlowingWater() {
		return flowingWater;
	}
	public void setFlowingWater(String flowingWater) {
		this.flowingWater = flowingWater;
	}
	
	
}
