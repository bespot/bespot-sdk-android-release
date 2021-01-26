package com.bespot.sample

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bespot.sdk.Store
import com.bespot.sdk.sample.databinding.ListItemStoreBinding

fun interface OnStoreSelectedListener {
    fun onStoreSelected(store: Store)
}

class StoreListAdapter(private val listener: OnStoreSelectedListener) :
    ListAdapter<StoreWrapper, StoreListAdapter.StoreViewHolder>(StatusDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val binding =
            ListItemStoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoreViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class StoreViewHolder(
        private val binding: ListItemStoreBinding,
        private val listener: OnStoreSelectedListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var store: Store

        init {
            binding.root.setOnClickListener {
                listener.onStoreSelected(store)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: StoreWrapper) {
            this.store = item.store
            binding.name.text = item.store.name
            binding.address.text = item.store.address

            val distance = item.getFormattedDistance()
            if (distance != null) {
                binding.distance.text = distance
                binding.distance.visibility = View.VISIBLE
            } else {
                binding.distance.visibility = View.INVISIBLE
            }
        }
    }

    class StatusDiffCallback : DiffUtil.ItemCallback<StoreWrapper>() {
        override fun areItemsTheSame(oldItem: StoreWrapper, newItem: StoreWrapper): Boolean {
            return oldItem.store.uuid == newItem.store.uuid
        }

        override fun areContentsTheSame(oldItem: StoreWrapper, newItem: StoreWrapper): Boolean {
            return oldItem.store.uuid == newItem.store.uuid
        }
    }
}
