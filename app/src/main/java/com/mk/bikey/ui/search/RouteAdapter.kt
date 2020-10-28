package com.mk.bikey.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mk.bikey.databinding.ListItemRouteBinding
import com.mk.bikey.model.Route

class RouteAdapter : ListAdapter<Route, RecyclerView.ViewHolder>(RouteDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RouteViewHolder(
            ListItemRouteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RouteViewHolder).bind(getItem(position))
    }

    class RouteViewHolder(
        private val binding: ListItemRouteBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var item: Route
        init {
            binding.tvRouteName.setOnClickListener {
                with(it.findNavController()) {
                    previousBackStackEntry?.savedStateHandle?.set("route", item)
                    navigateUp()
                }
            }
        }

        fun bind(item: Route) {
            this.item = item
            binding.apply {
                route = item
                executePendingBindings()
            }
        }
    }
}

private class RouteDiffCallback : DiffUtil.ItemCallback<Route>() {
    override fun areItemsTheSame(oldItem: Route, newItem: Route): Boolean {
        // TODO
        return true
    }

    override fun areContentsTheSame(oldItem: Route, newItem: Route): Boolean {
        // TODO
        return true
    }

}