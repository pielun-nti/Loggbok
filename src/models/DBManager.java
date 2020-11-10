package models;

import config.Env;

import javax.swing.*;
import java.sql.ResultSet;
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

    public boolean editRowInTable(String table, ArrayList<String> filtercolumns, ArrayList<String> filtervalues, ArrayList<String> setcolumns, ArrayList<String> setvalues){
        Statement stmt = null;
        try {
            stmt = db.getConnection().createStatement();
            //String queryold = "update logs set editors='" + editors + "' " + "where id in(" + logid + ")";
            String query = "update " + table + " set ";

            for (int i = 0; i < setcolumns.size(); i++){
                if (setcolumns.get(i) != null){
                    if (i == 0){
                        if ((setcolumns.size() - 1) == i) {
                            query += setcolumns.get(i) + " = '" + setvalues.get(i) + "'";
                        }else{
                            query += setcolumns.get(i) + " = '" + setvalues.get(i);
                        }
                    }else if (i < (setcolumns.size() - 1)){
                        query += "' and " + setcolumns.get(i) + " = '" + setvalues.get(i);
                    }else if (i == (setcolumns.size() - 1)){
                        query += "' and " + setcolumns.get(i) + " = '" + setvalues.get(i) + "'";
                    }
                }
            }
            query += " where ";
            for (int i = 0; i < filtercolumns.size(); i++){
                if (filtercolumns.get(i) != null){
                    if (i == 0){
                        if ((filtercolumns.size() - 1) == i) {
                            query += filtercolumns.get(i) + " = '" + filtervalues.get(i) + "'";
                        }else{
                            query += filtercolumns.get(i) + " = '" + filtervalues.get(i);
                        }
                    }else if (i < (filtercolumns.size() - 1)){
                        query += "' and " + filtercolumns.get(i) + " = '" + filtervalues.get(i);
                    }else if (i == (filtercolumns.size() - 1)){
                        query += "' and " + filtercolumns.get(i) + " = '" + filtervalues.get(i) + "'";
                    }
                }
            }
            System.out.println("Executing query: " + query);
            stmt.executeUpdate(query);
            System.out.println("Successfully Executed Query: " + query);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkDuplicate(DB db, String table, ArrayList<String> columns, ArrayList<String> values){
        Statement stmt = null;
        try {
            stmt = db.getConnection().createStatement();
            String query = "SELECT * from " + table + " where ";

            for (int i = 0; i < columns.size(); i++){
                if (columns.get(i) != null){
                    if (i == 0){
                        query += columns.get(i) + " = '" + values.get(i);
                    }else if (i < (columns.size() - 1)){
                        query += "' and " + columns.get(i) + " = '" + values.get(i);
                    }else if (i == (columns.size() - 1)){
                        query += "' and " + columns.get(i) + " = '" + values.get(i) + "'";
                    }
                }
            }
            System.out.println("Executing query: " + query);
            ResultSet rs = stmt.executeQuery(query);
            System.out.println("Successfully Executed Query: " + query);
            if (rs.next()){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean checkDuplicate(String table, ArrayList<String> columns, ArrayList<String> values){
        Statement stmt = null;
        try {
            stmt = db.getConnection().createStatement();
            String query = "SELECT * from " + table + " where ";

            for (int i = 0; i < columns.size(); i++){
                if (columns.get(i) != null){
                    if (i == 0){
                        query += columns.get(i) + " = '" + values.get(i);
                    }else if (i < (columns.size() - 1)){
                        query += "' and " + columns.get(i) + " = '" + values.get(i);
                    }else if (i == (columns.size() - 1)){
                        query += "' and " + columns.get(i) + " = '" + values.get(i) + "'";
                    }
                }
            }
            System.out.println("Executing query: " + query);
            ResultSet rs = stmt.executeQuery(query);
            System.out.println("Successfully Executed Query: " + query);
            if (rs.next()){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean insert(DB db, String table, ArrayList<String> columns, ArrayList<String> values){
        Statement stmt = null;
        try {
            stmt = db.getConnection().createStatement();
            String query = "INSERT INTO " + table + " (";
            for (int i = 0; i < columns.size(); i++) {
                if (columns.get(i) != null) {
                    if (i == (columns.size() - 1)){
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
            System.out.println("Executing query: " + query);
            stmt.executeUpdate(query);
            System.out.println("Successfully Executed Query: " + query);
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
                    if (i == (columns.size() - 1)){
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
            System.out.println("Executing query: " + query);
            stmt.executeUpdate(query);
            System.out.println("Successfully Executed Query: " + query);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
