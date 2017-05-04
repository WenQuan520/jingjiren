package cn.softbank.purchase.widget;


import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.yicheng.jingjiren.R;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * <pre>listview或gridview滑动监听</pre>
 * <pre>自动处理footer状态</pre>
 * @author GXH
 *
 */
public class FooterListViewUtil implements OnScrollListener{

	private Context context;
	
	public FooterListViewUtil(Context context) {
		this.context = context;
	}
	
	private FooterScrollListener listener;
	private boolean pauseOnScroll;
	private boolean pauseOnFling;
	/**
	 * 设置滚动监听
	 * @param absListView
	 * @param listener
	 * @param pauseOnScroll 手势滑动过程停止加载图片
	 * @param pauseOnFling  滑动过程停止加载图片
	 */
	public void setOnScrollListener(AbsListView absListView,FooterScrollListener listener, boolean pauseOnScroll, boolean pauseOnFling){
		//初始化底部view
		if(absListView instanceof ListView)
			((ListView)absListView).addFooterView(initFootView());
		else if(absListView instanceof HeaderFooterGridView)
			((HeaderFooterGridView)absListView).addFooterView(initFootView());
		else
			throw new NullPointerException("必须是ListView或HeaderFooterGridView才能使用");
		
		//滑动中是否加载图片
		this.pauseOnFling = pauseOnFling;
		this.pauseOnScroll = pauseOnScroll;
		
		//监听者
		this.listener = listener;
		//开启监听
		absListView.setOnScrollListener(this);
	}

	private View footerView;
	private TextView tv_probar;
	private ProgressBar probar;
	private int defaultLayoutId = R.layout.my_footview;
	
	/** 初始化底部view */
	private View initFootView() {
		// TODO Auto-generated method stub
		footerView = View.inflate(context,defaultLayoutId, null);
		tv_probar = (TextView) footerView.findViewById(R.id.tv_probar);
		probar = (ProgressBar) footerView.findViewById(R.id.probar);
		footerView.setVisibility(View.GONE);
		footerView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		return footerView;
	}
	
	public FooterListViewUtil initLayout(int defaultLayoutId){
		this.defaultLayoutId = defaultLayoutId;
		return this;
	}
	
	public View getFooterView(){
		return footerView;
	}
	
	/**
	 * 正在加载数据
	 */
	public static final int FOOTER_STATE_LOADING = 0;
	/**
	 * 数据加载完毕
	 */
	public static final int FOOTER_STATE_LOAD_FINISH = 1;// 界面刷新
	/**
	 * 没有更多数据
	 */
	public static final int FOOTER_STATE_NO_MORE_DATA = 2;
	
	/**
	 * 没有更多数据 不现实footerView
	 */
	public static final int FOOTER_STATE_EMPTY = 3;
	
	private int footerState=FOOTER_STATE_LOAD_FINISH;
	
	/**
	 * 设置数据加载对应的footerview状态
	 * @param state
	 */
	public void setFooterState(int state){
		footerState = state;
		
		switch (state) {
		case FOOTER_STATE_LOADING:
			//正在加载数据
			footerView.setVisibility(View.VISIBLE);
			tv_probar.setText("加载数据中");
			break;

		case FOOTER_STATE_LOAD_FINISH:
			//加载完毕
			footerView.setVisibility(View.INVISIBLE);
			break;

		case FOOTER_STATE_NO_MORE_DATA:
			//没有更多数据
			footerView.setVisibility(View.VISIBLE);
			tv_probar.setText(endStr);
			probar.setVisibility(View.GONE);
			break;
			
		case FOOTER_STATE_EMPTY:
			//没有更多数据
			footerView.setVisibility(View.GONE);
			break;

		default:
			break;
		}
	}
	
	private String endStr = "没有更多的数据";
	public void setEndStr(String endStr){
		this.endStr = endStr;
	}
	
	private int getFooterState(){
		return footerState;
	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		//监听滚动
		if(listener == null)
			return;
		listener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		//监听是否滚动到底部
		final int totalCount = firstVisibleItem + visibleItemCount;
		if (getFooterState()==FOOTER_STATE_LOAD_FINISH && visibleItemCount != totalItemCount
				&& totalCount == totalItemCount && totalCount>0) {// 已经移动到了listView的最后
			// 滑到底部，状态置为正在加载数据
			setFooterState(FOOTER_STATE_LOADING);
			//发送监听
			listener.onSrollToBottom();
		}
		
	}
	

	/** 滑动过程图片加载处理 **/
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(listener != null)
			listener.onScrollStateChanged(view,scrollState);
		//不处理
		if(!pauseOnScroll && !pauseOnFling)
			return;
		//处理滑动停止加载
		switch (scrollState) {
		case OnScrollListener.SCROLL_STATE_IDLE:
			ImageLoader.getInstance().resume();
			break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
			if (pauseOnScroll) 
				ImageLoader.getInstance().pause();
			break;
		case OnScrollListener.SCROLL_STATE_FLING:
			if (pauseOnFling) 
				ImageLoader.getInstance().pause();
			break;
		}
		
	}
	
	/**
	 * 监听滑动事件 
	 * @author Administrator GXH
	 *
	 */
	public interface FooterScrollListener{
		public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount);
		public void onSrollToBottom();
		public void onScrollStateChanged(AbsListView view, int scrollState);
	}
	
}