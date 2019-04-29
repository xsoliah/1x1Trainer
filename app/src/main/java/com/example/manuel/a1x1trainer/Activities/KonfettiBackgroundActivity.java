package com.example.manuel.a1x1trainer.Activities;

import android.graphics.Color;
import android.os.Bundle;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

/**
 * Konfetti Background Activity
 *
 * Abstract parent class for activities with a konfetti background
 */
public abstract class KonfettiBackgroundActivity extends GameModeActivity {
    protected KonfettiView viewKonfetti;
    private boolean isConfettiRendered = false;

    private boolean isTestRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isTestRunning();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        checkRenderConfetti();
    }

    /**
     * checks if called from automated testing
     */
    public void isTestRunning () {
        boolean istest;

        try {

            Class.forName ("com.example.manuel.a1x1trainer.ActivityTest"); // for e.g. com.example.MyTest

            istest = true;
        } catch (ClassNotFoundException e) {
            istest = false;
        }

        isTestRunning = istest;
    }

    /**
     * renders konfetti if not yet rendered in view
     */
    protected void checkRenderConfetti() {
        if (!isConfettiRendered && !isTestRunning)
        {
            isConfettiRendered = true;
            viewKonfetti.build()
                    .addColors(Color.YELLOW, Color.GREEN, Color.RED, Color.BLUE)
                    .setDirection(0.0, 359.0)
                    .setSpeed(1f, 1.5f)
                    .setFadeOutEnabled(true)
                    .setTimeToLive(2000L)
                    .addShapes(Shape.RECT)
                    .addSizes(new Size(10, 4))
                    .setPosition(-50f, viewKonfetti.getWidth() + 50f, -50f, viewKonfetti.getHeight() + 50f)
                    .streamFor(30, Long.MAX_VALUE);
        }
    }
}
