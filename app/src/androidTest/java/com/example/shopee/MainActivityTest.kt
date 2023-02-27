package com.example.shopee

import androidx.lifecycle.Lifecycle.State
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import com.example.shopee.view.MainActivity
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @get:Rule
    val activityScenarioRule = activityScenarioRule<MainActivity>()

    @Test
    fun mainActivity_testAppLifeCycle() {
        launchActivity<MainActivity>().use { scenario ->
            scenario.moveToState(State.CREATED)
            scenario.moveToState(State.STARTED)
            scenario.moveToState(State.RESUMED)
            scenario.moveToState(State.DESTROYED)
        }
    }

    @Test
    fun testSearchBar_expectedReceivingInputs() {
        onView(withId(R.id.searchBar)).perform(typeText("Item 1"))
    }

    @Test
    fun testSearchBar_expectedMinimumOneSearchResult() {
        onView(withId(R.id.searchBar)).perform(typeText("Item 1"))
        onView(withId(R.id.linearRecyclerView))
            .check(matches(hasMinimumChildCount(1)))
    }

    @Test
    fun testSearchBar_SearchTextWithPrice190_expectedExactly4SearchResultAndErrorTExtNotDisplayed() {
        onView(withId(R.id.searchBar)).perform(typeText("190"))
        onView(withId(R.id.errorText))
            .check(matches(not(isDisplayed())))
        onView(withId(R.id.linearRecyclerView))
            .check(matches(hasChildCount(4)))
    }

    @Test
    fun testSearchBar_SearchTextWithProductNameAsProduct_expectedNoSearchResult() {
        onView(withId(R.id.searchBar))
            .perform(typeText("Product"))
        onView(withId(R.id.errorText))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testSwipeToRefresh_expectedMinimumOneSearchResult() {
        onView(withId(R.id.swipeRefresh))
            .perform(swipeDown()).check(matches(hasMinimumChildCount(1)))
    }

    @Test
    fun testBottomNav_expectedMinimumOneSearchResult() {
        onView(withId(R.id.swipeRefresh))
            .perform(swipeDown()).check(matches(hasMinimumChildCount(1)))
    }
}