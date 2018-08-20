package com.zmy.diamond.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.zmy.diamond.R;
import com.zmy.diamond.base.MyBaseSwipeBackActivity;
import com.zmy.diamond.utli.MyUtlis;

import butterknife.BindView;

/**
 * Created by zhangmengyun on 2018/7/28.
 */

public class TestActivity extends MyBaseSwipeBackActivity {
    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;

    public static void start(Context context) {
        Intent intent = new Intent(context, TestActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void initUI() {
        setContentView(R.layout.activity_test);
        super.initUI();
        tv_back.setTypeface(MyUtlis.getTTF());
        tv_title.setText("测试");

    }

    @Override
    public void initData() {


    }


}
