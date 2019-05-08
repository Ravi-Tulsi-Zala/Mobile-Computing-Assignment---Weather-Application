package com.example.ravizala.weather;

// Zala Ravi Tulsi      B00805073


// Junit testing for UI testing
// Test case will fail if any null value leaks from UI widgets
// Got the help from https://developer.android.com/training/testing/unit-testing/local-unit-tests


import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainTest = new ActivityTestRule<MainActivity>(MainActivity.class);

    private MainActivity mainActivity = null;

    @Before
    public void setUp() throws Exception {

        mainActivity = mainTest.getActivity();

    }

    @Test
    public void testCaseForWidgets()
    {
        View view = mainActivity.findViewById(R.id.tv_city);
        assertNotNull(view);

         view = mainActivity.findViewById(R.id.tv_atmosphere);
        assertNotNull(view);

        view = mainActivity.findViewById(R.id.tv_clouds);
        assertNotNull(view);

        view = mainActivity.findViewById(R.id.tv_description);
        assertNotNull(view);

        view = mainActivity.findViewById(R.id.tv_minvalue);
        assertNotNull(view);

        view = mainActivity.findViewById(R.id.tv_maxvalue);
        assertNotNull(view);

        view = mainActivity.findViewById(R.id.tv_temp);
        assertNotNull(view);

    }


    @After
    public void tearDown() throws Exception {

        mainActivity = null;

    }
}