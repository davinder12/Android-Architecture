package com.android.savery.ui.baseclass

import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import dagger.android.support.DaggerFragment

abstract class BaseBackStack : DaggerFragment() {


    companion object {
        const val BACK_STACK_DESCRIPTION = "BACK_STACK_DESCRIPTION"
        const val BACK_STACK_DOUBLE = "BACK_STACK_DOUBLE"
        const val BACK_STACK_INT = "BACK_STACK_INT"
        const val BACK_STACK_BOOLEAN = "BACK_STACK_BOOLEAN"

    }


    fun backStackGetBooleanData(key: String): MutableLiveData<Boolean>? {
        return findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData(key)
    }

    fun backStackPutBooleanData(key: String, value: Boolean) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(key, value)
    }


    fun backStackGetData(key: String): MutableLiveData<String>? {
        return findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(key)
    }

    fun backStackPutData(key: String, value: String) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(key, value)
    }


    fun backStackPutDouble(key: String, value: Double) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(key, value)
    }

    fun backStackGetDoubleData(key: String): MutableLiveData<Double>? {
        return findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Double>(key)
    }

    fun backStackPutInt(key: String, value: Int) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(key, value)
    }

    fun backStackGetIntData(key: String): MutableLiveData<Int>? {
        return findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Int>(key)
    }

}