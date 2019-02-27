package com.example.manuel.a1x1trainer;

import android.content.Intent;

import com.example.manuel.a1x1trainer.Activities.QuizActivity;
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
import static com.example.manuel.a1x1trainer.TestUtils.checkNotClickableButtonWithText;
import static com.example.manuel.a1x1trainer.TestUtils.checkTextViewShows;
import static com.example.manuel.a1x1trainer.TestUtils.checkViewGone;
import static com.example.manuel.a1x1trainer.TestUtils.checkViewInvisible;
import static com.example.manuel.a1x1trainer.TestUtils.checkViewVisible;

@RunWith(AndroidJUnit4.class)
public class QuizActivityTest {
    @Rule
    public IntentsTestRule<QuizActivity> mActivityRule =
            new IntentsTestRule<>(QuizActivity.class, true, false);

    @Before
    public void initKurzspiel() {
        Intent i = new Intent();
        i.putExtra("MODE", GameMode.KURZSPIEL.toString());
        mActivityRule.launchActivity(i);
    }

    @Test
    public void testElements() {
        checkViewVisible(R.id.quiz_background_modal);
        checkViewVisible(R.id.quiz_clear_btn);
        checkViewVisible(R.id.quiz_countdown);
        checkViewVisible(R.id.quiz_countdown_background);
        checkViewVisible(R.id.quiz_question);
        checkViewVisible(R.id.quiz_answer);
        checkButtonWithoutText(R.id.quiz_back_btn);
        checkViewInvisible(R.id.quiz_fake_middle);
        checkViewGone(R.id.quiz_feedback_modal);
        checkViewGone(R.id.quiz_feedback_modal_text);
        checkViewVisible(R.id.quiz_left_paint_edit);
        checkViewVisible(R.id.quiz_right_paint_edit);
        checkViewVisible(R.id.quiz_score_background);
        checkTextViewShows(R.id.quiz_score, R.string.zero);
        checkViewGone(R.id.quiz_loading_spinner);
        checkButtonWithText(R.id.quiz_clear_btn, R.string.quiz_clear_btn);
        checkNotClickableButtonWithText(R.id.quiz_ok_btn, R.string.quiz_ok_btn);
        checkViewVisible(R.id.quiz_progressbar);
    }

    public void testBackClick() {
        checkButtonClosesActivity(mActivityRule, R.id.quiz_back_btn);
    }
}
