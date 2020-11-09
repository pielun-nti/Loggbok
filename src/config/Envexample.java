package config;
/**
 * Byt namnet på denna klass till Env och byt ut dessa variabler till de som passar din databas
 * Mvh Pierre
 */
public class Envexample {
    public static String dbName = "logs";
    public static String driverName ="com.mysql.jdbc.Driver";
    public static String conURL = "jdbc:mysql://localhost:3306/" + dbName + "?characterEncoding=latin1";
    public static String user = "";
    public static String pass = "";

}
