package com.bespot.sample

import android.location.Location
import com.bespot.sdk.Status
import com.bespot.sdk.StatusResult
import com.bespot.sdk.Store
import com.bespot.sdk.common.Failure
import java.util.*

enum class InOutStatus {
    INSIDE, OUTSIDE, UNKNOWN, ERROR
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
                else -> InOutStatus.UNKNOWN
            },
            description = "", // TODO Add description
            timestamp = result.timestamp
        )

        fun error(error: Failure): StatusWrapper = StatusWrapper(
            status = InOutStatus.ERROR,
            description = error.toString(),
            timestamp = Date().time
        )
    }
}

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
