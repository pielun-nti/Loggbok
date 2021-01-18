package controllers;

import config.Env;
import core.*;
import models.LoginModel;
import models.LogsModel;
import models.SecurityModel;
import models.User;
import views.LoginView;
import views.LogsView;
import views.SecurityView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.Security;
import java.sql.*;

public class LogsController{
    LogsView view;
    LogsModel model;
    User user;

    /**
     * Controls both the logs view and logs model. LogsController adds ActionListener and FrameWindowListener.
     * @param view
     * @param model
     * @param user
     */
    public LogsController(LogsView view, LogsModel model, User user){
        this.view = view;
        this.model = model;
        this.user = user;
        this.view.addListeners(new LogsListener());
        this.view.addFrameWindowListener(new FrameWindowListener());
    }

    private class FrameWindowListener implements WindowListener {

        @Override
        public void windowOpened(WindowEvent windowEvent) {

        }

        /**
         * Closes window without terminating process. Hides window without terminating process on windowclosing.
         * @param windowEvent
         */
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

    private class LogsListener implements ActionListener {
        /**
         * Listens for JMenuItem and JCheckBoxMenuItem clicks and then executes methods. Onactionperformed gets called
         * when a JMenuItem or JCheckBoxMenuItem gets clicked by user and then it executes methods.
         * @param actionEvent
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String command = actionEvent.getActionCommand();
            System.out.println("Executed command: " + command);
            if (command != null){
                if (command.equalsIgnoreCase("Create new log")){
                    createNewLog();
                }
                if (command.equalsIgnoreCase("Edit log")){
                    editLog();
                }
                if (command.equalsIgnoreCase("Delete Log")){
                    deleteLog();
                }
                if (command.equalsIgnoreCase("Get all logs")){
                    getAllLogs();
                }
                if (command.equalsIgnoreCase("Get Logs Changes History")){
                    getLogHistory();
                }
                if (command.equalsIgnoreCase("Delete all logs")){
                    deleteAllLogs();
                }
                if (command.equalsIgnoreCase("Delete all log changes history")){
                    deleteAllLogHistory();
                }
                if (command.equalsIgnoreCase("Filter logs")){
                    filterLogs();
                }
                if (command.equalsIgnoreCase("Save logs as")){
                    saveFileDialog();
                }
                if (command.equalsIgnoreCase("Open logs")){
                    openFileDialog();
                }
                if (command.equalsIgnoreCase("Logout")){
                    Logout();
                }
                if (command.equalsIgnoreCase("Open Security")){
                    openSecurity();
                }
                if (command.equalsIgnoreCase("Exit application")){
                    view.dispose();
                }
                if (command.equalsIgnoreCase("Change font size")){
                    int fontSize = view.getFontSize();
                    try {
                        fontSize = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter new font size ( current = " + view.getFontSize() + ")", Env.LogsMessageBoxTitle, JOptionPane.INFORMATION_MESSAGE));
                    } catch (NumberFormatException ex){
                        ex.printStackTrace();
                        view.displayErrorMsg(ex.toString());
                    }
                    Font mainFont = new Font("Verdana", Font.BOLD, fontSize);
                    view.setMainFont(mainFont);
                    view.getTxtLogs().setFont(mainFont);
                    view.setFont(mainFont);
                }
                if (command.equalsIgnoreCase("About")){
                    JOptionPane.showMessageDialog(null, "Made by Pierre Lundström", Env.LogsMessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
        }

    /**
     * Logout out the user. Shows login gui and disposes current logs view.
     */
        void Logout(){
            LoginView loginView = new LoginView();
            LoginModel loginModel = new LoginModel();
            LoginController loginController = new LoginController(loginView, loginModel);
            loginView.setVisible(true);
            loginView.getTxtUsername().requestFocus();
            view.dispose();
        }

    /**
     * Opens security gui. Disposes current logs gui.
     */
    void openSecurity(){
        SecurityView securityView = new SecurityView(user);
        SecurityModel securityModel = new SecurityModel(user);
        SecurityController securityController = new SecurityController(securityView, securityModel, user);
        securityView.setVisible(true);
        view.dispose();
    }

    /**
     * Get all logs. Tells the model to get all logs from database into a resultset and then loop through all log rows and prints them out in the logsview textarea.
     */
    void getAllLogs(){
        try {
            ResultSet rs = model.getAllLogs();
            view.setLogsTXT("");
            while(rs.next())
                if (view.getLogsTXT().trim().equals("")) {
                    view.setLogsTXT("ID: " + rs.getString(1) + "\r\nAuthor: " + rs.getString(2) + "\r\nEditors: " + rs.getString(4) + "\r\nBody: " + rs.getString(3));
                }else{
                    view.appendLogsTXT("\r\n--------------\r\nID: " + rs.getString(1) + "\r\nAuthor: " + rs.getString(2) + "\r\nEditors: " + rs.getString(4) + "\r\nBody: " + rs.getString(3));
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tells the model to open a file dialog. Also waits for user to select file.
     * Then the model returns the data found in the selected file and then shows it in the logsview textarea.
     */
    void openFileDialog(){
        String data = model.openFileDialog();
        if (data != null) {
            view.setLogsTXT(data);
        }
    }

    /**
     * Tells logsmodel to delete all logs.
     */
    void deleteAllLogs(){
        model.deleteAllLogs();
    }

    /**
     * Tells logsmodel to delete all log history.
     */
    void deleteAllLogHistory(){
        model.deleteAllLogHistory();
    }

    /**
     * Gets and print log history. Tells logsmodel to get all log history from database and then logsmodel turns it into a resultset and
     * returns it here, then this method loops through all log history rows and print them out in the logsview textarea.
     */
    void getLogHistory(){
        try {

            ResultSet rs = model.getLogHistory();
            view.setLogsTXT("");
            while(rs.next())
                if (view.getLogsTXT().trim().equals("")) {
                    view.setLogsTXT("ID: " + rs.getString(1) + "\r\nLog ID: " + rs.getString(2) + "\r\nAuthor: " + rs.getString(3)
                            + "\r\nEditor: " + rs.getString(8)
                            + "\r\nBody: " + rs.getString(4) + "\r\nCreated At: " + rs.getString(5) + "\r\nLast Edited: " + rs.getString(6)
                            + "\r\nType Of Change: " + rs.getString(7
                    ));
                }else{
                    view.appendLogsTXT("\r\n--------------\r\nID: " + rs.getString(1) + "\r\nLog ID:" + rs.getString(2)
                            + "\r\nAuthor: "
                            + rs.getString(3)
                            + "\r\nEditor: " + rs.getString(8) + "\r\nBody: " + rs.getString(4) + "\r\nCreated At: " + rs.getString(5)
                            + "\r\nLast Edited: " + rs.getString(6) + "\r\nType Of Change: " + rs.getString(7));
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tells logsmodel to edit log. Tells logmodel to ask user for edit input.
     */
    void editLog(){
        model.editLog();
    }

    /**
     * Tells logsmodel to delete a certain log.
     */
    void deleteLog(){
        model.deleteLog();
    }

    /**
     * Tells logmodel to create a new log.
     */
    void createNewLog(){
        model.createNewLog();
    }

    /**
     * Tells logmodel to open a JFileChooser dialog. With intent to save current logsview textarea data,
     * it tells logmodel to open a JFileChooser dialog.
     */
    private void saveFileDialog() {
       model.saveFileDialog(view.getLogsTXT());
    }

    /**
     * Tells logsmodel to filter logs from database. When logsmodel returns a resultset of the filtered logs by certain keywords given from the user input,
     * this method will loop through the resultset and print out the filtered logs.
     */
    void filterLogs(){
        try {
            ResultSet rs = model.filterLogs();
        view.setLogsTXT("");
        while(rs.next())
            if (view.getLogsTXT().trim().equals("")) {
                view.setLogsTXT("ID: " + rs.getString(1) + "\r\nAuthor: " + rs.getString(2) + "\r\nEditors: " + rs.getString(4) + "\r\nBody: " + rs.getString(3));
            }else{
                view.appendLogsTXT("\r\n--------------\r\nID: " + rs.getString(1) + "\r\nAuthor: " + rs.getString(2) + "\r\nEditors: " + rs.getString(4) + "\r\nBody: " + rs.getString(3));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
