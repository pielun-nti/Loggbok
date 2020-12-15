package models;

import config.Env;
import controllers.LogsController;
import views.LogsView;

import javax.swing.*;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class LoginModel {
    DBManager dbManager;
    User user;
    PassUtil passUtil;
    public LoginModel(){
        dbManager = new DBManager();
        passUtil = new PassUtil();
    }

    public boolean Login(String username, String password) {
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
        val.add(passUtil.toHexString(passUtil.getSHA256(password)));
        try {
            ResultSet rs = dbManager.selectAllWhere("users", col, val);
            if (!rs.next()){
                JOptionPane.showMessageDialog(null, "Invalid username or password. Access denied.", Env.LoginMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                col.remove(col.size() - 1);
                val.remove(val.size() - 1);
                col.add("success");
                col.add("date_time");
                col.add("admin");
                val.add("FALSE");
                val.add(new Date().toString());
                val.add(Boolean.toString(false));
                dbManager.insert("logins", col, val);
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
            col.remove(col.size() - 1);
            val.remove(val.size() - 1);
            col.add("success");
            col.add("date_time");
            col.add("admin");
            val.add("TRUE");
            val.add(new Date().toString());
            val.add(Boolean.toString(user.isAdmin()));
            dbManager.insert("logins", col, val);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        col.remove(col.size() - 1);
        val.remove(val.size() - 1);
        col.add("success");
        col.add("date_time");
        col.add("admin");
        val.add("FALSE");
        val.add(new Date().toString());
        val.add(Boolean.toString(user.isAdmin()));
        dbManager.insert("logins", col, val);
        return false;
    }

}
