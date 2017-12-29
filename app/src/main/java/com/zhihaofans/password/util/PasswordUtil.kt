package com.zhihaofans.password.util

import android.content.SharedPreferences
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import com.zhihaofans.password.gson.PasswordGson
import com.zhihaofans.password.gson.PasswordListGson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonParser


/**
 *
 * @author zhihaofans
 * @date 2017/12/18
 */

class PasswordUtil {
    private val g = Gson()

    private var passwordJson = "[]"
    private var pwList = PasswordListGson()

    private var password = mutableSetOf<String>()
    private var passwords = mutableListOf<PasswordGson>()
    private var password_app = mutableListOf<PasswordGson>()
    private var password_web = mutableListOf<PasswordGson>()
    private val varUtil = VarUtil()


    fun init(sp: SharedPreferences) {
        varUtil.sharedPreferences = sp
        initPasswordList()
    }


    fun initPasswordList(): Boolean {
        clearCache()
        passwordJson = varUtil.sharedPreferences.getString("passwordlist", passwordJson)
        val parser = JsonParser()
        return if (testBackup(passwordJson)) {
            val jsonArray = parser.parse(passwordJson).asJsonArray
            for (js in jsonArray) {
                passwords.add(g.fromJson(js, PasswordGson::class.java))
            }
            for (pw in passwords) {
                when (pw.type) {
                    "app" -> password_app.add(pw)
                    "web" -> password_web.add(pw)
                }
            }
            true
        } else {
            false
        }
    }

    fun testBackup(backup: String): Boolean {
        try {
            val parser = JsonParser()
            val jsonArray = parser.parse(backup).asJsonArray
            Logger.d(jsonArray.size())
            val passwordTest = mutableListOf<PasswordGson>()
            for (js in jsonArray) {
                val thisJs = g.fromJson(js, PasswordGson::class.java)
                passwordTest.add(thisJs)
                Logger.d(thisJs.all)
            }
            Logger.d(passwordTest.size)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun clearAll() {
        //varUtil.sharedPreferences.edit().putStringSet("password", mutableSetOf<String>()).apply()
        varUtil.sharedPreferences.edit().putString("passwordlist", "[]").apply()
        initPasswordList()
    }

    fun testInit(): PasswordListGson {
        return g.fromJson(export2txt(), PasswordListGson::class.java)
    }

    fun add(title: String, account: String, pass_word: String, site: String) {
        val thisPw = PasswordGson()
        val nowTime = (System.currentTimeMillis() / 1000).toInt()
        Logger.d("yes\n$title\n$account\n$pass_word\n$site")
        thisPw.title = title
        thisPw.account = account
        thisPw.password = pass_word
        thisPw.site = site
        thisPw.editTime = nowTime
        thisPw.creatTime = nowTime
        Logger.d(thisPw.creatTime)
        passwords.add(0, thisPw)
        updatePassword(passwords)
    }

    fun edit(pwIndex: Int, title: String, account: String, pass_word: String, site: String) {
        val pwGson = passwords[pwIndex]
        val nowTime = (System.currentTimeMillis() / 1000).toInt()
        Logger.d("edit\n$title\n$account\n$pass_word\n$site\n${pwGson.creatTime}")
        pwGson.title = title
        pwGson.account = account
        pwGson.password = pass_word
        pwGson.site = site
        pwGson.editTime = nowTime
        passwords[pwIndex] = pwGson
        updatePassword(passwords)
    }

    fun remove(index: Int) {
        passwords.removeAt(index)
        updatePassword(passwords)
    }

    fun save() {
        varUtil.sharedPreferences.edit().putString("passwordlist", passwordJson).apply()
    }

    fun clearCache() {
        //Clear cache, You need init after clear cache.
        passwordJson = "[]"
        password = mutableSetOf()
        passwords = mutableListOf()
        password_app = mutableListOf()
        password_web = mutableListOf()

    }

    fun import(json: String) {
        passwordJson = json
        save()
    }

    fun export2txt(): String {
        return if (passwords.isEmpty()) {
            "[]"
        } else {
            g.toJson(passwords)
        }
    }

    fun getList(): MutableList<PasswordGson> {
        return passwords
    }

    fun updatePassword(newPw: MutableList<PasswordGson>) {
        passwords - newPw
        passwordJson = if (passwords.size > 0) {
            g.toJson(passwords)
        } else {
            "[]"
        }
        save()
    }


    fun getAppPassword(): MutableList<PasswordGson> {
        return password_app
    }

    fun getWebPassword(): MutableList<PasswordGson> {
        return password_web
    }
}