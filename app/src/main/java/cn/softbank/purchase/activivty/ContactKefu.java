package cn.softbank.purchase.activivty;

import android.view.View;
import cn.softbank.purchase.base.BaseActivity;
import cn.softbank.purchase.utils.CommonUtils;
import cn.softbank.purchase.utils.Constant;
import cn.yicheng.jingjiren.R;

public class ContactKefu extends BaseActivity {

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_contact_customer);
		
		initTitleBar(getString(R.string.contact_customer),
				Constant.DEFAULT_LEFT_BACK, null);
		findViewById(R.id.customer_service_phone_fram).setOnClickListener(this);
		findViewById(R.id.customer_service_online_lil).setOnClickListener(this);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.customer_service_phone_fram:
			// 客服热线
			CommonUtils.readyCall(this,
					getString(R.string.customer_service_phone_number));
			break;


		default:
			break;
		}
	}

}
