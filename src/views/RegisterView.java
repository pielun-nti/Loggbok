package views;

import config.Env;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;

public class RegisterView extends javax.swing.JFrame {
    JFrame frame;
    JMenuBar menuBar;
    JMenu helpMenu;
    JMenuItem menuItemLogin;
    JMenuItem menuItemAnonymous;
    JTextField txtUsername;
    JPasswordField txtPassword;
    JLabel labelUsername;
    JLabel labelPassword;
    JButton btnRegister;
    JPanel mainPanel;
    private Font mainFont;
    int fontSize = 18;
    public RegisterView(){
        initComponents();
        setFonts();
        setLocation();
        setToolTips();
        initKeystrokes();
        addComponents();
        Dimension res = new Dimension(1200, 800);
        frame.setPreferredSize(res);
        frame.setSize(res);
        frame.setContentPane(mainPanel);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
        txtUsername.requestFocus();
    }

    void setLocation(){
        txtUsername.setLocation(130, 10);
        txtUsername.setSize(300, 30);
        txtPassword.setLocation(130, 40);
        txtPassword.setSize(300, 30);
        labelUsername.setLocation(10, 10);
        labelUsername.setSize(200, 30);
        labelPassword.setLocation(10, 40);
        labelPassword.setSize(200, 30);
        btnRegister.setLocation(10, 70);
        btnRegister.setSize(200, 30);
    }

    void initKeystrokes(){
        menuItemLogin.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
    }

    void setToolTips(){
        txtUsername.setToolTipText("Enter username here");
        txtPassword.setToolTipText("Enter password here");
    }

    void setFonts(){
        frame.setFont(mainFont);
        helpMenu.setFont(mainFont);
        menuBar.setFont(mainFont);
        menuItemLogin.setFont(mainFont);
        menuItemAnonymous.setFont(mainFont);
        txtUsername.setFont(mainFont);
        txtPassword.setFont(mainFont);
        labelUsername.setFont(mainFont);
        labelPassword.setFont(mainFont);
        btnRegister.setFont(mainFont);
    }

    void initComponents(){
        frame = new JFrame("Register");
        mainPanel = new JPanel();
        menuBar = new JMenuBar();
        helpMenu = new JMenu("Register");
        mainFont = new Font("Verdana", Font.BOLD, fontSize);
        menuItemLogin = new JMenuItem("Login");
        menuItemAnonymous = new JMenuItem("Login as Anonymous");
        txtPassword = new JPasswordField();
        txtUsername = new JTextField();
        labelUsername = new JLabel("Username: ");
        labelPassword = new JLabel("Password: ");
        btnRegister = new JButton("Register");
    }

    void addComponents(){
        helpMenu.add(menuItemLogin);
        helpMenu.add(menuItemAnonymous);
        menuBar.add(helpMenu);
        frame.setJMenuBar(menuBar);
        mainPanel.setLayout(null);
        txtPassword.setEchoChar('*');
        mainPanel.add(txtUsername);
        mainPanel.add(txtPassword);
        mainPanel.add(labelUsername);
        mainPanel.add(labelPassword);
        mainPanel.add(btnRegister);
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
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

    public JMenuItem getMenuItemLogin() {
        return menuItemLogin;
    }

    public void setMenuItemLogin(JMenuItem menuItemLogin) {
        this.menuItemLogin = menuItemLogin;
    }

    public JMenuItem getMenuItemAnonymous() {
        return menuItemAnonymous;
    }

    public void setMenuItemAnonymous(JMenuItem menuItemAnonymous) {
        this.menuItemAnonymous = menuItemAnonymous;
    }

    public JTextField getTxtUsername() {
        return txtUsername;
    }

    public void setTxtUsername(JTextField txtUsername) {
        this.txtUsername = txtUsername;
    }

    public JPasswordField getTxtPassword() {
        return txtPassword;
    }

    public void setTxtPassword(JPasswordField txtPassword) {
        this.txtPassword = txtPassword;
    }

    public JLabel getLabelUsername() {
        return labelUsername;
    }

    public void setLabelUsername(JLabel labelUsername) {
        this.labelUsername = labelUsername;
    }

    public JLabel getLabelPassword() {
        return labelPassword;
    }

    public void setLabelPassword(JLabel labelPassword) {
        this.labelPassword = labelPassword;
    }

    public JButton getBtnRegister() {
        return btnRegister;
    }

    public void setBtnRegister(JButton btnRegister) {
        this.btnRegister = btnRegister;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
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

    public void addListeners(ActionListener listener){
        btnRegister.addActionListener(listener);
        menuItemAnonymous.addActionListener(listener);
        menuItemLogin.addActionListener(listener);
    }

    public void addKeyListeners(KeyListener username, KeyListener password){
        txtUsername.addKeyListener(username);
        txtPassword.addKeyListener(password);
    }

    public void addFrameWindowListener(WindowListener listener){
        frame.addWindowListener(listener);
    }

    public void displayErrorMsg(String msg) {
        JOptionPane.showMessageDialog(this, msg, Env.RegisterMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
    }
}
