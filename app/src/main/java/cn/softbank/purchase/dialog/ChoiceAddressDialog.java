package cn.softbank.purchase.dialog;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.softbank.purchase.adapter.BaseViewHolder;
import cn.softbank.purchase.adapter.CommonAdapter;
import cn.softbank.purchase.db.DBhelper;
import cn.softbank.purchase.domain.Area;
import cn.softbank.purchase.utils.CommonUtils;
import cn.yicheng.jingjiren.R;

public class ChoiceAddressDialog extends Dialog{

	private LinearLayout lil_chooise;
	private LinearLayout lil_eorror;
	private TextView tv_eorror;
	private ListView lv_address;

	private List<Area> mDatas;
	private List<Area> provinceList;
	private AddressAdapter mAdapter;

	private int mStatus = 1;
	private String choiseIdP;

	private ProgressBar probar;

	private String proviceName;
	private String cityName;
	
	private DBhelper dBhelper;

	public ChoiceAddressDialog(Context context, int theme) {
		super(context, theme);
		Window window = getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.main_menu_animstyle);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = CommonUtils.getScreenSize(context)[1];
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		dBhelper = new DBhelper(context);
		provinceList = dBhelper.getProvince();

		// 设置显示位置
		onWindowAttributesChanged(wl);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_choiseaddress);
		initViews();
	}

	private void initViews() {
		lil_chooise = (LinearLayout) findViewById(R.id.lil_chooise);
		lil_eorror = (LinearLayout) findViewById(R.id.lil_eorror);
		lv_address = (ListView) findViewById(R.id.lv_address);
		tv_eorror = (TextView) findViewById(R.id.tv_eorror);
		probar = (ProgressBar) findViewById(R.id.probar);
		
		findViewById(R.id.btn_error).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// 默认 查询省
						queryArea(Status.PROVICE, "");
					}
				});

		findViewById(R.id.iv_close).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dismiss();
					}
				});

		for (int i = 0; i < lil_chooise.getChildCount(); i++) {
			TextView tv = (TextView) lil_chooise.getChildAt(i);
			tv.setTag(i + 1);
			tv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if ((Integer) v.getTag() < mStatus) {
						if ((Integer) v.getTag() == 1) {
							queryArea(Status.PROVICE, "");
						} else {
							queryArea(Status.CITY, choiseIdP);
						}
					}
				}
			});
		}

		changeTabViewStatus();

		mDatas = new ArrayList<Area>();

		mAdapter = new AddressAdapter(getContext(), mDatas,
				R.layout.layout_choise_item);
		lv_address.setAdapter(mAdapter);

		lv_address
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						switch (mStatus) {
						case Status.PROVICE:
							choiseIdP = mAdapter.getItem(position).getCode();
							proviceName = mAdapter.getItem(position).getName();
							queryArea(Status.CITY,
									mAdapter.getItem(position).getCode());
							break;

						case Status.CITY:
							cityName = mAdapter.getItem(position).getName();
							queryArea(Status.AREA,
									mAdapter.getItem(position).getCode());
							break;

						case Status.AREA:
							if (mOnForResultCallBack != null)
								mOnForResultCallBack.onCallBack(true, proviceName,cityName,mAdapter.getItem(position).getName());
							dismiss();
							break;

						default:
							break;
						}
					}
				});

	}

	public void setTabText(String content, int status) {
		if (TextUtils.isEmpty(content)) {
			return;
		}

		TextView tv = (TextView) lil_chooise.getChildAt(status - 1);
		tv.setText(content);
	}

	private void changeTabViewStatus() {
		if (lil_chooise == null)
			return;

		for (int i = 0; i < lil_chooise.getChildCount(); i++) {
			View tv = lil_chooise.getChildAt(i);
			int position = (Integer) tv.getTag();
			tv.setSelected(position == mStatus);
		}
	}

	@Override
	public void show() {
		super.show();
		// 默认 查询省
		queryArea(Status.PROVICE, "");
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
	}

	private class AddressAdapter extends CommonAdapter<Area> {

		public AddressAdapter(Context context, List<Area> mDatas,
				int itemLayoutId) {
			super(context, mDatas, itemLayoutId);
		}

		@Override
		public void convert(BaseViewHolder holder, Area itemData, int position,
				ViewGroup parent) {
			holder.getView(R.id.tv_name_left).setVisibility(View.INVISIBLE);
			holder.getView(R.id.tv_name_center).setVisibility(View.INVISIBLE);
			holder.getView(R.id.tv_name_right).setVisibility(View.INVISIBLE);

			switch (mStatus) {
			case Status.PROVICE:
				holder.getView(R.id.tv_name_left).setVisibility(View.VISIBLE);
				if (!TextUtils.isEmpty(itemData.getName()))
					holder.setText(R.id.tv_name_left, itemData.getName());
				break;

			case Status.CITY:
				holder.getView(R.id.tv_name_center).setVisibility(View.VISIBLE);
				if (!TextUtils.isEmpty(itemData.getName()))
					holder.setText(R.id.tv_name_center, itemData.getName());
				break;

			case Status.AREA:
				holder.getView(R.id.tv_name_right).setVisibility(View.VISIBLE);
				if (!TextUtils.isEmpty(itemData.getName()))
					holder.setText(R.id.tv_name_right, itemData.getName());
				break;

			default:
				break;
			}
		}

	}

	private void queryArea(int type, String id) {
		
		probar.setVisibility(View.GONE);
		lil_eorror.setVisibility(View.GONE);
			mStatus = type;
			mDatas.clear();
			changeTabViewStatus();

			switch (mStatus) {
			case Status.PROVICE:
				setTabText("选择省", Status.PROVICE);
				setTabText("选择市", Status.CITY);
				setTabText("选择区", Status.AREA);
				mDatas.addAll(provinceList);
				break;

			case Status.CITY:
				setTabText(proviceName, Status.PROVICE);
				setTabText("选择市", Status.CITY);
				setTabText("选择区", Status.AREA);
				mDatas.addAll(dBhelper.getCity(id));
				break;

			case Status.AREA:
				setTabText(proviceName, Status.PROVICE);
				setTabText(cityName, Status.CITY);
				setTabText("选择区", Status.AREA);
				mDatas.addAll(dBhelper.getDistrict(id));
				break;

			default:
				break;
			}

			
			mAdapter.notifyDataSetChanged();
			lv_address.setSelection(0);
	}

	private void setQueryFail(){
		probar.setVisibility(View.GONE);
		tv_eorror.setText(getContext().getString(R.string.error_server));
	}

	public static class Status {
		public final static int PROVICE = 1;// 省
		public final static int CITY = 2;// 城市
		public final static int AREA = 3;// 区域
	}

	private OnForResultCallBack mOnForResultCallBack;

	public void setOnForResultCallBack(OnForResultCallBack mOnForResultCallBack) {
		this.mOnForResultCallBack = mOnForResultCallBack;
	}

	public interface OnForResultCallBack {
		public void onCallBack(boolean isSucced, String province, String city, String area);
	}

	public void showToast(String msg) {
		Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
	}
}
