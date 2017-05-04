package cn.softbank.purchase.activivty;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
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

public class AddkehuActivity extends BaseActivity {

	private MyCheckBox checkbox_boy;
	private MyCheckBox checkbox_girl;
	
	private EditText edit_xiangmu;
	private EditText edit_area;
	private EditText edit_price;
	private EditText edit_fangxing;
	private EditText edit_mianji;
	
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_add_kehu);
		
		Customer customer = (Customer) getIntent().getSerializableExtra("data");
		
		initTitleBar(customer==null?"添加客户":"客户信息", Constant.DEFAULT_LEFT_BACK, customer==null?"完成":null);
		
		checkbox_boy = (MyCheckBox) findViewById(R.id.checkbox_boy);
		checkbox_girl = (MyCheckBox) findViewById(R.id.checkbox_girl);
		
		edit_xiangmu = (EditText) findViewById(R.id.edit_xiangmu);
		edit_area = (EditText) findViewById(R.id.edit_area);
		edit_price = (EditText) findViewById(R.id.edit_price);
		edit_fangxing = (EditText) findViewById(R.id.edit_fangxing);
		edit_mianji = (EditText) findViewById(R.id.edit_mianji);
		
		if(customer == null){
			checkbox_girl.setIsChecked(false);
			findViewById(R.id.tv_boy).setOnClickListener(this);
			findViewById(R.id.tv_girl).setOnClickListener(this);
			checkbox_boy.setOnClickListener(this);
			checkbox_girl.setOnClickListener(this);
			edit_xiangmu.setOnClickListener(this);
			edit_area.setOnClickListener(this);
			edit_price.setOnClickListener(this);
			edit_fangxing.setOnClickListener(this);
			edit_mianji.setOnClickListener(this);
		}else{
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
			
			if(customer.getGender().equals("1")){
				checkbox_boy.setIsChecked(false);
				checkbox_girl.setIsChecked(true);
			}else
				checkbox_girl.setIsChecked(false);
			
			findTextView(R.id.edit_address_name).setEnabled(false);
			findTextView(R.id.edit_phone).setEnabled(false);
			findTextView(R.id.edit_beizhu).setEnabled(false);
			setText(R.id.edit_address_name, customer.getName());
			setText(R.id.edit_phone, customer.getPhone());
			
			setText(R.id.edit_beizhu, customer.getAsk(),"暂无");
			setText(R.id.edit_xiangmu, customer.getYixiang(),"暂无");
			setText(R.id.edit_area, customer.getRegion(),"暂无");
			setText(R.id.edit_price, customer.getPrice(),"暂无");
			setText(R.id.edit_fangxing, customer.getType(),"暂无");
			setText(R.id.edit_mianji, customer.getArea(),"暂无");
		}
		
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	private final static int RERUEST_CODE_LOUPAN = 10;
	private final static int RERUEST_CODE_AREA = 20;
	private final static int RERUEST_CODE_PRICE = 30;
	private final static int RERUEST_CODE_FANGXING = 40;
	private final static int RERUEST_CODE_ACREAGE = 50;
	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_boy:
			checkbox_boy.setIsChecked(true);
			checkbox_girl.setIsChecked(false);
			break;
		case R.id.tv_girl:
			checkbox_boy.setIsChecked(false);
			checkbox_girl.setIsChecked(true);
			break;
		case R.id.checkbox_boy:
			checkbox_boy.setIsChecked(true);
			checkbox_girl.setIsChecked(false);
			break;
		case R.id.checkbox_girl:
			checkbox_boy.setIsChecked(false);
			checkbox_girl.setIsChecked(true);
			break;
		case R.id.edit_xiangmu:
			startActivityForResult(new Intent(context, ChooseLoupanActivity.class),RERUEST_CODE_LOUPAN);
			break;
		case R.id.edit_area:
			startActivityForResult(new Intent(context, ChooseAreaActivity.class),RERUEST_CODE_AREA);
			break;
		case R.id.edit_price:
			startActivityForResult(new Intent(context, ChoosePriceActivity.class),RERUEST_CODE_PRICE);
			break;
		case R.id.edit_fangxing:
			startActivityForResult(new Intent(context, ChooseFangxingActivity.class),RERUEST_CODE_FANGXING);
			break;
		case R.id.edit_mianji:
			startActivityForResult(new Intent(context, ChoosePriceActivity.class).putExtra("chooseAcreage", true),RERUEST_CODE_ACREAGE);
			break;
		case R.id.bt_title_right:
			requestCollect();
			break;

		default:
			break;
		}
	}
	
	private String loupanId;
	private String area;
	private String town;
	private String price;
	private String buildingType;
	private String houseType;
	private String acreage;
	
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
			edit_xiangmu.setText(data.getStringExtra("name"));
			break;
		case RERUEST_CODE_AREA:
			//区域
			area = data.getStringExtra("area");
			town = data.getStringExtra("town");
			if(!TextUtils.isEmpty(town))
				edit_area.setText(area+"—"+town);
			else
				edit_area.setText(area);
			break;
		case RERUEST_CODE_PRICE:
			//价格
			price = data.getStringExtra("name");
			edit_price.setText(price);
			break;
		case RERUEST_CODE_FANGXING:
			//房型
			buildingType = data.getStringExtra("buildingType");
			houseType = data.getStringExtra("houseType");
			if(!TextUtils.isEmpty(houseType))
				edit_fangxing.setText(buildingType+"-"+houseType);
			else
				edit_fangxing.setText(buildingType);
			break;
		case RERUEST_CODE_ACREAGE:
			//价格
			acreage = data.getStringExtra("name");
			edit_mianji.setText(acreage);
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
		CharSequence name = findTextView(R.id.edit_address_name).getText();
		if(TextUtils.isEmpty(name)){
			showToast("请输入客户姓名");
			return;
		}
		
		CharSequence phone = findTextView(R.id.edit_phone).getText();
		if(TextUtils.isEmpty(name)){
			showToast("请输入客户手机号");
			return;
		}
		
		showProgressBar(null);
		
		JsonElementRequest<DefaultMamahaoServerError> req = new JsonElementRequest<DefaultMamahaoServerError>(
				context,"",this);
		
		req.setParam("apiCode", "_customer_add");
		req.setParam("userId", MyApplication.getInstance().getMemberId());
		req.setParam("name", name.toString().trim());
		req.setParam("phone", phone.toString().trim());
		req.setParam("gender", checkbox_boy.isChecked()?"0":"1");
		req.setParam("project", loupanId);
		req.setParam("city", MyApplication.getInstance().getCity());
		req.setParam("area", area);
		req.setParam("town", town);
		req.setParam("price", price);
		req.setParam("buildingType", buildingType);
		req.setParam("houseType", houseType);
		req.setParam("acreage", acreage);
		req.setParam("ask", findTextView(R.id.edit_beizhu).getText()==null?"":findTextView(R.id.edit_beizhu).getText().toString().trim());
		//添加备注客户
		/* $apiCode='_customer_add';
		$param = array();
		$param['userId'] = '118';
		$param['name'] = '张四';
		$param['phone'] = '15911112425';
		$param['gender'] = '1';
		$param['project'] = '2';		//楼盘ID
		$param['city']	= '杭州市';
		$param['area'] = '上城区';
		$param['town'] = '清波街道';
		$param['price'] = '100-120万';
		$param['buildingType'] = '住宅';
		$param['houseType'] = '三房';
		$param['acreage'] = '90-110平米';
		$param['ask'] = '无';
		$param['Beizhu'] = '报备'; */

		
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
			showToast("添加成功");
			CustomerFragment.userInfoChange = true;
			MyFragment.orderInfoChange = true;
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
