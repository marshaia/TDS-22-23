package com.example.fase1.PremiumUser;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.fase1.LoginActivity;
import com.example.fase1.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class FavoritesTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void FavoritesToTrailTest() {

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.navBar_perfil), withContentDescription("Profile"),
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
                                2),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.profile_favorites), withText("Check Favorites"),
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
                                8),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction relativeLayout = onView(
                allOf(withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        relativeLayout.check(matches(isDisplayed()));
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
