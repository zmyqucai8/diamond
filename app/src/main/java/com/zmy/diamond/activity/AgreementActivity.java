package com.zmy.diamond.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.zmy.diamond.R;
import com.zmy.diamond.base.MyBaseSwipeBackActivity;
import com.zmy.diamond.utli.AppConstant;
import com.zmy.diamond.utli.MyUtlis;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 协议页面
 * Created by zhangmengyun on 2018/6/11.
 */

public class AgreementActivity extends MyBaseSwipeBackActivity {

    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.rootview)
    View rootView;

    String content = "【软件功能声明】\n" +
            "   本软件仅用于采集相关行业网站页面公开资源（仅对公开信息的复制、统计和整计），所有数据均来源于互联网相关网站，软件通过网络蜘蛛技术实时抓取，数据内容全部为网民自行公布在相关网站上的公开信息，不涉及任何个人隐私问题，软件后台也无缓存任何个人信息数据，数据来源可以在对应目标官方网站中查看，本软件以及官方严格遵守国家法律法规，本软件只在合法范围内开发、使用，切勿用于违法用途！\n" +
            "\n" +
            "【使用须知】-必读！\n" +
            "1、本软件仅对公开数据复制、归类和整理，不涉及个人隐私。\n" +
            "2、本软件推出旨在于辅助大数据时代的数据整合、搜索、决策等方面提供便捷化的工具，目的在于提高工作效率，助力互联网大数据时代各行业企业的业务推广。\n" +
            "3、本软件遵守国家法律法规，所有数据均来源于互联网相关网站抓取，全部为公开的数据信息，不存在任何涉及个人隐私的内容。\n" +
            "4、若想获取公民个人隐私信息的，请自觉绕道，本软件不符合也不适用，请立即关闭并卸载本软件！\n" +
            "5、切勿用我们软件采集信息后从事信息篡改、倒卖、电信诈骗等违法犯罪活动，否则后果自负！一经发现，我们会第一时间向公安部门举报并配合调查！\n" +
            "6、切勿将软件用于其他不合理用途或其他违法用途,否则如有造成任何纠纷，后果自负！本软件以及官方不承担任何责任！\n" +
            "7、如本软件给您造成某些困扰或不便，请告知我们，我们将提供您解决渠道或从技术层面进行协助排除。\n" +
            "\n" +
            "友情提示：任何公民信息的倒卖均属违法行为！请自觉遵守相关法律法规！\n" +
            "上述事项，使用本软件请提前知晓并自觉遵守，同时也默认您同意上述内容。\n" +
            "让我们同营造干净、和谐的互联网环境，感谢您的支持与配合！谢谢！";

    @Override
    public void initUI() {
        setContentView(R.layout.activity_regist_agreement);
        super.initUI();
        tv_content.setText(content);
        tv_back.setTypeface(MyUtlis.getTTF());
        tv_title.setText(getString(R.string.title_agreement));

        if (getIntent().getBooleanExtra(AppConstant.ExtraKey.IS_SHOW_BLUE_ACTIONBAR, false)) {
            //显示蓝底白字 18sp
            rootView.setBackgroundColor(ContextCompat.getColor(this, R.color.color_logo));
            tv_back.setTextColor(ContextCompat.getColor(this, R.color.select_text_white_color));
            tv_title.setTextColor(Color.WHITE);
//            tv_title.setTextSize(18);
        } else {
            //默认
        }
    }

    @Override
    public void initData() {

    }

    /**
     * @param context
     * @param isShowBlueActionBar 是否显示蓝色actionbar ,默认白底黑字
     */
    public static void start(Context context, boolean isShowBlueActionBar) {
        Intent intent = new Intent(context, AgreementActivity.class);
        intent.putExtra(AppConstant.ExtraKey.IS_SHOW_BLUE_ACTIONBAR, isShowBlueActionBar);
        context.startActivity(intent);
    }

    @OnClick(R.id.tv_back)
    public void tv_back() {
        ActivityUtils.finishActivity(this, true);
    }
}
