package controllers;

import config.Env;
import models.LoginModel;
import models.LogsModel;
import models.RegisterModel;
import models.User;
import views.LoginView;
import views.LogsView;
import views.RegisterView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginController {
    LoginView view;
    LoginModel model;
    User user;

    public LoginController(LoginView view, LoginModel model){
        this.view = view;
        this.model = model;
        this.view.addListeners(new LoginListener());
        this.view.addFrameWindowListener(new FrameWindowListener());
        this.view.addKeyListeners(new UsernameKeyListener(), new PasswordKeyListener());
    }

    private class UsernameKeyListener implements java.awt.event.KeyListener {

        @Override
        public void keyTyped(KeyEvent keyEvent) {

        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {
                    if (keyEvent.getKeyChar() == KeyEvent.VK_ENTER){
                        view.getTxtPassword().requestFocus();
                    }
        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {

        }
    }

    private class PasswordKeyListener implements java.awt.event.KeyListener {

        @Override
        public void keyTyped(KeyEvent keyEvent) {

        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            if (keyEvent.getKeyChar() == KeyEvent.VK_ENTER){
                view.getBtnLogin().doClick();
            }
        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {

        }
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

    private class LoginListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String command = actionEvent.getActionCommand();
            System.out.println("Executed command: " + command);
            if (command != null){
                if (command.equalsIgnoreCase("Login")){
                    Login();
                }
                if (command.equalsIgnoreCase("Register")){
                    Register();
                }
                if (command.equalsIgnoreCase("Login as Anonymous")){
                    loginAsAnonymous();
                }
            }
        }
    }

    public void Register(){
        RegisterView registerView = new RegisterView();
        RegisterModel registerModel = new RegisterModel();
        RegisterController registerController = new RegisterController(registerView, registerModel);
        registerView.setVisible(true);
        registerView.getTxtUsername().requestFocus();
        view.dispose();
    }

    public void loginAsAnonymous(){
        user = new User("Unknown", false);
        LogsView logsView = new LogsView(user);
        LogsModel logsModel = new LogsModel(user);
        LogsController logsController = new LogsController(logsView, logsModel, user);
        logsView.setVisible(true);
        view.dispose();
    }
    
    public void Login(){
        String username = view.getTxtUsername().getText().trim();
        String password = view.getTxtPassword().getText().trim();
        if (!model.Login(username, password)){
                view.getTxtUsername().setText("");
                view.getTxtPassword().setText("");
                view.getTxtUsername().requestFocus();
                return;
            }else{
                view.dispose();
            }
    }
}
