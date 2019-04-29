package com.example.manuel.a1x1trainer.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manuel.a1x1trainer.Classifier.ClassificationResultPaintViewIdentifier;
import com.example.manuel.a1x1trainer.Drawable.PaintView;
import com.example.manuel.a1x1trainer.Exceptions.GameModeNotPresentException;
import com.example.manuel.a1x1trainer.R;
import com.example.manuel.a1x1trainer.Ressources.GameMode;
import com.example.manuel.a1x1trainer.Ressources.RuntimeConstants;

import java.util.Random;

/**
 * Onboarding Activity
 *
 * Pre-game activity that should act like a drawing-classification-test
 * the user draws a random chosen digit and the classifier tries to recognise that drawen digit
 */
public class OnboardingActivity extends ClassificationReceiverActivity {
    private PaintView paintView;
    private ImageView digitView;
    private TextView classifiedResult;
    private Button actionButton;
    private Button scoreboardButton;
    protected Integer digitToDraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            getGameMode();
        } catch (GameModeNotPresentException e) {
            // should really never get here
            e.printStackTrace();
        }

        // get random digit
        Random random = new Random();
        digitToDraw = random.nextInt(9);

        // TODO: set digit image
        setContentView(R.layout.activity_onboarding);

        paintView = findViewById(R.id.onboarding_paint_edit);
        digitView = findViewById(R.id.onboarding_digit_to_draw);
        digitView.setBackgroundResource(RuntimeConstants.digitImages.get(digitToDraw));
        classifiedResult = findViewById(R.id.onboarding_user_input);
        actionButton = findViewById(R.id.onboarding_action_btn);
        actionButton.setText(getString(R.string.onboarding_delete_button));
        scoreboardButton = findViewById(R.id.onboarding_scoreboard_btn);

        scoreboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnboardingActivity.this, ScoreboardActivity.class);
                intent.putExtra(getString(R.string.intent_extra_game_mode), GameMode.KURZSPIEL.toString());
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionButton.getText().equals(getString(R.string.onboarding_delete_button))) {
                    // clear screens and text
                    classifiedResult.setText(getString(R.string.empty_string));
                    paintView.clearScreen();
                }
                else {
                    // start game and close this view
                    Intent intent = new Intent(OnboardingActivity.this, QuizActivity.class);
                    intent.putExtra(getString(R.string.intent_extra_game_mode), gameMode.toString());
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                }
            }
        });

        Button drawTestBackButton = (Button)findViewById(R.id.onboarding_back_btn);
        drawTestBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
            }
        });
    }

    /**
     * implementation of parent method
     * @param s classified digit represented as string
     * @param identifier unnecessary
     */
    public void returnClassificationResult(String s, ClassificationResultPaintViewIdentifier identifier) {
        classifiedResult.setText(s);
        if (s.equals(digitToDraw.toString())){
            scoreboardButton.setVisibility(View.VISIBLE);
            actionButton.setText(getString(R.string.onboarding_go_button));
            actionButton.setBackgroundResource(R.mipmap.button_green);
        }
    }

    /**
     * sets the paint views size
     * @param hasFocus unnecessary
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // set size of paintView
        double height = paintView.getHeight();
        loadModel();
        paintView.init((int)height, (int)height, this, ClassificationResultPaintViewIdentifier.NOT_IMPORTANT);
    }
}
