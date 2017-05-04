package cn.softbank.purchase.widget;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.softbank.purchase.utils.CommonUtils;
import cn.yicheng.jingjiren.R;
/**
 * @描述 热词分类自定义View
 * @Copyright Copyright (c) 2016
 * @Company 杭州百米贩科技公司.
 * @author GXH
 * @version 1.0 
 */
public class HotWordView extends LinearLayout implements OnClickListener{

	/**
	 * 整体下边距
	 */
	private int paddingBottom = 10;
	/**
	 * 整体左边距
	 */
	private int paddingLeft = 10;
	/**
	 * 文字大小
	 */
	private int textSize = 13;
	/**
	 * 文字颜色
	 */
	private int textColor = 0xFF333333;
	/**
	 * 文字颜色(选中状态)
	 */
	private int textColorSel = 0xFFCC2267;
	/**
	 * 文字背景
	 */
	private int textBg = R.drawable.bg_hot_word;
	/**
	 * 文字左右间距
	 */
	private int textPadding = 8;
	/**
	 * 文字高度
	 */
	private int textHeight = 28;
	/**
	 * 文字最小宽度
	 */
	private int textMinWidth = 60;
	/**
	 * 文字右边距
	 */
	private int textMarginRight = 10;
	/**
	 * 文字上边距
	 */
	private int textMarginBottom = 8;
	
	public HotWordView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		// TODO Auto-generated constructor stub
	}

	public HotWordView(Context context) {
		super(context);
		init(context);
		// TODO Auto-generated constructor stub
	}
	
	private Context context;
	private Paint pFont = new Paint(); 
	private Rect rect = new Rect();
	private void init(Context context){
		this.context = context;
		//计量单位转化
		float scale = context.getResources().getDisplayMetrics().density;
		paddingLeft = dip2px(paddingLeft, scale);
		paddingBottom = dip2px(paddingBottom, scale);
		textPadding = dip2px(textPadding, scale);
		textHeight = dip2px(textHeight, scale);
		textMarginRight = dip2px(textMarginRight, scale);
		textMarginBottom = dip2px(textMarginBottom, scale);
		textMinWidth = dip2px(textMinWidth, scale);
		//设置测量参数
		pFont.setTextSize((int)(textSize * context.getResources().getDisplayMetrics().scaledDensity + 0.5f));
		
		setOrientation(LinearLayout.VERTICAL);
		setPadding(paddingLeft, 0, 0, 0);
	}
	
	private int dip2px(int dpValue,float scale){
		return (int) (dpValue * scale + 0.5f);
	}
	
//	private List<SKUData> datas;
	private HotWordChangeListener wordListener;
	
	public void setDatas(List<String> datas,HotWordChangeListener listener){
//		this.datas = datas;
		this.wordListener = listener;
		int totalWidth = CommonUtils.getScreenSize(context)[0] - paddingLeft*2;
		
		//LinearLayout布局属性
		LayoutParams params = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 0, 0, textMarginBottom);
		
		//文字布局属性
		LayoutParams paramsText = new LayoutParams(
				LayoutParams.WRAP_CONTENT, textHeight);
		paramsText.setMargins(0, 0, textMarginRight, 0);
		
		//横向属性父view
		LinearLayout ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		
		ll.setLayoutParams(params);
		addView(ll);
		
		int width = 0;
		//开始绘制
		for(int i=0,size=datas.size();i<size;i++){
			String data = datas.get(i);
			
			TextView tv_sku = new TextView(context);
			tv_sku.setTextSize(textSize);
			tv_sku.setPadding(textPadding, 0, textPadding, 0);
			tv_sku.setMinWidth(textMinWidth);
			tv_sku.setSingleLine(true);
			tv_sku.setGravity(Gravity.CENTER);
			tv_sku.setBackgroundResource(textBg);
			tv_sku.setText(data);
			tv_sku.setLayoutParams(paramsText);
			tv_sku.setTag(data);
			tv_sku.setOnClickListener(this);
			
			//初始选择项
//			if(i==0)
//				currentTv = tv_sku;
//				tv_sku.setTextColor(textColorSel);
//			}else
				tv_sku.setTextColor(textColor);
			
			//宽度更新，左右边距加右外边距加文字宽度
			int textWidth = textPadding*2+getTextWidth(data);
			width = width+(textWidth<textMinWidth?textMinWidth:textWidth)+textMarginRight;
			
			if(width > totalWidth){
				//新起一行
				ll = new LinearLayout(context);
				ll.setOrientation(LinearLayout.HORIZONTAL);
				ll.setLayoutParams(params);
				addView(ll);
				width = textPadding*2+getTextWidth(data)+textMarginRight;
			}
			//添加textview
			ll.addView(tv_sku);
		}
		
	}
	
	private int getTextWidth(String text){
		pFont.getTextBounds(text, 0, text.length(), rect);
		return rect.width();
	}
	
//	private TextView currentTv;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//重复点击
//		if(currentTv == v)
//			return;
		//改变之前的文字颜色
//		currentTv.setTextColor(textColor);
		//当前tv
//		currentTv = (TextView) v;
//		currentTv.setTextColor(textColorSel);
		
		wordListener.onWordChange((String) v.getTag());
	}
	
	/**
	 * 切换监听
	 * @author Administrator
	 *
	 */
	public interface HotWordChangeListener{
		public void onWordChange(String hotword);
	}

}
