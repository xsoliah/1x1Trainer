package com.example.manuel.a1x1trainer.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.manuel.a1x1trainer.R;
import com.example.manuel.a1x1trainer.Ressources.Game;
import com.example.manuel.a1x1trainer.Ressources.GameMode;
import com.example.manuel.a1x1trainer.Ressources.HighscoreEntry;
import com.example.manuel.a1x1trainer.Ressources.Question;
import com.google.gson.Gson;

import java.util.List;
import java.util.UUID;

/**
 * Post Quiz Activity
 *
 * Post-game activity, that should show an overview of the results.
 * Furthermore it should be possible to navigate to Quiz Result Activity to get a detailed
 * overview of the results.
 * Before launching the scoreboard the user also has to insert user-credentials
 * for a highscore entry.
 */
public class PostQuizActivity extends KonfettiBackgroundActivity {
    private TextView solvedQuestionsOfTotalQuestionsText;
    private TextView pointsScored;
    private Button openResultButton;
    private Button checkButton;
    private GameMode gameMode;
    private EditText scoreboardNameEdit;
    private Game game;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            gameMode = null;
        } else {
            gameMode = GameMode.valueOf(extras.getString(getString(R.string.intent_extra_game_mode)));
            Gson g = new Gson();
            game = g.fromJson(extras.getString(getString(R.string.intent_extra_game)), Game.class);
        }

        sharedPreferences = getSharedPreferences(getString(R.string.shared_preferences_highscore), MODE_PRIVATE);

        setContentView(R.layout.activity_post_quiz_);

        solvedQuestionsOfTotalQuestionsText = findViewById(R.id.postquiz_solved_questions);
        pointsScored = findViewById(R.id.postquiz_score);
        openResultButton = findViewById(R.id.postquiz_result_btn);
        checkButton = findViewById(R.id.ppostquiz_check_btn);
        scoreboardNameEdit = findViewById(R.id.postquiz_name_input);
        viewKonfetti = findViewById(R.id.postquiz_view_konfetti);

        String solvedTasksString = new String(getSolvedTasksString() + " / " + game.getCurrentNumberOfQuestions());
        solvedQuestionsOfTotalQuestionsText.setText(solvedTasksString);
        pointsScored.setText(game.getScore().toString());

        disableButton(checkButton);

        // EDIT Event
        scoreboardNameEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (scoreboardNameEdit.getText().length() > 0)
                    enableButton(checkButton);
                else
                    disableButton(checkButton);
                return false;
            }
        });

        // RESULT Button
        openResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show scoreboard
                Intent intent = new Intent(PostQuizActivity.this, QuizResultActivity.class);
                Gson g = new Gson();
                intent.putExtra(getString(R.string.intent_extra_game), g.toJson(game));
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

        // CHECK Button
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add entry to bestenliste
                // create new entry for highscores
                HighscoreEntry new_highscore_entry = new HighscoreEntry(scoreboardNameEdit.getText().toString(), gameMode, game.getScore());

                // generate unique identifier
                UUID guid = UUID.randomUUID();
                while (sharedPreferences.contains(guid.toString()))
                    guid = UUID.randomUUID();

                // add entry to highscores
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(guid.toString(), new_highscore_entry.marshal());
                editor.commit();

                // show scoreboard and finish this activity
                Intent intent = new Intent(PostQuizActivity.this, ScoreboardActivity.class);
                intent.putExtra(getString(R.string.intent_extra_game_mode), gameMode.toString());
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });
    }

    /**
     * counts the right answers and returns a string representation
     * @return
     */
    private String getSolvedTasksString() {
        // no lambda in java :(
        List<Question> questions = game.getQuestions();
        int solved = 0;
        for (Question question : questions) {
            if (question.isRightUserAnswer())
                solved++;
        }
        return String.valueOf(solved);
    }
}
