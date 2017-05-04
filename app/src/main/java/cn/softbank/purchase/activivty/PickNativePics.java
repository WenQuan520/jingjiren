package cn.softbank.purchase.activivty;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.softbank.purchase.adapter.BaseViewHolder;
import cn.softbank.purchase.adapter.CommonAdapter;
import cn.softbank.purchase.base.BaseActivity;
import cn.softbank.purchase.dialog.ListImageDirPopupWindow;
import cn.softbank.purchase.dialog.ListImageDirPopupWindow.OnImageDirSelected;
import cn.softbank.purchase.domain.ImageFloder;
import cn.softbank.purchase.utils.CommonUtils;
import cn.softbank.purchase.utils.Constant;
import cn.softbank.purchase.utils.ImageUtils;
import cn.yicheng.jingjiren.R;

import com.nostra13.universalimageloader.core.ImageLoader;

public class PickNativePics extends BaseActivity  implements OnImageDirSelected{
	
	private ProgressDialog mProgressDialog;
	private GridView pick_native_pics_gridview;
	private RelativeLayout pick_native_pics__bottom_rl;
	private TextView pick_native_pics_choose_dir;
	private TextView pick_native_pics_total_count;
	private View view_popwin_mask;
	
	private CommonAdapter adapter;

	//所有的图片
	private List<String> mImgs;
	private List<String> allImgs;
	//文件夹
	private File mImgDir;
	// 存储文件夹中的图片数量
	private int mPicsSize;
	// 临时的辅助类，用于防止同一个文件夹的多次扫描
	private HashSet<String> mDirPaths = new HashSet<String>();
	// 扫描拿到所有的图片文件夹
	private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();
	private ListImageDirPopupWindow mListImageDirPopupWindow;
	//用户选择的图片，存储为图片的完整路径
	private ArrayList<String> mSelectedImage = new ArrayList<String>();
	int totalCount = 0;
	
	private File mPhotoFile;
	public static final int REQUEST_CODE_CAMERA = 1;
	private int picsLimit=-1;
	private boolean isQRCode=false;
	/**
	 * 相机显示标志
	 */
	private boolean isShowingAll=true;

	private Handler mHandler = new Handler()
	
	{
		public void handleMessage(android.os.Message msg)
		{
			mProgressDialog.dismiss();
			// 为View绑定数据
			data2View();
			// 初始化展示文件夹的popupWindw
			initListDirPopupWindw();
		}
	};

	@Override
	protected void initView() {
		setContentView(R.layout.pick_native_pics);
		
		initTitleBar(getString(R.string.pick_photos), Constant.DEFAULT_LEFT_BACK, getString(R.string.ok));
		
		pick_native_pics_gridview = (GridView) findViewById(R.id.pick_native_pics_gridview);
		pick_native_pics__bottom_rl=(RelativeLayout) findViewById(R.id.pick_native_pics__bottom_rl);
		pick_native_pics_choose_dir = (TextView) findViewById(R.id.pick_native_pics_choose_dir);
		pick_native_pics_total_count = (TextView) findViewById(R.id.pick_native_pics_total_count);
		view_popwin_mask=findViewById(R.id.view_popwin_mask);

		pick_native_pics__bottom_rl.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		picsLimit = getIntent().getIntExtra("picsLimit", -1);
		isQRCode = getIntent().getBooleanExtra("isQRCode", false);
		isShowingAll = !isQRCode;
		
		if(isQRCode)
			initTitleBar(getString(R.string.pick_photos), Constant.DEFAULT_LEFT_BACK, null);
		
		mImgs=new ArrayList<String>();
		allImgs=new ArrayList<String>();
		getImages();
	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.pick_native_pics__bottom_rl:
			//弹窗相册选择
			mListImageDirPopupWindow.setAnimationStyle(R.style.AnimBottom);
			mListImageDirPopupWindow.showAsDropDown(pick_native_pics__bottom_rl, 0, 0);
		
			// 设置背景颜色变暗
			WindowManager.LayoutParams lp = getWindow().getAttributes();
			lp.alpha = .3f;
			getWindow().setAttributes(lp);
			break;
		case R.id.bt_title_right:
			if(null == mSelectedImage||mSelectedImage.size() == 0){
				showToast(getString(R.string.pl_choosepic));
			}else{
				Intent i=getIntent();
				i.putStringArrayListExtra("photos", mSelectedImage);
				setResult(100, i);
				finish();
			}
			break;
		default:
			break;
		}

	}
	
	/**
	 * 为View绑定数据
	 */
	private void data2View()
	{
		pick_native_pics_gridview.setAdapter(new CommonAdapter<String>(context, mImgs, R.layout.pick_native_pics_grid_item) {

			@Override
			public void convert(BaseViewHolder holder, final String itemData,
					final int position, ViewGroup parent) {
				final ImageView mImageView = holder.getView(R.id.pick_native_pics);
				final ImageView mSelect = holder.getView(R.id.pick_native_pics_select);
				
				if(isShowingAll && position==0){
					//选择全部并且是第一个位置，加照相
					holder.setImageResource(R.id.pick_native_pics, R.drawable.img_take_pics);
					holder.setImageBitmap(R.id.pick_native_pics_select, null);
				}else{
					holder.setImageBySDcard(R.id.pick_native_pics, itemData, ImageUtils.imgOptionsSmall);
					if(!isQRCode)
						holder.setImageResource(R.id.pick_native_pics_select,R.drawable.checkbox_off);
				}
				
				mImageView.setColorFilter(null);
				//设置ImageView的点击事件
				mImageView.setOnClickListener(new OnClickListener()
				{
					private PopupWindow pw;

					//选择，则将图片变暗，反之则反之
					@Override
					public void onClick(View v)
					{
//						if(isQRCode){
//							//识别二维码
//							Result result=ScanningImage.getInstance().scanImage(itemData);
//							if(result==null)
//								Toast.makeText(PickNativePics.this, getString(R.string.scan_img_failed),Toast.LENGTH_SHORT).show();
//							else{
//								String resultString = result.getText();
//								if (TextUtils.isEmpty(resultString)) 
//									Toast.makeText(PickNativePics.this, getString(R.string.scan_img_failed),Toast.LENGTH_SHORT).show();
//								else{
//									if(getIntent()!=null){
//										Intent i=getIntent();
//										i.putExtra("qr_code", recode(resultString));
//										setResult(100, i);
//										finish();
//										overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
//									}
//								}
//							}
//							
//						}else{
							if(isShowingAll && position==0){
								//拍照   
								//选择数量限制
								if(picsLimit>-1 && picsLimit<=mSelectedImage.size())
									showToast(getString(R.string.sel_pics_limit).replace("{}", picsLimit+""));
								else
									selectPicFromCamera();
							}else{
								//查看大图
								View image=View.inflate(context, R.layout.image_view, null);
								ImageView iv=(ImageView) image.findViewById(R.id.image);
						        ImageLoader.getInstance().displayImage("file://"+itemData, iv, ImageUtils.imgOptionsBig);							
						      
						        if(pw==null){
						        	pw = new PopupWindow(image,LayoutParams.MATCH_PARENT , (int)(CommonUtils.getScreenSize(context)[1]*0.9), true);		
						        	pw.setOnDismissListener(new OnDismissListener() {
										
										@Override
										public void onDismiss() {
											view_popwin_mask.setVisibility(View.GONE);
										}
									});
						        	
						        	iv.setOnClickListener(new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											pw.dismiss();
										}
									});
						        }
						        view_popwin_mask.setVisibility(View.VISIBLE);
						        pw.setBackgroundDrawable(new BitmapDrawable());
						        pw.showAtLocation((View)title_bar.getParent(), Gravity.CENTER , 0, 0);
							}
						}
//					}
				});
				
				if(!isQRCode){
					mSelect.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							// 已经选择过该图片
							if (mSelectedImage.contains(itemData))
							{
								mSelectedImage.remove(itemData);
								mSelect.setImageResource(R.drawable.checkbox_off);
								mImageView.setColorFilter(null);
							} else{
								// 未选择该图片
								//选择数量限制
								if(picsLimit>-1 && picsLimit<=mSelectedImage.size()){
									showToast(getString(R.string.sel_pics_limit).replace("{}", picsLimit+""));
									return;
								}
								
								mSelectedImage.add(itemData);
								mSelect.setImageResource(R.drawable.checkbox_on);
								mImageView.setColorFilter(Color.parseColor("#77000000"));
							}
						}
					});
				}
				
				/**
				 * 已经选择过的图片，显示出选择过的效果
				 */
				if (mSelectedImage.contains(itemData))
				{
					mSelect.setImageResource(R.drawable.checkbox_on);
					mImageView.setColorFilter(Color.parseColor("#77000000"));
				}
				
			}
		});
		
		pick_native_pics_total_count.setText(totalCount + "张");
		pick_native_pics_choose_dir.setText(getString(R.string.all));
		
	};
	
	/**
	 * 中文乱码处理
	 * @param str
	 * @return
	 */
	private String recode(String str) {  
        String formart = "";  
  
        try {  
            boolean ISO = Charset.forName("ISO-8859-1").newEncoder()  
                    .canEncode(str);  
            if (ISO) {  
                formart = new String(str.getBytes("ISO-8859-1"), "GB2312");  
            } else {  
                formart = str;  
            }  
        } catch (Exception e) {  
             
        }  
        return formart;  
    }
	
	/**
	 * 照相获取图片
	 */
	public void selectPicFromCamera() {
		if (!CommonUtils.isExitsSdcard()) {
			String st = getResources().getString(R.string.sd_card_does_not_exist);
			showToast(st);
			return;
		}
		String local_file = Environment.getExternalStorageDirectory().getAbsolutePath()+"/mamhao/";
		File f = new File(local_file);

		if(!f.exists())
			f.mkdirs();
		
		local_file = f.getAbsolutePath()+"/"+System.currentTimeMillis() + ".jpg";

		mPhotoFile = new File(local_file);
		
        if (!mPhotoFile.exists()) { 
            try {
				mPhotoFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
        }  	
        
		startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile)),
				REQUEST_CODE_CAMERA);
	}
	
	/**
	 * 初始化展示文件夹的popupWindw
	 */
	private void initListDirPopupWindw()
	{
		mListImageDirPopupWindow = new ListImageDirPopupWindow(
				LayoutParams.MATCH_PARENT, (int) (CommonUtils.getScreenSize(context)[1] * 0.8),
				mImageFloders, LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.pick_native_pics_list_dir, null));

		mListImageDirPopupWindow.setOnDismissListener(new OnDismissListener()
		{

			@Override
			public void onDismiss()
			{
				// 设置背景颜色变暗
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1.0f;
				getWindow().setAttributes(lp);
			}
		});
		// 设置选择文件夹的回调
		mListImageDirPopupWindow.setOnImageDirSelected(this);
	}
	
	/**
	 * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描
	 */
	private void getImages()
	{
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
		{
			Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
			return;
		}
		// 显示进度条
		mProgressDialog = ProgressDialog.show(this, null, "正在加载...");

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				String firstImage = null;
				
				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = getContentResolver();

				// 只查询jpeg和png的图片
				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
						new String[] { "image/jpeg", "image/png" },
						MediaStore.Images.Media.DATE_MODIFIED);

				while (mCursor.moveToNext())
				{
					// 获取图片的路径
					String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
					
					if(!allImgs.contains(path))
						allImgs.add(0,path);
					
					// 拿到第一张图片的路径
					if (firstImage == null)
						firstImage = path;
					// 获取该图片的父路径名
					File parentFile = new File(path).getParentFile();
					if (parentFile == null)
						continue;
					String dirPath = parentFile.getAbsolutePath();
					ImageFloder imageFloder = null;
					// 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
					if (mDirPaths.contains(dirPath))
					{
						continue;
					} else
					{
						mDirPaths.add(dirPath);
						// 初始化imageFloder
						imageFloder = new ImageFloder();
						imageFloder.setDir(dirPath);
						imageFloder.setFirstImagePath(path);
					}

					String[] spics=parentFile.list(new FilenameFilter()
					{
						@Override
						public boolean accept(File dir, String filename)
						{
							if (filename.endsWith(".jpg")
									|| filename.endsWith(".png")
									|| filename.endsWith(".jpeg"))
								return true;
							return false;
						}
					});
					if(spics!=null && spics.length>0){
						
						int picSize = spics.length;
						totalCount += picSize;
						
						imageFloder.setCount(picSize);
						mImageFloders.add(imageFloder);
						
						if (picSize > mPicsSize)
						{
							mPicsSize = picSize;
							mImgDir = parentFile;
						}
					}
					
				}
				mCursor.close();
				
				// 扫描完成，辅助的HashSet也就可以释放内存了
				mDirPaths = null;
				
				//将所有照片装入
                if (allImgs != null & !allImgs.isEmpty()) {
				ImageFloder imageFloder=new ImageFloder();
				imageFloder.setDir("/"+getString(R.string.all));
				imageFloder.setFirstImagePath(allImgs.get(0));
				imageFloder.setCount(allImgs.size());
				mImageFloders.add(0, imageFloder);
                }
                // 装入拍照
                if (isShowingAll)
                    allImgs.add(0, "");
				//默认初始显示所有图片
				mImgs.addAll(allImgs);
				// 通知Handler扫描图片完成
				mHandler.sendEmptyMessage(0x110);

			}
		}).start();

	}
	
	@Override
	public void selected(ImageFloder floder) {
		if(floder.getName().equals(getString(R.string.all))){
			//全部
			if(isQRCode){
				//识别二维码
				if(floder.getCount()==allImgs.size()){
					//显示全部
					mSelectedImage.clear();
					mImgs.clear();
					mImgs.addAll(allImgs);
				}
			}else{
				//选照片
				if(floder.getCount()==allImgs.size()-1){
					isShowingAll=true;
					mSelectedImage.clear();
					mImgs.clear();
					mImgs.addAll(allImgs);
				}
			}
		}else{
			//某文件夹
			isShowingAll=false;
			mImgDir = new File(floder.getDir());
			
			List<String> dirImgs = Arrays.asList(mImgDir.list(new FilenameFilter()
			{
				@Override
				public boolean accept(File dir, String filename)
				{
					if (filename.endsWith(".jpg") || filename.endsWith(".png")
							|| filename.endsWith(".jpeg"))
						return true;
					return false;
				}
			}));
			
			mSelectedImage.clear();
			mImgs.clear();
			String path=mImgDir.getAbsolutePath();
			for(int i=dirImgs.size()-1;i>=0;i--){
				mImgs.add(path+"/"+dirImgs.get(i));
			}
		}
		
		if(adapter==null)
			adapter=(CommonAdapter) pick_native_pics_gridview.getAdapter();
		adapter.notifyDataSetChanged();
		
		pick_native_pics_choose_dir.setText(floder.getName());
		pick_native_pics_total_count.setText(floder.getCount() + "张");
		mListImageDirPopupWindow.dismiss();
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	super.onActivityResult(requestCode, resultCode, data);
    	if (resultCode!=RESULT_CANCELED && requestCode == REQUEST_CODE_CAMERA) { // 发送照片
			if (mPhotoFile != null && mPhotoFile.exists()){
				mSelectedImage.add(mPhotoFile.getAbsolutePath());
				if(getIntent()!=null){
					Intent i=getIntent();
					i.putStringArrayListExtra("photos", mSelectedImage);
					setResult(100, i);
					finish();
				}
			}
		}
    }

}
