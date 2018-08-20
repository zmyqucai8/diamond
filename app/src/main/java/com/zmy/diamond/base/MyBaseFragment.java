package com.zmy.diamond.base;

import com.king.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 所有的fragment都要继承此类
 * Created by zhangmengyun on 2018/6/11.
 */

public abstract class MyBaseFragment extends BaseFragment {
    Unbinder bind;

    @Override
    public void initUI() {
        bind = ButterKnife.bind(this, rootView);
//        LogUtils.e("绑定fragment");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

    @Override
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageStart("");
    }

    @Override
    public void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
    }


    public void showLoading() {

        if(getActivity() instanceof  MyBaseActivity){
            ((MyBaseActivity) getActivity()).showLoading();
        }

    }

    public void hideLoading() {
        if(getActivity() instanceof  MyBaseActivity){
            ((MyBaseActivity) getActivity()).hideLoading();
        }

    }
}
