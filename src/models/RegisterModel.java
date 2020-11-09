package models;

import config.Env;
import controllers.LogsController;
import old.LogsManager;
import views.LogsView;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RegisterModel {

    DB db;
    User user;
    public RegisterModel(){
        db = new DB();
        if (!db.initDB()){
            JOptionPane.showMessageDialog(null, "Init DB Error!", Env.RegisterMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
            return;
        }
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
            String query2 = "SELECT * from users where username = '" + username + "'";
            Statement stmt2 = (Statement) db.getConnection().createStatement();
            ResultSet rs2 = stmt2.executeQuery(query2);
            if (rs2.next()){
                JOptionPane.showMessageDialog(null, "That username is already taken. Try another one..", Env.RegisterMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String query = "INSERT INTO users (username, password) VALUES ('" + username + "', '" + password + "')";
        Statement stmt = null;
        try {
            stmt = (Statement) db.getConnection().createStatement();
            stmt.executeUpdate(query);
            user = new User(username, false);
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
