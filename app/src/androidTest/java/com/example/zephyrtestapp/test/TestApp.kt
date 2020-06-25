package com.example.zephyrtestapp.test

import com.example.zephyrtestapp.MyApp


/**
 * The application class used when running the app in instrumented tests.
 *
 * This is activated in [TestRunner].
 */
class TestApp : MyApp() {

    lateinit var component: TestAppComponent

    override fun initDagger() {
        component = DaggerTestAppComponent.builder()
            .application(this)
            .build()

        component.inject(this)
    }
}
