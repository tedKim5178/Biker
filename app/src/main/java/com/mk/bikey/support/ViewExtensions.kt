package com.mk.bikey.support

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter


@BindingAdapter("visibleOrGone")
fun View.bindVisibility(value: Boolean?) {
    isVisible = value == true
}
