package cn.softbank.purchase.activivty;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
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
import cn.softbank.purchase.dialog.YesOrNoDialog;
import cn.softbank.purchase.dialog.YesOrNoDialog.OnYesOrNoDialogClickListener;
import cn.softbank.purchase.dialog.YesOrNoDialog.YesOrNoType;
import cn.softbank.purchase.dialog.YesOrNoDialogEntity;
import cn.softbank.purchase.network.AbstractRequest.MamaHaoError;
import cn.softbank.purchase.network.BeanRequest;
import cn.softbank.purchase.network.JsonElementRequest;
import cn.softbank.purchase.network.NetworkManager;
import cn.softbank.purchase.network.ReqTag;
import cn.softbank.purchase.network.entity.DefaultMamahaoServerError;
import cn.softbank.purchase.network.entity.MamaHaoServerError;
import cn.softbank.purchase.utils.CommonUtils;
import cn.softbank.purchase.utils.Constant;
import cn.yicheng.jingjiren.R;

public class MessageSystemList extends BaseActivity {

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.message_activity);
		initTitleBar("消息中心", Constant.DEFAULT_LEFT_BACK, "清空");
	}

	private MyAdapter adapter;
	private List<MessageData> datas = new ArrayList<MessageData>();
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		ListView lv = (ListView) findViewById(R.id.listview);
		adapter = new MyAdapter();
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				MessageData itemData = datas.get(position);
				startActivity(new Intent(context, MessageActivityDetail.class)
				.putExtra("data", itemData.getDesc())
				);
				requestCollect(itemData.getMsgId());
			}
		});
		
		requestMsgDatas();
	}
	
	/** 请求标识 **/
	private final int REQUEST_MESSAGE_DATAS = 1;
	private final int REQUEST_COLLECT = 2; 
	private final int REQUEST_CLEAR = 3; 
	/**
	 * 收藏商品
	 */
	private void requestCollect(String msgId){
		
		JsonElementRequest<DefaultMamahaoServerError> req = new JsonElementRequest<DefaultMamahaoServerError>(
				context,"",this);
		
		req.setParam("apiCode", "_user_message_isread");
		req.setParam("userId", MyApplication.getInstance().getMemberId());
		req.setParam("msgId", msgId);
		//消息标记为已读
		/* $apiCode='_user_message_isread';
		$param = array();
		$param['userId'] = '118';
		$param['msgId'] = '8'; */
		
		//获取token
		String token = NetworkManager.getInstance().getPostToken(req.getParam());
		req.setParam("token", token);
		
		addRequestQueue(req, REQUEST_COLLECT);
	}
	
	/**
	 * 收藏商品
	 */
	private void requestClear(){
		showProgressBar(null);
		JsonElementRequest<DefaultMamahaoServerError> req = new JsonElementRequest<DefaultMamahaoServerError>(
				context,"",this);
		
		req.setParam("apiCode", "_user_message_clear");
		req.setParam("userId", MyApplication.getInstance().getMemberId());
		
		//获取token
		String token = NetworkManager.getInstance().getPostToken(req.getParam());
		req.setParam("token", token);
		
		addRequestQueue(req, REQUEST_CLEAR, new ReqTag.Builder().handleSimpleRes(true));
	}
	
	/**
	 * 请求数据
	 */
	private void requestMsgDatas(){
		showProgressBar(null);
		BeanRequest<MessageDatas, MamaHaoServerError> req = new BeanRequest<>(
				context, 
				"", 
				this, 
				MessageDatas.class);
		
		req.setParam("apiCode", "_user_message_lists");
		req.setParam("userId", MyApplication.getInstance().getMemberId());
		
		//获取token
		String token = NetworkManager.getInstance().getPostToken(req.getParam());
		req.setParam("token", token);
		
		addRequestQueue(req, REQUEST_MESSAGE_DATAS, new ReqTag.Builder().handleSimpleRes(true));
		
	}
	
	@Override
	protected void onResponseSuccess(ReqTag tag, Object data) {
		// TODO Auto-generated method stub
		super.onResponseSuccess(tag, data);
		switch (tag.getReqId()) {
		case REQUEST_MESSAGE_DATAS:
			datas.clear();
			datas.addAll(((MessageDatas)data).getMsgList());
			adapter.notifyDataSetChanged();
			break;
		case REQUEST_COLLECT:
			requestMsgDatas();
			break;
		case REQUEST_CLEAR:
			datas.clear();
			adapter.notifyDataSetChanged();
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
		switch (tag.getReqId()) {
		case REQUEST_MESSAGE_DATAS:
			showBlankPage(null, R.drawable.center_picture_xiaoxzhongxini, "暂时没有消息", null, Constant.DEFAULT_TOP_MARGIN);
			break;

		default:
			break;
		}
	}
	
	private class MyAdapter extends CommonAdapter<MessageData>{

		public MyAdapter() {
			super(context, datas, R.layout.item_message);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void convert(BaseViewHolder holder, MessageData itemData,
				int position, ViewGroup parent) {
			// TODO Auto-generated method stub
			holder.setText(R.id.tv_title, itemData.getTitle());
			holder.setText(R.id.tv_time, CommonUtils.stampToDate(Long.parseLong(itemData.getTime())));
			holder.setText(R.id.tv_content, itemData.getDesc());
			
			TextView tv_title = holder.getView(R.id.tv_title);
			TextView tv_content = holder.getView(R.id.tv_content);
			TextView tv_time = holder.getView(R.id.tv_time);
			if(itemData.getIsRead().equals("0")){
				//未读
				tv_title.setTextColor(Color.parseColor("#333333"));
				tv_content.setTextColor(Color.parseColor("#666666"));
				tv_time.setTextColor(Color.parseColor("#999999"));
			}else{
				//已读
				tv_title.setTextColor(Color.parseColor("#cccccc"));
				tv_content.setTextColor(Color.parseColor("#cccccc"));
				tv_time.setTextColor(Color.parseColor("#cccccc"));
			}
		}
		
	}
//
	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.bt_title_right){
				new YesOrNoDialog(context, new YesOrNoDialogEntity("是否确认清空消息?", "否", "是"), new OnYesOrNoDialogClickListener() {
				
				@Override
				public void onYesOrNoDialogClick(YesOrNoType yesOrNoType) {
					// TODO Auto-generated method stub
					switch (yesOrNoType) {
					case BtnOk:
						requestClear();
						break;
	
					default:
						break;
					}
					
				}
			}).show();
		}
	}
//	
	public class MessageData{
		private String msgId;
		private String isRead;
		private String title;
		private String desc;
		private String time;
//		 "msgId": "1",
//         "isRead": "0",
//         "title": "测试消息",
//         "desc": "测试消息",
//         "time": "1474389182"
		public String getMsgId() {
			return msgId;
		}
		public void setMsgId(String msgId) {
			this.msgId = msgId;
		}
		public String getIsRead() {
			return isRead;
		}
		public void setIsRead(String isRead) {
			this.isRead = isRead;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getDesc() {
			return desc;
		}
		public void setDesc(String desc) {
			this.desc = desc;
		}
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		
	}
	
	public class MessageDatas{
		private List<MessageData> msgList;

		public List<MessageData> getMsgList() {
			return msgList;
		}

		public void setMsgList(List<MessageData> msgList) {
			this.msgList = msgList;
		}
		
	}

}
