package com.example.manuel.a1x1trainer.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.manuel.a1x1trainer.Exceptions.GameModeNotPresentException;
import com.example.manuel.a1x1trainer.R;
import com.example.manuel.a1x1trainer.Ressources.GameMode;
import com.example.manuel.a1x1trainer.Ressources.RuntimeConstants;

/**
 * Game Mode Activity
 *
 * Abstract parent class for Activities which have to know the current GameMode
 * (Kurzspiel v Training)
 */
public abstract class GameModeActivity extends Activity {
    protected GameMode gameMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // for hiding title

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * gets the GameMode from the Bundle built by the calling activity
     * @throws GameModeNotPresentException
     */
    protected void getGameMode() throws GameModeNotPresentException {
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            throw new GameModeNotPresentException();
        } else {
            gameMode = GameMode.valueOf(extras.getString(getString(R.string.intent_extra_game_mode)));
        }
    }

    /**
     * disables a given button
     * @param toDisable the button to disable
     */
    protected void disableButton(Button toDisable) {
        toDisable.setEnabled(false);
        toDisable.setAlpha(RuntimeConstants.DISABLED_ALPHA);
    }

    /**
     * enables a given button
     * @param toEnable the button to enable
     */
    protected void enableButton(Button toEnable) {
        toEnable.setEnabled(true);
        toEnable.setAlpha(RuntimeConstants.ENABLED_ALPHA);
    }
}
