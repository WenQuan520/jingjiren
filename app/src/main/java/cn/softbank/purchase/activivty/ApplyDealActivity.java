package cn.softbank.purchase.activivty;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import cn.softbank.purchase.base.BaseActivity;
import cn.softbank.purchase.base.MyApplication;
import cn.softbank.purchase.network.JsonElementRequest;
import cn.softbank.purchase.network.NetworkManager;
import cn.softbank.purchase.network.ReqTag;
import cn.softbank.purchase.network.AbstractRequest.MamaHaoError;
import cn.softbank.purchase.network.entity.DefaultMamahaoServerError;
import cn.softbank.purchase.network.entity.MamaHaoServerError;
import cn.softbank.purchase.utils.Base64;
import cn.softbank.purchase.utils.BitmapUtils;
import cn.softbank.purchase.utils.Constant;
import cn.softbank.purchase.utils.FileUtils;
import cn.softbank.purchase.utils.ShowNativePics;
import cn.yicheng.jingjiren.R;

public class ApplyDealActivity extends BaseActivity {

	private GridView photos_gridview;
	
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.apply_deal);
		initTitleBar("申请成交", Constant.DEFAULT_LEFT_BACK, "提交");
		
		photos_gridview=(GridView) findViewById(R.id.photos_gridview);
		
		initPicImgs();
	}
	
	/**
	 * 图片显示器
	 */
	private ShowNativePics showNativePics;
	//选择数量限制
	private int picsLimit=9;
	private void initPicImgs(){
		showNativePics = new ShowNativePics(this, photos_gridview, picsLimit);
		photos_gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(position==showNativePics.photoPaths.size()-1 && showNativePics.lastPostion!=-1)
					getNativePics(picsLimit-showNativePics.photoPaths.size()+1);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==100){
			if(data==null || data.getStringArrayListExtra("photos")==null)
				return;
			//照片数据
			ArrayList<String> photoPaths = data.getStringArrayListExtra("photos");
			showNativePics.resetPhotos(photoPaths);
			
		}
	}
	
	/**
	 * 获取本地图片，拍照图片
	 * @param picsNumLimit 图片数量限制  无限制传-1
	 */
	private void getNativePics(int picsNumLimit){
		startActivityForResult(new Intent(this, PickNativePics.class).putExtra("picsLimit", picsNumLimit), 100);
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.bt_title_right){
			
			CharSequence price = findTextView(R.id.edit_address_name).getText();
			if(TextUtils.isEmpty(price)){
				showToast("请输入成交价格");
				return;
			}
			
			CharSequence room = findTextView(R.id.edit_address_detail_address).getText();
			if(TextUtils.isEmpty(room)){
				showToast("请输入成交房间号");
				return;
			}
			
			ArrayList<String> photoPaths = new ArrayList<>();
			photoPaths.addAll(showNativePics.photoPaths);
			
			if(photoPaths.size()<=1){
				showToast("请上传证明图片");
				return;
			}
			
			if(photoPaths.get(photoPaths.size()-1).equals("add_photo"))
				photoPaths.remove(photoPaths.size()-1);
			
			showProgressBar(null);
			String[] imgs = new String[photoPaths.size()];
			for(int i=0;i<imgs.length;i++){
				imgs[i] = photoPaths.get(i);
			}
			new PhotoAsyncTask().execute(imgs);
		}
	}
	
	private final int REQUEST_COLLECT = 2; 
	/**
	 * 收藏商品
	 */
	private void requestCollect(List<String> list){
		
		CharSequence price = findTextView(R.id.edit_address_name).getText();
		if(TextUtils.isEmpty(price)){
			showToast("请输入成交价格");
			return;
		}
		
		CharSequence room = findTextView(R.id.edit_address_detail_address).getText();
		if(TextUtils.isEmpty(room)){
			showToast("请输入成交房间号");
			return;
		}
		
		showProgressBar(null);
		
		JsonElementRequest<DefaultMamahaoServerError> req = new JsonElementRequest<DefaultMamahaoServerError>(
				context,"",this);
		
		req.setParam("apiCode", "_customer_done");
		req.setParam("customerId", getIntentExtra("id"));
		req.setParam("userId", MyApplication.getInstance().getMemberId());
		req.setParam("room", room.toString().trim());
		req.setParam("price", price.toString().trim());
		for(int i=0;i<list.size();i++){
			req.setParam("images_"+(i+1), getImage(new File(list.get(i))));
		}

		
		//获取token
		String token = NetworkManager.getInstance().getPostToken(req.getParam());
		req.setParam("token", token);
		
		addRequestQueue(req, REQUEST_COLLECT, new ReqTag.Builder().handleSimpleRes(true));
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
		case REQUEST_COLLECT:
			showToast("申请成功");
			finish();
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
	
	public class PhotoAsyncTask extends AsyncTask<String, Void, List<String>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Override this method to perform a computation on a background thread. The specified
         * parameters are the parameters passed to {@link #execute} by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected List<String> doInBackground(String... params) {
            try {

                List<String> results = new ArrayList<String>();
                if (params == null || params.length <= 0)
                    return results;
                for (String path : params) {
                    Bitmap bitmap = BitmapUtils.getImage(path);
                    String name = System.currentTimeMillis()
                            + path.substring(path.lastIndexOf("."));
                    String filePath = FileUtils.getImageFolder() + "/"
                            + name;
                    File myCaptureFile = new File(filePath);
                    BufferedOutputStream bos = new BufferedOutputStream(
                            new FileOutputStream(myCaptureFile));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bos.flush();
                    bos.close();
                    bos = null;

                    results.add(filePath);
                }

                return results;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
					public void run() {
						showToast("执行失败");
					}
				});
                
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
					public void run() {
						showToast("执行失败");
					}
				});
            }
            return null;
        }

        @Override
        protected void onPostExecute(final List<String> list) {
            super.onPostExecute(list);
            requestCollect(list);
        }
    }

}
