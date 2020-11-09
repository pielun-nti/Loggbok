package models;

import config.Env;
import controllers.LogsController;
import views.LogsView;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginModel {
    DB db;
    User user;
    public LoginModel(){
        db = new DB();
        if (!db.initDB()){
            JOptionPane.showMessageDialog(null, "Init DB Error!", Env.LoginMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
            return;
        }
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
        String query = "SELECT * from users where username = '" + username + "' and password = '" + password + "'";
        Statement stmt = null;
        try {
            stmt = (Statement) db.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(query);
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
            logsView.getFrame().setVisible(true);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
