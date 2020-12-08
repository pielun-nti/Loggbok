package models;

import config.Env;

import javax.swing.*;
import java.sql.ResultSet;

public class SecurityModel {
    DBManager dbManager;
    User user;
    public SecurityModel(){
        dbManager = new DBManager();
    }

    public ResultSet getLoginLogs(){
        ResultSet rs = dbManager.selectAll("logins");
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
