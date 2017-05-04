package cn.softbank.purchase.activivty;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;
import cn.softbank.purchase.base.BaseActivity;
import cn.softbank.purchase.utils.CommonUtils;

/**
 * H5界面的基类，所有h5调用的方法写在本类中，所有其他WebView界面均可以自动执行
 */
public class BaseWebView extends BaseActivity{

	public static final int REQUESTCODE = 1000;
	public static final int REQUESTCODE_DISTRI = 1001;
	public static final int REQUESTCODE_USER = 1002;

	public static final int FILECHOOSER_RESULTCODE = 1;

	public boolean isShow = false;
	private WebView mWebView;
	public boolean needRefresh = false;
	public String currentUrl = "";
	public String rightTitle = "";
	public ProgressBar progress_bar;
	public ArrayMap<String, String> titleMap = new ArrayMap<String, String>();
	public boolean isContainTitleBar;
	private LoadListener listener;
	private ValueCallback<Uri> mUploadMessage;
	private boolean isChoiseFile = false;
	public String appCachePath;

	public interface LoadListener {
		public void onLoadFinish();
	}

	public void setLoadListener(LoadListener listener) {
		this.listener = listener;
	}

	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isShow = true;
		if (mWebView != null&&!isChoiseFile)
			mWebView.reload();
		else
			isChoiseFile = false;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// finish();
		isShow = false;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// 清理Webview缓存数据库
		try {
			deleteDatabase("webview.db");
			deleteDatabase("webviewCache.db");
		} catch (Exception e) {
			e.printStackTrace();
		}
		needRefresh = false;
	}

	public void initWebView(WebView webView) {
		mWebView = webView;
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setDomStorageEnabled(true);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setAllowFileAccess(true);
		webSettings.setAppCacheEnabled(true);
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
		webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
		appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

		webSettings.setAppCachePath(appCachePath);
		webSettings.setUserAgentString(getUserAgent());
		mWebView.setWebChromeClient(new MyWebChromeClient());// alert
		mWebView.addJavascriptInterface(this, "demo");
		mWebView.loadUrl("javascript:alert(injectedObject.toString())");
		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				if (progress_bar != null)
					progress_bar.setVisibility(View.GONE);
				if (!TextUtils.isEmpty(url) && !currentUrl.equals(url)) {
					rightTitle = "";
				}

				if (mWebView.getUrl() != null)
					currentUrl = mWebView.getUrl();
				if (listener != null)
					listener.onLoadFinish();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// TODO Auto-generated method stub
				super.onReceivedError(view, errorCode, description, failingUrl);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				if (progress_bar != null)
					progress_bar.setVisibility(View.VISIBLE);
			}

		});
	}

	public final class MyWebChromeClient extends WebChromeClient {
		@Override
		@JavascriptInterface
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			result.confirm();
			return true;
		}

		public void openFileChooser(ValueCallback<Uri> uploadMsg,
				String acceptType) {
			if (mUploadMessage != null)
				return;
			mUploadMessage = uploadMsg;
		}

		// For Android < 3.0
		public void openFileChooser(ValueCallback<Uri> uploadMsg) {
			openFileChooser(uploadMsg, "");
		}

		// For Android > 4.1.1
		public void openFileChooser(ValueCallback<Uri> uploadMsg,
				String acceptType, String capture) {
			openFileChooser(uploadMsg, acceptType);
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			// TODO Auto-generated method stub
			if (progress_bar != null)
				progress_bar.setProgress(newProgress);
//			if (!TextUtils.isEmpty(titleMap.get(view.getUrl()))
//					&& isContainTitleBar) {
//				initTitleBar(titleMap.get(view.getUrl()),
//						Constant.DEFAULT_LEFT_BACK, null);
//			}
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			// TODO Auto-generated method stub
			super.onReceivedTitle(view, title);
//			if (isContainTitleBar)
//				initTitleBar(title, Constant.DEFAULT_LEFT_BACK, null);
//			titleMap.put(view.getUrl(), title);
//			if (bt_title_right != null)
//				bt_title_right.setVisibility(View.GONE);
		}

	}
	
	/**
	 * 检查SD卡是否存在
	 * 
	 * @return
	 */
	public final boolean checkSDcard() {
		boolean flag = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (!flag) {
			Toast.makeText(this, "请插入手机存储卡再使用本功能", Toast.LENGTH_SHORT).show();
		}
		return flag;
	}

	/***
	 * @1 得到 User-Agent: 手机型号，分辨率，系统版本，项目名称，项目版本
	 * @2 用这个格式传值
	 * @return
	 */
	public String getUserAgent() {

		String user_agent = "User-Agent: " + android.os.Build.MODEL + ","
				+ getWeithAndHeight() + ",Android "
				+ android.os.Build.VERSION.RELEASE + ","
				// + getString(R.string.app_name) + ","
				+ "mamahao 1" + ","
				+ CommonUtils.getAppVersion(BaseWebView.this);
		return user_agent;
	}

	/***
	 * 获取手机分辨率
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String getWeithAndHeight() {
		// 这种方式在service中无法使用，
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels; // 宽
		int height = dm.heightPixels; // 高

		// 在service中也能得到高和宽
		WindowManager mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		width = mWindowManager.getDefaultDisplay().getWidth();
		height = mWindowManager.getDefaultDisplay().getHeight();
		String strPM = width + "x" + height;
		return strPM;
	}

	private String address;

	public void startNet(String url) {
		// TODO Auto-generated method stub
		address = url;
		if (null != address && !"".equals(address)) {
			if (!address.startsWith("http://")) {
				if (address.startsWith("www.")) {
					address = "http://" + address;
					mWebView.loadUrl(address);
				} else {
					try {
						mWebView.loadUrl("http://www.baidu.com.cn/s?wd="
								+ URLEncoder.encode(address, "gb2312"));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				mWebView.loadUrl(address);
			}
		} else {
			Toast.makeText(this, "请先输入网址", Toast.LENGTH_SHORT).show();
		}
	}

	public void backOperate() {
		if (mWebView.canGoBack())
			mWebView.goBack();
		else
			finish();
	}

	Handler mHandler = new Handler();

	/**
	 * 清理缓存 clear the cache before time numDays
	 */
	private int clearCacheFolder(File dir, long numDays) {

		// WebView 缓存文件
		File appCacheDir = new File(appCachePath);

		File webviewCacheDir = new File(getCacheDir().getAbsolutePath()
				+ "/webviewCache");

		// 删除webview 缓存目录
		if (webviewCacheDir.exists()) {
			deleteFile(webviewCacheDir);
		}
		// 删除webview 缓存 缓存目录
		if (appCacheDir.exists()) {
			deleteFile(appCacheDir);
		}

		int deletedFiles = 0;
		if (dir != null && dir.isDirectory()) {
			try {
				for (File child : dir.listFiles()) {
					if (child.isDirectory()) {
						deletedFiles += clearCacheFolder(child, numDays);
					}
					if (child.lastModified() < numDays) {
						if (child.delete()) {
							deletedFiles++;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return deletedFiles;
	}

	public void deleteFile(File file) {

		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteFile(files[i]);
				}
			}
			file.delete();
		} 
	}

	/**
	 * js调用,清空历史记录
	 */
	@JavascriptInterface
	public void toClear() {
		mHandler.post(new Runnable() {
			public void run() {
				try {
					mWebView.clearHistory();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	
	/**
	 * js调用,关闭页面
	 */
	@SuppressLint("NewApi")
	@JavascriptInterface
	public void closeWebView() {
//		WebViewFragment.isNeedRefresh = true;
		finish();
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		
	}


}
