package core;

import sun.plugin2.message.Message;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLDecoder;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Calendar;

/**
 * Skapad av Pierre 2020-10-29
 */
public class LogsManager extends javax.swing.JFrame {
    static Connection connection;
    static JFrame frame;
    static String username;
    static boolean admin;
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

    public LogsManager(String username, boolean admin){
        this.username = username;
        this.admin = admin;
        if (!initDB()){
            JOptionPane.showMessageDialog(null, "Init DB Error!", MessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        initComponents();
        setFonts();
        initKeystrokes();
        addListeners();
        addComponents();
        Dimension res = new Dimension(1200, 800);
        frame.setPreferredSize(res);
        frame.setSize(res);
        txtLogs.setEditable(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
        //JOptionPane.showMessageDialog(null, "Welcome to LogsManager, " +username);
        //todo:
        /**
         * Lägg till följande:
         * keystrokes för menyn om tid över (gjort)
         * delete log by id (gjort)
         * historik för ändringar av logs (skapa ett nytt table i databasen som heter changes eller history) (gjort)
         * remove log history (gjort)
         * remove all logs (gjort)
         * fixa så man inte kan göra log duplicates (gjort)
         * gör så att man kan spara logs/ändringshistorik i en fil via savefiledialog om jag vill (gjort)
         * gör db dump i slutet (egentligen behövs ej för om db inte existerar så skapar programmet en) (gjort)
         * gör så att man kan logga in eller registrera användare. (gjort)
         * gör så att man kan välja att vara anonym och isåfall blir authorn unknown (gjort)
         * lägg till så att man kan filtrera loggar sen om jag vill (gjort)
         * lägg till så att bara admins kan ta bort loggar (gjort)
         * förbättra filtreringen av loggar
         */
    }

    static void setFonts(){
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

    static void initKeystrokes(){
        menuItemGetLogs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.InputEvent.CTRL_MASK));
        menuItemShowLogHistory.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2, java.awt.event.InputEvent.CTRL_MASK));
        menuitemNewLog.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_3, java.awt.event.InputEvent.CTRL_MASK));
        menuItemEditLog.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_4, java.awt.event.InputEvent.CTRL_MASK));
        menuItemDeleteLog.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_5, java.awt.event.InputEvent.CTRL_MASK));
        menuItemSaveAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        menuItemLogout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        menuItemFilterLogs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
    }

    static void initComponents(){
        frame = new JFrame("LogsManager - logged in as: " + username);
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit");
        settingsMenu = new JMenu("Settings");
        aboutMenu = new JMenu("About");
        mainFont = new Font("Verdana", Font.BOLD, fontSize);
        txtLogs = new JTextArea();
        scroll = new JScrollPane (txtLogs,
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

    static void addListeners(){
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });
        menuItemShowLogHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                getLogHistory();
            }
        });
        menuItemSaveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                saveFileDialog();
            }
        });
        menuItemDeleteAllLogs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!admin) {
                 JOptionPane.showMessageDialog(null, "Only admins can do that. Access denied.", MessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                 return;
                }
                deleteAllLogs();
            }
        });
        menuItemDeleteLogHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!admin) {
                    JOptionPane.showMessageDialog(null, "Only admins can do that. Access denied.", MessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                    return;
                }
                deleteAllLogHistory();
            }
        });
        menuItemDeleteLog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!admin) {
                    JOptionPane.showMessageDialog(null, "Only admins can do that. Access denied.", MessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                    return;
                }
                deleteLog();
            }
        });
        menuItemEditLog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                editLog();
            }
        });
        menuItemGetLogs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                getAllLogs();
            }
        });
        menuitemNewLog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                createNewLog();
            }
        });
        menuItemChangeFontSize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                fontSize = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter new font size ( current = " + fontSize + ")", MessageBoxTitle, JOptionPane.INFORMATION_MESSAGE));
                mainFont = new Font("Verdana", Font.BOLD, fontSize);
                txtLogs.setFont(mainFont);
                frame.setFont(mainFont);
            }
        });
        menuItemAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(null, "Made by Pierre Lundström", MessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
            }
        });
        menuItemExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                frame.dispose();

            }
        });
        menuItemLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Logout();
            }
        });
        menuItemFilterLogs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                filterLogs();
            }
        });
    }

    static void Logout(){
        Login login = new Login();
        frame.dispose();
    }

    static void addComponents(){
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


    static void getAllLogs(){
        Statement stmt= null;
        try {
            stmt = connection.createStatement();

            ResultSet rs=stmt.executeQuery("select * from logs");
            txtLogs.setText("");
            while(rs.next())
                if (txtLogs.getText().trim().equals("")) {
                    txtLogs.setText("ID: " + rs.getString(1) + "\r\nAuthor: " + rs.getString(2) + "\r\nEditors: " + rs.getString(4) + "\r\nBody: " + rs.getString(3));
                }else{
                    txtLogs.append("\r\n--------------\r\nID: " + rs.getString(1) + "\r\nAuthor: " + rs.getString(2) + "\r\nEditors: " + rs.getString(4) + "\r\nBody: " + rs.getString(3));
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void deleteAllLogs(){
        Statement stmt= null;
        try {
            stmt = connection.createStatement();

            int deletedRows =stmt.executeUpdate("DELETE from logs");
            if(deletedRows>0){
                String last_edited = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
                String type = "Deletion of all logs";
                String query2 = "INSERT INTO changes (created_at, last_edited, type, logid, author, editor, body)"
                        + "VALUES ('" + "" + "', '" + last_edited + "', '" + type + "', '" + "404" + "', '" + ""
                        + "', '" + username + "', '" + "" + "')";
                Statement stmt2 = (Statement) connection.createStatement();
                stmt2.executeUpdate(query2);
                JOptionPane.showMessageDialog(null, "Successfully deleted all logs", MessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "There are no logs to delete", MessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void deleteAllLogHistory(){
        Statement stmt= null;
        try {
            stmt = connection.createStatement();

            int deletedRows =stmt.executeUpdate("DELETE from changes");
            if(deletedRows>0){
                JOptionPane.showMessageDialog(null, "Successfully deleted all log changes history", MessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "There are no log changes history to delete", MessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void getLogHistory(){
        Statement stmt= null;
        try {
            stmt = connection.createStatement();

            ResultSet rs=stmt.executeQuery("select * from changes");
            txtLogs.setText("");
            while(rs.next())
                if (txtLogs.getText().trim().equals("")) {
                    txtLogs.setText("ID: " + rs.getString(1) + "\r\nLog ID: " + rs.getString(2) + "\r\nAuthor: " + rs.getString(3)
                            + "\r\nEditor: " + rs.getString(8)
                            + "\r\nBody: " + rs.getString(4) + "\r\nCreated At: " + rs.getString(5) + "\r\nLast Edited: " + rs.getString(6)
                            + "\r\nType Of Change: " + rs.getString(7
                    ));
                }else{
                    txtLogs.append("\r\n--------------\r\nID: " + rs.getString(1) + "\r\nLog ID:" + rs.getString(2)
                            + "\r\nAuthor: "
                            + rs.getString(3)
                            + "\r\nEditor: " + rs.getString(8) + "\r\nBody: " + rs.getString(4) + "\r\nCreated At: " + rs.getString(5)
                            + "\r\nLast Edited: " + rs.getString(6) + "\r\nType Of Change: " + rs.getString(7));
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
            if (!rs.next()){
                JOptionPane.showMessageDialog(null, "No log with that ID was found", MessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
                return;
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
            String query4 = "SELECT * from logs where author = '" + rs.getString(2) + "' and body = '" + logbody + "'";
            Statement stmt4 = (Statement) connection.createStatement();
            ResultSet rs4 = stmt4.executeQuery(query4);
            if (rs4.next()){
                JOptionPane.showMessageDialog(null, "A log with exactly identical author and body already exists. Please change.", MessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String editors = rs.getString(4);
            if (editors != null) {
                if (!editors.contains(username)) {
                    if (editors.trim().equals("")){
                     editors = username;
                    }else {
                        editors += ", " + username;
                    }
                }
            }
            stmt = (Statement) connection.createStatement();
            String query6 = "update logs set editors='" + editors + "' " + "where id in(" + logid + ")";
            stmt.executeUpdate(query6);
            //also updates changes here
            String query3 = "select * from changes where logid = " + logid;

                if (!logbody.trim().equals(rs.getString(2))){
                    stmt = (Statement) connection.createStatement();
                    String query1 = "update logs set body='" + logbody + "' " + "where id in(" + logid + ")";
                    stmt.executeUpdate(query1);
                    Statement stmt3 = (Statement) connection.createStatement();
                    ResultSet rs2=stmt3.executeQuery(query3);
                    String created_at = null;
                    if (rs2.next()) {
                        created_at = rs2.getString(5);
                    }
                    String last_edited = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
                    String type = "Editing";
                    String query2 = "INSERT INTO changes (created_at, last_edited, type, logid, author, editor, body)"
                            + "VALUES ('" + created_at + "', '" + last_edited + "', '" + type + "', '" + logid + "', '"
                            + rs.getString(2) + "', '"
                            + username + "', '"
                            + logbody + "')";
                    Statement stmt2 = (Statement) connection.createStatement();
                    stmt2.executeUpdate(query2);
                }
            //}
            JOptionPane.showMessageDialog(null, "Successfully updated author and body for log id: " + logid, MessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void deleteLog(){
        int logid = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter ID of the log you want to delete", MessageBoxTitle, JOptionPane.QUESTION_MESSAGE));
        if (logid < 0){
            JOptionPane.showMessageDialog(null, "ID Must be greater or equal to 0", MessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        //get info from the log, show it then allow editing
        Statement stmt= null;
        try {
            stmt = connection.createStatement();

            ResultSet rs=stmt.executeQuery("select * from logs where id = " + logid);
            if (!rs.next()){
                JOptionPane.showMessageDialog(null, "No log with that ID was found", MessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            //while(rs.next()) {

            //also update changes here
            String query3 = "select * from changes where logid = " + logid;

            Statement stmt3 = (Statement) connection.createStatement();
            ResultSet rs2=stmt3.executeQuery(query3);
            String created_at = null;
            if (rs2.next()) {
                created_at = rs2.getString(5);
            }
            String last_edited = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            String type = "Deletion";
            String query2 = "INSERT INTO changes (created_at, last_edited, type, logid, author, editor, body)"
                    + "VALUES ('" + created_at + "', '" + last_edited + "', '" + type + "', '" + logid + "', '" + rs.getString(2)
                     + "', '" + username + "', '" + rs.getString(3) + "')";
            Statement stmt2 = (Statement) connection.createStatement();
            stmt2.executeUpdate(query2);
            stmt = (Statement) connection.createStatement();
            String query1 = "delete from logs where id = " + logid;
            stmt.executeUpdate(query1);
            //update changes here
            //}
            JOptionPane.showMessageDialog(null, "Successfully deleted log with id: " + logid, MessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void createNewLog(){
        String author = username;
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
            String query4 = "SELECT * from logs where author = '" + author + "' and body = '" + body + "'";
            Statement stmt4 = (Statement) connection.createStatement();
            ResultSet rs4 = stmt4.executeQuery(query4);
            if (rs4.next()){
                JOptionPane.showMessageDialog(null, "A log with exactly identical author and body already exists. Please change.", MessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String query1 = "INSERT INTO logs (author, editors, body)" + "VALUES ('" + author + "', '" + "" + "', '" + body + "')";
            Statement stmt = (Statement) connection.createStatement();
            stmt.executeUpdate(query1);
            String query3 = "select * from logs where author = '" + author + "' and body = '" + body + "'";

            Statement stmt3 = (Statement) connection.createStatement();
            ResultSet rs=stmt3.executeQuery(query3);
            if (!rs.next()){
                JOptionPane.showMessageDialog(null, "No log with that ID was found", MessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int logid = rs.getInt(1);
            //also update changes here
            String created_at = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            String last_edited = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            String type = "Creation";
            String query2 = "INSERT INTO changes (created_at, last_edited, type, logid, author, editor, body)"
                    + "VALUES ('" + created_at + "', '" + last_edited + "', '" + type + "', '" + logid + "', '"
                    + author + "', '" + username + "', '"
                    + body + "')";
            Statement stmt2 = (Statement) connection.createStatement();
            stmt2.executeUpdate(query2);
            JOptionPane.showMessageDialog(null, "Inserted New Log Into Database Successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Insertion to Mysql Database Failure: " + e.toString());
        }
    }

    private static void saveFileDialog() {
        final JFileChooser saveAsFileChooser = new JFileChooser();
        saveAsFileChooser.setApproveButtonText("Save");
        saveAsFileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Text File", "txt"));
        saveAsFileChooser.addChoosableFileFilter(new FileNameExtensionFilter("HyperText Markup Language", "html"));
        int actionDialog = saveAsFileChooser.showOpenDialog(null);
        if (actionDialog != JFileChooser.APPROVE_OPTION) {
            return;
        }

        // !! File fileName = new File(SaveAs.getSelectedFile() + ".txt");
        File file = saveAsFileChooser.getSelectedFile();
        if (!file.getName().endsWith(".html") & (!file.getName().endsWith(".txt"))) {
            file = new File(file.getAbsolutePath() + ".txt");
        }

        BufferedWriter outFile = null;
        try {
            outFile = new BufferedWriter(new FileWriter(file));
            outFile.write(txtLogs.getText());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error writing to saved file", MessageBoxTitle, JOptionPane.WARNING_MESSAGE);
        } finally {
            if (outFile != null) {
                try {
                    outFile.close();
                } catch (IOException e) {}
            }
        }
    }

    static void filterLogs(){
        String filterby = JOptionPane.showInputDialog(null, "Filter by author or body", MessageBoxTitle, JOptionPane.QUESTION_MESSAGE);
        try {
            if (filterby != null){
                if (filterby.trim().equalsIgnoreCase("author")){
                    String keyword = JOptionPane.showInputDialog(null, "Enter filter keywords for author", MessageBoxTitle, JOptionPane.QUESTION_MESSAGE);
                    Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * from logs where author = '" + keyword + "'");
                    txtLogs.setText("");
                    while(rs.next())
                        if (txtLogs.getText().trim().equals("")) {
                            txtLogs.setText("ID: " + rs.getString(1) + "\r\nAuthor: " + rs.getString(2) + "\r\nEditors: " + rs.getString(4) + "\r\nBody: " + rs.getString(3));
                        }else{
                            txtLogs.append("\r\n--------------\r\nID: " + rs.getString(1) + "\r\nAuthor: " + rs.getString(2) + "\r\nEditors: " + rs.getString(4) + "\r\nBody: " + rs.getString(3));
                        }
                }else if (filterby.trim().equalsIgnoreCase("body")){
                    String keyword = JOptionPane.showInputDialog(null, "Enter filter keywords for author", MessageBoxTitle, JOptionPane.QUESTION_MESSAGE);
                    Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * from logs where body = '" + keyword + "'");
                    txtLogs.setText("");
                    while(rs.next())
                        if (txtLogs.getText().trim().equals("")) {
                            txtLogs.setText("ID: " + rs.getString(1) + "\r\nAuthor: " + rs.getString(2) + "\r\nEditors: " + rs.getString(4) + "\r\nBody: " + rs.getString(3));
                        }else{
                            txtLogs.append("\r\n--------------\r\nID: " + rs.getString(1) + "\r\nAuthor: " + rs.getString(2) + "\r\nEditors: " + rs.getString(4) + "\r\nBody: " + rs.getString(3));
                        }
                }else{
                    JOptionPane.showMessageDialog(null, "You can only filter by author or body. Nothing else at the moment.", MessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
