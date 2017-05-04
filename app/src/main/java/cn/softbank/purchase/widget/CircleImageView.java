package cn.softbank.purchase.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;
import cn.yicheng.jingjiren.R;

public class CircleImageView extends ImageView
{
	  private final RectF mRound = new RectF();
	  private float mAdius = 6.0F;
	  private final Paint mMaskPaint = new Paint();
	  private final Paint mZonePaint = new Paint();
	  private Paint mCirclePaint;
	  /**
	   * 是否绘制背景圆
	   */
	  private boolean isDrawCirclebg;
	  /**
	   * 背景圆宽度
	   */
	  private float circlebgWidth;

	  public CircleImageView(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    init();
	    
	    TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.circleImageView, 0, 0);
	    
	    /** update by GXH  **/
	    isDrawCirclebg = ta.getBoolean(R.styleable.circleImageView_isDrawCirclebg, false);
	    if(isDrawCirclebg){
	    	mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    	circlebgWidth = ta.getDimension(R.styleable.circleImageView_circlebgWidth, 1);
	    	mCirclePaint.setColor(ta.getColor(R.styleable.circleImageView_circlebgColor, 0xFFDCDCDC));
	    	mCirclePaint.setStyle(Paint.Style.STROKE);
	    	mCirclePaint.setStrokeWidth(circlebgWidth);
	    }
	    /** update by GXH  **/
	    
	    ta.recycle();
	  }

	  public CircleImageView(Context context) {
	    super(context);
	    init();
	  }

	  protected void init() {
	    this.mMaskPaint.setAntiAlias(true);
	    this.mMaskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
	    this.mZonePaint.setAntiAlias(true);
	    this.mZonePaint.setColor(-1);
	    float density = getResources().getDisplayMetrics().density;
	    this.mAdius *= density;
	    
	    setRectAdius(180.0F);
	  }

	  public void setRectAdius(float adius) {
	    this.mAdius = adius;
	    invalidate();
	  }

	  protected void onLayout(boolean changed, int left, int top, int right, int bottom)
	  {
	    super.onLayout(changed, left, top, right, bottom);
	    int w = getWidth();
	    int h = getHeight();
	    this.mRound.set(0.0F, 0.0F, w, h);
	  }

	  public void draw(Canvas canvas)
	  {
	    canvas.saveLayer(this.mRound, this.mZonePaint, 31);
	    canvas.drawRoundRect(this.mRound, this.mAdius, this.mAdius, this.mZonePaint);
	    canvas.saveLayer(this.mRound, this.mMaskPaint, 31);
	    super.draw(canvas);
	    canvas.restore();
	    
	    /** update by GXH  **/
	    if(isDrawCirclebg)//绘制背景圆
	    	canvas.drawCircle(this.mRound.right/2, this.mRound.right/2, this.mRound.right/2-circlebgWidth, this.mCirclePaint);
	    /** update by GXH  **/
	  }
	}