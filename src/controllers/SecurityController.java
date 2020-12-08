package controllers;

import config.Env;
import models.SecurityModel;
import models.LogsModel;
import models.RegisterModel;
import models.User;
import views.SecurityView;
import views.LogsView;
import views.RegisterView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SecurityController {
    SecurityView view;
    SecurityModel model;
    User user;

    public SecurityController(SecurityView view, SecurityModel model){
        this.view = view;
        this.model = model;
        this.view.addListeners(new SecurityListener());
        this.view.addFrameWindowListener(new FrameWindowListener());
    }

    private class FrameWindowListener implements WindowListener {

        @Override
        public void windowOpened(WindowEvent windowEvent) {

        }

        @Override
        public void windowClosing(WindowEvent windowEvent) {
            view.dispose();
        }

        @Override
        public void windowClosed(WindowEvent windowEvent) {

        }

        @Override
        public void windowIconified(WindowEvent windowEvent) {

        }

        @Override
        public void windowDeiconified(WindowEvent windowEvent) {

        }

        @Override
        public void windowActivated(WindowEvent windowEvent) {

        }

        @Override
        public void windowDeactivated(WindowEvent windowEvent) {


        }
    }

    private class SecurityListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String command = actionEvent.getActionCommand();
            System.out.println("Executed command: " + command);
            if (command != null){
                if (command.equalsIgnoreCase("Get Login Logs")){
                    getLoginLogs();
                }
                if (command.equalsIgnoreCase("Clear Login Logs")){
                    clearLoginLogs();
                }
            }
        }
    }

    public void getLoginLogs(){
        model.getLoginLogs();
    }
    public void clearLoginLogs(){
        model.clearLoginLogs();
    }
}
