package cn.softbank.purchase.fragment;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.softbank.purchase.activivty.AgreementActivity;
import cn.softbank.purchase.activivty.CollectionActivity;
import cn.softbank.purchase.activivty.LoginActivity;
import cn.softbank.purchase.activivty.MessageSystemList;
import cn.softbank.purchase.activivty.MoneyManageActivivty;
import cn.softbank.purchase.activivty.PersonCenterActivivty;
import cn.softbank.purchase.activivty.SettingActivity;
import cn.softbank.purchase.activivty.UseHelpActivity;
import cn.softbank.purchase.base.BaseFragment;
import cn.softbank.purchase.base.MyApplication;
import cn.softbank.purchase.domain.Segment;
import cn.softbank.purchase.domain.SegmentList;
import cn.softbank.purchase.domain.UserInfo;
import cn.softbank.purchase.network.AbstractRequest.MamaHaoError;
import cn.softbank.purchase.network.BeanRequest;
import cn.softbank.purchase.network.NetworkManager;
import cn.softbank.purchase.network.ReqTag;
import cn.softbank.purchase.network.entity.MamaHaoServerError;
import cn.softbank.purchase.utils.ImageUtils;
import cn.yicheng.jingjiren.R;


public class MyFragment extends BaseFragment {
	
	private UserInfo userInfo;

	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.my_fragment, container, false);
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_icon:
			baseActivity.jumpToNextActivity(PersonCenterActivivty.class, false);
			break;
		case R.id.bt_login:
			baseActivity.jumpToNextActivity(LoginActivity.class, false);
			break;
		case R.id.rela_yongjin:
			if(MyApplication.getInstance().isUserLogin())
				baseActivity.jumpToNextActivity(MoneyManageActivivty.class, false);
			else
				baseActivity.jumpToNextActivity(LoginActivity.class, false);
			break;
		case R.id.rela_message:
			if(MyApplication.getInstance().isUserLogin())
				baseActivity.jumpToNextActivity(MessageSystemList.class, false);
			else
				baseActivity.jumpToNextActivity(LoginActivity.class, false);
			break;
		case R.id.rela_collection:
			if(MyApplication.getInstance().isUserLogin())
				baseActivity.jumpToNextActivity(CollectionActivity.class, false);
			else
				baseActivity.jumpToNextActivity(LoginActivity.class, false);
			break;
		case R.id.rela_help:
			baseActivity.jumpToNextActivity(UseHelpActivity.class, false);
			break;
		case R.id.rela_work:
			if(MyApplication.getInstance().isUserLogin())
				baseActivity.jumpToNextActivity(AgreementActivity.class, false);
			else
				baseActivity.jumpToNextActivity(LoginActivity.class, false);
			break;
		case R.id.rela_setting:
			if(MyApplication.getInstance().isUserLogin())
				baseActivity.jumpToNextActivity(SettingActivity.class, false);
			else
				baseActivity.jumpToNextActivity(LoginActivity.class, false);
			break;

		default:
			break;
		}
	}
	
	/** 用户头像 **/
	private ImageView iv_icon;
	/** 用户昵称 **/
	private TextView tv_name;
	/** 用户昵称 **/
	private TextView tv_shenfen;
	/** fragment创建标志 **/
	private boolean isShow;
	/**订单数目**/
	private TextView[] orderTexts;
	/**订单数目**/
	private String[] orderNums;
	
	@Override
	protected void onFirstUserVisible() {
		// TODO Auto-generated method stub
		super.onFirstUserVisible();
		iv_icon = findImageView(R.id.iv_icon);
		tv_name = findTextView(R.id.tv_name);
		tv_shenfen = findTextView(R.id.tv_shenfen);
		
		iv_icon.setOnClickListener(this);
		findView(R.id.rela_yongjin).setOnClickListener(this);
		findView(R.id.rela_message).setOnClickListener(this);
		findView(R.id.rela_collection).setOnClickListener(this);
		findView(R.id.rela_help).setOnClickListener(this);
		findView(R.id.rela_work).setOnClickListener(this);
		findView(R.id.rela_setting).setOnClickListener(this);
		
		
		orderTexts = new TextView[]{findTextView(R.id.tv_wait_baobei),
				findTextView(R.id.tv_wait_look),
				findTextView(R.id.tv_wait_deal),
				findTextView(R.id.tv_deal)};
		
		orderNums = new String[4];
		
		setUserInfo();
		
		if(MyApplication.getInstance().isUserLogin())
			requestSegmentList();
		
		
		//初始化标志
		userInfoChange = false;
		orderInfoChange = false;
		isShow = true;
	}
	
	public static boolean userInfoChange = false;
	public static boolean orderInfoChange = false;
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//用户信息发生变化调用
		if(userInfoChange && isShow){
			setUserInfo();
			userInfoChange = false;
		}
		
		if(orderInfoChange && isShow){
			requestSegmentList();
			orderInfoChange = false;
		}
	}
	
	/**
	 * 设置用户信息
	 */
	private void setUserInfo(){
		if(MyApplication.getInstance().isUserLogin()){
			//如果已登录
			findView(R.id.bt_login).setVisibility(View.GONE);
			iv_icon.setVisibility(View.VISIBLE);
			tv_name.setVisibility(View.VISIBLE);
			tv_shenfen.setVisibility(View.VISIBLE);
			
			requestUserInfo();
		}else{
			//显示登录按钮
			findView(R.id.bt_login).setVisibility(View.VISIBLE);
			findView(R.id.bt_login).setOnClickListener(this);
			iv_icon.setVisibility(View.GONE);
			tv_name.setVisibility(View.GONE);
			tv_shenfen.setVisibility(View.GONE);
			
			//所有未读数置为0,不显示
			for(TextView tv:orderTexts){
				tv.setText("0");
			}
		}
	}
	
	/** 请求标识 **/
	private final int REQUEST_GOODS_DATAS = 0;
	private final int REQUEST_USERINFOS = 1;
	
	/**
	 * 请求订单数据
	 */
	/** 请求标识 **/
	private void requestSegmentList() {
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
	
	/**
	 * 请求订单数据
	 */
	private void requestUserInfo() {
		baseActivity.showProgressBar(this);
		
		BeanRequest<UserInfo, MamaHaoServerError> req = new BeanRequest<>(
				context, 
				"", 
				this, 
				UserInfo.class);
		
		req.setParam("apiCode", "_user_info");
		req.setParam("userId", MyApplication.getInstance().getMemberId());
		
		//获取token
		String token = NetworkManager.getInstance().getPostToken(req.getParam());
		req.setParam("token", token);
		
		baseActivity.addRequestQueue(req, REQUEST_USERINFOS);
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
				orderNums[0] = datas.get(0).getCount();
				orderNums[1] = datas.get(1).getCount();
				orderNums[2] = datas.get(2).getCount();
				orderNums[3] = datas.get(3).getCount();
				
			}
			setOrderNums();
			break;
		case REQUEST_USERINFOS:
			userInfo = (UserInfo)data;
			baseActivity.showImage(userInfo.getAvatar(), iv_icon,ImageUtils.getImageOptions(R.drawable.mmq_ic_weidenglu));
			tv_name.setText(userInfo.getName());
			tv_shenfen.setText(userInfo.getLevel());
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
	
	/**
	 * 设置订单数量
	 */
	private void setOrderNums(){
		for(int i=0;i<orderTexts.length;i++){
			orderTexts[i].setText(orderNums[i]);
		}
	}
}
