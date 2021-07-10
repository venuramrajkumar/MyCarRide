package com.raj.mycarride.ui.databinding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class GlideBindingAdapter {

    companion object {
        @BindingAdapter ("ImageUrl")
        @JvmStatic
        fun setImageUrl(view: ImageView, imageUrl: String) {

            val requestOptions : RequestOptions = RequestOptions()
                .placeholder(android.R.drawable.editbox_background)
                .error(android.R.drawable.editbox_background)
            Glide.with(view).applyDefaultRequestOptions(requestOptions).load(imageUrl).apply(RequestOptions().override(64,64))
                .into(view)

        }

    }
}