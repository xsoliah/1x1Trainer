package com.example.manuel.a1x1trainer;

import com.example.manuel.a1x1trainer.Activities.HelpActivity;
import com.example.manuel.a1x1trainer.Activities.KurzspielActivity;
import com.example.manuel.a1x1trainer.Activities.LoginActivity;
import com.example.manuel.a1x1trainer.Activities.StartActivity;

import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;
import static com.example.manuel.a1x1trainer.TestUtils.checkActivityAfterClick;
import static com.example.manuel.a1x1trainer.TestUtils.checkButtonWithText;
import static com.example.manuel.a1x1trainer.TestUtils.checkButtonWithoutText;

public class StartActivityTest {
    @Rule
    public ActivityTestRule<StartActivity> mActivityRule =
            new ActivityTestRule<>(StartActivity.class);

    @Test
    public void testElements() {
        checkButtonWithText(R.id.start_trainer_btn, R.string.start_trainer);
        checkButtonWithText(R.id.start_kurzspiel_btn, R.string.start_kuzspiel_button);
        checkButtonWithoutText(R.id.start_help_btn);
    }

    @Test
    public void testTrainerClick() {
        checkActivityAfterClick(R.id.start_trainer_btn, LoginActivity.class.getName());
    }

    @Test
    public void testKurzspielClick() {
        checkActivityAfterClick(R.id.start_kurzspiel_btn, KurzspielActivity.class.getName());
    }

    @Test
    public void testHelpClick() {
        checkActivityAfterClick(R.id.start_help_btn, HelpActivity.class.getName());
    }
}
