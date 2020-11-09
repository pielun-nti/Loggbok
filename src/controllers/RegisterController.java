package controllers;

import models.LoginModel;
import models.LogsModel;
import models.RegisterModel;
import models.User;
import views.LoginView;
import views.LogsView;
import views.RegisterView;

import java.awt.event.*;

public class RegisterController {
    RegisterView view;
    RegisterModel model;
    User user;

    public RegisterController(RegisterView view, RegisterModel model){
        this.view = view;
        this.model = model;
        this.view.addListeners(new RegisterListener());
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
                view.getBtnRegister().doClick();
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
            view.getFrame().dispose();
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

    private class RegisterListener implements ActionListener {

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
        String username = view.getTxtUsername().getText().trim();
        String password = view.getTxtPassword().getText().trim();
        if (!model.Register(username, password)){
            view.getTxtUsername().setText("");
            view.getTxtPassword().setText("");
            view.getTxtUsername().requestFocus();
            return;
        }else{
            view.getFrame().dispose();
        }
    }

    public void Login(){
        LoginView loginView = new LoginView();
        LoginModel loginModel = new LoginModel();
        LoginController loginController = new LoginController(loginView, loginModel);
        loginView.getFrame().setVisible(true);
        loginView.getTxtUsername().requestFocus();
        view.getFrame().dispose();
    }

    public void loginAsAnonymous(){
        user = new User("Unknown", false);
        LogsView logsView = new LogsView(user);
        LogsModel logsModel = new LogsModel(user);
        LogsController logsController = new LogsController(logsView, logsModel, user);
        logsView.getFrame().setVisible(true);
        view.getFrame().dispose();
    }
}
