package core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LogsView extends javax.swing.JFrame {
    static JFrame frame;
    static JMenuBar menuBar;
    static JMenu fileMenu;
    static JMenu editMenu;
    static JMenu settingsMenu;
    static JMenu aboutMenu;
    static JScrollPane scroll;
    static JMenuItem menuitemNewLog;
    static JMenuItem menuItemGetLogs;
    static JMenuItem menuItemEditLog;
    static JMenuItem menuItemDeleteLog;
    static JMenuItem menuItemShowLogHistory;
    static JMenuItem menuItemDeleteAllLogs;
    static JMenuItem menuItemDeleteLogHistory;
    static JMenuItem menuItemSaveAs;
    static JMenuItem menuItemLogout;
    static JMenuItem menuItemExit;
    static JMenuItem menuItemFilterLogs;
    static JTextArea txtLogs;
    static JMenuItem menuItemChangeFontSize;
    static JMenuItem menuItemAbout;
    private static String MessageBoxTitle = "LogsManager GUI";
    private static Font mainFont;
    static int fontSize = 18;
    static String username;
    
    public LogsView(String username){
        this.username = username;
        initComponents();
        setFonts();
        initKeystrokes();
        addComponents();
        Dimension res = new Dimension(1200, 800);
        frame.setPreferredSize(res);
        frame.setSize(res);
        txtLogs.setEditable(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
    void setFonts(){
        frame.setFont(mainFont);
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
    }

    void initComponents() {
        frame = new JFrame("LogsManager - logged in as: " + username);
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
        menuItemSaveAs = new JMenuItem("Save As");
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
        fileMenu.add(menuItemLogout);
        fileMenu.add(menuItemExit);
        editMenu.add(menuItemChangeFontSize);
        aboutMenu.add(menuItemAbout);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        //menuBar.add(settingsMenu);
        menuBar.add(aboutMenu);
        frame.setJMenuBar(menuBar);
        frame.add(scroll);
    }

    public static JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        LogsView.frame = frame;
    }

    @Override
    public static JMenuBar getMenuBar() {
        return menuBar;
    }

    public void setMenuBar(JMenuBar menuBar) {
        LogsView.menuBar = menuBar;
    }

    public static JMenu getFileMenu() {
        return fileMenu;
    }

    public void setFileMenu(JMenu fileMenu) {
        LogsView.fileMenu = fileMenu;
    }

    public static JMenu getEditMenu() {
        return editMenu;
    }

    public void setEditMenu(JMenu editMenu) {
        LogsView.editMenu = editMenu;
    }

    public static JMenu getSettingsMenu() {
        return settingsMenu;
    }

    public void setSettingsMenu(JMenu settingsMenu) {
        LogsView.settingsMenu = settingsMenu;
    }

    public static JMenu getAboutMenu() {
        return aboutMenu;
    }

    public void setAboutMenu(JMenu aboutMenu) {
        LogsView.aboutMenu = aboutMenu;
    }

    public static JScrollPane getScroll() {
        return scroll;
    }

    public void setScroll(JScrollPane scroll) {
        LogsView.scroll = scroll;
    }

    public static JMenuItem getMenuitemNewLog() {
        return menuitemNewLog;
    }

    public void setMenuitemNewLog(JMenuItem menuitemNewLog) {
        LogsView.menuitemNewLog = menuitemNewLog;
    }

    public static JMenuItem getMenuItemGetLogs() {
        return menuItemGetLogs;
    }

    public void setMenuItemGetLogs(JMenuItem menuItemGetLogs) {
        LogsView.menuItemGetLogs = menuItemGetLogs;
    }

    public static JMenuItem getMenuItemEditLog() {
        return menuItemEditLog;
    }

    public void setMenuItemEditLog(JMenuItem menuItemEditLog) {
        LogsView.menuItemEditLog = menuItemEditLog;
    }

    public static JMenuItem getMenuItemDeleteLog() {
        return menuItemDeleteLog;
    }

    public void setMenuItemDeleteLog(JMenuItem menuItemDeleteLog) {
        LogsView.menuItemDeleteLog = menuItemDeleteLog;
    }

    public static JMenuItem getMenuItemShowLogHistory() {
        return menuItemShowLogHistory;
    }

    public void setMenuItemShowLogHistory(JMenuItem menuItemShowLogHistory) {
        LogsView.menuItemShowLogHistory = menuItemShowLogHistory;
    }

    public static JMenuItem getMenuItemDeleteAllLogs() {
        return menuItemDeleteAllLogs;
    }

    public void setMenuItemDeleteAllLogs(JMenuItem menuItemDeleteAllLogs) {
        LogsView.menuItemDeleteAllLogs = menuItemDeleteAllLogs;
    }

    public static JMenuItem getMenuItemDeleteLogHistory() {
        return menuItemDeleteLogHistory;
    }

    public void setMenuItemDeleteLogHistory(JMenuItem menuItemDeleteLogHistory) {
        LogsView.menuItemDeleteLogHistory = menuItemDeleteLogHistory;
    }

    public static JMenuItem getMenuItemSaveAs() {
        return menuItemSaveAs;
    }

    public void setMenuItemSaveAs(JMenuItem menuItemSaveAs) {
        LogsView.menuItemSaveAs = menuItemSaveAs;
    }

    public static JMenuItem getMenuItemLogout() {
        return menuItemLogout;
    }

    public void setMenuItemLogout(JMenuItem menuItemLogout) {
        LogsView.menuItemLogout = menuItemLogout;
    }

    public static JMenuItem getMenuItemExit() {
        return menuItemExit;
    }

    public void setMenuItemExit(JMenuItem menuItemExit) {
        LogsView.menuItemExit = menuItemExit;
    }

    public static JMenuItem getMenuItemFilterLogs() {
        return menuItemFilterLogs;
    }

    public void setMenuItemFilterLogs(JMenuItem menuItemFilterLogs) {
        LogsView.menuItemFilterLogs = menuItemFilterLogs;
    }

    public static JTextArea getTxtLogs() {
        return txtLogs;
    }

    public void setTxtLogs(JTextArea txtLogs) {
        LogsView.txtLogs = txtLogs;
    }

    public static JMenuItem getMenuItemChangeFontSize() {
        return menuItemChangeFontSize;
    }

    public void setMenuItemChangeFontSize(JMenuItem menuItemChangeFontSize) {
        LogsView.menuItemChangeFontSize = menuItemChangeFontSize;
    }

    public static JMenuItem getMenuItemAbout() {
        return menuItemAbout;
    }

    public void setMenuItemAbout(JMenuItem menuItemAbout) {
        LogsView.menuItemAbout = menuItemAbout;
    }

    public static String getMessageBoxTitle() {
        return MessageBoxTitle;
    }

    public void setMessageBoxTitle(String messageBoxTitle) {
        MessageBoxTitle = messageBoxTitle;
    }

    public static Font getMainFont() {
        return mainFont;
    }

    public void setMainFont(Font mainFont) {
        LogsView.mainFont = mainFont;
    }

    public static int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        LogsView.fontSize = fontSize;
    }

    public static String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        LogsView.username = username;
    }
    public void setLogsTXT(String text){
        txtLogs.setText(text);
    }

    public String getLogsTXT(){
        return txtLogs.getText();
    }

    public void appendLogsTXT(String text){
        txtLogs.append(text);
    }

}
