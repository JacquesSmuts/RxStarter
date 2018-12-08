package com.jacquessmuts.rxstarter;


import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static com.jacquessmuts.rxstarter.EspressoUtils.childAtPosition;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ButtonRapidClickActivity {

    @Rule
    public ActivityTestRule<com.jacquessmuts.rxstarter.java.sample.ButtonRapidClickActivity> mActivityTestRule =
            new ActivityTestRule<>(com.jacquessmuts.rxstarter.java.sample.ButtonRapidClickActivity.class);

    @Test
    public void buttonRapidClickActivity() {

        ViewInteraction textView = onView(withId(R.id.textView));

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.button), withText("Press Me Rapidly"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                        1),
                                1),
                        isDisplayed()));
        appCompatButton.perform(click());

        textView.check(matches(withText("1")));

        appCompatButton.perform(click());
        textView.check(matches(withText("1")));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        appCompatButton.perform(click());
        textView.check(matches(withText("2")));

    }

}
