package com.example.manuel.a1x1trainer.Activities;

// TODO: check if this is neccessary
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.manuel.a1x1trainer.AppServices.AppService;
import com.example.manuel.a1x1trainer.AppServices.GetQuestionService;
import com.example.manuel.a1x1trainer.AppServices.GetSessionService;
import com.example.manuel.a1x1trainer.AppServices.GiveAnswerService;
import com.example.manuel.a1x1trainer.Classifier.ClassificationResultPaintViewIdentifier;
import com.example.manuel.a1x1trainer.Drawable.PaintView;
import com.example.manuel.a1x1trainer.Exceptions.GameModeNotPresentException;
import com.example.manuel.a1x1trainer.R;
import com.example.manuel.a1x1trainer.Ressources.Game;
import com.example.manuel.a1x1trainer.Ressources.GameMode;
import com.example.manuel.a1x1trainer.Ressources.Question;
import com.example.manuel.a1x1trainer.Ressources.QuestionFactory;
import com.example.manuel.a1x1trainer.Ressources.RuntimeConstants;
import com.example.manuel.a1x1trainer.Ressources.UserCredentials;
import com.google.gson.Gson;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Quiz Activity
 *
 * Controlls the whole game until finished or aborted
 */
public class QuizActivity extends ClassificationReceiverActivity {
    private PaintView leftPaintEdit;
    private PaintView rightPaintEdit;
    private Button clearButton;
    private Button okButton;
    private Button quizBackButton;
    private EinmalEinsApiWebService getQuestionService;
    private EinmalEinsApiWebService getSessionService;
    private EinmalEinsApiWebService giveAnswerService;
    private TextView questionTextView;
    private TextView answerTextView;
    private String currentAnswerLeft;
    private String currentAnswerRight;
    private CountDownTimer countDownTimer;
    private TextView countdownTextView;
    private Game game;
    private ProgressBar progressBar;
    private ProgressBar questionLoadingSpinner;
    private ImageView feedbackPopup;
    private TextView feedbackPopupText;
    private TextView score;
    private ImageView progressWorm;

    private boolean countdownRunning;
    private boolean firstCountdown = true;

    private long timeRemaining;

    /**
     * refreshes the answer in the view after a classification
     */
    private void refreshAnswer() {
        String toSet = currentAnswerLeft.concat(currentAnswerRight);
        // TODO: check neccessary??
        if (toSet.length() > 0)
            enableButton(okButton);
        answerTextView.setText(toSet);
    }

    /**
     * handles the classification result
     * @param s classified digit as string
     * @param identifier classification identifier
     */
    public void returnClassificationResult(String s, ClassificationResultPaintViewIdentifier identifier) {
        if (identifier == ClassificationResultPaintViewIdentifier.NOT_IMPORTANT)
            return;
        else if (identifier == ClassificationResultPaintViewIdentifier.LEFT)
            currentAnswerLeft = s;
        else if (identifier == ClassificationResultPaintViewIdentifier.RIGHT)
            currentAnswerRight = s;

        this.refreshAnswer();
        return;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            getGameMode();
        } catch (GameModeNotPresentException e) {
            e.printStackTrace();
            // TODO: fatal exception --> ??
        }

        currentAnswerLeft = new String();
        currentAnswerRight = new String();
        countdownRunning = false;

        setContentView(R.layout.activity_quiz);

        game = new Game();

        leftPaintEdit = findViewById(R.id.quiz_left_paint_edit);
        rightPaintEdit = findViewById(R.id.quiz_right_paint_edit);
        clearButton = findViewById(R.id.quiz_clear_btn);
        okButton = findViewById(R.id.quiz_ok_btn);
        quizBackButton = findViewById(R.id.quiz_back_btn);
        questionTextView = findViewById(R.id.quiz_question);
        answerTextView = findViewById(R.id.quiz_answer);
        countdownTextView = findViewById(R.id.quiz_countdown);
        progressBar = findViewById(R.id.quiz_progressbar);
        progressBar.setMax((RuntimeConstants.MAX_NUMBER_OF_QUESTIONS+1) * RuntimeConstants.PROGRESS_FACTOR);
        progressBar.setProgress((game.getCurrentNumberOfQuestions()+1) * RuntimeConstants.PROGRESS_FACTOR);
        questionLoadingSpinner = findViewById(R.id.quiz_loading_spinner);
        feedbackPopup = findViewById(R.id.quiz_feedback_modal);
        feedbackPopupText = findViewById(R.id.quiz_feedback_modal_text);
        score = findViewById(R.id.quiz_score);
        progressWorm = findViewById(R.id.quiz_worm);

        // set back button image according to game-mode
        if (gameMode == GameMode.TRAINING) {
            quizBackButton.setBackgroundResource(R.mipmap.button_back);
        } else {
            quizBackButton.setBackgroundResource(R.mipmap.button_cancel);
        }

        progressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));

        // X Button
        quizBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // game is over --> view results
                UserCredentials.FinishedGame = game;
                Intent intent = new Intent(QuizActivity.this, PostQuizActivity.class);
                intent.putExtra(getString(R.string.intent_extra_game_mode), gameMode.toString());
                Gson g = new Gson();
                intent.putExtra(getString(R.string.intent_extra_game), g.toJson(game));
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });

        // OK Button
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAnswer();
            }
        });

        // LÖSCHEN Button
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftPaintEdit.clearScreen();
                rightPaintEdit.clearScreen();
                currentAnswerLeft = getString(R.string.empty_string);
                currentAnswerRight = getString(R.string.empty_string);
                answerTextView.setText(getString(R.string.empty_string));
                disableButton(okButton);
            }
        });

        if (gameMode.equals(GameMode.TRAINING)) {
            getSessionService = new EinmalEinsApiWebService(new GetSessionService());
            getSessionService.execute();
        }
        else {
            getQuestion();
        }
    }

    /**
     * Checks if the game is over, if yes it gets a new question according to game mode, otherwise
     * navigates to Post Quiz Activity
     */
    private void getQuestion() {
        disableButton(okButton);
        if (!game.isRunning()) {
            // game is over --> view results
            UserCredentials.FinishedGame = game;
            Intent intent = new Intent(QuizActivity.this, PostQuizActivity.class);
            intent.putExtra(getString(R.string.intent_extra_game_mode), gameMode.toString());
            Gson g = new Gson();
            intent.putExtra(getString(R.string.intent_extra_game), g.toJson(game));
            startActivity(intent);
            overridePendingTransition(0,0);
            this.finish();
        }
        else {
            if (gameMode.equals(GameMode.TRAINING)) {
                // get new question
                getQuestionService = new EinmalEinsApiWebService(new GetQuestionService());

                // make loading spinner visible
                questionTextView.setVisibility(View.GONE);
                answerTextView.setVisibility(View.GONE);
                questionLoadingSpinner.setVisibility(View.VISIBLE);

                getQuestionService.execute();
            }
            else {
                UserCredentials.CurrentQuestion = QuestionFactory.createQuestion();
                viewQuestion(UserCredentials.CurrentQuestion);
            }
        }
    }

    /**
     * Binds the current question to the view
     * @param question
     */
    private void viewQuestion(Question question) {
        // view actions
        questionTextView.setText(question.getLabel().concat(" ="));
        currentAnswerLeft = getString(R.string.empty_string);
        currentAnswerRight = getString(R.string.empty_string);
        answerTextView.setText(getString(R.string.empty_string));

        // make question and answer visible
        questionTextView.setVisibility(View.VISIBLE);
        answerTextView.setVisibility(View.VISIBLE);

        // restart clock
        if (firstCountdown) {
            firstCountdown = false;
            restartCountDown();
        }
    }

    /**
     * submits an answer according to the game mode
     */
    private void submitAnswer() {
        // cancel timer
        if (countdownRunning) {
            countDownTimer.cancel();
            countdownRunning = false;
        }

        // calculate reactionTime
        int reactionTimeInSeconds = RuntimeConstants.TIME_TO_SOLVE - Integer.parseInt(countdownTextView.getText().toString());

        String answer = answerTextView.getText().toString();
        UserCredentials.CurrentQuestion.setUserAnswer(answer);

        if (gameMode.equals(GameMode.TRAINING)) {
            giveAnswerService = new EinmalEinsApiWebService(new GiveAnswerService(answer, UserCredentials.CurrentQuestion.getQuestionId(), reactionTimeInSeconds, UserCredentials.SessionId));
            giveAnswerService.execute();
        }
        else {
            evaluateUserAnswer();
        }
    }

    /**
     * Views the feedback modal
     * Should tell the user if his answer was right or wrong
     * @param success true if successful
     */
    public void viewFeedbackModal(boolean success) {
        feedbackPopupText.setTextColor(success ? getResources().getColor(R.color.green)
                : getResources().getColor(R.color.red));
        String text = success ? getString(R.string.quiz_feedback_true) : getString(R.string.quiz_feedback_false);
        feedbackPopupText.setText(text);
        feedbackPopup.setVisibility(View.VISIBLE);
        feedbackPopupText.setVisibility(View.VISIBLE);
        final CountDownTimer visibilityCountdown = new CountDownTimer(
                RuntimeConstants.OVERLAY_VISIBLE_DELAY,
                RuntimeConstants.OVERLAY_VISIBLE_DELAY) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                feedbackPopup.animate()
                        .alpha(0.0f)
                        .setDuration(RuntimeConstants.OVERLAY_ANIMATION_DELAY)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                feedbackPopup.setVisibility(View.INVISIBLE);
                            }
                        });
                feedbackPopupText.animate()
                        .alpha(0.0f)
                        .setDuration(RuntimeConstants.OVERLAY_ANIMATION_DELAY)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                feedbackPopupText.setVisibility(View.INVISIBLE);
                                feedbackPopupText.setText(getString(R.string.empty_string));
                                restartCountDown();
                            }
                        });
            }
        }.start();
    }

    private final Integer ONE_MINUTE = 600000;
    private final Integer THOUSAND = 1000;
    private final long INTERVAL = 10;

    /**
     * evaluates the user answer and performs following actions
     *      * in the end getQuestion is called to request a new question
     */
    private void evaluateUserAnswer() {
        boolean success = UserCredentials.CurrentQuestion.isRightUserAnswer();

        // open feedback modal
        feedbackPopup.setAlpha(RuntimeConstants.ENABLED_ALPHA);
        feedbackPopupText.setAlpha(RuntimeConstants.ENABLED_ALPHA);
        viewFeedbackModal(success);

        // check if user answer was right
        if (success) {
            double secondsRemaining = (timeRemaining % ONE_MINUTE / THOUSAND);
            final double score = ((secondsRemaining / (double) RuntimeConstants.TIME_TO_SOLVE) *
                    (double) RuntimeConstants.MAX_POINTS_PER_QUESTION) / 2;

            // increase points animation
            final CountDownTimer increasePointsCountdown = new CountDownTimer((long)score, INTERVAL) {
                @Override
                public void onTick(long millisUntilFinished) {
                    float fg_pc = (float)(score-millisUntilFinished)/(float)score;
                    QuizActivity.this.score.setText(String.valueOf(game.getScore() + (int)(fg_pc * score)));
                }

                @Override
                public void onFinish() {
                    game.addPointsToScore((int)score);
                    QuizActivity.this.score.setText(String.valueOf(game.getScore()));
                }
            }.start();
        }

        // add answered question to store
        game.addQuestion(UserCredentials.CurrentQuestion);

        // clear screens
        leftPaintEdit.clearScreen();
        rightPaintEdit.clearScreen();

        // reset progress
        ObjectAnimator oa;
        oa = ObjectAnimator.ofInt(progressBar, getString(R.string.property_name_activity_progress),
                progressBar.getProgress(),
                (game.getCurrentNumberOfQuestions()+1)* RuntimeConstants.PROGRESS_FACTOR);
        oa.setDuration(1000);
        ObjectAnimator oa1;
        // reset progress worm
        float progress_pc = (float)game.getCurrentNumberOfQuestions() / (float)(RuntimeConstants.MAX_NUMBER_OF_QUESTIONS+1);
        float progress_x = progressBar.getX() + (progressBar.getWidth() * progress_pc);
        oa1 = ObjectAnimator.ofFloat(progressWorm, "x", progressWorm.getX(), progress_x);
        oa1.setDuration(1000);
        oa.start();
        oa1.start();

        // get new question
        getQuestion();
    }

    /**
     * Restarts the countdown
     */
    private void restartCountDown() {
        int millisRemaining = RuntimeConstants.TIME_TO_SOLVE*1000;
        countdownRunning = true;
        countDownTimer = new CountDownTimer(millisRemaining, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining = millisUntilFinished;
                Integer seconds = (int)(millisUntilFinished % (RuntimeConstants.TIME_TO_SOLVE * 10000) / 1000);
                countdownTextView.setText(seconds.toString());
            }

            @Override
            public void onFinish() {
                // TODO: what if time runs out
                countdownRunning = false;
                evaluateUserAnswer();
            }
        }.start();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // set size of paintView
        double height = leftPaintEdit.getHeight();
        leftPaintEdit.init((int)height, (int)height, this, ClassificationResultPaintViewIdentifier.LEFT);
        rightPaintEdit.init((int)height, (int)height, this, ClassificationResultPaintViewIdentifier.RIGHT);

        int[] coordinates = new int[2];
        progressBar.getLocationOnScreen(coordinates);

        progressWorm.setX(progressBar.getX());

        loadModel();
    }

    /**
     * Einmaleins API Web-Service
     *
     * Internal class to call a specific Webservice from the Quiz Activity
     */
    class EinmalEinsApiWebService extends AsyncTask<Void, Void, SoapObject> {
        private com.example.manuel.a1x1trainer.AppServices.AppService AppService;

        public EinmalEinsApiWebService(AppService appService) {
            this.AppService = appService;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(SoapObject response) {
            if (response.getName().equals(getString(R.string.param_get_session_response)))
            {
                Integer sessionId = (Integer)response.getProperty(0);
                if (sessionId != null)
                {
                    UserCredentials.SessionId = sessionId;
                    getQuestion();
                }
                else
                {
                    // TODO: game without session?
                }
            }
            else if (response.getName().equals(getString(R.string.param_get_question_response)))
            {
                SoapObject getQuestionReturn = (SoapObject)response.getProperty(0);
                if (getQuestionReturn != null){
                    // set timer to 60 to avoid never seeing 60
                    countdownTextView.setText(RuntimeConstants.TIME_TO_SOLVE.toString());

                    Integer answer = (Integer)getQuestionReturn.getProperty(getString(R.string.get_question_response_prop_answer));
                    Integer questionId = (Integer)getQuestionReturn.getProperty(getString(R.string.get_question_response_prop_id));
                    String questionLabel = (String)getQuestionReturn.getProperty(getString(R.string.get_question_response_prop_label));
                    UserCredentials.CurrentQuestion = new Question(questionId, questionLabel, answer);

                    // make spinner gone
                    questionLoadingSpinner.setVisibility(View.GONE);

                    viewQuestion(UserCredentials.CurrentQuestion);
                }
            }
            else if (response.getName().equals(getString(R.string.param_give_answer_response)))
            {
                // TODO: check if return value is unneccessary
                evaluateUserAnswer();
            }
        }


        @Override
        protected SoapObject doInBackground(Void... voids) {
            SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);

            // TODO: check if necessary
            envelope.implicitTypes = true;
            envelope.setAddAdornments(false);

            envelope.setOutputSoapObject(this.AppService.createSoapObject());

            HttpTransportSE httpTransportSE = new HttpTransportSE(this.AppService.getURL());
            try {
                httpTransportSE.call(this.AppService.getSoapAction(), envelope);
                SoapObject response = (SoapObject)envelope.bodyIn;
                return response;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
