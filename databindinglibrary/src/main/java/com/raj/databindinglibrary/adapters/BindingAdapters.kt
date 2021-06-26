package com.raj.databindinglibrary.adapters

import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.databinding.BindingAdapter
import com.raj.databindinglibrary.utils.Popularity

@BindingAdapter("app:setPopularIcon")
 fun setPopularIcon(imageView: ImageView, popularity : Popularity) {

        when(popularity) {

            Popularity.NORMAL -> {
                ImageViewCompat.setImageTintList(
                    imageView,
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            imageView.context,
                            android.R.color.holo_green_dark
                        )
                    )
                )
                imageView.setImageDrawable(ContextCompat.getDrawable(imageView.context,android.R.drawable.stat_sys_warning))
            }

            Popularity.STAR -> {
                ImageViewCompat.setImageTintList(
                    imageView,
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            imageView.context,
                            android.R.color.holo_red_light
                        )
                    )
                )
                imageView.setImageDrawable(ContextCompat.getDrawable(imageView.context,android.R.drawable.arrow_down_float))
            }

        }
}