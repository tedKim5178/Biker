package com.mk.bikey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mk.bikey.databinding.FragmentSearchRouteBinding

class SearchRouteFragment : Fragment() {

    private lateinit var binding: FragmentSearchRouteBinding
    private val adapter = RouteAdapter()

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
        binding.list.adapter = adapter
        getItems()
    }

    private fun getItems() {
        val database = Firebase.database
        val reference = database.reference
        val listener = object :  ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val routeList = snapshot.children.map { it.getValue(Route::class.java) }
                adapter.submitList(routeList)
//                val route = routeList[0]
//                findNavController().previousBackStackEntry?.savedStateHandle?.set("test", route)
            }
        }
        reference.child("route").addListenerForSingleValueEvent(listener)
    }
}