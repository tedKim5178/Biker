package com.mk.bikey.ui.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mk.bikey.databinding.ListItemRecordBinding
import com.mk.bikey.model.Record

class RecordAdapter : ListAdapter<Record, RecyclerView.ViewHolder>(RecordDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RecordViewHolder(
            ListItemRecordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RecordViewHolder).bind(getItem(position))
    }

    class RecordViewHolder(
        private val binding: ListItemRecordBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var item: Record

        init {
            binding.tvRecordName.setOnClickListener {
                with(it.findNavController()) {
                    navigate(
                        RecordFragmentDirections.actionRecordFragmentToRecordDetailFragment(
                            item
                        )
                    )
                }
            }
        }

        fun bind(item: Record) {
            this.item = item
            binding.apply {
                record = item
                executePendingBindings()
            }
        }
    }
}

private class RecordDiffCallback : DiffUtil.ItemCallback<Record>() {
    override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean {
        // TODO
        return true
    }

    override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean {
        // TODO
        return oldItem == newItem
    }

}