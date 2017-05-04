package cn.softbank.purchase.activivty;

import com.google.gson.JsonElement;

import android.view.View;
import cn.softbank.purchase.base.BaseActivity;
import cn.softbank.purchase.base.MyApplication;
import cn.softbank.purchase.network.JsonElementRequest;
import cn.softbank.purchase.network.NetworkManager;
import cn.softbank.purchase.network.ReqTag;
import cn.softbank.purchase.network.AbstractRequest.MamaHaoError;
import cn.softbank.purchase.network.entity.DefaultMamahaoServerError;
import cn.softbank.purchase.network.entity.MamaHaoServerError;
import cn.softbank.purchase.utils.Constant;
import cn.yicheng.jingjiren.R;

public class AboutUsActivity extends BaseActivity {

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_about_us);
		initTitleBar(getString(R.string.about_us), Constant.DEFAULT_LEFT_BACK, null);
		
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		requestData();
	}
	
	/** 请求标识 **/
	private final int REQUEST_DATAS = 0;
	/**
	 * 删除地址
	 */
	private void requestData(){
		showProgressBar(null);
		
		
		JsonElementRequest<DefaultMamahaoServerError> req = new JsonElementRequest<DefaultMamahaoServerError>(
				context,"",this);
		
		req.setParam("apiCode", "_help_002");
		req.setParam("userToken", MyApplication.getInstance().getUserToken());
		
		//获取token
		String token = NetworkManager.getInstance().getPostToken(req.getParam());
		req.setParam("token", token);
	
		addRequestQueue(req, REQUEST_DATAS, new ReqTag.Builder().handleSimpleRes(true));
	}
	
	@Override
	protected void onResponseSuccess(ReqTag tag, Object data) {
		// TODO Auto-generated method stub
		super.onResponseSuccess(tag, data);
		switch (tag.getReqId()) {
		case REQUEST_DATAS:
			// 内容
			findTextView(R.id.tv_content).setText(((JsonElement)data).getAsString());
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
	}

	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub

	}

}
