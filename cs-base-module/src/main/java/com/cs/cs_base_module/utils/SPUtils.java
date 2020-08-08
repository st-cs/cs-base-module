package com.cs.cs_base_module.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.GsonUtils;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SPUtils {

    private static final Map<String, SPUtils> SP_UTILS_MAP = new HashMap<>();
    private static String SP_NAME = "infinite_sp";

    /**
     * @see com.yy.common.constants.SPConstants 常量维护类
     */
    public static final String KEY_USERDATA = "key_userdata";
    public static final String KEY_DOMAINLIST = "key_domain_list";
    /**
     * 赛事排序
     */
    public static final String EVENT_SORT = "EVENT_SORT";
    /**
     * 盘口
     */
    public static final String EVENT_OT = "EVENT_OT";


    private SharedPreferences sp;

    /**
     * Return the single {@link SPUtils} instance
     *
     * @return the single {@link SPUtils} instance
     */
    public static SPUtils getInstance() {
        return getInstance("", Context.MODE_PRIVATE);
    }

    /**
     * Return the single {@link SPUtils} instance
     *
     * @param mode Operating mode.
     * @return the single {@link SPUtils} instance
     */
    public static SPUtils getInstance(final int mode) {
        return getInstance("", mode);
    }

    /**
     * Return the single {@link SPUtils} instance
     *
     * @param spName The name of sp.
     * @return the single {@link SPUtils} instance
     */
    public static SPUtils getInstance(String spName) {
        return getInstance(spName, Context.MODE_PRIVATE);
    }

    /**
     * Return the single {@link SPUtils} instance
     *
     * @param spName The name of sp.
     * @param mode   Operating mode.
     * @return the single {@link SPUtils} instance
     */
    public static SPUtils getInstance(String spName, final int mode) {
        if (isSpace(spName)) spName = SP_NAME;
        SPUtils SharePreferenceUtils = SP_UTILS_MAP.get(spName);
        if (SharePreferenceUtils == null) {
            synchronized (SPUtils.class) {
                SharePreferenceUtils = SP_UTILS_MAP.get(spName);
                if (SharePreferenceUtils == null) {
                    SharePreferenceUtils = new SPUtils(spName, mode);
                    SP_UTILS_MAP.put(spName, SharePreferenceUtils);
                }
            }
        }
        return SharePreferenceUtils;
    }

    private SPUtils(final String spName) {
        sp = AppManager.getsApplication().getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    private SPUtils(final String spName, final int mode) {
        sp = AppManager.getsApplication().getSharedPreferences(spName, mode);
    }

    /**
     * Put the string value in sp.
     *
     * @param key   The key of sp.
     * @param value The value of sp.
     */
    public void put(@NonNull final String key, final String value) {
        put(key, value, false);
    }

    /**
     * Put the string value in sp.
     *
     * @param key      The key of sp.
     * @param value    The value of sp.
     * @param isCommit True to use {@link SharedPreferences.Editor#commit()},
     *                 false to use {@link SharedPreferences.Editor#apply()}
     */
    public void put(@NonNull final String key, final String value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putString(key, value).commit();
        } else {
            sp.edit().putString(key, value).apply();
        }
    }

    /**
     * Return the string value in sp.
     *
     * @param key The key of sp.
     * @return the string value if sp exists or {@code ""} otherwise
     */
    public String getString(@NonNull final String key) {
        return getString(key, "");
    }

    /**
     * Return the string value in sp.
     *
     * @param key          The key of sp.
     * @param defaultValue The default value if the sp doesn't exist.
     * @return the string value if sp exists or {@code defaultValue} otherwise
     */
    public String getString(@NonNull final String key, final String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    /**
     * Put the int value in sp.
     *
     * @param key   The key of sp.
     * @param value The value of sp.
     */
    public void put(@NonNull final String key, final int value) {
        put(key, value, false);
    }

    /**
     * Put the int value in sp.
     *
     * @param key      The key of sp.
     * @param value    The value of sp.
     * @param isCommit True to use {@link SharedPreferences.Editor#commit()},
     *                 false to use {@link SharedPreferences.Editor#apply()}
     */
    public void put(@NonNull final String key, final int value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putInt(key, value).commit();
        } else {
            sp.edit().putInt(key, value).apply();
        }
    }

    /**
     * Return the int value in sp.
     *
     * @param key The key of sp.
     * @return the int value if sp exists or {@code -1} otherwise
     */
    public int getInt(@NonNull final String key) {
        return getInt(key, -1);
    }

    /**
     * Return the int value in sp.
     *
     * @param key          The key of sp.
     * @param defaultValue The default value if the sp doesn't exist.
     * @return the int value if sp exists or {@code defaultValue} otherwise
     */
    public int getInt(@NonNull final String key, final int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    /**
     * Put the long value in sp.
     *
     * @param key   The key of sp.
     * @param value The value of sp.
     */
    public void put(@NonNull final String key, final long value) {
        put(key, value, false);
    }

    /**
     * Put the long value in sp.
     *
     * @param key      The key of sp.
     * @param value    The value of sp.
     * @param isCommit True to use {@link SharedPreferences.Editor#commit()},
     *                 false to use {@link SharedPreferences.Editor#apply()}
     */
    public void put(@NonNull final String key, final long value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putLong(key, value).commit();
        } else {
            sp.edit().putLong(key, value).apply();
        }
    }

    /**
     * Return the long value in sp.
     *
     * @param key The key of sp.
     * @return the long value if sp exists or {@code -1} otherwise
     */
    public long getLong(@NonNull final String key) {
        return getLong(key, -1L);
    }

    /**
     * Return the long value in sp.
     *
     * @param key          The key of sp.
     * @param defaultValue The default value if the sp doesn't exist.
     * @return the long value if sp exists or {@code defaultValue} otherwise
     */
    public long getLong(@NonNull final String key, final long defaultValue) {
        return sp.getLong(key, defaultValue);
    }

    /**
     * Put the float value in sp.
     *
     * @param key   The key of sp.
     * @param value The value of sp.
     */
    public void put(@NonNull final String key, final float value) {
        put(key, value, false);
    }

    /**
     * Put the float value in sp.
     *
     * @param key      The key of sp.
     * @param value    The value of sp.
     * @param isCommit True to use {@link SharedPreferences.Editor#commit()},
     *                 false to use {@link SharedPreferences.Editor#apply()}
     */
    public void put(@NonNull final String key, final float value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putFloat(key, value).commit();
        } else {
            sp.edit().putFloat(key, value).apply();
        }
    }

    /**
     * Return the float value in sp.
     *
     * @param key The key of sp.
     * @return the float value if sp exists or {@code -1f} otherwise
     */
    public float getFloat(@NonNull final String key) {
        return getFloat(key, -1f);
    }

    /**
     * Return the float value in sp.
     *
     * @param key          The key of sp.
     * @param defaultValue The default value if the sp doesn't exist.
     * @return the float value if sp exists or {@code defaultValue} otherwise
     */
    public float getFloat(@NonNull final String key, final float defaultValue) {
        return sp.getFloat(key, defaultValue);
    }

    /**
     * Put the boolean value in sp.
     *
     * @param key   The key of sp.
     * @param value The value of sp.
     */
    public void put(@NonNull final String key, final boolean value) {
        put(key, value, false);
    }

    /**
     * Put the boolean value in sp.
     *
     * @param key      The key of sp.
     * @param value    The value of sp.
     * @param isCommit True to use {@link SharedPreferences.Editor#commit()},
     *                 false to use {@link SharedPreferences.Editor#apply()}
     */
    public void put(@NonNull final String key, final boolean value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putBoolean(key, value).commit();
        } else {
            sp.edit().putBoolean(key, value).apply();
        }
    }

    /**
     * Return the boolean value in sp.
     *
     * @param key The key of sp.
     * @return the boolean value if sp exists or {@code false} otherwise
     */
    public boolean getBoolean(@NonNull final String key) {
        return getBoolean(key, false);
    }

    /**
     * Return the boolean value in sp.
     *
     * @param key          The key of sp.
     * @param defaultValue The default value if the sp doesn't exist.
     * @return the boolean value if sp exists or {@code defaultValue} otherwise
     */
    public boolean getBoolean(@NonNull final String key, final boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    /**
     * Put the set of string value in sp.
     *
     * @param key   The key of sp.
     * @param value The value of sp.
     */
    public void put(@NonNull final String key, final Set<String> value) {
        put(key, value, false);
    }

    /**
     * Put the set of string value in sp.
     *
     * @param key      The key of sp.
     * @param value    The value of sp.
     * @param isCommit True to use {@link SharedPreferences.Editor#commit()},
     *                 false to use {@link SharedPreferences.Editor#apply()}
     */
    public void put(@NonNull final String key,
                    final Set<String> value,
                    final boolean isCommit) {
        if (isCommit) {
            sp.edit().putStringSet(key, value).commit();
        } else {
            sp.edit().putStringSet(key, value).apply();
        }
    }

    /**
     * put the entity of in sp
     *
     * @param key      key
     * @param javaBean entity
     */
    public void put(@NonNull final String key, final Object javaBean) {
        put(key, javaBean, false);
    }

    /**
     * put the entity of in sp
     *
     * @param key      string key
     * @param javaBean entity
     * @param isCommit commit or apply
     */
    public void put(@NonNull final String key, final Object javaBean, boolean isCommit) {
        if (isCommit) {
            sp.edit().putString(key, GsonUtils.toJson(javaBean)).commit();
        } else {
            sp.edit().putString(key, GsonUtils.toJson(javaBean)).apply();
        }
    }

    /**
     * return the entity from in sp
     *
     * @param key    string key
     * @param tClass entity class
     * @param <T>    type
     * @return return entity
     */
    public <T extends Object> T getEntity(@NonNull final String key, final Class<T> tClass) {
        String value = getString(key, "");
        return GsonUtils.fromJson(value, tClass);
    }

    /**
     * return the entity from in sp
     *
     * @param key          string key
     * @param tClass       entity class
     * @param defaultValue default value
     * @param <T>          type
     * @return return entity
     */
    public <T extends Object> T getEntity(@NonNull final String key, final Class<T> tClass, final String defaultValue) {
        String value = getString(key, defaultValue);
        return GsonUtils.fromJson(value, tClass);
    }


    /**
     * @param key
     * @param typeOfT
     * @param <T>
     * @return
     */
    public <T extends Object> T getEntity(@NonNull final String key, Type typeOfT) {
        String value = getString(key, "");
        return GsonUtils.fromJson(value, typeOfT);
    }

    /**
     * Return the set of string value in sp.
     *
     * @param key The key of sp.
     * @return the set of string value if sp exists
     * or {@code Collections.<String>emptySet()} otherwise
     */
    public Set<String> getStringSet(@NonNull final String key) {
        return getStringSet(key, Collections.<String>emptySet());
    }

    /**
     * Return the set of string value in sp.
     *
     * @param key          The key of sp.
     * @param defaultValue The default value if the sp doesn't exist.
     * @return the set of string value if sp exists or {@code defaultValue} otherwise
     */
    public Set<String> getStringSet(@NonNull final String key,
                                    final Set<String> defaultValue) {
        return sp.getStringSet(key, defaultValue);
    }


    /**
     * Return all values in sp.
     *
     * @return all values in sp
     */
    public Map<String, ?> getAll() {
        return sp.getAll();
    }

    /**
     * Return whether the sp contains the preference.
     *
     * @param key The key of sp.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public boolean contains(@NonNull final String key) {
        return sp.contains(key);
    }

    /**
     * Remove the preference in sp.
     *
     * @param key The key of sp.
     */
    public void remove(@NonNull final String key) {
        remove(key, false);
    }

    /**
     * Remove the preference in sp.
     *
     * @param key      The key of sp.
     * @param isCommit True to use {@link SharedPreferences.Editor#commit()},
     *                 false to use {@link SharedPreferences.Editor#apply()}
     */
    public void remove(@NonNull final String key, final boolean isCommit) {
        if (isCommit) {
            sp.edit().remove(key).commit();
        } else {
            sp.edit().remove(key).apply();
        }
    }

    /**
     * Remove all preferences in sp.
     */
    public void clear() {
        clear(false);
    }

    /**
     * Remove all preferences in sp.
     *
     * @param isCommit True to use {@link SharedPreferences.Editor#commit()},
     *                 false to use {@link SharedPreferences.Editor#apply()}
     */
    public void clear(final boolean isCommit) {
        if (isCommit) {
            sp.edit().clear().commit();
        } else {
            sp.edit().clear().apply();
        }
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 设置项目SP的名字
     *
     * @param spName
     */
    public static void setSpName(String spName) {
        SP_NAME = spName;
    }
}
