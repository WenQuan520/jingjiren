package cn.softbank.purchase.dialog;

import java.util.List;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.softbank.purchase.adapter.BaseViewHolder;
import cn.softbank.purchase.adapter.CommonAdapter;
import cn.softbank.purchase.domain.ImageFloder;
import cn.yicheng.jingjiren.R;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class ListImageDirPopupWindow extends BasePopupWindowForListView<ImageFloder>
{
	private ListView mListDir;
	
	public ListImageDirPopupWindow(int width, int height,
			List<ImageFloder> datas, View convertView)
	{
		super(convertView, width, height, true, datas);
	}

	@Override
	public void initViews()
	{
		mListDir = (ListView) findViewById(R.id.pick_native_pics_list_dir);
		mListDir.setAdapter(new CommonAdapter<ImageFloder>(context, mDatas,
				R.layout.pick_native_pics_list_dir_item)
		{

			@Override
			public void convert(BaseViewHolder holder, ImageFloder itemData,
					int position, ViewGroup parent) {
				holder.setText(R.id.pick_native_pics_dir_item_name, itemData.getName());
				DisplayImageOptions imgOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.default_img_small)
				.showImageForEmptyUri(R.drawable.default_img_small)
				.showImageOnFail(R.drawable.default_img_small)
				.cacheInMemory(false)
				.cacheOnDisk(false)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
				
				holder.setImageBySDcard(R.id.pick_native_pics_dir_item_image,itemData.getFirstImagePath(), imgOptions);
				holder.setText(R.id.pick_native_pics_dir_item_count, itemData.getCount() + "张");
			}
		});
	}

	public interface OnImageDirSelected
	{
		void selected(ImageFloder floder);
	}

	private OnImageDirSelected mImageDirSelected;

	public void setOnImageDirSelected(OnImageDirSelected mImageDirSelected)
	{
		this.mImageDirSelected = mImageDirSelected;
	}

	@Override
	public void initEvents()
	{
		mListDir.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{

				if (mImageDirSelected != null)
				{
					mImageDirSelected.selected(mDatas.get(position));
				}
			}
		});
	}

	@Override
	public void init()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void beforeInitWeNeedSomeParams(Object... params)
	{
		// TODO Auto-generated method stub
	}

}
