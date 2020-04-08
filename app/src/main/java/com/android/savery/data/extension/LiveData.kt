package com.android.savery.data.extension

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.sdi.joyersmajorplatform.common.livedataext.mutableLiveData

fun <TSOURCE, TOUT> mediatorLiveData(
    source: LiveData<TSOURCE>,
    initial: TOUT? = null,
    onChanged: MediatorLiveData<TOUT>.(TSOURCE?) -> Unit
): MediatorLiveData<TOUT> {
    val liveData = MediatorLiveData<TOUT>()
    initial?.let { liveData.postValue(it) }
    liveData.addSource(source) { onChanged(liveData, it) }
    return liveData
}

fun <T> liveData(value: T?): LiveData<T> {
    return mutableLiveData(value)
}