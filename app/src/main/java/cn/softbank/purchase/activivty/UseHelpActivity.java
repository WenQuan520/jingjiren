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

public class UseHelpActivity extends BaseActivity {

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_help);
		initTitleBar("使用帮助", Constant.DEFAULT_LEFT_BACK, null);
		
		findViewById(R.id.rela_zhuce).setOnClickListener(this);
		findViewById(R.id.rela_yewucaozuo).setOnClickListener(this);
		findViewById(R.id.rela_yejiguishu).setOnClickListener(this);
		findViewById(R.id.rela_wodeqianbao).setOnClickListener(this);
		findViewById(R.id.rela_xingweizhunze).setOnClickListener(this);
		findViewById(R.id.rela_tousujianyi).setOnClickListener(this);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rela_zhuce:
			startActivity(new Intent(context, WebViewActivity.class).putExtra("URI", Constant.URL_ZHUCE).putExtra("title", "注册账号"));
			break;
		case R.id.rela_yewucaozuo:
			startActivity(new Intent(context, WebViewActivity.class).putExtra("URI", Constant.URL_YEWUCAOZUO).putExtra("title", "业务操作"));
			break;
		case R.id.rela_yejiguishu:
			startActivity(new Intent(context, WebViewActivity.class).putExtra("URI", Constant.URL_YEJIGUISHU).putExtra("title", "业绩归属"));
			break;
		case R.id.rela_wodeqianbao:
			startActivity(new Intent(context, WebViewActivity.class).putExtra("URI", Constant.URL_WODEQIANBAO).putExtra("title", "我的钱包"));
			break;
		case R.id.rela_xingweizhunze:
			startActivity(new Intent(context, WebViewActivity.class).putExtra("URI", Constant.URL_XINGWEIZHUNZE).putExtra("title", "行为准则"));
			break;
		case R.id.rela_tousujianyi:
			startActivity(new Intent(context, WebViewActivity.class).putExtra("URI", Constant.URL_TOUSUJIANYI).putExtra("title", "投诉建议"));
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
				progressDialog = new ProgressDialog(UseHelpActivity.this);
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
