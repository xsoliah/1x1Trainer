package com.example.manuel.a1x1trainer.Ressources;

/**
 * Question
 *
 * Represents a quiz-question
 */
public class Question {
    private Integer id;
    private String label;
    private Integer answer;
    private String userAnswer;

    public Question(Integer _id, String _label, Integer _answer)
    {
        id = _id;
        label = _label;
        answer = _answer;
        userAnswer = "";
    }

    public Question(String _label, Integer _answer)
    {
        label = _label;
        answer = _answer;
        userAnswer = "";
    }

    /**
     * Getter for the label
     * @return label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the user answer
     * @param user_answer provided user-answer
     */
    public void setUserAnswer(String user_answer) {
        userAnswer = user_answer.length() == 2 && user_answer.startsWith("0") ? user_answer.substring(1) : user_answer;
    }

    /**
     * Checks if the user's answer was right
     * @return true if right answer
     */
    public boolean isRightUserAnswer() {
        return userAnswer.equals(answer.toString());
    }

    /**
     * Getter for the question Id
     * @return question-id
     */
    public Integer getQuestionId() {
        return id;
    }

    /**
     * Gets the answer's string representation
     * @return answer as string
     */
    public String getAnswerString() {
        return answer.toString();
    }

    /**
     * Getter for the user answer
     * @return user-answer
     */
    public String getUserAnswer() {
        return userAnswer;
    }
}
