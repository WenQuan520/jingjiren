package cn.softbank.purchase.activivty;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import cn.softbank.purchase.utils.Constant;
import cn.softbank.purchase.utils.MyWebViewClient;
import cn.yicheng.jingjiren.R;

public class WebViewActivity extends BaseWebView {

	private WebView mWebView;
    String title = "";
	String htmlContent="";
//	Handler uiHandler;// = new Handler();
//	private boolean isShow=false;
//	private String currentUrl="";
	String url = "";
//	private ArrayMap<String, String> titleMap = new ArrayMap<String, String>();
//    private  boolean needRefresh=false;

    @SuppressLint("JavascriptInterfabooleance")
	@Override
	protected void initView() {
		setContentView(R.layout.common_webview);

		progress_bar = (ProgressBar) findViewById(R.id.common_webview_progress);
		mWebView = (WebView) findViewById(R.id.common_webview);
		isContainTitleBar = true;

		if (getIntent().getExtras() != null) {
			url = getIntent().getStringExtra("URI");
			title = getIntent().getStringExtra("title");
			htmlContent = getIntent().getStringExtra("htmlContent");
		} else {
			url = "https://www.baidu.com/";
			title = "";
		}
//        url ="http://h5.mamahao.com/sdk.html";//测试用
		if(!TextUtils.isEmpty(mWebView.getTitle()))
			title = mWebView.getTitle();
			
		initTitleBar(title, Constant.DEFAULT_LEFT_BACK, "");
		
		mWebView.setWebViewClient(new MyWebViewClient(progress_bar,context));
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				progress_bar.setProgress(newProgress);
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
			}
			
			
		});
        initWebView(mWebView);
        
		if(!TextUtils.isEmpty(htmlContent)){
			mWebView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null);
		}else{
			mWebView.loadUrl(url);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			backOperate();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
//	private void backOperate(){
//		if(!TextUtils.isEmpty(backOperate)){
//			//自定义返回操作
//			if(backOperate.equals("close"))
//				finish();
//			else if(backOperate.startsWith("http"))
//				mWebView.loadUrl(backOperate);
//
//			backOperate = null;
//		}else if(mWebView.canGoBack()){
//			mWebView.goBack();
//		}else{
//			if(AppManager.getInstance().getAllActivity().size()>1){
//				finish();
//			}else{
//				jumpToNextActivity(MainActivity.class, true);
//			}
//		}
//	}
	
	// }
//@Override
//protected void onResume() {
//	// TODO Auto-generated method stub
//	super.onResume();
//	isShow=true;
//    if(needRefresh && !TextUtils.isEmpty(SharedPreference.getString(WebViewActivity.this,SharedPreference.memberId))){
//        mWebView.reload();
//        needRefresh=false;
//    }
//}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// finish();
		isShow=false;
	}

	@Override
	protected void initData() {

	}
@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	switch (v.getId()) {
	case R.id.bt_title_left:
		backOperate();
		break;
	default:
		break;
	}
}
//    Handler uiHandler=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            initTitleBar(title, Constant.DEFAULT_LEFT_BACK, "更多");
//        }
//    };
	@Override
	protected void processClick(View v) {
	}
	
}
