package com.patrykkosieradzki.quizletify.api.google;

public enum Language {
    POLISH("pl"),
    ENGLISH("en");

    private final String language;

    Language(final String language) {
        this.language = language;
    }

    public static Language fromString(final String language) {
        for (Language lang : values()) {
            if (lang.toString().equals(language)) {
                return lang;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return language;
    }

    public static Language[] getLanguages() {
        return Language.class.getEnumConstants();
    }


}
