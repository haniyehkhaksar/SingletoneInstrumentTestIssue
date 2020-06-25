package com.example.zephyrtestapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

@AppScope
class ViewModelFactory @Inject constructor(
    private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (viewModels.containsKey(modelClass))
            return viewModels[modelClass]?.get() as T
        throw IllegalAccessException("Could not find view model class. Did you forget to add view model binding to ViewModelModule?")
    }
}