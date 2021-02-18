package com.bespot.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bespot.sdk.Store
import com.bespot.sdk.sample.R
import com.bespot.sdk.sample.databinding.ActivitySessionBinding

class SessionActivity : AppCompatActivity() {

    companion object {
        private const val ARG_STORE = "store"

        fun start(context: Context, store: Store) {
            val intent = Intent(context, SessionActivity::class.java)
                .putExtra(ARG_STORE, store)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            context.startActivity(intent)
        }
    }

    private lateinit var model: SessionViewModel
    private lateinit var store: Store
    private lateinit var binding: ActivitySessionBinding
    private lateinit var adapter: StatusListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProvider(this).get(SessionViewModel::class.java)
        binding = ActivitySessionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup toolbar
        store = requireNotNull(intent.getParcelableExtra(ARG_STORE))
        if (store != Store.empty()) {
            binding.toolbar.title = store.name
            binding.toolbar.subtitle = store.uuid
        } else {
            binding.toolbar.title = getString(R.string.no_store_mode)
        }

        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        // Setup latest status
        model.lastStatus().observe(
            this,
            {
                binding.lastStatus.text = it.status.name
                when (it.status) {
                    InOutStatus.INSIDE -> {
                        binding.lastStatus.setChipBackgroundColorResource(R.color.status_in)
                        binding.lastStatus.setChipIconResource(R.drawable.ic_status_in)
                    }
                    InOutStatus.OUTSIDE -> {
                        binding.lastStatus.setChipBackgroundColorResource(R.color.status_out)
                        binding.lastStatus.setChipIconResource(R.drawable.ic_status_out)
                    }
                    InOutStatus.ERROR -> {
                        binding.lastStatus.setChipBackgroundColorResource(R.color.status_error)
                        binding.lastStatus.setChipIconResource(R.drawable.ic_status_error)
                    }
                    else -> {
                        binding.lastStatus.setChipBackgroundColorResource(R.color.status_unknown)
                        binding.lastStatus.setChipIconResource(R.drawable.ic_status_unknown)
                    }
                }
            }
        )

        // Setup status list
        adapter = StatusListAdapter()
        binding.list.adapter = adapter
        model.statusList().observe(
            this,
            {
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
                binding.list.postDelayed({ binding.list.scrollToPosition(adapter.itemCount - 1) }, 10)
            }
        )

        // Setup subscribe/unsubscribe button
        binding.subscribe.setOnClickListener { model.subscribe(store) }
        binding.unsubscribe.setOnClickListener { model.unsubscribe() }
        model.isSubscribed().observe(
            this,
            {
                binding.subscribe.visibility = if (it) View.GONE else View.VISIBLE
                binding.unsubscribe.visibility = if (it) View.VISIBLE else View.GONE
            }
        )
    }

    override fun onDestroy() {
        model.unsubscribe()
        super.onDestroy()
    }
}
