package com.example.manuel.a1x1trainer.Drawable;

import android.graphics.Path;

/**
 * Finger Path
 *
 * Represents a drawen path in the Paint View
 */
public class FingerPath {
    public int color;
    boolean emboss;
    boolean blur;
    int strokeWidth;
    Path path;

    public FingerPath(int color, boolean emboss, boolean blur, int strokeWidth, Path path) {
        this.color = color;
        this.emboss = emboss;
        this.blur = blur;
        this.strokeWidth = strokeWidth;
        this.path = path;
    }
}
