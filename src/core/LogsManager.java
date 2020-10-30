package core;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.*;

/**
 * Skapad av Pierre 2020-10-29
 */
public class LogsManager extends javax.swing.JFrame {
    static Connection connection;
    static JFrame frame;
    static JMenuBar menuBar;
    static JMenu fileMenu;
    static JMenu editMenu;
    static JMenu settingsMenu;
    static JMenu aboutMenu;
    static JMenuItem menuitemNewLog;
    static JMenuItem menuItemGetLogs;
    static JMenuItem menuItemEditLog;
    static JMenuItem menuItemDeleteLog;
    static JMenuItem menuItemShowLogHistory;
    static JMenuItem menuItemExit;
    static JTextArea txtLogs;
    static JMenuItem menuItemChangeFontSize;
    private JPanel mainPanel;
    private static String MessageBoxTitle = "LogsManager GUI";
    private static String openedFile = null;
    private static Font mainFont;
    static int fontSize = 18;

    public LogsManager(){
        //todo:
        /**
         * Lägg till följande:
         * keystrokes för menyn
         * delete log by id
         * historik för ändringar av logs (skapa ett nytt table i databasen som heter changes eller history)
         * gör db dump i slutet
         */
    }
    private static void confirmExit() {
        //int dialogResult = JOptionPane.showConfirmDialog (null, "ARE YOU SURE YOU WANT TO EXIT THIS APPLICATION? ", MessageBoxTitle, JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
        //if(dialogResult == JOptionPane.YES_OPTION){
            frame.dispose();
        //}
    }
    public static void main(String[]args){
        if (!initDB()){
            JOptionPane.showMessageDialog(null, "Init DB Error!", MessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        frame = new JFrame("LogsManager");
        BufferedImage image = null;
        /*try {
            image = ImageIO.read(
                    LogsManager.class.getResource("../img/logsicon.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        frame.setIconImage(image);*/
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });
        Dimension res = new Dimension(1200, 800);
        frame.setPreferredSize(res);
        frame.setSize(res);
        txtLogs = new JTextArea();
        txtLogs.setEditable(true);
        mainFont = new Font("Verdana", Font.BOLD, fontSize);
        txtLogs.setFont(mainFont);
        frame.setFont(mainFont);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.pack();
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        fileMenu.setFont(mainFont);
        editMenu = new JMenu("Edit");
        settingsMenu = new JMenu("Settings");
        aboutMenu = new JMenu("About");
        editMenu.setFont(mainFont);
        settingsMenu.setFont(mainFont);
        aboutMenu.setFont(mainFont);
        menuItemChangeFontSize = new JMenuItem("Change Font Size");
        menuItemEditLog = new JMenuItem("Edit Log");
        menuItemEditLog.setFont(mainFont);
        menuItemEditLog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                editLog();
            }
        });
        menuItemGetLogs = new JMenuItem("Get All Logs");
        menuItemGetLogs.setFont(mainFont);
        menuItemGetLogs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                getAllLogs();
            }
        });
        menuitemNewLog = new JMenuItem("Create New Log");
        menuitemNewLog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                createNewLog();
            }
        });
        menuItemChangeFontSize.setFont(mainFont);
        menuItemChangeFontSize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                fontSize = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter new font size ( current = " + fontSize + ")", MessageBoxTitle, JOptionPane.INFORMATION_MESSAGE));
                mainFont = new Font("Verdana", Font.BOLD, fontSize);
                txtLogs.setFont(mainFont);
                frame.setFont(mainFont);
            }
        });
        menuItemExit = new JMenuItem("Exit application");
        menuItemExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                confirmExit();
            }
        });
        menuitemNewLog.setFont(mainFont);
        menuItemExit.setFont(mainFont);
        fileMenu.add(menuitemNewLog);
        fileMenu.add(menuItemEditLog);
        fileMenu.add(menuItemGetLogs);
        fileMenu.add(menuItemExit);
        editMenu.add(menuItemChangeFontSize);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        //menuBar.add(settingsMenu);
        menuBar.add(aboutMenu);
        frame.setJMenuBar(menuBar);
        frame.add(txtLogs);
        frame.setVisible(true);
    }

    static void getAllLogs(){
        Statement stmt= null;
        try {
            stmt = connection.createStatement();

            ResultSet rs=stmt.executeQuery("select * from logs");
            txtLogs.setText("");
            while(rs.next())
                if (txtLogs.getText().trim().equals("")) {
                    txtLogs.setText(rs.getString(1) + "\r\n" + rs.getString(2) + "\r\n" + rs.getString(3));
                }else{
                    txtLogs.append("\r\n--------------\r\n" + rs.getString(1) + "\r\n" + rs.getString(2) + "\r\n" + rs.getString(3));
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void editLog(){
        int logid = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter ID of the log you want to edit", MessageBoxTitle, JOptionPane.QUESTION_MESSAGE));
        if (logid < 0){
            JOptionPane.showMessageDialog(null, "ID Must be greater or equal to 0", MessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        //get info from the log, show it then allow editing
        Statement stmt= null;
        try {
            stmt = connection.createStatement();

            ResultSet rs=stmt.executeQuery("select * from logs where id = " + logid);
            if (rs == null){
                JOptionPane.showMessageDialog(null, "No log with that ID was found", MessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            while(rs.next()) {
                String logauthor = (String) JOptionPane.showInputDialog(null, "Edit author for log id: " + logid, MessageBoxTitle, JOptionPane.QUESTION_MESSAGE, null, null, rs.getString(2));
                if (logauthor == null){
                    JOptionPane.showMessageDialog(null, "Author cannot be null.", MessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (logauthor.trim().equals("")){
                    JOptionPane.showMessageDialog(null, "Author must have an value.", MessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!logauthor.trim().equals(rs.getString(1))){
                    stmt = (Statement) connection.createStatement();
                    String query1 = "update logs set author='" + logauthor + "' " + "where id in(" + logid + ")";
                    stmt.executeUpdate(query1);
                    //also update changes here
                }
                String logbody = (String) JOptionPane.showInputDialog(null, "Edit body for log id: " + logid, MessageBoxTitle, JOptionPane.QUESTION_MESSAGE, null, null, rs.getString(3));
                if (logbody == null){
                    JOptionPane.showMessageDialog(null, "Body cannot be null.", MessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (logbody.trim().equals("")){
                    JOptionPane.showMessageDialog(null, "Body must have an value.", MessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!logbody.trim().equals(rs.getString(2))){
                    stmt = (Statement) connection.createStatement();
                    String query1 = "update logs set body='" + logbody + "' " + "where id in(" + logid + ")";
                    stmt.executeUpdate(query1);
                    //also updates changes here
                }
            }
            JOptionPane.showMessageDialog(null, "Successfully updated author and body for log id: " + logid, MessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void createNewLog(){
        String author = (JOptionPane.showInputDialog(null, "Enter log author", MessageBoxTitle, JOptionPane.QUESTION_MESSAGE));
        String body = (JOptionPane.showInputDialog(null, "Enter log body", MessageBoxTitle, JOptionPane.QUESTION_MESSAGE));
        if (author == null || body == null){
            JOptionPane.showMessageDialog(null, "Author or body cannot be null.", MessageBoxTitle, JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (author.trim().equals("") || body.trim().equals("")){
            JOptionPane.showMessageDialog(null, "Author and body must have an value.", MessageBoxTitle, JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            String query1 = "INSERT INTO logs (author, body)" + "VALUES ('" + author + "', '" + body + "')";
            Statement stmt = (Statement) connection.createStatement();
            stmt.executeUpdate(query1);
            //also update changes here
            JOptionPane.showMessageDialog(null, "Inserted New Log Into Database Successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Insertion to Mysql Database Failure: " + e.toString());
        }
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
                //använder detta om man har ett % tecken i sitt db lösenord
                connection = DriverManager.getConnection(sqlURL.replaceAll("%(?![0-9a-fA-F]{2})", "%25"));

                Statement s=connection.createStatement();
                int result =s.executeUpdate("CREATE DATABASE " + Env.dbName);
                connection.close();
                connection = DriverManager.getConnection(conURL, user, pass);
                String create_logs_table="CREATE TABLE logs ("
                        + "ID int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                        + "AUTHOR varchar(255) DEFAULT NULL,"
                        + "BODY varchar(255) DEFAULT NULL)";
                String create_changes_table="CREATE TABLE changes ("
                        + "ID int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                        + "LOGID int unsigned NOT NULL,"
                        + "AUTHOR varchar(255) DEFAULT NULL,"
                        + "BODY varchar(255) DEFAULT NULL)";
                s = connection.createStatement();
                s.executeUpdate(create_logs_table);
                s = connection.createStatement();
                s.executeUpdate(create_changes_table);
            } catch (SQLException ex) {
                ex.printStackTrace();
                success = false;
                JOptionPane.showMessageDialog(null, "InitDB SQL Error: " + e.toString());
            }
        }
        return success;
    }

}
