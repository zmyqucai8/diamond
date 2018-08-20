package com.zmy.diamond.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.just.agentweb.AgentWeb;
import com.zmy.diamond.R;
import com.zmy.diamond.base.MyBaseSwipeBackActivity;
import com.zmy.diamond.utli.AppConstant;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.JoinVipResponseBean;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * web内置浏览器页面 :所有web页面都用此窗口打开
 * Created by zhangmengyun on 2018/6/13.
 */

public class WebViewActivity extends MyBaseSwipeBackActivity {

    @BindView(R.id.ll_webview)
    LinearLayout ll_webview;
    AgentWeb mAgentWeb;
    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;

    /**
     * 启动页面
     *
     * @param dataType  0=url 1= html代码  默认=0
     * @param context
     * @param urlOrHtml
     */
    public static void start(Context context, int dataType, String urlOrHtml, String title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(AppConstant.ExtraKey.URL_HTML, urlOrHtml);
        intent.putExtra(AppConstant.ExtraKey.TITLE, title);
        intent.putExtra(AppConstant.ExtraKey.DATA_TYPE, dataType);
        context.startActivity(intent);
    }

    /**
     * 启动webview
     *
     * @param context
     * @param url
     */
    public static void start(Context context, String url) {
        start(context, 0, url, "");
    }

    /**
     * 启动webview
     *
     * @param context
     * @param url
     * @param title
     */
    public static void start(Context context, String url, String title) {
        start(context, 0, url, title);
    }


    /**
     * 采用请求模式 加载webview
     *
     * @param context
     * @param bean
     */
    public static void start2(Context context, JoinVipResponseBean.DataBean bean) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(AppConstant.ExtraKey.DATA_TYPE, 1);
        intent.putExtra(AppConstant.ExtraKey.DATA, bean);
        context.startActivity(intent);
    }


    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //do you work
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (!title.contains("about") && !title.contains("blank")) {
                tv_title.setText(title);
            }
        }
    };


    //点击返回的时候是否关闭窗口
    boolean isBackFinish;
    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //do you  work
            LogUtils.e("onPageStarted=" + url);
//            https://jinshuju.net/f/aHsNIJ/success
            if (url.contains("jinshuju") && url.contains("success")) {
                WebView webView = mAgentWeb.getWebCreator().getWebView();
                LogUtils.e("金数据提交成功");
                webView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return true;
                    }
                });
                ll_webview.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return true;
                    }
                });
                isBackFinish = true;
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            LogUtils.e("onPageFinished=" + url);
//            tv_title.setText(view.getTitle());
        }
    };

    @Override
    public void initUI() {
        setContentView(R.layout.activity_web);
        super.initUI();
        tv_back.setTypeface(MyUtlis.getTTF());
    }

    int dataType;

    @Override
    public void initData() {
        String url_html = getIntent().getStringExtra(AppConstant.ExtraKey.URL_HTML);

        dataType = getIntent().getIntExtra(AppConstant.ExtraKey.DATA_TYPE, 0);
        String title = getIntent().getStringExtra(AppConstant.ExtraKey.TITLE);
        tv_title.setText(title);


        if (dataType == 0) {
            mAgentWeb = AgentWeb.with(this)
                    .setAgentWebParent(ll_webview, new LinearLayout.LayoutParams(-1, -1))
                    .useDefaultIndicator()
                    .setWebChromeClient(mWebChromeClient)
                    .setWebViewClient(mWebViewClient)
                    .createAgentWeb()
                    .ready()
                    .go(url_html);
        } else {
            mAgentWeb = AgentWeb.with(this)
                    .setAgentWebParent(ll_webview, new LinearLayout.LayoutParams(-1, -1))
                    .useDefaultIndicator()
                    .setWebChromeClient(mWebChromeClient)
                    .setWebViewClient(mWebViewClient)
                    .createAgentWeb()
                    .ready()
                    .go("");
            isBackFinish = true;

            WebView webView = mAgentWeb.getWebCreator().getWebView();
            //发起请求
            //需要访问的网址
            //post访问需要提交的参数
            JoinVipResponseBean.DataBean bean = (JoinVipResponseBean.DataBean) getIntent().getSerializableExtra(AppConstant.ExtraKey.DATA);
            StringBuilder sb = new StringBuilder();
            sb.append("api_user=");
            sb.append(bean.getApi_user());
            sb.append("&price=");
            sb.append(bean.getPrice());
            sb.append("&type=");
            sb.append(bean.getType());
            sb.append("&redirect=");
            sb.append(bean.getRedirect());
            sb.append("&order_id=");
            sb.append(bean.getOrder_id());
            sb.append("&order_info=");
            sb.append(bean.getOrder_info());
            sb.append("&signature=");
            sb.append(bean.getSignature());

            String postData = sb.toString();
            LogUtils.e("postData=" + postData);
            byte[] postBytes = new byte[0];
            try {
                postBytes = postData.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                ToastUtils.showShort("支付出错,请重试");
                super.onBackPressed();
                return;
            }
//            EncodeUtils.urlEncode(postData, "UTF-8");
            //由于webView.postUrl(url, postData)中 postData类型为byte[] ，
            //通过EncodingUtils.getBytes(data, charset)方法进行转换
            webView.postUrl(AppConstant.Api.paypayzhuPay, postBytes);
//            webView.loadData(url_html, "text/html", "UTF-8");
        }
    }

    @Override
    public void onBackPressed() {
        tv_back();
    }

    @OnClick(R.id.tv_back)
    public void tv_back() {


        if (isBackFinish || !mAgentWeb.back()) {
            super.onBackPressed();
        }
    }


    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (dataType != 0) {
            ActivityUtils.finishActivity(this);
        }
    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }
}
