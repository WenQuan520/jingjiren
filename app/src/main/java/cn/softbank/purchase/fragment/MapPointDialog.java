package cn.softbank.purchase.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import cn.yicheng.jingjiren.R;

public class MapPointDialog extends Dialog implements OnClickListener {

	private TextView tv_address;
	private TextView tv_disance;

	private TextView tv_kuai;
	private TextView tv_man;
	private TextView tv_daoh;

	private TextView tv_tip;
	private FrameLayout fra_details;
	private FrameLayout fra_oppoint;

	public MapPointDialog(Context context) {
		super(context, R.style.dialog_re);
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
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_choisesex);
		initViews();
	}

	private void initViews() {
	}

	@Override
	public void show() {
		super.show();
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_add_num:
			//
			dismiss();
			break;

		default:
			break;
		}
	}

}
