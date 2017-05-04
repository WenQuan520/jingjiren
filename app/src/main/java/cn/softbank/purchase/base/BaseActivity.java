package cn.softbank.purchase.base;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.softbank.purchase.client.PurchaseApi;
import cn.softbank.purchase.network.AbstractRequest.ApiCallBack;
import cn.softbank.purchase.network.AbstractRequest.MamaHaoError;
import cn.softbank.purchase.network.ReqTag;
import cn.softbank.purchase.network.entity.MamaHaoServerError;
import cn.softbank.purchase.utils.AppManager;
import cn.softbank.purchase.utils.CommonUtils;
import cn.softbank.purchase.utils.Constant;
import cn.softbank.purchase.utils.OnClickContiNuousUtil;
import cn.softbank.purchase.utils.SharedPreference;
import cn.yicheng.jingjiren.R;

import com.android.volley.Request;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @描述 activity基类
 * @Copyright Copyright (c) 2016
 * @Company 广州软银信息科技有限科技
 * @author GXH
 * @version 1.0 
 */
@SuppressLint("NewApi")
public abstract class BaseActivity extends FragmentActivity implements
		OnClickListener, ApiCallBack {
	protected NotificationManager notificationManager;

	private boolean wasCreated, wasInterrupted;
	protected Context context;
	// title_view
	public View title_bar;
	protected TextView bt_title_left;
	protected TextView title_name;
	protected TextView bt_title_right;
	protected TextView bt_title_second_right;

	private int LEFT_FLAG = 1;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		context = this;

		initView();
		initData();
		// MobclickAgent.setSessionContinueMillis(1000 * 60);// 后台生命时长
		// MobclickAgent.updateOnlineConfig(this);
		// MobclickAgent.openActivityDurationTrack(false);//禁用默认统计时长方式
//		MobclickAgent.setDebugMode(true);
		// AnalyticsConfig.enableEncrypt(true);//

		this.wasCreated = true;
		AppManager.getInstance().addActivity(this);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		wasInterrupted = true;
	}

	/**
	 * 添加网络请求
	 * 
	 * @param req
	 * @param reqId
	 */
	public <T> void addRequestQueue(Request<T> req, int reqId) {
		addRequestQueue(req, reqId,new ReqTag.Builder());
	}

	/**
	 * 添加自定义内�?	 * 
	 * @param req
	 * @param reqId
	 * @param builder
	 */
	public <T> void addRequestQueue(Request<T> req, int reqId,ReqTag.Builder builder) {
		req.setTag(builder.reqGroupId(this.getClass().getName()).build(reqId));
		PurchaseApi.getInstance().add(req);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// 取消本activity对应的网络请�?		MamaHaoApi.getInstance().cacelAll(this.getClass().getName());
		AppManager.getInstance().finishActivity(this);
	}

	/**
	 * 显示图片
	 * 
	 * @param url
	 * @param imgView
	 * @param defaultImg
	 *            占位图资�?	 */
	public void showImage(String url, ImageView imgView,
			DisplayImageOptions options) {
		ImageLoader.getInstance().displayImage(url, imgView, options);
	}

	/**
	 * 初始化标�?	 * 
	 * @param title
	 *            中间标题，文字或图片资源ID
	 * @param left
	 *            左边标题，文字或图片资源ID
	 * @param right
	 *            右边标题，文字或图片资源ID
	 */
	protected <T> void initTitleBar(T title, T left, T right) {
		title_bar = findViewById(R.id.title_bar);
		bt_title_left = (TextView) title_bar.findViewById(R.id.bt_title_left);
		title_name = (TextView) title_bar.findViewById(R.id.title_name);
		bt_title_right = (TextView) title_bar.findViewById(R.id.bt_title_right);

		if (title instanceof String)
			title_name.setText((String) title);
		else if (title instanceof Integer)
			title_name.setCompoundDrawables(
					CommonUtils.GetDrawable(context, (Integer) title), null,
					null, null);

		if (left instanceof String) {
			bt_title_left.setCompoundDrawables(null, null, null, null);
			bt_title_left.setText((String) left);
		} else if (left instanceof Integer) {
			LEFT_FLAG = (Integer) left;
			if (LEFT_FLAG != Constant.DEFAULT_LEFT_BACK)
				bt_title_left.setCompoundDrawables(
						CommonUtils.GetDrawable(context, (Integer) left), null,
						null, null);
		} else
			bt_title_left.setVisibility(View.GONE);

		if (right instanceof String) {
			bt_title_right.setVisibility(View.VISIBLE);
			bt_title_right.setText((String) right);
			bt_title_right.setOnClickListener(this);
		} else if (right instanceof Integer) {
			bt_title_right.setVisibility(View.VISIBLE);
			bt_title_right.setCompoundDrawables(
					CommonUtils.GetDrawable(context, (Integer) right), null,
					null, null);
			bt_title_right.setOnClickListener(this);
		}

		bt_title_left.setOnClickListener(this);

	}

	/**
	 * 初始化标�?	 * 
	 * @param title
	 *            中间标题，文字或图片资源ID
	 * @param left
	 *            左边标题，文字或图片资源ID
	 * @param right
	 *            右边标题，文字或图片资源ID
	 * @param secondRight
	 *            右边倒数第二个标题，文字或图片资源ID
	 */
	protected <T> void initTitleBar(T title, T left, T right, T secondRight) {
		initTitleBar(title, left, right);

		bt_title_second_right = (TextView) title_bar
				.findViewById(R.id.bt_title_right_second);
		bt_title_second_right.setVisibility(View.VISIBLE);

		if (secondRight instanceof String) {
			bt_title_second_right.setText((String) secondRight);
			bt_title_second_right.setOnClickListener(this);
		} else if (secondRight instanceof Integer) {
			bt_title_second_right.setCompoundDrawables(
					CommonUtils.GetDrawable(context, (Integer) secondRight),
					null, null, null);
			bt_title_second_right.setOnClickListener(this);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// onresume时，取消notification显示
		// EMChatManager.getInstance().activityResumed();
		// umeng
//		MobclickAgent.onResume(this);
//		JPushInterface.onResume(this);
		// MobclickAgent.onPageStart(context.toString());
//		MobclickAgent.onResume(this);

	}

	@Override
	protected void onPause() {
		super.onPause();
		wasCreated = wasInterrupted = false;
//		JPushInterface.onPause(this);
		// MobclickAgent.onPageEnd(context.toString());
//		MobclickAgent.onPause(this);
	}

	public boolean isRestoring() {
		return wasInterrupted;
	}

	public boolean isResuming() {
		return !wasCreated;
	}

	public boolean isLaunching() {
		return !wasInterrupted && wasCreated;
	}

	protected TextView findTextView(int id) {
		return (TextView) findViewById(id);
	}

	protected ImageView findImageView(int id) {
		return (ImageView) findViewById(id);
	}
	
	protected void setText(int id,String text) {
		findTextView(id).setText(text==null?"":text);
	}
	
	protected void setText(int id,String text,String defaultStr) {
		findTextView(id).setText(TextUtils.isEmpty(text)?defaultStr:text);
	}

	/**
	 * 加载viewstub控件
	 * 
	 * @param id
	 */
	protected View inflateView(int id) {
		return ((ViewStub) findViewById(id)).inflate();
	}

	/**
	 * �?��Toast
	 * 
	 * @param msg
	 *            toast内容
	 */
	public void showToast(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * activity跳转
	 * 
	 * @param clazz
	 *            要跳转的activity
	 * @param isFinish
	 *            是否�?��当前activity
	 */
	public void jumpToNextActivity(Class<?> clazz, boolean isFinish) {
		startActivity(new Intent(this, clazz));
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
		if (isFinish)
			finish();
	}
	
	public String getIntentExtra(String key) {
		return getIntent().getStringExtra(key);
	}

	public int getIntentExtra(String key, int defaultValue) {
		return getIntent().getIntExtra(key, defaultValue);
	}

	public boolean getIntentExtra(String key, boolean defaultValue) {
		return getIntent().getBooleanExtra(key, defaultValue);
	}

	/**
	 * 用于处理网络请求后成功或错误提示，一般用于简单按钮操�?	 * 
	 * @param res
	 *            网络返回的结�?	 * @param toastTip
	 *            成功提示
	 * @param bf
	 *            fragment，activity则传�?	 */
	public boolean handleSimpleHttpRes(String res, String toastTip,
			BaseFragment bf) {
		if (res.contains(Constant.NET_SUCCESS_FLAG)) {
			if (!TextUtils.isEmpty(toastTip))
				Toast.makeText(context, toastTip, Toast.LENGTH_SHORT).show();
			return true;
		} else {
			Toast.makeText(context, getString(R.string.operate_fail),
					Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		// umeng
	}

	@Override
	public void onClick(View v) {
		// 校验快�?点击
		if (OnClickContiNuousUtil.isFastClick()) {
			return;
		}

		if (v.getId() == R.id.bt_title_left){
			this.finish();
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
		}
		else if (v.getId() == R.id.common_error_page_bt)
			refreshHttp(null);
		processClick(v);
	}

	/**
	 * 处理网络异常UI
	 * 
	 * @param <T>
	 * @param res
	 *            请求网络数据返回的异常结�?	 * @param bf
	 *            BaseFragment，如果不是在Fragment进行的请求则传null
	 * @return
	 */
	public <T> boolean handleNetErrorPage(T res, BaseFragment bf) {
		// 取消加载网络失败UI
		if (bf == null) {
			if (isShowingReLoadPage)
				hideReLoadPage(null);
		} else {
			if (bf.isShowingReLoadPage)
				hideReLoadPage(bf);
		}
		// 加载网络失败UI
		if (res instanceof String) {
			if (((String) res).equals(Constant.NET_NOT_AVAILABLE)) {
				showErrorPage(bf, Constant.DEFAULT_TOP_MARGIN);
				return false;
			}
		}
		return true;
	}

	/**
	 * 显示网络异常view
	 * 
	 * @param fragmentView
	 * @param topMargin
	 *            位置 DEFAULT_TITLE_BAR_LENGTH 代表默认，fragment默认�?，activity是标题栏高度
	 */
	private View errorPage;
	public boolean isShowingErrorPage = false;

	public synchronized void showErrorPage(BaseFragment bf, int topMargin) {
		// 可以执行操作
		if ((bf == null && !isShowingErrorPage)
				|| (bf != null && !bf.isShowingErrorPage)) {
			// 错误页面
			View errorView = bf == null ? errorPage : bf.errorPage;

			errorView = getCommonView(errorView, R.layout.common_error_page);

			// 刷新按钮
			Button bt_fresh = (Button) errorView
					.findViewById(R.id.common_error_page_bt);

			if (bf == null) {
				// activity
				bt_fresh.setOnClickListener(this);
				// 关联view
				errorPage = errorView;
				// 标志位置为true
				isShowingErrorPage = true;
				// 添加View
				addCommonView(null, errorView, topMargin);
			} else {
				// fragment
				bt_fresh.setOnClickListener(bf);
				// 关联view
				bf.errorPage = errorView;
				// 标志位置为true
				bf.isShowingErrorPage = true;
				// 添加View
				addCommonView(bf, errorView, topMargin);
			}

		}

	}

	public synchronized void showErrorPage(BaseFragment bf, int topMargin,
			String eorrorTxt) {
		// 可以执行操作
		if ((bf == null && !isShowingErrorPage)
				|| (bf != null && !bf.isShowingErrorPage)) {
			// 错误页面
			View errorView = bf == null ? errorPage : bf.errorPage;

			errorView = getCommonView(errorView, R.layout.common_error_page);
			TextView common_error_page_tv = (TextView) errorView
					.findViewById(R.id.common_error_page_tv);
			common_error_page_tv.setText(eorrorTxt);
			// 刷新按钮
			Button bt_fresh = (Button) errorView
					.findViewById(R.id.common_error_page_bt);

			if (bf == null) {
				// activity
				bt_fresh.setOnClickListener(this);
				// 关联view
				errorPage = errorView;
				// 标志位置为true
				isShowingErrorPage = true;
				// 添加View
				addCommonView(null, errorView, topMargin);
			} else {
				// fragment
				bt_fresh.setOnClickListener(bf);
				// 关联view
				bf.errorPage = errorView;
				// 标志位置为true
				bf.isShowingErrorPage = true;
				// 添加View
				addCommonView(bf, errorView, topMargin);
			}

		}

	}

	/**
	 * �?��公共View的获取方�?	 * 
	 * @param cacheView
	 * @param layoutId
	 *            布局ID
	 * @return
	 */
	private View getCommonView(View cacheView, int layoutId) {
		if (cacheView == null) {
			cacheView = View.inflate(context, layoutId, null);
			cacheView.setOnClickListener(this);
		}
		return cacheView;
	}

	/**
	 * 添加View
	 * 
	 * @param bf
	 * @param view
	 * @param topMargin
	 */
	private void addCommonView(BaseFragment bf, View view, int topMargin) {
		// 宽高
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT);
		// activity
		if (bf == null) {
			// marginTop值，默认为标题栏高度
			params.topMargin = topMargin == -1 ? getResources()
					.getDimensionPixelSize(R.dimen.title_bar_height)
					: topMargin;
			addContentView(view, params);
		}
		// fragment
		else {
			// marginTop值，默认�?
			params.topMargin = topMargin == -1 ? 0 : topMargin;
			((ViewGroup) bf.rootView).addView(view, params);
		}
	}

	/**
	 * 显示重新加载View
	 */
	private View progressBar;
	public boolean isShowingProgressBar = false;

	public synchronized void showProgressBar(BaseFragment bf) {
		if (bf == null) {
			if (!isShowingProgressBar) {
				progressBar = getCommonView(progressBar,
						R.layout.common_progress_bar);
				addCommonView(null, progressBar, Constant.DEFAULT_TOP_MARGIN);
				isShowingProgressBar = true;
			}
		}
		// 代表fragment
		else if (!bf.isShowingProgressBar) {
			bf.isShowingProgressBar = true;
			bf.progressBar = getCommonView(bf.progressBar,
					R.layout.common_progress_bar);
			addCommonView(bf, bf.progressBar, Constant.DEFAULT_TOP_MARGIN);
		}
	}

	/**
	 * 隐藏重新加载
	 */
	public synchronized void hideProgressBar(BaseFragment bf) {
		if (bf == null) {
			if (isShowingProgressBar) {
				isShowingProgressBar = false;
				((ViewGroup) progressBar.getParent()).removeView(progressBar);
			}
		} else {
			if (bf.isShowingProgressBar) {
				bf.isShowingProgressBar = false;
				((ViewGroup) bf.progressBar.getParent())
						.removeView(bf.progressBar);
			}
		}

	}
	
	public boolean isLogin(){
		return !TextUtils.isEmpty(SharedPreference.getString(context, "memberId"));
	}

	/**
	 * 显示重新加载View
	 */
	private View reloadPage;
	public boolean isShowingReLoadPage = false;

	public synchronized void showReLoadPage(BaseFragment bf, int topMargin) {
		if (bf == null) {
			if (!isShowingReLoadPage) {
				reloadPage = getCommonView(reloadPage,
						R.layout.common_reload_page);
				addCommonView(null, reloadPage, topMargin);
				isShowingReLoadPage = true;
			}
		}
		// 代表fragment
		else if (!bf.isShowingReLoadPage) {
			bf.isShowingReLoadPage = true;
			bf.reloadPage = getCommonView(bf.reloadPage,
					R.layout.common_reload_page);
			addCommonView(bf, bf.reloadPage, topMargin);
		}
	}

	/**
	 * 隐藏重新加载
	 */
	public synchronized void hideReLoadPage(BaseFragment bf) {
		if (bf == null) {
			if (isShowingReLoadPage) {
				isShowingReLoadPage = false;
				((ViewGroup) reloadPage.getParent()).removeView(reloadPage);
			}
		} else {
			if (bf.isShowingReLoadPage) {
				bf.isShowingReLoadPage = false;
				((ViewGroup) bf.reloadPage.getParent())
						.removeView(bf.reloadPage);
			}
		}

	}

	/**
	 * 隐藏ErrorPage
	 */
	public synchronized void hideErrorPage(BaseFragment bf) {
		if (bf == null) {
			if (isShowingErrorPage) {
				isShowingErrorPage = false;
				((ViewGroup) errorPage.getParent()).removeView(errorPage);
			}
		} else {
			if (bf.isShowingErrorPage) {
				bf.isShowingErrorPage = false;
				((ViewGroup) bf.errorPage.getParent()).removeView(bf.errorPage);
			}
		}

	}

	// error_page_view
	public boolean isShowingBlankPage = false;
	protected View blankPage = null;

	/**
	 * 空白数据UI
	 * 
	 * @param bf
	 *            加载blankPage的fragment，若是在activity加载则传null
	 * @param imgId
	 *            图片资源ID
	 * @param text
	 *            错误文本提示
	 * @param btText
	 *            按钮，不�?��按钮则传�?	 */
	public synchronized void showBlankPage(BaseFragment bf, int imgId,
			String text, String btText, int topMargin) {
		// 缓冲页面
		View cacheView = bf == null ? blankPage : bf.blankPage;
		// 初始化View
		cacheView = getCommonView(cacheView, R.layout.common_blank_page);

		// 空白页的图片,文本描述及按�?		
		((ImageView) cacheView.findViewById(R.id.common_blank_page_img)).setImageResource(imgId);
		((TextView) cacheView.findViewById(R.id.common_blank_page_tv)).setText(text);
		Button bt = (Button) cacheView.findViewById(R.id.common_blank_page_bt);
		if (TextUtils.isEmpty(btText))// 不显示按�?			
			bt.setVisibility(View.GONE);
		else {
			bt.setText(btText);
			bt.setOnClickListener(bf == null ? BaseActivity.this : bf);
		}

		// 添加blank_page
		if (bf == null) {
			if (!isShowingBlankPage) {
				isShowingBlankPage = true;
				blankPage = cacheView;
				addCommonView(null, cacheView, topMargin);
			}
		} else if (!bf.isShowingBlankPage) {
			bf.isShowingBlankPage = true;
			bf.blankPage = cacheView;
			addCommonView(bf, cacheView, topMargin);
		}
	}



	/**
	 * 隐藏空白UI
	 */
	public synchronized void hideBlankPage(BaseFragment bf) {
		if (bf == null) {
			if (isShowingBlankPage) {
				isShowingBlankPage = false;
				((ViewGroup) blankPage.getParent()).removeView(blankPage);
			}
		} else {
			if (bf.isShowingBlankPage) {
				bf.isShowingBlankPage = false;
				((ViewGroup) bf.blankPage.getParent()).removeView(bf.blankPage);
			}
		}
	}

	/**
	 * 重新请求网络数据
	 * 
	 * @param bf
	 *            BaseFragment，如果不是在Fragment进行的请求则传null
	 */
	public void refreshHttp(BaseFragment bf) {
		if (bf == null) {
			// 加载activity页面
			if (isShowingErrorPage) {
				hideErrorPage(bf);
			}
			showReLoadPage(null, Constant.DEFAULT_TOP_MARGIN);
			reQueryHttp();
		} else {
			// 加载Fragment页面
			if (bf.isShowingErrorPage) {
				hideErrorPage(bf);
			}
			showReLoadPage(bf, Constant.DEFAULT_TOP_MARGIN);
			bf.reQueryHttp();
		}
	}

	@Override
	public void onApiOnSuccess(ReqTag tag, Object data) {
		if (tag == null)
			return;
		handleNetRes(tag, null, null, null);
		onResponseSuccess(tag, data);
	}

	@Override
	public void onApiFailure(ReqTag tag, MamaHaoServerError error,
			MamaHaoError clientError) {
		if (tag == null)
			return;
		if (handleNetRes(tag, null, error, clientError))
			onResponseFailure(tag, error, clientError);
	}

	/**
	 * 网络连接失败后重新请求数�?	 */
	protected void reQueryHttp() {
	}

	/**
	 * 初始化控�?	 */
	protected abstract void initView();

	/**
	 * 初始化数�?	 */
	protected abstract void initData();

	/**
	 * 点击事件
	 */
	protected abstract void processClick(View v);

	/**
	 * first_in view屏蔽点击事件
	 * 
	 * @param view
	 */
	public void hide_onclick(View view) {
	}

	/**
	 * @描述 网络请求成功回调
	 */
	protected void onResponseSuccess(ReqTag tag, Object data) {
	}

	/**
	 * @描述 网络请求失败回调
	 */
	protected void onResponseFailure(ReqTag tag, MamaHaoServerError error,
			MamaHaoError clientError) {
	}

	/**
	 * 处理网络异常UI
	 * 
	 * @param tag
	 *            请求网络数据返回的异常结�?	 * @param bf
	 *            BaseFragment，如果不是在Fragment进行的请求则传null
	 * @return
	 */
	public boolean handleNetRes(ReqTag tag, BaseFragment bf,
			MamaHaoServerError error, MamaHaoError clientError) {
		// 隐藏Progress
		if (!tag.isDisAbleHideProgress())
			hideProgressBar(bf);
		// 取消加载网络失败UI
		if (tag.isHandleNetworkError()) {
			// 隐藏正在加载占位�?			
			if (bf == null) {
				if (isShowingReLoadPage)
					hideReLoadPage(null);
			} else if (bf.isShowingReLoadPage)
				hideReLoadPage(bf);
		}
		if (clientError != null
				&& (tag.isHandleNetworkError() || tag.isHandleSimpleRes())) {
			switch (clientError) {
			case AuthFailureError:
				Toast.makeText(context, "无权限！", Toast.LENGTH_SHORT).show();
				break;
			case ParseError:
//				Toast.makeText(context, "服务器返回数据异常！", Toast.LENGTH_SHORT)
//						.show();
				break;
			case ServerError:
				Toast.makeText(context, "服务器内部异常！", Toast.LENGTH_SHORT).show();
				break;
			case TimeoutError:
				Toast.makeText(context, "服务器忙", Toast.LENGTH_SHORT).show();
				break;
			case UnKnowError:
				Toast.makeText(context, "未知错误", Toast.LENGTH_SHORT).show();
				break;
			case NetworkError:
			case NoConnectionError:
				if (tag.isHandleNetworkError()) {
					showErrorPage(bf, Constant.DEFAULT_TOP_MARGIN);
					return false;
				} else
					Toast.makeText(context, "无网络连接，请检查！", Toast.LENGTH_SHORT)
							.show();
				break;
			default:
				break;
			}
		}
		if (error != null && tag.isHandleSimpleRes()) {
			Toast.makeText(context, error.msg, Toast.LENGTH_SHORT).show();
			// Toast.makeText(context,
			// MamaHaoApi.getInstance().getErrorInfo(error.errorCode),
			// Toast.LENGTH_SHORT).show();
		}
		return true;
	}

}
