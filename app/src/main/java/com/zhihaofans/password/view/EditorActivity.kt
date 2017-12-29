package com.zhihaofans.password.view

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.orhanobut.logger.Logger
import com.zhihaofans.password.R
import com.zhihaofans.password.gson.PasswordGson
import com.zhihaofans.password.util.PasswordUtil
import kotlinx.android.synthetic.main.activity_editor.*
import kotlinx.android.synthetic.main.content_editor.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.yesButton
import android.view.inputmethod.InputMethodManager


class EditorActivity : AppCompatActivity() {
    private var password = mutableListOf<PasswordGson>()
    private var this_pw = PasswordGson()
    private val pw = PasswordUtil()
    private var editMode = false
    private var editIndex = -1
    private var _title = ""
    private var _ac = ""
    private var _pw = ""
    private var _site = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this@EditorActivity.title = getString(R.string.text_edit)
        if (intent.extras.size() > 0 && intent.hasExtra("pw_index")) {
            editIndex = intent.getIntExtra("pw_index", -1)
        }
        Logger.d("editIndex:$editIndex")
        pw.init(getSharedPreferences("com.zhihaofans.password", MODE_PRIVATE))
        password = pw.getList()
        if ((editMode || editIndex != -1) && password.size == 0) {
            button_save.isClickable = false
            button_remove.isClickable = false
            Logger.e("password.size == ${password.size}")
            Snackbar.make(coordinatorLayout_editor, "发生错误，共有0个密码", Snackbar.LENGTH_LONG).show()
        } else {
            init(editIndex)
            if (!editMode) {
                this@EditorActivity.title = getString(R.string.text_addnew)
            }
            button_remove.onClick {
                hintKeyboard()
                alert {
                    message = "确定要删除吗？"
                    yesButton {
                        pw.remove(editIndex)
                        finish()
                    }
                    noButton { }
                }.show()
            }
            button_save.onClick {
                hintKeyboard()
                _title = editText_title.text.toString()
                _ac = editText_account.text.toString()
                _pw = editText_password.text.toString()
                _site = editText_site.text.toString()
                if (_title.isEmpty() || _ac.isEmpty() || editText_password.text.isEmpty() || _site.isEmpty()) {
                    Snackbar.make(coordinatorLayout_editor, "必须全部输入", Snackbar.LENGTH_SHORT).show()
                } else {
                    Logger.d("yes\n$_title\n$_ac\n$_pw\n$_site")
                    pw.initPasswordList()
                    if (editMode) {
                        pw.edit(editIndex, _title, _ac, _pw, _site)
                    } else {
                        pw.add(_title, _ac, _pw, _site)
                        password = pw.getList()
                        init(0)
                        editMode = true
                    }
                    Snackbar.make(coordinatorLayout_editor, R.string.text_finish, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        this@EditorActivity.setResult(0)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                android.R.id.home -> {
                    hintKeyboard()
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun hintKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive && currentFocus != null) {
            if (currentFocus!!.windowToken != null) {
                imm.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
    }

    private fun init(index: Int) {
        if (editIndex != index) {
            editIndex = index
        }
        button_save.isClickable = true
        if (editIndex != -1) {
            editMode = true
            this_pw = password[editIndex]
            _title = this_pw.title
            _ac = this_pw.account
            _pw = this_pw.password
            _site = this_pw.site
            editText_title.setText(_title)
            editText_account.setText(_ac)
            editText_password.setText(_pw)
            editText_site.setText(_site)
        }
        button_remove.isClickable = !editMode
    }
}
