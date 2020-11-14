package com.mk.bikey.ui.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.mk.bikey.databinding.FragmentRecordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordFragment : Fragment() {
    private lateinit var binding: FragmentRecordBinding
    private val adapter = RecordAdapter()
    private val recordViewModel: RecordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentRecordBinding.inflate(inflater, container, false).run {
        binding = this
        root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        observeUi()
    }

    private fun setAdapter() {
        binding.list.adapter = adapter
    }

    private fun observeUi() {
        recordViewModel.records.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

}