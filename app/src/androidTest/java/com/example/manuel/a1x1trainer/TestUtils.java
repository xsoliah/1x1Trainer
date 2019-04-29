package com.example.manuel.a1x1trainer;

import com.example.manuel.a1x1trainer.Activities.ClassificationReceiverActivity;
import com.example.manuel.a1x1trainer.Classifier.Classification;
import com.example.manuel.a1x1trainer.Ressources.RuntimeConstants;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasTextColor;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withAlpha;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertTrue;

public class TestUtils {
    public static void checkActivityAfterClick(final int btn_id, final String expected_activity_classname) {
        Intents.init();
        onView(withId(btn_id)).perform(click());
        intended(hasComponent(expected_activity_classname));
        Intents.release();
    }

    public static void checkButtonClosesActivity(final ActivityTestRule activityTestRule, final int btn_id) {
        onView(withId(btn_id)).perform(click());
        assertTrue(activityTestRule.getActivity().isFinishing());
    }

    public static void checkButtonWithText(final int btn_id, final int str_id) {
        onView(withId(btn_id)).check(matches(isDisplayed()));
        onView(withId(btn_id)).check(matches(isEnabled()));
        onView(withId(btn_id)).check(matches(isClickable()));
        onView(withId(btn_id)).check(matches(withText(str_id)));
    }

    public static void checkNotClickableButtonWithText(final int btn_id, final int str_id) {
        onView(withId(btn_id)).check(matches(isDisplayed()));
        onView(withId(btn_id)).check(matches(withText(str_id)));
        onView(withId(btn_id)).check(matches(withAlpha(RuntimeConstants.DISABLED_ALPHA)));
    }

    public static void checkButtonWithoutText(final int btn_id) {
        onView(withId(btn_id)).check(matches(isDisplayed()));
        onView(withId(btn_id)).check(matches(isEnabled()));
        onView(withId(btn_id)).check(matches(isClickable()));
    }

    public static void checkTextViewShows(final int tv_id, final int str_id) {
        onView(withId(tv_id)).check(matches(isDisplayed()));
        onView(withId(tv_id)).check(matches(withText(str_id)));
    }

    public static void checkTextViewShows(final int tv_id, final String str) {
        onView(withId(tv_id)).check(matches(isDisplayed()));
        onView(withId(tv_id)).check(matches(withText(str)));
    }

    public static void checkViewVisible(final int iv_id) {
        onView(withId(iv_id)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    public static void checkViewGone(final int iv_id) {
        onView(withId(iv_id)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    public static void checkViewInvisible(final int iv_id) {
        onView(withId(iv_id)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }

    public static void checkEditText(final int tv_id) {
        String testInput = "Hello World!";
        onView(withId(tv_id)).check(matches(isDisplayed()));
        onView(withId(tv_id)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        typeTextToEditText(tv_id, testInput);
        onView(withId(tv_id)).check(matches(withText(testInput)));
        onView(withId(tv_id)).perform(clearText());
        onView(withId(tv_id)).check(matches(withText("")));
    }

    public static void typeTextToEditText(final int tv_id, final String to_type) {
        onView(withId(tv_id)).perform(typeText(to_type));
        onView(withId(tv_id)).perform(closeSoftKeyboard());
    }

    public static void checkIfClassifierWorks(final ClassificationReceiverActivity classificationReceiverActivity) {
        float[] pixel_data = new float[28*28];
        for (int i = 0; i < 28*28; ++i)
        {
            if (i % 28 == 15)
                pixel_data[i] = 1;
            else
                pixel_data[i] = 0;
        }
        Classification c = classificationReceiverActivity.classifier.recognize(pixel_data);
        assert c.getLabel().equals("1");
    }

    public static void checkTextColor(final int tv_id, final int color_id) {
        onView(withId(tv_id)).check(matches(hasTextColor(color_id)));
    }
}
