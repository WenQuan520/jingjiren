package cn.softbank.purchase.activivty;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import cn.softbank.purchase.base.BaseActivity;
import cn.softbank.purchase.network.AbstractRequest.MamaHaoError;
import cn.softbank.purchase.network.JsonElementRequest;
import cn.softbank.purchase.network.NetworkManager;
import cn.softbank.purchase.network.ReqTag;
import cn.softbank.purchase.network.entity.DefaultMamahaoServerError;
import cn.softbank.purchase.network.entity.MamaHaoServerError;
import cn.softbank.purchase.utils.Constant;
import cn.yicheng.jingjiren.R;

public class SuggestActivity extends BaseActivity {

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_suggest);
		initTitleBar(getString(R.string.suggest), Constant.DEFAULT_LEFT_BACK, null);
		
		findViewById(R.id.bt_ok).setOnClickListener(this);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_ok:
			String content = ((EditText)findViewById(R.id.edit_text)).getText().toString().trim();
			if(TextUtils.isEmpty(content))
				showToast(getString(R.string.suggest_null));
			else
				requestSuggest(content);
			break;

		default:
			break;
		}
	}
	
	/** 请求标识 **/
	private final int REQUEST_SUGGEST = 0;
	/**
	 * 提交意见建议
	 */
	private void requestSuggest(String content){
		showProgressBar(null);
		
		JsonElementRequest<DefaultMamahaoServerError> req = new JsonElementRequest<DefaultMamahaoServerError>(
				context,"",this);
		
		req.setParam("apiCode", "_user_suggestion");
		req.setParam("content", content);
		
		//获取token
		String token = NetworkManager.getInstance().getPostToken(req.getParam());
		req.setParam("token", token);
		
		addRequestQueue(req, REQUEST_SUGGEST);//, new ReqTag.Builder().handleSimpleRes(true));
	}
	
	@Override
	protected void onResponseSuccess(ReqTag tag, Object data) {
		// TODO Auto-generated method stub
		super.onResponseSuccess(tag, data);
		showToast(getString(R.string.sugget_ok));
		finish();
	}
	@Override
	protected void onResponseFailure(ReqTag tag, MamaHaoServerError error,
			MamaHaoError clientError) {
		// TODO Auto-generated method stub
		super.onResponseFailure(tag, error, clientError);
		showToast(getString(R.string.sugget_ok));
		finish();
	}

}
