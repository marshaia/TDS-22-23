package com.example.fase1.PremiumUser;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import com.example.fase1.LoginActivity;
import com.example.fase1.MainActivity;
import com.example.fase1.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TrailsTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION",
                    "android.permission.ACCESS_COARSE_LOCATION");

    @Test
    public void trailsToTrailTest() {

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.navBar_roteiros), withContentDescription("Trails"),
                        childAtPosition(
                                childAtPosition(
                                        allOf(withId(R.id.bottom_navigation),
                                                childAtPosition(
                                                        allOf(withId(R.id.include),
                                                                childAtPosition(
                                                                        childAtPosition(
                                                                                allOf(withId(android.R.id.content),
                                                                                        childAtPosition(
                                                                                                allOf(withId(com.google.android.material.R.id.decor_content_parent),
                                                                                                        childAtPosition(
                                                                                                                childAtPosition(
                                                                                                                        withClassName(is("android.widget.LinearLayout")),
                                                                                                                        1),
                                                                                                                0)),
                                                                                                0)),
                                                                                0),
                                                                        1)),
                                                        1)),
                                        0),
                                1),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyclerViewTrails),
                        childAtPosition(
                                childAtPosition(
                                        allOf(withId(R.id.scrollViewTrails),
                                                childAtPosition(
                                                        childAtPosition(
                                                                allOf(withId(android.R.id.content),
                                                                        childAtPosition(
                                                                                allOf(withId(com.google.android.material.R.id.decor_content_parent),
                                                                                        childAtPosition(
                                                                                                childAtPosition(
                                                                                                        withClassName(is("android.widget.LinearLayout")),
                                                                                                        1),
                                                                                                0)),
                                                                                0)),
                                                                0),
                                                        0)),
                                        0),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction viewGroup = onView(
                allOf(withParent(allOf(withId(R.id.trailActivity),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        viewGroup.check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
