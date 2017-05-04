package cn.softbank.purchase.activivty;

import java.io.File;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import cn.softbank.purchase.base.BaseActivity;
import cn.softbank.purchase.base.MyApplication;
import cn.softbank.purchase.fragment.MyFragment;
import cn.softbank.purchase.network.AbstractRequest.MamaHaoError;
import cn.softbank.purchase.network.BeanRequest;
import cn.softbank.purchase.network.JsonElementRequest;
import cn.softbank.purchase.network.NetworkManager;
import cn.softbank.purchase.network.ReqTag;
import cn.softbank.purchase.network.entity.DefaultMamahaoServerError;
import cn.softbank.purchase.network.entity.MamaHaoServerError;
import cn.softbank.purchase.utils.CommonUtils;
import cn.softbank.purchase.utils.Constant;
import cn.softbank.purchase.utils.DownloadHelper;
import cn.softbank.purchase.utils.SharedPreference;
import cn.yicheng.jingjiren.R;

public class SettingActivity extends BaseActivity {

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_setting);
		initTitleBar(getString(R.string.set), Constant.DEFAULT_LEFT_BACK, null);
		
		findViewById(R.id.rela_connect_manager).setOnClickListener(this);
		findViewById(R.id.rela_suggest).setOnClickListener(this);
		findViewById(R.id.rela_about_us).setOnClickListener(this);
		findViewById(R.id.rela_check_version).setOnClickListener(this);
		findViewById(R.id.bt_ok).setOnClickListener(this);
		
		if(!TextUtils.isEmpty(SharedPreference.getString(context, "kehujingliName"))){
			setText(R.id.tv_manager, 
					SharedPreference.getString(context, "kehujingliName")
					+"("
					+SharedPreference.getString(context, "kehujingliPhone")
					+")");
		}
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		findTextView(R.id.tv_check_version)
			.setText(String.format(getString(R.string.appversion),
				CommonUtils.getAppVersion(this)));
	}

	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rela_connect_manager:
			CommonUtils.readyCall(context, SharedPreference.getString(context, "kehujingliPhone"));
			break;
		case R.id.rela_suggest:
			jumpToNextActivity(SuggestActivity.class, false);
			break;
		case R.id.rela_about_us:
			startActivity(new Intent(context, WebViewActivity.class).putExtra("URI", Constant.URL_GUANYUWOMEN).putExtra("title", "关于我们"));
			break;
		case R.id.rela_check_version:
			//检查版本
			requestVersion();
			break;
		case R.id.bt_ok:
			SharedPreference.clearUserData(context);
			MyFragment.userInfoChange = true;
			finish();
//			requestLoginOut();
			break;

		default:
			break;
		}
	}
	
	/** 请求标识 **/
	private final int REQUEST_LOGIN_OUT = 0;
	private final int REQUEST_VERSION = 1;
	/**
	 * 退出登录
	 */
	private void requestLoginOut(){
		showProgressBar(null);
		
		JsonElementRequest<DefaultMamahaoServerError> req = new JsonElementRequest<DefaultMamahaoServerError>(
				context,"",this);
		
		req.setParam("apiCode", "_logout_001");
		req.setParam("userToken", MyApplication.getInstance().getUserToken());
		
		//获取token
		String token = NetworkManager.getInstance().getPostToken(req.getParam());
		req.setParam("token", token);
	
		addRequestQueue(req, REQUEST_LOGIN_OUT, new ReqTag.Builder().handleSimpleRes(true));
	}
	
	/**
	 * 请求登录
	 */
	private void requestVersion() {
		showProgressBar(null);
		
		BeanRequest<UpdateVersion, DefaultMamahaoServerError> req = new BeanRequest<UpdateVersion, DefaultMamahaoServerError>(
                context,
                "", 
                this, UpdateVersion.class,false);
		
		req.setParam("apiCode", "_app_check_version");
		
		//获取token
		String token = NetworkManager.getInstance().getPostToken(req.getParam());
		req.setParam("token", token);
		
		addRequestQueue(req, REQUEST_VERSION, new ReqTag.Builder().handleSimpleRes(true));
	}
	
	@Override
	protected void onResponseSuccess(ReqTag tag, Object data) {
		// TODO Auto-generated method stub
		super.onResponseSuccess(tag, data);
		switch (tag.getReqId()) {
		case REQUEST_LOGIN_OUT:
			//退出登录成功
			SharedPreference.clearUserData(context);
			MyFragment.userInfoChange = true;
			finish();
			break;
		case REQUEST_VERSION:
			//版本信息
			UpdateVersion updateVersion = (UpdateVersion)data;
			//版本信息
			if(updateVersion.getApp_version().equals(CommonUtils.getAppVersionCode(context)))
				showToast(getString(R.string.already_new));
			else
				alertUpdate(updateVersion.getDescription(),updateVersion.getDownloadurl());
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
		case REQUEST_VERSION:
			//版本信息
			showToast(getString(R.string.already_new));
			break;

		default:
			break;
		}
	}
	
	public class UpdateVersion{
		private String app_version;
		private String downloadurl;
		private String description;
		public String getApp_version() {
			return app_version;
		}
		public void setApp_version(String app_version) {
			this.app_version = app_version;
		}
		public String getDownloadurl() {
			return downloadurl;
		}
		public void setDownloadurl(String downloadurl) {
			this.downloadurl = downloadurl;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		
	}
	
	
	private ProgressDialog progressDialog;
	private File file;
	protected void alertUpdate(String msg,final String url) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("版本更新").setMessage(msg)
				.setCancelable(false);

		builder.setPositiveButton("更新", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				final Message msg = new Message();
				progressDialog = new ProgressDialog(SettingActivity.this);
				progressDialog.setTitle("正在下载");
				progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressDialog.show();
				new Thread() {
					public void run() {
						file = new DownloadHelper().downloadNewVersion(url, progressDialog);
						progressDialog.dismiss();
						if (file == null) {
							msg.what = FAIL_DOWNLOAD;
							handler.sendMessage(msg);
						} else {
							msg.what = INSTALL;
							handler.sendMessage(msg);
						}
					};
				}.start();
			}

		});
		builder.setNegativeButton("下次", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});

		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	private final int ALERT_UPDATE = 0;
	private final int CONN_ERROR = 1;
	private final int INSTALL = 2;
	private final int FAIL_DOWNLOAD = 3;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case INSTALL:
				install();
				break;
			case CONN_ERROR:
				Toast.makeText(getApplicationContext(), "网络连接失败",Toast.LENGTH_SHORT).show();
				break;
			case FAIL_DOWNLOAD:
				Toast.makeText(getApplicationContext(), "下载失败",Toast.LENGTH_SHORT).show();
				break;
			}

		}
	};
	
	protected void install() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
		startActivity(intent);
	}

}
