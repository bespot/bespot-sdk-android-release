package com.bespot.sample

import android.location.Location
import com.bespot.sdk.Beacon
import com.bespot.sdk.Event
import com.bespot.sdk.Status
import com.bespot.sdk.StatusResult
import com.bespot.sdk.Store
import com.bespot.sdk.common.Failure
import com.bespot.sdk.common.StatusFailure
import java.util.Date

enum class InOutStatus {
    INSIDE, OUTSIDE, UNVERIFIED, ERROR
}

data class StatusWrapper(
    val status: InOutStatus,
    val description: String,
    val timestamp: Long
) {
    companion object {
        fun success(result: StatusResult): StatusWrapper = StatusWrapper(
            status = when (result.status) {
                Status.IN -> InOutStatus.INSIDE
                Status.OUT -> InOutStatus.OUTSIDE
                Status.AWAY -> InOutStatus.OUTSIDE
                else -> InOutStatus.UNVERIFIED
            },
            description = "Resolved EIDs: ${result.eids.size}",
            timestamp = result.timestamp
        )

        fun error(error: Failure): StatusWrapper = StatusWrapper(
            status = InOutStatus.ERROR,
            description = error.toText(),
            timestamp = Date().time
        )
    }
}

interface AdapterItem

data class EventWrapper(
    val timestamp: Long = Date().time,
    var beacons: List<BeaconWrapper>
) : AdapterItem {

    companion object {
        fun fromEvent(event: Event) =
            EventWrapper(
                timestamp = event.timestamp,
                beacons = event.beacons.map { BeaconWrapper.fromBeacon(it) }
            )
    }
}

data class BeaconWrapper(
    var name: String,
    var uuid: String,
    var major: String,
    var minor: String,
    var rssi: Int,
    var distance: Double
) : AdapterItem {
    companion object {
        fun fromBeacon(beacon: Beacon) =
            BeaconWrapper(
                name = beacon.kontaktID ?: beacon.type.toString(),
                uuid = beacon.uuid ?: beacon.eid ?: beacon.namespace + beacon.instanceId ?: "N/A",
                major = beacon.major?.toString() ?: "N/A",
                minor = beacon.minor?.toString() ?: "N/A",
                rssi = beacon.rssi,
                distance = beacon.distance
            )
    }
}

data class Header(val timestamp: String, val count: String) : AdapterItem

data class StoreWrapper(
    val store: Store,
    val distance: Float = Float.NaN
) {
    companion object {
        fun withLastLocation(store: Store, lastLocation: Location?): StoreWrapper {
            return StoreWrapper(
                store,
                lastLocation?.let { location -> store.distanceTo(location) } ?: Float.NaN
            )
        }
    }

    fun getFormattedDistance(): String? {
        if (distance.isNaN()) {
            return null
        }
        return when {
            distance > 10000 -> {
                val km = distance / 1000
                "%.0f km".format(km)
            }
            distance > 1000 -> {
                val km = distance / 1000f
                "%.1f km".format(km)
            }
            else -> {
                "%.0f m".format(distance)
            }
        }
    }
}

fun Failure.toText(): String {
    return when (this) {
        is StatusFailure.NoConfigurationFound -> "No Configuration Found"
        is StatusFailure.NoStoreReadings -> "No Store Readings"
        is StatusFailure.CloseDistance -> "Close Distance"
        is StatusFailure.NoStatusCached -> "No cached status"
        is StatusFailure.IndoorDataModelNotFound -> "Indoor data model Not found"
        is Failure.BluetoothPermissionDenied -> "Bluetooth Permission Denied"
        is Failure.BluetoothDisabled -> "Bluetooth Disabled"
        is Failure.LocationPermissionDenied -> "Location Permission Denied"
        is Failure.NetworkConnection -> "Connection error"
        is Failure.NotInitialized -> "SDK isn't initialized"
        is Failure.ServerError -> "Remote server error"
        else -> "Unmapped error: $this"
    }
}
