package com.bespot.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import com.bespot.sdk.Store
import com.bespot.sdk.sample.R
import com.bespot.sdk.sample.databinding.ActivitySessionBinding
import com.bespot.sdk.sample.databinding.DialogBuffReadingsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

private const val MAX_BUFF_PICKER_VALUE = 80
private const val MIN_BUFF_PICKER_VALUE = 0
private const val DEFAULT_BUFF_VALUE = 0
private const val OFFSET_BUFF_VALUE = -40

class SessionActivity : AppCompatActivity() {

    companion object {
        private const val ARG_STORE = "store"
        private const val CONFIG_KEY = "prefs_debug_mode"
        private const val STATUS_CONFIG_KEY = "prefs_status_type"
        private const val BUFF_SIGNAL_FEATURE_AVAILABLE = false

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
    private var labelPressedCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProvider(this).get(SessionViewModel::class.java)
        binding = ActivitySessionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        restoreConfig()
        store = intent.getParcelableExtra(ARG_STORE)
        setTitle()

        // binding.toolbar.setNavigationOnClickListener { onBackPressed() }
        binding.lastStatus.setOnClickListener { onLabelPressed() }
        binding.buffAmount.setOnClickListener { buffReadingsPressed() }

        setSupportActionBar(binding.toolbar)
        // Setup latest status
        model.lastStatusSDK.observe(this, ::showSDKLastStatus)

        model.lastStatus().observe(
            this,
            {
                binding.lastStatus.text =
                    if (it.status == InOutStatus.UNVERIFIED) getString(R.string.status_calculating) else it.status.name
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
        )
        model.buffValue().observe(this) { buffValue ->
            binding.buffAmount.apply {
                text = getString(R.string.buffed_reading_amount, buffValue ?: 0)
            }
        }
        // Setup status list
        adapter = StatusListAdapter()
        binding.list.adapter = adapter
        model.statusList().observe(
            this,
            {
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
                binding.list.postDelayed(
                    { binding.list.scrollToPosition(adapter.itemCount - 1) },
                    10
                )
            }
        )

        // Setup subscribe/unsubscribe button
        binding.subscribe.setOnClickListener { model.subscribe(store) }
        binding.unsubscribe.setOnClickListener {
            binding.lastStatus.setText(R.string.status_waiting)
            binding.lastStatus.setChipBackgroundColorResource(R.color.status_unknown)
            binding.lastStatus.setChipIconResource(R.drawable.ic_status_unknown)
            model.unsubscribe()
        }
        model.isSubscribed().observe(
            this,
            {
                binding.subscribe.visibility = if (it) View.GONE else View.VISIBLE
                binding.unsubscribe.visibility = if (it) View.VISIBLE else View.GONE
            }
        )
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
            } else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        model.unsubscribe()
        super.onDestroy()
    }

    private fun buffReadingsPressed() {
        when {
            !BUFF_SIGNAL_FEATURE_AVAILABLE -> {
                Snackbar.make(
                    binding.root,
                    getString(R.string.error_buff_feature),
                    Snackbar.LENGTH_LONG
                )
                    .setAnchorView(binding.buttonContainer)
                    .show()
            }
            model.isSubscribed().value == true -> {
                Snackbar.make(
                    binding.root,
                    getString(R.string.error_cannot_update_buff_value),
                    Snackbar.LENGTH_LONG
                )
                    .setAnchorView(binding.buttonContainer)
                    .show()
            }
            else -> {
                val pickerViewBinding = DialogBuffReadingsBinding.inflate(layoutInflater)
                with(pickerViewBinding.buffedValuePicker) {
                    maxValue = MAX_BUFF_PICKER_VALUE
                    minValue = MIN_BUFF_PICKER_VALUE
                    wrapSelectorWheel = false
                    value = model.buffValue().value.toPickerValue()
                    setFormatter { unformattedValue -> "${unformattedValue.fromPickerValue()}" }
                }

                AlertDialog.Builder(this)
                    .setView(pickerViewBinding.root)
                    .setNegativeButton(getString(R.string.action_cancel)) { _, _ -> }
                    .setPositiveButton(getString(R.string.action_ok)) { _, _ ->
                        model.buffValue()
                            .postValue(pickerViewBinding.buffedValuePicker.value.fromPickerValue())
                    }
                    .setNeutralButton(getString(R.string.action_clear)) { _, _ ->
                        model.buffValue().postValue(null)
                    }
                    .show()
            }
        }
    }

    private fun onLabelPressed() {
        labelPressedCounter++
        if (labelPressedCounter > 4) {
            onAlertDialog()
            labelPressedCounter = 0
        }
    }

    private fun onAlertDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Change IN|OUT Mode")
            .setCancelable(true)
            .setItems(arrayOf("Verified", "Experimental", "Raw")) { _, which ->
                storeConfig(which)
                setTitle()
                // setStatusType(which)
                Toast.makeText(this, "Mode changed", Toast.LENGTH_SHORT).show()
            }.show()
    }

    private fun setTitle() {
        store?.let {
            when (model.type) {
                0 -> binding.toolbar.title = it.name
                1 -> binding.toolbar.title = it.name + " (Exp Mode)"
                2 -> binding.toolbar.title = it.name + " (Raw Mode)"
            }
            binding.toolbar.subtitle = it.uuid
        } ?: run {
            when (model.type) {
                0 -> binding.toolbar.title = getString(R.string.no_store_mode)
                1 -> binding.toolbar.title = getString(R.string.no_store_mode_exp)
                2 -> binding.toolbar.title = getString(R.string.no_store_mode_raw)
            }
        }
    }

    private fun restoreConfig() {
        model.type = getPreferences(Context.MODE_PRIVATE).getInt(STATUS_CONFIG_KEY, 0)
    }

    private fun storeConfig(type: Int) {
        model.type = type
        getPreferences(Context.MODE_PRIVATE).edit {
            putInt(STATUS_CONFIG_KEY, type)
                .apply()
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

private fun Int?.toPickerValue() = if (this == null) {
    DEFAULT_BUFF_VALUE - OFFSET_BUFF_VALUE
} else {
    this - OFFSET_BUFF_VALUE
}

private fun Int.fromPickerValue() = this + OFFSET_BUFF_VALUE
