package com.patrykkosieradzki.quizletify.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class LoginDialog extends JDialog implements ActionListener, WindowListener {

    private Quizletify quizletify;

    private Frame frame;
    private JPanel panel;
    private GridBagConstraints cs;

    private JLabel labelUsername;
    private JTextField textUsername;
    private JLabel labelClientId;
    private JTextField textClientId;
    private JLabel labelSecretKey;
    private JTextField textSecretKey;

    private JButton loginButton;
    private JButton cancelButton;

    public LoginDialog(Quizletify quizletify, Frame frame) {
        super(frame, "Login", true);

        this.quizletify = quizletify;

        this.frame = frame;
        init();
    }

    private void init() {
        preparePanel();
        setThings();
        setDialog();
    }

    private void preparePanel() {
        panel = new JPanel(new GridBagLayout());
        cs = new GridBagConstraints();
        cs.fill = GridBagConstraints.HORIZONTAL;
    }

    private void setThings() {
        labelUsername = new JLabel("Username: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(labelUsername, cs);

        textUsername = new JTextField(30);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(textUsername, cs);

        labelClientId = new JLabel("Client ID: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(labelClientId, cs);

        textClientId = new JTextField(30);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(textClientId, cs);

        labelSecretKey = new JLabel("Secret Key: ");
        cs.gridx = 0;
        cs.gridy = 2;
        cs.gridwidth = 1;
        panel.add(labelSecretKey, cs);

        textSecretKey = new JTextField(30);
        cs.gridx = 1;
        cs.gridy = 2;
        cs.gridwidth = 2;
        panel.add(textSecretKey, cs);

        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);

        JPanel bp = new JPanel();
        bp.add(loginButton);
        bp.add(cancelButton);

        getRootPane().setDefaultButton(loginButton);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);

        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
    }

    private void setDialog() {
        addWindowListener(this);
        setLocationRelativeTo(frame);

        pack();
        setResizable(false);
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    public String getUsername() {
        return textUsername.getText();
    }

    public String getClientId() {
        return textClientId.getText();
    }

    public String getSecretKey() {
        return textSecretKey.getText();
    }

    private void exit() {
        dispose();
        System.exit(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Login")) {
            String username = getUsername();
            String clientId = getClientId();
            String secretKey = getSecretKey();
            quizletify.login(username, clientId, secretKey);
            dispose();
        }
        else if (command.equals("Cancel")) {
            exit();
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        exit();
    }

    @Override
    public void windowOpened(WindowEvent e) {}
    @Override
    public void windowClosed(WindowEvent e) {}
    @Override
    public void windowIconified(WindowEvent e) {}
    @Override
    public void windowDeiconified(WindowEvent e) {}
    @Override
    public void windowActivated(WindowEvent e) {}
    @Override
    public void windowDeactivated(WindowEvent e) {}


}
