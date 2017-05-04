/**
 * 项目名称:MamaHao1.01
 * 文件名称:YesOrNoDialog.java
 * 包名称:cn.atmobi.mamhao.dialog
 * 日期:2015年7月22日下午3:57:21
 * Copyright (c) 2015, 杭州尽在网络技术有限公司 All Rights Reserved.
 *
 */
package cn.softbank.purchase.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import cn.yicheng.jingjiren.R;

/**
 * @描述 是否选择通用框
 * @Copyright Copyright (c) 2015
 * @Company 杭州尽在网络技术有限公司.
 * @author WL
 * @date 2015年7月22日下午3:57:21
 * @version 1.0
 */
public class YesOrNoDialog extends Dialog implements OnClickListener {
    private YesOrNoDialogEntity dialogEntity;
    private OnYesOrNoDialogClickListener listener;
    private TextView mSureBtn;
    private TextView mCancelBtn;
    private TextView mYesOrNoLabel;
    public YesOrNoDialog(Context context, YesOrNoDialogEntity dialogEntity,
            OnYesOrNoDialogClickListener listener) {
        super(context, R.style.BMF_Dialog);
        this.dialogEntity = dialogEntity;
        this.listener = listener;
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.common_simple_dialog);
        initViews();
        notifyDataSetChanged();

    }
    
    public void changeDialogMsg(String msg){
        this.dialogEntity.titleOne = msg;
        notifyDataSetChanged();
    }

    /**
     * 
     */
    private void initViews() {
        mYesOrNoLabel = (TextView) this.findViewById(R.id.tv_content);
        mSureBtn = (TextView) this.findViewById(R.id.bt_yes);
        mCancelBtn = (TextView) this.findViewById(R.id.bt_no);
        mSureBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
    }

    private void notifyDataSetChanged() {
        if (!TextUtils.isEmpty(dialogEntity.btnCancelLabel)) {
            mCancelBtn.setText(dialogEntity.btnCancelLabel);
            mCancelBtn.setVisibility(View.VISIBLE);
        } else {
            mCancelBtn.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(dialogEntity.btnOkLabel)) {
            mSureBtn.setText(dialogEntity.btnOkLabel);
            mSureBtn.setVisibility(View.VISIBLE);
        } else {
            mSureBtn.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(dialogEntity.titleOne)) {
            mYesOrNoLabel.setText(dialogEntity.titleOne);
            mYesOrNoLabel.setVisibility(View.VISIBLE);
        } else {
            mYesOrNoLabel.setVisibility(View.GONE);
        }
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_yes:
                dismiss();
                if (listener != null) {
                    listener.onYesOrNoDialogClick(YesOrNoType.BtnOk);
                }
                break;
            case R.id.bt_no:
                dismiss();
                if (listener != null) {
                    listener.onYesOrNoDialogClick(YesOrNoType.BtnCancel);
                }
                break;

            default:
                break;
        }

    }

    public interface OnYesOrNoDialogClickListener {
        void onYesOrNoDialogClick(YesOrNoType yesOrNoType);
    }

    public enum YesOrNoType {
        BtnCancel, BtnOk
    }
}
