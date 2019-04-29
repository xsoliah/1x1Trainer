package com.example.manuel.a1x1trainer.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manuel.a1x1trainer.R;

/**
 * Start Activity
 *
 * Entry point of the application
 * Should provide a navigation to Kurzspiel Activity and Login Activity.
 * Furthermore a help modal can be toggled.
 */
public class StartActivity extends GameModeActivity {
    ImageView helpModal;
    ImageView tuGrazLogo;
    TextView helpLine1;
    TextView helpLine2;
    TextView helpLine3;
    Button helpBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        helpModal = findViewById(R.id.start_help_modal);
        tuGrazLogo = findViewById(R.id.start_tugraz_logo);
        helpLine1 = findViewById(R.id.start_help_line_1);
        helpLine2 = findViewById(R.id.start_help_line_2);
        helpLine3 = findViewById(R.id.start_help_line_3);
        helpBackButton = findViewById(R.id.start_help_back_btn);

        Button trainerButton = (Button)findViewById(R.id.start_trainer_btn);
        trainerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, LoginActivity.class));
                overridePendingTransition(0, 0);
            }
        });

        Button kurzspielButton = (Button)findViewById(R.id.start_kurzspiel_btn);
        kurzspielButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, KurzspielActivity.class));
                overridePendingTransition(0, 0);
            }
        });

        Button helpButton = (Button)findViewById(R.id.start_help_btn);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helpModal.setVisibility(View.VISIBLE);
                helpLine1.setVisibility(View.VISIBLE);
                helpLine2.setVisibility(View.VISIBLE);
                helpLine3.setVisibility(View.VISIBLE);
                helpBackButton.setVisibility(View.VISIBLE);
                tuGrazLogo.setVisibility(View.VISIBLE);

                helpBackButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        helpModal.setVisibility(View.INVISIBLE);
                        helpLine1.setVisibility(View.INVISIBLE);
                        helpLine2.setVisibility(View.INVISIBLE);
                        helpLine3.setVisibility(View.INVISIBLE);
                        helpBackButton.setVisibility(View.INVISIBLE);
                        tuGrazLogo.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
    }
}
