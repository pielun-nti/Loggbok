package models;

import javax.swing.*;
import java.util.ArrayList;

public class DBManagerTest {
    public static void main(String[] args){
        DBManager dbManager = new DBManager();
        String table = "logs";
        ArrayList<String> columns = new ArrayList<String>();
        columns.add("author");
        columns.add("body");
        columns.add("editors");
        ArrayList<String> values = new ArrayList<>();
        values.add("testinsertauthor");
        values.add("testinsertbody");
        values.add("testinserteditors");
        if (dbManager.insert(table, columns, values)){
            JOptionPane.showMessageDialog(null, "Successfully inserted using db manager", "DBManagerTest", JOptionPane.INFORMATION_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(null, "Failed to insert using db manager", "DBManagerTest", JOptionPane.ERROR_MESSAGE);
        }
    }
}
