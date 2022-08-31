package com.xu.kotandroid.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.xu.kotandroid.R;


/**
 * Created by ysp
 * on 2022/7/25
 */
public class MoreMenuPopupWindow extends PopupWindow implements View.OnClickListener {
    private Context mContext;
    private View contentView;

    public MoreMenuPopupWindow(Context context) {
        mContext = context;
        intView();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        this.setContentView(contentView);
        this.setWidth(500);
        this.setHeight(600);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        this.setBackgroundDrawable(new ColorDrawable(0));
        Activity activity = (Activity) context;
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.7f;
        activity.getWindow().setAttributes(lp);
    }

    private void intView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.popupwindow_my, null);
        TextView tv_switch = contentView.findViewById(R.id.tv_switch);
        tv_switch.setOnClickListener(this);
        TextView tv_message = contentView.findViewById(R.id.tv_message);
        tv_message.setOnClickListener(this);
        TextView tv_scan = contentView.findViewById(R.id.tv_scan);
        tv_scan.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_switch:
                Toast.makeText(mContext, "切换", Toast.LENGTH_SHORT).show();
                dismiss();
                break;
            case R.id.tv_message:
                Toast.makeText(mContext, "消息", Toast.LENGTH_SHORT).show();
                dismiss();
                break;
            case R.id.tv_scan:
                Toast.makeText(mContext, "扫一扫", Toast.LENGTH_SHORT).show();
                dismiss();
                break;
        }
    }

    public void showPopupWindow(View view, int x, int y) {
        if (!this.isShowing()) {
            showAsDropDown(view, x, y);
        } else {
            this.dismiss();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        Activity activity = (Activity) mContext;
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 1f;
        activity.getWindow().setAttributes(lp);
    }
}
