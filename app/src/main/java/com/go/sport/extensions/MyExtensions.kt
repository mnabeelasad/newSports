package com.go.sport.extensions

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import androidx.core.widget.ImageViewCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.go.sport.R
import de.hdodenhof.circleimageview.CircleImageView


fun ImageView.setGlide(source: String, @IntRange(from = 1, to = 100) radius: Int? = 31) {
    val circularLoader = CircularProgressDrawable(context)
    circularLoader.strokeWidth = 5f
    circularLoader.centerRadius = 15f
    circularLoader.start()
    if(source != "") {
        if (radius == 1)
            Glide.with(context).load(source).placeholder(circularLoader)
                .into(this)
        else
            Glide.with(context).load(source).placeholder(circularLoader)
                .apply(RequestOptions().transforms(CenterCrop(), RoundedCorners(radius ?: 0)))
                .into(this)
    }else {
        if (radius == 1)
            Glide.with(context).load(context.getDrawable(R.drawable.avatar)).placeholder(circularLoader)
                .into(this)
        else
            Glide.with(context).load(R.drawable.avatar).placeholder(circularLoader)
                .apply(RequestOptions().transforms(CenterCrop(), RoundedCorners(radius ?: 0)))
                .into(this)
    }

}



fun ImageView.setTint(@ColorInt color: Int?) {
    if (color == null) {
        ImageViewCompat.setImageTintList(this, null)
        return
    }
    ImageViewCompat.setImageTintMode(this, PorterDuff.Mode.SRC_ATOP)
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(color))
}

