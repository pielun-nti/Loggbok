package models;

import config.Env;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class LogsModel {
    DBManager dbManager;
    User user;
    public LogsModel(User user){
        dbManager = new DBManager();
        this.user = user;
    }

    /**
     * Sparar textloggar till en fil på en viss sökväg. Den skriver det som läsbar UTF och inte binär kod.
     * @param path Sökväg till fil
     * @param data Text som ska sparas
     * @return boolean true/false
     */
    public boolean saveLogsToDataFile(String path, String data){
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(new FileOutputStream(path));
            out.writeUTF(data);
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Läser in data från en fil på en viss sökväg och läser UTF8
     * @param path Sökväg
     * @return Loggdata
     */
    public String readLogsFromDataFile(String path){
        DataInputStream in = null;
        try {
            in = new DataInputStream(new BufferedInputStream(new FileInputStream(path)));
            return in.readUTF();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Läser nästa rad i en fil vid en viss sökväg
     * @param path Sökväg
     * @return Loggdata
     */
    public String readLogsFromTextFile(String path){
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Öppnar JFileChooser dialog. Där användaren kan välja .txt eller .dat filer och programmet läser då in dem.
     * @return Text från filen användaren valt.
     */
    public String openFileDialog(){
        final JFileChooser openFileChooser = new JFileChooser();
        openFileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        openFileChooser.setApproveButtonText("Open Log");
        openFileChooser.setFileFilter(new FileNameExtensionFilter("Data File", "dat"));
        openFileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Txt file", "txt"));
        int actionDialog = openFileChooser.showOpenDialog(null);
        if (actionDialog != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        File file = openFileChooser.getSelectedFile();
        if (!file.getName().endsWith(".txt") & (!file.getName().endsWith("dat"))) {
            file = new File(file.getAbsolutePath() + ".dat");
        }
        if (file.getName().endsWith(".dat")){
            String data = readLogsFromDataFile(file.getAbsolutePath());
            if (data == null) {
                JOptionPane.showMessageDialog(null, "Error reading logs data from file: " + file.getAbsolutePath(), Env.LogsMessageBoxTitle, JOptionPane.WARNING_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "Opened log data successfully!", Env.LogsMessageBoxTitle, JOptionPane.WARNING_MESSAGE);
                return data;
            }
        }
        if (file.getName().endsWith(".txt")){
            String data = readLogsFromTextFile(file.getAbsolutePath());
            if (data == null) {
                JOptionPane.showMessageDialog(null, "Error reading logs data from file: " + file.getAbsolutePath(), Env.LogsMessageBoxTitle, JOptionPane.WARNING_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "Opened log data successfully!", Env.LogsMessageBoxTitle, JOptionPane.WARNING_MESSAGE);
                return data;
            }
        }
        return null;
    }

    /**
     * Kontaktar databasen med dbManager och väljer alla rader i tabellen logs och om det finns så returnar den resultatdatan.
     * @return ResultSet med alla loggar och dess data
     */
    public ResultSet getAllLogs(){
        ResultSet rs = dbManager.selectAll("logs");
        if (rs != null) {
            return rs;
        }
        return null;
    }
    public void editLog(){
        int logid = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter ID of the log you want to edit", Env.LogsMessageBoxTitle, JOptionPane.QUESTION_MESSAGE));
        if (logid < 0){
            JOptionPane.showMessageDialog(null, "ID Must be greater or equal to 0", Env.LogsMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            ArrayList<String> co = new ArrayList<>();
            ArrayList<String> va = new ArrayList<>();
            co.add("logid");
            va.add(Integer.toString(logid));
            ResultSet resultSet = dbManager.selectAllWhere("editing", co, va);
            if (resultSet.next()){
                JOptionPane.showMessageDialog(null, "Log with ID " + logid + " is already being edited by " + resultSet.getString(3), Env.LogsMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                return;
            }
            ArrayList<String> colu = new ArrayList<>();
            ArrayList<String> valu = new ArrayList<>();
            colu.add("logid");
            colu.add("editor");
            valu.add(Integer.toString(logid));
            valu.add(user.getUsername());
            dbManager.insert("editing", colu, valu);
            ArrayList<String> columns = new ArrayList<>();
            ArrayList<String> values = new ArrayList<>();
            columns.add("id");
            values.add(Integer.toString(logid));
            ResultSet rs = dbManager.selectAllWhere("logs", columns, values);
            if (!rs.next()){
                ArrayList<String> colo = new ArrayList<>();
                ArrayList<String> valo = new ArrayList<>();
                colo.add("logid");
                valo.add(Integer.toString(logid));
                dbManager.delete("editing", colo, valo);
                JOptionPane.showMessageDialog(null, "No log with that ID was found", Env.LogsMessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String logbody = (String) JOptionPane.showInputDialog(null, "Edit body for log id: " + logid, Env.LogsMessageBoxTitle, JOptionPane.QUESTION_MESSAGE, null, null, rs.getString(3));
            if (logbody == null){
                ArrayList<String> colo = new ArrayList<>();
                ArrayList<String> valo = new ArrayList<>();
                colo.add("logid");
                valo.add(Integer.toString(logid));
                dbManager.delete("editing", colo, valo);
                JOptionPane.showMessageDialog(null, "Body cannot be null.", Env.LogsMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (logbody.trim().equals("")){
                JOptionPane.showMessageDialog(null, "Body must have an value.", Env.LogsMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                ArrayList<String> colo = new ArrayList<>();
                ArrayList<String> valo = new ArrayList<>();
                colo.add("logid");
                valo.add(Integer.toString(logid));
                dbManager.delete("editing", colo, valo);
                return;
            }
            ArrayList<String> col = new ArrayList<>();
            ArrayList<String> val = new ArrayList<>();
            col.add("author");
            col.add("body");
            val.add(rs.getString(2));
            val.add(logbody);
            ResultSet rs4 = dbManager.selectAllWhere("logs", col, val);
            if (rs4.next()){
                ArrayList<String> colo = new ArrayList<>();
                ArrayList<String> valo = new ArrayList<>();
                colo.add("logid");
                valo.add(Integer.toString(logid));
                dbManager.delete("editing", colo, valo);
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
            ArrayList<String> setcol = new ArrayList<>();
            ArrayList<String> setval = new ArrayList<>();
            ArrayList<String> filtercol = new ArrayList<>();
            ArrayList<String> filterval = new ArrayList<>();
            setcol.add("editors");
            setval.add(editors);
            filtercol.add("id");
            filterval.add(Integer.toString(logid));
            dbManager.edit("logs", filtercol, filterval, setcol, setval);
            ArrayList<String> c = new ArrayList<>();
            ArrayList<String> v = new ArrayList<>();
            c.add("logid");
            v.add(Integer.toString(logid));

            if (!logbody.trim().equals(rs.getString(2))){
                ArrayList<String> setcol2 = new ArrayList<>();
                ArrayList<String> setval2 = new ArrayList<>();
                ArrayList<String> filtercol2 = new ArrayList<>();
                ArrayList<String> filterval2 = new ArrayList<>();
                setcol2.add("body");
                setval2.add(logbody);
                filtercol2.add("id");
                filterval2.add(Integer.toString(logid));
                dbManager.edit("logs", filtercol2, filterval2, setcol2, setval2);
                ArrayList<String> colo = new ArrayList<>();
                ArrayList<String> valo = new ArrayList<>();
                colo.add("logid");
                valo.add(Integer.toString(logid));
                dbManager.delete("editing", colo, valo);
                ResultSet rs2= dbManager.selectAllWhere("editing", c, v);
                String created_at = null;
                if (rs2.next()) {
                    created_at = rs2.getString(5);
                }
                String last_edited = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
                String type = "Editing";
                ArrayList<String> changescol = new ArrayList<>();
                ArrayList<String> changesval = new ArrayList<>();
                changescol.add("created_at");
                changescol.add("last_edited");
                changescol.add("type");
                changescol.add("logid");
                changescol.add("author");
                changescol.add("editor");
                changescol.add("body");
                changesval.add(created_at);
                changesval.add(last_edited);
                changesval.add(type);
                changesval.add(Integer.toString(logid));
                changesval.add(rs.getString(2));
                changesval.add(user.getUsername());
                changesval.add(logbody);
                dbManager.insert("changes", changescol, changesval);
            }
            JOptionPane.showMessageDialog(null, "Successfully updated author and body for log id: " + logid, Env.LogsMessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            ArrayList<String> colo = new ArrayList<>();
            ArrayList<String> valo = new ArrayList<>();
            colo.add("logid");
            valo.add(Integer.toString(logid));
            dbManager.delete("editing", colo, valo);
        }
    }

    public void deleteLog(){
        int logid = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter ID of the log you want to delete", Env.LogsMessageBoxTitle, JOptionPane.QUESTION_MESSAGE));
        if (logid < 0){
            JOptionPane.showMessageDialog(null, "ID Must be greater or equal to 0", Env.LogsMessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try {
            ArrayList<String> col = new ArrayList<>();
            ArrayList<String> val = new ArrayList<>();
            col.add("id");
            val.add(Integer.toString(logid));
            ResultSet rs = dbManager.selectAllWhere("logs", col, val);
            if (!rs.next()){
                JOptionPane.showMessageDialog(null, "No log with that ID was found", Env.LogsMessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            ArrayList<String> co = new ArrayList<>();
            ArrayList<String> va = new ArrayList<>();
            co.add("logid");
            va.add(Integer.toString(logid));
            ResultSet rs2= dbManager.selectAllWhere("changes", co, va);
            String created_at = null;
            if (rs2.next()) {
                created_at = rs2.getString(5);
            }
            String last_edited = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            String type = "Deletion";
            ArrayList<String> changescol = new ArrayList<>();
            ArrayList<String> changesval = new ArrayList<>();
            changescol.add("created_at");
            changescol.add("last_edited");
            changescol.add("type");
            changescol.add("logid");
            changescol.add("author");
            changescol.add("editor");
            changescol.add("body");
            changesval.add(created_at);
            changesval.add(last_edited);
            changesval.add(type);
            changesval.add(Integer.toString(logid));
            changesval.add(rs.getString(2));
            changesval.add(user.getUsername());
            changesval.add(rs.getString(3));
            dbManager.insert("changes", changescol, changesval);
            ArrayList<String> c = new ArrayList<>();
            ArrayList<String> v = new ArrayList<>();
            c.add("id");
            v.add(Integer.toString(logid));
            dbManager.delete("logs", c, v);
            JOptionPane.showMessageDialog(null, "Successfully deleted log with id: " + logid, Env.LogsMessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            ArrayList<String> col = new ArrayList<>();
            ArrayList<String> val = new ArrayList<>();
            col.add("author");
            col.add("body");
            val.add(author);
            val.add(body);
            ResultSet rs4 = dbManager.selectAllWhere("logs", col, val);
            if (rs4.next()){
                JOptionPane.showMessageDialog(null, "A log with exactly identical author and body already exists. Please change.", Env.LogsMessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            ArrayList<String> c = new ArrayList<>();
            ArrayList<String> v = new ArrayList<>();
            c.add("author");
            c.add("editors");
            c.add("body");
            v.add(author);
            v.add("");
            v.add(body);
            dbManager.insert("logs", c, v);
            ArrayList<String> co = new ArrayList<>();
            ArrayList<String> va = new ArrayList<>();
            co.add("author");
            co.add("body");
            va.add(author);
            va.add(body);
            ResultSet rs= dbManager.selectAllWhere("logs", co, va);
            if (!rs.next()){
                JOptionPane.showMessageDialog(null, "No log with that ID was found", Env.LogsMessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int logid = rs.getInt(1);
            String created_at = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            String last_edited = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            String type = "Creation";
            ArrayList<String> changescol = new ArrayList<>();
            ArrayList<String> changesval = new ArrayList<>();
            changescol.add("created_at");
            changescol.add("last_edited");
            changescol.add("type");
            changescol.add("logid");
            changescol.add("author");
            changescol.add("editor");
            changescol.add("body");
            changesval.add(created_at);
            changesval.add(last_edited);
            changesval.add(type);
            changesval.add(Integer.toString(logid));
            changesval.add(rs.getString(2));
            changesval.add(user.getUsername());
            changesval.add(rs.getString(3));
            dbManager.insert("changes", changescol, changesval);
            JOptionPane.showMessageDialog(null, "Inserted New Log Into Database Successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Insertion to Mysql Database Failure: " + e.toString());
        }
    }

    public ResultSet filterLogs(){
        String filterby = JOptionPane.showInputDialog(null, "Filter by author or body", Env.LogsMessageBoxTitle, JOptionPane.QUESTION_MESSAGE);
            if (filterby != null){
                if (filterby.trim().equalsIgnoreCase("author")){
                    String keyword = JOptionPane.showInputDialog(null, "Enter filter keywords for author", Env.LogsMessageBoxTitle, JOptionPane.QUESTION_MESSAGE);
                    ArrayList<String> col = new ArrayList<>();
                    ArrayList<String> val = new ArrayList<>();
                    col.add("author");
                    val.add(keyword);
                    ResultSet rs = dbManager.selectAllWhere("logs", col, val);
                    return rs;
                }else if (filterby.trim().equalsIgnoreCase("body")){
                    String keyword = JOptionPane.showInputDialog(null, "Enter filter keywords for author", Env.LogsMessageBoxTitle, JOptionPane.QUESTION_MESSAGE);
                    ArrayList<String> col = new ArrayList<>();
                    ArrayList<String> val = new ArrayList<>();
                    col.add("body");
                    val.add(keyword);
                    ResultSet rs = dbManager.selectAllWhere("logs", col, val);
                    return rs;
                }else{
                    JOptionPane.showMessageDialog(null, "You can only filter by author or body. Nothing else at the moment.", Env.LogsMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                    return null;
                }
            }
        return null;
    }

    public void saveFileDialog(String textToSave) {
        final JFileChooser saveAsFileChooser = new JFileChooser();
        saveAsFileChooser.setApproveButtonText("Save Log");
        saveAsFileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Data File", "dat"));
        saveAsFileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Text File", "txt"));
        int actionDialog = saveAsFileChooser.showOpenDialog(null);
        if (actionDialog != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File file = saveAsFileChooser.getSelectedFile();
        if (!file.getName().endsWith(".dat") & (!file.getName().endsWith(".txt"))) {
            file = new File(file.getAbsolutePath() + ".dat");
        }
        boolean saved = saveLogsToDataFile(file.getAbsolutePath(), textToSave);
        if (!saved) {
            JOptionPane.showMessageDialog(null, "Error writing to saved file", Env.LogsMessageBoxTitle, JOptionPane.WARNING_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(null, "Saved log to file", Env.LogsMessageBoxTitle, JOptionPane.WARNING_MESSAGE);
        }
    }

    public ResultSet getLogHistory(){
        ResultSet rs = dbManager.selectAll("changes");
        return rs;
    }

    public void deleteAllLogHistory(){
            int deletedRows = dbManager.deleteAll("changes");
            if(deletedRows>0){
                JOptionPane.showMessageDialog(null, "Successfully deleted all log changes history", Env.LogsMessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "There are no log changes history to delete", Env.LogsMessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
            }
    }

    public void deleteAllLogs(){
            int deletedRows = dbManager.deleteAll("logs");
            if(deletedRows>0){
                String last_edited = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
                String type = "Deletion of all logs";
                ArrayList<String> changescol = new ArrayList<>();
                ArrayList<String> changesval = new ArrayList<>();
                changescol.add("created_at");
                changescol.add("last_edited");
                changescol.add("type");
                changescol.add("logid");
                changescol.add("author");
                changescol.add("editor");
                changescol.add("body");
                changesval.add("");
                changesval.add(last_edited);
                changesval.add(type);
                changesval.add("404");
                changesval.add("");
                changesval.add(user.getUsername());
                changesval.add("");
                dbManager.insert("changes", changescol, changesval);
                JOptionPane.showMessageDialog(null, "Successfully deleted all logs", Env.LogsMessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "There are no logs to delete", Env.LogsMessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
            }
    }
}