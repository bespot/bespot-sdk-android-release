package com.bespot.sample

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bespot.sdk.Bespot
import com.bespot.sdk.Store
import com.bespot.sdk.StoresCallback
import com.bespot.sdk.common.Failure
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class StoreSelectionViewModel : ViewModel() {

    private val stores = MutableLiveData<List<StoreWrapper>>().apply {
        value = arrayListOf()
    }

    fun stores(): LiveData<List<StoreWrapper>> = stores

    private val error = MutableLiveData<Failure>().apply {
        value = null
    }

    fun error(): LiveData<Failure> = error

    fun fetch(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val lastLocation = try {
                getLastLocation(context)
            } catch (e: Exception) {
                Timber.d(e, "Could not get last location")
                null
            }

            try {
                val stores = getStores()
                Timber.d("Got  ${stores.size} stores")

                // Notify for data
                this@StoreSelectionViewModel.stores.postValue(
                    stores.filter { it.lat != null && it.lon != null }
                        .map { store -> StoreWrapper.withLastLocation(store, lastLocation) }
                        .sortedWith(compareBy({ it.distance }, { it.store.name }))
                )
            } catch (e: Exception) {
                // Notify for error
                this@StoreSelectionViewModel.error.postValue(Failure.UnknownError("Could not get stores"))
                Timber.d(e, "Could not get stores")
            }
        }
    }

    private suspend fun getLastLocation(context: Context): Location =
        suspendCancellableCoroutine { continuation ->
            fun hasPermission(permission: String): Boolean {
                return ActivityCompat.checkSelfPermission(context, permission) == PERMISSION_GRANTED
            }
            if (!hasPermission(ACCESS_FINE_LOCATION) && !hasPermission(ACCESS_COARSE_LOCATION)) {
                return@suspendCancellableCoroutine continuation.resumeWithException(
                    SecurityException("No location permission")
                )
            }
            LocationServices.getFusedLocationProviderClient(context).lastLocation
                .addOnSuccessListener {
                    it?.let { continuation.resume(it) } ?: continuation.resumeWithException(
                        NullPointerException("No last location data")
                    )
                }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }

    private suspend fun getStores(): List<Store> = suspendCancellableCoroutine { continuation ->
        Bespot.getStores(object : StoresCallback {
            override fun onStoresReceived(stores: List<Store>) {
                continuation.resume(stores)
            }

            override fun onError(error: Failure) {
                continuation.resumeWithException(java.lang.Exception(error.toString()))
            }
        })
    }
}
