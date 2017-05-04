package cn.softbank.purchase.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.softbank.purchase.activivty.ChooseCityActivity;
import cn.softbank.purchase.activivty.ChooseLoupanActivity;
import cn.softbank.purchase.activivty.GoodsDetailActitvivty;
import cn.softbank.purchase.activivty.MainActivity;
import cn.softbank.purchase.activivty.SearchActivity;
import cn.softbank.purchase.adapter.BaseViewHolder;
import cn.softbank.purchase.adapter.CommonAdapter;
import cn.softbank.purchase.adapter.MyViewPagerAdapter;
import cn.softbank.purchase.base.BaseFragment;
import cn.softbank.purchase.base.MyApplication;
import cn.softbank.purchase.client.PurchaseApi;
import cn.softbank.purchase.db.DBhelper;
import cn.softbank.purchase.domain.DeliveryAddr;
import cn.softbank.purchase.domain.HomeGoodsDatas;
import cn.softbank.purchase.domain.HomeTypeData;
import cn.softbank.purchase.domain.SortItem;
import cn.softbank.purchase.network.AbstractRequest.MamaHaoError;
import cn.softbank.purchase.network.BeanRequest;
import cn.softbank.purchase.network.NetworkManager;
import cn.softbank.purchase.network.ReqTag;
import cn.softbank.purchase.network.entity.MamaHaoServerError;
import cn.softbank.purchase.utils.CommonUtils;
import cn.softbank.purchase.utils.ImageUtils;
import cn.softbank.purchase.utils.PageLoadUtil;
import cn.softbank.purchase.widget.FooterListViewUtil;
import cn.softbank.purchase.widget.FooterListViewUtil.FooterScrollListener;
import cn.softbank.purchase.widget.HomeSortNaviBar;
import cn.softbank.purchase.widget.HomeSortNaviBar.OnNaviBarChangeListener;
import cn.softbank.purchase.widget.MyListView;
import cn.yicheng.jingjiren.R;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HomeFragment extends BaseFragment implements AMapLocationListener,FooterScrollListener,OnItemClickListener{

	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.home_fragment, container, false);
	}
	
	private TextView tv_city;
	private LocationManagerProxy mLocationManagerProxy;
	protected MyListView listview;
	protected View headerView;
	protected List<HomeGoodsDatas> datas;
	private CommonAdapter<HomeGoodsDatas> adapter;
	
	/**
	 * 分页加载工具
	 */
	protected PageLoadUtil pageLoadUtil;
	private HomeSortNaviBar naviBar;
	private HomeSortNaviBar naviBarFloat;
	private List<SortItem> sortDatas = new ArrayList<>();
	private CommonAdapter<SortItem> sortAdapter;

	@Override
	protected void initData(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(getActivity() instanceof MainActivity){
			mLocationManagerProxy = LocationManagerProxy.getInstance(context);
			mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork,
	                -1, 50, this);
	        mLocationManagerProxy.setGpsEnable(false);
		}else
			findView(R.id.title_bar).setVisibility(View.GONE);
		
		statusHeight = CommonUtils.getStatusHeight(baseActivity);
		titlebarHeight = CommonUtils.dip2px(context, 48);
        
		tv_city = findTextView(R.id.tv_city);
		tv_city.setOnClickListener(this);
		findView(R.id.iv_city).setOnClickListener(this);
		findView(R.id.iv_search).setOnClickListener(this);
		
		dBhelper = new DBhelper(context);
		
		listview = (MyListView) findView(R.id.listview);
		
		headerView = View.inflate(context, R.layout.home_header_view, null);
		
		naviBarFloat = (HomeSortNaviBar) findView(R.id.sort_navi_bar);
		naviBar = (HomeSortNaviBar) headerView.findViewById(R.id.sort_navi_bar);
		
		naviBarFloat.setOnNaviBarChangeListener(new OnNaviBarChangeListener() {

			@Override
			public void onNaviChanged(int lastNaviPostion,int currentNaviPostion) {
				// TODO Auto-generated method stub
				naviBar.changeNavi(currentNaviPostion, false);
				showSort(currentNaviPostion == lastNaviPostion);
			}
		});
		
		naviBar.setOnNaviBarChangeListener(new OnNaviBarChangeListener() {

			@Override
			public void onNaviChanged(int lastNaviPostion,int currentNaviPostion) {
				// TODO Auto-generated method stub
				naviBarFloat.changeNavi(currentNaviPostion, false);
				showSort(currentNaviPostion == lastNaviPostion);
				
			}
		});
		
		if(!(getActivity() instanceof MainActivity)){
			vp_home_banner=(ViewPager) headerView.findViewById(R.id.vp_banners);
			
    		vp_home_banner.setVisibility(View.GONE);
    		headerViewShowFlag = false;
    		naviBarFloat.setVisibility(View.VISIBLE);
    		hasBanner = false;
		}
		
		listview.addHeaderView(headerView);
		listview.setOnMyScrollListener(this, true, true);
		
		datas = new ArrayList<>();
		
		adapter = new CommonAdapter<HomeGoodsDatas>(baseActivity, datas, R.layout.home_list_item) {

			@Override
			public void convert(BaseViewHolder holder, HomeGoodsDatas itemData,
					int position, ViewGroup parent) {
				// TODO Auto-generated method stub
				holder.setImageByUrl(R.id.iv_img, itemData.getImage(), ImageUtils.imgOptionsBig);
				holder.setText(R.id.tv_title, itemData.getName());
				if(!TextUtils.isEmpty(itemData.getPrice()))
					holder.setText(R.id.tv_price, itemData.getPrice()+"元/平米");
				else
					holder.setText(R.id.tv_price, "待定");
				//类型
				TextView tv_type = holder.getView(R.id.tv_type);
				if(itemData.getFlag()!=null && itemData.getFlag().size()>0){
					tv_type.setVisibility(View.VISIBLE);
					tv_type.setText(itemData.getFlag().get(0));
				}else
					tv_type.setVisibility(View.GONE);
				//tv_area
				if(!TextUtils.isEmpty(itemData.getArea()))
					holder.setText(R.id.tv_area, itemData.getArea());
				else if(TextUtils.isEmpty(sortStrs[0]))
					holder.setText(R.id.tv_area, MyApplication.getInstance().getCity());
				else
					holder.setText(R.id.tv_area, MyApplication.getInstance().getCity()+sortStrs[0]);
				
				holder.setText(R.id.tv_distance, itemData.getDistance());
			}
		};
		
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
		
		pageLoadUtil = new PageLoadUtil(10);
		
		//分类
		listview_sort = (ListView) findView(R.id.listview_sort);
		
		sortAdapter = new CommonAdapter<SortItem>(context, sortDatas, R.layout.item_home_sort) {

			@Override
			public void convert(BaseViewHolder holder, SortItem itemData,
					int position, ViewGroup parent) {
				// TODO Auto-generated method stub
				holder.setText(R.id.tv_title, itemData.getTitle());
				holder.getView(R.id.iv_sel).setVisibility(itemData.isChoosed()?View.VISIBLE:View.GONE);
			}
		};
		listview_sort.setAdapter(sortAdapter);
		listview_sort.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				List<SortItem> sortItems = MyApplication.getInstance().getSortTotalDatas().get(naviBar.getNaviPostion());
				//之前的取消选中
				for(SortItem item:sortItems){
					item.setChoosed(false);
				}
				//当前被选中
				SortItem itemCurrent=sortItems.get(arg2);
				itemCurrent.setChoosed(true);
				
				if(itemCurrent.getTitle().equals("不限"))
					sortStrs[naviBar.getNaviPostion()] = "";
				else
					sortStrs[naviBar.getNaviPostion()] = itemCurrent.getTitle();
				
				sortAdapter.notifyDataSetChanged();
				
				listview_sort.setVisibility(View.GONE);
				refreshDatas(true, true);
			}
		});
		
		if(!(getActivity() instanceof MainActivity)){
			tv_city.setText(MyApplication.getInstance().getCity());
			requestBanners();
			refreshDatas(true,true);
		}
//		requestBanners();
//		refreshDatas(true,true);
		
	}
	
	
	
	private final int REQUEST_CODE_CITY = 100;
	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_city:
			startActivityForResult(new Intent(context, ChooseCityActivity.class), REQUEST_CODE_CITY);
			break;
		case R.id.iv_city:
			startActivityForResult(new Intent(context, ChooseCityActivity.class), REQUEST_CODE_CITY);
			break;
		case R.id.iv_search:
			baseActivity.jumpToNextActivity(SearchActivity.class, false);
			break;

		default:
			break;
		}
	}
	
	public void refreshDatas(boolean isRefresh,boolean isShowProgress){
		if(isAdded() && pageLoadUtil!=null){
			pageLoadUtil.updataPage(isRefresh);
			if (isRefresh && isShowProgress) 
				baseActivity.showProgressBar(this);
			requestGoodsDatas(pageLoadUtil.getCurrentPage(),pageLoadUtil.getPageSize());
		}
	}
	
	protected void handleDatas(List<HomeGoodsDatas> newDatas) {
		baseActivity.hideProgressBar(this);
		//处理数据
		pageLoadUtil.handleDatas(datas, newDatas);
		//处理footerView
		if(pageLoadUtil.judgeHasMoreData(newDatas))
			listview.setFooterState(FooterListViewUtil.FOOTER_STATE_LOAD_FINISH);
		else
			listview.setFooterState(FooterListViewUtil.FOOTER_STATE_NO_MORE_DATA);
		
		adapter.notifyDataSetChanged();
	}
	
	/** 请求标识 **/
	private final int REQUEST_GOODS_DATAS = 0;
	private final int REQUEST_BANNER = 1;
	private final int REQUEST_REGION = 2;
//	private String region;//西城区
//	private String type;//写字楼
//	private String area;//90-110平米以下
//	private String price;//100-120万
	private String[] sortStrs = new String[]{"","","",""};
	
	private void requestGoodsDatas(int currentPage, int pageSize) {
		// TODO Auto-generated method stub
		BeanRequest<LoupanDatas, MamaHaoServerError> req = new BeanRequest<>(
				context, 
				"", 
				this, 
				LoupanDatas.class);
		
		req.setParam("apiCode", "_building_lists");
		
		req.setParam("city", MyApplication.getInstance().getCity());
		req.setParam("region", sortStrs[0]);//西城区
		req.setParam("type", sortStrs[1]);//写字楼
		req.setParam("area", sortStrs[2]);//90-110平米以下
		req.setParam("price", sortStrs[3]);//100-120万
		req.setParam("lon", MyApplication.getInstance().getLon()+"");//100-120万
		req.setParam("lat", MyApplication.getInstance().getLat()+"");//100-120万
		req.setParam("page", currentPage+"");//100-120万
		req.setParam("size", pageSize+"");//100-120万
		
		if(getActivity().getIntent()!=null)
			req.setParam("name", getActivity().getIntent().getStringExtra("key"));
		
		//获取token
		String token = NetworkManager.getInstance().getPostToken(req.getParam());
		req.setParam("token", token);
		
		baseActivity.addRequestQueue(req, REQUEST_GOODS_DATAS, new ReqTag.Builder().handleSimpleRes(true));
	
	}
	
	protected void requestBanners() {
		// TODO Auto-generated method stub
		BeanRequest<HomeTypeData, MamaHaoServerError> req = new BeanRequest<>(
				context, 
				"", 
				this, 
				HomeTypeData.class);
		
		req.setParam("apiCode", "_building_all_type");
		req.setParam("city", MyApplication.getInstance().getCity());
		
		//获取token
		String token = NetworkManager.getInstance().getPostToken(req.getParam());
		req.setParam("token", token);
		
		baseActivity.addRequestQueue(req, REQUEST_BANNER);
	
	}
	
	private void requestRegion() {
		// TODO Auto-generated method stub
		BeanRequest<RegionDatas, MamaHaoServerError> req = new BeanRequest<>(
				context, 
				"", 
				this, 
				RegionDatas.class);
		
		req.setParam("apiCode", "_building_region");
		req.setParam("city", MyApplication.getInstance().getCity());
		
		//获取token
		String token = NetworkManager.getInstance().getPostToken(req.getParam());
		req.setParam("token", token);
		
		baseActivity.addRequestQueue(req, REQUEST_REGION);
	
	}
	
	@Override
	protected void onResponseSuccess(ReqTag tag, Object data) {
		// TODO Auto-generated method stub
		super.onResponseSuccess(tag, data);
		
		switch (tag.getReqId()) {
		case REQUEST_GOODS_DATAS:
			//普通商品列表
			handleDatas(((LoupanDatas)data).getLists());
			break;
		case REQUEST_REGION:
			//普通商品列表
//			//更新
			List<SortItem> sorts= MyApplication.getInstance().getSortTotalDatas().get(0);
			sorts.clear();
			sorts.add(new SortItem("不限", true));
			for(String a:((RegionDatas)data).getRegion()){
				sorts.add(new SortItem(a, false));
			}
			
			sortStrs[0] = "";
			
			sortDatas.clear();
			sortDatas.addAll(MyApplication.getInstance().getSortTotalDatas().get(naviBar.getNaviPostion()));
			sortAdapter.notifyDataSetChanged();
			
			refreshDatas(true, true);
			break;
		case REQUEST_BANNER:
			if(getActivity() instanceof MainActivity)
				setBanners(((HomeTypeData)data).getBannerList());
			
			MyApplication.getInstance().setSortTotalDatas((HomeTypeData)data);
			
			sortDatas.clear();
			sortDatas.addAll(MyApplication.getInstance().getSortTotalDatas().get(naviBar.getNaviPostion()));
			sortAdapter.notifyDataSetChanged();
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
		case REQUEST_GOODS_DATAS:
			//普通商品列表
			handleDatas(null);
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(data==null)
			return;
		if(requestCode == REQUEST_CODE_CITY && resultCode == baseActivity.RESULT_OK){
			String city = data.getStringExtra("city");
			tv_city.setText(city);
			
			MyApplication.getInstance().setCity(city);
			requestRegion();
		}
	}
	
	private DBhelper dBhelper;
	private void updateAreas(String city){
//		ArrayList<Area> citys=dBhelper.getAllCity();
//		String mCity = null;
//		for(Area area:citys){
//			mCity = area.getName().trim();
//			if(mCity.endsWith("市"))
//				mCity = mCity.substring(0,mCity.length()-1);
//			
//			if(city.equals(mCity)){
//				//找到对应的城市
//				ArrayList<Area> areas=dBhelper.getDistrict(area.getCode());
//				
//				//更新
//				List<SortItem> sorts= MyApplication.getInstance().getSortTotalDatas().get(0);
//				sorts.clear();
//				sorts.add(new SortItem("不限", true));
//				for(Area a:areas){
//					sorts.add(new SortItem(a.getName().trim(), false));
//				}
//				break;
//			}
//		}
		
		sortDatas.clear();
		sortDatas.addAll(MyApplication.getInstance().getSortTotalDatas().get(naviBar.getNaviPostion()));
		sortAdapter.notifyDataSetChanged();
		
		refreshDatas(true, true);
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChanged(AMapLocation aMapLocation) {
		// TODO Auto-generated method stub
		DeliveryAddr addr = new DeliveryAddr();
		 if (aMapLocation != null && aMapLocation.getAMapException().getErrorCode() == 0) {
	            // 获取位置信息
	            if ("lbs".equals(aMapLocation.getProvider())) {
	                addr.setAddrDetail(aMapLocation.getAddress());
	                addr.setProvince(aMapLocation.getProvince());
	                addr.setCity(aMapLocation.getCity());
	                addr.setArea(aMapLocation.getDistrict());
	                
	                MyApplication.getInstance().setLon(aMapLocation.getLongitude());
	                MyApplication.getInstance().setLat(aMapLocation.getLatitude());
	            }
	            // 处理失败情况，设置默认地址为杭州市西湖区
	            else {
	                addr = new DeliveryAddr();
	                addr.setProvince("广东省");
	                addr.setCity("广州市");
	                addr.setArea("天河区");
	                addr.setAddrDetail("");
	                
	                MyApplication.getInstance().setLon(120.19);
	                MyApplication.getInstance().setLat(30.26);
	            }
	       }
	        // 地位失败处理
			 else {
	             addr = new DeliveryAddr();
	             addr.setProvince("广东省");
	             addr.setCity("广州市");
	             addr.setArea("天河区");
	             addr.setAddrDetail("");
	             
	             MyApplication.getInstance().setLon(120.19);
	             MyApplication.getInstance().setLat(30.26);
	         }
		 
		 String city = addr.getCity();
//		 if(!TextUtils.isEmpty(city) && city.endsWith("市"))
//			 city = city.substring(0, city.length()-1);
		 tv_city.setText(city);
		 MyApplication.getInstance().setCity(city);
		 
//		 updateAreas(city);
		 
		 PurchaseApi.getInstance().changedAddress(addr);
		 
	        if (mLocationManagerProxy != null) {
	            mLocationManagerProxy.removeUpdates(this);
	            mLocationManagerProxy.destroy();
	            mLocationManagerProxy = null;
	        }
	    
	     requestBanners();
	     refreshDatas(true, true);
	     
	}
	
	private ViewPager vp_home_banner;
	private int bannerCurrent = 0;// banner
	private int bannerSize;
	private boolean isLoopTopPic;//循环标志位
	/**
	 * 设置顶部轮播图
	 */
	protected void setBanners(final List<String> banners){
		vp_home_banner=(ViewPager) headerView.findViewById(R.id.vp_banners);
		
    	if(banners==null || banners.size()<=0){
    		vp_home_banner.setVisibility(View.GONE);
    		headerViewShowFlag = false;
    		naviBarFloat.setVisibility(View.VISIBLE);
    		hasBanner = false;
    		return;
    	}
    	
    	hasBanner = true;
    	
    	bannerSize=banners.size();
        if(bannerSize==2)
            banners.addAll(banners);
        
        //一张图片时不循环
        isLoopTopPic = bannerSize==1?false:true;
        if(isLoopTopPic)
            initHotShopDot(0, bannerSize, Integer.MAX_VALUE);
    	
    	List<View > viewList=new ArrayList<View>();
    	for (int i = 0; i < banners.size(); i++) {
    		String banner=banners.get(i);
			ImageView iv=new ImageView(context);
			iv.setScaleType(ImageView.ScaleType.FIT_XY);
			iv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, CommonUtils.dip2px(context, 180)));
			ImageLoader.getInstance().displayImage(banner, iv,ImageUtils.imgOptionsBig);
			viewList.add(iv);
			iv.setTag(i);
			iv.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
//						startActivity(new Intent(context, GoodsDetailActitvivty.class).putExtra("id", banners.get((int)v.getTag()).getLink())
//								.putExtra("flag", Integer.valueOf(banners.get((int)v.getTag()).getGood_type()))
//								);
					} catch (Exception e) {
						// TODO: handle exception
					}
					
				}
			});
		}
		
		MyViewPagerAdapter homeBannerAdapter=new MyViewPagerAdapter(viewList);
    	if(isLoopTopPic)
    		homeBannerAdapter.setSize(Integer.MAX_VALUE);
    	else
    		homeBannerAdapter.setSize(1);
    	
    	vp_home_banner.setAdapter(homeBannerAdapter);

    	vp_home_banner.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                bannerCurrent = arg0;
                updataDot(arg0%bannerSize);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    	
    	startBannerTimer();
	}
	
	private View[] dots;
	/**
     * banner小点
     * @param position
     */
    private void initHotShopDot(int position, int size,int maxSize) {
    	LinearLayout layout = (LinearLayout) headerView.findViewById(R.id.dot_views);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                CommonUtils.dip2px(context, 6), CommonUtils.dip2px(context, 6));
        params.setMargins(CommonUtils.dip2px(context, 4), 0,
                CommonUtils.dip2px(context, 4), 0);
        dots = new View[size];
        // 先清后加
//        layout.removeAllViews();
        for (int i = 0; i < size && i < maxSize; i++) {
            View dot = new View(context);
            dot.setLayoutParams(params);
            dots[i] = dot;
            layout.addView(dot);
        }
        for (int i = 0; i < dots.length; i++) {
            if (i == position%dots.length) {
                dots[i].setBackgroundResource(R.drawable.dot_red_7);
            } else {
                dots[i].setBackgroundResource(R.drawable.dot_gray_7);
            }
        }
    }
    
    private int lastPos;
    private void updataDot(int pos){
    	dots[lastPos].setBackgroundResource(R.drawable.dot_gray_7);
    	dots[pos].setBackgroundResource(R.drawable.dot_red_7);
    	lastPos = pos;
    }
    
    private Timer timer;
	private void startBannerTimer(){
		if (timer == null) {
			timer = new Timer();
			timer.schedule(new TimerTask() {
        		
        		@Override
        		public void run() {
        			bannerCurrent++;
        			if(isLoopTopPic){
        				baseActivity.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								vp_home_banner.setCurrentItem(bannerCurrent);
							}
						});
        			}
                		
        		}
        	}, 5000, 5000);
        }
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if((getActivity() instanceof ChooseLoupanActivity) && TextUtils.isEmpty(getActivity().getIntent().getStringExtra("key"))){
			getActivity();
			getActivity().setResult(Activity.RESULT_OK, 
					getActivity().getIntent()
					.putExtra("id", datas.get(arg2-1).getId())
					.putExtra("name", datas.get(arg2-1).getName())
					);
			getActivity().finish();
		}
		else{
			startActivity(new Intent(baseActivity, GoodsDetailActitvivty.class).putExtra("id", datas.get(arg2-1).getId()));
		}
			
	}

	private int[] location=new int[2];
	private int statusHeight = 0;// 状态栏高度
	private int titlebarHeight = 0;// titleBar的高度
	private boolean headerViewShowFlag = true;
	private ListView listview_sort;
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		if (headerViewShowFlag) {
			// navibar浮起控制
			naviBar.getLocationOnScreen(location);
			
			if (location[1] <= statusHeight + titlebarHeight) {
				if (naviBarFloat.getVisibility() == View.GONE) {
					naviBarFloat.setVisibility(View.VISIBLE);
				}
			}else if(naviBarFloat.getVisibility() == View.VISIBLE) 
				naviBarFloat.setVisibility(View.GONE);
			
		}
		
	}
	
	private boolean hasBanner = true;
	private void showSort(boolean isSameNavi){
		sortDatas.clear();
		sortDatas.addAll(MyApplication.getInstance().getSortTotalDatas().get(naviBar.getNaviPostion()));
		sortAdapter.notifyDataSetChanged();
		
		if(listview_sort.getVisibility() == View.GONE){
			RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) listview_sort.getLayoutParams();
			int topMargin = location[1] + CommonUtils.dip2px(context, 41) - statusHeight;
			
			if(!hasBanner || topMargin < statusHeight + titlebarHeight)
				topMargin = titlebarHeight + CommonUtils.dip2px(context, 41);
			
			if(!(getActivity() instanceof MainActivity))
				topMargin = CommonUtils.dip2px(context, 41);
				
			params.topMargin = topMargin;
			listview_sort.setLayoutParams(params);
			listview_sort.setVisibility(View.VISIBLE);
		}else if(isSameNavi)
			listview_sort.setVisibility(View.GONE);
		
	}

	@Override
	public void onSrollToBottom() {
		// TODO Auto-generated method stub
		refreshDatas(false, false);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}
	
	
	public class LoupanDatas{
		private List<HomeGoodsDatas> lists;

		public List<HomeGoodsDatas> getLists() {
			return lists;
		}

		public void setLists(List<HomeGoodsDatas> lists) {
			this.lists = lists;
		}
		
	}
	
	public class RegionDatas{
		private List<String> region;

		public List<String> getRegion() {
			return region;
		}

		public void setRegion(List<String> region) {
			this.region = region;
		}
		
	}
	
}
