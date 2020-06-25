package com.example.zephyrtestapp.ui

import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zephyrtestapp.EspressoIdlingResource
import com.example.zephyrtestapp.network.MyRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class MyViewModel @Inject constructor(private val repository: MyRepository) : ViewModel() {


    private lateinit var textView: TextView


    private val defaultHandler = CoroutineExceptionHandler { _, exception ->
        when (exception) {
            is HttpException -> {
                textView.text = "HttpException-Error"
            }
            else -> {
                textView.text = "Other-Error"
            }
        }
    }

    fun load(activity: FragmentActivity?, textView: TextView, email: String, pass: String) {
        this.textView = textView
        viewModelScope.launch(defaultHandler) {
            withContext(Dispatchers.IO) {
                EspressoIdlingResource.increment()
                try {
                    val loginResponse = withContext(Dispatchers.IO) {
                        repository.login(email, pass)
                    }
                    withContext(Dispatchers.Main) {
                        this@MyViewModel.textView.text = "Successful"
                    }
                } finally {
                    EspressoIdlingResource.decrement()
                }
            }
        }
    }
}