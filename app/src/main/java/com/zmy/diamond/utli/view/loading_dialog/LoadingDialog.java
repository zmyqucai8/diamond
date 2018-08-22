package com.zmy.diamond.utli.view.loading_dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.wang.avi.AVLoadingIndicatorView;
import com.zmy.diamond.R;

/**
 * Created by zhangmengyun on 2018/8/10.
 */

public class LoadingDialog extends Dialog {


    public String text_defult = "正在加载...";

    /**
     * 是否可以按取消键关闭 ，点击其他地方是否关闭， 默认true=可以
     */
    boolean isCanceled = true;

    AVLoadingIndicatorView loadingview;
    TextView tv_text;

    private void initView(final Context context, final String text) {

        View view = View.inflate(context, R.layout.loading_view, null);

        loadingview = (AVLoadingIndicatorView) view.findViewById(R.id.loadingview);
        setContentView(view);
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;


        tv_text = view.findViewById(R.id.tv_text);
        lp.y = -ConvertUtils.dp2px(50);
        window.setAttributes(lp);
//        setCancelable(isCanceled);//设置进度条是否可以按退回键取消 ,可以点击空白取消;
//        setCanceledOnTouchOutside(isCanceled);//按对话框以外的地方不起作用。按返回键还起作用
        if (!TextUtils.isEmpty(text)) {
            tv_text.setText(text);
        }

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
//                if (null != context) {
//                    OkGo.getInstance().cancelTag(context);
//                }
//                if (null != loadingview) {
//                    loadingview.hide();
//                }
            }
        });
    }

    /**
     * 构造方法  使用默认的文本提示内容 "正在加载..." ，并且可以手动关闭
     *
     * @param context
     */
    public LoadingDialog(@NonNull final Context context) {
        super(context, R.style.loading_style);
        this.isCanceled = true;
        initView(context, text_defult);
    }

    /**
     * 构造方法  ，  使用传入的text
     *
     * @param text       提示语
     * @param context
     * @param isCanceled true=可以取消，false=不可以取消
     */
    public LoadingDialog(@NonNull final Context context, boolean isCanceled, String text) {
        super(context, R.style.loading_style);
        this.isCanceled = isCanceled;
        initView(context, text);
    }

    /**
     * 构造方法  ， 使用默认的文本内容， "正在加载..."
     *
     * @param context
     * @param isCanceled true=可以取消，false=不可以取消
     */
    public LoadingDialog(@NonNull final Context context, boolean isCanceled) {
        super(context, R.style.loading_style);
        this.isCanceled = isCanceled;
        initView(context, "");
    }

    /**
     * 显示loading
     */
    public void showLoading() {

        if (null != loadingview) {
            loadingview.show();
        }
        this.show();
    }

    /**
     * 隐藏loading
     */
    public void hideLoading() {
        this.dismiss();
    }

    public void setText(String text) {
        if (!TextUtils.isEmpty(text)) {
            tv_text.setText(text);
        } else {
            tv_text.setText(text_defult);
        }
    }
}
