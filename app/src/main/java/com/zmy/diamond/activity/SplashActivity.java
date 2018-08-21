package com.zmy.diamond.activity;

import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.zmy.diamond.MainActivity;
import com.zmy.diamond.R;
import com.zmy.diamond.base.MyBaseActivity;
import com.zmy.diamond.utli.ApiUtlis;
import com.zmy.diamond.utli.AppConstant;
import com.zmy.diamond.utli.JsonCallBack;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.LoginResponseBean;
import com.zmy.diamond.utli.dao.DaoUtlis;
import com.zmy.diamond.utli.view.MyAlertView;

import java.util.List;

import butterknife.BindView;

/**
 * 启动页
 * Created by zhangmengyun on 2018/6/11.
 */

public class SplashActivity extends MyBaseActivity {
    @BindView(R.id.tv_ttf_logo)
    TextView tv_ttf_logo;

    @Override
    public void initUI() {
        MyUtlis.huaweiSplash(this);
        setContentView(R.layout.activity_splash);
        super.initUI();
        tv_ttf_logo.setTypeface(MyUtlis.getTTF());
    }

    MyAlertView myAlertView;

    @Override
    public void initData() {
//        MyUtlis.getInfoData(null);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppConstant.DEBUG) {


            if (null == myAlertView) {
                View view = View.inflate(this, R.layout.view_debug_mode, null);

                final EditText edit_view = (EditText) view.findViewById(R.id.edit_view);

                String tip = "1.请输入正确的ip+port\n2.如果不输入,默认为:\nhttp://139.199.154.179:8080";
                myAlertView = new MyAlertView("Debug模式", tip, null, new String[]{getString(R.string.hint_confirm)}, null, this,
                        MyAlertView.Style.Alert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {

                        String text = edit_view.getText().toString().trim();

                        if (TextUtils.isEmpty(text)) {
                            myAlertView.dismiss();
                            startLogin();
                        } else if (!text.startsWith("http://")) {
                            ToastUtils.showShort("请输入有效的地址");
                        } else {
                            AppConstant.HOST = text;
                            myAlertView.dismiss();
                            startLogin();
                        }

                    }
                }).addExtView(view);

            }
            if (!myAlertView.isShowing())
                myAlertView.show();


        } else {
            startLogin();
        }

    }

    public void startLogin() {

        //权限检测
        PermissionUtils.permission(PermissionConstants.PHONE, PermissionConstants.LOCATION, PermissionConstants.STORAGE)
                .rationale(new PermissionUtils.OnRationaleListener() {
                    @Override
                    public void rationale(final ShouldRequest shouldRequest) {
                        MyUtlis.showRationaleDialog(shouldRequest);
                    }
                })
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        //开启定位服务
                        SDKInitializer.initialize(Utils.getApp());
                        SDKInitializer.setCoordType(CoordType.BD09LL);
                        MyUtlis.startLocatinService();
                        //请求登录接口
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                login();
                            }
                        }, 100);
                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever,
                                         List<String> permissionsDenied) {
                        if (!permissionsDeniedForever.isEmpty()) {
                            MyUtlis.showOpenAppSettingDialog();
                        }

                    }
                })
                .request();

    }


    /**
     * 登录
     */
    private void login() {
        ApiUtlis.login(this, new JsonCallBack<LoginResponseBean>(LoginResponseBean.class) {
            @Override
            public void onSuccess(Response<LoginResponseBean> response) {
                if (null != response.body()) {
                    if (response.body().getCode() == AppConstant.CODE_SUCCESS && null != DaoUtlis.getCurrentLoginUser()) {
//                        //更新本地用户信息
                        boolean isShowGuide = SPUtils.getInstance().getBoolean(AppConstant.SPKey.IS_SHOW_GUIDE_PAGE);
                        if (AppConstant.DEBUG) {
                            isShowGuide = false;
                        }
                        if (!isShowGuide && AppConstant.isGuide) {
                            GuideActivity.start(SplashActivity.this);

                        } else {
                            MainActivity.start(SplashActivity.this);
                        }
                        ActivityUtils.finishActivity(SplashActivity.this);
                    } else {
                        showLoginError("\ncode=" + response.body().getCode() + "\nmsg=" + response.body().getMsg());
                    }
                } else {
                    showLoginError("");
                }
            }

            @Override
            public void onStart(Request<LoginResponseBean, ? extends Request> request) {
                super.onStart(request);
                showLoading();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoading();
            }
        });

//                                String last_login_user = SPUtils.getInstance().getString(AppConstant.SPKey.LAST_LOGIN_USER);
////获取最后一个登录的用户, 进行验证,判断是跳转到主页,还是登录页.
//                                if (!TextUtils.isEmpty(last_login_user)) {
//                                    UserBean user = DaoUtlis.getUser(last_login_user);
//                                    if (null != user) {
//                                        MainActivity.start(SplashActivity.this);
//                                    } else {
//                                        LoginActivity.start(SplashActivity.this);
//                                    }
//                                } else {
//                                    LoginActivity.start(SplashActivity.this);
//                                }
//                                ActivityUtils.finishActivity(SplashActivity.this, true);

    }


    public void showLoginError(String info) {
        new AlertView("提示", "登录错误，请稍后重试" + info, null, new String[]{"好的"}, null, SplashActivity.this,
                AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                ActivityUtils.finishActivity(SplashActivity.this);
            }
        }).show();
    }

    @Override
    public void addListeners() {

    }
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//
//        try {
//            if (null != myAlertView && !myAlertView.isShowing()) {
//                initData();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    @Override
    public void onBackPressed() {
        ActivityUtils.startHomeActivity();
    }
}
