package com.mk.bikey.ui.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.mk.bikey.databinding.FragmentRecordDetailBinding

class RecordDetailFragment : Fragment() {

    private val args by navArgs<RecordDetailFragmentArgs>()
    private val record by lazy { args.record }
    private lateinit var binding: FragmentRecordDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentRecordDetailBinding.inflate(inflater, container, false).run {
        binding = this
        root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            record = this@RecordDetailFragment.record
            executePendingBindings()
        }
    }
}