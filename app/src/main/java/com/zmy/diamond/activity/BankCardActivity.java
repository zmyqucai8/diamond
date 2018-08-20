package com.zmy.diamond.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.zmy.diamond.R;
import com.zmy.diamond.adapter.BankCardAdapter;
import com.zmy.diamond.base.MyBaseSwipeBackActivity;
import com.zmy.diamond.utli.ApiUtlis;
import com.zmy.diamond.utli.AppConstant;
import com.zmy.diamond.utli.JsonCallBack;
import com.zmy.diamond.utli.MessageEvent;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.BankCardBean;
import com.zmy.diamond.utli.bean.PublicResponseBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 银行卡页面
 * Created by zhangmengyun on 2018/6/13.
 */

public class BankCardActivity extends MyBaseSwipeBackActivity {

    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    BankCardAdapter mAdapter;

    @Override
    public void initUI() {
        setContentView(R.layout.activity_back_card);
        super.initUI();

        EventBus.getDefault().register(this);
        tv_back.setTypeface(MyUtlis.getTTF());
        tv_title.setText(getString(R.string.manage_bank_card));
        tv_right.setText(getString(R.string.ttf_add));
        tv_right.setTextSize(25);
        tv_right.setTypeface(MyUtlis.getTTF());
        tv_right.setVisibility(View.VISIBLE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new BankCardAdapter(new ArrayList<BankCardBean.DataBean>());
//        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mAdapter.addFooterView(getBankCardView());
        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int adpterPosition) {
                new AlertView(getString(R.string.hint_text), "卡号：" + mAdapter.getData().get(adpterPosition).getCard() + "\n你想解绑这张银行卡吗？", null, new String[]{"解绑"}, new String[]{"取消"}, BankCardActivity.this,
                        AlertView.Style.Alert, new com.bigkoo.alertview.OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, final int position) {
                        if (position == 0) {

                            ApiUtlis.walletUnBinDingBankCard(BankCardActivity.this,MyUtlis.getToken(), mAdapter.getData().get(adpterPosition).getId(), new JsonCallBack<PublicResponseBean>(PublicResponseBean.class) {
                                @Override
                                public void onSuccess(Response<PublicResponseBean> response) {
                                    if (null != response.body()) {
                                        if (response.body().getCode() == AppConstant.CODE_SUCCESS) {
                                            MyUtlis.showShortYes(BankCardActivity.this, response.body().getMsg());
//                                            MyUtlis.eventAddBankCardOK();
                                            mAdapter.remove(adpterPosition);

                                        } else {
                                            MyUtlis.showShortNo(BankCardActivity.this, response.body().getMsg());
                                        }
                                    }

                                }

                                @Override
                                public void onStart(Request<PublicResponseBean, ? extends Request> request) {
                                    super.onStart(request);
                                    showLoading();
                                }

                                @Override
                                public void onFinish() {
                                    super.onFinish();
                                    hideLoading();
                                }
                            });


                        }

                    }
                }).show();

                return true;

            }
        });
        recyclerView.setAdapter(mAdapter);
    }


//    UserBean user;

    @Override
    public void initData() {
//        user = DaoUtlis.getCurrentLoginUser();


        ApiUtlis.walletBankCardList(BankCardActivity.this,MyUtlis.getToken(), new JsonCallBack<BankCardBean>(BankCardBean.class) {
            @Override
            public void onSuccess(Response<BankCardBean> response) {
                if (null != response.body()) {
                    if (response.body().getCode() == AppConstant.CODE_SUCCESS && null != response.body().getData()) {
                        //银行卡数据
                        mAdapter.setNewData(response.body().getData());
                    }
                }
            }

            @Override
            public void onStart(Request<BankCardBean, ? extends Request> request) {
                super.onStart(request);
                showLoading(false);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoading();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.eventType == MessageEvent.ADD_BANK_CARD_OK) {
            LogUtils.e("刷新银行卡列表");
            initData();
        }
    }

    /**
     * 获取vipheadview
     *
     * @return
     */
    private View getBankCardView() {
        View footerView = View.inflate(BankCardActivity.this, R.layout.view_bank_card_footer, null);
        TextView tv_ttf_add = footerView.findViewById(R.id.tv_ttf_add);
        tv_ttf_add.setTypeface(MyUtlis.getTTF());

        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BankCardAddActivity.start(BankCardActivity.this);

            }
        });
        return footerView;

    }

    public static void start(Context context) {
        Intent intent = new Intent(context, BankCardActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.tv_back)
    public void tv_back() {
        ActivityUtils.finishActivity(this, true);
    }

    @OnClick(R.id.tv_right)
    public void tv_right() {
        BankCardAddActivity.start(BankCardActivity.this);
    }

}
