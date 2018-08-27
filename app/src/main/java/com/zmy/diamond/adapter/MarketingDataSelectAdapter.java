package com.zmy.diamond.adapter;

import android.view.View;
import android.widget.CheckBox;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zmy.diamond.R;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.MarketingDataSelectGroupBean;
import com.zmy.diamond.utli.bean.MarketingDataSelectSingleBean;

import java.util.List;

/**
 * 营销数据选择器
 * int type  ： 1= 选择模式 2=删除模式
 * <p>
 * Created by zhangmengyun on 2018/8/25.
 */

public class MarketingDataSelectAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public static final int TYPE_LEVEL_GROUP = 1;
    public static final int TYPE_LEVEL_SINGLE = 2;

    public static final int TYPE_DELETE = 2;
    public static final int TYPE_SELECT = 1;

    //1= 选择模式 2=删除模式，默认选择
    public int mType = TYPE_SELECT;


    public OnSelectListener mSelectListener;
    public OnDeleteListener mDeleteListener;

    public interface OnSelectListener {
        void onSelect();

        void onUnSelect();
    }

    public interface OnDeleteListener {
        void onDelete();
    }

    public void setOnSelectListener(OnSelectListener listener) {
        this.mSelectListener = listener;
    }

    public void setOnDeleteListener(OnDeleteListener listener) {
        this.mDeleteListener = listener;
    }


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MarketingDataSelectAdapter(List<MultiItemEntity> data, int type) {
        super(data);
        this.mType = type;
        addItemType(TYPE_LEVEL_GROUP, R.layout.item_markeeting_data_select_group);
        addItemType(TYPE_LEVEL_SINGLE, R.layout.item_markeeting_data_select_single);

    }


    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {


        switch (helper.getItemViewType()) {

            case TYPE_LEVEL_GROUP:
                //1组数据
                final MarketingDataSelectGroupBean groupBean = (MarketingDataSelectGroupBean) item;
                //设置UI
                StringBuilder sb = new StringBuilder();
                sb.append(groupBean.name);
                sb.append("  【");
                sb.append(null != groupBean.getSubItems() ? groupBean.getSubItems().size() : groupBean.size);
                sb.append("人】");
                helper.setText(R.id.tv_name, sb.toString());

                //设置展开按钮
                helper.setText(R.id.tv_more, groupBean.isExpanded() ? mContext.getString(R.string.ttf_down2) : mContext.getString(R.string.ttf_right2))
                        .setTypeface(R.id.tv_more, MyUtlis.getTTF());

                helper.setTypeface(R.id.tv_delete, MyUtlis.getTTF());

                //判断模式
                if (mType == TYPE_DELETE) {
                    //删除模式：隐藏checkbox 显示delete,下拉箭头一直显示

                    helper
                            .setGone(R.id.checkbox, false)
                            .setGone(R.id.tv_delete, true);
                } else {
                    //选择模式：显示checkbox ，隐藏delete ， 下拉箭头一直显示
                    helper
                            .setGone(R.id.checkbox, true)
                            .setGone(R.id.tv_delete, false);
                    ((CheckBox) helper.getView(R.id.checkbox)).setChecked(groupBean.isCheck);
                }

                //设置点击事件
                helper.getView(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            //删除
//                            ToastUtils.showShort("删除整组=" + groupBean.name);

                            remove(helper.getAdapterPosition());
                            if (null != mDeleteListener) {
                                mDeleteListener.onDelete();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtils.e("暴力快速点击删除异常");
                        }

                    }
                });

                helper.getView(R.id.tv_more).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (groupBean.isExpanded()) {
                            //展开就关闭
                            collapse(helper.getAdapterPosition());
                        } else {
                            //关闭就展开
                            expand(helper.getAdapterPosition());
                        }
                    }
                });
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (mType == TYPE_SELECT) {
                            //组 选择模式
                            groupBean.isCheck = !groupBean.isCheck;
                            ((CheckBox) helper.getView(R.id.checkbox)).setChecked(groupBean.isCheck);


                            //组选中，选中全部子
                            //组取消选中，取消选中全部子
                            List<MarketingDataSelectSingleBean> subItems = groupBean.getSubItems();
                            for (int i = 0; i < subItems.size(); i++) {
                                if (subItems.get(i).isCheck != groupBean.isCheck) {
                                    updateSelectCount(groupBean.isCheck);
                                }
                                subItems.get(i).isCheck = groupBean.isCheck;

                                notifyDataSetChanged();
                            }
                        }else {
                            //组 删除模式点击
                            if (groupBean.isExpanded()) {
                                //展开就关闭
                                collapse(helper.getAdapterPosition());
                            } else {
                                //关闭就展开
                                expand(helper.getAdapterPosition());
                            }
                        }
//                        ToastUtils.showShort(groupBean.name);

                    }
                });

                break;
            case TYPE_LEVEL_SINGLE:
                //单个数据
                final MarketingDataSelectSingleBean singleBean = (MarketingDataSelectSingleBean) item;
                //设置UI

                helper.setText(R.id.tv_name, singleBean.name);
                helper.setText(R.id.tv_phone, singleBean.phone);
                helper.setText(R.id.tv_address, singleBean.address);
                helper.setTypeface(R.id.tv_delete, MyUtlis.getTTF());

                //判断模式
                if (mType == TYPE_DELETE) {
                    //删除模式：隐藏checkbox 显示delete

                    helper
                            .setGone(R.id.checkbox, false)
                            .setGone(R.id.tv_delete, true);
                } else {
                    //选择模式：显示checkbox ，隐藏delete
                    helper
                            .setGone(R.id.checkbox, true)
                            .setGone(R.id.tv_delete, false);
                    ((CheckBox) helper.getView(R.id.checkbox)).setChecked(singleBean.isCheck);
                }

                //设置点击事件
                helper.getView(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            //删除
//                            ToastUtils.showShort("删除单个=" + singleBean.name);

// 获取当前父级数据
                            int groupPosition = getParentPosition(singleBean);
                            MarketingDataSelectGroupBean groupBean = (MarketingDataSelectGroupBean) getData().get(groupPosition);
                            //如果父级数据大于1，删除单个，否则删除整组
                            if (groupBean.getSubItems().size() > 1) {
                                //删除单
                                // 通过父级位置找到当前list，删除指定下级
//                            groupBean.removeSubItem(singleBean);
                                remove(helper.getAdapterPosition());
                                //更新组数量
                                notifyItemChanged(groupPosition);

                            } else {
                                //删除组
                                remove(groupPosition);
                            }


                            if (null != mDeleteListener) {
                                mDeleteListener.onDelete();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtils.e("暴力快速点击删除异常");

                        }
                    }
                });


                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (mType == TYPE_SELECT) {
                            singleBean.isCheck = !singleBean.isCheck;
                            ((CheckBox) helper.getView(R.id.checkbox)).setChecked(singleBean.isCheck);
                            updateSelectCount(singleBean.isCheck);
                        }


                        int groupPosition = getParentPosition(singleBean);
                        MarketingDataSelectGroupBean groupBean = (MarketingDataSelectGroupBean) getData().get(groupPosition);

                        if (singleBean.isCheck) {
                            //子选中， 选中组
                            groupBean.isCheck = singleBean.isCheck;
                            notifyItemChanged(groupPosition);
//                            notifyDataSetChanged();
                        } else {
                            //子取消， 判断子中是否有选中，如果没有了，取消组选中
                            boolean isGroupBeanCheck = false;
                            for (int i = 0; i < groupBean.getSubItems().size(); i++) {
                                if (groupBean.getSubItems().get(i).isCheck) {
                                    isGroupBeanCheck = true;
                                    break;
                                }
                            }
                            groupBean.isCheck = isGroupBeanCheck;
                            notifyItemChanged(groupPosition);

                        }


//                        ToastUtils.showShort(singleBean.name);
                    }
                });

                break;

        }

    }

    /**
     * 在选中或者未选中时， 更新选中数量
     *
     * @param isCheck
     */
    public void updateSelectCount(boolean isCheck) {
        if (null == mSelectListener) {
            return;
        }
        if (isCheck) {
            mSelectListener.onSelect();
        } else {
            mSelectListener.onUnSelect();
        }
    }


}
