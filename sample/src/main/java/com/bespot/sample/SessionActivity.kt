package com.bespot.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bespot.sdk.Store
import com.bespot.sdk.sample.R
import com.bespot.sdk.sample.databinding.ActivitySessionBinding
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

class SessionActivity : AppCompatActivity() {

    companion object {
        private const val ARG_STORE = "store"

        fun start(context: Context, store: Store? = null) {
            val intent = Intent(context, SessionActivity::class.java)
                .putExtra(ARG_STORE, store)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            context.startActivity(intent)
        }
    }

    private lateinit var model: SessionViewModel
    private var store: Store? = null
    private lateinit var binding: ActivitySessionBinding
    private lateinit var adapter: StatusListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProvider(this).get(SessionViewModel::class.java)
        binding = ActivitySessionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        store = intent.getParcelableExtra(ARG_STORE)
        setTitle()

        setSupportActionBar(binding.toolbar)
        // Setup latest status
        model.lastStatusSDK.observe(this, ::showSDKLastStatus)

        model.lastStatus().observe(this) {
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
                    model.unsubscribe()
                }
                else -> {
                    // binding.lastStatus.setChipBackgroundColorResource(R.color.status_unknown)
                    binding.lastStatus.setChipIconResource(R.drawable.ic_status_unknown)
                }
            }
        }
        // Setup status list
        adapter = StatusListAdapter()
        binding.list.adapter = adapter
        model.statusList().observe(this) {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
            binding.list.postDelayed(
                { binding.list.scrollToPosition(adapter.itemCount - 1) },
                10
            )
        }

        // Setup subscribe/unsubscribe button
        binding.subscribe.setOnClickListener { model.subscribe(store) }
        binding.unsubscribe.setOnClickListener {
            binding.lastStatus.setText(R.string.status_waiting)
            binding.lastStatus.setChipBackgroundColorResource(R.color.status_unknown)
            binding.lastStatus.setChipIconResource(R.drawable.ic_status_unknown)
            model.unsubscribe()
        }
        model.isSubscribed().observe(this) {
            binding.subscribe.visibility = if (it) View.GONE else View.VISIBLE
            binding.unsubscribe.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_session, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_last_status -> {
                requestLastStatus()
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        model.unsubscribe()
        super.onDestroy()
    }

    private fun setTitle() {
        store?.let {
            binding.toolbar.title = it.name
            binding.toolbar.subtitle = it.uuid
        } ?: run {
            binding.toolbar.title = getString(R.string.no_store_mode)
        }
    }

    private fun requestLastStatus() {
        model.requestLastStatus()
    }

    private fun showSDKLastStatus(status: StatusWrapper) {
        Timber.i(status.toString())
        Snackbar.make(
            binding.root,
            status.toString(),
            Snackbar.LENGTH_LONG
        )
            .setAnchorView(binding.buttonContainer)
            .show()
    }
}
