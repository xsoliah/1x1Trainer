package com.example.manuel.a1x1trainer.Activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import com.example.manuel.a1x1trainer.Exceptions.GameModeNotPresentException;
import com.example.manuel.a1x1trainer.R;
import com.example.manuel.a1x1trainer.Ressources.GameMode;

import java.util.concurrent.atomic.AtomicBoolean;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public abstract class GameModeActivity extends Activity {
    protected GameMode gameMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void getGameMode() throws GameModeNotPresentException {
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            throw new GameModeNotPresentException();
        } else {
            gameMode = GameMode.valueOf(extras.getString(getString(R.string.intent_extra_game_mode)));
        }
    }

    protected void disableButton(Button toDisable) {
        toDisable.setEnabled(false);
        toDisable.setAlpha(0.8f);
    }

    protected void enableButton(Button toEnable) {
        toEnable.setEnabled(true);
        toEnable.setAlpha(1.0f);
    }
}
