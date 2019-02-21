package com.example.manuel.a1x1trainer.Ressources;

public class RuntimeConstants {
    public static final boolean RUNNING_LIVE = false;

    /***
     * LOGIN
     */
    public static final String USERMANAGER_URL = RUNNING_LIVE ?
            "https://schule.learninglab.tugraz.at/usermanager/soap" :
            "https://schule-dev.tugraz.at/usermanager/soap";
    public static final Integer APPLICATION_ID = 44;
    public static final String APPLICATION_KEY_SECRET = "5ecbd0b247e7e38b252442a9b3f3cdd7";
    public static final Integer NETWORK = 0;

    public static final Integer MAX_USERNAME_INPUT_STRING_LENGTH = 64;
    public static final Integer MAX_PASSWORD_INPUT_STRING_LENGTH = 64;

    /***
     * TENSORFLOW
     */
    public static final String MODEL_LABELS_FILENAME = "labels.txt";
    public static final String TENSORFLOW_MODEL_FILENAME = "opt_mnist_convnet.pb";
    public static final String MODEL_NAME = "Tensorflow";
    public static final String MODEL_INPUT_NAME = "input";
    public static final String MODEL_OUTPUT_NAME = "output";
    public static final int MODEL_INPUT_DIM_SIZE = 28;
    public static final String ON_MODEL_LOAD_ERROR_RUNTIME_EXCEPTION =
            "Error initializing classifiers!";

    /***
     * IN GAME
     */
    public static final Integer TIME_TO_SOLVE = 60;
    public static final Integer MAX_NUMBER_OF_QUESTIONS = 2;
    public static final Integer PROGRESS_FACTOR = 50;
    public static final Integer OVERLAY_ANIMATION_DELAY = 1000;
    public static final Integer OVERLAY_VISIBLE_DELAY = 1000;



    /***
     * KURZSPIEL
     */
    public static final Integer WORM_ANIMATION_FPS = 24;
    public static final Integer WORM_ANIMATION_TIME = 3500;

    public static final Integer MAX_POINTS_PER_QUESTION = 500;
}
