package com.example.manuel.a1x1trainer.Activities;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.manuel.a1x1trainer.Exceptions.GameModeNotPresentException;
import com.example.manuel.a1x1trainer.R;
import com.example.manuel.a1x1trainer.Ressources.HighscoreEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import static android.util.TypedValue.COMPLEX_UNIT_PX;

public class ScoreboardActivity extends GameModeActivity {
    private TableRow scoreboardTableHeader;
    private TableLayout scoreboardInnerTable;
    private Button scoreboardBackButton;
    private SharedPreferences sharedPreferences;
    private TextView sampleTableTextView;

    private ArrayList<HighscoreEntry> loadScoreBoard() {
        ArrayList<HighscoreEntry> scoreBoard = new ArrayList<>();

        sharedPreferences = getSharedPreferences(getString(R.string.shared_preferences_highscore), MODE_PRIVATE);
        Map<String, String> highscores = (Map<String, String>) sharedPreferences.getAll();
        for (Map.Entry<String,String> entry: highscores.entrySet()) {
            HighscoreEntry highscoreEntry = HighscoreEntry.unmarshal(entry.getValue());
            if (highscoreEntry.getGameMode().equals(gameMode))
                scoreBoard.add(highscoreEntry);
        }
        return scoreBoard;
    }

    private ArrayList<HighscoreEntry> sort(ArrayList<HighscoreEntry> unsorted) {
        Collections.sort(unsorted, new Comparator<HighscoreEntry>() {
            @Override
            public int compare(HighscoreEntry o1, HighscoreEntry o2) {
                return o1.getScore() > o2.getScore() ? -1 : o1.getScore() == o2.getScore() ? 0 : 1;
            }
        });
        return unsorted;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void renderScoreBoard() {
        ArrayList<HighscoreEntry> scoreBoard = sort(loadScoreBoard());

        for (HighscoreEntry entry : scoreBoard) {
            TableRow newTableRow = new TableRow(this);
            newTableRow.setLayoutParams(scoreboardTableHeader.getLayoutParams());

            // generate name
            TextView newName = new TextView(this);
            newName.setText(entry.getName());
            newName.setLayoutParams(sampleTableTextView.getLayoutParams());
            newName.setTextColor(getColor(R.color.black));
            newName.setGravity(sampleTableTextView.getGravity());
            newName.setTextSize(COMPLEX_UNIT_PX, sampleTableTextView.getTextSize());

            // generate score
            TextView newScore = new TextView(this);
            newScore.setText(entry.getScore().toString());
            newScore.setLayoutParams(sampleTableTextView.getLayoutParams());
            newScore.setGravity(sampleTableTextView.getGravity());
            newScore.setTextColor(getColor(R.color.black));
            newScore.setTextSize(COMPLEX_UNIT_PX ,sampleTableTextView.getTextSize());

            // set parents
            newTableRow.addView(newName);
            newTableRow.addView(newScore);
            scoreboardInnerTable.addView(newTableRow);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            getGameMode();
        } catch (GameModeNotPresentException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_scoreboard);

        scoreboardBackButton = findViewById(R.id.scoreboard_back_btn);
        scoreboardTableHeader = findViewById(R.id.scoreboard_table_header_row);
        sampleTableTextView = findViewById(R.id.scoreboard_table_header_name);
        scoreboardInnerTable = findViewById(R.id.scoreboard_inner_table);

        renderScoreBoard();

        scoreboardBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0, 0);
            }
        });
    }
}
