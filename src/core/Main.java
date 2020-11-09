package core;

import controllers.LoginController;
import controllers.LogsController;
import models.LoginModel;
import models.LogsModel;
import models.User;
import views.LoginView;
import views.LogsView;

public class Main {
    public static void main(String[] args) {
        LoginView loginView = new LoginView();
        LoginModel loginModel = new LoginModel();
        LoginController loginController = new LoginController(loginView, loginModel);
        loginView.getFrame().setVisible(true);
        loginView.getTxtUsername().requestFocus();
    }
}