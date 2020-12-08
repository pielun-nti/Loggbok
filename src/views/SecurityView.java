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
    JScrollPane scroll;
    JTextArea txtSecLogs;
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
        txtSecLogs.setFont(mainFont);
    }

    void initComponents(){
        setTitle("Security - logged in as: " + user.getUsername());
        mainPanel = new JPanel();
        menuBar = new JMenuBar();
        helpMenu = new JMenu("Help");
        mainFont = new Font("Verdana", Font.BOLD, fontSize);
        menuItemGetLoginLogs = new JMenuItem("Get Login Logs");
        menuItemClearLogs = new JMenuItem("Clear Login Logs");
        txtSecLogs = new JTextArea();
        scroll = new JScrollPane(txtSecLogs,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    }

    void addComponents() {
        helpMenu.add(menuItemGetLoginLogs);
        helpMenu.add(menuItemClearLogs);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
        mainPanel.setLayout(null);
        mainPanel.add(scroll);
    }

    public void addListeners(ActionListener listener){
        menuItemClearLogs.addActionListener(listener);
        menuItemGetLoginLogs.addActionListener(listener);
    }

    public void addFrameWindowListener(WindowListener listener){
        addWindowListener(listener);
    }

    public void setMenuBar(JMenuBar menuBar) {
        this.menuBar = menuBar;
    }

    public JMenu getHelpMenu() {
        return helpMenu;
    }

    public void setHelpMenu(JMenu helpMenu) {
        this.helpMenu = helpMenu;
    }

    public JMenuItem getMenuItemGetLoginLogs() {
        return menuItemGetLoginLogs;
    }

    public void setMenuItemGetLoginLogs(JMenuItem menuItemGetLoginLogs) {
        this.menuItemGetLoginLogs = menuItemGetLoginLogs;
    }

    public JMenuItem getMenuItemClearLogs() {
        return menuItemClearLogs;
    }

    public void setMenuItemClearLogs(JMenuItem menuItemClearLogs) {
        this.menuItemClearLogs = menuItemClearLogs;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public JScrollPane getScroll() {
        return scroll;
    }

    public void setScroll(JScrollPane scroll) {
        this.scroll = scroll;
    }

    public JTextArea getTxtSecLogs() {
        return txtSecLogs;
    }

    public void setTxtSecLogs(JTextArea txtSecLogs) {
        this.txtSecLogs = txtSecLogs;
    }

    public Font getMainFont() {
        return mainFont;
    }

    public void setMainFont(Font mainFont) {
        this.mainFont = mainFont;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSecLogsTXT(){
        return txtSecLogs.getText();
    }

    public void appendSecLogsTXT(String text){
        txtSecLogs.append(text);
    }

    public void displayErrorMsg(String msg) {
        JOptionPane.showMessageDialog(this, msg, Env.SecurityMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
    }
}

