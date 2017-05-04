package cn.softbank.purchase.widget;

import cn.softbank.purchase.widget.FooterListViewUtil.FooterScrollListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
/**
 * @描述: 1.设置最大高度限制，如果是在复合控件中如scrollview，则 setLimitHeight(MyListView.MaxHeight)
 * <pre>2.自动监听滑到底部，根据状态处理footerview,
 * 给listview添加FooterScrollListener.ScrollFooterListener监听
 * 数据返回后调用
 * setFooterState(FooterListViewUtil.FOOTER_STATE_LOAD_FINISH)
 * 数据全部加载完，没有更多数据调用
 * setFooterState(FooterListViewUtil.FOOTER_STATE_NO_MORE_DATA)
 * </pre>
 * @author GXH
 */
public class MyListView extends ListView {
	/**
	 * 不限制高度,用于复合控件中使用
	 */
	public static final int MaxHeight = Integer.MAX_VALUE >> 2;
	/**
	 * 高度限制
	 */
	private int limitHeight = MaxHeight;
	private Context context;

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MyListView(Context context) {
		super(context);
		init(context);
	}

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	private void init(Context context) {
		// TODO Auto-generated method stub
		this.context = context;
	}

	/**
	 * 设置监听
	 * @param listener
	 */
	private FooterListViewUtil footerListViewUtil;
	public void setOnMyScrollListener(FooterScrollListener listener){
		setOnMyScrollListener(listener, false, false);
	}
	
	/**
	 * 设置监听
	 * @param listener
	 * @param pauseOnScroll 手势滑动过程暂停加载图片
	 * @param pauseOnFling  滑动过程暂停加载图片
	 */
	public void setOnMyScrollListener(FooterScrollListener listener, boolean pauseOnScroll, boolean pauseOnFling){
		//一般使用监听底部功能者不需要处理高度问题
		setLimitHeight(0);
		footerListViewUtil = new FooterListViewUtil(context);
		footerListViewUtil.setOnScrollListener(this, listener, pauseOnScroll, pauseOnFling);
	}
	
	/**
	 * 设置数据加载对应的footerview状态
	 * FooterListViewUtil.FOOTER_STATE_LOADING 正在加载数据
	 * FooterListViewUtil.FOOTER_STATE_LOAD_FINISH 分页加载完毕
	 * FooterListViewUtil.FOOTER_STATE_NO_MORE_DATA 没有更多数据了
	 * @param state
	 */
	public void setFooterState(int state){
		if(state == FooterListViewUtil.FOOTER_STATE_EMPTY){
			try {
				removeFooterView(footerListViewUtil.getFooterView());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		footerListViewUtil.setFooterState(state);
	}
	
	public void setEndStr(String endStr){
		footerListViewUtil.setEndStr(endStr);
	}
	
	public View getFooterView(){
		return footerListViewUtil.getFooterView();
	}
	
	/**
	 * 设置listView最大高度
	 * 
	 * @param height
	 */
	public void setLimitHeight(int height) {
		this.limitHeight = height;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec,
				limitHeight > 0 ? MeasureSpec.makeMeasureSpec(limitHeight,MeasureSpec.AT_MOST) : heightMeasureSpec);
	}
	
	private boolean touchAble = true;
	private boolean isFunctionUp = true;
	/**
	 * 设置是否消化touch事件
	 * @param touchAble
	 */
	public void setTouchAble(boolean touchAble){
		this.touchAble = touchAble;
	}
	/**
	 * 当listview向下滑动时禁掉
	 */
	public void setTouchFuntionDown(){
		isFunctionUp = false;
	}
	
	private float lastY;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastY = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			//滑动到顶部且继续向上滑动或继续向下滑动
			if(!touchAble && (isFunctionUp == event.getY()-lastY>0))
				return false;
			break;

		default:
			break;
		}
		return super.onTouchEvent(event);
	}
}