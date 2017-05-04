package cn.softbank.purchase.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import cn.softbank.purchase.adapter.BaseViewHolder;
import cn.softbank.purchase.adapter.CommonAdapter;
import cn.yicheng.jingjiren.R;

public class ShowNativePics {

	// 初始化添加图片
	public ArrayList<String> photoPaths;
	private GoodsPhotoGVAdapter mGVadapter;
	private Context context;
	private GridView photo_gridview;
	private int picsLimit;
	public int lastPostion;

	public ShowNativePics(Context context, GridView photo_gridview,
			int picsLimit) {
		super();
		this.context = context;
		this.photo_gridview = photo_gridview;
		this.picsLimit = picsLimit;
		initPhotoData();
	}

	/**
	 * 图片数据初始化
	 */
	private void initPhotoData() {
		// 照片，照片路径
		photoPaths = new ArrayList<String>();
		// 添加“添加图片”
		photoPaths.add("add_photo");
		lastPostion = 0;

		// gridview设置
		mGVadapter = new GoodsPhotoGVAdapter(context,photoPaths,R.layout.photo_gridview_item);
		photo_gridview.setAdapter(mGVadapter);

	}

	/**
	 * 刷新图片显示
	 * 
	 * @param selPhotos
	 */
	public void resetPhotos(ArrayList<String> selPhotos) {

		if (selPhotos.size() > 0) {
            photoPaths.remove("add_photo");
			// photos.add(Tools.lessenUriImage(context,path));
			for (String path : selPhotos) {
				if (!photoPaths.contains(path))
					photoPaths.add(path);
			}
			// 增加照片
			if (picsLimit > photoPaths.size()) {
				photoPaths.add("add_photo");
				lastPostion = photoPaths.size() - 1;
			} else
				lastPostion = -1;

			mGVadapter.notifyDataSetChanged();
		}

	}

	private class GoodsPhotoGVAdapter extends CommonAdapter<String> {

		public GoodsPhotoGVAdapter(Context context, List<String> mDatas,
				int itemLayoutId) {
			super(context, mDatas, itemLayoutId);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void convert(BaseViewHolder holder, String itemData,
				final int position, ViewGroup parent) {
			// TODO Auto-generated method stub
			ImageView iv_del=holder.getView(R.id.photo_gridview_item_del);
			if (position == lastPostion) {
				// 最后一个，添加图片按钮
				holder.setImageByResource(R.id.photo_gridview_item_img, R.drawable.img_add_photo,null);
				iv_del.setVisibility(View.GONE);
			} else{
				holder.setImageBySDcard(R.id.photo_gridview_item_img, itemData,null);
				iv_del.setVisibility(View.VISIBLE);
			}
			iv_del.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					photoPaths.remove(position);
					// 之前已到达最多图片数量值，此时则增加默认图片
					if (lastPostion == -1)
						photoPaths.add("add_photo");

					lastPostion = photoPaths.size() - 1;

					GoodsPhotoGVAdapter.this.notifyDataSetChanged();
				}
			});

		}
	}

}
