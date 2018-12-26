package com.jacquessmuts.rxstarter;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.jacquessmuts.rxstarter.java.sample.PinActivity;
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
public class PinActivityTest extends BaseInstrumentTest {

    @Rule
    public ActivityTestRule<PinActivity> mActivityTestRule = new ActivityTestRule<>(PinActivity.class);

    @Test
    public void pinActivityTest() {

        ViewInteraction button1 = onView(
                allOf(withId(R.id.button1), withText("One"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                        1),
                                3),
                        isDisplayed()));


        ViewInteraction textView = onView(ViewMatchers.withId(R.id.textView));
        textView.check(matches(withText("")));

        button1.perform(click());
        button1.perform(click());
        button1.perform(click());
        button1.perform(click());

        textView.check(matches(isDisplayed()));
        textView.check(matches(isTextLength(4,4)));
        textView.check(matches(withText("****")));

        button1.perform(click());
        button1.perform(click());
        button1.perform(click());
        button1.perform(click());

        textView.check(matches(isDisplayed()));
        textView.check(matches(withText(appContext.getString(R.string.pin_match_success))));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.button2), withText("Two"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                        1),
                                8),
                        isDisplayed()));
        button2.perform(click());
        button2.perform(click());
        button2.perform(click());
        button2.perform(click());

        textView.check(matches(isDisplayed()));
        textView.check(matches(isTextLength(4,4)));
        textView.check(matches(withText("****")));

        ViewInteraction button3 = onView(
                allOf(withId(R.id.button3), withText("Three"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                        1),
                                9),
                        isDisplayed()));
        button3.perform(click());
        button3.perform(click());
        button3.perform(click());
        button3.perform(click());

        textView.check(matches(isDisplayed()));
        textView.check(matches(isTextLength(4,4)));
        textView.check(matches(withText("****")));

    }
}
