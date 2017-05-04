package cn.softbank.purchase.utils;

import java.util.List;


/**
 * @描述 分页加载工具
 * @author Administrator GXH
 * 
 */
public class PageLoadUtil {
	/**
	 * 每页请求数据量
	 */
	private int pageSize = 20;
	/**
	 * 当前页
	 */
	private int currentPage = 0;// 请求当前页
	/**
	 * 重置或增加新数据
	 */
	private boolean isResetData;

	public PageLoadUtil(int pageSize) {
		super();
		this.pageSize = pageSize;
	}

	/**
	 * 处理返回的数据
	 * 
	 * @param showDatas
	 *            最终展示使用的数据
	 * @param backDatas
	 *            返回的新数据
	 */
	public <T> void handleDatas(List<T> showDatas, List<T> backDatas) {
		// 重置数据
		if (isResetData)
			showDatas.clear();

		if (backDatas != null && backDatas.size() > 0) {
			showDatas.addAll(backDatas);
		}
	}

	/**
	 * 数据是否全部加载完毕
	 * 
	 * @param backDatas
	 *            返回的数据
	 * @return
	 */
	public <T> boolean judgeHasMoreData(List<T> backDatas) {
		if (backDatas != null && backDatas.size() == pageSize)
			return true;
		return false;
	}
	
	public <T> boolean isMore(List<T> backDatas) {
		if (backDatas == null || backDatas.size() == 0)
			return false;
		
		return backDatas.size() % pageSize == 0;
	}

	/**
	 * 设置是否重置数据，true表示重置数据,false表示增加数据
	 * 
	 * @param isResetData
	 */
	public boolean isResetData() {
		return isResetData;
	}

	/**
	 * 更新当前页，true表示重置数据,false表示增加数据
	 * 
	 * @param isResetData
	 */
	public void updataPage(boolean isResetData) {
		if (isResetData)
			currentPage = 1;
		else
			currentPage++;
		this.isResetData = isResetData;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

}
