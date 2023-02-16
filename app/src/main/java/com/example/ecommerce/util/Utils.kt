package com.example.ecommerce.util

import android.app.Activity
import android.os.Build
import android.text.format.DateFormat
import android.util.Log
import android.util.Patterns
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*


fun isEmailValid(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun logMessage(message: String) {
    Log.d(Constants.TAG, message)
}
fun <T : Serializable?> getSerializable(activity: Activity, name: String, clazz: Class<T>): T
{
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        activity.intent.getSerializableExtra(name, clazz)!!
    else
        activity.intent.getSerializableExtra(name) as T
}

fun getDateAndTime(): String {
    val calendar = Calendar.getInstance()
    val df = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
    return  df.format(calendar.time)
}

fun getTimeInMillis(): Long {
    return GregorianCalendar().timeInMillis
}

fun showSnackBar(view: View, message: String): Snackbar {
    val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
    snackbar.show()
    return snackbar
}


