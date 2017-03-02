package com.patrykkosieradzki.quizletify.api.quizlet;

public class QuizletException extends Exception {

    public QuizletException() {
        super();
    }
    public QuizletException(String message) {
        super(message);
    }
    public QuizletException(String message, Throwable cause) {
        super(message, cause);
    }
    public QuizletException(Throwable cause) {
        super(cause);

    }

}
