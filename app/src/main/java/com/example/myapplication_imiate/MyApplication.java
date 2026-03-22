package com.example.myapplication_imiate;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.example.myapplication_imiate.database.dbhelper;
import com.example.myapplication_imiate.entiy.HotSaleItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {
    private static MyApplication mApp;
    public static MyApplication getInstance() {
        return mApp;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        mApp = this;
        Log.d("ning", "MyApplication onCreate");

        initHotSaleItem();
    }

    private void initHotSaleItem() {
        List<HotSaleItem> items = new ArrayList<>();
        items.add(new HotSaleItem(
                "推荐",
                "政盈11号24个月",
                "6.9%~6.9%",
                "期限：24个月",
                "起投金额：100万"
        ));
        items.add(new HotSaleItem(
                "推荐",
                "宝能华府24个月",
                "6.5%~7.9%",
                "期限：24个月",
                "起投金额：10万"
        ));
        items.add(new HotSaleItem(
                "推荐",
                "宝能华府15个⽉",
                "6.35%~7.2%",
                "期限：15个月",
                "起投金额：10万"
        ));
        items.add(new HotSaleItem(
                "推荐",
                "宝能华府18个⽉",
                "6.3%~7.3%",
                "期限：18个月",
                "起投金额：10万"
        ));
        items.add(new HotSaleItem(
                "推荐",
                "宝能华府18个⽉",
                "6.3%~7.3%",
                "期限：18个月",
                "起投金额：10万"
        ));
        items.add(new HotSaleItem(
                "推荐",
                "宝能华府18个⽉",
                "6.3%~7.3%",
                "期限：18个月",
                "起投金额：10万"
        ));
        boolean isFirst=SharedUtil.getInstance(this).readBoolean("first", true);
        if (isFirst){
            dbhelper dbHelper=dbhelper.getInstance(this);
            dbHelper.openWriteLink();
            dbHelper.insertHotSaleItem(items);
            dbHelper.closeLink();
            // 把是否首次打开写入共享参数
            SharedUtil.getInstance(this).writeBoolean("first", false);
        }
    }
}
