package com.zmy.diamond.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.zmy.diamond.R;
import com.zmy.diamond.base.MyBaseSwipeBackActivity;
import com.zmy.diamond.utli.ApiUtlis;
import com.zmy.diamond.utli.AppConstant;
import com.zmy.diamond.utli.JsonCallBack;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.PublicResponseBean;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 意见反馈页面
 * Created by zhangmengyun on 2018/6/13.
 */

public class OpinionActivity extends MyBaseSwipeBackActivity {
    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.edit_view)
    EditText edit_view;


    @Override
    public void initUI() {
        setContentView(R.layout.activity_opinion_feedback);
        super.initUI();
        tv_back.setTypeface(MyUtlis.getTTF());
        tv_title.setText(getString(R.string.title_opinion_feedback));
    }

    @Override
    public void initData() {


    }


    @BindView(R.id.btn_commit)
    Button btn_commit;

    @OnClick(R.id.btn_commit)
    public void btn_commit() {
//        MyUtlis.showShort(this, "提交");
        String message = edit_view.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            MyUtlis.showShort(this, "内容不能为空");
            return;
        }
        ApiUtlis.opinion(OpinionActivity.this, message, MyUtlis.getToken(), new JsonCallBack<PublicResponseBean>(PublicResponseBean.class) {
            @Override
            public void onSuccess(Response<PublicResponseBean> response) {
                if (null != response.body()) {
                    MyUtlis.showShort(OpinionActivity.this, response.body().getMsg());
                    if (response.body().getCode() == AppConstant.CODE_SUCCESS) {
                        tv_back();
//                        ActivityUtils.finishActivity(OpinionActivity.this);
                    }
                }
            }

            @Override
            public void onStart(Request<PublicResponseBean, ? extends Request> request) {
                super.onStart(request);
//                showLoading();
                btn_commit.setClickable(false);
            }

            @Override
            public void onFinish() {
                super.onFinish();
//                hideLoading();
                btn_commit.setClickable(true);
            }
        });

    }

    @OnClick(R.id.tv_back)
    public void tv_back() {
        KeyboardUtils.hideSoftInput(this);
        ActivityUtils.finishActivity(this, true);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, OpinionActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
