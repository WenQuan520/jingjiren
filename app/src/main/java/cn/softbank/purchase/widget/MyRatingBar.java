package cn.softbank.purchase.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RatingBar;
/**
 * 评价等级
 * @author Administrator GXH
 *
 */
public class MyRatingBar extends RatingBar {

	public MyRatingBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyRatingBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyRatingBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	private int limitNum=1;
	private int offset=40;
	public void setLimitNum(int limitNum,int offset){
		this.limitNum = limitNum;
		this.offset = offset;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(event.getX()<offset){
			setRating(limitNum);
			return true;
		}
		return super.onTouchEvent(event);
	}

}
