package com.mk.bikey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mk.bikey.databinding.FragmentCreateRouteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateRouteFragment : Fragment() {

    private lateinit var binding: FragmentCreateRouteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentCreateRouteBinding.inflate(inflater, container, false).run {
        binding = this
        root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}