package com.zmy.diamond.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.alertview.OnItemClickListener;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.Utils;
import com.github.tamir7.contacts.Contacts;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.zmy.diamond.R;
import com.zmy.diamond.utli.AppConstant;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.DaoMaster;
import com.zmy.diamond.utli.bean.DaoSession;
import com.zmy.diamond.utli.dao.DBHelper;
import com.zmy.diamond.utli.view.MyAlertView;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;


/**
 * 程序入口
 * Created by zhangmengyun on 2018/6/11.
 */

public class BaseApp extends Application {

    /**
     * 是否需要停止采集的 全局标志, 在循环采集发起请求时,每次判断该值,判断是否要停止,如果=true.则结束循环.停止采集
     * 停止以后将此值重置=false
     */
    public static boolean isStopCollect = false;


    /**
     * 本次采集的数据量， 默认=0， 进行中会一直增加，当停止时需要请求接口，更新服务端数量。完成后归零
     */
    public static int currentDownloadDataCount = 0;

    public static LinkedList<Activity> activities = new LinkedList<>();


    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }


    public static void finishActivities() {
        if (null != activities) {
            for (int i = 0; i < activities.size(); i++) {
                activities.get(i).finish();
            }
        }
    }

    public static void finishOtherActivities(@NonNull final Class<? extends Activity> clz) {
        if (null != activities) {
            for (int i = 0; i < activities.size(); i++) {
                Activity activity = activities.get(i);
                if (!activity.getClass().equals(clz)) {
                    activity.finish();
                }

            }
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        insatnce = this;
        Utils.init(this);
        LogUtils.getConfig().setLogSwitch(AppConstant.LOG);
        initDatabase();
        Contacts.initialize(this);
        initUMeng();

        initOKHttp();
    }

    /**
     * 初始化okhttpgo
     */
    private void initOKHttp() {


        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
//log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
//log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);
        //全局的读取超时时间
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
//全局的写入超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
//全局的连接超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
//使用sp保持cookie，如果cookie不过期，则一直有效
//        builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));
//使用数据库保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));
//使用内存保持cookie，app退出后，cookie消失
//        builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));


        //方法一：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
//方法二：自定义信任规则，校验服务端证书
//        HttpsUtils.SSLParams sslParams2 = HttpsUtils.getSslSocketFactory(new SafeTrustManager());
//方法三：使用预埋证书，校验服务端证书（自签名证书）
//        HttpsUtils.SSLParams sslParams3 = HttpsUtils.getSslSocketFactory(getAssets().open("srca.cer"));
//方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
//        HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
//配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
//        builder.hostnameVerifier(new SafeHostnameVerifier());


        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
        HttpHeaders headers = new HttpHeaders();
        headers.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");    //header不支持中文，不允许有特殊字符
//        headers.put("commonHeaderKey2", "commonHeaderValue2");
//        HttpParams params = new HttpParams();
//        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
//        params.put("commonParamsKey2", "这里支持中文参数");
//-------------------------------------------------------------------------------------//

        OkGo.getInstance().init(this)                       //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(0)                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                .addCommonHeaders(headers);                    //全局公共头
//                .addCommonParams(params);                       //全局公共参数


    }


    /**
     * 初始化umeng
     */
    private void initUMeng() {
            /*
    注意：如果您已经在AndroidManifest.xml中配置过appkey和channel值，可以调用此版本初始化函数。
    */
        String pushSecret = "";
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, pushSecret);
        /**
         * 设置组件化的Log开关
         * 参数: boolean 默认为false，如需查看LOG设置为true
         */
        UMConfigure.setLogEnabled(AppConstant.DEBUG);
        //设置场景类型 默认普通
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

        PushAgent mPushAgent = PushAgent.getInstance(this);
//注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                LogUtils.e("Push注册成功=" + deviceToken);
                SPUtils.getInstance().put(AppConstant.SPKey.PUSH_DEVICE_TOKEN, deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                LogUtils.e("Push注册失败=" + s + "-" + s1);
            }
        });
    }

    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    public static BaseApp insatnce;

    public static BaseApp getInstance() {
        return insatnce;
    }

    /**
     * 初始化数据库
     */
    private void initDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
//        DaoMaster.DevOpenHelper mHelpter = new DaoMaster.DevOpenHelper(this,"notes-db");
        DBHelper mHelper = new DBHelper(this);//为数据库升级封装过的使用方式
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    /**
     * 获取dao
     *
     * @return
     */
    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    /**
     * 获取db
     *
     * @return
     */
    public SQLiteDatabase getDb() {
        return db;
    }


    /**
     * 当前本次计时时长 单位毫秒
     */
    public long currentUseTime = 0;

    /**
     * 开始使用计时 , 1小时限制, 超过1小时,需要输入体验码, 输入过体验码的以后都不进行限制
     * 1.   boolean useLimit :是否需要计时限制使用
     * 2.   long  useTime : 已经使用的时长, 单位毫秒  1小时=60分钟=3600秒=1000*3600=3600000
     * 3.
     */
    public void startUseTiming() {

        boolean useLimit = SPUtils.getInstance().getBoolean(AppConstant.SPKey.USE_LIMIT, true);
        if (useLimit) {
            //需要计时限制
            //1.先取出上次的计时时间
            long useTime = SPUtils.getInstance().getLong(AppConstant.SPKey.USE_TIME);
            currentUseTime = useTime;
            if (AppConstant.DEBUG) {
                currentUseTime = 0;
            }
            //启动计时
//            startUseTime();
            handler.postDelayed(useTimeRunnable, USE_TIME_INTERVAL);

        } else {
            //不限制使用, 不需要做任何处理
        }


    }

    public Handler handler = new Handler();

    /**
     * 使用限制的提示框
     */
    MyAlertView alertView;
    /**
     * 使用限制的最大时长  : 60分钟
     */
    long USE_TIME_MAX = 1000 * 60 * 60;
    /**
     * 多久计时1次: 10秒
     */
    long USE_TIME_INTERVAL = 1000 * 10;
    Runnable useTimeRunnable = new Runnable() {
        @Override
        public void run() {
            //每10秒计时一次
            currentUseTime += (1000 * 10);
//            LogUtils.e("计时=" + currentUseTime);
            SPUtils.getInstance().put(AppConstant.SPKey.USE_TIME, currentUseTime);
            if (currentUseTime >= USE_TIME_MAX) {
                //超出使用限制

                final Activity topActivity = ActivityUtils.getTopActivity();
                if (null != topActivity) {
                    //提示用户,
                    View view = View.inflate(getInstance(), R.layout.view_use_limit, null);
                    TextView tv_ttf_wechat = view.findViewById(R.id.tv_ttf_wechat);
                    tv_ttf_wechat.setTypeface(MyUtlis.getTTF());
                    view.findViewById(R.id.ll_wechat).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //复制微信
                            MyUtlis.copyText(getString(R.string.limit_wechat));
                            MyUtlis.showShort(topActivity, getString(R.string.hint_wechat_copy));
                        }
                    });
                    final EditText edit_code = (EditText) view.findViewById(R.id.edit_code);

                    alertView = new MyAlertView(getString(R.string.hint_text), getString(R.string.hint_use_limit), null, new String[]{getString(R.string.hint_confirm)}, null, topActivity,
                            MyAlertView.Style.Alert, new OnItemClickListener() {
                        @Override
                        public void onItemClick(Object o, int position) {
                            //0=完成
                            int code = 0;
                            try {
                                code = Integer.valueOf(edit_code.getText().toString().trim());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (code == AppConstant.LIMIT_CODE) {
                                //解除限制
                                KeyboardUtils.hideSoftInput(edit_code);
                                SPUtils.getInstance().put(AppConstant.SPKey.USE_LIMIT, false);
                                alertView.dismiss();
                            } else {
                                MyUtlis.showShort(topActivity, getString(R.string.hint_code_error));
                            }

                        }
                    }).addExtView(view);

                    alertView.show();

                } else {
                    //继续计时
                    handler.postDelayed(this, USE_TIME_INTERVAL);
                }

            } else {
                //继续计时
                handler.postDelayed(this, USE_TIME_INTERVAL);
            }

        }
    };

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
