package com.zmy.diamond.utli;

/**
 * Created by zhangmengyun on 2018/6/21.
 */

public interface MyListener<T> {

    void onSucceed(T t);

    void onError(T t);




}
