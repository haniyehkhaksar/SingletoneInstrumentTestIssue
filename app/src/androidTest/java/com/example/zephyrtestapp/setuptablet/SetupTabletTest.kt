package com.example.zephyrtestapp.setuptablet

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.zephyrtestapp.R
import org.junit.Test

class SetupTabletTest : SharedTabletSetup() {


    @Test
    fun tabletSetupTest() {

        plugAndPlayInterceptor.plugIn(SetupTabletMockInterceptor())

        onView(withId(R.id.text_email_input)).perform(typeText("correct@test.com"))

        onView(withId(R.id.text_password_input)).perform(typeText("test"))

        onView(withId(R.id.submit_button)).perform(click())

        onView(withId(R.id.result)).check(
            ViewAssertions.matches(withText("Successful"))
        )

    }

    @Test
    fun tabletSetupTestWithWrongValue() {
        // Section 1
        onView(withId(R.id.text_email_input)).perform(typeText("wrong@test.com"))
        onView(withId(R.id.text_password_input)).perform(typeText("test"))

        onView(withId(R.id.submit_button)).perform(click())

        onView(withId(R.id.result)).check(
            ViewAssertions.matches(withText("HttpException-Error"))
        )

        // Section 2
        onView(withId(R.id.text_email_input)).perform(clearText())
        onView(withId(R.id.text_password_input)).perform(clearText())

        onView(withId(R.id.text_email_input)).perform(typeText("correct@test.com"))
        onView(withId(R.id.text_password_input)).perform(typeText("wrong-test"))

        onView(withId(R.id.submit_button)).perform(click())

        onView(withId(R.id.result)).check(
            ViewAssertions.matches(withText("HttpException-Error"))
        )
    }
}
