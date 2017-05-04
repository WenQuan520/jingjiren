package cn.softbank.purchase.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import cn.yicheng.jingjiren.R;

/***
 * 自定义toast
 * @author borui
 *
 */
public class MyToast extends Toast{
	
	private LayoutInflater mInflater;
	
	private TextView text;
	
	private Context mContext;
	
	public MyToast(Context mContext) {
		super(mContext);
		this.mContext = mContext;
		this.mInflater = LayoutInflater.from(mContext);
		View viewGroup  = mInflater.inflate(R.layout.toast, null, false);
		text = (TextView)viewGroup.findViewById(R.id.tv_toast);
		this.setView(viewGroup);
	}

	@Override
	public void setText(CharSequence s) {
		// TODO Auto-generated method stub
		text.setText(s);
	}

	@Override
	public void setText(int resId) {
		// TODO Auto-generated method stub
		text.setText(mContext.getString(resId));
	}

}
