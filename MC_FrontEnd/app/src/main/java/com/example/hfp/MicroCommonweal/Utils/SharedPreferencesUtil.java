package com.example.hfp.MicroCommonweal.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil{
    private Context context;
    private SharedPreferences sp = null;
    private SharedPreferences.Editor edit = null;
    public SharedPreferencesUtil(Context context, SharedPreferences sp) {
        this.context = context;
        this.sp = sp;
        edit = sp.edit();
    }
    public SharedPreferencesUtil(Context context, String filename) {
        this(context, context.getSharedPreferences(filename,
                Context.MODE_PRIVATE));
    }
    // 设置值
    public void setValue(String key, String value) {
        edit.putString(key, value);
        edit.commit();
    }
    // 获取值
    public String getValue(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    public void clear(){
        edit.clear();
        edit.commit();
    }

}
