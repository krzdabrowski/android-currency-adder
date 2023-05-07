package eu.krzdabrowski.currencyadder.core.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

// Based on: https://github.com/google/accompanist/blob/main/permissions/src/main/java/com/google/accompanist/permissions/PermissionsUtil.kt
fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    error("Permissions should be called in the context of an Activity")
}
