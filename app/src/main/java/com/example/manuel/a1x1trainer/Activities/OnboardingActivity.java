package com.example.manuel.a1x1trainer.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manuel.a1x1trainer.Classifier.ClassificationResultPaintViewIdentifier;
import com.example.manuel.a1x1trainer.Exceptions.GameModeNotPresentException;
import com.example.manuel.a1x1trainer.R;
import com.example.manuel.a1x1trainer.Drawable.PaintView;
import com.example.manuel.a1x1trainer.Ressources.GameMode;

import java.util.Random;

public class OnboardingActivity extends ClassificationReceiverActivity {
    private PaintView paintView;
    private ImageView modalView;
    private TextView classifiedResult;
    private Button actionButton;

    // TODO: remove
    protected TextView testText;

    protected Integer digitToDraw;

    public void returnClassificationResult(String s, ClassificationResultPaintViewIdentifier identifier) {
        classifiedResult.setText(s);
        if (s.equals(digitToDraw.toString())){
            actionButton.setText(getString(R.string.onboarding_go_button));
            // TODO: change background of button
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            getGameMode();
        } catch (GameModeNotPresentException e) {
            e.printStackTrace();
            // TODO: fatal exception --> ??
        }

        // get random digit
        Random random = new Random();
        digitToDraw = random.nextInt(9);

        // TODO: set digit image
        setContentView(R.layout.activity_onboarding);

        paintView = findViewById(R.id.onboarding_paint_edit);
        modalView = findViewById(R.id.onboarding_background_modal);
        classifiedResult = findViewById(R.id.onboarding_user_input);
        actionButton = findViewById(R.id.onboarding_action_btn);
        actionButton.setText(getString(R.string.onboarding_delete_button));


        // TODO: remove
        testText = findViewById(R.id.test_text);
        testText.setText(digitToDraw.toString());

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

    public void setGameMode(GameMode game_mode) {
        gameMode = game_mode;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // set size of paintView
        double height = paintView.getHeight();
        loadModel();
        paintView.init((int)height, (int)height, this, ClassificationResultPaintViewIdentifier.NOT_IMPORTANT);
    }
}
