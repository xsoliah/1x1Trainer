package com.example.manuel.a1x1trainer.Drawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.manuel.a1x1trainer.Activities.ClassificationReceiverActivity;
import com.example.manuel.a1x1trainer.Classifier.Classification;
import com.example.manuel.a1x1trainer.Classifier.ClassificationResultPaintViewIdentifier;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Paint View
 *
 * View to draw onto
 */
public class PaintView extends View {
    public static int BRUSH_SIZE = 40;
    public static final int DEFAULT_COLOR = Color.BLACK;
    public static final int DEFAULT_BG_COLOR = Color.TRANSPARENT;
    private static final float TOUCH_TOLERANCE = 4;
    private float mX, mY;
    private Path mPath;
    private Paint mPaint;
    private ArrayList<FingerPath> paths = new ArrayList<>();
    private int currentColor;
    private int backgroundColor = DEFAULT_BG_COLOR;
    private int strokeWidth;
    private boolean emboss;
    private boolean blur;
    private MaskFilter mEmboss;
    private MaskFilter mBlur;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    Lock drawingLock;
    Condition onFinishedDrawing;
    ClassificationReceiverActivity classificationReceiverActivity;
    ClassificationResultPaintViewIdentifier classificationResultPaintViewIdentifier;

    public PaintView(Context context) {
        this(context, null);
    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(DEFAULT_COLOR);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xff);

        mEmboss = new EmbossMaskFilter(new float[] {1, 1, 1}, 0.4f, 6, 3.5f);
        mBlur = new BlurMaskFilter(5, BlurMaskFilter.Blur.NORMAL);

        drawingLock = new ReentrantLock();
        onFinishedDrawing = drawingLock.newCondition();
    }

    /**
     * initializes the Paint View
     * called from parent activity
     * @param width
     * @param height
     * @param classification_receiver
     * @param identifier
     */
    public void init(int width, int height, ClassificationReceiverActivity classification_receiver, ClassificationResultPaintViewIdentifier identifier) {
        classificationResultPaintViewIdentifier = identifier;
        classificationReceiverActivity = classification_receiver;
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        currentColor = DEFAULT_COLOR;
        strokeWidth = BRUSH_SIZE;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();

        mCanvas.drawColor(backgroundColor);

        for (FingerPath fp : paths) {
            mPaint.setColor(fp.color);
            mPaint.setStrokeWidth(fp.strokeWidth);
            mPaint.setMaskFilter(null);

            if (fp.emboss)
                mPaint.setMaskFilter(mEmboss);
            else if (fp.blur)
                mPaint.setMaskFilter(mBlur);

            mCanvas.drawPath(fp.path, mPaint);

        }

        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.restore();
    }

    /**
     * called when the user starts touching the Paint View
     * @param x x-coordinate
     * @param y y-coordinate
     */
    private void touchStart(float x, float y) {
        mPath = new Path();
        FingerPath fp = new FingerPath(currentColor, emboss, blur, strokeWidth, mPath);
        paths.add(fp);

        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    /**
     * called when the user moves his finger while touching the Paint View
     * @param x x-coordinate
     * @param y y-coordinate
     */
    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    /**
     * clears the drawing from the Paint View
     */
    public void clearScreen() {
        // clear
        mCanvas.drawColor(DEFAULT_BG_COLOR, PorterDuff.Mode.CLEAR);
        paths.clear();
        // normal
        emboss = false;
        blur = false;

        invalidate();
    }

    /**
     * called when the user stops touching the Paint View
     * connects the old with the new location to draw a line
     */
    private void touchUp() {
        mPath.lineTo(mX, mY);

        // convolute drawing
        float[] pixelData = getPixelData();
        // classify drawing
        Classification c = classificationReceiverActivity.classifier.recognize(pixelData);
        classificationReceiverActivity.returnClassificationResult(c.getLabel(), this.classificationResultPaintViewIdentifier);
    }

    /**
     * called when a motion event is triggered
     * should react according to the type of the motion event
     * @param event motion event to react to
     * @return unnecessary
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE :
                touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP :
                touchUp();
                invalidate();
                break;
        }

        return true;
    }

    /**
     * Get 28x28 pixel data for tensorflow input.
     */
    public float[] getPixelData() {
        if (mBitmap == null) {
            return null;
        }

        Bitmap shaped = Bitmap.createScaledBitmap(mBitmap, 28, 28, true);

        int width = shaped.getWidth();
        int height = shaped.getHeight();

        // Get 28x28 pixel data from bitmap
        int[] pixels = new int[width * height];
        shaped.getPixels(pixels, 0, width, 0, 0, width, height);

        float[] retPixels = new float[pixels.length];
        for (int i = 0; i < pixels.length; ++i) {
            // Set 0 for white and 255 for black pixel
            retPixels[i] = pixels[i] == 0 ? 0 : 255;
            /*int pix = pixels[i];
            int b = pix & 0xff;
            retPixels[i] = (float)((0xff - b)/255.0);*/
        }
        return retPixels;
    }
}
