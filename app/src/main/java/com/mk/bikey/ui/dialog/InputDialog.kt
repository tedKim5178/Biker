package com.mk.bikey.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.mk.bikey.support.FragmentArgumentDelegate
import com.mk.bikey.R
import com.mk.bikey.databinding.FragmentInputDialogBinding

class InputDialog : DialogFragment() {

    private lateinit var binding: FragmentInputDialogBinding
    private var onConfirm: ((String) -> Unit)? = null
    private var title by FragmentArgumentDelegate<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentInputDialogBinding.inflate(inflater, container, false).run {
        binding = this
        root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            tvTitle.text = title
            btnConfirm.setOnClickListener {
                val inputText = etInput.text.toString()
                if (inputText.isEmpty()) {
                    Toast.makeText(context,
                        R.string.input_nothing_toast, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                onConfirm?.invoke(inputText)
                dismiss()
            }
        }
    }

    companion object {
        fun show(
            fragmentManager: FragmentManager,
            titleText: String,
            onConfirm: ((String) -> Unit)? = null
        ) {
            InputDialog().apply {
                title = titleText
                this.onConfirm = onConfirm
            }.showAllowingStateLoss(fragmentManager, "InputDialog")
        }
    }
}

fun DialogFragment.showAllowingStateLoss(fragmentManager: FragmentManager, tag: String) {
    if (!fragmentManager.isDestroyed && !isStateSaved) {
        show(fragmentManager, tag)
    }
}