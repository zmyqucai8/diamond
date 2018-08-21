package com.zmy.diamond.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Random;

/**
 * Created by zhangmengyun on 2018/8/21.
 */

public class GuidePagerAdapter extends PagerAdapter {


    public int[] mList;

    public Context mContext;

    public GuidePagerAdapter(Context context, int[] list) {

        this.mList = list;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return null == mList ? 0 : mList.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {


//        View view = View.inflate(mContext, R.layout.item_guide, null);

        ImageView imageView = new ImageView(mContext);

        Integer resId = mList[position];

        if (resId > 0) {
            imageView.setImageResource(resId);
        } else {
            Random random = new Random();
            int color = Color.argb(random.nextInt(205) + 50, random.nextInt(255), random.nextInt(255), random.nextInt(255));
            imageView.setBackgroundColor(color);
        }
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);
        container.removeView((View) object);
    }
}
