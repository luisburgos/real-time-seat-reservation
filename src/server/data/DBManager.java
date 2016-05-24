package server.data;

import java.sql.*;

public final class DBManager {

    private static DBManager _instance = null;
    private Connection _con = null;

    public DBManager() {
        //Connect to MS Access
        //_con = getMsAccessConnection();
        _con = getMySQLConnection();
    }

    //Thread safe instantiate method
    public static synchronized DBManager getInstance(){
        if(_instance == null){
            _instance = new DBManager();
        }
        return _instance;
    }

    public Connection getConnection(){
        return _con;
    }

    /**
    *  Connection to MySQL Database
    */
    private static Connection getMySQLConnection(){
        Connection con = null;

        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            String strCon = "jdbc:mysql://127.0.0.1/remoteseat";
            con = DriverManager.getConnection(strCon, "root", "");
        } catch (SQLException se){
            System.out.println(se);
        }
        return con;
    }

}
