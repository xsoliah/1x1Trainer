package com.example.manuel.a1x1trainer.Ressources;
import java.util.ArrayList;
import java.util.List;

public class Game {
    // TODO: check if game has something todo with session

    private Integer numberOfQuestions;
    private boolean running;
    private List<Question> questions;
    private Integer points;

    public Game() {
        numberOfQuestions = 0;
        running = true;
        questions = new ArrayList<Question>();
        points = 0;
    }

    public void addQuestion(Question question)
    {
        questions.add(question);
        numberOfQuestions++;
        if (numberOfQuestions == RuntimeConstants.MAX_NUMBER_OF_QUESTIONS)
            running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public int getCurrentQuestions() {
        return numberOfQuestions;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public Integer getPoints() {
        return points;
    }

    public void addPointsToScore(int score) {
        points += score;
    }
}
