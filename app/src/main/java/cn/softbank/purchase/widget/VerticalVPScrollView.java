package cn.softbank.purchase.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
/**
 * @描述 主要用于滑到底部时把touch事件传递给父View，配合VerticalViewPager使用的
 * @author Administrator GXH
 *
 */
public class VerticalVPScrollView extends ObservableScrollView{
	public VerticalVPScrollView(Context context) {
		super(context);
	}

	public VerticalVPScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public VerticalVPScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	private boolean touchAble = true;
	/**
	 * 设置是否禁用touch事件
	 * @param touchAble 是否禁用
	 */
	public void setTouchAble(boolean touchAble){
		this.touchAble = touchAble;
	}
	
	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		//不阻断
		touchAble = true;
		//可以阻断touch事件
		if (y + getHeight() >= computeVerticalScrollRange())
			touchAble = false;
		
	}
	
	private float xLast;
	private float yLast;
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			this.xLast = ev.getX();
			this.yLast = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			//X轴滑动一律不截断
			if (Math.abs(ev.getX() - this.xLast) <= Math.abs(ev.getY() - this.yLast)){
				//Y轴到底部时向下滑动事件截断
				if(!touchAble && (ev.getY() - yLast)<0)
					return true;
				return super.onInterceptTouchEvent(ev);
			}
			return false;
		}

		return super.onInterceptTouchEvent(ev);
	}
	
	private float mYLast;
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mYLast = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			//向下滑动时是否阻断事件消化
			if(!touchAble && (ev.getY() - mYLast)<0)
				return false;
		}
		
		return super.onTouchEvent(ev);
	}
	
}
