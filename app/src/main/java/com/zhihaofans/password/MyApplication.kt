package com.zhihaofans.password

import android.app.Application
import android.support.v7.app.AppCompatActivity
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.zhihaofans.password.util.AndroidUtil

/**
 *
 * @author zhihaofans
 * @date 2017/12/18
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Logger.addLogAdapter(AndroidLogAdapter())
        Logger.d("初始化完毕")
    }
}