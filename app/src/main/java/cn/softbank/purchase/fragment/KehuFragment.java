package cn.softbank.purchase.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.softbank.purchase.activivty.KehuDetailActivity;
import cn.softbank.purchase.adapter.BaseViewHolder;
import cn.softbank.purchase.adapter.CommonAdapter;
import cn.softbank.purchase.base.BaseFragment;
import cn.softbank.purchase.base.MyApplication;
import cn.softbank.purchase.domain.Customer;
import cn.softbank.purchase.domain.CustomerList;
import cn.softbank.purchase.network.BeanRequest;
import cn.softbank.purchase.network.NetworkManager;
import cn.softbank.purchase.network.ReqTag;
import cn.softbank.purchase.network.AbstractRequest.MamaHaoError;
import cn.softbank.purchase.network.entity.MamaHaoServerError;
import cn.softbank.purchase.utils.CommonUtils;
import cn.softbank.purchase.utils.Constant;
import cn.yicheng.jingjiren.R;

public abstract class KehuFragment extends BaseFragment {

	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.my_kehu_fragment, container, false);
	}
	
	
	
	@Override
	protected void initData(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}
	
	@Override
	protected void onFirstUserVisible() {
		// TODO Auto-generated method stub
		super.onFirstUserVisible();
		requestGoodsDatas();
	}
	
	protected abstract int getType();
	
	public void refreshDatas(){
		requestGoodsDatas();
	}
	
	/** 请求标识 **/
	private final int REQUEST_GOODS_DATAS = 0;
	private void requestGoodsDatas() {
		// TODO Auto-generated method stub
		baseActivity.showProgressBar(this);
		BeanRequest<CustomerList, MamaHaoServerError> req = new BeanRequest<>(
				context, 
				"", 
				this, 
				CustomerList.class);
		
		req.setParam("apiCode", "_customer_list");
		req.setParam("userId", MyApplication.getInstance().getMemberId());
		req.setParam("segmentId", getType()+"");
		
//		req.setParam("name", String.valueOf(pageSize));
		
		//获取token
		String token = NetworkManager.getInstance().getPostToken(req.getParam());
		req.setParam("token", token);
		
		baseActivity.addRequestQueue(req, REQUEST_GOODS_DATAS);
	
	}
	
	@Override
	protected void onResponseSuccess(ReqTag tag, final Object data) {
		// TODO Auto-generated method stub
		super.onResponseSuccess(tag, data);
		switch (tag.getReqId()) {
		case REQUEST_GOODS_DATAS:
			//普通商品列表
//			handleDatas((List<HomeGoodsDatas>)data);
			if(((CustomerList)data)!=null && ((CustomerList)data).getCustomerList()!=null && ((CustomerList)data).getCustomerList().size()>0){
				ListView listview = (ListView) findView(R.id.listview);
				listview.setAdapter(new CommonAdapter<Customer>(baseActivity, ((CustomerList)data).getCustomerList(), R.layout.item_my_kehu) {

					@Override
					public void convert(BaseViewHolder holder, Customer itemData,
							int position, ViewGroup parent) {
						// TODO Auto-generated method stub
						holder.setText(R.id.tv_name, itemData.getName());
						//客户意向
						String yixiang = itemData.getYixiang();
						if(TextUtils.isEmpty(yixiang))
							yixiang = "暂无意向";
						holder.setText(R.id.tv_yixiang, "客户意向："+yixiang);
						
						if(itemData.getZuihougenjin()<=0)
							holder.setText(R.id.tv_follow, "最后跟进：暂无信息");
						else
							holder.setText(R.id.tv_follow, "最后跟进："+CommonUtils.stampToDate(itemData.getZuihougenjin()));
						
						addClickListener(holder.getView(R.id.iv_call), position);
					}
					
					@Override
					public void processClick(int viewId, Customer itemData) {
						// TODO Auto-generated method stub
						super.processClick(viewId, itemData);
						if(TextUtils.isEmpty(itemData.getPhone()))
							baseActivity.showToast("暂无联系方式");
						else
							CommonUtils.readyCall(context, itemData.getPhone());
					}
				});
				
				listview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						startActivity(new Intent(context, KehuDetailActivity.class).putExtra("data", (((CustomerList)data).getCustomerList()).get(position)).putExtra("type", getType()));
					}
				});
				
			}else{
				baseActivity.showBlankPage(this, R.drawable.img_error_page_orders, "暂无客户信息~", null,Constant.DEFAULT_TOP_MARGIN);
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
		baseActivity.showBlankPage(this, R.drawable.img_error_page_orders, "暂无客户信息~", null,Constant.DEFAULT_TOP_MARGIN);
	}

	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub

	}

}
