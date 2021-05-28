package com.bespot.sample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bespot.sdk.Bespot
import com.bespot.sdk.SessionConfigKey
import com.bespot.sdk.StatusObserver
import com.bespot.sdk.StatusResult
import com.bespot.sdk.Store
import com.bespot.sdk.common.Failure
import timber.log.Timber

class SessionViewModel : ViewModel(), StatusObserver {

    private val isSubscribed = MutableLiveData<Boolean>().apply { value = false }

    var type = 0
        set(type) {
            when (type) {
                0 -> Bespot.changeToVerifiedStatus()
                1 -> Bespot.changeToExperimentalStatus()
                2 -> Bespot.changeToRawStatus()
            }
            field = type
        }

    fun isSubscribed(): LiveData<Boolean> = isSubscribed

    private val statusList = MutableLiveData<List<StatusWrapper>>().apply {
        value = arrayListOf()
    }

    fun statusList(): MutableLiveData<List<StatusWrapper>> = statusList

    private val lastStatus = MutableLiveData<StatusWrapper>()

    val lastStatusSDK = MutableLiveData<StatusWrapper>()

    fun lastStatus(): LiveData<StatusWrapper> = lastStatus

    private val buffValue = MutableLiveData<Int>()
    fun buffValue(): MutableLiveData<Int> = buffValue

    /* Just a quick solution in order to have an init value in buff signal.
     In next update and when we are sure that none wants it anymore,
     we can remove the entire implementation from both SDK and sample.
    */
    init {
        buffValue.postValue(0)
    }

    private fun sessionConfig(): Map<SessionConfigKey, String> =
        when (val value = buffValue.value) {
            null -> emptyMap()
            else -> mapOf(SessionConfigKey.BUFF_RSSI_BY to value.toString())
        }

    fun subscribe(store: Store? = null) {
        store?.let {
            Bespot.subscribe(this, store.location(), sessionConfig())
        } ?: Bespot.subscribe(this, null, sessionConfig())

        isSubscribed.postValue(true)
    }

    fun unsubscribe() {
        Bespot.unsubscribe()
        isSubscribed.postValue(false)
    }

    fun requestLastStatus() {
        Bespot.lastStatus(object : StatusObserver {
            override fun onStatusUpdate(status: StatusResult) {
                lastStatusSDK.postValue(StatusWrapper.success(status))
            }

            override fun onError(error: Failure) {
                lastStatusSDK.postValue(StatusWrapper.error(error))
            }
        })
    }

    override fun onStatusUpdate(result: StatusResult) {
        Timber.d("Status ${result.status}")
        handleStatus(StatusWrapper.success(result))
    }

    override fun onError(error: Failure) {
        handleStatus(StatusWrapper.error(error))
        Timber.d("Error $error")
    }

    private fun handleStatus(status: StatusWrapper) {
        lastStatus.postValue(status)
        if (type == 0 && status.status == InOutStatus.UNVERIFIED) {
            return
        }
        val items = statusList.value as ArrayList
        items.add(status)
        items.sortedBy { status.timestamp }
        statusList.postValue(items)
    }
}
