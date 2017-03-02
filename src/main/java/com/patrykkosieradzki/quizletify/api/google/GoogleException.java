package com.patrykkosieradzki.quizletify.api.google;

public class GoogleException extends Exception {

    public GoogleException() {
        super();
    }

    public GoogleException(String s) {
        super(s);
    }

    public GoogleException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public GoogleException(Throwable throwable) {
        super(throwable);
    }

}
