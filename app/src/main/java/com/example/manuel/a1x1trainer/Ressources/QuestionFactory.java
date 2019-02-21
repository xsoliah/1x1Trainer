package com.example.manuel.a1x1trainer.Ressources;

import java.util.Random;

public class QuestionFactory {
    private static Random r = new Random();
    private static final Integer MAX_DIGIT = 9;
    private static final String QUESTION_STRING_FORMAT = "%s x %s";

    public static Integer getRandomDigit() {
        return r.nextInt(MAX_DIGIT);
    }

    public static Question generateQuestion() {
        Integer left_operand = 0;
        Integer right_operand = 0;
        while (left_operand == 0 || right_operand == 0)
        {
            left_operand = getRandomDigit();
            right_operand = getRandomDigit();
        }
        Question question = new Question(getQuestionString(left_operand, right_operand),
                left_operand * right_operand);
        return question;
    }

    private static String getQuestionString(Integer left_operand, Integer right_operand) {
        return String.format(QUESTION_STRING_FORMAT, left_operand, right_operand);
    }
}
