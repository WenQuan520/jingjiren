package cn.softbank.purchase.activivty;

import java.io.File;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.softbank.purchase.base.BaseActivity;
import cn.softbank.purchase.base.MyApplication;
import cn.softbank.purchase.fragment.CustomerFragment;
import cn.softbank.purchase.fragment.HomeFragment;
import cn.softbank.purchase.fragment.MyFragment;
import cn.softbank.purchase.network.BeanRequest;
import cn.softbank.purchase.network.NetworkManager;
import cn.softbank.purchase.network.ReqTag;
import cn.softbank.purchase.network.AbstractRequest.MamaHaoError;
import cn.softbank.purchase.network.entity.DefaultMamahaoServerError;
import cn.softbank.purchase.network.entity.MamaHaoServerError;
import cn.softbank.purchase.utils.AppManager;
import cn.softbank.purchase.utils.CommonUtils;
import cn.softbank.purchase.utils.DownloadHelper;
import cn.yicheng.jingjiren.R;


public class MainActivity extends BaseActivity{
	
	private HomeFragment homePage;
	private CustomerFragment customerPage;
	private MyFragment myPage;
	
	private Fragment[] fragmentPages;
	private TextView[] tv_tabs;
	private int[] tab_icons;
	public static MainActivity instance;
	
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.main_activivty);
	}
	
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		//fragment页面
		instance = this;
		
		requestVersion();
		
		homePage = new HomeFragment();
		customerPage = new CustomerFragment();
		myPage = new MyFragment();
        fragmentPages = new Fragment[] {homePage, customerPage ,myPage};
        
        //底部tab
        tv_tabs = new TextView[]{findTextView(R.id.tab_home),
        		findTextView(R.id.tab_cart),
        		findTextView(R.id.tab_my)};
        
        for(TextView tv:tv_tabs)
        	tv.setOnClickListener(this);
        
        //底部tab图片资源id
        tab_icons = new int[]{R.drawable.jjr_ic_lp_1,
        		R.drawable.jjr_ic_wdkh_1,
        		R.drawable.jjr_ic_wo_1,
        		R.drawable.jjr_ic_lp_2,
        		R.drawable.jjr_ic_wdkh_2,
        		R.drawable.jjr_ic_wo_2};

		// 添加显示第一个fragment
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_container, homePage).commit();
		
	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.tab_home:
			changeTab(0);
			break;
        case R.id.tab_cart:
        	if(MyApplication.getInstance().isUserLogin())
        		changeTab(1);
        	else
        		jumpToNextActivity(LoginActivity.class, false);
            break;
        case R.id.tab_my:
        	changeTab(2);
            break;

		default:
			break;
		}
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	
	private int currentTabIndex;
	/**
	 * 切换tab
	 * @param index
	 */
	@SuppressWarnings("deprecation")
	public void changeTab(int index) {
		//当前tab不执行操作
		if (currentTabIndex == index)
			return;
		
		//切换之前选中tab的图标和文字颜色
		tv_tabs[currentTabIndex].setTextColor(getResources().getColor(
				R.color.C7));
		tv_tabs[currentTabIndex].setCompoundDrawables(null, CommonUtils.GetDrawable(
				context, tab_icons[currentTabIndex]), null, null);
		//切换当前选中tab的图标和文字颜色
		tv_tabs[index].setTextColor(getResources().getColor(
				R.color.title_bg));
		tv_tabs[index].setCompoundDrawables(null, CommonUtils.GetDrawable(
				context, tab_icons[index+tv_tabs.length]), null, null);
			
		FragmentTransaction ft = getSupportFragmentManager()
				.beginTransaction();
		ft.hide(fragmentPages[currentTabIndex]);
		if (!fragmentPages[index].isAdded()) {
			ft.add(R.id.fragment_container, fragmentPages[index]);
		}
		ft.show(fragmentPages[index]).commit();
		
		currentTabIndex = index;
		
	}
	
	private int mBackKeyPressedTimes = 0;  
    @Override  
    public void onBackPressed() {  
        if (mBackKeyPressedTimes == 0) {  
        	showToast(getString(R.string.exit_app));
            mBackKeyPressedTimes = 1;  
            new Thread() {  
                @Override  
                public void run() {  
                    try {  
                            Thread.sleep(2000);  
                    } catch (InterruptedException e) {  
                            e.printStackTrace();  
                    } finally {  
                            mBackKeyPressedTimes = 0;  
                    }  
                }  
            }.start();  
            return;  
               
        } else{
            AppManager.getInstance().finishAllActivity();
//        	finish();
         }  
        super.onBackPressed();  
    }
    
    private final int REQUEST_VERSION = 1;
    
    /**
	 * 请求登录
	 */
	private void requestVersion() {
//		showProgressBar(null);
		
		BeanRequest<UpdateVersion, DefaultMamahaoServerError> req = new BeanRequest<UpdateVersion, DefaultMamahaoServerError>(
                context,
                "", 
                this, UpdateVersion.class,false);
		
		req.setParam("apiCode", "_app_check_version");
		
		//获取token
		String token = NetworkManager.getInstance().getPostToken(req.getParam());
		req.setParam("token", token);
		
		addRequestQueue(req, REQUEST_VERSION);
	}
	
	@Override
	protected void onResponseSuccess(ReqTag tag, Object data) {
		// TODO Auto-generated method stub
		super.onResponseSuccess(tag, data);
		switch (tag.getReqId()) {
		case REQUEST_VERSION:
			//版本信息
			UpdateVersion updateVersion = (UpdateVersion)data;
			//版本信息
			if(!updateVersion.getApp_version().equals(CommonUtils.getAppVersionCode(context)))
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
				progressDialog = new ProgressDialog(MainActivity.this);
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
