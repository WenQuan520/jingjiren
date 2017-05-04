package cn.softbank.purchase.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;
import cn.softbank.purchase.network.AbstractRequest.ApiCallBack;
import cn.softbank.purchase.network.AbstractRequest.MamaHaoError;
import cn.softbank.purchase.network.ReqTag;
import cn.softbank.purchase.network.entity.MamaHaoServerError;
import cn.softbank.purchase.utils.OnClickContiNuousUtil;
import cn.yicheng.jingjiren.R;

/**
 * @描述 fragment基类
 * @Copyright Copyright (c) 2016
 * @Company 广州软银信息科技有限科技
 * @author GXH
 * @version 1.0 
 */
public abstract class BaseFragment extends Fragment
        implements OnClickListener,  ApiCallBack {

	protected Context context;
	protected BaseActivity baseActivity;
	public View rootView;
	public View errorPage;
	public View reloadPage;
	public View blankPage;
	public View progressBar;
	public boolean isShowingProgressBar = false;
	public boolean isShowingErrorPage=false;
	public boolean isShowingReLoadPage=false;
	public boolean isShowingBlankPage=false;
	public View common_first_in_view;
	//判断第一次有效载入fragment
	private boolean isActivityCreated;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		baseActivity = (BaseActivity)activity;
		context = activity;
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initData(savedInstanceState);
		//判断是否刚创建并可见
		isActivityCreated = true;
		judgeIsFirstVisible();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView  = initView(inflater,container);
		return rootView;
	}
	
	@Override
	public void onClick(View v) {
		// 校验快�?点击
		if (OnClickContiNuousUtil.isFastClick())
			return;
		
		if(v.getId()==R.id.common_error_page_bt){
			//重新请求网络数据
			baseActivity.refreshHttp(BaseFragment.this);
		}
		processClick(v);
	}
	
	protected View findView(int id){
		return rootView.findViewById(id);
	}
	
	protected TextView findTextView(int id){
		return (TextView) rootView.findViewById(id);
	}
	
	protected ImageView findImageView(int id){
		return (ImageView) rootView.findViewById(id);
	}
	/**
	 * 加载viewstub控件
	 * @param id
	 */
	protected View inflateView(int id){
		return ((ViewStub)rootView.findViewById(id)).inflate();
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(isAdded() && isActivityCreated && isVisibleToUser)
			onReVisible();
		judgeIsFirstVisible();
	}
	
	/**
	 * 判断是否第一次可�?
	 */
	private void judgeIsFirstVisible(){
		if(isActivityCreated && getUserVisibleHint()){
			isActivityCreated = false;
			onFirstUserVisible();
		}
	}
	
	/**
	 * 相当于onresume
	 */
	protected void onReVisible(){}

	/**
	 * 第一次fragment可见（进行初始化工作�?
	 */
	protected void onFirstUserVisible(){}

	/**
	 * 网络连接失败后重新请求数�?
	 */
	protected void reQueryHttp(){}
	
	protected abstract View initView(LayoutInflater inflater, ViewGroup container);

	protected abstract void initData(Bundle savedInstanceState);

	protected abstract void processClick(View v);

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

    @Override
    public void onApiOnSuccess(ReqTag tag, Object data) {
        if (tag != null && isAdded()){
        	baseActivity.handleNetRes(tag, this, null, null);
            onResponseSuccess(tag, data);
        }
    }

    @Override
    public void onApiFailure(ReqTag tag, MamaHaoServerError error, MamaHaoError clientError) {
        if (tag != null && isAdded()) {
        	if(baseActivity.handleNetRes(tag, this, error, clientError))
        		onResponseFailure(tag, error, clientError);
        }
    }

    /**
     * @描述 网络请求成功回调
     * @方法�?onResponseSuccess
     * @param tag
     * @param data
     * @返回类型 void
     * @创建�?WL
     * @创建时间 2015�?�?3日上�?:36:14
     * @修改�?WL
     * @修改时间 2015�?�?3日上�?:36:14
     * @修改备注 �?
     * @since �?
     * @throws �?
     */
    protected void onResponseSuccess(ReqTag tag, Object data) {}

    /**
     * @描述 网络请求失败回调
     * @方法�?onResponseFailure
     * @param tag
     * @param error
     * @param clientError
     * @返回类型 void
     * @创建�?WL
     * @创建时间 2015�?�?3日上�?:37:37
     * @修改�?WL
     * @修改时间 2015�?�?3日上�?:37:37
     * @修改备注 �?
     * @since �?
     * @throws �?
     */
    protected void onResponseFailure(ReqTag tag, MamaHaoServerError error,
            MamaHaoError clientError) {

    }

}
