package com.jacquessmuts.rxstarter.sample;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.jacquessmuts.rxstarter.R;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.jacquessmuts.rxstarter.EspressoUtils.isTextLength;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DistinctActivityTest {

    @Rule
    public ActivityTestRule<com.jacquessmuts.rxstarter.java.sample.DistinctActivity> mActivityTestRule =
            new ActivityTestRule<>(com.jacquessmuts.rxstarter.java.sample.DistinctActivity.class);

    @Test
    public void buttonClickOnceTest() {

        ViewInteraction textView = onView(ViewMatchers.withId(R.id.textView));

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.button), isDisplayed()));
        appCompatButton.perform(click());

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        textView.check(matches(isDisplayed()));
        textView.check(matches(isTextLength(2, 12)));

        appCompatButton.perform(click());
        textView.check(matches(isTextLength(2, 12)));
    }

}

