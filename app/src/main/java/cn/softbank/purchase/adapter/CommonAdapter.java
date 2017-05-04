package cn.softbank.purchase.adapter;

import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
/**
 * @描述:通用的布局适配器
 * @Copyright Copyright (c) 2016
 * @Company 杭州百米贩科技公司.
 * @author GXH
 * @version 1.0
 */
public abstract class CommonAdapter<T> extends BaseAdapter implements OnClickListener{
	protected Context mContext;
	protected List<T> mDatas;
	protected final int mItemLayoutId;
	protected  int count;
	
	public CommonAdapter(Context context, List<T> mDatas, int itemLayoutId) {
		this.mContext = context;
		this.mDatas = mDatas;
		this.mItemLayoutId = itemLayoutId;
	}
	public CommonAdapter(Context context, List<T> mDatas, int itemLayoutId,int count) {
		this.mContext = context;
		this.mDatas = mDatas;
		this.mItemLayoutId = itemLayoutId;
		this.count=count;
	}



	public void setData(List<T> mDatas){
		if(null != mDatas){
			this.mDatas=mDatas;
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
//		if(mDatas!= null && mDatas.size()<=6){
//			return mDatas.size();
//		}else if(count==Integer.MAX_VALUE){
//			return count;
//		}else{
//			return mDatas.size();
//		}
		if(count==Integer.MAX_VALUE){
			return count;
		}
		
		if(mDatas == null){
			return 0;
		}
		
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final BaseViewHolder viewHolder = getViewHolder(position,
				convertView, parent);
		// 将viewHolder，item返回给调用者去处理
		if(count==Integer.MAX_VALUE&&position>=mDatas.size()){
			convert(viewHolder, getItem(position%mDatas.size()), position%mDatas.size(), parent);
		}else{
			convert(viewHolder, getItem(position), position, parent);
		}
		return viewHolder.getConvertView();

	}

	public abstract void convert(BaseViewHolder holder, T itemData,
			int position, ViewGroup parent);

	private BaseViewHolder getViewHolder(int position, View convertView,
			ViewGroup parent) {
		return BaseViewHolder.getHolder(mContext, convertView, parent,
				mItemLayoutId);
	}
	
	/**
	 * 给控件添加点击事件
	 * @param v
	 * @param position
	 */
	public void addClickListener(View v,int position){
		v.setTag(getItem(position));
		v.setOnClickListener(this);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onClick(View v) {
		if(v.getTag() != null)
			processClick(v.getId(), (T)v.getTag());
	}
	
	public void processClick(int viewId,T itemData){}
	
}
