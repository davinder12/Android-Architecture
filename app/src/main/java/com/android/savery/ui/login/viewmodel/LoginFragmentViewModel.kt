package com.android.savery.ui.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.savery.data.api.models.LoginResponse
import com.android.savery.data.repository.UserRepository
import com.android.savery.data.network.NetworkState
import com.android.savery.utils.BaseViewModel
import com.sdi.joyersmajorplatform.common.livedataext.postUpdate
import javax.inject.Inject

class LoginFragmentViewModel @Inject constructor(private val userRepository: UserRepository) :
    BaseViewModel(){

   var loginResponse = MutableLiveData<LoginResponse>()


//    fun LoginApiHit(): LiveData<NetworkState> {
//        val request = userRepository.signInApi("dev.test@gmail.com", "123456").also { it ->
//            subscribe(it.request) {
//                it.body()?.let {
//                    loginResponse.postUpdate(it)
//                }
//            }
//        }
//        return request.networkState
//    }


}