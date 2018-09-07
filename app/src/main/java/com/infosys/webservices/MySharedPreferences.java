package com.infosys.webservices;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MySharedPreferences {


    public synchronized static MySharedPreferences newInstance() {
        MySharedPreferences sharePre = new MySharedPreferences();
        return sharePre;
    }

    public SharedPreferences getSharedFile(Context context) {
        SharedPreferences shareFile = PreferenceManager.getDefaultSharedPreferences(context);//context.getSharedPreferences(preferencFile, Context.MODE_PRIVATE);
        return shareFile;
    }

    public void setValues(Context context, JSONObject map) {
        SharedPreferences shareFile = getSharedFile(context);
        Editor editor = shareFile.edit();
        try {
            if (map != null && map.length() > 0) {
                Iterator<String> iterator = map.keys();
                Object obj = null;
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    obj = map.get(key);
                    if (obj instanceof String) {
                        editor.putString(key, String.valueOf(obj));
                    } else if (obj instanceof Integer) {
                        editor.putInt(key, (Integer) obj);
                    } else if (obj instanceof Boolean) {
                        editor.putBoolean(key, (Boolean) obj);
                    } else {
                        editor.putString(key, String.valueOf(obj));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        editor.commit();
    }

    public Map<String, ?> getMap(Context context) {
        SharedPreferences shareFile = getSharedFile(context);
        Map<String, ?> map = shareFile.getAll();
        return map;
    }


    public void clearShareFile(Context context) {
        SharedPreferences shareFile = getSharedFile(context);
        shareFile.edit().clear().commit();
    }

    public String getString(Context context, String key, String defaultValue) {
        if (context == null)
            return "";
        SharedPreferences sp = newInstance().getSharedFile(context);
        return sp.getString(key, defaultValue);
    }


    public void putStringKeyValue(Context context, String key, String value) {
        Editor ed = newInstance().getSharedFile(context).edit();
        ed.putString(key, value);
        ed.commit();
    }


    public String getStringUser(Context context, String key, String defaultValue) {
        SharedPreferences sp = newInstance().getSharedFile(context);
        return sp.getString(key, defaultValue);
    }
}
