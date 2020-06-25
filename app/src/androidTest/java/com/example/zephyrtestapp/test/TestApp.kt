package com.example.zephyrtestapp.test

import com.example.zephyrtestapp.MyApp


/**
 * The application class used when running the app in instrumented tests.
 *
 * This is activated in [TestRunner].
 */
class TestApp : MyApp() {

    override fun initDagger() {
        DaggerTestAppComponent.builder()
            .application(this)
            .build()
            .inject(this)
    }
}
