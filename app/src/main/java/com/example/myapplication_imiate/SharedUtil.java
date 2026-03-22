package com.example.myapplication_imiate;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedUtil {
    private static SharedUtil mUtil;// 静态成员变量，用于缓存 SharedUtil 的唯一实例（单例模式核心）
    private SharedPreferences preferences;

    public static SharedUtil getInstance(Context ctx){
        if (mUtil == null) {
            mUtil = new SharedUtil();
            mUtil.preferences = ctx.getSharedPreferences("HotSaleItem", Context.MODE_PRIVATE);//存储文件名为 HotSaleItem.xml,私有模式，仅本应用可读写
        }
        return mUtil;
    }

    public void writeBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();// // 获取编辑器
        editor.putBoolean(key, value); // 放入数据
        editor.commit();  // 同步提交保存
    }

    public boolean readBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }
}
