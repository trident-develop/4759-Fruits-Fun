package org.example.project

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

@SuppressLint("StaticFieldLeak")
object AndroidContext {
    lateinit var context: Context
}

actual class AppPreferences(private val prefs: SharedPreferences) {
    actual fun isOnboardingCompleted(): Boolean =
        prefs.getBoolean("onboarding_completed", false)

    actual fun setOnboardingCompleted() {
        prefs.edit().putBoolean("onboarding_completed", true).apply()
    }

    actual fun getString(key: String, default: String): String =
        prefs.getString(key, default) ?: default

    actual fun putString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    actual fun getInt(key: String, default: Int): Int =
        prefs.getInt(key, default)

    actual fun putInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }
}

actual fun createAppPreferences(): AppPreferences {
    val prefs = AndroidContext.context.getSharedPreferences("fruits_fun_prefs", Context.MODE_PRIVATE)
    return AppPreferences(prefs)
}
