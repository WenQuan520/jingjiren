package cn.softbank.purchase.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.softbank.purchase.utils.CommonUtils;
import cn.yicheng.jingjiren.R;

public class HomeSortNaviBar extends RelativeLayout implements OnClickListener{
	
	private TextView[] navi_bar_tvs;
	private Context context;
	private OnNaviBarChangeListener onNaviBarChangeListener;
	//未选中的文本颜色
	private int normalTextColor = 0xFF999999;
	//选择的文本颜色
	private int selTextColor = 0xFF14C431;

	public HomeSortNaviBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public HomeSortNaviBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public HomeSortNaviBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	private void init(Context context) {
		// TODO Auto-generated method stub
		this.context = context;
		
		View.inflate(context, R.layout.goods_list_navi_bar, this);
		navi_bar_tvs = new TextView[] {
				(TextView) findViewById(R.id.navi_bar_01),
				(TextView) findViewById(R.id.navi_bar_02),
				(TextView) findViewById(R.id.navi_bar_03),
				(TextView) findViewById(R.id.navi_bar_04), };
		for (int i = 0; i < navi_bar_tvs.length; i++) {
			navi_bar_tvs[i].setOnClickListener(this);
		}
		
	}
	
	/**
	 * 导航变化监听
	 * @param listener
	 */
	public void setOnNaviBarChangeListener(OnNaviBarChangeListener listener){
		this.onNaviBarChangeListener = listener;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.navi_bar_01:
			changeNavi(0,true);
			break;
		case R.id.navi_bar_02:
			changeNavi(1,true);
			break;
		case R.id.navi_bar_03:
			changeNavi(2,true);
			break;
		case R.id.navi_bar_04:
			changeNavi(3,true);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 查询类型 0综合，1销量，2价格，3最新
	 */
	private int naviPostion;
	/**
	 * 箭头   0代表 高>低，1代表 低>高
	 */
//	private int arrowDirectionNavi01;
//	private int arrowDirectionNavi02;
	
	/**
	 * 改变导航状态
	 * @param postion 点击的位置
	 * @param isNotifyListner 是否监听改变
	 */
	public void changeNavi(int postion,boolean isNotifyListner) {
//		if (postion != naviPostion) {
			// 改变之前和现在的文本颜色
			changeTextColor(naviPostion, postion);
			
			// 改变之前箭头方向样式
			changeArrow(naviPostion, false);
			
			//上次位置
			int lastPos = naviPostion;
			//当前位置
			naviPostion = postion;
			//通知变化
			if(isNotifyListner && onNaviBarChangeListener != null)
				onNaviBarChangeListener.onNaviChanged(lastPos,naviPostion);
			
			// 改变现在箭头方向样式
			changeArrow(naviPostion, true);
//		}
	}
	
	/**
	 * 改变字体颜色
	 * @param lastSelPos 上次选择的位置
	 * @param currentSelPos 当前选中位置
	 */
	private void changeTextColor(int lastSelPos,int currentSelPos){
		navi_bar_tvs[lastSelPos].setTextColor(normalTextColor);
		navi_bar_tvs[currentSelPos].setTextColor(selTextColor);
	}
	
	/**
	 * 改变箭头方向
	 * 
	 * @param pos 位置
	 * @param isSel 选中还是取消选中
	 */
	private void changeArrow(int postion, boolean isSel) {
		Drawable drawableRight = null ;
		if(isSel)
			drawableRight = CommonUtils.GetDrawable(context,R.drawable.mmh_ic_sortdown_checked);
		else
			drawableRight = CommonUtils.GetDrawable(context,R.drawable.mmh_ic_sortdown_normal);
			
		navi_bar_tvs[postion].setCompoundDrawables(null,null,drawableRight,null);
	}

	/**
	 * @return  当前选中项
	 */
	public int getNaviPostion() {
		return this.naviPostion;
	}
	
	public interface OnNaviBarChangeListener{
		/**
		 * 导航切换
		 */
		public void onNaviChanged(int lastNaviPostion, int currentNaviPostion);
	}

}
