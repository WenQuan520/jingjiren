package cn.softbank.purchase.activivty;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import cn.softbank.purchase.adapter.BaseViewHolder;
import cn.softbank.purchase.adapter.CommonAdapter;
import cn.softbank.purchase.base.BaseActivity;
import cn.softbank.purchase.base.MyApplication;
import cn.softbank.purchase.network.BeanRequest;
import cn.softbank.purchase.network.NetworkManager;
import cn.softbank.purchase.network.ReqTag;
import cn.softbank.purchase.network.AbstractRequest.MamaHaoError;
import cn.softbank.purchase.network.entity.MamaHaoServerError;
import cn.softbank.purchase.utils.Constant;
import cn.yicheng.jingjiren.R;

public class ChooseAreaActivity extends BaseActivity {

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_choose_area);
		initTitleBar("选择区域", Constant.DEFAULT_LEFT_BACK, null);
		
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		requestDatas();
	}
	
	/** 请求标识 **/
    private final int REQUEST_GOODS_DATAS = 0;
	
	private void requestDatas() {
		// TODO Auto-generated method stub
		BeanRequest<Regions, MamaHaoServerError> req = new BeanRequest<>(
				context, 
				"", 
				this, 
				Regions.class);
		
		req.setParam("apiCode", "_customer_region");
		
		req.setParam("city", MyApplication.getInstance().getCity());
		
//		req.setParam("name", String.valueOf(pageSize));
		
		//获取token
		String token = NetworkManager.getInstance().getPostToken(req.getParam());
		req.setParam("token", token);
		
		addRequestQueue(req, REQUEST_GOODS_DATAS, new ReqTag.Builder().handleSimpleRes(true));
	
	}
	
	private List<Region> regions = new ArrayList<>();
	private CommonAdapter<Region> adapter;
	
	private List<String> contents = new ArrayList<>();
	private CommonAdapter<String> contentAdapter;
	
	private String area;
	private String town;
	@Override
	protected void onResponseSuccess(ReqTag tag, Object data) {
		// TODO Auto-generated method stub
		super.onResponseSuccess(tag, data);
		
		regions.add(new Region("不限", null, true));
		
		if(data!=null && ((Regions)data).getRegion()!=null && (((Regions)data).getRegion()).size()>0)
			regions.addAll(((Regions)data).getRegion());
		init();
		
		
	}
	
	@Override
	protected void onResponseFailure(ReqTag tag, MamaHaoServerError error,
			MamaHaoError clientError) {
		// TODO Auto-generated method stub
		super.onResponseFailure(tag, error, clientError);
		
		regions.add(new Region("不限", null, true));
		
		init();
	}
	
	private void init(){
		ListView listview_title = (ListView) findViewById(R.id.listview_title);
		adapter = new CommonAdapter<Region>(context, regions, R.layout.item_choose) {

			@Override
			public void convert(BaseViewHolder holder, Region itemData,
					int position, ViewGroup parent) {
				// TODO Auto-generated method stub
				TextView tv_title = holder.getView(R.id.tv_title);
				tv_title.setText(itemData.getName());
				if(itemData.isSel)
					tv_title.setBackgroundColor(Color.parseColor("#14c431"));
				else
					tv_title.setBackgroundColor(Color.parseColor("#ffffff"));
			}
		};
		listview_title.setAdapter(adapter);
		listview_title.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Region itemData = regions.get(position);
				if(itemData.getName().equals("不限")){
					area = "不限";
					town = "";
					backRes();
				}else{
					for(Region region:regions)
						region.setSel(false);
					
					itemData.setSel(true);
					
					area = itemData.getName();
					contents.clear();
					contents.add("不限");
					contents.addAll(itemData.getTown());
					adapter.notifyDataSetChanged();
					contentAdapter.notifyDataSetChanged();
				}
				
			}
		});
		
		contents.add("不限");
		ListView listview_content = (ListView) findViewById(R.id.listview_content);
		contentAdapter = new CommonAdapter<String>(context, contents, R.layout.item_choose) {

			@Override
			public void convert(BaseViewHolder holder, String itemData,
					int position, ViewGroup parent) {
				// TODO Auto-generated method stub
				holder.setText(R.id.tv_title, itemData);
			}
		};
		listview_content.setAdapter(contentAdapter);
		listview_content.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				town = contents.get(position);
				backRes();
			}
		});
	}
	
	private void backRes(){
		if(TextUtils.isEmpty(area)){
			area = "不限";
			town = "";
		}
		setResult(RESULT_OK, getIntent().putExtra("area", area)
				.putExtra("town", town));
		finish();
	}

	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub

	}
	
	public class Regions{
		private List<Region> region;

		public List<Region> getRegion() {
			return region;
		}

		public void setRegion(List<Region> region) {
			this.region = region;
		}

	}
	
	public class Region{
		private String name;
		private List<String> town;
		private boolean isSel;
		
		public Region(String name, List<String> town, boolean isSel) {
			super();
			this.name = name;
			this.town = town;
			this.isSel = isSel;
		}
		public Region() {
			super();
			// TODO Auto-generated constructor stub
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public List<String> getTown() {
			return town;
		}
		public void setTown(List<String> town) {
			this.town = town;
		}
		public boolean isSel() {
			return isSel;
		}
		public void setSel(boolean isSel) {
			this.isSel = isSel;
		}
		
	}

}
