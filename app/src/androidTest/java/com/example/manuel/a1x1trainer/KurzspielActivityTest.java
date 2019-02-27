package com.example.manuel.a1x1trainer;

import com.example.manuel.a1x1trainer.Activities.KurzspielActivity;
import com.example.manuel.a1x1trainer.Activities.OnboardingActivity;
import com.example.manuel.a1x1trainer.Activities.ScoreboardActivity;

import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;

import static com.example.manuel.a1x1trainer.TestUtils.checkActivityAfterClick;
import static com.example.manuel.a1x1trainer.TestUtils.checkButtonClosesActivity;
import static com.example.manuel.a1x1trainer.TestUtils.checkButtonWithText;
import static com.example.manuel.a1x1trainer.TestUtils.checkButtonWithoutText;
import static com.example.manuel.a1x1trainer.TestUtils.checkViewInvisible;
import static com.example.manuel.a1x1trainer.TestUtils.checkViewVisible;
import static com.example.manuel.a1x1trainer.TestUtils.checkTextViewShows;

public class KurzspielActivityTest {
    @Rule
    public ActivityTestRule<KurzspielActivity> mActivityRule =
            new ActivityTestRule<>(KurzspielActivity.class);

    @Test
    public void testElements() {
        checkViewInvisible(R.id.kurzspiel_bubble);
        TestUtils.checkViewInvisible(R.id.kurzspiel_bubble_text1);
        TestUtils.checkViewInvisible(R.id.kurzspiel_bubble_text2);
        checkViewVisible(R.id.kurzspiel_worm);
        checkButtonWithoutText(R.id.kurzspiel_back_btn);
        checkButtonWithText(R.id.kurzspiel_play_btn, R.string.kurzspiel_play);
        checkButtonWithoutText(R.id.kurzspiel_scoreboard_btn);
        checkTextViewShows(R.id.kurzspiel_title_text, R.string.kurzspiel_title);

        while (mActivityRule.getActivity().isWormCountdownTimerRunning() == null ||
                mActivityRule.getActivity().isWormCountdownTimerRunning());

        checkViewVisible(R.id.kurzspiel_bubble);
        checkViewVisible(R.id.kurzspiel_bubble_text1);
        checkViewVisible(R.id.kurzspiel_bubble_text2);
    }

    @Test
    public void testScoreboardClick() {
        checkActivityAfterClick(R.id.kurzspiel_scoreboard_btn, ScoreboardActivity.class.getName());
    }

    @Test
    public void testBackClick() {
        checkButtonClosesActivity(mActivityRule, R.id.kurzspiel_back_btn);
    }

    @Test
    public void testPlayClick() {
        checkActivityAfterClick(R.id.kurzspiel_play_btn, OnboardingActivity.class.getName());
    }
}
