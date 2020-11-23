package core;

import controllers.LoginController;
import controllers.LogsController;
import models.LoginModel;
import models.LogsModel;
import models.User;
import views.LoginView;
import views.LogsView;

import javax.swing.*;
import java.awt.*;

/**
 * Loggbok gjord av Pierre 2020-11-09
 * Innan du kör programmet, fyll i dina databasvariabler i src/config/Env.java filen
 */
public class Main {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }
                LoginView loginView = new LoginView();
                LoginModel loginModel = new LoginModel();
                LoginController loginController = new LoginController(loginView, loginModel);
                loginView.setVisible(true);
                loginView.getTxtUsername().requestFocus();
            }
        });
    }
}