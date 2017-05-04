package cn.softbank.purchase.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.softbank.purchase.activivty.AddkehuActivity;
import cn.softbank.purchase.adapter.PageVPAdapter;
import cn.softbank.purchase.base.BaseFragment;
import cn.softbank.purchase.base.MyApplication;
import cn.softbank.purchase.domain.Segment;
import cn.softbank.purchase.domain.SegmentList;
import cn.softbank.purchase.network.AbstractRequest.MamaHaoError;
import cn.softbank.purchase.network.BeanRequest;
import cn.softbank.purchase.network.NetworkManager;
import cn.softbank.purchase.network.ReqTag;
import cn.softbank.purchase.network.entity.MamaHaoServerError;
import cn.softbank.purchase.utils.CommonUtils;
import cn.softbank.purchase.widget.pagerindicator.TabPageIndicator;
import cn.yicheng.jingjiren.R;

public class CustomerFragment extends BaseFragment {

	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_customer, container, false);
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}
	
	/** fragment创建标志 **/
	private boolean isShow;
	public static boolean userInfoChange = false;
	@Override
	protected void onFirstUserVisible() {
		// TODO Auto-generated method stub
		super.onFirstUserVisible();
		
		findView(R.id.bt_title_left).setVisibility(View.INVISIBLE);
		findTextView(R.id.title_name).setText("我的客户");
		TextView bt_title_right = findTextView(R.id.bt_title_right);
		bt_title_right.setVisibility(View.VISIBLE);
		bt_title_right.setCompoundDrawables(
				CommonUtils.GetDrawable(context, R.drawable.jjr_ic_add), null,
				null, null);
		bt_title_right.setOnClickListener(this);
		
		isShow = true;
		requestGoodsDatas();
	}

	private KehuFragment[] fargments;
	private ViewPager pager;
	
	private void init(String[] titles){
		fargments = new KehuFragment[]{
				new KehuWaitBaobeiFragment(),
				new KehuWaitLookFragment(),
				new KehuWaitDealFragment(),
				new KehuDealFragment()
				};
		
		TabPageIndicator indicator = (TabPageIndicator) findView(R.id.tab_page_indicator);
		pager = (ViewPager) findView(R.id.tag_page_viewpager);
		pager.setAdapter(new PageVPAdapter(getChildFragmentManager(), titles, fargments));
		indicator.setViewPager(pager);
	}
	
	public static int refreshData = -1;
	private boolean isFinishInit;
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//刷新数据
		if(isFinishInit && refreshData >= 0){
			refreshData(refreshData);
			refreshData=-1;
		}
		//用户信息发生变化调用
		if(userInfoChange && isShow){
			fargments[0].refreshDatas();
			userInfoChange = false;
		}
	}
	
	/**
	 * 刷新
	 */
	public void refreshData(int toItem){
		fargments[0].refreshDatas();
		if(0 != pager.getCurrentItem())
			fargments[pager.getCurrentItem()].refreshDatas();
		if(toItem != pager.getCurrentItem()){
			if(toItem != 0)
				fargments[toItem].refreshDatas();
			pager.setCurrentItem(toItem);
		}
	}
	
	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.bt_title_right){
			baseActivity.jumpToNextActivity(AddkehuActivity.class, false);
		}
	}
	
	/** 请求标识 **/
	private final int REQUEST_GOODS_DATAS = 0;
	private void requestGoodsDatas() {
		// TODO Auto-generated method stub
		BeanRequest<SegmentList, MamaHaoServerError> req = new BeanRequest<>(
				context, 
				"", 
				this, 
				SegmentList.class);
		
		req.setParam("apiCode", "_customer_my");
		req.setParam("userId", MyApplication.getInstance().getMemberId());
		
		//获取token
		String token = NetworkManager.getInstance().getPostToken(req.getParam());
		req.setParam("token", token);
		
		baseActivity.addRequestQueue(req, REQUEST_GOODS_DATAS);
	
	}
	
	@Override
	protected void onResponseSuccess(ReqTag tag, Object data) {
		// TODO Auto-generated method stub
		super.onResponseSuccess(tag, data);
		switch (tag.getReqId()) {
		case REQUEST_GOODS_DATAS:
			//普通商品列表
//			handleDatas((List<HomeGoodsDatas>)data);
			List<Segment> datas = ((SegmentList)data).getSegmentList();
			if(datas!=null && datas.size()==4){
				String[] titles = new String[]{
						"待报备("+datas.get(0).getCount()+")",
						"未带看("+datas.get(1).getCount()+")",
						"未成交("+datas.get(2).getCount()+")",
						"已成交("+datas.get(3).getCount()+")"
						};
				
				init(titles);
			}else{
				String[] titles = new String[]{
						"待报备",
						"未带看",
						"未成交",
						"已成交"
						};
				
				init(titles);
			}
			
			isFinishInit = true;
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
		String[] titles = new String[]{
				"待报备",
				"未带看",
				"未成交",
				"已成交"
				};
		
		init(titles);

		isFinishInit = true;
	}

}
