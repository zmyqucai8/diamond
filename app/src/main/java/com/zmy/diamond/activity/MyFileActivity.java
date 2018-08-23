package com.zmy.diamond.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.zmy.diamond.R;
import com.zmy.diamond.adapter.MyFileAdapter;
import com.zmy.diamond.base.MyBaseSwipeBackActivity;
import com.zmy.diamond.utli.MyUtlis;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * 我的导出文件
 * Created by zhangmengyun on 2018/6/16.
 */

public class MyFileActivity extends MyBaseSwipeBackActivity {
    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;


    @BindView(R.id.recyclerView)
    SwipeMenuRecyclerView mRecyclerView;
    MyFileAdapter mAdapter;

    @Override
    public void initUI() {
        setContentView(R.layout.activity_my_file);
        super.initUI();
        tv_title.setText(getString(R.string.title_file));
        tv_back.setTypeface(MyUtlis.getTTF());


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(linearLayoutManager);


        mAdapter = new MyFileAdapter(this, new ArrayList<File>());
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.setEmptyView(MyUtlis.getEmptyView(this, "暂无文件"));


        //侧滑菜单
        mRecyclerView.setSwipeMenuCreator(mSwipeMenuCreator);
// 菜单点击监听。
        mRecyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener);

        mRecyclerView.setAdapter(mAdapter);
    }

    // 菜单点击监听。
    SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge) {
            // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
            menuBridge.closeMenu();
//            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
//            int adapterPosition = menuBridge.getAdapterPosition() ; // RecyclerView的Item的position。
//            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            try {
                if (FileUtils.deleteFile(mAdapter.getData().get(menuBridge.getAdapterPosition()))) {
                    mAdapter.remove(menuBridge.getAdapterPosition());
                }
            } catch (Exception e) {
                e.printStackTrace();

                MyUtlis.showShort(MyFileActivity.this, "删除失败");
            }


        }
    };

    // 创建菜单：
    SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int viewType) {
            SwipeMenuItem deleteItem = new SwipeMenuItem(MyFileActivity.this);
            // 注意：哪边不想要菜单，那么不要添加即可。
            // 各种文字和图标属性设置。
            rightMenu.addMenuItem(deleteItem); // 在Item右侧添加一个菜单。
            deleteItem.setBackgroundColor(ContextCompat.getColor(MyFileActivity.this, R.color.holo_red_light));
            deleteItem.setText("　　删除　　");
            deleteItem.setTextSize(15);
            deleteItem.setTextColor(Color.WHITE);
            deleteItem.setHeight(MATCH_PARENT);

        }
    };

    /**
     * 获取导出的文件数据
     *
     * @return
     */
    private List<File> getFileListData() {


        String appFileDirPath = MyUtlis.getAppFileDirPath();
        //获取目录
        FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.getName().contains(".csv")) {
                    return true;
                }
                return false;
            }
        };
        List<File> files = FileUtils.listFilesInDirWithFilter(appFileDirPath, fileFilter);
        if (null == files) {
            files = new ArrayList<>();
        }
        if (files.size() > 1) {
            //排序
            Collections.sort(files, new Comparator<File>() {
                @Override
                public int compare(File dt1, File dt2) {
                    if (dt1.lastModified() > dt2.lastModified()) {
                        return -1;
                    } else if (dt1.lastModified() < dt2.lastModified()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });
        }
        return files;
    }

    @Override
    public void initData() {


        PermissionUtils.permission(PermissionConstants.STORAGE)
                .rationale(new PermissionUtils.OnRationaleListener() {
                    @Override
                    public void rationale(final ShouldRequest shouldRequest) {
                        MyUtlis.showRationaleDialog(shouldRequest);
                    }
                })
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {


                        List<File> fileListData = getFileListData();
                        mAdapter.setNewData(fileListData);
                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever,
                                         List<String> permissionsDenied) {
                        if (!permissionsDeniedForever.isEmpty()) {
                            MyUtlis.showOpenAppSettingDialog();
                        }
                    }
                })
                .request();


    }


    public static void start(Context context) {
        Intent intent = new Intent(context, MyFileActivity.class);
        context.startActivity(intent);
    }


    @OnClick(R.id.tv_back)
    public void tv_back() {
        ActivityUtils.finishActivity(this, true);
    }


}
