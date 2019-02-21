package com.example.manuel.a1x1trainer;

import android.view.MotionEvent;
import android.view.View;

import com.example.manuel.a1x1trainer.Activities.ClassificationReceiverActivity;
import com.example.manuel.a1x1trainer.Classifier.Classification;

import org.hamcrest.Matcher;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.MotionEvents;
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
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertTrue;

public class TestUtils {
    public static void checkActivityAfterClick(int btn_id, String expected_activity_classname) {
        Intents.init();
        onView(withId(btn_id)).perform(click());
        intended(hasComponent(expected_activity_classname));
        Intents.release();
    }

    public static void checkButtonClosesActivity(ActivityTestRule activityTestRule, int btn_id) {
        onView(withId(btn_id)).perform(click());
        assertTrue(activityTestRule.getActivity().isFinishing());
    }

    public static void checkButtonWithText(int btn_id, int str_id) {
        onView(withId(btn_id)).check(matches(isDisplayed()));
        onView(withId(btn_id)).check(matches(isEnabled()));
        onView(withId(btn_id)).check(matches(isClickable()));
        onView(withId(btn_id)).check(matches(withText(str_id)));
    }

    public static void checkButtonWithoutText(int btn_id) {
        onView(withId(btn_id)).check(matches(isDisplayed()));
        onView(withId(btn_id)).check(matches(isEnabled()));
        onView(withId(btn_id)).check(matches(isClickable()));
    }

    public static void checkTextViewShows(int tv_id, int str_id) {
        onView(withId(tv_id)).check(matches(isDisplayed()));
        onView(withId(tv_id)).check(matches(withText(str_id)));
    }

    public static void checkImageViewVisible(int iv_id) {
        onView(withId(iv_id)).check(matches(isDisplayed()));
        onView(withId(iv_id)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    public static void checkImageViewGone(int iv_id) {
        onView(withId(iv_id)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    public static void checkImageViewInvisible(int iv_id) {
        onView(withId(iv_id)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }

    public static void checkEditText(int tv_id) {
        String testInput = "Hello World!";
        onView(withId(tv_id)).check(matches(isDisplayed()));
        onView(withId(tv_id)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        typeTextToEditText(tv_id, testInput);
        onView(withId(tv_id)).check(matches(withText(testInput)));
        onView(withId(tv_id)).perform(clearText());
        onView(withId(tv_id)).check(matches(withText("")));
    }

    public static void typeTextToEditText(int tv_id, String to_type) {
        onView(withId(tv_id)).perform(typeText(to_type));
        onView(withId(tv_id)).perform(closeSoftKeyboard());
    }

    public static void checkIfClassifierWorks(ClassificationReceiverActivity classificationReceiverActivity) {
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

    public static ViewAction touchDownAndUp(final float x, final float y) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "Send touch events.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                // Get view absolute position
                int[] location = new int[2];
                view.getLocationOnScreen(location);

                // Offset coordinates by view position
                float[] coordinates = new float[] { x + location[0], y + location[1] };
                float[] precision = new float[] { 1f, 1f };

                // Send down event, pause, and send up
                MotionEvent down = MotionEvents.sendDown(uiController, coordinates, precision).down;
                uiController.loopMainThreadForAtLeast(200);
                MotionEvents.sendUp(uiController, down, coordinates);
            }
        };
    }
}
