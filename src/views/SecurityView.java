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
    JMenu optionsMenu;
    JMenuItem menuItemGetLoginLogs;
    JMenuItem menuItemClearLogs;
    JMenuItem menuItemGoBack;
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
        setResizable(true);
        setLocationRelativeTo(null);
        pack();
    }

    void initKeystrokes(){
        menuItemGetLoginLogs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.InputEvent.CTRL_MASK));
    }

    void setFonts(){
        setFont(mainFont);
        menuBar.setFont(mainFont);
        optionsMenu.setFont(mainFont);
        menuItemGetLoginLogs.setFont(mainFont);
        menuItemGoBack.setFont(mainFont);
        menuItemClearLogs.setFont(mainFont);
        txtSecLogs.setFont(mainFont);
    }

    void initComponents(){
        setTitle("Security - logged in as: " + user.getUsername());
        menuBar = new JMenuBar();
        optionsMenu = new JMenu("Options");
        mainFont = new Font("Verdana", Font.BOLD, fontSize);
        menuItemGetLoginLogs = new JMenuItem("Get Login Logs");
        menuItemClearLogs = new JMenuItem("Clear Login Logs");
        menuItemGoBack = new JMenuItem("Go Back");
        txtSecLogs = new JTextArea();
        scroll = new JScrollPane(txtSecLogs,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    }

    void addComponents() {
        optionsMenu.add(menuItemGetLoginLogs);
        optionsMenu.add(menuItemClearLogs);
        optionsMenu.add(menuItemGoBack);
        menuBar.add(optionsMenu);
        setJMenuBar(menuBar);
        add(scroll);
    }

    public void addListeners(ActionListener listener){
        menuItemClearLogs.addActionListener(listener);
        menuItemGetLoginLogs.addActionListener(listener);
        menuItemGoBack.addActionListener(listener);
    }

    public void addFrameWindowListener(WindowListener listener){
        addWindowListener(listener);
    }

    public void setMenuBar(JMenuBar menuBar) {
        this.menuBar = menuBar;
    }

    public JMenu getoptionsMenu() {
        return optionsMenu;
    }

    public void setoptionsMenu(JMenu optionsMenu) {
        this.optionsMenu = optionsMenu;
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
    public void setSecLogsTXT(String txt){
        txtSecLogs.setText(txt);
    }

    public void appendSecLogsTXT(String text){
        txtSecLogs.append(text);
    }

    public void displayErrorMsg(String msg) {
        JOptionPane.showMessageDialog(this, msg, Env.SecurityMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
    }
}

