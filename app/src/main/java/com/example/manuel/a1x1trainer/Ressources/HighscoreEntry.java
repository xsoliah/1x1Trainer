package com.example.manuel.a1x1trainer.Ressources;

import com.google.gson.Gson;

public class HighscoreEntry {
    private String name;
    private GameMode gameMode;
    private Integer score;

    public HighscoreEntry(String name_, GameMode game_mode, Integer score_)
    {
        name = name_;
        gameMode = game_mode;
        score = score_;
    }

    public String marshal() {
        Gson g = new Gson();
        return g.toJson(this);
    }

    public static HighscoreEntry unmarshal(String marshaled) {
        Gson g = new Gson();
        HighscoreEntry highscoreEntry = g.fromJson(marshaled, HighscoreEntry.class);
        return highscoreEntry;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public String getName() {
        return name;
    }

    public Integer getScore() {
        return score;
    }
}
