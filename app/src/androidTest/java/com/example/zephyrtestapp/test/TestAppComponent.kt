package com.example.zephyrtestapp.test

import android.app.Application
import com.example.zephyrtestapp.di.*
import com.example.zephyrtestapp.setuptablet.SharedTabletSetup
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule


@AppScope
@Component(modules = [AndroidSupportInjectionModule::class, UiModule::class, ViewModelModule::class, MockNetworkModule::class])
interface TestAppComponent : AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): TestAppComponent
    }

    fun inject(test: SharedTabletSetup)
}
