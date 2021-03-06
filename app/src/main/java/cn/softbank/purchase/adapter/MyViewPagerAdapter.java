package cn.softbank.purchase.adapter;

import java.util.List;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

public class MyViewPagerAdapter extends PagerAdapter {
	private List<View> mListViews;
	private int size;


	public MyViewPagerAdapter(List<View> mListViews) {
		super();
		this.mListViews = mListViews;
		this.size = mListViews.size();
	}

	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// container.removeView(mListViews.get(position%mListViews.size()));//删除页卡
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) { // 这个方法用来实例化页卡
		ViewParent vp = mListViews.get(position % mListViews.size())
				.getParent();
		if (vp != null) {
			ViewGroup parent = (ViewGroup) vp;
			parent.removeView(mListViews.get(position % mListViews.size()));
		}
		container.addView(mListViews.get(position % mListViews.size()), 0);// 添加页卡

		return mListViews.get(position % mListViews.size());
	}

	@Override
	public int getCount() {
		// return mListViews.size();//返回页卡的数量
		return size;// 返回页卡的数量
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;// 官方提示这样写
	}
}