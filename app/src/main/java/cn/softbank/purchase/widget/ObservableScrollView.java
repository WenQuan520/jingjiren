package cn.softbank.purchase.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;
/**
 * 监听滑动到底部的scrolview
 * @author Administrator
 *
 */
public class ObservableScrollView extends ScrollView {
	private ScrollViewListener scrollViewListener = null;

	public ObservableScrollView(Context context) {
		super(context);
	}

	public ObservableScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public ObservableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setOnScrollViewListener(ScrollViewListener scrollViewListener) {
		this.scrollViewListener = scrollViewListener;
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		
		if (scrollViewListener != null)
			scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
		
		
		if (scrollViewListener != null && y + getHeight() >= computeVerticalScrollRange())
				scrollViewListener.onScrollToBottom(this);
		
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
			if (Math.abs(ev.getX() - this.xLast) <= Math.abs(ev.getY() - this.yLast))
				return super.onInterceptTouchEvent(ev);
			
			return false;
		}

		return super.onInterceptTouchEvent(ev);
	}
	
	@Override
	protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
		// 禁止scrollView内布局变化后自动滚动
		return 0;
	}

	public interface ScrollViewListener {

		void onScrollChanged(ObservableScrollView scrollView, int x, int y,
                             int oldx, int oldy);

		void onScrollToBottom(ObservableScrollView scrollView);
	}

}
