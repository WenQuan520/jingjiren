package cn.softbank.purchase.dialog;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import cn.yicheng.jingjiren.R;
public class FollowDialog extends Dialog implements OnClickListener {
    private HuxingDialogClickListener listener;
    public FollowDialog(Context context,
    		HuxingDialogClickListener listener) {
        super(context, R.style.BMF_Dialog);
        this.listener = listener;
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.dialog_follow);
        initViews();

    }

    /**
     * 
     */
    private void initViews() {
      //户型列表
      findViewById(R.id.tv_no_consider).setOnClickListener(this);	
      findViewById(R.id.tv_guaduan).setOnClickListener(this);	
      findViewById(R.id.tv_ok).setOnClickListener(this);	
    }

    @Override
    public void onClick(View v) {
    	switch (v.getId()) {
		case R.id.tv_no_consider:
			listener.onYesOrNoDialogClick("暂不考虑");
			dismiss();
			break;
		case R.id.tv_guaduan:
			listener.onYesOrNoDialogClick("直接挂断");
			dismiss();
			break;
		case R.id.tv_ok:
			listener.onYesOrNoDialogClick(((EditText)findViewById(R.id.edit_text)).getText().toString());
			dismiss();
			break;

		default:
			break;
		}
    	
    }

    public interface HuxingDialogClickListener {
        void onYesOrNoDialogClick(String info);
    }
}
