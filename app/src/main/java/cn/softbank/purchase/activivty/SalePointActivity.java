package cn.softbank.purchase.activivty;


import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import cn.softbank.purchase.adapter.BaseViewHolder;
import cn.softbank.purchase.adapter.CommonAdapter;
import cn.softbank.purchase.base.BaseActivity;
import cn.softbank.purchase.domain.TitleDesc;
import cn.softbank.purchase.domain.TitleDescs;
import cn.softbank.purchase.utils.Constant;
import cn.yicheng.jingjiren.R;

public class SalePointActivity extends BaseActivity {

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_sale_point);
		initTitleBar(getIntentExtra("title"), Constant.DEFAULT_LEFT_BACK, null);
		
		TitleDescs datas = (TitleDescs) getIntent().getSerializableExtra("datas");
		datas.getTitleDescs().addAll(datas.getTitleDescs());
		//楼盘卖点
		if(datas!=null && datas.getTitleDescs()!=null && datas.getTitleDescs().size()>0){
			ListView listview_sales = (ListView) findViewById(R.id.listview_sales);
			listview_sales.setAdapter(new CommonAdapter<TitleDesc>(context, datas.getTitleDescs(), R.layout.item_param) {

				@Override
				public void convert(BaseViewHolder holder, TitleDesc itemData,
						int position, ViewGroup parent) {
					// TODO Auto-generated method stub
					holder.setText(R.id.tv_tag, itemData.getTitle()+"：");
					holder.setText(R.id.tv_value, itemData.getDesc());
				}
			});
		}
		
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
