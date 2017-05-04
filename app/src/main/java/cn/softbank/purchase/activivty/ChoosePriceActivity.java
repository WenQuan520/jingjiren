package cn.softbank.purchase.activivty;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.softbank.purchase.adapter.BaseViewHolder;
import cn.softbank.purchase.adapter.CommonAdapter;
import cn.softbank.purchase.base.BaseActivity;
import cn.softbank.purchase.base.MyApplication;
import cn.softbank.purchase.domain.SortItem;
import cn.softbank.purchase.utils.Constant;
import cn.yicheng.jingjiren.R;

public class ChoosePriceActivity extends BaseActivity {

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_choose_price);
		initTitleBar(getIntentExtra("chooseAcreage", false)?"选择面积":"选择价格", Constant.DEFAULT_LEFT_BACK, null);
		
		ListView listview = (ListView) findViewById(R.id.listview);
		listview.setAdapter(new CommonAdapter<SortItem>(context, MyApplication.getInstance().getSortTotalDatas().get(getIntentExtra("chooseAcreage", false)?2:3), R.layout.item_choose_price) {

			@Override
			public void convert(BaseViewHolder holder, SortItem itemData,
					int position, ViewGroup parent) {
				// TODO Auto-generated method stub
				holder.setText(R.id.tv_title, itemData.getTitle());
			}
		});
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				setResult(RESULT_OK, getIntent().putExtra("name", MyApplication.getInstance().getSortTotalDatas().get(3).get(position).getTitle()));
				finish();
			}
		});
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub

	}

}
