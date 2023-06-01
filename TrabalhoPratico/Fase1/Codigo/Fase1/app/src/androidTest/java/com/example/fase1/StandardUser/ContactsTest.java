package com.example.fase1.StandardUser;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.example.fase1.ContactsActivity;
import com.example.fase1.R;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ContactsTest {

    @Rule
    public ActivityTestRule<ContactsActivity> contactsActivityRule = new ActivityTestRule<>(ContactsActivity.class, true, false);

    @Before
    public void setUp() {
        Intents.init();
    }


    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(RecyclerView.class);
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View childView = view.findViewById(id);
                childView.performClick();
            }
        };
    }

    @Test
    public void checkContactsToDial() {
        contactsActivityRule.launchActivity(new Intent());

        // Click the callicon
        onView(withId(R.id.recyclerViewContacts)).perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.callIcon)));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        intended(toPackage("com.android.dialer"));
    }

    @After
    public void tearDown() {
        Intents.release();
    }



}