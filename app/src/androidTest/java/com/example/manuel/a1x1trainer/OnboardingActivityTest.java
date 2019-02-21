package com.example.manuel.a1x1trainer;

import android.content.Intent;

import com.example.manuel.a1x1trainer.Activities.OnboardingActivity;
import com.example.manuel.a1x1trainer.Ressources.GameMode;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static com.example.manuel.a1x1trainer.TestUtils.checkButtonClosesActivity;
import static com.example.manuel.a1x1trainer.TestUtils.checkButtonWithText;
import static com.example.manuel.a1x1trainer.TestUtils.checkButtonWithoutText;
import static com.example.manuel.a1x1trainer.TestUtils.checkIfClassifierWorks;
import static com.example.manuel.a1x1trainer.TestUtils.checkImageViewVisible;
import static com.example.manuel.a1x1trainer.TestUtils.checkTextViewShows;

@RunWith(AndroidJUnit4.class)
public class OnboardingActivityTest {
    @Rule
    public IntentsTestRule<OnboardingActivity> mActivityRule =
            new IntentsTestRule<>(OnboardingActivity.class, true, false);

    @Before
    public void initKurzspiel() {
        Intent i = new Intent();
        i.putExtra("MODE", GameMode.KURZSPIEL.toString());
        mActivityRule.launchActivity(i);
    }

    @Test
    public void testElements() {
        // description
        checkTextViewShows(R.id.onboarding_description1, R.string.onboarding_description_1);
        checkTextViewShows(R.id.onboarding_description2, R.string.onboarding_description_2);
        checkTextViewShows(R.id.onboarding_user_input_text, R.string.onboarding_user_input);

        // button
        checkButtonWithoutText(R.id.onboarding_action_btn);

        // paint edit
        checkImageViewVisible(R.id.onboarding_paint_edit);

        // back btn
        checkButtonWithoutText(R.id.onboarding_back_btn);

        // action button should show delete in the beginning
        checkButtonWithText(R.id.onboarding_action_btn, R.string.onboarding_delete_button);
    }

    @Test
    public void testBackClick() {
        checkButtonClosesActivity(mActivityRule, R.id.onboarding_back_btn);
    }

    @Test
    public void testClassifier() {
        checkIfClassifierWorks(mActivityRule.getActivity());
    }

    // TODO: check if digit image present
}
