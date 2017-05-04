package cn.softbank.purchase.domain;

import java.util.List;

public class MoneyData {
	private int ketixian;
	private int count;
	private int already;
	private int withdrawalsIng;
	private List<MoneyDetailData> detail;
	public int getKetixian() {
		return ketixian;
	}
	public void setKetixian(int ketixian) {
		this.ketixian = ketixian;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getAlready() {
		return already;
	}
	public void setAlready(int already) {
		this.already = already;
	}
	public int getWithdrawalsIng() {
		return withdrawalsIng;
	}
	public void setWithdrawalsIng(int withdrawalsIng) {
		this.withdrawalsIng = withdrawalsIng;
	}
	public List<MoneyDetailData> getDetail() {
		return detail;
	}
	public void setDetail(List<MoneyDetailData> detail) {
		this.detail = detail;
	}
	 
}
