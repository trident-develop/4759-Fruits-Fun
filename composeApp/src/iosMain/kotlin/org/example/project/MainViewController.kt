package org.example.project

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

object ViewControllerHolder {
    var viewController: UIViewController? = null
}

fun MainViewController(): UIViewController {
    val vc = ComposeUIViewController { App() }
    ViewControllerHolder.viewController = vc
    return vc
}