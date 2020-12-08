package models;

import config.Env;

import javax.swing.*;
import java.sql.ResultSet;
import java.util.ArrayList;

public class SecurityModel {
    DBManager dbManager;
    User user;
    public SecurityModel(User user){
        this.user = user;
        dbManager = new DBManager();
    }

    public ResultSet getLoginLogs(){
        ArrayList<String> co = new ArrayList<>();
        ArrayList<String> va = new ArrayList<>();
        co.add("username");
        va.add(user.getUsername());
        ResultSet rs = dbManager.selectAllWhere("logins", co, va);
        if (rs != null) {
            return rs;
        }
        return null;
    }
    public void clearLoginLogs(){
        int deletedRows = dbManager.deleteAll("logins");
        if(deletedRows>0){
            JOptionPane.showMessageDialog(null, "Successfully deleted all login logs", Env.SecurityMessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(null, "There are no login logs to delete", Env.SecurityMessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
