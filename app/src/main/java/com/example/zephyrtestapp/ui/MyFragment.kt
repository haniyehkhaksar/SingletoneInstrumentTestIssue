package com.example.zephyrtestapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.zephyrtestapp.R
import com.google.android.material.textfield.TextInputEditText
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class MyFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val myViewModel: MyViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.my_fragment, container, false)
        view.findViewById<Button>(R.id.submit_button).setOnClickListener {
            myViewModel.load(
                activity, view.findViewById(R.id.result),
                view.findViewById<TextInputEditText>(R.id.text_email_input).text.toString(),
                view.findViewById<TextInputEditText>(R.id.text_password_input).text.toString()
            )
        }
        return view
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

}

