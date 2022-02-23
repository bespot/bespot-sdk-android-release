package com.bespot.sample

import android.Manifest.permission.*
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bespot.sdk.BuildConfig
import com.bespot.sdk.sample.R
import com.bespot.sdk.sample.databinding.ActivityMainBinding
import com.qifan.powerpermission.coroutines.awaitAskPermissionsAllGranted
import com.qifan.powerpermission.rationale.createDialogRationale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.S)
private val ANDROID_S_PERMISSIONS =
    arrayOf(BLUETOOTH_SCAN, BLUETOOTH_CONNECT, ACCESS_FINE_LOCATION, BLUETOOTH_ADMIN)
private val LEGACY_ANDROID_PERMISSIONS =
    arrayOf(BLUETOOTH, ACCESS_FINE_LOCATION, BLUETOOTH_ADMIN)
private val requiredPermissions =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) ANDROID_S_PERMISSIONS else LEGACY_ANDROID_PERMISSIONS

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup subscribe/unsubscribe button
        binding.startSession.setOnClickListener { checkPermission(Action.START_SESSION) }
        binding.pickStore.setOnClickListener { checkPermission(Action.PICK_STORE) }
        binding.version.text = "SDK version: " + BuildConfig.VERSION_NAME
    }

    private fun checkPermission(action: Action) {
        CoroutineScope(Dispatchers.Main).launch {
            val hasPermission = awaitAskPermissionsAllGranted(
                permissions = requiredPermissions,
                rationaleDelegate = createDialogRationale(
                    R.string.permission_prompt_title,
                    listOf(ACCESS_FINE_LOCATION, BLUETOOTH, BLUETOOTH_ADMIN),
                    getString(R.string.permission_prompt_rationale)
                )
            )

            if (hasPermission) {
                when (action) {
                    Action.PICK_STORE -> {
                        Timber.d("Pick store button pressed")
                        StoreSelectionActivity.start(this@MainActivity)
                    }
                    Action.SCAN -> {
                        Timber.d("Scan button pressed")
                        Toast.makeText(this@MainActivity, "Permissions denied!", Toast.LENGTH_SHORT)
                            .show()
                    }
                    Action.START_SESSION -> {
                        Timber.d("Start session button pressed")
                        SessionActivity.start(this@MainActivity, null)
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "Permissions denied!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    enum class Action {
        PICK_STORE, SCAN, START_SESSION
    }
}
