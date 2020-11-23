package models;

import config.Env;
import controllers.LogsController;
import views.LogsView;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class LoginModel {
    DBManager dbManager;
    User user;
    public LoginModel(){
        dbManager = new DBManager();
    }

    public boolean Login(String username, String password){
        if (username == null || password == null){
            JOptionPane.showMessageDialog(null, "Username or password cannot be null.", Env.LoginMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (username.trim().equals("") || password.trim().equals("")){
            JOptionPane.showMessageDialog(null, "Username and password must have an value.", Env.LoginMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        ArrayList<String> col = new ArrayList<>();
        ArrayList<String> val = new ArrayList<>();
        col.add("username");
        col.add("password");
        val.add(username);
        val.add(password);
        try {
            ResultSet rs = dbManager.selectAllWhere("users", col, val);
            if (!rs.next()){
                JOptionPane.showMessageDialog(null, "Invalid username or password. Access denied.", Env.LoginMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                return false;
            }
            String adminstr = rs.getString(4);
            boolean admin = false;
            if (adminstr != null){
                if (adminstr.equalsIgnoreCase("true")){
                    admin = true;
                }
            }
            user = new User(username, admin);
            LogsView logsView = new LogsView(user);
            LogsModel logsModel = new LogsModel(user);
            LogsController logsController = new LogsController(logsView, logsModel, user);
            logsView.setVisible(true);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
