package com.example.manuel.a1x1trainer.Ressources;

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
        userAnswer = new String();
    }

    public Question(String _label, Integer _answer)
    {
        label = _label;
        answer = _answer;
        userAnswer = new String();
    }

    public String getLabel() {
        return label;
    }

    public void setUserAnswer(String user_answer) {
        userAnswer = user_answer;
    }

    public boolean isRightUserAnswer() {
        return userAnswer.equals(answer.toString());
    }

    public Integer getQuestionId() {
        return id;
    }

    public String getAnswerString() {
        return answer.toString();
    }

    public String getUserAnswer() {
        return userAnswer;
    }
}
