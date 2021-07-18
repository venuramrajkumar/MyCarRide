package com.raj.mycarride.ui.utils

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.raj.mycarride.R
import kotlin.math.roundToInt

object ViewUtils {

    fun pxToDp(px: Float): Float {
        val densityDpi = Resources.getSystem().displayMetrics.densityDpi
        return px / (densityDpi / 160f)
    }

    fun dpToPx(dp: Float): Int {
        val density = Resources.getSystem().displayMetrics.density
        return (dp * density).roundToInt()
    }

    fun enableTransparentStatusBar(window: Window,context : Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            val winParams = window.attributes
            winParams.flags =
                winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
            window.attributes = winParams
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        } else {
            window.setDecorFitsSystemWindows(false)
            window.setStatusBarColor(ContextCompat.getColor(context, R.color.trans_color))
        }
    }

}