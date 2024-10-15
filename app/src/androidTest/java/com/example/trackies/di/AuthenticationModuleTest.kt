package com.example.trackies.di

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest // generates Hilt components for each test
class AuthenticationModuleTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this) // manages the components' state and performs injection on test

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun test() {}
}