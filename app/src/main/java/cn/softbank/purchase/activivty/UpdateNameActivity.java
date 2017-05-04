package cn.softbank.purchase.activivty;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cn.softbank.purchase.base.BaseActivity;
import cn.softbank.purchase.base.MyApplication;
import cn.softbank.purchase.network.JsonElementRequest;
import cn.softbank.purchase.network.NetworkManager;
import cn.softbank.purchase.network.ReqTag;
import cn.softbank.purchase.network.entity.DefaultMamahaoServerError;
import cn.softbank.purchase.utils.Constant;
import cn.softbank.purchase.utils.SharedPreference;
import cn.yicheng.jingjiren.R;

public class UpdateNameActivity extends BaseActivity {

	private EditText et_name;
	private Button bt_ok;
	private int max;
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_update_name);
		initTitleBar("提现", Constant.DEFAULT_LEFT_BACK, null);
		
		max = getIntentExtra("max", 0);
		
		setText(R.id.tv_max, "可提现最大金额为："+max+"元");
		setText(R.id.tv_tixian_account, SharedPreference.getString(context, "alipay"),"暂无信息");
		
		bt_ok = (Button) findViewById(R.id.bt_ok);
		et_name = (EditText) findViewById(R.id.et_name);
		
		if(TextUtils.isEmpty(SharedPreference.getString(context, "alipay"))){
			et_name.setEnabled(false);
		}
		
		bt_ok.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		et_name.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				//监听输入框内容，改变button状态
				if(!TextUtils.isEmpty(et_name.getText().toString().trim())){
					int putMoney = Integer.valueOf(et_name.getText().toString().trim());
					if(putMoney>max){
						et_name.setText(max+"");
						et_name.setSelection((max+"").length());
						return;
					}
				}
				setButtonState(!TextUtils.isEmpty(et_name.getText().toString().trim()));
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	private void setButtonState(boolean isEnabled){
		if(isEnabled){
			bt_ok.setEnabled(true);
			bt_ok.setBackgroundResource(R.drawable.bt_bottom_yellow);
		}else{
			bt_ok.setEnabled(false);
			bt_ok.setBackgroundResource(R.drawable.bt_bottom_gray);
		}
	}

	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.bt_ok:
				//返回结果
				requestCollect(et_name.getText().toString().trim());
				finish();
				break;
			
			default:
				break;
		}
	}
	
	private final int REQUEST_COLLECT = 2;
	/**
	 * 收藏商品
	 */
	private void requestCollect(String money){
		showProgressBar(null);
		
		JsonElementRequest<DefaultMamahaoServerError> req = new JsonElementRequest<DefaultMamahaoServerError>(
				context,"",this);
		
		req.setParam("apiCode", "_user_commission_extract");
		req.setParam("userId", MyApplication.getInstance().getMemberId());
		req.setParam("tixianPrice", money);
		
		//获取token
		String token = NetworkManager.getInstance().getPostToken(req.getParam());
		req.setParam("token", token);
		
		addRequestQueue(req, REQUEST_COLLECT, new ReqTag.Builder().handleSimpleRes(true));
	}
	
	@Override
	protected void onResponseSuccess(ReqTag tag, Object data) {
		// TODO Auto-generated method stub
		super.onResponseSuccess(tag, data);
		showToast("已提交申请");
		finish();
	}

}
