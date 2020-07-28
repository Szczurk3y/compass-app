package com.szczurk3y.compass

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.szczurk3y.compass.view.compass.CompassActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InputDialogTest {

    @Test fun test_showDialog_captureInput() {

        // GIVEN
        var activityScenario = ActivityScenario.launch(CompassActivity::class.java)
        val EXPECTED_LATITUDE = "55.123"
        val EXPECTED_LONGITUDE = "44.856"

        // Execute and Verify
        Espresso.onView(ViewMatchers.withId(R.id.latitudeButton)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText(R.string.input_dialog_title))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // enter input and quit
        Espresso.onView(ViewMatchers.withId(R.id.enterLatitude))
            .perform(ViewActions.typeText("55,123"))
        Espresso.onView(ViewMatchers.withId(R.id.enterLongitude))
            .perform(ViewActions.typeText("44,856"))

        Espresso.onView(ViewMatchers.withId(R.id.confirm_buttom)).perform(ViewActions.click())

        // make sure dialog is gone
        Espresso.onView(ViewMatchers.withText(R.string.input_dialog_title))
            .check(ViewAssertions.doesNotExist())

        // confirm latitude and longitude are transformed and set to TextView in activity
        Espresso.onView(ViewMatchers.withId(R.id.direction_latitude))
            .check(ViewAssertions.matches(ViewMatchers.withText(EXPECTED_LATITUDE)))
        Espresso.onView(ViewMatchers.withId(R.id.direction_longitude))
            .check(ViewAssertions.matches(ViewMatchers.withText(EXPECTED_LONGITUDE)))
    }

}