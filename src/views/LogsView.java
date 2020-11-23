package views;

import config.Env;
import models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LogsView extends javax.swing.JFrame {
    JMenuBar menuBar;
    JMenu fileMenu;
    JMenu editMenu;
    JMenu settingsMenu;
    JMenu aboutMenu;
    JScrollPane scroll;
    JMenuItem menuitemNewLog;
    JMenuItem menuItemGetLogs;
    JMenuItem menuItemEditLog;
    JMenuItem menuItemDeleteLog;
    JMenuItem menuItemShowLogHistory;
    JMenuItem menuItemDeleteAllLogs;
    JMenuItem menuItemDeleteLogHistory;
    JMenuItem menuItemSaveAs;
    JMenuItem menuItemOpen;
    JMenuItem menuItemLogout;
    JMenuItem menuItemExit;
    JMenuItem menuItemFilterLogs;
    JTextArea txtLogs;
    JMenuItem menuItemChangeFontSize;
    JMenuItem menuItemAbout;
    private Font mainFont;
    int fontSize = 18;
    User user;

    public LogsView(User user){
        this.user = user;
        initComponents();
        setFonts();
        initKeystrokes();
        addComponents();
        Dimension res = new Dimension(1200, 800);
        setPreferredSize(res);
        setSize(res);
        txtLogs.setEditable(true);
        setResizable(false);
        setLocationRelativeTo(null);
        pack();
    }
    void setFonts(){
        setFont(mainFont);
        txtLogs.setFont(mainFont);
        menuBar.setFont(mainFont);
        fileMenu.setFont(mainFont);
        editMenu.setFont(mainFont);
        aboutMenu.setFont(mainFont);
        menuitemNewLog.setFont(mainFont);
        menuItemEditLog.setFont(mainFont);
        menuItemDeleteLog.setFont(mainFont);
        menuItemGetLogs.setFont(mainFont);
        menuItemShowLogHistory.setFont(mainFont);
        menuItemDeleteAllLogs.setFont(mainFont);
        menuItemDeleteLogHistory.setFont(mainFont);
        menuItemSaveAs.setFont(mainFont);
        menuItemOpen.setFont(mainFont);
        menuItemExit.setFont(mainFont);
        menuItemChangeFontSize.setFont(mainFont);
        menuItemAbout.setFont(mainFont);
        menuItemLogout.setFont(mainFont);
        menuItemFilterLogs.setFont(mainFont);
    }

    void initKeystrokes(){
        menuItemGetLogs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.InputEvent.CTRL_MASK));
        menuItemShowLogHistory.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2, java.awt.event.InputEvent.CTRL_MASK));
        menuitemNewLog.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_3, java.awt.event.InputEvent.CTRL_MASK));
        menuItemEditLog.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_4, java.awt.event.InputEvent.CTRL_MASK));
        menuItemDeleteLog.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_5, java.awt.event.InputEvent.CTRL_MASK));
        menuItemSaveAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        menuItemLogout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        menuItemFilterLogs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        menuItemOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
    }

    void initComponents() {
        setTitle("LogsManager - logged in as: " + user.getUsername());
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit");
        settingsMenu = new JMenu("Settings");
        aboutMenu = new JMenu("About");
        mainFont = new Font("Verdana", Font.BOLD, fontSize);
        txtLogs = new JTextArea();
        scroll = new JScrollPane(txtLogs,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        menuItemExit = new JMenuItem("Exit application");
        menuItemSaveAs = new JMenuItem("Save Logs As");
        menuItemOpen = new JMenuItem("Open Logs");
        menuItemDeleteAllLogs = new JMenuItem("Delete All Logs");
        menuItemDeleteLogHistory = new JMenuItem("Delete All Logs Changes History");
        menuItemDeleteLog = new JMenuItem("Delete Log");
        menuItemEditLog = new JMenuItem("Edit Log");
        menuItemGetLogs = new JMenuItem("Get All Logs");
        menuitemNewLog = new JMenuItem("Create New Log");
        menuItemChangeFontSize = new JMenuItem("Change Font Size");
        menuItemShowLogHistory = new JMenuItem("Get Logs Changes History");
        menuItemAbout = new JMenuItem("About");
        menuItemLogout = new JMenuItem("Logout");
        menuItemFilterLogs = new JMenuItem("Filter Logs");
    }

    void addComponents(){
        fileMenu.add(menuitemNewLog);
        fileMenu.add(menuItemEditLog);
        fileMenu.add(menuItemDeleteLog);
        fileMenu.add(menuItemGetLogs);
        fileMenu.add(menuItemShowLogHistory);
        fileMenu.add(menuItemDeleteAllLogs);
        fileMenu.add(menuItemDeleteLogHistory);
        fileMenu.add(menuItemFilterLogs);
        fileMenu.add(menuItemSaveAs);
        fileMenu.add(menuItemOpen);
        fileMenu.add(menuItemLogout);
        fileMenu.add(menuItemExit);
        editMenu.add(menuItemChangeFontSize);
        aboutMenu.add(menuItemAbout);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        //menuBar.add(settingsMenu);
        menuBar.add(aboutMenu);
        setJMenuBar(menuBar);
        add(scroll);
    }


    public void setMenuBar(JMenuBar menuBar) {
        this.menuBar = menuBar;
    }

    public JMenu getFileMenu() {
        return fileMenu;
    }

    public void setFileMenu(JMenu fileMenu) {
        this.fileMenu = fileMenu;
    }

    public JMenu getEditMenu() {
        return editMenu;
    }

    public void setEditMenu(JMenu editMenu) {
        this.editMenu = editMenu;
    }

    public JMenu getSettingsMenu() {
        return settingsMenu;
    }

    public void setSettingsMenu(JMenu settingsMenu) {
        this.settingsMenu = settingsMenu;
    }

    public JMenu getAboutMenu() {
        return aboutMenu;
    }

    public void setAboutMenu(JMenu aboutMenu) {
        this.aboutMenu = aboutMenu;
    }

    public JScrollPane getScroll() {
        return scroll;
    }

    public void setScroll(JScrollPane scroll) {
        this.scroll = scroll;
    }

    public JMenuItem getMenuitemNewLog() {
        return menuitemNewLog;
    }

    public void setMenuitemNewLog(JMenuItem menuitemNewLog) {
        this.menuitemNewLog = menuitemNewLog;
    }

    public JMenuItem getMenuItemGetLogs() {
        return menuItemGetLogs;
    }

    public void setMenuItemGetLogs(JMenuItem menuItemGetLogs) {
        this.menuItemGetLogs = menuItemGetLogs;
    }

    public JMenuItem getMenuItemEditLog() {
        return menuItemEditLog;
    }

    public void setMenuItemEditLog(JMenuItem menuItemEditLog) {
        this.menuItemEditLog = menuItemEditLog;
    }

    public JMenuItem getMenuItemDeleteLog() {
        return menuItemDeleteLog;
    }

    public void setMenuItemDeleteLog(JMenuItem menuItemDeleteLog) {
        this.menuItemDeleteLog = menuItemDeleteLog;
    }

    public JMenuItem getMenuItemShowLogHistory() {
        return menuItemShowLogHistory;
    }

    public void setMenuItemShowLogHistory(JMenuItem menuItemShowLogHistory) {
        this.menuItemShowLogHistory = menuItemShowLogHistory;
    }

    public JMenuItem getMenuItemDeleteAllLogs() {
        return menuItemDeleteAllLogs;
    }

    public void setMenuItemDeleteAllLogs(JMenuItem menuItemDeleteAllLogs) {
        this.menuItemDeleteAllLogs = menuItemDeleteAllLogs;
    }

    public JMenuItem getMenuItemDeleteLogHistory() {
        return menuItemDeleteLogHistory;
    }

    public void setMenuItemDeleteLogHistory(JMenuItem menuItemDeleteLogHistory) {
        this.menuItemDeleteLogHistory = menuItemDeleteLogHistory;
    }

    public JMenuItem getMenuItemSaveAs() {
        return menuItemSaveAs;
    }

    public void setMenuItemSaveAs(JMenuItem menuItemSaveAs) {
        this.menuItemSaveAs = menuItemSaveAs;
    }

    public JMenuItem getMenuItemLogout() {
        return menuItemLogout;
    }

    public void setMenuItemLogout(JMenuItem menuItemLogout) {
        this.menuItemLogout = menuItemLogout;
    }

    public JMenuItem getMenuItemExit() {
        return menuItemExit;
    }

    public void setMenuItemExit(JMenuItem menuItemExit) {
        this.menuItemExit = menuItemExit;
    }

    public JMenuItem getMenuItemFilterLogs() {
        return menuItemFilterLogs;
    }

    public void setMenuItemFilterLogs(JMenuItem menuItemFilterLogs) {
        this.menuItemFilterLogs = menuItemFilterLogs;
    }

    public JTextArea getTxtLogs() {
        return txtLogs;
    }

    public void setTxtLogs(JTextArea txtLogs) {
        this.txtLogs = txtLogs;
    }

    public JMenuItem getMenuItemChangeFontSize() {
        return menuItemChangeFontSize;
    }

    public void setMenuItemChangeFontSize(JMenuItem menuItemChangeFontSize) {
        this.menuItemChangeFontSize = menuItemChangeFontSize;
    }

    public JMenuItem getMenuItemAbout() {
        return menuItemAbout;
    }

    public void setMenuItemAbout(JMenuItem menuItemAbout) {
        this.menuItemAbout = menuItemAbout;
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


    public void setLogsTXT(String text){
        txtLogs.setText(text);
    }

    public void addListeners(ActionListener listener){
        menuitemNewLog.addActionListener(listener);
        menuItemEditLog.addActionListener(listener);
        menuItemDeleteLog.addActionListener(listener);
        menuItemGetLogs.addActionListener(listener);
        menuItemShowLogHistory.addActionListener(listener);
        menuItemDeleteAllLogs.addActionListener(listener);
        menuItemDeleteLogHistory.addActionListener(listener);
        menuItemFilterLogs.addActionListener(listener);
        menuItemSaveAs.addActionListener(listener);
        menuItemOpen.addActionListener(listener);
        menuItemLogout.addActionListener(listener);
        menuItemExit.addActionListener(listener);
        menuItemChangeFontSize.addActionListener(listener);
        menuItemAbout.addActionListener(listener);
    }

    public void addFrameWindowListener(WindowListener listener){
        addWindowListener(listener);
    }

    public String getLogsTXT(){
        return txtLogs.getText();
    }

    public void appendLogsTXT(String text){
        txtLogs.append(text);
    }
    public void displayErrorMsg(String msg) {
        JOptionPane.showMessageDialog(this, msg, Env.LogsMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
    }
    

}
