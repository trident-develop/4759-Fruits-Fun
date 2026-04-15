package org.example.project

expect class AppPreferences {
    fun isOnboardingCompleted(): Boolean
    fun setOnboardingCompleted()

    fun getString(key: String, default: String): String
    fun putString(key: String, value: String)
    fun getInt(key: String, default: Int): Int
    fun putInt(key: String, value: Int)
}

expect fun createAppPreferences(): AppPreferences
