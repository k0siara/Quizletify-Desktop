package com.patrykkosieradzki.quizletify.ui;

import com.patrykkosieradzki.quizletify.Word;

public class CheckBoxListItem {

    private Word word;

    public CheckBoxListItem(Word word) {
        this.word = word;
    }

    private boolean isSelected = false;


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getWord() {
        return word.getWord();
    }

    public String getTranslation() {
        return word.getTranslation();
    }

}
