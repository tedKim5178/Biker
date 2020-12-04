package com.mk.bikey.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.mk.bikey.R
import com.mk.bikey.databinding.ViewBackWithTextBinding

class BackWithTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr){

    val binding: ViewBackWithTextBinding = ViewBackWithTextBinding.inflate(LayoutInflater.from(context), this, true)
}

@BindingAdapter("title")
fun BackWithTextView.setTitle(title: String?) {
    binding.tvTitle.text = title
}