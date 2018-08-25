package com.zmy.diamond.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zmy.diamond.R;
import com.zmy.diamond.adapter.MarketingDataSelectAdapter;
import com.zmy.diamond.base.MyBaseSwipeBackActivity;
import com.zmy.diamond.utli.AppConstant;
import com.zmy.diamond.utli.MessageEvent;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.CollectRecordBean;
import com.zmy.diamond.utli.bean.DataBean;
import com.zmy.diamond.utli.bean.MarketingDataSelectGroupBean;
import com.zmy.diamond.utli.bean.MarketingDataSelectSingleBean;
import com.zmy.diamond.utli.dao.DaoUtlis;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zmy.diamond.adapter.MarketingDataSelectAdapter.TYPE_SELECT;
import static com.zmy.diamond.utli.AppConstant.SHOW_TYPE_ALL;
import static com.zmy.diamond.utli.AppConstant.SHOW_TYPE_PHONE;
import static com.zmy.diamond.utli.AppConstant.SHOW_TYPE_TEL;


/**
 * 营销列表，数据 选择页面
 * type =  0=全部 1=手机 2=固话 默认0 ，
 * 单选多选？ 暂只支持多选，不考虑单选情况。
 * 已选数据回显？需要的。
 * Created by zhangmengyun on 2018/6/26.
 */

public class MarketingDataSelectActivity extends MyBaseSwipeBackActivity {

    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_select_count)
    TextView tv_select_count;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    //电话数据类型 0全部1手机2固话
    int type;


    //选择数量
    int selectCount;


    //之前选择的数据
    // 如果回显的是组= key+city ,  如果回显的是单个数据= getPlatformId=dataId;
    //使用&&&拼接
    String beforeDataStr;

    MarketingDataSelectAdapter mAdapter;

    @Override
    public void initUI() {
        setContentView(R.layout.activity_marketing_data_select);
        super.initUI();
        tv_title.setText(getString(R.string.text_select_data));
        tv_back.setTypeface(MyUtlis.getTTF());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new MarketingDataSelectAdapter(new ArrayList<MultiItemEntity>(), TYPE_SELECT);
        mAdapter.setOnSelectListener(new MarketingDataSelectAdapter.OnSelectListener() {
            @Override
            public void onSelect() {
                selectCount++;
                updateSelectCount();
            }

            @Override
            public void onUnSelect() {
                selectCount--;
                updateSelectCount();
            }


        });
        mAdapter.setEmptyView(MyUtlis.getEmptyView(this, "暂无数据\n快去采集吧"));
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 更新选择数量
     */
    private void updateSelectCount() {
        tv_select_count.setText(getString(R.string.text_select_count, selectCount));
    }


    /**
     * 获取数据库里的采集数据
     *
     * @return
     */
    private List<MultiItemEntity> getMarketingData() {

        List<MultiItemEntity> res = new ArrayList<>();
        //1.先获取营销列表数据

        List<CollectRecordBean> collectRecordList = DaoUtlis.getCollectRecord(MyUtlis.getLoginUserId());


        for (int i = 0; i < collectRecordList.size(); i++) {

            CollectRecordBean collectRecordBean = collectRecordList.get(i);
            //2.去获取列表中的每一条数据
            List<DataBean> dataBeanList = DaoUtlis.getDataByCityKey(collectRecordBean.getPlatformId(), collectRecordBean.getKey(), collectRecordBean.getCity(), collectRecordBean.getUserId());


            MarketingDataSelectGroupBean groupBean = new MarketingDataSelectGroupBean();
            groupBean.name = collectRecordBean.city.replace("市", "") + collectRecordBean.getKey();
//            groupBean.size = dataBeanList.size();

            //todo:已选数据设置
            groupBean.isCheck = false;


            //子列表数据设置

            for (int x = 0; x < dataBeanList.size(); x++) {
                DataBean dataBean = dataBeanList.get(x);
                String tel = dataBean.getTel();
                String phone = dataBean.getPhone();
                //过滤数据
                switch (type) {

                    case SHOW_TYPE_ALL:
                        //有1个就行
                        if (TextUtils.isEmpty(tel) && TextUtils.isEmpty(phone)) {
                            continue;
                        }
                        break;
                    case SHOW_TYPE_PHONE:
                        //必须有手机
                        if (TextUtils.isEmpty(phone)) {
                            continue;
                        }
                        break;
                    case AppConstant.SHOW_TYPE_TEL:
                        //必须有固话
                        if (TextUtils.isEmpty(tel)) {
                            continue;
                        }
                        break;

                }
                MarketingDataSelectSingleBean singleBean = new MarketingDataSelectSingleBean();
                singleBean.name = dataBean.name;
                singleBean.phone = type == SHOW_TYPE_PHONE ? dataBean.getPhone() : type == SHOW_TYPE_TEL ? dataBean.getTel() : !TextUtils.isEmpty(dataBean.getTel()) ? dataBean.getTel() : dataBean.getPhone();
                singleBean.address = dataBean.address;
                //todo:已选处理
                singleBean.isCheck = false;

                groupBean.addSubItem(singleBean);
            }


            if (null != groupBean.getSubItems() && groupBean.getSubItems().size() > 0) {
                groupBean.size = groupBean.getSubItems().size();
                res.add(groupBean);
            }


        }


        return res;
    }


    @Override
    public void initData() {

        type = getIntent().getIntExtra(AppConstant.ExtraKey.DATA_TYPE, SHOW_TYPE_ALL);

        beforeDataStr = getIntent().getStringExtra(AppConstant.ExtraKey.DATA);


        ThreadUtils.executeByFixed(AppConstant.THREAD_SIZE, new ThreadUtils.SimpleTask<List<MultiItemEntity>>() {
            @Override
            public List<MultiItemEntity> doInBackground() {
                return getMarketingData();
            }

            @Override
            public void onSuccess(List<MultiItemEntity> result) {
                mAdapter.setNewData(result);
            }
        });
    }


    /**
     * @param context
     * @param type           电话数据类型过滤
     * @param beforeDataStr: // 如果回显的是组= key+city ,  如果回显的是单个数据= getPlatformId=dataId;
     *                       //使用&&&拼接
     */
    public static void start(Context context, int type, String beforeDataStr) {
        Intent intent = new Intent(context, MarketingDataSelectActivity.class);
        intent.putExtra(AppConstant.ExtraKey.DATA_TYPE, type);
        intent.putExtra(AppConstant.ExtraKey.DATA, beforeDataStr);
        context.startActivity(intent);
    }

    @OnClick(R.id.tv_back)
    public void tv_back() {
        ActivityUtils.finishActivity(this, true);
    }

    @OnClick(R.id.btn_complete)
    public void btn_complete() {
//        setResult(Activity.RESULT_OK,);
        //这里不雅result传值，而是使用event


        List<MultiItemEntity> data = mAdapter.getData();
        List<MultiItemEntity> retrunData = new ArrayList<>();

        //选中的数据返回， 没有选中的过滤掉

        for (MultiItemEntity entity : data) {
            if (entity.getItemType() == MarketingDataSelectAdapter.TYPE_LEVEL_GROUP) {
                MarketingDataSelectGroupBean groupBean = (MarketingDataSelectGroupBean) entity;
                MarketingDataSelectGroupBean retrunGroupBean = new MarketingDataSelectGroupBean();
                retrunGroupBean.name = groupBean.name;
                retrunGroupBean.isCheck = groupBean.isCheck;

                //如果组下面有选中，则返回该组所有选中的数据
                List<MarketingDataSelectSingleBean> singleBeanList = groupBean.getSubItems();
                for (MarketingDataSelectSingleBean singleBean : singleBeanList) {
                    if (singleBean.isCheck) {
                        //选中添加
                        retrunGroupBean.addSubItem(singleBean);
                    }
                }
                //循环完毕判断组里是否有选中的数据
                if (null != retrunGroupBean.getSubItems() && retrunGroupBean.getSubItems().size() > 0) {
                    retrunGroupBean.size = retrunGroupBean.getSubItems().size();
                    retrunData.add(retrunGroupBean);
                }
            }
        }

        if (retrunData.size() == 0) {
            ToastUtils.showShort("你还未选择数据");
            return;
        }

        MessageEvent event = new MessageEvent();
        event.eventType = MessageEvent.SELECT_MARKETING_DATA_COMPLETE;
        event.objectValue = retrunData;
        EventBus.getDefault().post(event);
        ActivityUtils.finishActivity(this, true);
    }

    @OnClick(R.id.btn_cancel)
    public void btn_cancel() {
        ActivityUtils.finishActivity(this, true);
    }


}
