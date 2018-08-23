package com.zmy.diamond.utli;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.allenliu.versionchecklib.callback.OnCancelListener;
import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.CustomDownloadFailedListener;
import com.allenliu.versionchecklib.v2.callback.CustomDownloadingDialogListener;
import com.allenliu.versionchecklib.v2.callback.CustomVersionDialogListener;
import com.allenliu.versionchecklib.v2.callback.ForceUpdateListener;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.lzy.okgo.model.Response;
import com.zmy.diamond.R;
import com.zmy.diamond.utli.bean.AppVersionBean;
import com.zmy.diamond.utli.view.BaseDialog;

/**
 * APP更新工具类
 * Created by zhangmengyun on 2018/8/23.
 */

public class UpdateAppUtlis {
    /**
     * 检测APP更新
     *
     * @param activity
     */
    public static void checkAppUpdate(final Activity activity) {
        ApiUtlis.getAppVersion(activity, MyUtlis.getToken(), new JsonCallBack<AppVersionBean>(AppVersionBean.class) {

            @Override
            public void onSuccess(final Response<AppVersionBean> response) {
                if (null != response.body()) {
                    if (response.body().getCode() == AppConstant.CODE_SUCCESS && null != response.body().getData()) {
                        int newVersion = response.body().getData().getNew_version();
                        int oldVersion = AppUtils.getAppVersionCode();
                        if (oldVersion < newVersion && !TextUtils.isEmpty(response.body().getData().getDown_url())) {
                            String updateContent = response.body().getData().getMessage();
                            //设置更新地址更新标题更新内容
//                            response.body().getData().setDown_url("http://test-1251233192.coscd.myqcloud.com/1_1.apk");
                            final DownloadBuilder downloadBuilder = AllenVersionChecker
                                    .getInstance()
                                    .downloadOnly(
                                            UIData.create().setDownloadUrl(response.body().getData().getDown_url())
                                                    .setTitle("版本更新")
                                                    .setContent(updateContent)
                                    );
                            if (response.body().getData().isNeed_update()) {
                                //设置是否强制更新
                                downloadBuilder.setForceUpdateListener(new ForceUpdateListener() {
                                    @Override
                                    public void onShouldForceUpdate() {
                                        LogUtils.e("强制更新 ，取消了");
                                    }
                                });
                            }
                            //设置下载路径
                            downloadBuilder.setDownloadAPKPath(MyUtlis.getAppFileDirPath() + "/");
                            //设置有缓存也重新下载
                            downloadBuilder.setForceRedownload(true);
                            downloadBuilder.setCustomVersionDialogListener(createCustomDialogTwo(response.body().getData().isNeed_update()));
                            downloadBuilder.setCustomDownloadingDialogListener(createCustomDownloadingDialog());
//                            downloadBuilder.setCustomDownloadFailedListener(createCustomDownloadFailedDialog());
                            downloadBuilder.setOnCancelListener(new OnCancelListener() {
                                @Override
                                public void onCancel() {
                                    if (response.body().getData().isNeed_update()) {
                                        //强制时取消更新
                                        AppUtils.exitApp();
                                    } else {
                                        ToastUtils.showShort("取消更新");
                                    }

                                }
                            });
                            downloadBuilder.excuteMission(activity);
                        } else {
                            //服务端版本 等于或者小于当前版本,不更新
                            LogUtils.e("不需要更新");
                        }


                    }

                }
            }
        });


    }

    public static CustomDownloadFailedListener createCustomDownloadFailedDialog() {


        return new CustomDownloadFailedListener() {
            @Override
            public Dialog getCustomDownloadFailed(Context context, UIData versionBundle) {
                BaseDialog baseDialog = new BaseDialog(context, R.style.BaseDialog, R.layout.custom_download_failed_dialog);
                return baseDialog;
            }
        };
    }

    public static CustomVersionDialogListener createCustomDialogTwo(final boolean need_update) {
        return new CustomVersionDialogListener() {
            @Override
            public Dialog getCustomVersionDialog(Context context, UIData versionBundle) {
                final BaseDialog baseDialog = new BaseDialog(context, R.style.BaseDialog, R.layout.custom_dialog_update_layout);
                TextView textView = baseDialog.findViewById(R.id.tv_msg);
                textView.setText(versionBundle.getContent());
                TextView tv_title = baseDialog.findViewById(R.id.tv_title);
                tv_title.setText(versionBundle.getTitle());
                baseDialog.setCanceledOnTouchOutside(false);
                baseDialog.setCancelable(false);

                View btn_cancel = baseDialog.findViewById(R.id.btn_cancel);

                if (need_update) {
                    //强制更新
                    btn_cancel.setVisibility(View.GONE);
                } else {
                    btn_cancel.setVisibility(View.VISIBLE);
                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            baseDialog.dismiss();
                        }
                    });
                }

                return baseDialog;
            }
        };

    }


    /**
     * 自定义下载中对话框，下载中会连续回调此方法 updateUI
     * 务必用库传回来的context 实例化你的dialog
     *
     * @return
     */
    public static CustomDownloadingDialogListener createCustomDownloadingDialog() {
        return new CustomDownloadingDialogListener() {
            @Override
            public Dialog getCustomDownloadingDialog(Context context, int progress, UIData versionBundle) {
                BaseDialog baseDialog = new BaseDialog(context, R.style.BaseDialog, R.layout.custom_download_layout);
                baseDialog.setCancelable(false);
                baseDialog.setCanceledOnTouchOutside(false);
                return baseDialog;
            }

            @Override
            public void updateUI(Dialog dialog, int progress, UIData versionBundle) {
                TextView tvProgress = dialog.findViewById(R.id.tv_progress);
                ProgressBar progressBar = dialog.findViewById(R.id.pb);
                progressBar.setProgress(progress);
                tvProgress.setText(Utils.getApp().getString(R.string.versionchecklib_progress, progress));
            }
        };
    }


}
