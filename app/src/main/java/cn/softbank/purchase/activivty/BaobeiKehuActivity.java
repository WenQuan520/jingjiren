package cn.softbank.purchase.activivty;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import cn.softbank.purchase.base.BaseActivity;
import cn.softbank.purchase.base.MyApplication;
import cn.softbank.purchase.domain.Customer;
import cn.softbank.purchase.fragment.CustomerFragment;
import cn.softbank.purchase.fragment.MyFragment;
import cn.softbank.purchase.network.JsonElementRequest;
import cn.softbank.purchase.network.NetworkManager;
import cn.softbank.purchase.network.ReqTag;
import cn.softbank.purchase.network.AbstractRequest.MamaHaoError;
import cn.softbank.purchase.network.entity.DefaultMamahaoServerError;
import cn.softbank.purchase.network.entity.MamaHaoServerError;
import cn.softbank.purchase.utils.Constant;
import cn.softbank.purchase.widget.MyCheckBox;
import cn.yicheng.jingjiren.R;

public class BaobeiKehuActivity extends BaseActivity {

	private Customer customer;
	private MyCheckBox checkbox_boy;
	private MyCheckBox checkbox_girl;
	private TextView edit_address_name; 
	private TextView edit_phone; 
	
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_baobei_kehu);
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		
		initTitleBar("报备客户", Constant.DEFAULT_LEFT_BACK, "提交");
		
		customer = (Customer) getIntent().getSerializableExtra("data");
		checkbox_boy = (MyCheckBox) findViewById(R.id.checkbox_boy);
		checkbox_girl = (MyCheckBox) findViewById(R.id.checkbox_girl);
		edit_address_name = findTextView(R.id.edit_address_name);
		edit_phone = findTextView(R.id.edit_phone);
		
		
		if(customer!=null){
			edit_address_name.setText(customer.getName());
			edit_phone.setText(customer.getPhone());
			
			edit_address_name.setEnabled(false);
			edit_phone.setEnabled(false);
			
			if(!customer.getGender().equals("0")){
				checkbox_boy.setIsChecked(false);
			}else
				checkbox_girl.setIsChecked(false);
		}else{
			checkbox_girl.setIsChecked(false);
			findViewById(R.id.ll_boy).setOnClickListener(this);
			findViewById(R.id.ll_girl).setOnClickListener(this);
		}
		
		
		if(!TextUtils.isEmpty(getIntentExtra("loupanId"))){
			loupanId = getIntentExtra("loupanId");
			findTextView(R.id.edit_xiangmu).setText(getIntentExtra("loupanName"));
		}else
			findViewById(R.id.edit_xiangmu).setOnClickListener(this);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	private final static int RERUEST_CODE_LOUPAN = 10;
	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_boy:
			checkbox_boy.setIsChecked(true);
			checkbox_girl.setIsChecked(false);
			break;
		case R.id.ll_girl:
			checkbox_boy.setIsChecked(false);
			checkbox_girl.setIsChecked(true);
			break;
		case R.id.bt_title_right:
			requestCollect();
			break;
		case R.id.edit_xiangmu:
			startActivityForResult(new Intent(context, ChooseLoupanActivity.class),RERUEST_CODE_LOUPAN);
			break;

		default:
			break;
		}
	}
	
	private String loupanId;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		//重新请求数据
		if(data == null || resultCode != RESULT_OK)
			return;
		
		switch (requestCode) {
		case RERUEST_CODE_LOUPAN:
			//楼盘
			loupanId = data.getStringExtra("id");
			findTextView(R.id.edit_xiangmu).setText(data.getStringExtra("name"));
			break;

		default:
			break;
		}
	}
	
	private final int REQUEST_COLLECT = 2; 
	/**
	 * 收藏商品
	 */
	private void requestCollect(){
		CharSequence name = null;
		CharSequence phone = null;
		if(customer == null){
			name = edit_address_name.getText();
			if(TextUtils.isEmpty(name)){
				showToast("请输入用户 姓名");
				return;
			}
				
			phone = edit_phone.getText();
			if(TextUtils.isEmpty(phone)){
				showToast("请输入用户 手机号");
				return;
			}
		}else{
			name = customer.getName();
			phone = customer.getPhone();
		}
			
		
		
		showProgressBar(null);
		
		JsonElementRequest<DefaultMamahaoServerError> req = new JsonElementRequest<DefaultMamahaoServerError>(
				context,"",this);
		
		req.setParam("apiCode", "_customer_add");
		req.setParam("userId", MyApplication.getInstance().getMemberId());
		req.setParam("name", name.toString().trim());
		req.setParam("phone", phone.toString().trim());
		req.setParam("Beizhu", findTextView(R.id.edit_beizhu).getText().toString());
		req.setParam("project", loupanId);
		//跟进客户
//		$param['name'] = '张四';
//		$param['phone'] = '15911112425';
//		$param['Beizhu'] = '报备'; */
//		$param['project'] = '2';		//楼盘ID
		
		//获取token
		String token = NetworkManager.getInstance().getPostToken(req.getParam());
		req.setParam("token", token);
		
		addRequestQueue(req, REQUEST_COLLECT, new ReqTag.Builder().handleSimpleRes(true));
	}
	
	@Override
	protected void onResponseSuccess(ReqTag tag, Object data) {
		// TODO Auto-generated method stub
		super.onResponseSuccess(tag, data);
		switch (tag.getReqId()) {
		case REQUEST_COLLECT:
			showToast("已提交报备");
			MyFragment.orderInfoChange = true;
			CustomerFragment.userInfoChange = true;
			finish();
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onResponseFailure(ReqTag tag, MamaHaoServerError error,
			MamaHaoError clientError) {
		// TODO Auto-generated method stub
		super.onResponseFailure(tag, error, clientError);
	}

}
