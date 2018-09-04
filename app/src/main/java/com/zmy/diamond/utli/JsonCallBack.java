package com.zmy.diamond.utli;


import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.exception.HttpException;
import com.lzy.okgo.exception.StorageException;
import com.zmy.diamond.utli.bean.LoginResponseBean;

import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * json数据解析
 * Created by zhangmengyun on 2018/8/3.
 */

public abstract class JsonCallBack<T> extends AbsCallback<T> {
    private Type type;
    private Class<T> clazz;

    public JsonCallBack(Type type) {
        this.type = type;
    }

    public JsonCallBack(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T convertResponse(Response response) throws Throwable {
        try {
            ResponseBody body = response.body();
            if (body == null) return null;
            T data = null;
            Gson gson = new Gson();
            JsonReader jsonReader = new JsonReader(body.charStream());
            if (type != null) data = gson.fromJson(jsonReader, type);
            if (clazz != null) data = gson.fromJson(jsonReader, clazz);
            LogUtils.e("JsonCallBack=" + data.toString());

            return data;
        } catch (Exception e) {
            T data2 = null;
            return data2;
        }
    }


    @Override
    public void onError(com.lzy.okgo.model.Response<T> response) {
        super.onError(response);

//这里获取到异常后，打印出来，使用printStackTrace( )这个方法看打印的详情，如果你不打印，发生异常了你都不知道在哪
//这里获取到异常后，打印出来，使用printStackTrace( )这个方法看打印的详情，如果你不打印，发生异常了你都不知道在哪
//这里获取到异常后，打印出来，使用printStackTrace( )这个方法看打印的详情，如果你不打印，发生异常了你都不知道在哪
        Throwable exception = response.getException();
        if (exception != null) exception.printStackTrace();
        if (exception instanceof UnknownHostException || exception instanceof ConnectException) {
            ToastUtils.showShort("网络连接失败，请连接网络");
        } else if (exception instanceof SocketTimeoutException) {
            ToastUtils.showShort("网络请求超时");
        } else if (exception instanceof HttpException) {
            ToastUtils.showShort("服务端响应码404和500了，知道该怎么办吗? ");
        } else if (exception instanceof StorageException) {
            ToastUtils.showShort("sd卡不存在或者没有权限");
        } else if (exception instanceof IllegalStateException) {
//这个异常类型就是你自己抛的，当然你也可以抛其他类型，或者自定义类型，无所谓，只要是异常就行
//这里获取的message就是你前面抛出来的数据，至于你想怎么解析都行
            String message = exception.getMessage();
            System.out.println(message);
        }
    }


    /**
     * 数据解析示例
     */

    public void test() {

        //解析普通数据
        OkGo.<LoginResponseBean>get("http: //server. jeasonlzy. com/")
                .tag(this)
                .execute(new JsonCallBack<LoginResponseBean>(LoginResponseBean.class) {

                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LoginResponseBean> response) {
                        LoginResponseBean body = response.body();
                    }
                });


        //解析集合数据
        Type type = new TypeToken<List<LoginResponseBean>>() {
        }.getType();
        OkGo.<List<LoginResponseBean>>get("http://server. jeasonlzy. com/")
                .tag(this)
                .execute(new JsonCallBack<List<LoginResponseBean>>(type) {
                             @Override
                             public void onSuccess(com.lzy.okgo.model.Response<List<LoginResponseBean>> response) {
                                 List<LoginResponseBean> body = response.body();
                             }
                         }
                );
    }


}