package com.patrykkosieradzki.quizletify.ui;

import javax.swing.*;
import java.awt.*;

public class CheckboxListRenderer extends JCheckBox implements
        ListCellRenderer<CheckBoxListItem> {

    @Override
    public Component getListCellRendererComponent(
            JList<? extends CheckBoxListItem> list, CheckBoxListItem value,
            int index, boolean isSelected, boolean cellHasFocus) {

        if (value.isSelected()) {
            setFont(new Font("Arial", Font.BOLD, 14));
        } else {
            setFont(new Font("Arial", Font.PLAIN, 14));

        }

        setBackground(list.getBackground());
        setForeground(list.getForeground());
        setEnabled(list.isEnabled());
        setSelected(value.isSelected());


        setText(value.getWord() + " => " + value.getTranslation());
        return this;
    }

}
