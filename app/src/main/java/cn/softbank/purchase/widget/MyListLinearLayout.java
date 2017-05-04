package cn.softbank.purchase.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.LinearLayout;

public class MyListLinearLayout extends LinearLayout {
	
	private AdapterView.OnItemClickListener mListener;

	public MyListLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyListLinearLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void setAdapter(Adapter adapter) {
		initViews(adapter);
	}

	private void initViews(Adapter adapter) {
		// TODO Auto-generated method stub
		removeAllViewsInLayout();
		for (int i = 0; i < adapter.getCount(); i++) {
			View viewItems =  adapter.getView(i,getChildAt(i), this);
			final int position = i;
			if(mListener!=null){
				viewItems.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mListener.onItemClick(null, v, position, (long)1);
					}
				});
			}
			addView(viewItems);
		}
	}
	
	public void setVisibleItems(int visible) {
		for (int i = 0; i < getChildCount(); i++) {
			if (i != 0) {
				getChildAt(i).setVisibility(visible);
			}
		}
	}
	
	public void setOnItemClickListener(AdapterView.OnItemClickListener mListener){
		this.mListener = mListener;
	}
}
