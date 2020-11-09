package core;

public class Main {

    public static void main(String[] args) {
        //Login login = new Login();
        LogsView logsView = new LogsView("pierre");
        LogsModel logsModel = new LogsModel();
        LogsController logsController = new LogsController(logsView, logsModel);
        logsView.getFrame().setVisible(true);
    }
}