package cn.softbank.purchase.activivty;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import cn.softbank.purchase.adapter.BaseViewHolder;
import cn.softbank.purchase.adapter.CommonAdapter;
import cn.softbank.purchase.base.BaseActivity;
import cn.softbank.purchase.base.MyApplication;
import cn.softbank.purchase.dialog.FollowDialog;
import cn.softbank.purchase.dialog.FollowDialog.HuxingDialogClickListener;
import cn.softbank.purchase.domain.Customer;
import cn.softbank.purchase.domain.GenjinInfo;
import cn.softbank.purchase.network.JsonElementRequest;
import cn.softbank.purchase.network.NetworkManager;
import cn.softbank.purchase.network.ReqTag;
import cn.softbank.purchase.network.AbstractRequest.MamaHaoError;
import cn.softbank.purchase.network.entity.DefaultMamahaoServerError;
import cn.softbank.purchase.network.entity.MamaHaoServerError;
import cn.softbank.purchase.utils.CommonUtils;
import cn.softbank.purchase.utils.Constant;
import cn.yicheng.jingjiren.R;

public class KehuDetailActivity extends BaseActivity {

	private Customer customer;
	private List<GenjinInfo> genjinInfo = new ArrayList<GenjinInfo>();
	private CommonAdapter<GenjinInfo> adapter;

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_kehu_detail);
		initTitleBar("客户详情", Constant.DEFAULT_LEFT_BACK, null);
		
		customer = (Customer) getIntent().getSerializableExtra("data");
		
		setText(R.id.tv_name, customer.getName());
		setText(R.id.tv_phone, customer.getPhone());
		
		if(customer.getGenjinInfo()!=null && customer.getGenjinInfo().size()>0){
			findViewById(R.id.no_mes).setVisibility(View.GONE);
			genjinInfo.addAll(customer.getGenjinInfo());
		}
		
		adapter = new CommonAdapter<GenjinInfo>(context, genjinInfo, R.layout.item_genjin) {

			@Override
			public void convert(BaseViewHolder holder, GenjinInfo itemData,
					int position, ViewGroup parent) {
				// TODO Auto-generated method stub
				holder.setText(R.id.tv_info, itemData.getInfo());
				holder.setText(R.id.tv_name, itemData.getGenjinren());
				holder.setText(R.id.tv_time, CommonUtils.stampToDate(itemData.getTime()));
			}
		}; 
		
		ListView listview = (ListView) findViewById(R.id.listview);
		listview.setAdapter(adapter);
		
		findViewById(R.id.view_kehu).setOnClickListener(this);
		findViewById(R.id.tab_home).setOnClickListener(this);
		findViewById(R.id.tab_cart).setOnClickListener(this);
		findViewById(R.id.tab_my).setOnClickListener(this);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tab_home:
			new FollowDialog(context, new HuxingDialogClickListener() {
				
				@Override
				public void onYesOrNoDialogClick(String info) {
					// TODO Auto-generated method stub
					requestCollect(info);
				}
			}).show();
			break;
		case R.id.tab_cart:
			CommonUtils.readyCall(context, customer.getPhone());
			break;
		case R.id.view_kehu:
			startActivity(new Intent(context,AddkehuActivity.class).putExtra("data", customer));
			break;
		case R.id.tab_my:
			switch (getIntentExtra("type", MyKehuActitvty.KEHU_FRAGMENT_WAIT_BAOBEI)) {
			case MyKehuActitvty.KEHU_FRAGMENT_WAIT_BAOBEI:
				startActivity(new Intent(context,BaobeiKehuActivity.class).putExtra("data", customer));
				break;
			case MyKehuActitvty.KEHU_FRAGMENT_WAIT_LOOK:
				startActivity(new Intent(context,ApplyLookActivity.class).putExtra("id", customer.getCustomerId()));
				break;
			case MyKehuActitvty.KEHU_FRAGMENT_WAIT_DEAL:
				startActivity(new Intent(context,ApplyDealActivity.class).putExtra("id", customer.getCustomerId()));
				break;

			default:
				break;
			}
			
			
			break;

		default:
			break;
		}
	}
	
	private final int REQUEST_COLLECT = 2; 
	private String info;
	/**
	 * 收藏商品
	 */
	private void requestCollect(String info){
		showProgressBar(null);
		
		JsonElementRequest<DefaultMamahaoServerError> req = new JsonElementRequest<DefaultMamahaoServerError>(
				context,"",this);
		
		req.setParam("apiCode", "_customer_follow");
		req.setParam("userId", MyApplication.getInstance().getMemberId());
		req.setParam("customerId", customer.getCustomerId());
		req.setParam("info", info);
		this.info = info;
		//跟进客户
		// $apiCode='_customer_follow';
		// $param = array();
		// $param['userId'] = '118';
		// $param['customerId'] = '5';
		// $param['info'] = '暂不考虑暂不考虑';
		
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
			GenjinInfo genjin = new GenjinInfo();
			genjin.setGenjinren(customer.getName());
			genjin.setInfo(info);
			genjin.setTime(System.currentTimeMillis()+"");
			genjinInfo.add(genjin);
			adapter.notifyDataSetChanged();
			findViewById(R.id.no_mes).setVisibility(View.GONE);
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
