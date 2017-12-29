package com.zhihaofans.password.util

import android.R.id.edit
import android.annotation.SuppressLint
import android.content.SharedPreferences.Editor
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences


/**
 *
 * @author zhihaofans
 * @date 2017/12/18
 */
class AndroidUtil {
    class Application {
        @SuppressLint("ApplySharedPref")
        fun setting(sharedPreferences: SharedPreferences, SetName: String) {
            fun save(string: String) {
                sharedPreferences.edit().putString(SetName, string).commit()
            }

            fun get(defaultString: String): String {
                return sharedPreferences.getString(SetName, defaultString)
            }

            fun save(stringSet: MutableSet<String>) {
                sharedPreferences.edit().putStringSet(SetName, stringSet).commit()
            }

            fun get(defaultStringSet: MutableSet<String>): MutableSet<String> {
                return sharedPreferences.getStringSet(SetName, defaultStringSet)
            }

            fun save(int: Int) {
                sharedPreferences.edit().putInt(SetName, int).commit()
            }

            fun get(defaultInt: Int): Int {
                return sharedPreferences.getInt(SetName, defaultInt)
            }

            fun save(boolean: Boolean) {
                sharedPreferences.edit().putBoolean(SetName, boolean).commit()
            }

            fun get(defaultBoolean: Boolean): Boolean {
                return sharedPreferences.getBoolean(SetName, defaultBoolean)
            }
        }
    }
}