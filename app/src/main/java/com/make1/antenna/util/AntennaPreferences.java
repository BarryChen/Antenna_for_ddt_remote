package com.make1.antenna.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

/**
 * Created by Vange on 2017/9/27.
 * <p>
 * Email:Vangelis.wang@make1.cn
 * Company:Make1
 */

public class AntennaPreferences {

    private static class SingletonHolder {
        private static final AntennaPreferences INSTANCE = new AntennaPreferences();
    }

    public static AntennaPreferences getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init(Context context) {
        sContext = context.getApplicationContext();
    }

    private static Context sContext;

    //登陆后获取的Token值
    private static final String PORT = "port";

    /**
     * 存储用户别名
     *
     * @param nickName
     */
    public void savePortData(String nickName) {
        if (TextUtils.isEmpty(nickName)) {
            Logger.e("PORT 为空,无法存储");
        } else {
            Logger.i("Save PORT:" + nickName + ",Success!");
            saveString(PORT, nickName);
        }
    }

    /**
     * 获取用户别名
     *
     * @return
     */
    public String getPortData() {
        String nickName = getString(PORT, null);
        if (nickName == null) {
            Logger.i("PORT为空");
            return null;
        } else {
            return nickName;
        }
    }

    /**
     * 置空所有存储的值
     */
    public void setAllValueToNull() {
        getPreferences().edit().clear().apply();
    }

    private boolean getBoolean(String key, boolean defValue) {
        return getPreferences().getBoolean(key, defValue);
    }

    private void saveBoolean(String key, boolean value) {
        getPreferences().edit().putBoolean(key, value).apply();
    }

    private int getInt(String key, int defValue) {
        return getPreferences().getInt(key, defValue);
    }

    private void saveInt(String key, int value) {
        getPreferences().edit().putInt(key, value).apply();
    }

    private long getLong(String key, long defValue) {
        return getPreferences().getLong(key, defValue);
    }

    private void saveLong(String key, long value) {
        getPreferences().edit().putLong(key, value).apply();
    }

    private String getString(String key, @Nullable String defValue) {
        return getPreferences().getString(key, defValue);
    }

    private void saveString(String key, @Nullable String value) {
        getPreferences().edit().putString(key, value).apply();
    }

    private SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(sContext);
    }
}
