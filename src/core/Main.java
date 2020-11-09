package core;

public class Main {
    public static void main(String[] args) {
        //Login login = new Login();
        User user = new User("pierre", true);
        LogsView logsView = new LogsView(user);
        LogsModel logsModel = new LogsModel(user);
        LogsController logsController = new LogsController(logsView, logsModel, user);
        logsView.getFrame().setVisible(true);
    }
}