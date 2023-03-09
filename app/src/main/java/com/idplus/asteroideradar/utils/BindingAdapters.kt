package com.idplus.asteroideradar.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.idplus.asteroideradar.R
import com.idplus.asteroideradar.data.remote.model.PictureOfDay
import com.squareup.picasso.Picasso


@BindingAdapter("pictureOfDayImage")
fun bindPictureOfTheDay(imageView: ImageView, pictureOfDay: PictureOfDay?) {

    val context = imageView.context
    if(pictureOfDay != null && pictureOfDay.url.isNotBlank()) {

        // if the picture is valid, we use Picasso library to display the image from the URL
        Picasso.with(context)
            .load(pictureOfDay.url)
//            .placeholder(R.drawable)
            .error(R.drawable.no_image_available)
            .fit()
            .centerCrop()
            .into(imageView)
    }
    else {
        // if the picture is not valid, we display a blank picture
        imageView.setImageResource(R.drawable.no_image_available)
        imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
    }
}

@BindingAdapter("statusIconHazardous")
fun bindStatusHazardous(imageView: ImageView, isHazardous: Boolean) {

    if(isHazardous)
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    else
        imageView.setImageResource(R.drawable.ic_status_normal)

    imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
}

@BindingAdapter("pictureHazardousImage")
fun bindPictureHazardous(imageView: ImageView, isHazardous: Boolean) {

    if(isHazardous)
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    else
        imageView.setImageResource(R.drawable.asteroid_safe)

    imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
}