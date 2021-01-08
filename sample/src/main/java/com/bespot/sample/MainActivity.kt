package com.bespot.sample

import android.Manifest.permission.*
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bespot.sample.databinding.ActivityMainBinding
import com.bespot.sdk.Bespot
import com.bespot.sdk.StatusError
import com.bespot.sdk.StatusObserver
import com.bespot.sdk.StatusResult
import com.qifan.powerpermission.coroutines.awaitAskPermissionsAllGranted
import com.qifan.powerpermission.rationale.createDialogRationale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), StatusObserver {

    companion object {
        const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.permission.setOnClickListener {
            // Get location permission
            CoroutineScope(Dispatchers.Main).launch {
                val hasPermission = awaitAskPermissionsAllGranted(
                    permissions = *arrayOf(ACCESS_FINE_LOCATION, BLUETOOTH, BLUETOOTH_ADMIN),
                    rationaleDelegate = createDialogRationale(
                        R.string.permission_prompt_title,
                        listOf(ACCESS_FINE_LOCATION, BLUETOOTH, BLUETOOTH_ADMIN),
                        getString(R.string.permission_prompt_rationale)
                    )
                )

                binding.toggle.isEnabled = hasPermission
            }
        }

        binding.toggle.addOnButtonCheckedListener { _, checkedId, _ ->
            if (checkedId == R.id.subscribe) {
                val location = Location("")
                location.latitude = 37.99
                location.longitude = 23.70
                Bespot.subscribe(location, this)
            } else {
                Bespot.unsubscribe()
            }
        }

        Log.d(TAG, "Main Activity was created")
    }

    override fun onStatusUpdate(status: StatusResult) {
        Toast.makeText(this, status.status.name, Toast.LENGTH_SHORT).show()
    }

    override fun onStatusError(error: StatusError) {
        Log.e(TAG, "Status error: ${error.errorCode}")
    }
}