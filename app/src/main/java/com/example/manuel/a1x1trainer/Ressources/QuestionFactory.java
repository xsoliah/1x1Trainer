package com.example.manuel.a1x1trainer.Ressources;

import java.util.Random;

/**
 * Question Factory
 *
 * Factory for creating random questions (needed in Kurzspiel)
 */
public class QuestionFactory {
    private static Random r = new Random();
    private static final Integer MAX_DIGIT = 9;
    private static final String QUESTION_STRING_FORMAT = "%s x %s";

    /**
     * Returns a random digit
     * @return random digit
     */
    private static Integer getRandomDigit() {
        return r.nextInt(MAX_DIGIT);
    }

    /**
     * Creates a random question
     * @return created question
     */
    public static Question createQuestion() {
        Integer left_operand = 0;
        Integer right_operand = 0;
        while (left_operand == 0 || right_operand == 0)
        {
            left_operand = getRandomDigit();
            right_operand = getRandomDigit();
        }
        return new Question(getQuestionString(left_operand, right_operand),
                left_operand * right_operand);
    }

    /**
     * Formats the question
     * @param left_operand left factor
     * @param right_operand right factor
     * @return formatted string
     */
    private static String getQuestionString(Integer left_operand, Integer right_operand) {
        return String.format(QUESTION_STRING_FORMAT, left_operand, right_operand);
    }
}
