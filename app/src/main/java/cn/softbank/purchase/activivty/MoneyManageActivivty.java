package cn.softbank.purchase.activivty;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import cn.softbank.purchase.adapter.BaseViewHolder;
import cn.softbank.purchase.adapter.CommonAdapter;
import cn.softbank.purchase.base.BaseActivity;
import cn.softbank.purchase.base.MyApplication;
import cn.softbank.purchase.domain.MoneyData;
import cn.softbank.purchase.domain.MoneyDetailData;
import cn.softbank.purchase.network.BeanRequest;
import cn.softbank.purchase.network.NetworkManager;
import cn.softbank.purchase.network.ReqTag;
import cn.softbank.purchase.network.AbstractRequest.MamaHaoError;
import cn.softbank.purchase.network.entity.MamaHaoServerError;
import cn.softbank.purchase.utils.ArithmeticUtil;
import cn.softbank.purchase.utils.CommonUtils;
import cn.yicheng.jingjiren.R;

public class MoneyManageActivivty extends BaseActivity {

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_money_manage);
		
		findViewById(R.id.bt_title_left).setOnClickListener(this);
		findViewById(R.id.bt_ok).setOnClickListener(this);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		requestUserInfo();
	}

	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_title_left:
			finish();
			break;
		case R.id.bt_ok:
			int max = moneyData.getKetixian();
			if(max>0)
				startActivity(new Intent(context, UpdateNameActivity.class).putExtra("max", max));
			else
				showToast("没有可提现的金额");
			
			break;

		default:
			break;
		}
	}
	
	private final int REQUEST_USERINFOS = 0;
	private MoneyData moneyData;
	/**
	 * 请求订单数据
	 */
	private void requestUserInfo() {
		showProgressBar(null);
		
		BeanRequest<MoneyData, MamaHaoServerError> req = new BeanRequest<>(
				context, 
				"", 
				this, 
				MoneyData.class);
		
		req.setParam("apiCode", "_user_commission");
		req.setParam("userId", MyApplication.getInstance().getMemberId());
		
		//获取token
		String token = NetworkManager.getInstance().getPostToken(req.getParam());
		req.setParam("token", token);
		
		addRequestQueue(req, REQUEST_USERINFOS);
	}
	
	@Override
	protected void onResponseSuccess(ReqTag tag, Object data) {
		// TODO Auto-generated method stub
		super.onResponseSuccess(tag, data);
		switch (tag.getReqId()) {
		case REQUEST_USERINFOS:
			moneyData = (MoneyData)data;
			
			setText(R.id.tv_money, CommonUtils.getPriceFormatNo¥(ArithmeticUtil.div(moneyData.getKetixian()+"", "10000").doubleValue()));
			setText(R.id.tv_total, CommonUtils.getPriceFormatNo¥(ArithmeticUtil.div(moneyData.getCount()+"", "10000").doubleValue()));
			setText(R.id.tv_tixian_finish, CommonUtils.getPriceFormatNo¥(ArithmeticUtil.div(moneyData.getAlready()+"", "10000").doubleValue()));
			setText(R.id.tv_tixian_ing, CommonUtils.getPriceFormatNo¥(ArithmeticUtil.div(moneyData.getWithdrawalsIng()+"", "10000").doubleValue()));
			
			if(moneyData.getDetail()!=null && moneyData.getDetail().size()>0){
				ListView listview = (ListView) findViewById(R.id.listview);
				listview.setAdapter(new CommonAdapter<MoneyDetailData>(context, moneyData.getDetail(), R.layout.item_money) {

					@Override
					public void convert(BaseViewHolder holder,
							MoneyDetailData itemData, int position, ViewGroup parent) {
						// TODO Auto-generated method stub
						holder.setText(R.id.tv_title, itemData.getTitle(),"暂无信息");
						
						holder.setText(R.id.tv_sub_title, itemData.getDetail(),"暂无信息");
						holder.setText(R.id.tv_time, "成交："+itemData.getTime(),"暂无信息");
						holder.setText(R.id.tv_money, itemData.getFlowingWater());
						if(!TextUtils.isEmpty(itemData.getFlowingWater())){
							TextView tv_money = holder.getView(R.id.tv_money);
							if(itemData.getFlowingWater().startsWith("+")){
								tv_money.setTextColor(Color.parseColor("#14c431"));
							}else{
								tv_money.setTextColor(Color.parseColor("#ffcd00"));
							}
						}
						
					}
				});
			}
			
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
