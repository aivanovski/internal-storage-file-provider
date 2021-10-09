package com.github.ai.fprovider.demo.extension

import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun Fragment.setupActionBar(action: ActionBar.() -> Unit) {
    val activity = (this.activity as? AppCompatActivity) ?: return

    activity.supportActionBar?.run {
        action.invoke(this)
    }
}

fun Fragment.showToastMessage(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT)
        .show()
}