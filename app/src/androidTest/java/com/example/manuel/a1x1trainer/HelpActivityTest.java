package com.example.manuel.a1x1trainer;

import com.example.manuel.a1x1trainer.Activities.HelpActivity;

import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;
import static com.example.manuel.a1x1trainer.TestUtils.checkTextViewShows;

public class HelpActivityTest {
    @Rule
    public ActivityTestRule<HelpActivity> mActivityRule =
            new ActivityTestRule<>(HelpActivity.class);

    @Test
    public void testElements() {
        checkTextViewShows(R.id.help_line1, R.string.help_line1);
        checkTextViewShows(R.id.help_line2, R.string.help_line2);
        checkTextViewShows(R.id.help_line3, R.string.help_line3);
    }
}
