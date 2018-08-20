package com.zmy.diamond.utli;

import com.zmy.diamond.R;

/**
 * Created by zhangmengyun on 2018/6/15.
 */

public class AppConstant {
    /**
     * debug 模式=true ,发布版本一定要关闭
     */
    public final static boolean DEBUG = false;

    /**
     * 日志开关
     */
    public final static boolean LOG = true;


    /**
     * 是否开启体验模式（使用时间计时限制） 默认false
     */
    public final static boolean isExperienceMode = false;


    //白金=1 黄金=2
    public static final int VIP_GRADE_1 = 1;
    public static final int VIP_GRADE_2 = 2;


    /**
     * 请求HTTP 成功的响应吗
     */
    public static final int CODE_SUCCESS = 200;


    /**
     * APP 错误码
     */
    public static class Code {
        public static String database_error = "1";

    }


    /**
     * 接口域名
     */
//    public final static String HOST = "http://192.168.3.162:8080";
//
    public static String HOST = "http://139.199.154.179:8080";

    /**
     * Bmob id  or key
     */
    public static final String BMOB_ID = "261470dea509b6b577ae94c9512aed73";
    public static final String BMOB_KEY = "bea14c950b720cc1aede589619596a05";
    /**
     * 友盟Key
     */
    public static final String UMENG_KEY = "5b442b58a40fa3291200003a";

    /**
     * 存储到通讯录时, 统一拼接此后缀,
     */
    public final static String CONTACTS_SUFFIX = "_jgz";
    /**
     * 软件使用限制的 code  邀请码,
     */
    public static final int LIMIT_CODE = 2018;
    /**
     * 默认的userid
     */
    public static final String DEFAULT_USER_ID = "0";

    public static final String DEFAULT_TOKEN = "";

    /**
     * 数据的UI加载速度等级  1-2-3 , 1最快3最慢
     */
    public static int LOAD_DATA_SPEED_LEVEL = AppConstant.LOAD_DATA_SPEED_LEVEL_MIDDLE;

    public static final int LOAD_DATA_SPEED_LEVEL_HIGHT = 1;
    public static final int LOAD_DATA_SPEED_LEVEL_MIDDLE = 2;
    public static final int LOAD_DATA_SPEED_LEVEL_LOW = 3;


    /**
     * 交易大厅，发布&修改 账户编辑页面相关操作
     */
    public static final int ACTION_ACCOUNT_RELEASE = 1;//发布
    public static final int ACTION_ACCOUNT_EDEITE = 2;//编辑

    public static final String PACKAGENAME_QQ = "com.tencent.mobileqq";
    public static final String PACKAGENAME_VX = "com.tencent.mm";

    //买账户
    public static final int DATA_TYPE_BUY_ACCOUNT = 2;
    //卖账户
    public static final int DATA_TYPE_SELL_ACCOUNT = 1;


    /**
     * 点击操作类型 打开web显示url
     */
    public static int CLICK_ACTION_TYPE_OPEN_WEB_URL = 1;
    /**
     * 点击操作类型  打开列表请求data显示data
     */
    public static int CLICK_ACTION_TYPE_OPEN_LIST_DATA = 2;

    /**
     * 固定线程池大小
     */
    public static final int THREAD_SIZE = 10;
    /**
     * 每日签到积分 增加数
     */
    public static final int DAY_SIGN_INTEGRAL = 10;
    /**
     * string no
     */
    public static final String HIHT_NO_STR = "无";


    /**
     * 全部数据, 即有手机或者有固话都可以
     */
    public static final int COLLECT_DATA_PHONE_TYPE_ALL = 0;
    /**
     * 必须手机和固话都有才可以
     */
    public static final int COLLECT_DATA_PHONE_TYPE_PHONE_TEL = 1;
    /**
     * 必须有手机
     */
    public static final int COLLECT_DATA_PHONE_TYPE_PHONE = 2;


    /**
     * 营销数据 电话 过滤显示类型
     * 0=全部 默认
     * 1=手机
     * 2=固话
     */
    public static final int SHOW_TYPE_PHONE = 1;
    public static final int SHOW_TYPE_TEL = 2;
    public static final int SHOW_TYPE_ALL = 0;

    /**
     * API接口
     */
    public static class Api {

        /**
         * 获取验证码
         * 参数: 无
         * 返回: {"msg":"成功","code":200,"data":"5811"}
         */
        public static final String getCode = "/poi/api/getCode";
        /**
         * 注册
         * 参数: String macAddress=mac地址 String code=验证码
         * 返回: {"msg":"成功","code":200,"data":"null"}
         */
//        public static final String register = "/poi/api/register";

        /**
         * 登录
         * 参数: macAddress=mac地址
         * 返回: LoginResponseBean.class
         */
        public static final String login = HOST + "/poi/api/login";


        /**
         * 保存次数 更新
         * token  : 从login接口获取的
         * saveNumber: 本次新增的保存次数 ,服务端会+
         * 返回: LoginResponseBean.class
         */
        public static final String saveNumberUpdate = HOST + "/poi/api/saveNumberUpdate";
        /**
         * 采集下载数据 条数更新
         * token  : 从login接口获取的
         * downNumber: 本次新增的采集下载次数 ,服务端会+
         * 返回: LoginResponseBean.class
         */
        public static final String downNumberUpdate = HOST + "/poi/api/downNumberUpdate";
        /**
         * 签到
         * token  : 从login接口获取的
         * 返回: LoginResponseBean.class
         */
        public static final String signIn = HOST + "/poi/api/signIn";
        /**
         * 意见反馈
         * token  : 从login接口获取的
         * message  : 反馈内容
         * retrun : {"msg":"成功","code":200,"data":null}
         */
        public static final String opinion = HOST + "/poi/api/opinion";
        /**
         * 加入VIP
         * token  : 从login接口获取的
         * grade:   int （1或者2）  1=黄金会员2=白金会员
         * recomCode: String  （推荐码可有可无）
         * retrun:
         */
        public static final String joinVIP = HOST + "/poi/api/joinVIP";
        /**
         * 关于APP内容
         * token  : 从login接口获取的
         * retrun: {"msg":"成功","code":200,"data":{"aboutAppContent":"xxxxxxxxxx","appName":"精准抓潛","serviceProtocol":"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx","version":"v-1.0"}}
         */
        public static final String aboutApp = HOST + "/poi/api/aboutApp";


        /**
         * paypayzhu 支付接口
         * 文档: https://www.paypayzhu.com/doc/#_4
         */
        public static final String paypayzhuPay = "https://www.paypayzhu.com/api/pay";

        /**
         * 订单查询
         * token : 从login接口获取的
         * orderId :订单id
         * retrun:
         */
        public static final String orderQuery = HOST + "/poi/api/orderQuery";
        /**
         * 发布交易
         * token : 从login接口获取的
         * retrun:
         */
        public static final String releaseTrade = HOST + "/poi/api/releaseTrade";


        /**
         * 获取交易列表数据
         * token
         * action
         * page
         */
        public static final String getTrade = HOST + "/poi/api/getTrade";


        /**
         * 钱包接口
         * token、action、amount、card_id【1为提现】
         * token、action、page【2为钱包明细】
         * token、action、card、phone_number、name、bank_name【3为绑定银行卡】
         * token、action【4获取绑定的银行卡】、
         * token、action、card_id【5为删除银行卡】
         */
        public static final String wallet = HOST + "/poi/api/wallet";


        /**
         * token、version（当前版本）
         */
        public static final String updateApp = HOST + "/poi/api/version";


        /**
         * 操作发布
         * ----------------------------------（卖  action="1"）
         * action、token、weixin、price、trade_describe、industry、city、friend_number ， type
         * ----------------------------------（修  action="2"）
         * action、token、id、weixin、describe、price、industry、city、friend_number
         * ---------------------------------（删  action="3"）
         * action、token、id
         * ----------------------------------(查  action="4")
         * action、token
         */
        public static final String operationPublish = HOST + "/poi/api/operationPublish";
    }


    /**
     *注意事项
     * 1.百度地图
     *      1.百度地图android SDK下载包的服务有如下,但并不是所有功能都已使用:
     *          全量定位-基础地图-检索功能-lbs云检索-计算工具-周边雷达-全景图功能
     */


    /**
     * Intent Extra Key
     */
    public static class ExtraKey {

        /**
         * webview url value
         */
        public static final String URL_HTML = "url_html";
        /**
         * webview actionbar title value
         */
        public static final String TITLE = "title";
        /**
         * webview data TYPE   0=url 1=htmlcode
         */
        public static final String DATA_TYPE = "datatype";

        /**
         * webview data  value =object
         */
        public static final String DATA = "data";

        /**
         * data bean
         */
        public static final String DATA_BEAN = "data_bean";
        public static final String COLLECT_ID = "collectId";
        public static final String PLATFORM_ID = "platformId";
        public static final String USER_ID = "userId";
        public static final String CITY = "city";


        /**
         * 数据在adapter里的位置 value =int
         */
        public static final String DATA_POSITION = "adapterPosition";


        /**
         * 操作类型
         */
        public static final String ACTION_TYPE = "action_type";


        /**
         * is Show Blue ActionBar value
         */
        public static String IS_SHOW_BLUE_ACTIONBAR = "isShowBlueActionBar";
        /**
         * value =string
         */
        public static String KEY = "key";
    }

    /**
     * SharedPreferences key
     */
    public static class SPKey {
        /**
         * 最后一个登录的用户 , value= phone 或者是 userid
         */
        public static final String LAST_LOGIN_USER = "last_login_user_id";

        /**
         * 登录token
         */
        public static final String TOKEN = "token";
        /**
         * 最后一个选择的平台索引, 注意不是id
         */
        public static final String LAST_PLATFORM_INDEX = "last_platform_index";
        /**
         * 采集数据的 . 电话类型,  value=int
         * COLLECT_DATA_PHONE_TYPE_ALL
         * COLLECT_DATA_PHONE_TYPE_PHONE_TEL
         * COLLECT_DATA_PHONE_TYPE_PHONE
         */
        public static final String COLLECT_DATA_PHONE_TYPE = "collect_data_phone_type";
        /**
         * 采集范围 : 城市
         */
        public static final String COLLECT_CITY = "collect_city";
        /**
         * 采集关键词 : key
         */
        public static final String COLLECT_KEY = "collect_key";
        /**
         * 是否启用计时限制使用 value = boolean  true=限制false=不限制
         */
        public static final String USE_LIMIT = "useLimit";
        /**
         * 已经使用的时间 , 在限制使用的情况下, 每次计时会累计  value =long  单位毫秒
         */
        public static final String USE_TIME = "useTime";


        /**
         * 推送设备id token
         */
        public static final String PUSH_DEVICE_TOKEN = "push_device_token";
    }


    /**
     * 平台数据, 数据长度要一一对应起来
     */
    public static class Platform {
        /**
         * 平台名称
         */
        public static final String[] NAME = {
//                "百度地图", "高德地图", "美团"
//                , "饿了么外卖", "淘宝商城", "京东商城", "唯品会"
                "鱼塘①", "鱼塘②"
        };

        /**
         * 平台id
         */
        public static final int[] PLATFORM_ID = {1, 2};
        /**
         * 平台图标
         */
        public static final int[] TTF_STR_ID = {
//                R.string.ttf_baidu_map, R.string.ttf_gaode_map, R.string.ttf_meituan, R.string.ttf_eleme, R.string.ttf_taobao, R.string.ttf_jd, R.string.ttf_weipinhui
                R.string.ttf_yutang1,
                R.string.ttf_yutang2,
//                R.string.ttf_yutang3,
//                R.string.ttf_yutang4,
//                R.string.ttf_yutang5,
//                R.string.ttf_yutang6,
//                R.string.ttf_yutang7,
        };

        /**
         * 百度地图key  l0xAxSm8fpo7vG8QyHc3AmdD3qKSzbfB = 伟峰账户 test项目
         */
        public static final String KEY_BAIDU_MAP = "l0xAxSm8fpo7vG8QyHc3AmdD3qKSzbfB";
        /**
         * 高德地图key
         */
        public static final String KEY_GAODE_MAP = "3256c9fb7e2b66fa266150a18cdfd462";
    }


    /**
     * 平台类型 value
     */
    public static class PlatformType {
        /**
         * 百度地图
         */
        public static final int BAIDU_MAP = 1;

        /**
         * 高德地图
         */
        public static final int GAODE_MAP = 2;
        /**
         * 美团
         */
        public static final int MEITUAN = 3;
        /**
         * 饿了么外卖
         */
        public static final int ELEME = 4;
        /**
         * 淘宝商城
         */
        public static final int TAOBAO = 5;
        /**
         * 京东商城
         */
        public static final int JD = 6;
        /**
         * 唯品会
         */
        public static final int WEIPINHUI = 7;

    }


    /**
     * 默认的infojson数据, 防止第一次请求就错误,没数据的情况
     */
    public static final String infoJsonStr = "{\"reason\":\"成功的返回\",\"result\":{\"stat\":\"1\",\"data\":[{\"uniquekey\":\"fcc336c05acf17b66ba6dd33f9a1de2a\",\"title\":\"开启万物互联时代 推动网络强省建设 河南联通宣布NB-IoT正式商用\",\"date\":\"2018-07-04 17:33\",\"category\":\"财经\",\"author_name\":\"大河网\",\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/180704173345797.html\",\"thumbnail_pic_s\":\"http:\\/\\/04.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173345_0843bcd2c7e63666d83959ed71312ffd_1_mwpm_03200403.jpg\",\"thumbnail_pic_s02\":\"http:\\/\\/04.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173345_0843bcd2c7e63666d83959ed71312ffd_2_mwpm_03200403.jpg\"},{\"uniquekey\":\"cc01c42651e79340f6eb4f4bffe43237\",\"title\":\"真给力!人民币对美元汇率上演绝地反击\",\"date\":\"2018-07-04 17:33\",\"category\":\"财经\",\"author_name\":\"金投网\",\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/180704173303170.html\",\"thumbnail_pic_s\":\"http:\\/\\/06.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173303_f7bf885df9b01eb3766c4897c248e733_1_mwpm_03200403.jpg\"},{\"uniquekey\":\"d58927ec9cf1f32a20295a99d47da624\",\"title\":\"7月4日A股收评：明天重点关注大盘是否会新低\",\"date\":\"2018-07-04 17:32\",\"category\":\"财经\",\"author_name\":\"冯晓宁1234\",\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/180704173229005.html\",\"thumbnail_pic_s\":\"http:\\/\\/07.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173229_bebe79fb09798c7aa5700ab0870e5b23_2_mwpm_03200403.jpg\",\"thumbnail_pic_s02\":\"http:\\/\\/07.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173229_bebe79fb09798c7aa5700ab0870e5b23_1_mwpm_03200403.jpg\",\"thumbnail_pic_s03\":\"http:\\/\\/07.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173229_bebe79fb09798c7aa5700ab0870e5b23_3_mwpm_03200403.jpg\"},{\"uniquekey\":\"396752a2fdf9202ee27497a0082102ad\",\"title\":\"信用卡和贷款申请，这5点需要完善！\",\"date\":\"2018-07-04 17:32\",\"category\":\"财经\",\"author_name\":\"北京时间\",\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/180704173204341.html\",\"thumbnail_pic_s\":\"http:\\/\\/01.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173204_a2e739123a1ed7c9f6211c9b58a3735f_2_mwpm_03200403.jpg\",\"thumbnail_pic_s02\":\"http:\\/\\/01.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173204_a2e739123a1ed7c9f6211c9b58a3735f_3_mwpm_03200403.jpg\",\"thumbnail_pic_s03\":\"http:\\/\\/01.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173204_a2e739123a1ed7c9f6211c9b58a3735f_1_mwpm_03200403.jpg\"},{\"uniquekey\":\"db547d2362ef08e59d8f100e4dfa4042\",\"title\":\"事业经理人：进苏宁就是投资自己 分享公司成长红利\",\"date\":\"2018-07-04 17:31\",\"category\":\"财经\",\"author_name\":\"中国新闻网\",\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/180704173139771.html\",\"thumbnail_pic_s\":\"http:\\/\\/03.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173139_5afe5f16fba52b324d907460e9c9d5ed_1_mwpm_03200403.jpg\",\"thumbnail_pic_s02\":\"http:\\/\\/03.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173139_5afe5f16fba52b324d907460e9c9d5ed_2_mwpm_03200403.jpg\"},{\"uniquekey\":\"d0b25a86585dbc521754afa3a65a50dc\",\"title\":\"“太平鼓故乡”甘肃皋兰自荐优势 引多家名企“落户”\",\"date\":\"2018-07-04 17:31\",\"category\":\"财经\",\"author_name\":\"中国新闻网\",\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/180704173139609.html\",\"thumbnail_pic_s\":\"http:\\/\\/03.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173139_748d1c966331d28da6fe8f5e19ead30a_2_mwpm_03200403.jpg\",\"thumbnail_pic_s02\":\"http:\\/\\/03.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173139_748d1c966331d28da6fe8f5e19ead30a_1_mwpm_03200403.jpg\"},{\"uniquekey\":\"f9d198f69699e5842e16281db0d1fdd3\",\"title\":\"北京市属医院预约挂号率超86% 将提升医疗服务信息化\",\"date\":\"2018-07-04 17:31\",\"category\":\"财经\",\"author_name\":\"中国新闻网\",\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/180704173139371.html\",\"thumbnail_pic_s\":\"http:\\/\\/07.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173139_1b72f50a5913d8a0a460162bcb8f0e2f_1_mwpm_03200403.jpg\"},{\"uniquekey\":\"8252bb9c000640033bbbecc973a7a578\",\"title\":\"假如:股市跌到2500，人民币破7;这次:还是买房靠谱吗?\",\"date\":\"2018-07-04 17:30\",\"category\":\"财经\",\"author_name\":\"中国房评报道\",\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/180704173045439.html\",\"thumbnail_pic_s\":\"http:\\/\\/01.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173045_0f4cf688a9c2364400d466d8275e058e_5_mwpm_03200403.jpg\",\"thumbnail_pic_s02\":\"http:\\/\\/01.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173045_0f4cf688a9c2364400d466d8275e058e_4_mwpm_03200403.jpg\",\"thumbnail_pic_s03\":\"http:\\/\\/01.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173045_0f4cf688a9c2364400d466d8275e058e_2_mwpm_03200403.jpg\"},{\"uniquekey\":\"be2db47a42a0c157b9ec5e898f99f061\",\"title\":\"澳元 \\/ 美元：尽管澳洲数据强劲，但股市下挫令澳元吸引力下降\",\"date\":\"2018-07-04 17:30\",\"category\":\"财经\",\"author_name\":\"金汇财经\",\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/180704173037237.html\",\"thumbnail_pic_s\":\"http:\\/\\/04.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173037_be345967043a2e3cc488c552510974d1_1_mwpm_03200403.jpg\"},{\"uniquekey\":\"1d6c7c8e2cf337f7a030a0b790ddda35\",\"title\":\"Token 的发行和流通之路\",\"date\":\"2018-07-04 17:30\",\"category\":\"财经\",\"author_name\":\"中金网\",\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/180704173036463.html\",\"thumbnail_pic_s\":\"http:\\/\\/04.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173036_535758b4a730e643ecea990638c6e288_1_mwpm_03200403.jpg\"},{\"uniquekey\":\"b7093142f48201e0245667e9548f6f4f\",\"title\":\"业绩预增逾 30 倍！安阳钢铁逆市涨停 “钢铁侠”拉开中报行情序幕\",\"date\":\"2018-07-04 17:30\",\"category\":\"财经\",\"author_name\":\"天天基金\",\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/180704173033773.html\",\"thumbnail_pic_s\":\"http:\\/\\/03.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173033_cd0f9c0bd58015f5de2f0208f3514a3e_3_mwpm_03200403.jpg\",\"thumbnail_pic_s02\":\"http:\\/\\/03.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173033_cd0f9c0bd58015f5de2f0208f3514a3e_2_mwpm_03200403.jpg\",\"thumbnail_pic_s03\":\"http:\\/\\/03.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173033_cd0f9c0bd58015f5de2f0208f3514a3e_1_mwpm_03200403.jpg\"},{\"uniquekey\":\"c27411934ff4a41c43c4e1bb6b66a3fc\",\"title\":\"柳钢股份：上半年净利预增 395% 到 467%\",\"date\":\"2018-07-04 17:30\",\"category\":\"财经\",\"author_name\":\"鹰眼资讯\",\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/180704173024017.html\",\"thumbnail_pic_s\":\"http:\\/\\/02.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173024_676b96d2bcee27ab1002055a17ca2693_1_mwpm_03200403.jpg\"},{\"uniquekey\":\"414930e572ef2f0d39c347b00c9a48ac\",\"title\":\"大亚圣象：控股股东今日增持 200 万股\",\"date\":\"2018-07-04 17:30\",\"category\":\"财经\",\"author_name\":\"鹰眼资讯\",\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/180704173023802.html\",\"thumbnail_pic_s\":\"http:\\/\\/01.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173023_83f4dcbfe5c75ac5d3616a009be46b31_1_mwpm_03200403.jpg\"},{\"uniquekey\":\"00a3df94a244cbe4e7b717c985e89064\",\"title\":\"分享散户简单实用的MACD指标运用\",\"date\":\"2018-07-04 17:30\",\"category\":\"财经\",\"author_name\":\"老刘谈股论金\",\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/180704173019110.html\",\"thumbnail_pic_s\":\"http:\\/\\/04.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173019_396c7575901796169d53c81439bedd76_1_mwpm_03200403.jpg\",\"thumbnail_pic_s02\":\"http:\\/\\/04.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173019_396c7575901796169d53c81439bedd76_2_mwpm_03200403.jpg\",\"thumbnail_pic_s03\":\"http:\\/\\/04.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173019_396c7575901796169d53c81439bedd76_3_mwpm_03200403.jpg\"},{\"uniquekey\":\"21ee184937f11dc32190fb7b58c6eb1d\",\"title\":\"陕航集团：力争将航空产业打造成为陕西省新的支柱产业\",\"date\":\"2018-07-04 17:30\",\"category\":\"财经\",\"author_name\":\"陕西网\",\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/180704173015240.html\",\"thumbnail_pic_s\":\"http:\\/\\/04.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173015_ab8b7bad62e1c1b681e2788dbdb9d8c0_1_mwpm_03200403.jpg\",\"thumbnail_pic_s02\":\"http:\\/\\/04.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173015_ab8b7bad62e1c1b681e2788dbdb9d8c0_2_mwpm_03200403.jpg\",\"thumbnail_pic_s03\":\"http:\\/\\/04.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173015_ab8b7bad62e1c1b681e2788dbdb9d8c0_3_mwpm_03200403.jpg\"},{\"uniquekey\":\"ffd5c5f7b981db2abf9f29ba80abef38\",\"title\":\"比亚迪：新一代唐上市首周订单销量超 1.5 万台\",\"date\":\"2018-07-04 17:30\",\"category\":\"财经\",\"author_name\":\"鹰眼资讯\",\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/180704173010285.html\",\"thumbnail_pic_s\":\"http:\\/\\/00.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173010_ac23d835b0f7548d13bd5d73db1fadf2_1_mwpm_03200403.jpg\"},{\"uniquekey\":\"fd75ad97521c9c8b35bb8cb3a366688e\",\"title\":\"咚咚智能：高校无线智能门锁，新蓝海下的突围\",\"date\":\"2018-07-04 17:30\",\"category\":\"财经\",\"author_name\":\"鹰眼资讯\",\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/180704173009893.html\",\"thumbnail_pic_s\":\"http:\\/\\/07.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173009_46a3983d7bc667cea902b6005ceae509_2_mwpm_03200403.jpg\",\"thumbnail_pic_s02\":\"http:\\/\\/07.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173009_46a3983d7bc667cea902b6005ceae509_4_mwpm_03200403.jpg\",\"thumbnail_pic_s03\":\"http:\\/\\/07.imgmini.eastday.com\\/mobile\\/20180704\\/20180704173009_46a3983d7bc667cea902b6005ceae509_1_mwpm_03200403.jpg\"},{\"uniquekey\":\"9f22117f1c82e9aa99f15cfdf57b4a24\",\"title\":\"哪家银行理财产品收益高 哈尔滨银行某产品收益达5.98%\",\"date\":\"2018-07-04 17:29\",\"category\":\"财经\",\"author_name\":\"北京时间\",\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/180704172943575.html\",\"thumbnail_pic_s\":\"http:\\/\\/09.imgmini.eastday.com\\/mobile\\/20180704\\/20180704172943_208544838f69756b26deb2f16ed711cb_1_mwpm_03200403.jpg\"},{\"uniquekey\":\"f2d8a6863e25108e41e51d1093be9f29\",\"title\":\"11个项目拿到19张投资邀请函｜连云港、江阴掀起“i创”旋风！\",\"date\":\"2018-07-04 17:28\",\"category\":\"财经\",\"author_name\":\"i创杯\",\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/180704172836703.html\",\"thumbnail_pic_s\":\"http:\\/\\/00.imgmini.eastday.com\\/mobile\\/20180704\\/20180704172836_660b6e101cae4d24fea9721f8e803820_37_mwpm_03200403.jpg\",\"thumbnail_pic_s02\":\"http:\\/\\/00.imgmini.eastday.com\\/mobile\\/20180704\\/20180704172836_660b6e101cae4d24fea9721f8e803820_38_mwpm_03200403.jpg\",\"thumbnail_pic_s03\":\"http:\\/\\/00.imgmini.eastday.com\\/mobile\\/20180704\\/20180704172836_660b6e101cae4d24fea9721f8e803820_22_mwpm_03200403.jpg\"},{\"uniquekey\":\"0f6126aa81864207439076d7927d3c6a\",\"title\":\"请善待你身边每一个在做期货的人！\",\"date\":\"2018-07-04 17:28\",\"category\":\"财经\",\"author_name\":\"涛博杂论\",\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/180704172812482.html\",\"thumbnail_pic_s\":\"http:\\/\\/03.imgmini.eastday.com\\/mobile\\/20180704\\/20180704172812_14e2e17bd3b28f15013bf93d37bd7981_5_mwpm_03200403.jpg\",\"thumbnail_pic_s02\":\"http:\\/\\/03.imgmini.eastday.com\\/mobile\\/20180704\\/20180704172812_14e2e17bd3b28f15013bf93d37bd7981_2_mwpm_03200403.jpg\",\"thumbnail_pic_s03\":\"http:\\/\\/03.imgmini.eastday.com\\/mobile\\/20180704\\/20180704172812_14e2e17bd3b28f15013bf93d37bd7981_6_mwpm_03200403.jpg\"},{\"uniquekey\":\"1cb89fb1dd5d7ed5c01df285590a9d22\",\"title\":\"中国西部直达印度洋的大通道，看看地图就知道了\",\"date\":\"2018-07-04 17:26\",\"category\":\"财经\",\"author_name\":\"喜闻乐见社区\",\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/180704172652506.html\",\"thumbnail_pic_s\":\"http:\\/\\/04.imgmini.eastday.com\\/mobile\\/20180704\\/20180704172652_8f3278d2636788c2451cefba68a3a91e_3_mwpm_03200403.jpg\",\"thumbnail_pic_s02\":\"http:\\/\\/04.imgmini.eastday.com\\/mobile\\/20180704\\/20180704172652_8f3278d2636788c2451cefba68a3a91e_2_mwpm_03200403.jpg\",\"thumbnail_pic_s03\":\"http:\\/\\/04.imgmini.eastday.com\\/mobile\\/20180704\\/20180704172652_8f3278d2636788c2451cefba68a3a91e_4_mwpm_03200403.jpg\"},{\"uniquekey\":\"e720fd3269392aa3bcc986ae9484bf81\",\"title\":\"西安多举措鼓励优秀“双创”项目落地\",\"date\":\"2018-07-04 17:26\",\"category\":\"财经\",\"author_name\":\"精彩陕西\",\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/180704172617311.html\",\"thumbnail_pic_s\":\"http:\\/\\/00.imgmini.eastday.com\\/mobile\\/20180704\\/20180704172617_ce465b30866f85ea9a5b33bee14a9166_1_mwpm_03200403.jpg\"},{\"uniquekey\":\"5b250354ca6b931956b67f4aa38b0f8b\",\"title\":\"余晚晚——“创业者”的大梦想和小幸福\",\"date\":\"2018-07-04 17:26\",\"category\":\"财经\",\"author_name\":\"TOM   \",\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/180704172608851.html\",\"thumbnail_pic_s\":\"http:\\/\\/01.imgmini.eastday.com\\/mobile\\/20180704\\/20180704172608_4bbc64ac5955fb6f79365cf78a23db20_7_mwpm_03200403.jpg\",\"thumbnail_pic_s02\":\"http:\\/\\/01.imgmini.eastday.com\\/mobile\\/20180704\\/20180704172608_4bbc64ac5955fb6f79365cf78a23db20_2_mwpm_03200403.jpg\",\"thumbnail_pic_s03\":\"http:\\/\\/01.imgmini.eastday.com\\/mobile\\/20180704\\/20180704172608_4bbc64ac5955fb6f79365cf78a23db20_4_mwpm_03200403.jpg\"},{\"uniquekey\":\"93a295d073776f0d14d3e1b06a6ea074\",\"title\":\"同益股份获得政府补助370万元\",\"date\":\"2018-07-04 17:25\",\"category\":\"财经\",\"author_name\":\"金融界\",\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/180704172539618.html\",\"thumbnail_pic_s\":\"http:\\/\\/02.imgmini.eastday.com\\/mobile\\/20180704\\/20180704172539_972a726e3895cebc9b077b19a74f1ed3_1_mwpm_03200403.jpg\"},{\"uniquekey\":\"221d642ebe1daeeff83a86d79873786c\",\"title\":\"41亿大罚单！创出会所最高罚单纪录！\",\"date\":\"2018-07-04 17:25\",\"category\":\"财经\",\"author_name\":\"鲁召辉论财富\",\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/180704172539360.html\",\"thumbnail_pic_s\":\"http:\\/\\/08.imgmini.eastday.com\\/mobile\\/20180704\\/20180704172539_75833f88aaddbf22135cb196f84bf034_2_mwpm_03200403.jpg\",\"thumbnail_pic_s02\":\"http:\\/\\/08.imgmini.eastday.com\\/mobile\\/20180704\\/20180704172539_75833f88aaddbf22135cb196f84bf034_3_mwpm_03200403.jpg\",\"thumbnail_pic_s03\":\"http:\\/\\/08.imgmini.eastday.com\\/mobile\\/20180704\\/20180704172539_75833f88aaddbf22135cb196f84bf034_1_mwpm_03200403.jpg\"},{\"uniquekey\":\"22856d1cbee4bcce15a7f40bb38ce5f5\",\"title\":\"海博科技即将登陆新三板 去年净赚218.39万\",\"date\":\"2018-07-04 17:25\",\"category\":\"财经\",\"author_name\":\"鲁网临沂新闻中心\",\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/180704172514197.html\",\"thumbnail_pic_s\":\"http:\\/\\/07.imgmini.eastday.com\\/mobile\\/20180704\\/20180704172514_e2cc57758665f0e56c3ddaeac25a7543_1_mwpm_03200403.jpg\",\"thumbnail_pic_s02\":\"http:\\/\\/07.imgmini.eastday.com\\/mobile\\/20180704\\/20180704172514_e2cc57758665f0e56c3ddaeac25a7543_2_mwpm_03200403.jpg\",\"thumbnail_pic_s03\":\"http:\\/\\/07.imgmini.eastday.com\\/mobile\\/20180704\\/20180704172514_e2cc57758665f0e56c3ddaeac25a7543_3_mwpm_03200403.jpg\"},{\"uniquekey\":\"030b8faacdd9bbaa48ac18bf26effe10\",\"title\":\"在您管理财务之前，您可以看到这些财务管理的真相\",\"date\":\"2018-07-04 17:24\",\"category\":\"财经\",\"author_name\":\"汇泰\",\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/180704172454556.html\",\"thumbnail_pic_s\":\"http:\\/\\/01.imgmini.eastday.com\\/mobile\\/20180704\\/20180704172454_969fca37649257a15d93d5953d791f79_1_mwpm_03200403.jpg\"},{\"uniquekey\":\"68cbfa7095341672aa48b553197e7b3f\",\"title\":\"数字经济发展与信用体系建设研讨会在京举行\",\"date\":\"2018-07-04 17:24\",\"category\":\"财经\",\"author_name\":\"中国信息通信研究院\",\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/180704172439970.html\",\"thumbnail_pic_s\":\"http:\\/\\/08.imgmini.eastday.com\\/mobile\\/20180704\\/20180704172439_ee73d075ce35a531a4849c03632ed608_3_mwpm_03200403.jpg\",\"thumbnail_pic_s02\":\"http:\\/\\/08.imgmini.eastday.com\\/mobile\\/20180704\\/20180704172439_ee73d075ce35a531a4849c03632ed608_1_mwpm_03200403.jpg\",\"thumbnail_pic_s03\":\"http:\\/\\/08.imgmini.eastday.com\\/mobile\\/20180704\\/20180704172439_ee73d075ce35a531a4849c03632ed608_5_mwpm_03200403.jpg\"},{\"uniquekey\":\"336da4c07c18b912768818eea3246170\",\"title\":\"英国服务业增速创八个月新高，英银加息添砝码英镑蹿升30点\",\"date\":\"2018-07-04 17:24\",\"category\":\"财经\",\"author_name\":\"汇通网\",\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/180704172436843.html\",\"thumbnail_pic_s\":\"http:\\/\\/02.imgmini.eastday.com\\/mobile\\/20180704\\/20180704172436_a82746e7234ccae9141ac6fd1d0a74ba_1_mwpm_03200403.jpg\"},{\"uniquekey\":\"e292d4a79436444ec7aa6631a8c884e1\",\"title\":\"太平人寿临沂中心支公司全面启动7.8保险公众宣传日活动\",\"date\":\"2018-07-04 17:24\",\"category\":\"财经\",\"author_name\":\"日照网\",\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/180704172435629.html\",\"thumbnail_pic_s\":\"http:\\/\\/01.imgmini.eastday.com\\/mobile\\/20180704\\/20180704172435_fb3d9dcb995348c84b26ac9c30c0fccf_1_mwpm_03200403.jpg\"}]},\"error_code\":0}";

    public class CLICK {

        public static final String umeng_home_collect_setting = "umeng_home_collect_setting";
        public static final String umeng_setting_collect_setting = "umeng_setting_collect_setting";
        public static final String umeng_login = "umeng_login";
        public static final String umeng_share = "umeng_share";
        public static final String umeng_vip = "umeng_vip";
        public static final String umeng_sign = "umeng_sign";
        public static final String umeng_export_all_contact = "umeng_export_all_contact";
        public static final String umeng_save_single_contact = "umeng_save_single_contact";
        public static final String umeng_delete_contact = "umeng_delete_contact";
        public static final String umeng_delete_all_data = "umeng_delete_all_data";
        public static final String umeng_collect_request = "umeng_collect_request";
        public static final String umeng_export_data = "umeng_export_data";
        public static final String umeng_home_clear_data = "umeng_home_clear_data";
        public static final String umeng_stop_collect = "umeng_stop_collect";
        public static final String umeng_start_collect = "umeng_start_collect";
    }
}
