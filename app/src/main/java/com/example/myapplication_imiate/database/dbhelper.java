package com.example.myapplication_imiate.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.myapplication_imiate.entiy.HotSaleItem;

import java.util.ArrayList;
import java.util.List;

public class dbhelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "dbhelper";
    private static final String TABLE_HotSaleItem ="HotSaleItem";
    private static final String TABLE_Reservation ="Reservation";
    private static final int DB_VERSION = 1;
    private static dbhelper mHelper = null;
    private SQLiteDatabase mRDB = null;
    private SQLiteDatabase mWDB = null;


    // 利用单例模式获取数据库帮助器的唯一实例
    //核心作用是：保证整个 App 运行过程中，ShoppingDBHelper（SQLite 数据库辅助类）只有一个实例，避免重复创建数据库连接导致的性能损耗、数据冲突甚至崩溃
    public static dbhelper getInstance(Context context) {
        if (mHelper == null) {
            mHelper = new dbhelper(context);
        }
        return mHelper;
    }

    public dbhelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // 打开数据库的读连接
    public SQLiteDatabase openReadLink() {
        if (mRDB == null || !mRDB.isOpen()) {
            mRDB = this.getReadableDatabase();
        }
        return mRDB;
    }

    // 打开数据库的写连接
    public SQLiteDatabase openWriteLink() {
        if (mWDB == null || !mWDB.isOpen()) {
            mWDB = this.getWritableDatabase();
        }
        return mWDB;
    }

    // 关闭数据库连接
    public void closeLink() {
        if (mRDB != null && mRDB.isOpen()) {
            mRDB.close();
            mRDB = null;
        }

        if (mWDB != null && mWDB.isOpen()) {
            mWDB.close();
            mWDB = null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建HotSaleItem表
        String sql = "CREATE TABLE IF NOT EXISTS "+TABLE_HotSaleItem+
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
                "tag VARCHAR NOT NULL,"+
                "name VARCHAR NOT NULL,"+
                "percentRange VARCHAR NOT NULL,"+
                "period VARCHAR NOT NULL,"+
                "minInvestment VARCHAR NOT NULL,"+
                "reservation INTEGER NOT NULL);";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 处理数据库版本升级
        if (oldVersion < newVersion) {
            // 在这里添加版本升级逻辑
        }
    }

    public void insertHotSaleItem(List<HotSaleItem> list){
        if (mWDB == null || !mWDB.isOpen()) {
            openWriteLink();
        }
        try {
            mWDB.beginTransaction();//开启数据库事务
            for (HotSaleItem info:list){
                ContentValues values = new ContentValues();//SQLite 中用于封装 “字段 - 值” 的容器（类似 Map），专门给 insert/update 方法传参
                values.put("tag",info.getTag());
                values.put("name", info.getName());
                values.put("percentRange", info.getPercentRange());
                values.put("period", info.getPeriod());
                values.put("minInvestment", info.getMinInvestment());
                values.put("reservation",info.getReservation());
                mWDB.insert(TABLE_HotSaleItem,null,values);
            }
            mWDB.setTransactionSuccessful();//标记 “事务执行成功”—— 只有执行了这行，事务才会最终提交（所有插入生效）；如果没执行这行（比如循环中抛异常），事务会回滚，所有插入都作废。
        }catch (Exception e){  //捕获循环 / 插入过程中的所有异常
            e.printStackTrace(); //打印异常日志
        }finally { //无论 try 中是否抛异常，都会执行（用于释放资源）
            if (mWDB != null && mWDB.inTransaction()) {
                mWDB.endTransaction();
            }
        }

    }

    public List<HotSaleItem> queryAllHotSaleItem() {
        if (mRDB == null || !mRDB.isOpen()) {
            openReadLink();
        }
        String sql = "select * from " + TABLE_HotSaleItem;//拼接查询 SQL 语句：select * from 表名 表示 “查询表中所有列的所有行数据”
        List<HotSaleItem> list = new ArrayList<>();
        // 参数 1：要执行的查询 SQL（这里是查所有商品）；
        // 参数 2：SQL 中的占位符参数（比如 select * from goods_info where price > ?，第二个参数就传 new String[]{"1000"}），这里没有占位符，传 null；
        // 返回值：Cursor 对象（结果集游标），指向查询结果的初始位置（-1，第一行之前）
        Cursor cursor = mRDB.rawQuery(sql, null);
        while (cursor.moveToNext()) { //将 Cursor 指针移到下一行，返回 boolean
            HotSaleItem info = new HotSaleItem(); //新建一个 GoodsInfo 商品对象，用于封装当前行的字段值
            info.id = cursor.getInt(0);
            info.tag = cursor.getString(1);
            info.name = cursor.getString(2);
            info.percentRange = cursor.getString(3);
            info.period = cursor.getString(4);
            info.minInvestment=cursor.getString(5);
            info.reservation=cursor.getInt(6);
            list.add(info);
        }
        cursor.close();
        return list;
    }
    //修改特定字段名的数据reservation为1
    public void setreservation(HotSaleItem item,int num) {
        if (mWDB == null || !mWDB.isOpen()) {
            openWriteLink();
        }
        try {
            mWDB.beginTransaction();
            ContentValues values = new ContentValues();
            values.put("reservation", num);
            mWDB.update(TABLE_HotSaleItem, values, "_id=?", new String[]{String.valueOf(item.id)});
            mWDB.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mWDB != null && mWDB.inTransaction()) {
                mWDB.endTransaction();
            }
        }
    }

    //用于查询res为1的数据
    public List<HotSaleItem> queryspecHotSaleItem(){
        List<HotSaleItem> list = new ArrayList<>();
        if (mRDB == null || !mRDB.isOpen()) {
            openReadLink();
        }
        String sql="select * from "+TABLE_HotSaleItem+ " where reservation = ?";
        Cursor cursor = mRDB.rawQuery(sql, new String[]{"1"});
        while (cursor.moveToNext()){
            HotSaleItem info = new HotSaleItem(); //新建一个 GoodsInfo 商品对象，用于封装当前行的字段值
            info.id = cursor.getInt(0);
            info.tag = cursor.getString(1);
            info.name = cursor.getString(2);
            info.percentRange = cursor.getString(3);
            info.period = cursor.getString(4);
            info.minInvestment=cursor.getString(5);
            info.reservation=cursor.getInt(6);
            list.add(info);
        }
        return list;
    }

}
