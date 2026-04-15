package org.example.project

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

val isIOS: Boolean get() = getPlatform().name.startsWith("iOS")