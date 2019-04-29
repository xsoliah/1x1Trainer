package com.example.manuel.a1x1trainer.Ressources;

import com.google.gson.Gson;

/**
 * Highscore Entry
 *
 * Should represent an entry on the scoreboard
 */
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

    /**
     * serializes this
     * @return serialized string
     */
    public String marshal() {
        Gson g = new Gson();
        return g.toJson(this);
    }

    public static HighscoreEntry unmarshal(String marshaled) {
        Gson g = new Gson();
        return g.fromJson(marshaled, HighscoreEntry.class);
    }

    /**
     * Getter for the game mode
     * @return game mode
     */
    public GameMode getGameMode() {
        return gameMode;
    }

    /**
     * Getter for the name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the score
     * @return score
     */
    public Integer getScore() {
        return score;
    }
}
