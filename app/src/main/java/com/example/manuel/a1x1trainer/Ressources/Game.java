package com.example.manuel.a1x1trainer.Ressources;
import java.util.ArrayList;
import java.util.List;

/**
 * Game
 *
 * Represents a quiz game
 */
public class Game {
    // TODO: check if game has something todo with session

    private Integer numberOfQuestions;
    private boolean running;
    private List<Question> questions;
    private Integer score;

    public Game() {
        numberOfQuestions = 0;
        running = true;
        questions = new ArrayList<>();
        score = 0;
    }

    /**
     * adds an answered question to the game
     * sets running to false if the maximum number of questions is reached
     * @param question the question to add
     */
    public void addQuestion(Question question)
    {
        questions.add(question);
        numberOfQuestions++;
        if (numberOfQuestions.equals(RuntimeConstants.MAX_NUMBER_OF_QUESTIONS))
            running = false;
    }

    /**
     * checks if the game is running
     * @return true if running
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Getter for the current number of questions
     * @return number of questions
     */
    public int getCurrentNumberOfQuestions() {
        return numberOfQuestions;
    }

    /**
     * Getter for all current answered questions
     * @return List of answered questions
     */
    public List<Question> getQuestions() {
        return questions;
    }

    /**
     * Getter for the current score
     * @return score
     */
    public Integer getScore() {
        return score;
    }

    /**
     * Adds points to the current points
     * @param points points to add
     */
    public void addPointsToScore(int points) {
        this.score += points;
    }
}
