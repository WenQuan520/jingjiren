package cn.softbank.purchase.activivty;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import cn.softbank.purchase.adapter.ViewPagerAdapter;
import cn.softbank.purchase.base.BaseActivity;
import cn.softbank.purchase.dialog.HuxingDialog;
import cn.softbank.purchase.dialog.HuxingDialog.HuxingDialogClickListener;
import cn.softbank.purchase.domain.AssetList;
import cn.softbank.purchase.domain.HouseAsset;
import cn.softbank.purchase.utils.Constant;
import cn.softbank.purchase.utils.ImageUtils;
import cn.yicheng.jingjiren.R;

public class HuxingPicsActivity extends BaseActivity {

	private HouseAsset houseAsset;
	private ViewPager view_pager;
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_huxing_pics);
		houseAsset = (HouseAsset) getIntent().getSerializableExtra("houseAsset");
		initTitleBar("楼盘相册", Constant.DEFAULT_LEFT_BACK, "1/"+houseAsset.getAssetList().size());
		
		view_pager = (ViewPager) findViewById(R.id.view_pager);
		
		findViewById(R.id.tv_shinei).setOnClickListener(this);
		findViewById(R.id.tv_shiwai).setOnClickListener(this);
		findViewById(R.id.tv_huxings).setOnClickListener(this);
		
		setCurrent(0);
		setImgs(0);
		
	}
	
	private int pos;
	private void setCurrent(int pos){
		this.pos = pos;
		AssetList assetList = houseAsset.getAssetList().get(pos);
		bt_title_right.setText((pos+1)+"/"+houseAsset.getAssetList().size());
		setText(R.id.tv_huxing, assetList.getName());
		setText(R.id.tv_area, assetList.getArea());
	}
	
	private void setImgs(int location){
		//图片viewpager
		AssetList assetList = houseAsset.getAssetList().get(pos);
		List<String> imgs = new ArrayList<>();
		if(location == 0)
			imgs.addAll(assetList.getPic().getShinei());
		else if(location == 1)
			imgs.addAll(assetList.getPic().getShiwai());
		else if(location == 2)
			imgs.addAll(assetList.getPic().getHuxing());
		
		ArrayList<View> views=new ArrayList<View>();
		for(final String url:imgs){
			ImageView iv=new ImageView(context);
			iv.setScaleType(ScaleType.FIT_XY);
			iv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			showImage(url, iv, ImageUtils.imgOptionsBig);

			views.add(iv);
		}
		view_pager.setAdapter(new ViewPagerAdapter(views));
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
	}
	
	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_shinei:
			setImgs(0);
			break;
		case R.id.tv_shiwai:
			setImgs(1);
			break;
		case R.id.tv_huxings:
			setImgs(2);
			break;
		case R.id.bt_title_right:
			new HuxingDialog(context, new HuxingDialogClickListener() {
				
				@Override
				public void onYesOrNoDialogClick(int pos) {
					// TODO Auto-generated method stub
					setCurrent(pos);
					setImgs(0);
				}
			}, houseAsset).show();
			break;
		
		default:
			break;
		}
	}

}
