package cn.softbank.purchase.activivty;

import android.view.View;
import cn.softbank.purchase.base.BaseActivity;
import cn.softbank.purchase.fragment.HomeFragment;
import cn.softbank.purchase.utils.Constant;
import cn.yicheng.jingjiren.R;

public class ChooseLoupanActivity extends BaseActivity {

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.shopping_cart);
		
		initTitleBar(getIntentExtra("key")==null?"选择楼盘":"", Constant.DEFAULT_LEFT_BACK, null);
		
		getSupportFragmentManager().beginTransaction()
		.add(R.id.fragment_container, new HomeFragment()).commit();
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
