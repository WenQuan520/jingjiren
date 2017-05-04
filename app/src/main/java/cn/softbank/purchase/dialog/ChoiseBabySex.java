package cn.softbank.purchase.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import cn.yicheng.jingjiren.R;

public class ChoiseBabySex extends Dialog implements
		View.OnClickListener {

	TextView tv_title;
	TextView tv_option1;
	TextView tv_option2;

	public ChoiseBabySex(Context context, int theme) {
		super(context, theme);
		Window window = getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.main_menu_animstyle);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = ((Activity) context).getWindowManager().getDefaultDisplay()
				.getHeight();
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

		// 设置显示位置
		onWindowAttributesChanged(wl);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_choisesex);
		initViews();
	}

	private void initViews() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_option1 = (TextView) findViewById(R.id.tv_option1);
		tv_option2 = (TextView) findViewById(R.id.tv_option2);
		tv_title.setOnClickListener(this);
		tv_option1.setOnClickListener(this);
		tv_option2.setOnClickListener(this);
	}

	@Override
	public void show() {
		super.show();
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
	}

	private OnForResultCallBack mOnForResultCallBack;

	public void setOnForResultCallBack(OnForResultCallBack mOnForResultCallBack) {
		this.mOnForResultCallBack = mOnForResultCallBack;
	}

	public interface OnForResultCallBack {
		public void onCallBack(int gender);
	}

	public void showToast(String msg) {
		Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// 选择性别
		switch (v.getId()) {
		case R.id.tv_option1:
			// 男
			if (mOnForResultCallBack != null) {
				mOnForResultCallBack.onCallBack(1);
				dismiss();
			}
			break;

		case R.id.tv_option2:
			// 女
			if (mOnForResultCallBack != null) {
				mOnForResultCallBack.onCallBack(2);
				dismiss();
			}
			break;

		default:
			break;
		}
	}
}