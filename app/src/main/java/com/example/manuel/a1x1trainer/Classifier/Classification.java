package com.example.manuel.a1x1trainer.Classifier;

/**
 * Classification
 *
 * Represents a classification from the model
 */
public class Classification {
    //conf is the output
    private float conf;
    //input label
    private String label;

    Classification() {
        this.conf = -1.0F;
        this.label = null;
    }

    /**
     * updates the properties
     * @param conf new conf
     * @param label new label
     */
    void update(float conf, String label) {
        this.conf = conf;
        this.label = label;
    }

    /**
     * Getter for the label
     * @return label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Getter for the conf
     * @return conf
     */
    public float getConf() {
        return conf;
    }
}
