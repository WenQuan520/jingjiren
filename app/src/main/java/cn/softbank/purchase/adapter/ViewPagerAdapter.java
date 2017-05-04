package cn.softbank.purchase.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerAdapter extends PagerAdapter {
	
	private ArrayList<View> views;
	
	public ViewPagerAdapter(ArrayList<View> views) {
		super();
		this.views = views;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return views.size();
	}

	// �Ƿ���ͬһ��ͼƬ
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup view, int position, Object object) {
		// TODO Auto-generated method stub
		view.removeView(views.get(position));

	}

	@Override
	public Object instantiateItem(ViewGroup view, int position) {
		// TODO Auto-generated method stub
		view.addView(views.get(position));
		return views.get(position);
	}
}
