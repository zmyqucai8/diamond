package com.zmy.diamond.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SPUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.zmy.diamond.R;
import com.zmy.diamond.base.MyBaseSwipeBackActivity;
import com.zmy.diamond.utli.AppConstant;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.UserBean;
import com.zmy.diamond.utli.dao.DaoUtlis;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 信息编辑页面
 * Created by zhangmengyun on 2018/6/13.
 */

public class EditInfoActivity extends MyBaseSwipeBackActivity {
    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.tv_ttf_avatar_r)
    TextView tv_ttf_avatar_r;
    @BindView(R.id.tv_ttf_name_r)
    TextView tv_ttf_name_r;
    @BindView(R.id.edit_name)
    EditText edit_name;
    @BindView(R.id.iv_avatar)
    ImageView iv_avatar;


    @Override
    public void initUI() {
        setContentView(R.layout.activity_edit_info);
        super.initUI();
        tv_ttf_name_r.setTypeface(MyUtlis.getTTF());
        tv_ttf_avatar_r.setTypeface(MyUtlis.getTTF());
        tv_back.setTypeface(MyUtlis.getTTF());
        tv_title.setText(getString(R.string.title_edit_info));
        tv_right.setText(getString(R.string.text_save));
        tv_right.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        UserBean currentLoginUser = DaoUtlis.getCurrentLoginUser();
        if (null == currentLoginUser) {
            return;
        }
        setAvatar(currentLoginUser.getAvatarUrl(), false);
        setNickName(currentLoginUser.getNickName());
    }

    /**
     * 设置昵称
     *
     * @param nickName
     */
    private void setNickName(String nickName) {
        if (!TextUtils.isEmpty(nickName)) {
            edit_name.setText(nickName);
        }
    }

    @OnClick(R.id.rl_avatar)
    public void rl_avatar() {
//        MyUtlis.showShort("头像");
        MyUtlis.openPhoto(this, 1);
    }

    @OnClick(R.id.tv_right)
    public void tv_right() {
//        MyUtlis.showShort("保存");
        String nickName = edit_name.getText().toString().trim();
        if (TextUtils.isEmpty(nickName)) {
            MyUtlis.showShort(this, getString(R.string.hint_nickname_no_data));
            return;
        }
        KeyboardUtils.hideSoftInput(this);
        boolean b = DaoUtlis.updateUserNickName(nickName);
        if (b) {
            MyUtlis.eventUpdateNickName(nickName);
            MyUtlis.showShortYes(this, getString(R.string.hint_nickname_edit_ok));
        } else {
            MyUtlis.showShortNo(this, getString(R.string.hint_nickname_edit_no));
        }
    }

    @OnClick(R.id.tv_back)
    public void tv_back() {
        ActivityUtils.finishActivity(this, true);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, EditInfoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST://相册选择图片
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if (null == selectList || selectList.size() == 0) {
                        MyUtlis.showShort(this, getString(R.string.hint_photo_no_file_reselect));
                        return;
                    }
                    LocalMedia localMedia = selectList.get(0);
                    String path = localMedia.getPath();
                    setAvatar(path, true);
                    break;
            }
        }


    }

    //修改头像
    private void setAvatar(String path, boolean isShort) {
        if (FileUtils.isFileExists(path)) {
            if (DaoUtlis.updateUserAvatar(SPUtils.getInstance().getString(AppConstant.SPKey.LAST_LOGIN_USER), path)) {
                MyUtlis.showImage(path, iv_avatar);
                MyUtlis.eventUpdateAvatar(path);
                if (isShort)
                    MyUtlis.showShortYes(this, getString(R.string.hint_avatar_edit_ok));
            }
        }


    }
}
