package com.patrykkosieradzki.quizletify.ui;

import com.patrykkosieradzki.quizletify.TextDocument;
import com.patrykkosieradzki.quizletify.api.google.Language;
import com.patrykkosieradzki.quizletify.api.quizlet.QuizletUser;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.prefs.*;

public class Quizletify implements ActionListener, WindowListener {

    private JFrame frame;
    private JLabel label;
    private JPanel panel;

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu windowMenu;
    private JMenu helpMenu;

    private QuizletUser user;
    private Preferences preferences;

    public Quizletify() {
        setLookAndFeel();
        init();
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        prepareFrame();
        prepareLabel();
        preparePanel();
        setFrame();

        checkPrefs();
        loadApp();
    }

    private void prepareFrame() {
        frame = new JFrame("Quizletify");
        frame.setSize(new Dimension(600, 400));
        frame.setLayout(new GridLayout(3,1));
        frame.setLocationRelativeTo(null);
        frame.addWindowListener(this);
    }

    private void prepareLabel() {
        label = new JLabel("",JLabel.CENTER);
    }

    private void preparePanel() {
        panel = new JPanel();
        panel.setLayout(new FlowLayout());
    }

    private void setFrame() {
        frame.add(label);
        frame.add(panel);
        frame.setVisible(true);
    }

    private void checkPrefs() {
        try {
            preferences = Preferences.userNodeForPackage(Quizletify.class);
            if (preferences.nodeExists("login_data")) {
                String username = preferences.node("login_data").get("username", null);
                String clientId = preferences.node("login_data").get("clientId", null);
                String secretKey = preferences.node("login_data").get("secretKey", null);
                login(username, clientId, secretKey);
            } else {
                displayLoginDialog();
            }
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }

    private void displayLoginDialog() {
        LoginDialog loginDialog = new LoginDialog(this, frame);

        String username = loginDialog.getUsername();
        String clientId = loginDialog.getClientId();
        String secretKey = loginDialog.getSecretKey();

        putPreferences(username, clientId, secretKey);
    }

    public void login(String username, String clientId, String secretKey) {
        try {
            user = new QuizletUser(username, clientId, secretKey);
            user.authorize();

        } catch (Exception e) {
            showMessageDialog(e.getMessage());
            displayLoginDialog();
        }
    }

    private void logout() {
        removePreferences();
        frame.dispose();
        new Quizletify();
    }

    private void putPreferences(String username, String clientId, String secretKey) {
        preferences = Preferences
                .userNodeForPackage(Quizletify.class)
                .node("login_data");

        preferences.put("username", username);
        preferences.put("clientId", clientId);
        preferences.put("secretKey", secretKey);
    }

    private void removePreferences() {
        try {
            preferences = Preferences
                    .userNodeForPackage(Quizletify.class)
                    .node("login_data");
            preferences.removeNode();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }


    private void loadApp() {
        menuBar = new JMenuBar();

        fileMenu = new JMenu("File");

        JMenuItem openMenuItem = new JMenuItem("Open");
        fileMenu.add(openMenuItem);
        openMenuItem.addActionListener(this);

        JMenuItem logoutMenuItem = new JMenuItem("Logout");
        fileMenu.add(logoutMenuItem);
        logoutMenuItem.addActionListener(this);

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        fileMenu.add(exitMenuItem);
        exitMenuItem.addActionListener(this);

        windowMenu = new JMenu("Window");

        JMenuItem minimizeMenuItem = new JMenuItem("Minimize");
        windowMenu.add(minimizeMenuItem);
        minimizeMenuItem.addActionListener(this);

        helpMenu = new JMenu("Help");

        JMenuItem licensesMenuItem = new JMenuItem("Licenses");
        helpMenu.add(licensesMenuItem);
        licensesMenuItem.addActionListener(this);

        JMenuItem registerMenuItem = new JMenuItem("Register");
        helpMenu.add(registerMenuItem);
        registerMenuItem.addActionListener(this);

        menuBar.add(fileMenu);
        menuBar.add(windowMenu);
        menuBar.add(helpMenu);

        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
    }

    private void showMessageDialog(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    private void exit() {
        frame.dispose();
        System.exit(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Open")) {
            openFile();
        }
        else if (command.equals("Logout")) {
            logout();
        }
        else if (command.equals("Exit")) {
            exit();
        }
        else if (command.equals("Minimize")) {
            minimize();
        }
        else if (command.equals("Licenses")) {
            showLicenses();
        }
        else if (command.equals("Register")) {
            register();
        }
    }

    private void openFile() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Text Documents", "txt", "doc", "srt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(frame);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                TextDocument document = new TextDocument(chooser.getSelectedFile().getPath());
                document.retrieveWords();
                new WordsCheckBoxList(document, user);

            } catch (FileNotFoundException e) {
                showMessageDialog("File Not Found");
            }

        }
    }

    private void minimize() {
        frame.setState(Frame.ICONIFIED);
    }

    private void showLicenses() {
        JFrame licensesFrame = new JFrame("Licenses");

        JLabel licenseLabel = new JLabel("",JLabel.CENTER);

        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
    }

    private void register() {
        try {
            URI uri = new URI("https://quizlet.com/sign-up");
            Desktop.getDesktop().browse(uri);
        } catch (Exception e) {
            e.printStackTrace();
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
