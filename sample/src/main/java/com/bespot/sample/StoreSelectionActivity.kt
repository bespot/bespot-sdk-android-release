package com.bespot.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenCreated
import com.bespot.sdk.sample.databinding.ActivityStoreSelectionBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class StoreSelectionActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, StoreSelectionActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            context.startActivity(intent)
        }
    }

    init {
        lifecycleScope.launch {
            whenCreated {
                model.fetch(this@StoreSelectionActivity)
            }
        }
    }

    private lateinit var model: StoreSelectionViewModel
    private lateinit var binding: ActivityStoreSelectionBinding
    private lateinit var adapter: StoreListAdapter
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProvider(this).get(StoreSelectionViewModel::class.java)
        binding = ActivityStoreSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup toolbar
        binding.toolbar.setNavigationOnClickListener {
            Toast.makeText(this, "Cancelled!", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }

        // Listen for errors
        model.error().observe(this, { error ->
            snackbar?.dismiss()
            error?.let {
                snackbar = Snackbar.make(binding.root, "Error! $error", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry") { model.fetch(this@StoreSelectionActivity) }
                snackbar?.show()
            }
        })

        // Setup status list
        adapter = StoreListAdapter {
            SessionActivity.start(this, it)
            finish()
        }
        binding.list.adapter = adapter
        
        model.stores().observe(this, {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
            binding.list.scrollToPosition(adapter.itemCount - 1)
        })
    }
}