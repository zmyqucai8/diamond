package com.zmy.diamond.base;

import com.gyf.barlibrary.ImmersionBar;
import com.king.base.BaseActivity;
import com.lzy.okgo.OkGo;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.zmy.diamond.R;
import com.zmy.diamond.utli.view.loading_dialog.LoadingDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 不能侧滑返回的BaseActivity
 * Created by zhangmengyun on 2018/6/11.
 */

public abstract class MyBaseActivity extends BaseActivity {
    Unbinder bind;
    protected ImmersionBar mImmersionBar;

    @Override
    public void initUI() {

//        BaseApp.addActivity(this);

        PushAgent.getInstance(context).onAppStart();
        mImmersionBar = ImmersionBar.with(this);

        if (findViewById(R.id.statusBarView) != null) {
            mImmersionBar.statusBarView(findViewById(R.id.statusBarView)).init();
        } else {
            mImmersionBar.init();
        }

        bind = ButterKnife.bind(this);

    }


    @Override
    protected void onDestroy() {
        OkGo.getInstance().cancelTag(this);
        super.onDestroy();
        if (null != bind) {
            bind.unbind();
        }
        if (mImmersionBar != null)
            mImmersionBar.destroy();  //必须调用该方法，防止内存泄漏，不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态


        if (null != mLoadingDialog) {
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
            mLoadingDialog = null;
        }


//        BaseApp.removeActivity(this);
    }


    @Override
    public void addListeners() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }


    public LoadingDialog mLoadingDialog;

    /**
     * 显示loading
     *
     * @param isCancelable 是否可以手动取消
     */
    public void showLoading(boolean isCancelable) {
        if (null == mLoadingDialog) {
            mLoadingDialog = new LoadingDialog(this);
        }
        mLoadingDialog.setCancelable(isCancelable);
        mLoadingDialog.setCanceledOnTouchOutside(isCancelable);
        if (mLoadingDialog.isShowing()) {
            mLoadingDialog.hideLoading();
        }
        mLoadingDialog.showLoading();
    }


    /**
     * 隐藏loading
     */
    public void hideLoading() {
        if (null != mLoadingDialog) {
            mLoadingDialog.hideLoading();
        }
    }

    /**
     * 显示loading 可以手动取消
     */
    public void showLoading() {
        showLoading(true);
    }

}
