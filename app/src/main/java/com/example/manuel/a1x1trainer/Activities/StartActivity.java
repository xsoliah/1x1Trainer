package com.example.manuel.a1x1trainer.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.manuel.a1x1trainer.R;

import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class StartActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

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
                startActivity(new Intent(StartActivity.this, HelpActivity.class));
                overridePendingTransition(0, 0);
            }
        });
    }
}
