package cn.softbank.purchase.activivty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import cn.softbank.purchase.adapter.BaseViewHolder;
import cn.softbank.purchase.adapter.CommonAdapter;
import cn.softbank.purchase.base.BaseActivity;
import cn.softbank.purchase.base.MyApplication;
import cn.softbank.purchase.db.SPDAO;
import cn.softbank.purchase.network.BeanRequest;
import cn.softbank.purchase.network.NetworkManager;
import cn.softbank.purchase.network.ReqTag;
import cn.softbank.purchase.network.entity.MamaHaoServerError;
import cn.softbank.purchase.utils.SharedPreference;
import cn.softbank.purchase.widget.HotWordView;
import cn.softbank.purchase.widget.HotWordView.HotWordChangeListener;
import cn.yicheng.jingjiren.R;

public class SearchActivity extends BaseActivity {
	
	private HotWordView hot_word_view;
	private EditText et_search;
	private List<String> searchHisWords;
	private CommonAdapter<String> searchAdapter;
	private CommonAdapter<String> lianxiangAdapter;
	private View view_other;
	private List<String> lianxiangWords = new ArrayList<>();
	private ListView listview_lianxiang;
	
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.search_activity);
		et_search = (EditText) findViewById(R.id.et_search);
		final TextView bt_title_right = findTextView(R.id.bt_title_right);
		view_other = findViewById(R.id.view_other);
		bt_title_right.setOnClickListener(this);
		findViewById(R.id.tv_clear).setOnClickListener(this);
		findViewById(R.id.bt_title_left).setOnClickListener(this);
		
		listview_lianxiang = (ListView) findViewById(R.id.listview_lianxiang);
		lianxiangAdapter = new CommonAdapter<String>(context, lianxiangWords, R.layout.search_history_item) {

			@Override
			public void convert(BaseViewHolder holder, String itemData,
					int position, ViewGroup parent) {
				// TODO Auto-generated method stub
				holder.setText(R.id.tv_title, itemData);
			}
		};
		
		
		listview_lianxiang.setAdapter(lianxiangAdapter);
		listview_lianxiang.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				search(lianxiangWords.get(position));
			}
		});
		
		et_search.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(!TextUtils.isEmpty(et_search.getText().toString().trim())){
					view_other.setVisibility(View.GONE);
					bt_title_right.setVisibility(View.VISIBLE);
					listview_lianxiang.setVisibility(View.VISIBLE);
					setLianxiangdata(et_search.getText().toString().trim());
				}
				else{
					view_other.setVisibility(View.VISIBLE);
					bt_title_right.setVisibility(View.GONE);
					listview_lianxiang.setVisibility(View.GONE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//热词
		hot_word_view = (HotWordView) findViewById(R.id.hot_word_view);
		searchHisWords = new ArrayList<String>();
		//搜索历史
		String words=SharedPreference.getString(context, SPDAO.SEARCH_HISTORY);
		//最多存20条
		if(!TextUtils.isEmpty(words)){
			try {
				Collections.addAll(searchHisWords, words.split(","));
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		ListView listView = (ListView) findViewById(R.id.listview);
		if(searchHisWords.size()==0)
			findViewById(R.id.divider).setVisibility(View.INVISIBLE);
		else
			findViewById(R.id.divider).setVisibility(View.VISIBLE);
		searchAdapter = new CommonAdapter<String>(context, searchHisWords, R.layout.search_history_item) {

			@Override
			public void convert(BaseViewHolder holder, String itemData,
					int position, ViewGroup parent) {
				// TODO Auto-generated method stub
				holder.setText(R.id.tv_title, itemData);
			}
		};
		listView.setAdapter(searchAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				search(searchHisWords.get(position));
			}
		});
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		//热词
		requestDatas();
	}
	
	private final int REQUEST_DATAS = 0;
	
	/**
	 * 请求热词数据
	 */
	private void requestDatas(){
		showProgressBar(null);
		BeanRequest<HotDatas, MamaHaoServerError> req = new BeanRequest<>(
				context, 
				"", 
				this, 
				HotDatas.class);
		
		req.setParam("apiCode", "_building_search_key");
		req.setParam("city", MyApplication.getInstance().getCity());
		
		//获取token
		String token = NetworkManager.getInstance().getPostToken(req.getParam());
		req.setParam("token", token);
		
		addRequestQueue(req, REQUEST_DATAS);
	}
	
	@Override
	protected void onResponseSuccess(ReqTag tag, Object data) {
		// TODO Auto-generated method stub
		super.onResponseSuccess(tag, data);
		switch (tag.getReqId()) {
		case REQUEST_DATAS:
			// 商品列表
			if((HotDatas)data!=null && ((HotDatas)data).getHot()!=null && ((HotDatas)data).getHot().size()>0)
				setHotWords(((HotDatas)data).getHot());
			break;
		default:
			break;
		}
	}

	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_title_left:
			finish();
			break;
		case R.id.bt_title_right:
			//搜索完成; 
			String key = et_search.getText().toString().trim();
			search(key);
			break;
		case R.id.tv_clear:
			findViewById(R.id.divider).setVisibility(View.INVISIBLE);
			SPDAO.getInstance().clear(context, SPDAO.SEARCH_HISTORY);
			searchHisWords.clear();
			searchAdapter.notifyDataSetChanged();
			break;

		default:
			break;
		}
	}
	
	/**
	 * 设置热词
	 * @param words
	 */
	private void setHotWords(List<String> words){
		//默认第一条是综合
		//设置热词数据
		hot_word_view.setDatas(words, new HotWordChangeListener() {
			
			@Override
			public void onWordChange(String hotword) {
				//热词选项改变
				search(hotword);
			}
		});
	}
	
	/**
	 * 搜索
	 * @param key
	 */
	private void search(String key){
		//排重
		if(SPDAO.getInstance().judgeExists(context, SPDAO.SEARCH_HISTORY, key))
			SPDAO.getInstance().delete(context, SPDAO.SEARCH_HISTORY, key);
		
		String words=SharedPreference.getString(context, SPDAO.SEARCH_HISTORY);
		//最多存20条
		if(!TextUtils.isEmpty(words)){
			String[] ss=words.split(",");
			if(ss.length>5)
				SPDAO.getInstance().delete(context, SPDAO.SEARCH_HISTORY, ss[ss.length-1]);
		}
		//插入最新一条
		SPDAO.getInstance().insertIndex(context, SPDAO.SEARCH_HISTORY, key);
		startActivity(new Intent(context,ChooseLoupanActivity.class).putExtra("key", key));
	}
	
	private void setLianxiangdata(String word){
		lianxiangWords.clear();
		for(String searchWord:searchHisWords){
			if(searchWord.contains(word))
				lianxiangWords.add(searchWord);
		}
		lianxiangAdapter.notifyDataSetChanged();
	}
	
	public class HotDatas{
		private List<String> hot;

		public List<String> getHot() {
			return hot;
		}

		public void setHot(List<String> hot) {
			this.hot = hot;
		}
		
	}

}
