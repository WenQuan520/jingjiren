package cn.softbank.purchase.activivty;

import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import cn.softbank.purchase.adapter.BaseViewHolder;
import cn.softbank.purchase.adapter.CommonAdapter;
import cn.softbank.purchase.base.BaseActivity;
import cn.softbank.purchase.domain.HomeGoodsDatas;
import cn.softbank.purchase.network.AbstractRequest.MamaHaoError;
import cn.softbank.purchase.network.NetworkManager;
import cn.softbank.purchase.network.PureListRequest;
import cn.softbank.purchase.network.ReqTag;
import cn.softbank.purchase.network.entity.MamaHaoServerError;
import cn.softbank.purchase.utils.Constant;
import cn.softbank.purchase.utils.ImageUtils;
import cn.softbank.purchase.utils.PageLoadUtil;
import cn.softbank.purchase.widget.FooterListViewUtil;
import cn.softbank.purchase.widget.FooterListViewUtil.FooterScrollListener;
import cn.softbank.purchase.widget.MyListView;
import cn.softbank.purchase.widget.SortNaviBar;
import cn.softbank.purchase.widget.SortNaviBar.OnNaviBarChangeListener;
import cn.yicheng.jingjiren.R;

public class GoodsListActivity extends BaseActivity  implements FooterScrollListener,OnItemClickListener {
	/**
	 * 排序导航栏
	 */
	private SortNaviBar naviBar;
	private MyListView listview;
	private String key;
	
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.goods_list);
		
		EditText et_search = (EditText) findViewById(R.id.et_search);
		key = getIntentExtra("key");
		et_search.setText(key);
		et_search.setOnClickListener(this);
		findViewById(R.id.bt_title_left).setOnClickListener(this);
		
		naviBar = (SortNaviBar) findViewById(R.id.sort_navi_bar);
		//添加监听
		naviBar.setOnNaviBarChangeListener(new OnNaviBarChangeListener() {

			@Override
			public void onNaviChanged(int lastNaviPostion,
					int currentNaviPostion) {
				// TODO Auto-generated method stub
				requestGoodsDatas(true);
			}

			@Override
			public void onArrowChanged(int naviPosition, int arrowDirection) {
				// TODO Auto-generated method stub
				requestGoodsDatas(true);
			}
		});
		
		listview = (MyListView) findViewById(R.id.listview);
		// 设置滑动监听
		listview.setOnMyScrollListener(this, true, true);
		listview.setOnItemClickListener(this);
	}

	/**
	 * 分页加载工具
	 */
	private PageLoadUtil pageLoadUtil;
	protected List<HomeGoodsDatas> datas;
	private CommonAdapter<HomeGoodsDatas> adapter;
	
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		datas = new ArrayList<>();
		
		adapter = new CommonAdapter<HomeGoodsDatas>(context, datas, R.layout.goods_list_item) {

			@Override
			public void convert(BaseViewHolder holder, HomeGoodsDatas itemData,
					int position, ViewGroup parent) {
				// TODO Auto-generated method stub
				holder.setImageByUrl(R.id.iv_img, itemData.getImage(), ImageUtils.imgOptionsBig);
				holder.setText(R.id.tv_title, itemData.getName());
				holder.setText(R.id.tv_price, String.valueOf(itemData.getPrice()));
			}
		};
		
		listview.setAdapter(adapter);
		//分页加载，每页加载20条
		pageLoadUtil = new PageLoadUtil(20);
		
		requestGoodsDatas(true);
	}

	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_title_left:
			finish();
			break;
		case R.id.et_search:
			finish();
			break;

		default:
			break;
		}
	}
	
	/** 请求标识 **/
	private final int REQUEST_DATAS = 0;
	
	/**
	 * 请求数据
	 */
	private void requestGoodsDatas(boolean isRefresh){
		
		pageLoadUtil.updataPage(isRefresh);
		if (isRefresh) 
			showProgressBar(null);
		
		PureListRequest<HomeGoodsDatas, MamaHaoServerError> req = new PureListRequest<>(
				context, 
				"", 
				this, 
				HomeGoodsDatas.class);
		
		req.setParam("apiCode", "_search_001");
		req.setParam("key", key);
		req.setParam("search_type", String.valueOf(naviBar.getNaviPostion()+1));
		req.setParam("sort", String.valueOf(naviBar.getArrowDirection()+1));
		req.setParam("page", String.valueOf(pageLoadUtil.getCurrentPage()));
		req.setParam("size", String.valueOf(pageLoadUtil.getPageSize()));
		
		//获取token
		String token = NetworkManager.getInstance().getPostToken(req.getParam());
		req.setParam("token", token);
		
		addRequestQueue(req, REQUEST_DATAS, new ReqTag.Builder().handleSimpleRes(true));
	}
	
	@Override
	public void onResponseSuccess(ReqTag tag, Object data) {
		// TODO Auto-generated method stub
		super.onResponseSuccess(tag, data);
		
		switch (tag.getReqId()) {
		case REQUEST_DATAS:
			// 商品列表
			List<HomeGoodsDatas> newDatas = (List<HomeGoodsDatas>) data;
			
			if(pageLoadUtil.getCurrentPage() == 1){
				if(newDatas == null || newDatas.size() == 0)
					showBlankPage(null, R.drawable.img_error_page_search, getString(R.string.empty_list_goods), null, Constant.DEFAULT_TOP_MARGIN);
			}
			
			//处理数据
			pageLoadUtil.handleDatas(datas, newDatas);
			
			//处理footerView
			if(pageLoadUtil.judgeHasMoreData(newDatas))
				listview.setFooterState(FooterListViewUtil.FOOTER_STATE_LOAD_FINISH);
			else
				listview.setFooterState(FooterListViewUtil.FOOTER_STATE_NO_MORE_DATA);
			
			adapter.notifyDataSetChanged();
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
		
		switch (tag.getReqId()) {
		case REQUEST_DATAS:
			// 商品列表
			if (!pageLoadUtil.isResetData())
				listview.setFooterState(FooterListViewUtil.FOOTER_STATE_LOAD_FINISH);
			break;
	
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// 去详情
//		startActivity(new Intent(context, GoodsDetailActitvivty.class).putExtra("id", datas.get(position).getId()));
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSrollToBottom() {
		// TODO Auto-generated method stub
		requestGoodsDatas(false);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}

}
