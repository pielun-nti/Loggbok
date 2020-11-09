package core;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LogsController{
    Connection connection;
    LogsView view;
    LogsModel model;

    public LogsController(LogsView view, LogsModel model){
        this.view = view;
        this.model = model;
        if (!initDB()){
            JOptionPane.showMessageDialog(null, "Init DB Error!", view.getMessageBoxTitle(), JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        this.view.addListeners(new LogsListener());
        this.view.addFrameWindowListener(new FrameWindowListener());
    }

    private class FrameWindowListener implements WindowListener {

        @Override
        public void windowOpened(WindowEvent windowEvent) {

        }

        @Override
        public void windowClosing(WindowEvent windowEvent) {
                    view.getFrame().dispose();
            }

        @Override
        public void windowClosed(WindowEvent windowEvent) {

        }

        @Override
        public void windowIconified(WindowEvent windowEvent) {

        }

        @Override
        public void windowDeiconified(WindowEvent windowEvent) {

        }

        @Override
        public void windowActivated(WindowEvent windowEvent) {

        }

        @Override
        public void windowDeactivated(WindowEvent windowEvent) {


        }
    }

    private class LogsListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String command = actionEvent.getActionCommand();
            System.out.println("Executed command: " + command);
            if (command != null){
                if (command.equalsIgnoreCase("Create new log")){
                    createNewLog();
                }
                if (command.equalsIgnoreCase("Edit log")){
                    editLog();
                }
                if (command.equalsIgnoreCase("Delete Log")){
                    deleteLog();
                }
                if (command.equalsIgnoreCase("Get all logs")){
                    getAllLogs();
                }
                if (command.equalsIgnoreCase("Get Logs Changes History")){
                    getLogHistory();
                }
                if (command.equalsIgnoreCase("Delete all logs")){
                    deleteAllLogs();
                }
                if (command.equalsIgnoreCase("Delete all log changes history")){
                    deleteAllLogHistory();
                }
                if (command.equalsIgnoreCase("Filter logs")){
                    filterLogs();
                }
                if (command.equalsIgnoreCase("Save as")){
                    saveFileDialog();
                }
                if (command.equalsIgnoreCase("Logout")){
                    Logout();
                }
                if (command.equalsIgnoreCase("Exit application")){
                    view.getFrame().dispose();
                }
                if (command.equalsIgnoreCase("Change font size")){
                    int fontSize = view.getFontSize();
                    try {
                        fontSize = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter new font size ( current = " + view.getFontSize() + ")", view.getMessageBoxTitle(), JOptionPane.INFORMATION_MESSAGE));
                    } catch (NumberFormatException ex){
                        ex.printStackTrace();
                        view.displayErrorMsg(ex.toString());
                    }
                    Font mainFont = new Font("Verdana", Font.BOLD, fontSize);
                    view.setMainFont(mainFont);
                    view.getTxtLogs().setFont(mainFont);
                    view.getFrame().setFont(mainFont);
                }
                if (command.equalsIgnoreCase("About")){
                    JOptionPane.showMessageDialog(null, "Made by Pierre Lundström", view.getMessageBoxTitle(), JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
        }

        void Logout(){
        Login login = new Login();
        view.getFrame().dispose();
        }

    void getAllLogs(){
        Statement stmt= null;
        try {
            stmt = connection.createStatement();

            ResultSet rs=stmt.executeQuery("select * from logs");
            view.setLogsTXT("");
            while(rs.next())
                if (view.getLogsTXT().trim().equals("")) {
                    view.setLogsTXT("ID: " + rs.getString(1) + "\r\nAuthor: " + rs.getString(2) + "\r\nEditors: " + rs.getString(4) + "\r\nBody: " + rs.getString(3));
                }else{
                    view.appendLogsTXT("\r\n--------------\r\nID: " + rs.getString(1) + "\r\nAuthor: " + rs.getString(2) + "\r\nEditors: " + rs.getString(4) + "\r\nBody: " + rs.getString(3));
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void deleteAllLogs(){
        Statement stmt= null;
        try {
            stmt = connection.createStatement();

            int deletedRows =stmt.executeUpdate("DELETE from logs");
            if(deletedRows>0){
                String last_edited = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
                String type = "Deletion of all logs";
                String query2 = "INSERT INTO changes (created_at, last_edited, type, logid, author, editor, body)"
                        + "VALUES ('" + "" + "', '" + last_edited + "', '" + type + "', '" + "404" + "', '" + ""
                        + "', '" + view.getUsername() + "', '" + "" + "')";
                Statement stmt2 = (Statement) connection.createStatement();
                stmt2.executeUpdate(query2);
                JOptionPane.showMessageDialog(null, "Successfully deleted all logs", view.getMessageBoxTitle(), JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "There are no logs to delete", view.getMessageBoxTitle(), JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void deleteAllLogHistory(){
        Statement stmt= null;
        try {
            stmt = connection.createStatement();

            int deletedRows =stmt.executeUpdate("DELETE from changes");
            if(deletedRows>0){
                JOptionPane.showMessageDialog(null, "Successfully deleted all log changes history", view.getMessageBoxTitle(), JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "There are no log changes history to delete", view.getMessageBoxTitle(), JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void getLogHistory(){
        Statement stmt= null;
        try {
            stmt = connection.createStatement();

            ResultSet rs=stmt.executeQuery("select * from changes");
            view.setLogsTXT("");
            while(rs.next())
                if (view.getLogsTXT().trim().equals("")) {
                    view.setLogsTXT("ID: " + rs.getString(1) + "\r\nLog ID: " + rs.getString(2) + "\r\nAuthor: " + rs.getString(3)
                            + "\r\nEditor: " + rs.getString(8)
                            + "\r\nBody: " + rs.getString(4) + "\r\nCreated At: " + rs.getString(5) + "\r\nLast Edited: " + rs.getString(6)
                            + "\r\nType Of Change: " + rs.getString(7
                    ));
                }else{
                    view.appendLogsTXT("\r\n--------------\r\nID: " + rs.getString(1) + "\r\nLog ID:" + rs.getString(2)
                            + "\r\nAuthor: "
                            + rs.getString(3)
                            + "\r\nEditor: " + rs.getString(8) + "\r\nBody: " + rs.getString(4) + "\r\nCreated At: " + rs.getString(5)
                            + "\r\nLast Edited: " + rs.getString(6) + "\r\nType Of Change: " + rs.getString(7));
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void editLog(){
        int logid = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter ID of the log you want to edit", view.getMessageBoxTitle(), JOptionPane.QUESTION_MESSAGE));
        if (logid < 0){
            JOptionPane.showMessageDialog(null, "ID Must be greater or equal to 0", view.getMessageBoxTitle(), JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        //get info from the log, show it then allow editing
        Statement stmt= null;
        try {
            stmt = connection.createStatement();

            ResultSet rs=stmt.executeQuery("select * from logs where id = " + logid);
            if (!rs.next()){
                JOptionPane.showMessageDialog(null, "No log with that ID was found", view.getMessageBoxTitle(), JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String logbody = (String) JOptionPane.showInputDialog(null, "Edit body for log id: " + logid, view.getMessageBoxTitle(), JOptionPane.QUESTION_MESSAGE, null, null, rs.getString(3));
            if (logbody == null){
                JOptionPane.showMessageDialog(null, "Body cannot be null.", view.getMessageBoxTitle(), JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (logbody.trim().equals("")){
                JOptionPane.showMessageDialog(null, "Body must have an value.", view.getMessageBoxTitle(), JOptionPane.ERROR_MESSAGE);
                return;
            }
            String query4 = "SELECT * from logs where author = '" + rs.getString(2) + "' and body = '" + logbody + "'";
            Statement stmt4 = (Statement) connection.createStatement();
            ResultSet rs4 = stmt4.executeQuery(query4);
            if (rs4.next()){
                JOptionPane.showMessageDialog(null, "A log with exactly identical author and body already exists. Please change.", view.getMessageBoxTitle(), JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String editors = rs.getString(4);
            if (editors != null) {
                if (!editors.contains(view.getUsername())) {
                    if (editors.trim().equals("")){
                        editors = view.getUsername();
                    }else {
                        editors += ", " + view.getUsername();
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
                        + view.getUsername() + "', '"
                        + logbody + "')";
                Statement stmt2 = (Statement) connection.createStatement();
                stmt2.executeUpdate(query2);
            }
            //}
            JOptionPane.showMessageDialog(null, "Successfully updated author and body for log id: " + logid, view.getMessageBoxTitle(), JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void deleteLog(){
        int logid = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter ID of the log you want to delete", view.getMessageBoxTitle(), JOptionPane.QUESTION_MESSAGE));
        if (logid < 0){
            JOptionPane.showMessageDialog(null, "ID Must be greater or equal to 0", view.getMessageBoxTitle(), JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        //get info from the log, show it then allow editing
        Statement stmt= null;
        try {
            stmt = connection.createStatement();

            ResultSet rs=stmt.executeQuery("select * from logs where id = " + logid);
            if (!rs.next()){
                JOptionPane.showMessageDialog(null, "No log with that ID was found", view.getMessageBoxTitle(), JOptionPane.INFORMATION_MESSAGE);
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
                    + "', '" + view.getUsername() + "', '" + rs.getString(3) + "')";
            Statement stmt2 = (Statement) connection.createStatement();
            stmt2.executeUpdate(query2);
            stmt = (Statement) connection.createStatement();
            String query1 = "delete from logs where id = " + logid;
            stmt.executeUpdate(query1);
            //update changes here
            //}
            JOptionPane.showMessageDialog(null, "Successfully deleted log with id: " + logid, view.getMessageBoxTitle(), JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void createNewLog(){
        String author = view.getUsername();
        String body = (JOptionPane.showInputDialog(null, "Enter log body", view.getMessageBoxTitle(), JOptionPane.QUESTION_MESSAGE));
        if (author == null || body == null){
            JOptionPane.showMessageDialog(null, "Author or body cannot be null.", view.getMessageBoxTitle(), JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (author.trim().equals("") || body.trim().equals("")){
            JOptionPane.showMessageDialog(null, "Author and body must have an value.", view.getMessageBoxTitle(), JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            String query4 = "SELECT * from logs where author = '" + author + "' and body = '" + body + "'";
            Statement stmt4 = (Statement) connection.createStatement();
            ResultSet rs4 = stmt4.executeQuery(query4);
            if (rs4.next()){
                JOptionPane.showMessageDialog(null, "A log with exactly identical author and body already exists. Please change.", view.getMessageBoxTitle(), JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String query1 = "INSERT INTO logs (author, editors, body)" + "VALUES ('" + author + "', '" + "" + "', '" + body + "')";
            Statement stmt = (Statement) connection.createStatement();
            stmt.executeUpdate(query1);
            String query3 = "select * from logs where author = '" + author + "' and body = '" + body + "'";

            Statement stmt3 = (Statement) connection.createStatement();
            ResultSet rs=stmt3.executeQuery(query3);
            if (!rs.next()){
                JOptionPane.showMessageDialog(null, "No log with that ID was found", view.getMessageBoxTitle(), JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int logid = rs.getInt(1);
            //also update changes here
            String created_at = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            String last_edited = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            String type = "Creation";
            String query2 = "INSERT INTO changes (created_at, last_edited, type, logid, author, editor, body)"
                    + "VALUES ('" + created_at + "', '" + last_edited + "', '" + type + "', '" + logid + "', '"
                    + author + "', '" + view.getUsername() + "', '"
                    + body + "')";
            Statement stmt2 = (Statement) connection.createStatement();
            stmt2.executeUpdate(query2);
            JOptionPane.showMessageDialog(null, "Inserted New Log Into Database Successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Insertion to Mysql Database Failure: " + e.toString());
        }
    }

    private void saveFileDialog() {
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
            outFile.write(view.getLogsTXT());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error writing to saved file", view.getMessageBoxTitle(), JOptionPane.WARNING_MESSAGE);
        } finally {
            if (outFile != null) {
                try {
                    outFile.close();
                } catch (IOException e) {}
            }
        }
    }

    void filterLogs(){
        String filterby = JOptionPane.showInputDialog(null, "Filter by author or body", view.getMessageBoxTitle(), JOptionPane.QUESTION_MESSAGE);
        try {
            if (filterby != null){
                if (filterby.trim().equalsIgnoreCase("author")){
                    String keyword = JOptionPane.showInputDialog(null, "Enter filter keywords for author", view.getMessageBoxTitle(), JOptionPane.QUESTION_MESSAGE);
                    Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * from logs where author = '" + keyword + "'");
                    view.setLogsTXT("");
                    while(rs.next())
                        if (view.getLogsTXT().trim().equals("")) {
                            view.setLogsTXT("ID: " + rs.getString(1) + "\r\nAuthor: " + rs.getString(2) + "\r\nEditors: " + rs.getString(4) + "\r\nBody: " + rs.getString(3));
                        }else{
                            view.appendLogsTXT("\r\n--------------\r\nID: " + rs.getString(1) + "\r\nAuthor: " + rs.getString(2) + "\r\nEditors: " + rs.getString(4) + "\r\nBody: " + rs.getString(3));
                        }
                }else if (filterby.trim().equalsIgnoreCase("body")){
                    String keyword = JOptionPane.showInputDialog(null, "Enter filter keywords for author", view.getMessageBoxTitle(), JOptionPane.QUESTION_MESSAGE);
                    Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * from logs where body = '" + keyword + "'");
                    view.setLogsTXT("");
                    while(rs.next())
                        if (view.getLogsTXT().trim().equals("")) {
                            view.setLogsTXT("ID: " + rs.getString(1) + "\r\nAuthor: " + rs.getString(2) + "\r\nEditors: " + rs.getString(4) + "\r\nBody: " + rs.getString(3));
                        }else{
                            view.appendLogsTXT("\r\n--------------\r\nID: " + rs.getString(1) + "\r\nAuthor: " + rs.getString(2) + "\r\nEditors: " + rs.getString(4) + "\r\nBody: " + rs.getString(3));
                        }
                }else{
                    JOptionPane.showMessageDialog(null, "You can only filter by author or body. Nothing else at the moment.", view.getMessageBoxTitle(), JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean initDB(){
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
