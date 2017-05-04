package cn.softbank.purchase.activivty;

import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import cn.softbank.purchase.base.BaseActivity;
import cn.softbank.purchase.base.MyApplication;
import cn.softbank.purchase.network.BeanRequest;
import cn.softbank.purchase.network.JsonElementRequest;
import cn.softbank.purchase.network.NetworkManager;
import cn.softbank.purchase.network.ReqTag;
import cn.softbank.purchase.network.entity.DefaultMamahaoServerError;
import cn.softbank.purchase.network.entity.MamaHaoServerError;
import cn.softbank.purchase.utils.CommonUtils;
import cn.softbank.purchase.utils.Constant;
import cn.softbank.purchase.widget.MyCheckBox;
import cn.yicheng.jingjiren.R;

public class AgreementActivity extends BaseActivity {

	private MyCheckBox checkbox_boy;
	private MyCheckBox checkbox_girl;
	private EditText edit_yifang;
	private EditText edit_id;
	private EditText edit_address;
	private EditText edit_phone;
	private EditText edit_yifang_sign;
	private EditText edit_id_sign;
	
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_agreement);
		initTitleBar("合作协议", Constant.DEFAULT_LEFT_BACK, "保存");
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		
		edit_yifang = (EditText) findViewById(R.id.edit_yifang);
		edit_id = (EditText) findViewById(R.id.edit_id);
		edit_address = (EditText) findViewById(R.id.edit_address);
		edit_phone = (EditText) findViewById(R.id.edit_phone);
		edit_yifang_sign = (EditText) findViewById(R.id.edit_yifang_sign);
		edit_id_sign = (EditText) findViewById(R.id.edit_id_sign);
		checkbox_boy = (MyCheckBox) findViewById(R.id.checkbox_boy);
		checkbox_girl = (MyCheckBox) findViewById(R.id.checkbox_girl);
		
		checkbox_boy.setAutoChange(true);
		checkbox_girl.setAutoChange(true);
		
		checkbox_boy.setIsChecked(false);
		checkbox_girl.setIsChecked(false);
		
		String from = CommonUtils.stampToDate2(System.currentTimeMillis());
		String end = (Integer.valueOf(from.substring(0,4))+1) + from.substring(4);
		
		findTextView(R.id.tv_agreement_con).setText(String.format(getString(R.string.agreenment), from+"起至"+end));
		findTextView(R.id.tv_time).setText(from);
		
		requestMsgDatas();
		
//		2016年10月1日起至2017年10月1日
		
	}
	
	private final int REQUEST_DATA = 1; 
	private final int REQUEST_COLLECT = 2; 
	
	/**
	 * 请求数据
	 */
	private void requestMsgDatas(){
		showProgressBar(null);
		BeanRequest<Contract, MamaHaoServerError> req = new BeanRequest<>(
				context, 
				"", 
				this, 
				Contract.class);
		
		req.setParam("apiCode", "_user_signing_info");
		req.setParam("userId", MyApplication.getInstance().getMemberId());
		
		//获取token
		String token = NetworkManager.getInstance().getPostToken(req.getParam());
		req.setParam("token", token);
		
		addRequestQueue(req, REQUEST_DATA);
		
	}
	
	/**
	 * 收藏商品
	 */
	private void requestCollect(){
		
		CharSequence name = edit_yifang_sign.getText();
		if(TextUtils.isEmpty(name)){
			showToast("请输入名字");
			return;
		}
		
		CharSequence id = edit_id_sign.getText();
		if(TextUtils.isEmpty(id)){
			showToast("请输入身份证号");
			return;
		}
		
		CharSequence address = edit_address.getText();
		if(TextUtils.isEmpty(address)){
			showToast("请输入联系地址");
			return;
		}
		
		CharSequence phone = edit_phone.getText();
		if(TextUtils.isEmpty(phone)){
			showToast("请输入手机号");
			return;
		}
		
		if((!checkbox_boy.isChecked()) && (!checkbox_girl.isChecked())){
			showToast("请选择业务模式");
			return;
		}
		
		showProgressBar(null);
		
		JsonElementRequest<DefaultMamahaoServerError> req = new JsonElementRequest<DefaultMamahaoServerError>(
				context,"",this);
		
		req.setParam("apiCode", "_user_signing");
		req.setParam("userId", MyApplication.getInstance().getMemberId());
		req.setParam("realname", name.toString().trim());
		req.setParam("license_no", address.toString().trim());
		req.setParam("card", id.toString().trim());
		req.setParam("telephone", phone.toString().trim());
		req.setParam("date_contract", CommonUtils.stampToDate2(System.currentTimeMillis()));
		req.setParam("isfulltime", checkbox_boy.isChecked()?"1":"0");
		
		
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
		case REQUEST_DATA:
			if(((Contract)data).getContract()!=null && !TextUtils.isEmpty(((Contract)data).getContract().getRealname())){
				//已经签约
				bt_title_right.setText("");
				SignUser signUser = ((Contract)data).getContract();
				
				edit_yifang.setText(signUser.getRealname());
				edit_yifang.setEnabled(false);
				
				edit_id.setText(signUser.getCard());
				edit_id.setEnabled(false);
				
				edit_address.setText(signUser.getLicense_no());
				edit_address.setEnabled(false);
				
				edit_phone.setText(signUser.getTelephone());
				edit_phone.setEnabled(false);
				
				edit_yifang_sign.setText(signUser.getRealname());
				edit_yifang_sign.setEnabled(false);
				
				edit_id_sign.setText(signUser.getCard());
				edit_id_sign.setEnabled(false);
				
				if("1".equals(signUser.getIsfulltime())){
					checkbox_boy.setIsChecked(true);
					checkbox_girl.setIsChecked(false);
				}else{
					checkbox_boy.setIsChecked(false);
					checkbox_girl.setIsChecked(true);
				}
				
				checkbox_boy.setAutoChange(false);
				checkbox_girl.setAutoChange(false);
				
				
				findTextView(R.id.tv_agreement_con).setText(String.format(getString(R.string.agreenment), signUser.getDate_start()+"起至"+signUser.getDate_end()));
				findTextView(R.id.tv_time).setText(signUser.getDate_start());
				
			}
			break;
		case REQUEST_COLLECT:
//			requestMsgDatas();
			showToast("提交成功");
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.bt_title_right)
			requestCollect();
	}
	
	public class Contract{
		private SignUser contract;

		public SignUser getContract() {
			return contract;
		}

		public void setContract(SignUser contract) {
			this.contract = contract;
		}
		
	}
	
	public class SignUser{
		private String card;
		private String date_contract;
		private String date_end;
		private String date_start;
		private String isfulltime;
		private String license_no;
		private String telephone;
		private String realname;
		public String getCard() {
			return card;
		}
		public void setCard(String card) {
			this.card = card;
		}
		public String getDate_contract() {
			return date_contract;
		}
		public void setDate_contract(String date_contract) {
			this.date_contract = date_contract;
		}
		public String getDate_end() {
			return date_end;
		}
		public void setDate_end(String date_end) {
			this.date_end = date_end;
		}
		public String getDate_start() {
			return date_start;
		}
		public void setDate_start(String date_start) {
			this.date_start = date_start;
		}
		public String getIsfulltime() {
			return isfulltime;
		}
		public void setIsfulltime(String isfulltime) {
			this.isfulltime = isfulltime;
		}
		public String getLicense_no() {
			return license_no;
		}
		public void setLicense_no(String license_no) {
			this.license_no = license_no;
		}
		public String getTelephone() {
			return telephone;
		}
		public void setTelephone(String telephone) {
			this.telephone = telephone;
		}
		public String getRealname() {
			return realname;
		}
		public void setRealname(String realname) {
			this.realname = realname;
		}

	}

}
