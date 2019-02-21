package com.example.manuel.a1x1trainer;

import android.content.Intent;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.manuel.a1x1trainer.Activities.QuizResultActivity;
import com.example.manuel.a1x1trainer.Ressources.Game;
import com.example.manuel.a1x1trainer.Ressources.GameMode;
import com.example.manuel.a1x1trainer.Ressources.Question;
import com.example.manuel.a1x1trainer.Ressources.QuestionFactory;
import com.example.manuel.a1x1trainer.Ressources.RuntimeConstants;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Random;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import okhttp3.internal.Internal;

import static com.example.manuel.a1x1trainer.TestUtils.checkButtonClosesActivity;
import static com.example.manuel.a1x1trainer.TestUtils.checkImageViewVisible;
import static com.example.manuel.a1x1trainer.TestUtils.checkTextViewShows;

@RunWith(AndroidJUnit4.class)
public class QuizResultActivityTest extends ActivityTest {
    private Game sampleGame;

    @Rule
    public IntentsTestRule<QuizResultActivity> mActivityRule =
            new IntentsTestRule<>(QuizResultActivity.class, true, false);

    @Before
    public void initGame() {
        sampleGame = generateRandomGame();
        Intent i = new Intent();
        Gson g = new Gson();
        i.putExtra("GAME", g.toJson(sampleGame));
        mActivityRule.launchActivity(i);
    }

    private Game generateRandomGame() {
        Game game = new Game();
        Random r = new Random();
        for (int i = 0; i < RuntimeConstants.MAX_NUMBER_OF_QUESTIONS; ++i) {
            Question question = QuestionFactory.generateQuestion();
            boolean shouldUserAnswerRandom = r.nextBoolean();
            if (!shouldUserAnswerRandom)
                question.setUserAnswer(question.getAnswerString());
            else
                question.setUserAnswer(Integer.toString(r.nextInt(90)));
            game.addQuestion(question);
        }
        return game;
    }

    @Test
    public void testElements() {
        // title
        checkImageViewVisible(R.id.result_titlebar);
        checkTextViewShows(R.id.result_title_text, R.string.result_titlebar);

        TableLayout table = mActivityRule.getActivity().findViewById(R.id.result_table);
        int num_rows = table.getChildCount();
        for (int i = 2; i < num_rows; ++i) {
            TableRow row = (TableRow) table.getChildAt(i);
            TextView number = (TextView) row.getChildAt(0);
            TextView question = (TextView) row.getChildAt(1);
            TextView answer = (TextView) row.getChildAt(2);

            Question currentSampleQuestion = sampleGame.getQuestions().get(i-2);
            assert number.getText().equals(Integer.toString(i-1).concat("."));
            assert question.getText().equals(currentSampleQuestion.getLabel().concat(" = ").concat(currentSampleQuestion.getAnswerString()));
            assert answer.equals(currentSampleQuestion.getUserAnswer());
            // TODO: check color
        }
    }

    @Test
    public void testBackClick() {
        checkButtonClosesActivity(mActivityRule, R.id.result_back_btn);
    }
}
