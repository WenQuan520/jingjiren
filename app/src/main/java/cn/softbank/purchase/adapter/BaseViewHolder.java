package cn.softbank.purchase.adapter;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * @描述:通用的布局适配器ViewHolder
 * @Copyright Copyright (c) 2016
 * @Company 杭州百米贩科技公司.
 * @author GXH
 * @version 1.0
 */
public class BaseViewHolder {
	private final SparseArray<View> mViews;
	private View mConvertView;

	private BaseViewHolder(Context context, ViewGroup parent, int layoutId) {
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
				false);
		// setTag
		mConvertView.setTag(this);
	}

	/**
	 * 拿到一个ViewHolder对象
	 * 
	 * @param context
	 * @param convertView
	 * @param parent
	 * @param layoutId
	 * @param position
	 * @return
	 */
	public static BaseViewHolder getHolder(Context context, View convertView,
			ViewGroup parent, int layoutId) {
		if (convertView == null) {
			return new BaseViewHolder(context, parent, layoutId);
		}
		return (BaseViewHolder) convertView.getTag();
	}

	public View getConvertView() {
		return mConvertView;
	}

	/**
	 * 通过控件的Id获取对于的控件，如果没有则加入views
	 * 
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	/**
	 * 为TextView设置字符串
	 * 
	 * @param viewId
	 * @param text
	 * @return
	 */
	public BaseViewHolder setText(int viewId, CharSequence text) {
		if(text==null)
			text = "";
		TextView view = getView(viewId);
		view.setText(TextUtils.isEmpty(text)?"":text);
		return this;
	}
	
	/**
	 * 为TextView设置字符串
	 * 
	 * @param viewId
	 * @param text
	 * @return
	 */
	public BaseViewHolder setText(int viewId, CharSequence text,CharSequence nullStr) {
		TextView view = getView(viewId);
		view.setText(TextUtils.isEmpty(text)?nullStr:text);
		return this;
	}
	
	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public BaseViewHolder setImageResource(int viewId, int drawableId) {
		ImageView view = getView(viewId);
		view.setImageResource(drawableId);
		return this;
	}

	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public BaseViewHolder setImageBitmap(int viewId, Bitmap bm) {
		ImageView view = getView(viewId);
		view.setImageBitmap(bm);
		return this;
	}

	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @param url
	 *            image地址 String imageUri = "http://site.com/image.png"; // from Web 
	 *            String imageUri = "file:///mnt/sdcard/image.png"; // from SD card 
	 *            String imageUri = "content://media/external/audio/albumart/13"; // from content
	 *            
	 *            provider String imageUri = "assets://image.png"; // from
	 *            assets String imageUri = "drawable://" + R.drawable.image; //
	 *            from drawables (only images, non-9patch)
	 * @param options
	 *            ImageLoader配置参数
	 * @return
	 */
	public BaseViewHolder setImageByUrl(int viewId, String url,
			DisplayImageOptions options) {
		ImageView imgView = getView(viewId);
		ImageLoader.getInstance().displayImage(url, imgView, options);
		return this;
	}
	
	public BaseViewHolder setImageByUrl(ImageView imgView, String url,
			DisplayImageOptions options) {
		ImageLoader.getInstance().displayImage(url, imgView, options);
		return this;
	}
	
	public BaseViewHolder setImageByUrl(int viewId, String url) {
		ImageLoader.getInstance().displayImage(url, (ImageView)getView(viewId));
		return this;
	}
	
	public BaseViewHolder setImageByResource(int viewId, int drawableId,
			DisplayImageOptions options) {
		ImageView imgView = getView(viewId);
		if(options==null)
			ImageLoader.getInstance().displayImage("drawable://" + drawableId, imgView);
		else
			ImageLoader.getInstance().displayImage("drawable://" + drawableId, imgView,options);
		return this;
	}

	public BaseViewHolder setImageBySDcard(int viewId, String url,
			DisplayImageOptions options) {
		ImageView imgView = getView(viewId);
		if(options==null)
			ImageLoader.getInstance().displayImage("file://"+url, imgView);
		else
			ImageLoader.getInstance().displayImage("file://"+url, imgView,options);
		return this;
	}
	
}
