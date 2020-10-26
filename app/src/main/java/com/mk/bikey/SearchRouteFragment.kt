package com.mk.bikey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.mk.bikey.databinding.FragmentSearchRouteBinding

class SearchRouteFragment : Fragment() {

    private lateinit var binding: FragmentSearchRouteBinding
    private val adapter = RouteAdapter()
    private val searchRouteViewModel: SearchRouteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentSearchRouteBinding.inflate(inflater, container, false).run {
        binding = this
        root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        observeUi()
        fetch()
    }

    private fun setAdapter() {
        binding.list.adapter = adapter
    }

    private fun observeUi() {
        searchRouteViewModel.routeList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    private fun fetch() {
        searchRouteViewModel.fetch()
    }
}