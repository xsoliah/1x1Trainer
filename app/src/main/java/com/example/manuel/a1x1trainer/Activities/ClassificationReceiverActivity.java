package com.example.manuel.a1x1trainer.Activities;

import com.example.manuel.a1x1trainer.Classifier.ClassificationResultPaintViewIdentifier;
import com.example.manuel.a1x1trainer.Classifier.TensorFlowClassifier;
import com.example.manuel.a1x1trainer.Ressources.RuntimeConstants;


/**
 * Classification Receiver Activity
 *
 * Abstract parent class for activities which are receiving classification results from a PaintView
 */
public abstract class ClassificationReceiverActivity extends GameModeActivity {
    public TensorFlowClassifier classifier;
    public abstract void returnClassificationResult(String s, ClassificationResultPaintViewIdentifier identifier);

    /**
     * Loads the tensorflow model
     */
    public void loadModel() {
        //The Runnable interface is another way in which you can implement multi-threading other than extending the
        // //Thread class due to the fact that Java allows you to extend only one class. Runnable is just an interface,
        // //which provides the method run.
        // //Threads are implementations and use Runnable to call the method run().
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //add 2 classifiers to our classifier arraylist
                    //the tensorflow classifier and the keras classifier
                    classifier = TensorFlowClassifier.create(getAssets(), RuntimeConstants.MODEL_NAME,
                            RuntimeConstants.TENSORFLOW_MODEL_FILENAME, RuntimeConstants.MODEL_LABELS_FILENAME, RuntimeConstants.MODEL_INPUT_DIM_SIZE,
                            RuntimeConstants.MODEL_INPUT_NAME, RuntimeConstants.MODEL_OUTPUT_NAME, true);
                } catch (final Exception e) {
                    //if they aren't found, throw an error!
                    throw new RuntimeException(RuntimeConstants.ON_MODEL_LOAD_ERROR_RUNTIME_EXCEPTION, e);
                }
            }
        }).start();
    }
}
