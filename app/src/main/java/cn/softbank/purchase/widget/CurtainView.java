package cn.softbank.purchase.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import cn.yicheng.jingjiren.R;

/**
 * 
 * @author ld
 */
public class CurtainView extends RelativeLayout {
	private Context mContext;
	/** Scroller 拖动类 */
	private Scroller mScroller;
	/** 广告幕布的高度 */
	private int curtainHeigh = 200;
	/** 是否 打开 */
	private boolean isOpen = false;
	/** 是否在动画 */
	private boolean isMove = false;
	/** 上升动画时间 */
	private int upDuration = 1000;
	/** 下落动画时间 */
	private int downDuration = 500;
	
	private View view;
	
	private OnPopCreateViewListener onPopCreateViewListener;
	
	public CurtainView(Context context) {
		super(context);
	}

	public CurtainView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CurtainView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/** 初始化 */
	public void init(Context context,int layoutResId,OnPopCreateViewListener onPopCreateViewListener) {
		this.mContext = context;
		// Interpolator 设置为有反弹效果的 （Bounce：反弹）
		Interpolator interpolator = new BounceInterpolator();
		mScroller = new Scroller(context, null);
		View view = LayoutInflater.from(mContext).inflate(layoutResId, null);
		curtainHeigh = (int) getResources().getDimension(R.dimen.search_pop_maxheigh);
		this.onPopCreateViewListener = onPopCreateViewListener;
		view = onPopCreateViewListener.getView(view, this);
		addView(view);
		view.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (mScroller.computeScrollOffset() && !isOpen) {
					CurtainView.this.setVisibility(View.GONE);
				}
			}
		});
	}

	public void setHeight(int curtainHeigh) {
		this.curtainHeigh = curtainHeigh;
	}
	
	/**
	 * 拖动动画
	 * 
	 * @param startY
	 * @param dy
	 *            垂直距离, 滚动的y距离
	 * @param duration
	 *            时间
	 */
	public void startMoveAnim(int startY, int dy, int duration) {
		isMove = true;
		mScroller.startScroll(0, startY, 0, dy, duration);
		invalidate();// 通知UI线程的更新
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
	}

	@Override
	public void computeScroll() {
		// 判断是否还在滚动，还在滚动为true
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			// 更新界面
			postInvalidate();
			isMove = true;
		} else {
			isMove = false;
		}
		super.computeScroll();
	}

	/**
	 * 点击开关，会展开关闭 在onToch中使用这个中的方法来当点击事件，避免了点击时候响应onTouch的衔接不完美的影响
	 */
	public void onRopeClick() {
		if (isOpen) {
			CurtainView.this.startMoveAnim(0, curtainHeigh, upDuration);
			view.setVisibility(View.GONE);
		} else {
			this.setVisibility(View.VISIBLE);
			view.setVisibility(View.VISIBLE);
			CurtainView.this.startMoveAnim(curtainHeigh, -curtainHeigh,
					downDuration);
		}
		isOpen = !isOpen;
		onPopCreateViewListener.onChange();
	}

	public boolean isOpen() {
		return isOpen;
	}
	
	public void setViewbg(View view){
		this.view = view;
	}
	
	public interface OnPopCreateViewListener{
		public View getView(View view, View groupView);
		public void onChange();
	}

}
