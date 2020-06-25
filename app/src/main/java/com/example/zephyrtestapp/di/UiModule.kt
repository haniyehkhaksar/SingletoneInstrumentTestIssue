package com.example.zephyrtestapp.di

import com.example.zephyrtestapp.ui.MainActivity
import com.example.zephyrtestapp.ui.MyFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class UiModule {

    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun bindMyFragment(): MyFragment

}