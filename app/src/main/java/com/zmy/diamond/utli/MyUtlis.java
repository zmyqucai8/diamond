package com.zmy.diamond.utli;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.ForceUpdateListener;
import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ServiceUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Contacts;
import com.github.tamir7.contacts.Query;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.umeng.analytics.MobclickAgent;
import com.zmy.diamond.R;
import com.zmy.diamond.activity.LoginActivity;
import com.zmy.diamond.service.LocationService;
import com.zmy.diamond.utli.bean.AppVersionBean;
import com.zmy.diamond.utli.bean.DataBean;
import com.zmy.diamond.utli.bean.InfoBannerBean;
import com.zmy.diamond.utli.bean.InfoBean;
import com.zmy.diamond.utli.bean.InfoDataBean;
import com.zmy.diamond.utli.bean.InfoMenuBean;
import com.zmy.diamond.utli.bean.InfoNoticeBean;
import com.zmy.diamond.utli.bean.JsonBean_JuHeNews;
import com.zmy.diamond.utli.bean.KeyListBean;
import com.zmy.diamond.utli.bean.LocationBean;
import com.zmy.diamond.utli.bean.PlatformBean;
import com.zmy.diamond.utli.bean.TradingDataBean;
import com.zmy.diamond.utli.bean.UserBean;
import com.zmy.diamond.utli.bean.VipBean;
import com.zmy.diamond.utli.dao.DaoUtlis;
import com.zmy.diamond.utli.view.loading_button.customViews.CircularProgressButton;
import com.zmy.diamond.utli.view.loading_button.interfaces.OnAnimationEndListener;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import xyz.bboylin.universialtoast.UniversalToast;

/**
 * Created by zhangmengyun on 2018/6/11.
 */

public class MyUtlis {

    //注册成功
    public final static int STATE_REGIST_YES = MyUtlis.STATE_YES;
    //注册失败
    public final static int STATE_REGIST_NO = MyUtlis.STATE_NO;
    //用户以存在
    public final static int STATE_REGIST_USER_REPEAT = MyUtlis.STATE_ERROR;


    //登录成功
    public final static int STATE_LOGIN_YES = MyUtlis.STATE_YES;
    //登录失败
    public final static int STATE_LOGIN_NO = MyUtlis.STATE_NO;
    //账户不存在
    public final static int STATE_LOGIN_NO_USER = MyUtlis.STATE_ERROR;


    //toast状态
    public final static int STATE_YES = 1;
    public final static int STATE_NO = 0;
    public final static int STATE_ERROR = 2;

    /**
     * 金数据:html页面,联系客服
     */
    public static final String HTML_JINSHUJU_contact_service = "https://jinshuju.net/f/aHsNIJ";
    /**
     * 金数据:html页面,意见反馈
     */
    public static final String HTML_JINSHUJU_feedback = "https://jinshuju.net/f/G3RTXK";

    /**
     * 字体库
     */
    public static Typeface mTypeface;

    /**
     * 获取ttf字体库
     *
     * @return
     */
    public static synchronized Typeface getTTF() {
        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(Utils.getApp().getResources().getAssets(), "iconfont.ttf");
        }
        return mTypeface;
    }

    /**
     * 获取一个没有数据时显示的view  ， textview
     *
     * @param act
     * @param str 提示文本
     * @return
     */
    public static View getEmptyView(Context act, String str) {

        if (null == act) {
            act = Utils.getApp();
        }
        LinearLayout linearLayout = new LinearLayout(act);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);
        TextView ttf = new TextView(act);
        ttf.setTextSize(45);
        ttf.setPadding(0, 0, 0, 10);
        ttf.setGravity(Gravity.CENTER);
        ttf.setText(getString(R.string.ttf_no_data_icon));
        ttf.setTypeface(getTTF());
        linearLayout.addView(ttf);
        TextView tv = new TextView(act);
        tv.setText(str);
        tv.setGravity(Gravity.CENTER);
        linearLayout.addView(tv);
        return linearLayout;
    }


    public static String getString(int stringId) {
        if (stringId > 0) {
            return Utils.getApp().getResources().getString(stringId);
        }
        return "";
    }


    /**
     * 获取 关键词 key item 数据
     *
     * @return
     */
    public static List<KeyListBean> getKeyItemData() {

        String[] titles = {"餐饮美食",
                "丽人行业",
                "娱乐休闲",
                "教育培训",
                "日常生活",
                "购物场所",
                "母婴玩具",
                "亲子护理",
                "宠物行业",
                "服饰行业",
                "婚庆行业",
                "食品行业",
                "物流快递",
                "金银首饰",
                "汽车行业",
                "数码行业",
                "建材装修",
                "医疗保健",
                "酒店公寓",
                "旅游景点"};
        String[] title_ttf = {
                getString(R.string.key_title_chihe),
                getString(R.string.key_title_liren),
                getString(R.string.key_title_yule),
                getString(R.string.key_title_jiaoyu),
                getString(R.string.key_title_shenghuo),
                getString(R.string.key_title_gouwu),
                getString(R.string.key_title_muying),
                getString(R.string.key_title_qinzi),
                getString(R.string.key_title_chongwu),
                getString(R.string.key_title_fushi),
                getString(R.string.key_title_hunqing),
                getString(R.string.key_title_shipin),
                getString(R.string.key_title_wuliu),
                getString(R.string.key_title_shoushi),
                getString(R.string.key_title_qiche),
                getString(R.string.key_title_shuma),
                getString(R.string.key_title_jiancai),
                getString(R.string.key_title_yiliao),
                getString(R.string.key_title_zhusu),
                getString(R.string.key_title_jingdian),

        };
        int[] title_color = {R.color.coral,
                R.color.mediumslateblue,
                R.color.holo_blue_bright,
                R.color.holo_orange_light,
                R.color.holo_green_light,
                R.color.darkslategray,
                R.color.gold,
                R.color.coral,
                R.color.mediumslateblue,
                R.color.holo_blue_bright,
                R.color.holo_orange_light,
                R.color.holo_green_light,
                R.color.darkslategray,
                R.color.gold,
                R.color.coral,
                R.color.mediumslateblue,
                R.color.holo_blue_bright,
                R.color.holo_orange_light,
                R.color.holo_green_light,
                R.color.darkslategray,
                R.color.gold,
                R.color.coral,

        };

        String[][] key_name = {
                {"餐厅", "小吃", "饭店", "火锅", "江浙菜", "日本菜", "甜点", "咖啡厅", "自助餐", "快餐", "西餐", "料理", "粵菜", "烧烤", "川菜", "素菜", "东北菜", "湘菜", "云南菜", "新疆菜", "海鲜", "西北菜", "蟹宴", "台湾菜", "贵州菜", "清真菜", "江西菜", "美食", "寿司", "饭团", "海参",},
                {"美发", "美容", "美甲", "祛斑", "瑜伽", "舞蹈", "纹绣", "健身", "化妆", "整形", "塑形", "彩妆", "丽人", "减肥", "瘦身", "造型", "养发", "化妆品", "养生馆", "美容院", "美甲店", "美容中心", "美容连锁", "美容会所", "美容美发", "美肤", "美容美体", "美甲工作室", "美妆",},
                {"休闲", "娱乐", "KTV", "足疗", "按摩", "影院", "洗浴", "桑拿", "理疗", "艾灸", "推拿", "咖啡厅", "酒吧", "网吧", "网咖", "农家乐", "游戏", "棋牌", "游乐", "游艺", "茶馆", "桌球馆", "中医", "养生", "文化艺术",},
                {"培训", "技能", "辅导", "工作室", "艺术", "艺术中心", "双语", "音乐班", "乐器", "口才", "学校", "补习", "儿童", "少年", "舞蹈", "钢琴", "琴", "棋", "书法", "绘画", "街舞", "跆拳道", "书画", "作文", "美术", "拉丁舞", "早教", "托管", "幼儿园", "托班", "英语", "留学", "口语", "雅思", "外国语"},
                {"酒店", "超市", "百货商场", "购物广场", "批发部", "便利店", "烟酒", "内衣", "政府", "公司", "批发", "装修", "工厂", "厨具", "理发", "医院", "诊所", "手机", "汽车", "电脑", "养殖", "旅社", "旅行社", "家具", "家政", "水果", "家用电器", "床上用品", "农贸市场", "菜市场", "鲜肉市场", "农产品",},
                {"超市", "批发", "专卖店", "便利店", "商场", "家电商城", "服装店", "百货批发", "烟酒便利店"},

                {"童装", "婴儿服", "童鞋", "玩具", "奶粉",},
                {"亲子", "亲子摄影", "早教中心", "亲子游乐", "婴儿游泳", "幼儿教育", "幼儿外语", "幼儿才艺", "月子会所", "孕妇写真", "孕产护理", "上门拍", "宝宝派对", "亲子购物", "托班",},
                {"宠物", "宠物医院", "宠物店", "宠物部落", "宠物美容", "宠物会所", "宠物诊所",},
                {"服装", "男装", "女装", "内衣", "童装", "户外装", "运动装",},
                {"婚纱", "摄影", "礼服", "鲜花", "花店", "婚庆", "婚宴", "彩妆", "婚车", "婚礼", "主持", "司仪", "婚戒", "首饰", "个性", "婚房", "婚庆设计", "婚典", "婚庆花艺", "婚庆用品", "婚礼订制", "婚庆公司", "婚礼策划", "婚庆首饰", "婚庆市场", "影楼",},
                {"食品", "茶叶", "零食", "乳品", "水果", "牛奶", "蛋糕", "海鲜", "坚果", "小吃",},
                {"物流", "通讯", "圆通", "申通", "顺丰", "中通", "韵达", "天天", "百世汇通", "德邦",},
                {"黄金", "珠宝", "首饰", "饰品", "钻石", "宝石", "珍珠", "玉",},
                {"驾校", "洗车", "汽车维修", "汽车保养", "汽车美容", "汽车租赁", "汽车销售", "汽车装饰"},
                {"数码产品", "电脑", "手机", "智能设备", "家用设备", "笔记本", "智能家居", "摄像机",},
                {"厨具", "装修", "家装", "建材", "家纺", "家具", "灯具", "家饰", "灯饰", "照明", "水暖", "园林", "装潢", "五金", "机械", "设备", "五金工具", "五金配件", "五金原料", "农机", "配件", "修理厂", "货场", "玻璃",},
                {"医院", "医药", "药店", "诊所", "门诊", "药房", "医院", "中药", "保健品"},
                {"酒店", "宾馆", "公寓", "商务酒店", "足浴店", "快捷酒店", "大酒店", "客栈", "民宿"},
                {"景点", "公园", "名胜古迹", "度假村", "步行街"}

        };

        List<KeyListBean> listData = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {

            List<KeyListBean.KeyItemBean> keyItemBeanList = new ArrayList<>();
            for (int x = 0; x < key_name[i].length; x++) {
                KeyListBean.KeyItemBean bean = new KeyListBean.KeyItemBean();
                bean.name = key_name[i][x];
                keyItemBeanList.add(bean);
            }
            KeyListBean keyListBean = new KeyListBean();
            keyListBean.listData = keyItemBeanList;
            keyListBean.title_name = titles[i];
            keyListBean.title_ttf = title_ttf[i];
            keyListBean.title_color = title_color[i];
            listData.add(keyListBean);
        }
        return listData;
    }


    /**
     * 获取vip 价格数据
     * 1=黄金会员2=白金会员
     *
     * @return
     */
    public static List<VipBean> getVipItemData() {
        List<VipBean> vipBeanList = new ArrayList<>();
        String[] names = {"黄金会员", "白金会员"};
        String[] price_des = {"998/年，每天可采集1万条数据", "1998/年，每天可采集6万条数据"};
        int[] grade = {1, 2};
        int[] monthCount = {12, 12};
        int[] price = {998, 1998};
        for (int i = 0; i < names.length; i++) {
            VipBean bean = new VipBean();
            bean.name = names[i];
            bean.price_des = price_des[i];
            bean.grade = grade[i];
            bean.price = price[i];
            bean.monthCount = monthCount[i];
            if (i == 0) {
                bean.isCheck = true;
            }
            vipBeanList.add(bean);
        }
        return vipBeanList;

    }


    /**
     * 权限被拒绝 显示提示
     */
    public static void showRationaleDialog(final PermissionUtils.OnRationaleListener.ShouldRequest shouldRequest) {
        Activity topActivity = ActivityUtils.getTopActivity();
        if (topActivity == null) return;


        new AlertView("提示", getString(R.string.permission_rationale_message), null, new String[]{"好的"}, new String[]{"取消"}, topActivity,
                AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                LogUtils.e("position=" + position);
                //0=好的
                shouldRequest.again(position == 0);
            }
        }).show();


    }

    /**
     * 权限被拒绝 显示打开appsetting页面
     */
    public static void showOpenAppSettingDialog() {
        Activity topActivity = ActivityUtils.getTopActivity();
        if (topActivity == null) return;

        new AlertView("提示", getString(R.string.permission_denied_forever_message), null, null, new String[]{"好的"}, topActivity,
                AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                LogUtils.e("position=" + position);
                com.blankj.utilcode.util.PermissionUtils.launchAppDetailsSettings();
            }
        }).show();
    }

    /**
     * 显示客服信息弹框
     */
    public static void showServiceInfoAlert(final Context context, String content) {
        final AlertView alertView;

        View view_service_info = View.inflate(context, R.layout.view_service_info, null);

        alertView = new AlertView("请联系客服", content, null, null, new String[]{"好的"}, context,
                AlertView.Style.Alert, null).addExtView(view_service_info);
        TextView tv_ttf_wechat = (TextView) view_service_info.findViewById(R.id.tv_ttf_wechat);
        TextView tv_ttf_tel = (TextView) view_service_info.findViewById(R.id.tv_ttf_tel);
        TextView tv_ttf_qq = (TextView) view_service_info.findViewById(R.id.tv_ttf_qq);
        tv_ttf_wechat.setTypeface(MyUtlis.getTTF());
        tv_ttf_tel.setTypeface(MyUtlis.getTTF());
        tv_ttf_qq.setTypeface(MyUtlis.getTTF());

        view_service_info.findViewById(R.id.ll_tel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //拨打电话

                alertView.dismissImmediately();

                PermissionUtils.permission(PermissionConstants.PHONE)
                        .rationale(new PermissionUtils.OnRationaleListener() {
                            @Override
                            public void rationale(final ShouldRequest shouldRequest) {
                                showRationaleDialog(shouldRequest);
                            }
                        })
                        .callback(new PermissionUtils.FullCallback() {
                            @Override
                            public void onGranted(List<String> permissionsGranted) {
                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                PhoneUtils.call(getString(R.string.service_tel));
                                LogUtils.d(permissionsGranted);
                            }

                            @Override
                            public void onDenied(List<String> permissionsDeniedForever,
                                                 List<String> permissionsDenied) {
                                if (!permissionsDeniedForever.isEmpty()) {
                                    showOpenAppSettingDialog();
                                }
                            }
                        })
                        .request();


            }
        });
        view_service_info.findViewById(R.id.ll_wechat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //复制微信
                copyText(getString(R.string.service_wechat));
                MyUtlis.showShort(context, "微信号码已复制");
            }
        });
        view_service_info.findViewById(R.id.ll_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//复制QQ
                copyText(getString(R.string.service_qq));

                MyUtlis.showShort(context, "QQ号码已复制");

            }
        });

        alertView.show();

    }


    /**
     * 用户登录
     *
     * @param phone
     * @param pwd
     * @return
     */
    public static int userLogin(String phone, String pwd) {
        return STATE_LOGIN_NO;
//        UserBean userByPhone = DaoUtlis.getUserByPhone(phone);
//        if (null == userByPhone) {
//            return STATE_LOGIN_NO_USER;
//        } else if (userByPhone.getPhone().equals(phone) && userByPhone.getPwd().equals(pwd)) {
//            MyUtlis.clickEvent(AppConstant.CLICK.umeng_login);
//            return STATE_LOGIN_YES;
//        } else {
//            return STATE_LOGIN_NO;
//        }
    }

    public static Drawable getDrawable(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(id);
        } else {
            return context.getResources().getDrawable(id);
        }

    }


    /**
     * 显示loading button
     *
     * @param pb       view
     * @param showType 显示的类型
     * @param listener 回调
     */
    public static void showLoadingBtn(final CircularProgressButton pb, final int showType, final OnAnimationEndListener listener) {


        pb.startAnimation();
        final int color = showType == STATE_YES
                ? ContextCompat.getColor(Utils.getApp(), R.color.holo_blue_bright)
                : showType == STATE_NO
                ? ContextCompat.getColor(Utils.getApp(), R.color.holo_red_light)
                : ContextCompat.getColor(Utils.getApp(), R.color.holo_orange_light);

        final Bitmap bitmap = showType == STATE_YES
                ? BitmapFactory.decodeResource(Utils.getApp().getResources(), R.drawable.ic_done_white_48dp) : showType == STATE_NO
                ? BitmapFactory.decodeResource(Utils.getApp().getResources(), R.drawable.ic_error)
                : BitmapFactory.decodeResource(Utils.getApp().getResources(), R.drawable.ic_error2);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pb.doneLoadingAnimation(
                        color,
                        bitmap);
            }
        }, 2000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pb.revertAnimation(listener);
            }
        }, 3000);

    }

    /**
     * 退出登录
     */
    public static void userLoginOut(Context context, boolean isShowAlert) {
        if (isShowAlert) {


            new AlertView(getString(R.string.hint_text), getString(R.string.hint_outlogin_user), null, new String[]{getString(R.string.hint_confirm)}, new String[]{getString(R.string.hint_cancel)}, context,
                    AlertView.Style.Alert, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {

                    if (position == 0) {
                        userLoginOut();
                    }
                }
            }).show();

        } else {
            //直接退出
            userLoginOut();
        }
    }

    /**
     * 退出登录
     */
    public static void userLoginOut() {
        SPUtils.getInstance().clear();
        LoginActivity.start(Utils.getApp());
//        BaseApp.finishOtherActivities(LoginActivity.class);
        ActivityUtils.finishOtherActivities(LoginActivity.class);
    }


    /**
     * 复制文本到剪贴板
     *
     * @param text 文本
     */
    public static void copyText(final CharSequence text) {
        ClipboardManager clipboard = (ClipboardManager) Utils.getApp().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText("text", text));
    }


    /**
     * 显示带图标的强调提示
     *
     * @param state
     * @param text
     */
    public static void showShort(Context context, int state, String text, int toastType) {
        UniversalToast.makeText(context, text, UniversalToast.LENGTH_SHORT, toastType)
//                .setGravity(gravity,xOffset,yOffset)
//                .setBackground(drawable)//set the background drawable as you like
//                .setColor(R.color.my_color)//set the background color as you like
                .setIcon(state == STATE_YES ? R.drawable.ic_done_white_48dp : state == STATE_NO ? R.drawable.ic_error : R.drawable.ic_error2)// set the icon as you like (it's visibility is gone until you set icon)
//                .setClickCallBack(text,R.drawable.my_btn,onClickListener)
                .show();
    }

    public static void showShort(Context context, String text) {
        showShort(context, STATE_ERROR, text, UniversalToast.EMPHASIZE);
    }

    public static void showShort(Context context, int state, String text) {
        showShort(context, state, text, UniversalToast.EMPHASIZE);
    }

    public static void showShortYes(Context context, String text) {
        showShort(context, STATE_YES, text);
    }

    public static void showShortNo(Context context, String text) {
        showShort(context, STATE_NO, text);
    }

    public static void showShortSimple(Context context, String text) {
        showShort(context, STATE_ERROR, text, UniversalToast.UNIVERSAL);
    }


    /**
     * 判断当前用户是否是vip
     */
    public static boolean isVip() {

        UserBean user = DaoUtlis.getCurrentLoginUser();
        if (null == user) {
            return false;
        }
        if (user.getGrade() == AppConstant.VIP_GRADE_1 || user.getGrade() == AppConstant.VIP_GRADE_2) {
            return true;
        }
        return false;
    }


    /**
     * 显示文件保存成功的toast
     *
     * @param context
     * @param text
     */
    public static void showFileSaveToast(Context context, String text) {
        UniversalToast.makeText(context, text, UniversalToast.LENGTH_LONG, UniversalToast.UNIVERSAL)
//                .setGravity(gravity,xOffset,yOffset)
//                .setBackground(drawable)//set the background drawable as you like
//                .setColor(R.color.my_color)//set the background color as you like
                .setIcon(R.drawable.ic_done_white_48dp)// set the icon as you like (it's visibility is gone until you set icon)
//                .setClickCallBack(text,R.drawable.my_btn,onClickListener)
                .show();
    }

    /**
     * 打开相册选取相片
     *
     * @param activity
     * @param maxNum   结果回调onActivityResult code  = PictureConfig.CHOOSE_REQUEST
     */
    public static void openPhoto(final Activity activity, final int maxNum) {
        PermissionUtils.permission(PermissionConstants.CAMERA, PermissionConstants.STORAGE)
                .rationale(new PermissionUtils.OnRationaleListener() {
                    @Override
                    public void rationale(final ShouldRequest shouldRequest) {
                        showRationaleDialog(shouldRequest);
                    }
                })
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {

                        // 进入相册 以下是例子：不需要的api可以不写
                        PictureSelector.create(activity)
                                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                                .theme(R.style.my_picture_default_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                                .maxSelectNum(maxNum)// 最大图片选择数量 int
                                .minSelectNum(1)// 最小选择数量 int
                                .imageSpanCount(4)// 每行显示个数 int
                                .selectionMode(maxNum > 1 ? PictureConfig.MULTIPLE : PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                                .previewImage(true)// 是否可预览图片 true or false
                                .previewVideo(false)// 是否可预览视频 true or false
                                .enablePreviewAudio(false) // 是否可播放音频  true or false
//                .compressGrade()// luban压缩档次，默认3档 Luban.THIRD_GEAR、Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                                .isCamera(true)// 是否显示拍照按钮 ture or false
                                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                                .setOutputCameraPath("/Chinayie/App")// 自定义拍照保存路径,可不填
                                .compress(false)// 是否压缩 true or false
//                .compressMode()//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
//                .glideOverride()// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度 int
                                .isGif(false)// 是否显示gif图片 true or false
                                .openClickSound(false)// 是否开启点击声音 true or false
//                .selectionMedia()// 是否传入已选图片 List<LocalMedia> list
                                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
//                .compressMaxKB()//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效 int
//                .compressWH() // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效 int
//                .videoQuality()// 视频录制质量 0 or 1 int
//                .videoSecond()//显示多少秒以内的视频or音频也可适用 int
//                .recordVideoSecond()//录制视频秒数 默认60s int
                                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code


                        LogUtils.d(permissionsGranted);
                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever,
                                         List<String> permissionsDenied) {
                        if (!permissionsDeniedForever.isEmpty()) {
                            showOpenAppSettingDialog();
                        }
                    }
                })
                .request();


    }


    /**
     * 打开相册选取相片
     *
     * @param activity
     * @param maxNum   结果回调onActivityResult code  = PictureConfig.CHOOSE_REQUEST
     */
    public static void openPhoto(final Fragment activity, final int maxNum) {
        PermissionUtils.permission(PermissionConstants.CAMERA, PermissionConstants.STORAGE)
                .rationale(new PermissionUtils.OnRationaleListener() {
                    @Override
                    public void rationale(final ShouldRequest shouldRequest) {
                        showRationaleDialog(shouldRequest);
                    }
                })
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {

                        // 进入相册 以下是例子：不需要的api可以不写
                        PictureSelector.create(activity)
                                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                                .theme(R.style.my_picture_default_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                                .maxSelectNum(maxNum)// 最大图片选择数量 int
                                .minSelectNum(1)// 最小选择数量 int
                                .imageSpanCount(4)// 每行显示个数 int
                                .selectionMode(maxNum > 1 ? PictureConfig.MULTIPLE : PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                                .previewImage(true)// 是否可预览图片 true or false
                                .previewVideo(false)// 是否可预览视频 true or false
                                .enablePreviewAudio(false) // 是否可播放音频  true or false
//                .compressGrade()// luban压缩档次，默认3档 Luban.THIRD_GEAR、Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                                .isCamera(true)// 是否显示拍照按钮 ture or false
                                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                                .setOutputCameraPath("/Chinayie/App")// 自定义拍照保存路径,可不填
                                .compress(false)// 是否压缩 true or false
//                .compressMode()//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
//                .glideOverride()// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度 int
                                .isGif(false)// 是否显示gif图片 true or false
                                .openClickSound(false)// 是否开启点击声音 true or false
//                .selectionMedia()// 是否传入已选图片 List<LocalMedia> list
                                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
//                .compressMaxKB()//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效 int
//                .compressWH() // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效 int
//                .videoQuality()// 视频录制质量 0 or 1 int
//                .videoSecond()//显示多少秒以内的视频or音频也可适用 int
//                .recordVideoSecond()//录制视频秒数 默认60s int
                                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code


                        LogUtils.d(permissionsGranted);
                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever,
                                         List<String> permissionsDenied) {
                        if (!permissionsDeniedForever.isEmpty()) {
                            showOpenAppSettingDialog();
                        }
                    }
                })
                .request();


    }


    /**
     * 获取数据采集的电话类型 int value
     *
     * @return
     */
    public static int getCollectDataPhoneType() {
        return SPUtils.getInstance().getInt(AppConstant.SPKey.COLLECT_DATA_PHONE_TYPE, AppConstant.COLLECT_DATA_PHONE_TYPE_ALL);

    }

    /**
     * 获取数据采集的电话类型 int value
     *
     * @return
     */
    public static void setCollectDataPhoneType(int type) {
        SPUtils.getInstance().put(AppConstant.SPKey.COLLECT_DATA_PHONE_TYPE, type);

    }

    /**
     * 获取数据采集的电话类型 string描述
     *
     * @return
     */
    public static String getCollectDataPhoneTypeStr() {
        switch (getCollectDataPhoneType()) {
            case AppConstant.COLLECT_DATA_PHONE_TYPE_PHONE_TEL:
                return "固话+手机";
            case AppConstant.COLLECT_DATA_PHONE_TYPE_PHONE:

                return "只要手机";
//            case AppConstant.COLLECT_DATA_PHONE_TYPE_ALL:
//                return "全部数据";
            default:
                return "全部数据";
        }
    }

    /**
     * 显示图片到imageview 圆形
     *
     * @param url
     * @param imageView
     */
    public static void showImage(String url, ImageView imageView) {
        Glide.with(Utils.getApp()).load(url)
                .apply(MyUtlis.getGlideRequestOptions()).into(imageView);
    }


    /**
     * 设置圆形RequestOptions
     *
     * @return
     */
    public static RequestOptions getGlideRequestOptions() {
        return getGlideRequestOptions(R.drawable.d_img_logo_2);
    }

    /**
     * 设置圆形RequestOptions
     *
     * @return
     */
    public static RequestOptions getGlideRequestOptions2() {
        return getGlideRequestOptions2(R.drawable.d_img_logo_2);
    }

    /**
     * 设置矩形RequestOptions
     *
     * @return
     */
    public static RequestOptions getGlideRequestOptions2(@DrawableRes int iconId) {
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.error(iconId);
//        options.bitmapTransform(new CircleCrop());
//        options.skipMemoryCache(false);
        options.dontAnimate();
//        options.diskCacheStrategy(DiskCacheStrategy.DATA);
        options.placeholder(iconId);
        options.priority(Priority.HIGH);
        return options;
    }


    /**
     * 设置头像RequestOptions
     *
     * @return
     */
    public static RequestOptions getGlideRequestOptions(@DrawableRes int iconId) {
        RequestOptions options = new RequestOptions();
        options.circleCrop();
//        options.centerCrop();
        options.error(iconId);
//        options.bitmapTransform(new CircleCrop());
//        options.skipMemoryCache(false);
        options.dontAnimate();
//        options.diskCacheStrategy(DiskCacheStrategy.DATA);
        options.placeholder(iconId);
        options.priority(Priority.HIGH);
        return options;
    }


    /**
     * 更新头像
     *
     * @param avatarUrl
     */
    public static void eventUpdateAvatar(String avatarUrl) {
        MessageEvent event = new MessageEvent();
        event.eventType = MessageEvent.UPDATE_AVATAR;
        event.stringValue = avatarUrl;
        EventBus.getDefault().post(event);
    }

    /**
     * 更新所有平台数据总量
     *
     * @param allDataCount 数据总数量
     */
    public static void eventUpdateAllPlatfromDataCount(int allDataCount) {
        MessageEvent event = new MessageEvent();
        event.eventType = MessageEvent.UPDATE_ALL_PLATFORM_DATA_COUNT;
        event.intValue = allDataCount;
        EventBus.getDefault().post(event);
    }

    /**
     * 更新保存通讯录数据的 数据总量
     *
     * @param
     */
    public static void eventUpdateSaveDataCount(int saveDataCount) {
        MessageEvent event = new MessageEvent();
        event.eventType = MessageEvent.UPDATE_SAVE_DATA_COUNT;
        event.intValue = saveDataCount;
        EventBus.getDefault().post(event);
    }

    /**
     * 更新昵称
     *
     * @param nickName
     */
    public static void eventUpdateNickName(String nickName) {
        MessageEvent event = new MessageEvent();
        event.eventType = MessageEvent.UPDATE_NICI_NAME;
        event.stringValue = nickName;
        EventBus.getDefault().post(event);
    }

    /**
     * 删除缓存
     */
    public static void deleteChace(Context context) {
        try {
            FileUtils.deleteDir(Utils.getApp().getCacheDir());
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                FileUtils.deleteDir(Utils.getApp().getExternalCacheDir());
            }
            PictureFileUtils.deleteCacheDirFile(Utils.getApp());
            showShortYes(context, context.getString(R.string.hint_delete_chace_ok));

        } catch (Exception e) {
            e.printStackTrace();
            showShortYes(context, context.getString(R.string.hint_delete_chace_no));
        }
    }

    /**
     * 删除数据
     *
     * @param
     */
    public static void deleteData(final Context context) {

        final List<DataBean> allData = DaoUtlis.getAllData(MyUtlis.getLoginUserId());
        if (allData.isEmpty()) {
            showShort(context, getString(R.string.hint_no_data_2));
            return;
        }

        new AlertView(getString(R.string.hint_text), context.getString(R.string.hint_confirm_delete_all_platform_data, allData.size()), null, new String[]{getString(R.string.hint_confirm)}, new String[]{getString(R.string.hint_cancel)}, context,
                AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    boolean b1 = DaoUtlis.deleteAllData(allData);
                    if (b1) {
                        eventUpdateHomeData();
                        showShortYes(context, getString(R.string.hint_delete_yes));
                        clickEvent(AppConstant.CLICK.umeng_delete_all_data);
                    } else {
                        showShortNo(context, getString(R.string.hint_delete_no));
                    }
                }
            }
        }).show();

    }

    /**
     * 刷新所有平台数据
     */
    private static void eventUpdateHomeData() {
        MessageEvent event = new MessageEvent();
        event.eventType = MessageEvent.UPDATE_HOME_DATA;
//        event.stringValue = avatarUrl;
        EventBus.getDefault().post(event);
    }

    /**
     * 获取缓存大小
     *
     * @return
     */
    public static String getChaceSize() {

//        FileUtils.getDirSize(Utils.getApp().getCacheDir());
        try {
            long cacheSize = FileUtils.getDirLength(Utils.getApp().getCacheDir());
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                cacheSize += FileUtils.getDirLength(Utils.getApp().getExternalCacheDir());
            }
            if (cacheSize <= 0) {
                return "OK";
            }
            String seizeStr = ConvertUtils.byte2FitMemorySize(cacheSize);
            if (seizeStr.equals("0.000B")) {
                return "OK";
            }

            return seizeStr;
        } catch (Exception e) {
            e.printStackTrace();
            return "0K";
        }
    }


    /**
     * 处理华为手机home键返回点击app图标重新打开应用程序的问题  todo:需要重新验证问题及代码
     *
     * @param activity
     */
    public static void huaweiSplash(Activity activity) {
        if (!activity.isTaskRoot()) {

            Intent mainIntent = activity.getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                activity.finish();
                return;
            }
        }
    }

    /**
     * 定位完成更新
     *
     * @param location
     */
    public static void eventUpdateLocation(LocationBean location) {
        MessageEvent event = new MessageEvent();
        event.eventType = MessageEvent.UPDATE_LOCATION;
        event.locationBean = location;
        EventBus.getDefault().post(event);
    }

    /**
     * 数据采集完成
     *
     * @param
     */
    public static void eventCollectComplete() {
        MessageEvent event = new MessageEvent();
        event.eventType = MessageEvent.COLLECT_COMPLETE;
        EventBus.getDefault().post(event);
    }


    /**
     * 数据采集并添加成功, 一次完整采集中,可能会多次回调
     *
     * @param
     */
    public static void eventAddDataOK() {
        MessageEvent event = new MessageEvent();
        event.eventType = MessageEvent.ADD_COLLECT_DATA_OK;
        EventBus.getDefault().post(event);
    }


    /**
     * 刷新营销页面数据
     *
     * @param
     */
    public static void eventRefreshMarketingFragmentData() {
        MessageEvent event = new MessageEvent();
        event.eventType = MessageEvent.ADD_COLLECT_DATA_OK;
        EventBus.getDefault().post(event);
    }

    /**
     * 数据采集错误
     *
     * @param
     */
    public static void eventCollectError() {
        MessageEvent event = new MessageEvent();
        event.eventType = MessageEvent.COLLECT_ERROR;
        EventBus.getDefault().post(event);
    }

    /**
     * 数据采集完成
     *
     * @param isShow
     */
    public static void eventHomeMenuVisibility(boolean isShow) {
        MessageEvent event = new MessageEvent();
        event.eventType = MessageEvent.HOME_MENU_VISIBILITY;
        event.booleanValue = isShow;
        EventBus.getDefault().post(event);
    }

    /**
     * 开启定位服务
     */
    public static void startLocatinService() {

        if (!AppConstant.DEBUG) {
            ServiceUtils.startService(LocationService.class);
        }
    }

    /**
     * 保存当前定位信息 到数据库
     *
     * @param location
     */
    public static LocationBean saveLocationInfo(BDLocation location) {
        logLocationInfo(location);
        LocationBean bean = new LocationBean();
        bean.id = 1L;
        bean.province = location.getProvince();
        bean.time = location.getTime();
        bean.locType = location.getLocType();
        bean.locTypeDescription = location.getLocTypeDescription();
        bean.latitude = location.getLatitude();
        bean.lontitude = location.getLongitude();
        bean.radius = location.getRadius();
        bean.countryCode = location.getCountryCode();
        bean.country = location.getCountry();
        bean.citycode = location.getCityCode();
        bean.city = location.getCity();
        bean.district = location.getDistrict();
        bean.street = location.getStreet();
        bean.addr = location.getAddrStr();
        bean.userIndoorState = location.getUserIndoorState();
        bean.direction = location.getDirection();
        bean.locationdescribe = location.getLocationDescribe();
        if (location.getPoiList() != null && !location.getPoiList().isEmpty() && location.getPoiList().size() > 0) {
            Poi poi = location.getPoiList().get(0);
            bean.poiName = poi.getName();
        }

        if (!TextUtils.isEmpty(bean.city)) {
            LogUtils.e("定位信息无误, 更新");
            DaoUtlis.saveLocation(bean);
        } else {
            LogUtils.e("定位信息有误, 不更新");
        }

        return bean;

    }


    /**
     * 打印定位信息
     *
     * @param location
     */
    private static void logLocationInfo(BDLocation location) {

        try {
            if (AppConstant.DEBUG) {

                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                sb.append(location.getLocTypeDescription());
                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");// 经度
                sb.append(location.getLongitude());
                sb.append("\nradius : ");// 半径
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");// 国家码
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");// 国家名称
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");// 城市编码
                sb.append(location.getCityCode());
                sb.append("\ncity : ");// 城市
                sb.append(location.getCity());
                sb.append("\nDistrict : ");// 区
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");// 街道
                sb.append(location.getStreet());
                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddrStr());
                sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
                sb.append(location.getUserIndoorState());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());// 方向
                sb.append("\nlocationdescribe: ");
                sb.append(location.getLocationDescribe());// 位置语义化信息
                sb.append("\nPoi: ");// POI信息
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");

                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 速度 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());// 卫星数目
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 海拔高度 单位：米
                    sb.append("\ngps status : ");
                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    if (location.hasAltitude()) {// *****如果有海拔高度*****
                        sb.append("\nheight : ");
                        sb.append(location.getAltitude());// 单位：米
                    }
                    sb.append("\noperationers : ");// 运营商信息
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                LogUtils.e(sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 获取首页平台菜单数据
     *
     * @return
     */
    public static List<PlatformBean> getPlatformBeanList() {
        List<PlatformBean> list = new ArrayList<>();
        for (int i = 0; i < AppConstant.Platform.NAME.length; i++) {

            PlatformBean bean = new PlatformBean();

            bean.ttfStr = getString(AppConstant.Platform.TTF_STR_ID[i]);
            bean.name = AppConstant.Platform.NAME[i];
            bean.platformId = AppConstant.Platform.PLATFORM_ID[i];
            list.add(bean);
        }

        return list;
    }


    /**
     * 获取采集测试数据
     *
     * @param datacount
     * @return
     */
    public static List<DataBean> getTestData(int datacount, PlatformBean platformBean) {
        List<DataBean> list = new ArrayList<>();
        String loginUserId = getLoginUserId();
        int uid = new Random().nextInt(1000);
        for (int i = 0; i < datacount; i++) {
            DataBean bean = new DataBean();
            bean.setUserId(loginUserId);
            bean.source = platformBean.name;
            bean.name = "测试商家" + (i + uid);
            bean.tel = "0745-123456";
            bean.phone = "1300000000" + i;
            bean.address = "测试地址" + i;
            bean.address_details = "广东省深圳市南山区深南大道北" + i;
            bean.uId = String.valueOf(i + uid);
            bean.platformId = platformBean.platformId;
            bean.key = "测试key";
            bean.city = "测试city";
            bean.setDataId(bean.platformId + bean.uId);
            list.add(bean);
        }
        return list;
    }

    /**
     * 保存最后一个选中的平台的索引
     *
     * @param index
     */
    public static void setLastSelectPlatformIndex(int index) {
        SPUtils.getInstance().put(AppConstant.SPKey.LAST_PLATFORM_INDEX, index);
    }

    /**
     * 获取最后一个选中的平台的索引
     */
    public static int getLastSelectPlatformIndex() {
        return SPUtils.getInstance().getInt(AppConstant.SPKey.LAST_PLATFORM_INDEX, 0);
    }

    /**
     * 获取登录的userid
     */
    public static String getLoginUserId() {
        return SPUtils.getInstance().getString(AppConstant.SPKey.LAST_LOGIN_USER, AppConstant.DEFAULT_USER_ID);
    }

    /**
     * 设置登录的userid
     */
    public static void setLoginUserId(String userId) {
        SPUtils.getInstance().put(AppConstant.SPKey.LAST_LOGIN_USER, userId);
    }

    /**
     * 获取token
     */
    public static String getToken() {
        return SPUtils.getInstance().getString(AppConstant.SPKey.TOKEN, "");
    }

    /**
     * 设置token
     */
    public static void setToken(String token) {
        SPUtils.getInstance().put(AppConstant.SPKey.TOKEN, token);
    }


    /**
     * 采集数据event
     *
     * @param platformId
     * @param dataBeanList
     */
    public static void eventCollectData(int platformId, List<DataBean> dataBeanList) {
        MessageEvent event = new MessageEvent();
        event.eventType = MessageEvent.COLLECT_DATA;
        event.dataList = dataBeanList;
        event.intValue = platformId;
        EventBus.getDefault().post(event);
    }

    /**
     * 采集数据event
     *
     * @param
     */
    public static void eventUpdatePlatfromDataCount() {
        MessageEvent event = new MessageEvent();
        event.eventType = MessageEvent.UPDATE_PLATFORM_DATA_COUNT;
        EventBus.getDefault().post(event);
    }


    /**
     * 往数据库中新增单个联系人
     *
     * @isCloseActivity 添加成功是否关闭页面
     */
    public static void addContact(Context context, DataBean dataBean) {
        addContact(context, dataBean, false);
    }

    /**
     * 往数据库中新增单个联系人
     *
     * @isCloseActivity 添加成功是否关闭页面
     */
    public static void addContact(final Context context, final DataBean dataBean, final boolean isCloseActivity) {

        if (dataBean == null || (TextUtils.isEmpty(dataBean.phone) && TextUtils.isEmpty(dataBean.tel))) {
            //对象为null , 或者 手机和电话同时为null
            return;
        }

        PermissionUtils.permission(PermissionConstants.CONTACTS, PermissionConstants.STORAGE)
                .rationale(new PermissionUtils.OnRationaleListener() {
                    @Override
                    public void rationale(final ShouldRequest shouldRequest) {
                        showRationaleDialog(shouldRequest);
                    }
                })
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {


                        try {
                            ContentValues values = new ContentValues();
                            // 首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获取系统返回的rawContactId
                            Uri rawContactUri = Utils.getApp().getContentResolver().insert(
                                    ContactsContract.RawContacts.CONTENT_URI, values);
                            long rawContactId = ContentUris.parseId(rawContactUri);
                            // 往data表插入姓名数据
                            values.clear();
                            values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
                            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);// 内容类型
                            values.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, dataBean.getName() + AppConstant.CONTACTS_SUFFIX);
                            Utils.getApp().getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
                                    values);

                            // 往data表插入电话数据

                            if (!TextUtils.isEmpty(dataBean.getPhone())) {
                                values.clear();
                                values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
                                values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                                values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, dataBean.phone);
                                values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
                                Utils.getApp().getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
                                        values);
                            }

                            //插入座机
                            if (!TextUtils.isEmpty(dataBean.tel)) {
                                values.clear();
                                values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
                                values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                                values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, dataBean.tel);
                                values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE);
                                Utils.getApp().getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
                                        values);
                            }

                            //插入地址
                            if (!TextUtils.isEmpty(dataBean.address)) {
                                values.clear();
                                values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
                                values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.SipAddress.CONTENT_ITEM_TYPE);
                                values.put(ContactsContract.CommonDataKinds.SipAddress.SIP_ADDRESS, dataBean.address);
                                values.put(ContactsContract.CommonDataKinds.SipAddress.TYPE, ContactsContract.CommonDataKinds.SipAddress.TYPE_WORK);
                                Utils.getApp().getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
                                        values);
                            }

                            //插入备注

                            values.clear();
                            values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
                            values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE);
                            values.put(ContactsContract.CommonDataKinds.Note.NOTE, "来源:" + dataBean.source);
//        values.put(ContactsContract.CommonDataKinds.Note., ContactsContract.CommonDataKinds.SipAddress.TYPE_WORK);
                            Utils.getApp().getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
                                    values);
                            showShortYes(context, getString(R.string.hint_addContact_yes));
                            clickEvent(AppConstant.CLICK.umeng_save_single_contact);
                            updateSaveDataCount(1);
                            if (context instanceof Activity && isCloseActivity) {
                                ActivityUtils.finishActivity((Activity) context, true);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            showShortNo(context, getString(R.string.hint_addContact_no));
                        }

                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever,
                                         List<String> permissionsDenied) {
                        if (!permissionsDeniedForever.isEmpty()) {
                            showOpenAppSettingDialog();
                        }
                    }
                })
                .request();


    }


    /**
     * 更新保存通讯录的 数据的总数量
     *
     * @param saveDataCount
     */
    public static void updateSaveDataCount(int saveDataCount) {
        UserBean user = DaoUtlis.getUser(getLoginUserId());
        user.setSaveNumber(user.getSaveNumber() + saveDataCount);
        boolean b = DaoUtlis.addUser(user);
        if (b) {
            eventUpdateSaveDataCount(user.getSaveNumber());
        }
    }


    /**
     * 更新获取的数据总量
     *
     * @param allDataCount
     */
    public static void updateAllDataCont(int allDataCount) {
        UserBean user = DaoUtlis.getUser(getLoginUserId());
        user.setDownNumber(user.getDownNumber() + allDataCount);
        boolean b = DaoUtlis.addUser(user);
        if (b) {
            eventUpdateAllPlatfromDataCount(user.getDownNumber());
        }
    }

    /**
     * 批量添加通讯录
     */
    public static void addContacts(final Context context, final List<DataBean> list) {

        if (null == list || list.isEmpty()) {
            return;
        }
        PermissionUtils.permission(PermissionConstants.CONTACTS, PermissionConstants.STORAGE)
                .rationale(new PermissionUtils.OnRationaleListener() {
                    @Override
                    public void rationale(final ShouldRequest shouldRequest) {
                        showRationaleDialog(shouldRequest);
                    }
                })
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        ThreadUtils.executeByFixed(AppConstant.THREAD_SIZE, new ThreadUtils.SimpleTask<Boolean>() {
                            @Override
                            public Boolean doInBackground() {
                                ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
                                int rawContactInsertIndex = 0;

                                boolean isOk = false;
                                for (DataBean contact : list) {

                                    try {

                                        if (contact == null || (TextUtils.isEmpty(contact.phone) && TextUtils.isEmpty(contact.tel))) {
                                            //对象为null , 或者 手机和电话同时为null
                                            continue;
                                        }
                                        rawContactInsertIndex = ops.size(); // 有了它才能给真正的实现批量添加
                                        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                                                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                                                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                                                .withYieldAllowed(true).build());

                                        // 添加姓名
                                        ops.add(ContentProviderOperation
                                                .newInsert(
                                                        android.provider.ContactsContract.Data.CONTENT_URI)
                                                .withValueBackReference(ContactsContract.Contacts.Data.RAW_CONTACT_ID,
                                                        rawContactInsertIndex)
                                                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                                                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.getName() + AppConstant.CONTACTS_SUFFIX)
                                                .withYieldAllowed(true).build());
                                        // 添加手机
                                        if (!TextUtils.isEmpty(contact.getPhone())) {
                                            ops.add(ContentProviderOperation
                                                    .newInsert(
                                                            android.provider.ContactsContract.Data.CONTENT_URI)
                                                    .withValueBackReference(ContactsContract.Contacts.Data.RAW_CONTACT_ID,
                                                            rawContactInsertIndex)
                                                    .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getPhone())
                                                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                                                    .withYieldAllowed(true).build());
                                        }


                                        //插入座机
                                        if (!TextUtils.isEmpty(contact.getTel())) {
                                            ops.add(ContentProviderOperation
                                                    .newInsert(
                                                            android.provider.ContactsContract.Data.CONTENT_URI)
                                                    .withValueBackReference(ContactsContract.Contacts.Data.RAW_CONTACT_ID,
                                                            rawContactInsertIndex)
                                                    .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getTel())
                                                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE)
                                                    .withYieldAllowed(true).build());
                                        }


                                        //插入地址

                                        if (!TextUtils.isEmpty(contact.getTel())) {
                                            ops.add(ContentProviderOperation
                                                    .newInsert(
                                                            android.provider.ContactsContract.Data.CONTENT_URI)
                                                    .withValueBackReference(ContactsContract.Contacts.Data.RAW_CONTACT_ID,
                                                            rawContactInsertIndex)
                                                    .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.SipAddress.CONTENT_ITEM_TYPE)
                                                    .withValue(ContactsContract.CommonDataKinds.SipAddress.SIP_ADDRESS, contact.getAddress())
                                                    .withValue(ContactsContract.CommonDataKinds.SipAddress.TYPE, ContactsContract.CommonDataKinds.SipAddress.TYPE_WORK)
                                                    .withYieldAllowed(true).build());
                                        }


                                        //插入备注
                                        if (!TextUtils.isEmpty(contact.getSource())) {
                                            ops.add(ContentProviderOperation
                                                    .newInsert(
                                                            android.provider.ContactsContract.Data.CONTENT_URI)
                                                    .withValueBackReference(ContactsContract.Contacts.Data.RAW_CONTACT_ID,
                                                            rawContactInsertIndex)
                                                    .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)
                                                    .withValue(ContactsContract.CommonDataKinds.Note.NOTE, "来源:" + contact.source)
//                        .withValue(ContactsContract.CommonDataKinds.SipAddress.TYPE, ContactsContract.CommonDataKinds.SipAddress.TYPE_WORK)
                                                    .withYieldAllowed(true).build());
                                        }


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        continue;
                                    }
                                }
                                if (ops != null) {
                                    // 真正添加
                                    ContentProviderResult[] results = new ContentProviderResult[0];
                                    try {
                                        results = Utils.getApp().getContentResolver()
                                                .applyBatch(ContactsContract.AUTHORITY, ops);
                                        isOk = true;

                                        updateSaveDataCount(list.size());
                                    } catch (Exception e) {
                                        e.printStackTrace();


                                    }
//                                    for (ContentProviderResult result : results) {
//                                        Log.e("TAG", "添加结果="
//                                                + result.uri.toString());
//                                    }
                                }
                                return isOk;
                            }

                            @Override
                            public void onSuccess(Boolean result) {
                                if (result) {
                                    showShortYes(context, getString(R.string.hint_addContact_yes));
                                    clickEvent(AppConstant.CLICK.umeng_export_all_contact);
                                } else {
                                    showShortNo(context, getString(R.string.hint_addContact_no));

                                }
                            }


                        });

                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever,
                                         List<String> permissionsDenied) {
                        if (!permissionsDeniedForever.isEmpty()) {
                            showOpenAppSettingDialog();
                        }
                    }
                })
                .

                        request();


    }


    /**
     * 获取网络时间
     *
     * @return
     */
    public static void getNetDate(final MyListener listener) {


        ThreadUtils.executeByFixed(AppConstant.THREAD_SIZE, new ThreadUtils.SimpleTask<Calendar>() {
            @Override
            public Calendar doInBackground() {

                String webUrl = "http://www.bjtime.cn";//bjTime
                try {
                    URL url = new URL(webUrl);// 取得资源对象
                    URLConnection uc = url.openConnection();// 生成连接对象
                    uc.connect();// 发出连接
                    long ld = uc.getDate();// 读取网站日期时间
                    Date date = new Date(ld);// 转换为标准时间对象

                    Calendar calendar = Calendar.getInstance();

                    calendar.setTime(date);

//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);// 输出北京时间
                    return calendar;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void onSuccess(Calendar result) {
                if (null != result && null != listener) {
                    listener.onSucceed(result);
                } else {
                    LogUtils.e("获取时间错误");
                }
            }
        });


    }


    //最后一次点击的时间
    private static long lastClickTime = 0;
    //2次点击的间隔
    private static long DIFF = 1000;
    //最后点击的按钮id
    private static int lastButtonId = -1;

    /**
     * 判断两次点击的间隔，如果小于1000，则认为是多次无效点击
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        return isFastDoubleClick(-1, DIFF);
    }

    /**
     * 判断两次点击的间隔，如果小于1000，则认为是多次无效点击
     *
     * @return
     */
    public static boolean isFastDoubleClick(int buttonId) {
        return isFastDoubleClick(buttonId, DIFF);
    }


    /**
     * 判断两次点击的间隔，如果小于diff，则认为是多次无效点击
     *
     * @param diff
     * @return
     */
    public static boolean isFastDoubleClick(int buttonId, long diff) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (lastButtonId == buttonId && lastClickTime > 0 && timeD < diff) {
            LogUtils.e("短时间内按钮多次触发");
            return true;
        }
        lastClickTime = time;
        lastButtonId = buttonId;
        return false;
    }

    /**
     * 删除所有保存到通讯录的 数据
     */
    public static void deleteContact(final Context context) {


        PermissionUtils.permission(PermissionConstants.CONTACTS, PermissionConstants.STORAGE)
                .rationale(new PermissionUtils.OnRationaleListener() {
                    @Override
                    public void rationale(final ShouldRequest shouldRequest) {
                        showRationaleDialog(shouldRequest);
                    }
                })
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {


                        try {
                            //显示提示框
                            Query q = Contacts.getQuery();
                            q.whereContains(Contact.Field.DisplayName, AppConstant.CONTACTS_SUFFIX);
                            final List<Contact> contacts = q.find();

                            if ((null == contacts || contacts.isEmpty())) {
                                showShort(context, getString(R.string.hint_no_contact_data));
                                return;
                            }

//                            LogUtils.e("查询到名称包含_jgz联系人size= " + contacts.size());
//                            for (Contact contact : contacts) {
//                                LogUtils.e(contact.getDisplayName() + " ID=" + contact.getId());
//                            }


                            new AlertView(getString(R.string.hint_text), context.getString(R.string.hint_confirm_delete_all_contact, contacts.size()), null, new String[]{getString(R.string.hint_confirm)}, new String[]{getString(R.string.hint_cancel)}, context,
                                    AlertView.Style.Alert, new OnItemClickListener() {
                                @Override
                                public void onItemClick(Object o, int position) {
                                    if (position == 0) {
                                        deleteContacts(context, contacts);
                                    }
                                }
                            }).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever,
                                         List<String> permissionsDenied) {
                        if (!permissionsDeniedForever.isEmpty()) {
                            showOpenAppSettingDialog();
                        }
                    }
                })
                .request();

    }


    /**
     * 批量删除通讯录
     */
    public static void deleteContacts(final Context context, final List<Contact> list) {
        if (null == list || list.isEmpty()) {
            return;
        }
        PermissionUtils.permission(PermissionConstants.CONTACTS, PermissionConstants.STORAGE)
                .rationale(new PermissionUtils.OnRationaleListener() {
                    @Override
                    public void rationale(final ShouldRequest shouldRequest) {
                        showRationaleDialog(shouldRequest);
                    }
                })
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        ThreadUtils.executeByFixed(AppConstant.THREAD_SIZE, new ThreadUtils.SimpleTask<Boolean>() {
                            @Override
                            public Boolean doInBackground() {
                                ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
                                boolean isOk = false;
                                for (Contact contact : list) {
                                    try {
                                        if (contact == null) {
                                            //对象为null
                                            continue;
                                        }
                                        //注意删除的id
                                        ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI)
                                                .withSelection(ContactsContract.Contacts._ID + "=?", new String[]{String.valueOf(contact.getId())})
                                                .withYieldAllowed(true)
                                                .build());
                                        ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI)
                                                .withSelection(ContactsContract.RawContacts.CONTACT_ID + "=?", new String[]{String.valueOf(contact.getId())})
                                                .withYieldAllowed(true)
                                                .build());


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        continue;
                                    }
                                }
                                if (ops != null) {
                                    // 真正删除
                                    ContentProviderResult[] results = new ContentProviderResult[0];
                                    try {
                                        results = Utils.getApp().getContentResolver()
                                                .applyBatch(ContactsContract.AUTHORITY, ops);
                                        isOk = true;
                                        //更新保存数量, 先不清空
//                                        updateSaveDataCount(0);
//                                        return DaoUtlis.setUserSaveDataCount(0);


                                    } catch (Exception e) {
                                        e.printStackTrace();


                                    }
                                }
                                return isOk;
                            }

                            @Override
                            public void onSuccess(Boolean result) {
                                if (result) {
                                    showShortYes(context, getString(R.string.hint_delete_yes));
                                    clickEvent(AppConstant.CLICK.umeng_delete_contact);
                                } else {
                                    showShortNo(context, getString(R.string.hint_delete_no));
                                }
                            }


                        });

                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever,
                                         List<String> permissionsDenied) {
                        if (!permissionsDeniedForever.isEmpty()) {
                            showOpenAppSettingDialog();
                        }
                    }
                })
                .

                        request();


    }


    /**
     * 判断字符串是否为null,如果是则返回一个默认字符串: 无
     *
     * @param text
     * @return
     */
    public static String isEmpty(String text) {
        return isEmpty(text, AppConstant.HIHT_NO_STR);
    }

    /**
     * 判断字符串是否为null,如果是则返回一个默认字符串
     *
     * @param text
     * @return
     */
    public static String isEmpty(String text, @NonNull String def) {

        if (TextUtils.isEmpty(text)) {
            return def;
        } else {
            return text;
        }
    }


    /**
     * 导出数据到CSV文件,
     *
     * @param dataBeanList
     * @return
     */
    public static void exportCSVFile(final Context context, final PlatformBean platformBean, final List<DataBean> dataBeanList) {

        if (null == dataBeanList || null == platformBean || dataBeanList.isEmpty()) {
            showShort(context, getString(R.string.hint_no_data_2));
            return;
        }
        PermissionUtils.permission(PermissionConstants.STORAGE)
                .rationale(new PermissionUtils.OnRationaleListener() {
                    @Override
                    public void rationale(final ShouldRequest shouldRequest) {
                        showRationaleDialog(shouldRequest);
                    }
                })
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        ThreadUtils.executeByFixed(AppConstant.THREAD_SIZE, new ThreadUtils.SimpleTask<String>() {
                            @Override
                            public String doInBackground() {
                                String filePath = "";
                                try {
                                    //获取文件存储路径
                                    String appFileDirPath = getAppFileDirPath();
                                    //生成文件名称  金刚钻_百度地图_2018-06-26_123_111122121.csv
                                    String fileName = getString(R.string.app_name) + "_" + platformBean.name + dataBeanList.get(0).getCity() + dataBeanList.get(0).getKey() + "_" + TimeUtils.getNowString(new SimpleDateFormat("yyyyMMddHHmmss")) + "_" + dataBeanList.size() + ".csv";
                                    filePath = appFileDirPath + File.separator + fileName;
                                    StringBuffer sb = new StringBuffer();
                                    String dataSeparator = ",";
                                    //先写头部数据
                                    sb.append("来源,");
                                    sb.append("关键词,");
                                    sb.append("名称,");
                                    sb.append("固话,");
                                    sb.append("手机,");
                                    sb.append("省份,");
                                    sb.append("城市,");
                                    sb.append("区域,");
                                    sb.append("地址\n");
                                    //接着写数据内容
                                    for (int i = 0; i < dataBeanList.size(); i++) {
                                        try {
                                            DataBean dataBean = dataBeanList.get(i);
                                            sb.append(dataBean.source);
                                            sb.append(dataSeparator);
                                            sb.append(dataBean.key);
                                            sb.append(dataSeparator);
                                            sb.append(dataBean.name);
                                            sb.append(dataSeparator);
                                            sb.append(isEmpty(dataBean.tel));
                                            sb.append(dataSeparator);
                                            sb.append(isEmpty(dataBean.phone));
                                            sb.append(dataSeparator);
                                            sb.append(isEmpty(dataBean.province));
                                            sb.append(dataSeparator);
                                            sb.append(isEmpty(dataBean.city));
                                            sb.append(dataSeparator);
                                            sb.append(isEmpty(dataBean.district));
                                            sb.append(dataSeparator);
                                            sb.append(dataBean.address);
                                            sb.append("\n");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            continue;
                                        }
                                    }
                                    boolean isOk = FileIOUtils.writeFileFromString(filePath, sb.toString());
                                    if (isOk) {
                                        LogUtils.e("文件写入成功" + filePath);
                                    } else {
                                        LogUtils.e("文件写入失败" + filePath);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return filePath;
                            }

                            @Override
                            public void onSuccess(String filePath) {
                                if (FileUtils.isFileExists(filePath)) {
                                    showFileSaveToast(context, context.getString(R.string.hint_export_file_ok_path, filePath));
                                    clickEvent(AppConstant.CLICK.umeng_export_data);
                                } else {
                                    showShortNo(context, getString(R.string.hint_export_no));
                                }

                            }


                        });

                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever,
                                         List<String> permissionsDenied) {
                        if (!permissionsDeniedForever.isEmpty()) {
                            showOpenAppSettingDialog();
                        }
                    }
                })
                .request();


    }

    /**
     * 获取本app文件存储路径,没有则创建文件夹 ,  /diamond/file
     *
     * @return
     */
    public static String getAppFileDirPath() {

        String fileDirPath = "";

        if (SDCardUtils.isSDCardEnable()) {
            //sd卡可用
            List<String> sdCardPaths = SDCardUtils.getSDCardPaths();
            if (null != sdCardPaths && !sdCardPaths.isEmpty()) {
                String dir = sdCardPaths.get(0);

                File dirFile = new File(dir + File.separator + "diamond" + File.separator + "file");
                boolean orExistsDir = FileUtils.createOrExistsDir(dirFile);
                if (orExistsDir) {
                    fileDirPath = dirFile.getAbsolutePath();
                }
                LogUtils.e(orExistsDir ? "创建成功" + fileDirPath : "创建失败" + fileDirPath);
            }
        }
        return fileDirPath;
    }


    /**
     * 地址信息优化, 只处理 : 市+地区
     *
     * @param address
     * @param city
     * @param district
     * @return
     */
    public static String getAddress(String address, String city, String district) {
        return getAddress(address, "", city, district);
    }


    /**
     * 优化地址信息 处理 :省市区
     *
     * @param address
     * @param province
     * @param city
     * @param district
     * @return
     */
    public static String getAddress(String address, String province, String city, String district) {

        //地址信息显示优化,
        //1.百度地图返回的address字段中,有时候是省市区街详细位置,有时候没有省市区,只有XX街道XX号,所以要做下拼接处理

        StringBuilder sb = new StringBuilder();
        if (!address.contains(province)) {
            //地址不包含省, 拼接省到前面
            sb.append(province);
        }

        if (!address.contains(city)) {
            //地址不包含市, 拼接市到前面
            sb.append(city);
        }

        if (!address.contains(district)) {
            //地址不包区域, 拼接区域到前面
            sb.append(district);
        }

        //最后拼接地址
        sb.append(address);

        return sb.toString();


    }

    /**
     * 检测APP更新
     *
     * @param activity
     */
    public static void checkAppUpdate(final Activity activity) {
        String url = "https://api.bmob.cn/1/classes/app_version/bC5RAAAU";
        OkGo.<String>get(url).tag(activity)
                .headers("X-Bmob-Application-Id", AppConstant.BMOB_ID)
                .headers("X-Bmob-REST-API-Key", AppConstant.BMOB_KEY)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String json = response.body().toString();
                        AppVersionBean bean = new Gson().fromJson(json, AppVersionBean.class);
                        if (null != bean) {
                            int version_code = bean.getVersion_code();
                            int appVersionCode = AppUtils.getAppVersionCode();
                            if (version_code > appVersionCode && !TextUtils.isEmpty(bean.getDownloadUrl())) {
                                String update_content = bean.getUpdate_content();
                                String updateContent = update_content.replace("\\n", "\n");
                                //服务端版本大于当前版本 更新,暂时统一强制更新
                                StringBuilder sb = new StringBuilder();
                                sb.append("最新版本:");
                                sb.append(bean.getVersion_name());
                                sb.append("\n大小:");
                                sb.append(bean.getApp_size());
                                sb.append("\n更新内容:\n");
                                sb.append(updateContent);
                                AllenVersionChecker
                                        .getInstance()
                                        .downloadOnly(
                                                UIData.create().setDownloadUrl(bean.getDownloadUrl())
                                                        .setTitle("版本更新")
                                                        .setContent(sb.toString())


                                        )
                                        .setForceUpdateListener(new ForceUpdateListener() {
                                            @Override
                                            public void onShouldForceUpdate() {

                                            }
                                        })
                                        .setDownloadAPKPath(MyUtlis.getAppFileDirPath())
                                        .excuteMission(activity);
                            } else {

                                //服务端版本 等于或者小于当前版本,不更新
                            }


                        }


                    }
                });


    }

    /**
     * 设置采集范围 : 城市
     *
     * @param city 城市名
     */
    public static void setCollectCity(@NonNull String city) {
        SPUtils.getInstance().put(AppConstant.SPKey.COLLECT_CITY, city);
    }

    /**
     * 设置采集关键词 :
     *
     * @param key
     */
    public static void setCollectKey(@NonNull String key) {
        SPUtils.getInstance().put(AppConstant.SPKey.COLLECT_KEY, key);
    }


    /**
     * 获取采集范围 : 城市
     *
     * @retrun city 城市名
     */
    public static String getCollectCity() {
        return SPUtils.getInstance().getString(AppConstant.SPKey.COLLECT_CITY, "深圳");
    }

    /**
     * 获取采集关键词 :
     *
     * @retrun key
     */
    public static String getCollectKey() {
        return SPUtils.getInstance().getString(AppConstant.SPKey.COLLECT_KEY, "美食");
    }


    /**
     * 保存info数据
     *
     * @param infoJsonStr
     * @param callback
     */
    public static void saveInfoData(String infoJsonStr, StringCallback callback) {
        try {
            JsonBean_JuHeNews jsonBean_juHeNews = new Gson().fromJson(infoJsonStr, JsonBean_JuHeNews.class);

            JsonBean_JuHeNews.ResultBean resultBean = jsonBean_juHeNews.getResult();
            if (null != resultBean && null != resultBean.getData() && resultBean.getData().size() >= 10) {
                boolean deleteInfoBean = DaoUtlis.deleteInfoBean();
                LogUtils.e(deleteInfoBean ? "删除infoBean成功" : "删除infoBean失败");
                //1.获取menu数据
                final String loginUserId = MyUtlis.getLoginUserId();
                InfoBean infoBean = new InfoBean();
                infoBean.userId = loginUserId;
                infoBean.updateTime = TimeUtils.getNowMills();
                boolean addInfoBean = DaoUtlis.addInfoBean(infoBean);
                LogUtils.e(addInfoBean ? "添加infoBean成功" : "添加infoBean失败");

                String[] menuTitles = {"原上", "百度", "高德", "京东", "淘宝", "知乎", "唯品会", "美团", "新浪", "网易", "搜狐"};
                String[] menuDetailsUrl = {"http://www.yscoco.com", "http://baidu.com", "https://www.amap.com/"
                        , "https://www.jd.com"
                        , "https://www.taobao.com"
                        , "https://www.zhihu.com"
                        , "https://www.vip.com"
                        , "https://www.meituan.com"
                        , "http://www.sina.com.cn"
                        , "http://www.163.com"
                        , "https://www.sohu.com"

                };
                String[] menuImageUrl = {"http://www.yscoco.com/images/logo-blue.png"
                        , "https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo_top_ca79a146.png"
                        , "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEQAAABECAYAAAA4E5OyAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyRpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoTWFjaW50b3NoKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDo4RDlDOTA3RDJDQUExMUU3QUY3REU4MDc1M0MzNjY5RiIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDo4RDlDOTA3RTJDQUExMUU3QUY3REU4MDc1M0MzNjY5RiI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjhEOUM5MDdCMkNBQTExRTdBRjdERTgwNzUzQzM2NjlGIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjhEOUM5MDdDMkNBQTExRTdBRjdERTgwNzUzQzM2NjlGIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+RQc1VAAACpVJREFUeNrsnHtsFNcVh3+7O7NP73jttRdjG2MTkO0C5tHQQMDYBKdqQ9VKNBVSVKokopGo1KR/RG3TBKWKmoqmIQ2ksYiaiLZCghDRkkchQEpVWrUkBBsDxmCD8RO/1thee9f7mpmeu2uM117vy2PjR490NN7xzJ07355z7jn3XluFGOXzriOZEvwveGTX1z2yM2t9Z53J6PdDFeW+UzYbXBoNEpVkLgObrM+MOe8c6IUk+sPdMkjaT1pHWkV6hvSEYEl3xfI8LtoF57uOlrnk3t+3i7X5EsTh81pRjArjPolhSG2k60l/xAA5ersO0nE3gWlKCMiX9mOWQanv01ax+iEZMma4mEl3kj5NYHbT8dcExhvuQnW4kxX2j0q6xYbbdqlhNsAYKTrSl0nPEpismIBcsH+49bZYc8Yl9xkwe+Uh0nMEZXFEIOQm69rF60d8sluN2S/ZpH8nKJlhgZBlmO6ITae98qAGc0dySI8QFM0YIG65/xOn3GPC3BM2Ej0fAoSC6Fq72FCKuSu7yErmDwNxSj37R+YYc1CYZzwXAHLRftzSI7UW4f+yg6yEV3tl189F+FSz9S2dfhV2V2jw3NmoA6eVtIzzyAOPzUYQHYNq7L+mw/6LIpweP94q4+msJ9ptmzm3PJA3m0DUOTTYV63HoXodvH3dACsALfNQktEby+1f5TxwGmcDiM+7OOwlEH9r1gaLDXsLRUhyk9RMLDBJyE2SYmlmCTeTs1KJ3vxkK4/fVRtwrnOoTpXpZBtV/ryeYGQHTpVk+GJt0sLNRBBeyhD+VMdj7xUTavtGJNbMPZqv0iBqAdJzhk/HAcQ0o4D0Ukx8p1rG3ioZbc5RtafXDTRepkLfGgKDSXGGP+ZnzAggLQMIQGAw+sPNYrgcBOMKkJIBZCwKDQqCiEyjNDuANDnScby2BGcaJfjGe6feTiJ2jeLF/DEwmGyMwzqmLZDqrhz89frDuNC2OPKFnY2UcNwKjCTIXBL2kjjiR0BUHtElq1TxJ6r8lRcoiLmjXucv3AVZmxrTiHGsXo09lRqc74jSH5nMpeU6WUdH0E2yC8K/HGn70yqkxzHVxWnViU6MxQaRU+noUv24vx8ki/7zNRmvV8q40RdDg35fMF64+gIJF7Lyx720KA2wGVUzw2XukHGVX5ax75KMrsEYb/K4gAYaSbx0Q3J60DIiWHdpVvyWP+VAGvuBNy7KeO+qDGc87j3QQ1G2OphrCDS0LiiMCINJ2QJMXyBVduC1Chnv18kQ453Iv9MG3K4NZqFmikc5SwlG5ARbTayKMxOwEFmWJzGCAKdbOPz2soTTzQk8hPWtvZ7qkqGbWQYaAwwmD9rIkHgZ8b4eN+DoTghIEj1pPCh+WY0P7KvxemsZKgbMCRYqlJ831wAO+z0Yucvpq49tDnx9mhsDDtf9dRmnqMWBznXY07oZDW5r4g35PME0fHAg+NkoAAuXxQwjEFDn+xJ6tCJA7L4k7LtdivK2jej2T3DifrA/OKz6PPdg5BYBmti7ypNHrU33Tz2Qm+40soYyHOhYC7fET5wscw/mJtLQhLee4C5cHhcMJl8jGAZOnjogFd0c3q75AY52FUFSag8AC5xt9SyS3oORt5J6GD/ojfN8CXcjZiCsm5+18th7VY+z7ayTK5QBwYYBNqSyofWuaCl7zluREIxgQTeJQFiVebRBhzer9bjaq/AqJ0uyWLLFkq6RMBYxy9Am1KRBI2NNgvEjIhBWbJXX6FF+TY8W5yTMMrL0m6XhnhFDI091z6IVwWOCstbmh3YC3VVHyvQW8r1QS37lYTipMLtREQbGyuBc6ASkZALuEhEIky0PaHF8/S3szLNT1arQxhlWst+qIncZ0XEWKxgM7cS3pJRk+CcPiFqthi1FwE8Wd+LY2nqssrgm1ls2mcOGVVkKhZGnDAwzpeorrZMIJDD66XUw6PVYkuTBoTUNeKWwDQIX58I4A9B0NTjDNVJYfsFg6JXZhbF+nh+aCWYBMYUfQRCgobSZPWtbdg9OrL+JbwtfxvYEvxeovwj0dY56MhfMQPXKbUkpnWD8iBkIc53k5OThz2laP8qz3sXB7D3I4bsiTOg4g8GTzYqHNEjDd15RMC1XUIqnCggTnU4LkzF01XOT6TLO5L6InaknoMGoafH+OwSjkhIZ92i6wUJNYRhWnYxlKeLUAQkELbM54DohiZDai5fS38fJ3JexSn8zeLL7djDHGD1kB2BQbZKUovhIvoHSdSWKiLiAsNl5iyU57O8Kdc34aOGv8D3pL8FUfPT+Vjbdt2DppMCYSLk/JlNVi41x3aAnAxH0ToiQwtCVscFYjQ9QPBYGm+kSrJgsKUlvgjqGZZGoQEx9L8V9k5G+/C51OvxSOF8O06nsQoKRNmkwsvTdWCU/TyPZFLvMyC88RQhfb6TpRwHxUM3S1wXIk7dFvNR6SbG2Ei6DeE4Ns3FsRWrTjVpkcdPQW3se+OITIMH522hSlnbx/gNhkmTkA2AiuoxnCBCDwaBc/4IKRp/CFnJ5egC56zoj14Y5tQSBH9qzwOZFJTFkMqhg8D84kf8MHrX8V5EXWGRsR66hY3oACQDQMNcJndmy8J5Q6xiSgvReHHniMyxLbsYfF+/CHx74JebxE3OjTQrGD0WA3HUdLX8vYRPCALkLw2q851KPpfwb/1z2FJ60fUhJVWJB95G0KkWBcCJXkNCNGoTGCotZi64eN9hKYKqWfidJw0sJ+TYXDm2/CYsxe8wGciPVeK8sOoGtthr8rH47alxZcfVjueBFh2+5ckBcwotUdCA1bqvoeRUq2R3iOoJJi74BDwwa37B1fCVTj/d+uBpGUykiLfLnm4GjGQ4caOTw1k0b3FL0RDzLAFQN7gIGFYKhlmXmMreUomsycNBpNUjh3YE505XzHDi4IwepMe7t06hk7MjtxvGHb6DYOhD1+kJB2R3pek5yMSCVSjZqMetgIgtZJnTi8HcuwayPP0xlGXx4d3UT3ljegjTt+BXsUmULZhg4qZH19h9KNqpRq7CtoA0fP34JqUaJCtzEv8Ut5EIni2/h+3ljpy5ZqwVmhS2El04xW/6Y5ZPss1INP5rnCIRd9uezRhvVPGoBoiiSShRrxaGfg5/9fj8kefxtkwIn4TerHHg8x42fVgqodQTdb4kgI0nBpXq1KrB1YjcnWNLZH/kepnNPTlatwWbcmPLjLMSxkSk8MDGwPsQSvzVWL04/Ykd5nQlvXjPjQYUL51SD//qmQn3HXcavkW4f+lqnXNgLcxxHGgamhoMpyRKAxvTV+TKeKpJwnu2h8Sr0fDZqasVnhxMzspIaOpRjGguDxqxMo9Eg38oFXEYpSTd5K4vzDadGZ6q/QPAfCMwpMfCS18hL3xiTupOVsIF/K6tL5woMlohlmLzbyDo6w9YyBOUKHb41F6BQEihnmj0/3pBvOBaxuCMo/2I1E2nTbIWh4yQxW/A8UVJgeDumapegXKDDKtKDsw1GqsHXQG5SsLHAcDiu8p+g3CFlQ/E60k+Bmf1/M5J1/u4FgufZLUXaPIJxY9y4Eq0hgnKODt+k5C2Xjt8l3UzK/vA5a1rHCAqYJl7spRHkPAXP3ZR0xVSi/E+AAQAmx7WFUqnKJgAAAABJRU5ErkJggg=="
                        , "https://ss0.bdstatic.com/-0U0bnSm1A5BphGlnYG/tam-ogel/299c55e31d7f50ae4dc85faa90d6f445_121_121.jpg"
                        , "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=961224733,2320658306&fm=179&app=42&f=JPEG?w=121&h=140"
                        , "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=4094533855,2791674877&fm=58&bpow=500&bpoh=500"
                        , "https://ss0.bdstatic.com/-0U0bnSm1A5BphGlnYG/tam-ogel/596dcc03554a7df640fdbb60f40a4dcd_121_121.jpg"
                        , "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1829525744,3963438567&fm=58&s=80035F32C570EE334EE31C56020010FA&bpow=121&bpoh=75"
                        , "https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=2957043501,255162184&fm=58&s=2CA2A91ACCB44C801E7194D6010000B1"
                        , "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=2490895003,2642262494&fm=58&s=2370E532CFA568134854D6FC0300F020"
                        , "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1767309032,288351360&fm=58&s=61EDBE468CA449131EA6F27A0300F07B&bpow=121&bpoh=75"

                };

                int menuCount = new Random().nextInt(100) % 3 == 0 ? 5 : 10;
                List<InfoMenuBean> infoMenuBeanList = new ArrayList<>();
                for (int a = 0; a < menuCount; a++) {
                    InfoMenuBean bean = new InfoMenuBean();
                    bean.menuId = String.valueOf(a);
                    bean.actionType = 1;
                    bean.title = menuTitles[a];
//            bean.content = "Banner 内容" + a;
                    bean.detailsUrl = menuDetailsUrl[a];
                    bean.userId = loginUserId;
                    bean.imageUrl = menuImageUrl[a];
                    infoMenuBeanList.add(bean);
                }
                DaoUtlis.addInfoMenuBean(infoMenuBeanList);


                //转成banner数据和 datalist数据 + notice数据
                int bannerCount = new Random().nextInt(5) + 3;
                //banner 数量在3-8  剩下的都是datalist ,而notice数据是全部
                int dataListCount = resultBean.getData().size() - bannerCount;

                List<InfoNoticeBean> infoNoticeBeanList = new ArrayList<>();
                List<InfoBannerBean> infoBannerBeanList = new ArrayList<>();
                List<InfoDataBean> infoDataBeanList = new ArrayList<>();
                for (int i = 0; i < resultBean.getData().size(); i++) {
                    JsonBean_JuHeNews.ResultBean.DataBean newsData = resultBean.getData().get(i);
                    //创建banner数据
                    if (i < bannerCount) {
                        InfoBannerBean bannerBean = new InfoBannerBean();
                        bannerBean.bannerId = newsData.getUniquekey();
                        bannerBean.actionType = 1;
                        bannerBean.title = newsData.getTitle();
                        bannerBean.content = newsData.getAuthor_name();
                        bannerBean.detailsUrl = newsData.getUrl();
                        bannerBean.userId = loginUserId;
                        bannerBean.imageUrl = newsData.getThumbnail_pic_s();
                        infoBannerBeanList.add(bannerBean);
                    } else {
                        //创建dataList数据

                        InfoDataBean bean = new InfoDataBean();
                        bean.infoDataId = newsData.getUniquekey();
                        bean.actionType = 1;
                        bean.title = newsData.getTitle();
                        bean.content = newsData.getAuthor_name();
                        bean.detailsUrl = newsData.getUrl();
                        bean.imageUrl = newsData.getThumbnail_pic_s();
                        bean.userId = loginUserId;
                        infoDataBeanList.add(bean);
                    }

                    //创建notice数据
                    InfoNoticeBean bean = new InfoNoticeBean();
                    bean.noticeId = newsData.getUniquekey();
                    bean.actionType = 1;
                    bean.title = newsData.getTitle();
                    bean.content = newsData.getAuthor_name();
                    bean.detailsUrl = newsData.getUrl();
                    bean.userId = loginUserId;
                    infoNoticeBeanList.add(bean);
                }
                DaoUtlis.addBannerBean(infoBannerBeanList);
                DaoUtlis.addInfoDataBean(infoDataBeanList);
                DaoUtlis.addInfoNoticeBean(infoNoticeBeanList);

            }

            if (null != callback)
                callback.onSuccess(null);
            LogUtils.e("saveInfoData Success");
        } catch (Exception e) {
            e.printStackTrace();

            LogUtils.e("saveInfoData Error");
            callback.onError(null);
        }

    }

    /**
     * 通过聚合api, 获取新闻类, 财经相关新闻数据 , 整合数据成infobean需要的数据,存入db
     */
    public static void getInfoData(final StringCallback callback) {
        //2.获取banner ,notice , dataList数据
        String url = "http://v.juhe.cn/toutiao/index";
        OkGo.<String>get(url)
                .params("type", "caijing")
                .params("key", "0656181eff9766e313f7130f035ec520").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String infoJsonStr = response.body().toString();
                LogUtils.e("http infoData onSuccess");
                saveInfoData(infoJsonStr, callback);
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                //当网络请求出错时,判断是否有缓存infoData,如果没有,使用默认数据

                LogUtils.e("http infoData error");
                InfoBean infoBean = DaoUtlis.getInfoBean();
                if (null == infoBean
                        || null == infoBean.getInfoNoticeList()
                        || null == infoBean.getInfoMenuList()
                        || null == infoBean.getInfoBannerList()
                        ) {
                    LogUtils.e("infoBean=null");
                    saveInfoData(AppConstant.infoJsonStr, callback);
                } else {

                    if (null != callback)
                        callback.onSuccess(null);
                }
            }
        });


    }


    /**
     * 点击事件统计
     *
     * @param eventId
     */
    public static void clickEvent(String eventId) {
        MobclickAgent.onEvent(Utils.getApp(), eventId);
    }

    /**
     * 校验过程：
     * 1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
     * 2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，将个位十位数字相加，即将其减去9），再求和。
     * 3、将奇数位总和加上偶数位总和，结果应该可以被10整除。
     * 校验银行卡卡号
     */
    public static boolean checkBankCard(String bankCard) {
        if (bankCard.length() < 15 || bankCard.length() > 19) {
            return false;
        }
        char bit = getBankCardCheckCode(bankCard.substring(0, bankCard.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return bankCard.charAt(bankCard.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhn 校验算法获得校验位
     */
    public static char getBankCardCheckCode(String nonCheckCodeBankCard) {
        if (nonCheckCodeBankCard == null || nonCheckCodeBankCard.trim().length() == 0
                || !nonCheckCodeBankCard.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeBankCard.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }


    /**
     * 获取交易大厅测试数据
     *
     * @param mPages
     * @param dataType
     * @return
     */
    public static List<TradingDataBean> getTestTradingData(int mPages, int dataType) {
        List<TradingDataBean> list = new ArrayList<>();
        int start = mPages * 20;
        int end = start + 20;
        for (int i = start; i < end; i++) {
            TradingDataBean bean = new TradingDataBean();
//            bean.dataType = dataType;
//            bean.industry = "IT - " + (dataType == 0 ? "买账号数据" : "卖账号数据");
//            bean.friendCount = (i * 100);
//            bean.price = String.valueOf(i * 212);
//            bean.city = "深圳";
//            bean.describe = "描述" + i;
            list.add(bean);
        }

        return list;
    }

    /**
     * 刷新交易大厅页面数据 ， dataType ，买账号||卖账号
     *
     * @param dataType
     */
    public static void eventUpdateTradingData(int dataType) {
        MessageEvent event = new MessageEvent();
        event.eventType = MessageEvent.UPDATE_TRADING_DATA;
        event.intValue = dataType;
        EventBus.getDefault().post(event);
    }

    /**
     * 刷新交易大厅页面数据 ， dataType ，买账号+卖账号
     *
     * @param
     */
    public static void eventUpdateTradingData() {
        MessageEvent event = new MessageEvent();
        event.eventType = MessageEvent.UPDATE_TRADING_DATA;
        event.intValue = 0;
        EventBus.getDefault().post(event);
    }

    /**
     * 刷新我的交易页面数据
     */
    public static void eventUpdateMyTradingData() {
        MessageEvent event = new MessageEvent();
        event.eventType = MessageEvent.UPDATE_MY_TRADING_DATA;
        EventBus.getDefault().post(event);
    }


    /**
     * 更新登录用户信息
     */
    public static void eventUpdateLoginUserInfo() {
        MessageEvent event = new MessageEvent();
        event.eventType = MessageEvent.UPDATE_LOGIN_USER_INFO;
        EventBus.getDefault().post(event);
    }

    /**
     * 添加银行卡成功
     */
    public static void eventAddBankCardOK() {
        MessageEvent event = new MessageEvent();
        event.eventType = MessageEvent.ADD_BANK_CARD_OK;
        EventBus.getDefault().post(event);
    }

    /**
     * 提现成功
     */
    public static void eventTiXianOK() {
        MessageEvent event = new MessageEvent();
        event.eventType = MessageEvent.TIXIAN_OK;
        EventBus.getDefault().post(event);
    }


    /**
     * 延时1s关闭窗口
     *
     * @param activity
     */
    public static void finishActivityDelay(@NonNull final Activity activity) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityUtils.finishActivity(activity);
            }
        }, 1000);

    }

    /**
     * 立即关闭窗口
     *
     * @param activity
     */
    public static void finishActivity(@NonNull final Activity activity) {
        ActivityUtils.finishActivity(activity);
    }


    /**
     * 获取银行卡号码后四位
     */
    public static String getBankCardAfter4(String cardCode) {
        LogUtils.e("cardCode=" + cardCode);
        try {
            if (!TextUtils.isEmpty(cardCode) && cardCode.length() >= 4) {

                return cardCode.substring(cardCode.length() - 4, cardCode.length());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "****";
        }
        return cardCode;
    }


    /**
     * 显示暗屏
     *
     * @param activity
     */
    public static void showDarkScreen(@Nullable Activity activity) {
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = 0.3f;
        activity.getWindow().setAttributes(params);
    }

    /**
     * 显示亮屏
     *
     * @param activity
     */
    public static void showBrightScreen(@Nullable Activity activity) {
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = 1.0f;
        activity.getWindow().setAttributes(params);
    }

    /**
     * 打开客服QQ聊天
     */
    public static void openServiceQQ(Context context) {


        if (AppUtils.isAppInstalled(AppConstant.PACKAGENAME_QQ)) {

            try {
                String url = "mqqwpa://im/chat?chat_type=wpa&uin="+context.getString(R.string.service_qq);
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.showShort("打开QQ失败");
            }
        } else {

            ToastUtils.showShort("请先安装QQ");

        }

    }
}
