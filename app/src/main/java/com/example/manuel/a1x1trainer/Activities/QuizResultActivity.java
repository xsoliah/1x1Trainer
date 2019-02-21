package com.example.manuel.a1x1trainer.Activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.manuel.a1x1trainer.R;
import com.example.manuel.a1x1trainer.Ressources.Game;
import com.example.manuel.a1x1trainer.Ressources.Question;
import com.google.gson.Gson;

import java.util.List;

import static android.util.TypedValue.COMPLEX_UNIT_PX;

public class QuizResultActivity extends KonfettiBackgroundActivity {

    private TableLayout gameResultTable;
    private TextView trueSampleNumberCell;
    private TextView trueSampleQuestionCell;
    private TextView trueSampleAnswerCell;
    private TextView falseSampleNumberCell;
    private TextView falseSampleQuestionCell;
    private TextView falseSampleAnswerCell;
    Button resultBackButton;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
        } else {
            Gson g = new Gson();
            game = g.fromJson(extras.getString(getString(R.string.intent_extra_game)), Game.class);
        }

        setContentView(R.layout.activity_quiz_result);

        gameResultTable = findViewById(R.id.result_table);
        trueSampleNumberCell = findViewById(R.id.result_true_sample_num_cell);
        trueSampleQuestionCell = findViewById(R.id.result_true_sample_question_cell);
        trueSampleAnswerCell = findViewById(R.id.result_true_sample_answer_cell);
        falseSampleNumberCell = findViewById(R.id.result_false_sample_num_cell);
        falseSampleQuestionCell = findViewById(R.id.result_false_sample_question_cell);
        falseSampleAnswerCell = findViewById(R.id.result_false_sample_answer_cell);
        resultBackButton = findViewById(R.id.result_back_btn);
        viewKonfetti = findViewById(R.id.result_view_konfetti);

        renderResultTable();

        resultBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0,0);
            }
        });
    }

    private void renderResultTable() {
        String green = "#003300";
        String red = "#b30000";
        List<Question> questions = game.getQuestions();
        Integer counter = 1;
        for (Question question : questions) {
            TableRow newTableRow = new TableRow(this);

            // generate number
            TextView newNumber = new TextView(this);
            newNumber.setText(counter.toString().concat("."));
            newNumber.setLayoutParams(question.isRightUserAnswer() ? trueSampleNumberCell.getLayoutParams() : falseSampleNumberCell.getLayoutParams());
            newNumber.setGravity(question.isRightUserAnswer() ? trueSampleNumberCell.getGravity() : falseSampleNumberCell.getGravity());
            newNumber.setTextColor(Color.parseColor(getString(R.string.color_black)));
            newNumber.setTextSize(COMPLEX_UNIT_PX , question.isRightUserAnswer() ? trueSampleNumberCell.getTextSize() : falseSampleNumberCell.getTextSize());

            // generate question
            TextView newQuestion = new TextView(this);
            newQuestion.setText(question.getLabel().concat(" = ").concat(question.getAnswerString()));
            newQuestion.setLayoutParams(question.isRightUserAnswer() ? trueSampleQuestionCell.getLayoutParams() : falseSampleQuestionCell.getLayoutParams());
            newQuestion.setTextColor(Color.parseColor(getString(R.string.color_black)));
            newQuestion.setGravity(question.isRightUserAnswer() ? trueSampleQuestionCell.getGravity() : falseSampleQuestionCell.getGravity());
            newQuestion.setTextSize(COMPLEX_UNIT_PX, question.isRightUserAnswer() ? trueSampleQuestionCell.getTextSize() : falseSampleQuestionCell.getTextSize());

            // generate answer
            TextView newAnswer = new TextView(this);
            newAnswer.setText(question.getAnswerString());
            newAnswer.setLayoutParams(question.isRightUserAnswer() ? trueSampleAnswerCell.getLayoutParams() : falseSampleAnswerCell.getLayoutParams());
            newAnswer.setTextColor(Color.parseColor(question.isRightUserAnswer() ? green : red));
            newAnswer.setGravity(question.isRightUserAnswer() ? trueSampleAnswerCell.getGravity() : falseSampleAnswerCell.getGravity());
            newAnswer.setTextSize(COMPLEX_UNIT_PX, question.isRightUserAnswer() ? trueSampleAnswerCell.getTextSize() : falseSampleAnswerCell.getTextSize());

            // set parents
            newTableRow.addView(newNumber);
            newTableRow.addView(newQuestion);
            newTableRow.addView(newAnswer);
            gameResultTable.addView(newTableRow);

            counter++;
        }
    }
}
