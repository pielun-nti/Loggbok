package models;

import config.Env;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LogsModel {
    DB db;
    User user;
    public LogsModel(User user){
        db = new DB();
        if (!db.initDB()){
            JOptionPane.showMessageDialog(null, "Init DB Error!", Env.LogsMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
            return;
        }
        this.user = user;
    }
    public ResultSet getAllLogs(){
        Statement stmt= null;
        try {
            stmt = db.getConnection().createStatement();

            ResultSet rs=stmt.executeQuery("select * from logs");
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void editLog(){
        int logid = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter ID of the log you want to edit", Env.LogsMessageBoxTitle, JOptionPane.QUESTION_MESSAGE));
        if (logid < 0){
            JOptionPane.showMessageDialog(null, "ID Must be greater or equal to 0", Env.LogsMessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        //get info from the log, show it then allow editing
        Statement stmt= null;
        try {
            stmt = db.getConnection().createStatement();

            ResultSet rs=stmt.executeQuery("select * from logs where id = " + logid);
            if (!rs.next()){
                JOptionPane.showMessageDialog(null, "No log with that ID was found", Env.LogsMessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String logbody = (String) JOptionPane.showInputDialog(null, "Edit body for log id: " + logid, Env.LogsMessageBoxTitle, JOptionPane.QUESTION_MESSAGE, null, null, rs.getString(3));
            if (logbody == null){
                JOptionPane.showMessageDialog(null, "Body cannot be null.", Env.LogsMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (logbody.trim().equals("")){
                JOptionPane.showMessageDialog(null, "Body must have an value.", Env.LogsMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                return;
            }
            String query4 = "SELECT * from logs where author = '" + rs.getString(2) + "' and body = '" + logbody + "'";
            Statement stmt4 = (Statement) db.getConnection().createStatement();
            ResultSet rs4 = stmt4.executeQuery(query4);
            if (rs4.next()){
                JOptionPane.showMessageDialog(null, "A log with exactly identical author and body already exists. Please change.", Env.LogsMessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String editors = rs.getString(4);
            if (editors != null) {
                if (!editors.contains(user.getUsername())) {
                    if (editors.trim().equals("")){
                        editors = user.getUsername();
                    }else {
                        editors += ", " + user.getUsername();
                    }
                }
            }
            stmt = (Statement) db.getConnection().createStatement();
            String query6 = "update logs set editors='" + editors + "' " + "where id in(" + logid + ")";
            stmt.executeUpdate(query6);
            //also updates changes here
            String query3 = "select * from changes where logid = " + logid;

            if (!logbody.trim().equals(rs.getString(2))){
                stmt = (Statement) db.getConnection().createStatement();
                String query1 = "update logs set body='" + logbody + "' " + "where id in(" + logid + ")";
                stmt.executeUpdate(query1);
                Statement stmt3 = (Statement) db.getConnection().createStatement();
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
                        + user.getUsername() + "', '"
                        + logbody + "')";
                Statement stmt2 = (Statement) db.getConnection().createStatement();
                stmt2.executeUpdate(query2);
            }
            JOptionPane.showMessageDialog(null, "Successfully updated author and body for log id: " + logid, Env.LogsMessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteLog(){
        int logid = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter ID of the log you want to delete", Env.LogsMessageBoxTitle, JOptionPane.QUESTION_MESSAGE));
        if (logid < 0){
            JOptionPane.showMessageDialog(null, "ID Must be greater or equal to 0", Env.LogsMessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Statement stmt= null;
        try {
            stmt = db.getConnection().createStatement();

            ResultSet rs=stmt.executeQuery("select * from logs where id = " + logid);
            if (!rs.next()){
                JOptionPane.showMessageDialog(null, "No log with that ID was found", Env.LogsMessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String query3 = "select * from changes where logid = " + logid;

            Statement stmt3 = (Statement) db.getConnection().createStatement();
            ResultSet rs2=stmt3.executeQuery(query3);
            String created_at = null;
            if (rs2.next()) {
                created_at = rs2.getString(5);
            }
            String last_edited = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            String type = "Deletion";
            String query2 = "INSERT INTO changes (created_at, last_edited, type, logid, author, editor, body)"
                    + "VALUES ('" + created_at + "', '" + last_edited + "', '" + type + "', '" + logid + "', '" + rs.getString(2)
                    + "', '" + user.getUsername() + "', '" + rs.getString(3) + "')";
            Statement stmt2 = (Statement) db.getConnection().createStatement();
            stmt2.executeUpdate(query2);
            stmt = (Statement) db.getConnection().createStatement();
            String query1 = "delete from logs where id = " + logid;
            stmt.executeUpdate(query1);
            JOptionPane.showMessageDialog(null, "Successfully deleted log with id: " + logid, Env.LogsMessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DB getDb() {
        return db;
    }
    
    public void setDB(DB db){
        this.db = db;
    }

    public void createNewLog(){
        String author = user.getUsername();
        String body = (JOptionPane.showInputDialog(null, "Enter log body", Env.LogsMessageBoxTitle, JOptionPane.QUESTION_MESSAGE));
        if (author == null || body == null){
            JOptionPane.showMessageDialog(null, "Author or body cannot be null.", Env.LogsMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (author.trim().equals("") || body.trim().equals("")){
            JOptionPane.showMessageDialog(null, "Author and body must have an value.", Env.LogsMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            String query4 = "SELECT * from logs where author = '" + author + "' and body = '" + body + "'";
            Statement stmt4 = (Statement) db.getConnection().createStatement();
            ResultSet rs4 = stmt4.executeQuery(query4);
            if (rs4.next()){
                JOptionPane.showMessageDialog(null, "A log with exactly identical author and body already exists. Please change.", Env.LogsMessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String query1 = "INSERT INTO logs (author, editors, body)" + "VALUES ('" + author + "', '" + "" + "', '" + body + "')";
            Statement stmt = (Statement) db.getConnection().createStatement();
            stmt.executeUpdate(query1);
            String query3 = "select * from logs where author = '" + author + "' and body = '" + body + "'";
            Statement stmt3 = (Statement) db.getConnection().createStatement();
            ResultSet rs=stmt3.executeQuery(query3);
            if (!rs.next()){
                JOptionPane.showMessageDialog(null, "No log with that ID was found", Env.LogsMessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int logid = rs.getInt(1);
            String created_at = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            String last_edited = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            String type = "Creation";
            String query2 = "INSERT INTO changes (created_at, last_edited, type, logid, author, editor, body)"
                    + "VALUES ('" + created_at + "', '" + last_edited + "', '" + type + "', '" + logid + "', '"
                    + author + "', '" + user.getUsername() + "', '"
                    + body + "')";
            Statement stmt2 = (Statement) db.getConnection().createStatement();
            stmt2.executeUpdate(query2);
            JOptionPane.showMessageDialog(null, "Inserted New Log Into Database Successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Insertion to Mysql Database Failure: " + e.toString());
        }
    }

    public ResultSet filterLogs(){
        String filterby = JOptionPane.showInputDialog(null, "Filter by author or body", Env.LogsMessageBoxTitle, JOptionPane.QUESTION_MESSAGE);
        try {
            if (filterby != null){
                if (filterby.trim().equalsIgnoreCase("author")){
                    String keyword = JOptionPane.showInputDialog(null, "Enter filter keywords for author", Env.LogsMessageBoxTitle, JOptionPane.QUESTION_MESSAGE);
                    Statement stmt = db.getConnection().createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * from logs where author = '" + keyword + "'");
                    return rs;
                }else if (filterby.trim().equalsIgnoreCase("body")){
                    String keyword = JOptionPane.showInputDialog(null, "Enter filter keywords for author", Env.LogsMessageBoxTitle, JOptionPane.QUESTION_MESSAGE);
                    Statement stmt = db.getConnection().createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * from logs where body = '" + keyword + "'");
                    return rs;
                }else{
                    JOptionPane.showMessageDialog(null, "You can only filter by author or body. Nothing else at the moment.", Env.LogsMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveFileDialog(String textToSave) {
        final JFileChooser saveAsFileChooser = new JFileChooser();
        saveAsFileChooser.setApproveButtonText("Save");
        saveAsFileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Text File", "txt"));
        saveAsFileChooser.addChoosableFileFilter(new FileNameExtensionFilter("HyperText Markup Language", "html"));
        int actionDialog = saveAsFileChooser.showOpenDialog(null);
        if (actionDialog != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File file = saveAsFileChooser.getSelectedFile();
        if (!file.getName().endsWith(".html") & (!file.getName().endsWith(".txt"))) {
            file = new File(file.getAbsolutePath() + ".txt");
        }
        BufferedWriter outFile = null;
        try {
            outFile = new BufferedWriter(new FileWriter(file));
            outFile.write(textToSave);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error writing to saved file", Env.LogsMessageBoxTitle, JOptionPane.WARNING_MESSAGE);
        } finally {
            if (outFile != null) {
                try {
                    outFile.close();
                } catch (IOException e) {}
            }
        }
    }

    public ResultSet getLogHistory(){
        Statement stmt= null;
        try {
            stmt = db.getConnection().createStatement();

            ResultSet rs=stmt.executeQuery("select * from changes");
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteAllLogHistory(){
        Statement stmt= null;
        try {
            stmt = db.getConnection().createStatement();

            int deletedRows =stmt.executeUpdate("DELETE from changes");
            if(deletedRows>0){
                JOptionPane.showMessageDialog(null, "Successfully deleted all log changes history", Env.LogsMessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "There are no log changes history to delete", Env.LogsMessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAllLogs(){
        Statement stmt= null;
        try {
            stmt = db.getConnection().createStatement();

            int deletedRows =stmt.executeUpdate("DELETE from logs");
            if(deletedRows>0){
                String last_edited = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
                String type = "Deletion of all logs";
                String query2 = "INSERT INTO changes (created_at, last_edited, type, logid, author, editor, body)"
                        + "VALUES ('" + "" + "', '" + last_edited + "', '" + type + "', '" + "404" + "', '" + ""
                        + "', '" + user.getUsername() + "', '" + "" + "')";
                Statement stmt2 = (Statement) db.getConnection().createStatement();
                stmt2.executeUpdate(query2);
                JOptionPane.showMessageDialog(null, "Successfully deleted all logs", Env.LogsMessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "There are no logs to delete", Env.LogsMessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}