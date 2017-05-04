package cn.softbank.purchase.activivty;

import java.util.List;

import android.support.v4.view.ViewPager;
import android.view.View;
import cn.softbank.purchase.adapter.PageVPAdapter;
import cn.softbank.purchase.base.BaseActivity;
import cn.softbank.purchase.base.MyApplication;
import cn.softbank.purchase.domain.Segment;
import cn.softbank.purchase.domain.SegmentList;
import cn.softbank.purchase.fragment.KehuDealFragment;
import cn.softbank.purchase.fragment.KehuFragment;
import cn.softbank.purchase.fragment.KehuWaitBaobeiFragment;
import cn.softbank.purchase.fragment.KehuWaitDealFragment;
import cn.softbank.purchase.fragment.KehuWaitLookFragment;
import cn.softbank.purchase.network.AbstractRequest.MamaHaoError;
import cn.softbank.purchase.network.BeanRequest;
import cn.softbank.purchase.network.NetworkManager;
import cn.softbank.purchase.network.ReqTag;
import cn.softbank.purchase.network.entity.MamaHaoServerError;
import cn.softbank.purchase.utils.Constant;
import cn.softbank.purchase.widget.pagerindicator.TabPageIndicator;
import cn.yicheng.jingjiren.R;

public class MyKehuActitvty extends BaseActivity{

	public static final int KEHU_FRAGMENT_WAIT_BAOBEI = 0;
	public static final int KEHU_FRAGMENT_WAIT_LOOK = 1;
	public static final int KEHU_FRAGMENT_WAIT_DEAL = 2;
	public static final int KEHU_FRAGMENT_DEAL = 3;
	private KehuFragment[] fargments;
	private ViewPager pager;
	
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.my_kehu);
		initTitleBar("我的客户", Constant.DEFAULT_LEFT_BACK, R.drawable.jjr_ic_add);
	}
	
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		// 订单标题
		requestGoodsDatas();
	}
	
	private void init(String[] titles){
		fargments = new KehuFragment[]{
				new KehuWaitBaobeiFragment(),
				new KehuWaitLookFragment(),
				new KehuWaitDealFragment(),
				new KehuDealFragment()
				};
		
		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.tab_page_indicator);
		pager = (ViewPager) findViewById(R.id.tag_page_viewpager);
		pager.setAdapter(new PageVPAdapter(getSupportFragmentManager(), titles, fargments));
		indicator.setViewPager(pager);
		if (getIntent() != null)
			indicator.setCurrentItem(getIntent().getIntExtra("item", 0));
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
			jumpToNextActivity(AddkehuActivity.class, false);
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
		
		addRequestQueue(req, REQUEST_GOODS_DATAS);
	
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
