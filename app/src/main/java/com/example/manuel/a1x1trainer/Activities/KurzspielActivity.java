package com.example.manuel.a1x1trainer.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manuel.a1x1trainer.R;
import com.example.manuel.a1x1trainer.Ressources.GameMode;
import com.example.manuel.a1x1trainer.Ressources.RuntimeConstants;

public class KurzspielActivity extends Activity {
    private Button kurzspielBackButton;
    private Button scoreboardButton;
    private Button playButton;
    private ImageView worm;
    private ImageView bubble;
    private TextView bubbleText1;
    private TextView bubbleText2;
    private CountDownTimer wormCountdownTimer;
    private Boolean wormCountdownTimerRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_kurzspiel);

        kurzspielBackButton = findViewById(R.id.kurzspiel_back_btn);
        scoreboardButton = findViewById(R.id.kurzspiel_scoreboard_btn);
        playButton = findViewById(R.id.kurzspiel_play_btn);
        worm = findViewById(R.id.kurzspiel_worm);
        bubble = findViewById(R.id.kurzspiel_bubble);
        bubbleText1 = findViewById(R.id.kurzspiel_bubble_text1);
        bubbleText2 = findViewById(R.id.kurzspiel_bubble_text2);

        // BACK
        kurzspielBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0, 0);
            }
        });

        // BESTENLISTE
        scoreboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KurzspielActivity.this, ScoreboardActivity.class);
                intent.putExtra(getString(R.string.intent_extra_game_mode), GameMode.KURZSPIEL.toString());
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        // SPIELEN
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KurzspielActivity.this, OnboardingActivity.class);
                intent.putExtra(getString(R.string.intent_extra_game_mode), GameMode.KURZSPIEL.toString());
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });


        // WORM
        worm.setX(0);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final int width = dm.widthPixels;
        final float border = (2*width)/5;

        float seconds = (float)RuntimeConstants.WORM_ANIMATION_TIME / (float)1000;
        float total_frames = seconds * RuntimeConstants.WORM_ANIMATION_FPS;
        float interval = (float)RuntimeConstants.WORM_ANIMATION_TIME / total_frames;

        wormCountdownTimerRunning = true;
        wormCountdownTimer = new CountDownTimer(RuntimeConstants.WORM_ANIMATION_TIME, (long)interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                float fg_pc = (float)(RuntimeConstants.WORM_ANIMATION_TIME-millisUntilFinished)/(float)RuntimeConstants.WORM_ANIMATION_TIME;
                // move step
                worm.setX(fg_pc * border);
            }

            @Override
            public void onFinish() {
                bubble.setVisibility(View.VISIBLE);
                bubbleText1.setVisibility(View.VISIBLE);
                bubbleText2.setVisibility(View.VISIBLE);
                wormCountdownTimerRunning = false;
            }
        }.start();
    }

    public Boolean isWormCountdownTimerRunning() {
        return wormCountdownTimerRunning;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    class WormWalker extends AsyncTask {

        @Override
        protected Void doInBackground(Object... objects) {
            ImageView wormImage = findViewById(R.id.kurzspiel_worm);

            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);

            int width = dm.widthPixels;
            float border = (2*width)/5;

            float xStart = wormImage.getX();
            for (int i = (int)xStart; i < border; i+=7)
            {
                wormImage.setX(i);
                if (isCancelled())
                    break;
                android.os.SystemClock.sleep(1);
            }
            return null;
        }
    }
}
