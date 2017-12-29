package com.zhihaofans.password

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.google.gson.JsonArray
import com.orhanobut.logger.Logger
import com.zhihaofans.password.gson.PasswordGson
import com.zhihaofans.password.util.PasswordUtil
import com.zhihaofans.password.util.VarUtil
import com.zhihaofans.password.view.EditorActivity

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onItemClick

class MainActivity : AppCompatActivity() {
    private val testJson: String = "{\"creattime\":0,\"edittime\":1,\"type\":\"app\",\"account\":\"user\",\"password\":\"password\",\"title\":\"app\",\"site\":\"com.zhihaofans.android\",\"icon\":\"icon\"}"
    private val pw = PasswordUtil()
    private val varUtil = VarUtil()

    @SuppressLint("ApplySharedPref")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val sharedPreferences = getSharedPreferences("com.zhihaofans.password", MODE_PRIVATE)
        varUtil.sharedPreferences = sharedPreferences
        varUtil.passwordUtil = pw
        pw.init(sharedPreferences)
        initList()
        fab.setOnClickListener { view ->
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
            editPw(-1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Logger.d("onActivityResult\nrequestCode:$requestCode\nresultCode:$resultCode")
        pw.initPasswordList()
        initList()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                Snackbar.make(coordinatorLayout_main, "还没有设置呢。", Snackbar.LENGTH_SHORT).show()
                true
            }
            R.id.action_export -> {
                val pwTxt = pw.export2txt()
                alert {
                    this@alert.title = getString(R.string.text_plzCopy)
                    customView {
                        linearLayout {
                            val et = editText(pwTxt)
                            Logger.d(et)
                            positiveButton("分享") {
                                share(et.text.toString())
                            }
                        }
                    }
                }.show()
                Logger.d(pwTxt)
                true
            }
            R.id.action_import -> {
                var pwTxt = pw.export2txt()
                alert {
                    this@alert.title = getString(R.string.action_import)
                    message = "请确保备份正确，否则后果自负。"
                    customView {
                        linearLayout {
                            val et = editText()
                            et.layoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT
                            et.singleLine = true
                            yesButton {
                                pwTxt = et.text.toString()
                                if (pwTxt.isEmpty()) {
                                    Snackbar.make(coordinatorLayout_main, "请输入内容", Snackbar.LENGTH_SHORT).show()
                                } else {
                                    if (pw.testBackup(pwTxt)) {
                                        pw.import(pwTxt)
                                        pw.initPasswordList()
                                        initList()
                                        Snackbar.make(coordinatorLayout_main, "导入完毕，如果无法进入程序请在系统的应用管理里清空本程序所有数据。", Snackbar.LENGTH_SHORT).show()
                                    } else {
                                        Snackbar.make(coordinatorLayout_main, "备份格式错误，导入失败。", Snackbar.LENGTH_SHORT).show()
                                    }
                                }
                            }
                            noButton { }
                        }
                    }
                }.show()
                Logger.d(pwTxt)
                true
            }
            R.id.action_init -> {
                alert("初始化用于解决导入错误数据导致出现BUG，将清空密码库，请注意备份。", getString(R.string.text_initPassword) + "?") {
                    yesButton {
                        pw.clearAll()
                        initList()
                        Snackbar.make(coordinatorLayout_main, "初始化完毕，密码库已被清空，现在你可以导入你的备份。", Snackbar.LENGTH_SHORT).show()
                    }
                    noButton { }
                }.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun editPw(pwIndex: Int) {
        val intent = Intent(this, EditorActivity::class.java)
        intent.putExtra("pw_index", pwIndex)
        startActivityForResult(intent, 0)
    }

    private fun initList() {
        val passwords: MutableList<PasswordGson> = pw.getList()
        Logger.d(passwords)
        this@MainActivity.title = "共有 ${passwords.size} 个密码"
        val listData = passwords.map { it.title }
        //lv.adapter = PwAdapter(this@MainActivity, R.layout.item_listview, listData)
        lv.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listData)
        lv.onItemClick { _, _, index, _ ->
            editPw(index)
        }
        //Snackbar.make(coordinatorLayout_main, R.string.text_loaded, Snackbar.LENGTH_LONG).setAction("Action", null).show()
    }

}
