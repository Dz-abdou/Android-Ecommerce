package com.example.ecommerce.util

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView

fun moveViewOutOfScreenLeftWithAnimation(view: View, context: Context) {
    view.animate().translationX(
        -context.resources.displayMetrics.widthPixels
            .toFloat()
    )
}

fun moveViewOutOfScreenRightWithAnimation(view: View, context: Context) {
    view.animate().translationX(
        context.resources.displayMetrics.widthPixels.toFloat()
    )
}

fun moveViewToInsideOfTheScreenWithAnimation(view: View) {
    view.animate().translationX(0F)
}

fun moveViewOutsideOfTheScreenLeft(view: View, context: Context) {
    view.translationX = -context.resources.displayMetrics.widthPixels
        .toFloat()
}

fun moveViewOutsideOfTheScreenRight(view: View, context: Context) {
    view.translationX = context.resources.displayMetrics.widthPixels
        .toFloat()
}


fun setProgressDialog(context: Context, message: String): AlertDialog {
    val llPadding = 30
    val ll = LinearLayout(context)
    ll.orientation = LinearLayout.HORIZONTAL
    ll.setPadding(llPadding, llPadding, llPadding, llPadding)
    ll.gravity = Gravity.CENTER
    var llParam = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )
    llParam.gravity = Gravity.CENTER
    ll.layoutParams = llParam

    val progressBar = ProgressBar(context)
    progressBar.isIndeterminate = true
    progressBar.setPadding(0, 0, llPadding, 0)
    progressBar.layoutParams = llParam

    llParam = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    llParam.gravity = Gravity.CENTER
    val tvText = TextView(context)
    tvText.text = message
    tvText.setTextColor(Color.parseColor("#000000"))
    tvText.textSize = 20.toFloat()
    tvText.layoutParams = llParam

    ll.addView(progressBar)
    ll.addView(tvText)

    val builder = AlertDialog.Builder(context)
    builder.setCancelable(true)
    builder.setView(ll)

    val dialog = builder.create()
    val window = dialog.window
    if (window != null) {
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = layoutParams
    }
    return dialog
}
