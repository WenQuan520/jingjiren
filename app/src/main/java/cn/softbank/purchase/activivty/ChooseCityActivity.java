package cn.softbank.purchase.activivty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import cn.softbank.purchase.adapter.BaseViewHolder;
import cn.softbank.purchase.adapter.CityAdapter;
import cn.softbank.purchase.adapter.CommonAdapter;
import cn.softbank.purchase.base.BaseActivity;
import cn.softbank.purchase.client.PurchaseApi;
import cn.softbank.purchase.domain.CityData;
import cn.softbank.purchase.domain.CityItem;
import cn.softbank.purchase.network.BeanRequest;
import cn.softbank.purchase.network.NetworkManager;
import cn.softbank.purchase.network.ReqTag;
import cn.softbank.purchase.network.AbstractRequest.MamaHaoError;
import cn.softbank.purchase.network.entity.MamaHaoServerError;
import cn.softbank.purchase.utils.Constant;
import cn.softbank.purchase.widget.ContactItemInterface;
import cn.softbank.purchase.widget.ContactListViewImpl;
import cn.yicheng.jingjiren.R;


public class ChooseCityActivity extends BaseActivity// implements TextWatcher
{
	private Context context_ = ChooseCityActivity.this;

	private ContactListViewImpl listview;

//	private EditText searchBox;
//	private String searchString;

	private Object searchLock = new Object();
	boolean inSearchMode = false;

	private final static String TAG = "MainActivity2";

	List<ContactItemInterface> contactList;
	List<ContactItemInterface> filterList;
	private SearchListTask curSearchTask = null;

	private List<String> hotCitys;

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.citylist);
		initTitleBar(getString(R.string.choose_city), Constant.DEFAULT_LEFT_BACK, null);
		
//		searchBox = (EditText) findViewById(R.id.input_search_query);
//		searchBox.addTextChangedListener(this);
	}
	
	/** 请求标识 **/
	private final int REQUEST_GOODS_DATAS = 0;
	private void requestGoodsDatas() {
		// TODO Auto-generated method stub
		BeanRequest<Citys, MamaHaoServerError> req = new BeanRequest<>(
				context, 
				"", 
				this, 
				Citys.class);
		
		req.setParam("apiCode", "_building_city");
		
//		req.setParam("name", String.valueOf(pageSize));
		
		//获取token
		String token = NetworkManager.getInstance().getPostToken(req.getParam());
		req.setParam("token", token);
		
		addRequestQueue(req, REQUEST_GOODS_DATAS);
	
	}
	
	@Override
	protected void onResponseSuccess(ReqTag tag, Object data) {
		// TODO Auto-generated method stub
		super.onResponseSuccess(tag, data);
		switch (tag.getReqId()) {
		case REQUEST_GOODS_DATAS:
			//普通商品列表
//			handleDatas((List<HomeGoodsDatas>)data);
			init(((Citys)data).getCitys());
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
		
		init(null);
	}
	
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		requestGoodsDatas();
	}
	
	private void init(List<String> data){
		filterList = new ArrayList<ContactItemInterface>();
		if(data!=null && data.size()>0)
			contactList = CityData.getSampleContactList(data);
		else
			contactList = CityData.getSampleContactList();

		CityAdapter adapter = new CityAdapter(this,R.layout.city_item, contactList);

		listview = (ContactListViewImpl) this.findViewById(R.id.listview);
		listview.setFastScrollEnabled(true);
		
		View headerView = View.inflate(context_, R.layout.city_header_view, null);
		hotCitys = new ArrayList<>();
		Collections.addAll(hotCitys, getResources().getStringArray(R.array.hot_citys));
		
		GridView gridview_hot_city = (GridView) headerView.findViewById(R.id.gridview_hot_city);
		gridview_hot_city.setAdapter(new CommonAdapter<String>(context_, hotCitys, R.layout.hot_city_item) {

			@Override
			public void convert(BaseViewHolder holder, String itemData,
					int position, ViewGroup parent) {
				// TODO Auto-generated method stub
				holder.setText(R.id.tv_title, itemData);
			}
		});
		gridview_hot_city.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView parent, View v, int position,
					long id)
			{
				setResult(RESULT_OK, getIntent().putExtra("city", hotCitys.get(position)));
				finish();
			}
		});
		
		//定位城市
		TextView tv=(TextView) headerView.findViewById(R.id.tv_search_locationcity);
		tv.setText(String.format(getString(R.string.location_city), PurchaseApi.getInstance().getDeliveryAddr().getCity()));
		tv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setResult(RESULT_OK, getIntent().putExtra("city", PurchaseApi.getInstance().getDeliveryAddr().getCity()));
				finish();
			}
		});
		
		listview.addHeaderView(headerView, null, true);
		
		
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView parent, View v, int position,
					long id)
			{
				List<ContactItemInterface> searchList = inSearchMode ? filterList
						: contactList;

				setResult(RESULT_OK, getIntent().putExtra("city", searchList.get(position-1).getDisplayInfo()));
				finish();
			}
		});
	}
	
	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public void afterTextChanged(Editable s)
//	{
//		searchString = searchBox.getText().toString().trim().toUpperCase();
//
//		if (curSearchTask != null
//				&& curSearchTask.getStatus() != AsyncTask.Status.FINISHED)
//		{
//			try
//			{
//				curSearchTask.cancel(true);
//			} catch (Exception e)
//			{
//				Log.i(TAG, "Fail to cancel running search task");
//			}
//
//		}
//		curSearchTask = new SearchListTask();
//		curSearchTask.execute(searchString); 
//	}

//	@Override
//	public void beforeTextChanged(CharSequence s, int start, int count,
//			int after)
//	{
//	}
//
//	@Override
//	public void onTextChanged(CharSequence s, int start, int before, int count)
//	{
//		// do nothing
//	}

	private class SearchListTask extends AsyncTask<String, Void, String>
	{

		@Override
		protected String doInBackground(String... params)
		{
			filterList.clear();

			String keyword = params[0];

			inSearchMode = (keyword.length() > 0);

			if (inSearchMode)
			{
				// get all the items matching this
				for (ContactItemInterface item : contactList)
				{
					CityItem contact = (CityItem) item;

					boolean isPinyin = contact.getFullName().toUpperCase().indexOf(keyword) > -1;
					boolean isChinese = contact.getNickName().indexOf(keyword) > -1;

					if (isPinyin || isChinese)
					{
						filterList.add(item);
					}

				}

			}
			return null;
		}

		protected void onPostExecute(String result)
		{

			synchronized (searchLock)
			{

				if (inSearchMode)
				{

					CityAdapter adapter = new CityAdapter(context_,R.layout.city_item, filterList);
					adapter.setInSearchMode(true);
					listview.setInSearchMode(true);
					listview.setAdapter(adapter);
				} else
				{
					CityAdapter adapter = new CityAdapter(context_,R.layout.city_item, contactList);
					adapter.setInSearchMode(false);
					listview.setInSearchMode(false);
					listview.setAdapter(adapter);
				}
			}

		}
	}
	
	public class Citys{
		private List<String> city;

		public List<String> getCitys() {
			return city;
		}

		public void setCitys(List<String> citys) {
			this.city = citys;
		}
		
	}
}
