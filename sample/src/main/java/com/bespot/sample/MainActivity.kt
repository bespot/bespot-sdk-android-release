package com.bespot.sample

import android.Manifest.permission.*
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bespot.sdk.sample.R
import com.bespot.sdk.sample.databinding.ActivityMainBinding
import com.qifan.powerpermission.coroutines.awaitAskPermissionsAllGranted
import com.qifan.powerpermission.rationale.createDialogRationale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup subscribe/unsubscribe button
        binding.startSession.setOnClickListener {
            // Get location permission
            CoroutineScope(Dispatchers.Main).launch {
                val hasPermission = awaitAskPermissionsAllGranted(
                    permissions = arrayOf(ACCESS_FINE_LOCATION, BLUETOOTH, BLUETOOTH_ADMIN),
                    rationaleDelegate = createDialogRationale(
                        R.string.permission_prompt_title,
                        listOf(ACCESS_FINE_LOCATION, BLUETOOTH, BLUETOOTH_ADMIN),
                        getString(R.string.permission_prompt_rationale)
                    )
                )

                if (hasPermission) {
                    //SessionActivity.start(this@MainActivity, STORE)
                    StoreSelectionActivity.start(this@MainActivity)
                } else {
                    Toast.makeText(this@MainActivity, "Permissions denied!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}