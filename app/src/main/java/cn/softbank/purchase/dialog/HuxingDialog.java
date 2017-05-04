package cn.softbank.purchase.dialog;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.softbank.purchase.adapter.BaseViewHolder;
import cn.softbank.purchase.adapter.CommonAdapter;
import cn.softbank.purchase.domain.AssetList;
import cn.softbank.purchase.domain.HouseAsset;
import cn.yicheng.jingjiren.R;
public class HuxingDialog extends Dialog implements OnClickListener {
    private HuxingDialogClickListener listener;
    private Context context;
    public HuxingDialog(Context context,
    		HuxingDialogClickListener listener,HouseAsset houseAsset) {
        super(context, R.style.BMF_Dialog);
        this.listener = listener;
        this.context = context;
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.dialog_huxing);
        initViews(houseAsset);

    }

    /**
     * 
     */
    private void initViews(HouseAsset houseAsset) {
      //户型列表
		ListView listview_huxing = (ListView) findViewById(R.id.listview_huxing);
			
		listview_huxing.setAdapter(new CommonAdapter<AssetList>(context, houseAsset.getAssetList(), R.layout.item_house) {

			@Override
			public void convert(BaseViewHolder holder, AssetList itemData,
					int position, ViewGroup parent) {
				// TODO Auto-generated method stub
				holder.setText(R.id.tv_title, itemData.getName());
				holder.setText(R.id.tv_area, itemData.getArea()+"㎡");
				if(itemData.getPic()!=null && itemData.getPic().getHuxing()!=null && itemData.getPic().getHuxing().size()>0)
					holder.setImageByUrl(R.id.iv_img, itemData.getPic().getHuxing().get(0));
			}
		});
		
		listview_huxing.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				listener.onYesOrNoDialogClick(position);
				dismiss();
			}
		});
    }

    @Override
    public void onClick(View v) {


    }

    public interface HuxingDialogClickListener {
        void onYesOrNoDialogClick(int pos);
    }
}
