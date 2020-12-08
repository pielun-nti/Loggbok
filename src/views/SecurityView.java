package views;


import config.Env;
import models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;

public class SecurityView extends javax.swing.JFrame {

    JMenuBar menuBar;
    JMenu helpMenu;
    JMenuItem menuItemGetLoginLogs;
    JMenuItem menuItemClearLogs;
    JPanel mainPanel;
    private Font mainFont;
    int fontSize = 18;
    User user;

    public SecurityView(User user){
        this.user = user;
        initComponents();
        setFonts();
        initKeystrokes();
        addComponents();
        Dimension res = new Dimension(1200, 800);
        setPreferredSize(res);
        setSize(res);
        setContentPane(mainPanel);
        setResizable(false);
        setLocationRelativeTo(null);
        pack();
    }

    void initKeystrokes(){
        menuItemGetLoginLogs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.InputEvent.CTRL_MASK));
    }

    void setFonts(){
        setFont(mainFont);
        menuBar.setFont(mainFont);
        helpMenu.setFont(mainFont);
        menuItemGetLoginLogs.setFont(mainFont);
        menuItemClearLogs.setFont(mainFont);
    }

    void initComponents(){
        setTitle("Security - logged in as: " + user.getUsername());
        mainPanel = new JPanel();
        menuBar = new JMenuBar();
        helpMenu = new JMenu("Help");
        mainFont = new Font("Verdana", Font.BOLD, fontSize);
        menuItemGetLoginLogs = new JMenuItem("Get Login Logs");
        menuItemClearLogs = new JMenuItem("Clear Login Logs");
    }

    void addComponents() {
        helpMenu.add(menuItemGetLoginLogs);
        helpMenu.add(menuItemClearLogs);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
        mainPanel.setLayout(null);
    }

    public void addListeners(ActionListener listener){
        menuItemClearLogs.addActionListener(listener);
        menuItemGetLoginLogs.addActionListener(listener);
    }

    public void addFrameWindowListener(WindowListener listener){
        addWindowListener(listener);
    }

    public void displayErrorMsg(String msg) {
        JOptionPane.showMessageDialog(this, msg, Env.SecurityMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
    }
}

