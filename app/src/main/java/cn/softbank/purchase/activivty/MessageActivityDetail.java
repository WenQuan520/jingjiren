package cn.softbank.purchase.activivty;

import android.view.View;
import cn.softbank.purchase.base.BaseActivity;
import cn.softbank.purchase.utils.Constant;
import cn.yicheng.jingjiren.R;

public class MessageActivityDetail extends BaseActivity {

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_message_detail);
		initTitleBar("消息详情", Constant.DEFAULT_LEFT_BACK, null);
		
		setText(R.id.tv_content, getIntentExtra("data"));
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub

	}

}
