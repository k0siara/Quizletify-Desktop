package com.patrykkosieradzki.quizletify.ui;

import com.patrykkosieradzki.quizletify.TextDocument;
import com.patrykkosieradzki.quizletify.api.google.GoogleDetector;
import com.patrykkosieradzki.quizletify.api.google.Language;
import com.patrykkosieradzki.quizletify.api.quizlet.QuizletException;
import com.patrykkosieradzki.quizletify.api.quizlet.QuizletSet;
import com.patrykkosieradzki.quizletify.api.quizlet.QuizletUser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class WordsCheckBoxList implements MouseListener, ActionListener {

    private Language[] languages;

    private JFrame frame;
    private JPanel panel;

    private GridBagConstraints cs;
    private Font font;

    private JTextField textTitle;
    private JTextArea textDescription;

    private JComboBox termsLanguageChooser;
    private JComboBox definitionsLanguageChooser;

    private TextDocument document;
    private QuizletUser user;

    private JList<CheckBoxListItem> list;
    private DefaultListModel<CheckBoxListItem> words;

    WordsCheckBoxList(TextDocument document, QuizletUser user) {
        this.document = document;
        this.languages = Language.getLanguages();
        this.user = user;

        font = new Font("SansSerif", Font.PLAIN, 14);

        init();
    }

    private void init() {
        prepareFrame();
        prepareList();
        loadList();
        loadThings();
        setFrame();
    }

    private void prepareFrame() {
        frame = new JFrame();
        frame.setMinimumSize(new Dimension(300, 400));
    }

    private void prepareList() {
        words = new DefaultListModel<CheckBoxListItem>();
        list = new JList<CheckBoxListItem>(words);
        list.setCellRenderer(new CheckboxListRenderer());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        list.addMouseListener(this);
    }

    private void loadList() {
        for (int i = 0; i < document.getWords().size(); i++) {
            words.addElement(new CheckBoxListItem(document.getWords().get(i)));
            words.get(i).setSelected(true);
        }
    }

    private void loadThings() {
        textTitle = new JTextField(30);
        frame.add(textTitle);
    }

    private void setFrame() {
        panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        setGridBagConstraints();
        setName();
        setDescription();
        setTermsAndDefinitionsLanguages();
        setButtons();

        frame.getContentPane().add(new JScrollPane(list), BorderLayout.WEST);
        frame.getContentPane().add(panel, BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void setGridBagConstraints() {
        cs = new GridBagConstraints();
        cs.fill = GridBagConstraints.HORIZONTAL;
    }

    private void setName() {
        JLabel labelTitle = new JLabel("Name: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(labelTitle, cs);

        textTitle = new JTextField(30);
        textTitle.setFont(font);
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(textTitle, cs);

        labelTitle.setBorder(new EmptyBorder(5, 5, 5, 5));
    }

    private void setDescription() {
        JLabel labelDescription = new JLabel("Description: ");
        cs.gridx = 0;
        cs.gridy = 2;
        cs.gridwidth = 1;
        panel.add(labelDescription, cs);

        textDescription = new JTextArea();
        textDescription.setFont(font);
        cs.gridx = 0;
        cs.gridy = 3;
        cs.gridwidth = 2;
        panel.add(textDescription, cs);

        labelDescription.setBorder(new EmptyBorder(5, 5, 5, 5));
    }

    private void setTermsAndDefinitionsLanguages() {
        JLabel labelTermslanguage = new JLabel("Terms language: ");
        cs.gridx = 0;
        cs.gridy = 4;
        cs.gridwidth = 1;
        panel.add(labelTermslanguage, cs);

        termsLanguageChooser = new JComboBox();
        for (Language language : languages) {
            termsLanguageChooser.addItem(language);
        }
        cs.gridx = 1;
        cs.gridy = 4;
        cs.gridwidth = 1;
        panel.add(termsLanguageChooser, cs);
        termsLanguageChooser.setSelectedItem(GoogleDetector.languageFromText(words.get(0).getWord()));

        JLabel labelDefinitionsLanguage = new JLabel("Definitions language: ");
        cs.gridx = 0;
        cs.gridy = 5;
        cs.gridwidth = 1;
        panel.add(labelDefinitionsLanguage, cs);

        definitionsLanguageChooser = new JComboBox();
        for (Language language : languages) {
            definitionsLanguageChooser.addItem(language);
        }
        cs.gridx = 1;
        cs.gridy = 5;
        cs.gridwidth = 2;
        panel.add(definitionsLanguageChooser, cs);
        definitionsLanguageChooser.setSelectedItem(Language.POLISH);

        labelTermslanguage.setBorder(new EmptyBorder(5, 5, 5, 5));
        labelDefinitionsLanguage.setBorder(new EmptyBorder(5, 5, 5, 5));
    }

    private void setButtons() {
        JButton okButton = new JButton("OK");
        okButton.setBorder(new EmptyBorder(10, 10, 10, 10));
        okButton.addActionListener(this);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBorder(new EmptyBorder(10, 10, 10, 10));
        cancelButton.addActionListener(this);

        JPanel bp = new JPanel();
        bp.add(okButton);
        bp.add(cancelButton);

        cs.gridx = 0;
        cs.gridy = 6;
        cs.gridwidth = 2;
        panel.add(bp, cs);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JList<CheckBoxListItem> list =
                (JList<CheckBoxListItem>) e.getSource();

        int index = list.locationToIndex(e.getPoint());
        CheckBoxListItem item = list.getModel()
                .getElementAt(index);

        item.setSelected(!item.isSelected());
        list.repaint(list.getCellBounds(index, index));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("OK")) {
            try {
                uploadSet();
                showMessageDialog("Upload complete");
            } catch (QuizletException e2) {
                showMessageDialog(e2.getMessage());
            }
        } else if (command.equals("Cancel")) {
            cancel();
        }
    }

    private void uploadSet() throws QuizletException {
        String title = textTitle.getText();
        String description = textDescription.getText();

        Language termsLang = Language.fromString(termsLanguageChooser.getSelectedItem().toString());
        Language definitionsLang = Language.fromString(definitionsLanguageChooser.getSelectedItem().toString());

        ArrayList<String> terms = new ArrayList<String>();
        ArrayList<String> definitions = new ArrayList<String>();
        for(int i = 0; i < words.size(); i++) {
            if (words.get(i).isSelected()) {
                terms.add(words.get(i).getWord());
                definitions.add(words.get(i).getTranslation());
            }
        }

        user.addSet(new QuizletSet(title, description, termsLang, definitionsLang, terms, definitions));
    }

    private void showMessageDialog(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    private void cancel() {
        dispose();
    }

    private void dispose() {
        frame.dispose();
    }

    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

}
