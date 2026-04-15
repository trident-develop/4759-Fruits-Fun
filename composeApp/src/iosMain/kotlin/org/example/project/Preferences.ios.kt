package org.example.project

import platform.Foundation.NSUserDefaults

actual class AppPreferences(private val defaults: NSUserDefaults) {
    actual fun isOnboardingCompleted(): Boolean =
        defaults.boolForKey("onboarding_completed")

    actual fun setOnboardingCompleted() {
        defaults.setBool(true, forKey = "onboarding_completed")
    }

    actual fun getString(key: String, default: String): String =
        defaults.stringForKey(key) ?: default

    actual fun putString(key: String, value: String) {
        defaults.setObject(value, forKey = key)
    }

    actual fun getInt(key: String, default: Int): Int =
        if (defaults.objectForKey(key) != null) defaults.integerForKey(key).toInt() else default

    actual fun putInt(key: String, value: Int) {
        defaults.setInteger(value.toLong(), forKey = key)
    }
}

actual fun createAppPreferences(): AppPreferences {
    return AppPreferences(NSUserDefaults.standardUserDefaults)
}
