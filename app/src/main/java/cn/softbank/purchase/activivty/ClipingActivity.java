package cn.softbank.purchase.activivty;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import cn.softbank.purchase.base.BaseActivity;
import cn.softbank.purchase.utils.BitmapUtils;
import cn.softbank.purchase.widget.ClipImageView;
import cn.yicheng.jingjiren.R;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

/***
 * 头像截取界面
 * 
 * @author 博瑞
 * 
 */
public class ClipingActivity extends BaseActivity {

	public static final String CLIPINGDIR = "clipingPic.jpg";
	public static final String CLIPINGDIR_BABY = "clipingPic_baby.jpg";

	public static final int CLIPING_SUCCESS = 123;
	public static final int CLIPING_RETURN = 321;

	private ClipImageView imageView;

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_cliping);

		imageView = (ClipImageView) findViewById(R.id.src_pic);
		findViewById(R.id.rela_cliping).setOnClickListener(this);

		if (this.getIntent() != null){
			ImageLoader.getInstance().displayImage("file://" + this.getIntent().getStringExtra("path"), imageView, new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					// TODO Auto-generated method stub
					showToast("读取图片失败");
					setResult(CLIPING_RETURN, getIntent());
					finish();
				}
				
				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					// TODO Auto-generated method stub
					
				}
			});
		}
//			ImageLoader.getInstance().displayImage(
//					"file://" + this.getIntent().getStringExtra("path"),
//					imageView);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub

	}

	public void photoMic(View v) {
		// 使用照片
		Bitmap bitmap = imageView.clip();
		bitmap = BitmapUtils.comp(bitmap);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
		byte[] bitmapByte = baos.toByteArray();

		Intent intent = new Intent(this, ClipingActivity.class);
		intent.putExtra("bitmap", bitmapByte);
		setResult(CLIPING_SUCCESS, getIntent().putExtra("bitmap", bitmapByte));
		finish();
	}

	public void returnMic(View v) {
		// 重新拍照
		setResult(CLIPING_RETURN, this.getIntent());
		finish();
	}

	public static File saveFile(String fileName, Bitmap bm, Context context)
			throws IOException {
		File dirFile = StorageUtils.getIndividualCacheDirectory(context);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		File myCaptureFile = new File(dirFile.getAbsolutePath() + "/"
				+ fileName);
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(myCaptureFile));
		bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		bos.flush();
		bos.close();

		return myCaptureFile;
	}
}
