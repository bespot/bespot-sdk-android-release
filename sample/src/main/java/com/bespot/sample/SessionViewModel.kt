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
import java.util.*

class SessionViewModel : ViewModel(), StatusObserver {

    private val isSubscribed = MutableLiveData<Boolean>().apply { value = false }

    fun isSubscribed(): LiveData<Boolean> = isSubscribed

    private val statusList = MutableLiveData<List<StatusWrapper>>().apply {
        value = arrayListOf()
    }

    fun statusList(): MutableLiveData<List<StatusWrapper>> = statusList

    private val lastStatus = MutableLiveData<StatusWrapper>().apply {
        value = StatusWrapper(InOutStatus.UNKNOWN, "", Date().time)
    }

    fun lastStatus(): LiveData<StatusWrapper> = lastStatus

    fun subscribe(store: Store) {
        Bespot.subscribe(store.location(), this)
        isSubscribed.postValue(true)
    }

    fun unsubscribe() {
        Bespot.unsubscribe()
        isSubscribed.postValue(false)
    }

    override fun onStatusUpdate(result: StatusResult) {
        Timber.d("Status ${result.status}")
        handleStatus(StatusWrapper.success(result))
    }

    override fun onError(error: Failure) {
        Timber.d("Error $error")
        handleStatus(StatusWrapper.error(error))
    }

    private fun handleStatus(status: StatusWrapper) {
        lastStatus.postValue(status)
        val items = statusList.value as ArrayList
        items.add(status)
        items.sortedBy { status.timestamp }
        statusList.postValue(items)
    }
}