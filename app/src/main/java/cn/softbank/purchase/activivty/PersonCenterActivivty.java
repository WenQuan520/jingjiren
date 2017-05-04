package cn.softbank.purchase.activivty;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.ImageView;

import cn.softbank.purchase.base.BaseActivity;
import cn.softbank.purchase.base.MyApplication;
import cn.softbank.purchase.dialog.ChoiseBabySex;
import cn.softbank.purchase.domain.UserInfo;
import cn.softbank.purchase.fragment.MyFragment;
import cn.softbank.purchase.network.AbstractRequest.MamaHaoError;
import cn.softbank.purchase.network.BeanRequest;
import cn.softbank.purchase.network.JsonElementRequest;
import cn.softbank.purchase.network.NetworkManager;
import cn.softbank.purchase.network.ReqTag;
import cn.softbank.purchase.network.entity.DefaultMamahaoServerError;
import cn.softbank.purchase.network.entity.MamaHaoServerError;
import cn.softbank.purchase.utils.Base64;
import cn.softbank.purchase.utils.Constant;
import cn.softbank.purchase.utils.ImageUtils;
import cn.softbank.purchase.utils.SharedPreference;
import cn.yicheng.jingjiren.R;

import com.google.gson.JsonElement;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 个人信息
 * @author 巩兴华
 *
 */
public class PersonCenterActivivty extends BaseActivity {

	private ImageView iv_icon;
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.person_center_activivty);
		initTitleBar(getString(R.string.person_info), Constant.DEFAULT_LEFT_BACK, null);
		
		//个人信息内容
		iv_icon = findImageView(R.id.iv_icon);
		
		//点击监听
		findViewById(R.id.rela_icon).setOnClickListener(this);
		
	}

	/** 性别选择框 **/
	private ChoiseBabySex mDialogSex;
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		//设置用户个人信息
		requestUserInfo();
	}
	
	private final int REQUEST_USERINFOS = 0;
	/**
	 * 请求订单数据
	 */
	private void requestUserInfo() {
		showProgressBar(null);
		
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
		
		addRequestQueue(req, REQUEST_USERINFOS);
	}

	private final int REQUEST_CODE_ICON = 100;
	private final int REQUEST_CODE_EDIT_PIC = 105;
	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rela_icon:
			//修改头像
			startActivityForResult(new Intent(context, PickNativePics.class).putExtra("picsLimit", 1), REQUEST_CODE_ICON);
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(data==null)
			return;
		switch (requestCode) {
		case REQUEST_CODE_ICON:
			// 前去截取图片
			if (null != data.getStringArrayListExtra("photos")
					&& data.getStringArrayListExtra("photos").size() > 0)
				this.startActivityForResult(new Intent(this,
						ClipingActivity.class).putExtra("path", data
						.getStringArrayListExtra("photos").get(0)),
						REQUEST_CODE_EDIT_PIC);
			break;
		case REQUEST_CODE_EDIT_PIC:
			if (resultCode == ClipingActivity.CLIPING_SUCCESS) {
				// progressdialog.show();
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						byte[] bis = data.getByteArrayExtra("bitmap");
						final Bitmap bitmap = BitmapFactory
								.decodeByteArray(bis, 0, bis.length);
						if (bitmap != null) {
							try {
								String headPicName = ClipingActivity.CLIPINGDIR;
								final File file = ClipingActivity.saveFile(
										headPicName, bitmap,
										PersonCenterActivivty.this);
								runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										ImageLoader.getInstance().displayImage("file://"+file.getAbsolutePath(), iv_icon);
										//请求上传头像
										requestUploadPic(file);
									}
								});
								//上传
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}).start();
			} else if (resultCode == ClipingActivity.CLIPING_RETURN) {
				// 截取失敗
				startActivityForResult(
						new Intent(this, PickNativePics.class).putExtra("picsLimit", 1),
						REQUEST_CODE_ICON);
			}
			break;

		default:
			break;
		}
	}
	
	/** 请求标识 **/
	private final int REQUEST_UPLOAD_PIC = 1;
	/**
	 * 请求保存个人信息
	 */
	private void requestUploadPic(final File file) {
		showProgressBar(null);
		JsonElementRequest<DefaultMamahaoServerError> req = new JsonElementRequest<DefaultMamahaoServerError>(
				context,"",this);
		
		req.setParam("apiCode", "_user_avatar_upd");
		req.setParam("userId", MyApplication.getInstance().getMemberId());
		req.setParam("avatar", getImage(file));
		
		//获取token
		String token = NetworkManager.getInstance().getPostToken(req.getParam());
		req.setParam("token", token);
		
		addRequestQueue(req, REQUEST_UPLOAD_PIC, new ReqTag.Builder().handleSimpleRes(true));
	
	}
	
	private String getImage(File file){
        InputStream in = null;  
        byte[] data = null;  
        // 读取图片字节数组  
        try {  
            in = new FileInputStream(file);  
            data = new byte[in.available()];  
            in.read(data);  
            in.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        // 对字节数组Base64编码  
        return Base64.encode(data);// 返回Base64编码过的字节数组字符串  
	}
	
	@Override
	protected void onResponseSuccess(ReqTag tag, Object data) {
		// TODO Auto-generated method stub
		super.onResponseSuccess(tag, data);
		switch (tag.getReqId()) {
		case REQUEST_USERINFOS:
			//用户信息
//			 "avatar": "http://139.224.44.8/Uploads/Download/2016-09-22/dc665052fab36986.jpg",
//		     "name": "巩",
//		     "idCard": "430111111111111111",
//		     "level": "普通经纪人",
//		     "city": "北京市",
//		     "company": "杭州公司",
//		     "subShop": "上城门店",
//		     "user": "14225252"
			UserInfo userInfo = (UserInfo)data;
			showImage(userInfo.getAvatar(), iv_icon, ImageUtils.getImageOptions(R.drawable.mmq_ic_weidenglu));
			setText(R.id.tv_name, userInfo.getName());
			setText(R.id.tv_id, userInfo.getIdCard());
			setText(R.id.tv_level, userInfo.getLevel());
			setText(R.id.tv_city, userInfo.getCity());
			setText(R.id.tv_company, userInfo.getCompany());
			setText(R.id.tv_store, userInfo.getSubShop());
			setText(R.id.tv_login_name, userInfo.getUser());
			break;
		case REQUEST_UPLOAD_PIC:
			//头像上传成功
			SharedPreference.saveToSP(context, "avatar_url", ((JsonElement)data).getAsJsonObject().get("avatar").getAsString());
			MyFragment.userInfoChange = true;
			showToast(getString(R.string.save_icon_success));
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

}
