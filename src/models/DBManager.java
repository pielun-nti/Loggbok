package models;

import config.Env;

import javax.swing.*;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBManager {
    DB db;
    public DBManager(){
        db = new DB();
        if (!db.initDB()){
            JOptionPane.showMessageDialog(null, "Init DB Error!", Env.DBMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
    public boolean checkDuplicate(DB db, String table, ArrayList<String> columns, ArrayList<String> values){
        Statement stmt = null;
        try {
            stmt = db.getConnection().createStatement();
            //String queryold = "SELECT * from logs where author = '" + author + "' and body = '" + body + "'";
            String query =
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void checkDuplicate(String table, String text){

    }
    public boolean insert(DB db, String table, ArrayList<String> columns, ArrayList<String> values){
        Statement stmt = null;
        try {
            stmt = db.getConnection().createStatement();
            String query = "INSERT INTO " + table + " (";
            for (int i = 0; i < columns.size(); i++) {
                if (columns.get(i) != null) {
                    query += columns.get(i) + ", ";
                }
            }
                query += ")VALUES (";
            for (int i = 0; i < values.size(); i++){
                query += "'" + values.get(i) + "', ";
            }
            query += ")";
            stmt.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }
    public boolean insert(String table, ArrayList<String> columns, ArrayList<String> values){
        Statement stmt = null;
        try {
            stmt = db.getConnection().createStatement();
            String query = "INSERT INTO " + table + " (";
            for (int i = 0; i < columns.size(); i++) {
                if (columns.get(i) != null) {
                    if (i == (values.size() - 1)){
                     query += columns.get(i);
                    }else {
                        query += columns.get(i) + ", ";
                    }
                }
            }
            query += ")VALUES (";
            for (int i = 0; i < values.size(); i++){
                if (i == (values.size() - 1)){
                    query += "'" + values.get(i) + "'";
                }else {
                    query += "'" + values.get(i) + "', ";
                }
            }
            query += ")";
            stmt.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
