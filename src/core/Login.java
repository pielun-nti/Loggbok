package core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.Arrays;

public class Login extends javax.swing.JFrame {

    static Connection connection;
    static JFrame frame;
    static JMenuBar menuBar;
    static JMenu helpMenu;
    static JMenuItem menuItemRegister;
    static JMenuItem menuItemAnonymous;
    static JTextField txtUsername;
    static JPasswordField txtPassword;
    static JLabel labelUsername;
    static JLabel labelPassword;
    static JButton btnLogin;
    static JPanel mainPanel;
    private static String MessageBoxTitle = "Login GUI";
    private static Font mainFont;
    static int fontSize = 18;

    public Login(){
        if (!initDB()){
            JOptionPane.showMessageDialog(null, "Init DB Error!", MessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        initComponents();
        setFonts();
        setLocation();
        setToolTips();
        initKeystrokes();
        addListeners();
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

    static void initKeystrokes(){
        menuItemRegister.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
    }

    static void setFonts(){
        frame.setFont(mainFont);
        menuBar.setFont(mainFont);
        helpMenu.setFont(mainFont);
        menuItemRegister.setFont(mainFont);
        menuItemAnonymous.setFont(mainFont);
        txtUsername.setFont(mainFont);
        txtPassword.setFont(mainFont);
        labelUsername.setFont(mainFont);
        labelPassword.setFont(mainFont);
        btnLogin.setFont(mainFont);
    }

    static void setLocation(){
        txtUsername.setLocation(130, 10);
        txtUsername.setSize(300, 30);
        txtPassword.setLocation(130, 40);
        txtPassword.setSize(300, 30);
        labelUsername.setLocation(10, 10);
        labelUsername.setSize(200, 30);
        labelPassword.setLocation(10, 40);
        labelPassword.setSize(200, 30);
        btnLogin.setLocation(10, 70);
        btnLogin.setSize(200, 30);
    }

    static void setToolTips(){
        txtUsername.setToolTipText("Enter username here");
        txtPassword.setToolTipText("Enter password here");
    }

    static void initComponents(){
        frame = new JFrame("Login");
        mainPanel = new JPanel();
        menuBar = new JMenuBar();
        helpMenu = new JMenu("Help");
        mainFont = new Font("Verdana", Font.BOLD, fontSize);
        menuItemRegister = new JMenuItem("Register");
        menuItemAnonymous = new JMenuItem("Login as Anonymous");
        txtPassword = new JPasswordField();
        txtUsername = new JTextField();
        labelUsername = new JLabel("Username: ");
        labelPassword = new JLabel("Password: ");
        btnLogin = new JButton("Login");
    }

    static void addComponents() {
        helpMenu.add(menuItemRegister);
        helpMenu.add(menuItemAnonymous);
        menuBar.add(helpMenu);
        frame.setJMenuBar(menuBar);
        mainPanel.setLayout(null);
        txtPassword.setEchoChar('*');
        mainPanel.add(txtUsername);
        mainPanel.add(txtPassword);
        mainPanel.add(labelUsername);
        mainPanel.add(labelPassword);
        mainPanel.add(btnLogin);
    }

    static void addListeners(){
        txtUsername.addKeyListener(new java.awt.event.KeyAdapter(){
            public void keyPressed(java.awt.event.KeyEvent evt){
                if (evt.getKeyChar() == KeyEvent.VK_ENTER){
                    txtPassword.requestFocus();
                }
            }
        });
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter(){
            public void keyPressed(java.awt.event.KeyEvent evt){
                if (evt.getKeyChar() == KeyEvent.VK_ENTER){
                    btnLogin.doClick();
                }
            }
        });
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Login();
            }
        });
        menuItemRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Register();
            }
        });
        menuItemAnonymous.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                loginAnonymous();
            }
        });
    }

    static void Login(){
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        if (username == null || password == null){
            JOptionPane.showMessageDialog(null, "Username or password cannot be null.", MessageBoxTitle, JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (username.trim().equals("") || password.trim().equals("")){
            JOptionPane.showMessageDialog(null, "Username and password must have an value.", MessageBoxTitle, JOptionPane.ERROR_MESSAGE);
            return;
        }
        String query = "SELECT * from users where username = '" + username + "' and password = '" + password + "'";
        Statement stmt = null;
        try {
            stmt = (Statement) connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (!rs.next()){
            JOptionPane.showMessageDialog(null, "Invalid username or password. Access denied.", MessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
            txtUsername.setText("");
            txtPassword.setText("");
            txtUsername.requestFocus();
            return;
        }
        String adminstr = rs.getString(4);
        boolean admin = false;
        if (adminstr != null){
            if (adminstr.equalsIgnoreCase("true")){
                admin = true;
            }
        }
        LogsManager logsManager = new LogsManager(username, admin);
        frame.dispose();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void Register(){
        Register register = new Register();
        frame.dispose();
    }

    static void loginAnonymous(){
        LogsManager logsManager = new LogsManager("Unknown", false);
        frame.dispose();
    }

    public static boolean initDB(){
        boolean success = true;

        String driverName = Env.driverName;
        String conURL = Env.conURL;
        String user = Env.user;
        String pass = Env.pass;

        try {
            Class.forName(driverName).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "InitDB SQL Error: " + e.toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "InitDB SQL Error: " + e.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            success = false;
            JOptionPane.showMessageDialog(null, "InitDB SQL Error: " + e.toString());
        }

        try {
            connection = DriverManager.getConnection(conURL, user, pass);
        } catch (SQLException e) {
            System.out.println("Maybe DB not exist, creating db, then trying to connect again...");
            try {
                String sqlURL = "jdbc:mysql://localhost:3306/?user=" + user + "&password=" + pass + "&characterEncoding=latin1";
                //använder detta om man har ett % eller annat regex tecken i sitt db lösenord
                connection = DriverManager.getConnection(sqlURL.replaceAll("%(?![0-9a-fA-F]{2})", "%25"));

                Statement s=connection.createStatement();
                int result =s.executeUpdate("CREATE DATABASE " + Env.dbName);
                connection.close();
                connection = DriverManager.getConnection(conURL, user, pass);
                String create_users_table ="CREATE TABLE users ("
                        + "ID int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                        + "USERNAME varchar(255) DEFAULT NULL,"
                        + "PASSWORD varchar(255) DEFAULT NULL,"
                        + "ADMIN varchar(255) DEFAULT NULL)";
                String create_logs_table="CREATE TABLE logs ("
                        + "ID int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                        + "AUTHOR varchar(255) DEFAULT NULL,"
                        + "BODY varchar(255) DEFAULT NULL,"
                        + "EDITORS varchar(255) DEFAULT NULL)";
                String create_changes_table="CREATE TABLE changes ("
                        + "ID int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                        + "LOGID int unsigned NOT NULL,"
                        + "AUTHOR varchar(255) DEFAULT NULL,"
                        + "BODY varchar(255) DEFAULT NULL,"
                        + "CREATED_AT varchar(255) DEFAULT NULL,"
                        + "LAST_EDITED varchar(255) DEFAULT NULL,"
                        + "TYPE varchar(255) DEFAULT NULL,"
                        + "EDITOR varchar(255) DEFAULT NULL)";
                s = connection.createStatement();
                s.executeUpdate(create_logs_table);
                s = connection.createStatement();
                s.executeUpdate(create_changes_table);
                s = connection.createStatement();
                s.executeUpdate(create_users_table);
            } catch (SQLException ex) {
                ex.printStackTrace();
                success = false;
                JOptionPane.showMessageDialog(null, "InitDB SQL Error: " + e.toString());
            }
        }
        return success;
    }
}
