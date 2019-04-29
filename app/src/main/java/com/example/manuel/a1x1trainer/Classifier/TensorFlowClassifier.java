package com.example.manuel.a1x1trainer.Classifier;

import android.content.res.AssetManager;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Tensorflow Classifier
 *
 * Implementation of the Classifier with Google's tensorflow framework
 */
public class TensorFlowClassifier {
    private TensorFlowInferenceInterface tfHelper;

    private String name;
    private String inputName;
    private String outputName;
    private int inputSize;
    private boolean feedKeepProb;

    private List<String> labels;
    private float[] output;
    private String[] outputNames;

    private String KEEP_PROP_INPUT = "keep_prob";

    /**
     * given a saved drawn model, lets read all the classification labels that are
     * stored and write them to our in memory labels list
     * @param am asset manager
     * @param fileName name of the file
     * @return list of labels
     * @throws IOException
     */
    private static List<String> readLabels(AssetManager am, String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(am.open(fileName)));

        String line;
        List<String> labels = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            labels.add(line);
        }

        br.close();
        return labels;
    }

    /**
     *
     * given a model, its label file, and its metadata
     * fill out a classifier object with all the necessary
     * metadata including output prediction
     * @param assetManager asset manager
     * @param name name for the model
     * @param modelPath path of the model
     * @param labelFile path of the labels
     * @param inputSize size of the input layer
     * @param inputName name of the input
     * @param outputName name of the output
     * @param feedKeepProb not sure
     * @return the created Tensorflow Classifier
     * @throws IOException
     */
    public static TensorFlowClassifier create(AssetManager assetManager, String name,
                                              String modelPath, String labelFile, int inputSize, String inputName, String outputName,
                                              boolean feedKeepProb) throws IOException {
        //intialize a classifier
        TensorFlowClassifier c = new TensorFlowClassifier();

        //store its name, input and output labels
        c.name = name;

        c.inputName = inputName;
        c.outputName = outputName;

        //read labels for label file
        c.labels = readLabels(assetManager, labelFile);

        //set its model path and where the raw asset files are
        c.tfHelper = new TensorFlowInferenceInterface(assetManager, modelPath);
        int numClasses = 10;

        //how big is the input?
        c.inputSize = inputSize;

        // Pre-allocate buffer.
        c.outputNames = new String[] { outputName };

        c.outputName = outputName;
        c.output = new float[numClasses];

        c.feedKeepProb = feedKeepProb;

        return c;
    }

    /**
     * Propergates the input through the model and searches the node with the highest conf
     * @param pixels convolution of the drawen image
     * @return a Classification
     */
    public Classification recognize(final float[] pixels) {

        //using the interface
        //give it the input name, raw pixels from the drawing,
        //input size
        tfHelper.feed(inputName, pixels, 1, inputSize, inputSize, 1);

        //probabilities
        if (feedKeepProb) {
            tfHelper.feed(KEEP_PROP_INPUT, new float[] { 1 });
        }
        //get the possible outputs
        tfHelper.run(outputNames);

        //get the output
        tfHelper.fetch(outputName, output);

        // Find the best classification
        //for each output prediction
        //if its above the threshold for accuracy we predefined
        //write it out to the view
        Classification ans = new Classification();
        for (int i = 0; i < output.length; ++i) {
            if (output[i] > ans.getConf()) {
                ans.update(output[i], labels.get(i));
            }
        }

        return ans;
    }
}
