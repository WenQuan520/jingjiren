package cn.softbank.purchase.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;
import cn.yicheng.jingjiren.R;
/**
 * 开关控件
 * @author Administrator GXH
 *
 */
public class MyCheckBox extends ImageButton {
	
	private boolean isChecked;
	private Drawable checkOnImg;
	private Drawable checkOffImg;
	/**
	 * 是否自动变化
	 */
	private boolean isAutoChange;
	
	public MyCheckBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	public MyCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		//自定义属性
		TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.MyCheckBox);
		//开关按钮样式
		checkOnImg = typedArray.getDrawable(R.styleable.MyCheckBox_checkOnImg);
		if(checkOnImg == null){
			checkOnImg = getResources().getDrawable(R.drawable.checkbox_on);
			checkOffImg = getResources().getDrawable(R.drawable.checkbox_off);
		}else
			checkOffImg = typedArray.getDrawable(R.styleable.MyCheckBox_checkOffImg);
		
		isChecked = typedArray.getBoolean(R.styleable.MyCheckBox_isChecked, true);
		isAutoChange = typedArray.getBoolean(R.styleable.MyCheckBox_isAutoChange, false);
		
		typedArray.recycle();
		
		setBackgroundColor(getResources().getColor(android.R.color.transparent));
		setPadding(0, 0, 0, 0);
		setIsChecked(isChecked);
	}
	
	public void setAutoChange(boolean isAutoChange){
		this.isAutoChange = isAutoChange;
	}

	public boolean isChecked(){
		return isChecked;
	}
	
	public void setIsChecked(boolean isChecked){
		this.isChecked=isChecked;
		if (isChecked)
			setImageDrawable(checkOnImg);
		else
			setImageDrawable(checkOffImg);
	}
	
	/**
	 * 设置是否禁用功能
	 */
	@SuppressLint("NewApi") public void setUnabled(boolean isUnabled){
		isAutoChange = !isUnabled;
		if(isUnabled)
			setAlpha(0.6f);
		else
			setAlpha(1.0f);	
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isAutoChange && event.getAction() == MotionEvent.ACTION_UP)
			setIsChecked(!isChecked);
		
		return super.onTouchEvent(event);
	}

}
