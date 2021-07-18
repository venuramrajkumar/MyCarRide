package com.raj.mycarride.ui.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.raj.mycarride.R


object MapUtils {

    fun getCarBitmap(context: Context): Bitmap {
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_car)
        return Bitmap.createScaledBitmap(bitmap, 50, 100, true)
    }
}