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
import cn.softbank.purchase.network.BeanRequest;
import cn.softbank.purchase.network.NetworkManager;
import cn.softbank.purchase.network.ReqTag;
import cn.softbank.purchase.network.AbstractRequest.MamaHaoError;
import cn.softbank.purchase.network.entity.MamaHaoServerError;
import cn.softbank.purchase.utils.Constant;
import cn.yicheng.jingjiren.R;

public class ChooseFangxingActivity extends BaseActivity {

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_choose_area);
		initTitleBar("选择房型", Constant.DEFAULT_LEFT_BACK, null);
		
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
		BeanRequest<Buildings, MamaHaoServerError> req = new BeanRequest<>(
				context, 
				"", 
				this, 
				Buildings.class);
		
		req.setParam("apiCode", "_customer_house_type");
		
		//获取token
		String token = NetworkManager.getInstance().getPostToken(req.getParam());
		req.setParam("token", token);
		
		addRequestQueue(req, REQUEST_GOODS_DATAS, new ReqTag.Builder().handleSimpleRes(true));
	
	}
	
	private List<Building> buildings = new ArrayList<>();
	private CommonAdapter<Building> adapter;
	
	private List<String> contents = new ArrayList<>();
	private CommonAdapter<String> contentAdapter;
	
	private String buildingType;
	private String houseType;
	@Override
	protected void onResponseSuccess(ReqTag tag, Object data) {
		// TODO Auto-generated method stub
		super.onResponseSuccess(tag, data);
		
		buildings.add(new Building("不限", null, true));
		
		if(data!=null && ((Buildings)data).getBuildingType()!=null && (((Buildings)data).getBuildingType()).size()>0)
			buildings.addAll(((Buildings)data).getBuildingType());
		init();
		
		
	}
	
	@Override
	protected void onResponseFailure(ReqTag tag, MamaHaoServerError error,
			MamaHaoError clientError) {
		// TODO Auto-generated method stub
		super.onResponseFailure(tag, error, clientError);
		
		buildings.add(new Building("不限", null, true));
		
		init();
	}
	
	private void init(){
		ListView listview_title = (ListView) findViewById(R.id.listview_title);
		adapter = new CommonAdapter<Building>(context, buildings, R.layout.item_choose) {

			@Override
			public void convert(BaseViewHolder holder, Building itemData,
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
				Building itemData = buildings.get(position);
				if(itemData.getName().equals("不限")){
					buildingType = "不限";
					houseType = "";
					backRes();
				}else{
					for(Building building:buildings)
						building.setSel(false);
					
					itemData.setSel(true);
					
					buildingType = itemData.getName();
					contents.clear();
					contents.addAll(itemData.getSon());
					adapter.notifyDataSetChanged();
					contentAdapter.notifyDataSetChanged();
				}
				
			}
		});
		
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
				houseType = contents.get(position);
				backRes();
			}
		});
	}
	
	private void backRes(){
		if(TextUtils.isEmpty(buildingType)){
			buildingType = "不限";
			houseType = "";
		}
		setResult(RESULT_OK, getIntent().putExtra("buildingType", buildingType)
				.putExtra("houseType", houseType));
		finish();
	}

	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub

	}
	
	public class Buildings{
		private List<Building> buildingType;

		public List<Building> getBuildingType() {
			return buildingType;
		}

		public void setBuildingType(List<Building> buildingType) {
			this.buildingType = buildingType;
		}

	}
	
	public class Building{
		private String name;
		private List<String> son;
		private boolean isSel;
		
		public Building(String name, List<String> son, boolean isSel) {
			super();
			this.name = name;
			this.son = son;
			this.isSel = isSel;
		}
		public Building() {
			super();
			// TODO Auto-generated constructor stub
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
		public List<String> getSon() {
			return son;
		}
		public void setSon(List<String> son) {
			this.son = son;
		}
		public boolean isSel() {
			return isSel;
		}
		public void setSel(boolean isSel) {
			this.isSel = isSel;
		}
		
	}

}
