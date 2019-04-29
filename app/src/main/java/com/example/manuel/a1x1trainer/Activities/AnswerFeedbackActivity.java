package com.example.manuel.a1x1trainer.Activities;

import android.os.Bundle;
import android.util.DisplayMetrics;

import com.example.manuel.a1x1trainer.R;

/**
 * Answer Feedback Activity
 *
 * should indicate if the given answer was right or wrong
 */
public class AnswerFeedbackActivity extends GameModeActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_answer_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        // metrics relative to parents metrics
        getWindow().setLayout((int)(width*.8), (int)(height*.6));
    }
}
