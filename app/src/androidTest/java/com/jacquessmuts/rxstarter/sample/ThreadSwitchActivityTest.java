package com.jacquessmuts.rxstarter.sample;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import com.jacquessmuts.rxstarter.R;
import com.jacquessmuts.rxstarter.java.sample.ThreadSwitchActivity;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static com.jacquessmuts.rxstarter.EspressoUtils.childAtPosition;
import static com.jacquessmuts.rxstarter.EspressoUtils.isTextLength;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ThreadSwitchActivityTest {

    @Rule
    public ActivityTestRule<ThreadSwitchActivity> mActivityTestRule = new ActivityTestRule<>(ThreadSwitchActivity.class);

    @Test
    public void threadSwitchActivityTest() {

        ViewInteraction textView = onView(
                Matchers.allOf(ViewMatchers.withId(R.id.textView),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                        1),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("0")));

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.buttonNoThreading), withText("no Threading"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                        1),
                                1),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.buttonGoodThreading), withText("Good threading"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                        1),
                                2),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.buttonNoThreading), withText("no Threading"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                        1),
                                1),
                        isDisplayed()));
        appCompatButton3.perform(click());

        textView.check(matches(isDisplayed()));

        textView.check(matches(isTextLength(1, 3)));
    }

}
