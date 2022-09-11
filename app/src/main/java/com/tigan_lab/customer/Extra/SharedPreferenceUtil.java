package com.tigan_lab.customer.Extra;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferenceUtil {

    private SharedPreferences _prefs = null;
    private Editor _editor = null;

    public SharedPreferenceUtil(Context context) {
        this._prefs = context.getSharedPreferences("GOGrocer",
                Context.MODE_PRIVATE);
        this._editor = this._prefs.edit();

    }


    public String getString(String key, String defaultvalue) {
        if (this._prefs == null) {
            return defaultvalue;
        }
        return this._prefs.getString(key, defaultvalue);
    }

    public void setString(String key, String value) {
        if (this._editor == null) {
            return;
        }
        this._editor.putString(key, value);
        _editor.apply();
    }

    public Boolean getBoolean(String key, Boolean defaultvalue) {
        if (this._prefs == null) {
            return defaultvalue;
        }
        return this._prefs.getBoolean(key, defaultvalue);
    }

    public void setBoolean(String key, Boolean value) {
        if (this._editor == null) {
            return;
        }
        this._editor.putBoolean(key, value);
        _editor.apply();
    }

    public int getInt(String key, int defaultvalue) {
        if (this._prefs == null) {
            return defaultvalue;
        }
        return this._prefs.getInt(key, defaultvalue);
    }

    public void setInt(String key, int value) {
        if (this._editor == null) {
            return;
        }
        this._editor.putInt(key, value);
        _editor.apply();
    }
    public void clearAll() {
        if (this._editor == null) {
            return;
        }
        this._editor.clear().apply();

    }

    public void removeOneItem(String key) {
        if (this._editor == null) {
            return;
        }
        this._editor.remove(key);
    }

    public void save() {
        if (this._editor == null) {
            return;
        }
        this._editor.apply();
    }


}