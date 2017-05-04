package cn.softbank.purchase.domain;

import java.util.List;

public class EvalutionList {

	private Sorceinfo sorceinfo;
	private List<EvalutionListData> data;
	
	public Sorceinfo getSorceinfo() {
		return sorceinfo;
	}
	public void setSorceinfo(Sorceinfo sorceinfo) {
		this.sorceinfo = sorceinfo;
	}
	public List<EvalutionListData> getRows() {
		return data;
	}
	public void setRows(List<EvalutionListData> rows) {
		this.data = rows;
	}
	
	public class Sorceinfo{
		private String count;
		private String star;
		public String getCount() {
			return count;
		}
		public void setCount(String count) {
			this.count = count;
		}
		public String getStar() {
			return star;
		}
		public void setStar(String star) {
			this.star = star;
		}
	}

}
