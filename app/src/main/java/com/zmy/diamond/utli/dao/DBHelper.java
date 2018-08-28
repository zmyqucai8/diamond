package com.zmy.diamond.utli.dao;

import android.content.Context;
import android.database.Cursor;

import com.blankj.utilcode.util.LogUtils;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.CollectCityBeanDao;
import com.zmy.diamond.utli.bean.CollectRecordBeanDao;
import com.zmy.diamond.utli.bean.DaoMaster;

import org.greenrobot.greendao.database.Database;

/**
 * Created by win7 on 2017/7/27.
 */

public class DBHelper extends DaoMaster.OpenHelper {
    public static final String DBNAME = "diamond-db";

    public DBHelper(Context context) {
        super(context, DBNAME, null);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {

        LogUtils.e("数据库版本 oldVersion=" + oldVersion + " newVersion=" + newVersion);
//        [数据库升级] !!! 一定要做判断版本,设置需要升级的表做数据迁移, 不需要改的表不要动
        //1.没有改动的表,不要操作!!!
        //2.需要升级表结构且保存原数据的表,不要删除原表, 写SQL,插入新字段
        //3.新创建的表,需要新建:  xxxDao.createTable(db, true);
        //4.修改字段的表,如果不需要保存数据,直接先删除再创建  xxDao.dropTable  xxDao.createTable
        try {
            if (oldVersion < 5) {
                DaoMaster.dropAllTables(db, true);
                DaoMaster.createAllTables(db, true);
            }
            if (oldVersion == 5) {
                DaoUtlis.deleteAllData(MyUtlis.getLoginUserId());
            }

            if (oldVersion < 7) {
                CollectRecordBeanDao.createTable(db, true);
            }

            if (oldVersion < 11) {
                DaoMaster.dropAllTables(db, true);
                DaoMaster.createAllTables(db, true);
            }

            if (oldVersion < 12) {
                CollectCityBeanDao.createTable(db, true);
            }

//            if (oldVersion < 4) {
//                //版本4: 新建消息草稿表
//                MessageDraftBeanDao.createTable(db, true);
//            }
//            if (oldVersion < 5) {
//                //版本<5的数据库升级时清除所有数据,新建表结构
//                DaoMaster.dropAllTables(db, true);
//                onCreate(db);
//            }
//            if (oldVersion < 6) {
//                MessageFileRecordBeanDao.createTable(db, true);
//            }
//            if (oldVersion < 8) {
//                DaoMaster.dropAllTables(db, true);
//                DaoMaster.createAllTables(db, true);
//            }
//
//            if (oldVersion < 11) {
//                //文件消息记录表中, 新增locaurl字段
//                if (!isExistColumn(db, "MESSAGE_FILE_RECORD_BEAN", "LOCA_URL")) {
//                    db.execSQL("ALTER TABLE MESSAGE_FILE_RECORD_BEAN ADD LOCA_URL TEXT");
//                    WinfreeUtlis.log("更新数据库,插入新字段LOCA_URL");
////                    db.execSQL("ALTER TABLE MESSAGE_FILE_RECORD_BEAN ADD COLUMN LOCA_URL VARCHAR(60) NOT NULL");
//                }
//            }

            LogUtils.e("数据库升级成功");
        } catch (ClassCastException e) {
            e.printStackTrace();
            LogUtils.e("数据库升级失败");
        }
    }

    /**
     * 判断里面表里面是否存在要添加的那一列
     *
     * @param db
     * @param tableName
     * @param columnName
     * @return
     */
    public static boolean isExistColumn(Database db, String tableName, String columnName) {


        boolean isExist = false;
        String sql = "SELECT sql FROM sqlite_master WHERE type='table' AND name='" + tableName + "';";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            String str = cursor.getString(0);
            int index = str.indexOf(columnName);
            if (index >= 0) {
                isExist = true;
            }
        }
        cursor.close();
        return isExist;
    }
}
