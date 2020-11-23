package models;

import config.Env;
import controllers.LogsController;
import views.LogsView;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class RegisterModel {

    DBManager dbManager;
    User user;
    public RegisterModel(){
        dbManager = new DBManager();
    }

    public boolean Register(String username, String password){
        if (username == null || password == null){
            JOptionPane.showMessageDialog(null, "Username or password cannot be null.", Env.RegisterMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (username.trim().equals("") || password.trim().equals("")){
            JOptionPane.showMessageDialog(null, "Username and password must have an value.", Env.RegisterMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            ArrayList<String> col = new ArrayList<>();
            ArrayList<String> val = new ArrayList<>();
            col.add("username");
            val.add(username);
            ResultSet rs2 = dbManager.selectAllWhere("users", col, val);
            if (rs2.next()){
                JOptionPane.showMessageDialog(null, "That username is already taken. Try another one..", Env.RegisterMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ArrayList<String> col = new ArrayList<>();
        ArrayList<String> val = new ArrayList<>();
        col.add("username");
        col.add("password");
        val.add(username);
        val.add(password);
            dbManager.insert("users", col, val);
            user = new User(username, false);
            LogsView logsView = new LogsView(user);
            LogsModel logsModel = new LogsModel(user);
            LogsController logsController = new LogsController(logsView, logsModel, user);
            logsView.setVisible(true);
            return true;
    }
}
