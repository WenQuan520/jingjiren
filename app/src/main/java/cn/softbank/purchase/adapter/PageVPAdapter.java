package cn.softbank.purchase.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class PageVPAdapter extends FragmentPagerAdapter {

	private String[] titles;
	private Fragment[] fragments;

	public PageVPAdapter(FragmentManager fm, Fragment[] fragments) {
		super(fm);
		this.fragments = fragments;
	}
	
	public PageVPAdapter(FragmentManager fm, String[] titles, Fragment[] fragments) {
		super(fm);
		this.fragments = fragments;
		this.titles = titles;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		if(titles!=null)
			return titles[position];
		return "";
	}
	
	@Override
	public Fragment getItem(int position) {
		return fragments[position];
	}

	@Override
	public int getCount() {
		return fragments.length;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// avoid fragment recreate
		// super.destroyItem(container, position, object);
	}

	@Override
	public void startUpdate(ViewGroup container) {
		// TODO Auto-generated method stub
		super.startUpdate(container);
	}

}
