package cn.softbank.purchase.activivty;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;
import cn.sharesdk.framework.ShareSDK;
import cn.softbank.purchase.adapter.BaseViewHolder;
import cn.softbank.purchase.adapter.CommonAdapter;
import cn.softbank.purchase.adapter.ViewPagerAdapter;
import cn.softbank.purchase.base.BaseActivity;
import cn.softbank.purchase.base.MyApplication;
import cn.softbank.purchase.domain.AssetList;
import cn.softbank.purchase.domain.GoodsDetailData;
import cn.softbank.purchase.domain.TitleDesc;
import cn.softbank.purchase.domain.TitleDescs;
import cn.softbank.purchase.network.AbstractRequest.MamaHaoError;
import cn.softbank.purchase.network.BeanRequest;
import cn.softbank.purchase.network.JsonElementRequest;
import cn.softbank.purchase.network.NetworkManager;
import cn.softbank.purchase.network.ReqTag;
import cn.softbank.purchase.network.entity.DefaultMamahaoServerError;
import cn.softbank.purchase.network.entity.MamaHaoServerError;
import cn.softbank.purchase.onekeyshare.OnekeyShare;
import cn.softbank.purchase.utils.CommonUtils;
import cn.softbank.purchase.utils.Constant;
import cn.softbank.purchase.utils.ImageUtils;
import cn.softbank.purchase.utils.SharedPreference;
import cn.yicheng.jingjiren.R;

public class GoodsDetailActitvivty extends BaseActivity {

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.goods_detail_activity);
		initTitleBar("楼盘详情", Constant.DEFAULT_LEFT_BACK, R.drawable.jjr_ic_sharh, R.drawable.jjr_ic_soucang_1);
		
		findViewById(R.id.iv_position).setOnClickListener(this);
		findViewById(R.id.tv_money_detail).setOnClickListener(this);
		findViewById(R.id.tv_type_detail).setOnClickListener(this);
		findViewById(R.id.tv_sale_detail).setOnClickListener(this);
		findViewById(R.id.tv_params_more).setOnClickListener(this);
		findViewById(R.id.view_anchuang).setOnClickListener(this);
		findViewById(R.id.bt_my_kehu).setOnClickListener(this);
		findViewById(R.id.bt_baobei).setOnClickListener(this);
		
	}

	private GoodsDetailData detailData;
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		requestDetailDatas();
	}

	/** 请求标识 **/
	private final int REQUEST_DATAS = 0;
	private final int REQUEST_COLLECT = 2;
	private final int REQUEST_CANCEL_COLLECT = 3;
	
	/**
	 * 请求商品数据
	 */
	private void requestDetailDatas() {
		showProgressBar(null);
		
		BeanRequest<GoodsDetailData, DefaultMamahaoServerError> req = new BeanRequest<>(
                context,
                "", 
                this, GoodsDetailData.class,false);
		
		req.setParam("apiCode", "_building_info");
		req.setParam("id", getIntentExtra("id"));
		
		//获取token
		String token = NetworkManager.getInstance().getPostToken(req.getParam());
		req.setParam("token", token);
		
		addRequestQueue(req, REQUEST_DATAS, new ReqTag.Builder().handleSimpleRes(true).handleNetworkError(true));
	}
	
	/**
	 * 收藏商品
	 */
	private void requestCollect(){
		showProgressBar(null);
		
		JsonElementRequest<DefaultMamahaoServerError> req = new JsonElementRequest<DefaultMamahaoServerError>(
				context,"",this);
		
		req.setParam("apiCode", "_user_favorite_add");
		req.setParam("userId", MyApplication.getInstance().getMemberId());
		req.setParam("buildingId", detailData.getId());
		// //添加收藏
		// $apiCode='_user_favorite_add';
		// $param = array();
		// $param['userId'] = '118';
		// $param['buildingId'] = '1';

		
		//获取token
		String token = NetworkManager.getInstance().getPostToken(req.getParam());
		req.setParam("token", token);
		
		addRequestQueue(req, REQUEST_COLLECT, new ReqTag.Builder().handleSimpleRes(true));
	}
	
	/**
	 * 收藏商品
	 */
	private void requestCancelCollect(){
		showProgressBar(null);
		
		JsonElementRequest<DefaultMamahaoServerError> req = new JsonElementRequest<DefaultMamahaoServerError>(
				context,"",this);
		
		req.setParam("apiCode", "_user_favorite_del");
		req.setParam("userId", MyApplication.getInstance().getMemberId());
		req.setParam("buildingId", detailData.getId());

		
		//获取token
		String token = NetworkManager.getInstance().getPostToken(req.getParam());
		req.setParam("token", token);
		
		addRequestQueue(req, REQUEST_CANCEL_COLLECT, new ReqTag.Builder().handleSimpleRes(true));
	}
	
	@Override
	protected void onResponseSuccess(ReqTag tag, Object data) {
		// TODO Auto-generated method stub
		super.onResponseSuccess(tag, data);
		switch (tag.getReqId()) {
		case REQUEST_DATAS:
			detailData = (GoodsDetailData) data;
			
			setTopImgs((ArrayList<String>) detailData.getHouseAsset().getHouseImg());
			
			setTitlePrice();
			
			break;
		case REQUEST_COLLECT:
			setCollectionState(true);
			break;
		case REQUEST_CANCEL_COLLECT:
			setCollectionState(false);
			break;

		default:
			break;
		}
	}
	
	private boolean isCollect;
	private void setCollectionState(boolean isCollect){
		this.isCollect = isCollect;
		if(isCollect){
			bt_title_second_right.setCompoundDrawables(
					CommonUtils.GetDrawable(context, R.drawable.jjr_ic_soucang_2), null,
					null, null);
		}else{
			bt_title_second_right.setCompoundDrawables(
					CommonUtils.GetDrawable(context, R.drawable.jjr_ic_soucang_1), null,
					null, null);
		}
	}
	
	@Override
	protected void onResponseFailure(ReqTag tag, MamaHaoServerError error,
			MamaHaoError clientError) {
		// TODO Auto-generated method stub
		super.onResponseFailure(tag, error, clientError);
	}
	
	/** 图片相关控件 **/
	private ViewPager vp_top_img;
	private TextView tv_top_current_img;
	private TextView tv_top_total_img;
	/**
	 * 设置顶部轮播图片
	 */
	@SuppressWarnings("deprecation")
	private void setTopImgs(final ArrayList<String> topImgUrls){
		//图片地址集合
		if(topImgUrls!=null && topImgUrls.size()>0){
			//初始化控件，一般来讲此方法不会被调用两次
			if(vp_top_img == null){
				vp_top_img = (ViewPager) findViewById(R.id.vp_top_img);
				tv_top_current_img = findTextView(R.id.tv_top_current_img);
				tv_top_total_img = findTextView(R.id.tv_top_total_img);
				
				//图片滑动监听
				vp_top_img.setOnPageChangeListener(new OnPageChangeListener() {
					
					@Override
					public void onPageSelected(int position) {
						// TODO Auto-generated method stub
						// 更新当前页
						tv_top_current_img.setText(String.valueOf(position+1));
					}
					
					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onPageScrollStateChanged(int arg0) {
						// TODO Auto-generated method stub
						
					}
				});
			}else//更新当前页为第一页
				tv_top_current_img.setText("1");
			//共有多少张
			tv_top_total_img.setText(new StringBuilder("/").append(topImgUrls.size()).toString());
			//图片viewpager
			ArrayList<View> topImgs=new ArrayList<View>();
			for(final String url:topImgUrls){
				ImageView iv=new ImageView(context);
				iv.setBackgroundColor(0xffffffff);
				iv.setScaleType(ScaleType.FIT_XY);
				iv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				showImage(url, iv, ImageUtils.imgOptionsBig);

				topImgs.add(iv);
//				iv.setOnClickListener(new View.OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						// 查看大图
//						Intent intent = new Intent(context, ImagePagerActivity.class);
//						// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
//						String[] array = (String[])topImgUrls.toArray(new String[topImgUrls.size()]);  
//						intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, array);
//						intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, topImgUrls.indexOf(url));
//						startActivity(intent);
//					}
//				});
			}
			//给图片vp设置适配器
			vp_top_img.setAdapter(new ViewPagerAdapter(topImgs));
		}else//没有图片隐藏topView
			findViewById(R.id.top_view).setVisibility(View.GONE);
		
	}
	
	/**
	 * 设置标题价格信息
	 */
	private void setTitlePrice(){
		//收藏状态
		isCollect = "1".equals(detailData.getIsFavorite());
		setCollectionState(isCollect);
		findTextView(R.id.tv_title).setText(detailData.getHouseName());
		
		//类型
		if(detailData.getFlags() == null || detailData.getFlags().size()==0){
			findViewById(R.id.tv_type).setVisibility(View.GONE);
			findViewById(R.id.tv_type2).setVisibility(View.GONE);
		}else if(detailData.getFlags().size()==1){
			findTextView(R.id.tv_type).setText(detailData.getFlags().get(0));
			findViewById(R.id.tv_type2).setVisibility(View.GONE);
		}else{
			findTextView(R.id.tv_type).setText(detailData.getFlags().get(0));
			findTextView(R.id.tv_type2).setText(detailData.getFlags().get(1));
		}
		
		//价格
		findTextView(R.id.tv_price).setText(detailData.getHousePrice()+"元/㎡");
		//地址
		setText(R.id.tv_address, detailData.getHouseLocation());
		//佣金
		setText(R.id.tv_money_rule, detailData.getCommissionInfo().getTitleHlt());
		setText(R.id.tv_count_company, detailData.getCommissionInfo().getTitle());
		
		//户型
		try {
			setText(R.id.tv_type_detail, "全部户型(共"+detailData.getHouseAsset().getAssetList().size()+"个)");
		} catch (Exception e) {
			setText(R.id.tv_type_detail, "全部户型(共"+0+"个)");
			// TODO: handle exception
		}
		
		//户型列表
		ListView listview_huxing = (ListView) findViewById(R.id.listview_huxing);
		try {
			List<AssetList> houseAssetData = new ArrayList<>();
			if(detailData.getHouseAsset().getAssetList().size()>3)
				houseAssetData = detailData.getHouseAsset().getAssetList().subList(0, 3);
			else
				houseAssetData.addAll(detailData.getHouseAsset().getAssetList());
			
			listview_huxing.setAdapter(new CommonAdapter<AssetList>(context, houseAssetData, R.layout.item_house) {

				@Override
				public void convert(BaseViewHolder holder, AssetList itemData,
						int position, ViewGroup parent) {
					// TODO Auto-generated method stub
					holder.setText(R.id.tv_title, itemData.getName());
					holder.setText(R.id.tv_area, itemData.getArea()+"㎡");
					if(itemData.getPic()!=null && itemData.getPic().getHuxing()!=null && itemData.getPic().getHuxing().size()>0)
						holder.setImageByUrl(R.id.iv_img, itemData.getPic().getHuxing().get(0));
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		//楼盘卖点
		ListView listview_sales = (ListView) findViewById(R.id.listview_sales);
		try {
			List<TitleDesc> house_sellingdata = new ArrayList<>();
			if(detailData.getHouse_selling().size()>2)
				house_sellingdata = detailData.getHouse_selling().subList(0, 2);
			else
				house_sellingdata.addAll(detailData.getHouse_selling());
			
			listview_sales.setAdapter(new CommonAdapter<TitleDesc>(context, house_sellingdata, R.layout.item_param) {

				@Override
				public void convert(BaseViewHolder holder, TitleDesc itemData,
						int position, ViewGroup parent) {
					// TODO Auto-generated method stub
					holder.setText(R.id.tv_tag, itemData.getTitle()+"：");
					holder.setText(R.id.tv_value, itemData.getDesc());
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		//楼盘参数
		ListView listview_params = (ListView) findViewById(R.id.listview_params);
		try {
			List<TitleDesc> house_paramsdata = new ArrayList<>();
			if(detailData.getHouse_params().size()>2)
				house_paramsdata = detailData.getHouse_params().subList(0, 2);
			else
				house_paramsdata.addAll(detailData.getHouse_params());
			
			
			listview_params.setAdapter(new CommonAdapter<TitleDesc>(context, house_paramsdata, R.layout.item_param) {

				@Override
				public void convert(BaseViewHolder holder, TitleDesc itemData,
						int position, ViewGroup parent) {
					// TODO Auto-generated method stub
					holder.setText(R.id.tv_tag, itemData.getTitle()+"：");
					holder.setText(R.id.tv_value, itemData.getDesc());
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		//案场
		setText(R.id.connect_anchuang, "联系案场("+detailData.getLingkan().getName()+")");
	}
	
	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_title_right_second:
			if(MyApplication.getInstance().isUserLogin()){
				if(isCollect)
					requestCancelCollect();
				else
					requestCollect();
			}else
        		jumpToNextActivity(LoginActivity.class, false);
			break;
		case R.id.bt_title_right:
			share();
			break;
		case R.id.bt_my_kehu:
			if(MyApplication.getInstance().isUserLogin()){
				jumpToNextActivity(MyKehuActitvty.class, false);
			}
        	else
        		jumpToNextActivity(LoginActivity.class, false);
			
			break;
		case R.id.bt_baobei:
			if(MyApplication.getInstance().isUserLogin()){
				startActivity(new Intent(context, BaobeiKehuActivity.class)
				.putExtra("loupanId", detailData.getId())
				.putExtra("loupanName", detailData.getHouseName()));	
			}
        	else
        		jumpToNextActivity(LoginActivity.class, false);
			break;
		case R.id.tv_money_detail:
			startActivity(new Intent(context, WebViewActivity.class).putExtra("URI", detailData.getCommissionInfo().getLinkUrl()).putExtra("title", "佣金奖励"));
			break;
		case R.id.iv_position:
			startActivity(new Intent(context, LocationActivity.class)
			.putExtra("lat", Double.valueOf(detailData.getLat()))
			.putExtra("lon", Double.valueOf(detailData.getLon()))
			.putExtra("title", detailData.getHouseName())
			.putExtra("content", detailData.getHouseLocation()));
			break;
		case R.id.tv_type_detail:
			//户型
			if(detailData.getHouseAsset().getAssetList()!=null && detailData.getHouseAsset().getAssetList().size()>0){
				//
				startActivity(new Intent(context, HuxingPicsActivity.class)
				.putExtra("houseAsset", detailData.getHouseAsset()));
			}
			break;
		case R.id.tv_sale_detail:
			if(detailData.getHouse_selling()!=null && detailData.getHouse_selling().size()>0){
				//楼盘卖点
				startActivity(new Intent(context, SalePointActivity.class)
				.putExtra("title", "楼盘卖点")
				.putExtra("datas", new TitleDescs(detailData.getHouse_selling()))
				);
			}
			break;
		case R.id.tv_params_more:
			if(detailData.getHouse_selling()!=null && detailData.getHouse_selling().size()>0){
				//楼盘参数
				startActivity(new Intent(context, SalePointActivity.class)
				.putExtra("title", "楼盘参数")
				.putExtra("datas", new TitleDescs(detailData.getHouse_params()))
						);
			}
			break;
		case R.id.view_anchuang:
			if(detailData.getLingkan()!=null && detailData.getLingkan().getPhone()!=null){
				//联系案场
				CommonUtils.readyCall(context, detailData.getLingkan().getPhone());
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * 分享
	 */
	private void share(){
		ShareSDK.initSDK(this);
		 OnekeyShare oks = new OnekeyShare();
		 //关闭sso授权
		 oks.disableSSOWhenAuthorize(); 
		 
		// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
		 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		 oks.setTitle(detailData.getHouseName());
		 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		 String shareUrl = "http://139.224.44.8/app/share.html?id="+detailData.getId()+"&phone="+SharedPreference.getString(context, "phone")+"&referee_id="+MyApplication.getInstance().getMemberId();
		 oks.setTitleUrl(shareUrl);
		 // text是分享文本，所有平台都需要这个字段
		 oks.setText(detailData.getHouseLocation());
		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		 //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		 // url仅在微信（包括好友和朋友圈）中使用
		 oks.setUrl(shareUrl);
		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//		 oks.setComment(shareContent);
		 // site是分享此内容的网站名称，仅在QQ空间使用
		 oks.setSite(shareUrl);
		 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
		 oks.setSiteUrl(shareUrl);
		 if(detailData.getHouseAsset().getHouseImg()!=null && detailData.getHouseAsset().getHouseImg().size()>0)
			 oks.setImageUrl(detailData.getHouseAsset().getHouseImg().get(0));
		 
		// 启动分享GUI
		 oks.show(this);
	}
	
}
