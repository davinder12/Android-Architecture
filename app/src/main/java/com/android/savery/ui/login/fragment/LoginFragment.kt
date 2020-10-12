package com.android.savery.ui.login.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer

import com.android.savery.R
import com.android.savery.databinding.FragmentLoginBinding
import com.android.savery.data.network.NetworkState
import com.android.savery.ui.baseclass.BaseDaggerFragment
import com.android.savery.ui.login.viewmodel.LoginFragmentViewModel


class LoginFragment : BaseDaggerFragment<FragmentLoginBinding>() {
    override val layoutRes: Int
        get() = R.layout.fragment_login

    val viewModel: LoginFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
     //   viewModel.LoginApiHit().observe()



        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            Log.e("infor",""+it.body.app_name)
        })
    }
}
