package core;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.*;

/**
 * Skapad av Pierre 2020-10-29
 */
public class LogsManager extends javax.swing.JFrame {
    static Connection connection;
    static JFrame frame;
    static JMenuBar menuBar;
    static JMenu fileMenu;
    static JMenu editMenu;
    static JMenu settingsMenu;
    static JMenu aboutMenu;
    static JMenuItem menuitemNewLog;
    static JMenuItem menuItemGetLogs;
    static JMenuItem menuItemEditLog;
    static JMenuItem menuItemExit;
    static JTextArea txtLogs;
    private JPanel mainPanel;
    private static String MessageBoxTitle = "LogsManager GUI";
    private static String openedFile = null;
    private static Font mainFont;
    static int fontSize = 18;

    public LogsManager(){

    }
    private static void confirmExit() {
        //int dialogResult = JOptionPane.showConfirmDialog (null, "ARE YOU SURE YOU WANT TO EXIT THIS APPLICATION? ", MessageBoxTitle, JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
        //if(dialogResult == JOptionPane.YES_OPTION){
            frame.dispose();
        //}
    }
    public static void main(String[]args){
        if (!initDB()){
            JOptionPane.showMessageDialog(null, "Init DB Error!", MessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        frame = new JFrame("LogsManager");
        BufferedImage image = null;
        /*try {
            image = ImageIO.read(
                    LogsManager.class.getResource("../img/logsicon.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        frame.setIconImage(image);*/
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });
        Dimension res = new Dimension(1200, 800);
        frame.setPreferredSize(res);
        frame.setSize(res);
        txtLogs = new JTextArea();
        txtLogs.setEditable(true);
        mainFont = new Font("Verdana", Font.BOLD, fontSize);
        txtLogs.setFont(mainFont);
        frame.setFont(mainFont);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.pack();
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        fileMenu.setFont(mainFont);
        editMenu = new JMenu("Edit");
        settingsMenu = new JMenu("Settings");
        aboutMenu = new JMenu("About");
        editMenu.setFont(mainFont);
        settingsMenu.setFont(mainFont);
        aboutMenu.setFont(mainFont);
        menuItemEditLog = new JMenuItem("Edit Log");
        menuItemEditLog.setFont(mainFont);
        menuItemEditLog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String logid = JOptionPane.showInputDialog(null, "Enter ID of the log you want to edit", MessageBoxTitle, JOptionPane.QUESTION_MESSAGE);
                //get info from the log, show it then allow editing
            }
        });
        menuItemGetLogs = new JMenuItem("Get All Logs");
        menuItemGetLogs.setFont(mainFont);
        menuItemGetLogs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //String author = JOptionPane.showInputDialog(null, "Enter log author", MessageBoxTitle, JOptionPane.QUESTION_MESSAGE);
                Statement stmt= null;
                try {
                    stmt = connection.createStatement();

                ResultSet rs=stmt.executeQuery("select * from logs");
                while(rs.next())
                    if (txtLogs.getText().trim().equals("")) {
                        txtLogs.setText(rs.getString(1) + "\r\n" + rs.getString(2) + "\r\n" + rs.getString(3));
                    }else{
                        txtLogs.append("\r\n--------------\r\n" + rs.getString(1) + "\r\n" + rs.getString(2) + "\r\n" + rs.getString(3));
                    }
                    //JOptionPane.showMessageDialog(null, rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
                //con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        menuitemNewLog = new JMenuItem("Create New Log");
        menuitemNewLog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String author = (JOptionPane.showInputDialog(null, "Enter log author", MessageBoxTitle, JOptionPane.QUESTION_MESSAGE));
                String body = (JOptionPane.showInputDialog(null, "Enter log body", MessageBoxTitle, JOptionPane.QUESTION_MESSAGE));
                try {
                    String query1 = "INSERT INTO logs (author, body)" + "VALUES ('" + author + "', '" + body + "')";
                    Statement stmt = (Statement) connection.createStatement();
                    stmt.executeUpdate(query1);
                    JOptionPane.showMessageDialog(null, "Inserted New Log Into Database Successfully!");
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Insertion to Mysql Database Failure: " + e.toString());
                }

            }
        });
        menuItemExit = new JMenuItem("Exit application");
        menuItemExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                confirmExit();
            }
        });
        menuitemNewLog.setFont(mainFont);
        menuItemExit.setFont(mainFont);
        fileMenu.add(menuitemNewLog);
        fileMenu.add(menuItemEditLog);
        fileMenu.add(menuItemGetLogs);
        fileMenu.add(menuItemExit);
        menuBar.add(fileMenu);
        //menuBar.add(editMenu);
        //menuBar.add(settingsMenu);
        menuBar.add(aboutMenu);
        frame.setJMenuBar(menuBar);
        frame.add(txtLogs);
        frame.setVisible(true);
    }

    public static boolean initDB(){
        boolean success = true;

        String driverName = Env.driverName;
        String conURL = Env.conURL;
        String user = Env.user;
        String pass = Env.pass;

        try {
            Class.forName(driverName).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "InitDB SQL Error: " + e.toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "InitDB SQL Error: " + e.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            success = false;
            JOptionPane.showMessageDialog(null, "InitDB SQL Error: " + e.toString());
        }

        try {
            connection = DriverManager.getConnection(conURL, user, pass);
        } catch (SQLException e) {
            e.printStackTrace();
            success = false;
            JOptionPane.showMessageDialog(null, "InitDB SQL Error: " + e.toString());
        }
        return success;
    }

}
