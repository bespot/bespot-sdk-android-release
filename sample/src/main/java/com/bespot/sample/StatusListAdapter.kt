package com.bespot.sample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bespot.sdk.sample.R
import com.bespot.sdk.sample.databinding.ListItemStatusBinding
import java.text.DateFormat
import java.util.*

class StatusListAdapter :
    ListAdapter<StatusWrapper, StatusListAdapter.StatusViewHolder>(StatusDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val binding =
            ListItemStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StatusViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class StatusViewHolder(private val binding: ListItemStatusBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            val DATE_FORMAT: DateFormat = DateFormat.getDateTimeInstance()
        }

        fun bind(item: StatusWrapper) {
            binding.status.text = item.status.name

            if (item.description.isEmpty()) {
                binding.description.visibility = View.GONE
            } else {
                binding.description.text = item.description
                binding.description.visibility = View.VISIBLE
            }

            binding.timestamp.text = DATE_FORMAT.format(Date(item.timestamp))

            when (item.status) {
                InOutStatus.INSIDE -> {
                    binding.indicator.setBackgroundResource(R.color.status_in)
                }
                InOutStatus.OUTSIDE -> {
                    binding.indicator.setBackgroundResource(R.color.status_out)
                }
                InOutStatus.ERROR -> {
                    binding.indicator.setBackgroundResource(R.color.status_error)
                }
                else -> {
                    binding.indicator.setBackgroundResource(R.color.status_unknown)
                }
            }
        }
    }

    class StatusDiffCallback : DiffUtil.ItemCallback<StatusWrapper>() {
        override fun areItemsTheSame(oldItem: StatusWrapper, newItem: StatusWrapper): Boolean {
            return oldItem.timestamp == newItem.timestamp
        }

        override fun areContentsTheSame(oldItem: StatusWrapper, newItem: StatusWrapper): Boolean {
            return oldItem.timestamp == newItem.timestamp && oldItem.status == newItem.status
        }
    }
}
