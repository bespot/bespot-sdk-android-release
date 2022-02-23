package com.bespot.sample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bespot.sdk.Bespot
import com.bespot.sdk.StatusObserver
import com.bespot.sdk.StatusResult
import com.bespot.sdk.Store
import com.bespot.sdk.common.Failure
import timber.log.Timber

class SessionViewModel : ViewModel(), StatusObserver {

    private val isSubscribed = MutableLiveData<Boolean>().apply { value = false }

    fun isSubscribed(): LiveData<Boolean> = isSubscribed

    private val statusList = MutableLiveData<List<StatusWrapper>>().apply {
        value = arrayListOf()
    }

    fun statusList(): MutableLiveData<List<StatusWrapper>> = statusList

    private val lastStatus = MutableLiveData<StatusWrapper>()

    val lastStatusSDK = MutableLiveData<StatusWrapper>()

    fun lastStatus(): LiveData<StatusWrapper> = lastStatus

    fun subscribe(store: Store? = null) {
        store?.let {
            Bespot.subscribe(this, store.location(), null)
        } ?: Bespot.subscribe(this, null, null)

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
        val items = statusList.value as ArrayList
        items.add(status)
        items.sortedBy { status.timestamp }
        statusList.postValue(items)
    }
}
