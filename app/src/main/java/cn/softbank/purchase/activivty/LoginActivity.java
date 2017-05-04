package cn.softbank.purchase.activivty;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cn.softbank.purchase.base.BaseActivity;
import cn.softbank.purchase.base.MyApplication;
import cn.softbank.purchase.domain.LoginData;
import cn.softbank.purchase.fragment.MyFragment;
import cn.softbank.purchase.network.AbstractRequest.MamaHaoError;
import cn.softbank.purchase.network.BeanRequest;
import cn.softbank.purchase.network.NetworkManager;
import cn.softbank.purchase.network.ReqTag;
import cn.softbank.purchase.network.entity.DefaultMamahaoServerError;
import cn.softbank.purchase.network.entity.MamaHaoServerError;
import cn.softbank.purchase.utils.Constant;
import cn.softbank.purchase.utils.SharedPreference;
import cn.softbank.purchase.widget.MyCheckBox;
import cn.yicheng.jingjiren.R;

public class LoginActivity extends BaseActivity{

	private EditText et_phone;
	private EditText et_password;
	private Button bt_ok;
	private MyCheckBox checkbox;
	
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.login_activity);
		initTitleBar(getString(R.string.login), R.drawable.qgh_ic_quxiao, "");
		
		if(MyApplication.getInstance().isUserLogin()){
			jumpToNextActivity(MainActivity.class, true);
		}
	
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_password = (EditText) findViewById(R.id.et_password);
		
		checkbox = (MyCheckBox) findViewById(R.id.checkbox);
		findViewById(R.id.tv_argeement).setOnClickListener(this);
		checkbox.setOnClickListener(this);
		
		bt_ok = (Button) findViewById(R.id.bt_ok);
		bt_ok.setOnClickListener(this);
		findViewById(R.id.tv_hang_out).setOnClickListener(this);
		
		et_phone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				//监听输入框内容，改变button状态
				setButtonState(checkButtonIsOk());
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
		et_password.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				
				//监听输入框内容，改变button状态
				setButtonState(checkButtonIsOk());
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

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_hang_out:
			//随便逛逛
			jumpToNextActivity(MainActivity.class, true);
			break;
		case R.id.bt_ok:
//			MyApplication.getInstance().setMemberId("1");
//			finish();
			requestLogin(et_phone.getText().toString().trim(), et_password.getText().toString().trim());
			break;
		case R.id.tv_argeement:
			startActivity(new Intent(context, WebViewActivity.class).putExtra("title", "用户登录协议").putExtra("URI", Constant.URL_AGREEMENT));
			break;
		case R.id.checkbox:
			setButtonState(checkButtonIsOk());
			break;

		default:
			break;
		}
	}
	
	private void setButtonState(boolean isEnabled){
		if(isEnabled){
			bt_ok.setEnabled(true);
			bt_ok.setTextColor(Color.parseColor("#ffffff"));
			bt_ok.setBackgroundResource(R.drawable.bt_bottom_yellow);
		}else{
			bt_ok.setEnabled(false);
			bt_ok.setTextColor(Color.parseColor("#ffffff"));
			bt_ok.setBackgroundResource(R.drawable.bt_bottom_gray);
		}
	}
	
	private boolean checkButtonIsOk(){
		String phone = et_phone.getText().toString().trim();
		if(TextUtils.isEmpty(phone))
			return false;
		String psw = et_password.getText().toString().trim();
		if(TextUtils.isEmpty(psw))
			return false;
		if(isPhone(phone) && psw.length()>=6)
			return true;
		return checkbox.isChecked();
	}
	
	/**
	 * 验证手机格式
	 * 
	 * @param phone
	 * @return
	 */
	private boolean isPhone(String phone) {
		String regex = "^0?1[0-9]{10}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(phone);
		return matcher.matches();
	}
	
	/** 请求标识 **/
	private final int REQUEST_LOGIN = 0;
	
	/**
	 * 请求登录
	 */
	private void requestLogin(String account,String password) {
		showProgressBar(null);
		BeanRequest<LoginData, DefaultMamahaoServerError> req = new BeanRequest<LoginData, DefaultMamahaoServerError>(
                context,
                "", 
                this, LoginData.class,false);
		
		req.setParam("apiCode", "_user_login");
		// 登陆
		// $apiCode='_user_login';
		// $param = array();
		// $param['username'] = '14225252';
		// $param['userPwd'] = '123123';

		req.setParam("username", account);
		req.setParam("userPwd", password);
		//获取token
		String token = NetworkManager.getInstance().getPostToken(req.getParam());
		req.setParam("token", token);
		
		addRequestQueue(req, REQUEST_LOGIN, new ReqTag.Builder().handleSimpleRes(true));
			
	}
	
	@Override
	protected void onResponseSuccess(ReqTag tag, Object data) {
		// TODO Auto-generated method stub
		super.onResponseSuccess(tag, data);
		switch (tag.getReqId()) {
		case REQUEST_LOGIN:
			LoginData d = (LoginData)data;
			SharedPreference.saveUserData(context, d,true);
			SharedPreference.saveToSP(context, "phone", et_phone.getText().toString().trim());
			MyFragment.userInfoChange = true;
			MyFragment.orderInfoChange = true;
			jumpToNextActivity(MainActivity.class, true);
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
