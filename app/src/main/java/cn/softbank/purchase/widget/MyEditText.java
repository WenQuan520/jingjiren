package cn.softbank.purchase.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.LoginFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.EditText;
import cn.softbank.purchase.utils.CommonUtils;
import cn.softbank.purchase.utils.EmojiUtil;
import cn.yicheng.jingjiren.R;
public class MyEditText extends EditText {
	private Drawable imgAble;
	private Context mContext;
	private Drawable drawableLeft;
	private Drawable backGroundResouse;//
	private boolean isFilterRegex;
	private boolean isFilterEmoji;
	private boolean isShowClose;
	private static final String regEx = "<>;'\\/*&%()";

	public MyEditText(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public MyEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}

	public MyEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;

		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.MyEditTextAttr);
		drawableLeft = typedArray
				.getDrawable(R.styleable.MyEditTextAttr_editDrawableLeft);
		backGroundResouse = typedArray
				.getDrawable(R.styleable.MyEditTextAttr_editBackground);
		isFilterRegex = typedArray.getBoolean(
				R.styleable.MyEditTextAttr_isFilterRegex, true);
		isFilterEmoji = typedArray.getBoolean(
				R.styleable.MyEditTextAttr_isFilterEmoji, true);
		isShowClose = typedArray.getBoolean(
				R.styleable.MyEditTextAttr_isShowClose, true);
		init();
	}

	@SuppressLint("NewApi")
	private void init() {
		imgAble = mContext.getResources().getDrawable(
				R.drawable.icon_login_delete);
		if (backGroundResouse != null) {
			setBackground(backGroundResouse);
		}
		addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if(maxLength>0 && s.length()>maxLength){
					setText(new StringBuilder(s).delete(start, start+s.length()-maxLength));
					setSelection(maxLength);
					return;
				}

				if (isFilterEmoji && EmojiUtil.containsEmoji(s.toString())) {
					s = EmojiUtil.filterEmoji(s.toString());
					setText(s);
					setSelection(s.length());
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (isShowClose)
					setDrawable();
			}
		});

		if (isShowClose)
			setDrawable();

		if (isFilterRegex) {
			InputFilter[] filters = new InputFilter[1];
			filters[0] = new MyInputFilter(regEx);
			setFilters(filters);
		}
	}
	
	private int maxLength;
	/**
	 * 字符数限制
	 * @param maxLength
	 */
	public void setMaxLength(int maxLength){
		this.maxLength = maxLength;
	}
	
	
	public class MyInputFilter extends LoginFilter.UsernameFilterGeneric {
		private String mAllowedDigits;

		public MyInputFilter(String digits) {
			mAllowedDigits = digits;
		}

		@Override
		public boolean isAllowed(char c) {
			if (mAllowedDigits.indexOf(c) != -1) {
				return false;
			}
			return true;
		}
	}

	// 设置删除图片
	private void setDrawable() {
		if (length() < 1)
			setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null,
					null);
		else {
			setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null,
					imgAble, null);
			// 加大图片区域便于用户触碰
			// 此句无效,点击时间是在onTouchEvent中处理的
			// setCompoundDrawablePadding(2);
		}

	}

	// 处理删除事件
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (imgAble != null && event.getAction() == MotionEvent.ACTION_UP&&isShowClose) {
			int eventX = (int) event.getRawX();
			int eventY = (int) event.getRawY();
			Rect rect = new Rect();
			getGlobalVisibleRect(rect);
			// 得到图片范围
			// px转dp,获取40dp的点击区域
			rect.left = rect.right - CommonUtils.dip2px(mContext, 40);
			if (rect.contains(eventX, eventY))
				setText("");
		}
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			// Just ignore the [Enter] key
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

}
